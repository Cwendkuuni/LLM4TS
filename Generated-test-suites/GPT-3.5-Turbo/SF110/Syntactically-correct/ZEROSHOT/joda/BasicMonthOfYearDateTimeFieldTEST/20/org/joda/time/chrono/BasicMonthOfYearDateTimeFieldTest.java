package org.joda.time.chrono;

import org.joda.time.field.*;
import org.joda.time.*;
import org.junit.Test;
import org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

  @Test
  public void testIsLenient() {
    ImpreciseDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
    assertFalse(field.isLenient());
  }

  @Test
  public void testGet() {
    BasicChronology chronology = new BasicChronology();
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
    long n = 0L;
    int expected = 0;
    int actual = field.get(n);
    assertEquals(expected, actual);
  }

  @Test
  public void testAddInt() {
    BasicChronology chronology = new BasicChronology();
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
    long n = 0L;
    int n2 = 0;
    long expected = 0L;
    long actual = field.add(n, n2);
    assertEquals(expected, actual);
  }

  @Test
  public void testAddLong() {
    BasicChronology chronology = new BasicChronology();
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
    long n = 0L;
    long n2 = 0L;
    long expected = 0L;
    long actual = field.add(n, n2);
    assertEquals(expected, actual);
  }

  @Test
  public void testAddReadablePartialIntIntArrayInt() {
    BasicChronology chronology = new BasicChronology();
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
    ReadablePartial readablePartial = null;
    int n = 0;
    int[] array = new int[1];
    int n2 = 0;
    int[] expected = new int[1];
    int[] actual = field.add(readablePartial, n, array, n2);
    assertArrayEquals(expected, actual);
  }

  @Test
  public void testAddWrapField() {
    BasicChronology chronology = new BasicChronology();
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
    long n = 0L;
    int n2 = 0;
    long expected = 0L;
    long actual = field.addWrapField(n, n2);
    assertEquals(expected, actual);
  }

  @Test
  public void testGetDifferenceAsLong() {
    BasicChronology chronology = new BasicChronology();
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
    long n = 0L;
    long set = 0L;
    long expected = 0L;
    long actual = field.getDifferenceAsLong(n, set);
    assertEquals(expected, actual);
  }

  @Test
  public void testSet() {
    BasicChronology chronology = new BasicChronology();
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
    long n = 0L;
    int n2 = 0;
    long expected = 0L;
    long actual = field.set(n, n2);
    assertEquals(expected, actual);
  }

  @Test
  public void testGetRangeDurationField() {
    BasicChronology chronology = new BasicChronology();
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
    DurationField expected = null;
    DurationField actual = field.getRangeDurationField();
    assertEquals(expected, actual);
  }

  @Test
  public void testIsLeap() {
    BasicChronology chronology = new BasicChronology();
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
    long n = 0L;
    boolean expected = false;
    boolean actual = field.isLeap(n);
    assertEquals(expected, actual);
  }

  @Test
  public void testGetLeapAmount() {
    BasicChronology chronology = new BasicChronology();
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
    long n = 0L;
    int expected = 0;
    int actual = field.getLeapAmount(n);
    assertEquals(expected, actual);
  }

  @Test
  public void testGetLeapDurationField() {
    BasicChronology chronology = new BasicChronology();
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
    DurationField expected = null;
    DurationField actual = field.getLeapDurationField();
    assertEquals(expected, actual);
  }

  @Test
  public void testGetMinimumValue() {
    BasicChronology chronology = new BasicChronology();
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
    int expected = 1;
    int actual = field.getMinimumValue();
    assertEquals(expected, actual);
  }

  @Test
  public void testGetMaximumValue() {
    BasicChronology chronology = new BasicChronology();
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
    int expected = 0;
    int actual = field.getMaximumValue();
    assertEquals(expected, actual);
  }

  @Test
  public void testRoundFloor() {
    BasicChronology chronology = new BasicChronology();
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
    long n = 0L;
    long expected = 0L;
    long actual = field.roundFloor(n);
    assertEquals(expected, actual);
  }

  @Test
  public void testRemainder() {
    BasicChronology chronology = new BasicChronology();
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
    long n = 0L;
    long expected = 0L;
    long actual = field.remainder(n);
    assertEquals(expected, actual);
  }

  @Test
  public void testReadResolve() {
    BasicChronology chronology = new BasicChronology();
    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 0);
    Object expected = null;
    Object actual = field.readResolve();
    assertEquals(expected, actual);
  }
}