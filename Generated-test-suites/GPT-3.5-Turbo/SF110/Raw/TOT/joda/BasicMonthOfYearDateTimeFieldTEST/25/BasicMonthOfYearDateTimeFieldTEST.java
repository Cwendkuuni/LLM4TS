import org.joda.time.*;
import org.junit.*;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    private BasicMonthOfYearDateTimeField field;

    @Before
    public void setUp() {
        BasicChronology chronology = new BasicChronology();
        field = new BasicMonthOfYearDateTimeField(chronology, 6);
    }

    @Test
    public void testConstructorAndGet() {
        assertEquals(DateTimeFieldType.monthOfYear(), field.getType());
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        long input = 1609459200000L; // 2021-01-01T00:00:00
        assertEquals(1, field.get(input));
        input = 1612137600000L; // 2021-02-01T00:00:00
        assertEquals(2, field.get(input));
        input = 1672406400000L; // 2023-01-01T00:00:00
        assertEquals(1, field.get(input));
        input = 1669728000000L; // 2022-12-01T00:00:00
        assertEquals(12, field.get(input));
    }

    @Test
    public void testAdd() {
        long input = 1609459200000L; // 2021-01-01T00:00:00
        long expected = 1612137600000L; // 2021-02-01T00:00:00
        assertEquals(expected, field.add(input, 1));
        expected = 1675075200000L; // 2023-03-01T00:00:00
        assertEquals(expected, field.add(input, 14));
        input = 1612137600000L; // 2021-02-01T00:00:00
        expected = 1609459200000L; // 2021-01-01T00:00:00
        assertEquals(expected, field.add(input, -1));
        expected = 1672406400000L; // 2023-01-01T00:00:00
        assertEquals(expected, field.add(input, 11));
    }

    @Test
    public void testAddWrapField() {
        long input = 1609459200000L; // 2021-01-01T00:00:00
        long expected = 1609459200000L; // 2021-01-01T00:00:00
        assertEquals(expected, field.addWrapField(input, 0));
        expected = 1609632000000L; // 2021-01-03T00:00:00
        assertEquals(expected, field.addWrapField(input, 2));
        input = 1679136000000L; // 2023-02-18T00:00:00
        expected = 1679136000000L; // 2023-02-18T00:00:00
        assertEquals(expected, field.addWrapField(input, 0));
        expected = 1679136000000L; // 2023-02-18T00:00:00
        assertEquals(expected, field.addWrapField(input, 14));
    }

    @Test
    public void testSet() {
        long input = 1609459200000L; // 2021-01-01T00:00:00
        long expected = 1612051200000L; // 2021-02-01T00:00:00
        assertEquals(expected, field.set(input, 2));
        expected = 1696099200000L; // 2023-04-01T00:00:00
        assertEquals(expected, field.set(input, 16));
        input = 1612137600000L; // 2021-02-01T00:00:00
        expected = 1612137600000L; // 2021-02-01T00:00:00
        assertEquals(expected, field.set(input, 2));
        expected = 1705808000000L; // 2023-06-01T00:00:00
        assertEquals(expected, field.set(input, 18));
    }

    @Test
    public void testGetLeapAmount() {
        long input = 1609459200000L; // 2021-01-01T00:00:00
        assertEquals(0, field.getLeapAmount(input));
        input = 1664208000000L; // 2022-12-25T00:00:00
        assertEquals(0, field.getLeapAmount(input));
        input = 1676016000000L; // 2023-01-10T00:00:00
        assertEquals(0, field.getLeapAmount(input));
    }

    @Test
    public void testGetRangeDurationField() {
        DurationField rangeField = field.getRangeDurationField();
        assertEquals(1, rangeField.get(1L));
        assertEquals(1000, rangeField.get(1000L));
        assertEquals(31536000000L, rangeField.getDurationField().getField(1L).getMillis());
    }

    @Test
    public void testIsLeap() {
        long input = 1609459200000L; // 2021-01-01T00:00:00
        assertFalse(field.isLeap(input));
        input = 1676985600000L; // 2023-01-20T00:00:00
        assertFalse(field.isLeap(input));
        input = 1675017600000L; // 2023-02-01T00:00:00
        assertTrue(field.isLeap(input));
    }

    @Test
    public void testGetDifferenceAsLong() {
        long input1 = 1609459200000L; // 2021-01-01T00:00:00
        long input2 = 1672406400000L; // 2023-01-01T00:00:00
        assertEquals(23, field.getDifferenceAsLong(input1, input2));
        assertEquals(-23, field.getDifferenceAsLong(input2, input1));
        input1 = 1612137600000L; // 2021-02-01T00:00:00
        input2 = 1669728000000L; // 2022-12-01T00:00:00
        assertEquals(-22, field.getDifferenceAsLong(input1, input2));
        assertEquals(22, field.getDifferenceAsLong(input2, input1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDifferenceAsLong_InvalidInput() {
        long input1 = 1609459200000L; // 2021-01-01T00:00:00
        long input2 = 1893024000000L; // 2030-01-01T00:00:00
        field.getDifferenceAsLong(input1, input2);
    }

    @Test
    public void testRoundFloor() {
        long input = 1609459200000L; // 2021-01-01T00:00:00
        long expected = 1609459200000L; // 2021-01-01T00:00:00
        assertEquals(expected, field.roundFloor(input));
        input = 1675545600000L; // 2023-02-05T00:00:00
        expected = 1672406400000L; // 2023-01-01T00:00:00
        assertEquals(expected, field.roundFloor(input));
        input = 1684512000000L; // 2023-04-18T00:00:00
        expected = 1676544000000L; // 2023-02-01T00:00:00
        assertEquals(expected, field.roundFloor(input));
    }

    @Test
    public void testRemainder() {
        long input = 1609459200000L; // 2021-01-01T00:00:00
        assertEquals(0L, field.remainder(input));
        input = 1669728000000L; // 2022-12-01T00:00:00
        assertEquals(0L, field.remainder(input));
        input = 1706908800000L; // 2023-12-02T00:00:00
        assertEquals(0L, field.remainder(input));
    }

}