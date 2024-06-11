package org.mockito.internal.verification.argumentmatching;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ArgumentMatchingToolTest {

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NullArguments() {
 List<Matcher> matchers = Arrays.asList(new ContainsText("foo"));
 Object[] arguments = null;
 Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertNull(result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_EmptyListOfMatchers() {
 Object[] arguments = new Object[]{"arg1", "arg2"};
 Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(Arrays.asList(), arguments);
 assertNull(result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_EmptyArrayOfArguments() {
 List<Matcher> matchers = Arrays.asList(new ContainsText("foo"), new ContainsText("bar"));
 Object[] arguments = {};
 Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertNull(result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_ArgumentsListSmallerThanMatchersList() {
 List<Matcher> matchers = Arrays.asList(new ContainsText("foo"), new ContainsText("bar"));
 Object[] arguments = new Object[]{"arg1"};
 Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertNull(result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersListBiggerThanArgumentsArray() {
 List<Matcher> matchers = Arrays.asList(new ContainsText("foo"), new ContainsText("bar"), new ContainsText("baz"));
 Object[] arguments = new Object[]{"arg1"};
 Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertNull(result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NoSuspiciousArgs() {
 List<Matcher> matchers = Arrays.asList(new ContainsText("foo"), new ContainsText("foo"));
 Object[] arguments = new Object[]{"arg1", "arg2"};
 Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_SingleSuspiciousArg() {
 List<Matcher> matchers = Arrays.asList(new ContainsText("foo"), new ContainsText("bar"));
 Object[] arguments = new Object[]{"arg1", new Integer(1)};
 Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertEquals(new Integer(1), result[0]);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MultipleSuspiciousArgs() {
 List<Matcher> matchers = Arrays.asList(new ContainsText("foo"), new ContainsText("bar"), new ContainsText("baz"));
 Object[] arguments = new Object[]{"arg1", new Integer(1), new Integer(2), new Integer(3)};
 Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[]{new Integer(1), new Integer(3)}, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersContainExtraTypeInformation() {
 List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation("String", new ContainsText("foo")));
 Object[] arguments = new Object[]{"arg1", new Integer(1)};
 Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertNull(result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersDontMatchArgsButToStringEquals() {
 List<Matcher> matchers = Arrays.asList(new ContainsText("foo"), new ContainsText("bar"));
 Object[] arguments = new Object[]{"arg1", new Integer(1)};
 Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertNull(result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersToStringEqualsArgsButTypesDontMatch() {
 List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation("Integer", new ContainsText("1")));
 Object[] arguments = new Object[]{"arg1", new Integer(1)};
 Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertNull(result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersToStringEqualsArgsAndTypesMatch() {
 List<Matcher> matchers = Arrays.asList(new ContainsText("foo"), new ContainsText("foo"));
 Object[] arguments = new Object[]{"arg1", "arg2"};
 Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersToStringEqualsArgsButTypesDontMatchAndSuspiciousArgIndexIsFirst() {
 List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation("Integer", new ContainsText("1")));
 Object[] arguments = new Object[]{"arg1", new Integer(1)};
 Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertNull(result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersToStringEqualsArgsButTypesDontMatchAndSuspiciousArgIndexIsLast() {
 List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation("Integer", new ContainsText("1")));
 Object[] arguments = new Object[]{"arg1", new Integer(1), new Integer(2)};
 Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertNull(result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersToStringEqualsArgsButTypesDontMatchAndSuspiciousArgIndexIsMiddle() {
 List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation("Integer", new ContainsText("1")));
 Object[] arguments = new Object[]{"arg1", new Integer(2), new Integer(1)};
 Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertNull(result);
 }
}