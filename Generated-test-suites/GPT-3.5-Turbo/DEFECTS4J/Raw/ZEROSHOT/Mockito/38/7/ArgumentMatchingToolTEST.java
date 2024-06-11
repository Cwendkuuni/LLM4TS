import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

import static org.junit.Assert.*;

public class ArgumentMatchingToolTest {
  
  @Test
  public void testGetSuspiciouslyNotMatchingArgsIndexes_matchingArgsSizeMismatch() {
    ArgumentMatchingTool tool = new ArgumentMatchingTool();
    List<Matcher> matchers = Arrays.asList();
    Object[] arguments = {1, 2};
    Integer[] expected = {};
    
    Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    
    assertArrayEquals(expected, actual);
  }
  
  @Test
  public void testGetSuspiciouslyNotMatchingArgsIndexes_noSuspiciousArgs() {
    ArgumentMatchingTool tool = new ArgumentMatchingTool();
    List<Matcher> matchers = Arrays.asList();
    Object[] arguments = {};
    Integer[] expected = {};
    
    Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    
    assertArrayEquals(expected, actual);
  }
  
  @Test
  public void testGetSuspiciouslyNotMatchingArgsIndexes_matchingArgs() {
    ArgumentMatchingTool tool = new ArgumentMatchingTool();
    List<Matcher> matchers = Arrays.asList();
    Object[] arguments = {};
    Integer[] expected = {};
    
    Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    
    assertArrayEquals(expected, actual);
  }
  
  // Additional tests for other methods in the ArgumentMatchingTool class
  
}