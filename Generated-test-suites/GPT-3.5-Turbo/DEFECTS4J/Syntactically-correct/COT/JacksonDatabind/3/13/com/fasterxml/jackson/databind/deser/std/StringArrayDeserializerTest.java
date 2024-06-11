
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

public class StringArrayDeserializerTest {

    private StringArrayDeserializer stringArrayDeserializer;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;

    @Before
    public void setUp() {
        stringArrayDeserializer = new StringArrayDeserializer();
        jsonParser = new JsonParser();
        deserializationContext = new DeserializationContext();
    }
    
    @Test
    public void testDeserialize() throws IOException {
        String[] expected = {"value1", "value2"};
        jsonParser.setStrings("value1", "value2");
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testDeserializeWithNulls() throws IOException {
        String[] expected = {"value1", null, "value3"};
        jsonParser.setStrings("value1", null, "value3");
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testDeserializeWithEmptyArray() throws IOException {
        String[] expected = {};
        jsonParser.setStrings();
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testDeserializeWithType() throws IOException {
        String[] expected = {"value1", "value2"};
        jsonParser.setStrings("value1", "value2");
        Object result = stringArrayDeserializer.deserializeWithType(jsonParser, deserializationContext, null);
        assertTrue(result instanceof String[]);
        assertArrayEquals(expected, (String[]) result);
    }
    
    @Test
    public void testDeserializeWithCustomDeserializer() throws IOException {
        JsonDeserializer<String> customDeserializer = new CustomStringDeserializer();
        StringArrayDeserializer stringArrayDeserializerWithCustom = new StringArrayDeserializer(customDeserializer);
        String[] expected = {"customValue1", "customValue2"};
        jsonParser.setStrings("customValue1", "customValue2");
        stringArrayDeserializerWithCustom._elementDeserializer = customDeserializer;
        String[] result = stringArrayDeserializerWithCustom.deserialize(jsonParser, deserializationContext);
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testCreateContextual() throws IOException {
        assertNull(stringArrayDeserializer.createContextual(deserializationContext, null));
    }
    
    private class CustomStringDeserializer extends JsonDeserializer<String> {

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getText().toUpperCase();
        }
        
    }
}
