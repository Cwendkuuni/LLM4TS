import org.joda.time.DateTimeConstants;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {
    
    private BasicMonthOfYearDateTimeField field;
    private static final long TEST_INSTANT = 1609459200000L; // 01/01/2021
    
    @Before
    public void setup() {
        field = new BasicMonthOfYearDateTimeField(new BasicChronology(), DateTimeConstants.JANUARY);
    }
    
    @Test
    public void testGet_returnsMonthComponent() {
        int actual = field.get(TEST_INSTANT);
        assertEquals(1, actual);
    }
    
    @Test
    public void testAdd_addsMonthsToInstant() {
        long actual = field.add(TEST_INSTANT, 5);
        assertEquals(1612137600000L, actual); // 02/01/2021
    }
    
    @Test
    public void testAddWrapsField_addsMonthsWrappingWithinComponent() {
        long actual = field.addWrapField(TEST_INSTANT, 2);
        assertEquals(1617254400000L, actual); // 01/03/2021
    }
    
    @Test
    public void testSet_setsMonthComponentToGivenValue() {
        long actual = field.set(TEST_INSTANT, 3);
        assertEquals(1617272400000L, actual); // 01/03/2021
    }
    
    @Test
    public void testIsLeap_returnsFalse() {
        boolean actual = field.isLeap(TEST_INSTANT);
        assertFalse(actual);
    }
    
    @Test
    public void testGetLeapAmount_returnsZero() {
        int actual = field.getLeapAmount(TEST_INSTANT);
        assertEquals(0, actual);
    }
    
    @Test
    public void testGetMinimumValue_returnsJanuary() {
        int actual = field.getMinimumValue();
        assertEquals(DateTimeConstants.JANUARY, actual);
    }
    
    @Test
    public void testGetMaximumValue_returnsMaxMonth() {
        int actual = field.getMaximumValue();
        assertEquals(12, actual);
    }
    
    @Test
    public void testRoundFloor_returnsYearMonthMillis() {
        long actual = field.roundFloor(TEST_INSTANT);
        assertEquals(1609459200000L, actual); // 01/01/2021
    }
    
    @Test
    public void testRemainder_returnsDifferenceBetweenInstantAndFloor() {
        long actual = field.remainder(TEST_INSTANT);
        assertEquals(0L, actual);
    }
}