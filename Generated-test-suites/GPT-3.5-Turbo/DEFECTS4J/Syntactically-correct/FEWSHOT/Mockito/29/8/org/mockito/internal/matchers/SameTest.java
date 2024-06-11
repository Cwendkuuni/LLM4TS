import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class SameTest {
    @Test
    public void testMatches() {
        Same same = new Same("test");
        
        assertEquals(true, same.matches("test"));
        assertEquals(false, same.matches("diff"));
        assertEquals(false, same.matches(null));
    }
    
    @Test
    public void testDescribeTo() {
        Same same = new Same("test");
        
        Description description = new Description();
        same.describeTo(description);
        
        assertEquals("same(\"test\")", description.toString());
    }
    
    @Test
    public void testAppendQuoting() {
        Same sameString = new Same("test");
        Same sameChar = new Same('t');
        
        Description descriptionString = new Description();
        Description descriptionChar = new Description();
        
        sameString.appendQuoting(descriptionString);
        sameChar.appendQuoting(descriptionChar);
        
        assertEquals("\"", descriptionString.toString());
        assertEquals("'", descriptionChar.toString());
    }
    
    @Test
    public void testEquals() {
        Same same1 = new Same("test");
        Same same2 = new Same("test");
        Same same3 = new Same("diff");
        
        assertEquals(true, same1.equals(same2));
        assertEquals(false, same1.equals(same3));
    }
    
    @Test
    public void testNotEquals() {
        Same same1 = new Same("test");
        Same same2 = new Same("test");
        Same same3 = new Same("diff");
        
        assertNotEquals(true, same1.equals(same3));
        assertNotEquals(true, same2.equals(same3));
    }
}