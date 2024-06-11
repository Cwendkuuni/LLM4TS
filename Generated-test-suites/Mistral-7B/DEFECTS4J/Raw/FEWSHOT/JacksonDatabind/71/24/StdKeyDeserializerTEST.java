package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.StdKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class StdKeyDeserializerTest {

    @Test
    public void testStringKD() {
        KeyDeserializer kd = StdKeyDeserializer.forType(String.class);
        assertEquals(String.class, kd.getKeyClass());
        String key = "test";
        Object result = null;
        try {
            result = kd.deserializeKey(key, null);
        } catch (IOException e) {
            fail("IOException thrown when it shouldn't be");
        }
        assertEquals(key, result);
    }

    @Test
    public void testBooleanKD() {
        KeyDeserializer kd = StdKeyDeserializer.forType(Boolean.class);
        assertEquals(Boolean.class, kd.getKeyClass());
        Object result = null;
        try {
            result = kd.deserializeKey("true", null);
        } catch (IOException e) {
            fail("IOException thrown when it shouldn't be");
        }
        assertTrue((Boolean) result);

        try {
            result = kd.deserializeKey("false", null);
        } catch (IOException e) {
            fail("IOException thrown when it shouldn't be");
        }
        assertFalse((Boolean) result);

        try {
            result = kd.deserializeKey("1", null);
            fail("Should not be able to parse integer as Boolean");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testByteKD() {
        KeyDeserializer kd = StdKeyDeserializer.forType(Byte.class);
        assertEquals(Byte.class, kd.getKeyClass());
        Object result = null;
        try {
            result = kd.deserializeKey("127", null);
        } catch (IOException e) {
            fail("IOException thrown when it shouldn't be");
        }
        assertEquals((Byte) new Byte((byte) 127), result);

        try {
            result = kd.deserializeKey("-128", null);
        } catch (IOException e) {
            fail("IOException thrown when it shouldn't be");
        }
        assertEquals((Byte) new Byte((byte) -128), result);

        try {
            result = kd.deserializeKey("256", null);
            fail("Should not be able to parse value greater than Byte.MAX_VALUE");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testShortKD() {
        KeyDeserializer kd = StdKeyDeserializer.forType(Short.class);
        assertEquals(Short.class, kd.getKeyClass());
        Object result = null;
        try {
            result = kd.deserializeKey("32767", null);
        } catch (IOException e) {
            fail("IOException thrown when it shouldn't be");
        }
        assertEquals((Short) new Short((short) 32767), result);

        try {
            result = kd.deserializeKey("-32768", null);
        } catch (IOException e) {
            fail("IOException thrown when it shouldn't be");
        }
        assertEquals((Short) new Short((short) -32768), result);

        try {
            result = kd.deserializeKey("65536", null);
            fail("Should not be able to parse value greater than Short.MAX_VALUE");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testCharKD() {
        KeyDeserializer kd = StdKeyDeserializer.forType(Character.class);
        assertEquals(Character.class, kd.getKeyClass());
        Object result = null;
        try {
            result = kd.deserializeKey("A", null);
        } catch (IOException e) {
            fail("IOException thrown when it shouldn't be");
        }
        assertEquals((Character) 'A', result);

        try {
            result = kd.deserializeKey("\\u00E9", null);
        } catch (IOException e) {
            fail("IOException thrown when it shouldn't be");
        }
        assertEquals((Character) '\u00E9', result);

        try {
            result = kd.deserializeKey("", null);
            fail("Should not be able to parse empty string as Character");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testIntKD() {
        KeyDeserializer kd = StdKeyDeserializer.forType(Integer.class);
        assertEquals(Integer.class, kd.getKeyClass());
        Object result = null;
        try {
            result = kd.deserializeKey("123456", null);
        } catch (IOException e) {
            fail("IOException thrown when it shouldn't be");
        }
        assertEquals((Integer) 123456, result);
    }

    @Test
    public void testLongKD() {
        KeyDeserializer kd = StdKeyDeserializer.forType(Long.class);
        assertEquals(Long.class, kd.getKeyClass());
        Object result = null;
        try {
            result = kd.deserializeKey("12345678901234567890", null);
        } catch (IOException e) {
            fail("IOException thrown when it shouldn't be");
        }
        assertEquals((Long) 12345678901234567890L, result);
    }

    @Test
    public void testFloatKD() {
        KeyDeserializer kd = StdKeyDeserializer.forType(Float.class);
        assertEquals(Float.class, kd.getKeyClass());
        Object result = null;
        try {
            result = kd.deserializeKey("3.14159", null);
        } catch (IOException e) {
            fail("IOException thrown when it shouldn't be");
        }
        assertEquals((Float) 3.14159f, result);
    }

    @Test
    public void testDoubleKD() {
        KeyDeserializer kd = StdKeyDeserializer.forType(Double.class);
        assertEquals(Double.class, kd.getKeyClass());
        Object result = null;
        try {
            result = kd.deserializeKey("3.14159265358979323846", null);
        } catch (IOException e) {
            fail("IOException thrown when it shouldn't be");
        }
        assertEquals((Double) 3.14159265358979323846d, result);
    }

    @Test
    public void testLocaleKD() throws Exception {
        KeyDeserializer kd = StdKeyDeserializer.forType(Locale.class);
        assertEquals(Locale.class, kd.getKeyClass());
        Object result = null;
        try {
            result = kd.deserializeKey("en_US", null);
        } catch (IOException e) {
            fail("IOException thrown when it shouldn't be");
        }
        assertEquals(Locale.US, result);

        try {
            result = kd.deserializeKey("fr_FR", null);
        } catch (IOException e) {
            fail("IOException thrown when it shouldn't be");
        }
        assertEquals(Locale.FRANCE, result);
    }

    @Test
    public void testURIKD() {
        KeyDeserializer kd = StdKeyDeserializer.forType(URI.class);
        assertEquals(URI.class, kd.getKeyClass());
        Object result = null;
        try {
            result = kd.deserializeKey("https://www.example.com", null);
        } catch (IOException e) {
            fail("IOException thrown when it shouldn't be");
        }
        assertEquals(new URI("https://www.example.com"), result);
    }

    @Test
    public void testURLKD() {
        KeyDeserializer kd = StdKeyDeserializer.forType(URL.class);
        assertEquals(URL.class, kd.getKeyClass());
        Object result = null;
        try {
            result = kd.deserializeKey("http://www.example.com", null);
        } catch (IOException e) {
            fail("IOException thrown when it shouldn't be");
        }
        assertEquals(new URL("http://www.example.com"), result);
    }

    @Test
    public void testClassKD() {
        KeyDeserializer kd = StdKeyDeserializer.forType(String[].class);
        assertEquals(String[].class, kd.getKeyClass());
        Object result = null;
        try {
            result = kd.deserializeKey("[Ljava.lang.String;", null);
        } catch (IOException e) {
            fail("IOException thrown when it shouldn't be");
        }
        assertEquals(new String[] {"foo", "bar"}, result);

        AnnotatedClass ac = new AnnotatedClass(String[].class);
        AnnotatedMethod am = ac.findMethod("valueOf", String[].class);
        KeyDeserializer factoryKd = new StdKeyDeserializer.StringFactoryKeyDeserializer(am);
        assertEquals(String[].class, factoryKd.getKeyClass());
        result = null;
        try {
            result = factoryKd.deserializeKey("[Ljava.lang.String;", null);
        } catch (IOException e) {
            fail("IOException thrown when it shouldn't be");
        }
        assertEquals(new String[] {"foo", "bar"}, result);
    }

    @Test
    public void testEnumKD() {
        KeyDeserializer kd = StdKeyDeserializer.forType(MyEnum.class);
        assertEquals(MyEnum.class, kd.getKeyClass());
        Object result = null;
        try {
            result = kd.deserializeKey("VALUE_1", null);
        } catch (IOException e) {
            fail("IOException thrown when it shouldn't be");
        }
        assertEquals(MyEnum.VALUE_1, result);

        try {
            result = kd.deserializeKey("VALUE_X", null);
            fail("Should not be able to parse unknown enum value");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testDelegatingKD() {
        Class<?> clazz = MyCustomClass.class;
        JsonDeserializer<?> jsonDeserializer = new MyCustomJsonDeserializer();
        KeyDeserializer kd = new StdKeyDeserializer.DelegatingKD(clazz, jsonDeserializer);
        Object result = null;
        try {
            result = kd.deserializeKey("{\"name\": \"test\"}", null);
        } catch (IOException e) {
            fail("IOException thrown when it shouldn't be");
        }
        assertEquals(new MyCustomClass("test"), result);
    }

    private static class MyEnum {
        public static enum VALUE_1 {
            VALUE_1
        }

        public static enum VALUE_2 {
            VALUE_2
        }
    }

    private static class MyCustomClass {
        private final String name;

        public MyCustomClass(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "{\"name\": \"" + name + "\"}";
        }
    }

    private static class MyCustomJsonDeserializer extends JsonDeserializer<MyCustomClass> {
        @Override
        public MyCustomClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            ObjectCodec oc = p.getCodec();
            MapNode node = oc.readTree(p);
            return new MyCustomClass(node.get("name").asText());
        }
    }
}