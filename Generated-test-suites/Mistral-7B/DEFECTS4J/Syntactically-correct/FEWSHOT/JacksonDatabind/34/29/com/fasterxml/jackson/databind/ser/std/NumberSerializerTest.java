package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.Version.VERSION_2_12;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.version.Version;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer.NumberSerializerBase;

/**
 * Comprehensive test cases for {@link NumberSerializer} class.
 */
public class NumberSerializerTest {

    private static final JsonNodeFactory NF = JsonNodeFactory.instance;

    @Test
    public void testSerialization() throws Exception {
        SerializerProvider serializers = new StandardSerializerFinder().findValueSerializers(null, null);
        NumberSerializerBase serializer = NumberSerializer.instance;

        // Test serialization for simple number types
        assertEquals(NF.numberNode(123), serializer.serialize(123, null, serializers));
        assertEquals(NF.numberNode(45.6), serializer.serialize(45.6, null, serializers));
        assertEquals(NF.numberNode(0.123), serializer.serialize(0.123, null, serializers));
        assertEquals(NF.numberNode(-123), serializer.serialize(-123, null, serializers));

        // Test serialization for BigInteger and BigDecimal
        BigInteger bigInt = BigInteger.valueOf(12345);
        BigDecimal bigDec = new BigDecimal("123.45");
        JsonGenerator gen = new TestJsonGenerator();
        serializer.serialize(bigInt, gen, serializers);
        assertEquals(new TextNode("12345"), gen.getOutputContent());
        gen.reset();
        serializer.serialize(bigDec, gen, serializers);
        assertEquals(new TextNode("123.45"), gen.getOutputContent());

        // Test serialization for custom Number types
        CustomNumber customNum = new CustomNumber(123);
        ObjectWriter writer = new JsonFactory().writer();
        assertEquals(writer.writeValueAsString(customNum), serializer.serialize(customNum, null, serializers).toString());
    }

    @Test
    public void testDeserialization() throws Exception {
        DeserializationConfig config = new DeserializationConfig(new JsonFactory(), Version.builder(VERSION_2_12).build());
        ObjectMapper mapper = new ObjectMapper(config);
        NumberDeserializers deserializers = new StandardDeserializerFinder().findNumberDeserializers(config, null);

        // Test deserialization for simple number types
        assertEquals(123, mapper.readValue(NF.numberNode(123).toString(), Number.class));
        assertEquals(45.6, mapper.readValue(NF.numberNode(45.6).toString(), Number.class));
        assertEquals(0.123, mapper.readValue(NF.numberNode(0.123).toString(), Number.class));
        assertEquals(-123, mapper.readValue(NF.numberNode(-123).toString(), Number.class));

        // Test deserialization for BigInteger and BigDecimal
        JsonParser parser = new TestJsonParser();
        assertEquals(BigInteger.valueOf(12345), mapper.readValue(new TextNode("12345"), Number.class));
        assertEquals(new BigDecimal("123.45"), mapper.readValue(new TextNode("123.45"), Number.class));

        // Test deserialization for custom Number types
        CustomNumber customNum = mapper.readValue(new TextNode("123"), CustomNumber.class);
        assertEquals(123, customNum.getValue());
    }

    @Test
    public void testSerializationAndDeserialization() throws Exception {
        SerializerProvider serializers = new StandardSerializerFinder().findValueSerializers(null, null);
        DeserializationConfig config = new DeserializationConfig(new JsonFactory(), Version.builder(VERSION_2_12).build());
        ObjectMapper mapper = new ObjectMapper(config);
        NumberSerializerBase serializer = NumberSerializer.instance;
        NumberDeserializers deserializers = new StandardDeserializerFinder().findNumberDeserializers(config, null);

        // Test serialization and deserialization for simple number types
        assertEquals(123, mapper.readValue(serializer.serialize(123, null, serializers).toString(), Number.class));

        // Test serialization and deserialization for BigInteger and BigDecimal
        BigInteger bigInt = BigInteger.valueOf(12345);
        BigDecimal bigDec = new BigDecimal("123.45");
        ObjectWriter writer = new JsonFactory().writer();
        assertEquals(bigInt, mapper.readValue(writer.writeValueAsString(serializer.serialize(bigInt, null, serializers)), Number.class));
        assertEquals(bigDec, mapper.readValue(writer.writeValueAsString(serializer.serialize(bigDec, null, serializers)), Number.class));

        // Test serialization and deserialization for custom Number types
        CustomNumber customNum = new CustomNumber(123);
        ObjectWriter writer2 = new JsonFactory().writer();
        assertEquals(customNum, mapper.readValue(writer2.writeValueAsString(serializer.serialize(customNum, null, serializers)), CustomNumber.class));
    }
}

class TestJsonGenerator extends JsonGenerator {
    // Empty implementation
}

class TestJsonParser extends JsonParser {
    // Empty implementation
}

class CustomNumber {
    private int value;

    public CustomNumber(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}