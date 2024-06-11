import org.junit.*;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

   private BasicMonthOfYearDateTimeField field;
   private long instant;

   @Before
   public void setUp() {
       field = new BasicMonthOfYearDateTimeField(new BasicChronology(), 7);
       instant = 0L;
   }

   @Test
   public void testIsLenient() {
       assertFalse(field.isLenient());
   }

   @Test
   public void testGet() {
       int result = field.get(instant);
       assertEquals(1, result);
   }

   @Test
   public void testAdd() {
       long result = field.add(instant, 2);
       assertEquals(2678400000L, result);
   }

   @Test
   public void testGetDifferenceAsLong() {
       long minuend = 10000000L;
       long subtrahend = 5000000L;

       long result = field.getDifferenceAsLong(minuend, subtrahend);
       assertEquals(2, result);
   }

   @Test
   public void testSet() {
       long result = field.set(instant, 2);
       assertEquals(7884000000L, result);
   }

   @Test
   public void testIsLeap() {
       boolean result = field.isLeap(instant);
       assertFalse(result);
   }

   @Test
   public void testGetLeapAmount() {
       int result = field.getLeapAmount(instant);
       assertEquals(0, result);
   }

   @Test
   public void testGetMinimumValue() {
       int result = field.getMinimumValue();
       assertEquals(0, result);
   }

   @Test
   public void testGetMaximumValue() {
       int result = field.getMaximumValue();
       assertEquals(100, result);
   }

   @Test
   public void testRoundFloor() {
       long result = field.roundFloor(instant);
       assertEquals(0L, result);
   }

   @Test
   public void testRemainder() {
       long result = field.remainder(instant);
       assertEquals(0L, result);
   }

}