import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class FromStringDeserializerTest {

    private FromStringDeserializer.Std deserializer;

    @Before
    public void setup() {
        deserializer = new FromStringDeserializer.Std(String.class, 0);
    }

    @Test
    public void testDeserialize_File_ValidPath_Success() {
        String filePath = "path/to/file.txt";
        File file = (File) deserializer._deserialize(filePath, null);
        assertNotNull(file);
        assertEquals(filePath, file.getPath());
    }

    @Test
    public void testDeserialize_File_EmptyPath_ReturnNull() {
        String filePath = "";
        File file = (File) deserializer._deserialize(filePath, null);
        assertNull(file);
    }

    @Test
    public void testDeserialize_URL_ValidURL_Success() throws Exception {
        String urlString = "http://example.com";
        URL url = (URL) deserializer._deserialize(urlString, null);
        assertNotNull(url);
        assertEquals(urlString, url.toString());
    }

    @Test
    public void testDeserialize_URL_InvalidURL_ThrowMalformedURLException() {
        String urlString = "invalid url";
        try {
            deserializer._deserialize(urlString, null);
        } catch (Exception e) {
            assertTrue(e instanceof MalformedURLException);
            assertEquals("no protocol: invalid url", e.getMessage());
        }
    }

    @Test
    public void testDeserialize_URI_ValidURI_Success() {
        String uriString = "http://example.com";
        URI uri = (URI) deserializer._deserialize(uriString, null);
        assertNotNull(uri);
        assertEquals(uriString, uri.toString());
    }

    @Test
    public void testDeserialize_Class_ValidClassName_Success() throws ClassNotFoundException {
        String className = "java.lang.String";
        Class<?> clazz = (Class<?>) deserializer._deserialize(className, null);
        assertNotNull(clazz);
        assertEquals(className, clazz.getName());
    }

    @Test
    public void testDeserialize_Class_InvalidClassName_ThrowClassNotFoundException() {
        String className = "non.existent.Class";
        try {
            deserializer._deserialize(className, null);
        } catch (Exception e) {
            assertTrue(e instanceof ClassNotFoundException);
            assertEquals("non.existent.Class", e.getMessage());
        }
    }

    @Test
    public void testDeserialize_Currency_ValidCurrency_Success() {
        String currencyCode = "USD";
        Currency currency = (Currency) deserializer._deserialize(currencyCode, null);
        assertNotNull(currency);
        assertEquals(currencyCode, currency.getCurrencyCode());
    }

    @Test
    public void testDeserialize_Currency_InvalidCurrencyCode_ThrowIllegalArgumentException() {
        String currencyCode = "INVALID";
        try {
            deserializer._deserialize(currencyCode, null);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
            assertEquals("Unknown currency code: INVALID", e.getMessage());
        }
    }

    @Test
    public void testDeserialize_Pattern_ValidPattern_Success() {
        String regex = "[a-z]+";
        Pattern pattern = (Pattern) deserializer._deserialize(regex, null);
        assertNotNull(pattern);
        assertEquals(regex, pattern.pattern());
    }

    @Test
    public void testDeserialize_Pattern_InvalidRegex_ThrowPatternSyntaxException() {
        String regex = "(abc";
        try {
            deserializer._deserialize(regex, null);
        } catch (Exception e) {
            assertTrue(e instanceof PatternSyntaxException);
            assertEquals("Unclosed group near index 4\n" +
                    "(abc\n" +
                    "    ^", e.getMessage());
        }
    }

    @Test
    public void testDeserialize_Locale_ValidLanguageCode_Success() {
        String languageCode = "en";
        Locale locale = (Locale) deserializer._deserialize(languageCode, null);
        assertNotNull(locale);
        assertEquals(languageCode, locale.getLanguage());
        assertEquals("", locale.getCountry());
    }

    @Test
    public void testDeserialize_Locale_LanguageAndCountryCode_Success() {
        String languageCode = "en";
        String countryCode = "US";
        String localeString = languageCode + "_" + countryCode;
        Locale locale = (Locale) deserializer._deserialize(localeString, null);
        assertNotNull(locale);
        assertEquals(languageCode, locale.getLanguage());
        assertEquals(countryCode, locale.getCountry());
    }

    @Test
    public void testDeserialize_Locale_InvalidLocale_ThrowIllegalArgumentException() {
        String localeString = "invalid";
        try {
            deserializer._deserialize(localeString, null);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
            assertEquals("Invalid locale format: invalid", e.getMessage());
        }
    }

    @Test
    public void testDeserialize_Charset_ValidCharset_Success() {
        String charsetName = "UTF-8";
        Charset charset = (Charset) deserializer._deserialize(charsetName, null);
        assertNotNull(charset);
        assertEquals(charsetName, charset.name());
    }

    @Test
    public void testDeserialize_Charset_InvalidCharset_ThrowUnsupportedCharsetException() {
        String charsetName = "INVALID";
        try {
            deserializer._deserialize(charsetName, null);
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedCharsetException);
            assertEquals("INVALID", e.getMessage());
        }
    }

    @Test
    public void testDeserialize_TimeZone_ValidTimeZoneId_Success() {
        String timeZoneId = "America/New_York";
        TimeZone timeZone = (TimeZone) deserializer._deserialize(timeZoneId, null);
        assertNotNull(timeZone);
        assertEquals(timeZoneId, timeZone.getID());
    }

    @Test
    public void testDeserialize_TimeZone_InvalidTimeZoneId_ThrowIllegalArgumentException() {
        String timeZoneId = "INVALID";
        try {
            deserializer._deserialize(timeZoneId, null);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
            assertEquals("Unknown time zone ID: INVALID", e.getMessage());
        }
    }

    @Test
    public void testDeserialize_StringBuilder_ValidValue_Success() {
        String value = "test";
        StringBuilder stringBuilder = (StringBuilder) deserializer._deserialize(value, null);
        assertNotNull(stringBuilder);
        assertEquals(value, stringBuilder.toString());
    }

    @Test
    public void testDeserialize_EmptyString_URIType_ReturnEmptyURI() {
        deserializer = new FromStringDeserializer.Std(URI.class, 0);
        Object result = deserializer._deserialize("", null);
        assertNotNull(result);
        assertTrue(result instanceof URI);
        assertEquals("", result.toString());
    }

    @Test
    public void testDeserialize_EmptyString_LocaleType_ReturnRootLocale() {
        deserializer = new FromStringDeserializer.Std(Locale.class, 0);
        Object result = deserializer._deserialize("", null);
        assertNotNull(result);
        assertTrue(result instanceof Locale);
        assertEquals(Locale.ROOT, result);
    }

    @Test
    public void testDeserialize_EmptyString_StringBuilderType_ReturnEmptyStringBuilder() {
        deserializer = new FromStringDeserializer.Std(StringBuilder.class, 0);
        Object result = deserializer._deserialize("", null);
        assertNotNull(result);
        assertTrue(result instanceof StringBuilder);
        assertEquals("", result.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_EmptyString_UnknownType_ThrowIllegalArgumentException() {
        deserializer._deserialize("", null);
    }
}