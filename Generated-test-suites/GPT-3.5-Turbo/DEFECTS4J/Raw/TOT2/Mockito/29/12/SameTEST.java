import org.junit.Test;
import static org.junit.Assert.*;

public class SameTest {

    @Test
    public void testMatchesWhenObjectsAreSame() {
        Object obj = new Object();
        Same same = new Same(obj);
        assertTrue(same.matches(obj));
    }

    @Test
    public void testMatchesWhenObjectsAreDifferent() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Same same = new Same(obj1);
        assertFalse(same.matches(obj2));
    }

    @Test
    public void testDescribeToForString() {
        String str = "test";
        Same same = new Same(str);
        // test description generation for string
    }

    @Test
    public void testDescribeToForCharacter() {
        Character ch = 'c';
        Same same = new Same(ch);
        // test description generation for character
    }

    @Test
    public void testDescribeToForOtherTypes() {
        Object obj = new Object();
        Same same = new Same(obj);
        // test description generation for other types
    }

}