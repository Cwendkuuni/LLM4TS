
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.core.JsonPointer;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testMatches() {
        JsonPointer pointer = new JsonPointer();
        boolean expected = true;
        boolean actual = pointer.matches();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer();
        String expected = "";
        String actual = pointer.getMatchingProperty();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer();
        int expected = -1;
        int actual = pointer.getMatchingIndex();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        boolean expected = true;
        boolean actual = pointer.mayMatchProperty();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer();
        boolean expected = false;
        boolean actual = pointer.mayMatchElement();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        String name = "";
        JsonPointer expected = null;
        JsonPointer actual = pointer.matchProperty(name);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer();
        int index = 0;
        JsonPointer expected = null;
        JsonPointer actual = pointer.matchElement(index);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer();
        JsonPointer expected = null;
        JsonPointer actual = pointer.tail();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCompile() {
        String input = "";
        JsonPointer expected = new JsonPointer();
        JsonPointer actual = JsonPointer.compile(input);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testValueOf() {
        String input = "";
        JsonPointer expected = new JsonPointer();
        JsonPointer actual = JsonPointer.valueOf(input);
        assertEquals(expected, actual);
    }

}
