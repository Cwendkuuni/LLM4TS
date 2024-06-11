import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    // Test case for empty JSON Pointer
    @Test
    public void testEmptyJsonPointer() {
        String input = "";
        JsonPointer pointer = JsonPointer.compile(input);
        
        // Test matching
        assertTrue(pointer.matches());
        
        // Test getMatchingProperty()
        assertNull(pointer.getMatchingProperty());
        
        // Test getMatchingIndex()
        assertEquals(-1, pointer.getMatchingIndex());
        
        // Test mayMatchProperty()
        assertFalse(pointer.mayMatchProperty());
        
        // Test mayMatchElement()
        assertFalse(pointer.mayMatchElement());
        
        // Test matchProperty()
        assertNull(pointer.matchProperty("name"));
        
        // Test matchElement()
        assertNull(pointer.matchElement(0));
        
        // Test tail()
        assertNull(pointer.tail());
        
        // Test toString()
        assertEquals("", pointer.toString());
        
        // Test hashCode()
        assertEquals(0, pointer.hashCode());
        
        // Test equals()
        assertTrue(pointer.equals(JsonPointer.EMPTY));
    }
    
    // Test case for valid JSON Pointer with only property segments
    @Test
    public void testPropertyJsonPointer() {
        String input = "/employee/firstName";
        JsonPointer pointer = JsonPointer.compile(input);
        
        // Test matching
        assertFalse(pointer.matches());
        
        // Test getMatchingProperty()
        assertNull(pointer.getMatchingProperty());
        
        // Test getMatchingIndex()
        assertEquals(-1, pointer.getMatchingIndex());
        
        // Test mayMatchProperty()
        assertTrue(pointer.mayMatchProperty());
        
        // Test mayMatchElement()
        assertFalse(pointer.mayMatchElement());
        
        // Test matchProperty()
        assertEquals(JsonPointer.compile("/firstName"), pointer.matchProperty("firstName"));
        assertNull(pointer.matchProperty("lastName"));
        
        // Test matchElement()
        assertNull(pointer.matchElement(0));
        
        // Test tail()
        assertNotNull(pointer.tail());
        assertEquals("/firstName", pointer.tail().toString());
        
        // Test toString()
        assertEquals("/employee/firstName", pointer.toString());
        
        // Test hashCode()
        assertEquals(input.hashCode(), pointer.hashCode());
        
        // Test equals()
        assertTrue(pointer.equals(JsonPointer.compile(input)));
        assertFalse(pointer.equals(JsonPointer.compile("/lastName")));
    }
    
    // Test case for valid JSON Pointer with only array index segments
    @Test
    public void testArrayIndexJsonPointer() {
        String input = "/employees/0";
        JsonPointer pointer = JsonPointer.compile(input);
        
        // Test matching
        assertFalse(pointer.matches());
        
        // Test getMatchingProperty()
        assertNull(pointer.getMatchingProperty());
        
        // Test getMatchingIndex()
        assertEquals(-1, pointer.getMatchingIndex());
        
        // Test mayMatchProperty()
        assertFalse(pointer.mayMatchProperty());
        
        // Test mayMatchElement()
        assertTrue(pointer.mayMatchElement());
        
        // Test matchProperty()
        assertNull(pointer.matchProperty("firstName"));
        
        // Test matchElement()
        assertEquals(JsonPointer.compile("/0"), pointer.matchElement(0));
        assertNull(pointer.matchElement(1));
        
        // Test tail()
        assertNotNull(pointer.tail());
        assertEquals("/0", pointer.tail().toString());
        
        // Test toString()
        assertEquals("/employees/0", pointer.toString());
        
        // Test hashCode()
        assertEquals(input.hashCode(), pointer.hashCode());
        
        // Test equals()
        assertTrue(pointer.equals(JsonPointer.compile(input)));
        assertFalse(pointer.equals(JsonPointer.compile("/employees/1")));
    }
    
    // Test case for valid JSON Pointer with mixed property and array index segments
    @Test
    public void testMixedJsonPointer() {
        String input = "/company/employees/0/details/firstName";
        JsonPointer pointer = JsonPointer.compile(input);
        
        // Test matching
        assertFalse(pointer.matches());
        
        // Test getMatchingProperty()
        assertNull(pointer.getMatchingProperty());
        
        // Test getMatchingIndex()
        assertEquals(-1, pointer.getMatchingIndex());
        
        // Test mayMatchProperty()
        assertTrue(pointer.mayMatchProperty());
        
        // Test mayMatchElement()
        assertFalse(pointer.mayMatchElement());
        
        // Test matchProperty()
        assertEquals(JsonPointer.compile("/employees/0/details/firstName"), pointer.matchProperty("firstName"));
        assertNull(pointer.matchProperty("lastName"));
        
        // Test matchElement()
        assertNull(pointer.matchElement(0));
        
        // Test tail()
        assertNotNull(pointer.tail());
        assertEquals("/employees/0/details/firstName", pointer.tail().toString());
        
        // Test toString()
        assertEquals("/company/employees/0/details/firstName", pointer.toString());
        
        // Test hashCode()
        assertEquals(input.hashCode(), pointer.hashCode());
        
        // Test equals()
        assertTrue(pointer.equals(JsonPointer.compile(input)));
        assertFalse(pointer.equals(JsonPointer.compile("/company/employees/1/details/firstName")));
    }
    
    // Test case for invalid JSON Pointer without leading "/"
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidJsonPointerWithoutLeadingSlash() {
        String input = "employee/firstName";
        JsonPointer.compile(input);
    }
    
    // Test case for invalid JSON Pointer with empty input
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidJsonPointerWithEmptyInput() {
        String input = "";
        JsonPointer.compile(input);
    }
    
    // Test case for JSON Pointer with invalid property name
    @Test
    public void testInvalidPropertyJsonPointer() {
        String input = "/employee/firstName";
        JsonPointer pointer = JsonPointer.compile(input);
        
        // Test matchProperty()
        assertNull(pointer.matchProperty("lastName"));
        
        // Test tail()
        assertNotNull(pointer.tail());
        assertEquals("/firstName", pointer.tail().toString());
    }
    
    // Test case for JSON Pointer with invalid array index
    @Test
    public void testInvalidArrayIndexJsonPointer() {
        String input = "/employees/0";
        JsonPointer pointer = JsonPointer.compile(input);
        
        // Test matchElement()
        assertNull(pointer.matchElement(1));
        
        // Test tail()
        assertNotNull(pointer.tail());
        assertEquals("/0", pointer.tail().toString());
    }
    
    // Test case for JSON Pointer with invalid escape sequence
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEscapeSequenceJsonPointer() {
        String input = "/company/~invalid/firstName";
        JsonPointer.compile(input);
    }
}