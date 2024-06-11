package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import org.junit.Test;
import static org.junit.Assert.*;

@JacksonStdImpl
public class StdKeyDeserializerTest {

    @Test
    public void testBooleanDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        Object result = deserializer.deserializeKey("true", new TestContext());
        assertTrue((Boolean) result);

        result = deserializer.deserializeKey("false", new TestContext());
        assertFalse((Boolean) result);

        result = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testByteDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
        Object result = deserializer.deserializeKey("127", new TestContext());
        assertEquals((Byte) new Byte((byte) 127), result);

        result = deserializer.deserializeKey("-128", new TestContext());
        assertEquals((Byte) new Byte((byte) -128), result);

        result = deserializer.deserializeKey("256", new TestContext());
        assertNull(result);
    }

    @Test
    public void testShortDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
        Object result = deserializer.deserializeKey("32767", new TestContext());
        assertEquals((Short) new Short((short) 32767), result);

        result = deserializer.deserializeKey("-32768", new TestContext());
        assertEquals((Short) new Short((short) -32768), result);

        result = deserializer.deserializeKey("65536", new TestContext());
        assertNull(result);
    }

    @Test
    public void testCharDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
        Object result = deserializer.deserializeKey("A", new TestContext());
        assertEquals((Character) 'A', result);

        result = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testIntDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        Object result = deserializer.deserializeKey("2147483647", new TestContext());
        assertEquals((Integer) new Integer(2147483647), result);

        result = deserializer.deserializeKey("-2147483648", new TestContext());
        assertEquals((Integer) new Integer(-2147483648), result);

        result = deserializer.deserializeKey("4294967296", new TestContext());
        assertNull(result);
    }

    @Test
    public void testLongDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
        Object result = deserializer.deserializeKey("9223372036854775807", new TestContext());
        assertEquals((Long) new Long(9223372036854775807L), result);

        result = deserializer.deserializeKey("-9223372036854775808", new TestContext());
        assertEquals((Long) new Long(-92233372036854775808L), result);

        result = deserializer.deserializeKey("18446744073709551616", new TestContext());
        assertNull(result);
    }

    @Test
    public void testFloatDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
        Object result = deserializer.deserializeKey("3.14159265", new TestContext());
        assertEquals((Float) new Float(3.14159265f), result);

        result = deserializer.deserializeKey("-1.79769313486231E38", new TestContext());
        assertEquals((Float) new Float(-1.79769313486231E38f), result);

        result = deserializer.deserializeKey("4.94066e-324", new TestContext());
        assertNull(result);
    }

    @Test
    public void testDoubleDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
        Object result = deserializer.deserializeKey("1.79769313486231E308", new TestContext());
        assertEquals((Double) new Double(1.79769313486231E308d), result);

        result = deserializer.deserializeKey("-4.94066e-324", new TestContext());
        assertEquals((Double) new Double(-4.94066e-324d), result);

        result = deserializer.deserializeKey("1.189731495342E-494", new TestContext());
        assertNull(result);
    }

    @Test
    public void testLocaleDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
        Object result = deserializer.deserializeKey("en_US", new TestContext());
        assertEquals(Locale.US, result);

        result = deserializer.deserializeKey("fr_FR", new TestContext());
        assertEquals(Locale.FRANCE, result);

        result = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testDateDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
        Object result = deserializer.deserializeKey("2001-12-31", new TestContext());
        assertEquals(new Date(102230400000L), result);

        result = deserializer.deserializeKey("1999-12-31T23:59:59.999Z", new TestContext());
        assertEquals(new Date(1153171299999L), result);

        result = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testCalendarDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
        Object result = deserializer.deserializeKey("2001-12-31", new TestContext());
        assertEquals(Calendar.getInstance().getTime(), ((Calendar) result).getTime());

        result = deserializer.deserializeKey("1999-12-31T23:59:59.999Z", new TestContext());
        assertEquals(Calendar.getInstance().getTime(), ((Calendar) result).getTime());

        result = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testUUIDDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);
        Object result = deserializer.deserializeKey("00000000-0000-0000-0000-000000000000", new TestContext());
        assertNotNull(result);
        UUID uuid = (UUID) result;
        assertTrue(uuid.version() == 0);

        result = deserializer.deserializeKey("11111111-1111-1111-1111-111111111111", new TestContext());
        assertNotNull(result);
        uuid = (UUID) result;
        assertTrue(uuid.version() == 1);

        result = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testURIDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
        Object result = deserializer.deserializeKey("http://test.com", new TestContext());
        assertEquals(new URI("http://test.com"), result);

        result = deserializer.deserializeKey("https://test.com:8080/path?query=param#fragment", new TestContext());
        assertEquals(new URI("https://test.com:8080/path?query=param#fragment"), result);

        result = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testURLDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
        Object result = deserializer.deserializeKey("http://test.com", new TestContext());
        assertEquals(new URL("http://test.com"), result);

        result = deserializer.deserializeKey("https://test.com:8080/path?query=param#fragment", new TestContext());
        assertEquals(new URL("https://test.com:8080/path?query=param#fragment"), result);

        result = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testClassDeserializer() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String[].class);
        Object result = deserializer.deserializeKey("[Ljava.lang.String;@12345", new TestContext());
        assertNotNull(result);
        assertArrayEquals(new String[]{"one", "two", "three"}, (String[]) result);

        result = deserializer.deserializeKey("java.util.ArrayList", new TestContext());
        assertInstanceOf(ArrayList.class, result);

        result = deserializer.deserializeKey("java.lang.String", new TestContext());
        assertEquals("Hello", result);

        result = deserializer.deserializeKey("java.util.HashMap", new TestContext());
        assertInstanceOf(HashMap.class, result);

        result = deserializer.deserializeKey("java.lang.Enum", new TestContext());
        assertNull(result);
    }

    @Test
    public void testEnumDeserializer() throws Exception {
        enum TestEnum {
            ONE, TWO, THREE
        }

        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(TestEnum.class, new EnumDeserializer());
        mapper.registerModule(module);

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(TestEnum.class);
        Object result = deserializer.deserializeKey("ONE", new TestContext());
        assertEquals(TestEnum.ONE, result);

        result = deserializer.deserializeKey("THREE", new TestContext());
        assertEquals(TestEnum.THREE, result);

        result = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    private static class TestContext extends DeserializationContext {
        TestContext() {
            super(null, null, new ObjectMapper(), null, null);
        }
    }
}