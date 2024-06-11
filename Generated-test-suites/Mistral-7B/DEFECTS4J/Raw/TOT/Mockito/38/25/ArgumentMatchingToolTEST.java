import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool tool = new ArgumentMatchingTool();

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_sameSize_noSuspiciousArgs() {
 List<Matcher> matchers = new LinkedList<>();
 Object[] arguments = new Object[]{"str1", 1, 2.0};
 matchers.add(contains("str1"));
 matchers.add(eq(1));
 matchers.add(isA(Double.class));
 Integer[] expected = new Integer[0];
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_sameSize_suspiciousArg() {
 List<Matcher> matchers = new LinkedList<>();
 Object[] arguments = new Object[]{"str1", 1, "str1"};
 matchers.add(contains("str1"));
 matchers.add(eq(1));
 matchers.add(instanceOf(String.class));
 Integer[] expected = {1};
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_differentSizes() {
 List<Matcher> matchers = new LinkedList<>();
 Object[] arguments = new Object[]{"str1", 1, 2.0, 3};
 matchers.add(contains("str1"));
 matchers.add(eq(1));
 matchers.add(isA(Double.class));
 }

 @Test
 public void testSafelyMatches() {
 Matcher matcher = contains("str1");
 Object arg = "str1";
 assertTrue(tool.safelyMatches(matcher, arg));
 }

 @Test
 public void testSafelyMatches_false() {
 Matcher matcher = contains("str1");
 Object arg = "str2";
 assertFalse(tool.safelyMatches(matcher, arg));
 }

 @Test
 public void testToStringEquals_equalStrings() {
 Matcher matcher = contains("str1");
 Object arg = "str1";
 assertTrue(tool.toStringEquals(matcher, arg));
 }

 @Test
 public void testToStringEquals_notEqualStrings() {
 Matcher matcher = contains("str1");
 Object arg = "str2";
 assertFalse(tool.toStringEquals(matcher, arg));
 }

 @Test
 public void testToStringEquals_nullArg() {
 Matcher matcher = contains("str1");
 Object arg = null;
 assertFalse(tool.toStringEquals(matcher, arg));
 }

 @Test
 public void testToStringEquals_matcherWithEmptyDescription() {
 Matcher matcher = isNull();
 Object arg = "";
 assertTrue(tool.toStringEquals(matcher, arg));
 }

 @Test
 public void testToStringEquals_matcherWithComplexDescription() {
 Matcher matcher = containsString("abcd efgh");
 Object arg = "abcd efgh ijkl";
 assertTrue(tool.toStringEquals(matcher, arg));
 }

 @Test
 public void testToStringEquals_matcherWithNegatedDescription() {
 Matcher matcher = not(contains("str1"));
 Object arg = "str1";
 assertFalse(tool.toStringEquals(matcher, arg));
 }

 @Test
 public void testToStringEquals_matcherWithComplexNegatedDescription() {
 Matcher matcher = not(containsString("abcd efgh"));
 Object arg = "abcd efgh ijkl";
 assertTrue(tool.toStringEquals(matcher, arg));
 }

 @Test
 public void testToStringEquals_matcherWithDifferentClass() {
 Matcher matcher = contains("str1");
 Object arg = 1;
 assertFalse(tool.toStringEquals(matcher, arg));
 }

 @Test
 public void testTypeMatches() {
 Matcher matcher = contains("str1");
 Object arg = "str1";
 assertTrue(tool.getTypeMatches(matcher, arg));
 }

 @Test
 public void testTypeMatches_false() {
 Matcher matcher = contains("str1");
 Object arg = 1;
 assertFalse(tool.getTypeMatches(matcher, arg));
 }

 @Test
 public void testTypeMatches_nullArg() {
 Matcher matcher = contains("str1");
 Object arg = null;
 assertFalse(tool.getTypeMatches(matcher, arg));
 }

 @Test
 public void testTypeMatches_matcherWithEmptyDescription() {
 Matcher matcher = isNull();
 Object arg = null;
 assertTrue(tool.getTypeMatches(matcher, arg));
 }

 @Test
 public void testTypeMatches_matcherWithComplexDescription() {
 Matcher matcher = containsString("abcd efgh");
 Object arg = "abcd efgh ijkl";
 assertTrue(tool.getTypeMatches(matcher, arg));
 }

 @Test
 public void testTypeMatches_matcherWithNegatedDescription() {
 Matcher matcher = not(contains("str1"));
 Object arg = "str1";
 assertFalse(tool.getTypeMatches(matcher, arg));
 }

 @Test
 public void testTypeMatches_matcherWithComplexNegatedDescription() {
 Matcher matcher = not(containsString("abcd efgh"));
 Object arg = "abcd efgh ijkl";
 assertTrue(tool.getTypeMatches(matcher, arg));
 }

 @Test
 public void testTypeMatches_matcherWithDifferentClass() {
 Matcher matcher = contains("str1");
 Object arg = 1;
 assertFalse(tool.getTypeMatches(matcher, arg));
 }
}