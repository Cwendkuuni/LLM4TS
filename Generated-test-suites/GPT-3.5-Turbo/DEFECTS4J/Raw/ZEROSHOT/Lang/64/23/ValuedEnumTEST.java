import org.junit.Test;
import org.junit.Assert;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

public class ValuedEnumTest {

  @Test
  public void testGetValue() {
    JavaVersionEnum version = JavaVersionEnum.JAVA1_0;
    Assert.assertEquals(100, version.getValue());
  }

  @Test
  public void testGetEnum() {
    JavaVersionEnum version = JavaVersionEnum.getEnum("Java 1.1");
    Assert.assertEquals(JavaVersionEnum.JAVA1_1, version);

    version = JavaVersionEnum.getEnum(120);
    Assert.assertEquals(JavaVersionEnum.JAVA1_2, version);
  }

  @Test
  public void testGetEnumMap() {
    Map map = JavaVersionEnum.getEnumMap();
    Assert.assertEquals(4, map.size());

    JavaVersionEnum version = (JavaVersionEnum) map.get("Java 1.0");
    Assert.assertEquals(JavaVersionEnum.JAVA1_0, version);
  }

  @Test
  public void testGetEnumList() {
    List list = JavaVersionEnum.getEnumList();
    Assert.assertEquals(4, list.size());

    JavaVersionEnum version = (JavaVersionEnum) list.get(2);
    Assert.assertEquals(JavaVersionEnum.JAVA1_2, version);
  }

  @Test
  public void testIterator() {
    Iterator iterator = JavaVersionEnum.iterator();
    Assert.assertTrue(iterator.hasNext());

    JavaVersionEnum version = (JavaVersionEnum) iterator.next();
    Assert.assertEquals(JavaVersionEnum.JAVA1_0, version);
  }
}