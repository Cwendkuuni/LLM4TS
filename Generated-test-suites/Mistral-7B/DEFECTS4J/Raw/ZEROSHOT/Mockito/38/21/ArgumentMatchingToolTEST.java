import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Stubber;
import org.mockito.utils.MockUtil;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.runner.JUnitCore;

import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;

@RunWith(JUnitCore.class)
public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool argumentMatchingTool;

 @Before
 public void setUp() {
 argumentMatchingTool = new ArgumentMatchingTool();
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_emptyList() {
 List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation(IsInstanceOf.class, String.class));
 Object[] arguments = new Object[0];
 Integer[] indexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(new Integer[0], indexes);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_mismatchedSize() {
 List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation(IsInstanceOf.class, String.class));
 Object[] arguments = new Object[2];
 Integer[] indexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(new Integer[0], indexes);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_noSuspiciousMatches() {
 List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation(IsInstanceOf.class, String.class), new ContainsExtraTypeInformation(IsInstanceOf.class, Integer.class));
 Object[] arguments = new Object[]{new String("test"), new Integer(1)};
 Integer[] indexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(new Integer[0], indexes);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_singleSuspiciousMatch() {
 List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation(IsInstanceOf.class, String.class), new ContainsExtraTypeInformation(IsInstanceOf.class, Integer.class), new ContainsExtraTypeInformation(IsNot.class, Integer.class));
 Object[] arguments = new Object[]{new String("test"), new Integer(1), new Integer(2)};
 Integer[] indexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(new Integer[]{2}, indexes);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_multipleSuspiciousMatches() {
 List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation(IsInstanceOf.class, String.class), new ContainsExtraTypeInformation(IsInstanceOf.class, Integer.class), new ContainsExtraTypeInformation(IsInstanceOf.class, Number.class), new ContainsExtraTypeInformation(IsNot.class, Integer.class));
 Object[] arguments = new Object[]{new String("test"), new Integer(1), new Integer(1), new Number(2.0)};
 Integer[] indexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(new Integer[]{1, 3}, indexes);
 }

 @Test
 public void testToStringEquals_sameToString() {
 Matcher m = new TypeSafeMatcher<String>() {
 @Override
 public void describeTo(Description description) {
 description.description = "expected string";
 }

 @Override
 protected boolean matchesSafely(String item) {
 return true;
 }
 };
 Object arg = "actual string";
 boolean result = argumentMatchingTool.toStringEquals(m, arg);
 Assert.assertTrue(result);
 }

 @Test
 public void testToStringEquals_differentToString() {
 Matcher m = new TypeSafeMatcher<String>() {
 @Override
 public void describeTo(Description description) {
 description.description = "expected string";
 }

 @Override
 protected boolean matchesSafely(String item) {
 return true;
 }
 };
 Object arg = new Object() {};
 boolean result = argumentMatchingTool.toStringEquals(m, arg);
 Assert.assertFalse(result);
 }

 @Test
 public void testSafelyMatches_matches() {
 Matcher m = new IsInstanceOf<String>(String.class);
 Object arg = "test";
 boolean result = argumentMatchingTool.safelyMatches(m, arg);
 Assert.assertTrue(result);
 }

 @Test
 public void testSafelyMatches_mismatches() {
 Matcher m = new IsInstanceOf<Number>(Number.class);
 Object arg = "test";
 boolean result = argumentMatchingTool.safelyMatches(m, arg);
 Assert.assertFalse(result);
 }

 @Test
 public void testContainsExtraTypeInformation_match() {
 Matcher m = new ContainsExtraTypeInformation(IsInstanceOf.class, String.class);
 Object arg = "test";
 boolean result = m.matches(arg);
 assertTrue(result);
 }

 @Test
 public void testContainsExtraTypeInformation_noMatch() {
 Matcher m = new ContainsExtraTypeInformation(IsInstanceOf.class, String.class);
 Object arg = new Object() {};
 boolean result = m.matches(arg);
 assertFalse(result);
 }

 @Test
 public void testContainsExtraTypeInformation_matchWithType() {
 Matcher m = new ContainsExtraTypeInformation(IsInstanceOf.class, String.class);
 Object arg = "test";
 boolean result = ((ContainsExtraTypeInformation) m).typeMatches(arg);
 assertTrue(result);
 }

 @Test
 public void testContainsExtraTypeInformation_noMatchWithType() {
 Matcher m = new ContainsExtraTypeInformation(IsInstanceOf.class, String.class);
 Object arg = new Object() {};
 boolean result = ((ContainsExtraTypeInformation) m).typeMatches(arg);
 assertFalse(result);
 }

 @Test
 public void testContainsExtraTypeInformation_matchWithWrongType() {
 Matcher m = new ContainsExtraTypeInformation(IsInstanceOf.class, String.class);
 Object arg = new Integer(1);
 boolean result = ((ContainsExtraTypeInformation) m).typeMatches(arg);
 assertFalse(result);
 }
}