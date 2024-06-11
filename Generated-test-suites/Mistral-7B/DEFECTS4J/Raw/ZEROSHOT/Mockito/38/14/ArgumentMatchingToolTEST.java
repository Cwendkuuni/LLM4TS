import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.Mockito;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ArgumentMatchingToolTest {

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_EmptyListAndEmptyArray_ReturnsEmptyArray() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher> matchers = new java.util.ArrayList<>();
 Object[] arguments = new Object[0];
 Integer[] expected = new Integer[0];
 Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MismatchedSize_ReturnsEmptyArray() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher> matchers = Arrays.asList(Mockito.mock(Matcher.class));
 Object[] arguments = new Object[0];
 Integer[] expected = new Integer[0];
 Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NoSuspiciousMatches_ReturnsEmptyArray() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher> matchers = Arrays.asList(Mockito.mock(ContainsExtraTypeInformation.class), Mockito.mock(Matcher.class));
 Object[] arguments = new Object[]{"testString", 1, "testString"};
 Integer[] expected = new Integer[0];
 Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_SingleSuspiciousMatch_ReturnsSingleIndexArray() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher> matchers = Arrays.asList(new TypeSafeMatcher<Object>() {
 @Override
 protected boolean matchesSafely(Object item) {
 return "testString".equals(item.toString()) && !(item instanceof Number);
 }

 @Override
 public void describeTo(StringDescription description) {
 description.appendText("a string that is not a number");
 }
 }, Mockito.mock(Matcher.class));
 Object[] arguments = new Object[]{"testString", 1, "testString"};
 Integer[] expected = new Integer[]{1};
 Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MultipleSuspiciousMatches_ReturnsCorrectIndexes() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher> matchers = Arrays.asList(new TypeSafeMatcher<Object>() {
 @Override
 protected boolean matchesSafely(Object item) {
 return "testString".equals(item.toString()) && !(item instanceof Number);
 }

 @Override
 public void describeTo(StringDescription description) {
 description.appendText("a string that is not a number");
 }
 }, new TypeSafeMatcher<Object>() {
 @Override
 protected boolean matchesSafely(Object item) {
 return "testString2".equals(item.toString()) && !(item instanceof Number);
 }

 @Override
 public void describeTo(StringDescription description) {
 description.appendText("a string that is not a number and equals testString2");
 }
 }, Mockito.mock(Matcher.class));
 Object[] arguments = new Object[]{"testString", 1, "testString", "testString2"};
 Integer[] expected = new Integer[]{0, 3};
 Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testSafelyMatches_Matches_ReturnsTrue() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 Matcher matcher = Mockito.mock(Matcher.class);
 Object arg = "testString";
 assertTrue(tool.safelyMatches(matcher, arg));
 }

 @Test
 public void testSafelyMatches_DoesNotMatch_ReturnsFalse() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 Matcher matcher = Mockito.mock(Matcher.class);
 Object arg = new Integer(1);
 assertFalse(tool.safelyMatches(matcher, arg));
 }

 @Test
 public void testToStringEquals_Matches_ReturnsTrue() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 Matcher matcher = new TypeSafeMatcher<Object>() {
 @Override
 protected boolean matchesSafely(Object item) {
 return item.toString().equals("testString");
 }

 @Override
 public void describeTo(StringDescription description) {
 description.appendText("a string equals testString");
 }
 };
 Object arg = "testString";
 assertTrue(tool.toStringEquals(matcher, arg));
 }

 @Test
 public void testToStringEquals_DoesNotMatch_ReturnsFalse() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 Matcher matcher = new TypeSafeMatcher<Object>() {
 @Override
 protected boolean matchesSafely(Object item) {
 return item.toString().equals("testString");
 }

 @Override
 public void describeTo(StringDescription description) {
 description.appendText("a string equals testString");
 }
 };
 Object arg = new Integer(1);
 assertFalse(tool.toStringEquals(matcher, arg));
 }
}