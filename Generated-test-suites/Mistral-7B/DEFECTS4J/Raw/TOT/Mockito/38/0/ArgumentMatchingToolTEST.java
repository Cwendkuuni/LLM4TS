import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool tool = new ArgumentMatchingTool();

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_sameNumberOfArgumentsAndMatchers_noSuspiciousArgs() {
 List<Matcher> matchers = new ArrayList<>();
 Object[] arguments = new Object[]{"expectedString1", Integer.valueOf(1), Double.valueOf(3.14), "expectedString2"};
 matchers.add(contains("expectedString1"));
 matchers.add(isA(Integer.class));
 matchers.add(isA(Double.class));
 matchers.add(contains("expectedString2"));
 Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertEquals(new Integer[0], indexes);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_differentNumberOfArgumentsAndMatchers_noSuspiciousArgs() {
 List<Matcher> matchers = new ArrayList<>();
 Object[] arguments = new Object[]{"expectedString1", Integer.valueOf(1), Double.valueOf(3.14), "expectedString2"};
 matchers.add(contains("expectedString1"));
 matchers.add(isA(Integer.class));
 Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertEquals(new Integer[0], indexes);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_matchingArguments_noSuspiciousArgs() {
 List<Matcher> matchers = new ArrayList<>();
 Object[] arguments = new Object[]{"expectedString1", Integer.valueOf(1), Double.valueOf(3.14), "expectedString2"};
 matchers.add(contains("expectedString1"));
 matchers.add(isA(Integer.class));
 matchers.add(isA(Double.class));
 matchers.add(contains("expectedString2"));
 Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertEquals(new Integer[0], indexes);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_differentTypesButToStringEquals_suspiciousArgs() {
 List<Matcher> matchers = new ArrayList<>();
 Object[] arguments = new Object[]{"expectedString1", Integer.class, Double.valueOf(3.14), "expectedString2"};
 matchers.add(contains("expectedString1"));
 matchers.add(isA(String.class));
 matchers.add(isA(Double.class));
 matchers.add(contains("expectedString2"));
 Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(new Integer[]{1}, indexes);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_differentNumberOfArgumentsAndMatchers() {
 List<Matcher> matchers = new ArrayList<>();
 Object[] arguments = new Object[]{"expectedString1", Integer.valueOf(1), Double.valueOf(3.14), "expectedString2"};
 matchers.add(contains("expectedString1"));
 matchers.add(isA(Integer.class));
 Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 fail("IllegalArgumentException expected but not thrown.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_nullMatcher() {
 List<Matcher> matchers = Arrays.asList(null, contains("expectedString1"), isA(Integer.class), contains("expectedString2"));
 Object[] arguments = new Object[]{"expectedString1", Integer.valueOf(1), Double.valueOf(3.14), "expectedString2"};
 Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 fail("IllegalArgumentException expected but not thrown.");
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_nullArgument() {
 List<Matcher> matchers = new ArrayList<>();
 Object[] arguments = new Object[]{"expectedString1", Integer.valueOf(1), null, "expectedString2"};
 matchers.add(contains("expectedString1"));
 matchers.add(isA(Integer.class));
 matchers.add(isA(Double.class));
 matchers.add(contains("expectedString2"));
 Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertEquals(new Integer[0], indexes);
 }
}