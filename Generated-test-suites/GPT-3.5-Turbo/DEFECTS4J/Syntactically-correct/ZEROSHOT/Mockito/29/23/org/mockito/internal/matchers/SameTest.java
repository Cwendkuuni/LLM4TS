import org.hamcrest.Description;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.matchers.Same;

public class SameTest {

  @Test
  public void testMatches() {
    Same same = new Same("test");
    Assert.assertTrue(same.matches("test"));
    Assert.assertFalse(same.matches("other"));
  }

  @Test
  public void testDescribeTo() {
    Same same = new Same("test");
    Description description = new StringDescription();
    same.describeTo(description);
    Assert.assertEquals("same(\"test\")", description.toString());
  }

  @Test
  public void testAppendQuoting() {
    Same same = new Same('a');
    Description description = new StringDescription();
    same.appendQuoting(description);
    Assert.assertEquals("'", description.toString());

    same = new Same("test");
    description = new StringDescription();
    same.appendQuoting(description);
    Assert.assertEquals("\"", description.toString());
  }
}