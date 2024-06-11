import org.joda.time.DurationFieldType;
import org.junit.Assert;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {
  
  @Test
  public void testGetValue() {
    BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
    Assert.assertEquals(10, period.getValue());
  }
  
  @Test
  public void testSetValue() {
    BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
    period.setValue(20);
    Assert.assertEquals(20, period.getValue());
  }
  
  @Test
  public void testGetFieldType() {
    BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
      public DurationFieldType getFieldType() {
        return DurationFieldType.days();
      }
      
      public PeriodType getPeriodType() {
        return null;
      }
    };
    Assert.assertEquals(DurationFieldType.days(), period.getFieldType());
  }
  
  @Test
  public void testGetPeriodType() {
    BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
      public DurationFieldType getFieldType() {
        return null;
      }
      
      public PeriodType getPeriodType() {
        return PeriodType.standard();
      }
    };
    Assert.assertEquals(PeriodType.standard(), period.getPeriodType());
  }
  
  @Test
  public void testSize() {
    BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
    Assert.assertEquals(1, period.size());
  }
  
  @Test
  public void testGetFieldTypeIndexOutOfBoundsException() {
    BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
      public DurationFieldType getFieldType() {
        return null;
      }
      
      public PeriodType getPeriodType() {
        return null;
      }
    };
    try {
      period.getFieldType(1);
      Assert.fail("Expected IndexOutOfBoundsException to be thrown");
    } catch (IndexOutOfBoundsException e) {
      // Expected
    }
  }
  
  @Test
  public void testGetFieldTypeNotIndexOutOfBoundsException() {
    BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
      public DurationFieldType getFieldType() {
        return null;
      }
      
      public PeriodType getPeriodType() {
        return null;
      }
    };
    try {
      period.getFieldType(0);
    } catch (IndexOutOfBoundsException e) {
      Assert.fail("Unexpected IndexOutOfBoundsException");
    }
  }
  
  @Test
  public void testGetValueIndexOutOfBoundsException() {
    BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
      public DurationFieldType getFieldType() {
        return null;
      }
      
      public PeriodType getPeriodType() {
        return null;
      }
    };
    try {
      period.getValue(1);
      Assert.fail("Expected IndexOutOfBoundsException to be thrown");
    } catch (IndexOutOfBoundsException e) {
      // Expected
    }
  }
  
  @Test
  public void testGetValueNotIndexOutOfBoundsException() {
    BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
      public DurationFieldType getFieldType() {
        return null;
      }
      
      public PeriodType getPeriodType() {
        return null;
      }
    };
    try {
      period.getValue(0);
    } catch (IndexOutOfBoundsException e) {
      Assert.fail("Unexpected IndexOutOfBoundsException");
    }
  }
  
  @Test
  public void testGet() {
    BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
      public DurationFieldType getFieldType() {
        return DurationFieldType.days();
      }
      
      public PeriodType getPeriodType() {
        return null;
      }
    };
    Assert.assertEquals(10, period.get(DurationFieldType.days()));
    Assert.assertEquals(0, period.get(DurationFieldType.minutes()));
  }
  
  @Test
  public void testIsSupported() {
    BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
      public DurationFieldType getFieldType() {
        return DurationFieldType.days();
      }
      
      public PeriodType getPeriodType() {
        return null;
      }
    };
    Assert.assertTrue(period.isSupported(DurationFieldType.days()));
    Assert.assertFalse(period.isSupported(DurationFieldType.hours()));
  }
  
  @Test
  public void testToPeriod() {
    BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
      public DurationFieldType getFieldType() {
        return null;
      }
      
      public PeriodType getPeriodType() {
        return null;
      }
    };
    Assert.assertEquals(Period.ZERO.withFields(period), period.toPeriod());
  }
  
  @Test
  public void testToMutablePeriod() {
    BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
      public DurationFieldType getFieldType() {
        return null;
      }
      
      public PeriodType getPeriodType() {
        return null;
      }
    };
    MutablePeriod mutablePeriod = new MutablePeriod();
    mutablePeriod.add(period);
    Assert.assertEquals(mutablePeriod, period.toMutablePeriod());
  }
  
  @Test
  public void testEquals() {
    BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10) {
      public DurationFieldType getFieldType() {
        return DurationFieldType.days();
      }
      
      public PeriodType getPeriodType() {
        return null;
      }
    };
    BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10) {
      public DurationFieldType getFieldType() {
        return DurationFieldType.days();
      }
      
      public PeriodType getPeriodType() {
        return null;
      }
    };
    BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(20) {
      public DurationFieldType getFieldType() {
        return DurationFieldType.days();
      }
      
      public PeriodType getPeriodType() {
        return null;
      }
    };
    BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriod(10) {
      public DurationFieldType getFieldType() {
        return DurationFieldType.hours();
      }
      
      public PeriodType getPeriodType() {
        return null;
      }
    };
    
    Assert.assertTrue(period1.equals(period2));
    Assert.assertFalse(period1.equals(period3));
    Assert.assertFalse(period1.equals(period4));
    Assert.assertFalse(period1.equals(null));
    Assert.assertFalse(period1.equals("not a period"));
  }
  
  @Test
  public void testHashCode() {
    BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10) {
      public DurationFieldType getFieldType() {
        return DurationFieldType.days();
      }
      
      public PeriodType getPeriodType() {
        return null;
      }
    };
    
    Assert.assertEquals(27 * (27 * 17 + 10) + DurationFieldType.days().hashCode(), period.hashCode());
  }
  
  @Test
  public void testCompareTo() {
    BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10) {
      public DurationFieldType getFieldType() {
        return DurationFieldType.days();
      }
      
      public PeriodType getPeriodType() {
        return null;
      }
    };
    BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(20) {
      public DurationFieldType getFieldType() {
        return DurationFieldType.days();
      }
      
      public PeriodType getPeriodType() {
        return null;
      }
    };
    BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(10) {
      public DurationFieldType getFieldType() {
        return DurationFieldType.days();
      }
      
      public PeriodType getPeriodType() {
        return null;
      }
    };
    BaseSingleFieldPeriod period4 = new BaseSingleFieldPeriod(10) {
      public DurationFieldType getFieldType() {
        return DurationFieldType.hours();
      }
      
      public PeriodType getPeriodType() {
        return null;
      }
    };
    
    Assert.assertEquals(-1, period1.compareTo(period2));
    Assert.assertEquals(1, period2.compareTo(period1));
    Assert.assertEquals(0, period1.compareTo(period3));
    try {
      period1.compareTo(period4);
      Assert.fail("Expected ClassCastException to be thrown");
    } catch (ClassCastException e) {
      // Expected
    }
  }
}