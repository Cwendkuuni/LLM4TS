import static org.junit.Assert.*;
import org.junit.Test;

public class ValuedEnumTest {

  @Test
  public void testGetValue() {
    JavaVersionEnum javaVersionEnum_1 = JavaVersionEnum.getEnum(100);
    assertEquals(100, javaVersionEnum_1.getValue());

    JavaVersionEnum javaVersionEnum_2 = JavaVersionEnum.getEnum(110);
    assertEquals(110, javaVersionEnum_2.getValue());

    JavaVersionEnum javaVersionEnum_3 = JavaVersionEnum.getEnum(120);
    assertEquals(120, javaVersionEnum_3.getValue());
  }

  @Test
  public void testCompareTo() {
    JavaVersionEnum javaVersionEnum_1 = JavaVersionEnum.getEnum(100);
    JavaVersionEnum javaVersionEnum_2 = JavaVersionEnum.getEnum(110);
    JavaVersionEnum javaVersionEnum_3 = JavaVersionEnum.getEnum(120);

    assertTrue(javaVersionEnum_1.compareTo(javaVersionEnum_2) < 0);
    assertTrue(javaVersionEnum_2.compareTo(javaVersionEnum_3) < 0);
    assertTrue(javaVersionEnum_1.compareTo(javaVersionEnum_3) < 0);
  }

  @Test
  public void testToString() {
    JavaVersionEnum javaVersionEnum_1 = JavaVersionEnum.getEnum(100);
    assertEquals("JavaVersionEnum[Java 1.0=100]", javaVersionEnum_1.toString());

    JavaVersionEnum javaVersionEnum_2 = JavaVersionEnum.getEnum(110);
    assertEquals("JavaVersionEnum[Java 1.1=110]", javaVersionEnum_2.toString());

    JavaVersionEnum javaVersionEnum_3 = JavaVersionEnum.getEnum(120);
    assertEquals("JavaVersionEnum[Java 1.2=120]", javaVersionEnum_3.toString());
  }
}