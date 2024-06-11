package org.mockito.internal.matchers;

import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.Same.same;

import org.hamcrest.Description;
import org.junit.Test;

public class SameTest {

 @Test
 public void testSameInstanceMatches() {
 Object wanted = new Object();
 assertTrue(same(wanted).matches(wanted));
 }

 @Test
 public void testSameInstanceOfStringMatches() {
 String wanted = "foo";
 assertTrue(same(wanted).matches(wanted));
 }

 @Test
 public void testSameInstanceOfCharacterMatches() {
 char wanted = 'a';
 assertTrue(same(wanted).matches(wanted));
 }

 @Test
 public void testSameInstanceOfDifferentTypeDoesNotMatch() {
 Object wanted = new Object();
 int actual = 1;
 assertFalse(same(wanted).matches(actual));
 }

 @Test
 public void testSameInstanceOfNullMatchesNull() {
 assertTrue(same(null).matches(null));
 }

 @Test
 public void testSameStringWithQuotes() {
 String wanted = "\"foo\"";
 String actual = "\"foo\"";
 assertTrue(same(wanted).matches(actual));
 }

 @Test
 public void testSameCharacterWithQuotes() {
 Character wanted = '\'a\'';
 Character actual = '\'a\'';
 assertTrue(same(wanted).matches(actual));
 }

 @Test
 public void testDescriptionMatchesSameInstance() {
 Object wanted = new Object();
 Description description = new Description();
 same(wanted).describeTo(description);
 String expected = "same(<object>foo</object>)";
 String actual = description.toString();
 assertTrue(expected.equals(actual));
 }
}