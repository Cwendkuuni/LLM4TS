import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.joda.time.chrono.BasicChronology;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {
    
    private BasicMonthOfYearDateTimeField field;
    
    @Before
    public void setUp() {
        field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.FEBRUARY);
    }
    
    @Test
    public void testIsLenient() {
        assertFalse(field.isLenient());
    }
    
    @Test
    public void testGet() {
        assertEquals(2, field.get(1585689600000L));
    }
    
    @Test
    public void testAdd() {
        assertEquals(1614547200000L, field.add(1585689600000L, 24));
    }
    
    @Test
    public void testAddWrapField() {
        assertEquals(86400000L, field.addWrapField(86400000L, -2));
    }
    
    @Test
    public void testGetDifferenceAsLong() {
        assertEquals(24, field.getDifferenceAsLong(1614547200000L, 1585689600000L));
    }
    
    @Test
    public void testSet() {
        assertEquals(1588291200000L, field.set(1585689600000L, 5));
    }
    
    @Test
    public void testIsLeap() {
        assertFalse(field.isLeap(1585689600000L));
    }
    
    @Test
    public void testGetLeapAmount() {
        assertEquals(0, field.getLeapAmount(1585689600000L));
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
        assertEquals(1580515200000L, field.roundFloor(1585689600000L));
    }
    
    @Test
    public void testRemainder() {
        assertEquals(51782400000L, field.remainder(1585689600000L));
    }
    
}