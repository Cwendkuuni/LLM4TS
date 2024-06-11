import org.mockito.internal.matchers.Same;
import org.hamcrest.Description;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SameTest {

  // Test the constructor
  @Test
  public void testConstructor() {
    Same same = new Same("test");
    assertNotNull(same);
  }

  // Test the matches() method
  @Test
  public void testMatches() {
    Same same = new Same("test");
    assertTrue(same.matches("test"));
    assertFalse(same.matches("different"));
  }

  // Test the describeTo() method
  @Test
  public void testDescribeTo() {
    Same same = new Same("test");
    Description description = mock(Description.class);
    same.describeTo(description);
    verify(description).appendText("same(\"test\")");
  }
}