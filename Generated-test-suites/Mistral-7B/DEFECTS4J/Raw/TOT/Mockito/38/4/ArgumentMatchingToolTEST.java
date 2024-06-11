package org.mockito.internal.verification.argumentmatching.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;

import java.util.ArrayList;
import java.util.List;

public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool tool = new ArgumentMatchingTool();

 @Test
 void testGetSuspiciouslyNotMatchingArgsIndexesWithSameTypes() {
 List<Matcher> matchers = new ArrayList<>();
 Matcher m1 = Arguments.is(1);
 Matcher m2 = Arguments.is(2);
 Matcher m3 = Arguments.is(3);
 Object[] arguments = new Object[]{1, 2, 3};
 List<Integer> expected = new ArrayList<>();
 expected.add(0);
 expected.add(2);
 List<Integer> actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assertions.assertArrayEquals(expected.toArray(new Integer[0]), actual);
 }

 @Test
 void testGetSuspiciouslyNotMatchingArgsIndexesWithDifferentTypes() {
 List<Matcher> matchers = new ArrayList<>();
 Matcher m1 = Arguments.is("str1");
 Matcher m2 = Arguments.is(1);
 Object[] arguments = new Object[]{"str1", 1};
 List<Integer> actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assertions.assertEquals(0, actual.length);
 }

 @Test
 void testGetSuspiciouslyNotMatchingArgsIndexesWithToStringEquals() {
 List<Matcher> matchers = new ArrayList<>();
 Matcher m1 = new ContainsExtraTypeInformation("str1", String.class) {};
 Matcher m2 = Arguments.is("str1");
 Object[] arguments = new Object[]{"str1", "str1"};
 List<Integer> expected = new ArrayList<>();
 expected.add(1);
 List<Integer> actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assertions.assertArrayEquals(expected.toArray(new Integer[0]), actual);
 }

 @Test
 void testGetSuspiciouslyNotMatchingArgsIndexesWithToStringNotEquals() {
 List<Matcher> matchers = new ArrayList<>();
 Matcher m1 = new ContainsExtraTypeInformation("str1", String.class) {};
 Matcher m2 = Arguments.is("str2");
 Object[] arguments = new Object[]{"str1", "str2"};
 List<Integer> actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assertions.assertEquals(0, actual.length);
 }

 @Test
 void testGetSuspiciouslyNotMatchingArgsIndexesWithWrongTypes() {
 List<Matcher> matchers = new ArrayList<>();
 Matcher m1 = Arguments.is(1);
 Object[] arguments = new Object[]{1, "str1"};
 List<Integer> actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assertions.assertEquals(0, actual.length);
 }

 @Test
 void testGetSuspiciouslyNotMatchingArgsIndexesWithNullMatcher() {
 List<Matcher> matchers = new ArrayList<>();
 matchers.add(null);
 Object[] arguments = new Object[]{1, 2, 3};
 List<Integer> actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assertions.assertEquals(0, actual.length);
 }

 @Test
 void testGetSuspiciouslyNotMatchingArgsIndexesWithNullArgument() {
 List<Matcher> matchers = new ArrayList<>();
 Matcher m1 = Arguments.is(1);
 Object[] arguments = new Object[]{1, null};
 List<Integer> actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assertions.assertEquals(1, actual.length);
 Assertions.assertEquals(1, actual[0].intValue());
 }
}