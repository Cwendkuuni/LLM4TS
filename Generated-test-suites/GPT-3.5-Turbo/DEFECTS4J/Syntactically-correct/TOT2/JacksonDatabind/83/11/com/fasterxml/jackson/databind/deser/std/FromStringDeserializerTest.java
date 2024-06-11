import static org.junit.Assert.*;

import org.junit.Test;

public class FromStringDeserializerTest {

    @Test
    public void testTypes() {
        FromStringDeserializer<?> deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        assertNotNull(deserializer);

        Class<?>[] expectedTypes = { File.class, URL.class, URI.class, Class.class, JavaType.class, Currency.class, Pattern.class, Locale.class, Charset.class, TimeZone.class, InetAddress.class, InetSocketAddress.class, StringBuilder.class };

        assertArrayEquals(expectedTypes, FromStringDeserializer.types());
    }

    @Test
    public void testFindDeserializer() {
        Std deserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(File.class);
        assertNotNull(deserializer);
        assertEquals(FromStringDeserializer.Std.STD_FILE, deserializer._kind);

        deserializer = (FromStringDeserializer.Std) FromStringDeserializer.findDeserializer(JavaType.class);
        assertNotNull(deserializer);
        assertEquals(FromStringDeserializer.Std.STD_JAVA_TYPE, deserializer._kind);
    }

    @Test
    public void testDeserializeFromEmptyString() {
        Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        assertNotNull(deserializer);

        assertEquals(URI.create(""), deserializer._deserializeFromEmptyString());
    }

    // Add more test cases for other methods

}