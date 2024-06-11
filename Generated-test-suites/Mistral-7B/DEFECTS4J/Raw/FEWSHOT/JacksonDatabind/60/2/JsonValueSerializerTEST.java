package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.Converter.BaseConverter;
import com.fasterxml.jackson.databind.util.Converter.StdConverter;
import com.fasterxml.jackson.databind.util.Serializers;
import com.fasterxml.jackson.databind.util.TypeUtil;

import java.io.IOException;
import java.lang.reflect.AnnotatedMethod;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Test cases for {@link JsonValueSerializer}.
 */
public class JsonValueSerializerTest {

    @Test
    public void testSerialization() throws Exception {
        JsonValueSerializer serializer = new JsonValueSerializer(
                new AnnotatedMethod(MyClassWithJsonValue.class.getMethod("myMethod")), null);

        Object bean = new MyClassWithJsonValue();
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        SerializerProvider provider = new SerializerProvider();
        serializer.serialize(bean, gen, provider);
        JsonNode result = JsonParser.parseMemoized(new StringReader(gen.getOutputStream().toString()));

        assertEquals("expectedValue", result.getTextValue());
    }

    @Test
    public void testSerializationWithContextualSerializer() throws Exception {
        JsonValueSerializer serializer = new JsonValueSerializer(
                new AnnotatedMethod(MyClassWithJsonValueContextual.class.getMethod("myMethod")),
                new JsonSerializer<String>() {}, null, false);

        Object bean = new MyClassWithJsonValueContextual();
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        SerializerProvider provider = new SerializerProvider();
        serializer.serialize(bean, gen, provider);
        JsonNode result = JsonParser.parseMemoized(new StringReader(gen.getOutputStream().toString()));

        assertEquals("expectedValue", result.getTextValue());
    }

    @Test
    public void testSerializationWithTypeSerialization() throws Exception {
        JsonValueSerializer serializer = new JsonValueSerializer(
                new AnnotatedMethod(MyClassWithJsonValueType.class.getMethod("myMethod")), null, null, true);

        Object bean = new MyClassWithJsonValueType();
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        SerializerProvider provider = new SerializerProvider();
        TypeSerializer typeSerializer = new TypeSerializerBuilder().build();

        serializer.serializeWithType(bean, gen, provider, typeSerializer);

        JsonNode result = JsonParser.parseMemoized(new StringReader(gen.getOutputStream().toString()));
        assertEquals("java.lang.Integer", result.path("$type").asText());
        assertEquals(123, result.get(0).asInt());
    }

    @Test
    public void testSerializationWithEnum() throws Exception {
        JsonValueSerializer serializer = new JsonValueSerializer(
                new AnnotatedMethod(MyEnumClass.class.getMethod("getValue")) {}, null, null, false);

        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        SerializerProvider provider = new SerializerProvider();

        for (MyEnum enumValue : MyEnumClass.values()) {
            Object bean = enumValue;
            serializer.serialize(bean, gen, provider);
            JsonNode result = JsonParser.parseMemoized(new StringReader(gen.getOutputStream().toString()));

            assertEquals(enumValue.name(), result.getTextValue());
        }
    }

    @Test
    public void testSerializationWithCustomConverter() throws Exception {
        JsonValueSerializer serializer = new JsonValueSerializer(
                new AnnotatedMethod(MyClassWithCustomConverter.class.getMethod("myMethod")), null, null, false);

        Object bean = new MyClassWithCustomConverter();
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        SerializerProvider provider = new SerializerProvider();

        JsonSerializer<String> stringSerializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider provider)
                    throws IOException {
                StringWriter stringWriter = new StringWriter();
                JsonFactory jsonFactory = new JsonFactory();
                JsonGenerator jsonGenerator = jsonFactory.createGenerator(stringWriter);
                new ObjectMapper().writeValue(jsonGenerator, value);
                gen.writeString(stringWriter.toString());
            }
        };

        JsonSerializer<MyClassWithCustomConverter> customSerializer = new StdSerializer<MyClassWithCustomConverter>(
                MyClassWithCustomConverter.class) {
            @Override
            public void serializeWithType(MyClassWithCustomConverter value, JsonGenerator gen,
                    SerializerProvider provider, TypeSerializer typeSerializer) throws IOException {
                JsonSerializer<Object> valueSerializer = provider.findValueSerializer(
                        value.getClass(), null);
                JsonSerializer<String> stringSerializer = provider.findValueSerializer(String.class, null);

                valueSerializer.serializeWithType(value.getValue(), gen, provider, typeSerializer);
                stringSerializer.serialize(value.getValue().toString(), gen, provider);
            }
        };

        serializer.setValueSerializer(stringSerializer);
        serializer.setContextualSerializer(customSerializer);

        serializer.serialize(bean, gen, provider);
        JsonNode result = JsonParser.parseMemoized(new StringReader(gen.getOutputStream().toString()));

        assertEquals("expectedValue", result.get(0).get("value").getTextValue());
        assertEquals("expectedValueType", result.get(0).get("_class").getTextValue());
    }

    @Test
    public void testSerializationWithNestedClass() throws Exception {
        JsonValueSerializer serializer = new JsonValueSerializer(
                new AnnotatedMethod(MyClassWithNestedJsonValue.class.getMethod("myMethod")), null);

        Object bean = new MyClassWithNestedJsonValue();
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        SerializerProvider provider = new SerializerProvider();

        serializer.serialize(bean, gen, provider);
        JsonNode result = JsonParser.parseMemoized(new StringReader(gen.getOutputStream().toString()));

        assertEquals("expectedNestedValue", result.path("nestedValue").getTextValue());
    }

    @Test
    public void testCreateContextual() throws Exception {
        JsonValueSerializer serializer = new JsonValueSerializer(
                new AnnotatedMethod(MyClassWithJsonValue.class.getMethod("myMethod")), null);

        BeanProperty property = BeanUtil.findProperty("myProperty", MyClassWithJsonValue.class);
        JsonSerializer<String> stringSerializer = new StdSerializer<String>(String.class) {};
        JsonSerializer<MyClassWithJsonValue> serializerWithContext = new JsonValueSerializer(
                serializer, property, stringSerializer, false);

        assertEquals(serializerWithContext, serializer.withResolved(property, stringSerializer, false));

        JsonSerializer<MyClassWithJsonValue> serializerWithDifferentSerializer = new JsonValueSerializer(
                serializer, property, new JsonSerializer<String>() {}, false);

        assertNotSame(serializerWithContext, serializerWithDifferentSerializer);

        JsonSerializer<MyClassWithJsonValue> serializerWithSameSerializer = new JsonValueSerializer(
                serializer, property, stringSerializer, false);

        assertEquals(serializerWithContext, serializerWithSameSerializer);

        JsonSerializer<MyClassWithJsonValue> serializerWithForceTypeInfo = new JsonValueSerializer(
                serializer, property, stringSerializer, true);

        assertNotSame(serializerWithContext, serializerWithForceTypeInfo);
    }

    @Test
    public void testGetSchema() throws Exception {
        JsonValueSerializer serializer = new JsonValueSerializer(
                new AnnotatedMethod(MyClassWithJsonValue.class.getMethod("myMethod")), null);

        SchemaAware schemaAware = new ObjectMapper().findSchema(MyClassWithJsonValue.class);

        JsonNode schema = serializer.getSchema(new SerializerProvider(), null);

        assertEquals(schemaAware.getSchema(new SerializerProvider(), null), schema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        JsonValueSerializer serializer = new JsonValueSerializer(
                new AnnotatedMethod(MyClassWithJsonValue.class.getMethod("myMethod")), null);

        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = mapper.schemaFactory().getJsonSchemaGenerator().acceptJsonFormatVisitor(
                new TypeReference<JsonNode>() {});

        serializer.acceptJsonFormatVisitor(visitor, null);

        JsonNode expectedSchema = JsonNodeFactory.instance.objectNode();
        expectedSchema.set("title", "MyClassWithJsonValue");
        expectedSchema.set("type", "object");
        expectedSchema.set("properties", JsonNodeFactory.instance.objectNode());
        expectedSchema.get("properties").set("myProperty", JsonNodeFactory.instance.objectNode());
        expectedSchema.get("properties").get("myProperty").set("type", "string");

        assertEquals(expectedSchema, visitor.getSchema());
    }

    private static class MyClassWithJsonValue {
        private int myProperty;

        @JsonValue
        public String myMethod() {
            return "expectedValue";
        }
    }

    private static class MyClassWithJsonValueContextual {
        private int myProperty;

        @JsonValue
        @JsonSerialize(using = MyCustomSerializer.class)
        public String myMethod() {
            return "expectedValue";
        }

        static class MyCustomSerializer extends JsonSerializer<String> {
            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider provider)
                    throws IOException {
                // Custom serialization logic here
            }
        }
    }

    private static class MyClassWithJsonValueType {
        private int myProperty;

        @JsonValue
        public Integer myMethod() {
            return 123;
        }
    }

    private static class MyEnumClass {
        private final String value;

        MyEnumClass(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static final MyEnumClass MY_VALUE_1 = new MyEnumClass("value1");
        public static final MyEnumClass MY_VALUE_2 = new MyEnumClass("value2");

        public static enum MyEnum {
            VALUE_1(MyEnumClass.MY_VALUE_1),
            VALUE_2(MyEnumClass.MY_VALUE_2);

            private final MyEnumClass enumValue;

            MyEnum(MyEnumClass enumValue) {
                this.enumValue = enumValue;
            }

            public MyEnumClass getValue() {
                return enumValue;
            }
        }
    }

    private static class MyClassWithCustomConverter {
        private int myProperty;
        private String myValue;

        public String myMethod() {
            return myValue;
        }

        public void setValue(String value) {
            this.myValue = value;
        }
    }

    private static class MyClassWithNestedJsonValue {
        private NestedClass nestedValue;

        @JsonValue
        public NestedClass myMethod() {
            return new NestedClass();
        }

        private static class NestedClass {
            private String nestedValue;

            @JsonValue
            public String getNestedValue() {
                return nestedValue;
            }

            public void setNestedValue(String value) {
                this.nestedValue = value;
            }
        }
    }
}