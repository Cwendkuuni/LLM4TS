import org.junit.Test;
import org.mockito.internal.matchers.Same;
import static org.junit.Assert.*;

public class SameTest {

    @Test
    public void testSameConstructor() {
        Same same = new Same("expectedString");
        assertNotNull(same);
    }

    @Test
    public void testMatchesSameObjects() {
        Same same = new Same(new Integer(1));
        assertTrue(same.matches(new Integer(1)));
    }

    @Test
    public void testMatchesDifferentObjectsOfSameClass() {
        Same same = new Same(new Integer(1));
        assertFalse(same.matches(new Integer(2)));
    }

    @Test
    public void testMatchesDifferentObjectsOfDifferentClasses() {
        Same same = new Same("expectedString");
        assertFalse(same.matches(new Integer(1)));
    }
}