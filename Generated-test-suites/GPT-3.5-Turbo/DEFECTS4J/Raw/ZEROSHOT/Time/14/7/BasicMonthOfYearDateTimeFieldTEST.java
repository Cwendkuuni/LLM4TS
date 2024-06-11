import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertFalse(field.isLenient());
    }

    @Test
    public void testGet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertEquals(0, field.get(0));
    }

    @Test
    public void testAdd() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertEquals(1, field.add(0, 1));
    }

    @Test
    public void testAddWrapField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertEquals(2, field.addWrapField(0, 2));
    }

    @Test
    public void testGetDifferenceAsLong() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertEquals(1, field.getDifferenceAsLong(1, 0));
    }

    @Test
    public void testSet() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertEquals(1, field.set(0, 1));
    }

    @Test
    public void testIsLeap() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertFalse(field.isLeap(0));
    }

    @Test
    public void testGetLeapAmount() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertEquals(0, field.getLeapAmount(0));
    }

    @Test
    public void testGetMinimumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertEquals(0, field.getMinimumValue());
    }

    @Test
    public void testGetMaximumValue() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertEquals(Integer.MAX_VALUE, field.getMaximumValue());
    }

    @Test
    public void testRoundFloor() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertEquals(0, field.roundFloor(1));
    }

    @Test
    public void testRemainder() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        assertEquals(0, field.remainder(1));
    }

    @Test
    public void testSerialization() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(null, 0);
        BasicMonthOfYearDateTimeField deserializedField = (BasicMonthOfYearDateTimeField) SerializationUtils.deserialize(SerializationUtils.serialize(field));
        assertNotNull(deserializedField);
    }

}