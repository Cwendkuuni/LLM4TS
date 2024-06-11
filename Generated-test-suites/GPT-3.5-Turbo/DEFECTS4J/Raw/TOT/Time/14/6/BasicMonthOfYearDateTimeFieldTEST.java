import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {
    
    private BasicMonthOfYearDateTimeField field;
    private long instant;
    private int year;
    private int month;
    
    @Before
    public void setUp() {
        field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 2);
        instant = 1577836800000L; // January 1, 2020 00:00:00
        year = 2020;
        month = 1;
    }
    
    @Test
    public void testGet() {
        assertEquals(month, field.get(instant));
    }
    
    @Test
    public void testAdd_positiveMonths() {
        long newInstant = field.add(instant, 10);
        int newMonth = field.get(newInstant);
        int newYear = field.iChronology.getYear(newInstant);
        
        assertEquals(month + 10, newMonth); // January + 10 months = November
        assertEquals(year, newYear); // Year remains the same
    }
    
    @Test
    public void testAdd_negativeMonths() {
        long newInstant = field.add(instant, -3);
        int newMonth = field.get(newInstant);
        int newYear = field.iChronology.getYear(newInstant);
        
        assertEquals(month - 3, newMonth); // January - 3 months = October
        assertEquals(year, newYear); // Year remains the same
    }
    
    @Test
    public void testAdd_zeroMonths() {
        long newInstant = field.add(instant, 0);
        assertEquals(instant, newInstant); // Adding zero months should not change the instant
    }
    
    @Test
    public void testAddWrapField_positiveMonths() {
        long newInstant = field.addWrapField(instant, 3);
        int newMonth = field.get(newInstant);
        int newYear = field.iChronology.getYear(newInstant);
        
        assertEquals(month + 3, newMonth); // January + 3 months = April
        assertEquals(year, newYear); // Year remains the same
    }
    
    @Test
    public void testAddWrapField_negativeMonths() {
        long newInstant = field.addWrapField(instant, -5);
        int newMonth = field.get(newInstant);
        int newYear = field.iChronology.getYear(newInstant);
        
        assertEquals(month - 5, newMonth); // January - 5 months = August
        assertEquals(year, newYear); // Year remains the same
    }
    
    @Test
    public void testSet_validMonth() {
        long newInstant1 = field.set(instant, 2);
        int newMonth1 = field.get(newInstant1);
        int newYear1 = field.iChronology.getYear(newInstant1);
        
        assertEquals(2, newMonth1); // Month is set to 2 (February)
        assertEquals(year, newYear1); // Year remains the same
        
        long newInstant2 = field.set(instant, 12);
        int newMonth2 = field.get(newInstant2);
        int newYear2 = field.iChronology.getYear(newInstant2);
        
        assertEquals(12, newMonth2); // Month is set to 12 (December)
        assertEquals(year, newYear2); // Year remains the same
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSet_invalidMonth() {
        field.set(instant, 13); // Month 13 is invalid, expected IllegalArgumentException
    }
    
}