import org.junit.Test;
import static org.junit.Assert.*;

public class SameTest {

    @Test
    public void testMatches_sameObjects_returnTrue() {
        Object object = new Object();
        Same same = new Same(object);

        assertTrue(same.matches(object));
    }

    @Test
    public void testMatches_differentObjects_returnFalse() {
        Object object1 = new Object();
        Object object2 = new Object();
        Same same = new Same(object1);

        assertFalse(same.matches(object2));
    }

    @Test
    public void testDescribeTo_stringObject_appendQuoting() {
        String stringObject = "hello";
        Same same = new Same(stringObject);
        Description description = new Description();

        same.describeTo(description);

        assertEquals("same(\"" + stringObject + "\")", description.toString());
    }

    @Test
    public void testDescribeTo_charObject_appendQuoting() {
        Character charObject = 'a';
        Same same = new Same(charObject);
        Description description = new Description();

        same.describeTo(description);

        assertEquals("same('" + charObject + "')", description.toString());
    }
}