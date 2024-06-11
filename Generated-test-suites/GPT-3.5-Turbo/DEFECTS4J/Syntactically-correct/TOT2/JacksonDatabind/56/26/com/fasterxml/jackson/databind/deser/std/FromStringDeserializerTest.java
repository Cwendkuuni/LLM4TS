import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class FromStringDeserializerTest {

    @Test
    public void testTypes() {
        Class<?>[] types = FromStringDeserializer.types();
        assertNotNull(types);
        assertEquals(12, types.length);
        assertTrue(types[0].equals(File.class));
        assertTrue(types[1].equals(URL.class));
        assertTrue(types[2].equals(URI.class));
        assertTrue(types[3].equals(Class.class));
        assertTrue(types[4].equals(JavaType.class));
        assertTrue(types[5].equals(Currency.class));
        assertTrue(types[6].equals(Pattern.class));
        assertTrue(types[7].equals(Locale.class));
        assertTrue(types[8].equals(Charset.class));
        assertTrue(types[9].equals(TimeZone.class));
        assertTrue(types[10].equals(InetAddress.class));
        assertTrue(types[11].equals(InetSocketAddress.class));
    }

    @Test
    public void testFindDeserializer() {
        Std deserializer = FromStringDeserializer.findDeserializer(URI.class);
        assertNotNull(deserializer);
        assertEquals(Std.STD_URI, deserializer._kind);

        deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        assertNotNull(deserializer);
        assertEquals(Std.STD_CURRENCY, deserializer._kind);

        deserializer = FromStringDeserializer.findDeserializer(String.class);
        assertNull(deserializer);
    }

    @Test
    public void testDeserialize() throws IOException {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(URI.class, Std.STD_URI);
        DeserializationContext ctxt = new DeserializationContext(null, null);
        JsonParser p = Mockito.mock(JsonParser.class);
        Mockito.when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(p.getValueAsString()).thenReturn("http://example.com");

        URI uri = (URI) std.deserialize(p, ctxt);
        assertEquals(URI.create("http://example.com"), uri);
    }

    @Test
    public void testDeserialize_EmptyString() throws IOException {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(URI.class, Std.STD_URI);
        DeserializationContext ctxt = new DeserializationContext(null, null);
        JsonParser p = Mockito.mock(JsonParser.class);
        Mockito.when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(p.getValueAsString()).thenReturn("");

        URI uri = (URI) std.deserialize(p, ctxt);
        assertEquals(uri, URI.create(""));
    }

    @Test
    public void testDeserialize_Null() throws IOException {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(URI.class, Std.STD_URI);
        DeserializationContext ctxt = new DeserializationContext(null, null);
        JsonParser p = Mockito.mock(JsonParser.class);
        Mockito.when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        URI uri = (URI) std.deserialize(p, ctxt);
        assertNull(uri);
    }

    @Test(expected = Exception.class)
    public void testDeserialize_InvalidInput() throws IOException {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(URI.class, Std.STD_URI);
        DeserializationContext ctxt = new DeserializationContext(null, null);
        JsonParser p = Mockito.mock(JsonParser.class);
        Mockito.when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(p.getValueAsString()).thenReturn("invalid");

        std.deserialize(p, ctxt);
    }

    @Test
    public void testDeserialize_EmbeddedObject() throws IOException {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(File.class, Std.STD_FILE);
        DeserializationContext ctxt = new DeserializationContext(null, null);
        JsonParser p = Mockito.mock(JsonParser.class);
        Mockito.when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_EMBEDDED_OBJECT);
        Mockito.when(p.getEmbeddedObject()).thenReturn(new File("test.txt"));

        File file = (File) std.deserialize(p, ctxt);
        assertEquals(file, new File("test.txt"));
    }

    @Test(expected = Exception.class)
    public void testDeserialize_UnknownType() throws IOException {
        FromStringDeserializer.Std std = new FromStringDeserializer.Std(String.class, 0);
        DeserializationContext ctxt = new DeserializationContext(null, null);
        JsonParser p = Mockito.mock(JsonParser.class);
        Mockito.when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(p.getValueAsString()).thenReturn("test");

        std.deserialize(p, ctxt);
    }

}