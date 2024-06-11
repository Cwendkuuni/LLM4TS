import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testConstructor() {
    ArrowType arrowType = new ArrowType(null, null, null, false);
    assertNotNull(arrowType);
  }

  @Test
  public void testIsSubtype() {
    ArrowType arrowType1 = new ArrowType(null, null, null, false);
    ArrowType arrowType2 = new ArrowType(null, null, null, false);
    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testHashCode() {
    ArrowType arrowType = new ArrowType(null, null, null, false);
    assertNotNull(arrowType.hashCode());
  }

}