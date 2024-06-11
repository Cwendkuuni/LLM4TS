package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.matchers.ContainsExtraTypeInformation.typeMatches;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

public class ArgumentMatchingToolTest {

 private static Matcher<String> stringMatcher(String value) {
 return new TypeSafeMatcher<String>() {
 @Override
 public boolean matchesSafely(String item) {
 return item.equals(value);
 }

 @Override
 public void describeTo(StringDescription description) {
 description.appendText("equals ").appendValue(value);
 }
 };
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_Empty() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher<Object>> matchers = Arrays.asList(stringMatcher(""));
 Object[] arguments = new Object[0];
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MismatchedSize() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher<Object>> matchers = Arrays.asList(stringMatcher(""));
 Object[] arguments = new Object[]{new Integer(1)};
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NoSuspicious() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher<Object>> matchers = Arrays.asList(stringMatcher("foo"), stringMatcher("bar"));
 Object[] arguments = new Object[]{new String("foo"), new String("bar")};
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_SingleSuspicious() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher<Object>> matchers = Arrays.asList(new TypeSafeMatcher<Object>() {
 @Override
 public boolean matchesSafely(Object item) {
 return item instanceof String && ((String) item).startsWith("foo");
 }

 @Override
 public void describeTo(StringDescription description) {
 description.appendText("startsWith 'foo'").appendText(" (String)");
 }
 }, stringMatcher("bar"));
 Object[] arguments = new Object[]{new Integer(1), new String("foo bar")};
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[]{0}, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_MultipleSuspicious() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher<Object>> matchers = Arrays.asList(new TypeSafeMatcher<Object>() {
 @Override
 public boolean matchesSafely(Object item) {
 return item instanceof String && ((String) item).startsWith("foo");
 }

 @Override
 public void describeTo(StringDescription description) {
 description.appendText("startsWith 'foo'").appendText(" (String)");
 }
 }, new TypeSafeMatcher<Object>() {
 @Override
 public boolean matchesSafely(Object item) {
 return item instanceof String && ((String) item).startsWith("bar");
 }

 @Override
 public void describeTo(StringDescription description) {
 description.appendText("startsWith 'bar'").appendText(" (String)");
 }
 }, stringMatcher("baz"));
 Object[] arguments = new Object[]{new Integer(1), new String("foo bar baz"), new Integer(2)};
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[]{0, 2}, result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NullMatcher() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher<Object>> matchers = Arrays.asList(null, stringMatcher(""));
 Object[] arguments = new Object[]{new Integer(1), new String("foo")};
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[0], result);
 }

 @Test
 public void testGetSuspiciouslyNotMatchingArgsIndexes_NullArgument() {
 ArgumentMatchingTool tool = new ArgumentMatchingTool();
 List<Matcher<Object>> matchers = Arrays.asList(stringMatcher("foo"), stringMatcher("bar"));
 Object[] arguments = new Object[]{new String("foo"), null};
 Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
 assertArrayEquals(new Integer[0], result);
 }
}