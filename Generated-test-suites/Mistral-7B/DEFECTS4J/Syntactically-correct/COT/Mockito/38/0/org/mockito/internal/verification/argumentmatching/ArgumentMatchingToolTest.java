package org.mockito.internal.verification.argumentmatching.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool tool = new ArgumentMatchingTool();

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_correctNumberOfArguments() {
     List<Matcher> matchers = new LinkedList<>();
     matchers.add(contains("expectedValue1"));
     matchers.add(contains("expectedValue2"));
     Object[] arguments = new Object[]{"actualValue1", "actualValue2"};

     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[]{null, null}, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_noSuspiciousArgs() {
     List<Matcher> matchers = new LinkedList<>();
     matchers.add(contains("expectedValue1").withSource(MyClass.class));
     matchers.add(contains("expectedValue2").withSource(MyClass.class));
     Object[] arguments = new Object[]{"actualValue1", "actualValue2"};

     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[]{null, null}, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_suspiciousArgExists_firstIndex() {
     List<Matcher> matchers = new LinkedList<>();
     matchers.add(contains("expectedValue1").withSource(MyClass.class));
     matchers.add(contains("expectedString").withSource(String.class));
     Object[] arguments = new Object[]{"actualValue1", "actualString"};

     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[]{0}, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_suspiciousArgExists_lastIndex() {
     List<Matcher> matchers = new LinkedList<>();
     matchers.add(contains("expectedValue1").withSource(MyClass.class));
     matchers.add(contains("expectedValue2").withSource(MyClass.class));
     Object[] arguments = new Object[]{"actualValue1", "actualValueWithDifferentType"};

     Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
     Assert.assertArrayEquals(new Integer[]{1}, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_mismatchedSize() {
     List<Matcher> matchers = new LinkedList<>();
     matchers.add(contains("expectedValue1").withSource(MyClass.class));
     Object[] arguments = new Object[]{"actualValue1"};

     tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 }

 @Test
 public void testToStringEquals_match() {
     Matcher m = contains("expectedValue");
     Object arg = "expectedValue";

     assertTrue(tool.toStringEquals(m, arg));
 }

 @Test
 public void testToStringEquals_doesNotMatch() {
     Matcher m = contains("expectedValue");
     Object arg = new Object() {};

     assertFalse(tool.toStringEquals(m, arg));
 }

 @Test
 public void testSafelyMatches_matches() {
     Matcher m = contains("expectedValue");
     Object arg = "expectedValue";

     assertTrue(tool.safelyMatches(m, arg));
 }

 @Test(expected = Throwable.class)
 public void testSafelyMatches_doesNotMatch() {
     Matcher m = contains("expectedValue");
     Object arg = new Object() {};

     tool.safelyMatches(m, arg);
 }

 @Test
 public void testSafelyMatches_throwsIllegalArgumentException_whenMatcherDoesNotMatchString() {
     Matcher m = contains("expectedValue");
     Object arg = "notExpectedValue";

     assertFalse(tool.safelyMatches(m, arg));
 }
}