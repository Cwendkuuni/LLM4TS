import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.joda.time.*;
import org.joda.time.base.*;

public class BaseSingleFieldPeriodTest {
  
  private BaseSingleFieldPeriod period;
  
  @Before
  public void setup() {
    period = new BaseSingleFieldPeriod(10);
  }
  
  @Test
  public void testBetween_withValidReadableInstantParameters_shouldReturnDifference() {
    ReadableInstant instant1 = new Instant(100);
    ReadableInstant instant2 = new Instant(1000);
    int expected = 900;
    
    int result = BaseSingleFieldPeriod.between(instant1, instant2, DurationFieldType.millis());
    
    assertEquals(expected, result);
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testBetween_withNullReadableInstant1_shouldThrowException() {
    ReadableInstant instant1 = null;
    ReadableInstant instant2 = new Instant(1000);
    
    BaseSingleFieldPeriod.between(instant1, instant2, DurationFieldType.millis());
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testBetween_withNullReadableInstant2_shouldThrowException() {
    ReadableInstant instant1 = new Instant(100);
    ReadableInstant instant2 = null;
    
    BaseSingleFieldPeriod.between(instant1, instant2, DurationFieldType.millis());
  }
  
  @Test
  public void testBetween_withValidReadablePartialParameters_shouldReturnDifference() {
    ReadablePartial partial1 = new DateTime(2020, 1, 1, 0, 0);
    ReadablePartial partial2 = new DateTime(2020, 1, 2, 0, 0);
    ReadablePeriod readablePeriod = new Period().withYears(1);
    int expected = 1;
    
    int result = BaseSingleFieldPeriod.between(partial1, partial2, readablePeriod);
    
    assertEquals(expected, result);
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testBetween_withNullReadablePartial1_shouldThrowException() {
    ReadablePartial partial1 = null;
    ReadablePartial partial2 = new DateTime(2020, 1, 2, 0, 0);
    ReadablePeriod readablePeriod = new Period().withYears(1);
    
    BaseSingleFieldPeriod.between(partial1, partial2, readablePeriod);
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testBetween_withNullReadablePartial2_shouldThrowException() {
    ReadablePartial partial1 = new DateTime(2020, 1, 1, 0, 0);
    ReadablePartial partial2 = null;
    ReadablePeriod readablePeriod = new Period().withYears(1);
    
    BaseSingleFieldPeriod.between(partial1, partial2, readablePeriod);
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testBetween_withDifferentSizeReadablePartials_shouldThrowException() {
    ReadablePartial partial1 = new DateTime(2020, 1, 1, 0, 0);
    ReadablePartial partial2 = new DateTime(2020, 1, 2, 0, 0, 0);
    ReadablePeriod readablePeriod = new Period().withYears(1);
    
    BaseSingleFieldPeriod.between(partial1, partial2, readablePeriod);
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testBetween_withReadablePartialsHavingDifferentSetOfFields_shouldThrowException() {
    ReadablePartial partial1 = new DateTime(2020, 1, 1, 0, 0);
    ReadablePartial partial2 = new DateTime(2020, 1, 2, 0, 0);
    ReadablePeriod readablePeriod = new Period().withMonths(1);
    
    BaseSingleFieldPeriod.between(partial1, partial2, readablePeriod);
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testBetween_withNonContiguousReadablePartial_shouldThrowException() {
    ReadablePartial partial1 = new DateTime(2020, 1, 1, 0, 0);
    ReadablePartial partial2 = new DateTime(2020, 1, 3, 0, 0);
    ReadablePeriod readablePeriod = new Period().withDays(1);
    
    BaseSingleFieldPeriod.between(partial1, partial2, readablePeriod);
  }
  
  @Test
  public void testStandardPeriodIn_withValidReadablePeriod_shouldReturnStandardPeriod() {
    ReadablePeriod readablePeriod = new Period().withSeconds(10);
    long n = 1000;
    int expected = 10;
    
    int result = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, n);
    
    assertEquals(expected, result);
  }
  
  @Test
  public void testStandardPeriodIn_withNullReadablePeriod_shouldReturnZero() {
    ReadablePeriod readablePeriod = null;
    long n = 1000;
    int expected = 0;
    
    int result = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, n);
    
    assertEquals(expected, result);
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testStandardPeriodIn_withNonPreciseReadablePeriod_shouldThrowException() {
    ReadablePeriod readablePeriod = new Period().withWeeks(1);
    long n = 1000;
    
    BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, n);
  }
  
  @Test
  public void testGetValue_withInitialValue_shouldReturnInitialValue() {
    int expected = 10;
    
    int result = period.getValue();
    
    assertEquals(expected, result);
  }
  
  @Test
  public void testSetValue_withPositiveValue_shouldSetNewValue() {
    int newValue = 20;
    
    period.setValue(newValue);
    
    assertEquals(newValue, period.getValue());
  }
  
  @Test
  public void testGetFieldType_shouldReturnFieldType() {
    DurationFieldType expected = DurationFieldType.years();
    
    DurationFieldType result = period.getFieldType();
    
    assertEquals(expected, result);
  }
  
  @Test
  public void testGetPeriodType_shouldReturnPeriodType() {
    PeriodType expected = PeriodType.standard();
    
    PeriodType result = period.getPeriodType();
    
    assertEquals(expected, result);
  }
  
  @Test
  public void testSize_shouldReturnOne() {
    int expected = 1;
    
    int result = period.size();
    
    assertEquals(expected, result);
  }
  
  @Test(expected=IndexOutOfBoundsException.class)
  public void testGetFieldType_withInvalidIndex_shouldThrowException() {
    int index = 1;
    
    period.getFieldType(index);
  }
  
  @Test(expected=IndexOutOfBoundsException.class)
  public void testGetValue_withInvalidIndex_shouldThrowException() {
    int index = 1;
    
    period.getValue(index);
  }
  
  @Test
  public void testGet_withMatchingFieldType_shouldReturnValue() {
    DurationFieldType durationFieldType = DurationFieldType.years();
    int expected = 10;
    
    int result = period.get(durationFieldType);
    
    assertEquals(expected, result);
  }
  
  @Test
  public void testGet_withNonMatchingFieldType_shouldReturnZero() {
    DurationFieldType durationFieldType = DurationFieldType.months();
    int expected = 0;
    
    int result = period.get(durationFieldType);
    
    assertEquals(expected, result);
  }
  
  @Test
  public void testIsSupported_withSupportedFieldType_shouldReturnTrue() {
    DurationFieldType durationFieldType = DurationFieldType.years();
    
    boolean result = period.isSupported(durationFieldType);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsSupported_withUnsupportedFieldType_shouldReturnFalse() {
    DurationFieldType durationFieldType = DurationFieldType.months();
    
    boolean result = period.isSupported(durationFieldType);
    
    assertFalse(result);
  }
  
  @Test
  public void testToPeriod_shouldReturnEquivalentPeriod() {
    Period expected = new Period().withYears(10);
    
    Period result = period.toPeriod();
    
    assertEquals(expected, result);
  }
  
  @Test
  public void testToMutablePeriod_shouldReturnEquivalentMutablePeriod() {
    MutablePeriod expected = new MutablePeriod().withYears(10);
    
    MutablePeriod result = period.toMutablePeriod();
    
    assertEquals(expected, result);
  }
  
  @Test
  public void testEquals_withSameObject_shouldReturnTrue() {
    boolean result = period.equals(period);
    
    assertTrue(result);
  }
  
  @Test
  public void testEquals_withDifferentTypeObject_shouldReturnFalse() {
    Object obj = new Object();
    
    boolean result = period.equals(obj);
    
    assertFalse(result);
  }
  
  @Test
  public void testEquals_withDifferentReadablePeriod_shouldReturnFalse() {
    ReadablePeriod readablePeriod = new Period().withYears(5);
    
    boolean result = period.equals(readablePeriod);
    
    assertFalse(result);
  }
  
  @Test
  public void testEquals_withEqualReadablePeriod_shouldReturnTrue() {
    ReadablePeriod readablePeriod = new Period().withYears(10);
    
    boolean result = period.equals(readablePeriod);
    
    assertTrue(result);
  }
  
  @Test
  public void testHashCode_withEqualReadablePeriod_shouldReturnSameHashCode() {
    ReadablePeriod readablePeriod = new Period().withYears(10);
    int expected = period.hashCode();
    
    int result = readablePeriod.hashCode();
    
    assertEquals(expected, result);
  }
  
  @Test
  public void testHashCode_withDifferentReadablePeriod_shouldReturnDifferentHashCode() {
    ReadablePeriod readablePeriod = new Period().withYears(5);
    int expected = period.hashCode();
    
    int result = readablePeriod.hashCode();
    
    assertNotEquals(expected, result);
  }
  
  @Test
  public void testCompareTo_withSamePeriodType_shouldReturnZero() {
    BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
    int expected = 0;
    
    int result = period.compareTo(period2);
    
    assertEquals(expected, result);
  }
  
  @Test(expected=ClassCastException.class)
  public void testCompareTo_withDifferentPeriodType_shouldThrowException() {
    BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10) {
      @Override
      public DurationFieldType getFieldType() {
        return DurationFieldType.months();
      }
      @Override
      public PeriodType getPeriodType() {
        return PeriodType.standard();
      }
    };
    
    period.compareTo(period2);
  }
  
  @Test
  public void testCompareTo_withGreaterValue_shouldReturnPositive() {
    BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
    int expected = 1;
    
    int result = period.compareTo(period2);
    
    assertEquals(expected, result);
  }
  
  @Test
  public void testCompareTo_withSmallerValue_shouldReturnNegative() {
    BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(15);
    int expected = -1;
    
    int result = period.compareTo(period2);
    
    assertEquals(expected, result);
  }
}