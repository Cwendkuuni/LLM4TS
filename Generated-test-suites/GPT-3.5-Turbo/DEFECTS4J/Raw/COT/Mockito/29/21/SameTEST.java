import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class SameTest {

    @Test
    public void testMatches_sameObjects_returnsTrue() {
        // Arrange
        Object testObject = new Object();
        Same same = new Same(testObject);

        // Act
        boolean result = same.matches(testObject);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testMatches_differentObjects_returnsFalse() {
        // Arrange
        Object object1 = new Object();
        Object object2 = new Object();
        Same same = new Same(object1);

        // Act
        boolean result = same.matches(object2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testDescribeTo_descriptionContainsObjectToString() {
        // Arrange
        Object testObject = new Object();
        Same same = new Same(testObject);
        Description description = new Description() {
            private String value;

            @Override
            public Description appendText(String text) {
                value = text;
                return this;
            }

            @Override
            public Description appendValue(Object value) {
                this.value = value.toString();
                return this;
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
            public Description appendValueList(String start, String separator, String end, Iterable<?> values) {
                return null;
            }

            @Override
            public Description appendTextList(String start, String separator, String end, Iterable<?> values) {
                return null;
            }

            @Override
            public Description appendList(String start, String separator, String end, Object... values) {
                return null;
            }

            @Override
            public Description appendValueList(String start, String separator, String end, Object... values) {
                return null;
            }

            @Override
            public Description appendTextList(String start, String separator, String end, Object... values) {
                return null;
            }

            @Override
            public Description appendNewLine() {
                return null;
            }

            @Override
            public Description appendText(String text, int start, int end) {
                return null;
            }

            @Override
            public Description appendDescriptionOf(Describable value) {
                return null;
            }

            @Override
            public Description appendText(String text, int start, int end, int max) {
                return null;
            }

            @Override
            public Description appendValue(Object value, boolean forceDetail) {
                return null;
            }

            @Override
            public Description appendList(String start, String separator, String end, Object value, Object... values) {
                return null;
            }

            @Override
            public Description appendValueList(String start, String separator, String end, Object value, Object... values) {
                return null;
            }

            @Override
            public Description appendTextList(String start, String separator, String end, Object value, Object... values) {
                return null;
            }

            @Override
            public Description appendList(String start, String separator, String end, Iterable<?> values, Object value, Object... values2) {
                return null;
            }

            @Override
            public Description appendValueList(String start, String separator, String end, Iterable<?> values, Object value, Object... values2) {
                return null;
            }

            @Override
            public Description appendTextList(String start, String separator, String end, Iterable<?> values, Object value, Object... values2) {
                return null;
            }

            @Override
            public Description appendValue(Object value, String pattern) {
                return null;
            }

            @Override
            public Description appendValue(Object value, String prefix, String suffix) {
                return null;
            }

            @Override
            public Description appendList(String start, String separator, String end, Object value) {
                return null;
            }

            @Override
            public String toString() {
                return value;
            }
        };

        // Act
        same.describeTo(description);
        String descriptionText = description.toString();

        // Assert
        assertThat(descriptionText, is("same(" + testObject.toString() + ")"));
    }
}