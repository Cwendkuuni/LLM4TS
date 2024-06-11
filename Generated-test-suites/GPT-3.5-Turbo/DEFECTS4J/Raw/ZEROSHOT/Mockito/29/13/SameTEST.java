import org.hamcrest.Description;
import org.mockito.internal.matchers.Same;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import static org.junit.Assert.*;

public class SameTest {

    private Same same;

    @Before
    public void setUp() {
        same = new Same("test");
    }

    @Test
    public void testMatches() {
        boolean result = same.matches("test");
        assertTrue(result);
    }

    @Test
    public void testMatches_NotSame() {
        boolean result = same.matches("different");
        assertFalse(result);
    }

    @Test
    public void testMatches_ObjectIsNull() {
        boolean result = same.matches(null);
        assertFalse(result);
    }

    @Test
    public void testDescribeTo_String() {
        Description description = new Description() {
            private StringBuilder stringBuilder = new StringBuilder();

            @Override
            public Description appendText(String text) {
                stringBuilder.append(text);
                return this;
            }

            @Override
            public Description appendValue(Object value) {
                stringBuilder.append(value);
                return this;
            }

            @Override
            public Description appendValueList(String start, String separator, String end, Object... values) {
                return null;
            }

            @Override
            public Description appendValueList(String start, String separator, String end, Iterable<?> values) {
                return null;
            }

            @Override
            public Description appendList(String start, String separator, String end, Iterable<?> values) {
                return null;
            }

            @Override
            public Description appendList(String start, String separator, String end, Object... values) {
                return null;
            }

            @Override
            public Description appendText(Description description) {
                return null;
            }

            @Override
            public String toString() {
                return stringBuilder.toString();
            }
        };

        same.describeTo(description);
        assertEquals("same(\"test\")", description.toString());
    }

    @Test
    public void testDescribeTo_Character() {
        Same sameCharacter = new Same('a');

        Description description = new Description() {
            private StringBuilder stringBuilder = new StringBuilder();

            @Override
            public Description appendText(String text) {
                stringBuilder.append(text);
                return this;
            }

            @Override
            public Description appendValue(Object value) {
                stringBuilder.append(value);
                return this;
            }

            @Override
            public Description appendValueList(String start, String separator, String end, Object... values) {
                return null;
            }

            @Override
            public Description appendValueList(String start, String separator, String end, Iterable<?> values) {
                return null;
            }

            @Override
            public Description appendList(String start, String separator, String end, Iterable<?> values) {
                return null;
            }

            @Override
            public Description appendList(String start, String separator, String end, Object... values) {
                return null;
            }

            @Override
            public Description appendText(Description description) {
                return null;
            }

            @Override
            public String toString() {
                return stringBuilder.toString();
            }
        };

        sameCharacter.describeTo(description);
        assertEquals("same('a')", description.toString());
    }
}