
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {
  
  @Test
  public void testStripLeadingHyphens() {
    String input1 = "--test";
    String output1 = Util.stripLeadingHyphens(input1);
    assertEquals("test", output1);
    
    String input2 = "-test";
    String output2 = Util.stripLeadingHyphens(input2);
    assertEquals("test", output2);
    
    String input3 = "test";
    String output3 = Util.stripLeadingHyphens(input3);
    assertEquals("test", output3);
  }
  
  @Test
  public void testStripLeadingAndTrailingQuotes() {
    String input1 = "\"one two\"";
    String output1 = Util.stripLeadingAndTrailingQuotes(input1);
    assertEquals("one two", output1);
    
    String input2 = "test";
    String output2 = Util.stripLeadingAndTrailingQuotes(input2);
    assertEquals("test", output2);
  }
  
}
