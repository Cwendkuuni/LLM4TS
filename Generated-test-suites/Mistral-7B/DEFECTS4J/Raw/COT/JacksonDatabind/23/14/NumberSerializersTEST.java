package com.yourpackage; // replace with your test package name

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

    

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        Assert.assertEquals(11, allDeserializers.size());
        Assert.assertNotNull(allDeserializers.get(Integer.class.getName()));
        Assert.assertNotNull(allDeserializers.get(Integer.TYPE.getName()));
        Assert.assertNotNull(allDeserializers.get(Long.class.getName()));
        Assert.assertNotNull(allDeserializers.get(Long.TYPE.getName()));
        Assert.assertNotNull(allDeserializers.get(Byte.class.getName()));
        Assert.assertNotNull(allDeserializers.get(Byte.TYPE.getName()));
        Assert.assertNotNull(allDeserializers.get(Short.class.getName()));
        Assert.assertNotNull(allDeserializers.get(Short.TYPE.getName()));
        Assert.assertNotNull(allDeserializers.get(Float.class.getName()));
        Assert.assertNotNull(allDeserializers.get(Float.TYPE.getName()));
        Assert.assertNotNull(allDeserializers.get(Double.class.getName()));
        Assert.assertNotNull(allDeserializers.get(Double.TYPE.getName()));
    }

    @Test
    public void testIntegerSerializer() {
        JsonSerializer<Object> integerSerializer = NumberSerializers.getInstance(IntegerSerializer.class);

        ObjectNode rootNode = ObjectMapperBuilder.json()
                .build()
                .createObjectNode();

        Integer number = 123;
        Integer serializedNumber = 456;

        try (JsonGenerator gen = rootNode.setUpTree(ObjectMapperBuilder.json().build().getFactory().createGenerator(new ByteArrayOutputStream()))){
            integerSerializer.serialize(number, gen, new DefaultSerializerProvider());
            gen.flush();
            ObjectNode jsonNode = rootNode;
            JsonParser parser = ObjectMapperBuilder.json().build().getFactory().createParser(rootNode.toString().getBytes());
            assertTrue(parser.nextValue().isIntegralNumber());
            assertEquals(number.intValue(), parser.nextValue().asInt());
            parser.close();

            rootNode.replace("number", ObjectMapper.parseNumber(Integer.toString(serializedNumber)));
            integerSerializer.deserialize(rootNode, Long.TYPE, new DefaultSerializerProvider().withoutContextualSerialization());
            assertEquals(serializedNumber, integerSerializer.deserialize(rootNode, Integer.TYPE, new DefaultSerializerProvider()).getValueAsInt());
            rootNode.close();
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException occurred during test");
        }
    }

    @Test
    public void testIntLikeSerializer() {
        JsonSerializer<Number> intLikeSerializer = NumberSerializers.getInstance(IntLikeSerializer.class);

        ObjectNode rootNode = ObjectMapperBuilder.json()
                .build()
                .createObjectNode();

        Byte number = 123;
        Byte serializedNumber = 45;

        try (JsonGenerator gen = rootNode.setUpTree(ObjectMapperBuilder.json().build().getFactory().createGenerator(new ByteArrayOutputStream()))){
            intLikeSerializer.serialize(number, gen, new DefaultSerializerProvider());
            gen.flush();
            ObjectNode jsonNode = rootNode;
            JsonParser parser = ObjectMapperBuilder.json().build().getFactory().createParser(rootNode.toString().getBytes());
            assertTrue(parser.nextValue().isIntegralNumber());
            assertEquals(number.byteValue(), parser.nextValue().asInt());
            parser.close();

            rootNode.replace("number", ObjectMapper.parseNumber(Byte.toString(serializedNumber)));
            intLikeSerializer.deserialize(rootNode, Byte.TYPE, new DefaultSerializerProvider().withoutContextualSerialization());
            assertEquals(serializedNumber, intLikeSerializer.deserialize(rootNode, Number.class, new DefaultSerializerProvider()).getValueAsInt());
            rootNode.close();
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException occurred during test");
        }
    }

    @Test
    public void testLongSerializer() {
        JsonSerializer<Object> longSerializer = NumberSerializers.getInstance(LongSerializer.class);

        ObjectNode rootNode = ObjectMapperBuilder.json()
                .build()
                .createObjectNode();

        Long number = 123456789L;
        Long serializedNumber = 987654321L;

        try (JsonGenerator gen = rootNode.setUpTree(ObjectMapperBuilder.json().build().getFactory().createGenerator(new ByteArrayOutputStream()))){
            longSerializer.serialize(number, gen, new DefaultSerializerProvider());
            gen.flush();
            ObjectNode jsonNode = rootNode;
            JsonParser parser = ObjectMapperBuilder.json().build().getFactory().createParser(rootNode.toString().getBytes());
            assertTrue(parser.nextValue().isLong());
            assertEquals(number.longValue(), parser.nextValue().asLong());
            parser.close();

            rootNode.replace("number", ObjectMapper.parseNumber(Long.toString(serializedNumber)));
            longSerializer.deserialize(rootNode, Long.TYPE, new DefaultSerializerProvider().withoutContextualSerialization());
            assertEquals(serializedNumber, longSerializer.deserialize(rootNode, Long.TYPE, new DefaultSerializerProvider()).getValueAsLong());
            rootNode.close();
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException occurred during test");
        }
    }

    @Test
    public void testFloatSerializer() {
        JerkerSerializer<Object> floatSerializer = NumberSerializers.getInstance(FloatSerializer.class);

        ObjectNode rootNode = ObjectMapperBuilder.json()
                .build()
                .createObjectNode();

        Float number = 123.45f;
        Float serializedNumber = 98.76f;

        try (JsonGenerator gen = rootNode.setUpTree(ObjectMapperBuilder.json().build().getFactory().createGenerator(new ByteArrayOutputStream()))){
            floatSerializer.serialize(number, gen, new DefaultSerializerProvider());
            gen.flush();
            ObjectNode jsonNode = rootNode;
            JsonParser parser = ObjectMapperBuilder.json().build().getFactory().createParser(rootNode.toString().getBytes());
            assertTrue(parser.nextValue().isFloat());
            assertEquals(number.floatValue(), parser.nextValue().asFloat(), 0.001f);
            parser.close();

            rootNode.replace("number", ObjectMapper.parseNumber(Float.toString(serializedNumber)));
            floatSerializer.deserialize(rootNode, Float.TYPE, new DefaultSerializerProvider().withoutContextualSerialization());
            assertEquals(serializedNumber, floatSerializer.deserialize(rootNode, Float.TYPE, new DefaultSerializerProvider()).getValueAsFloat(), 0.001f);
            rootNode.close();
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException occurred during test");
        }
    }

    @Test
    public void testDoubleSerializer() {
        JerkerSerializer<Object> doubleSerializer = NumberSerializers.getInstance(DoubleSerializer.class);

        ObjectNode rootNode = ObjectMapperBuilder.json()
                .build()
                .createObjectNode();

        Double number = 123.456789d;
        Double serializedNumber = 98.7654321d;

        try (JsonGenerator gen = rootNode.setUpTree(ObjectMapperBuilder.json().build().getFactory().createGenerator(new ByteArrayOutputStream()))){
            doubleSerializer.serialize(number, gen, new DefaultSerializerProvider());
            gen.flush();
            ObjectNode jsonNode = rootNode;
            JsonParser parser = ObjectMapperBuilder.json().build().getFactory().createParser(rootNode.toString().getBytes());
            assertTrue(parser.nextValue().isNumber());
            assertEquals(number.doubleValue(), parser.nextValue().asDouble(), 0.0001);
            parser.close();

            rootNode.replace("number", ObjectMapper.parseNumber(Double.toString(serializedNumber)));
            doubleSerializer.deserialize(rootNode, Double.TYPE, new DefaultSerializerProvider().withoutContextualSerialization());
            assertEquals(serializedNumber, doubleSerializer.deserialize(rootNode, Double.TYPE, new DefaultSerializerProvider()).getValueAsDouble(), 0.0001);
            rootNode.close();
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException occurred during test");
        }
    }

    
}