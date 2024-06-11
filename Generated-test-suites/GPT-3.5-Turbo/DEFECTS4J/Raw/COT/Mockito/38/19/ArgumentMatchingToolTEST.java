
import org.junit.Assert;
import org.junit.Test;
import org.hamcrest.CoreMatchers;

public class ArgumentMatchingToolTest {
  
  @Test
  public void testGetSuspiciouslyNotMatchingArgsIndexes() {
    ArgumentMatchingTool tool = new ArgumentMatchingTool();
    
    List<Matcher> matchers = new LinkedList<Matcher>();
    matchers.add(CoreMatchers.is(1));
    matchers.add(CoreMatchers.is("test"));
    matchers.add(CoreMatchers.is(true));
    
    Object[] arguments = {1, "test", false};
    
    Integer[] expected = {};
    Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    
    Assert.assertArrayEquals(expected, indexes);
  }
  
  @Test
  public void testGetSuspiciouslyNotMatchingArgsIndexes_LengthMismatch() {
    ArgumentMatchingTool tool = new ArgumentMatchingTool();
    
    List<Matcher> matchers = new LinkedList<Matcher>();
    matchers.add(CoreMatchers.is(1));
    matchers.add(CoreMatchers.is("test"));
    matchers.add(CoreMatchers.is(true));
    
    Object[] arguments = {1, "test"};
    
    Integer[] expected = {};
    Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    
    Assert.assertArrayEquals(expected, indexes);
  }
  
  @Test
  public void testGetSuspiciouslyNotMatchingArgsIndexes_ContainsExtraTypeInformation() {
    ArgumentMatchingTool tool = new ArgumentMatchingTool();
    
    List<Matcher> matchers = new LinkedList<Matcher>();
    matchers.add(CoreMatchers.instanceOf(String.class));
    matchers.add(CoreMatchers.is("test"));
    
    Object[] arguments = {new ContainsExtraTypeInformation(String.class), "test"};
    
    Integer[] expected = {};
    Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    
    Assert.assertArrayEquals(expected, indexes);
  }
  
  @Test
  public void testGetSuspiciouslyNotMatchingArgsIndexes_SafeMatches() {
    ArgumentMatchingTool tool = new ArgumentMatchingTool();
    
    List<Matcher> matchers = new LinkedList<Matcher>();
    matchers.add(CoreMatchers.is(1));
    matchers.add(CoreMatchers.is("test"));
    
    Object[] arguments = {CoreMatchers.is(1), "test"};
    
    Integer[] expected = {};
    Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    
    Assert.assertArrayEquals(expected, indexes);
  }
  
  @Test
  public void testGetSuspiciouslyNotMatchingArgsIndexes_ToStringEquals() {
    ArgumentMatchingTool tool = new ArgumentMatchingTool();
    
    List<Matcher> matchers = new LinkedList<Matcher>();
    matchers.add(CoreMatchers.is(1));
    matchers.add(CoreMatchers.is("test"));
    
    Object[] arguments = {"1", "test"};
    
    Integer[] expected = {};
    Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    
    Assert.assertArrayEquals(expected, indexes);
  }
  
  @Test
  public void testGetSuspiciouslyNotMatchingArgsIndexes_TypeDoesNotMatch() {
    ArgumentMatchingTool tool = new ArgumentMatchingTool();
    
    List<Matcher> matchers = new LinkedList<Matcher>();
    matchers.add(CoreMatchers.is(1));
    matchers.add(CoreMatchers.is("test"));
    
    Object[] arguments = {1, 2};
    
    Integer[] expected = {1};
    Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    
    Assert.assertArrayEquals(expected, indexes);
  }
  
  @Test
  public void testSafelyMatches_ThrowsException() {
    ArgumentMatchingTool tool = new ArgumentMatchingTool();
    
    Matcher m = CoreMatchers.is(1);
    Object arg = new Object();
    
    boolean result = tool.safelyMatches(m, arg);
    
    Assert.assertFalse(result);
  }
  
  @Test
  public void testToStringEquals() {
    ArgumentMatchingTool tool = new ArgumentMatchingTool();
    
    Matcher m = CoreMatchers.is("test");
    Object arg = "test";
    
    boolean result = tool.toStringEquals(m, arg);
    
    Assert.assertTrue(result);
  }
}
