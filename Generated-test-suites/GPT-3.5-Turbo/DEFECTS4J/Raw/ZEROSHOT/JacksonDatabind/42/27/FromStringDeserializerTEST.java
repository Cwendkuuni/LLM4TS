import static org.junit.Assert.*;
import org.junit.Test;

public class FromStringDeserializerTest {

    @Test
    public void testDeserialize() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(String.class, 0);
        assertEquals("test", deserializer.deserialize(new JsonParser(), new DeserializationContext()));
    }

    @Test
    public void testDeserialize_EmptyString() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(String.class, 0);
        assertEquals("", deserializer.deserialize(new JsonParser(), new DeserializationContext()));
    }

    @Test
    public void test_deserialize_invalid_text() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(String.class, 0);
        try {
            deserializer.deserialize(new JsonParser(), new DeserializationContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException ex) {
            // expected
        }
    }

    @Test
    public void test_deserialize_null_current_token() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(String.class, 0);
        try {
            deserializer.deserialize(new JsonParser(null), new DeserializationContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException ex) {
            // expected
        }
    }

    @Test
    public void test_deserialize_value_embedded_object() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(String.class, 0);
        try {
            deserializer.deserialize(new JsonParser(), new DeserializationContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException ex) {
            // expected
        }
    }

    @Test
    public void test_deserialize_unknown_kind() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(String.class, -1);
        try {
            deserializer.deserialize(new JsonParser(), new DeserializationContext());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    @Test
    public void test_deserialize_valid_text() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(String.class, 0);
        assertEquals("test", deserializer.deserialize(new JsonParser(), new DeserializationContext()));
    }

    @Test
    public void test_deserialize_valid_text_with_empty_string() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(String.class, 0);
        assertEquals("", deserializer.deserialize(new JsonParser(), new DeserializationContext()));
    }

    @Test
    public void test_deserialize_from_empty_string() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(String.class, 0);
        assertNull(deserializer._deserializeFromEmptyString());
    }
}