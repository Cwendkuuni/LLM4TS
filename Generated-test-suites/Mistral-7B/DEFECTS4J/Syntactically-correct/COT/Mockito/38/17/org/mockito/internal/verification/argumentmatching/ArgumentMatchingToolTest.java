package org.mockito.internal.verification.argumentmatching.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_validArgs() {
 List<Matcher> matchers = new LinkedList<>();
 matchers.add(ArgumentMatchingWithSameValue.anyInt());
 matchers.add(ArgumentMatchingWithSameValue.anyInt());
 Object[] arguments = new Object[]{1, 1};
 Integer[] expected = new Integer[0];
 Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_suspiciousArgs() {
 List<Matcher> matchers = new LinkedList<>();
 matchers.add(ArgumentMatchingWithDifferentTypes.stringMatcher());
 matchers.add(ArgumentMatchingWithDifferentTypes.integerMatcher());
 Object[] arguments = new Object[]{"test", 1};
 Integer[] expected = new Integer[]{0};
 Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_invalidArgumentsSize() {
 List<Matcher> matchers = new LinkedList<>();
 matchers.add(ArgumentMatchingWithSameValue.anyInt());
 Object[] arguments = new Object[]{1};
 argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_invalidMatcherSize() {
 List<Matcher> matchers = new LinkedList<>();
 Object[] arguments = new Object[]{1};
 argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 }

 @Test(expected = NullPointerException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_nullMatchers() {
 Object[] arguments = new Object[]{1};
 argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(null, arguments);
 }

 @Test(expected = NullPointerException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_nullArguments() {
 List<Matcher> matchers = new LinkedList<>();
 matchers.add(ArgumentMatchingWithSameValue.anyInt());
 argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, null);
 }

 @Test
 public void testSafelyMatches_matches() {
 Matcher m = ArgumentMatchingWithSameValue.equalTo(1);
 Object arg = 1;
 Assert.assertTrue(argumentMatchingTool.safelyMatches(m, arg));
 }

 @Test
 public void testSafelyMatches_doesNotMatch() {
 Matcher m = ArgumentMatchingWithSameValue.equalTo(1);
 Object arg = "test";
 Assert.assertFalse(argumentMatchingTool.safelyMatches(m, arg));
 }

 @Test
 public void testToStringEquals_stringsEqual() {
 Matcher m = ArgumentMatchingWithSameValue.stringMatcher();
 Object arg = "test";
 Assert.assertTrue(argumentMatchingTool.toStringEquals(m, arg));
 }

 @Test
 public void testToStringEquals_stringsNotEqual() {
 Matcher m = ArgumentMatchingWithSameValue.stringMatcher();
 Object arg = "otherTest";
 Assert.assertFalse(argumentMatchingTool.toStringEquals(m, arg));
 }

 private static class ArgumentMatchingWithSameValue {
 static Matcher anyInt() {
 return org.hamcrest.MatcherRegistry.getObject("any integer");
 }

 static Matcher stringMatcher() {
 return org.hamcrest.MatcherRegistry.getObject("a string");
 }

 static Matcher integerMatcher() {
 return org.hamcrest.MatcherRegistry.getObject("an integer");
 }

 static Matcher equalTo(Object value) {
 return org.hamcrest.MatcherRegistry.getObject("equal to " + value);
 }
 }

}