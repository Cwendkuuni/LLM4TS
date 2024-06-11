package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer.Std;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

@RunWith(Parameterized.class)
public class FromStringDeserializerTest {

    private final Std deserializer;
    private final Class<?> targetType;

    public FromStringDeserializerTest(Std deserializer, Class<?> targetType) {
        this.deserializer = deserializer;
        this.targetType = targetType;
    }

    @Parameters(name = "{index}: deserialize {0} to {1}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                new Std(FromStringDeserializer.Std.StdFile.class, File.class),
                new Std(FromStringDeserializer.Std.StdUrl.class, URL.class),
                new Std(FromStringDeserializer.Std.StdUri.class, URI.class),
                new Std(FromStringDeserializer.Std.StdClass.class, Class.class),
                new Std(FromStringDeserializer.Std.StdJavaType.class, JavaType.class),
                new Std(FromStringDeserializer.Std.StdCurrency.class, Currency.class),
                new Std(FromStringDeserializer.Std.StdPattern.class, Pattern.class),
                new Std(FromStringDeserializer.Std.StdLocale.class, Locale.class),
                new Std(FromStringDeserializer.Std.StdCharset.class, Charset.class),
                new Std(FromStringDeserializer.Std.StdTimeZone.class, TimeZone.class),
                new Std(FromStringDeserializer.Std.StdInetAddress.class, InetAddress.class),
                new Std(FromStringDeserializer.Std.StdInetSocketAddress.class, InetSocketAddress.class),
                new Std(FromStringDeserializer.Std.StdStringBuilder.class, StringBuilder.class)
        });
    }

    @Test
    public void testDeserialize() throws IOException {
        String json = String.format("\"%s\"", _getValueAsString(targetType));

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser(new StringReader(json));
        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object result = deserializer.deserialize(parser, ctxt);

        if (targetType == StringBuilder.class) {
            assertEquals(expectedLength(), result.toString().length());
        } else {
            assertEquals(expectedValue(), result);
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithInvalidValue() throws IOException {
        String json = String.format("\"%s\"", _getValueAsString(Object.class));

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser(new StringReader(json));
        DeserializationContext ctxt = mapper.getDeserializationContext();

        deserializer.deserialize(parser, ctxt);
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        String json = "\"\"";

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser(new StringReader(json));
        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object result = deserializer.deserialize(parser, ctxt);

        if (targetType == StringBuilder.class) {
            assertNull(result);
        } else {
            if (targetType == File.class) {
                assertEquals(File.class, result.getClass());
            } else if (targetType == URL.class || targetType == URI.class) {
                assertEquals(targetType, result.getClass());
            } else {
                assertEquals(expectedValue(), result);
            }
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithMalformedValue() throws IOException {
        String json = String.format("\"%s\"", _getValueAsString(InetSocketAddress.class));

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser(new StringReader(json));
        DeserializationContext ctxt = mapper.getDeserializationContext();

        deserializer.deserialize(parser, ctxt);
    }

    @Test
    public void testDeserializeWithEmbeddedObject() throws IOException {
        String json = "{\"value\": \"class.forName(\"java.net.URL\")\"}";

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser(new StringReader(json));
        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object result = deserializer.deserialize(parser, ctxt);

        assertEquals(URL.class, result.getClass());

        URL url = (URL) result;
        assertEquals("http://example.com", url.toString());
    }

    @Test
    public void testDeserializeFromArray() throws IOException {
        String json = "[\"127.0.0.1\", \"80\"]";

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser(new StringReader(json));
        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object result = deserializer.deserialize(parser, ctxt);

        InetSocketAddress address = (InetSocketAddress) result;
        assertEquals(127, address.getPort());
        assertEquals("127.0.0.1", address.getAddress().getHostAddress());
    }

    private String _getValueAsString(Class<?> targetType) {
        if (targetType == File.class) {
            return "/path/to/file.txt";
        } else if (targetType == URL.class) {
            return "http://example.com";
        } else if (targetType == URI.class) {
            return "http://example.com/path/to/file.txt";
        } else if (targetType == Class.class) {
            return "java.lang.String";
        } else if (targetType == JavaType.class) {
            return "com.fasterxml.jackson.databind.JsonNode";
        } else if (targetType == Currency.class) {
            return "USD";
        } else if (targetType == Pattern.class) {
            return "\\d{3}-\\d{2}-\\d{3}";
        } else if (targetType == Locale.class) {
            return "en_US";
        } else if (targetType == Charset.class) {
            return "UTF-8";
        } else if (targetType == TimeZone.class) {
            return "UTC";
        } else if (targetType == InetAddress.class) {
            return "127.0.0.1";
        } else if (targetType == InetSocketAddress.class) {
            return "[127.0.0.1, 80]";
        } else if (targetType == StringBuilder.class) {
            return "hello";
        } else {
            throw new IllegalArgumentException("Unsupported target type: " + targetType.getName());
        }
    }

    private int expectedLength() {
        if (targetType == StringBuilder.class) {
            return 5;
        } else {
            return 0;
        }
    }

    private Object expectedValue() {
        if (targetType == File.class) {
            return new File("/path/to/file.txt");
        } else if (targetType == URL.class) {
            return new URL("http://example.com");
        } else if (targetType == URI.class) {
            return new URI("http://example.com/path/to/file.txt");
        } else if (targetType == Class.class) {
            return ClassUtil.loadClass(targetType.getName());
        } else if (targetType == JavaType.class) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.getTypeFactory().constructType(JsonAutoDetect.None.class);
        } else if (targetType == Currency.class) {
            return Currency.getInstance("USD");
        } else if (targetType == Pattern.class) {
            return Pattern.compile("\\d{3}-\\d{2}-\\d{3}");
        } else if (targetType == Locale.class) {
            return Locale.ENGLISH;
        } else if (targetType == Charset.class) {
            return Charset.forName("UTF-8");
        } else if (targetType == TimeZone.class) {
            return TimeZone.getTimeZone("UTC");
        } else if (targetType == InetAddress.class) {
            return InetAddress.getByName("127.0.0.1");
        } else if (targetType == InetSocketAddress.class) {
            return new InetSocketAddress("127.0.0.1", 80);
        } else if (targetType == StringBuilder.class) {
            return new StringBuilder("hello");
        } else {
            throw new IllegalArgumentException("Unsupported target type: " + targetType.getName());
        }
    }
}