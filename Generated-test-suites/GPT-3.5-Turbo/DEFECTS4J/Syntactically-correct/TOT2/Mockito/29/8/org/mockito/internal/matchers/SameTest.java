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
        Same same = new Same("test");
        Description description = new Description();
        same.describeTo(description);
        assertEquals(description.toString(), "same(\"test\")");
    }

    @Test
    public void testAppendQuoting() {
        Same same1 = new Same("test");
        Description description1 = new Description();
        same1.appendQuoting(description1);
        assertEquals(description1.toString(), "\"");

        Same same2 = new Same('c');
        Description description2 = new Description();
        same2.appendQuoting(description2);
        assertEquals(description2.toString(), "'");
    }
}