package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.core.io.NumberInput;

/**
 * Test class for StdDateFormat class.
 */
public class StdDateFormatTest {

	/**
	 * Test constructor.
	 */
	@Test
	public void testConstructor() {
		StdDateFormat dateFormat = new StdDateFormat();
		assertNotNull(dateFormat);
		assertEquals(Locale.US, dateFormat.getLocale());
		assertEquals(TimeZone.getDefault(), dateFormat.getTimeZone());

		StdDateFormat dateFormatWithTimeZone = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.US);
		assertEquals(Locale.US, dateFormatWithTimeZone.getLocale());
		assertEquals(TimeZone.getTimeZone("UTC"), dateFormatWithTimeZone.getTimeZone());

		StdDateFormat dateFormatWithLocale = new StdDateFormat(TimeZone.getTimeZone("UTC"), Locale.FRANCE);
		assertEquals(Locale.FRANCE, dateFormatWithLocale.getLocale());
		assertEquals(TimeZone.getTimeZone("UTC"), dateFormatWithLocale.getTimeZone());
	}

	/**
	 * Test getInstance method.
	 */
	@Test
	public void testGetInstance() {
		DateFormat dateFormat = StdDateFormat.getInstance();
		assertEquals(Locale.US, dateFormat.getLocale());
		assertEquals(TimeZone.getDefault(), dateFormat.getTimeZone());

		DateFormat dateFormatWithTimeZone = StdDateFormat.getInstance(Locale.FRANCE, TimeZone.getTimeZone("UTC"));
		assertEquals(Locale.FRANCE, dateFormatWithTimeZone.getLocale());
		assertEquals(TimeZone.getTimeZone("UTC"), dateFormatWithTimeZone.getTimeZone());
	}

	/**
	 * Test parse method with valid ISO-8601 dates.
	 */
	@Test
	public void testParseISO8601() {
		StdDateFormat dateFormat = new StdDateFormat();

		Date date = dateFormat.parse("2022-12-31T23:59:59.123Z");
		assertEquals("Fri Dec 31 23:59:59 IST 2022", dateFormat.format(date));
		assertEquals(0L, date.getTime());

		date = dateFormat.parse("2022-12-31T23:59:59.123+01:00");
		assertEquals("Sat Jan 01 00:59:59 CET 2023", dateFormat.format(date));
		assertEquals(60 * 60 * 1000L + 0L, date.getTime());

		date = dateFormat.parse("2022-12-31 23:59:59.123Z");
		assertEquals("Fri Dec 31 23:59:59 IST 2022", dateFormat.format(date));
		assertEquals(0L, date.getTime());
	}

	/**
	 * Test parse method with invalid ISO-8601 dates.
	 */
	@Test(expected = ParseException.class)
	public void testParseInvalidISO8601() {
		StdDateFormat dateFormat = new StdDateFormat();
		dateFormat.parse("2022-12-31T23:59:59.123A");
	}

	/**
	 * Test parse method with RFC-1123 dates.
	 */
	@Test
	public void testParseRFC1123() {
		StdDateFormat dateFormat = new StdDateFormat();

		Date date = dateFormat.parse("Fri, 31 Dec 2022 23:59:59 GMT");
		assertEquals("Fri Dec 31 23:59:59 IST 2022", dateFormat.format(date));
		assertEquals(0L, date.getTime());

		date = dateFormat.parse("Sat, 01 Jan 2023 00:59:59 CET");
		assertEquals("Sat Jan 01 00:59:59 CET 2023", dateFormat.format(date));
		assertEquals(60 * 60 * 1000L + 0L, date.getTime());
	}

	/**
	 * Test parse method with negative numbers.
	 */
	@Test
	public void testParseNegativeNumbers() {
		StdDateFormat dateFormat = new StdDateFormat();

		Date date = dateFormat.parse("-1234567890");
		assertEquals("Wed Jan 01 1970 02:57:33 IST", dateFormat.format(date));
		assertEquals(-1234567890000L, date.getTime());
	}

	/**
	 * Test parse method with plain dates.
	 */
	@Test
	public void testParsePlainDates() {
		StdDateFormat dateFormat = new StdDateFormat();

		Date date = dateFormat.parse("2022-12-31");
		assertEquals("Fri Dec 31 00:00:00 IST 2022", dateFormat.format(date));
		assertEquals(0L, date.getTime());

		date = dateFormat.parse("2022-12-31 12:34:56");
		assertEquals("Fri Dec 31 12:34:56 IST 2022", dateFormat.format(date));
		assertEquals(12 * 60 * 60 * 1000L + 34 * 60 * 1000L + 56 * 1000L, date.getTime());
	}

	/**
	 * Test format method.
	 */
	@Test
	public void testFormat() {
		StdDateFormat dateFormat = new StdDateFormat();

		Date date = new Date(0L);
		assertEquals("Fri Jan 01 00:00:00 IST 1970", dateFormat.format(date));

		date = new Date(60 * 60 * 1000L);
		assertEquals("Fri Jan 01 01:00:00 IST 1970", dateFormat.format(date));

		date = new Date(60 * 60 * 1000L + 30 * 60 * 1000L);
		assertEquals("Fri Jan 01 02:30:00 IST 1970", dateFormat.format(date));
	}

	/**
	 * Test toString method.
	 */
	@Test
	public void testToString() {
		StdDateFormat dateFormat = new StdDateFormat();
		assertEquals("DateFormat com.fasterxml.jackson.databind.util.StdDateFormat (timezone: sun.util.calendar.ZoneInfo[id=" +
				"sun.util.calendar.ZonedId[id=\"America/New_York\",offset=0,rules=null],offset=0,dbcs=false,isDaylightSaveTime=false,usesDaylightSaveTime=false,isLenient=true,hasSameDaylightSaveTime=true,supportsTimeZone=true,usesUTC=false]",
				dateFormat.toString());
	}

	/**
	 * Test cloning.
	 */
	@Test
	public void testClone() {
		StdDateFormat dateFormat = new StdDateFormat();
		DateFormat clonedDateFormat = dateFormat.clone();

		assertEquals(dateFormat.getLocale(), clonedDateFormat.getLocale());
		assertEquals(dateFormat.getTimeZone(), clonedDateFormat.getTimeZone());
		assertEquals(dateFormat.getClass(), clonedDateFormat.getClass());
	}

	/**
	 * Test withTimeZone method.
	 */
	@Test
	public void testWithTimeZone() {
		StdDateFormat dateFormat = new StdDateFormat();
		DateFormat dateFormatWithTimeZone = dateFormat.withTimeZone(TimeZone.getTimeZone("UTC"));

		assertEquals(dateFormat.getLocale(), dateFormatWithTimeZone.getLocale());
		assertEquals(TimeZone.getTimeZone("UTC"), dateFormatWithTimeZone.getTimeZone());
	}

	/**
	 * Test withLocale method.
	 */
	@Test
	public void testWithLocale() {
		StdDateFormat dateFormat = new StdDateFormat();
		DateFormat dateFormatWithLocale = dateFormat.withLocale(Locale.FRANCE);

		assertEquals(Locale.FRANCE, dateFormatWithLocale.getLocale());
		assertEquals(dateFormat.getTimeZone(), dateFormatWithLocale.getTimeZone());
	}
}