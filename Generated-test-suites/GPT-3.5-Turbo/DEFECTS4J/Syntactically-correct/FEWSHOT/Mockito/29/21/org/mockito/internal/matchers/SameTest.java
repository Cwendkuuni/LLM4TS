import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Same;
import org.junit.Test;

public class SameTest {
    @Test
    public void testMatches() {
        Object obj = new Object();
        Same same = new Same(obj);
        assertEquals(true, same.matches(obj));
        
        Object obj2 = new Object();
        assertEquals(false, same.matches(obj2));
    }
    
    @Test
    public void testDescribeTo() {
        Object obj = new Object();
        Same same = new Same(obj);
        Description description = Mockito.mock(Description.class);
        same.describeTo(description);
        Mockito.verify(description).appendText("same(");
        Mockito.verify(description).appendText(obj.toString());
        Mockito.verify(description).appendText(")");
    }
    
    @Test
    public void testAppendQuoting() {
        Same same = new Same("foo");
        Description description = Mockito.mock(Description.class);
        same.appendQuoting(description);
        Mockito.verify(description).appendText("\"");
        
        Same same2 = new Same('f');
        Description description2 = Mockito.mock(Description.class);
        same2.appendQuoting(description2);
        Mockito.verify(description2).appendText("'");
    }
}