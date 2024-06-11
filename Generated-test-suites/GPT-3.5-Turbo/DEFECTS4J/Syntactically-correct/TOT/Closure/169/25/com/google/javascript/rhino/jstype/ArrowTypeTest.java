import org.junit.Test;
import static org.junit.Assert.*;

public class ArrowTypeTest {

  // Test case for the constructor with all parameters provided
  @Test
  public void testConstructorWithParameters() {
    Node parameters = new Node();
    JSType returnType = new JSType();
    boolean returnTypeInferred = false;

    ArrowType arrowType = new ArrowType(parameters, returnType, returnTypeInferred);

    assertEquals(parameters, arrowType.parameters);
    assertEquals(returnType, arrowType.returnType);
    assertEquals(returnTypeInferred, arrowType.returnTypeInferred);
  }

  // Test case for the constructor with null parameters and return type
  @Test
  public void testConstructorWithNullParametersAndReturnType() {
    Node parameters = null;
    JSType returnType = null;
    boolean returnTypeInferred = false;

    ArrowType arrowType = new ArrowType(parameters, returnType, returnTypeInferred);

    assertNull(arrowType.parameters);
    assertNull(arrowType.returnType);
    assertEquals(returnTypeInferred, arrowType.returnTypeInferred);
  }

  // Test case for the constructor with null parameters and non-null return type
  @Test
  public void testConstructorWithNullParametersAndNonNullReturnType() {
    Node parameters = null;
    JSType returnType = new JSType();
    boolean returnTypeInferred = false;

    ArrowType arrowType = new ArrowType(parameters, returnType, returnTypeInferred);

    assertNull(arrowType.parameters);
    assertEquals(returnType, arrowType.returnType);
    assertEquals(returnTypeInferred, arrowType.returnTypeInferred);
  }

  // Test case for the constructor with non-null parameters and null return type
  @Test
  public void testConstructorWithNonNullParametersAndNullReturnType() {
    Node parameters = new Node();
    JSType returnType = null;
    boolean returnTypeInferred = false;

    ArrowType arrowType = new ArrowType(parameters, returnType, returnTypeInferred);

    assertEquals(parameters, arrowType.parameters);
    assertNull(arrowType.returnType);
    assertEquals(returnTypeInferred, arrowType.returnTypeInferred);
  }

  // Test case for the constructor with return type inferred
  @Test
  public void testConstructorWithReturnTypeInferred() {
    Node parameters = new Node();
    JSType returnType = new JSType();
    boolean returnTypeInferred = true;

    ArrowType arrowType = new ArrowType(parameters, returnType, returnTypeInferred);

    assertEquals(parameters, arrowType.parameters);
    assertEquals(returnType, arrowType.returnType);
    assertEquals(returnTypeInferred, arrowType.returnTypeInferred);
  }

  // Test case for isSubtype() method
  @Test
  public void testIsSubtype() {
    // Create two arrow types with different return types
    Node parameters = new Node();
    JSType returnType1 = new JSType();
    ArrowType arrowType1 = new ArrowType(parameters, returnType1, false);
    JSType returnType2 = new JSType();
    ArrowType arrowType2 = new ArrowType(parameters, returnType2, false);

    assertFalse(arrowType1.isSubtype(arrowType2));
  }

  // Test case for hasEqualParameters() method with equal parameters
  @Test
  public void testHasEqualParametersWithEqualParameters() {
    Node parameters1 = new Node();
    parameters1.put("param1", new JSType());
    Node parameters2 = new Node();
    parameters2.put("param1", new JSType());

    ArrowType arrowType1 = new ArrowType(parameters1, new JSType(), false);
    ArrowType arrowType2 = new ArrowType(parameters2, new JSType(), false);

    assertTrue(arrowType1.hasEqualParameters(arrowType2, true));
  }

  // Test case for hasEqualParameters() method with unequal parameters
  @Test
  public void testHasEqualParametersWithUnequalParameters() {
    Node parameters1 = new Node();
    parameters1.put("param1", new JSType());
    Node parameters2 = new Node();
    parameters2.put("param2", new JSType());

    ArrowType arrowType1 = new ArrowType(parameters1, new JSType(), false);
    ArrowType arrowType2 = new ArrowType(parameters2, new JSType(), false);

    assertFalse(arrowType1.hasEqualParameters(arrowType2, true));
  }

  // Test case for checkArrowEquivalenceHelper() method
  @Test
  public void testCheckArrowEquivalenceHelper() {
    // Create two arrow types with equal parameters and return types
    Node parameters = new Node();
    parameters.put("param", new JSType());
    JSType returnType = new JSType();
    ArrowType arrowType1 = new ArrowType(parameters, returnType, false);
    ArrowType arrowType2 = new ArrowType(parameters, returnType, false);

    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));
  }

  // Test case for hashCode() method
  @Test
  public void testHashCode() {
    Node parameters = new Node();
    parameters.put("param", new JSType());
    JSType returnType = new JSType();
    ArrowType arrowType = new ArrowType(parameters, returnType, false);

    int expectedHashCode = parameters.hashCode() + returnType.hashCode();

    assertEquals(expectedHashCode, arrowType.hashCode());
  }

  // Test case for hasUnknownParamsOrReturn() method with unknown parameters and return type
  @Test
  public void testHasUnknownParamsOrReturnWithUnknownParamsAndReturnType() {
    Node parameters = new Node();
    parameters.put("param", new JSType());
    JSType returnType = new JSType();

    ArrowType arrowType = new ArrowType(parameters, returnType, false);

    assertFalse(arrowType.hasUnknownParamsOrReturn());
  }

  // Test case for hasUnknownParamsOrReturn() method with null parameters and return type
  @Test
  public void testHasUnknownParamsOrReturnWithNullParamsAndReturnType() {
    Node parameters = null;
    JSType returnType = null;

    ArrowType arrowType = new ArrowType(parameters, returnType, false);

    assertTrue(arrowType.hasUnknownParamsOrReturn());
  }

  // Test case for toStringHelper() method
  @Test
  public void testToStringHelper() {
    Node parameters = new Node();
    JSType returnType = new JSType();
    boolean returnTypeInferred = false;

    ArrowType arrowType = new ArrowType(parameters, returnType, returnTypeInferred);

    assertEquals("[ArrowType]", arrowType.toStringHelper(false));
  }

  // Test case for hasAnyTemplateInternal() method with no template type
  @Test
  public void testHasAnyTemplateInternalWithNoTemplateType() {
    Node parameters = new Node();
    JSType returnType = new JSType();
    boolean returnTypeInferred = false;

    ArrowType arrowType = new ArrowType(parameters, returnType, returnTypeInferred);

    assertFalse(arrowType.hasAnyTemplateInternal());
  }

  // Test case for hasAnyTemplateInternal() method with template type in parameters
  @Test
  public void testHasAnyTemplateInternalWithTemplateTypeInParameter() {
    Node parameters = new Node();
    parameters.put("param1", new JSType(true));
    parameters.put("param2", new JSType());
    JSType returnType = new JSType();
    boolean returnTypeInferred = false;

    ArrowType arrowType = new ArrowType(parameters, returnType, returnTypeInferred);

    assertTrue(arrowType.hasAnyTemplateInternal());
  }
}