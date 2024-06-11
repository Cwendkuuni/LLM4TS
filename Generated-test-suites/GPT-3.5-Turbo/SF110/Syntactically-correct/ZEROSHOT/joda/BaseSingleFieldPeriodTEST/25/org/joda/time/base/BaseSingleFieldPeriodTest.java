import org.joda.time.*;
import org.joda.time.field.*;
import org.joda.time.chrono.*;
import org.junit.*;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

  @Test
  public void testBetweenWithReadableInstant() {
    ReadableInstant instant1 = new DateTime();
    ReadableInstant instant2 = new DateTime().plusHours(1);
    DurationFieldType fieldType = DurationFieldType.hours();
    
    int result = BaseSingleFieldPeriod.between(instant1, instant2, fieldType);
    
    assertEquals(1, result);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testBetweenWithNullReadableInstant() {
    ReadableInstant instant1 = new DateTime();
    ReadableInstant instant2 = null;
    DurationFieldType fieldType = DurationFieldType.hours();
    
    BaseSingleFieldPeriod.between(instant1, instant2, fieldType);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testBetweenWithDifferentFieldType() {
    ReadablePartial partial1 = new LocalDate();
    ReadablePartial partial2 = new LocalDate().plusDays(1);
    ReadablePeriod period = new Period();
    
    BaseSingleFieldPeriod.between(partial1, partial2, period);
  }
  
  // ... other test methods for other methods in the BaseSingleFieldPeriod class
  
}