package org.joda.time.chrono;

import org.junit.Before;
import org.junit.Test;
import org.joda.time.*;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

  private BasicMonthOfYearDateTimeField field;

  @Before
  public void setUp() {
    BasicChronology chronology = new BasicChronology();
    int leapMonth = 6;
    field = new BasicMonthOfYearDateTimeField(chronology, leapMonth);
  }

  @Test
  public void testIsLenient() {
    assertFalse(field.isLenient());
  }

  @Test
  public void testGet() {
    long n = 1580937600000L; // January 6, 2020
    assertEquals(1, field.get(n));
  }

  @Test
  public void testAddWithZeroAmount() {
    long n = 1580937600000L; // January 6, 2020
    assertEquals(n, field.add(n, 0));
  }

  @Test
  public void testAddWithNegativeAmount() {
    long n = 1580937600000L; // January 6, 2020
    long expected = 1578268800000L; // December 6, 2019
    assertEquals(expected, field.add(n, -1));
  }

  @Test
  public void testAddWithPositiveAmount() {
    long n = 1580937600000L; // January 6, 2020
    long expected = 1583193600000L; // February 6, 2020
    assertEquals(expected, field.add(n, 1));
  }

  @Test
  public void testAddWithLargePositiveAmount() {
    long n = 1580937600000L; // January 6, 2020
    long expected = 1596172800000L; // July 6, 2020
    assertEquals(expected, field.add(n, 6));
  }

  @Test
  public void testAddWithLargeNegativeAmount() {
    long n = 1580937600000L; // January 6, 2020
    long expected = 1564752000000L; // July 6, 2019
    assertEquals(expected, field.add(n, -6));
  }

  @Test
  public void testAddWrapFieldWithPositiveAmount() {
    long n = 1580937600000L; // January 6, 2020
    long expected = 1588281600000L; // January 6, 2021
    assertEquals(expected, field.addWrapField(n, 1));
  }

  @Test
  public void testAddWrapFieldWithNegativeAmount() {
    long n = 1580937600000L; // January 6, 2020
    long expected = 1573593600000L; // January 6, 2019
    assertEquals(expected, field.addWrapField(n, -1));
  }

  @Test
  public void testGetDifferenceAsLong() {
    long n1 = 1580937600000L; // January 6, 2020
    long n2 = 1577836800000L; // January 1, 2020
    long expected = 5;
    assertEquals(expected, field.getDifferenceAsLong(n1, n2));
  }

  @Test
  public void testIsLeapWithLeapYearAndLeapMonth() {
    long n = 1580937600000L; // January 6, 2020
    assertTrue(field.isLeap(n));
  }

  @Test
  public void testIsLeapWithLeapYearAndNonLeapMonth() {
    long n = 1580937600000L; // January 6, 2020
    assertFalse(field.isLeap(n + 3600000)); // February 6, 2020
  }

  @Test
  public void testGetLeapAmountWithLeapYearAndLeapMonth() {
    long n = 1580937600000L; // January 6, 2020
    int expected = 1;
    assertEquals(expected, field.getLeapAmount(n));
  }

  @Test
  public void testGetLeapAmountWithLeapYearAndNonLeapMonth() {
    long n = 1580937600000L; // January 6, 2020
    int expected = 0;
    assertEquals(expected, field.getLeapAmount(n + 3600000)); // February 6, 2020
  }

  @Test
  public void testGetMinimumValue() {
    int expected = 1;
    assertEquals(expected, field.getMinimumValue());
  }

  @Test
  public void testGetMaximumValue() {
    int expected = 12;
    assertEquals(expected, field.getMaximumValue());
  }
}