import org.joda.time.*;
import org.testng.Assert;
import org.testng.annotations.*;

public class BasePeriodTest {

    BasePeriod basePeriod;

    @BeforeClass
    public void setup() {
       basePeriod = new BasePeriod(1,2,3,4,5,6,7,8);
    }

    @Test
    public void testGetPeriodType() {
       PeriodType expectedPeriodType = PeriodType.standard();
       PeriodType actualPeriodType = basePeriod.getPeriodType();

       Assert.assertEquals(actualPeriodType, expectedPeriodType);
    }

    @Test
    public void testGetValue_ExistingIndex() {
       int expectedValue = 1;
       int actualValue = basePeriod.getValue(0);

       Assert.assertEquals(actualValue, expectedValue);
    }

    @Test(expectedExceptions = ArrayIndexOutOfBoundsException.class)
    public void testGetValue_IndexOutOfBounds() {
       basePeriod.getValue(10);
    }

    @Test
    public void testToDurationFrom() {
       ReadableInstant readableInstant = new DateTime();
       Duration expectedDuration = new Duration(readableInstant.getMillis(), DateTimeUtils.getInstantChronology(readableInstant).add(basePeriod, readableInstant.getMillis(), 1));
       Duration actualDuration = basePeriod.toDurationFrom(readableInstant);

       Assert.assertEquals(actualDuration, expectedDuration);
    }

    @Test
    public void testToDurationTo() {
       ReadableInstant readableInstant = new DateTime();
       Duration expectedDuration = new Duration(DateTimeUtils.getInstantChronology(readableInstant).add(basePeriod, readableInstant.getMillis(), -1), readableInstant.getMillis());
       Duration actualDuration = basePeriod.toDurationTo(readableInstant);

       Assert.assertEquals(actualDuration, expectedDuration);
    }

    @Test
    public void testSetPeriod() {
       ReadablePeriod readablePeriod = new Period();
       basePeriod.setPeriod(readablePeriod);

       int[] expectedValues = new int[basePeriod.size()];
       int[] actualValues = basePeriod.getValues();

       Assert.assertEquals(actualValues, expectedValues);
    }

    @Test
    public void testSetField_ExistingField() {
       basePeriod.setField(DurationFieldType.years(), 10);

       int expectedValue = 10;
       int actualValue = basePeriod.getValue(0);

       Assert.assertEquals(actualValue, expectedValue);
    }
    
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetField_NotSupportedField() {
       basePeriod.setField(DurationFieldType.eras(), 2);
    }

    @Test
    public void testAddField_ExistingField() {
       basePeriod.addField(DurationFieldType.years(), 5);

       int expectedValue = 6; // previous value + 5
       int actualValue = basePeriod.getValue(0);

       Assert.assertEquals(actualValue, expectedValue);
    }
    
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddField_NotSupportedField() {
       basePeriod.addField(DurationFieldType.eras(), 1);
    }

    @Test
    public void testMergePeriod() {
       ReadablePeriod readablePeriod = new Period(1,1,1,1,1,1,1,1);
       basePeriod.mergePeriod(readablePeriod);

       int[] expectedValues = new int[]{ 2, 3, 4, 5, 6, 7, 8, 9 };
       int[] actualValues = basePeriod.getValues();

       Assert.assertEquals(actualValues, expectedValues);
    }

    @Test
    public void testAddPeriod() {
       ReadablePeriod readablePeriod = new Period(1,1,1,1,1,1,1,1);
       basePeriod.addPeriod(readablePeriod);

       int[] expectedValues = new int[]{ 2, 3, 4, 5, 6, 7, 8, 9 };
       int[] actualValues = basePeriod.getValues();

       Assert.assertEquals(actualValues, expectedValues);
    }

    @Test
    public void testSetValue() {
       basePeriod.setValue(0, 10);

       int expectedValue = 10;
       int actualValue = basePeriod.getValue(0);

       Assert.assertEquals(actualValue, expectedValue);
    }

    @Test
    public void testSetValues() {
       int[] values = new int[]{ 10, 20, 30, 40, 50, 60, 70, 80 };
       basePeriod.setValues(values);

       int[] expectedValues = new int[]{ 10, 20, 30, 40, 50, 60, 70, 80 };
       int[] actualValues = basePeriod.getValues();

       Assert.assertEquals(actualValues, expectedValues);
    }

}