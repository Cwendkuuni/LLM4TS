import static org.junit.Assert.*;
import org.joda.time.DurationFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.chrono.ISOChronology;
import org.junit.Before;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

  private static final int TEST_PERIOD = 10;
  private BaseSingleFieldPeriod baseSingleFieldPeriod;

  @Before
  public void setup() {
    baseSingleFieldPeriod = new BaseSingleFieldPeriod(TEST_PERIOD) {
      @Override
      public DurationFieldType getFieldType() {
        return null;
      }

      @Override
      public PeriodType getPeriodType() {
        return null;
      }
    };
  }

  @Test
  public void getValue() {
    assertEquals(TEST_PERIOD, baseSingleFieldPeriod.getValue());
  }

  @Test
  public void setValue() {
    baseSingleFieldPeriod.setValue(20);
    assertEquals(20, baseSingleFieldPeriod.getValue());
  }

  @Test
  public void size() {
    assertEquals(1, baseSingleFieldPeriod.size());
  }

  @Test
  public void getFieldType() {
    assertNull(baseSingleFieldPeriod.getFieldType());
  }

  @Test
  public void getFieldType_index0() {
    assertEquals(baseSingleFieldPeriod.getFieldType(), baseSingleFieldPeriod.getFieldType(0));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void getFieldType_invalidIndex() {
    baseSingleFieldPeriod.getFieldType(1);
  }

  @Test
  public void getValue_index0() {
    assertEquals(baseSingleFieldPeriod.getValue(), baseSingleFieldPeriod.getValue(0));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void getValue_invalidIndex() {
    baseSingleFieldPeriod.getValue(1);
  }

  @Test
  public void get() {
    assertEquals(10, baseSingleFieldPeriod.get(null));
    assertEquals(0, baseSingleFieldPeriod.get(DurationFieldType.millis()));
  }

  @Test
  public void isSupported() {
    assertTrue(baseSingleFieldPeriod.isSupported(null));
  }

  @Test
  public void toPeriod() {
    Period period = baseSingleFieldPeriod.toPeriod();
    assertEquals(0, period.getYears());
    assertEquals(0, period.getMonths());
    assertEquals(0, period.getWeeks());
    assertEquals(0, period.getDays());
    assertEquals(0, period.getHours());
    assertEquals(0, period.getMinutes());
    assertEquals(0, period.getSeconds());
    assertEquals(0, period.getMillis());
  }

  @Test
  public void toMutablePeriod() {
    MutablePeriod period = baseSingleFieldPeriod.toMutablePeriod();
    assertEquals(0, period.getYears());
    assertEquals(0, period.getMonths());
    assertEquals(0, period.getWeeks());
    assertEquals(0, period.getDays());
    assertEquals(0, period.getHours());
    assertEquals(0, period.getMinutes());
    assertEquals(0, period.getSeconds());
    assertEquals(0, period.getMillis());
  }

  @Test
  public void equals() {
    assertTrue(baseSingleFieldPeriod.equals(baseSingleFieldPeriod));
    assertFalse(baseSingleFieldPeriod.equals(new BaseSingleFieldPeriod(20) {
      @Override
      public DurationFieldType getFieldType() {
        return null;
      }

      @Override
      public PeriodType getPeriodType() {
        return null;
      }
    }));
    assertFalse(baseSingleFieldPeriod.equals(null));
    assertFalse(baseSingleFieldPeriod.equals(new Object()));
    assertFalse(baseSingleFieldPeriod.equals(new BaseSingleFieldPeriod(10) {
      @Override
      public DurationFieldType getFieldType() {
        return DurationFieldType.millis();
      }

      @Override
      public PeriodType getPeriodType() {
        return null;
      }
    }));
    assertFalse(baseSingleFieldPeriod.equals(new BaseSingleFieldPeriod(10) {
      @Override
      public DurationFieldType getFieldType() {
        return null;
      }

      @Override
      public PeriodType getPeriodType() {
        return PeriodType.time();
      }
    }));
    assertTrue(baseSingleFieldPeriod.equals(new BaseSingleFieldPeriod(10) {
      @Override
      public DurationFieldType getFieldType() {
        return null;
      }

      @Override
      public PeriodType getPeriodType() {
        return null;
      }
    }));
  }

  @Test
  public void hashCode() {
    assertEquals(baseSingleFieldPeriod.hashCode(), baseSingleFieldPeriod.getValue() * 27 + baseSingleFieldPeriod.getFieldType().hashCode());
  }

  @Test(expected = ClassCastException.class)
  public void compareTo_invalidType() {
    baseSingleFieldPeriod.compareTo(new BaseSingleFieldPeriod(10) {
      @Override
      public DurationFieldType getFieldType() {
        return null;
      }

      @Override
      public PeriodType getPeriodType() {
        return null;
      }
    });
  }

  @Test
  public void compareTo() {
    assertEquals(0, baseSingleFieldPeriod.compareTo(baseSingleFieldPeriod));
    assertEquals(0, baseSingleFieldPeriod.compareTo(new BaseSingleFieldPeriod(10) {
      @Override
      public DurationFieldType getFieldType() {
        return null;
      }

      @Override
      public PeriodType getPeriodType() {
        return null;
      }
    }));
    assertEquals(1, baseSingleFieldPeriod.compareTo(new BaseSingleFieldPeriod(5) {
      @Override
      public DurationFieldType getFieldType() {
        return null;
      }

      @Override
      public PeriodType getPeriodType() {
        return null;
      }
    }));
    assertEquals(-1, baseSingleFieldPeriod.compareTo(new BaseSingleFieldPeriod(15) {
      @Override
      public DurationFieldType getFieldType() {
        return null;
      }

      @Override
      public PeriodType getPeriodType() {
        return null;
      }
    }));
  }
}