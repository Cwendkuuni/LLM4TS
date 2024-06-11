package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ContainsExtraTypeInformation.typeMatches;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

public class ArgumentMatchingToolTest {

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersAndArgumentsDontMatch_thenReturnsEmptyArray() {
 List<Matcher<Integer>> matchers = new LinkedList<>();
 matchers.add(org.hamcrest.Matcher.is(1));
 Object[] arguments = new Object[]{2};
 Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[0], indexes);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersAndArgumentsMatch_thenReturnsEmptyArray() {
 List<Matcher<String>> matchers = new LinkedList<>();
 matchers.add(org.hamcrest.Matcher.is("foo"));
 Object[] arguments = new Object[]{"foo"};
 Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[0], indexes);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersAndArgumentsHaveSameToStringRepresentationButDifferentTypes_thenReturnsIndexes() {
 List<Matcher<String>> matchers = new LinkedList<>();
 matchers.add(ContainsExtraTypeInformation.valueOf("java.lang.String").typeMatches(String.class)
 .describeTo(new StringDescription())
 .is("foo"));
 Object[] arguments = new Object[]{1};
 Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[0], indexes);

 matchers = new LinkedList<>();
 matchers.add(ContainsExtraTypeInformation.valueOf("java.lang.Integer").typeMatches(Integer.class)
 .describeTo(new StringDescription())
 .is("foo"));
 arguments = new Object[]{"foo"};
 indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[]{0}, indexes);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersAndArgumentsHaveSameToStringRepresentationButDifferentValues_thenReturnsEmptyArray() {
 List<Matcher<String>> matchers = new LinkedList<>();
 matchers.add(ContainsExtraTypeInformation.valueOf("java.lang.String").typeMatches(String.class)
 .describeTo(new StringDescription())
 .is("foo"));
 Object[] arguments = new Object[]{"bar"};
 Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[0], indexes);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersIsEmpty_thenReturnsEmptyArray() {
 List<Matcher<String>> matchers = new LinkedList<>();
 Object[] arguments = new Object[]{"foo"};
 Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[0], indexes);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenArgumentsIsEmpty_thenReturnsEmptyArray() {
 List<Matcher<String>> matchers = new LinkedList<>();
 Object[] arguments = new Object[]{};
 Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[0], indexes);
 }

 @Test
 public void testSafelyMatches_whenMatcherMatchesArgument_thenReturnsTrue() {
 Matcher<Integer> matcher = org.hamcrest.Matcher.is(1);
 Integer argument = 1;
 assertTrue(ArgumentMatchingTool.safelyMatches(matcher, argument));
 }

 @Test
 public void testSafelyMatches_whenMatcherDoesNotMatchArgument_thenReturnsFalse() {
 Matcher<Integer> matcher = org.hamcrest.Matcher.is(1);
 Integer argument = 2;
 assertFalse(ArgumentMatchingTool.safelyMatches(matcher, argument));
 }

 @Test
 public void testToStringEquals_whenMatcherAndArgumentHaveSameToStringRepresentation_thenReturnsTrue() {
 Matcher<String> matcher = org.hamcrest.Matcher.is("foo");
 String argument = "foo";
 assertTrue(ArgumentMatchingTool.toStringEquals(matcher, argument));
 }

 @Test
 public void testToStringEquals_whenMatcherAndArgumentHaveDifferentToStringRepresentation_thenReturnsFalse() {
 Matcher<String> matcher = org.hamcrest.Matcher.is("foo");
 Object argument = 1;
 assertFalse(ArgumentMatchingTool.toStringEquals(matcher, argument));
 }

 @Test
 public void testTypeMatches_whenTypesMatch_thenReturnsTrue() {
 assertTrue(typeMatches(String.class, "foo"));
 assertTrue(typeMatches(Integer.class, 1));
 }

 @Test
 public void testTypeMatches_whenTypesDoNotMatch_thenReturnsFalse() {
 assertFalse(typeMatches(String.class, 1));
 assertFalse(typeMatches(Integer.class, "foo"));
 }

 @Test(expected = NullPointerException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersIsNull_thenThrowsException() {
 Object[] arguments = new Object[]{"foo"};
 assertNull(ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(null, arguments));
 }

 @Test(expected = NullPointerException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenArgumentsIsNull_thenThrowsException() {
 List<Matcher<String>> matchers = new LinkedList<>();
 assertNull(ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, null));
 }
}