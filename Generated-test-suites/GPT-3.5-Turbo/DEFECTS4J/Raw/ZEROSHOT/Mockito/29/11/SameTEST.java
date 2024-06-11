import org.hamcrest.Description;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import java.io.Serializable;

public class SameTest {

    @Test
    public void testMatches() {
        Same same = new Same("test");
        boolean result = same.matches("test");
        Assert.assertTrue(result);
    }

    @Test
    public void testNotMatches() {
        Same same = new Same("test");
        boolean result = same.matches("different");
        Assert.assertFalse(result);
    }

    @Test
    public void testDescribeToWithString() {
        Same same = new Same("test");
        Description description = new TestDescription();
        same.describeTo(description);
        Assert.assertEquals("same(\"test\")", description.toString());
    }

    @Test
    public void testDescribeToWithChar() {
        Same same = new Same('a');
        Description description = new TestDescription();
        same.describeTo(description);
        Assert.assertEquals("same('a')", description.toString());
    }

    private class TestDescription implements Description {
        private StringBuilder stringBuilder;

        public TestDescription() {
            this.stringBuilder = new StringBuilder();
        }

        @Override
        public Description appendText(String text) {
            stringBuilder.append(text);
            return this;
        }

        @Override
        public Description appendDescriptionOf(ArgumentMatcher<?> argumentMatcher) {
            return this;
        }

        @Override
        public Description appendValue(Object value) {
            return this;
        }

        @Override
        public <T> Description appendValueList(String start, String separator, String end, T... values) {
            return this;
        }

        @Override
        public <T extends Iterable<?>> Description appendValueList(String start, String separator, String end, T values) {
            return this;
        }

        @Override
        public Description appendList(String start, String separator, String end, Iterable<?> values) {
            return this;
        }

        @Override
        public Description appendText(CharSequence sequence) {
            return this;
        }

        @Override
        public Description appendNewLine() {
            return this;
        }

        @Override
        public Description appendValue(Object value, String string) {
            return this;
        }

        @Override
        public String toString() {
            return stringBuilder.toString();
        }
    }
}