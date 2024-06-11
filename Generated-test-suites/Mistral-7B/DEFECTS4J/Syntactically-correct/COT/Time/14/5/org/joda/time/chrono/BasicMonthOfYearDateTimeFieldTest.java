import org.joda.time.BaseDateTime;
import org.joda.time.BasicChronology;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;
import org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new ISOChronology(), 2);;
        Assert.assertFalse(field.isLenient());
    }

    @Test
    public void testGetInvalidInstant() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new ISOChronology(), 2);;
        try {
            field.get(-1L);
            fail();
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testGetValidInstant() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new ISOChronology(), 2);
        long instant = new BaseDateTime(0, 2022, 1, 1, 0, 0, 0).getMillis();
        int result = field.get(instant);
        assertEquals(1, result);
    }

    @Test
    public void testAddValidInstant() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new ISOChronology(), 2);
        long instant = new BaseDateTime(0, 2022, 1, 1, 0, 0, 0).getMillis();
        long result = field.add(instant, 1);
        long expected = new BaseDateTime(0, 2022, 2, 1, 0, 0, 0).getMillis();
        assertEquals(expected, result);
    }

    @Test
    public void testAddInvalidAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new ISOChronology(), 2);
        long instant = new BaseDateTime(0, 2022, 1, 1, 0, 0, 0).getMillis();
        try {
            field.add(instant, 12);
            fail();
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testAddLeapYear() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new ISOChronology(), 2);
        long instant = new BaseDateTime(0, 2024, 1, 1, 0, 0, 0).getMillis();
        int result = iChronology.getMonthOfYear(instant, 2024);
        assertEquals(2, result);

        long resultAdd = field.add(instant, 1);
        int resultAddMonth = iChronology.getMonthOfYear(resultAdd, 2024);
        assertEquals(3, resultAddMonth);
    }

    @Test
    public void testAddMaxValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new ISOChronology(), 12);
        long instant = new BaseDateTime(0, 2100, 12, 1, 0, 0, 0).getMillis();
        long result = field.add(instant, 1);
        long expected = new BaseDateTime(0, 2101, 1, 1, 0, 0, 0).getMillis();
        assertEquals(expected, result);
    }

    @Test
    public void testAddMinValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new ISOChronology(), 1);
        long instant = new BaseDateTime(0, 1, 1, 1, 0, 0, 0).getMillis();
        long result = field.add(instant, -1);
        long expected = new BaseDateTime(0, 12, 11, 23, 59, 59, 59).getMillis();
        assertEquals(expected, result);
    }

    @Test
    public void testAddInvalidInstant() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new ISOChronology(), 2);
        long instant = Long.MIN_VALUE;
        try {
            field.add(instant, 1);
            fail();
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testAddWrapFieldValid() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new ISOChronology(), 12);
        long instant = new BaseDateTime(0, 2100, 12, 1, 0, 0, 0).getMillis();
        long result = field.addWrapField(instant, 1);
        long expected = new BaseDateTime(0, 2101, 1, 1, 0, 0, 0).getMillis();
        assertEquals(expected, result);
    }

    @Test
    public void testGetDifferenceAsLong() {
        long minuendInstant = new BaseDateTime(0, 2022, 1, 1, 0, 0, 0).getMillis();
        long subtrahendInstant = new BaseDateTime(0, 2022, 2, 1, 0, 0, 0).getMillis();
        long result = new BasicMonthOfYearDateTimeField(new ISOChronology(), 12).getDifferenceAsLong(minuendInstant, subtrahendInstant);
        long expected = 1;
        assertEquals(expected, result);
    }

    @Test
    public void testSetValidInstant() {
        long instant = new BaseDateTime(0, 2022, 1, 1, 0, 0, 0).getMillis();
        long expected = new BaseDateTime(0, 2022, 2, 1, 0, 0, 0).getMillis();
        long result = new BasicMonthOfYearDateTimeField(new ISOChronology(), 12).set(instant, 2);
        assertEquals(expected, result);
    }

    @Test
    public void testSetInvalidMonth() {
        long instant = new BaseDateTime(0, 2022, 1, 1, 0, 0, 0).getMillis();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new ISOChronology(), 12);
        try {
            field.set(instant, 13);
            fail();
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testGetRangeDurationField() {
        DurationField result = new BasicMonthOfYearDateTimeField(new ISOChronology(), 12).getRangeDurationField();
        DurationField expected = new ISOChronology().years();
        assertEquals(expected, result);
    }

    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new ISOChronology(), 2);
        long instant = new BaseDateTime(0, 2024, 2, 1, 0, 0, 0).getMillis();
        boolean result = field.isLeap(instant);
        assertTrue(result);

        long instant2 = new BaseDateTime(0, 2023, 2, 1, 0, 0, 0).getMillis();
        boolean result2 = field.isLeap(instant2);
        assertFalse(result2);
    }

    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new ISOChronology(), 2);
        long instant = new BaseDateTime(0, 2024, 2, 1, 0, 0, 0).getMillis();
        int result = field.getLeapAmount(instant);
        assertEquals(1, result);
    }

    @Test
    public void testGetLeapDurationField() {
        DurationField result = new BasicMonthOfYearDateTimeField(new ISOChronology(), 12).getLeapDurationField();
        DurationField expected = new ISOChronology().days();
        assertEquals(expected, result);
    }

    @Test
    public void testGetMinimumValue() {
        int result = new BasicMonthOfYearDateTimeField(new ISOChronology(), 12).getMinimumValue();
        int expected = 1;
        assertEquals(expected, result);
    }

    @Test
    public void testGetMaximumValue() {
        int result = new BasicMonthOfYearDateTimeField(new ISOChronology(), 1).getMaximumValue();
        int expected = 12;
        assertEquals(expected, result);
    }

    @Test
    public void testRoundFloorInvalidInstant() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new ISOChronology(), 12);
        try {
            field.roundFloor(-1L);
            fail();
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testRoundFloorValidInstant() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new ISOChronology(), 12);
        long instant = new BaseDateTime(0, 2022, 1, 1, 0, 0, 0).getMillis();
        long result = field.roundFloor(instant);
        long expected = new BaseDateTime(0, 2022, 1, 1, 0, 0, 0).getMillis();
        assertEquals(expected, result);
    }

    @Test
    public void testRemainder() {
        long instant = new BaseDateTime(0, 2022, 1, 1, 0, 0, 0).getMillis();
        long result = new BasicMonthOfYearDateTimeField(new ISOChronology(), 12).remainder(instant);
        long expected = 0;
        assertEquals(expected, result);
    }

    @Test
    public void testRemainderInvalidInstant() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new ISOChronology(), 12);
        try {
            field.remainder(-1L);
            fail();
        } catch (IllegalArgumentException e) {
            //expected
        }
    }
}