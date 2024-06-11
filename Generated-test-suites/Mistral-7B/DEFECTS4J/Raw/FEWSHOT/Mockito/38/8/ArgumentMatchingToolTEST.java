package org.mockito.internal.verification.argumentmatching;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Assert;
import org.junit.Test;

import org.mockito.internal.matchers.ContainsExtraTypeInformation;

@SuppressWarnings("unchecked")
public class ArgumentMatchingToolTest {

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_EmptyList() {
     List<Matcher> matchers = Arrays.asList();
     Object[] arguments = new Object[0];
     Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_DifferentSize() {
     List<Matcher> matchers = Arrays.asList(mock(Matcher.class));
     Object[] arguments = new Object[2];
     Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NoSuspicious() {
     List<Matcher> matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
     Object[] arguments = new Object[]{1, "string"};
     Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_OneSuspicious() {
     List<Matcher> matchers = Arrays.asList(mock(Matcher.class), mock(ContainsExtraTypeInformation.class));
     Object[] arguments = new Object[]{1, "String"};
     Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[]{1}, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MultipleSuspicious() {
     List<Matcher> matchers = Arrays.asList(
         mock(Matcher.class),
         mock(ContainsExtraTypeInformation.class),
         mock(ContainsExtraTypeInformation.class)
     );
     Object[] arguments = new Object[]{1, "String1", 2, "String2"};
     Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[]{1, 3}, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NullMatcher() {
     List<Matcher> matchers = Arrays.asList(mock(Matcher.class), null, mock(Matcher.class));
     Object[] arguments = new Object[]{1, "String", 2, "String"};
     Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NullArgument() {
     List<Matcher> matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
     Object[] arguments = new Object[]{1, null};
     Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[0], result);
 }

 private Matcher mock(Class<? extends Matcher> clazz) {
     return mock(clazz);
 }
}