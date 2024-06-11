import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class StdKeyDeserializerTest {

    @Test
    public void testBooleanType() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        Object deserializedKey = keyDeserializer.deserializeKey("true", new TestContext());
        assertEquals(Boolean.TRUE, deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("false", new TestContext());
        assertEquals(Boolean.FALSE, deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testByteType() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
        Object deserializedKey = keyDeserializer.deserializeKey("0", new TestContext());
        assertEquals((Byte) 0, deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("127", new TestContext());
        assertEquals((Byte) 127, deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("-128", new TestContext());
        assertEquals((Byte) -128, deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("256", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testShortType() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
        Object deserializedKey = keyDeserializer.deserializeKey("0", new TestContext());
        assertEquals((Short) 0, deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("32767", new TestContext());
        assertEquals((Short) 32767, deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("-32768", new TestContext());
        assertEquals((Short) -32768, deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("65536", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testCharType() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
        Object deserializedKey = keyDeserializer.deserializeKey("a", new TestContext());
        assertEquals((Character) 'a', deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("A", new TestContext());
        assertEquals((Character) 'A', deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testIntType() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        Object deserializedKey = keyDeserializer.deserializeKey("0", new TestContext());
        assertEquals((Integer) 0, deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("2147483647", new TestContext());
        assertEquals((Integer) 2147483647, deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("-2147483648", new TestContext());
        assertEquals((Integer) -2147483648, deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("4611686018427387903", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testLongType() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
        Object deserializedKey = keyDeserializer.deserializeKey("0", new TestContext());
        assertEquals((Long) 0, deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("9223372036854775807", new TestContext());
        assertEquals((Long) 9223372036854775807L, deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("-9223372036854775808", new TestContext());
        assertEquals((Long) -9223372036854775808L, deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("18446744073709551616", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testLocaleType() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
        Object deserializedKey = keyDeserializer.deserializeKey("en_US", new TestContext());
        assertEquals(Locale.US, deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("de_DE", new TestContext());
        assertEquals(Locale.GERMAN, deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testDateType() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);
        Object deserializedKey = keyDeserializer.deserializeKey("2022-01-01", new TestContext());
        assertNotNull(deserializedKey);
    }

    @Test(expected = IOException.class)
    public void testDateTypeInvalidFormat() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);
        keyDeserializer.deserializeKey("invalid", new TestContext());
    }

    @Test
    public void testURLType() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
        Object deserializedKey = keyDeserializer.deserializeKey("http://www.example.com", new TestContext());
        assertNotNull(deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("https://www.example.org", new TestContext());
        assertNotNull(deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("invalid", new TestContext());
        assertNull(deserializedKey);
    }

    @Test(expected = MalformedURLException.class)
    public void testURLTypeInvalidURL() throws Exception {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
        keyDeserializer.deserializeKey("invalid", new TestContext());
    }

    @Test
    public void testEnumType() throws Exception {
        Enum enumType = MyEnum.FOO;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(enumType.getDeclaringClass());
        Object deserializedKey = keyDeserializer.deserializeKey("FOO", new TestContext());
        assertEquals(enumType.FOO, deserializedKey);

        deserializedKey = keyDeserializer.deserializeKey("BAR", new TestContext());
        assertNull(deserializedKey);
    }

    @Test(expected = JsonProcessingException.class)
    public void testEnumTypeInvalidValue() throws Exception {
        Enum enumType = MyEnum.FOO;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(enumType.getDeclaringClass());
        keyDeserializer.deserializeKey("invalid", new TestContext());
    }
}