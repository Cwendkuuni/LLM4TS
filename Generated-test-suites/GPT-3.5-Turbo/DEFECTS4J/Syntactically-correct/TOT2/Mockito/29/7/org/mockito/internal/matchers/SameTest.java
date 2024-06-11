import org.junit.Test;
import org.mockito.internal.matchers.Same;
import org.mockito.ArgumentMatcher;
import org.hamcrest.Description;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SameTest {

    @Test
    public void testMatches() {
        Object obj = new Object();
        Same same = new Same(obj);
        assertTrue(same.matches(obj));
    }

    @Test
    public void testDescribeTo() {
        Object obj = new Object();
        Same same = new Same(obj);
        
        Description description = mock(Description.class);
        same.describeTo(description);
        
        verify(description).appendText("same(");
        verify(description).appendText(obj.toString());
    }

    @Test
    public void testDescribeToStringQuoting() {
        String str = "test";
        Same same = new Same(str);
        
        Description description = mock(Description.class);
        same.describeTo(description);
        
        verify(description).appendText("same(\"");
        verify(description).appendText(str);
        verify(description).appendText("\")");
    }

    @Test
    public void testDescribeToCharQuoting() {
        char ch = 'a';
        Same same = new Same(ch);
        
        Description description = mock(Description.class);
        same.describeTo(description);
        
        verify(description).appendText("same('");
        verify(description).appendText(String.valueOf(ch));
        verify(description).appendText("')");
    }
}