import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;
import org.junit.Assert;
import org.junit.Test;

public class EnumSerializerTest {

    @Test
    public void testSerialize_index() throws Exception {
        EnumValues enumValues = EnumValues.constructFromName(EnumClass.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
        ObjectMapper objectMapper = new ObjectMapper();
        
        // Define the expected JSON output
        String expected = "1";
        
        // Serialize an enum instance
        EnumClass enumInstance = EnumClass.VALUE_ONE;
        String result = objectMapper.writeValueAsString(enumInstance);
        
        // Compare the expected and actual JSON output
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testSerialize_toString() throws Exception {
        EnumValues enumValues = EnumValues.constructFromName(EnumClass.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        
        // Define the expected JSON output
        String expected = "\"VALUE_ONE\"";
        
        // Serialize an enum instance
        EnumClass enumInstance = EnumClass.VALUE_ONE;
        String result = objectMapper.writeValueAsString(enumInstance);
        
        // Compare the expected and actual JSON output
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testSerialize_customToString() throws Exception {
        EnumValues enumValues = EnumValues.constructFromToString(EnumClass.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        ObjectMapper objectMapper = new ObjectMapper();
        
        // Define the expected JSON output
        String expected = "\"CustomValueOne\"";
        
        // Serialize an enum instance with custom toString()
        EnumClass enumInstance = EnumClass.CUSTOM_VALUE_ONE;
        String result = objectMapper.writeValueAsString(enumInstance);
        
        // Compare the expected and actual JSON output
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testSerialize_customStringValue() throws Exception {
        EnumValues enumValues = EnumValues.constructFromCustomValue(EnumClass.class, "StringValue");
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        ObjectMapper objectMapper = new ObjectMapper();
        
        // Define the expected JSON output
        String expected = "\"StringValue\"";
        
        // Serialize an enum instance with custom string value
        EnumClass enumInstance = EnumClass.CUSTOM_VALUE_TWO;
        String result = objectMapper.writeValueAsString(enumInstance);
        
        // Compare the expected and actual JSON output
        Assert.assertEquals(expected, result);
    }

    private enum EnumClass {
        VALUE_ONE,
        VALUE_TWO,
        CUSTOM_VALUE_ONE {
            @Override
            public String toString() {
                return "CustomValueOne";
            }
        },
        CUSTOM_VALUE_TWO("StringValue");

        private final String stringValue;

        EnumClass() {
            this.stringValue = toString();
        }

        EnumClass(String stringValue) {
            this.stringValue = stringValue;
        }

        @Override
        public String toString() {
            return stringValue;
        }
    }
}