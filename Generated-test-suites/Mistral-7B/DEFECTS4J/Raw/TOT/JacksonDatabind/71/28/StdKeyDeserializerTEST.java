package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.TestBase;

public class StdKeyDeserializerTest extends TestBase {

    // Test for default constructor
    @Test
    public void testDefaultConstructor() throws Exception {
        StdKeyDeserializer kd = new StdKeyDeserializer(-1, String.class);
        assertNull(kd._deser);
    }

    // Test for constructor with FromStringDeserializer
    @Test
    public void testConstructorWithDeserializer() throws Exception {
        AnnotatedClass ac = mc.findClass("com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer");
        AnnotatedMethod am = ac.findMethod("forType", Class.class);
        StdKeyDeserializer kd = (StdKeyDeserializer) am.call(null, String.class);
        assertNotNull(kd._deser);
    }

    // Test deserialization with different kinds
    @Test
    public void testDeserializationWithDifferentKinds() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer kd = StdKeyDeserializer.forType(String.class);

        // Test boolean
        ObjectNode node1 = mapper.createObjectNode();
        node1.put("key", "true");
        Object key1 = kd.deserializeKey("key", mapper.getDeserializationContext());
        assertTrue((Boolean) key1);

        // Test byte
        ObjectNode node2 = mapper.createObjectNode();
        node2.put("key", "127");
        Object key2 = kd.deserializeKey("key", mapper.getDeserializationContext());
        assertEquals((Byte) (byte) 127, key2);

        // Test short
        ObjectNode node3 = mapper.createObjectNode();
        node3.put("key", "32767");
        Object key3 = kd.deserializeKey("key", mapper.getDeserializationContext());
        assertEquals((Short) 32767, key3);

        // Test char
        ObjectNode node4 = mapper.createObjectNode();
        node4.put("key", "A");
        Object key4 = kd.deserializeKey("key", mapper.getDeserializationContext());
        assertEquals((Character) 'A', key4);

        // Test int
        ObjectNode node5 = mapper.createObjectNode();
        node5.put("key", "42");
        Object key5 = kd.deserializeKey("key", mapper.getDeserializationContext());
        assertEquals(42, key5);

        // Test long
        ObjectNode node6 = mapper.createObjectNode();
        node6.put("key", "9223372036854775807");
        Object key6 = kd.deserializeKey("key", mapper.getDeserializationContext());
        assertEquals(9223372036854775807L, key6);

        // Test float
        ObjectNode node7 = mapper.createObjectNode();
        node7.put("key", "3.14159265358979323846");
        Object key7 = kd.deserializeKey("key", mapper.getDeserializationContext());
        assertEquals(3.14159265358979323846f, key7, 0.00001f);

        // Test double
        ObjectNode node8 = mapper.createObjectNode();
        node8.put("key", "3.14159265358979323846");
        Object key8 = kd.deserializeKey("key", mapper.getDeserializationContext());
        assertEquals(3.14159265358979323846d, key8, 0.0000001d);

        // Test Locale
        ObjectNode node9 = mapper.createObjectNode();
        node9.put("key", "en_US");
        Object key9 = kd.deserializeKey("key", mapper.getDeserializationContext());
        assertEquals(Locale.US, key9);

        // Test UUID
        ObjectNode node10 = mapper.createObjectNode();
        node10.put("key", "123e4567-e89b-12d3-a456-426614174000");
        Object key10 = kd.deserializeKey("key", mapper.getDeserializationContext());
        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), key10);

        // Test URI
        ObjectNode node11 = mapper.createObjectNode();
        node11.put("key", "http://test.com");
        Object key11 = kd.deserializeKey("key", mapper.getDeserializationContext());
        assertEquals(new URI("http://test.com"), key11);

        // Test URL
        ObjectNode node12 = mapper.createObjectNode();
        node12.put("key", "http://test.com");
        Object key12 = kd.deserializeKey("key", mapper.getDeserializationContext());
        assertEquals(new URL("http://test.com"), key12);

        // Test Class
        ObjectNode node13 = mapper.createObjectNode();
        node13.put("key", "java.lang.String");
        Object key13 = kd.deserializeKey("key", mapper.getDeserializationContext());
        assertEquals(String.class, key13);

        // Test Currency
        ObjectNode node14 = mapper.createObjectNode();
        node14.put("key", "USD");
        Object key14 = kd.deserializeKey("key", mapper.getDeserializationContext());
        assertEquals(Currency.getInstance("USD"), key14);
    }

    // Test deserialization with null key
    @Test
    public void testDeserializationWithNullKey() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer kd = StdKeyDeserializer.forType(String.class);

        JsonParser parser = mapper.getFactory().createParser("null");
        Object key = kd.deserializeKey(null, mapper.getDeserializationContext());
        assertNull(key);
    }

    // Test deserialization with invalid keys
    @Test
    public void testDeserializationWithInvalidKeys() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer kd = StdKeyDeserializer.forType(String.class);

        // Test boolean with invalid key
        ObjectNode node1 = mapper.createObjectNode();
        node1.put("key", "invalid");
        try {
            kd.deserializeKey("key", mapper.getDeserializationContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        // Test byte with invalid key
        ObjectNode node2 = mapper.createObjectNode();
        node2.put("key", "invalid");
        try {
            kd.deserializeKey("key", mapper.getDeserializationContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        // Test short with invalid key
        ObjectNode node3 = mapper.createObjectNode();
        node3.put("key", "invalid");
        try {
            kd.deserializeKey("key", mapper.getDeserializationContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        // Test char with invalid key
        ObjectNode node4 = mapper.createObjectNode();
        node4.put("key", "invalid");
        try {
            kd.deserializeKey("key", mapper.getDeserializationContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        // Test int with invalid key
        ObjectNode node5 = mapper.createObjectNode();
        node5.put("key", "invalid");
        try {
            kd.deserializeKey("key", mapper.getDeserializationContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        // Test long with invalid key
        ObjectNode node6 = mapper.createObjectNode();
        node6.put("key", "invalid");
        try {
            kd.deserializeKey("key", mapper.getDeserializationContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        // Test float with invalid key
        ObjectNode node7 = mapper.createObjectNode();
        node7.put("key", "invalid");
        try {
            kd.deserializeKey("key", mapper.getDeserializationContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        // Test double with invalid key
        ObjectNode node8 = mapper.createObjectNode();
        node8.put("key", "invalid");
        try {
            kd.deserializeKey("key", mapper.getDeserializationContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        // Test Locale with invalid key
        ObjectNode node9 = mapper.createObjectNode();
        node9.put("key", "invalid");
        try {
            kd.deserializeKey("key", mapper.getDeserializationContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        // Test UUID with invalid key
        ObjectNode node10 = mapper.createObjectNode();
        node10.put("key", "invalid");
        try {
            kd.deserializeKey("key", mapper.getDeserializationContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        // Test URI with invalid key
        ObjectNode node11 = mapper.createObjectNode();
        node11.put("key", "invalid");
        try {
            kd.deserializeKey("key", mapper.getDeserializationContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        // Test URL with invalid key
        ObjectNode node12 = mapper.createObjectNode();
        node12.put("key", "invalid");
        try {
            kd.deserializeKey("key", mapper.getDeserializationContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        // Test Class with invalid key
        ObjectNode node13 = mapper.createObjectNode();
        node13.put("key", "invalid");
        try {
            kd.deserializeKey("key", mapper.getDeserializationContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }

        // Test Currency with invalid key
        ObjectNode node14 = mapper.createObjectNode();
        node14.put("key", "invalid");
        try {
            kd.deserializeKey("key", mapper.getDeserializationContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }
    }

    // Test deserialization with wrong key type
    @Test
    public void testDeserializationWithWrongKeyType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer kd = StdKeyDeserializer.forType(String.class);

        ObjectNode node = mapper.createObjectNode();
        node.put("key", 123);

        try {
            kd.deserializeKey("key", mapper.getDeserializationContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }
    }
}