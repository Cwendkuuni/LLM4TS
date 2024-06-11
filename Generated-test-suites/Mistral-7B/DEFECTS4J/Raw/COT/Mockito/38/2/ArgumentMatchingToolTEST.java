import org.junit.Test;
import org.junit.Assert.*;
import org.hamcrest.Matcher;
import org.hamcrest.ContainsExtraTypeInformation;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.Arguments;

public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool tool = new ArgumentMatchingTool();

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NullArguments() {
     List<Matcher> matchers = new java.util.ArrayList<>();
     Object[] arguments = null;
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MismatchedSizes() {
     List<Matcher> matchers = new java.util.ArrayList<>();
     Object[] arguments = new Object[5];
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NoSuspiciousMatches() {
     List<Matcher> matchers = new java.util.ArrayList<>();
     Object[] arguments = new Object[]{1, "two", 3, "four"};
     Matcher m1 = ArgumentMatchers.equalTo(1);
     Matcher m2 = ArgumentMatchers.equalTo("two");
     Matcher m3 = ArgumentMatchers.equalTo(3);
     Matcher m4 = ArgumentMatchers.equalTo("four");
     matchers.add(m1);
     matchers.add(m2);
     matchers.add(m3);
     matchers.add(m4);
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciousMatch() {
     List<Matcher> matchers = new java.util.ArrayList<>();
     Object[] arguments = new Object[]{1, "one", 3, "two"};
     Matcher m1 = ArgumentMatchers.equalTo(1);
     Matcher m2 = ArgumentMatchers.instanceOf(String.class).and(ContainsExtraTypeInformation.type(String.class));
     Matcher m3 = ArgumentMatchers.equalTo(3);
     Matcher m4 = ArgumentMatchers.instanceOf(String.class).and(ContainsExtraTypeInformation.type(Integer.class));
     matchers.add(m1);
     matchers.add(m2);
     matchers.add(m3);
     matchers.add(m4);
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[]{3}, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NullMatchers() {
     Object[] arguments = new Object[]{1, "one", 3, "two"};
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(null, arguments);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NullArguments() {
     List<Matcher> matchers = new java.util.ArrayList<>();
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_EmptyMatchers() {
     Object[] arguments = new Object[]{1, "one", 3, "two"};
     List<Matcher> matchers = new java.util.ArrayList<>();
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 }

 @Test(expected = NullPointerException.class)
 public void testSafelyMatches_NullMatcher() {
     Object arg = new Object();
     Matcher m = null;
     Assert.assertFalse(tool.safelyMatches(m, arg));
 }

 @Test
 public void testToStringEquals_SameObject() {
     Matcher m = Arguments.is(new Object());
     Object arg = new Object();
     assertTrue(tool.toStringEquals(m, arg));
 }

 @Test
 public void testToStringEquals_DifferentObjects() {
     Matcher m1 = Arguments.is("one");
     Matcher m2 = Arguments.is("two");
     assertFalse(tool.toStringEquals(m1, m2));
 }

 @Test
 public void testToStringEquals_DifferentClassTypes() {
     Matcher m1 = Arguments.is(1);
     Matcher m2 = Arguments.is("one");
     assertFalse(tool.toStringEquals(m1, m2));
 }

 @Test
 public void testToStringEquals_SameClassTypesButDifferentValues() {
     Matcher m1 = Arguments.is(1);
     Matcher m2 = Arguments.is(2);
     assertFalse(tool.toStringEquals(m1, m2));
 }

 @Test
 public void testToStringEquals_SameMatcherWithDifferentArguments() {
     Matcher m = Arguments.is("one");
     Object arg1 = "one";
     Object arg2 = "two";
     assertFalse(tool.toStringEquals(m, arg2));
 }

}