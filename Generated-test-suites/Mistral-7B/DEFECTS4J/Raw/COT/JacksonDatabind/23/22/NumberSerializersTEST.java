package com.fasterxml.jackson.databind.ser.std; // Import the package of the class under test

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.annotate.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RunWith(MockitoJUnitRunner.class)
public class NumberSerializersTest {

    

    @Test
    public void testIntegerSerializer() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        IntegerSerializer integerSerializer = new IntegerSerializer();
        Integer intValue = 123;

        // Test serialization
        String json = mapper.writerWithView(NumberSerializers.Base.class).writeValueAsString(intValue);
        ObjectNode jsonNode = mapper.readTree(json);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        integerSerializer.serialize(intValue, gen, new SerializerProvider());

        assertEquals(Integer.toString(intValue), jsonNode.get(0).asText());
        assertEquals(Integer.toString(intValue), gen.getOutputTools().writeValueAsString(intValue));

        // Test deserialization
        int deserializedInt = mapper.readerForType(Integer.class).readValue(json);
        assertEquals(intValue, deserializedInt);
    }

    @Test
    public void testIntLikeSerializer() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        Number intValue = 123;

        // Test serialization
        String json = mapper.writerWithView(NumberSerializers.Base.class).writeValueAsString(intValue);
        ObjectNode jsonNode = mapper.readTree(json);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        intLikeSerializer.serialize(intValue, gen, new SerializerProvider());

        assertEquals(Integer.toString(intValue.intValue()), jsonNode.get(0).asText());
        assertEquals(Integer.toString(intValue.intValue()), gen.getOutputTools().writeValueAsString(intValue));

        // Test deserialization
        Number deserializedNumber = mapper.readerForType(Number.class).readValue(json);
        assertEquals(intValue, deserializedNumber);
    }

    @Test
    public void testLongSerializer() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        LongSerializer longSerializer = new LongSerializer();
        Long longValue = 123L;

        // Test serialization
        String json = mapper.writerWithView(NumberSerializers.Base.class).writeValueAsString(longValue);
        ObjectNode jsonNode = mapper.readTree(json);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        longSerializer.serialize(longValue, gen, new SerializerProvider());

        assertEquals(Long.toString(longValue), jsonNode.get(0).asText());
        assertEquals(Long.toString(longValue), gen.getOutputTools().writeValueAsString(longValue));

        // Test deserialization
        Long deserializedLong = mapper.readerForType(Long.class).readValue(json);
        assertEquals(longValue, deserializedLong);
    }

    @Test
    public void testShortSerializer() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        ShortSerializer shortSerializer = new ShortSerializer();
        Short shortValue = 123;

        // Test serialization
        String json = mapper.writerWithView(NumberSerializers.Base.class).writeValueAsString(shortValue);
        ObjectNode jsonNode = mapper.readTree(json);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        shortSerializer.serialize(shortValue, gen, new SerializerProvider());

        assertEquals(Short.toString(shortValue), jsonNode.get(0).asText());
        assertEquals(Short.toString(shortValue), gen.getOutputTools().writeValueAsString(shortValue));

        // Test deserialization
        Short deserializedShort = mapper.readerForType(Short.class).readValue(json);
        assertEquals(shortValue, deserializedShort);
    }

    @Test
    public void testFloatSerializer() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        FloatSerializer floatSerializer = new FloatSerializer();
        Float floatValue = 123.45f;

        // Test serialization
        String json = mapper.writerWithView(NumberSerializers.Base.class).writeValueAsString(floatValue);
        ObjectNode jsonNode = mapper.readTree(json);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        floatSerializer.serialize(floatValue, gen, new SerializerProvider());

        assertEquals(Float.toString(floatValue), jsonNode.get(0).asText());
        assertEquals(Float.toString(floatValue), gen.getOutputTools().writeValueAsString(floatValue));

        // Test deserialization
        Float deserializedFloat = mapper.readerForType(Float.class).readValue(json);
        assertEquals(floatValue, deserializedFloat);
    }

    @Test
    public void testDoubleSerializer() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DoubleSerializer doubleSerializer = new DoubleSerializer();
        Double doubleValue = 123.45;

        // Test serialization
        String json = mapper.writerWithView(NumberSerializers.Base.class).writeValueAsString(doubleValue);
        ObjectNode jsonNode = mapper.readTree(json);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        doubleSerializer.serialize(doubleValue, gen, new SerializerProvider());

        assertEquals(Double.toString(doubleValue), jsonNode.get(0).asText());
        assertEquals(Double.toString(doubleValue), gen.getOutputTools().writeValueAsString(doubleValue));

        // Test deserialization
        Double deserializedDouble = mapper.readerForType(Double.class).readValue(json);
        assertEquals(doubleValue, deserializedDouble);
    }

    @Test
    public void testAddAll() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        // Test serialization
        JsonParser parser = mapper.getFactory().createParser(new StringReader("{}"));
        ObjectNode rootNode = mapper.readTree(parser);
        for (Map.Entry<String, JsonSerializer<?>> entry : allDeserializers.entrySet()) {
            JsonSerializer<?> serializer = entry.getValue();
            Object value = serializer.getClass().newInstance();
            String serializedJson = mapper.writerWithView(NumberSerializers.Base.class)
                    .writeValueAsString(value);
            rootNode.put(entry.getKey(), serializedJson);
        }

        String json = mapper.writerForTree(rootNode).writeValueAsString(rootNode);
        assertEquals("{\"Integer\":\"123\",\"Integer.TYPE\":\"123\",\"Long\":\"123\",\"Long.TYPE\":\"123\",\"Byte\":\"0\",\"Byte.TYPE\":\"0\",\"Short\":\"123\",\"Short.TYPE\":\"123\",\"Float\":\"123.45\",\"Float.TYPE\":\"123.45\",\"Double\":\"123.45\",\"Double.TYPE\":\"123.45\"}", json);

        // Test deserialization
        ObjectMapper deserializerMapper = new ObjectMapper();
        ObjectNode deserializerRootNode = deserializerMapper.readTree(new StringReader(json));
        Map<String, JsonSerializer<?>> deserializedAllDeserializers = new java.util.HashMap<>();
        for (Map.Entry<String, JsonNode> entry : deserializerRootNode.entrySet()) {
            String className = entry.getKey();
            JsonNode jsonNode = entry.getValue();
            JsonDeserializer<? extends Object> deserializer = mapper.readerForValue(JsonNode.class).readValue(jsonNode);
            deserializableAllDeserializers.put(className, deserializer);
        }
        assertEquals(allDeserializers, deserializableAllDeserializers);
    }

    @Test
    public void testIntegerSerializerWithJsonFormatAnnotation() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("number", 123);
        rootNode.put("@JsonFormat(shape=JsonFormat.Shape.STRING)", 456);

        String json = mapper.writerForTree(rootNode).writeValueAsString(rootNode);
        ObjectMapper deserializerMapper = new ObjectMapper();
        ObjectNode deserializerRootNode = deserializerMapper.readTree(new StringReader(json));

        JsonParser parser = mapper.getFactory().createParser(new StringReader(json));
        NumberSerializer numberSerializer = NumberSerializers.ShortSerializer.class; // Replace with the appropriate serializer class
        JsonDeserializer<?> deserializer = mapper.findValueDeserializer(Number.class, JsonParser.NumberType.INT, null);
        Object deserializedObject = deserializer.deserialize(parser, NumberSerializers.ShortSerializer.class, null);
        Object deserializedWithAnnotationObject = deserializer.deserialize(parser, numberSerializer, null);

        assertEquals(123, deserializedObject);
        assertEquals(456, deserializedWithAnnotationObject);
    }

    
}