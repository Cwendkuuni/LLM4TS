import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

   @Test
   public void testGetValue() {
      ValuedEnum enum1 = new JavaVersionEnum("Java 1.0", 100);
      assertEquals(100, enum1.getValue());
      
      ValuedEnum enum2 = new JavaVersionEnum("Java 1.1", 110);
      assertEquals(110, enum2.getValue());
      
      ValuedEnum enum3 = new JavaVersionEnum("Java 1.2", 120);
      assertEquals(120, enum3.getValue());
      
      ValuedEnum enum4 = new JavaVersionEnum("Java 1.3", 130);
      assertEquals(130, enum4.getValue());
   }
   
   @Test
   public void testCompareTo() {
      ValuedEnum enum1 = new JavaVersionEnum("Java 1.0", 100);
      ValuedEnum enum2 = new JavaVersionEnum("Java 1.1", 110);
      ValuedEnum enum3 = new JavaVersionEnum("Java 1.2", 120);
      ValuedEnum enum4 = new JavaVersionEnum("Java 1.3", 130);
      
      assertTrue(enum1.compareTo(enum2) < 0);
      assertTrue(enum2.compareTo(enum1) > 0);
      assertTrue(enum3.compareTo(enum3) == 0);
      assertTrue(enum3.compareTo(enum4) < 0);
      assertTrue(enum4.compareTo(enum3) > 0);
   }
   
   @Test
   public void testToString() {
      ValuedEnum enum1 = new JavaVersionEnum("Java 1.0", 100);
      String expected1 = "JavaVersionEnum[Java 1.0=100]";
      assertEquals(expected1, enum1.toString());
      
      ValuedEnum enum2 = new JavaVersionEnum("Java 1.1", 110);
      String expected2 = "JavaVersionEnum[Java 1.1=110]";
      assertEquals(expected2, enum2.toString());
      
      ValuedEnum enum3 = new JavaVersionEnum("Java 1.2", 120);
      String expected3 = "JavaVersionEnum[Java 1.2=120]";
      assertEquals(expected3, enum3.toString());
      
      ValuedEnum enum4 = new JavaVersionEnum("Java 1.3", 130);
      String expected4 = "JavaVersionEnum[Java 1.3=130]";
      assertEquals(expected4, enum4.toString());
   }
}