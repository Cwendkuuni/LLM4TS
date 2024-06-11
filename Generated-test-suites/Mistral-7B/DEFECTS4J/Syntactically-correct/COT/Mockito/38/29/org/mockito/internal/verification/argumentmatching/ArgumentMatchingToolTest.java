package org.mockito.internal.verification.argumentmatching.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

import java.util.ArrayList;
import java.util.List;

public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool tool = new ArgumentMatchingTool();

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersAndArgumentsAreEqual_thenReturnEmptyArray() {
     List<Matcher> matchers = new ArrayList<>();
     Object[] arguments = new Object[]{1, "one", 1.0};
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersAndArgumentsAreDifferentTypesButToStringAreEqual_thenReturnIndexes() {
     List<Matcher> matchers = new ArrayList<>();
     matchers.add(new ContainsExtraTypeInformation(Integer.class, "integer"));
     Object[] arguments = new Object[]{1, "one", 1};
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[]{1}, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersAndArgumentsAreDifferentLengths_thenThrowIllegalArgumentException() {
     List<Matcher> matchers = new ArrayList<>();
     Object[] arguments = new Object[]{1, "one", 1.0, 4};
     tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatcherMatchesArgument_thenReturnEmptyArray() {
     List<Matcher> matchers = new ArrayList<>();
     matchers.add(new ContainsExtraTypeInformation(Integer.class, "integer"));
     Object[] arguments = new Object[]{1, 1};
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenToStringDoesNotMatch_thenReturnEmptyArray() {
     List<Matcher> matchers = new ArrayList<>();
     matchers.add(new ContainsExtraTypeInformation(Integer.class, "string"));
     Object[] arguments = new Object[]{1, 1};
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[0], result);
 }

 @Test(expected = NullPointerException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersIsNull_thenThrowNullPointerException() {
     Object[] arguments = new Object[]{1, 1};
     tool.getSuspiciouslyNotMatchingArgsIndexes(null, arguments);
 }

 @Test(expected = NullPointerException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenArgumentsIsNull_thenThrowNullPointerException() {
     List<Matcher> matchers = new ArrayList<>();
     tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, null);
 }
}