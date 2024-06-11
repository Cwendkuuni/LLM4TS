import static org.junit.Assert.assertEquals;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import java.util.List;

public class ArgumentMatchingToolTest {
  
  @Test
  public void testGetSuspiciouslyNotMatchingArgsIndexes() {
    ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
    
    // Test when matchers and arguments have different sizes
    List<Matcher> matchers1 = new LinkedList<Matcher>();
    Object[] arguments1 = new Object[2];
    Integer[] result1 = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers1, arguments1);
    assertEquals(0, result1.length);
    
    // Test when all matchers safely match arguments
    List<Matcher> matchers2 = new LinkedList<Matcher>();
    matchers2.add(new ContainsExtraTypeInformation());
    matchers2.add(new ContainsExtraTypeInformation());
    Object[] arguments2 = new Object[2];
    arguments2[0] = "test";
    arguments2[1] = "test";
    Integer[] result2 = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers2, arguments2);
    assertEquals(0, result2.length);
    
    // Test when there is a suspiciously not matching argument
    List<Matcher> matchers3 = new LinkedList<Matcher>();
    matchers3.add(new ContainsExtraTypeInformation());
    Object[] arguments3 = new Object[1];
    arguments3[0] = 1;
    Integer[] result3 = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers3, arguments3);
    assertEquals(1, result3.length);
    assertEquals(Integer.valueOf(0), result3[0]);
  }
}