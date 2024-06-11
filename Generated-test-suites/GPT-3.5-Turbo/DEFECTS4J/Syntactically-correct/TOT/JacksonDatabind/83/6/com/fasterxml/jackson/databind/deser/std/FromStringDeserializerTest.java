import static org.junit.Assert.*;
import org.junit.Test;

public class FromStringDeserializerTest {

    // Test cases for deserialize() method

    @Test
    public void testDeserialize_typicalCase() {
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class) {
            protected String _deserialize(String value, DeserializationContext ctxt) {
                return value.toUpperCase();
            }
        };

        String json = "\"test\"";
        JsonParser parser = new JsonParser(json);
        DeserializationContext context = new DeserializationContext();

        String result = deserializer.deserialize(parser, context);

        assertEquals("TEST", result);
    }

    @Test
    public void testDeserialize_emptyString() {
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class) {
            protected String _deserialize(String value, DeserializationContext ctxt) {
                return "DEFAULT";
            }

            protected String _deserializeFromEmptyString() {
                return "EMPTY";
            }
        };

        String json = "\"\"";
        JsonParser parser = new JsonParser(json);
        DeserializationContext context = new DeserializationContext();

        String result = deserializer.deserialize(parser, context);

        assertEquals("EMPTY", result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_invalidValue() {
        FromStringDeserializer<Integer> deserializer = new FromStringDeserializer<Integer>(Integer.class) {
            protected Integer _deserialize(String value, DeserializationContext ctxt) throws IOException {
                throw new IOException("Invalid number format");
            }
        };

        String json = "\"123\"";
        JsonParser parser = new JsonParser(json);
        DeserializationContext context = new DeserializationContext();

        deserializer.deserialize(parser, context);
    }

    // Test cases for _deserialize() method

    @Test
    public void test_deserialize_typicalCase() {
        FromStringDeserializer<URL> deserializer = new FromStringDeserializer<URL>(URL.class) {
            protected URL _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return new URL(value);
            }
        };

        String value = "https://example.com";
        DeserializationContext context = new DeserializationContext();

        URL result = deserializer._deserialize(value, context);

        assertEquals(value, result.toString());
    }

    @Test(expected = MalformedURLException.class)
    public void test_deserialize_malformedURL() {
        FromStringDeserializer<URL> deserializer = new FromStringDeserializer<URL>(URL.class) {
            protected URL _deserialize(String value, DeserializationContext ctxt) throws IOException {
                throw new MalformedURLException();
            }
        };

        String value = "invalid_url";
        DeserializationContext context = new DeserializationContext();

        deserializer._deserialize(value, context);
    }

    // Test cases for _deserializeFromEmptyString() method

    @Test
    public void test_deserializeFromEmptyString_defaultValue() {
        FromStringDeserializer<Integer> deserializer = new FromStringDeserializer<Integer>(Integer.class) {
            protected Integer _deserializeFromEmptyString() {
                return 0;
            }
        };

        Integer result = deserializer._deserializeFromEmptyString();

        assertEquals(0, result.intValue());
    }

    // Test cases for _deserializeEmbedded() method

    @Test(expected = JsonMappingException.class)
    public void test_deserializeEmbedded_unknownType() {
        FromStringDeserializer<TestClass> deserializer = new FromStringDeserializer<TestClass>(TestClass.class) {
            protected TestClass _deserializeEmbedded(Object ob, DeserializationContext ctxt) throws IOException {
                throw new IOException("Unknown embedded type");
            }
        };

        TestClass obj = new TestClass();
        DeserializationContext context = new DeserializationContext();

        deserializer._deserializeEmbedded(obj, context);
    }

    // Test cases for _firstHyphenOrUnderscore() method

    @Test
    public void test_firstHyphenOrUnderscore_singleHyphen() {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(String.class, 0);

        String str = "test-string";
        int result = stdDeserializer._firstHyphenOrUnderscore(str);

        assertEquals(4, result);
    }

    @Test
    public void test_firstHyphenOrUnderscore_multipleHyphens() {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(String.class, 0);

        String str = "test-string-test";
        int result = stdDeserializer._firstHyphenOrUnderscore(str);

        assertEquals(4, result);
    }

    @Test
    public void test_firstHyphenOrUnderscore_singleUnderscore() {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(String.class, 0);

        String str = "test_string";
        int result = stdDeserializer._firstHyphenOrUnderscore(str);

        assertEquals(4, result);
    }

    @Test
    public void test_firstHyphenOrUnderscore_multipleUnderscores() {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(String.class, 0);

        String str = "test_string_test";
        int result = stdDeserializer._firstHyphenOrUnderscore(str);

        assertEquals(4, result);
    }

    @Test
    public void test_firstHyphenOrUnderscore_noHyphenOrUnderscore() {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(String.class, 0);

        String str = "test";
        int result = stdDeserializer._firstHyphenOrUnderscore(str);

        assertEquals(-1, result);
    }

    @Test
    public void test_firstHyphenOrUnderscore_emptyString() {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(String.class, 0);

        String str = "";
        int result = stdDeserializer._firstHyphenOrUnderscore(str);

        assertEquals(-1, result);
    }

    // Test cases for Std class

    @Test
    public void test_Std_deserialize_typicalCase() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 0);

        String value = "test";
        DeserializationContext context = new DeserializationContext();

        Object result = deserializer._deserialize(value, context);

        assertEquals(value, result);
    }

    @Test
    public void test_Std_deserialize_emptyString() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(String.class, 0);

        String value = "";
        DeserializationContext context = new DeserializationContext();

        Object result = deserializer._deserialize(value, context);

        assertNull(result);
    }

    @Test
    public void test_Std_deserializeFromEmptyString_URI() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);

        Object result = deserializer._deserializeFromEmptyString();

        assertEquals(URI.create(""), result);
    }

    @Test
    public void test_Std_deserializeFromEmptyString_Locale() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, FromStringDeserializer.Std.STD_LOCALE);

        Object result = deserializer._deserializeFromEmptyString();

        assertEquals(Locale.ROOT, result);
    }

    @Test
    public void test_Std_deserializeFromEmptyString_StringBuilder() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(StringBuilder.class, FromStringDeserializer.Std.STD_STRING_BUILDER);

        Object result = deserializer._deserializeFromEmptyString();

        assertNotNull(result);
        assertEquals("", result.toString());
    }

}