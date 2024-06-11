import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;

public class BaseSingleFieldPeriodTest {

    @Test
    public void testConstructWithPeriod() {
        BaseSingleFieldPeriod testPeriod = new BaseSingleFieldPeriod(5);
        DurationFieldType fieldType = DurationFieldType.days();
        assertEquals(5, testPeriod.getValue());
        assertEquals(fieldType, testPeriod.getFieldType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithNegativePeriod() {
        new BaseSingleFieldPeriod(-5);
    }

    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod testPeriod = new BaseSingleFieldPeriod(5);
        assertEquals(5, testPeriod.getValue());
    }

    @Test
    public void testSetValue() {
        BaseSingleFieldPeriod testPeriod = new BaseSingleFieldPeriod(5);
        testPeriod.setValue(10);
        assertEquals(10, testPeriod.getValue());
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod testPeriod = new BaseSingleFieldPeriod(5);
        DurationFieldType fieldType = DurationFieldType.days();
        assertEquals(fieldType, testPeriod.getFieldType());
    }

    @Test
    public void testGetPeriodType() {
        BaseSingleFieldPeriod testPeriod = new BaseSingleFieldPeriod(5);
        PeriodType periodType = PeriodType.standard();
        assertEquals(periodType, testPeriod.getPeriodType());
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod testPeriod = new BaseSingleFieldPeriod(5);
        assertEquals(1, testPeriod.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldTypeInvalidIndex() {
        BaseSingleFieldPeriod testPeriod = new BaseSingleFieldPeriod(5);
        testPeriod.getFieldType(2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testgetValueInvalidIndex() {
        BaseSingleFieldPeriod testPeriod = new BaseSingleFieldPeriod(5);
        testPeriod.getValue(2);
    }

    @Test
    public void testGet() {
        BaseSingleFieldPeriod testPeriod = new BaseSingleFieldPeriod(5);
        DurationFieldType fieldType = DurationFieldType.days();
        assertEquals(5, testPeriod.get(fieldType));
    }

    @Test
    public void testIsSupported() {
        BaseSingleFieldPeriod testPeriod = new BaseSingleFieldPeriod(5);
        DurationFieldType fieldType = DurationFieldType.days();
        assertTrue(testPeriod.isSupported(fieldType));
    }

    @Test
    public void testIsSupportedInvalidFieldType() {
        BaseSingleFieldPeriod testPeriod = new BaseSingleFieldPeriod(5);
        DurationFieldType invalidFieldType = DurationFieldType.years();
        assertFalse(testPeriod.isSupported(invalidFieldType));
    }

    @Test
    public void testToPeriod() {
        BaseSingleFieldPeriod testPeriod = new BaseSingleFieldPeriod(5);
        Period period = testPeriod.toPeriod();
        assertEquals(5, period.getMillis());
    }

    @Test
    public void testToMutablePeriod() {
        BaseSingleFieldPeriod testPeriod = new BaseSingleFieldPeriod(5);
        MutablePeriod mutablePeriod = testPeriod.toMutablePeriod();
        mutablePeriod.add(DurationFieldType.days(), 2);
        assertEquals(7, mutablePeriod.getDays());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod testPeriod1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod testPeriod2 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod testPeriod3 = new BaseSingleFieldPeriod(10);
        assertEquals(testPeriod1, testPeriod1);
        assertEquals(testPeriod1, testPeriod2);
        assertNotEquals(testPeriod1, testPeriod3);
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod testPeriod1 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod testPeriod2 = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod testPeriod3 = new BaseSingleFieldPeriod(10);
        int hashCode1 = testPeriod1.hashCode();
        int hashCode2 = testPeriod2.hashCode();
        int hashCode3 = testPeriod3.hashCode();
        assertEquals(hashCode1, hashCode1);
        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }
}