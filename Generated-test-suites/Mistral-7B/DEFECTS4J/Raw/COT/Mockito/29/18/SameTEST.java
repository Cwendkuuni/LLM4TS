import org.junit.Test;
import org.junit.Assert.*;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testSameWithSameObjects() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        Object actual = wanted;

        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testSameWithDifferentObjectsOfSameClass() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        Object actual = new Object();

        assertFalse(sameMatcher.matches(actual));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSameWithNullWanted() {
        Same sameMatcher = new Same(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSameWithNullActual() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSameWithDifferentClasses() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        String actual = "test";

        assertFalse(sameMatcher.matches(actual));
    }

    @Test(expected = IllegalStateException.class)
    public void testSameWithDifferentStrings() {
        Object wanted = "string1";
        Same sameMatcher = new Same(wanted);
        Object actual = "string2";

        assertFalse(sameMatcher.matches(actual));
    }

    @Test(expected = IllegalStateException.class)
    public void testSameWithDifferentCharacters() {
        Object wanted = 'a';
        Same sameMatcher = new Same(wanted);
        Object actual = 'b';

        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testDescribeTo() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);

        Description description = new Description();
        sameMatcher.describeTo(description);

        String expectedDescription = "same(Object)";
        String actualDescription = description.toString();

        assertEquals(expectedDescription, actualDescription);
    }
}