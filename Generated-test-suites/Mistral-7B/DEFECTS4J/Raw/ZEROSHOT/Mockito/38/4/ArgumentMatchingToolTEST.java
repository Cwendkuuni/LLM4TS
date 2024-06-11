import org.junit.Test;
import org.junit.Assert.*;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;

public class ArgumentMatchingToolTest {

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_emptyList() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 Object[] arguments = new Object[0];
 List<Matcher> matchers = new java.util.ArrayList<>();
 Integer[] expected = new Integer[0];
 Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_wrongSize() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 Object[] arguments = new Object[]{"arg1"};
 List<Matcher> matchers = new java.util.ArrayList<>();
 matchers.add(Matchers.is(1));
 Integer[] expected = new Integer[0];
 Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_noSuspiciousArgs() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 Object[] arguments = new Object[]{1, "arg2"};
 List<Matcher> matchers = new java.util.ArrayList<>();
 matchers.add(Matchers.is(1));
 matchers.add(Matchers.is("arg2"));
 Integer[] expected = new Integer[0];
 Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_suspiciousArg() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 Object[] arguments = new Object[]{1, new Double(1.0), "arg2"};
 List<Matcher> matchers = new java.util.ArrayList<>();
 matchers.add(Matchers.is(1));
 matchers.add(Matchers.is("arg2"));
 matchers.add(Matchers.instanceOf(Double.class));
 Integer[] expected = new Integer[]{1};
 Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_multipleSuspiciousArgs() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 Object[] arguments = new Object[]{1, new Double(1.0), 1.1f, "arg2"};
 List<Matcher> matchers = new java.util.ArrayList<>();
 matchers.add(Matchers.is(1));
 matchers.add(Matchers.is("arg2"));
 matchers.add(Matchers.instanceOf(Double.class));
 matchers.add(Matchers.instanceOf(Float.class));
 Integer[] expected = new Integer[]{1, 2};
 Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(expected, actual);
 }
}