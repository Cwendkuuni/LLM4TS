import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_SameSize_DifferentTypes() {
 Matcher m1 = new ContainsString("expected1");
 Matcher m2 = new ContainsString("expected2");
 Object[] args = new Object[]{"arg1", 1};
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher> matchers = new LinkedList<Matcher>(){{
 add(m1);
 add(m2);
 }};
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, args);
 assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MismatchedSize() {
 Matcher m = new ContainsString("expected");
 Object[] args = new Object[]{1, "arg"};
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher> matchers = new LinkedList<Matcher>(){{
 add(m);
 }};
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, args);
 assertArrayEquals(new Integer[0], result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NullArguments() {
 Matcher m = new ContainsString("expected");
 Object[] args = null;
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher> matchers = new LinkedList<Matcher>(){{
 add(m);
 }};
 ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, args);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchedButTypesDifferent() {
 Matcher m = new ContainsString("expected");
 Object arg1 = "arg1";
 Object arg2 = 1;
 Object[] args = new Object[]{arg1, arg2};
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher> matchers = new LinkedList<Matcher>(){{
 add(m);
 }};
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, args);
 assertArrayEquals(new Integer[0], result);

 m = new ContainsExtraTypeInformation(String.class, new ContainsString("expected"));
 Integer[] expectedIndexes = new Integer[]{0};
 result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, args);
 Assert.assertArrayEquals(expectedIndexes, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchedButToStringDifferent() {
 Matcher m = new ContainsString("expected1");
 Object arg1 = "arg1";
 Object arg2 = "arg2";
 Object[] args = new Object[]{arg1, arg2};
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher> matchers = new LinkedList<Matcher>(){{
 add(m);
 }};
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, args);
 assertArrayEquals(new Integer[0], result);

 m = new ContainsString("expected");
 result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, args);
 assertArrayEquals(new Integer[0], result);

 m = new ContainsString("expected2");
 Integer[] expectedIndexes = new Integer[]{1};
 result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, args);
 Assert.assertArrayEquals(expectedIndexes, result);
 }
}