package org.mockito.internal.verification.argumentmatching.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import java.util.ArrayList;
import java.util.List;

public class ArgumentMatchingToolTest {

 private ArgumentMatchingTool tool = new ArgumentMatchingTool();

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_sameSize() {
 List<Matcher> matchers = new ArrayList<>();
 Object[] arguments = new Object[]{"test1", "test2"};
 Matcher m1 = ArgumentMatchers.equalTo("test1");
 Matcher m2 = ArgumentMatchers.equalTo("test2");
 matchers.add(m1);
 matchers.add(m2);
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(new Integer[]{}, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_differentSize() {
 List<Matcher> matchers = new ArrayList<>();
 Object[] arguments = new Object[]{"test1", "test2", "test3"};
 Matcher m1 = ArgumentMatchers.equalTo("test1");
 Matcher m2 = ArgumentMatchers.equalTo("test2");
 Matcher m3 = ArgumentMatchers.equalTo("test3");
 matchers.add(m1);
 matchers.add(m2);
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(new Integer[]{3}, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_noSuspiciousArgs() {
 List<Matcher> matchers = new ArrayList<>();
 Object[] arguments = new Object[]{"test1", "test1"};
 Matcher m1 = ArgumentMatchers.equalTo("test1");
 Matcher m2 = ArgumentMatchers.equalTo("test1");
 matchers.add(m1);
 matchers.add(m2);
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(new Integer[]{}, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_nullMatcherList() {
 Object[] arguments = new Object[]{"test1", "test2"};
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(null, arguments);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSuspiciouslyNotMatchingArgsIndexes_nullArgumentsList() {
 List<Matcher> matchers = new ArrayList<>();
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, null);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_suspiciousArgs() {
 List<Matcher> matchers = new ArrayList<>();
 Object[] arguments = new Object[]{"test1", new Object(){}, "test2"};
 Matcher m1 = ArgumentMatchers.equalTo("test1");
 Matcher m2 = ArgumentMatchers.instanceOf(Object.class);
 Matcher m3 = ArgumentMatchers.equalTo("test2");
 matchers.add(m1);
 matchers.add(m2);
 matchers.add(m3);
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(new Integer[]{1}, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_sameToStringButDifferentTypes() {
 List<Matcher> matchers = new ArrayList<>();
 Object[] arguments = new Object[]{"1", new Integer(1)};
 Matcher m1 = ArgumentMatchers.equalTo("1");
 Matcher m2 = ArgumentMatchers.instanceOf(Integer.class);
 matchers.add(m1);
 matchers.add(m2);
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(new Integer[]{1}, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_sameToStringButDifferentClasses() {
 List<Matcher> matchers = new ArrayList<>();
 Object[] arguments = new Object[]{"String", new StringBuilder()};
 Matcher m1 = ArgumentMatchers.equalTo("String");
 Matcher m2 = ArgumentMatchers.instanceOf(String.class);
 matchers.add(m1);
 matchers.add(m2);
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 Assert.assertArrayEquals(new Integer[]{1}, result);
 }

}