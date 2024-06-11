import static org.junit.Assert.*;
import org.junit.Test;
import org.hamcrest.Description;

public class SameTest {

  @Test
  public void testMatches() {
    Same same = new Same(null);
    assertFalse(same.matches("abc")); // basic test case
    
    same = new Same("abc");
    assertTrue(same.matches("abc")); // basic test case
  }
  
  @Test
  public void testDescribeTo() {
    Same same = new Same(null);
    Description description = new MyDescription();
    same.describeTo(description);
    assertEquals("same(null)", description.toString()); // basic test case
    
    same = new Same("abc");
    description = new MyDescription();
    same.describeTo(description);
    assertEquals("same(\"abc\")", description.toString()); // basic test case
  }
  
  private static class MyDescription extends Description {
    private StringBuilder sb = new StringBuilder();
    
    @Override
    public Description appendText(String text) {
      sb.append(text);
      return this;
    }

    @Override
    public Description appendDescriptionOf(SelfDescribing value) {
      value.describeTo(this);
      return this;
    }
    
    @Override
    public String toString() {
      return sb.toString();
    }
  }
}