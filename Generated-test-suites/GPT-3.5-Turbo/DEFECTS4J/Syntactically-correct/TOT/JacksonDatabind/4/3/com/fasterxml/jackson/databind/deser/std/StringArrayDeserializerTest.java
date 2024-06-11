import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;

public class StringArrayDeserializerTest {

    private StringArrayDeserializer deserializer = new StringArrayDeserializer();

    @Test
    public void testDeserialize() throws IOException {
        JsonParser jp = new JsonParser(null);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        
        // Test case 1: Empty array
        jp.nextToken();
        String[] result1 = deserializer.deserialize(jp, ctxt);
        assertNotNull(result1);
        assertEquals(0, result1.length);
        
        // Test case 2: Array with simple string values
        jp.nextToken();
        jp.nextToken();
        jp.nextToken();
        jp.nextToken();
        jp.nextToken();
        jp.nextToken();
        String[] result2 = deserializer.deserialize(jp, ctxt);
        assertNotNull(result2);
        assertEquals(3, result2.length);
        assertEquals("value1", result2[0]);
        assertEquals("value2", result2[1]);
        assertEquals("value3", result2[2]);
        
        // Test case 3: Array with null values
        jp.nextToken();
        jp.nextToken();
        jp.nextToken();
        jp.nextToken();
        String[] result3 = deserializer.deserialize(jp, ctxt);
        assertNotNull(result3);
        assertEquals(3, result3.length);
        assertNull(result3[0]);
        assertNull(result3[1]);
        assertNull(result3[2]);
        
        // Test case 4: Array with mixed string and null values
        jp.nextToken();
        jp.nextToken();
        jp.nextToken();
        jp.nextToken();
        jp.nextToken();
        jp.nextToken();
        String[] result4 = deserializer.deserialize(jp, ctxt);
        assertNotNull(result4);
        assertEquals(3, result4.length);
        assertEquals("value1", result4[0]);
        assertNull(result4[1]);
        assertNull(result4[2]);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser jp = new JsonParser(null);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        
        // Test case 1: Empty array
        jp.nextToken();
        Object result1 = deserializer.deserializeWithType(jp, ctxt, null);
        assertNotNull(result1);
        assertTrue(result1 instanceof String[]);
        String[] arrayResult1 = (String[]) result1;
        assertEquals(0, arrayResult1.length);
        
        // Test case 2: Array with simple string values
        jp.nextToken();
        jp.nextToken();
        jp.nextToken();
        jp.nextToken();
        jp.nextToken();
        jp.nextToken();
        Object result2 = deserializer.deserializeWithType(jp, ctxt, null);
        assertNotNull(result2);
        assertTrue(result2 instanceof String[]);
        String[] arrayResult2 = (String[]) result2;
        assertEquals(3, arrayResult2.length);
        assertEquals("value1", arrayResult2[0]);
        assertEquals("value2", arrayResult2[1]);
        assertEquals("value3", arrayResult2[2]);
        
        // Test case 3: Array with null values
        jp.nextToken();
        jp.nextToken();
        jp.nextToken();
        jp.nextToken();
        Object result3 = deserializer.deserializeWithType(jp, ctxt, null);
        assertNotNull(result3);
        assertTrue(result3 instanceof String[]);
        String[] arrayResult3 = (String[]) result3;
        assertEquals(3, arrayResult3.length);
        assertNull(arrayResult3[0]);
        assertNull(arrayResult3[1]);
        assertNull(arrayResult3[2]);
        
        // Test case 4: Array with mixed string and null values
        jp.nextToken();
        jp.nextToken();
        jp.nextToken();
        jp.nextToken();
        jp.nextToken();
        jp.nextToken();
        Object result4 = deserializer.deserializeWithType(jp, ctxt, null);
        assertNotNull(result4);
        assertTrue(result4 instanceof String[]);
        String[] arrayResult4 = (String[]) result4;
        assertEquals(3, arrayResult4.length);
        assertEquals("value1", arrayResult4[0]);
        assertNull(arrayResult4[1]);
        assertNull(arrayResult4[2]);
    }

    @Test
    public void testHandleNonArray() throws IOException {
        JsonParser jp = new JsonParser(null);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        
        // Test case 1: Accept single value as array disabled
        jp.nextToken(JsonToken.VALUE_STRING);
        try {
            deserializer.handleNonArray(jp, ctxt);
            fail("IOException should be thrown");
        } catch (IOException e) {
            // Expected IOException
        }
        
        // Test case 2: Empty String as null object accepted
        jp.nextToken(JsonToken.VALUE_STRING);
        jp.getText();
        assertNull(deserializer.handleNonArray(jp, ctxt));
        
        // Test case 3: Accept single value as array enabled
        jp.nextToken(JsonToken.VALUE_STRING);
        jp.nextToken(JsonToken.VALUE_TRUE);
        String[] result3 = deserializer.handleNonArray(jp, ctxt);
        assertNotNull(result3);
        assertEquals(1, result3.length);
        assertEquals("true", result3[0]);
        
        // Test case 4: Accept single value as array enabled with null value
        jp.nextToken(JsonToken.VALUE_NULL);
        String[] result4 = deserializer.handleNonArray(jp, ctxt);
        assertNotNull(result4);
        assertEquals(1, result4.length);
        assertNull(result4[0]);
        
        // Test case 5: Accept single value as array enabled with null value
        jp.nextToken(JsonToken.VALUE_FALSE);
        String[] result5 = deserializer.handleNonArray(jp, ctxt);
        assertNotNull(result5);
        assertEquals(1, result5.length);
        assertEquals("false", result5[0]);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        
        // Test case 1: No element deserializer
        assertNull(deserializer.createContextual(ctxt, null));
        
        // Test case 2: Element deserializer is the default String deserializer
        JsonDeserializer<?> elementDeserializer2 = new StringDeserializer();
        assertSame(deserializer, deserializer.createContextual(ctxt, null));
        
        // Test case 3: Element deserializer is not the default String deserializer
        JsonDeserializer<?> elementDeserializer3 = new CustomStringDeserializer();
        StringArrayDeserializer deserializer3 = (StringArrayDeserializer) deserializer.createContextual(ctxt, null);
        assertNotSame(deserializer, deserializer3);
        assertSame(elementDeserializer3, deserializer3.getElementDeserializer());
    }
}