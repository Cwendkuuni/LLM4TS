package org.mockito.internal.verification.argumentmatching.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class ArgumentMatchingToolTest {

 private static final Object[] ARGUMENTS_WITH_DIFFERENT_TYPES = new Object[]{1, "two"};
 private static final List<Matcher> MATCHERS_WITH_SAME_TOSTRING_AND_DIFFERENT_TYPE = Arrays.asList(
         ContainsExtraTypeInformation.not(instanceOf(Integer.class)),
         ContainsExtraTypeInformation.not(instanceOf(String.class))
 );

 @Test
 void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersAndArgumentsHaveDifferentTypes_thenReturnEmptyArray() {
     ArgumentMatchingTool tool = new ArgumentMatchingTool();
     List<Integer> result = Arrays.asList(0, 1).asList();
     Assertions.assertArrayEquals(new Integer[0], tool.getSuspiciouslyNotMatchingArgsIndexes(result, ARGUMENTS_WITH_DIFFERENT_TYPES));
 }

 @Test
 void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersAndArgumentsHaveSameToStringAndTypes_thenReturnEmptyArray() {
     ArgumentMatchingTool tool = new ArgumentMatchingTool();
     Object[] arguments = new Object[]{1, 1};
     List<Matcher> matchers = Arrays.asList(instanceOf(Integer.class), instanceOf(Integer.class));
     Assertions.assertArrayEquals(new Integer[0], tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test
 void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersAndArgumentsHaveSameToStringButDifferentTypes_thenReturnIndex() {
     ArgumentMatchingTool tool = new ArgumentMatchingTool();
     List<Integer> result = Arrays.asList(0, 1).asList();
     Assertions.assertArrayEquals(new Integer[]{0}, tool.getSuspiciouslyNotMatchingArgsIndexes(MATCHERS_WITH_SAME_TOSTRING_AND_DIFFERENT_TYPE, ARGUMENTS_WITH_DIFFERENT_TYPES));
 }

 @Test
 void testToStringEquals_whenMatcherAndObjectHaveSameToStringRepresentationButDifferentTypes_thenReturnFalse() {
     StringDescription stringDescription = new StringDescription();
     Matcher m = ContainsExtraTypeInformation.not(instanceOf(Integer.class));
     Assertions.assertFalse(new ArgumentMatchingTool().toStringEquals(m, new Object(){@Override public String toString() { return stringDescription.append("Matcher").toString(); }}), "Matcher");
 }

 @Test
 void testToStringEquals_whenMatcherAndObjectHaveSameToStringRepresentationAndSameType_thenReturnTrue() {
     Matcher m = instanceOf(Integer.class);
     Object arg = 1;
     Assertions.assertTrue(new ArgumentMatchingTool().toStringEquals(m, arg), "1");
 }
}