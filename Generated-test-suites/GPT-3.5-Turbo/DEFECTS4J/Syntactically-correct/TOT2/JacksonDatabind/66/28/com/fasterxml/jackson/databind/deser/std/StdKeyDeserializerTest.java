import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_NullKey() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        assertNull(stdKeyDeserializer.deserializeKey(null, ctxt));
    }

    @Test
    public void testDeserializeKey_InvalidKey() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        assertNull(stdKeyDeserializer.deserializeKey("invalid_key", ctxt));
    }

    @Test
    public void testDeserializeKey_ValidKey() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        assertEquals("valid_key", stdKeyDeserializer.deserializeKey("valid_key", ctxt));
    }

    @Test
    public void testParse_NullKey() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        assertNull(stdKeyDeserializer._parse(null, ctxt));
    }

    @Test
    public void testParse_InvalidKey() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        assertNull(stdKeyDeserializer._parse("invalid_key", ctxt));
    }

    @Test
    public void testParse_ValidKey() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        assertEquals("valid_key", stdKeyDeserializer._parse("valid_key", ctxt));
    }

    @Test
    public void testParse_EnumType() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_ENUM, Enum.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        assertNotNull(stdKeyDeserializer._parse("ENUM_VALUE", ctxt));
    }

    @Test
    public void testParse_BooleanType() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        assertEquals(false, stdKeyDeserializer._parse("false", ctxt));
        assertEquals(true, stdKeyDeserializer._parse("true", ctxt));
    }

    @Test
    public void testParse_ByteType() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        assertEquals(Byte.valueOf((byte) 123), stdKeyDeserializer._parse("123", ctxt));
    }

    @Test
    public void testParse_CharType() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        assertEquals(Character.valueOf('a'), stdKeyDeserializer._parse("a", ctxt));
    }

    @Test
    public void testParse_IntType() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        assertEquals(Integer.valueOf(123), stdKeyDeserializer._parse("123", ctxt));
    }

    @Test
    public void testParse_LongType() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        assertEquals(Long.valueOf(123L), stdKeyDeserializer._parse("123", ctxt));
    }

    @Test
    public void testParse_FloatType() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_FLOAT, Float.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        assertEquals(Float.valueOf(123.45f), stdKeyDeserializer._parse("123.45", ctxt));
    }

    @Test
    public void testParse_DoubleType() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        assertEquals(Double.valueOf(123.45), stdKeyDeserializer._parse("123.45", ctxt));
    }

    @Test
    public void testParse_LocaleType() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        FromStringDeserializer<?> deser = mock(FromStringDeserializer.class);
        when(deser._deserialize("en_US", ctxt)).thenReturn(Locale.US);

        StdKeyDeserializer localeKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class, deser);

        assertEquals(Locale.US, localeKeyDeserializer._parse("en_US", ctxt));
    }

    @Test
    public void testParse_CurrencyType() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CURRENCY, Currency.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        FromStringDeserializer<?> deser = mock(FromStringDeserializer.class);
        when(deser._deserialize("USD", ctxt)).thenReturn(Currency.getInstance("USD"));

        StdKeyDeserializer currencyKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CURRENCY, Currency.class, deser);

        assertEquals(Currency.getInstance("USD"), currencyKeyDeserializer._parse("USD", ctxt));
    }

    @Test
    public void testParse_DateType() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        assertNotNull(stdKeyDeserializer._parse("2022-01-01", ctxt));
    }

    @Test
    public void testParse_CalendarType() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CALENDAR, Calendar.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        Calendar calendar = (Calendar) stdKeyDeserializer._parse("2022-01-01", ctxt);
        assertNotNull(calendar);
        assertEquals(2022, calendar.get(Calendar.YEAR));
    }

    @Test
    public void testParse_UuidType() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_UUID, UUID.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        UUID uuid = (UUID) stdKeyDeserializer._parse("f47ac10b-58cc-4372-a567-0e02b2c3d479", ctxt);
        assertNotNull(uuid);
    }

    @Test
    public void testParse_UriType() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        URI uri = (URI) stdKeyDeserializer._parse("http://www.example.com", ctxt);
        assertNotNull(uri);
    }

    @Test
    public void testParse_UrlType() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        URL url = (URL) stdKeyDeserializer._parse("http://www.example.com", ctxt);
        assertNotNull(url);
    }

    @Test(expected = Exception.class)
    public void testParse_InvalidType() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(123, Integer.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        stdKeyDeserializer._parse("invalid_key", ctxt);
    }
}