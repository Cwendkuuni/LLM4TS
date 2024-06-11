import static org.junit.Assert.*;
import org.junit.Test;

public class TimeOfDayTest {

   @Test
   public void testConstructorWithIntegers() {
      TimeOfDay time = new TimeOfDay(12, 30);
      
      assertEquals((Integer)12, time.getHours());
      assertEquals((Integer)30, time.getMinutes());
   }
   
   @Test
   public void testConstructorWithString() {
      TimeOfDay time = new TimeOfDay("12:30");
      
      assertEquals((Integer)12, time.getHours());
      assertEquals((Integer)30, time.getMinutes());
   }
   
   @Test
   public void testCompareTo_SameHoursAndMinutes() {
      TimeOfDay time1 = new TimeOfDay(10, 20);
      TimeOfDay time2 = new TimeOfDay(10, 20);
      
      assertEquals(0, time1.compareTo(time2));
   }
   
   @Test
   public void testCompareTo_SameHoursDifferentMinutes() {
      TimeOfDay time1 = new TimeOfDay(10, 20);
      TimeOfDay time2 = new TimeOfDay(10, 30);
      
      assertTrue(time1.compareTo(time2) < 0);
      assertTrue(time2.compareTo(time1) > 0);
   }
   
   @Test
   public void testCompareTo_DifferentHoursSameMinutes() {
      TimeOfDay time1 = new TimeOfDay(10, 20);
      TimeOfDay time2 = new TimeOfDay(12, 20);
      
      assertTrue(time1.compareTo(time2) < 0);
      assertTrue(time2.compareTo(time1) > 0);
   }
   
   @Test
   public void testCompareTo_DifferentHoursAndMinutes() {
      TimeOfDay time1 = new TimeOfDay(10, 20);
      TimeOfDay time2 = new TimeOfDay(12, 30);
      
      assertTrue(time1.compareTo(time2) < 0);
      assertTrue(time2.compareTo(time1) > 0);
   }
   
   @Test
   public void testToString() {
      TimeOfDay time = new TimeOfDay(11, 45);
      
      assertEquals("11:45", time.toString());
   }
   
   @Test
   public void testHashCode() {
      TimeOfDay time1 = new TimeOfDay(10, 20);
      TimeOfDay time2 = new TimeOfDay(10, 20);
      
      assertEquals(time1.hashCode(), time2.hashCode());
   }
   
   @Test
   public void testEquals_ObjectIsNull() {
      TimeOfDay time = new TimeOfDay(10, 20);
      
      assertFalse(time.equals(null));
   }
   
   @Test
   public void testEquals_ObjectIsSame() {
      TimeOfDay time = new TimeOfDay(10, 20);
      
      assertTrue(time.equals(time));
   }
   
   @Test
   public void testEquals_ObjectIsDifferentClass() {
      TimeOfDay time = new TimeOfDay(10, 20);
      Object obj = new Object();
      
      assertFalse(time.equals(obj));
   }
   
   @Test
   public void testEquals_DifferentHoursSameMinutes() {
      TimeOfDay time1 = new TimeOfDay(10, 20);
      TimeOfDay time2 = new TimeOfDay(12, 20);
      
      assertFalse(time1.equals(time2));
   }
   
   @Test
   public void testEquals_DifferentMinutesSameHours() {
      TimeOfDay time1 = new TimeOfDay(10, 20);
      TimeOfDay time2 = new TimeOfDay(10, 30);
      
      assertFalse(time1.equals(time2));
   }
   
   @Test
   public void testEquals_SameHoursAndMinutes() {
      TimeOfDay time1 = new TimeOfDay(10, 20);
      TimeOfDay time2 = new TimeOfDay(10, 20);
      
      assertTrue(time1.equals(time2));
   }
}