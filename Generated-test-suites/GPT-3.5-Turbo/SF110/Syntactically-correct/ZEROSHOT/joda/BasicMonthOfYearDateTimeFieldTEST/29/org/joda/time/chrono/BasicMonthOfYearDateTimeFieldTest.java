package org.joda.time.chrono;

import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.field.FieldUtils;
import org.joda.time.*;

public class BasicMonthOfYearDateTimeFieldTest {

	@Test
	public void testIsLenient() {
		BasicChronology chronology = new BasicChronology();
		BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
		assertFalse(field.isLenient());
	}

	@Test
	public void testGet() {
		BasicChronology chronology = new BasicChronology();
		BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
		long n = 1000;
		assertEquals(1, field.get(n));
	}

	@Test
	public void testAddLongInt() {
		BasicChronology chronology = new BasicChronology();
		BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
		long n = 1000;
		int n2 = 2;
		assertEquals(1000, field.add(n, 0));
	}

	@Test
	public void testAddLongLong() {
		BasicChronology chronology = new BasicChronology();
		BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
		long n = 1000;
		long n2 = 2L;
		assertEquals(1000, field.add(n, 0));
	}

	@Test
	public void testAddReadablePartialIntArrayInt() {
		BasicChronology chronology = new BasicChronology();
		BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
		ReadablePartial readablePartial = new ReadablePartial();
		int n = 0;
		int[] array = new int[0];
		int n2 = 0;
		assertArrayEquals(array, field.add(readablePartial, n, array, n2));
	}

	@Test
	public void testAddWrapField() {
		BasicChronology chronology = new BasicChronology();
		BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
		long n = 1000;
		int n2 = 2;
		assertEquals(1000, field.addWrapField(n, 0));
	}

	@Test
	public void testGetDifferenceAsLong() {
		BasicChronology chronology = new BasicChronology();
		BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
		long n = 1000L;
		long set = 2000L;
		assertEquals(-0, field.getDifferenceAsLong(n, set));
	}

	@Test
	public void testSet() {
		BasicChronology chronology = new BasicChronology();
		BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
		long n = 1000;
		int n2 = 2;
		assertEquals(1000, field.set(n, n2));
	}

	@Test
	public void testGetRangeDurationField() {
		BasicChronology chronology = new BasicChronology();
		BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
		assertNotNull(field.getRangeDurationField());
	}

	@Test
	public void testIsLeap() {
		BasicChronology chronology = new BasicChronology();
		BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
		long n = 1000;
		assertTrue(field.isLeap(n));
	}

	@Test
	public void testGetLeapAmount() {
		BasicChronology chronology = new BasicChronology();
		BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
		long n = 1000;
		assertEquals(1, field.getLeapAmount(n));
	}

	@Test
	public void testGetLeapDurationField() {
		BasicChronology chronology = new BasicChronology();
		BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
		assertNotNull(field.getLeapDurationField());
	}

	@Test
	public void testGetMinimumValue() {
		BasicChronology chronology = new BasicChronology();
		BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
		assertEquals(1, field.getMinimumValue());
	}

	@Test
	public void testGetMaximumValue() {
		BasicChronology chronology = new BasicChronology();
		BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
		assertEquals(1, field.getMaximumValue());
	}

	@Test
	public void testRoundFloor() {
		BasicChronology chronology = new BasicChronology();
		BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
		long n = 1000;
		assertEquals(1000, field.roundFloor(n));
	}

	@Test
	public void testRemainder() {
		BasicChronology chronology = new BasicChronology();
		BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
		long n = 1000;
		assertEquals(0, field.remainder(n));
	}

	@Test
	public void testReadResolve() {
		BasicChronology chronology = new BasicChronology();
		BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 1);
		assertNotNull(field.readResolve());
	}

}