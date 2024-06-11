package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testStringKD() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.StringKD.forType(String.class);
        Object result = keyDeserializer.deserializeKey("test", new TestContext());
        assertEquals("test", result);
    }

    @Test
    public void testStringKD_Object() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.StringKD.forType(Object.class);
        Object result = keyDeserializer.deserializeKey("test", new TestContext());
        assertEquals("test", result);
    }

    @Test
    public void testBoolean() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        Object result = keyDeserializer.deserializeKey("true", new TestContext());
        assertTrue((Boolean) result);
        result = keyDeserializer.deserializeKey("false", new TestContext());
        assertFalse((Boolean) result);
    }

    @Test
    public void testByte() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);
        Object result = keyDeserializer.deserializeKey("10", new TestContext());
        assertEquals((byte) 10, result);
    }

    @Test
    public void testShort() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.class);
        Object result = keyDeserializer.deserializeKey("100", new TestContext());
        assertEquals((short) 100, result);
    }

    @Test
    public void testChar() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class);
        Object result = keyDeserializer.deserializeKey("A", new TestContext());
        assertEquals('A', result);
    }

    @Test
    public void testInt() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        Object result = keyDeserializer.deserializeKey("1000", new TestContext());
        assertEquals(1000, result);
    }

    @Test
    public void testLong() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        Object result = keyDeserializer.deserializeKey("1000000", new TestContext());
        assertEquals(1000000L, result);
    }

    @Test
    public void testFloat() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_FLOAT, Float.class);
        Object result = keyDeserializer.deserializeKey("1.23", new TestContext());
        assertEquals(1.23f, result, 0.001f);
    }

    @Test
    public void testDouble() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        Object result = keyDeserializer.deserializeKey("1.234", new TestContext());
        assertEquals(1.234, result, 0.0001);
    }

    @Test
    public void testLocale() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class, Locale.class.getDeserializer());
        Object result = keyDeserializer.deserializeKey("en_US", new TestContext());
        assertEquals(Locale.US, result);
    }

    @Test
    public void testCurrency() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CURRENCY, Currency.class, Currency.class.getDeserializer());
        Object result = keyDeserializer.deserializeKey("USD", new TestContext());
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testDate() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);
        Object result = keyDeserializer.deserializeKey("2022-01-01", new TestContext());
        assertNotNull(result);
        assertEquals("2022-01-01", new SimpleDateFormat("yyyy-MM-dd").format((Date) result));
    }

    @Test
    public void testCalendar() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CALENDAR, Calendar.class);
        Object result = keyDeserializer.deserializeKey("2022-01-01", new TestContext());
        assertNotNull(result);
        assertEquals("2022-01-01", ((GregorianCalendar) result).getDisplayName(GregorianCalendar.SHORT, GregorianCalendar.STANDALONE));
    }

    @Test
    public void testUUID() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_UUID, UUID.class);
        Object result = keyDeserializer.deserializeKey("123e4567-e89b-12d3-a456-426614174000", new TestContext());
        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), result);
    }

    @Test
    public void testURI() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);
        Object result = keyDeserializer.deserializeKey("http://test.com", new TestContext());
        assertEquals(new URI("http://test.com"), result);
    }

    @Test
    public void testURL() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
        Object result = keyDeserializer.deserializeKey("http://test.com", new TestContext());
        assertEquals(new URL("http://test.com"), result);
    }

    @Test
    public void testClass() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, String.class);
        Object result = keyDeserializer.deserializeKey("java.lang.String", new TestContext());
        assertEquals(String.class, result);
    }

    @Test
    public void testEnum() throws Exception {
        Enum<?>[] enumConstants = MyEnum.values();
        Class<MyEnum> enumClass = (Class<MyEnum>) enumConstants.getClass().getComponentType();
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_ENUM, enumClass, new EnumDeserializer());
        Object result = keyDeserializer.deserializeKey("VALUE_2", new TestContext());
        assertEquals(enumConstants[1], result);
    }

    @Test
    public void testUnknownEnumValue() throws Exception {
        Enum<?>[] enumConstants = MyEnum.values();
        Class<MyEnum> enumClass = (Class<MyEnum>) enumConstants.getClass().getComponentType();
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_ENUM, enumClass, new EnumDeserializer());
        Object result = keyDeserializer.deserializeKey("UNKNOWN_VALUE", new TestContext());
        assertNull(result);
    }

    @Test
    public void testUnknownEnumValueAsNull() throws Exception {
        Enum<?>[] enumConstants = MyEnum.values();
        Class<MyEnum> enumClass = (Class<MyEnum>) enumConstants.getClass().getComponentType();
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_ENUM, enumClass, new EnumDeserializer());
        Object result = keyDeserializer.deserializeKey("UNKNOWN_VALUE", new TestContext(true));
        assertNull(result);
    }

    @Test
    public void testDelegatingKD() throws Exception {
        JsonDeserializer<MyClass> deserializer = new SimpleDeserializer();
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(MyClass.class, deserializer);
        Object result = keyDeserializer.deserializeKey("test", new TestContext());
        assertEquals(new MyClass(), result);
    }

    @Test
    public void testStringCtorKeyDeserializer() throws Exception {
        Class<MyClass> cls = MyClass.class;
        Constructor<MyClass> constructor = cls.getDeclaredConstructor(String.class);
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(constructor);
        Object result = keyDeserializer.deserializeKey("test", new TestContext());
        assertEquals(new MyClass("test"), result);
    }

    @Test
    public void testStringFactoryKeyDeserializer() throws Exception {
        Class<MyClass> cls = MyClass.class;
        Method factoryMethod = cls.getDeclaredMethod("createInstance", String.class);
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(factoryMethod);
        Object result = keyDeserializer.deserializeKey("test", new TestContext());
        assertEquals(factoryMethod.invoke(null, "test"), result);
    }

    private static class TestContext extends DeserializationContext {
        TestContext() { this(false); }

        TestContext(boolean treatUnknownEnumValuesAsNull) {
            super(null, null, null);
            _inputFactory = new JsonTestInputFactory();
            _config = new ObjectMapper().getSerializationConfig();
            _config.setFeature(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, treatUnknownEnumValuesAsNull);
        }
    }

    private static class MyEnum {
        public static enum MyEnum { VALUE_1, VALUE_2 }
    }

    private static class MyClass {
        private MyClass(String s) {}

        public static MyClass createInstance(String s) {
            return new MyClass(s);
        }
    }

    private static class SimpleDeserializer extends JsonDeserializer<MyClass> {
        @Override
        public MyClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new MyClass(p.getValueAsString());
        }
    }
}