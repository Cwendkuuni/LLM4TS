package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class FromStringDeserializerTest {

    @SuppressWarnings("unchecked")
    private static <T> T deserialize(Class<T> type, String input) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        FromStringDeserializer<T> deserializer = (FromStringDeserializer<T>) FromStringDeserializer.findDeserializer(type);
        if (deserializer != null) {
            module.addDeserializer(type, deserializer);
        }
        mapper.registerModule(module);
        return mapper.readValue(new StringReader(input), type);
    }

    @Test
    public void testDeserializeFile() throws IOException {
        String input = "path/to/file.txt";
        File expected = new File(input);
        File result = deserialize(File.class, input);
       assertEquals(expected.getAbsolutePath(), result.getAbsolutePath());
    }

    @Test
    public void testDeserializeURL() throws IOException {
        String input = "http://example.com";
        URL expected = new URL(input);
        URL result = deserialize(URL.class, input);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testDeserializeURI() throws IOException {
        String input = "http://example.com/path/to";
        URI expected = new URI(input);
        URI result = deserialize(URI.class, input);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testDeserializeClass() throws IOException {
        String input = "java.lang.String";
        Class<?> expected = Class.forName(input);
        Class<?> result = deserialize(Class.class, input);
        assertEquals(expected.getName(), result.getName());
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        String input = "com.fasterxml.jackson.databind.JsonNode";
        JavaType expected = new TypeFactory().constructType(input);
        JavaType result = deserialize(JavaType.class, input);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        String input = "USD";
        Currency expected = Currency.getInstance(input);
        Currency result = deserialize(Currency.class, input);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        String input = "[a-zA-Z0-9_]+";
        Pattern expected = Pattern.compile(input);
        Pattern result = deserialize(Pattern.class, input);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        String input = "en_US";
        Locale expected = new Locale("en", "US");
        Locale result = deserialize(Locale.class, input);
        assertEquals(expected, result);

        String input2 = "en_US_POSIX";
        Locale expected2 = new Locale("en", "US", "");
        Locale result2 = deserialize(Locale.class, input2);
        assertEquals(expected2, result2);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        String input = "UTF-8";
        Charset expected = Charset.forName(input);
        Charset result = deserialize(Charset.class, input);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        String input = "America/Los_Angeles";
        TimeZone expected = TimeZone.getTimeZone(input);
        TimeZone result = deserialize(TimeZone.class, input);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        String input = "192.168.0.1";
        InetAddress expected = InetAddress.getByName(input);
        InetAddress result = deserialize(InetAddress.class, input);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        String input = "[::1]:8080";
        InetSocketAddress expected = new InetSocketAddress(input, 8080);
        InetSocketAddress result = deserialize(InetSocketAddress.class, input);
        assertEquals(expected, result);

        String input2 = "192.168.0.1:8080";
        InetSocketAddress expected2 = new InetSocketAddress(InetAddress.getByName("192.168.0.1"), 8080);
        InetSocketAddress result2 = deserialize(InetSocketAddress.class, input2);
        assertEquals(expected2, result2);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidDeserialize() throws IOException {
        String input = "invalid";
        deserialize(Object.class, input);
    }

}

class StringReader extends InputStreamReader {
    private final String data;

    public StringReader(String data) {
        super(new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)));
        this.data = data;
    }

    @Override
    public int read() throws IOException {
        return super.read();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int length = super.read(b, off, len);
        if (length < 0) {
            return length;
        }
        if (len - off < length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        System.arraycopy(data.getBytes(StandardCharsets.UTF_8), off + len - length, b, off, length);
        return length;
    }

    @Override
    public long skip(long n) throws IOException {
        long length = super.skip(n);
        if (length < n) {
            throw new EOFException();
        }
        return length;
    }

    @Override
    public synchronized void close() throws IOException {
        super.close();
    }
}