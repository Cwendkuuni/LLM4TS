package net.sf.lavalamp.device;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimeOfDayTest {
  
  private TimeOfDay time1;
  private TimeOfDay time2;
  private TimeOfDay time3;
  private TimeOfDay time4;
  
  @Before
  public void setUp() {
    time1 = new TimeOfDay(1, 30);
    time2 = new TimeOfDay("12:00");
    time3 = new TimeOfDay(5, 45);
    time4 = new TimeOfDay(6, 0);
  }
  
  @Test
  public void testCompareTo() {
    assertTrue(time1.compareTo(time2) > 0);
    assertTrue(time2.compareTo(time1) < 0);
    assertTrue(time4.compareTo(time3) > 0);
    assertEquals(0, time1.compareTo(time1));
  }
  
  @Test
  public void testGetHours() {
    assertEquals(new Integer(1), time1.getHours());
    assertEquals(new Integer(12), time2.getHours());
    assertEquals(new Integer(5), time3.getHours());
    assertEquals(new Integer(6), time4.getHours());
  }
  
  @Test
  public void testGetMinutes() {
    assertEquals(new Integer(30), time1.getMinutes());
    assertEquals(new Integer(0), time2.getMinutes());
    assertEquals(new Integer(45), time3.getMinutes());
    assertEquals(new Integer(0), time4.getMinutes());
  }
  
  @Test
  public void testToString() {
    assertEquals("1:30", time1.toString());
    assertEquals("12:00", time2.toString());
    assertEquals("5:45", time3.toString());
    assertEquals("6:0", time4.toString());
  }
  
  @Test
  public void testHashCode() {
    assertEquals(time1.hashCode(), time1.hashCode());
    assertEquals(time2.hashCode(), time2.hashCode());
    assertNotEquals(time1.hashCode(), time2.hashCode());
    assertNotEquals(time1.hashCode(), time3.hashCode());
    assertNotEquals(time1.hashCode(), time4.hashCode());
  }
  
  @Test
  public void testEquals() {
    assertEquals(time1, time1);
    assertEquals(time2, time2);
    assertNotEquals(time1, time2);
    assertNotEquals(time1, time3);
    assertNotEquals(time1, time4);
  }

}