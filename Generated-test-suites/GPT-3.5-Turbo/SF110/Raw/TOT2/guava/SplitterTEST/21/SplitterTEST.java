import static org.junit.Assert.*;
import org.junit.Test;

public class SplitterTest {
  
  @Test
  public void testOnCharSeparator() {
    Splitter splitter = Splitter.on(',');
    assertNotNull(splitter);
    // Add more test cases for different scenarios
  }
  
  @Test
  public void testOnStringSeparator() {
    Splitter splitter = Splitter.on(";");
    assertNotNull(splitter);
    // Add more test cases for different scenarios
  }
  
  @Test
  public void testOnPatternSeparator() {
    Splitter splitter = Splitter.on(Pattern.compile("\\s+"));
    assertNotNull(splitter);
    // Add more test cases for different scenarios
  }
  
  // Add similar test methods for other public methods
  
}