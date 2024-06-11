package org.mockito.internal.verification.argumentmatching.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_EmptyList() {
     List<Matcher> matchers = new ArrayList<>();
     Object[] arguments = new Object[0];
     Integer[] expected = new Integer[0];
     assertArrayEquals(expected, argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_InvalidListSize() {
     List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation("string"));
     Object[] arguments = new Object[2];
     Integer[] expected = new Integer[0];
     assertArrayEquals(expected, argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NoSuspiciousMatches() {
     List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation("string"), new ContainsExtraTypeInformation("int"), new ContainsExtraTypeInformation("string"));
     Object[] arguments = new Object[]{"test1", 1, "test2"};
     Integer[] expected = new Integer[0];
     assertArrayEquals(expected, argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciousMatch() {
     List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation("String"), new ContainsExtraTypeInformation("List<Integer>"), new ContainsExtraTypeInformation("String"));
     Object[] arguments = new Object[]{"test1", Arrays.asList(1, 2), "test2"};
     Integer[] expected = new Integer[]{1};
     assertArrayEquals(expected, argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NullMatcher() {
     List<Matcher> matchers = Arrays.asList(null, new ContainsExtraTypeInformation("String"), new ContainsExtraTypeInformation("List<Integer>"));
     Object[] arguments = new Object[]{"test1", Arrays.asList(1, 2), "test2"};
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NullArgument() {
     List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation("String"), new ContainsExtraTypeInformation("List<Integer>"), new ContainsExtraTypeInformation("String"));
     Object[] arguments = new Object[]{null, Arrays.asList(1, 2), "test2"};
 }

 @Test
 public void testToStringEquals_SameObjects() {
     Matcher m = new ContainsExtraTypeInformation("String");
     Object arg = new String("test");
     assertTrue(argumentMatchingTool.toStringEquals(m, arg));
 }

 @Test
 public void testToStringEquals_DifferentObjects_SameToString() {
     Matcher m = new ContainsExtraTypeInformation("String");
     Object arg1 = new String("test");
     Object arg2 = new String("test");
     assertTrue(argumentMatchingTool.toStringEquals(m, arg1));
     assertTrue(argumentMatchingTool.toStringEquals(m, arg2));
 }

 @Test
 public void testToStringEquals_DifferentObjects_DifferentToString() {
     Matcher m = new ContainsExtraTypeInformation("String");
     Object arg1 = new String("test1");
     Object arg2 = new String("test2");
     assertFalse(argumentMatchingTool.toStringEquals(m, arg1));
 }

 @Test
 public void testSafelyMatches_Matches() {
     Matcher m = new ContainsExtraTypeInformation("String");
     Object arg = new String("test");
     assertTrue(argumentMatchingTool.safelyMatches(m, arg));
 }

 @Test
 public void testSafelyMatches_DoesNotMatch() {
     Matcher m = new ContainsExtraTypeInformation("Integer");
     Object arg = new String("test");
     assertFalse(argumentMatchingTool.safelyMatches(m, arg));
 }
}