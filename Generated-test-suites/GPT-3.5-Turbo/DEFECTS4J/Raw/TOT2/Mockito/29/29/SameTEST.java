import static org.junit.Assert.*;
import org.junit.Test;

public class SameTest {

    @Test
    public void testMatches() {
        Object obj = new Object();
        Same same = new Same(obj);
        
        assertTrue(same.matches(obj));
        assertFalse(same.matches(new Object()));
    }

    @Test
    public void testDescribeTo() {
        Object obj = "test";
        Same same = new Same(obj);
        
        Description description = new StringDescription();
        same.describeTo(description);
        
        assertEquals("same(\"test\")", description.toString());
    }

    @Test
    public void testAppendQuoting() {
        Same same = new Same("test");
        
        Description description = new StringDescription();
        same.appendQuoting(description);
        
        assertEquals("\"", description.toString());
    }

}