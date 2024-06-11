import org.junit.Test;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testMatches() {
        // Arrange
        Object wanted = new Object();
        Same same = new Same(wanted);

        // Act
        boolean result = same.matches(wanted);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testDescribeTo() {
        // Arrange
        Object wanted = new Object();
        Same same = new Same(wanted);

        // Act
        DescriptionMock descriptionMock = new DescriptionMock();
        same.describeTo(descriptionMock);

        // Assert
        assertEquals("same(" + wanted.toString() + ")", descriptionMock.description);
    }

}

class DescriptionMock extends Description {
    String description;

    public DescriptionMock() {
    }

    public void appendText(String text) {
        description += text;
    }

    public void appendDescriptionOf(SelfDescribing value) {
    }

    public void appendList(String start, String separator, String end, Iterable<? extends SelfDescribing> values) {
    }

    public void appendValue(Object value) {
    }

    public void appendValueList(String start, String separator, String end, Iterable<?> values) {
    }

    public void appendList(String start, String separator, String end, Object... values) {
    }

    public void appendMismatchOf(DescribedMatcher<?> matcher, Object item) {
    }
}