import org.junit.Test;
import org.junit.Assert.*;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool tool = new ArgumentMatchingTool();

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NullMatcherList() {
     Object[] arguments = new Object[]{1, "two", 3.0};
     List<Matcher> matchers = null;
     Integer[] expected = new Integer[0];
     Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_EmptyMatcherList() {
     Object[] arguments = new Object[]{1, "two", 3.0};
     List<Matcher> matchers = new LinkedList<>();
     Integer[] expected = new Integer[0];
     Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_DifferentSizes() {
     Object[] arguments = new Object[]{1, "two", 3.0};
     List<Matcher> matchers = new LinkedList<>();
     matchers.add(Matchers.is(1));
     matchers.add(Matchers.is("two"));
     Integer[] expected = new Integer[0];
     Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MismatchedSizes() {
     Object[] arguments = new Object[]{1, "two", 3.0};
     List<Matcher> matchers = new LinkedList<>();
     matchers.add(Matchers.is(1));
     matchers.add(Matchers.is("two"));
     matchers.add(Matchers.is(3.0));
     tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NoSuspiciousArgs() {
     Object[] arguments = new Object[]{1, "two", 3.0};
     List<Matcher> matchers = new LinkedList<>();
     matchers.add(Matchers.is(1));
     matchers.add(Matchers.is("two"));
     Integer[] expected = new Integer[0];
     Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchedArgs() {
     Object[] arguments = new Object[]{1, "two", 3.0};
     List<Matcher> matchers = new LinkedList<>();
     matchers.add(Matchers.is(1));
     matchers.add(Matchers.is("two"));
     matchers.add(Matchers.is(3.0));
     Integer[] expected = new Integer[0];
     Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciousArgs() {
     Object[] arguments = new Object[]{1, new Object(), 3.0};
     List<Matcher> matchers = new LinkedList<>();
     matchers.add(Matchers.is(1));
     matchers.add(Matchers.is("two"));
     matchers.add(Matchers.is(3.0));
     Matcher m = new ContainsExtraTypeInformation() {
         @Override
         public boolean matches(Object actual) {
             return actual instanceof Object;
         }
         @Override
         public String description() {
             return "Object type";
         }
         @Override
         public void describeTo(StringDescription description) {
             description.appendText("an Object");
         }
     };
     matchers.add(m);
     Integer[] expected = new Integer[]{1};
     Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NullArgumentArray() {
     List<Matcher> matchers = new LinkedList<>();
     matchers.add(Matchers.is(1));
     matchers.add(Matchers.is("two"));
     matchers.add(Matchers.is(3.0));
     tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, null);
 }

 @Test(expected = NullPointerException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NullMatcher() {
     Object[] arguments = new Object[]{1, "two", 3.0};
     List<Matcher> matchers = new LinkedList<>();
     matchers.add(null);
     matchers.add(Matchers.is(3.0));
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
     Matcher m = Matchers.is("two");
     Object arg = 1;
     assertFalse(tool.safelyMatches(m, arg));
 }

 @Test
 public void testToStringEquals_Matches() {
     Matcher m = Matchers.is("two");
     Object arg = "two";
     assertTrue(tool.toStringEquals(m, arg));
 }

 @Test
 public void testToStringEquals_DoesNotMatch() {
     Matcher m = Matchers.is("two");
     Object arg = 2;
     assertFalse(tool.toStringEquals(m, arg));
 }
}