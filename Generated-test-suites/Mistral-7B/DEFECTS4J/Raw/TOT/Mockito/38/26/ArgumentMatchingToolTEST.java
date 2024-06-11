import org.junit.jupiter.params.JUnitParams;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool tool = new ArgumentMatchingTool();

 @ParameterizedTest(name = "{index}: getSuspiciouslyNotMatchingArgsIndexes test case {0} with arguments {1} and matchers {2}")
 @ArgumentsSource(ValueProvider.class)
 void getSuspiciouslyNotMatchingArgsIndexesTest(Integer[] expectedResult, List<Matcher> matchers, Object[] arguments) {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(expectedResult, result);
 }

 @Test
 void testGetSuspiciouslyNotMatchingArgsIndexesWithEmptyList() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher> matchers = Collections.emptyList();
 Object[] arguments = new Object[1];
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertEquals(new Integer[0], result);
 }

 @Test
 void testGetSuspiciouslyNotMatchingArgsIndexesWithDifferentSizeLists() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher> matchers = Arrays.asList(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString());
 Object[] arguments = new Object[]{1, "two"};
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertEquals(new Integer[0], result);
 }

 @Test
 void testGetSuspiciouslyNotMatchingArgsIndexesWithNoSuspiciousMatches() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq("one"));
 Object[] arguments = new Object[]{1, "one"};
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertEquals(new Integer[0], result);
 }

 @Test
 void testGetSuspiciouslyNotMatchingArgsIndexesWithSuspiciousMatch() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher> matchers = Arrays.asList(
 ArgumentMatchers.containsString("one"),
 ArgumentMatchers.containsString("two"),
 ArgumentMatchers.instanceOf(String.class)
 );
 Object[] arguments = new Object[]{"one", 1};
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertEquals(new Integer[]{1}, result);
 }

 @Test
 void testGetSuspiciouslyNotMatchingArgsIndexesWithExtraTypeInformation() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher> matchers = Arrays.asList(
 ArgumentMatchers.instanceOf(ContainsExtraTypeInformation.class),
 ArgumentMatchers.eq(new String("one")),
 ArgumentMatchers.anyInt()
 );
 Object[] arguments = new Object[]{new ContainsExtraTypeInformation("one"), "one", 1};
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertEquals(new Integer[]{0}, result);
 }

 @Test
 void testToStringEqualsWithDifferentObjects() {
 Matcher m1 = ArgumentMatchers.containsString("one");
 Matcher m2 = ArgumentMatchers.containsString("two");
 Object arg1 = "one";
 Object arg2 = "two";
 assertEquals(arg1.toString(), arg2.toString());
 assertFalse(toStringEquals(m1, arg1));
 assertFalse(toStringEquals(m1, arg2));
 assertFalse(toStringEquals(m2, arg1));
 assertFalse(toStringEquals(m2, arg2));
 }

 @Test
 void testToStringEqualsWithSameObjects() {
 Matcher m = ArgumentMatchers.containsString("one");
 Object arg = "one";
 assertTrue(toStringEquals(m, arg));
 }

 @Test
 void testSafelyMatchesWithMatchedArgument() {
 Matcher m = ArgumentMatchers.containsString("one");
 Object arg = "one";
 assertTrue(safelyMatches(m, arg));
 }

 @Test
 void testSafelyMatchesWithUnmatchedArgument() {
 Matcher m = ArgumentMatchers.containsString("one");
 Object arg = "two";
 assertFalse(safelyMatches(m, arg));
 }

 private static Object[][] valueProvider() {
 return new Object[][]{{new Integer[]{0}, Arrays.asList(ArgumentMatchers.eq(null), ArgumentMatchers.eq(1)), new Object[]{null}},
 {new Integer[]{1}, Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq(2)), new Object[]{1, 2}},
 {new Integer[0], Arrays.asList(ArgumentMatchers.containsString("one"), ArgumentMatchers.containsString("two"), ArgumentMatchers.instanceOf(String.class)), new Object[]{"one", "two", "three"}},
 {new Integer[]{2}, Arrays.asList(ArgumentMatchers.instanceOf(ContainsExtraTypeInformation.class), ArgumentMatchers.eq(new String("one")), ArgumentMatchers.anyInt()), new Object[]{new ContainsExtraTypeInformation("one"), "one", 1}}};
 }

 private boolean toStringEquals(Matcher m, Object arg) {
 return ArgumentMatchingTool.toStringEquals(m, arg);
 }

 private ArgumentMatchingTool.SafelyMatches safelyMatches(Matcher m, Object arg) {
 return ArgumentMatchingTool.safelyMatches(m, arg);
 }
}

class ContainsExtraTypeInformation extends String {
 public ContainsExtraTypeInformation(String str) {
 super(str);
 }
}