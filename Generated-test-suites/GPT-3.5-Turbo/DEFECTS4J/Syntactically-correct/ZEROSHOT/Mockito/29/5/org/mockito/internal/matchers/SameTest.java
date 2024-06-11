import org.junit.Test;
import static org.junit.Assert.*;

public class SameTest {

  @Test
  public void testMatches() {
    Object wanted = new Object();
    Same same = new Same(wanted);
    
    assertSame(wanted, wanted);
    assertSame(wanted, same);
    
    Object notWanted = new Object();
    assertFalse(same.matches(notWanted));
  }

  @Test
  public void testDescribeTo() {
    Object wanted = new Object();
    Same same = new Same(wanted);
    DescriptionMock descriptionMock = new DescriptionMock();
    
    same.describeTo(descriptionMock);
    
    assertEquals("same(" + wanted.toString() + ")", descriptionMock.getDescription());
  }
  
  private static class DescriptionMock implements Description {
    private String description;
    
    @Override
    public Description appendText(String text) {
      description += text;
      return this;
    }
    
    @Override
    public String toString() {
      return description;
    }
    
    // Additional methods for testing purposes
    
    public String getDescription() {
      return description;
    }
  }

}