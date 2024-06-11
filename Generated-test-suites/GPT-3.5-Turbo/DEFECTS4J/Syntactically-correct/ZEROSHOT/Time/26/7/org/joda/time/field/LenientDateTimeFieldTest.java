import static org.junit.Assert.*;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LenientDateTimeFieldTest {
  
  private Chronology chronology;
  private DateTimeField field;

  @Before
  public void setUp() {
    chronology = new LenientChronology();
    field = chronology.dayOfWeek();
  }

  @After
  public void tearDown() {
    DateTimeUtils.setCurrentMillisSystem();
  }

  @Test
  public void testGet() {
    long millis = DateTimeUtils.currentTimeMillis();
    int expected = chronology.dayOfWeek().get(millis);
    assertEquals(expected, field.get(millis));
  }

  @Test
  public void testSetValidValue() {
    long millis = DateTimeUtils.currentTimeMillis();
    int originalValue = field.get(millis);
    int newValue = originalValue + 1;
    long expected = chronology.dayOfWeek().set(millis, newValue);
    assertEquals(expected, field.set(millis, newValue));
  }

  @Test
  public void testSetInvalidValue() {
    long millis = DateTimeUtils.currentTimeMillis();
    int originalValue = field.get(millis);
    int newValue = originalValue + 8;
    long expected = chronology.dayOfWeek().set(millis, newValue);
    assertEquals(expected, field.set(millis, newValue));
  }

  @Test
  public void testSetOutOfBoundValue() {
    long millis = DateTimeUtils.currentTimeMillis();
    int originalValue = field.get(millis);
    int newValue = originalValue - 10;
    long expected = chronology.dayOfWeek().set(millis, newValue);
    assertEquals(expected, field.set(millis, newValue));
  }

}