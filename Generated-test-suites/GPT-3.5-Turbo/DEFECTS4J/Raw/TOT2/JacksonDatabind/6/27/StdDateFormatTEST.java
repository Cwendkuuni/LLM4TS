package com.fasterxml.jackson.databind.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class StdDateFormatTest {

	@Test
	public void testParse() {
		StdDateFormat stdDateFormat = new StdDateFormat();

		try {
			assertNotNull(stdDateFormat.parse("2021-12-01T13:30:00.000Z"));
			assertNotNull(stdDateFormat.parse("2021-12-01"));
			assertNotNull(stdDateFormat.parse("Thu, 01 Dec 2021 13:30:00 GMT"));

			assertNull(stdDateFormat.parse("Invalid Date"));
			assertNull(stdDateFormat.parse("2021-12-01T13:30:00")); // Missing timezone
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}