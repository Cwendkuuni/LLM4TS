package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.*;
import org.hamcrest.Matcher;
import org.junit.Test;

public class ArgumentMatchingToolTest {

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_EmptyList() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher> matchers = new LinkedList<>();
 Object[] arguments = new Object[0];
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_DifferentSizes() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher> matchers = new LinkedList<>();
 Object[] arguments = new Object[3];
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NoSuspiciousMatches() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher> matchers = new LinkedList<>();
 Matcher m1 = isA(String.class);
 Object[] arguments = new Object[]{"foo", "bar"};
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciousMatch() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher> matchers = new LinkedList<>();
 Matcher m1 = isA(String.class);
 Matcher m2 = isA(Integer.class);
 Object[] arguments = new Object[]{"1", "foo"};
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[]{0}, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MultipleSuspiciousMatches() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher> matchers = new LinkedList<>();
 Matcher m1 = isA(String.class);
 Matcher m2 = isA(Integer.class);
 Object[] arguments = new Object[]{"1", "foo", 1, "bar"};
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Integer[] expected = new Integer[]{0, 2};
 assertArrayEquals(expected, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NullMatcher() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher> matchers = new LinkedList<>();
 matchers.add(null);
 Object[] arguments = new Object[]{"foo", "bar"};
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NullArgument() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher> matchers = new LinkedList<>();
 Matcher m1 = isA(String.class);
 Object[] arguments = new Object[]{null, "bar"};
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[0], result);
 }
}