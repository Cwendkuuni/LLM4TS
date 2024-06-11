import org.junit.Test;
import org.junit.Assert.*;
import org.hamcrest.CoreMatchers;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testMatchesSameObject() {
        Same same = new Same("testString");
        String testString = "testString";

        assertTrue(same.matches(testString));
    }

    @Test
    public void testMatchesNullAndNull() {
        Same same = new Same(null);

        assertNull(same.matches(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesThrowsIllegalArgumentExceptionForNullWanted() {
        Same same = new Same(null);

        same.matches("testString");
    }

    @Test
    public void testMatchesDifferentClassTypes() {
        Same intSame = new Same(1);
        Same stringSame = new Same("testString");

        assertFalse(intSame.matches(stringSame));
    }

    @Test
    public void testMatchesDifferentObjectsButSameValues() {
        Same stringSame1 = new Same("testString");
        Same stringSame2 = new Same("testString");

        assertTrue(stringSame1.matches(stringSame2));
    }

    @Test
    public void testDescribeToReturnsCorrectDescription() {
        Same stringSame = new Same("testString");

        String description = "same(\"testString\")";
        Description desc = new Description();
        stringSame.describeTo(desc);
        String actualDescription = desc.toString();

        assertEquals(description, actualDescription);
    }

    @Test
    public void testAppendQuotingQuotesString() {
        Same stringSame = new Same("testString");

        String description = "same(\"testString\")";
        Description desc = new Description();
        stringSame.appendQuoting(desc);
        String actualDescription = desc.toString();

        assertEquals(description, actualDescription);
    }

    @Test
    public void testAppendQuotingDoesNotQuoteOtherTypes() {
        Same integerSame = new Same(1);
        Same charSame = new Same('a');

        String description = "same(1) and same('a')";
        Description desc = new Description();
        integerSame.appendQuoting(desc);
        charSame.appendQuoting(desc);
        String actualDescription = desc.toString();

        assertEquals(description, actualDescription);
    }
}