
package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {
    
    @Test
    public void testShortSerializer() {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();
        Short value = 100;
        
        // Test serialization
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        SerializerProvider provider = new DefaultSerializerProvider();
        try {
            serializer.serialize(value, gen, provider);
            gen.flush();
            assertEquals("100", writer.toString());
        } catch (IOException e) {
            fail("Exception thrown during serialization: " + e.getMessage());
        }
    }
    
    @Test
    public void testIntegerSerializer() {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
        Integer value = 100;
        
        // Test serialization
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        SerializerProvider provider = new DefaultSerializerProvider();
        try {
            serializer.serialize(value, gen, provider);
            gen.flush();
            assertEquals("100", writer.toString());
        } catch (IOException e) {
            fail("Exception thrown during serialization: " + e.getMessage());
        }
    }
    
    // Add tests for other serializers
    
}

