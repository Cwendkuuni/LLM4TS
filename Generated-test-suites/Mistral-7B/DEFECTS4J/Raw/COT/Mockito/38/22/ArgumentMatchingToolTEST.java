import org.junit.Test;
import org.junit.Assert.*;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool tool = new ArgumentMatchingTool();

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_ValidArguments() {
     List<Matcher> matchers = new java.util.ArrayList<>();
     matchers.add(Matchers.is(1));
     matchers.add(Matchers.is(2));
     matchers.add(Matchers.isA(Integer.class));

     Object[] arguments = new Object[]{1, 2, 3};

     Integer[] expectedResult = new Integer[0];
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(expectedResult, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciouslyNotMatchingArgument() {
     List<Matcher> matchers = new ArrayList<>();
     matchers.add(Matchers.is(1));
     matchers.add(Matchers.isInstanceOf(String.class));
     matchers.add(Matchers.isA(Integer.class));

     Object[] arguments = new Object[]{1, "1", 3};

     Integer[] expectedResult = new Integer[]{1};
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_InvalidNumberOfMatchers() {
     List<Matcher> matchers = new ArrayList<>();
     matchers.add(Matchers.is(1));

     Object[] arguments = new Object[]{1, 2, 3};

     tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_InvalidNumberOfArguments() {
     List<Matcher> matchers = new ArrayList<>();
     matchers.add(Matchers.is(1));

     Object[] arguments = new Object[]{1};

     tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 }

 @Test
 public void testSafelyMatches_Matches() {
     Matcher m = Matchers.is(1);
     Object arg = 1;

     assertTrue(tool.safelyMatches(m, arg));
 }

 @Test
 public void testSafelyMatches_DoesNotMatch() {
     Matcher m = Matchers.is(1);
     Object arg = "1";

     assertFalse(tool.safelyMatches(m, arg));
 }

 @Test
 public void testToStringEquals_Matches() {
     Matcher m = Matchers.is("1");
     Object arg = "1";

     assertTrue(tool.toStringEquals(m, arg));
 }

 @Test
 public void testToStringEquals_DoesNotMatch() {
     Matcher m = Matchers.is("1");
     Object arg = 1;

     assertFalse(tool.toStringEquals(m, arg));
 }

 @Test
 public void testContainsExtraTypeInformation_Matches() {
     Matcher m = new ContainsExtraTypeInformation("java.lang.String", Matchers.is("1"));
     Object arg = "1";

     assertTrue(m.matches(arg));
     assertTrue(tool.safelyMatches(m, arg));
     assertTrue(tool.toStringEquals(m, arg.toString()));
     assertTrue(tool.toStringEquals(m.description(), arg.toString()));
     assertTrue(tool.toStringEquals(m.toString(), arg.toString()));
     assertTrue(((ContainsExtraTypeInformation) m).typeMatches(arg.getClass()));
 }

 @Test
 public void testContainsExtraTypeInformation_DoesNotMatch() {
     Matcher m = new ContainsExtraTypeInformation("java.lang.String", Matchers.is(1));
     Object arg = 1;

     assertFalse(m.matches(arg));
     assertFalse(tool.safelyMatches(m, arg));
     assertFalse(tool.toStringEquals(m, arg.toString()));
     assertFalse(tool.toStringEquals(m.description(), arg.toString()));
     assertFalse(tool.toStringEquals(m.toString(), arg.toString()));
     assertFalse(((ContainsExtraTypeInformation) m).typeMatches(arg.getClass()));
 }
}