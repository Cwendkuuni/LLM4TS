package com.yourpackagename.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.std.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.junit.Test;

public class FromStringDeserializerTest {

    

    @Test
    public void testDeserializeFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected File _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return new File(value);
            }
        };
        StdSerializer<File> serializer = new StdSerializer<>(File.class) {};
        ObjectReader reader = mapper.readerFor(File.class)
                .withValueHandler(deserializer)
                .withSerializer(serializer);
        String json = "{\"className\":\"File\",\"path\":\"test.txt\"}";
        File expected = new File("test.txt");
        File actual = reader.readValue(new StringReader(json));
        assertEquals(expected.getAbsolutePath(), actual.getAbsolutePath());
    }

    @Test
    public void testDeserializeUrl() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected URL _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return new URL(value);
            }
        };
        StdSerializer<URL> serializer = new StdSerializer<>(URL.class) {};
        ObjectReader reader = mapper.readerFor(URL.class)
                .withValueHandler(deserializer)
                .withSerializer(serializer);
        String json = "{\"className\":\"URL\",\"string\":\"http://www.example.com\"}";
        URL expected = new URL("http://www.example.com");
        URL actual = reader.readValue(new StringReader(json));
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testDeserializeUri() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected URI _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return new URI(value);
            }
        };
        StdSerializer<URI> serializer = new StdSerializer<>(URI.class) {};
        ObjectReader reader = mapper.readerFor(URI.class)
                .withValueHandler(deserializer)
                .withSerializer(serializer);
        String json = "{\"className\":\"URI\",\"string\":\"http://www.example.com/path/to/file\"}";
        URI expected = new URI("http://www.example.com/path/to/file");
        URI actual = reader.readValue(new StringReader(json));
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeClass() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected Class<?> _deserialize(String value, DeserializationContext ctxt) throws IOException {
                try {
                    return ctxt.findClass(value);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
        StdSerializer<Class<?>> serializer = new StdSerializer<>(Class.class) {};
        ObjectReader reader = mapper.readerFor(Class.class)
                .withValueHandler(deserializer)
                .withSerializer(serializer);
        String json = "{\"className\":\"java.lang.String\"}";
        Class<String> expected = String.class;
        Class<String> actual = reader.readValue(new StringReader(json));
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected JavaType _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return ctxt.getTypeFactory().constructType(value);
            }
        };
        StdSerializer<JavaType> serializer = new StdSerializer<>(JavaType.class) {};
        ObjectReader reader = mapper.readerFor(JavaType.class)
                .withValueHandler(deserializer)
                .withSerializer(serializer);
        String json = "{\"className\":\"java.util.List\",\"typeArguments\":[\"java.lang.String\"]}";
        JavaType expected = new TypeFactory()
                .constructType("java.util.List<java.lang.String>");
        JavaType actual = reader.readValue(new StringReader(json));
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected Currency _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return Currency.getInstance(value);
            }
        };
        StdSerializer<Currency> serializer = new StdSerializer<>(Currency.class) {};
        ObjectReader reader = mapper.readerFor(Currency.class)
                .withValueHandler(deserializer)
                .withSerializer(serializer);
        String json = "{\"className\":\"Currency\",\"currencyCode\":\"EUR\"}";
        Currency expected = Currency.getInstance("EUR");
        Currency actual = reader.readValue(new StringReader(json));
        assertEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidPattern() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected Pattern _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return Pattern.compile(value);
            }
        };
        ObjectReader reader = mapper.readerFor(Pattern.class);
        reader.setValueHandler(deserializer);
        String json = "{\"className\":\"Pattern\",\"regex\":\"[^\\\\]+\\\\\\\\\\\"}";
        reader.readValue(new StringReader(json));
        fail("Expected JsonMappingException");
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected Locale _deserialize(String value, DeserializationContext ctxt) throws IOException {
                int index = value.indexOf('_');
                if (index < 0) {
                    return new Locale(value);
                }
                String first = value.substring(0, index);
                String second = value.substring(index + 1);
                return new Locale(first, second);
            }
        };
        StdSerializer<Locale> serializer = new StdSerializer<>(Locale.class) {};
        ObjectReader reader = mapper.readerFor(Locale.class)
                .withValueHandler(deserializer)
                .withSerializer(serializer);
        String json = "{\"className\":\"Locale\",\"localeString\":\"en_US\"}";
        Locale expected = Locale.US;
        Locale actual = reader.readValue(new StringReader(json));
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected Charset _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return Charset.forName(value);
            }
        };
        StdSerializer<Charset> serializer = new StdSerializer<>(Charset.class) {};
        ObjectReader reader = mapper.readerFor(Charset.class)
                .withValueHandler(deserializer)
                .withSerializer(serializer);
        String json = "{\"className\":\"Charset\",\"charsetName\":\"UTF-8\"}";
        Charset expected = Charset.forName("UTF-8");
        Charset actual = reader.readValue(new StringReader(json));
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected TimeZone _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return TimeZone.getTimeZone(value);
            }
        };
        StdSerializer<TimeZone> serializer = new StdSerializer<>(TimeZone.class) {};
        ObjectReader reader = mapper.readerFor(TimeZone.class)
                .withValueHandler(deserializer)
                .withSerializer(serializer);
        String json = "{\"className\":\"TimeZone\",\"timeZoneID\":\"UTC\"}";
        TimeZone expected = TimeZone.getTimeZone("UTC");
        TimeZone actual = reader.readValue(new StringReader(json));
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected InetAddress _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return InetAddress.getByName(value);
            }
        };
        StdSerializer<InetAddress> serializer = new StdSerializer<>(InetAddress.class) {};
        ObjectReader reader = mapper.readerFor(InetAddress.class)
                .withValueHandler(deserializer)
                .withSerializer(serializer);
        String json = "{\"className\":\"InetAddress\",\"ipAddress\":\"127.0.0.1\"}";
        InetAddress expected = InetAddress.getByName("127.0.0.1");
        InetAddress actual = reader.readValue(new StringReader(json));
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected InetSocketAddress _deserialize(String value, DeserializationContext ctxt) throws IOException {
                InetSocketAddress address;
                int index = value.indexOf(':');
                if (index > 0) {
                    String[] parts = value.split(":");
                    InetAddress ipAddress = InetAddress.getByName(parts[0]);
                    int port = Integer.parseInt(parts[1]);
                    address = new InetSocketAddress(ipAddress, port);
                } else {
                    String ipAddress = value;
                    address = new InetSocketAddress(ipAddress, 0);
                }
                return address;
            }
        };
        StdSerializer<InetSocketAddress> serializer = new StdSerializer<>(InetSocketAddress.class) {};
        ObjectReader reader = mapper.readerFor(InetSocketAddress.class)
                .withValueHandler(deserializer)
                .withSerializer(serializer);
        String json1 = "{\"className\":\"InetSocketAddress\",\"ipAddress\":\"127.0.0.1\"}";
        InetSocketAddress expected1 = new InetSocketAddress("127.0.0.1", 0);
        InetSocketAddress actual1 = reader.readValue(new StringReader(json1));
        assertEquals(expected1.getAddress().getHostAddress(), actual1.getAddress().getHostAddress());
        assertEquals(expected1.getPort(), actual1.getPort());

        String json2 = "{\"className\":\"InetSocketAddress\",\"ipAddress\":\"127.0.0.1\",\"port\":\"5432\"}";
        InetSocketAddress expected2 = new InetSocketAddress("127.0.0.1", 5432);
        InetSocketAddress actual2 = reader.readValue(new StringReader(json2));
        assertEquals(expected2.getAddress().getHostAddress(), actual2.getAddress().getHostAddress());
        assertEquals(expected2.getPort(), actual2.getPort());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeInvalidValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = new FromStringDeserializer<Object>();
        ObjectReader reader = mapper.readerFor(Object.class)
                .withValueHandler(deserializer);
        String json = "{\"className\":\"InvalidClass\",\"string\":\"invalidValue\"}";
        reader.readValue(new StringReader(json));
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmptyValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = new FromStringDeserializer<Object>() {
            @Override
            protected Object _deserializeFromEmptyString() throws IOException {
                throw new RuntimeException("Should not reach here");
            }
        };
        ObjectReader reader = mapper.readerFor(Object.class)
                .withValueHandler(deserializer);
        String json = "{}";
        reader.readValue(new StringReader(json));
        fail("Expected JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = new FromStringDeserializer<Object>() {
            @Override
            protected Object _deserializeFromEmptyString() throws IOException {
                return null;
            }
        };
        ObjectReader reader = mapper.readerFor(Object.class)
                .withValueHandler(deserializer);
        String json = "{}";
        reader.readValue(new StringReader(json));
        fail("Expected JsonMappingException");
    }

    @Test
    public void testDeserializeEmptyJsonArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = new FromStringDeserializer<Object>() {};
        ObjectReader reader = mapper.readerFor(Object.class)
                .withValueHandler(deserializer);
        String json = "[ ]";
        Object actual = reader.readValue(new StringReader(json));
        assertNull(actual);
    }

    @Test
    public void testDeserializeJsonArrayWithSingleValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = new FromStringDeserializer<Object>() {};
        ObjectReader reader = mapper.readerFor(Object.class)
                .withValueHandler(deserializer);
        String json = "[\"Value\"]";
        Object actual = reader.readValue(new StringReader(json));
        assertEquals("Value", actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeJsonArrayWithMultipleValues() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = new FromStringDeserializer<Object>() {};
        ObjectReader reader = mapper.readerFor(Object.class)
                .withValueHandler(deserializer);
        String json = "[\"Value1\", \"Value2\"]";
        reader.readValue(new StringReader(json));
        fail("Expected JsonMappingException");
    }

    
}