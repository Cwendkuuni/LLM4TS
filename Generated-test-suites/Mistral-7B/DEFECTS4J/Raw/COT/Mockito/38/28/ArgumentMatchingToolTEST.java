package org.mockito.internal.verification.argumentmatching;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool tool = new ArgumentMatchingTool();

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NullArguments() {
     List<Matcher> matchers = Arrays.asList(isA(String.class), isA(Integer.class));
     Object[] arguments = null;
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_EmptyList() {
     List<Matcher> matchers = Arrays.asList();
     Object[] arguments = new Object[]{new String("test"), new Integer(1)};
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MismatchedSize() {
     List<Matcher> matchers = Arrays.asList(isA(String.class), isA(Integer.class));
     Object[] arguments = new Object[]{new String("test")};
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciousMatch() {
     List<Matcher> matchers = Arrays.asList(isA(String.class), isA(String.class));
     Object[] arguments = new Object[]{new String("test"), new String("test2")};
     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[]{0}, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NullMatcher() {
     List<Matcher> matchers = Arrays.asList(null, isA(String.class));
     Object[] arguments = new Object[]{new String("test"), new Integer(1)};
     tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NullArgument() {
     List<Matcher> matchers = Arrays.asList(isA(String.class), isA(String.class));
     Object[] arguments = new Object[]{new String("test"), null};
     tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 }

 @Test
 public void testSafelyMatches_Matches() {
     Matcher m = isA(String.class);
     Object arg = new String("test");
     assertTrue(tool.safelyMatches(m, arg));
 }

 @Test
 public void testSafelyMatches_DoesNotMatch() {
     Matcher m = isA(Integer.class);
     Object arg = new String("test");
     assertFalse(tool.safelyMatches(m, arg));
 }

 @Test
 public void testToStringEquals_Matches() {
     Matcher m = isA(String.class);
     Object arg = new String("test");
     assertTrue(tool.toStringEquals(m, arg));
 }

 @Test
 public void testToStringEquals_DoesNotMatch() {
     Matcher m = isA(String.class);
     Object arg = new StringBuilder("test").toString();
     assertFalse(tool.toStringEquals(m, arg));
 }

}