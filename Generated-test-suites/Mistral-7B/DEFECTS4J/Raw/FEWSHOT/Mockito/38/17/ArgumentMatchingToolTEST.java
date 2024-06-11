package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.internal.matchers.ContainsExtraTypeInformation.typeMatches;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

public class ArgumentMatchingToolTest {

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_empty() {
     List<Matcher> matchers = Arrays.asList(new ContainsString("foo"));
     Object[] arguments = new Object[0];
     Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_noSuspicious() {
     List<Matcher> matchers = Arrays.asList(new ContainsString("foo"), new ContainsString("bar"), new ContainsString("baz"));
     Object[] arguments = new Object[]{"foo", "bar", "baz"};
     Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_oneSuspicious() {
     List<Matcher> matchers = Arrays.asList(new ContainsString("foo"), new ContainsString("1"), new ContainsString("bar"));
     Object[] arguments = new Object[]{"1", "foo", "bar"};
     Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertEquals(new Integer[]{0}, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_multipleSuspicious() {
     List<Matcher> matchers = Arrays.asList(new ContainsString("foo"), new ContainsString("1"), new ContainsString("bar"), new ContainsString("1"));
     Object[] arguments = new Object[]{"1", "foo", "bar", "1"};
     Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     assertEquals(new Integer[]{0, 3}, result);
 }

 @Test
 public void testToStringEquals_match() {
     Matcher m = new ContainsString("foo");
     Object arg = "foo";
     assertEquals(true, ArgumentMatchingTool.toStringEquals(m, arg));
 }

 @Test
 public void testToStringEquals_notMatch() {
     Matcher m = new ContainsString("foo");
     Object arg = "bar";
     assertEquals(false, ArgumentMatchingTool.toStringEquals(m, arg));
 }

 @Test
 public void testTypeMatches_match() {
     Matcher m = new ContainsString("foo");
     Object arg = "foo";
     assertEquals(true, typeMatches(m, arg));
 }

 @Test
 public void testTypeMatches_notMatch() {
     Matcher m = new ContainsString("String");
     Object arg = new Integer(1);
     assertEquals(false, typeMatches(m, arg));
 }

 @Test
 public void testSafelyMatches_match() {
     Matcher m = new ContainsString("foo");
     Object arg = "foo";
     assertEquals(true, ArgumentMatchingTool.safelyMatches(m, arg));
 }

 @Test(expected = Throwable.class)
 public void testSafelyMatches_notMatch() {
     Matcher m = new ContainsString("foo");
     Object arg = new Integer(1);
     ArgumentMatchingTool.safelyMatches(m, arg);
 }
}