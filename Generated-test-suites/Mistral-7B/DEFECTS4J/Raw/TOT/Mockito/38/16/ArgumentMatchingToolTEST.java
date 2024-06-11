import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import java.util.Arrays;
import java.util.List;

public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_SameNumberOfMatchersAndArguments_NoSuspiciousMatches() {
 List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation("string"), new ContainsExtraTypeInformation(Integer.class));
 Object[] arguments = new Object[]{"string1", 1};
 Integer[] expected = new Integer[0];
 Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_DifferentNumberOfMatchersAndArguments_NoSuspiciousMatches() {
 List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation("string"), new ContainsExtraTypeInformation(Integer.class));
 Object[] arguments = new Object[]{"string1", 1, 2};
 Integer[] expected = new Integer[0];
 Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciousMatch_StringAndInteger() {
 List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation("string"), new ContainsExtraTypeInformation(Integer.class));
 Object[] arguments = new Object[]{"string1", new Integer(1)};
 Integer[] expected = new Integer[]{0};
 Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(expected, actual);
 }

 @Test(expected = AssertionError.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_DifferentNumberOfMatchersAndArguments_Mismatch() {
 List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation("string"));
 Object[] arguments = new Object[]{"string1", 1, 2};
 argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NullMatchers() {
 argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(null, new Object[]{"string1", 1});
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NullArguments() {
 argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(Arrays.asList(new ContainsExtraTypeInformation("string")), null);
 }

}