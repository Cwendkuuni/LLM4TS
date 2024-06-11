package com.google.gson.internal.bind.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;
import org.junit.Assert;
import org.junit.Test;

public class ISO8601UtilsTest {

   @Test
   public void testFormat() {
       Date date = new Date();
       String expected = ISO8601Utils.format(date, false, ISO8601Utils.TIMEZONE_UTC);
       String actual = ISO8601Utils.format(date);
       Assert.assertEquals(expected, actual);
   }

   @Test
   public void testFormatWithMillis() {
       Date date = new Date();
       String expected = ISO8601Utils.format(date, true, ISO8601Utils.TIMEZONE_UTC);
       String actual = ISO8601Utils.format(date, true);
       Assert.assertEquals(expected, actual);
   }

   @Test
   public void testFormatWithTimeZone() {
       Date date = new Date();
       TimeZone timeZone = TimeZone.getTimeZone("GMT+01:00");
       String expected = ISO8601Utils.format(date, false, timeZone);
       String actual = ISO8601Utils.format(date, false, timeZone);
       Assert.assertEquals(expected, actual);
   }

   @Test
   public void testParse() throws ParseException {
       String dateString = "2019-01-01";
       ParsePosition pos = new ParsePosition(0);
       Date expected = ISO8601Utils.parse(dateString, pos);
       Date actual = ISO8601Utils.parse(dateString, pos);
       Assert.assertEquals(expected, actual);
   }

   @Test(expected = ParseException.class)
   public void testParseInvalidDate() throws ParseException {
       String dateString = "9999-99-99";
       ParsePosition pos = new ParsePosition(0);
       ISO8601Utils.parse(dateString, pos);
   }

   @Test(expected = ParseException.class)
   public void testParseInvalidFormat() throws ParseException {
       String dateString = "2019-01-01T00:00:00";
       ParsePosition pos = new ParsePosition(0);
       ISO8601Utils.parse(dateString, pos);
   }

   @Test(expected = ParseException.class)
   public void testParseInvalidTimeZone() throws ParseException {
       String dateString = "2019-01-01T00:00:00+99:99";
       ParsePosition pos = new ParsePosition(0);
       ISO8601Utils.parse(dateString, pos);
   }

   @Test
   public void testCheckOffset() {
       String value = "2019-01-01T00:00:00";
       Assert.assertTrue(ISO8601Utils.checkOffset(value, 10, 'T'));
       Assert.assertFalse(ISO8601Utils.checkOffset(value, 10, 'A'));
   }

   @Test
   public void testPaddingInt() {
       StringBuilder buffer = new StringBuilder();
       ISO8601Utils.padInt(buffer, 1, 2);
       Assert.assertEquals("01", buffer.toString());
   }

   @Test
   public void testIndexOfNonDigit() {
       String string = "2019-01-01T00:00:00";
       int offset = 10;
       int expected = string.length();
       int actual = ISO8601Utils.indexOfNonDigit(string, offset);
       Assert.assertEquals(expected, actual);
   }
}