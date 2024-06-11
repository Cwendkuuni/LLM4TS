
import org.joda.time.DurationFieldType;
import org.joda.time.field.UnsupportedDurationField;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {
  
  @Test
  public void shouldReturnSameType() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    assertEquals(DurationFieldType.years(), field.getType());
  }
  
  @Test
  public void shouldReturnName() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
    assertEquals("months", field.getName());
  }
  
  @Test
  public void shouldReturnFalseForIsSupported() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
    assertFalse(field.isSupported());
  }
  
  @Test
  public void shouldReturnTrueForIsPrecise() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
    assertTrue(field.isPrecise());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void shouldThrowExceptionForGetValue() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
    field.getValue(1000);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void shouldThrowExceptionForGetValueAsLong() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
    field.getValueAsLong(1000L);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void shouldThrowExceptionForGetValueWithInstant() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
    field.getValue(1000L, 500L);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void shouldThrowExceptionForGetValueAsLongWithInstant() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
    field.getValueAsLong(1000L, 500L);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void shouldThrowExceptionForGetMillisWithValue() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
    field.getMillis(1000);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void shouldThrowExceptionForGetMillisWithValueLong() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
    field.getMillis(1000L);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void shouldThrowExceptionForGetMillisWithValueAndInstant() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
    field.getMillis(1000, 500L);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void shouldThrowExceptionForGetMillisWithValueLongAndInstant() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
    field.getMillis(1000L, 500L);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void shouldThrowExceptionForAdd() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
    field.add(1000L, 500);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void shouldThrowExceptionForAddWithValueLong() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.hours());
    field.add(1000L, 500L);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void shouldThrowExceptionForGetDifference() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
    field.getDifference(1000L, 500L);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void shouldThrowExceptionForGetDifferenceAsLong() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
    field.getDifferenceAsLong(1000L, 500L);
  }
  
  @Test
  public void shouldReturnZeroForGetUnitMillis() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
    assertEquals(0L, field.getUnitMillis());
  }
  
  @Test
  public void shouldReturnZeroForCompareTo() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.years());
    assertEquals(0, field.compareTo(field));
  }
  
  @Test
  public void shouldReturnTrueForEquals() {
    UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.hours());
    UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.hours());
    assertTrue(field1.equals(field2));
  }
  
  @Test
  public void shouldReturnFalseForEquals() {
    UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
    UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.hours());
    assertFalse(field1.equals(field2));
  }
  
  @Test
  public void shouldReturnHashCode() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.months());
    assertEquals("months".hashCode(), field.hashCode());
  }
  
  @Test
  public void shouldReturnToString() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
    assertEquals("UnsupportedDurationField[seconds]", field.toString());
  }
  
  @Test
  public void shouldReturnSameInstanceForReadResolve() {
    UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.days());
    assertSame(field, field.readResolve());
  }

}
