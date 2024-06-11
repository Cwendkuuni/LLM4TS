import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTimeFieldType;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.joda.time.chrono.GregorianChronology;
import org.junit.Before;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {
    private BasicMonthOfYearDateTimeField field;
    private BasicChronology chronology;
    
    @Before
    public void setUp() {
        chronology = GregorianChronology.getInstance();
        field = new BasicMonthOfYearDateTimeField(chronology, 0);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(DateTimeFieldType.monthOfYear(), field.getType());
    }
    
    @Test
    public void testGet() {
        assertEquals(5, field.get(10000L));
    }
    
    @Test
    public void testAdd() {
        assertEquals(100000000L, field.add(100000000L, 0));
        assertEquals(1109484618L, field.add(100000000L, 10));
        assertEquals(890514618L, field.add(100000000L, -10));
    }
    
    @Test
    public void testIsLeap() {
        assertTrue(field.isLeap(10000L));
        assertFalse(field.isLeap(20000L));
    }
    
    @Test
    public void testGetLeapAmount() {
        assertEquals(1, field.getLeapAmount(10000L));
        assertEquals(0, field.getLeapAmount(20000L));
    }
    
    @Test
    public void testGetLeapDurationField() {
        assertEquals(chronology.days(), field.getLeapDurationField());
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
        assertEquals(10000L, field.roundFloor(12345678L));
    }
    
    @Test
    public void testRemainder() {
        assertEquals(12345678L, field.remainder(12345678L));
    }
    
    @Test
    public void testSerialization() {
        field = new BasicMonthOfYearDateTimeField(chronology, 7);
        BasicMonthOfYearDateTimeField result = (BasicMonthOfYearDateTimeField) SerializationUtils.clone(field);
        assertEquals(field.getType(), result.getType());
    }
}