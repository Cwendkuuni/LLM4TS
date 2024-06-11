import org.joda.time.chrono.*;
import org.junit.Assert;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        boolean isLenient = field.isLenient();
        Assert.assertFalse(isLenient);
    }

    @Test
    public void testGet() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        int value = field.get(1596632400000L); // July 2020
        Assert.assertEquals(7, value);
    }

    @Test
    public void testAddWithInt() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long result = field.add(1596632400000L, 3); // July 2020 + 3 months = October 2020
        Assert.assertEquals(1601514000000L, result);
    }

    @Test
    public void testAddWithLong() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long result = field.add(1596632400000L, 3L); // July 2020 + 3 months = October 2020
        Assert.assertEquals(1601514000000L, result);
    }

    @Test
    public void testAddWrapField() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long result = field.addWrapField(1596632400000L, 10); // July 2020 + 10 months (wraps around to May 2021)
        Assert.assertEquals(1622428800000L, result);
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long difference = field.getDifferenceAsLong(1622428800000L, 1596632400000L); // May 2021 - July 2020
        Assert.assertEquals(10, difference);
    }

    @Test
    public void testSet() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long result = field.set(1596632400000L, 5); // July 2020 -> May 2020
        Assert.assertEquals(1588299600000L, result);
    }

    @Test
    public void testIsLeap() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        boolean isLeap = field.isLeap(1596632400000L); // July 2020
        Assert.assertFalse(isLeap);
    }

    @Test
    public void testGetLeapAmount() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        int amount = field.getLeapAmount(1596632400000L); // July 2020
        Assert.assertEquals(0, amount);
    }

    @Test
    public void testGetMinimumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        int minimumValue = field.getMinimumValue();
        Assert.assertEquals(1, minimumValue);
    }

    @Test
    public void testGetMaximumValue() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        int maximumValue = field.getMaximumValue();
        Assert.assertEquals(12, maximumValue);
    }

    @Test
    public void testRoundFloor() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long result = field.roundFloor(1596632400000L); // July 2020
        Assert.assertEquals(1593561600000L, result); // January 2020
    }

    @Test
    public void testRemainder() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        long remainder = field.remainder(1596632400000L); // July 2020
        Assert.assertEquals(30708000000L, remainder);
    }

    @Test
    public void testAddWithReadablePartial() {
        BasicChronology chronology = new BasicChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);
        int[] partial = new int[] {7}; // July
        int[] result = field.add(new ReadablePartial() {
            @Override
            public int size() {
                return 1;
            }

            @Override
            public DateTimeFieldType getFieldType(int index) {
                return DateTimeFieldType.monthOfYear();
            }

            @Override
            public int getValue(int index) {
                return partial[index];
            }

            @Override
            public int get(DateTimeFieldType fieldType) {
                return 0;
            }

            @Override
            public boolean isSupported(DateTimeFieldType fieldType) {
                return fieldType.equals(DateTimeFieldType.monthOfYear());
            }

            @Override
            public Chronology getChronology() {
                return null;
            }

            @Override
            public int[] getValues() {
                return partial;
            }
        }, 3, partial, 0); // July + 3 months = October
        Assert.assertArrayEquals(new int[]{10}, result);
    }
}

