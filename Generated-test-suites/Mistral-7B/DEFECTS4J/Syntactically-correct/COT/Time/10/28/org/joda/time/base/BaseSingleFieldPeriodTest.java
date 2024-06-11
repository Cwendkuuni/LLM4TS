package org.joda.time.base;

import org.junit.Test;
import org.junit.Assert.*;

import java.io.Serializable;
import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.Chronology;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;

public class BaseSingleFieldPeriodTest {

  @Test
  public void testBaseSingleFieldPeriodConstructor() {
    BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
    assertEquals(10, period.getValue());
  }

  @Test
  public void testGetFieldType() {
    BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
      @Override
      public DurationFieldType getFieldType() {
        return DurationFieldType.DAYS;
      }
    };
    assertEquals(DurationFieldType.DAYS, period.getFieldType());
  }

  @Test
  public void testGetPeriodType() {
    BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
      @Override
      public PeriodType getPeriodType() {
        return PeriodType.standardInstantly();
      }
    };
    PeriodType periodType = period.getPeriodType();
    assertTrue(periodType instanceof PeriodType);
  }

  @Test
  public void testSize() {
    BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
      @Override
      public int size() {
        return 2;
      }
    };
    assertEquals(1, period.size());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testGetValueOutOfBounds() {
    BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
    period.getValue(1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBetweenWithInvalidStart() {
    BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
    ReadableInstant start = null;
    ReadableInstant end = DateTimeUtils.currentTime().withMillisAdded(1000);
    DurationFieldType field = DurationFieldType.DAYS;
    try {
      BaseSingleFieldPeriod.between(start, end, field);
      fail();
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBetweenWithInvalidEnd() {
    BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
    ReadableInstant start = DateTimeUtils.currentTime().minusMillis(1000);
    ReadableInstant end = null;
    DurationFieldType field = DurationFieldType.DAYS;
    try {
      BaseSingleFieldPeriod.between(start, end, field);
      fail();
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test
  public void testBetween() {
    ReadableInstant start = DateTimeUtils.currentTime().minusMillis(1000);
    ReadableInstant end = DateTimeUtils.currentTime().plusMillis(1000);
    BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(BaseSingleFieldPeriod.between(start, end, DurationFieldType.MILLISECONDS));
    assertEquals(0, period.getValue());
  }

  @Test
  public void testStandardPeriodIn() {
    ReadablePeriod period = Period.parse("PT3S");
    long millisPerUnit = 1000;
    int expected = 4;
    int actual = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
    assertEquals(expected, actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStandardPeriodInWithImprecisePeriod() {
    ReadablePeriod period = Period.parse("P1Y");
    long millisPerUnit = 1000;
    BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);
    fail();
  }
}