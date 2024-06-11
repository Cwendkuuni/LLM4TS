import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.ReadableInstant;
import org.joda.time.LocalDate;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadablePeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.DateTimeUtils;
import org.joda.time.chrono.GregorianChronology;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {
  
  private BaseSingleFieldPeriod singleFieldPeriod;
  
  @Before
  public void setup() {
    singleFieldPeriod = new CustomSingleFieldPeriod(10);
  }
  
  @Test
  public void testPeriod_ReturnValue() {
    Assert.assertEquals(10, singleFieldPeriod.getValue());
  }
  
  @Test
  public void testGetFieldType_ReturnValue() {
    Assert.assertEquals(DurationFieldType.years(), singleFieldPeriod.getFieldType());
  }
  
  @Test
  public void testGetPeriodType_ReturnValue() {
    Assert.assertEquals(PeriodType.standard(), singleFieldPeriod.getPeriodType());
  }
  
  @Test
  public void testSize_ReturnValue() {
    Assert.assertEquals(1, singleFieldPeriod.size());
  }
  
  @Test
  public void testGetFieldTypeIndex_ReturnValue() {
    Assert.assertEquals(DurationFieldType.years(), singleFieldPeriod.getFieldType(0));
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testGetFieldTypeIndex_InvalidIndex() {
    singleFieldPeriod.getFieldType(1);
  }
  
  @Test
  public void testGetValueIndex_ReturnValue() {
    Assert.assertEquals(10, singleFieldPeriod.getValue(0));
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testGetValueIndex_InvalidIndex() {
    singleFieldPeriod.getValue(1);
  }
  
  @Test
  public void testGet_ReturnValue() {
    Assert.assertEquals(10, singleFieldPeriod.get(DurationFieldType.years()));
  }
  
  @Test
  public void testGet_UnsupportedFieldType() {
    Assert.assertEquals(0, singleFieldPeriod.get(DurationFieldType.months()));
  }
  
  @Test
  public void testIsSupported_ReturnValue() {
    Assert.assertTrue(singleFieldPeriod.isSupported(DurationFieldType.years()));
  }
  
  @Test
  public void testIsSupported_UnsupportedFieldType() {
    Assert.assertFalse(singleFieldPeriod.isSupported(DurationFieldType.months()));
  }
  
  @Test
  public void testToPeriod_ReturnValue() {
    Assert.assertEquals(Period.ZERO.withYears(10), singleFieldPeriod.toPeriod());
  }
  
  @Test
  public void testToMutablePeriod_ReturnValue() {
    MutablePeriod mutablePeriod = new MutablePeriod();
    mutablePeriod.addYears(10);
    Assert.assertEquals(mutablePeriod, singleFieldPeriod.toMutablePeriod());
  }
  
  @Test
  public void testEquals_SimilarObject() {
    BaseSingleFieldPeriod similarSingleFieldPeriod = new CustomSingleFieldPeriod(10);
    Assert.assertTrue(singleFieldPeriod.equals(similarSingleFieldPeriod));
  }
  
  @Test
  public void testEquals_DifferentObject() {
    BaseSingleFieldPeriod differentSingleFieldPeriod = new CustomSingleFieldPeriod(20);
    Assert.assertFalse(singleFieldPeriod.equals(differentSingleFieldPeriod));
  }
  
  @Test
  public void testEquals_NonPeriodObject() {
    Assert.assertFalse(singleFieldPeriod.equals(new Object()));
  }
  
  @Test
  public void testEquals_NullObject() {
    Assert.assertFalse(singleFieldPeriod.equals(null));
  }
  
  @Test
  public void testHashCode_ReturnValue() {
    int hashCode = 17;
    hashCode = 27 * hashCode + singleFieldPeriod.getValue();
    hashCode = 27 * hashCode + singleFieldPeriod.getFieldType().hashCode();
    Assert.assertEquals(hashCode, singleFieldPeriod.hashCode());
  }
  
  @Test
  public void testCompareTo_GreaterValue() {
    BaseSingleFieldPeriod greaterSingleFieldPeriod = new CustomSingleFieldPeriod(20);
    Assert.assertEquals(-1, singleFieldPeriod.compareTo(greaterSingleFieldPeriod));
  }
  
  @Test
  public void testCompareTo_LesserValue() {
    BaseSingleFieldPeriod lesserSingleFieldPeriod = new CustomSingleFieldPeriod(5);
    Assert.assertEquals(1, singleFieldPeriod.compareTo(lesserSingleFieldPeriod));
  }
  
  @Test(expected = ClassCastException.class)
  public void testCompareTo_DifferentClass() {
    singleFieldPeriod.compareTo(new Period(10));
  }
  
}

class CustomSingleFieldPeriod extends BaseSingleFieldPeriod {

    private static final long serialVersionUID = 12345L;

    public CustomSingleFieldPeriod(int period) {
        super(period);
    }

    @Override
    public DurationFieldType getFieldType() {
        return DurationFieldType.years();
    }

    @Override
    public PeriodType getPeriodType() {
        return PeriodType.standard();
    }
}