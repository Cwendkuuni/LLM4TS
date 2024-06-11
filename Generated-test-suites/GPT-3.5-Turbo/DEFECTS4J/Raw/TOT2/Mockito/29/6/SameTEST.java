import org.junit.Test;
import org.hamcrest.Description;
import org.mockito.internal.matchers.Same;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;


public class SameTest {

    @Test
    public void testMatches() {
        Same same = new Same("test");
        assertTrue(same.matches("test"));
        assertFalse(same.matches("not test"));
    }

    @Test
    public void testDescribeTo() {
        Same sameString = new Same("test");
        Description description = new Description() {
            StringBuilder sb = new StringBuilder();

            @Override
            public Description appendText(String text) {
                sb.append(text);
                return this;
            }

            @Override
            public Description appendValue(Object value) {
                sb.append(value.toString());
                return this;
            }

            @Override
            public Description appendDescriptionOf(Description value) {
                sb.append(value.toString());
                return this;
            }

            @Override
            public String toString() {
                return sb.toString();
            }
        };

        sameString.describeTo(description);
        assertEquals("same(\"test\")", description.toString());
    }

    @Test
    public void testDifferentTypes() {
        Same same1 = new Same("test");
        Same same2 = new Same('c');
        assertFalse(same1.matches('c'));
        assertFalse(same2.matches("test"));

    }
}