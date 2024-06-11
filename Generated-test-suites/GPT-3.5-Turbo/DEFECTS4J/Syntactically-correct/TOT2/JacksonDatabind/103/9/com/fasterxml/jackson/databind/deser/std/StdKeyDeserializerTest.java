import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonProcessingException;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.util.FromStringDeserializer;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        DeserializationContext ctxt = null;
        
        // Typical use case
        assertEquals(Boolean.TRUE, deserializer.deserializeKey("true", ctxt));
        
        // Edge case - invalid boolean value
        assertEquals(null, deserializer.deserializeKey("invalid", ctxt));
    }

    @Test
    public void testForType() {
        Class<?> raw = String.class;
        
        // Typical use case
        StdKeyDeserializer result = StdKeyDeserializer.forType(raw);
        assertNotNull(result);
        
        // Edge case - unknown type
        raw = StdKeyDeserializer.class;
        assertNull(StdKeyDeserializer.forType(raw));
    }

    @Test
    public void test_parse() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext ctxt = null;
        
        // Typical use case
        assertEquals(10, deserializer._parse("10", ctxt));
        
        // Edge case - invalid integer value
        Exception exception = assertThrows(IOException.class, () -> deserializer._parse("invalid", ctxt));
        assertEquals("For input string: \"invalid\"", exception.getMessage());
    }

    @Test
    public void test_weirdKey() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);
        DeserializationContext ctxt = null;
        
        // Typical use case
        assertEquals(ctxt.handleWeirdKey(URI.class, "invalid uri", "problem: null"), deserializer._weirdKey(ctxt, "invalid uri", null));
    }

    @Test
    public void testDeserializeKey_Class() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, Class.class);
        DeserializationContext ctxt = null;
        
        // Typical use case
        assertEquals(String.class, deserializer.deserializeKey("java.lang.String", ctxt));
        
        // Edge case - invalid class name
        Exception exception = assertThrows(ClassNotFoundException.class, () -> deserializer.deserializeKey("invalid.class.name", ctxt));
        assertEquals("invalid.class.name", exception.getMessage());
    }

    @Test
    public void testDeserializeKey_Enum() throws IOException {
        EnumKD deserializer = new EnumKD(null, null);
        DeserializationContext ctxt = null;

        // Typical use case
        assertEquals(TestEnum.ONE, deserializer._parse("ONE", ctxt));

        // Edge case - unknown value
        assertEquals(null, deserializer._parse("UNKNOWN", ctxt));
    }

    @Test
    public void testDeserializeKey_URL() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
        DeserializationContext ctxt = null;

        // Typical use case
        assertNotNull(deserializer.deserializeKey("http://www.example.com", ctxt));

        // Edge case - invalid URL
        Exception exception = assertThrows(MalformedURLException.class, () -> deserializer.deserializeKey("invalid url", ctxt));
        assertEquals("no protocol: invalid url", exception.getMessage());
    }

    @Test
    public void testDeserializeKey_Currency() throws IOException {
        FromStringDeserializer<Currency> currencyDeserializer = new FromStringDeserializer<Currency>(Currency.class);
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CURRENCY, Currency.class, currencyDeserializer);
        DeserializationContext ctxt = null;

        // Typical use case
        assertEquals(Currency.getInstance("USD"), deserializer.deserializeKey("USD", ctxt));

        // Edge case - invalid currency
        Exception exception = assertThrows(IllegalArgumentException.class, () -> deserializer.deserializeKey("invalid", ctxt));
        assertEquals("Currency unknown", exception.getMessage());
    }

    enum TestEnum {
        ONE, TWO, THREE
    }

}