import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyConstructor() {
        JsonPointer pointer = new JsonPointer();
        
        assertNull(pointer._nextSegment);
        assertEquals("", pointer._matchingPropertyName);
        assertEquals(-1, pointer._matchingElementIndex);
        assertEquals("", pointer._asString);
    }
    
    @Test
    public void testFullConstructor() {
        String fullString = "";
        String segment = "";
        JsonPointer next = new JsonPointer();
        
        JsonPointer pointer = new JsonPointer(fullString, segment, next);
        
        assertEquals(fullString, pointer._asString);
        assertEquals(segment, pointer._matchingPropertyName);
        assertEquals(-1, pointer._matchingElementIndex);
        assertEquals(next, pointer._nextSegment);
    }
    
    @Test
    public void testCompile() {
        String input = "/";
        
        JsonPointer pointer = JsonPointer.compile(input);
        
        assertNotNull(pointer);
        assertEquals("", pointer._asString);
        assertEquals("", pointer._matchingPropertyName);
        assertEquals(-1, pointer._matchingElementIndex);
        assertNull(pointer._nextSegment);
    }
    
    @Test
    public void testValueOf() {
        String input = "/";
        
        JsonPointer pointer = JsonPointer.valueOf(input);
        
        assertNotNull(pointer);
        assertEquals("", pointer._asString);
        assertEquals("", pointer._matchingPropertyName);
        assertEquals(-1, pointer._matchingElementIndex);
        assertNull(pointer._nextSegment);
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer next = new JsonPointer();
        JsonPointer pointer = new JsonPointer("", "", next);
        
        String name = "";
        
        JsonPointer result = pointer.matchProperty(name);
        
        assertEquals(next, result);
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer next = new JsonPointer();
        JsonPointer pointer = new JsonPointer("", "", next);
        
        int index = -1;
        
        JsonPointer result = pointer.matchElement(index);
        
        assertEquals(next, result);
    }
    
    @Test
    public void testTail() {
        JsonPointer next = new JsonPointer();
        JsonPointer pointer = new JsonPointer("", "", next);
        
        JsonPointer result = pointer.tail();
        
        assertEquals(next, result);
    }
    
    @Test
    public void testParseIndex_Valid() {
        String str = "123";
        
        int result = pointer._parseIndex(str);
        
        assertEquals(123, result);
    }
    
    @Test
    public void testParseIndex_Invalid() {
        String str = "abc";
        
        int result = pointer._parseIndex(str);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testEquals_SameObject() {
        JsonPointer pointer = new JsonPointer();
        
        assertTrue(pointer.equals(pointer));
    }
    
    @Test
    public void testEquals_NullObject() {
        JsonPointer pointer = new JsonPointer();
        
        assertFalse(pointer.equals(null));
    }
    
    @Test
    public void testEquals_DifferentClass() {
        JsonPointer pointer = new JsonPointer();
        
        assertFalse(pointer.equals("string"));
    }
    
    @Test
    public void testEquals_DifferentValues() {
        JsonPointer pointer1 = new JsonPointer("", "", null);
        JsonPointer pointer2 = new JsonPointer("", "name", null);
        
        assertFalse(pointer1.equals(pointer2));
    }
    
    @Test
    public void testHashCode() {
        JsonPointer pointer = new JsonPointer();
        
        assertNotNull(pointer.hashCode());
    }
    
    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer();
        
        assertNotNull(pointer.toString());
    }
}