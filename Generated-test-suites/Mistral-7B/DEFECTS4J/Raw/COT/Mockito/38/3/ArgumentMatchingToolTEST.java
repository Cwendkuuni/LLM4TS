package org.mockito.internal.verification.argumentmatching.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool tool = new ArgumentMatchingTool();

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_emptyListAndArray_returnsEmptyArray() {
     List<Matcher> matchers = new LinkedList<>();
     Object[] arguments = new Object[0];
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_invalidListSizeAndArray_returnsEmptyArray() {
     List<Matcher> matchers = Arrays.asList(Matchers.anyInt());
     Object[] arguments = new Object[0];
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[0], result);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_invalidListSizeAndArray_throwsException() {
     List<Matcher> matchers = Arrays.asList(Matchers.anyInt());
     Object[] arguments = new Object[1];
     tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersAndArgumentsWithCorrespondingElements_returnsEmptyArray() {
     List<Matcher> matchers = Arrays.asList(Matchers.eq(1), Matchers.eq(2), Matchers.eq(3));
     Object[] arguments = new Object[]{1, 2, 3};
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersAndArgumentsWithDifferentTypesAndSameToString_returnsIndexesArray() {
     List<Matcher> matchers = Arrays.asList(Matchers.instanceOf(String.class), Matchers.eq("abc"));
     Object[] arguments = new Object[]{1, "abc"};
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[]{1}, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersAndArgumentsWithDifferentTypesAndSameToStringButTypeDoesNotMatch_returnsEmptyArray() {
     List<Matcher> matchers = Arrays.asList(Matchers.instanceOf(String.class), Matchers.eq(1L));
     Object[] arguments = new Object[]{1, "abc"};
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersAndArgumentsWithDifferentTypesButToStringDoesNotMatch_returnsEmptyArray() {
     List<Matcher> matchers = Arrays.asList(Matchers.instanceOf(String.class), Matchers.eq(1));
     Object[] arguments = new Object[]{1, "abc"};
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[0], result);
 }

 @Test (expected = NullPointerException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_nullMatchers_throwsException() {
     Object[] arguments = new Object[]{1, "abc"};
     tool.getSuspiciouslyNotMatchingArgsIndexes(null, arguments);
 }

 @Test (expected = NullPointerException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_nullArguments_throwsException() {
     List<Matcher> matchers = Arrays.asList(Matchers.eq(1));
     tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, null);
 }

}