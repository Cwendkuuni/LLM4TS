import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.hamcrest.Description;
import static org.junit.Assert.*;

public class SameTest {

    @Test
    public void testMatches() {
        Object expected = new Object();
        Same same = new Same(expected);

        assertTrue(same.matches(expected));
        assertFalse(same.matches(new Object()));
    }

    @Test
    public void testDescribeTo() {
        Object expected = new Object();
        Same same = new Same(expected);
        Description description = new Description() {
            StringBuffer buffer = new StringBuffer();

            @Override
            public Description appendText(String text) {
                buffer.append(text);
                return this;
            }

            @Override
            public Description appendValue(Object value) {
                buffer.append(value);
                return this;
            }

            @Override
            public String toString() {
                return buffer.toString();
            }
        };

        same.describeTo(description);

        assertEquals("same(" + expected.toString() + ")", description.toString());
    }

    @Test
    public void testAppendQuoting() {
        Same same = new Same("abc");
        Description description = new Description() {
            StringBuffer buffer = new StringBuffer();

            @Override
            public Description appendText(String text) {
                buffer.append(text);
                return this;
            }

            @Override
            public String toString() {
                return buffer.toString();
            }
        };

        same.appendQuoting(description);

        assertEquals("\"", description.toString());

        same = new Same('a');
        description = new Description() {
            StringBuffer buffer = new StringBuffer();

            @Override
            public Description appendText(String text) {
                buffer.append(text);
                return this;
            }

            @Override
            public String toString() {
                return buffer.toString();
            }
        };

        same.appendQuoting(description);

        assertEquals("'", description.toString());
    }

}