import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.joda.time.DateTimeFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;

public class BaseSingleFieldPeriodTest {
  
  private BaseSingleFieldPeriod period;
  
  @Before
  public void setup() {
    period = new BaseSingleFieldPeriod(10) {
      public DurationFieldType getFieldType() {
        return DateTimeFieldType.days();
      }
      public PeriodType getPeriodType() {
        return PeriodType.standard();
      }
    };
  }
  
  @Test
  public void testGetValue() {
    assertEquals(10, period.getValue());
  }
  
  @Test
  public void testSetValue() {
    period.setValue(20);
    assertEquals(20, period.getValue());
  }
  
  @Test
  public void testSize() {
    assertEquals(1, period.size());
  }
  
  @Test
  public void testGetFieldTypeWithValidIndex() {
    assertEquals(DateTimeFieldType.days(), period.getFieldType(0));
  }
  
  @Test(expected=IndexOutOfBoundsException.class)
  public void testGetFieldTypeWithInvalidIndex() {
    period.getFieldType(1);
  }
  
  @Test
  public void testGetValueWithValidIndex() {
    assertEquals(10, period.getValue(0));
  }
  
  @Test(expected=IndexOutOfBoundsException.class)
  public void testGetValueWithInvalidIndex() {
    period.getValue(1);
  }
  
  @Test
  public void testGetWithMatchingFieldType() {
    assertEquals(10, period.get(DateTimeFieldType.days()));
  }
  
  @Test
  public void testGetWithNonMatchingFieldType() {
    assertEquals(0, period.get(DateTimeFieldType.hours()));
  }
  
  @Test
  public void testIsSupportedWithMatchingFieldType() {
    assertTrue(period.isSupported(DateTimeFieldType.days()));
  }
  
  @Test
  public void testIsSupportedWithNonMatchingFieldType() {
    assertFalse(period.isSupported(DateTimeFieldType.hours()));
  }
  
  @Test
  public void testToPeriod() {
    Period expectedPeriod = Period.ZERO.withFields(period);
    assertEquals(expectedPeriod, period.toPeriod());
  }
  
  @Test
  public void testToMutablePeriod() {
    MutablePeriod expectedPeriod = new MutablePeriod();
    expectedPeriod.add(period);
    assertEquals(expectedPeriod, period.toMutablePeriod());
  }
  
  @Test
  public void testEqualsWithNull() {
    assertFalse(period.equals(null));
  }
  
  @Test
  public void testEqualsWithDifferentClass() {
    assertFalse(period.equals(new Object()));
  }
  
  @Test
  public void testEqualsWithDifferentPeriodType() {
    assertFalse(period.equals(new BaseSingleFieldPeriod(10) {
      public DurationFieldType getFieldType() {
        return DateTimeFieldType.hours();
      }
      public PeriodType getPeriodType() {
        return PeriodType.standard();
      }
    }));
  }
  
  @Test
  public void testEqualsWithDifferentValue() {
    assertFalse(period.equals(new BaseSingleFieldPeriod(20) {
      public DurationFieldType getFieldType() {
        return DateTimeFieldType.days();
      }
      public PeriodType getPeriodType() {
        return PeriodType.standard();
      }
    }));
  }
  
  @Test
  public void testEqualsWithSamePeriod() {
    assertTrue(period.equals(new BaseSingleFieldPeriod(10) {
      public DurationFieldType getFieldType() {
        return DateTimeFieldType.days();
      }
      public PeriodType getPeriodType() {
        return PeriodType.standard();
      }
    }));
  }
  
  @Test
  public void testHashCode() {
    int expectedHashCode = 17;
    expectedHashCode = 27 * expectedHashCode + 10;
    expectedHashCode = 27 * expectedHashCode + DateTimeFieldType.days().hashCode();
    assertEquals(expectedHashCode, period.hashCode());
  }
  
  @Test(expected=ClassCastException.class)
  public void testCompareToWithDifferentClass() {
    period.compareTo(new BaseSingleFieldPeriod(10) {
      public DurationFieldType getFieldType() {
        return DateTimeFieldType.days();
      }
      public PeriodType getPeriodType() {
        return PeriodType.standard();
      }
    });
  }
  
  @Test
  public void testCompareToWithGreaterValue() {
    BaseSingleFieldPeriod greaterPeriod = new BaseSingleFieldPeriod(20) {
      public DurationFieldType getFieldType() {
        return DateTimeFieldType.days();
      }
      public PeriodType getPeriodType() {
        return PeriodType.standard();
      }
    };
    assertEquals(1, period.compareTo(greaterPeriod));
  }
  
  @Test
  public void testCompareToWithSmallerValue() {
    BaseSingleFieldPeriod smallerPeriod = new BaseSingleFieldPeriod(5) {
      public DurationFieldType getFieldType() {
        return DateTimeFieldType.days();
      }
      public PeriodType getPeriodType() {
        return PeriodType.standard();
      }
    };
    assertEquals(-1, period.compareTo(smallerPeriod));
  }
  
  @Test
  public void testCompareToWithEqualValue() {
    BaseSingleFieldPeriod equalPeriod = new BaseSingleFieldPeriod(10) {
      public DurationFieldType getFieldType() {
        return DateTimeFieldType.days();
      }
      public PeriodType getPeriodType() {
        return PeriodType.standard();
      }
    };
    assertEquals(0, period.compareTo(equalPeriod));
  }
  
}