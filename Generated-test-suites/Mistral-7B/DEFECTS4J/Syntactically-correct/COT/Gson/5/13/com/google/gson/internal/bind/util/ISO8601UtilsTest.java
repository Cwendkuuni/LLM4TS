package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {

	@Test
	public void testFormat_DateWithDefaultTimeZone() {
		Date date = new Date(123L);
		String formattedDate = ISO8601Utils.format(date);
		assertEquals("2000-01-01T00:00:00Z", formattedDate);
	}

	@Test
	public void testFormat_DateWithTimeZone() {
		Date date = new Date(123L);
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
		calendar.setTime(date);
		String formattedDate = ISO8601Utils.format(date, false, TimeZone.getTimeZone("America/New_York"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		String expectedFormattedDate = sdf.format(date);
		assertEquals(expectedFormattedDate, formattedDate);
	}

	@Test
	public void testFormat_DateWithMilliseconds() {
		Date date = new Date(123456789L);
		String formattedDate = ISO8601Utils.format(date, true, TimeZone.getDefault());
		assertEquals("2000-01-01T00:00:01.456Z", formattedDate);
	}

	@Test
	public void testParse_ValidDate() throws ParseException {
		String isoDate = "2000-01-01T00:00:01.456Z";
		Date parsedDate = ISO8601Utils.parse(isoDate, new ParsePosition(0));
		assertEquals(new Date(123456789L), parsedDate);
	}

	@Test(expected = ParseException.class)
	public void testParse_InvalidFormat() throws ParseException {
		String isoDate = "2000-01-01T00:00:01.456"; // missing Z
		ParsePosition pos = new ParsePosition(0);
		ISO8601Utils.parse(isoDate, pos);
	}

	@Test(expected = ParseException.class)
	public void testParse_InvalidTimeZone() throws ParseException {
		String isoDate = "2000-01-01T00:00:01.456-02:30"; // invalid timezone
		ParsePosition pos = new ParsePosition(0);
		ISO8601Utils.parse(isoDate, pos);
	}

	@Test
	public void testParse_DateWithMillisAndTimeZone() throws ParseException {
		String isoDate = "2000-01-01T00:00:01.456+02:30";
		Date parsedDate = ISO8601Utils.parse(isoDate, new ParsePosition(0));
		assertEquals(new Date(123456849L), parsedDate);
	}

	@Test
	public void testParse_DateWithoutMillis() throws ParseException {
		String isoDate = "2000-01-01T00:00:00Z";
		Date parsedDate = ISO8601Utils.parse(isoDate, new ParsePosition(0));
		assertEquals(new Date(0), parsedDate);
	}

	@Test
	public void testParse_DateWithoutTimeZone() throws ParseException {
		String isoDate = "2000-01-01T00:00:00";
		ParsePosition pos = new ParsePosition(0);
		assertNull(ISO8601Utils.parse(isoDate, pos));
	}
}