import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.hamcrest.Description;

public class SameTest {

    @Test
    public void testMatches() {
        Object object = new Object();
        Same same = new Same(object);

        assertTrue(same.matches(object));
        assertFalse(same.matches(new Object()));
    }

    @Test
    public void testDescribeTo() {
        Object object = new Object();
        Same same = new Same(object);

        Description description = new DescriptionStub();
        same.describeTo(description);

        assertEquals("same(" + object.toString() + ")", description.toString());
    }

    @Test
    public void testDescribeTo_withString() {
        String string = "example";
        Same same = new Same(string);

        Description description = new DescriptionStub();
        same.describeTo(description);

        assertEquals("same(\"" + string + "\")", description.toString());
    }

    @Test
    public void testDescribeTo_withCharacter() {
        Character character = 'a';
        Same same = new Same(character);

        Description description = new DescriptionStub();
        same.describeTo(description);

        assertEquals("same('" + character + "')", description.toString());
    }

    private class DescriptionStub implements Description {
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
            throw new UnsupportedOperationException();
        }

        @Override
        public Description appendDescriptionOf(ArgumentMatcher<?> matcher) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Description appendList(String start, String separator, String end, Iterable<?> values) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Description appendText(Description description) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return stringBuilder.toString();
        }
    }
}