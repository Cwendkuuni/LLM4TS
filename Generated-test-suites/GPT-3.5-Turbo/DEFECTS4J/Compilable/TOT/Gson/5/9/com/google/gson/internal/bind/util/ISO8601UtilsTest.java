package com.google.gson.internal.bind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

public class ISO8601UtilsTest {
	
	@Test
	public void testFormat_noMillis() {
		Date date = new GregorianCalendar(2020, Calendar.DECEMBER, 1, 12, 30, 0).getTime();
		String expected = "2020-12-01T12:30:00Z";
		String actual = ISO8601Utils.format(date);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testFormat_withMillis() {
		Date date = new GregorianCalendar(2020, Calendar.DECEMBER, 1, 12, 30, 0).getTime();
		String expected = "2020-12-01T12:30:00.000Z";
		String actual = ISO8601Utils.format(date, true);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testFormat_withTimezone() {
		Date date = new GregorianCalendar(2020, Calendar.DECEMBER, 1, 12, 30, 0).getTime();
		String expected = "2020-12-01T12:30:00+00:00";
		String actual = ISO8601Utils.format(date, false, TimeZone.getTimeZone("GMT+00:00"));
		assertEquals(expected, actual);
	}
	
	@Test
	public void testParse_validDateString() throws ParseException {
		String dateString = "2020-12-01T12:30:00Z";
		ParsePosition pos = new ParsePosition(0);
		Date expected = new GregorianCalendar(2020, Calendar.DECEMBER, 1, 12, 30, 0).getTime();
		Date actual = ISO8601Utils.parse(dateString, pos);
		assertNotNull(actual);
		assertEquals(expected, actual);
	}
	
	@Test(expected = ParseException.class)
	public void testParse_invalidDateString() throws ParseException {
		String dateString = "2020-12-01T12:30:00ZZ";
		ParsePosition pos = new ParsePosition(0);
		ISO8601Utils.parse(dateString, pos);
	}
	
	@Test
	public void testParse_withOffset() throws ParseException {
		String dateString = "2020-12-01T12:30:00+01:00";
		ParsePosition pos = new ParsePosition(0);
		Date expected = new GregorianCalendar(2020, Calendar.DECEMBER, 1, 12, 30, 0).getTime();
		Date actual = ISO8601Utils.parse(dateString, pos);
		assertNotNull(actual);
		assertEquals(expected, actual);
	}
}
