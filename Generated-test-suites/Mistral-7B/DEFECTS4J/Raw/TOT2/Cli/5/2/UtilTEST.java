package org.junit.tests.apache.commons.cli.util;

import org.apache.commons.cli.Util;
import org.junit.*;
import static org.junit.Assert.*;

public class UtilTest {

@Test
public void testStripLeadingHyphens() {
String input = "--test";
String expected = "test";
String actual = Util.stripLeadingHyphens(input);
assertEquals(expected, actual);
}

// Rest of the test cases

@Test
public void testStripLeadingHyphensEmptyString() {
String input = "";
String expected = "";
String actual = Util.stripLeadingHyphens(input);
assertEquals(expected, actual);
}

@Test
public void testStripLeadingHyphensNoLeadingHyphens() {
String input = "test";
String expected = "test";
String actual = Util.stripLeadingHyphens(input);
assertEquals(expected, actual);
}

@Test
public void testStripLeadingHyphensLeadingHyphensOtherChars() {
String input = "--test-other";
String expected = "test-other";
String actual = Util.stripLeadingHyphens(input);
assertEquals(expected, actual);
}

@Test
public void testStripLeadingHyphensLeadingHyphensSpaces() {
String input = "-- test";
String expected = " test";
String actual = Util.stripLeadingHyphens(input);
assertEquals(expected, actual);
}

@Test
public void testStripLeadingAndTrailingQuotes() {
String input = "\"test\"";
String expected = "test";
String actual = Util.stripLeadingAndTrailingQuotes(input);
assertEquals(expected, actual);
}

@Test
public void testStripLeadingAndTrailingQuotesEmptyString() {
String input = "";
String expected = "";
String actual = Util.stripLeadingAndTrailingQuotes(input);
assertEquals(expected, actual);
}

@Test
public void testStripLeadingAndTrailingQuotesNoQuotes() {
String input = "test";
String expected = "test";
String actual = Util.stripLeadingAndTrailingQuotes(input);
assertEquals(expected, actual);
}

@Test
public void testStripLeadingAndTrailingQuotesLeadingQuotes() {
String input = "\"test\"";
String expected = "test";
String actual = Util.stripLeadingAndTrailingQuotes(input);
assertEquals(expected, actual);
}

@Test
public void testStripLeadingAndTrailingQuotesTrailingQuotes() {
String input = "test\"";
String expected = "test";
String actual = Util.stripLeadingAndTrailingQuotes(input);
assertEquals(expected, actual);
}

@Test
public void testStripLeadingAndTrailingQuotesLeadingAndTrailingQuotes() {
String input = "\"test\"";
String expected = "test";
String actual = Util.stripLeadingAndTrailingQuotes(input);
assertEquals(expected, actual);
}

@Test
public void testStripLeadingAndTrailingQuotesLeadingQuotesMiddle() {
String input = "\"test\" other";
String expected = "test other";
String actual = Util.stripLeadingAndTrailingQuotes(input);
assertEquals(expected, actual);
}

@Test
public void testStripLeadingAndTrailingQuotesTrailingQuotesMiddle() {
String input = "test \"other\"";
String expected = "test other";
String actual = Util.stripLeadingAndTrailingQuotes(input);
assertEquals(expected, actual);
}

@Test(expected = IllegalArgumentException.class)
public void testStripLeadingAndTrailingQuotesNullString() {
String input = null;
try {
String actual = Util.stripLeadingAndTrailingQuotes(input);
} catch (IllegalArgumentException e) {
assertEquals("String contains null", e.getMessage());
}
}

}