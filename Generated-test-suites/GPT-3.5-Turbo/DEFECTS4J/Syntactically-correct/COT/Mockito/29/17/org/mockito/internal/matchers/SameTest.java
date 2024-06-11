
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.matchers.Same;
import org.hamcrest.Description;

public class SameTest {

    // Test Same constructor
    @Test
    public void testConstructor() {
        Object expected = new Object();
        Same same = new Same(expected);
        assertSame(expected, same.wanted);
    }

    // Test matches method for same object
    @Test
    public void testMatches_SameObject() {
        Object obj = new Object();
        Same same = new Same(obj);
        assertTrue(same.matches(obj));
    }

    // Test matches method for different objects
    @Test
    public void testMatches_DifferentObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Same same = new Same(obj1);
        assertFalse(same.matches(obj2));
    }

    // Test describeTo method for String object
    @Test
    public void testDescribeTo_String() {
        Object str = "Hello";
        Same same = new Same(str);
        Description description = new Description() {
            private StringBuilder sb = new StringBuilder();

            @Override
            public String toString() {
                return sb.toString();
            }

            @Override
            public Description appendText(String text) {
                sb.append(text);
                return this;
            }

            @Override
            public Description appendValue(Object value) {
                sb.append(value);
                return this;
            }

            @Override
            public Description appendValueList(String start, String separator, String end, Object... values) {
                return null;
            }

            @Override
            public Description appendDescriptionOf(SelfDescribing value) {
                return null;
            }

            @Override
            public Description appendList(String start, String separator, String end, Iterable<? extends SelfDescribing> values) {
                return null;
            }

            @Override
            public <T extends SelfDescribing> Description appendValueList(String start, String separator, String end, T... values) {
                return null;
            }
        };
        same.describeTo(description);
        assertEquals("same(\"Hello\")", description.toString());
    }

    // Test describeTo method for Character object
    @Test
    public void testDescribeTo_Character() {
        Object ch = 'C';
        Same same = new Same(ch);
        Description description = new Description() {
            private StringBuilder sb = new StringBuilder();

            @Override
            public String toString() {
                return sb.toString();
            }

            @Override
            public Description appendText(String text) {
                sb.append(text);
                return this;
            }

            @Override
            public Description appendValue(Object value) {
                sb.append(value);
                return this;
            }

            @Override
            public Description appendValueList(String start, String separator, String end, Object... values) {
                return null;
            }

            @Override
            public Description appendDescriptionOf(SelfDescribing value) {
                return null;
            }

            @Override
            public Description appendList(String start, String separator, String end, Iterable<? extends SelfDescribing> values) {
                return null;
            }

            @Override
            public <T extends SelfDescribing> Description appendValueList(String start, String separator, String end, T... values) {
                return null;
            }
        };
        same.describeTo(description);
        assertEquals("same('C')", description.toString());
    }
}
