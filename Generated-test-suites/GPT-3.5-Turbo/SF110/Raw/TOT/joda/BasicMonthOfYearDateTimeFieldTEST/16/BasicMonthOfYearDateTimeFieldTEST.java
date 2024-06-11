import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    private BasicMonthOfYearDateTimeField field;

    @Before
    public void setUp() {
        field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 1);
    }

    // Test for isLenient() method
    @Test
    public void testIsLenient() {
        assertFalse(field.isLenient());
    }

    // Test for get() method
    @Test
    public void testGet() {
        long input1 = 1622520000000L;  // Midnight on June 1, 2021
        int expectedOutput1 = 6;  // June

        long input2 = 1617235200000L;  // Midnight on April 1, 2021
        int expectedOutput2 = 4;  // April

        assertEquals(expectedOutput1, field.get(input1));
        assertEquals(expectedOutput2, field.get(input2));
    }

    // Test for add(long, int) method
    @Test
    public void testAddLongInt() {
        long input1 = 1622520000000L;  // Midnight on June 1, 2021
        int amount1 = 2;
        long expectedOutput1 = 1625102400000L;  // Midnight on August 1, 2021

        long input2 = 1617235200000L;  // Midnight on April 1, 2021
        int amount2 = 10;
        long expectedOutput2 = 1627660800000L;  // Midnight on February 1, 2022

        assertEquals(expectedOutput1, field.add(input1, amount1));
        assertEquals(expectedOutput2, field.add(input2, amount2));
    }

    // Test for add(long, long) method
    @Test
    public void testAddLongLong() {
        long input1 = 1622520000000L;  // Midnight on June 1, 2021
        long amount1 = 2L;
        long expectedOutput1 = 1625102400000L;  // Midnight on August 1, 2021

        long input2 = 1617235200000L;  // Midnight on April 1, 2021
        long amount2 = 10L;
        long expectedOutput2 = 1627660800000L;  // Midnight on February 1, 2022

        assertEquals(expectedOutput1, field.add(input1, amount1));
        assertEquals(expectedOutput2, field.add(input2, amount2));
    }

    // Test for add(ReadablePartial, int, int[], int) method
    @Test
    public void testAddReadablePartialIntIntArrayInt() {
        long input = 1622520000000L;  // Midnight on June 1, 2021
        int amount = 6;
        int[] inputArray = {2021, 6, 1};
        int[] expectedOutput = {2021, 12, 1};

        assertArrayEquals(expectedOutput, field.add(new LocalDateTime(input), amount, inputArray, 0));
    }

    // Test for addWrapField(long, int) method
    @Test
    public void testAddWrapField() {
        long input1 = 1622520000000L;  // Midnight on June 1, 2021
        int amount1 = 2;
        long expectedOutput1 = 1625102400000L;  // Midnight on August 1, 2021

        long input2 = 1617235200000L;  // Midnight on April 1, 2021
        int amount2 = 10;
        long expectedOutput2 = 1627660800000L;  // Midnight on February 1, 2022

        assertEquals(expectedOutput1, field.addWrapField(input1, amount1));
        assertEquals(expectedOutput2, field.addWrapField(input2, amount2));
    }

    // Test for getDifferenceAsLong(long, long) method
    @Test
    public void testGetDifferenceAsLong() {
        long input1 = 1622520000000L;  // Midnight on June 1, 2021
        long input2_1 = 1625102400000L;  // Midnight on August 1, 2021
        long input2_2 = 1617235200000L;  // Midnight on April 1, 2021

        long expectedOutput1 = 2;
        long expectedOutput2_1 = -2;
        long expectedOutput2_2 = -10;

        assertEquals(expectedOutput1, field.getDifferenceAsLong(input1, input2_1));
        assertEquals(expectedOutput2_1, field.getDifferenceAsLong(input2_1, input1));
        assertEquals(expectedOutput2_2, field.getDifferenceAsLong(input2_2, input1));
    }

    // Test for set(long, int) method
    @Test
    public void testSet() {
        long input1 = 1622520000000L;  // Midnight on June 1, 2021
        int newValue1 = 8;
        long expectedOutput1 = 1654060800000L;  // Midnight on August 1, 2022

        long input2 = 1617235200000L;  // Midnight on April 1, 2021
        int newValue2 = 11;
        long expectedOutput2 = 1656796800000L;  // Midnight on June 1, 2022

        assertEquals(expectedOutput1, field.set(input1, newValue1));
        assertEquals(expectedOutput2, field.set(input2, newValue2));
    }

    // Test for getRangeDurationField() method
    @Test
    public void testGetRangeDurationField() {
        assertEquals(new BasicChronology().years(), field.getRangeDurationField());
    }

    // Test for isLeap(long) method
    @Test
    public void testIsLeap() {
        long input1 = 1622520000000L;  // Midnight on June 1, 2021
        long input2 = 1617235200000L;  // Midnight on April 1, 2021

        assertTrue(field.isLeap(input1));
        assertFalse(field.isLeap(input2));
    }

    // Test for getLeapAmount(long) method
    @Test
    public void testGetLeapAmount() {
        long input1 = 1622520000000L;  // Midnight on June 1, 2021
        long input2 = 1617235200000L;  // Midnight on April 1, 2021

        int expectedOutput1 = 1;
        int expectedOutput2 = 0;

        assertEquals(expectedOutput1, field.getLeapAmount(input1));
        assertEquals(expectedOutput2, field.getLeapAmount(input2));
    }

    // Test for getLeapDurationField() method
    @Test
    public void testGetLeapDurationField() {
        assertEquals(new BasicChronology().days(), field.getLeapDurationField());
    }

    // Test for getMinimumValue() method
    @Test
    public void testGetMinimumValue() {
        assertEquals(1, field.getMinimumValue());
    }

    // Test for getMaximumValue() method
    @Test
    public void testGetMaximumValue() {
        assertEquals(new BasicChronology().getMaxMonth(), field.getMaximumValue());
    }

    // Test for roundFloor(long) method
    @Test
    public void testRoundFloor() {
        long input1 = 1622520000000L;  // Midnight on June 1, 2021
        long expectedOutput1 = 1622505600000L;  // Midnight on June 1, 2021

        long input2 = 1617235200000L;  // Midnight on April 1, 2021
        long expectedOutput2 = 1617220800000L;  // Midnight on April 1, 2021

        assertEquals(expectedOutput1, field.roundFloor(input1));
        assertEquals(expectedOutput2, field.roundFloor(input2));
    }

    // Test for remainder(long) method
    @Test
    public void testRemainder() {
        long input1 = 1622521212121L;  // Junk value
        long expectedOutput1 = 1212121L;  // Value after rounding to floor

        long input2 = 1617235268333L;  // Junk value
        long expectedOutput2 = 268333L;  // Value after rounding to floor

        assertEquals(expectedOutput1, field.remainder(input1));
        assertEquals(expectedOutput2, field.remainder(input2));
    }
}