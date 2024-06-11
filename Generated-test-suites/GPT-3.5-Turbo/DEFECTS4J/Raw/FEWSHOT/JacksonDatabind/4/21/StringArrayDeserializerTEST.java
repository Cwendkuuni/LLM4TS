import static org.junit.Assert.*;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() {
        // Test Case 1
        JsonParser jp1 = null; // replace with actual JsonParser object
        DeserializationContext ctxt1 = null; // replace with actual DeserializationContext object
        StringArrayDeserializer instance1 = new StringArrayDeserializer();
        String[] expResult1 = null; // replace with expected result
        String[] result1 = instance1.deserialize(jp1, ctxt1);
        assertArrayEquals(expResult1, result1);
        
        // Test Case 2
        JsonParser jp2 = null; // replace with actual JsonParser object
        DeserializationContext ctxt2 = null; // replace with actual DeserializationContext object
        StringArrayDeserializer instance2 = new StringArrayDeserializer();
        String[] expResult2 = null; // replace with expected result
        String[] result2 = instance2.deserialize(jp2, ctxt2);
        assertArrayEquals(expResult2, result2);
        
        // Test Case 3
        JsonParser jp3 = null; // replace with actual JsonParser object
        DeserializationContext ctxt3 = null; // replace with actual DeserializationContext object
        StringArrayDeserializer instance3 = new StringArrayDeserializer();
        String[] expResult3 = null; // replace with expected result
        String[] result3 = instance3.deserialize(jp3, ctxt3);
        assertArrayEquals(expResult3, result3);
        
        // Test Case 4
        JsonParser jp4 = null; // replace with actual JsonParser object
        DeserializationContext ctxt4 = null; // replace with actual DeserializationContext object
        StringArrayDeserializer instance4 = new StringArrayDeserializer();
        String[] expResult4 = null; // replace with expected result
        String[] result4 = instance4.deserialize(jp4, ctxt4);
        assertArrayEquals(expResult4, result4);
    }

    @Test
    public void test_deserializeCustom() {
        // Test Case 1
        JsonParser jp1 = null; // replace with actual JsonParser object
        DeserializationContext ctxt1 = null; // replace with actual DeserializationContext object
        StringArrayDeserializer instance1 = new StringArrayDeserializer();
        String[] expResult1 = null; // replace with expected result
        String[] result1 = instance1._deserializeCustom(jp1, ctxt1);
        assertArrayEquals(expResult1, result1);
        
        // Test Case 2
        JsonParser jp2 = null; // replace with actual JsonParser object
        DeserializationContext ctxt2 = null; // replace with actual DeserializationContext object
        StringArrayDeserializer instance2 = new StringArrayDeserializer();
        String[] expResult2 = null; // replace with expected result
        String[] result2 = instance2._deserializeCustom(jp2, ctxt2);
        assertArrayEquals(expResult2, result2);
        
        // Test Case 3
        JsonParser jp3 = null; // replace with actual JsonParser object
        DeserializationContext ctxt3 = null; // replace with actual DeserializationContext object
        StringArrayDeserializer instance3 = new StringArrayDeserializer();
        String[] expResult3 = null; // replace with expected result
        String[] result3 = instance3._deserializeCustom(jp3, ctxt3);
        assertArrayEquals(expResult3, result3);
        
        // Test Case 4
        JsonParser jp4 = null; // replace with actual JsonParser object
        DeserializationContext ctxt4 = null; // replace with actual DeserializationContext object
        StringArrayDeserializer instance4 = new StringArrayDeserializer();
        String[] expResult4 = null; // replace with expected result
        String[] result4 = instance4._deserializeCustom(jp4, ctxt4);
        assertArrayEquals(expResult4, result4);
    }

    @Test
    public void testDeserializeWithType() {
        // Test Case 1
        JsonParser jp1 = null; // replace with actual JsonParser object
        DeserializationContext ctxt1 = null; // replace with actual DeserializationContext object
        TypeDeserializer typeDeserializer1 = null; // replace with actual TypeDeserializer object
        StringArrayDeserializer instance1 = new StringArrayDeserializer();
        Object expResult1 = null; // replace with expected result
        Object result1 = instance1.deserializeWithType(jp1, ctxt1, typeDeserializer1);
        assertEquals(expResult1, result1);
        
        // Test Case 2
        JsonParser jp2 = null; // replace with actual JsonParser object
        DeserializationContext ctxt2 = null; // replace with actual DeserializationContext object
        TypeDeserializer typeDeserializer2 = null; // replace with actual TypeDeserializer object
        StringArrayDeserializer instance2 = new StringArrayDeserializer();
        Object expResult2 = null; // replace with expected result
        Object result2 = instance2.deserializeWithType(jp2, ctxt2, typeDeserializer2);
        assertEquals(expResult2, result2);
        
        // Test Case 3
        JsonParser jp3 = null; // replace with actual JsonParser object
        DeserializationContext ctxt3 = null; // replace with actual DeserializationContext object
        TypeDeserializer typeDeserializer3 = null; // replace with actual TypeDeserializer object
        StringArrayDeserializer instance3 = new StringArrayDeserializer();
        Object expResult3 = null; // replace with expected result
        Object result3 = instance3.deserializeWithType(jp3, ctxt3, typeDeserializer3);
        assertEquals(expResult3, result3);
        
        // Test Case 4
        JsonParser jp4 = null; // replace with actual JsonParser object
        DeserializationContext ctxt4 = null; // replace with actual DeserializationContext object
        TypeDeserializer typeDeserializer4 = null; // replace with actual TypeDeserializer object
        StringArrayDeserializer instance4 = new StringArrayDeserializer();
        Object expResult4 = null; // replace with expected result
        Object result4 = instance4.deserializeWithType(jp4, ctxt4, typeDeserializer4);
        assertEquals(expResult4, result4);
    }

    @Test
    public void testHandleNonArray() {
        // Test Case 1
        JsonParser jp1 = null; // replace with actual JsonParser object
        DeserializationContext ctxt1 = null; // replace with actual DeserializationContext object
        StringArrayDeserializer instance1 = new StringArrayDeserializer();
        String[] expResult1 = null; // replace with expected result
        String[] result1 = instance1.handleNonArray(jp1, ctxt1);
        assertArrayEquals(expResult1, result1);
        
        // Test Case 2
        JsonParser jp2 = null; // replace with actual JsonParser object
        DeserializationContext ctxt2 = null; // replace with actual DeserializationContext object
        StringArrayDeserializer instance2 = new StringArrayDeserializer();
        String[] expResult2 = null; // replace with expected result
        String[] result2 = instance2.handleNonArray(jp2, ctxt2);
        assertArrayEquals(expResult2, result2);
        
        // Test Case 3
        JsonParser jp3 = null; // replace with actual JsonParser object
        DeserializationContext ctxt3 = null; // replace with actual DeserializationContext object
        StringArrayDeserializer instance3 = new StringArrayDeserializer();
        String[] expResult3 = null; // replace with expected result
        String[] result3 = instance3.handleNonArray(jp3, ctxt3);
        assertArrayEquals(expResult3, result3);
        
        // Test Case 4
        JsonParser jp4 = null; // replace with actual JsonParser object
        DeserializationContext ctxt4 = null; // replace with actual DeserializationContext object
        StringArrayDeserializer instance4 = new StringArrayDeserializer();
        String[] expResult4 = null; // replace with expected result
        String[] result4 = instance4.handleNonArray(jp4, ctxt4);
        assertArrayEquals(expResult4, result4);
    }

    @Test
    public void testCreateContextual() {
        // Test Case 1
        DeserializationContext ctxt1 = null; // replace with actual DeserializationContext object
        BeanProperty property1 = null; // replace with actual BeanProperty object
        StringArrayDeserializer instance1 = new StringArrayDeserializer();
        JsonDeserializer<?> expResult1 = null; // replace with expected result
        JsonDeserializer<?> result1 = instance1.createContextual(ctxt1, property1);
        assertEquals(expResult1, result1);
        
        // Test Case 2
        DeserializationContext ctxt2 = null; // replace with actual DeserializationContext object
        BeanProperty property2 = null; // replace with actual BeanProperty object
        StringArrayDeserializer instance2 = new StringArrayDeserializer();
        JsonDeserializer<?> expResult2 = null; // replace with expected result
        JsonDeserializer<?> result2 = instance2.createContextual(ctxt2, property2);
        assertEquals(expResult2, result2);
        
        // Test Case 3
        DeserializationContext ctxt3 = null; // replace with actual DeserializationContext object
        BeanProperty property3 = null; // replace with actual BeanProperty object
        StringArrayDeserializer instance3 = new StringArrayDeserializer();
        JsonDeserializer<?> expResult3 = null; // replace with expected result
        JsonDeserializer<?> result3 = instance3.createContextual(ctxt3, property3);
        assertEquals(expResult3, result3);
        
        // Test Case 4
        DeserializationContext ctxt4 = null; // replace with actual DeserializationContext object
        BeanProperty property4 = null; // replace with actual BeanProperty object
        StringArrayDeserializer instance4 = new StringArrayDeserializer();
        JsonDeserializer<?> expResult4 = null; // replace with expected result
        JsonDeserializer<?> result4 = instance4.createContextual(ctxt4, property4);
        assertEquals(expResult4, result4);
    }
}