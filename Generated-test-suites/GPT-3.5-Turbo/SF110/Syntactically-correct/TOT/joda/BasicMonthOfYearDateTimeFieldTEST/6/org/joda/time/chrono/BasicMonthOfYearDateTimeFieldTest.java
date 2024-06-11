import static org.junit.Assert.*;
import org.junit.*;
import org.joda.time.*;
import org.joda.time.chrono.*;

public class BasicMonthOfYearDateTimeFieldTest {

    private BasicMonthOfYearDateTimeField field;
    private BasicChronology chronology;
    
    @Before
    public void setUp() {
        chronology = new BasicChronology();
        field = new BasicMonthOfYearDateTimeField(chronology, 1);
    }
    
    // Test for isLenient method
    
    @Test
    public void testIsLenient() {
        assertFalse(field.isLenient());
    }
    
    // Test for get method
    
    @Test
    public void testGet() {
        long dateTime = new DateTime(2010, 1, 15, 0, 0, 0).getMillis(); // January, 15th
        assertEquals(1, field.get(dateTime));
    }
    
    // Test for add method
    
    @Test
    public void testAdd_Zero() {
        long dateTime = new DateTime(2010, 1, 15, 0, 0, 0).getMillis(); // January, 15th
        assertEquals(dateTime, field.add(dateTime, 0));
    }
    
    @Test
    public void testAdd_Positive() {
        long dateTime = new DateTime(2010, 1, 15, 0, 0, 0).getMillis(); // January, 15th
        long expectedDateTime = new DateTime(2010, 2, 15, 0, 0, 0).getMillis(); // February, 15th
        assertEquals(expectedDateTime, field.add(dateTime, 1));
    }
    
    @Test
    public void testAdd_Positive_WrapYear() {
        long dateTime = new DateTime(2010, 12, 15, 0, 0, 0).getMillis(); // December, 15th
        long expectedDateTime = new DateTime(2011, 1, 15, 0, 0, 0).getMillis(); // January, 15th of next year
        assertEquals(expectedDateTime, field.add(dateTime, 1));
    }
    
    @Test
    public void testAdd_Positive_WrapMonth() {
        long dateTime = new DateTime(2010, 1, 15, 0, 0, 0).getMillis(); // January, 15th
        long expectedDateTime = new DateTime(2010, 2, 15, 0, 0, 0).getMillis(); // February, 15th
        for (int i = 0; i < 12; i++) {
            dateTime = field.add(dateTime, 1);
        }
        assertEquals(expectedDateTime, dateTime);
    }
    
    @Test
    public void testAdd_Negative() {
        long dateTime = new DateTime(2010, 1, 15, 0, 0, 0).getMillis(); // January, 15th
        long expectedDateTime = new DateTime(2009, 12, 15, 0, 0, 0).getMillis(); // December, 15th of previous year

        assertEquals(expectedDateTime, field.add(dateTime, -1));
    }
    
    @Test
    public void testAdd_Negative_WrapYear() {
        long dateTime = new DateTime(2010, 1, 15, 0, 0, 0).getMillis(); // January, 15th
        long expectedDateTime = new DateTime(2009, 12, 15, 0, 0, 0).getMillis(); // December, 15th of previous year
        assertEquals(expectedDateTime, field.add(dateTime, -1));
    }
    
    @Test
    public void testAdd_Negative_WrapMonth() {
        long dateTime = new DateTime(2010, 1, 15, 0, 0, 0).getMillis(); // January, 15th
        long expectedDateTime = new DateTime(2009, 12, 15, 0, 0, 0).getMillis(); // December, 15th of previous year
        for (int i = 0; i < 12; i++) {
            dateTime = field.add(dateTime, -1);
        }
        assertEquals(expectedDateTime, dateTime);
    }
    
    // Test for addWrapField method
    
    @Test
    public void testAddWrapField() {
        long dateTime = new DateTime(2010, 1, 15, 0, 0, 0).getMillis(); // January, 15th
        long expectedDateTime = new DateTime(2010, 2, 15, 0, 0, 0).getMillis(); // February, 15th
        assertEquals(expectedDateTime, field.addWrapField(dateTime, 1));
    }
    
    // Test for getDifferenceAsLong method
    
    @Test
    public void testGetDifferenceAsLong() {
        long dateTime1 = new DateTime(2010, 1, 15, 0, 0, 0).getMillis(); // January, 15th
        long dateTime2 = new DateTime(2010, 2, 15, 0, 0, 0).getMillis(); // February, 15th
        assertEquals(1, field.getDifferenceAsLong(dateTime2, dateTime1));
    }
    
    @Test
    public void testGetDifferenceAsLong_Negative() {
        long dateTime1 = new DateTime(2010, 1, 15, 0, 0, 0).getMillis(); // January, 15th
        long dateTime2 = new DateTime(2009, 12, 15, 0, 0, 0).getMillis(); // December, 15th of previous year
        assertEquals(-1, field.getDifferenceAsLong(dateTime2, dateTime1));
    }
    
    // Test for set method
    
    @Test
    public void testSet() {
        long dateTime = new DateTime(2010, 1, 15, 0, 0, 0).getMillis(); // January, 15th
        long expectedDateTime = new DateTime(2010, 12, 15, 0, 0, 0).getMillis(); // December, 15th

        assertEquals(expectedDateTime, field.set(dateTime, 12));
    }
    
    // Test for isLeap method
    
    @Test
    public void testIsLeap() {
        long dateTime = new DateTime(2010, 1, 15, 0, 0, 0).getMillis(); // January, 15th
        assertFalse(field.isLeap(dateTime));
    }
    
    // Test for getLeapAmount method
    
    @Test
    public void testGetLeapAmount() {
        long dateTime = new DateTime(2010, 1, 15, 0, 0, 0).getMillis(); // January, 15th
        assertEquals(0, field.getLeapAmount(dateTime));
    }
    
    // Test for getMinimumValue method
    
    @Test
    public void testGetMinimumValue() {
        assertEquals(1, field.getMinimumValue());
    }
    
    // Test for getMaximumValue method
    
    @Test
    public void testGetMaximumValue() {
        assertEquals(chronology.getMaxMonth(), field.getMaximumValue());
    }
    
}