import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testMatches() {
        Object expected = new Object();
        Same same = new Same(expected);
        assertTrue(same.matches(expected));
    }

    @Test
    public void testDescribeToWithSimilarType() {
        String expected = "test";
        Same same = new Same(expected);
        assertEquals("same(\"test\")", same.describeTo());
    }

    @Test
    public void testDescribeToWithDifferentType() {
        Integer expected = 5;
        Same same = new Same(expected);
        assertEquals("same(\"5\")", same.describeTo());
    }

    @Test
    public void testDescribeToWithCharacterType() {
        Character expected = 'c';
        Same same = new Same(expected);
        assertEquals("same(\'c\')", same.describeTo());
    }
}