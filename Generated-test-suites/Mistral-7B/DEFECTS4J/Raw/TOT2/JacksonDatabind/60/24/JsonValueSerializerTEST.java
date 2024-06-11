import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;

public class JsonValueSerializerTest {

    private JsonValueSerializer jsonValueSerializer;
    private Object testObject;

    @Before
    public void setup() throws InstantiationException, IllegalAccessException, NoSuchMethodException {
        Class<?> clazz = JsonValueSerializer.class;
        AnnotatedMethod annotatedMethod = new AnnotatedMethod(clazz.getDeclaredMethod("testMethod"), clazz);
        Object testValue = new Object() {
            @JsonValue
            public String getValue() {
                return "Test Value";
            }
        };
        this.jsonValueSerializer = new JsonValueSerializer(annotatedMethod, null);
        this.testObject = new TestClass(testValue);
    }

    @Test
    public void testSerializesValueMethod() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        this.jsonValueSerializer.serialize(this.testObject, gen, new ObjectMapper().getSerializerProvider());
        String result = gen.getOutputText();
        Assert.assertEquals("\"Test Value\"", result);
    }

    @Test
    public void testSerializesNullValue() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        this.jsonValueSerializer = new JsonValueSerializer(this.testObject._accessorMethod, null);
        this.jsonValueSerializer.serialize(null, gen, new ObjectMapper().getSerializerProvider());
        String result = gen.getOutputText();
        Assert.assertEquals("null", result);
    }

    @Test(expected = JsonMappingException.class)
    public void testThrowsExceptionWhenAccessingNullMethod() throws IOException {
        AnnotatedMethod annotatedMethod = new AnnotatedMethod(this.testObject._getClass().getDeclaredMethod("nullMethod"), this.testObject._getClass());
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(annotatedMethod, null);
        jsonValueSerializer.serialize(this.testObject, new JsonFactory().createGenerator(new StringWriter()), new ObjectMapper().getSerializerProvider());
    }

    @Test
    public void testSerializesEnumValue() throws IOException {
        Class<?> enumClass = EnumClass.class;
        AnnotatedMethod annotatedMethod = new AnnotatedMethod(enumClass.getDeclaredMethod("value"), enumClass);
        Object testEnumValue = EnumClass.VALUE_ONE;
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(annotatedMethod, null);
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        jsonValueSerializer.serialize(testEnumValue, gen, new ObjectMapper().getSerializerProvider());
        String result = gen.getOutputText();
        Assert.assertEquals("\"VALUE_ONE\"", result);
    }

    @Test
    public void testSerializesPrimitiveTypesWithStdHandling() throws IOException {
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(new AnnotatedMethod(JsonValueSerializer.class.getDeclaredMethod("serializePrimitiveTypes"), JsonValueSerializer.class), null);
        Object intValue = 1;
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        jsonValueSerializer.serialize(intValue, gen, new ObjectMapper().getSerializerProvider());
        String result = gen.getOutputText();
        Assert.assertEquals("1", result);
    }

    @Test(expected = JsonMappingException.class)
    public void testThrowsExceptionWhenAccessingNonExistentMethod() throws IOException {
        AnnotatedMethod annotatedMethod = new AnnotatedMethod(this.testObject._getClass().getDeclaredMethod("nonExistentMethod"), this.testObject._getClass());
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(annotatedMethod, null);
        jsonValueSerializer.serialize(this.testObject, new JsonFactory().createGenerator(new StringWriter()), new ObjectMapper().getSerializerProvider());
    }

    @Test
    public void testSerializesUsingDefaultSerializer() throws IOException {
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(new AnnotatedMethod(JsonValueSerializer.class.getDeclaredMethod("serializeUsingDefaultSerializer"), JsonValueSerializer.class), null);
        Object stringValue = "Test String";
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        jsonValueSerializer.serialize(stringValue, gen, new ObjectMapper().getSerializerProvider());
        String result = gen.getOutputText();
        Assert.assertEquals("\"Test String\"", result);
    }

    @Test
    public void testSerializesWithType() throws IOException {
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(new AnnotatedMethod(JsonValueSerializer.class.getDeclaredMethod("serializeWithType"), JsonValueSerializer.class), null);
        Object testValue = new Object() {
            @JsonValue
            public String getValue() {
                return "Test Value";
            }
            public
            Class<?> getType() {
                return String.class;
            }
        };
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        jsonValueSerializer.serializeWithType(testValue, gen, new ObjectMapper().getSerializerProvider(), new TypeSerializer());
        String result = gen.getOutputText();
        Assert.assertEquals("\"Test Value\"", result);
        Assert.assertEquals("\"String\"", gen.getOutputText(new StringSerializer()));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonSerializer<Object> jsonSerializer = objectMapper.findValueSerializer(String.class, null);
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = objectMapper.getFactory().constructJsonFormatVisitor(SerializerProvider.Std, null);
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(new AnnotatedMethod(JsonValueSerializer.class.getDeclaredMethod("acceptJsonFormatVisitor"), JsonValueSerializer.class), null);
        jsonValueSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, null);
        JsonNode schema = jsonFormatVisitorWrapper.getSchemaProvider().schemaForAnnotation(JsonValue.class);
        Assert.assertEquals("number", schema.getType().getPrimitiveType().getFullName());
    }

    static class TestClass {
        Object _value;

        public TestClass(Object value) {
            this._value = value;
        }

        public Object getValue() {
            return this._value;
        }

        @JsonValue
        public String testMethod() {
            return "Test Method Value";
        }

        @JsonValue
        public int testMethodInt() {
            return 1;
        }

        public void nullMethod() {
        }

        public int nonExistentMethod() {
            return 1;
        }

        public String serializePrimitiveTypes() {
            return "Test Primitive Types";
        }

        public String serializeUsingDefaultSerializer() {
            return "Test Using Default Serializer";
        }

        public String serializeWithType() {
            return "Test With Type";
        }
    }

    static class EnumClass {
        public enum EnumValue {
            VALUE_ONE, VALUE_TWO, VALUE_THREE
        }

        public EnumValue value() {
            return VALUE_ONE;
        }
    }
}