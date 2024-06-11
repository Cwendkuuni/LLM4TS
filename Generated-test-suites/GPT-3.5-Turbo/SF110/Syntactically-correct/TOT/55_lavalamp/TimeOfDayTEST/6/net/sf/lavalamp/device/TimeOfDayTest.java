package net.sf.lavalamp.device;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

   // Test case for TimeOfDay(int hours, int minutes)
   // typical use case - creating a TimeOfDay object with valid hours and minutes
   @Test
   public void testTimeOfDayConstructor1() {
      TimeOfDay time = new TimeOfDay(12, 30);
      assertEquals(Integer.valueOf(12), time.getHours());
      assertEquals(Integer.valueOf(30), time.getMinutes());
   }
   
   // Test case for TimeOfDay(int hours, int minutes)
   // edge case - creating a TimeOfDay object with minimum valid hours and minutes
   @Test
   public void testTimeOfDayConstructor2() {
      TimeOfDay time = new TimeOfDay(0, 0);
      assertEquals(Integer.valueOf(0), time.getHours());
      assertEquals(Integer.valueOf(0), time.getMinutes());
   }
   
   // Test case for TimeOfDay(int hours, int minutes)
   // edge case - creating a TimeOfDay object with maximum valid hours and minutes
   @Test
   public void testTimeOfDayConstructor3() {
      TimeOfDay time = new TimeOfDay(23, 59);
      assertEquals(Integer.valueOf(23), time.getHours());
      assertEquals(Integer.valueOf(59), time.getMinutes());
   }
   
   // Test case for TimeOfDay(String time)
   // typical use case - creating a TimeOfDay object with a valid time string
   @Test
   public void testTimeOfDayConstructor4() {
      TimeOfDay time = new TimeOfDay("12:30");
      assertEquals(Integer.valueOf(12), time.getHours());
      assertEquals(Integer.valueOf(30), time.getMinutes());
   }
   
   // Test case for TimeOfDay(String time)
   // edge case - creating a TimeOfDay object with minimum valid time string
   @Test
   public void testTimeOfDayConstructor5() {
      TimeOfDay time = new TimeOfDay("00:00");
      assertEquals(Integer.valueOf(0), time.getHours());
      assertEquals(Integer.valueOf(0), time.getMinutes());
   }
   
   // Test case for TimeOfDay(String time)
   // edge case - creating a TimeOfDay object with maximum valid time string
   @Test
   public void testTimeOfDayConstructor6() {
      TimeOfDay time = new TimeOfDay("23:59");
      assertEquals(Integer.valueOf(23), time.getHours());
      assertEquals(Integer.valueOf(59), time.getMinutes());
   }
   
   // Test case for compareTo(TimeOfDay that)
   // typical use case - comparing two TimeOfDay objects with different hours and minutes
   @Test
   public void testCompareTo1() {
      TimeOfDay time1 = new TimeOfDay(12, 30);
      TimeOfDay time2 = new TimeOfDay(15, 45);
      assertTrue(time1.compareTo(time2) < 0);
   }
   
   // Test case for compareTo(TimeOfDay that)
   // edge case - comparing two TimeOfDay objects with same hours and different minutes
   @Test
   public void testCompareTo2() {
      TimeOfDay time1 = new TimeOfDay(12, 30);
      TimeOfDay time2 = new TimeOfDay(12, 45);
      assertTrue(time1.compareTo(time2) < 0);
   }
   
   // Test case for compareTo(TimeOfDay that)
   // edge case - comparing two TimeOfDay objects with same hours and minutes
   @Test
   public void testCompareTo3() {
      TimeOfDay time1 = new TimeOfDay(12, 30);
      TimeOfDay time2 = new TimeOfDay(12, 30);
      assertEquals(0, time1.compareTo(time2));
   }
   
   // Test case for getHours()
   // typical use case - getting the hours of a TimeOfDay object
   @Test
   public void testGetHours() {
      TimeOfDay time = new TimeOfDay(12, 30);
      assertEquals(Integer.valueOf(12), time.getHours());
   }
   
   // Test case for getMinutes()
   // typical use case - getting the minutes of a TimeOfDay object
   @Test
   public void testGetMinutes() {
      TimeOfDay time = new TimeOfDay(12, 30);
      assertEquals(Integer.valueOf(30), time.getMinutes());
   }
   
   // Test case for toString()
   // typical use case - converting a TimeOfDay object to a string
   @Test
   public void testToString() {
      TimeOfDay time = new TimeOfDay(12, 30);
      assertEquals("12:30", time.toString());
   }
   
   // Test case for hashCode()
   // typical use case - getting the hash code of a TimeOfDay object
   @Test
   public void testHashCode() {
      TimeOfDay time = new TimeOfDay(12, 30);
      assertEquals(1230, time.hashCode());
   }
   
   // Test case for equals(Object obj)
   // typical use case - comparing two TimeOfDay objects for equality
   @Test
   public void testEquals() {
      TimeOfDay time1 = new TimeOfDay(12, 30);
      TimeOfDay time2 = new TimeOfDay(12, 30);
      assertTrue(time1.equals(time2));
   }
   
   // Test case for equals(Object obj)
   // edge case - comparing a TimeOfDay object with null
   @Test
   public void testEqualsNull() {
      TimeOfDay time = new TimeOfDay(12, 30);
      assertFalse(time.equals(null));
   }
   
   // Test case for equals(Object obj)
   // edge case - comparing a TimeOfDay object with a different type object
   @Test
   public void testEqualsDifferentType() {
      TimeOfDay time = new TimeOfDay(12, 30);
      assertFalse(time.equals("12:30"));
   }
}