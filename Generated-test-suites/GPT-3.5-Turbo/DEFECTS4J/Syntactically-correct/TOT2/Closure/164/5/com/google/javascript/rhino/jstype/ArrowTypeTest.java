import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeCreation() {
    ArrowType arrowType = new ArrowType(null, null, null);
    
    assertNotNull(arrowType);
    assertNull(arrowType.returnType);
    assertFalse(arrowType.returnTypeInferred);
    assertNull(arrowType.parameters);
  }

  @Test
  public void testSubtypeCheck() {
    ArrowType arrowType1 = new ArrowType(null, null, null);
    ArrowType arrowType2 = new ArrowType(null, null, null);

    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testEqualParameters() {
    Node parameters = new Node(nodeType, "param1");
    ArrowType arrowType1 = new ArrowType(null, parameters, null);
    ArrowType arrowType2 = new ArrowType(null, parameters, null);

    assertTrue(arrowType1.hasEqualParameters(arrowType2));
  }

  @Test
  public void testHashCode() {
    ArrowType arrowType = new ArrowType(null, null, null);

    assertEquals(arrowType.hashCode(), 0);
  }

}