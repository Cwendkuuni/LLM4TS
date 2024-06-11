package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.regex.Pattern;
import com.fasterxml.jackson.databind.JsonMappingException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FromStringDeserializerTest {

    @Test
    public void deserializeFile() throws Exception {
        String filePath = "/path/to/file.txt";
        File expectedFile = new File(filePath);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        FromStringDeserializer.Std fileDeserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);

        when(jsonParser.getValueAsString()).thenReturn(filePath);

        File deserializedFile = fileDeserializer.deserialize(jsonParser, deserializationContext);

        assertEquals(expectedFile, deserializedFile);
    }

    @Test
    public void deserializeURL() throws Exception {
        String urlString = "http://www.example.com";
        URL expectedURL = new URL(urlString);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        FromStringDeserializer.Std urlDeserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);

        when(jsonParser.getValueAsString()).thenReturn(urlString);

        URL deserializedURL = urlDeserializer.deserialize(jsonParser, deserializationContext);

        assertEquals(expectedURL, deserializedURL);
    }

    @Test
    public void deserializeURI() throws Exception {
        String uriString = "http://www.example.com";
        URI expectedURI = new URI(uriString);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        FromStringDeserializer.Std uriDeserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);

        when(jsonParser.getValueAsString()).thenReturn(uriString);

        URI deserializedURI = uriDeserializer.deserialize(jsonParser, deserializationContext);

        assertEquals(expectedURI, deserializedURI);
    }

    @Test
    public void deserializeClass() throws Exception {
        String className = "java.lang.String";
        Class<?> expectedClass = Class.forName(className);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        FromStringDeserializer.Std classDeserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);

        when(jsonParser.getValueAsString()).thenReturn(className);

        Class<?> deserializedClass = classDeserializer.deserialize(jsonParser, deserializationContext);

        assertEquals(expectedClass, deserializedClass);
    }

    @Test
    public void deserializeCurrency() throws Exception {
        String currencyCode = "USD";
        Currency expectedCurrency = Currency.getInstance(currencyCode);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        FromStringDeserializer.Std currencyDeserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);

        when(jsonParser.getValueAsString()).thenReturn(currencyCode);

        Currency deserializedCurrency = currencyDeserializer.deserialize(jsonParser, deserializationContext);

        assertEquals(expectedCurrency, deserializedCurrency);
    }

    @Test
    public void deserializePattern() throws Exception {
        String patternString = "\\d+";
        Pattern expectedPattern = Pattern.compile(patternString);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        FromStringDeserializer.Std patternDeserializer = new FromStringDeserializer.Std(Pattern.class, FromStringDeserializer.Std.STD_PATTERN);

        when(jsonParser.getValueAsString()).thenReturn(patternString);

        Pattern deserializedPattern = patternDeserializer.deserialize(jsonParser, deserializationContext);

        assertEquals(expectedPattern, deserializedPattern);
    }

    @Test
    public void deserializeLocale() throws Exception {
        String localeString = "en-US";
        Locale expectedLocale = new Locale(localeString);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        FromStringDeserializer.Std localeDeserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);

        when(jsonParser.getValueAsString()).thenReturn(localeString);

        Locale deserializedLocale = localeDeserializer.deserialize(jsonParser, deserializationContext);

        assertEquals(expectedLocale, deserializedLocale);
    }

    @Test
    public void deserializeCharset() throws Exception {
        String charsetString = "UTF-8";
        Charset expectedCharset = Charset.forName(charsetString);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        FromStringDeserializer.Std charsetDeserializer = new FromStringDeserializer.Std(Charset.class, FromStringDeserializer.Std.STD_CHARSET);

        when(jsonParser.getValueAsString()).thenReturn(charsetString);

        Charset deserializedCharset = charsetDeserializer.deserialize(jsonParser, deserializationContext);

        assertEquals(expectedCharset, deserializedCharset);
    }

    @Test
    public void deserializeTimeZone() throws Exception {
        String timeZoneString = "UTC";
        TimeZone expectedTimeZone = TimeZone.getTimeZone(timeZoneString);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        FromStringDeserializer.Std timeZoneDeserializer = new FromStringDeserializer.Std(TimeZone.class, FromStringDeserializer.Std.STD_TIME_ZONE);

        when(jsonParser.getValueAsString()).thenReturn(timeZoneString);

        TimeZone deserializedTimeZone = timeZoneDeserializer.deserialize(jsonParser, deserializationContext);

        assertEquals(expectedTimeZone, deserializedTimeZone);
    }

    @Test
    public void deserializeInetAddress() throws Exception {
        String inetAddressString = "127.0.0.1";
        InetAddress expectedInetAddress = InetAddress.getByName(inetAddressString);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        FromStringDeserializer.Std inetAddressDeserializer = new FromStringDeserializer.Std(InetAddress.class, FromStringDeserializer.Std.STD_INET_ADDRESS);

        when(jsonParser.getValueAsString()).thenReturn(inetAddressString);

        InetAddress deserializedInetAddress = inetAddressDeserializer.deserialize(jsonParser, deserializationContext);

        assertEquals(expectedInetAddress, deserializedInetAddress);
    }

    @Test
    public void deserializeInetSocketAddress() throws Exception {
        String inetSocketAddressString = "localhost:8080";
        InetSocketAddress expectedInetSocketAddress = new InetSocketAddress(inetSocketAddressString, 8080);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        FromStringDeserializer.Std inetSocketAddressDeserializer = new FromStringDeserializer.Std(InetSocketAddress.class, FromStringDeserializer.Std.STD_INET_SOCKET_ADDRESS);

        when(jsonParser.getValueAsString()).thenReturn(inetSocketAddressString);

        InetSocketAddress deserializedInetSocketAddress = inetSocketAddressDeserializer.deserialize(jsonParser, deserializationContext);

        assertEquals(expectedInetSocketAddress, deserializedInetSocketAddress);
    }

    @Test
    public void deserializeStringBuilder() throws Exception {
        String stringBuilderString = "test";
        StringBuilder expectedStringBuilder = new StringBuilder(stringBuilderString);
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        FromStringDeserializer.Std stringBuilderDeserializer = new FromStringDeserializer.Std(StringBuilder.class, FromStringDeserializer.Std.STD_STRING_BUILDER);

        when(jsonParser.getValueAsString()).thenReturn(stringBuilderString);

        StringBuilder deserializedStringBuilder = stringBuilderDeserializer.deserialize(jsonParser, deserializationContext);

        assertEquals(expectedStringBuilder, deserializedStringBuilder);
    }
}