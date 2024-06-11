import org.junit.Assert;
import org.junit.Test;

public class ValuedEnumTest {

  @Test
  public void testGetValue() {
    JavaVersionEnum javaVersion = JavaVersionEnum.JAVA1_0;
    Assert.assertEquals(100, javaVersion.getValue());
  }

  @Test
  public void testGetEnumByValue() {
    JavaVersionEnum javaVersion = JavaVersionEnum.getEnum(100);
    Assert.assertEquals(JavaVersionEnum.JAVA1_0, javaVersion);
  }

  @Test
  public void testGetEnumByName() {
    JavaVersionEnum javaVersion = JavaVersionEnum.getEnum("Java 1.0");
    Assert.assertEquals(JavaVersionEnum.JAVA1_0, javaVersion);
  }

  @Test
  public void testGetEnumMap() {
    Assert.assertEquals(4, JavaVersionEnum.getEnumMap().size());
  }

  @Test
  public void testGetEnumList() {
    Assert.assertEquals(4, JavaVersionEnum.getEnumList().size());
  }

  @Test
  public void testIterator() {
    Assert.assertTrue(JavaVersionEnum.iterator().hasNext());
  }

  @Test
  public void testCompareTo() {
    JavaVersionEnum javaVersion1 = JavaVersionEnum.JAVA1_0;
    JavaVersionEnum javaVersion2 = JavaVersionEnum.JAVA1_1;
    Assert.assertTrue(javaVersion1.compareTo(javaVersion2) < 0);
  }

  @Test
  public void testToString() {
    JavaVersionEnum javaVersion = JavaVersionEnum.JAVA1_0;
    Assert.assertEquals("JavaVersionEnum[Java 1.0=100]", javaVersion.toString());
  }

}