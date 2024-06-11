import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import java.util.Arrays;
import java.util.List;

public class ArgumentMatchingToolTest {

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersAndArgumentsMatch() {
 List<Matcher> matchers = Arrays.asList(isA(Integer.class), isA(Integer.class));
 Object[] arguments = new Object[]{1, 2};
 Integer[] expected = new Integer[0];
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MatcherDoesNotMatchArgument() {
 List<Matcher> matchers = Arrays.asList(isA(String.class), isA(Integer.class));
 Object[] arguments = new Object[]{1, "one"};
 Integer[] expected = new Integer[]{0};
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(expected, actual);
 }

 @Test(expected = AssertionError.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_DifferentNumberOfMatchersAndArguments() {
 List<Matcher> matchers = Arrays.asList(isA(Integer.class));
 Object[] arguments = new Object[]{1, 2};
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_ToStringEqualsButTypesDontMatch() {
 List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation(Integer.class, "int"), isA(String.class));
 Object[] arguments = new Object[]{1, "one"};
 Integer[] expected = new Integer[]{0};
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(expected, actual);
 }

 @Test
 public void testToStringEquals_MatcherAndArgumentHaveDifferentToStringRepresentation() {
 List<Matcher> matchers = Arrays.asList(isA(String.class), new ContainsText("one"));
 Object[] arguments = new Object[]{"two"};
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 Assert.assertFalse(tool.toStringEquals(matchers.get(0), arguments[0]));
 }

 @Test
 public void testToStringEquals_MatcherAndArgumentHaveSameToStringRepresentation() {
 List<Matcher> matchers = Arrays.asList(isA(String.class), new ContainsText("one"));
 Object[] arguments = new Object[]{"one"};
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 Assert.assertTrue(tool.toStringEquals(matchers.get(0), arguments[0]));
 }
}