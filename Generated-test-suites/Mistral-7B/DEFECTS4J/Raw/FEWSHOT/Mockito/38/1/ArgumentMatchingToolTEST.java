package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ContainsExtraTypeInformation.typeMatches;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

public class ArgumentMatchingToolTest {

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_nullMatcher_returnsEmptyArray() {
     Object[] arguments = new Object[] { 1, "two" };
     List<Matcher> matchers = null;
     Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(new Integer[0], indexes);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_differentSizes_returnsEmptyArray() {
     Object[] arguments = new Object[] { 1, "two" };
     List<Matcher> matchers = Arrays.asList(contains("one"));
     Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(new Integer[0], indexes);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_noSuspiciousMatches_returnsEmptyArray() {
     Object[] arguments = new Object[] { 1, "one" };
     List<Matcher> matchers = Arrays.asList(contains("one"), contains("one"));
     Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(new Integer[0], indexes);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_singleSuspiciousMatch_returnsSingleIndex() {
     Object[] arguments = new Object[] { 1L, "one" };
     List<Matcher> matchers = Arrays.asList(contains(is(1)), contains("one"));
     Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(new Integer[] { 1 }, indexes);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_multipleSuspiciousMatches_returnsAllIndices() {
     Object[] arguments = new Object[] { 1L, "one", 2L, "two" };
     List<Matcher> matchers = Arrays.asList(
         contains(is(1)),
         contains("one"),
         contains(is(2)),
         contains("two"),
         contains(is(3))
     );
     Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Integer[] expected = new Integer[] { 1, 3 };
     assertArrayEquals(expected, indexes);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_nullArguments_returnsEmptyArray() {
     List<Matcher> matchers = Arrays.asList(contains("one"), contains("two"));
     Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, null);
     assertArrayEquals(new Integer[0], indexes);
 }

 @Test
 public void testToStringEquals_matcherWithSameStringRepresentationButDifferentTypes_returnsFalse() {
     Matcher<String> matcher = contains("test");
     Object arg1 = "test";
     Object arg2 = 123;
     assertFalse(ArgumentMatchingTool.toStringEquals(matcher, arg1));
     assertFalse(ArgumentMatchingTool.toStringEquals(matcher, arg2));
 }

 @Test
 public void testToStringEquals_matcherAndArgumentWithSameToStringRepresentationAndSameType_returnsTrue() {
     Matcher<String> matcher = contains("test");
     Object arg = "test";
     assertTrue(ArgumentMatchingTool.toStringEquals(matcher, arg));
 }

 @Test
 public void testToStringEquals_nullMatcher_throwsNullPointerException() {
     Object arg = "test";
     try {
         ArgumentMatchingTool.toStringEquals(null, arg);
         fail("Expected NullPointerException");
     } catch (NullPointerException e) {
         // Expected
     }
 }

 @Test
 public void testToStringEquals_nullArgument_throwsNullPointerException() {
     Matcher<String> matcher = contains("test");
     try {
         ArgumentMatchingTool.toStringEquals(matcher, null);
         fail("Expected NullPointerException");
     } catch (NullPointerException e) {
         // Expected
     }
 }

 @Test
 public void testTypeMatches_matcherWithSameType_returnsTrue() {
     Matcher<Integer> matcher = contains(is(1));
     Object arg = 1;
     assertTrue(typeMatches(matcher, arg));
 }

 @Test
 public void testTypeMatches_matcherWithDifferentType_returnsFalse() {
     Matcher<Integer> matcher = contains(is(1));
     Object arg = "one";
     assertFalse(typeMatches(matcher, arg));
 }

 @Test
 public void testTypeMatches_matcherWithWildcardType_returnsTrueForAnyType() {
     Matcher<Object> matcher = contains(isA(Object.class));
     Object arg = new Object();
     assertTrue(typeMatches(matcher, arg));
 }

 @Test
 public void testSafelyMatches_matcherThatMatchesArgument_returnsTrue() {
     Matcher<Integer> matcher = contains(is(1));
     Object arg = 1;
     assertTrue(ArgumentMatchingTool.safelyMatches(matcher, arg));
 }

 @Test
 public void testSafelyMatches_matcherThatDoesNotMatchArgument_returnsFalse() {
     Matcher<Integer> matcher = contains(is(2));
     Object arg = 1;
     assertFalse(ArgumentMatchingTool.safelyMatches(matcher, arg));
 }

 @Test
 public void testSafelyMatches_matcherWithThrowingMatcher_throwsException() {
     Matcher<Object> matcher = new ThrowsExceptionMatcher("testException");
     Object arg = new Object();
     try {
         ArgumentMatchingTool.safelyMatches(matcher, arg);
         fail("Expected exception: testException");
     } catch (RuntimeException e) {
         // Expected
     }
 }

 @Test
 public void testSafelyMatches_matcherThatDoesNotThrowException_returnsTrue() {
     Matcher<String> matcher = contains("test");
     Object arg = "test";
     assertTrue(ArgumentMatchingTool.safelyMatches(matcher, arg));
 }
}