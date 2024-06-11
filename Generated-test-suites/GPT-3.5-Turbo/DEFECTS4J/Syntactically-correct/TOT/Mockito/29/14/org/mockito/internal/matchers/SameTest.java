import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

public class SameTest {

  private Same same;

  @Before
  public void setUp() {
    same = new Same(null);
  }

  // Test for the constructor
  @Test
  public void constructorTest() {
    Object expected = new Object();
    Same same = new Same(expected);
    assertSame(expected, same.wanted);
  }

  // Test for matches(Object actual)
  @Test
  public void matchesTest_sameObject() {
    Object expected = new Object();
    Same same = new Same(expected);
    assertTrue(same.matches(expected));
  }

  @Test
  public void matchesTest_differentObject() {
    Object expected = new Object();
    Same same = new Same(expected);
    assertFalse(same.matches(new Object()));
  }

  // Test for describeTo(Description description)
  @Test
  public void describeToTest_string() {
    Same same = new Same("hello");
    Description description = mock(Description.class);
    same.describeTo(description);

    verify(description).appendText("same(\"hello\")");
  }

  @Test
  public void describeToTest_character() {
    Same same = new Same('a');
    Description description = mock(Description.class);
    same.describeTo(description);

    verify(description).appendText("same('a')");
  }

  @Test
  public void describeToTest_otherObject() {
    Same same = new Same(new Object());
    Description description = mock(Description.class);
    same.describeTo(description);

    verify(description).appendText("same(");
    verify(description).appendText(anyString());
    verify(description).appendText(")");
  }

}