package org.mockito.internal.verification.argumentmatching.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_emptyList_returnsEmptyArray() {
     List<Matcher> matchers = new LinkedList<>();
     Object[] arguments = new Object[0];

     Integer[] result = new ArgumentMatchingTool().getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

     Assert.assertArrayEqual(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_differentListSizes_returnsEmptyArray() {
     List<Matcher> matchers = new LinkedList<>(Arrays.asList(new ContainsExtraTypeInformation(String.class, "foo"), new ContainsExtraTypeInformation(Integer.class, "bar")));
     Object[] arguments = new Object[]{new String("foo"), new Integer(1)};

     Integer[] result = new ArgumentMatchingTool().getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

     Assert.assertArrayEqual(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_noSuspiciousMatches_returnsEmptyArray() {
     List<Matcher> matchers = new LinkedList<>(Arrays.asList(new ContainsExtraTypeInformation(String.class, "foo"), new ContainsExtraTypeInformation(String.class, "foo"), new ContainsExtraTypeInformation(String.class, "foo"), new ContainsExtraTypeInformation(String.class, "foo"), new ContainsExtraTypeInformation(String.class, "foo"), new ContainsExtraTypeInformation(String.class, "foo"), new ContainsExtraTypeInformation(String.class, "foo"), new ContainsExtraTypeInformation(String.class, "foo"), new ContainsExtraTypeInformation(String.class, "foo")));
     Object[] arguments = new Object[]{new String("foo"), new String("foo"), new String("foo"), new String("foo"), new String("foo"), new String("foo"), new String("foo"), new String("foo"), new String("foo")};

     Integer[] result = new ArgumentMatchingTool().getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

     Assert.assertArrayEqual(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_matchingTypes_returnsEmptyArray() {
     List<Matcher> matchers = new LinkedList<>(Arrays.asList(new ContainsExtraTypeInformation(String.class, "foo"), new ContainsExtraTypeInformation(String.class, "foo"), new ContainsExtraTypeInformation(String.class, "foo"), new ContainsExtraTypeInformation(String.class, "foo"), new ContainsExtraTypeInformation(String.class, "foo"), new ContainsExtraTypeInformation(String.class, "foo"), new ContainsExtraTypeInformation(String.class, "foo"), new ContainsExtraTypeInformation(String.class, "foo"), new ContainsExtraTypeInformation(String.class, "foo")));
     Object[] arguments = new Object[]{new String("foo"), new String("foo"), new String("foo"), new String("foo"), new String("foo"), new String("foo"), new String("foo"), new String("foo"), new String("foo")};

     Integer[] result = new ArgumentMatchingTool().getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

     Assert.assertArrayEqual(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_differentTypes_returnsIndexes() {
     List<Matcher> matchers = new LinkedList<>(Arrays.asList(new ContainsExtraTypeInformation(String.class, "foo"), new ContainsExtraTypeInformation(Integer.class, "1"), new ContainsExtraTypeInformation(String.class, "foo"), new ContainsExtraTypeInformation(String.class, "foo"), new ContainsExtraTypeInformation(Integer.class, "2"), new ContainsExtraTypeInformation(String.class, "foo"), new ContainsExtraTypeInformation(String.class, "foo"), new ContainsExtraTypeInformation(String.class, "foo"), new ContainsExtraTypeInformation(Integer.class, "3")));
     Object[] arguments = new Object[]{new String("foo"), new Integer(1), new String("foo"), new String("foo"), new Integer(2), new String("foo"), new String("foo"), new String("foo"), new Integer(3)};

     Integer[] result = new ArgumentMatchingTool().getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

     Assert.assertArrayEquals(new Integer[]{1, 3}, result);
 }
}