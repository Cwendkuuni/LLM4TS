import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.PeriodType;
import org.joda.time.chrono.ISOChronology;

public class BasePeriodTest {

  @Test
  public void testGetValue() {
    BasePeriod period = new BasePeriod(1);
    int value = period.getValue(0);
    assertEquals(1, value);
  }
  
  @Test
  public void testGetPeriodType() {
    BasePeriod period = new BasePeriod(1);
    PeriodType periodType = period.getPeriodType();
    assertEquals(PeriodType.standard(), periodType);
  }
  
  @Test
  public void testToDurationFrom() {
    BasePeriod period = new BasePeriod(1);
    Duration duration = period.toDurationFrom(new DateTime(0));
    assertEquals(Duration.ZERO, duration);
  }
  
  @Test
  public void testToDurationTo() {
    BasePeriod period = new BasePeriod(1);
    Duration duration = period.toDurationTo(new DateTime(0));
    assertEquals(Duration.ZERO, duration);
  }
  
  @Test
  public void testSetField() {
    BasePeriod period = new BasePeriod(1);
    period.setField(ISOChronology.years(), 2);
    assertEquals(2, period.getValue(0));
  }
  
  @Test
  public void testAddField() {
    BasePeriod period = new BasePeriod(1);
    period.addField(ISOChronology.years(), 2);
    assertEquals(3, period.getValue(0));
  }
  
  @Test
  public void testMergePeriod() {
    BasePeriod period = new BasePeriod(1);
    period.mergePeriod(new BasePeriod(2));
    assertEquals(3, period.getValue(0));
  }
  
  @Test
  public void testAddPeriod() {
    BasePeriod period = new BasePeriod(1);
    period.addPeriod(new BasePeriod(2));
    assertEquals(3, period.getValue(0));
  }
  
  @Test
  public void testSetValue() {
    BasePeriod period = new BasePeriod(1);
    period.setValue(0, 2);
    assertEquals(2, period.getValue(0));
  }
  
  @Test
  public void testSetValues() {
    int[] values = {1};
    BasePeriod period = new BasePeriod(1);
    period.setValues(values);
    assertEquals(1, period.getValue(0));
  }

}