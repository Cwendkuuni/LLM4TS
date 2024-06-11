import org.junit.Test;
import org.junit.Assert.*;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;

public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool tool = new ArgumentMatchingTool();

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_validArguments() {
     List<Matcher> matchers = new java.util.ArrayList<>();
     matchers.add(Matchers.equalTo(1));
     matchers.add(Matchers.equalTo("one"));
     Object[] arguments = new Object[]{1, "one"};
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Integer[] expected = new Integer[0];
     assertArrayEquals(expected, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_mismatchedSize() {
     List<Matcher> matchers = new java.util.ArrayList<>();
     Object[] arguments = new Object[]{1};
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Integer[] expected = new Integer[0];
     assertArrayEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_invalidMatcherSize() {
     List<Matcher> matchers = new java.util.ArrayList<>();
     Object[] arguments = new Object[]{1, "one"};
     tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_suspiciousArgs() {
     List<Matcher> matchers = new java.util.ArrayList<>();
     matchers.add(Matchers.instanceOf(Integer.class));
     matchers.add(Matchers.equalTo(1));
     Object[] arguments = new Object[]{1, new Integer(1)};
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Integer[] expected = {0};
     assertArrayEquals(expected, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_toStringEquals() {
     List<Matcher> matchers = new java.util.ArrayList<>();
     Matcher toStringMatcher = new Matcher() {
         @Override
         public boolean matches(Object arg) {
             return arg.toString().equals("one");
         }
         @Override
         public String toString() {
             return "toStringMatcher";
         }
     };
     matchers.add(toStringMatcher);
     Object[] arguments = new Object[]{1, "one"};
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Integer[] expected = new Integer[0];
     assertArrayEquals(expected, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_typeMismatch() {
     List<Matcher> matchers = new java.util.ArrayList<>();
     Matcher integerMatcher = Matchers.instanceOf(Integer.class);
     matchers.add(integerMatcher);
     Object[] arguments = new Object[]{1, new Double(1)};
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Integer[] expected = {0};
     assertArrayEquals(expected, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_safelyMatchesException() {
     List<Matcher> matchers = new java.util.ArrayList<>();
     Matcher exceptionMatcher = new Matcher() {
         @Override
         public boolean matches(Object arg) {
             throw new RuntimeException("TestException");
         }
         @Override
         public String toString() {
             return "exceptionMatcher";
         }
     };
     matchers.add(exceptionMatcher);
     Object[] arguments = new Object[]{1};
     try {
         Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
         fail("Expected IllegalArgumentException");
     } catch (IllegalArgumentException e) {
         // expected
     }
 }

}