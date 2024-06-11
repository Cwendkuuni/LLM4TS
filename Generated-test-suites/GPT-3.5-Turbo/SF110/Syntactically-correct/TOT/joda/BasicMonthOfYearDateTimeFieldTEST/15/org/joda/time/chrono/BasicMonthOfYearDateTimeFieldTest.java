import org.joda.time.LocalDate;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Months;
import org.joda.time.chrono.BasicChronology;

public class BasicMonthOfYearDateTimeFieldTest {

    private BasicMonthOfYearDateTimeField basicMonthOfYearDateTimeField;

    @Before
    public void setUp() {
        BasicChronology basicChronology = BasicChronology.getInstance();
        int leapMonth = 2;
        basicMonthOfYearDateTimeField = new BasicMonthOfYearDateTimeField(basicChronology, leapMonth);
    }

    @Test
    public void testIsLenient() {
        assertFalse(basicMonthOfYearDateTimeField.isLenient());
    }

    @Test
    public void testGet() {
        long input = new LocalDate(2022, 1, 10).toDate().getTime();
        int expected = 1;
        assertEquals(expected, basicMonthOfYearDateTimeField.get(input));
    }

    @Test
    public void testAdd() {
        long input = new LocalDate(2022, 1, 10).toDate().getTime();
        int amount = 5;
        long expected = new LocalDate(2022, 6, 10).toDate().getTime();
        assertEquals(expected, basicMonthOfYearDateTimeField.add(input, amount));
    }

    @Test
    public void testAddWithLongAmount() {
        long input = new LocalDate(2022, 1, 10).toDate().getTime();
        long amount = 5L;
        long expected = new LocalDate(2022, 6, 10).toDate().getTime();
        assertEquals(expected, basicMonthOfYearDateTimeField.add(input, amount));
    }

    @Test
    public void testAddWrapField() {
        long input = new LocalDate(2022, 1, 10).toDate().getTime();
        int amount = 10;
        long expected = new LocalDate(2022, 11, 10).toDate().getTime();
        assertEquals(expected, basicMonthOfYearDateTimeField.addWrapField(input, amount));
    }

    @Test
    public void testGetDifferenceAsLong() {
        long start = new LocalDate(2022, 1, 10).toDate().getTime();
        long end = new LocalDate(2022, 6, 10).toDate().getTime();
        long expected = 5L;
        assertEquals(expected, basicMonthOfYearDateTimeField.getDifferenceAsLong(start, end));
    }

    @Test
    public void testSet() {
        long input = new LocalDate(2022, 1, 10).toDate().getTime();
        int value = 8;
        long expected = new LocalDate(2022, 8, 10).toDate().getTime();
        assertEquals(expected, basicMonthOfYearDateTimeField.set(input, value));
    }

    @Test
    public void testIsLeap() {
        long inputLeap = new LocalDate(2022, 2, 10).toDate().getTime();
        long inputNonLeap = new LocalDate(2022, 3, 10).toDate().getTime();
        assertTrue(basicMonthOfYearDateTimeField.isLeap(inputLeap));
        assertFalse(basicMonthOfYearDateTimeField.isLeap(inputNonLeap));
    }

    @Test
    public void testGetLeapAmount() {
        long inputLeap = new LocalDate(2022, 2, 10).toDate().getTime();
        long inputNonLeap = new LocalDate(2022, 3, 10).toDate().getTime();
        int expectedLeap = 1;
        int expectedNonLeap = 0;
        assertEquals(expectedLeap, basicMonthOfYearDateTimeField.getLeapAmount(inputLeap));
        assertEquals(expectedNonLeap, basicMonthOfYearDateTimeField.getLeapAmount(inputNonLeap));
    }

    @Test
    public void testGetMinimumValue() {
        int expected = 1;
        assertEquals(expected, basicMonthOfYearDateTimeField.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        int expected = 12;
        assertEquals(expected, basicMonthOfYearDateTimeField.getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        long input = new LocalDate(2022, 8, 10).toDate().getTime();
        long expected = new LocalDate(2022, 8, 1).toDate().getTime();
        assertEquals(expected, basicMonthOfYearDateTimeField.roundFloor(input));
    }

    @Test
    public void testRemainder() {
        long input = new LocalDate(2022, 8, 10).toDate().getTime();
        long expected = new LocalDate(2022, 8, 10).toDate().getTime() - new LocalDate(2022, 8, 1).toDate().getTime();
        assertEquals(expected, basicMonthOfYearDateTimeField.remainder(input));
    }

    @Test
    public void testAddWithReadablePartial() {
        LocalDate inputDate = new LocalDate(2022, 1, 10);
        LocalDate expectedDate = new LocalDate(2022, 2, 10);
        int amount = 1;
        int[] input = {inputDate.getYear(), inputDate.getMonthOfYear(), inputDate.getDayOfMonth()};
        int[] expected = {expectedDate.getYear(), expectedDate.getMonthOfYear(), expectedDate.getDayOfMonth()};
        assertEquals(expected, basicMonthOfYearDateTimeField.add(inputDate, amount, input, 0));
    }

}