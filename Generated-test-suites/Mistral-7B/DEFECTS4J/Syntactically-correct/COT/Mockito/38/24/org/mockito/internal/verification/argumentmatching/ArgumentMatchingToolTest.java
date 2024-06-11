import org.junit.Test;
import org.junit.Assert.*;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool tool = new ArgumentMatchingTool();

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_emptyList() {
     List<Matcher> matchers = new java.util.ArrayList<>();
     Object[] arguments = new Object[0];
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_noSuspiciousMatch() {
     List<Matcher> matchers = new java.util.ArrayList<>();
     matchers.add(Matchers.is(1));
     Object[] arguments = new Object[]{1};
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_oneSuspiciousMatch() {
     List<Matcher> matchers = new java.util.ArrayList<>();
     matchers.add(Matchers.is(1));
     Object[] arguments = new Object[]{2};
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[]{0}, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_mismatchedSizes() {
     List<Matcher> matchers = new java.util.ArrayList<>();
     matchers.add(Matchers.is(1));
     Object[] arguments = new Object[0];
     tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 }

 @Test
 public void testToStringEquals_sameString() {
     Matcher m = Matchers.is("test");
     Object arg = "test";
     assertTrue(tool.toStringEquals(m, arg));
 }

 @Test
 public void testToStringEquals_differentStrings() {
     Matcher m = Matchers.is("test1");
     Object arg = "test2";
     assertFalse(tool.toStringEquals(m, arg));
 }

 @Test
 public void testSafelyMatches_matches() {
     Matcher m = Matchers.is(1);
     Object arg = 1;
     assertTrue(tool.safelyMatches(m, arg));
 }

 @Test
 public void testSafelyMatches_mismatches() {
     Matcher m = Matchers.is(1);
     Object arg = 2;
     assertFalse(tool.safelyMatches(m, arg));
 }

 @Test(expected = Throwable.class)
 public void testSafelyMatches_throwsException() {
     Matcher m = Matchers.is("test");
     Object arg = new Object() {
         @Override
         public String toString() {
             throw new RuntimeException();
         }
     };
     assertFalse(tool.safelyMatches(m, arg));
 }

 @Test
 public void testContainsExtraTypeInformation_sameType() {
     Matcher m = Matchers.is(1);
     ContainsExtraTypeInformation c = new ContainsExtraTypeInformation(Integer.class, null);
     assertTrue(tool.containsExtraTypeInformationMatches(m, c));
 }

 @Test
 public void testContainsExtraTypeInformation_differentTypes() {
     Matcher m = Matchers.is("test");
     ContainsExtraTypeInformation c = new ContainsExtraTypeInformation(String.class, null);
     assertFalse(tool.containsExtraTypeInformationMatches(m, c));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_multipleSuspiciousMatches() {
     List<Matcher> matchers = new java.util.ArrayList<>();
     matchers.add(Matchers.is(1));
     matchers.add(Matchers.is("test1"));
     Object[] arguments = new Object[]{1, "test2"};
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[]{0, 1}, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_multipleSuspiciousMatches_multipleMatchesInOneArg() {
     List<Matcher> matchers = new java.util.ArrayList<>();
     matchers.add(Matchers.containsString("test"));
     Object[] arguments = new Object[]{"test1test", "test2"};
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[]{0}, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_multipleSuspiciousMatches_multipleMatchesInDifferentArgs() {
     List<Matcher> matchers = new java.util.ArrayList<>();
     matchers.add(Matchers.is(1));
     matchers.add(Matchers.is("test1"));
     Object[] arguments = new Object[]{1, "test2"};
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[]{0, 1}, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformation() {
     List<Matcher> matchers = new java.util.ArrayList<>();
     matchers.add(new ContainsExtraTypeInformation(Integer[].class, null));
     Object[] arguments = new Object[]{new Integer[]{1, 2}, "test"};
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[]{1}, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformation_multipleMatches() {
     List<Matcher> matchers = new java.util.ArrayList<>();
     matchers.add(new ContainsExtraTypeInformation(Integer[].class, null));
     matchers.add(new ContainsExtraTypeInformation(Double[].class, null));
     Object[] arguments = new Object[]{new Integer[]{1, 2}, new Double[]{3.0, 4.0}};
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[]{1, 1}, result);
 }

}