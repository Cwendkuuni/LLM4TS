package org.joda.time.base;

import org.joda.time.DateTime;
import org.joda.time.DurationFieldType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.UnsupportedDurationField;

import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    @Test(expected = IllegalArgumentException.class)
    public void between_NullReadableInstant_ThrowsException() {
        BaseSingleFieldPeriod.between(null, DateTime.now(), DurationFieldType.millis());
    }

    @Test(expected = IllegalArgumentException.class)
    public void between_NullReadableInstant2_ThrowsException() {
        BaseSingleFieldPeriod.between(DateTime.now(), null, DurationFieldType.millis());
    }

    @Test
    public void between_ValidParameters_CorrectResult() {
        assertEquals(1, BaseSingleFieldPeriod.between(DateTime.now(), DateTime.now().plusDays(1), DurationFieldType.days()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void between_NullReadablePartial_ThrowsException() {
        BaseSingleFieldPeriod.between(null, new DateTime(), DurationFieldType.millis());
    }

    @Test(expected = IllegalArgumentException.class)
    public void between_NullReadablePartial2_ThrowsException() {
        BaseSingleFieldPeriod.between(new DateTime(), null, DurationFieldType.millis());
    }

    @Test(expected = IllegalArgumentException.class)
    public void between_ReadablePartialSizeMismatch_ThrowsException() {
        ReadablePartial rp1 = new MockReadablePartial();
        ReadablePartial rp2 = new MockReadablePartial();

        BaseSingleFieldPeriod.between(rp1, rp2, DurationFieldType.millis());
    }

    @Test(expected = IllegalArgumentException.class)
    public void between_ReadablePartialFieldMismatch_ThrowsException() {
        ReadablePartial rp1 = new MockReadablePartial();
        ReadablePartial rp2 = new MockReadablePartial();

        BaseSingleFieldPeriod.between(rp1, rp2, DurationFieldType.seconds());
    }

    @Test(expected = IllegalArgumentException.class)
    public void between_ReadablePartialNonContiguous_ThrowsException() {
        ReadablePartial rp1 = new MockNonContiguousReadablePartial();
        ReadablePartial rp2 = new MockNonContiguousReadablePartial();

        BaseSingleFieldPeriod.between(rp1, rp2, DurationFieldType.millis());
    }

    @Test
    public void between_ValidParameters_CorrectResult2() {
        ReadablePartial rp1 = new MockReadablePartial();
        ReadablePartial rp2 = new MockReadablePartial();

        assertEquals(1, BaseSingleFieldPeriod.between(rp1, rp2, DurationFieldType.millis()));
    }

    @Test
    public void standardPeriodIn_NullReadablePeriod_ReturnsZero() {
        assertEquals(0, BaseSingleFieldPeriod.standardPeriodIn(null, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void standardPeriodIn_InvalidFieldInPeriod_ThrowsException() {
        ReadablePeriod rp = new MockInvalidFieldReadablePeriod();
        BaseSingleFieldPeriod.standardPeriodIn(rp, 1);
    }

    @Test
    public void standardPeriodIn_ValidParameters_ReturnsCorrectResult() {
        ReadablePeriod rp = new MockValidReadablePeriod();
        assertEquals(2, BaseSingleFieldPeriod.standardPeriodIn(rp, 1000));
    }
    
    @Test
    public void BaseSingleFieldPeriod_Constructor_CorrectValue() {
      BaseSingleFieldPeriod period = new MockBaseSingleFieldPeriod(5);
      assertEquals(5, period.getValue());
    }

    @Test
    public void BaseSingleFieldPeriod_setValue_CorrectValue() {
      BaseSingleFieldPeriod period = new MockBaseSingleFieldPeriod(5);
      period.setValue(10);
      assertEquals(10, period.getValue());
    }
    
    @Test
    public void getFieldType_CorrectFieldTypeReturned() {
      BaseSingleFieldPeriod period = new MockBaseSingleFieldPeriod(1);
      assertEquals(DurationFieldType.millis(), period.getFieldType());
    }
    
    @Test
    public void getPeriodType_CorrectPeriodTypeReturned() {
      BaseSingleFieldPeriod period = new MockBaseSingleFieldPeriod(1);
      assertEquals(PeriodType.yearMonthDayTime(), period.getPeriodType());
    }
    
    @Test
    public void size_AlwaysOne() {
      BaseSingleFieldPeriod period = new MockBaseSingleFieldPeriod(1);
      assertEquals(1, period.size());
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void getFieldType_OutOfBoundsIndex_ThrowsException() {
      BaseSingleFieldPeriod period = new MockBaseSingleFieldPeriod(1);
      period.getFieldType(1);
    }
    
    @Test
    public void getFieldType_ValidIndex_CorrectFieldTypeReturned() {
      BaseSingleFieldPeriod period = new MockBaseSingleFieldPeriod(1);
      assertEquals(DurationFieldType.millis(), period.getFieldType(0));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void getValue_OutOfBoundsIndex_ThrowsException() {
      BaseSingleFieldPeriod period = new MockBaseSingleFieldPeriod(1);
      period.getValue(1);
    }
    
    @Test
    public void getValue_ValidIndex_CorrectValueReturned() {
      BaseSingleFieldPeriod period = new MockBaseSingleFieldPeriod(1);
      assertEquals(1, period.getValue(0));
    }
    
    @Test
    public void get_CorrectFieldValueReturned() {
      BaseSingleFieldPeriod period = new MockBaseSingleFieldPeriod(1);
      assertEquals(1, period.get(DurationFieldType.millis()));
    }
    
    @Test
    public void isSupported_ValidFieldType_ReturnsTrue() {
      BaseSingleFieldPeriod period = new MockBaseSingleFieldPeriod(1);
      assertTrue(period.isSupported(DurationFieldType.millis()));
    }
  
    @Test
    public void isSupported_InvalidFieldType_ReturnsFalse() {
      BaseSingleFieldPeriod period = new MockBaseSingleFieldPeriod(1);
      assertFalse(period.isSupported(DurationFieldType.seconds()));
    }
  
    @Test
    public void toPeriod_ReturnsCorrectPeriod() {
      BaseSingleFieldPeriod period = new MockBaseSingleFieldPeriod(1);
      Period expectedPeriod = Period.years(1);
      Period actualPeriod = period.toPeriod();
      assertEquals(expectedPeriod, actualPeriod);
    }
  
    @Test
    public void toMutablePeriod_ReturnsCorrectMutablePeriod() {
      BaseSingleFieldPeriod period = new MockBaseSingleFieldPeriod(1);
      MutablePeriod expectedPeriod = new MutablePeriod().years(1);
      MutablePeriod actualPeriod = period.toMutablePeriod();
      assertEquals(expectedPeriod, actualPeriod);
    }
  
    @Test
    public void equals_SameInstance_ReturnsTrue() {
      BaseSingleFieldPeriod period = new MockBaseSingleFieldPeriod(1);
      assertTrue(period.equals(period));
    }
  
    @Test
    public void equals_NullObject_ReturnsFalse() {
      BaseSingleFieldPeriod period = new MockBaseSingleFieldPeriod(1);
      assertFalse(period.equals(null));
    }
    
    @Test
    public void equals_SameValue_ReturnsTrue() {
      BaseSingleFieldPeriod period1 = new MockBaseSingleFieldPeriod(1);
      BaseSingleFieldPeriod period2 = new MockBaseSingleFieldPeriod(1);
      assertTrue(period1.equals(period2));
    }
  
    @Test
    public void equals_DifferentValue_ReturnsFalse() {
      BaseSingleFieldPeriod period1 = new MockBaseSingleFieldPeriod(1);
      BaseSingleFieldPeriod period2 = new MockBaseSingleFieldPeriod(2);
      assertFalse(period1.equals(period2));
    }
  
    @Test
    public void equals_DifferentClass_ReturnsFalse() {
      BaseSingleFieldPeriod period = new MockBaseSingleFieldPeriod(1);
      assertFalse(period.equals(new DateTime()));
    }
  
    @Test
    public void hashCode_SameInstance_ReturnsSameHashcode() {
      BaseSingleFieldPeriod period = new MockBaseSingleFieldPeriod(1);
      assertEquals(period.hashCode(), period.hashCode());
    }
  
    @Test
    public void hashCode_SameValue_ReturnsSameHashcode() {
      BaseSingleFieldPeriod period1 = new MockBaseSingleFieldPeriod(1);
      BaseSingleFieldPeriod period2 = new MockBaseSingleFieldPeriod(1);
      assertEquals(period1.hashCode(), period2.hashCode());
    }
  
    @Test
    public void hashCode_DifferentValue_ReturnsDifferentHashcode() {
      BaseSingleFieldPeriod period1 = new MockBaseSingleFieldPeriod(1);
      BaseSingleFieldPeriod period2 = new MockBaseSingleFieldPeriod(2);
      assertNotEquals(period1.hashCode(), period2.hashCode());
    }
  
    @Test(expected = ClassCastException.class)
    public void compareTo_DifferentClass_ThrowsException() {
      BaseSingleFieldPeriod period1 = new MockBaseSingleFieldPeriod(1);
      BaseSingleFieldPeriod period2 = new MockBaseSingleFieldPeriod(2);
      period1.compareTo(new DateTime());
    }
    
    @Test
    public void compareTo_SameValue_ReturnsZero() {
      BaseSingleFieldPeriod period1 = new MockBaseSingleFieldPeriod(1);
      BaseSingleFieldPeriod period2 = new MockBaseSingleFieldPeriod(1);
      assertEquals(0, period1.compareTo(period2));
    }
  
    @Test
    public void compareTo_LesserValue_ReturnsNegative() {
      BaseSingleFieldPeriod period1 = new MockBaseSingleFieldPeriod(1);
      BaseSingleFieldPeriod period2 = new MockBaseSingleFieldPeriod(2);
      assertTrue(period1.compareTo(period2) < 0);
    }
  
    @Test
    public void compareTo_GreaterValue_ReturnsPositive() {
      BaseSingleFieldPeriod period1 = new MockBaseSingleFieldPeriod(2);
      BaseSingleFieldPeriod period2 = new MockBaseSingleFieldPeriod(1);
      assertTrue(period1.compareTo(period2) > 0);
    }
}

class MockReadablePartial implements ReadablePartial {

    @Override
    public int size() {
        return 1;
    }

    @Override
    public Chronology getChronology() {
        return null;
    }

    @Override
    public DateTimeField getField(int index) {
        return null;
    }

    @Override
    public DateTimeFieldType getFieldType(int index) {
        return DurationFieldType.millis();
    }

    @Override
    public int getValue(int index) {
        return 1;
    }

    @Override
    public boolean isSupported(DateTimeFieldType fieldType) {
        return false;
    }

    @Override
    public int get(DateTimeFieldType fieldType) {
        return 0;
    }

}

class MockNonContiguousReadablePartial implements ReadablePartial {

    @Override
    public int size() {
        return 2;
    }

    @Override
    public Chronology getChronology() {
        return null;
    }

    @Override
    public DateTimeField getField(int index) {
        return null;
    }

    @Override
    public DateTimeFieldType getFieldType(int index) {
        if (index == 0) {
            return DurationFieldType.millis();
        }
        return DurationFieldType.seconds();
    }

    @Override
    public int getValue(int index) {
        return 1;
    }

    @Override
    public boolean isSupported(DateTimeFieldType fieldType) {
        return false;
    }

    @Override
    public int get(DateTimeFieldType fieldType) {
        return 0;
    }

}

class MockInvalidFieldReadablePeriod implements ReadablePeriod {

    @Override
    public int getValue(int index) {
        return 0;
    }

    @Override
    public PeriodType getPeriodType() {
        return null;
    }

    @Override
    public DurationFieldType getFieldType(int index) {
        return DurationFieldType.seconds();
    }

    @Override
    public int size() {
        return 1;
    }

}

class MockValidReadablePeriod implements ReadablePeriod {

    @Override
    public int getValue(int index) {
        return 2;
    }

    @Override
    public PeriodType getPeriodType() {
        return PeriodType.yearMonthDayTime();
    }

    @Override
    public DurationFieldType getFieldType(int index) {
        return DurationFieldType.millis();
    }

    @Override
    public int size() {
        return 1;
    }

}

class MockBaseSingleFieldPeriod extends BaseSingleFieldPeriod {

    public MockBaseSingleFieldPeriod(int iPeriod) {
        super(iPeriod);
    }

    @Override
    public DurationFieldType getFieldType() {
        return DurationFieldType.millis();
    }

    @Override
    public PeriodType getPeriodType() {
        return PeriodType.yearMonthDayTime();
    }

    @Override
    public int compareTo(BaseSingleFieldPeriod baseSingleFieldPeriod) {
        return 0;
    }
}