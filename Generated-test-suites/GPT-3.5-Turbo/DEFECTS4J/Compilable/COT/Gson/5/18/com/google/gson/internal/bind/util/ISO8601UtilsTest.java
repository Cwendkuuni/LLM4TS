package com.google.gson.internal.bind.util;
import org.junit.Assert;
import org.junit.Test;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;

public class ISO8601UtilsTest {

  @Test
  public void testFormat() {
    Date date = new Date();
    String formattedDate = ISO8601Utils.format(date);
    Assert.assertNotNull(formattedDate);
    // Add additional assertions to validate the format
  }

  @Test
  public void testFormatWithMillis() {
    Date date = new Date();
    boolean millis = true;
    String formattedDate = ISO8601Utils.format(date, millis);
    Assert.assertNotNull(formattedDate);
    // Add additional assertions to validate the format
  }

  @Test
  public void testFormatWithTimeZone() {
    Date date = new Date();
    boolean millis = false;
    TimeZone tz = TimeZone.getDefault();
    String formattedDate = ISO8601Utils.format(date, millis, tz);
    Assert.assertNotNull(formattedDate);
    // Add additional assertions to validate the format
  }

  @Test
  public void testFormatWithMillisAndTimeZone() {
    Date date = new Date();
    boolean millis = true;
    TimeZone tz = TimeZone.getDefault();
    String formattedDate = ISO8601Utils.format(date, millis, tz);
    Assert.assertNotNull(formattedDate);
    // Add additional assertions to validate the format
  }

  @Test
  public void testParse() throws ParseException {
    String dateString = "2019-12-31T23:59:59Z";
    ParsePosition pos = new ParsePosition(0);
    Date parsedDate = ISO8601Utils.parse(dateString, pos);
    Assert.assertNotNull(parsedDate);
    // Add additional assertions to validate the parsed date
  }

  @Test(expected = ParseException.class)
  public void testParseInvalidDate() throws ParseException {
    String dateString = "ABCD";
    ParsePosition pos = new ParsePosition(0);
    ISO8601Utils.parse(dateString, pos);
  }

}