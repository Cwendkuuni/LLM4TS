import org.joda.time.*;
import org.junit.*;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {
  
  BasicChronology chronology;
  BasicMonthOfYearDateTimeField field;

  @Before
  public void setUp() {
    chronology = new BasicChronology();
    field = new BasicMonthOfYearDateTimeField(chronology, 0);
  }

  @Test
  public void testGet() {
    long instant = 0L;
    int month = field.get(instant);
    assertEquals(1, month);
  }

  @Test
  public void testAdd() {
    long instant = 0L;
    int months = 1;
    long newInstant = field.add(instant, months);
    assertEquals(DateTimeConstants.MILLIS_PER_DAY, newInstant);
  }

  @Test
  public void testAddWrapField() {
    long instant = 0L;
    int months = 1;
    long newInstant = field.addWrapField(instant, months);
    assertEquals(DateTimeConstants.MILLIS_PER_DAY, newInstant);
  }

  @Test
  public void testSet() {
    long instant = 0L;
    int month = 2;
    long newInstant = field.set(instant, month);
    assertEquals(DateTimeConstants.MILLIS_PER_DAY, newInstant);
  }

  @Test
  public void testGetMinimumValue() {
    int minValue = field.getMinimumValue();
    assertEquals(1, minValue);
  }

  @Test
  public void testGetMaximumValue() {
    int maxValue = field.getMaximumValue();
    assertEquals(12, maxValue);
  }

}