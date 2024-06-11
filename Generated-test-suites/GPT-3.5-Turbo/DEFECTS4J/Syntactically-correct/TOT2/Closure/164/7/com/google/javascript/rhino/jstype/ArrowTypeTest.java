import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(Token.BLOCK);
    JSType returnType = getNativeType(UNKNOWN_TYPE);

    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertNotNull(arrowType);
    assertEquals(parameters, arrowType.parameters);
    assertEquals(returnType, arrowType.returnType);
    assertFalse(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeIsSubtype() {
    // Write test cases for isSubtype method checking subtypes of ArrowType
  }

  @Test
  public void testArrowTypeHasEqualParameters() {
    // Write test cases for hasEqualParameters method checking parameter equality
  }

  @Test
  public void testArrowTypeIsEquivalentTo() {
    // Write test cases for isEquivalentTo method checking equivalence
  }

  @Test
  public void testArrowTypeHashCode() {
    // Write test cases for hashCode method
  }

  @Test
  public void testArrowTypeResolveInternal() {
    // Write test cases for resolveInternal method
  }

  @Test
  public void testArrowTypeHasUnknownParamsOrReturn() {
    // Write test cases for hasUnknownParamsOrReturn method
  }

  @Test
  public void testArrowTypeToStringHelper() {
    // Write test cases for toStringHelper method
  }

}