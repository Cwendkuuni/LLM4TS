import static org.junit.Assert.assertEquals;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Seconds;
import org.junit.Test;


public class BaseSingleFieldPeriodTest {

  @Test
  public void testBetweenInstant() {
    BaseSingleFieldPeriod b = Days.between(time1, time2);
    assertEquals(0, b.getValue());
  }

  @Test
  public void testBetweenPartial() {
    BaseSingleFieldPeriod b = Days.between(partial1, partial2);
    assertEquals(0, b.getValue());
  }

  @Test(expected=IllegalArgumentException.class)
  public void testBetweenPartial_NullStart() {
    Days.between(nullPartial, partial2);
  }

  @Test(expected=IllegalArgumentException.class)
  public void testBetweenPartial_NullEnd() {
    Days.between(partial1, nullPartial);
  }

  @Test(expected=IllegalArgumentException.class)
  public void testBetweenPartial_SizeMismatch() {
    Days.between(partial1, partial3);
  }

  @Test(expected=IllegalArgumentException.class)
  public void testBetweenPartial_FieldTypeMismatch() {
    Days.between(partial1, partial4);
  }

  @Test(expected=IllegalArgumentException.class)
  public void testBetweenPartial_NoncontiguousFirstPartial() {
    Days.between(nonContiguousPartial, partial2);
  }

  @Test
  public void testStandardPeriodIn() {
    int period = BaseSingleFieldPeriod.standardPeriodIn(standardPeriod, 1);
    assertEquals(0, period);
  }

  @Test(expected=IllegalArgumentException.class)
  public void testStandardPeriodIn_ImpreciseDuration() {
    BaseSingleFieldPeriod.standardPeriodIn(imprecisePeriod, 1);
  }

  @Test
  public void testGetValue() {
    int value = baseSingleFieldPeriod.getValue();
    assertEquals(0, value);
  }

  @Test
  public void testSetValue() {
    baseSingleFieldPeriod.setValue(7);
    int value = baseSingleFieldPeriod.getValue();
    assertEquals(7, value);
  }

  @Test
  public void testSize() {
    int size = baseSingleFieldPeriod.size();
    assertEquals(1, size);
  }

  @Test
  public void testGetFieldType() {
    DurationFieldType fieldType = baseSingleFieldPeriod.getFieldType();
    assertEquals(null, fieldType);
  }

  @Test
  public void testGetPeriodType() {
    PeriodType periodType = baseSingleFieldPeriod.getPeriodType();
    assertEquals(null, periodType);
  }

  @Test(expected=IndexOutOfBoundsException.class)
  public void testGetFieldType_IndexOutOfBounds() {
    baseSingleFieldPeriod.getFieldType(1);
  }

  @Test(expected=IndexOutOfBoundsException.class)
  public void testGetValue_IndexOutOfBounds() {
    baseSingleFieldPeriod.getValue(1);
  }

  @Test
  public void testGet_FieldTypeNotSupported() {
    int value = baseSingleFieldPeriod.get(durationFieldType);
    assertEquals(0, value);
  }

  @Test
  public void testisSupported() {
    boolean supported = baseSingleFieldPeriod.isSupported(durationFieldType);
    assertEquals(false, supported);
  }

  @Test
  public void testToPeriod() {
    Period period = baseSingleFieldPeriod.toPeriod();
    assertEquals("PT0S", period.toString());
  }

  @Test
  public void testToMutablePeriod() {
    MutablePeriod mutablePeriod = baseSingleFieldPeriod.toMutablePeriod();
    assertEquals("PT0S", mutablePeriod.toString());
  }

  @Test
  public void testEquals_SameObject() {
    boolean equals = baseSingleFieldPeriod.equals(baseSingleFieldPeriod);
    assertEquals(true, equals);
  }

  @Test
  public void testEquals_DifferentObject() {
    boolean equals = baseSingleFieldPeriod.equals("string");
    assertEquals(false, equals);
  }

  @Test
  public void testEquals_DifferentType() {
    boolean equals = baseSingleFieldPeriod.equals(otherBaseSingleFieldPeriod);
    assertEquals(false, equals);
  }

  @Test
  public void testEquals_SameValue() {
    boolean equals = baseSingleFieldPeriod.equals(sameValueBaseSingleFieldPeriod);
    assertEquals(true, equals);
  }

  @Test
  public void testHashCode() {
    int hashCode = baseSingleFieldPeriod.hashCode();
    assertEquals(323, hashCode);
  }

  @Test(expected=ClassCastException.class)
  public void testCompareTo_DifferentType() {
    baseSingleFieldPeriod.compareTo(otherBaseSingleFieldPeriod);
  }

  @Test
  public void testCompareTo_Greater() {
    int compare = baseSingleFieldPeriod.compareTo(greaterBaseSingleFieldPeriod);
    assertEquals(-1, compare);
  }

  @Test
  public void testCompareTo_Less() {
    int compare = baseSingleFieldPeriod.compareTo(lessBaseSingleFieldPeriod);
    assertEquals(1, compare);
  }

  @Test
  public void testCompareTo_Equal() {
    int compare = baseSingleFieldPeriod.compareTo(equalBaseSingleFieldPeriod);
    assertEquals(0, compare);
  }

}