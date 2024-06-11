package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.*;

import org.junit.Test;

public class NumberSerializersTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testIntegerSerializer() throws Exception {
        IntegerSerializer integerSerializer = new IntegerSerializer();
        Integer number = 123;
        JsonGenerator gen = mapper.getFactory().createGenerator();

        integerSerializer.serialize(number, gen, new SerializerProvider());

        TextNode expectedNode = JsonNodeFactory.instance.textNode("123");
        assertEquals(expectedNode, gen.getOutputContent());
    }

    @Test
    public void testIntLikeSerializer() throws Exception {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        Number number = 123;
        JsonGenerator gen = mapper.getFactory().createGenerator();

        intLikeSerializer.serialize(number, gen, new SerializerProvider());

        TextNode expectedNode = JsonNodeFactory.instance.textNode("123");
        assertEquals(expectedNode, gen.getOutputContent());
    }

    @Test
    public void testShortSerializer() throws Exception {
        ShortSerializer shortSerializer = new ShortSerializer();
        Short number = 127;
        JsonGenerator gen = mapper.getFactory().createGenerator();

        shortSerializer.serialize(number, gen, new SerializerProvider());

        TextNode expectedNode = JsonNodeFactory.instance.textNode("127");
        assertEquals(expectedNode, gen.getOutputContent());
    }

    @Test
    public void testLongSerializer() throws Exception {
        LongSerializer longSerializer = new LongSerializer();
        Long number = 12345678912345L;
        JsonGenerator gen = mapper.getFactory().createGenerator();

        longSerializer.serialize(number, gen, new SerializerProvider());

        TextNode expectedNode = JsonNodeFactory.instance.textNode("12345678912345");
        assertEquals(expectedNode, gen.getOutputContent());
    }

    @Test
    public void testFloatSerializer() throws Exception {
        FloatSerializer floatSerializer = new FloatSerializer();
        Float number = 3.14159F;
        JsonGenerator gen = mapper.getFactory().createGenerator();

        floatSerializer.serialize(number, gen, new SerializerProvider());

        TextNode expectedNode = JsonNodeFactory.instance.textNode("3.14159");
        assertEquals(expectedNode, gen.getOutputContent());
    }

    @Test
    public void testDoubleSerializer() throws Exception {
        DoubleSerializer doubleSerializer = new DoubleSerializer();
        Double number = 1.732055555556D;
        JsonGenerator gen = mapper.getFactory().createGenerator();

        doubleSerializer.serialize(number, gen, new SerializerProvider());

        TextNode expectedNode = JsonNodeFactory.instance.textNode("1.732055555556");
        assertEquals(expectedNode, gen.getOutputContent());
    }
}