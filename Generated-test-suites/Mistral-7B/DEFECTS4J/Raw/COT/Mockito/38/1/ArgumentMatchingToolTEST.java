package org.mockito.internal.verification.argumentmatching.test;

import org.junit.Test;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.internal.verification.argumentmatching.test.TestUtils.createMatcher;
import static org.mockito.internal.verification.argumentmatching.test.TestUtils.createObjectArray;

public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_EmptyListAndArray_ReturnsEmptyArray() {
     List<Matcher> matchers = new java.util.ArrayList<>();
     Object[] arguments = new Object[0];
     Integer[] expected = new Integer[0];
     assertArrayEquals(expected, argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MismatchedListAndArrayLengths_ReturnsEmptyArray() {
     List<Matcher> matchers = new java.util.ArrayList<>();
     matchers.add(createMatcher(Integer.class));
     Object[] arguments = new Object[0];
     Integer[] expected = new Integer[0];
     assertArrayEquals(expected, argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NoSuspiciousArgs_ReturnsEmptyArray() {
     List<Matcher> matchers = new java.util.ArrayList<>();
     matchers.add(createMatcher(Integer.class));
     matchers.add(createMatcher(Integer.class));
     Object[] arguments = new Object[]{1, 1};
     Integer[] expected = new Integer[0];
     assertArrayEquals(expected, argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NullMatcherList_ThrowsIllegalArgumentException() {
     Object[] arguments = new Object[]{1, "two"};
     argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(null, arguments);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NullArgumentsArray_ThrowsIllegalArgumentException() {
     List<Matcher> matchers = new java.util.ArrayList<>();
     matchers.add(createMatcher(Integer.class));
     argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, null);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciousArg_ReturnsIndex() {
     List<Matcher> matchers = new java.util.ArrayList<>();
     matchers.add(createMatcher(Integer.class));
     matchers.add(createMatcher(String.class));
     Object[] arguments = new Object[]{1, "one"};
     Integer[] expected = {1};
     assertArrayEquals(expected, argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MultipleSuspiciousArgs_ReturnsAllIndices() {
     List<Matcher> matchers = new java.util.ArrayList<>();
     matchers.add(createMatcher(Integer.class));
     matchers.add(createMatcher(String.class));
     Object[] arguments = new Object[]{1, "one", 1, "two"};
     Integer[] expected = {0, 3};
     assertArrayEquals(expected, argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test
 public void testToStringEquals_NullMatcher_ReturnsFalse() {
     Matcher m = null;
     Object arg = "string";
     boolean expected = false;
     boolean actual = argumentMatchingTool.toStringEquals(m, arg);
     assertFalse(actual);
 }

 @Test
 public void testToStringEquals_NullArgument_ReturnsFalse() {
     Matcher m = createMatcher(Integer.class);
     Object arg = null;
     boolean expected = false;
     boolean actual = argumentMatchingTool.toStringEquals(m, arg);
     assertFalse(actual);
 }

 @Test
 public void testToStringEquals_SameToStringRepresentation_ReturnsTrue() {
     Matcher m = createMatcher(Integer.class);
     Object arg = 1;
     boolean expected = true;
     boolean actual = argumentMatchingTool.toStringEquals(m, arg);
     assertTrue(actual);
 }

 @Test
 public void testToStringEquals_DifferentToStringRepresentation_ReturnsFalse() {
     Matcher m = createMatcher(Integer.class);
     Object arg = new Object(){};
     boolean expected = false;
     boolean actual = argumentMatchingTool.toStringEquals(m, arg);
     assertFalse(actual);
 }

 @Test
 public void testSafelyMatches_NullMatcher_ReturnsFalse() {
     Matcher m = null;
     Object arg = 1;
     boolean expected = false;
     boolean actual = argumentMatchingTool.safelyMatches(m, arg);
     assertFalse(actual);
 }

 @Test(expected = RuntimeException.class)
 public void testSafelyMatches_NullArgument_ThrowsRuntimeException() {
     Matcher m = createMatcher(Integer.class);
     Object arg = null;
     argumentMatchingTool.safelyMatches(m, arg);
 }

 @Test
 public void testSafelyMatches_MatcherMatchesArgument_ReturnsTrue() {
     Matcher m = createMatcher(Integer.class);
     Object arg = 1;
     boolean expected = true;
     boolean actual = argumentMatchingTool.safelyMatches(m, arg);
     assertTrue(actual);
 }

 @Test
 public void testSafelyMatches_MatcherDoesNotMatchArgument_ReturnsFalse() {
     Matcher m = createMatcher(Integer.class);
     Object arg = "one";
     boolean expected = false;
     boolean actual = argumentMatchingTool.safelyMatches(m, arg);
     assertFalse(actual);
 }

}