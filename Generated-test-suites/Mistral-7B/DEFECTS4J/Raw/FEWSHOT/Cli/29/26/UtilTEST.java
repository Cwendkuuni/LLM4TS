package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 // Test null input
 assertNull(Util.stripLeadingHyphens(null));

 // Test empty string
 assertEquals("", Util.stripLeadingHyphens(""));

 // Test single hyphen at the beginning
 assertEquals("foo", Util.stripLeadingHyphens("-foo"));

 // Test double hyphens at the beginning
 assertEquals("foo", Util.stripLeadingHyphens("--foo"));

 // Test triple hyphens at the beginning
 assertEquals("foo", Util.stripLeadingHyphens("---foo"));

 // Test string with hyphens in the middle
 assertEquals("bar baz", Util.stripLeadingHyphens("--bar-baz"));
 }

 @Test
 public void testStripLeadingAndTrailingQuotes() {
 // Test null input
 assertNull(Util.stripLeadingAndTrailingQuotes(null));

 // Test empty string
 assertEquals("", Util.stripLeadingAndTrailingQuotes(""));

 // Test string with leading quotes only
 assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));

 // Test string with trailing quotes only
 assertEquals("foo", Util.stripLeadingAndTrailingQuotes("foo\""));

 // Test string with both leading and trailing quotes
 assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));

 // Test string with quotes in the middle
 assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));
 }

 @Test
 public void testStripLeadingHyphensWithQuotes() {
 // Test string with leading hyphens and quotes
 assertEquals("foo", Util.stripLeadingHyphens("--\"foo\""));

 // Test string with trailing hyphens and quotes
 assertEquals("foo", Util.stripLeadingHyphens("\"foo--\""));

 // Test string with leading and trailing hyphens and quotes
 assertEquals("foo", Util.stripLeadingHyphens("--\"foo--\""));
 }
}