import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.*;
import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.*;
import java.util.regex.Pattern;

public class FromStringDeserializerTest {

    @Test
    public void testTypes() {
        Class<?>[] types = FromStringDeserializer.types();
        assertNotNull(types);
        assertEquals(12, types.length);
    }

    @Test
    public void testFindDeserializer() {
        Std deserializer = FromStringDeserializer.findDeserializer(File.class);
        assertNotNull(deserializer);
        assertEquals(Std.STD_FILE, deserializer._kind);
    }

    @Test
    public void testDeserialize() {
        Std deserializer = new Std(File.class, Std.STD_FILE);
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
        try {
            File file = (File) deserializer.deserialize(new JsonFactory().createParser(new ByteArrayInputStream("\"test.txt\"".getBytes())), ctxt);
            assertEquals("test.txt", file.getName());
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testDeserializeFromEmptyString() {
        Std uriDeserializer = new Std(URI.class, Std.STD_URI);
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
        try {
            URI emptyUri = (URI) uriDeserializer.deserialize(new JsonFactory().createParser(new ByteArrayInputStream("\"\"".getBytes())), ctxt);
            assertEquals("", emptyUri.toString());
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testDeserializeSpecialCases() {
        Std localeDeserializer = new Std(Locale.class, Std.STD_LOCALE);
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
        try {
            Locale locale = (Locale) localeDeserializer.deserialize(new JsonFactory().createParser(new ByteArrayInputStream("\"en_US\"".getBytes())), ctxt);
            assertEquals("en_US", locale.toString());

            Locale rootLocale = (Locale) localeDeserializer.deserialize(new JsonFactory().createParser(new ByteArrayInputStream("\"\"".getBytes())), ctxt);
            assertEquals("", rootLocale.toString());

            Locale threePartLocale = (Locale) localeDeserializer.deserialize(new JsonFactory().createParser(new ByteArrayInputStream("\"en_US_posix\"".getBytes())), ctxt);
            assertEquals("en_US_POSIX", threePartLocale.toString());
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testDeserializeError() {
        Std deserializer = new Std(File.class, Std.STD_FILE);
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
        try {
            deserializer.deserialize(new JsonFactory().createParser(new ByteArrayInputStream("\"test.txt\"".getBytes())), ctxt);
            assertNotNull(deserializer.deserialize(new JsonFactory().createParser(new ByteArrayInputStream("123".getBytes())), ctxt));
        } catch (IOException e) {
            // Expect an IllegalArgumentException
            assertTrue(e instanceof IllegalArgumentException);
        }
    }
}