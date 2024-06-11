import org.junit.Test;
import org.mockito.internal.matchers.Same;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class SameTest {

    @Test
    public void testMatchesForSameObject() {
        Object obj = new Object();
        Same same = new Same(obj);

        assertTrue(same.matches(obj));
    }

    @Test
    public void testMatchesForDifferentObject() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Same same = new Same(obj1);

        assertFalse(same.matches(obj2));
    }

    @Test
    public void testDescribeToString() {
        Object obj = new Object();
        Same same = new Same(obj);

        same.describeTo(mock(Description.class));

        // Need to check if describeTo() method runs without throwing an exception
    }

    @Test
    public void testAppendQuotingForString() {
        String str = "test";
        Same same = new Same(str);

        same.appendQuoting(mock(Description.class));

        // Need to check if appendQuoting() method runs without throwing an exception when the Object is a String
    }

    @Test
    public void testAppendQuotingForCharacter() {
        Character ch = 'a';
        Same same = new Same(ch);

        same.appendQuoting(mock(Description.class));

        // Need to check if appendQuoting() method runs without throwing an exception when the Object is a Character
    }
}