import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeConstructor() {
    Node parameters = new Node(Token.PARAM_LIST);
    JSType returnType = new JSType();
    ArrowType arrowType = new ArrowType(new JSTypeRegistry(), parameters, returnType);

    assertEquals(parameters, arrowType.parameters);
    assertEquals(returnType, arrowType.returnType);
    assertFalse(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeIsSubtype() {
    ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), null, new JSType());
    ArrowType arrowType2 = new ArrowType(new JSTypeRegistry(), null, new JSType());
    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testArrowTypeHashCode() {
    ArrowType arrowType = new ArrowType(new JSTypeRegistry(), null, new JSType());
    int expectedHashCode = arrowType.getReturnType().hashCode();
    expectedHashCode += 1; // returnTypeInferred is true

    assertEquals(expectedHashCode, arrowType.hashCode());
  }

}