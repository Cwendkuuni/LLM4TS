import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class SameTest {

    @Test
    public void testMatches() {
        Object object1 = new Object();
        Object object2 = new Object();

        Same same = new Same(object1);

        assertTrue(same.matches(object1));
        assertFalse(same.matches(object2));
    }

    @Test
    public void testDescribeTo() {
        Object object = new Object();

        Same same = new Same(object);

        Description description = mock(Description.class);
        same.describeTo(description);

        verify(description).appendText("same(");
        verify(description).appendText(object.toString());
        verify(description).appendText(")");
    }
}