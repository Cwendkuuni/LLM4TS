
import static org.junit.Assert.*;
import org.junit.Test;
import org.joda.time.*; 
import org.joda.time.field.*;
import org.joda.time.chrono.*;

public class BaseSingleFieldPeriodTest {
  
  @Test
  public void testBetweenReadableInstant() {
    ReadableInstant instant1 = new DateTime(2000, 1, 1, 0, 0, 0, 0);
    ReadableInstant instant2 = new DateTime(2000, 1, 2, 0, 0, 0, 0);
    DurationFieldType fieldType = DurationFieldType.days();
    
    int result = BaseSingleFieldPeriod.between(instant1, instant2, fieldType);
    
    assertEquals(1, result);
  }
  
  @Test
  public void testBetweenReadablePartial() {
    ReadablePartial partial1 = new LocalDate(2000, 1, 1);
    ReadablePartial partial2 = new LocalDate(2000, 1, 2);
    ReadablePeriod period = Period.months(1);
    
    int result = BaseSingleFieldPeriod.between(partial1, partial2, period);
    
    assertEquals(1, result);
  }
  
  @Test
  public void testStandardPeriodIn() {
    ReadablePeriod period = Period.days(5);
    long n = 24 * 60 * 60 * 1000; // 1 day in milliseconds
    
    int result = BaseSingleFieldPeriod.standardPeriodIn(period, n);
    
    assertEquals(5, result);
  }
  
  @Test
  public void testGetValue() {
    BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
    
    int result = period.getValue();
    
    assertEquals(5, result);
  }
  
  @Test
  public void testSetValue() {
    BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
    
    period.setValue(10);
    int result = period.getValue();
    
    assertEquals(10, result);
  }
  
  @Test
  public void testGetFieldType() {
    BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
      public DurationFieldType getFieldType() {
        return DurationFieldType.days();
      }
      
      public PeriodType getPeriodType() {
        return PeriodType.days();
      }
    };
    
    DurationFieldType result = period.getFieldType();
    
    assertEquals(DurationFieldType.days(), result);
  }
  
  @Test
  public void testGetPeriodType() {
    BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
      public DurationFieldType getFieldType() {
        return DurationFieldType.days();
      }
      
      public PeriodType getPeriodType() {
        return PeriodType.days();
      }
    };
    
    PeriodType result = period.getPeriodType();
    
    assertEquals(PeriodType.days(), result);
  }
  
  @Test
  public void testSize() {
    BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
    
    int result = period.size();
    
    assertEquals(1, result);
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testGetFieldTypeIndexOutOfBoundsException() {
    BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
    
    period.getFieldType(1);
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testGetValueIndexOutOfBoundsException() {
    BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5);
    
    period.getValue(1);
  }
  
  @Test
  public void testGet() {
    BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
      public DurationFieldType getFieldType() {
        return DurationFieldType.days();
      }
      
      public PeriodType getPeriodType() {
        return PeriodType.days();
      }
    };
    
    int result = period.get(DurationFieldType.days());
    
    assertEquals(5, result);
  }
  
  @Test
  public void testIsSupported() {
    BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
      public DurationFieldType getFieldType() {
        return DurationFieldType.days();
      }
      
      public PeriodType getPeriodType() {
        return PeriodType.days();
      }
    };
    
    boolean result = period.isSupported(DurationFieldType.days());
    
    assertTrue(result);
  }
  
  @Test
  public void testToPeriod() {
    BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
      public DurationFieldType getFieldType() {
        return DurationFieldType.days();
      }
      
      public PeriodType getPeriodType() {
        return PeriodType.days();
      }
    };
    
    Period result = period.toPeriod();
    
    assertEquals(Period.ZERO.withFields(period), result);
  }
  
  @Test
  public void testToMutablePeriod() {
    BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
      public DurationFieldType getFieldType() {
        return DurationFieldType.days();
      }
      
      public PeriodType getPeriodType() {
        return PeriodType.days();
      }
    };
    
    MutablePeriod result = period.toMutablePeriod();
    
    MutablePeriod expected = new MutablePeriod();
    expected.add(period);
    
    assertEquals(expected, result);
  }
  
  @Test
  public void testEquals() {
    BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5) {
      public DurationFieldType getFieldType() {
        return DurationFieldType.days();
      }
      
      public PeriodType getPeriodType() {
        return PeriodType.days();
      }
    };
    
    BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5) {
      public DurationFieldType getFieldType() {
        return DurationFieldType.days();
      }
      
      public PeriodType getPeriodType() {
        return PeriodType.days();
      }
    };
    
    boolean result = period1.equals(period2);
    
    assertTrue(result);
  }
  
  @Test
  public void testHashCode() {
    BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(5) {
      public DurationFieldType getFieldType() {
        return DurationFieldType.days();
      }
      
      public PeriodType getPeriodType() {
        return PeriodType.days();
      }
    };
    
    int result = period.hashCode();
    
    assertEquals(27 * (27 * 17 + 5) + DurationFieldType.days().hashCode(), result);
  }
  
  @Test
  public void testCompareTo() {
    BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5) {
      public DurationFieldType getFieldType() {
        return DurationFieldType.days();
      }
      
      public PeriodType getPeriodType() {
        return PeriodType.days();
      }
    };
    
    BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10) {
      public DurationFieldType getFieldType() {
        return DurationFieldType.days();
      }
      
      public PeriodType getPeriodType() {
        return PeriodType.days();
      }
    };
    
    int result = period1.compareTo(period2);
    
    assertEquals(-1, result);
  }
}
