import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FromStringDeserializerTest {
    
    private FromStringDeserializer<String> stringDeserializer;
    private FromStringDeserializer<Integer> integerDeserializer;
    private FromStringDeserializer<Boolean> booleanDeserializer;
    
    @Before
    public void setup() {
        // Create instances of the FromStringDeserializer for String, Integer, and Boolean classes
        stringDeserializer = new FromStringDeserializer<String>(String.class) {
            protected String _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return value;
            }
        };
        integerDeserializer = new FromStringDeserializer<Integer>(Integer.class) {
            protected Integer _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return Integer.parseInt(value);
            }
        };
        booleanDeserializer = new FromStringDeserializer<Boolean>(Boolean.class) {
            protected Boolean _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return Boolean.parseBoolean(value);
            }
        };
    }
    
    @Test
    public void testStringDeserializer() {
        // Typical use case
        String input1 = "Hello World";
        assertEquals(input1, stringDeserializer.deserialize(JsonParser.parseString(input1), DeserializationContext));
        
        // Edge case
        String input2 = "";
        assertEquals(input2, stringDeserializer.deserialize(JsonParser.parseString(input2), DeserializationContext));
        
        // Error scenario
        String input3 = null;
        try {
            stringDeserializer.deserialize(JsonParser.parseString(input3), DeserializationContext);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Exception is expected
        }
    }
    
    @Test
    public void testIntegerDeserializer() {
        // Typical use case
        String input1 = "42";
        assertEquals(Integer.valueOf(input1), integerDeserializer.deserialize(JsonParser.parseString(input1), DeserializationContext));
        
        // Edge case
        String input2 = "0";
        assertEquals(Integer.valueOf(input2), integerDeserializer.deserialize(JsonParser.parseString(input2), DeserializationContext));
        
        // Error scenario
        String input3 = "abc";
        try {
            integerDeserializer.deserialize(JsonParser.parseString(input3), DeserializationContext);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Exception is expected
        }
    }
    
    @Test
    public void testBooleanDeserializer() {
        // Typical use case
        String input1 = "true";
        assertEquals(Boolean.valueOf(input1), booleanDeserializer.deserialize(JsonParser.parseString(input1), DeserializationContext));
        
        // Edge case
        String input2 = "false";
        assertEquals(Boolean.valueOf(input2), booleanDeserializer.deserialize(JsonParser.parseString(input2), DeserializationContext));
        
        // Error scenario
        String input3 = "abc";
        try {
            booleanDeserializer.deserialize(JsonParser.parseString(input3), DeserializationContext);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Exception is expected
        }
    }
}