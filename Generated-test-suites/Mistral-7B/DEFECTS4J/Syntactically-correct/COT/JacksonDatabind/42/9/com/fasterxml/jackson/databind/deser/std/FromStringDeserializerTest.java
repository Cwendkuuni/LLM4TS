package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import org.junit.Assert;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class FromStringDeserializerTest {
    
    
    @Test
    public void testDeserializeFile() {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected File _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return new File(value);
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String jsonString = "\"/path/to/file.txt\"";
        File expected = new File("/path/to/file.txt");
        File actual = null;

        try {
            actual = deserializer.deserialize(mapper.getFactory().createParser(jsonString), new DeserializationContext());
        } catch (IOException e) {
            Assert.fail("IOException should not be thrown");
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeURL() {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected URL _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return new URL(value);
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String jsonString = "\"http://example.com\"";
        URL expected = new URL("http://example.com");
        URL actual = null;

        try {
            actual = deserializer.deserialize(mapper.getFactory().createParser(jsonString), new DeserializationContext());
        } catch (IOException e) {
            Assert.fail("IOException should not be thrown");
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeURI() {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected URI _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return URI.create(value);
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String jsonString = "\"http://example.com/path?query=value#fragment\"";
        URI expected = new URI("http", "example.com", "/path", null, null, null, null, null, null, null, null, null, "query=value", "#fragment");
        URI actual = null;

        try {
            actual = deserializer.deserialize(mapper.getFactory().createParser(jsonString), new DeserializationContext());
        } catch (IOException e) {
            Assert.fail("IOException should not be thrown");
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeClass() {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected Class<?> _deserialize(String value, DeserializationContext ctxt) throws IOException {
                try {
                    return Class.forName(value);
                } catch (ClassNotFoundException e) {
                    throw new IOException("Unable to load class: " + value);
                }
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String jsonString = "java.lang.String";
        Class<?> expected = String.class;
        Class<?> actual = null;

        try {
            actual = deserializer.deserialize(mapper.getFactory().createParser(jsonString), new DeserializationContext());
        } catch (IOException e) {
            Assert.fail("IOException should not be thrown");
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeJavaType() {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected JavaType _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return ctxt.getTypeFactory().constructFromCanonical(value);
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String jsonString = "java.util.HashMap<java.lang.Integer, java.lang.String>";
        JavaType expected = mapper.getTypeFactory().constructMapType(HashMap.class, Integer.class, String.class);
        JavaType actual = null;

        try {
            actual = deserializer.deserialize(mapper.getFactory().createParser(jsonString), new DeserializationContext());
        } catch (IOException e) {
            Assert.fail("IOException should not be thrown");
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeCurrency() {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected Currency _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return Currency.getInstance(value);
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String jsonString = "USD";
        Currency expected = Currency.getInstance("USD");
        Currency actual = null;

        try {
            actual = deserializer.deserialize(mapper.getFactory().createParser(jsonString), new DeserializationContext());
        } catch (IOException e) {
            Assert.fail("IOException should not be thrown");
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializePattern() {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected Pattern _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return Pattern.compile(value);
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String jsonString = "(.*)";
        Pattern expected = Pattern.compile("(.*)");
        Pattern actual = null;

        try {
            actual = deserializer.deserialize(mapper.getFactory().createParser(jsonString), new DeserializationContext());
        } catch (IOException e) {
            Assert.fail("IOException should not be thrown");
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeLocale() {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected Locale _deserialize(String value, DeserializationContext ctxt) throws IOException {
                if (value.contains("_")) {
                    String[] parts = value.split("_");
                    return new Locale(parts[0], parts[1]);
                } else {
                    return new Locale(value);
                }
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String jsonString = "en_US";
        Locale expected = Locale.US;
        Locale actual = null;

        try {
            actual = deserializer.deserialize(mapper.getFactory().createParser(jsonString), new DeserializationContext());
        } catch (IOException e) {
            Assert.fail("IOException should not be thrown");
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeCharset() {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected Charset _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return Charset.forName(value);
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String jsonString = "UTF-8";
        Charset expected = Charset.forName("UTF-8");
        Charset actual = null;

        try {
            Object readValue = mapper.readValue(new ByteArrayInputStream(jsonString.getBytes()), Charset.class);
            actual = (Charset) readValue;
        } catch (IOException e) {
            Assert.fail("IOException should not be thrown");
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeTimeZone() {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std() {
            @Override
            protected TimeZone _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return TimeZone.getTimeZone(value);
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String jsonString = "America/Los_Angeles";
        TimeZone expected = TimeZone.getTimeZone("America/Los_Angeles");
        TimeZone actual = null;

        try {
            actual = deserializer.deserialize(mapper.getFactory().createParser(jsonString), new DeserializationContext());
        } catch (IOException e) {
            Assert.fail("IOException should not be thrown");
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeInvalidString() {
        FromStringDeserializer deserializer = new FromStringDeserializer<Object>() {
            @Override
            protected Object _deserialize(String value, DeserializationContext ctxt) throws IOException {
                throw new IllegalArgumentException("Invalid String: " + value);
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String jsonString = "invalid";

        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(mapper.getFactory().createParser(jsonString), new DeserializationContext()));
    }

    @Test
    public void testDeserializeNullString() {
        FromStringDeserializer deserializer = new FromStringDeserializer<Object>() {
            @Override
            protected Object _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String jsonString = null;

        assertNull(deserializer.deserialize(mapper.getFactory().createParser(jsonString), new DeserializationContext()));
    }

    @Test
    public void testDeserializeEmptyArray() {
        FromStringDeserializer deserializer = new FromStringDeserializer<Object>() {
            @Override
            protected Object _deserialize(String value, DeserializationContext ctxt) throws IOException {
                if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
                    return deserialize(ctxt.getParser(), ctxt);
                } else {
                    throw new JsonMappingException(JsonMappingException.RootValueException.class, "Expected single value but found empty array");
                }
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String jsonString = "[ ]";

        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(mapper.getFactory().createParser(jsonString), new DeserializationContext()));
    }

    @Test
    public void testDeserializeInvalidJson() {
        FromStringDeserializer deserializer = new FromStringDeserializer<Object>() {
            @Override
            protected Object _deserialize(String value, DeserializationContext ctxt) throws IOException {
                throw new JsonMappingException("Invalid JSON: " + value);
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String jsonString = "{";

        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(mapper.getFactory().createParser(jsonString), new DeserializationContext()));
    }

    
}