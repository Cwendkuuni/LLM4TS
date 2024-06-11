
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;
import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {
  
  @Test
  public void testIsLenient() {
    DateTimeField field = new LenientDateTimeField(DateTimeFieldType.dayOfWeek(), ISOChronology.getInstance());
    assertTrue(field.isLenient());
  }
  
  @Test
  public void testSet() {
    DateTimeField field = new LenientDateTimeField(DateTimeFieldType.dayOfMonth(), ISOChronology.getInstance());
    long instant = field.set(0, 10);
    assertEquals(10, field.get(instant)); // Testing if sets the value correctly
    assertEquals(0, field.get(instant-1)); // Testing if the previous value is 0 when setting a value out of bounds
    assertEquals(20, field.get(instant+10)); // Testing if the value is incremented by the difference when setting a value out of bounds
  }
  
  @Test
  public void testGetInstance() {
    DateTimeField field = DateTimeFieldType.hourOfDay().getField(ISOChronology.getInstance());
    DateTimeField lenientField = LenientDateTimeField.getInstance(field, ISOChronology.getInstance());
    assertSame(field, lenientField); // Testing if a lenient field is returned as is if it is already lenient
    assertFalse(lenientField.isLenient()); // Testing if the lenient field is not lenient
    DateTimeField strictField = new StrictDateTimeField(field);
    lenientField = LenientDateTimeField.getInstance(strictField, ISOChronology.getInstance());
    assertTrue(lenientField instanceof LenientDateTimeField); // Testing if a new LenientDateTimeField is returned if the field is not lenient
    assertTrue(lenientField.isLenient()); // Testing if the lenient field is lenient
  }
  
}
