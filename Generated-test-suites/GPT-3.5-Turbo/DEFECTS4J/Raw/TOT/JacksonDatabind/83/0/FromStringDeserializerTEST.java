import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class FromStringDeserializerTest {

    @Test
    public void testDeserialize_File_ValidPath_SuccessfulDeserialization() {
        String path = "testFile.txt";
        FromStringDeserializer.Std fileDeserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        File result = (File) fileDeserializer._deserialize(path, null);
        assertNotNull(result);
    }

    @Test (expected = MalformedURLException.class)
    public void testDeserialize_URL_InvalidURL_ThrowMalformedURLException() throws MalformedURLException {
        String url = "invalid-url";
        FromStringDeserializer.Std urlDeserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        urlDeserializer._deserialize(url, null);
    }

    @Test
    public void testDeserialize_URL_ValidURL_SuccessfulDeserialization() throws MalformedURLException {
        String url = "http://www.example.com";
        FromStringDeserializer.Std urlDeserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        URL result = (URL) urlDeserializer._deserialize(url, null);
        assertNotNull(result);
    }

    @Test
    public void testDeserialize_URI_ValidURI_SuccessfulDeserialization() {
        String uri = "http://www.example.com";
        FromStringDeserializer.Std uriDeserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI result = (URI) uriDeserializer._deserialize(uri, null);
        assertNotNull(result);
    }

    @Test
    public void testDeserialize_Currency_ValidCurrencyCode_SuccessfulDeserialization() {
        String currencyCode = "USD";
        FromStringDeserializer.Std currencyDeserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        Currency result = (Currency) currencyDeserializer._deserialize(currencyCode, null);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_Currency_InvalidCurrencyCode_ThrowIllegalArgumentException() {
        String currencyCode = "AAA";
        FromStringDeserializer.Std currencyDeserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        currencyDeserializer._deserialize(currencyCode, null);
    }

    @Test
    public void testDeserialize_Pattern_ValidPatternPattern_SuccessfulDeserialization() {
        String pattern = "\\d+";
        FromStringDeserializer.Std patternDeserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        Pattern result = (Pattern) patternDeserializer._deserialize(pattern, null);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_Pattern_InvalidPattern_ThrowIllegalArgumentException() {
        String pattern = "(";
        FromStringDeserializer.Std patternDeserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);
        patternDeserializer._deserialize(pattern, null);
    }

    @Test
    public void testDeserialize_Locale_ValidLanguageTag_SuccessfulDeserialization() {
        String languageTag = "en";
        FromStringDeserializer.Std localeDeserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);
        Locale result = (Locale) localeDeserializer._deserialize(languageTag, null);
        assertNotNull(result);
    }

    @Test
    public void testDeserialize_TimeZone_ValidTimeZoneID_SuccessfulDeserialization() {
        String timeZoneID = "America/New_York";
        FromStringDeserializer.Std timeZoneDeserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        TimeZone result = (TimeZone) timeZoneDeserializer._deserialize(timeZoneID, null);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_TimeZone_InvalidTimeZoneID_ThrowIllegalArgumentException() {
        String timeZoneID = "Invalid";
        FromStringDeserializer.Std timeZoneDeserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);
        timeZoneDeserializer._deserialize(timeZoneID, null);
    }
}