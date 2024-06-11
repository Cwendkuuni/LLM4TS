import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testForType() throws Exception {
        Class<?>[] types = new Class[] {String.class, Object.class, UUID.class, Integer.class, Long.class,
                Date.class, Calendar.class, Boolean.class, Byte.class, Character.class, Short.class,
                Float.class, Double.class, URI.class, URL.class, Class.class, Locale.class, Currency.class};

        for (Class<?> type : types) {
            StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(type);
            assertEquals(type, keyDeserializer.getKeyClass());
        }
    }

    @Test
    public void testDeserializeKey() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        // Test with valid keys
        StdKeyDeserializer intDeserializer = StdKeyDeserializer.forType(Integer.class);
        Object key1 = intDeserializer.deserializeKey("1", ctxt);
        assertEquals(1, key1);

        StdKeyDeserializer stringDeserializer = StdKeyDeserializer.forType(String.class);
        Object key2 = stringDeserializer.deserializeKey("test", ctxt);
        assertEquals("test", key2);

        // Test with null key
        StdKeyDeserializer objectDeserializer = StdKeyDeserializer.forType(Object.class);
        Object key3 = objectDeserializer.deserializeKey(null, ctxt);
        assertNull(key3);

        // Test with invalid keys
        StdKeyDeserializer booleanDeserializer = StdKeyDeserializer.forType(Boolean.class);
        try {
            booleanKeyDeserializer.deserializeKey("invalid", ctxt);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("not a valid representation", e.getMessage());
        }

        // Test with Enum key
        JavaType enumType = TypeFactory.defaultInstance().constructType(Enum.class);
        StdKeyDeserializer enumDeserializer = StdKeyDeserializer.forType(enumType.getRawClass());
        Object key4 = enumDeserializer.deserializeKey("VALID_ENUM_VALUE", ctxt);
        assertTrue(key4 instanceof Enum);

        // Test with URL key
        StdKeyDeserializer urlDeserializer = StdKeyDeserializer.forType(URL.class);
        Object key5 = urlDeserializer.deserializeKey("http://test.com", ctxt);
        assertTrue(key5 instanceof URL);

        // Test with Class key
        StdKeyDeserializer classDeserializer = StdKeyDeserializer.forType(String.class);
        Class<?> key6Class = Class.forName("java.util.HashMap");
        Object key6 = classDeserializer.deserializeKey(key6Class.getName(), ctxt);
        assertEquals(key6Class, key6);
    }

    @Test
    public void testParse() throws Exception {
        StdKeyDeserializer intDeserializer = StdKeyDeserializer.forType(Integer.class);

        // Test with valid integers
        assertEquals(1, intDeserializer._parse("1", new DeserializationContext() {}));
        assertEquals(-1, intDeserializer._parse("-1", new DeserializationContext() {}));
        assertEquals(0, intDeserializer._parse("0", new DeserializationContext() {}));

        // Test with invalid integers
        try {
            intDeserializer._parse("invalid", new DeserializationContext() {});
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testStringKD() throws Exception {
        StdKeyDeserializer stringKD = StdKeyDeserializer.StringKD.forType(String.class);
        Object key1 = stringKD.deserializeKey("test", new DeserializationContext() {});
        assertEquals("test", key1);
    }

    @Test
    public void testDelegatingKD() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        JsonDeserializer<Object> deserializer = mapper.findValueDeserializer(Map.class, ctxt);
        StdKeyDeserializer delegatingKD = new StdKeyDeserializer(null, Object.class, deserializer);
        Object key1 = delegatingKD.deserializeKey("{\"key\": \"value\"}", ctxt);
        assertTrue(key1 instanceof Map);
    }

    @Test
    public void testEnumKD() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ctxt.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);

        JavaType enumType = TypeFactory.defaultInstance().constructType(MyEnum.class);
        StdKeyDeserializer enumKD = StdKeyDeserializer.EnumKD.forType(enumType.getRawClass(),
                mapper.getAnnotationIntrospector().findMethodAnnotations(StdKeyDeserializer.class,
                        MyEnum.class.getDeclaredMethod("valueOf"), null));
        Object key1 = enumKD.deserializeKey("MyEnum.VALID_ENUM_VALUE", ctxt);
        assertTrue(key1 instanceof MyEnum);

        Object key2 = enumKD.deserializeKey("InvalidEnumValue", ctxt);
        assertNull(key2);
    }

    @Test
    public void testStringCtorKeyDeserializer() throws Exception {
        Class<?> keyClass = String[].class;
        Constructor<String[]> constructor = keyClass.getDeclaredConstructor(String[].class);
        StdKeyDeserializer stringCtorKeyDeserializer = new StdKeyDeserializer(constructor);
        Object key1 = stringCtorKeyDeserializer.deserializeKey(new Object[]{"test1", "test2"}, new DeserializationContext() {});
        assertArrayEquals(new String[]{"test1", "test2"}, key1);
    }

    @Test
    public void testStringFactoryKeyDeserializer() throws Exception {
        Class<?> keyClass = StringBuffer.class;
        Method factoryMethod = keyClass.getDeclaredMethod("valueOf", String[].class);
        StdKeyDeserializer stringFactoryKeyDeserializer = new StdKeyDeserializer(factoryMethod);
        Object key1 = stringFactoryKeyDeserializer.deserializeKey(new Object[]{"test1", "test2"}, new DeserializationContext() {});
        assertInstanceOf(StringBuffer.class, key1);
    }

    private static void assertInstanceOf(Class<?> expectedClass, Object actualObject) {
        assertTrue("Expected an instance of " + expectedClass.getName() +
                ", but got an instance of " + actualObject.getClass().getName() + ".",
                expectedClass.isInstance(actualObject));
    }

    private static void assertArrayEquals(Object expectedArray, Object actualArray) {
        if (expectedArray.getClass() != actualArray.getClass()) {
            fail("Expected array of class " + expectedArray.getClass().getName() +
                    ", but got array of class " + actualArray.getClass().getName() + ".");
        }
        Object[] expected = (Object[])expectedArray;
        Object[] actual = (Object[])actualArray;
        if (expected.length != actual.length) {
            fail("Expected array of length " + expected.length + ", but got array of length " + actual.length + ".");
        }
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

    private static class MyEnum {
        public static MyEnum VALID_ENUM_VALUE;

        MyEnum(String name) {
            this.name = name;
        }

        private String name;

        public String toString() {
            return name;
        }
    }
}