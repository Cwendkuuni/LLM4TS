import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.hamcrest.Description;

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

        Description description = new DescriptionStub();
        same.describeTo(description);

        assertEquals("same(" + object.toString() + ")", description.toString());
    }

    private class DescriptionStub implements Description {

        private String text = "";

        @Override
        public Description appendText(String text) {
            this.text += text;
            return this;
        }

        @Override
        public Description appendDescriptionOf(ArgumentMatcher<?> arg0) {
            return null;
        }

        @Override
        public Description appendList(String arg0, String arg1, String arg2, Iterable<?> arg3) {
            return null;
        }

        @Override
        public Description appendValue(Object arg0) {
            return null;
        }

        @Override
        public Description appendValueList(String arg0, String arg1, String arg2, Iterable<?> arg3) {
            return null;
        }

        @Override
        public Description appendValueList(String arg0, String arg1, String arg2, Object... arg3) {
            return null;
        }

        @Override
        public Description appendList(String arg0, String arg1, String arg2, Object... arg3) {
            return null;
        }

        @Override
        public Description appendMismatchOf(ArgumentMatcher<?> arg0, Object arg1) {
            return null;
        }

        @Override
        public Description appendMismatchOf(ArgumentMatcher<?> arg0, Object arg1, int arg2) {
            return null;
        }

        @Override
        public Description appendText(CharSequence arg0) {
            return null;
        }

        @Override
        public Description appendNewLine() {
            return null;
        }

        @Override
        public Description appendNull() {
            return null;
        }

        @Override
        public Description appendText(char arg0) {
            return null;
        }

        @Override
        public Description appendText(int arg0) {
            return null;
        }

        @Override
        public Description appendValue(Object arg0, boolean arg1) {
            return null;
        }

        @Override
        public Description appendValue(Object arg0, Class<?> arg1) {
            return null;
        }

        @Override
        public Description appendValue(Object arg0, CharSequence arg1) {
            return null;
        }

        @Override
        public Description appendValue(Object arg0, Iterable<?> arg1) {
            return null;
        }

        @Override
        public Description appendValue(Object arg0, Object... arg1) {
            return null;
        }

        @Override
        public String toString() {
            return text;
        }

    }

}