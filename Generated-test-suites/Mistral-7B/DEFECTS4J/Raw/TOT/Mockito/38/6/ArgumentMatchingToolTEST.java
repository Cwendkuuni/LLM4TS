package org.mockito.internal.verification.argumentmatching.test;

import org.junit.Test;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertArrayEquals;

public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_emptyList() {
     List<Matcher> matchers = Collections.emptyList();
     Object[] arguments = new Object[0];
     Integer[] expected = new Integer[0];
     Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_noSuspiciousMatch() {
     List<Matcher> matchers = Arrays.asList(
             isA(Integer.class),
             instanceOf(List.class)
     );
     Object[] arguments = new Object[]{1, Arrays.asList(1, 2)};
     Integer[] expected = new Integer[0];
     Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_singleSuspiciousMatch() {
     List<Matcher> matchers = Arrays.asList(
             isA(Integer.class),
             new ContainsExtraTypeInformation(String[].class) {
                 @Override
                 public boolean typeMatches(Object argument) {
                     return argument instanceof String[];
                 }
             },
             instanceOf(List.class)
     );
     Object[] arguments = new Object[]{1, new String[]{"one", "two"}, Arrays.asList(1, 2)};
     Integer[] expected = new Integer[]{1};
     Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(expected, actual);
 }

 @Test(expected = AssertionError.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_mismatchedSize() {
     List<Matcher> matchers = Arrays.asList(isA(Integer.class));
     Object[] arguments = new Object[]{1, "one"};
     Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_nonStringDescriptionMismatch() {
     List<Matcher> matchers = Arrays.asList(
             new ContainsExtraTypeInformation(String[].class) {
                 @Override
                 public boolean typeMatches(Object argument) {
                     return argument instanceof String[];
                 }

                 @Override
                 public void describeTo(StringDescription description) {
                     description.appendText("a list of strings");
                 }
             },
             instanceOf(List.class)
     );
     Object[] arguments = new Object[]{new Object[]{1, "one"}, Arrays.asList(1, 2)};
     Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Integer[] expected = new Integer[0];
     assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_stringDescriptionMismatch() {
     List<Matcher> matchers = Arrays.asList(
             new ContainsExtraTypeInformation(String[].class) {
                 @Override
                 public boolean typeMatches(Object argument) {
                     return argument instanceof String[];
                 }

                 @Override
                 public void describeTo(StringDescription description) {
                     description.appendText("an array of strings");
                 }
             },
             instanceOf(List.class)
     );
     Object[] arguments = new Object[]{new Object[]{1, "one"}, Arrays.asList(1, 2)};
     Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Integer[] expected = new Integer[]{0};
     assertArrayEquals(expected, actual);
 }
}