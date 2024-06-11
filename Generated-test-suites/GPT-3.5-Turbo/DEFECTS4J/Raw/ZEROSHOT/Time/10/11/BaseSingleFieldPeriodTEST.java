import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.ReadablePeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.chrono.ISOChronology;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {
  
  private static final int VALUE = 10;
  private static final DurationFieldType FIELD_TYPE = DurationFieldType.days();
  
  private BaseSingleFieldPeriod period;
  
  @Before
  public void setUp() {
    period = new BaseSingleFieldPeriod(VALUE) {
      @Override
      public DurationFieldType getFieldType() {
        return FIELD_TYPE;
      }
      
      @Override
      public PeriodType getPeriodType() {
        return null;
      }
    };
  }
  
  @Test
  public void testGetValue() {
    assertEquals(VALUE, period.getValue());
  }
  
  @Test
  public void testSetValue() {
    int newValue = 20;
    period.setValue(newValue);
    assertEquals(newValue, period.getValue());
  }
  
  @Test
  public void testGetFieldType() {
    assertEquals(FIELD_TYPE, period.getFieldType());
  }
  
  @Test
  public void testGetPeriodType() {
    assertNull(period.getPeriodType());
  }
  
  @Test
  public void testSize() {
    assertEquals(1, period.size());
  }
  
  @Test
  public void testGetFieldTypeIndex() {
    assertEquals(FIELD_TYPE, period.getFieldType(0));
  }
  
  @Test
  public void testGetValueIndex() {
    assertEquals(VALUE, period.getValue(0));
  }
  
  @Test
  public void testGet() {
    assertEquals(VALUE, period.get(FIELD_TYPE));
    assertEquals(0, period.get(DurationFieldType.hours()));
  }
  
  @Test
  public void testIsSupported() {
    assertTrue(period.isSupported(FIELD_TYPE));
    assertFalse(period.isSupported(DurationFieldType.hours()));
  }
  
  @Test
  public void testToPeriod() {
    assertEquals(new Period(VALUE, FIELD_TYPE.getDurationType()), period.toPeriod());
  }
  
  @Test
  public void testToMutablePeriod() {
    MutablePeriod expected = new MutablePeriod();
    expected.add(VALUE, FIELD_TYPE.getDurationType());
    assertEquals(expected, period.toMutablePeriod());
  }
  
  @Test
  public void testEquals() {
    assertTrue(period.equals(period));
    assertFalse(period.equals(null));
    assertFalse(period.equals(new Object()));
    
    BaseSingleFieldPeriod other = new BaseSingleFieldPeriod(VALUE) {
      @Override
      public DurationFieldType getFieldType() {
        return FIELD_TYPE;
      }
      
      @Override
      public PeriodType getPeriodType() {
        return null;
      }
    };
    
    assertTrue(period.equals(other));
    
    other.setValue(VALUE + 1);
    assertFalse(period.equals(other));
    
    other.setValue(VALUE);
    
    DurationFieldType differentFieldType = DurationFieldType.minutes();
    other = new BaseSingleFieldPeriod(VALUE) {
      @Override
      public DurationFieldType getFieldType() {
        return differentFieldType;
      }
      
      @Override
      public PeriodType getPeriodType() {
        return null;
      }
    };
    
    assertFalse(period.equals(other));
  }
  
  @Test
  public void testHashCode() {
    int expected = 17;
    expected = 27 * expected + VALUE;
    expected = 27 * expected + FIELD_TYPE.hashCode();
    
    assertEquals(expected, period.hashCode());
  }
  
  @Test
  public void testCompareTo() {
    BaseSingleFieldPeriod otherLess = new BaseSingleFieldPeriod(VALUE - 1) {
      @Override
      public DurationFieldType getFieldType() {
        return FIELD_TYPE;
      }
      
      @Override
      public PeriodType getPeriodType() {
        return null;
      }
    };
    
    BaseSingleFieldPeriod otherGreater = new BaseSingleFieldPeriod(VALUE + 1) {
      @Override
      public DurationFieldType getFieldType() {
        return FIELD_TYPE;
      }
      
      @Override
      public PeriodType getPeriodType() {
        return null;
      }
    };
    
    assertEquals(0, period.compareTo(period));
    assertEquals(1, period.compareTo(otherLess));
    assertEquals(-1, period.compareTo(otherGreater));
    
    // ClassCastException test
    BaseSingleFieldPeriod wrongType = new BaseSingleFieldPeriod(VALUE) {
      @Override
      public DurationFieldType getFieldType() {
        return DurationFieldType.days();
      }
      
      @Override
      public PeriodType getPeriodType() {
        return null;
      }
    };
    
    try {
      period.compareTo(wrongType);
      fail("Expected ClassCastException");
    } catch (ClassCastException e) {
      // expected exception
    }
  }
}