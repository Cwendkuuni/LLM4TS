package com.example.junit; // replace with your package name

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.Map;

public class NumberSerializersTest {

    

    @org.junit.BeforeClass
    public static void setup() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @org.junit.Test
    public void testNumberSerializers_addAll() throws IOException {
        NumberSerializers numberSerializers = new NumberSerializers();
        JsonFactory jsonFactory = new JsonFactory();
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.TreeMap<>();
        numberSerializers.addAll(allDeserializers);

        assertEquals(8, allDeserializers.size());
    }

    @org.junit.Test
    public void testShortSerializer_serialize() throws IOException {
        ShortSerializer shortSerializer = NumberSerializers.ShortSerializer.instance;

        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new SerializerProvider();
        ObjectNode node = JsonFactory.instance.createObjectNode();
        TypeFactory typeFactory = node.getTypeFactory();

        shortSerializer.serialize(Short.MIN_VALUE, gen, provider);
        String shortMinValueJson = gen.getOutputContent();
        gen.close();

        JsonParser parser = jsonFactory.getFactory().getParser(new StringReader(shortMinValueJson));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(-32768, parser.getValueAsInt());

        shortSerializer.serialize(Short.MAX_VALUE, gen, provider);
        String shortMaxValueJson = gen.getOutputContent();
        gen.close();

        parser = jsonFactory.getFactory().getParser(new StringReader(shortMaxValueJson));
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(Short.MAX_VALUE, parser.getValueAsInt());
    }

    @org.junit.Test
    public void testIntegerSerializer_serialize() throws IOException {
        IntegerSerializer integerSerializer = NumberSerializers.IntegerSerializer.instance;

        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new SerializerProvider();
        ObjectNode node = JsonFactory.instance.createObjectNode();
        TypeFactory typeFactory = node.getTypeFactory();

        integerSerializer.serialize(-123, gen, provider);
        String intMinus123Json = gen.getOutputContent();
        gen.close();

        JsonParser parser = jsonFactory.getFactory().getParser(new StringReader(intMinus123Json));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(-123, parser.getValueAsInt());

        integerSerializer.serialize(Integer.MAX_VALUE, gen, provider);
        String intMaxValueJson = gen.getOutputContent();
        gen.close();

        parser = jsonFactory.getFactory().getParser(new StringReader(intMaxValueJson));
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(Integer.MAX_VALUE, parser.getValueAsInt());
    }

    @org.junit.Test
    public void testIntLikeSerializer_serialize() throws IOException {
        IntLikeSerializer intLikeSerializer = NumberSerializers.IntLikeSerializer.instance;

        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new SerializerProvider();
        ObjectNode node = JsonFactory.instance.createObjectNode();
        TypeFactory typeFactory = node.getTypeFactory();

        intLikeSerializer.serialize(-123L, gen, provider);
        String longMinus123Json = gen.getOutputContent();
        gen.close();

        JsonParser parser = jsonFactory.getFactory().getParser(new StringReader(longMinus123Json));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(-123, parser.getValueAsInt());

        intLikeSerializer.serialize(Long.MAX_VALUE, gen, provider);
        String longMaxValueJson = gen.getOutputContent();
        gen.close();

        parser = jsonFactory.getFactory().getParser(new StringReader(longMaxValueJson));
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(Long.MAX_VALUE, parser.getValueAsLong());
    }

    @org.junit.Test
    public void testLongSerializer_serialize() throws IOException {
        LongSerializer longSerializer = NumberSerializers.LongSerializer.instance;

        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new SerializerProvider();
        ObjectNode node = JsonFactory.instance.createObjectNode();
        TypeFactory typeFactory = node.getTypeFactory();

        longSerializer.serialize(-1234567890L, gen, provider);
        String longMinus1234567890Json = gen.getOutputContent();
        gen.close();

        JsonParser parser = jsonFactory.getFactory().getParser(new StringReader(longMinus1234567890Json));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_LONG, token);
        assertEquals(-1234567890L, parser.getValueAsLong());

        longSerializer.serialize(Long.MAX_VALUE, gen, provider);
        String longMaxValueJson = gen.getOutputContent();
        gen.close();

        parser = jsonFactory.getFactory().getParser(new StringReader(longMaxValueJson));
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_LONG, token);
        assertEquals(Long.MAX_VALUE, parser.getValueAsLong());
    }

    @org.junit.Test
    public void testFloatSerializer_serialize() throws IOException {
        FloatSerializer floatSerializer = NumberSerializers.FloatSerializer.instance;

        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new SerializerProvider();
        ObjectNode node = JsonFactory.instance.createObjectNode();
        TypeFactory typeFactory = node.getTypeFactory();

        floatSerializer.serialize(-123.45f, gen, provider);
        String floatMinus123Point45Json = gen.getOutputContent();
        gen.close();

        JsonParser parser = jsonFactory.getFactory().getParser(new StringReader(floatMinus123Point45Json));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, token);
        assertEquals(-123.45f, parser.getValueAsFloat(), 0.001f);

        floatSerializer.serialize(Float.MAX_VALUE, gen, provider);
        String floatMaxValueJson = gen.getOutputContent();
        gen.close();

        parser = jsonFactory.getFactory().getParser(new StringReader(floatMaxValueJson));
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, token);
        assertEquals(Float.MAX_VALUE, parser.getValueAsFloat(), 0.001f);
    }

    @org.junit.Test
    public void testDoubleSerializer_serialize() throws IOException {
        DoubleSerializer doubleSerializer = NumberSerializers.DoubleSerializer.instance;

        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new SerializerProvider();
        ObjectNode node = JsonFactory.instance.createObjectNode();
        TypeFactory typeFactory = node.getTypeFactory();

        doubleSerializer.serialize(-123.456789012345d, gen, provider);
        String doubleMinus123Point456789012345Json = gen.getOutputContent();
        gen.close();

        JsonParser parser = jsonFactory.getFactory().getParser(new StringReader(doubleMinus123Point456789012345Json));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, token);
        assertEquals(-123.456789012345d, parser.getValueAsDouble(), 0.000001);

        doubleSerializer.serialize(Double.MAX_VALUE, gen, provider);
        String doubleMaxValueJson = gen.getOutputContent();
        gen.close();

        parser = jsonFactory.getFactory().getParser(new StringReader(doubleMaxValueJson));
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, token);
        assertEquals(Double.MAX_VALUE, parser.getValueAsDouble(), 0.000001);
    }

    
}