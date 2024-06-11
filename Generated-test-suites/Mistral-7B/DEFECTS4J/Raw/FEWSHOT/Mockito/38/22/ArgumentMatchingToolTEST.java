package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ContainsExtraTypeInformation.typeMatches;

import org.hamcrest.Matcher;
import org.junit.Test;

public class ArgumentMatchingToolTest {

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_empty() {
     List<Matcher> matchers = new LinkedList<>();
     Object[] arguments = new Object[0];
     Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_mismatched_size() {
     List<Matcher> matchers = new LinkedList<>();
     Object[] arguments = new Object[1];
     Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_no_suspicious() {
     List<Matcher> matchers = new LinkedList<>();
     Object[] arguments = new Object[]{1, "foo"};
     Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_suspicious() {
     List<Matcher> matchers = new LinkedList<>();
     matchers.add(contains("1").ignoringCase());
     Object[] arguments = new Object[]{1, "foo"};
     Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(new Integer[0], result);

     matchers.clear();
     matchers.add(contains(1.0).ignoringCase());
     arguments = new Object[]{1, 1.0};
     result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(new Integer[]{0}, result);

     matchers.clear();
     matchers.add(contains("foo"));
     arguments = new Object[]{1, "foo"};
     result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(new Integer[]{1}, result);

     matchers.clear();
     matchers.add(contains("foo").ignoringCase());
     arguments = new Object[]{1, "foo"};
     result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(new Integer[]{1}, result);

     matchers.clear();
     matchers.add(contains("foo").ignoringCase());
     arguments = new Object[]{1, "bar"};
     result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_null_matchers() {
     Object[] arguments = new Object[]{1, "foo"};
     Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(null, arguments);
     assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_null_arguments() {
     List<Matcher> matchers = new LinkedList<>();
     Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, null);
     assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testToStringEquals() {
     Matcher m = contains("foo");
     String s = StringDescription.toString(m);
     Object arg = "foo";
     assertTrue(ArgumentMatchingTool.toStringEquals(m, arg));
     arg = "bar";
     assertFalse(ArgumentMatchingTool.toStringEquals(m, arg));
 }

 @Test
 public void testSafelyMatches() {
     Matcher m = contains("foo");
     Object arg1 = "foo";
     assertTrue(ArgumentMatchingTool.safelyMatches(m, arg1));

     m = contains(1.0);
     Object arg2 = 1.0;
     assertTrue(ArgumentMatchingTool.safelyMatches(m, arg2));

     m = contains("foo").ignoringCase();
     arg1 = "foo";
     assertTrue(ArgumentMatchingTool.safelyMatches(m, arg1));

     m = contains("FOO").ignoringCase();
     arg1 = "foo";
     assertTrue(ArgumentMatchingTool.safelyMatches(m, arg1));

     m = contains("foo").ignoringCase();
     arg1 = "bar";
     assertFalse(ArgumentMatchingTool.safelyMatches(m, arg1));

     m = contains(Integer.class);
     arg1 = 1;
     assertTrue(ArgumentMatchingTool.safelyMatches(m, arg1));

     m = contains(String.class);
     arg1 = "foo";
     assertTrue(ArgumentMatchingTool.safelyMatches(m, arg1));

     m = contains("foo").ignoringCase();
     arg1 = new String("foo");
     assertTrue(ArgumentMatchingTool.safelyMatches(m, arg1));

     m = contains("foo").ignoringCase();
     arg1 = new StringBuffer("foo");
     assertFalse(ArgumentMatchingTool.safelyMatches(m, arg1));
 }

 @Test
 public void testTypeMatches() {
     Matcher m = contains(Integer.class);
     Object arg = 1;
     assertTrue(ArgumentMatchingTool.typeMatches(m, arg));

     m = contains(String.class);
     arg = "foo";
     assertTrue(ArgumentMatchingTool.typeMatches(m, arg));

     m = contains(String[].class);
     arg = new String[]{"foo", "bar"};
     assertTrue(ArgumentMatchingTool.typeMatches(m, arg));

     m = contains(StringBuffer.class);
     arg = new StringBuffer("foo");
     assertTrue(ArgumentMatchingTool.typeMatches(m, arg));

     m = contains(StringBuilder.class);
     arg = new StringBuilder("foo");
     assertFalse(ArgumentMatchingTool.typeMatches(m, arg));
 }
}