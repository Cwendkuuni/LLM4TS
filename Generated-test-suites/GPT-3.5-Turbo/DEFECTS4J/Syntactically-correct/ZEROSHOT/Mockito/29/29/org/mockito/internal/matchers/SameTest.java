import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.matchers.Same;

public class SameTest {
    
    @Test
    public void testMatches() {
        Object object1 = new Object();
        Object object2 = new Object();
        Same same = new Same(object1);
        assertFalse(same.matches(object2));
    }
    
    @Test
    public void testDescribeTo() {
        Object object = new Object();
        Same same = new Same(object);
        Description description = new Description();
        same.describeTo(description);
        assertEquals("same(" + object.toString() + ")", description.toString());
    }

}