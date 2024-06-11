import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;
import org.junit.Test;
import static org.junit.Assert.*;

public class SameTest {

  @Test
  public void testMatches_shouldReturnTrueWhenActualIsSameAsWanted() {
    // Arrange
    Object wanted = new Object();
    Same same = new Same(wanted);

    // Act
    boolean result = same.matches(wanted);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testMatches_shouldReturnFalseWhenActualIsNotSameAsWanted() {
    // Arrange
    Object wanted = new Object();
    Object actual = new Object();
    Same same = new Same(wanted);

    // Act
    boolean result = same.matches(actual);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testDescribeTo_shouldAppendTextWithQuotesWhenWantedIsString() {
    // Arrange
    String wanted = "example";
    Same same = new Same(wanted);
    Description description = new Description() {
      @Override
      public Description appendText(CharSequence sequence) {
        // intentionally left blank
        return this;
      }

      @Override
      public Description appendText(String text) {
        // intentionally left blank
        return this;
      }

      @Override
      public Description appendDescriptionOf(SelfDescribing value) {
        // intentionally left blank
        return this;
      }

      @Override
      public Description appendValue(Object value) {
        // intentionally left blank
        return this;
      }

      @Override
      public Description appendValueList(String start, String separator, String end, Object... values) {
        // intentionally left blank
        return this;
      }

      @Override
      public Description appendValueList(String start, String separator, String end, Iterable<?> values) {
        // intentionally left blank
        return this;
      }

      @Override
      public <T> Description appendValueList(String start, String separator, String end, T[] values) {
        // intentionally left blank
        return this;
      }

      @Override
      public Description appendList(String start, String separator, String end, Iterable<?> values) {
        // intentionally left blank
        return this;
      }

      @Override
      public <T> Description appendList(String start, String separator, String end, T[] values) {
        // intentionally left blank
        return this;
      }

      @Override
      public Description appendText(StringBuffer stringBuffer) {
        // intentionally left blank
        return this;
      }

      @Override
      public String toString() {
        return ""; // intentionally left blank
      }
    };

    // Act
    same.describeTo(description);

    // Assert
    assertEquals("same(\"example\")", description.toString());
  }

  @Test
  public void testDescribeTo_shouldAppendTextWithSingleQuotesWhenWantedIsCharacter() {
    // Arrange
    char wanted = 'c';
    Same same = new Same(wanted);
    Description description = new Description() {
      @Override
      public Description appendText(CharSequence sequence) {
        // intentionally left blank
        return this;
      }

      @Override
      public Description appendText(String text) {
        // intentionally left blank
        return this;
      }

      @Override
      public Description appendDescriptionOf(SelfDescribing value) {
        // intentionally left blank
        return this;
      }

      @Override
      public Description appendValue(Object value) {
        // intentionally left blank
        return this;
      }

      @Override
      public Description appendValueList(String start, String separator, String end, Object... values) {
        // intentionally left blank
        return this;
      }

      @Override
      public Description appendValueList(String start, String separator, String end, Iterable<?> values) {
        // intentionally left blank
        return this;
      }

      @Override
      public <T> Description appendValueList(String start, String separator, String end, T[] values) {
        // intentionally left blank
        return this;
      }

      @Override
      public Description appendList(String start, String separator, String end, Iterable<?> values) {
        // intentionally left blank
        return this;
      }

      @Override
      public <T> Description appendList(String start, String separator, String end, T[] values) {
        // intentionally left blank
        return this;
      }

      @Override
      public Description appendText(StringBuffer stringBuffer) {
        // intentionally left blank
        return this;
      }

      @Override
      public String toString() {
        return ""; // intentionally left blank
      }
    };

    // Act
    same.describeTo(description);

    // Assert
    assertEquals("same('c')", description.toString());
  }

}