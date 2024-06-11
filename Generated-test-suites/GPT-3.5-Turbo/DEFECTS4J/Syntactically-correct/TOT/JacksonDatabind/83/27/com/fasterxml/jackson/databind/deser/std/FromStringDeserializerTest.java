import org.junit.*;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.*;

public class FromStringDeserializerTest {

    @Test
    public void testFileDeserialization() throws IOException {
        File expected = new File("test.txt");
        String json = "\"test.txt\"";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, 1);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext context = mapper.getDeserializationContext();
        File result = deserializer.deserialize(parser, context);
        assertEquals(expected, result);
    }

    @Test
    public void testURLDeserialization() throws IOException, MalformedURLException {
        URL expected = new URL("https://www.example.com");
        String json = "\"https://www.example.com\"";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, 2);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext context = mapper.getDeserializationContext();
        URL result = deserializer.deserialize(parser, context);
        assertEquals(expected, result);
    }

    @Test
    public void testURIDeserialization() throws IOException {
        URI expected = URI.create("https://www.example.com");
        String json = "\"https://www.example.com\"";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, 3);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext context = mapper.getDeserializationContext();
        URI result = deserializer.deserialize(parser, context);
        assertEquals(expected, result);
    }

    @Test
    public void testClassDeserialization() throws IOException, ClassNotFoundException {
        Class<?> expected = String.class;
        String json = "\"java.lang.String\"";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, 4);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext context = mapper.getDeserializationContext();
        Class<?> result = deserializer.deserialize(parser, context);
        assertEquals(expected, result);
    }

    @Test
    public void testJavaTypeDeserialization() throws IOException {
        JavaType expected = TypeFactory.defaultInstance().constructFromCanonical("java.util.List<java.lang.String>");
        String json = "\"java.util.List<java.lang.String>\"";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, 5);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext context = mapper.getDeserializationContext();
        JavaType result = deserializer.deserialize(parser, context);
        assertEquals(expected, result);
    }

    @Test
    public void testCurrencyDeserialization() throws IOException {
        Currency expected = Currency.getInstance("USD");
        String json = "\"USD\"";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, 6);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext context = mapper.getDeserializationContext();
        Currency result = deserializer.deserialize(parser, context);
        assertEquals(expected, result);
    }

    @Test
    public void testPatternDeserialization() throws IOException {
        Pattern expected = Pattern.compile("[a-z]+");
        String json = "\"[a-z]+\"";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, 7);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext context = mapper.getDeserializationContext();
        Pattern result = deserializer.deserialize(parser, context);
        assertEquals(expected, result);
    }

    @Test
    public void testLocaleDeserialization() throws IOException {
        Locale expected = new Locale("en", "US");
        String json = "\"en-US\"";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, 8);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext context = mapper.getDeserializationContext();
        Locale result = deserializer.deserialize(parser, context);
        assertEquals(expected, result);
    }

    @Test
    public void testCharsetDeserialization() throws IOException {
        Charset expected = Charset.forName("UTF-8");
        String json = "\"UTF-8\"";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Charset.class, 9);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext context = mapper.getDeserializationContext();
        Charset result = deserializer.deserialize(parser, context);
        assertEquals(expected, result);
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException {
        TimeZone expected = TimeZone.getTimeZone("UTC");
        String json = "\"UTC\"";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, 10);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext context = mapper.getDeserializationContext();
        TimeZone result = deserializer.deserialize(parser, context);
        assertEquals(expected, result);
    }

    @Test
    public void testInetAddressDeserialization() throws IOException, UnknownHostException {
        InetAddress expected = InetAddress.getLocalHost();
        String json = "\"" + expected.getHostAddress() + "\"";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, 11);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext context = mapper.getDeserializationContext();
        InetAddress result = deserializer.deserialize(parser, context);
        assertEquals(expected, result);
    }

    @Test
    public void testInetSocketAddressDeserialization() throws IOException {
        InetSocketAddress expected = new InetSocketAddress("127.0.0.1", 8080);
        String json = "\"127.0.0.1:8080\"";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetSocketAddress.class, 12);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext context = mapper.getDeserializationContext();
        InetSocketAddress result = deserializer.deserialize(parser, context);
        assertEquals(expected, result);
    }

    @Test
    public void testStringBuilderDeserialization() throws IOException {
        StringBuilder expected = new StringBuilder("test");
        String json = "\"test\"";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(StringBuilder.class, 13);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext context = mapper.getDeserializationContext();
        StringBuilder result = deserializer.deserialize(parser, context);
        assertEquals(expected.toString(), result.toString());
    }

}