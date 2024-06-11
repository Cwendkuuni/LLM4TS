import org.junit.Test;
import org.mockito.internal.matchers.Same;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class SameTest {

    @Test
    public void testMatchesSameObject() {
        Object obj = new Object();
        Same sameMatcher = new Same(obj);

        assertTrue(sameMatcher.matches(obj));
    }

    @Test
    public void testDoesNotMatchDifferentObject() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Same sameMatcher = new Same(obj1);

        assertFalse(sameMatcher.matches(obj2));
    }

    @Test
    public void testDescribeToString() {
        Object obj = "Test";
        Same sameMatcher = new Same(obj);

        StringBuilder stringBuilder = new StringBuilder();
        sameMatcher.describeTo(mock(Description.class).toString());

        // Check if description contains the expected String
        assertTrue(stringBuilder.toString().contains("same(\"Test\")"));
    }

    @Test
    public void testDescribeToCharacter() {
        Object obj = 'c';
        Same sameMatcher = new Same(obj);

        StringBuilder stringBuilder = new StringBuilder();
        sameMatcher.describeTo(mock(Description.class).toString());

        // Check if description contains the expected char
        assertTrue(stringBuilder.toString().contains("same('c')"));
    }

    @Test(expected = NullPointerException.class)
    public void testDescribeToNullObject() {
        Object obj = null;
        Same sameMatcher = new Same(obj);

        StringBuilder stringBuilder = new StringBuilder();
        sameMatcher.describeTo(mock(Description.class).toString());
    }
}