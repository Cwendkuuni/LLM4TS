package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsonschema.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.BeanProperty;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Test file for {@link JsonValueSerializer} class.
 */
public class JsonValueSerializerTest {

    private ObjectNode testObject;
    private JsonSerializer<Object> serializer;
    private ObjectMapper mapper;

    @Before
    public void setup() throws InstantiationException, IllegalAccessException, NoSuchMethodException {
        TestClass testClass = new TestClass();
        mapper = new ObjectMapper();
        TestClass.InnerClass innerClass = testClass.new InnerClass();
        testObject = mapper.valueToTree(testClass);

        Method getMethod = TestClass.class.getDeclaredMethod("getValue");
        getMethod.setAccessible(true);

        serializer = mapper.findValueSerializer(TestClass.class, null);
    }

    @Test
    public void testSerialize() throws JsonProcessingException, IOException {
        JsonSerializer<Object> valueSerializer = serializer._valueSerializer;

        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
        JsonSerializer<Object> jsonSerializer = mapper.findValueSerializer(Integer.TYPE, null);

        serializer.serialize(testObject, jsonGenerator, mapper.getSerializerProvider());
        jsonGenerator.flush();

        // Test serialization of value using provided valueSerializer
        Object value = getMethod.invoke(testObject);
        valueSerializer.serialize(value, jsonGenerator, mapper.getSerializerProvider());
        jsonGenerator.flush();

        // Test serialization of value using default serializer
        JsonSerializer<Object> defaultSerializer = mapper.getSerializerProvider().findValueSerializer(value.getClass(), null);
        jsonSerializer = mapper.getSerializerProvider().getSerializerProvider().findSerializer(value.getClass(), JsonSerializer.Nulls.NON_NULL);
        if (value.getClass() == Integer.class) {
            jsonSerializer = jsonSerializer != null ? jsonSerializer : jsonSerializer = jsonSerializer != null ? jsonSerializer : jsonSerializer = jsonSerializer != null ? jsonSerializer : jsonSerializer = jsonSerializer != null ? jsonSerializer : jsonSerializer = jsonSerializer != null ? jsonSerializer : jsonSerializer = jsonSerializer != null ? jsonSerializer : jsonSerializer = jsonSerializer != null ? jsonSerializer : mapper.getSerializerProvider().findValueSerializer(value.getClass(), null);
        }
        jsonSerializer.serialize(value, jsonGenerator, mapper.getSerializerProvider());
        jsonGenerator.flush();

        // Test serialization with type information
        JsonSerializer<Object> typeSerializer = mapper.getSerializerProvider().getSerializerProvider().findSerializer(Object.class, JsonSerializer.Nulls.NULL);
        JsonSerializer<Object> serializedObject = mapper.getSerializerProvider().findValueSerializer(Object.class, null);
        JsonSerializer<Object> serializedValue = mapper.getSerializerProvider().findValueSerializer(value.getClass(), null);
        TypeSerializer typeSer = mapper.getSerializerProvider().getTypeSerializer();

        serializer.serializeWithType(testObject, jsonGenerator, mapper.getSerializerProvider(), typeSer);
        jsonGenerator.flush();

        valueSerializer.serializeWithType(value, jsonGenerator, mapper.getSerializerProvider(), typeSer);
        jsonGenerator.flush();

        typeSerializer.writeTypePrefixForScalar(testObject, jsonGenerator);
        serializedObject.serialize(testObject, jsonGenerator, mapper.getSerializerProvider());
        typeSerializer.writeTypeSuffixForScalar(testObject, jsonGenerator);
        jsonGenerator.flush();

        typeSerializer.writeTypePrefixForScalar(value, jsonGenerator);
        serializedValue.serialize(value, jsonGenerator, mapper.getSerializerProvider());
        typeSerializer.writeTypeSuffixForScalar(value, jsonGenerator);
        jsonGenerator.flush();
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), null);
        Assert.assertEquals(com.fasterxml.jackson.databind.jsonschema.JsonSchema.getDefaultSchemaNode(), schema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = new JsonSerializer.StdJsonFormatVisitor();
        JsonSerializer<Object> serializerForInnerClass = mapper.findValueSerializer(TestClass.InnerClass.class, null);

        serializer.acceptJsonFormatVisitor(visitor, null);
        serializerForInnerClass.acceptJsonFormatVisitor(visitor, null);
    }

    @Test
    public void testIsNaturalTypeWithStdHandling() {
        Assert.assertTrue(serializer.isNaturalTypeWithStdHandling(Integer.TYPE, serializer._valueSerializer));
        Assert.assertFalse(serializer.isNaturalTypeWithStdHandling(String.class, serializer._valueSerializer));
    }

}

class TestClass {
    private int intValue;
    private boolean booleanValue;
    private double doubleValue;
    private String stringValue;
    private InnerClass innerClass;

    @JsonValue
    public int getValue() {
        return intValue;
    }

    @JsonValue
    public boolean getBooleanValue() {
        return booleanValue;
    }

    @JsonValue
    public double getDoubleValue() {
        return doubleValue;
    }

    @JsonValue
    public String getStringValue() {
        return stringValue;
    }

    public InnerClass getInnerClass() {
        return innerClass;
    }

    public static class InnerClass {
        private int intValue;

        @JsonValue
        public int getIntValue() {
            return intValue;
        }
    }
}