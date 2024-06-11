package org.mockito.internal.verification.argumentmatching;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes;

public class ArgumentMatchingToolTest {

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_nullMatcher_returnsEmptyArray() {
     Object[] arguments = new Object[]{1, "two", 3.0F};
     List<Matcher> matchers = null;
     Integer[] expected = new Integer[0];
     assertArrayEquals(expected, getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_differentSize_returnsEmptyArray() {
     Object[] arguments = new Object[]{1, "two", 3.0F};
     List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation() {
         @SuppressWarnings("unchecked")
         @Override
         public Class<?> getTypeThatMatchesNull() {
             return Integer.class;
         }

         @Override
         public boolean matches(Object argument) {
             return argument instanceof Number;
         }
     }, new ContainsExtraTypeInformation() {
         @SuppressWarnings("unchecked")
         @Override
         public Class<?> getTypeThatMatchesNull() {
             return String.class;
         }

         @Override
         public boolean matches(Object argument) {
             return argument instanceof String;
         }
     });
     Integer[] expected = new Integer[0];
     assertArrayEquals(expected, getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_noSuspiciousArgs_returnsEmptyArray() {
     Object[] arguments = new Object[]{1, "two", 3.0F};
     List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation() {
         @SuppressWarnings("unchecked")
         @Override
         public Class<?> getTypeThatMatchesNull() {
             return Integer.class;
         }

         @Override
         public boolean matches(Object argument) {
             return argument.equals(1);
         }
     }, new ContainsExtraTypeInformation() {
         @SuppressWarnings("unchecked")
         @Override
         public Class<?> getTypeThatMatchesNull() {
             return String.class;
         }

         @Override
         public boolean matches(Object argument) {
             return argument.equals("two");
         }
     }, new ContainsExtraTypeInformation() {
         @SuppressWarnings("unchecked")
         @Override
         public Class<?> getTypeThatMatchesNull() {
             return Float.class;
         }

         @Override
         public boolean matches(Object argument) {
             return argument.equals(3.0F);
         }
     });
     Integer[] expected = new Integer[0];
     assertArrayEquals(expected, getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_singleSuspiciousArg_returnsSingleIndexArray() {
     Object[] arguments = new Object[]{1, "two", 3.0F};
     List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation() {
         @SuppressWarnings("unchecked")
         @Override
         public Class<?> getTypeThatMatchesNull() {
             return Integer.class;
         }

         @Override
         public boolean matches(Object argument) {
             return argument.equals(2);
         }
     }, new ContainsExtraTypeInformation() {
         @SuppressWarnings("unchecked")
         @Override
         public Class<?> getTypeThatMatchesNull() {
             return String.class;
         }

         @Override
         public boolean matches(Object argument) {
             return argument.equals("two");
         }
     }, new ContainsExtraTypeInformation() {
         @SuppressWarnings("unchecked")
         @Override
         public Class<?> getTypeThatMatchesNull() {
             return Float.class;
         }

         @Override
         public boolean matches(Object argument) {
             return argument.equals(3.0F);
         }
     });
     Integer[] expected = new Integer[]{1};
     assertArrayEquals(expected, getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_multipleSuspiciousArgs_returnsMultipleIndexArray() {
     Object[] arguments = new Object[]{1, "two", 3.0F, 4.0F};
     List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation() {
         @SuppressWarnings("unchecked")
         @Override
         public Class<?> getTypeThatMatchesNull() {
             return Integer.class;
         }

         @Override
         public boolean matches(Object argument) {
             return argument.equals(1);
         }
     }, new ContainsExtraTypeInformation() {
         @SuppressWarnings("unchecked")
         @Override
         public Class<?> getTypeThatMatchesNull() {
             return Integer.class;
         }

         @Override
         public boolean matches(Object argument) {
             return argument.equals(4.0F);
         }
     }, new ContainsExtraTypeInformation() {
         @SuppressWarnings("unchecked")
         @Override
         public Class<?> getTypeThatMatchesNull() {
             return String.class;
         }

         @Override
         public boolean matches(Object argument) {
             return argument.equals("two");
         }
     });
     Integer[] expected = new Integer[]{2, 3};
     assertArrayEquals(expected, getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_nullArgument_returnsNull() {
     Object[] arguments = new Object[]{1, null, 3.0F};
     List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation() {
         @SuppressWarnings("unchecked")
         @Override
         public Class<?> getTypeThatMatchesNull() {
             return Integer.class;
         }

         @Override
         public boolean matches(Object argument) {
             return argument.equals(1);
         }
     }, new ContainsExtraTypeInformation() {
         @SuppressWarnings("unchecked")
         @Override
         public Class<?> getTypeThatMatchesNull() {
             return String.class;
         }

         @Override
         public boolean matches(Object argument) {
             return argument != null && argument.equals("two");
         }
     }, new ContainsExtraTypeInformation() {
         @SuppressWarnings("unchecked")
         @Override
         public Class<?> getTypeThatMatchesNull() {
             return Float.class;
         }

         @Override
         public boolean matches(Object argument) {
             return argument.equals(3.0F);
         }
     });
     assertNull(getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
 }
}