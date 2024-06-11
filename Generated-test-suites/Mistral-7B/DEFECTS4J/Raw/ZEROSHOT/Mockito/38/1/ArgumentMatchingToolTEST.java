import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.junit.MockitoJUnitRunner;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.TypeSafeMatcher.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_emptyList_returnEmptyArray() {
     List<Matcher> matchers = new LinkedList<>();
     Object[] arguments = new Object[0];
     Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_differentSizes_returnEmptyArray() {
     List<Matcher> matchers = new LinkedList<>();
     matchers.add(isA(Integer.class));
     Object[] arguments = new Object[0];
     Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_noSuspiciousMatches_returnEmptyArray() {
     List<Matcher> matchers = new LinkedList<>();
     matchers.add(isA(Integer.class));
     matchers.add(isA(String.class));
     Object[] arguments = new Object[]{1, "test"};
     Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_suspiciousMatch_returnIndex() {
     List<Matcher> matchers = new LinkedList<>();
     matchers.add(contains(isA(String.class)).not(contains("test")));
     Object[] arguments = new Object[]{1, "test"};
     Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(new Integer[]{1}, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_multipleSuspiciousMatches_returnArray() {
     List<Matcher> matchers = new LinkedList<>();
     matchers.add(contains(isA(Integer.class)).not(is(1)));
     matchers.add(contains(isA(Integer.class)).not(is(2)));
     Object[] arguments = new Object[]{1, 1};
     Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(new Integer[]{0, 1}, result);
 }

 @Test
 public void testSafelyMatches_match_returnTrue() {
     Matcher<Integer> matcher = isA(Integer.class);
     assertTrue(argumentMatchingTool.safelyMatches(matcher, 1));
 }

 @Test
 public void testSafelyMatches_mismatch_returnFalse() {
     Matcher<Integer> matcher = isA(Integer.class);
     assertFalse(argumentMatchingTool.safelyMatches(matcher, "test"));
 }

 @Test
 public void testToStringEquals_match_returnTrue() {
     Matcher<String> matcher = contains("test");
     String arg = "test";
     assertTrue(argumentMatchingTool.toStringEquals(matcher, arg));
 }

 @Test
 public void testToStringEquals_mismatch_returnFalse() {
     Matcher<String> matcher = contains("test");
     String arg = "otherTest";
     assertFalse(argumentMatchingTool.toStringEquals(matcher, arg));
 }

 @Test
 public void testToStringEquals_sameObject_returnTrue() {
     Matcher<String> matcher = contains("test");
     String arg = "test";
     assertTrue(argumentMatchingTool.toStringEquals(matcher, arg));
 }
}