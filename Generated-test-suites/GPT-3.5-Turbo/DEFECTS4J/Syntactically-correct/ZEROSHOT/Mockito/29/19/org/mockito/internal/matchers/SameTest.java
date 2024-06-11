import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.matchers.Same;

public class SameTest {
    
    @Test
    public void testMatches() {
        Same same = new Same("Hello");
        assertTrue(same.matches("Hello"));
        assertFalse(same.matches("World"));
    }
    
    @Test
    public void testDescribeTo() {
        Same same = new Same("Hello");
        Description description = new Description();

        same.describeTo(description);
        
        assertEquals("same(\"Hello\")", description.toString());
    }
    
    @Test
    public void testAppendQuoting() {
        Same same1 = new Same("Hello");
        Same same2 = new Same('W');
        Description description1 = new Description();
        Description description2 = new Description();

        same1.appendQuoting(description1);
        same2.appendQuoting(description2);
        
        assertEquals("\"", description1.toString());
        assertEquals("'", description2.toString());
    }
}