import org.junit.Test;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    private final BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 1);

    @Test
    public void testGet() {
        LocalDateTime dateTime = new LocalDateTime(2021, 5, 15, 8, 30);
        assertEquals(5, field.get(dateTime.getMillis()));
    }

    @Test
    public void testAdd() {
        LocalDateTime originalDateTime = new LocalDateTime(2021, 5, 15, 8, 30);
        LocalDateTime expectedDateTime = new LocalDateTime(2021, 7, 15, 8, 30);
        long newDateTime = field.add(originalDateTime.getMillis(), 2);
        assertEquals(expectedDateTime.toDateTime().getMillis(), newDateTime);
    }

    @Test
    public void testAddWrapField() {
        LocalDateTime originalDateTime = new LocalDateTime(2021, 5, 15, 8, 30);
        LocalDateTime expectedDateTime = new LocalDateTime(2021, 8, 15, 8, 30);
        long newDateTime = field.addWrapField(originalDateTime.getMillis(), 3);
        assertEquals(expectedDateTime.toDateTime().getMillis(), newDateTime);
    }

    @Test
    public void testGetDifferenceAsLong() {
        LocalDate date1 = new LocalDate(2021, 5, 15);
        LocalDate date2 = new LocalDate(2023, 8, 25);
        assertEquals(15, field.getDifferenceAsLong(date2.toDateTimeAtStartOfDay().getMillis(), date1.toDateTimeAtStartOfDay().getMillis()));
    }

    @Test
    public void testSet() {
        LocalDateTime originalDateTime = new LocalDateTime(2021, 5, 15, 8, 30);
        LocalDateTime expectedDateTime = new LocalDateTime(2021, 6, 15, 8, 30);
        long newDateTime = field.set(originalDateTime.getMillis(), 6);
        assertEquals(expectedDateTime.toDateTime().getMillis(), newDateTime);
    }

    @Test
    public void testIsLeap() {
        LocalDateTime dateTime = new LocalDateTime(2021, 5, 15, 8, 30);
        assertFalse(field.isLeap(dateTime.getMillis()));
    }

    @Test
    public void testGetLeapAmount() {
        LocalDateTime dateTime = new LocalDateTime(2021, 5, 15, 8, 30);
        assertEquals(0, field.getLeapAmount(dateTime.getMillis()));
    }

    @Test
    public void testGetMinimumValue() {
        assertEquals(1, field.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        assertEquals(12, field.getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        LocalDateTime dateTime = new LocalDateTime(2021, 5, 15, 8, 30);
        LocalDateTime expectedDateTime = new LocalDateTime(2021, 5, 1, 0, 0);
        long roundedDateTime = field.roundFloor(dateTime.toDateTime().getMillis());
        assertEquals(expectedDateTime.toDateTime().getMillis(), roundedDateTime);
    }

    @Test
    public void testRemainder() {
        LocalDateTime dateTime = new LocalDateTime(2021, 5, 15, 8, 30);
        LocalDateTime expectedDateTime = new LocalDateTime(2021, 5, 15, 0, 0);
        long remainderDateTime = field.remainder(dateTime.toDateTime().getMillis());
        assertEquals(expectedDateTime.toDateTime().getMillis(), remainderDateTime);
    }
}