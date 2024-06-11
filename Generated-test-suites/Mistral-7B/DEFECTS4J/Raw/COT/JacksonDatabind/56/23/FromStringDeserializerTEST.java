package com.example.tests.jackson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeFactory;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        JsonParser parser = Mockito.mock(JsonParser.class);

        String jsonString = "\"test.txt\"";
        File expected = new File("test.txt");

        Mockito.when(parser.getText()).thenReturn(jsonString);
        Mockito.when(parser.isExpectedStartArrayValue()).thenReturn(false);
        Mockito.when(parser.getValueAsString()).thenReturn(jsonString);

        FromStringDeserializer<File> deserializer = new FromStringDeserializer<File>(File.class) {
            @Override
            protected File _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return new File(value);
            }
        };

        File result = deserializer.deserialize(parser, context);

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeUrl() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        JsonParser parser = Mockito.mock(JsonParser.class);

        String jsonString = "\"http://www.example.com\"";
        URL expected = new URL("http://www.example.com");

        Mockito.when(parser.getText()).thenReturn(jsonString);
        Mockito.when(parser.isExpectedStartArrayValue()).thenReturn(false);
        Mockito.when(parser.getValueAsString()).thenReturn(jsonString);

        FromStringDeserializer<URL> deserializer = new FromStringDeserializer<URL>(URL.class) {
            @Override
            protected URL _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return new URL(value);
            }
        };

        URL result = deserializer.deserialize(parser, context);

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeUri() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        JsonParser parser = Mockito.mock(JsonParser.class);

        String jsonString = "\"http://www.example.com/path/to/file\"";
        URI expected = new URI("http://www.example.com/path/to/file");

        Mockito.when(parser.getText()).thenReturn(jsonString);
        Mockito.when(parser.isExpectedStartArrayValue()).thenReturn(false);
        Mockito.when(parser.getValueAsString()).thenReturn(jsonString);

        FromStringDeserializer<URI> deserializer = new FromStringDeserializer<URI>(URI.class) {
            @Override
            protected URI _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return new URI(value);
            }
        };

        URI result = deserializer.deserialize(parser, context);

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeClass() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        JsonParser parser = Mockito.mock(JsonParser.class);

        String jsonString = "java.lang.String";
        Class<?> expected = String.class;

        Mockito.when(parser.getText()).thenReturn(jsonString);
        Mockito.when(parser.isExpectedStartArrayValue()).thenReturn(false);
        Mockito.when(parser.getValueAsString()).thenReturn(jsonString);

        FromStringDeserializer<Class<?>> deserializer = new FromStringDeserializer<Class<?>>(Class.class) {
            @Override
            protected Class<?> _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return Class.forName(value);
            }
        };

        Class<?> result = deserializer.deserialize(parser, context);

        assertEquals(expected, result.getName());
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        JsonParser parser = Mockito.mock(JsonParser.class);

        String jsonString = "com.example.MyCustomType";
        JavaType expected = mapper.getTypeFactory().constructType(JavaType.class, Class.forName(jsonString));

        Mockito.when(parser.getText()).thenReturn(jsonString);
        Mockito.when(parser.isExpectedStartArrayValue()).thenReturn(false);
        Mockito.when(parser.getValueAsString()).thenReturn(jsonString);

        FromStringDeserializer<JavaType> deserializer = new FromStringDeserializer<JavaType>(JavaType.class) {
            @Override
            protected JavaType _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return mapper.getTypeFactory().constructType(JavaType.class, Class.forName(value));
            }
        };

        JavaType result = deserializer.deserialize(parser, context);

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        JsonParser parser = Mockito.mock(JsonParser.class);

        String jsonString = "USD";
        Currency expected = Currency.getInstance(Locale.US);

        Mockito.when(parser.getText()).thenReturn(jsonString);
        Mockito.when(parser.isExpectedStartArrayValue()).thenReturn(false);

        FromStringDeserializer<Currency> deserializer = new FromStringDeserializer<Currency>(Currency.class) {
            @Override
            protected Currency _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return Currency.getInstance(Locale.forLanguageTag(value));
            }
        };

        Currency result = deserializer.deserialize(parser, context);

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        JsonParser parser = Mockito.mock(JsonParser.class);

        String jsonString = "\\w+@\\w+\\.\\w+";
        Pattern expected = Pattern.compile(jsonString);

        Mockito.when(parser.getText()).thenReturn(jsonString);
        Mockito.when(parser.isExpectedStartArrayValue()).thenReturn(false);

        FromStringDeserializer<Pattern> deserializer = new FromStringDeserializer<Pattern>(Pattern.class) {
            @Override
            protected Pattern _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return Pattern.compile(value);
            }
        };

        Pattern result = deserializer.deserialize(parser, context);

        assertEquals(expected.pattern(), result.pattern());
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        JsonParser parser = Mockito.mock(JsonParser.class);

        String jsonString = "en_US";
        Locale expected = Locale.forLanguageTag(jsonString);

        Mockito.when(parser.getText()).thenReturn(jsonString);
        Mockito.when(parser.isExpectedStartArrayValue()).thenReturn(false);

        FromStringDeserializer<Locale> deserializer = new FromStringDeserializer<Locale>(Locale.class) {
            @Override
            protected Locale _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return Locale.forLanguageTag(value);
            }
        };

        Locale result = deserializer.deserialize(parser, context);

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        JsonParser parser = Mockito.mock(JsonParser.class);

        String jsonString = "UTF-8";
        Charset expected = Charset.forName(jsonString);

        Mockito.when(parser.getText()).thenReturn(jsonString);
        Mockito.when(parser.isExpectedStartArrayValue()).thenReturn(false);

        FromStringDeserializer<Charset> deserializer = new FromStringDeserializer<Charset>(Charset.class) {
            @Override
            protected Charset _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return Charset.forName(value);
            }
        };

        Charset result = deserializer.deserialize(parser, context);

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        JsonParser parser = Mockito.mock(JsonParser.class);

        String jsonString = "America/New_York";
        TimeZone expected = TimeZone.getTimeZone(jsonString);

        Mockito.when(parser.getText()).thenReturn(jsonString);
        Mockito.when(parser.isExpectedStartArrayValue()).thenReturn(false);

        FromStringDeserializer<TimeZone> deserializer = new FromStringDeserializer<TimeZone>(TimeZone.class) {
            @Override
            protected TimeZone _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return TimeZone.getTimeZone(value);
            }
        };

        TimeZone result = deserializer.deserialize(parser, context);

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        JsonParser parser = Mockito.mock(JsonParser.class);

        String jsonString = "192.168.0.1";
        InetAddress expected = InetAddress.getByName(jsonString);

        Mockito.when(parser.getText()).thenReturn(jsonString);
        Mockito.when(parser.isExpectedStartArrayValue()).thenReturn(false);

        FromStringDeserializer<InetAddress> deserializer = new FromStringDeserializer<InetAddress>(InetAddress.class) {
            @Override
            protected InetAddress _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return InetAddress.getByName(value);
            }
        };

        InetAddress result = deserializer.deserialize(parser, context);

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        JsonParser parser = Mockito.mock(JsonParser.class);

        String jsonString = "[192.168.0.1]:8080";
        InetSocketAddress expected = new InetSocketAddress(InetAddress.getByName("192.168.0.1"), 8080);

        Mockito.when(parser.getText()).thenReturn(jsonString);
        Mockito.when(parser.isExpectedStartArrayValue()).thenReturn(true);
        Mockito.when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        Mockito.when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(parser.getText()).thenReturn("192.168.0.1");
        Mockito.when(parser.nextToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
        Mockito.when(parser.nextIntValue()).thenReturn(8080);
        Mockito.when(parser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        FromStringDeserializer<InetSocketAddress> deserializer = new FromStringDeserializer<InetSocketAddress>(InetSocketAddress.class) {
            @Override
            protected InetSocketAddress _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return new InetSocketAddress(InetAddress.getByName(value), ctxt.readValueAsInt(ctxt.getFactory().constructNumberMinusBigdecimal(ctxt.getParser().nextToken(), null)));
            }
        };

        InetSocketAddress result = deserializer.deserialize(parser, context);

        assertEquals(expected, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        JsonParser parser = Mockito.mock(JsonParser.class);

        String jsonString = "{\"type\":\"invalid\", \"value\":\"abc\"}";
        Mockito.when(parser.getText()).thenReturn(jsonString);
        Mockito.when(parser.isExpectedStartArrayValue()).thenReturn(false);

        FromStringDeserializer<File> deserializer = new FromStringDeserializer<File>(File.class) {};

        deserializer.deserialize(parser, context);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmptyString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        JsonParser parser = Mockito.mock(JsonParser.class);

        String jsonString = "";
        Mockito.when(parser.getText()).thenReturn(jsonString);
        Mockito.when(parser.isExpectedStartArrayValue()).thenReturn(false);

        FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class) {};

        deserializer.deserialize(parser, context);
    }

    @Test(expected = InvalidFormatException.class)
    public void testDeserializeInvalidCurrencyCode() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        JsonParser parser = Mockito.mock(JsonParser.class);

        String jsonString = "XYZ";
        Mockito.when(parser.getText()).thenReturn(jsonString);
        Mockito.when(parser.isExpectedStartArrayValue()).thenReturn(false);

        FromStringDeserializer<Currency> deserializer = new FromStringDeserializer<Currency>(Currency.class) {};

        deserializer.deserialize(parser, context);
    }
}