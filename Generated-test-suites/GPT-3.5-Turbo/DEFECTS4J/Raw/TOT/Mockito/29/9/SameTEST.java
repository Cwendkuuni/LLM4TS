import org.hamcrest.Description;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void matches_SameObject_ReturnsTrue() {
        Object obj = new Object();
        Same same = new Same(obj);

        Assert.assertTrue(same.matches(obj));
    }

    @Test
    public void matches_DifferentObject_ReturnsFalse() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Same same = new Same(obj1);

        Assert.assertFalse(same.matches(obj2));
    }

    @Test
    public void describeTo_StringObjectByAppendingQuoting() {
        String str = "test string";
        Same same = new Same(str);
        Description description = new TestDescription();

        same.describeTo(description);

        Assert.assertEquals("same(\"test string\")", description.toString());
    }

    @Test
    public void describeTo_CharacterObjectByAppendingQuoting() {
        Character ch = 'a';
        Same same = new Same(ch);
        Description description = new TestDescription();

        same.describeTo(description);

        Assert.assertEquals("same('a')", description.toString());
    }

    private class TestDescription extends Description {
        private StringBuilder builder = new StringBuilder();

        @Override
        public Description appendText(String text) {
            builder.append(text);
            return this;
        }

        @Override
        public Description appendDescriptionOf(SelfDescribing value) {
            value.describeTo(this);
            return this;
        }

        @Override
        public Description appendValue(Object value) {
            builder.append(value);
            return this;
        }

        @Override
        public <T> Description appendValueList(String start, String separator, String end, T... values) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> Description appendValueList(String start, String separator, String end, Iterable<T> values) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Description appendList(String start, String separator, String end, Iterable<? extends SelfDescribing> values) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Description appendText(String text, Object... values) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return builder.toString();
        }
    }
}