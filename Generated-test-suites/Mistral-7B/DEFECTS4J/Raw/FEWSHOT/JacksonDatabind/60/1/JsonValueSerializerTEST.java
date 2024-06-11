package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.ClassType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.lang.reflect.Method;

public class JsonValueSerializerTest {

    @Test
    public void testJsonValueSerializer() throws Exception {
        Class<?> jsonValueSerializerClass = JsonValueSerializer.class;
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(null, null, null, false);

        // Test constructor
        assertEquals(Object.class, jsonValueSerializer.getHandlerType());
        assertEquals(jsonValueSerializerClass, jsonValueSerializer.getClass());

        // Test setters
        JsonValueSerializer setPropertySerializer = new JsonValueSerializer(null, null, null, false);
        Method setAccessormethod = JsonValueSerializer.class.getDeclaredMethod("_accessorMethod", Method.class);
        setAccessormethod.setAccessible(true);
        setAccessormethod.set(setPropertySerializer, new Method(JsonValueSerializerTest::getValue, JsonValueSerializerTest.class));
        assertEquals(JsonValueSerializerTest.class.getMethod("getValue"), setPropertySerializer._accessorMethod);

        JsonSerializer<Object> setValueserializer = new StdSerializer<Object>(Object.class) {};
        JsonValueSerializer setValueSerializerWithValueserializer = new JsonValueSerializer(null, null, setValueserializer, false);
        assertEquals(setValueserializer, setValueSerializerWithValueserializer._valueSerializer);

        JsonValueSerializer setForceTypeInformation = new JsonValueSerializer(null, null, null, true);
        assertEquals(true, setForceTypeInformation._forceTypeInformation);

        // Test serialize methods
        Object testObject = new TestObject();
        JsonGenerator jsonGenerator = createMockJsonGenerator();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.findAndRegisterModules();
        mapper.writeValue(jsonGenerator, testObject);

        JsonSerializer<Object> serializedSerializer = mapper.findValueSerializer(JsonValueSerializerTest.class, null);
        assertEquals(TestObject.class, serializedSerializer.getParameterType());

        JsonSerializer<Object> serializedSerializerWithType = mapper.findValueSerializer(JsonValueSerializerTest.class, ClassType.construct(Object.class, JsonValueSerializerTest.class));
        assertEquals(TestObject.class, serializedSerializerWithType.getParameterType());
        assertEquals(TestObject.class, serializedSerializerWithType.handleType());

        JsonSerializer<Object> serializedSerializerWithAccessormethod = mapper.findValueSerializer(JsonValueSerializerTest._accessorMethod, ClassType.construct(Object.class, JsonValueSerializerTest.class));
        assertEquals(TestObject.class, serializedSerializerWithAccessormethod.getParameterType());
        assertEquals(TestObject.class, serializedSerializerWithAccessormethod.handleType());
        assertEquals(JsonValueSerializerTest._accessorMethod, serializedSerializerWithAccessormethod._accessorMethod);

        JsonSerializer<Object> serializedSerializerWithValueserializer = mapper.findValueSerializer(JsonValueSerializerTest.class, null);
        JsonSerializer<Object> expectedValueserializer = mapper.findValueSerializer(Integer.TYPE, null);
        assertEquals(expectedValueserializer, serializedSerializerWithValueserializer._valueSerializer);

        JsonSerializer<Object> serializedSerializerWithForceTypeInformation = mapper.findValueSerializer(JsonValueSerializerTest.class, null);
        TypeSerializer typeSerializer = mapper.getTypeSerializer();
        JsonGenerator jsonGeneratorWithType = createMockJsonGenerator();
        jsonGeneratorWithType.setCodec(mapper);

        jsonGeneratorWithType.writeStartObject();
        jsonGeneratorWithType.writeFieldName("foo");
        serializedSerializerWithForceTypeInformation.serialize(testObject, jsonGeneratorWithType, mapper);
        jsonGeneratorWithType.writeEndObject();

        String expectedJson = "{\"foo\":1}";
        String actualJson = jsonGeneratorWithType.getOutputContent();
        assertEquals(expectedJson, actualJson);

        // Test acceptJsonFormatVisitor
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = createMockJsonFormatVisitorWrapper();
        jsonFormatVisitorWrapper.expectAnyFormat(TypeFactory.defaultInstance().constructType(JsonValueSerializerTest.class));
        serializedSerializerWithForceTypeInformation.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, null);

        jsonFormatVisitorWrapper.expectAnyFormat(TypeFactory.defaultInstance().constructType(JsonValueSerializerTest.class));
        serializedSerializerWithValueserializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, null);

        jsonFormatVisitorWrapper.expectStringFormat(TypeFactory.defaultInstance().constructType(JsonValueSerializerTest.class));
        serializedSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, null);
    }

    static Object getValue(TestObject testObject) {
        return testObject.getValue();
    }

    static class TestObject {
        private int value;

        public int getValue() {
            return value;
        }

        @JsonSerialize(using = JsonValueSerializer.class)
        public int getJsonValue() {
            return value;
        }
    }

    static JsonGenerator createMockJsonGenerator() {
        return createMock(JsonGenerator.class);
    }

    static JsonFormatVisitorWrapper createMockJsonFormatVisitorWrapper() {
        return createMock(JsonFormatVisitorWrapper.class);
    }
}

class TestObjectSerializer extends JsonSerializer<TestObject> implements SchemaAware {
    @Override
    public void serialize(TestObject testObject, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(testObject.getValue());
    }

    @Override
    public JsonNode getSchema(SerializerProvider serializerProvider, Type typeHint) throws JsonMappingException {
        return null;
    }

    @Override
    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
        visitor.writeStringFormat("number", "value");
    }
}

class TestObject {
    private int value;

    public int getValue() {
        return value;
    }

    @JsonSerialize(using = TestObjectSerializer.class)
    public int getJsonValue() {
        return value;
    }
}