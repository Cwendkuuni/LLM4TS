import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

  @Test
  public void testConstructor1() {
    TimeOfDay time = new TimeOfDay(5, 30);
    assertEquals(Integer.valueOf(5), time.getHours());
    assertEquals(Integer.valueOf(30), time.getMinutes());
  }
  
  @Test
  public void testConstructor2() {
    TimeOfDay time = new TimeOfDay("12:45");
    assertEquals(Integer.valueOf(12), time.getHours());
    assertEquals(Integer.valueOf(45), time.getMinutes());
  }
  
  @Test
  public void testCompareTo() {
    TimeOfDay time1 = new TimeOfDay(10, 15);
    TimeOfDay time2 = new TimeOfDay(10, 30);
    
    assertTrue(time1.compareTo(time2) < 0);
    assertTrue(time2.compareTo(time1) > 0);
    assertEquals(0, time1.compareTo(time1));
  }
  
  @Test
  public void testGetHours() {
    TimeOfDay time = new TimeOfDay(8, 0);
    assertEquals(Integer.valueOf(8), time.getHours());
  }
  
  @Test
  public void testGetMinutes() {
    TimeOfDay time = new TimeOfDay(6, 45);
    assertEquals(Integer.valueOf(45), time.getMinutes());
  }
  
  @Test
  public void testToString() {
    TimeOfDay time = new TimeOfDay(9, 30);
    assertEquals("9:30", time.toString());
  }
  
  @Test
  public void testHashCode() {
    TimeOfDay time = new TimeOfDay(10, 0);
    assertEquals(new Integer(1000).hashCode(), time.hashCode());
  }
  
  @Test
  public void testEquals() {
    TimeOfDay time1 = new TimeOfDay(5, 30);
    TimeOfDay time2 = new TimeOfDay(5, 30);
    TimeOfDay time3 = new TimeOfDay(5, 45);
    
    assertTrue(time1.equals(time2));
    assertFalse(time1.equals(null));
    assertFalse(time1.equals(new Object()));
    assertFalse(time1.equals(time3));
  }
}