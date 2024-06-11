package com.yourpackage;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
        JsonParser jp = mapper.getFactory().createParser("\"path/to/file.txt\"");
        File result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals("path/to/file.txt", result.getAbsolutePath());
    }

    @Test
    public void testDeserializeUrl() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
        JsonParser jp = mapper.getFactory().createParser("\"http://example.com\"");
        URL result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals("http://example.com", result.toString());
    }

    @Test
    public void testDeserializeUri() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
        JsonParser jp = mapper.getFactory().createParser("\"http://example.com/path/to/file\"");
        URI result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals("http://example.com/path/to/file", result.toString());
    }

    @Test
    public void testDeserializeClass() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        JsonParser jp = mapper.getFactory().createParser("\"java.lang.String\"");
        Class<?> result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals(String.class, result);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Integer.class);
        JsonParser jp = mapper.getFactory().createParser("\"java.lang.Integer\"");
        JavaType resultType = mapper.getTypeFactory().constructType(Integer.class);
        TypeDeserializer typeDeserializer = mapper.getPolymorphicTypeDeserializer();
        DeserializationContext ctxt = mapper.getDeserializationContext().withValueDeserializer(TypeDeserializer.class, typeDeserializer);
        Object result = deserializer.deserialize(jp, ctxt);
        JavaType actualType = (JavaType) mapper.readValue(mapper.writeValueAsString(result), resultType.getType());
        assertEquals(resultType, actualType);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        JsonParser jp = mapper.getFactory().createParser("\"EUR\"");
        Currency result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals(Currency.getInstance("EUR"), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidCurrency() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        JsonParser jp = mapper.getFactory().createParser("\"XCD\"");
        deserializer.deserialize(jp, mapper.getDeserializationContext());
    }

    @Test
    public void testDeserializePattern() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        JsonParser jp = mapper.getFactory().createParser("\"[0-9]+\"");
        Pattern result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertNotNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidPattern() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        JsonParser jp = mapper.getFactory().createParser("\"invalid\"");
        deserializer.deserialize(jp, mapper.getDeserializationContext());
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        JsonParser jp = mapper.getFactory().createParser("\"en_US\"");
        Locale result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals(Locale.US, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidLocale() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        JsonParser jp = mapper.getFactory().createParser("\"es_XX\"");
        deserializer.deserialize(jp, mapper.getDeserializationContext());
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        JsonParser jp = mapper.getFactory().createParser("\"UTF-8\"");
        Charset result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidCharset() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        JsonParser jp = mapper.getFactory().createParser("\"UTF-16\"");
        deserializer.deserialize(jp, mapper.getDeserializationContext());
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        JsonParser jp = mapper.getFactory().createParser("\"America/New_York\"");
        TimeZone result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals(TimeZone.getTimeZone("America/New_York"), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidTimeZone() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        JsonParser jp = mapper.getFactory().createParser("\"Europe/Prague\"");
        deserializer.deserialize(jp, mapper.getDeserializationContext());
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        JsonParser jp = mapper.getFactory().createParser("\"127.0.0.1\"");
        InetAddress result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertEquals(InetAddress.getByName("127.0.0.1"), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidInetAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        JsonParser jp = mapper.getFactory().createParser("\"256.0.0.1\"");
        deserializer.deserialize(jp, mapper.getDeserializationContext());
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        JsonParser jp = mapper.getFactory().createParser("[\"127.0.0.1\", 80]");
        InetSocketAddress result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        InetAddress address = InetAddress.getByName("127.0.0.1");
        assertEquals(new InetSocketAddress(address, 80), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidInetSocketAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        JsonParser jp = mapper.getFactory().createParser("[\"127.0.0.1\", 256]");
        deserializer.deserialize(jp, mapper.getDeserializationContext());
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<String> deserializer = FromStringDeserializer.findDeserializer(String.class);
        JsonParser jp = mapper.getFactory().createParser("\"\"");
        String result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
        JsonParser jp = mapper.getFactory().createParser("[\"path/to/file1.txt\", \"path/to/file2.txt\"]");
        Object result = deserializer.deserialize(jp, mapper.getDeserializationContext());
        File[] files = (File[]) result;
        assertEquals(2, files.length);
        assertEquals(new File("path/to/file1.txt"), files[0]);
        assertEquals(new File("path/to/file2.txt"), files[1]);
    }
}