package org.apache.commons.cli;
import org.apache.commons.cli.Util;
import org.junit.Assert;
import org.junit.Test;

public class UtilTest {

 @Test
 public void testStripLeadingHyphens() {
 String str = "--Test-Case";
 String expected = "Test-Case";
 String result = Util.stripLeadingHyphens(str);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testStripLeadingHyphens_EmptyString() {
 String str = "";
 String result = Util.stripLeadingHyphens(str);
 Assert.assertEquals(str, result);
 }

 @Test
 public void testStripLeadingHyphens_NoLeadingHyphens() {
 String str = "Test-Case";
 String result = Util.stripLeadingHyphens(str);
 Assert.assertEquals(str, result);
 }

 @Test
 public void testStripLeadingQuotes() {
 String str = "\"Test-Case\"";
 String expected = "Test-Case";
 String result = Util.stripLeadingAndTrailingQuotes(str);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testStripLeadingQuotes_NoLeadingQuotes() {
 String str = "Test-Case";
 String result = Util.stripLeadingAndTrailingQuotes(str);
 Assert.assertEquals(str, result);
 }

 @Test
 public void testStripLeadingQuotes_TrailingQuotes() {
 String str = "Test-Case\"";
 String expected = "Test-Case";
 String result = Util.stripLeadingAndTrailingQuotes(str);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testStripLeadingQuotes_LeadingAndTrailingQuotes() {
 String str = "\"Test-Case\"";
 String expected = "Test-Case";
 String result = Util.stripLeadingAndTrailingQuotes(str);
 Assert.assertEquals(expected, result);
 }
}