import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ArgumentMatchingToolTest {

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_SameSize_NoSuspiciousArgs() {
 List<Matcher<Object>> matchers = new LinkedList<>();
 Object[] arguments = new Object[]{"arg1", 1, 3.14};
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEqual(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_DifferentSizes() {
 List<Matcher<Object>> matchers = new LinkedList<>();
 matchers.add(new TypeSafeMatcher<Object>() {
 @Override
 public boolean matchesSafely(Object arg) {
 return arg.equals("arg1");
 }

 @Override
 public void describeTo(StringDescription description) {
 description.appendText("expected arg1");
 }

 @Override
 public void describeMismatch(Object item, StringDescription mismatchDescription) {
 mismatchDescription.appendText("actual: ").appendValue(item);
 }
 });
 Object[] arguments = new Object[]{1, "arg1", new Object()};
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEqual(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NoSuspiciousMatches() {
 List<Matcher<Object>> matchers = new LinkedList<>();
 matchers.add(new TypeSafeMatcher<Object>() {
 @Override
 public boolean matchesSafely(Object arg) {
 return arg.equals("arg1");
 }

 @Override
 public void describeTo(StringDescription description) {
 description.appendText("expected arg1");
 }

 @Override
 public void describeMismatch(Object item, StringDescription mismatchDescription) {
 mismatchDescription.appendText("actual: ").appendValue(item);
 }
 });
 Object[] arguments = new Object[]{"arg1", 1, "arg1"};
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEqual(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciousMatch() {
 List<Matcher<Object>> matchers = new LinkedList<>();
 matchers.add(new TypeSafeMatcher<Object>() {
 @Override
 public boolean matchesSafely(Object arg) {
 return arg instanceof String && arg.toString().equals("arg1");
 }

 @Override
 public void describeTo(StringDescription description) {
 description.appendText("expected arg1 of type String");
 }

 @Override
 public void describeMismatch(Object item, StringDescription mismatchDescription) {
 mismatchDescription.appendText("actual: ").appendValue(item);
 }
 });
 Object[] arguments = new Object[]{"arg1", 1, new String("arg1")};
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(new Integer[]{1}, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_InvalidArguments() {
 List<Matcher<Object>> matchers = new LinkedList<>();
 Object[] arguments = new Object[]{1};
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_InvalidMatchers() {
 List<Matcher<Object>> matchers = new LinkedList<>();
 matchers.add(null);
 Object[] arguments = new Object[]{"arg1", 1, "arg1"};
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 }
}