import org.junit.Test;
import org.junit.Assert.*;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool tool = new ArgumentMatchingTool();

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_EmptyList() {
     List<Matcher> matchers = new LinkedList<>();
     Object[] arguments = new Object[0];
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_ArgumentsLengthMismatch() {
     List<Matcher> matchers = new LinkedList<>();
     Object[] arguments = new Object[2];
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NoSuspiciousMatch() {
     List<Matcher> matchers = new LinkedList<>();
     matchers.add(Matchers.anyInt());
     Object[] arguments = new Object[]{1, "one"};
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciousMatch() {
     List<Matcher> matchers = new LinkedList<>();
     matchers.add(Matchers.anyInt());
     matchers.add(Matchers.instanceOf(String.class));
     Object[] arguments = new Object[]{1, "one"};
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[0], result);
     // String and Integer are not the same type, but their toString() representation is the same
     matchers.set(1, Matchers.stringContaining("one"));
     result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertEquals(1, result.length);
     Assert.assertEquals(1, result[0].intValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NullMatcherList() {
     Object[] arguments = new Object[0];
     tool.getSuspiciouslyNotMatchingArgsIndexes(null, arguments);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NullArgumentsArray() {
     List<Matcher> matchers = new LinkedList<>();
     tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, null);
 }

}