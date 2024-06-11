import org.junit.Test;
import org.junit.Assert.*;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool tool = new ArgumentMatchingTool();

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_sameSize_shouldReturnEmptyArray() {
     List<Matcher<Object>> matchers = new java.util.ArrayList<>();
     Object[] arguments = new Object[0];

     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_differentSize_shouldReturnEmptyArray() {
     List<Matcher<Object>> matchers = new java.util.ArrayList<>();
     Object[] arguments = new Object[1];

     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_noSuspiciousMatch_shouldReturnEmptyArray() {
     List<Matcher<Object>> matchers = new java.util.ArrayList<>();
     matchers.add(Matchers.anyInt());
     Object[] arguments = new Object[]{1, "one"};

     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[0], result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_differentSizes_shouldThrowIllegalArgumentException() {
     List<Matcher<Object>> matchers = new java.util.ArrayList<>();
     Object[] arguments = new Object[1];
     matchers.add(Matchers.anyInt());

     try {
         tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
         fail("IllegalArgumentException expected but not thrown.");
     } catch (IllegalArgumentException e) {
         //expected exception
     }
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_suspiciousMatch_shouldReturnIndex() {
     List<Matcher<Object>> matchers = new java.util.ArrayList<>();
     matchers.add(contains(new ContainsExtraTypeInformation("String")).typeMatches(String.class));
     Object[] arguments = new Object[]{1, "one"};

     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[]{1}, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_suspiciousMatch_shouldReturnTwoIndices() {
     List<Matcher<Object>> matchers = new java.util.ArrayList<>();
     matchers.add(contains(new ContainsExtraTypeInformation("String")).typeMatches(Integer.class));
     matchers.add(contains(new ContainsExtraTypeInformation("String")).typeMatches(String.class));
     Object[] arguments = new Object[]{1, "one"};

     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[]{0, 1}, result);
 }

 @Test(expected = NullPointerException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_nullMatcher_shouldThrowNullPointerException() {
     Object[] arguments = new Object[]{1, "one"};

     try {
         tool.getSuspiciouslyNotMatchingArgsIndexes(null, arguments);
         fail("NullPointerException expected but not thrown.");
     } catch (NullPointerException e) {
         //expected exception
     }
 }

 @Test(expected = NullPointerException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_nullArguments_shouldThrowNullPointerException() {
     List<Matcher<Object>> matchers = new java.util.ArrayList<>();
     matchers.add(contains(new ContainsExtraTypeInformation("String")).typeMatches(String.class));

     try {
         tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, null);
         fail("NullPointerException expected but not thrown.");
     } catch (NullPointerException e) {
         //expected exception
     }
 }

 private Matcher<Object> contains(Matcher<?> matcher) {
     return new org.hamcrest.Matcher<Object>() {
         @Override
         public boolean matches(Object argument) {
             return matcher.matches(argument);
         }

         @Override
         public void describeTo(StringDescription description) {
             description.appendText("contains ").appendDescriptionOf(matcher);
         }
     };
 }

}