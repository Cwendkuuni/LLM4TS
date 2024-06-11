import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.*;
import org.mockito.ArgumentMatcher;


public class SameTest {

    private Same same;

    @Before
    public void setUp() {
        same = new Same("test");
    }

    @Test
    public void testMatches_SameInstance_ReturnsTrue() {
        // Arrange
        Object actual = "test";

        // Act
        boolean result = same.matches(actual);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testMatches_DifferentInstance_ReturnsFalse() {
        // Arrange
        Object actual = new String("test");

        // Act
        boolean result = same.matches(actual);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testMatches_Null_ReturnsFalse() {
        // Arrange
        Object actual = null;

        // Act
        boolean result = same.matches(actual);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testDescribeTo_StringValue_AddsQuotes() {
        // Arrange
        Description description = new TestDescription();
        same = new Same("test");

        // Act
        same.describeTo(description);
        String result = description.toString();

        // Assert
        Assert.assertEquals("same(\"test\")", result);
    }

    @Test
    public void testDescribeTo_CharacterValue_AddsSingleQuotes() {
        // Arrange
        Description description = new TestDescription();
        same = new Same('t');

        // Act
        same.describeTo(description);
        String result = description.toString();

        // Assert
        Assert.assertEquals("same('t')", result);
    }

    @Test
    public void testDescribeTo_NullValue_AddsQuotes() {
        // Arrange
        Description description = new TestDescription();
        same = new Same(null);

        // Act
        same.describeTo(description);
        String result = description.toString();

        // Assert
        Assert.assertEquals("same(null)", result);
    }

    private class TestDescription extends BaseDescription {

        private StringBuilder descriptionBuilder = new StringBuilder();

        @Override
        public Description appendText(String text) {
            descriptionBuilder.append(text);
            return this;
        }

        @Override
        public String toString() {
            return descriptionBuilder.toString();
        }
    }

    private abstract class BaseDescription implements Description {

        @Override
        public Description appendDescriptionOf(Object value) {
            return appendText(String.valueOf(value));
        }

        @Override
        public Description appendValue(Object value) {
            return appendText(String.valueOf(value));
        }

        @Override
        public Description appendValueList(String start, String separator, String end, Object... values) {
            return appendValueList(start, separator, end, Arrays.asList(values));
        }

        @Override
        public Description appendValueList(String start, String separator, String end, Iterable<?> values) {
            boolean valueAdded = false;
            appendText(start);
            for (Object value : values) {
                if (valueAdded) {
                    appendText(separator);
                }
                appendValue(value);
                valueAdded = true;
            }
            appendText(end);
            
            return this;
        }

        @Override
        public Description appendList(String start, String separator, String end, Iterable<? extends Description> descriptions) {
            appendText(start);
            boolean first = true;
            for (Description description : descriptions) {
                if (first) {
                    first = false;
                } else {
                    appendText(separator);
                }
                appendDescriptionOf(description);
            }
            appendText(end);
            return this;
        }

        @Override
        public Description appendText(TextDescription textDescription) {
            appendText(textDescription.toString());
            return this;
        }

        @Override
        public Description appendNewLine() {
            appendText("\n");
            return this;
        }

        @Override
        public Description appendText(String indent, String text) {
            appendText(indent + text);
            return this;
        }
    }
}