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
 public void testGetSuspiciouslyNotMatchingArgsIndexes_EmptyList_ReturnsEmptyArray() {
     List<Matcher> matchers = new LinkedList<>();
     Object[] arguments = new Object[0];
     Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_DifferentSizes_ReturnsEmptyArray() {
     List<Matcher> matchers = new LinkedList<>();
     Object[] arguments = new Object[3];
     Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NoSuspicious_ReturnsEmptyArray() {
     List<Matcher> matchers = createMatcherList();
     Object[] arguments = createObjectArray(matchers.size());
     Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_Suspicious_ReturnsCorrectIndexes() {
     Matcher m1 = createMatcher("Matcher 1");
     Matcher m2 = createMatcher("Matcher 2");
     Matcher m3 = createMatcher("Matcher 3");
     Object[] arguments = new Object[]{1, "String 1", new Object()};
     List<Matcher> matchers = new LinkedList<>(Arrays.asList(m1, m2, m3));
     Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[]{1}, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_InvalidMatcherInList_ThrowsException() {
     List<Matcher> matchers = createMatcherList();
     matchers.add(null);
     Object[] arguments = createObjectArray(matchers.size());
     argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_InvalidArgument_ThrowsException() {
     List<Matcher> matchers = createMatcherList();
     Object[] arguments = new Object[0];
     argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 }

 private List<Matcher> createMatcherList() {
     ContainsExtraTypeInformation m1 = new ContainsString("String 1");
     ContainsExtraTypeInformation m2 = new ContainsString("String 2");
     ContainsExtraTypeInformation m3 = new ContainsString("String 3");
     return new LinkedList<>(Arrays.asList(m1, m2, m3));
 }

 private Object[] createObjectArray(int size) {
     return new Object[size];
 }

 private Matcher createMatcher(String name) {
     return new ContainsString(name);
 }

}