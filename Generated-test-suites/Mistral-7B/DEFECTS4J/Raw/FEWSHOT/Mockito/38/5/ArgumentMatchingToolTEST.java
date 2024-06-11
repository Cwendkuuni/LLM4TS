package org.mockito.internal.verification.argumentmatching;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("unchecked")
public class ArgumentMatchingToolTest {

 private List<Matcher<Object>> getMockMatchers() {
 return Arrays.asList(
 new ContainsString("foo"),
 new ContainsString("bar"),
 new ContainsString("baz"),
 new ContainsString("qux"),
 new ContainsExtraTypeInformation(new StringMatcher("foo"))
 );
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_emptyList_returnsEmptyArray() {
 List<Matcher<Object>> matchers = new LinkedList<>();
 Object[] arguments = new Object[0];
 Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_mismatchedSize_returnsEmptyArray() {
 List<Matcher<Object>> matchers = getMockMatchers();
 Object[] arguments = new Object[matchers.size() + 1];
 Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_noSuspicious_returnsEmptyArray() {
 List<Matcher<Object>> matchers = getMockMatchers();
 Object[] arguments = new Object[]{ 1, "foo", 3.0, "bar" };
 Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_oneSuspicious_returnsCorrectIndex() {
 List<Matcher<Object>> matchers = getMockMatchers();
 Object[] arguments = new Object[]{ 1, new String("foo"), 3.0, new String("bar") };
 Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertTrue(result.length > 0);
 assertTrue(Arrays.binarySearch(result, 2) >= 0);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_multipleSuspicious_returnsAllIndices() {
 List<Matcher<Object>> matchers = getMockMatchers();
 Object[] arguments = new Object[]{ 1, new String("foo"), new Integer(3), new String("bar"), new Integer(3) };
 Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertTrue(result.length > 0);
 assertTrue(Arrays.binarySearch(result, 2) >= 0);
 assertTrue(Arrays.binarySearch(result, 4) >= 0);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_nullArguments_returnsEmptyArray() {
 List<Matcher<Object>> matchers = getMockMatchers();
 Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, null);
 assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_nullMatcher_throwsNullPointerException() {
 Object[] arguments = new Object[]{ 1, "foo", 3.0, "bar" };
 try {
 ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(null, arguments);
 } catch (NullPointerException e) {
 assertNull("Unexpected exception message", e.getCause().getMessage());
 return;
 }
 assertFalse("Expected NullPointerException", true);
 }

 @Test
 public void testToStringEquals_nullMatcher_throwsNullPointerException() {
 Object arg = new Object();
 try {
 ArgumentMatchingTool.toStringEquals(null, arg);
 } catch (NullPointerException e) {
 assertNull("Unexpected exception message", e.getCause().getMessage());
 return;
 }
 assertFalse("Expected NullPointerException", true);
 }

 @Test
 public void testToStringEquals_nonMatching_returnsFalse() {
 Matcher<Object> matcher = new ContainsString("foo");
 Object arg1 = "bar";
 Object arg2 = new String("foo");
 assertFalse(ArgumentMatchingTool.toStringEquals(matcher, arg1));
 assertFalse(ArgumentMatchingTool.toStringEquals(matcher, arg2));
 }

 @Test
 public void testToStringEquals_matching_returnsTrue() {
 Matcher<Object> matcher = new ContainsString("foo");
 Object arg = "foo";
 assertTrue(ArgumentMatchingTool.toStringEquals(matcher, arg));
 }

 @Test
 public void testSafelyMatches_matching_returnsTrue() {
 Matcher<Object> matcher = new ContainsString("foo");
 Object arg = "foo";
 assertTrue(ArgumentMatchingTool.safelyMatches(matcher, arg));
 }

 @Test
 public void testSafelyMatches_nonMatching_returnsFalse() {
 Matcher<Object> matcher = new ContainsString("foo");
 Object arg = "bar";
 assertFalse(ArgumentMatchingTool.safelyMatches(matcher, arg));
 }

 @Test
 public void testContainsExtraTypeInformation_typeMatches_returnsTrue() {
 Matcher<Object> matcher = new ContainsExtraTypeInformation(new StringMatcher("foo"));
 Object arg = new String("foo");
 assertTrue(((ContainsExtraTypeInformation) matcher).typeMatches(arg));
 }

 @Test
 public void testContainsExtraTypeInformation_typeDoesNotMatch_returnsFalse() {
 Matcher<Object> matcher = new ContainsExtraTypeInformation(new StringMatcher("foo"));
 Object arg = new Integer(3);
 assertFalse(((ContainsExtraTypeInformation) matcher).typeMatches(arg));
 }
}