import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.internal.matchers.Same;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class SameTest {

    @Test
    public void testMatchesForSameObject() {
        Object obj = new Object();
        Same sameMatcher = new Same(obj);
        assertTrue(sameMatcher.matches(obj));
    }

    @Test
    public void testMatchesForDifferentObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Same sameMatcher = new Same(obj1);
        assertFalse(sameMatcher.matches(obj2));
    }

    @Test
    public void testDescribeToForString() {
         String str = "example";
         Same sameMatcher = new Same(str);
         Description description = mock(Description.class);
         sameMatcher.describeTo(description);
         verify(description).appendText("same(\"example\")");
    }

    @Test
    public void testDescribeToForCharacter() {
        Character c = 'a';
        Same sameMatcher = new Same(c);
        Description description = mock(Description.class);
        sameMatcher.describeTo(description);
        verify(description).appendText("same('a')");
    }

    @Test
    public void testDescribeToForObject() {
        Object obj = new Object();
        Same sameMatcher = new Same(obj);
        Description description = mock(Description.class);
        sameMatcher.describeTo(description);
        verify(description).appendText("same(" + obj.toString() + ")");
    }
}