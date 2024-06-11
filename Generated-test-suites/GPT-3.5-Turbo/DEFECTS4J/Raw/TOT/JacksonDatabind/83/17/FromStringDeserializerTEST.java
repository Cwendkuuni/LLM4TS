import static org.junit.Assert.*;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;

public class FromStringDeserializerTest {

    private DeserializationContext deserializationContext;

    @Before
    public void setUp() {
        deserializationContext = new DeserializationContext();
    }

    @Test
    public void testDeserialize_File() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, 1);
        File file = (File) deserializer.deserialize(new JsonParserMock("C:\\temp.txt"), deserializationContext);
        assertEquals("C:\\temp.txt", file.getPath());
    }

    @Test
    public void testDeserialize_URL() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, 2);
        URL url = (URL) deserializer.deserialize(new JsonParserMock("https://www.example.com"), deserializationContext);
        assertEquals("https://www.example.com", url.toString());
    }

    @Test
    public void testDeserialize_URI() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, 3);
        URI uri = (URI) deserializer.deserialize(new JsonParserMock("https://www.example.com"), deserializationContext);
        assertEquals("https://www.example.com", uri.toString());
    }

    @Test
    public void testDeserialize_Class() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, 4);
        Class<?> clazz = (Class<?>) deserializer.deserialize(new JsonParserMock("java.util.ArrayList"), deserializationContext);
        assertEquals("java.util.ArrayList", clazz.getName());
    }

    @Test
    public void testDeserialize_JavaType() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, 5);
        JavaType javaType = (JavaType) deserializer.deserialize(new JsonParserMock("java.util.ArrayList"), deserializationContext);
        assertEquals("java.util.ArrayList", javaType.getRawClass().getName());
    }

    @Test
    public void testDeserialize_Currency() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, 6);
        Currency currency = (Currency) deserializer.deserialize(new JsonParserMock("USD"), deserializationContext);
        assertEquals("USD", currency.getCurrencyCode());
    }

    @Test
    public void testDeserialize_Pattern() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, 7);
        Pattern pattern = (Pattern) deserializer.deserialize(new JsonParserMock("[a-z]+"), deserializationContext);
        assertEquals("[a-z]+", pattern.pattern());
    }

    @Test
    public void testDeserialize_Locale_SingleArgument() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, 8);
        Locale locale = (Locale) deserializer.deserialize(new JsonParserMock("en"), deserializationContext);
        assertEquals("en", locale.getLanguage());
    }

    @Test
    public void testDeserialize_Locale_TwoArguments() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, 8);
        Locale locale = (Locale) deserializer.deserialize(new JsonParserMock("en-US"), deserializationContext);
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
    }

    @Test
    public void testDeserialize_Locale_ThreeArguments() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, 8);
        Locale locale = (Locale) deserializer.deserialize(new JsonParserMock("en-US-NY"), deserializationContext);
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
        assertEquals("NY", locale.getVariant());
    }

    @Test
    public void testDeserialize_Charset() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, 9);
        Charset charset = (Charset) deserializer.deserialize(new JsonParserMock("UTF-8"), deserializationContext);
        assertEquals("UTF-8", charset.name());
    }

    @Test
    public void testDeserialize_TimeZone() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, 10);
        TimeZone timeZone = (TimeZone) deserializer.deserialize(new JsonParserMock("America/New_York"), deserializationContext);
        assertEquals("America/New_York", timeZone.getID());
    }

    @Test
    public void testDeserialize_InetAddress() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, 11);
        InetAddress inetAddress = (InetAddress) deserializer.deserialize(new JsonParserMock("127.0.0.1"), deserializationContext);
        assertEquals("127.0.0.1", inetAddress.getHostAddress());
    }

    @Test
    public void testDeserialize_InetSocketAddress_WithPort() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, 12);
        InetSocketAddress inetSocketAddress = (InetSocketAddress) deserializer.deserialize(new JsonParserMock("[::1]:8080"), deserializationContext);
        assertEquals("[::1]", inetSocketAddress.getAddress().toString());
        assertEquals(8080, inetSocketAddress.getPort());
    }

    @Test
    public void testDeserialize_InetSocketAddress_WithoutPort() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, 12);
        InetSocketAddress inetSocketAddress = (InetSocketAddress) deserializer.deserialize(new JsonParserMock("[::1]"), deserializationContext);
        assertEquals("[::1]", inetSocketAddress.getAddress().toString());
        assertEquals(0, inetSocketAddress.getPort());
    }

    @Test
    public void testDeserialize_InetSocketAddress_HostAndPort() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, 12);
        InetSocketAddress inetSocketAddress = (InetSocketAddress) deserializer.deserialize(new JsonParserMock("localhost:8080"), deserializationContext);
        assertEquals("localhost", inetSocketAddress.getHostString());
        assertEquals(8080, inetSocketAddress.getPort());
    }

    @Test
    public void testDeserialize_StringBuilder() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(StringBuilder.class, 13);
        StringBuilder stringBuilder = (StringBuilder) deserializer.deserialize(new JsonParserMock("test"), deserializationContext);
        assertEquals("test", stringBuilder.toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_InvalidString() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, 6);
        deserializer.deserialize(new JsonParserMock("Invalid"), deserializationContext);
    }

    @Test(expected = JsonParseException.class)
    public void testDeserialize_InvalidJSON() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, 6);
        deserializer.deserialize(new JsonParserMock("{\"key\": null}"), deserializationContext);
    }
}

class JsonParserMock extends JsonParser {

    private String value;

    public JsonParserMock(String value) {
        this.value = value;
    }

    @Override
    public String getValueAsString() {
        return value;
    }

    // implement other methods of JsonParser as needed

}