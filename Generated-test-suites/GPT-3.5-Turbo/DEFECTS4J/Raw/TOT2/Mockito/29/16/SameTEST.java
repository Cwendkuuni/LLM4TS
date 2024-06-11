import org.junit.Test;
import org.mockito.internal.matchers.Same;
import org.hamcrest.Description;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;

public class SameTest {

    @Test
    public void testMatches() {
        Same same = new Same("test");
        assertTrue(same.matches("test"));

        Same sameInt = new Same(5);
        assertTrue(sameInt.matches(5));

        Same sameNull = new Same(null);
        assertTrue(sameNull.matches(null));

        Same sameObject = new Same(new Object());
        Object obj = new Object();
        assertTrue(sameObject.matches(obj));
    }

    @Test
    public void testDescribeTo() {
        Same sameString = new Same("test");
        Description description = new Description();
        sameString.describeTo(description);
        assertEquals("same(\"test\")", description.toString());

        Same sameChar = new Same('c');
        Description descriptionChar = new Description();
        sameChar.describeTo(descriptionChar);
        assertEquals("same('c')", descriptionChar.toString());
    }

    @Test
    public void testAppendQuoting() {
        Same sameString = new Same("test");
        Description descriptionString = new Description();
        sameString.appendQuoting(descriptionString);
        assertEquals("\"", descriptionString.toString());

        Same sameChar = new Same('c');
        Description descriptionChar = new Description();
        sameChar.appendQuoting(descriptionChar);
        assertEquals("'", descriptionChar.toString());
    }
}