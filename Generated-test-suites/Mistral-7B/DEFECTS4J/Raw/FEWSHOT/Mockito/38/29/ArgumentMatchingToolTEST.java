package org.mockito.internal.verification.argumentmatching;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes;
import static org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool.safelyMatches;
import static org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool.toStringEquals;

public class ArgumentMatchingToolTest {

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_empty() {
 List<Matcher> matchers = Arrays.asList();
 Object[] arguments = new Object[0];
 Integer[] expected = new Integer[0];
 Integer[] actual = getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_noSuspiciousArgs() {
 List<Matcher> matchers = Arrays.asList(isA(Integer.class), isA(Integer.class));
 Object[] arguments = new Object[]{1, 2};
 Integer[] expected = new Integer[0];
 Integer[] actual = getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_oneSuspiciousArg() {
 List<Matcher> matchers = Arrays.asList(isA(String.class), isA(Integer.class));
 Object[] arguments = new Object[]{"foo", 1};
 Integer[] expected = new Integer[]{0};
 Integer[] actual = getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_multipleSuspiciousArgs() {
 List<Matcher> matchers = Arrays.asList(isA(String.class), isA(Integer.class), isA(String.class), isA(Integer.class));
 Object[] arguments = new Object[]{"foo", 1, "bar", 2};
 Integer[] expected = new Integer[]{0, 2};
 Integer[] actual = getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testSafelyMatches_Matches() {
 Matcher m = isA(Integer.class);
 Object arg = new Integer(1);
 assertTrue(safelyMatches(m, arg));
 }

 @Test
 public void testSafelyMatches_DoesntMatch_ThrowsException() {
 Matcher m = isA(String.class);
 Object arg = new Integer(1);
 assertFalse(safelyMatches(m, arg));
 }

 @Test
 public void testToStringEquals_Matches() {
 Matcher m = is("foo");
 Object arg = "foo";
 assertTrue(toStringEquals(m, arg));
 }

 @Test
 public void testToStringEquals_DoesntMatch() {
 Matcher m = is("foo");
 Object arg = "bar";
 assertFalse(toStringEquals(m, arg));
 }

 @Test
 public void testToStringEquals_Null() {
 Matcher m = is("foo");
 Object arg = null;
 assertFalse(toStringEquals(m, arg));
 }

 @Test
 public void testContainsExtraTypeInformation_Matches() {
 Matcher m = isA(String.class);
 Object arg = "foo";
 assertTrue(((ContainsExtraTypeInformation) m).typeMatches(arg));
 }

 @Test
 public void testContainsExtraTypeInformation_DoesntMatch() {
 Matcher m = isA(String.class);
 Object arg = 1;
 assertFalse(((ContainsExtraTypeInformation) m).typeMatches(arg));
 }
}