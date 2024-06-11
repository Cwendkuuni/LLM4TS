package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.matchers.ContainsExtraTypeInformation.typeMatches;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

public class ArgumentMatchingToolTest {

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_EmptyListAndArray_ReturnsEmptyArray() {
 List<Matcher> emptyList = Arrays.asList();
 Object[] emptyArray = new Object[0];
 Integer[] result = new ArgumentMatchingTool().getSuspiciouslyNotMatchingArgsIndexes(emptyList, emptyArray);
 assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_DifferentSizeListAndArray_ReturnsEmptyArray() {
 List<Matcher> list = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
 Object[] array = new Object[3];
 Integer[] result = new ArgumentMatchingTool().getSuspiciouslyNotMatchingArgsIndexes(list, array);
 assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_ValidArgs_ReturnsCorrectIndexes() {
 List<Matcher> list = Arrays.asList(
 mock(Matcher.class),
 mock(Matcher.class),
 mock(MatcherWithExtraTypeInformation.class),
 mock(Matcher.class)
 );
 Object[] array = new Object[]{"one", 1, new ObjectWithExtraTypeInformation(), "four"};
 Integer[] result = new ArgumentMatchingTool().getSuspiciouslyNotMatchingArgsIndexes(list, array);
 Integer[] expected = new Integer[]{1, 3};
 assertArrayEquals(expected, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NoSuspiciousMatch_ReturnsEmptyArray() {
 List<Matcher> list = Arrays.asList(
 mock(Matcher.class),
 mock(Matcher.class),
 mock(MatcherWithExtraTypeInformation.class),
 mock(Matcher.class)
 );
 Object[] array = new Object[]{"one", 1, new ObjectWithExtraTypeInformation(), "one"};
 Integer[] result = new ArgumentMatchingTool().getSuspiciouslyNotMatchingArgsIndexes(list, array);
 assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NullArguments_ThrowsNullPointerException() {
 assertNull(new ArgumentMatchingTool().getSuspiciouslyNotMatchingArgsIndexes(null, null));
 }

 @Test(expected = NullPointerException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NullMatcherList_ThrowsNullPointerException() {
 new ArgumentMatchingTool().getSuspiciouslyNotMatchingArgsIndexes(null, new Object[0]);
 }

 @Test
 public void testToStringEquals_Matches_ReturnsTrue() {
 Matcher matcher = mock(Matcher.class);
 Object obj = "test";
 StringDescription stringDescription = new StringDescription();
 String result = new ArgumentMatchingTool().toStringEquals(matcher, obj);
 assertTrue(result);
 }

 @Test
 public void testToStringEquals_NotMatches_ReturnsFalse() {
 Matcher matcher = mock(Matcher.class);
 Object obj = new Object();
 String result = new ArgumentMatchingTool().toStringEquals(matcher, obj);
 assertFalse(result);
 }
}

 class MatcherWithExtraTypeInformation implements Matcher<Object> {
 @Override
 public boolean matches(Object argument) {
 return false;
 }

 @Override
 public String toString() {
 return "MatcherWithExtraTypeInformation";
 }

 @Override
 public void describeTo(StringDescription description) {
 description.appendText("MatcherWithExtraTypeInformation");
 }
 }

 class ObjectWithExtraTypeInformation {
 @Override
 public String toString() {
 return "ObjectWithExtraTypeInformation";
 }
}