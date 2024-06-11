import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testConstructor() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(10, period.getValue());
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
    }

    /**
     * Test getFieldType method.
     */
    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(DurationFieldType.DAYS, period.getFieldType());
    }

    /**
     * Test getPeriodType method.
     */
    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        PeriodType periodType = period.getPeriodType();
        assertTrue(periodType instanceof PeriodType);
    }

    /**
     * Test size method.
     */
    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(1, period.size());
    }

    /**
     * Test getFieldType method with invalid index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        period.getFieldType(2);
    }

    /**
     * Test getValue method with valid index.
     */
    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(10, period.getValue(0));
    }

    /**
     * Test getValue method with invalid index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueInvalidIndex() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        period.getValue(2);
    }

    /**
     * Test get method with valid field type.
     */
    @Test
    public void testGetValidFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(10, period.get(DurationFieldType.DAYS));
    }

    /**
     * Test get method with invalid field type.
     */
    @Test
    public void testGetInvalidFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertEquals(0, period.get(DurationFieldType.MONTHS));
    }

    /**
     * Test isSupported method with valid field type.
     */
    @Test
    public void testIsSupportedValidFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertTrue(period.isSupported(DurationFieldType.DAYS));
    }

    /**
     * Test isSupported method with invalid field type.
     */
    @Test
    public void testIsSupportedInvalidFieldType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertFalse(period.isSupported(DurationFieldType.MONTHS));
    }

    /**
     * Test toPeriod method.
     */
    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        Period result = period.toPeriod();
        assertEquals(10, result.getDays());
    }

    /**
     * Test toMutablePeriod method.
     */
    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        MutablePeriod result = period.toMutablePeriod();
        assertEquals(10, result.getDays());
    }

    /**
     * Test equals method with self.
     */
    @Test
    public void testEqualsSelf() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
        assertTrue(period1.equals(period2));
    }

    /**
     * Test equals method with null.
     */
    @Test
    public void testEqualsNull() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        assertFalse(period.equals(null));
    }

    /**
     * Test equals method with different type.
     */
    @Test
    public void testEqualsDifferentType() {
        BaseSingleFieldPeriod period = new BaseSingleFieldPeriod(10);
        String str = new String("test");
        assertFalse(period.equals(str));
    }

    /**
     * Test equals method with different value.
     */
    @Test
    public void testEqualsDifferentValue() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        assertFalse(period1.equals(period2));
    }

    /**
     * Test equals method with matching period type but different values.
     */
    @Test
    public void testEqualsSameTypeDifferentValues() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period3 = new BaseSingleFieldPeriod(10);
        assertFalse(period1.equals(period2));
        assertTrue(period1.equals(period3));
    }

    /**
     * Test hashCode method.
     */
    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
        assertEquals(period1.hashCode(), period2.hashCode());
    }

    /**
     * Test compareTo method with self.
     */
    @Test
    public void testCompareToSelf() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
        assertEquals(0, period1.compareTo(period2));
    }

    /**
     * Test compareTo method with smaller period.
     */
    @Test
    public void testCompareToSmallerPeriod() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(10);
        assertEquals(-5, period1.compareTo(period2));
    }

    /**
     * Test compareTo method with larger period.
     */
    @Test
    public void testCompareToLargerPeriod() {
        BaseSingleFieldPeriod period1 = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod period2 = new BaseSingleFieldPeriod(5);
        assertEquals(5, period2.compareTo(period1));
    }

}