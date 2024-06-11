import org.junit.Test;
import org.junit.Assert.*;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testSameInstance() {
        Same sameInstance = new Same(new Object());
        Object actual = new Object();
        assertTrue(sameInstance.matches(actual));
    }

    @Test
    public void testMatchesSameObjects() {
        Same sameInstance = new Same("test");
        String actual = "test";
        assertTrue(sameInstance.matches(actual));
    }

    @Test
    public void testMatchesDifferentObjectsButSameClass() {
        Same sameInstance = new Same(new Object());
        Object anotherObject = new Object();
        assertFalse(sameInstance.matches(anotherObject));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThrowsExceptionForNullWanted() {
        new Same(null);
    }

    @Test
    public void testDescribeToQuotesString() {
        Same sameInstance = new Same("test");
        Description description = new Description();
        sameInstance.describeTo(description);
        String expectedDescription = "same(\"test\")";
        assertEquals(expectedDescription, description.toString());
    }

    @Test
    public void testDescribeToQuotesCharacter() {
        Same sameInstance = new Same('a');
        Description description = new Description();
        sameInstance.describeTo(description);
        String expectedDescription = "same('a')";
        assertEquals(expectedDescription, description.toString());
    }

    @Test
    public void testDescribeToDoesNotQuoteOtherTypes() {
        Same sameInstance = new Same(new Object());
        Description description = new Description();
        sameInstance.describeTo(description);
        String expectedDescription = "same(<Object>)";
        assertNotEquals(expectedDescription, description.toString());
    }
}