package com.example.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

    @Test
    public void testDeserializeStringFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        FileDeserializer fileDeserializer = new FileDeserializer(File.class);

        String fileString = "path/to/file.txt";
        JsonParser parser = new JsonFactory().createParser(new StringReader(fileString));
        parser.nextToken();

        T result = fileDeserializer.deserialize(parser, ctxt);
        File expectedFile = new File(fileString);
        assertEquals(expectedFile, result);
    }

    @Test
    public void testDeserializeStringURL() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        URLDeserializer urlDeserializer = new URLDeserializer(URL.class);

        String urlString = "http://www.example.com";
        JsonParser parser = new JsonFactory().createParser(new StringReader(urlString));
        parser.nextToken();

        T result = urlDeserializer.deserialize(parser, ctxt);
        URL expectedURL = new URL(urlString);
        assertEquals(expectedURL, result);
    }

    @Test
    public void testDeserializeStringURI() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        URIDeserializer uriDeserializer = new URIDeserializer(URI.class);

        String uriString = "http://www.example.com/path/to/file.txt";
        JsonParser parser = new JsonFactory().createParser(new StringReader(uriString));
        parser.nextToken();

        T result = uriDeserializer.deserialize(parser, ctxt);
        URI expectedURI = new URI(uriString);
        assertEquals(expectedURI, result);
    }

    @Test
    public void testDeserializeStringClass() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        ClassDeserializer classDeserializer = new ClassDeserializer(Class.class);

        String className = "java.lang.String";
        JsonParser parser = new JsonFactory().createParser(new StringReader(className));
        parser.nextToken();

        T result = classDeserializer.deserialize(parser, ctxt);
        Class<?> expectedClass = Class.forName(className);
        assertEquals(expectedClass, result.getClass());
    }

    @Test
    public void testDeserializeStringJavaType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JavaTypeDeserializer javaTypeDeserializer = new JavaTypeDeserializer(JavaType.class);

        String jsonTypeString = "com.example.MyClass";
        JsonParser parser = new JsonFactory().createParser(new StringReader(jsonTypeString));
        parser.nextToken();

        JavaType expectedJavaType = mapper.getTypeFactory().constructType(ClassUtil.loadClass(jsonTypeString, mapper));
        JavaType result = (JavaType) javaTypeDeserializer.deserialize(parser, ctxt);
        assertEquals(expectedJavaType, result);
    }

    @Test
    public void testDeserializeStringCurrency() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        CurrencyDeserializer currencyDeserializer = new CurrencyDeserializer(Currency.class);

        String currencyString = "$10.00";
        JsonParser parser = new JsonFactory().createParser(new StringReader(currencyString));
        parser.nextToken();

        Currency expectedCurrency = Currency.getInstance("USD");
        Currency result = currencyDeserializer.deserialize(parser, ctxt);
        assertEquals(expectedCurrency, result);
    }

    @Test
    public void testDeserializeStringPattern() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        PatternDeserializer patternDeserializer = new PatternDeserializer(Pattern.class);

        String patternString = "^[a-zA-Z]+";
        JsonParser parser = new JsonFactory().createParser(new StringReader(patternString));
        parser.nextToken();

        Pattern expectedPattern = Pattern.compile(patternString);
        Pattern result = patternDeserializer.deserialize(parser, ctxt);
        assertEquals(expectedPattern, result);
    }

    @Test
    public void testDeserializeStringLocale() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        LocaleDeserializer localeDeserializer = new LocaleDeserializer(Locale.class);

        String localeString = "fr_FR";
        JsonParser parser = new JsonFactory().createParser(new StringReader(localeString));
        parser.nextToken();

        Locale expectedLocale = new Locale("fr", "FR");
        Locale result = localeDeserializer.deserialize(parser, ctxt);
        assertEquals(expectedLocale, result);
    }

    @Test
    public void testDeserializeStringCharset() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        CharsetDeserializer charsetDeserializer = new CharsetDeserializer(Charset.class);

        String charsetString = "UTF-8";
        JsonParser parser = new JsonFactory().createParser(new StringReader(charsetString));
        parser.nextToken();

        Charset expectedCharset = Charset.forName(charsetString);
        Charset result = charsetDeserializer.deserialize(parser, ctxt);
        assertEquals(expectedCharset, result);
    }

    @Test
    public void testDeserializeStringTimeZone() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        TimeZoneDeserializer timeZoneDeserializer = new TimeZoneDeserializer(TimeZone.class);

        String timeZoneString = "Europe/Paris";
        JsonParser parser = new JsonFactory().createParser(new StringReader(timeZoneString));
        parser.nextToken();

        TimeZone expectedTimeZone = TimeZone.getTimeZone(timeZoneString);
        TimeZone result = timeZoneDeserializer.deserialize(parser, ctxt);
        assertEquals(expectedTimeZone, result);
    }

    @Test
    public void testDeserializeStringInetAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        InetAddressDeserializer inetAddressDeserializer = new InetAddressDeserializer(InetAddress.class);

        String ipv4AddressString = "192.168.1.1";
        JsonParser parser = new JsonFactory().createParser(new StringReader(ipv4AddressString));
        parser.nextToken();

        InetAddress expectedAddress = InetAddress.getByName(ipv4AddressString);
        InetAddress result = inetAddressDeserializer.deserialize(parser, ctxt);
        assertEquals(expectedAddress, result);
    }

    @Test
    public void testDeserializeStringInetSocketAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        InetSocketAddressDeserializer inetSocketAddressDeserializer = new InetSocketAddressDeserializer(InetSocketAddress.class);

        String ipv6AddressString = "[2001:0db8:85a3:0000:0000:8a2e:0370:7334]";
        JsonParser parser = new JsonFactory().createParser(new StringReader(ipv6AddressString));
        parser.nextToken();

        InetSocketAddress expectedAddress = new InetSocketAddress("2001:0db8:85a3:0000:0000:8a2e:0370:7334", 80);
        InetSocketAddress result = inetSocketAddressDeserializer.deserialize(parser, ctxt);
        assertEquals(expectedAddress, result);
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        StringDeserializer stringDeserializer = new StringDeserializer(String.class);

        String emptyString = "";
        JsonParser parser = new JsonFactory().createParser(new StringReader(emptyString));
        parser.nextToken();

        String expectedEmptyString = null;
        String result = stringDeserializer.deserialize(parser, ctxt);
        assertNull(result);
        assertEquals(expectedEmptyString, result);
    }

    @Test
    public void testDeserializeNullString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        StringDeserializer stringDeserializer = new StringDeserializer(String.class);

        JsonParser parser = new JsonFactory().createParser(new StringReader(null));
        parser.nextToken();

        String result = stringDeserializer.deserialize(parser, ctxt);
        assertNull(result);
    }

    @Test(expected = InvalidFormatException.class)
    public void testDeserializeInvalidFormatException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        StringDeserializer stringDeserializer = new StringDeserializer(String.class);

        String invalidFormatString = "invalidFormat";
        JsonParser parser = new JsonFactory().createParser(new StringReader(invalidFormatString));
        parser.nextToken();

        stringDeserializer.deserialize(parser, ctxt);
    }
}