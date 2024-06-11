import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.internal.matchers.Same;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class SameTest {

    @Test
    public void testMatchesWhenSameObject() {
        Object obj = new Object();
        Same same = new Same(obj);
        assertTrue(same.matches(obj));
    }

    @Test
    public void testMatchesWhenDifferentObject() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Same same = new Same(obj1);
        assertFalse(same.matches(obj2));
    }

    @Test
    public void testDescribeToWithString() {
        String str = "test";
        Same same = new Same(str);
        Description description = new Description();
        same.describeTo(description);
        assertEquals("same(\"test\")", description.toString());
    }

    @Test
    public void testDescribeToWithCharacter() {
        Character ch = 'a';
        Same same = new Same(ch);
        Description description = new Description();
        same.describeTo(description);
        assertEquals("same('a')", description.toString());
    }

}