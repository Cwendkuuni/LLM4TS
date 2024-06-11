import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;

import java.io.Serializable;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class SameTest {

    @Test
    public void testMatches() {
        Object obj1 = new Object();
        Object obj2 = obj1;
        Object obj3 = new Object();

        Same same = new Same(obj1);

        assertEquals(true, same.matches(obj2));
        assertEquals(false, same.matches(obj3));
    }

    @Test
    public void testDescribeTo() {
        Description description = mock(Description.class);
        Same same = new Same("hello");

        same.describeTo(description);

        // Add assertions here to verify the behavior of the method
    }
}