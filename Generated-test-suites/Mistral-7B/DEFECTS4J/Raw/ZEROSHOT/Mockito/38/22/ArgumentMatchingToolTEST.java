import org.junit.Test;
import org.junit.Assert;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

import java.util.Arrays;
import java.util.List;

public class ArgumentMatchingToolTest {

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_emptyList_returnsEmptyArray() {
     ArgumentMatchingTool tool = new ArgumentMatchingTool();
     Object[] arguments = new Object[0];
     List<Matcher> matchers = new java.util.ArrayList<>();

     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_differentSizes_returnsEmptyArray() {
     ArgumentMatchingTool tool = new ArgumentMatchingTool();
     Object[] arguments = new Object[]{1};
     List<Matcher> matchers = new java.util.ArrayList<>();
     matchers.add(createMatcher("expectedType1"));

     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_noSuspiciousMatches_returnsEmptyArray() {
     ArgumentMatchingTool tool = new ArgumentMatchingTool();
     Object[] arguments = new Object[]{1, "one"};
     List<Matcher> matchers = Arrays.asList(createMatcher("Integer"), createMatcher("String"), createMatcher("Integer"));

     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_oneSuspiciousMatch_returnsIndex() {
     ArgumentMatchingTool tool = new ArgumentMatchingTool();
     Object[] arguments = new Object[]{1, new Double(1.0), "one"};
     List<Matcher> matchers = Arrays.asList(createMatcher("Integer"), createMatcher("Double"), createMatcher("String"));

     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[]{1}, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_multipleSuspiciousMatches_returnsAllIndices() {
     ArgumentMatchingTool tool = new ArgumentMatchingTool();
     Object[] arguments = new Object[]{1, new Double(1.0), new Double(2.0), "one"};
     List<Matcher> matchers = Arrays.asList(createMatcher("Integer"), createMatcher("Double"), createMatcher("Double"), createMatcher("String"));

     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[]{0, 1, 2}, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersIsNull_throwsIllegalArgumentException() {
     ArgumentMatchingTool tool = new ArgumentMatchingTool();
     Object[] arguments = new Object[]{1, "one"};
     List<Matcher> matchers = null;

     tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_argumentsIsNull_throwsIllegalArgumentException() {
     ArgumentMatchingTool tool = new ArgumentMatchingTool();
     List<Matcher> matchers = Arrays.asList(createMatcher("Integer"));

     tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, null);
 }

 private Matcher createMatcher(String expectedType) {
     return new ContainsExtraTypeInformation(expectedType, Object.class);
 }
}