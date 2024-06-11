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
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenSameNumberOfMatchersAndArguments_shouldReturnSuspiciousIndexes() {
     List<Matcher> matchers = new LinkedList<>();
     matchers.add(createMatcher("1"));
     matchers.add(createMatcher("2"));
     Object[] arguments = new Object[]{"1", "2"};
     Integer[] expected = {0, 1};
     Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenDifferentNumberOfMatchersAndArguments_shouldReturnEmptyArray() {
     List<Matcher> matchers = new LinkedList<>();
     matchers.add(createMatcher("1"));
     Object[] arguments = new Object[]{"1"};
     Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[0], actual);
 }

 @Test(expected = AssertionError.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenListOfMatchersIsNull_shouldThrowAssertionError() {
     Object[] arguments = new Object[]{"1", "2"};
     List<Matcher> matchers = null;
     try {
         argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     } catch (AssertionError ae) {
         // expected
     }
 }

 @Test(expected = AssertionError.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenObjectArrayIsNull_shouldThrowAssertionError() {
     List<Matcher> matchers = Arrays.asList(createMatcher("1"));
     try {
         argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, null);
     } catch (AssertionError ae) {
         // expected
     }
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenNoSuspiciousArguments_shouldReturnEmptyArray() {
     List<Matcher> matchers = Arrays.asList(createMatcher("1"), createMatcher("2"), createMatcher("3"));
     Object[] arguments = new Object[]{"1", "2", "3"};
     Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[0], actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenOneSuspiciousArgument_shouldReturnIndexOfThatArgument() {
     List<Matcher> matchers = Arrays.asList(createMatcher("1"), createMatcher(new ContainsExtraTypeInformation("java.lang.String"){@Override public String typeName() { return "java.lang.Object"; }}), createMatcher("3"));
     Object[] arguments = new Object[]{"1", "2", "3"};
     Integer[] expected = {1};
     Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenSeveralSuspiciousArguments_shouldReturnIndexesOfAllSuspiciousArguments() {
     List<Matcher> matchers = Arrays.asList(createMatcher("1"), createMatcher(new ContainsExtraTypeInformation("java.lang.String"){@Override public String typeName() { return "java.lang.Object"; }}), createMatcher("3"), createMatcher("4"));
     Object[] arguments = new Object[]{"1", "2", "3", "4"};
     Integer[] expected = {1, 3};
     Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatcherIsNull_shouldThrowIllegalArgumentException() {
     Object[] arguments = new Object[]{"1", "2", "3"};
     Matcher m = null;
     List<Matcher> matchers = Arrays.asList(m);
     try {
         argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     } catch (IllegalArgumentException iae) {
         // expected
     }
 }

 private Matcher createMatcher(Object value) {
     return new ContainsExtraTypeInformation(value) {
         @Override public String typeName() {
             return value.getClass().getName();
         }
     };
 }

}