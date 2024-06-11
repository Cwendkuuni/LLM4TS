import org.junit.Test;

public class TimeOfDayTest {
  
  // Test cases for the constructor with int parameters
  @Test
  public void testConstructorWithIntParameters() {
    TimeOfDay timeOfDay = new TimeOfDay(12, 30);
    assertEquals(Integer.valueOf(12), timeOfDay.getHours());
    assertEquals(Integer.valueOf(30), timeOfDay.getMinutes());
  }
  
  // Test cases for the constructor with String parameter
  @Test
  public void testConstructorWithStringParameter() {
    TimeOfDay timeOfDay = new TimeOfDay("12:30");
    assertEquals(Integer.valueOf(12), timeOfDay.getHours());
    assertEquals(Integer.valueOf(30), timeOfDay.getMinutes());
  }
  
  // Test case for compareTo method
  @Test
  public void testCompareTo() {
    TimeOfDay timeOfDay1 = new TimeOfDay(12, 30);
    TimeOfDay timeOfDay2 = new TimeOfDay(15, 45);
    assertTrue(timeOfDay1.compareTo(timeOfDay2) < 0);
    assertTrue(timeOfDay2.compareTo(timeOfDay1) > 0);
    assertTrue(timeOfDay1.compareTo(timeOfDay1) == 0);
  }
  
  // Test case for getHours method
  @Test
  public void testGetHours() {
    TimeOfDay timeOfDay = new TimeOfDay(12, 30);
    assertEquals(Integer.valueOf(12), timeOfDay.getHours());
  }
  
  // Test case for getMinutes method
  @Test
  public void testGetMinutes() {
    TimeOfDay timeOfDay = new TimeOfDay(12, 30);
    assertEquals(Integer.valueOf(30), timeOfDay.getMinutes());
  }
  
  // Test case for toString method
  @Test
  public void testToString() {
    TimeOfDay timeOfDay = new TimeOfDay(12, 30);
    assertEquals("12:30", timeOfDay.toString());
  }
  
  // Test case for hashCode method
  @Test
  public void testHashCode() {
    TimeOfDay timeOfDay = new TimeOfDay(12, 30);
    assertEquals(Integer.valueOf(1230).hashCode(), timeOfDay.hashCode());
  }
  
  // Test cases for equals method
  @Test
  public void testEquals() {
    TimeOfDay timeOfDay1 = new TimeOfDay(12, 30);
    TimeOfDay timeOfDay2 = new TimeOfDay(12, 30);
    TimeOfDay timeOfDay3 = new TimeOfDay(15, 45);
    assertTrue(timeOfDay1.equals(timeOfDay2));
    assertFalse(timeOfDay1.equals(timeOfDay3));
    assertFalse(timeOfDay1.equals(null));
    assertFalse(timeOfDay1.equals("12:30"));
  }
  
}