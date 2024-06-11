
package org.joda.time.base;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {
  
  @Test
  public void testBetweenReadableInstant() {
    LocalDateTime dateTime1 = new LocalDateTime(2022, 1, 1, 0, 0, 0);
    LocalDateTime dateTime2 = new LocalDateTime(2022, 2, 1, 0, 0, 0);
    DurationFieldType fieldType = DurationFieldType.days();
  
    int result = BaseSingleFieldPeriod.between(dateTime1, dateTime2, fieldType);
  
    assertEquals(31, result);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testBetweenReadableInstantWithNullDateTime1() {
    LocalDateTime dateTime1 = null;
    LocalDateTime dateTime2 = new LocalDateTime(2022, 2, 1, 0, 0, 0);
    DurationFieldType fieldType = DurationFieldType.days();
  
    BaseSingleFieldPeriod.between(dateTime1, dateTime2, fieldType);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testBetweenReadableInstantWithNullDateTime2() {
    LocalDateTime dateTime1 = new LocalDateTime(2022, 1, 1, 0, 0, 0);
    LocalDateTime dateTime2 = null;
    DurationFieldType fieldType = DurationFieldType.days();
  
    BaseSingleFieldPeriod.between(dateTime1, dateTime2, fieldType);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testBetweenReadableInstantWithNullFieldType() {
    LocalDateTime dateTime1 = new LocalDateTime(2022, 1, 1, 0, 0, 0);
    LocalDateTime dateTime2 = new LocalDateTime(2022, 2, 1, 0, 0, 0);
    DurationFieldType fieldType = null;
  
    BaseSingleFieldPeriod.between(dateTime1, dateTime2, fieldType);
  }
  
  @Test
  public void testBetweenReadablePartial() {
    LocalDate date1 = new LocalDate(2022, 1, 1);
    LocalDate date2 = new LocalDate(2022, 2, 1);
    DurationFieldType fieldType = DurationFieldType.days();
  
    int result = BaseSingleFieldPeriod.between(date1, date2, fieldType);
  
    assertEquals(31, result);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testBetweenReadablePartialWithNullPartial1() {
    LocalDate date1 = null;
    LocalDate date2 = new LocalDate(2022, 2, 1);
    DurationFieldType fieldType = DurationFieldType.days();
  
    BaseSingleFieldPeriod.between(date1, date2, fieldType);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testBetweenReadablePartialWithNullPartial2() {
    LocalDate date1 = new LocalDate(2022, 1, 1);
    LocalDate date2 = null;
    DurationFieldType fieldType = DurationFieldType.days();
  
    BaseSingleFieldPeriod.between(date1, date2, fieldType);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testBetweenReadablePartialWithDifferentSizePartials() {
    LocalDate date1 = new LocalDate(2022, 1, 1);
    LocalDateTime dateTime2 = new LocalDateTime(2022, 2, 1, 0, 0, 0);
    DurationFieldType fieldType = DurationFieldType.days();
  
    BaseSingleFieldPeriod.between(date1, dateTime2, fieldType);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testBetweenReadablePartialWithDifferentFieldTypes() {
    LocalDateTime dateTime1 = new LocalDateTime(2022, 1, 1, 0, 0, 0);
    LocalDateTime dateTime2 = new LocalDateTime(2022, 2, 1, 0, 0, 0);
    DurationFieldType fieldType1 = DurationFieldType.days();
    DurationFieldType fieldType2 = DurationFieldType.hours();
  
    BaseSingleFieldPeriod.between(dateTime1, dateTime2, fieldType1, fieldType2);
  }
  
  @Test
  public void testStandardPeriodIn() {
    Period period = Period.hours(25);
  
    int result = BaseSingleFieldPeriod.standardPeriodIn(period, 1);
  
    assertEquals(25, result);
  }
  
  @Test
  public void testStandardPeriodInWithNullPeriod() {
    Period period = null;
  
    int result = BaseSingleFieldPeriod.standardPeriodIn(period, 1);
  
    assertEquals(0, result);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testStandardPeriodInWithUnprecisePeriod() {
    Period period = Period.months(1);
  
    BaseSingleFieldPeriod.standardPeriodIn(period, 1);
  }
  
}
