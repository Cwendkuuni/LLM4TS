import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BaseSingleFieldPeriodTest {

    private BaseSingleFieldPeriod testPeriod;

    @Before
    public void setUp() {
        testPeriod = new BaseSingleFieldPeriod(10);
    }

    @After
    public void tearDown() {
        testPeriod = null;
    }

    @Test
    public void testGetValue() {
        assertEquals(10, testPeriod.getValue());
    }

    @Test
    public void testSetValue() {
        testPeriod.setValue(20);
        assertEquals(20, testPeriod.getValue());
    }

    @Test
    public void testGetFieldType() {
        // Testing abstract method, needs implementation in concrete subclasses
    }

    @Test
    public void testGetPeriodType() {
        // Testing abstract method, needs implementation in concrete subclasses
    }

    @Test
    public void testSize() {
        assertEquals(1, testPeriod.size());
    }

    @Test
    public void testGetFieldTypeInt() {
        assertEquals(testPeriod.getFieldType(), testPeriod.getFieldType(0));
        try {
            testPeriod.getFieldType(1);
        } catch (IndexOutOfBoundsException e) {
            assertEquals("1", e.getMessage());
        }
    }

    @Test
    public void testGetValueInt() {
        assertEquals(testPeriod.getValue(), testPeriod.getValue(0));
        try {
            testPeriod.getValue(1);
        } catch (IndexOutOfBoundsException e) {
            assertEquals("1", e.getMessage());
        }
    }

    @Test
    public void testGet() {
        DurationFieldType fieldType = testPeriod.getFieldType();
        assertEquals(testPeriod.getValue(), testPeriod.get(fieldType));
        assertFalse(testPeriod.isSupported(DurationFieldType.millis()));
    }

    @Test
    public void testIsSupported() {
        assertTrue(testPeriod.isSupported(testPeriod.getFieldType()));
        assertFalse(testPeriod.isSupported(DurationFieldType.millis()));
    }

    @Test
    public void testToPeriod() {
        Period period = testPeriod.toPeriod();
        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getWeeks());
        assertEquals(0, period.getDays());
        assertEquals(0, period.getHours());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getSeconds());
        assertEquals(0, period.getMillis());
    }

    @Test
    public void testToMutablePeriod() {
        MutablePeriod mutablePeriod = testPeriod.toMutablePeriod();
        assertEquals(0, mutablePeriod.getYears());
        assertEquals(0, mutablePeriod.getMonths());
        assertEquals(0, mutablePeriod.getWeeks());
        assertEquals(0, mutablePeriod.getDays());
        assertEquals(0, mutablePeriod.getHours());
        assertEquals(0, mutablePeriod.getMinutes());
        assertEquals(0, mutablePeriod.getSeconds());
        assertEquals(0, mutablePeriod.getMillis());
    }

    @Test
    public void testEquals() {
        BaseSingleFieldPeriod equalPeriod = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod differentPeriod = new BaseSingleFieldPeriod(20);

        assertTrue(testPeriod.equals(equalPeriod));
        assertFalse(testPeriod.equals(differentPeriod));
        assertFalse(testPeriod.equals(null));
        assertFalse(testPeriod.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        BaseSingleFieldPeriod equalPeriod = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod differentPeriod = new BaseSingleFieldPeriod(20);

        assertEquals(testPeriod.hashCode(), equalPeriod.hashCode());
        assertNotEquals(testPeriod.hashCode(), differentPeriod.hashCode());
    }

    @Test
    public void testCompareTo() {
        BaseSingleFieldPeriod equalPeriod = new BaseSingleFieldPeriod(10);
        BaseSingleFieldPeriod smallerPeriod = new BaseSingleFieldPeriod(5);
        BaseSingleFieldPeriod largerPeriod = new BaseSingleFieldPeriod(20);

        assertEquals(0, testPeriod.compareTo(equalPeriod));
        assertEquals(-1, testPeriod.compareTo(largerPeriod));
        assertEquals(1, testPeriod.compareTo(smallerPeriod));

        try {
            testPeriod.compareTo(new Object());
        } catch (ClassCastException e) {
            assertEquals("class BaseSingleFieldPeriod cannot be compared to class java.lang.Object", e.getMessage());
        }
    }
}