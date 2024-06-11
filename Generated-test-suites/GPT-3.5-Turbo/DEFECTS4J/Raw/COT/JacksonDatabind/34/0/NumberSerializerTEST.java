
package com.fasterxml.jackson.databind.ser.std;

import org.junit.Assert;
import org.junit.Test;

public class NumberSerializerTest {
    
    @Test
    public void testSerializeBigDecimal() {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        BigDecimal value = new BigDecimal("10.5");
        JsonGeneratorStub generator = new JsonGeneratorStub();
        
        try {
            serializer.serialize(value, generator, null);
            String expected = "10.5";
            Assert.assertEquals(expected, generator.getOutput());
        } catch (IOException e) {
            // test failed
            Assert.fail();
        }
    }
    
    @Test
    public void testSerializeBigInteger() {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        BigInteger value = new BigInteger("1234567890");
        JsonGeneratorStub generator = new JsonGeneratorStub();
        
        try {
            serializer.serialize(value, generator, null);
            String expected = "1234567890";
            Assert.assertEquals(expected, generator.getOutput());
        } catch (IOException e) {
            // test failed
            Assert.fail();
        }
    }
    
    @Test
    public void testSerializeInteger() {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        Integer value = 10;
        JsonGeneratorStub generator = new JsonGeneratorStub();
        
        try {
            serializer.serialize(value, generator, null);
            String expected = "10";
            Assert.assertEquals(expected, generator.getOutput());
        } catch (IOException e) {
            // test failed
            Assert.fail();
        }
    }
    
    @Test
    public void testSerializeLong() {
        NumberSerializer serializer = new NumberSerializer(Long.class);
        Long value = 100L;
        JsonGeneratorStub generator = new JsonGeneratorStub();
        
        try {
            serializer.serialize(value, generator, null);
            String expected = "100";
            Assert.assertEquals(expected, generator.getOutput());
        } catch (IOException e) {
            // test failed
            Assert.fail();
        }
    }
    
    @Test
    public void testSerializeDouble() {
        NumberSerializer serializer = new NumberSerializer(Double.class);
        Double value = 3.14;
        JsonGeneratorStub generator = new JsonGeneratorStub();
        
        try {
            serializer.serialize(value, generator, null);
            String expected = "3.14";
            Assert.assertEquals(expected, generator.getOutput());
        } catch (IOException e) {
            // test failed
            Assert.fail();
        }
    }
    
    @Test
    public void testSerializeFloat() {
        NumberSerializer serializer = new NumberSerializer(Float.class);
        Float value = 2.5f;
        JsonGeneratorStub generator = new JsonGeneratorStub();
        
        try {
            serializer.serialize(value, generator, null);
            String expected = "2.5";
            Assert.assertEquals(expected, generator.getOutput());
        } catch (IOException e) {
            // test failed
            Assert.fail();
        }
    }
    
    @Test
    public void testSerializeByte() {
        NumberSerializer serializer = new NumberSerializer(Byte.class);
        Byte value = 5;
        JsonGeneratorStub generator = new JsonGeneratorStub();
        
        try {
            serializer.serialize(value, generator, null);
            String expected = "5";
            Assert.assertEquals(expected, generator.getOutput());
        } catch (IOException e) {
            // test failed
            Assert.fail();
        }
    }
    
    @Test
    public void testSerializeShort() {
        NumberSerializer serializer = new NumberSerializer(Short.class);
        Short value = 7;
        JsonGeneratorStub generator = new JsonGeneratorStub();
        
        try {
            serializer.serialize(value, generator, null);
            String expected = "7";
            Assert.assertEquals(expected, generator.getOutput());
        } catch (IOException e) {
            // test failed
            Assert.fail();
        }
    }
    
    @Test
    public void testFallbackNumber() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        Number value = 123.45;
        JsonGeneratorStub generator = new JsonGeneratorStub();
        
        try {
            serializer.serialize(value, generator, null);
            String expected = "123.45";
            Assert.assertEquals(expected, generator.getOutput());
        } catch (IOException e) {
            // test failed
            Assert.fail();
        }
    }
}
