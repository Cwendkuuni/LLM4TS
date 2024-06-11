package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.internal.matchers.ContainsExtraTypeInformation.typeMatches;
import static org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;

public class ArgumentMatchingToolTest {

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_emptyList() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 Object[] args = new Object[0];
 List<Matcher> matchers = new LinkedList<Matcher>();
 Integer[] expected = new Integer[0];
 assertArrayEquals(expected, tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, args));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_differentSizes() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 Object[] args = new Object[3];
 List<Matcher> matchers = new LinkedList<Matcher>();
 matchers.add(createMatcher("1"));
 Integer[] expected = new Integer[0];
 assertArrayEquals(expected, tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, args));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_noSuspiciousCases() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 Object[] args = new Object[]{"foo", 1, "bar"};
 List<Matcher> matchers = new LinkedList<Matcher>();
 matchers.add(createMatcher("String"));
 matchers.add(createMatcher(Integer.class));
 matchers.add(createMatcher("String"));
 Integer[] expected = new Integer[0];
 assertArrayEquals(expected, tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, args));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_singleSuspiciousCase() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 Object[] args = new Object[]{"foo", 1, "bar"};
 List<Matcher> matchers = new LinkedList<Matcher>();
 matchers.add(createMatcher("String"));
 matchers.add(createMatcher(Integer.class));
 matchers.add(createMatcher("Integer"));
 Integer[] expected = new Integer[]{2};
 assertArrayEquals(expected, tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, args));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_multipleSuspiciousCases() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 Object[] args = new Object[]{"foo", 1, "bar", 1};
 List<Matcher> matchers = new LinkedList<Matcher>();
 matchers.add(createMatcher("String"));
 matchers.add(createMatcher(Integer.class));
 matchers.add(createMatcher("Integer"));
 matchers.add(createMatcher(Integer.class));
 Integer[] expected = new Integer[]{2, 3};
 assertArrayEquals(expected, tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, args));
 }

 private Matcher createMatcher(Class<?> clazz) {
 return createMatcher(org.hamcrest.Matchers.instanceOf(clazz));
 }

 private Matcher createMatcher(Class<?> clazz, Object value) {
 return createMatcher(org.hamcrest.Matchers.equalTo(value));
 }

 private Matcher createMatcher(String description) {
 return org.hamcrest.Matcher.constant(description);
 }

 private Matcher createMatcher(Matcher matcher) {
 return ContainsExtraTypeInformation.value(matcher);
 }
}