import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {
  
  private ArgumentMatchingTool argumentMatchingTool;
  
  @Before
  public void setUp() {
    argumentMatchingTool = new ArgumentMatchingTool();
  }
  
  @After
  public void tearDown() {
    argumentMatchingTool = null;
  }
  
  @Test
  public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchSizeNotEqual_returnEmptyArray() {
    // Arrange
    List<Matcher> matchers = new LinkedList<Matcher>();
    Object[] arguments = new Object[4];
    
    // Act
    Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    
    // Assert
    assertEquals(0, result.length);
  }
  
  @Test
  public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatcherNotContainsExtraTypeInformation_returnEmptyArray() {
    // Arrange
    List<Matcher> matchers = new LinkedList<Matcher>();
    matchers.add(new ContainsExtraTypeInformation());
    matchers.add(new ContainsExtraTypeInformation());
    matchers.add(new ContainsExtraTypeInformation());
    Object[] arguments = new Object[]{"arg1", "arg2", "arg3"};
    
    // Act
    Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    
    // Assert
    assertEquals(0, result.length);
  }
  
  @Test
  public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatcherSafelyMatches_returnEmptyArray() {
    // Arrange
    List<Matcher> matchers = new LinkedList<Matcher>();
    matchers.add(new ContainsExtraTypeInformation());
    matchers.add(new ContainsExtraTypeInformation());
    matchers.add(new ContainsExtraTypeInformation());
    Object[] arguments = new Object[]{new Object(), new Object(), new Object()};
    
    // Act
    Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    
    // Assert
    assertEquals(0, result.length);
  }
  
  @Test
  public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatcherToStringEquals_returnEmptyArray() {
    // Arrange
    List<Matcher> matchers = new LinkedList<Matcher>();
    matchers.add(new ContainsExtraTypeInformation());
    matchers.add(new ContainsExtraTypeInformation());
    matchers.add(new ContainsExtraTypeInformation());
    Object[] arguments = new Object[]{"arg1", "arg2", "arg3"};
    
    // Act
    Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    
    // Assert
    assertEquals(0, result.length);
  }
  
  @Test
  public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatcherTypeMatches_returnEmptyArray() {
    // Arrange
    List<Matcher> matchers = new LinkedList<Matcher>();
    ContainsExtraTypeInformation matcher = new ContainsExtraTypeInformation();
    matchers.add(matcher);
    matchers.add(new ContainsExtraTypeInformation());
    matchers.add(new ContainsExtraTypeInformation());
    Object[] arguments = new Object[]{matcher.getType(), "arg2", "arg3"};
    
    // Act
    Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    
    // Assert
    assertEquals(0, result.length);
  }
  
  @Test
  public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatcherIsSuspicious_returnArrayWithIndex() {
    // Arrange
    List<Matcher> matchers = new LinkedList<Matcher>();
    ContainsExtraTypeInformation matcher = new ContainsExtraTypeInformation();
    matchers.add(matcher);
    matchers.add(new ContainsExtraTypeInformation());
    matchers.add(new ContainsExtraTypeInformation());
    Object[] arguments = new Object[]{new Object(), "arg2", "arg3"};
    
    // Act
    Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    
    // Assert
    assertEquals(1, result.length);
    assertEquals(Integer.valueOf(0), result[0]);
  }
}