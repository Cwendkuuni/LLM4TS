import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testEmptyConstructor() {
        JsonPointer pointer = new JsonPointer();
        assertNotNull(pointer);
    }
    
    @Test
    public void testCompileValidInput() {
        JsonPointer pointer = JsonPointer.compile("/property");
        assertNotNull(pointer);
    }
    
    @Test
    public void testCompileInvalidInput() {
        try {
            JsonPointer pointer = JsonPointer.compile("property");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    
    @Test
    public void testValueOf() {
        JsonPointer pointer = JsonPointer.valueOf("/index");
        assertNotNull(pointer);
    }
    
    @Test
    public void testMalformedIndexParsing() {
        int index = JsonPointer._parseIndex("abc");
        assertEquals(-1, index);
    }
    
    @Test
    public void testQuotedStringParsing() {
        JsonPointer pointer = JsonPointer.compile("/~1value/~0index");
        assertNotNull(pointer);
    }
    
    @Test
    public void testMatches() {
        JsonPointer pointer = JsonPointer.compile("/property");
        assertTrue(pointer.matches());
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/property");
        JsonPointer result = pointer.matchProperty("property");
        assertNotNull(result);
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/0");
        JsonPointer result = pointer.matchElement(0);
        assertNotNull(result);
    }
    
    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.compile("/property/0");
        JsonPointer tail = pointer.tail();
        assertNotNull(tail);
    }
    
    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.compile("/property");
        JsonPointer pointer2 = JsonPointer.compile("/property");
        assertEquals(pointer1, pointer2);
    }

}