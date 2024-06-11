import org.hamcrest.Description;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import java.io.Serializable;

public class SameTest {

    @Test
    public void testMatchesWhenObjectsAreSame() {
        // Arrange
        Object wanted = new Object();
        Same same = new Same(wanted);

        // Act
        boolean result = same.matches(wanted);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testMatchesWhenObjectsAreDifferent() {
        // Arrange
        Object wanted = new Object();
        Object actual = new Object();
        Same same = new Same(wanted);

        // Act
        boolean result = same.matches(actual);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testDescribeToWithQuotingForString() {
        // Arrange
        String wanted = "hello";
        Description description = new DescriptionStub();
        Same same = new Same(wanted);

        // Act
        same.describeTo(description);

        // Assert
        Assert.assertEquals("same(\"hello\")", description.toString());
    }

    @Test
    public void testDescribeToWithQuotingForCharacter() {
        // Arrange
        char wanted = 'c';
        Description description = new DescriptionStub();
        Same same = new Same(wanted);

        // Act
        same.describeTo(description);

        // Assert
        Assert.assertEquals("same('c')", description.toString());
    }

    private static class DescriptionStub implements Description {
        private StringBuilder stringBuilder = new StringBuilder();

        @Override
        public Description appendText(String text) {
            stringBuilder.append(text);
            return this;
        }

        @Override
        public Description appendDescriptionOf(ArgumentMatcher<?> argumentMatcher) {
            return null;
        }

        @Override
        public Description appendValue(Object value) {
            return null;
        }

        @Override
        public <T> Description appendValueList(String start, String separator, String end, T... values) {
            return null;
        }

        @Override
        public <T> Description appendValueList(String start, String separator, String end, Iterable<T> values) {
            return null;
        }

        @Override
        public Description appendList(String start, String separator, String end, Iterable<? extends Description> descriptions) {
            return null;
        }

        @Override
        public Description appendText(String text, Object... values) {
            return null;
        }

        @Override
        public Description appendNewLine() {
            return null;
        }

        @Override
        public Description appendList(String start, String separator, String end, Object... values) {
            return null;
        }

        @Override
        public String toString() {
            return stringBuilder.toString();
        }
    }
}