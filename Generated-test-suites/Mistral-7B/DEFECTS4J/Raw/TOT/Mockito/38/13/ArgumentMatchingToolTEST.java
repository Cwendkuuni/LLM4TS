import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool tool;

 @Before
 public void setUp() {
 tool = new ArgumentMatchingTool();
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_SameSizeLists_NoSuspiciousArgs() {
 List<Matcher> matchers = new LinkedList<>();
 Object[] arguments = new Object[]{"String1", 1, 3.0};
 matchers.add(contains("String1"));
 matchers.add(isInstanceOf(Integer.class));
 matchers.add(isInstanceOf(Double.class));
 Integer[] expected = new Integer[0];
 Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertTrue(Arrays.equals(expected, actual));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_DifferentSizes_NoSuspiciousArgs() {
 List<Matcher> matchers = new LinkedList<>();
 Object[] arguments = new Object[]{"String1", 1, 3.0, "FourthElement"};
 matchers.add(contains("String1"));
 matchers.add(isInstanceOf(Integer.class));
 matchers.add(isInstanceOf(Double.class));
 Integer[] expected = new Integer[0];
 Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertTrue(Arrays.equals(expected, actual));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciousArgsPresent() {
 List<Matcher> matchers = new LinkedList<>();
 Object[] arguments = new Object[]{"String1", new ContainsExtraTypeInformation("Integer"){@Override public boolean typeMatches(Object arg) { return arg instanceof String; }}, 3.0};
 matchers.add(contains("String1"));
 matchers.add(instanceOf(Integer.class));
 matchers.add(instanceOf(String.class));
 Integer[] expected = {1};
 Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertTrue(Arrays.equals(expected, actual));
 }

 @Test(expected = AssertionError.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_InvalidMatcherType() {
 List<Matcher> matchers = new LinkedList<>();
 Object[] arguments = new Object[]{"String1", 1, 3.0};
 matchers.add(new Object() {});
 matchers.add(contains("String1"));
 matchers.add(isInstanceOf(Integer.class));
 tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 }

 @Test(expected = AssertionError.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_InvalidArgumentType() {
 List<Matcher> matchers = new LinkedList<>();
 Object[] arguments = new Object[]{"String1", 1, new Object() {}};
 matchers.add(contains("String1"));
 matchers.add(isInstanceOf(Integer.class));
 matchers.add(isInstanceOf(String.class));
 tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 }
}