import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, 1);
        File expected = new File("test.txt");
        File actual = deserializer._deserialize("test.txt", mock(DeserializationContext.class));
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeURL() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, 2);
        URL expected = new URL("http://www.example.com");
        URL actual = deserializer._deserialize("http://www.example.com", mock(DeserializationContext.class));
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeURI() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, 3);
        URI expected = URI.create("http://www.example.com");
        URI actual = deserializer._deserialize("http://www.example.com", mock(DeserializationContext.class));
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeClass() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, 4);
        Class<?> expected = String.class;
        Class<?> actual = deserializer._deserialize("java.lang.String", mock(DeserializationContext.class));
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(JavaType.class, 5);
        JavaType expected = JavaType.constructFromCanonical("java.lang.String");
        JavaType actual = deserializer._deserialize("java.lang.String", mock(DeserializationContext.class));
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, 6);
        Currency expected = Currency.getInstance("USD");
        Currency actual = deserializer._deserialize("USD", mock(DeserializationContext.class));
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, 7);
        Pattern expected = Pattern.compile("[A-Z]+");
        Pattern actual = deserializer._deserialize("[A-Z]+", mock(DeserializationContext.class));
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, 8);
        Locale expected = new Locale("en", "US");
        Locale actual = deserializer._deserialize("en-US", mock(DeserializationContext.class));
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, 9);
        Charset expected = Charset.forName("UTF-8");
        Charset actual = deserializer._deserialize("UTF-8", mock(DeserializationContext.class));
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, 10);
        TimeZone expected = TimeZone.getTimeZone("GMT");
        TimeZone actual = deserializer._deserialize("GMT", mock(DeserializationContext.class));
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, 11);
        InetAddress expected = InetAddress.getByName("www.example.com");
        InetAddress actual = deserializer._deserialize("www.example.com", mock(DeserializationContext.class));
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, 12);
        InetSocketAddress expected = new InetSocketAddress("www.example.com", 80);
        InetSocketAddress actual = deserializer._deserialize("www.example.com:80", mock(DeserializationContext.class));
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeStringBuilder() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(StringBuilder.class, 13);
        StringBuilder expected = new StringBuilder("test");
        StringBuilder actual = deserializer._deserialize("test", mock(DeserializationContext.class));
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidValue() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, 1);
        deserializer._deserialize("invalid_value", mock(DeserializationContext.class));
    }

    private DeserializationContext mock(Class<DeserializationContext> deserializationContextClass) {
        return null;
    }
}