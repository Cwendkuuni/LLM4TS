package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"path/to/file.txt\"");
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected Class<?> _valueClass;

            @Override
            protected void init(Class<?> valueType, DeserializationContext ctxt) {
                super.init(valueType, ctxt);
                _valueClass = File.class;
            }
        };
        File result = deserializer.deserialize(parser, null);
        assertEquals(new File("path/to/file.txt"), result);
    }

    @Test
    public void testDeserializeUrl() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"http://example.com\"");
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected Class<?> _valueClass;

            @Override
            protected void init(Class<?> valueType, DeserializationContext ctxt) {
                super.init(valueType, ctxt);
                _valueClass = URL.class;
            }
        };
        URL result = deserializer.deserialize(parser, null);
        assertEquals("http://example.com", result.toString());
    }

    @Test
    public void testDeserializeUri() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"http://example.com/path/to/file.txt\"");
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected Class<?> _valueClass;

            @Override
            protected void init(Class<?> valueType, DeserializationContext ctxt) {
                super.init(valueType, ctxt);
                _valueClass = URI.class;
            }
        };
        URI result = deserializer.deserialize(parser, null);
        assertEquals("http://example.com/path/to/file.txt", result.toString());
    }

    @Test
    public void testDeserializeClass() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("java.lang.String");
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected Class<?> _valueClass;

            @Override
            protected void init(Class<?> valueType, DeserializationContext ctxt) {
                super.init(valueType, ctxt);
                _valueClass = String.class;
            }
        };
        String result = deserializer.deserialize(parser, null).getValueAsString();
        assertEquals("java.lang.String", result);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("com.example.MyClass");
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected Class<?> _valueClass;

            @Override
            protected void init(Class<?> valueType, DeserializationContext ctxt) {
                super.init(valueType, ctxt);
                _valueClass = MyClass.class;
            }
        };
        MyClass result = deserializer.deserialize(parser, null);
        assertNotNull(result);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"USD\"");
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected Class<?> _valueClass;

            @Override
            protected void init(Class<?> valueType, DeserializationContext ctxt) {
                super.init(valueType, ctxt);
                _valueClass = Currency.class;
            }
        };
        Currency result = deserializer.deserialize(parser, null);
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("/path/to/regex.pattern/");
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected Class<?> _valueClass;

            @Override
            protected void init(Class<?> valueType, DeserializationContext ctxt) {
                super.init(valueType, ctxt);
                _valueClass = Pattern.class;
            }
        };
        Pattern result = deserializer.deserialize(parser, null);
        assertNotNull(result);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"en_US\"");
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected Class<?> _valueClass;

            @Override
            protected void init(Class<?> valueType, DeserializationContext ctxt) {
                super.init(valueType, ctxt);
                _valueClass = Locale.class;
            }
        };
        Locale result = deserializer.deserialize(parser, null);
        assertEquals(Locale.US, result);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"UTF-8\"");
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected Class<?> _valueClass;

            @Override
            protected void init(Class<?> valueType, DeserializationContext ctxt) {
                super.init(valueType, ctxt);
                _valueClass = Charset.class;
            }
        };
        Charset result = deserializer.deserialize(parser, null);
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"America/New_York\"");
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected Class<?> _valueClass;

            @Override
            protected void init(Class<?> valueType, DeserializationContext ctxt) {
                super.init(valueType, ctxt);
                _valueClass = TimeZone.class;
            }
        };
        TimeZone result = deserializer.deserialize(parser, null);
        assertEquals(TimeZone.getTimeZone("America/New_York"), result);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("192.168.1.1");
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected Class<?> _valueClass;

            @Override
            protected void init(Class<?> valueType, DeserializationContext ctxt) {
                super.init(valueType, ctxt);
                _valueClass = InetAddress.class;
            }
        };
        InetAddress result = deserializer.deserialize(parser, null);
        assertEquals("192.168.1.1", result.getHostAddress());
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[255.255.255.255]:80");
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected Class<?> _valueClass;

            @Override
            protected void init(Class<?> valueType, DeserializationContext ctxt) {
                super.init(valueType, ctxt);
                _valueClass = InetSocketAddress.class;
            }
        };
        InetSocketAddress result = deserializer.deserialize(parser, null);
        assertEquals("255.255.255.255", result.getAddress().getHostAddress());
        assertEquals(80, result.getPort());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidJsonFormat() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("invalidJsonFormat");
        FromStringDeserializer deserializer = new FromStringDeserializer<Object>() {
            @Override
            protected Class<?> _valueClass;

            @Override
            protected void init(Class<?> valueType, DeserializationContext ctxt) {
                super.init(valueType, ctxt);
                _valueClass = Object.class;
            }

            @Override
            protected Object _deserialize(String value, DeserializationContext ctxt) throws IOException {
                throw new InvalidFormatException(ctxt.getParser(), "Invalid json format");
            }
        };
        deserializer.deserialize(parser, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeUnknownType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("invalidType");
        FromStringDeserializer deserializer = new FromStringDeserializer<Object>() {
            @Override
            protected Class<?> _valueClass;

            @Override
            protected void init(Class<?> valueType, DeserializationContext ctxt) {
                super.init(valueType, ctxt);
                _valueClass = Object.class;
            }
        };
        deserializer.deserialize(parser, null);
    }

}