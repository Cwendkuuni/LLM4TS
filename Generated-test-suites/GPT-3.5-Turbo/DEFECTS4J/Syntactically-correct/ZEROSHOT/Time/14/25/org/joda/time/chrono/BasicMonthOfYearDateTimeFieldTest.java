import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationField;
import org.joda.time.ReadablePartial;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.ImpreciseDateTimeField;

public class BasicMonthOfYearDateTimeFieldTest {
    
    private BasicMonthOfYearDateTimeField field;
    private BasicChronology chronology;
    
    @Before
    public void setUp() {
        chronology = new BasicChronology();
        field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
    }
    
    @Test
    public void testIsLenient() {
        assertFalse(field.isLenient());
    }
    
    @Test
    public void testGet() {
        assertEquals(1, field.get(0));
    }
    
    @Test
    public void testAdd() {
        assertEquals(0, field.add(0, -1));
        assertEquals(2, field.add(0, 1));
    }
    
    @Test
    public void testAddWrapField() {
        assertEquals(11, field.addWrapField(0, -2));
        assertEquals(1, field.addWrapField(0, 2));
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        assertEquals(10, field.getDifferenceAsLong(0, 100000));
        assertEquals(-10, field.getDifferenceAsLong(100000, 0));
    }
    
    @Test
    public void testSet() {
        assertEquals(0, field.set(0, 0));
        assertEquals(11, field.set(0, 11));
        assertEquals(0, field.set(0, 12));
    }
    
    @Test
    public void testIsLeap() {
        assertFalse(field.isLeap(0));
        assertTrue(field.isLeap(100000));
    }
    
    @Test
    public void testGetLeapAmount() {
        assertEquals(0, field.getLeapAmount(0));
        assertEquals(1, field.getLeapAmount(100000));
    }
    
    @Test
    public void testGetMinimumValue() {
        assertEquals(DateTimeConstants.JANUARY, field.getMinimumValue());
    }
    
    @Test
    public void testGetMaximumValue() {
        assertEquals(DateTimeConstants.DECEMBER, field.getMaximumValue());
    }
    
    @Test
    public void testRoundFloor() {
        assertEquals(0, field.roundFloor(0));
        assertEquals(0, field.roundFloor(100000));
    }
    
    @Test
    public void testRemainder() {
        assertEquals(0, field.remainder(0));
        assertEquals(0, field.remainder(100000));
    }
    
    @Test
    public void testSerialization() {
        // TODO: Add serialization test
    }
}