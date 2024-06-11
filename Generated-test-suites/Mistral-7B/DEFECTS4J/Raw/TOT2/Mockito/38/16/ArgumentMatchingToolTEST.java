import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import java.util.Arrays;
import java.util.List;

public class ArgumentMatchingToolTest {

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_EmptyList() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher> matchers = new ArrayList<>();
 Object[] arguments = new Object[0];
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NoSuspiciousArgs() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher> matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
 Object[] arguments = new Object[]{"arg1", "arg2"};
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_OneSuspiciousArg() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher> matchers = Arrays.asList(mock(Matcher.class), new ContainsExtraTypeInformation("expectedType", anyObject()), mock(Matcher.class));
 Object[] arguments = new Object[]{"arg1", new Object(){}, "arg3"};
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(new Integer[]{1}, result);
 }

}