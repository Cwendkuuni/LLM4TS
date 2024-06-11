import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.joda.time.DateTimeFieldType;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;

public class BasicMonthOfYearDateTimeFieldTest {

    BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);

    @Test
    public void testIsLenient() {
        assertEquals(false, field.isLenient());
    }

    @Test
    public void testGet() {
        assertEquals(0, field.get(0));
        assertEquals(5, field.get(5));
        assertEquals(12, field.get(12));
    }

    @Test
    public void testAdd() {
        assertEquals(2, field.add(0, 2));
        assertEquals(7, field.add(5, 2));
        assertEquals(14, field.add(12, 2));
    }

    @Test
    public void testAddWrapField() {
        assertEquals(4, field.addWrapField(0, 4));
        assertEquals(6, field.addWrapField(2, 4));
        assertEquals(12, field.addWrapField(8, 4));
        assertEquals(1, field.addWrapField(11, 2));
    }

    @Test
    public void testGetDifferenceAsLong() {
        assertEquals(-2, field.getDifferenceAsLong(0, 2));
        assertEquals(2, field.getDifferenceAsLong(4, 2));
        assertEquals(3, field.getDifferenceAsLong(15, 12));
    }

    @Test
    public void testSet() {
        assertEquals(3, field.set(0, 3));
        assertEquals(9, field.set(6, 9));
        assertEquals(11, field.set(12, 11));
    }

    @Test
    public void testGetLeapAmount() {
        assertEquals(0, field.getLeapAmount(0));
        assertEquals(0, field.getLeapAmount(5));
        assertEquals(0, field.getLeapAmount(12));
    }

    @Test
    public void testGetLeapDurationField() {
        assertEquals(DateTimeFieldType.days(), field.getLeapDurationField());
    }

    @Test
    public void testGetMinimumValue() {
        assertEquals(1, field.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        assertEquals(12, field.getMaximumValue());
    }
}