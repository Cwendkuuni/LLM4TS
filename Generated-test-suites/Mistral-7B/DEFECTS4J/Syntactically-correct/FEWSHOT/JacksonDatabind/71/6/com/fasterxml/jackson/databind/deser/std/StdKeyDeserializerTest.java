package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

public class StdKeyDeserializerTest {

    @Test
    public void testBooleanKeyDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
        Object key = serializer.deserializeKey("true", new TestContext());
        assertEquals(Boolean.TRUE, key);

        key = serializer.deserializeKey("false", new TestContext());
        assertEquals(Boolean.FALSE, key);

        try {
            key = serializer.deserializeKey("invalid", new TestContext());
            fail();
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testByteKeyDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
        Object key = serializer.deserializeKey("127", new TestContext());
        assertEquals((byte) 127, key);

        try {
            key = serializer.deserializeKey("128", new TestContext());
            fail();
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testShortKeyDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);
        Object key = serializer.deserializeKey("32767", new TestContext());
        assertEquals((short) 32767, key);

        try {
            key = serializer.deserializeKey("32768", new TestContext());
            fail();
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testCharKeyDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);
        Object key = serializer.deserializeKey("A", new TestContext());
        assertEquals('A', key);
    }

    @Test
    public void testIntKeyDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
        Object key = serializer.deserializeKey("42", new TestContext());
        assertEquals(42, key);
    }

    @Test
    public void testLongKeyDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);
        Object key = serializer.deserializeKey("9223372036854775807", new TestContext());
        assertEquals(9223372036854775807L, key);

        try {
            key = serializer.deserializeKey("9223372036854775808", new TestContext());
            fail();
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testFloatKeyDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);
        Object key = serializer.deserializeKey("3.14159265", new TestContext());
        assertEquals(3.14159265f, key);
    }

    @Test
    public void testDoubleKeyDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);
        Object key = serializer.deserializeKey("6.283185307179586", new TestContext());
        assertEquals(6.283185307179586, key);
    }

    @Test
    public void testLocaleKeyDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
        Object key = serializer.deserializeKey("en_US", new TestContext());
        assertEquals(Locale.US, key);
    }

    @Test
    public void testDateKeyDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Date.class);
        Object key = serializer.deserializeKey("2022-01-01", new TestContext());
        assertEquals("Sat Jan 01 00:00:00 CET 2022", key.toString());
    }

    @Test
    public void testCalendarKeyDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Calendar.class);
        Object key = serializer.deserializeKey("2022-01-01", new TestContext());
        assertEquals(2022, ((Calendar) key).get(Calendar.YEAR));
        assertEquals(1, ((Calendar) key).get(Calendar.MONTH) + 1);
        assertEquals(1, ((Calendar) key).get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void testUUIDKeyDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(UUID.class);
        Object key = serializer.deserializeKey("00000000-0000-0000-0000-000000000000", new TestContext());
        assertEquals("00000000-0000-0000-0000-000000000000", key.toString());
    }

    @Test
    public void testURIKeyDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);
        Object key = serializer.deserializeKey("http://www.example.com", new TestContext());
        assertEquals("http://www.example.com", key.toString());
    }

    @Test
    public void testURLKeyDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
        Object key = serializer.deserializeKey("http://www.example.com", new TestContext());
        assertEquals("http://www.example.com", key.toString());
    }

    @Test
    public void testClassKeyDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class);
        Object key = serializer.deserializeKey("java.lang.String", new TestContext());
        assertEquals(String.class, key);
    }

    @Test
    public void testEnumKeyDeserializer() {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(MyEnum.class);
        Object key = serializer.deserializeKey("VAL_B", new TestContext());
        assertEquals(MyEnum.VAL_B, key);
    }

    @Test
    public void testDelegatingKeyDeserializer() {
        StdKeyDeserializer serializer = new DelegatingKD(String.class, new SimpleStringDeserializer());
        Object key = serializer.deserializeKey("hello", new TestContext());
        assertEquals("hello", key);
    }

    @Test
    public void testStringFactoryKeyDeserializer() {
        StdKeyDeserializer serializer = new StringFactoryKeyDeserializer(MyStringFactory.class);
        Object key = serializer.deserializeKey("MyStringFactory:MyString:hello", new TestContext());
        assertEquals("hello", key);
    }

    @Test
    public void testStringCtorKeyDeserializer() {
        StdKeyDeserializer serializer = new StringCtorKeyDeserializer(MyStringCTOR.class);
        Object key = serializer.deserializeKey("MyStringCTOR:MyString(\"hello\")", new TestContext());
        assertEquals("hello", key);
    }

    private static class MyEnum {
        public static final MyEnum VAL_A = new MyEnum("A");
        public static final MyEnum VAL_B = new MyEnum("B");

        private final String name;

        MyEnum(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static class MyStringFactory {
        public static MyString create(String value) {
            return new MyString(value);
        }

        private static class MyString {
            private final String value;

            MyString(String value) {
                this.value = value;
            }

            @Override
            public String toString() {
                return value;
            }
        }
    }

    private static class MyStringCTOR {
        public static MyString create(String value) {
            return new MyString(value);
        }

        private static class MyString {
            private final String value;

            MyString(String value) {
                this.value = value;
            }
        }

        private static class TestContext extends TestBaseContext {}
    }

    private static class TestBaseContext extends DeserializationContext {
        @Override
        public Object findValueInMap(Map<String, Object> m, String key) throws IOException {
            return null;
        }

        @Override
        public Object findValueInMap(Map<?, Object> m, String key) throws IOException {
            return null;
        }

        @Override
        public Object findValueInMap(Object m, String key) throws IOException {
            return null;
        }

        @Override
        public Object findValueInMap(Map<?, ?> m, String key) throws IOException {
            return null;
        }

        @Override
        public Object findValueInMap(Object m, Iterator<?> i) throws IOException {
            return null;
        }

        @Override
        public Object parseValue(String text) throws IOException, JsonProcessingException {
            return null;
        }
    }
}