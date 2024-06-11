import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.Node;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeWithEmptyParameterAndUnknownReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node emptyParameters = registry.createParameters();
    ArrowType arrowType = new ArrowType(registry, emptyParameters, null);

    // Test isSubtype with another ArrowType with empty parameters and unknown return type
    ArrowType arrowTypeToTest = new ArrowType(registry, emptyParameters, null);
    assertTrue(arrowType.isSubtype(arrowTypeToTest));

    // Test isSubtype with a different ArrowType
    ArrowType differentArrowType = new ArrowType(registry, emptyParameters, new IntType(registry));
    assertFalse(arrowType.isSubtype(differentArrowType));

    // Test isSubtype with a FunctionType
    FunctionType functionType = new FunctionType(registry, new ArrayType(registry, new IntType(registry)), new IntType(registry));
    assertFalse(arrowType.isSubtype(functionType));

    // Test hasEqualParameters with another ArrowType with empty parameters and unknown return type
    ArrowType arrowTypeToTest2 = new ArrowType(registry, emptyParameters, null);
    assertTrue(arrowType.hasEqualParameters(arrowTypeToTest2));

    // Test isEquivalentTo with another ArrowType with empty parameters and unknown return type
    ArrowType arrowTypeToTest3 = new ArrowType(registry, emptyParameters, null);
    assertTrue(arrowType.isEquivalentTo(arrowTypeToTest3));
  }

  @Test
  public void testArrowTypeWithSingleParameterAndUnknownReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node singleParameter = registry.createParameter("p");
    ArrowType arrowType = new ArrowType(registry, singleParameter, null);

    // Test isSubtype with another ArrowType with single parameter and unknown return type
    ArrowType arrowTypeToTest = new ArrowType(registry, singleParameter, null);
    assertTrue(arrowType.isSubtype(arrowTypeToTest));

    // Test isSubtype with a different ArrowType
    ArrowType differentArrowType = new ArrowType(registry, singleParameter, new IntType(registry));
    assertFalse(arrowType.isSubtype(differentArrowType));

    // Test isSubtype with a FunctionType
    FunctionType functionType = new FunctionType(registry, new ArrayType(registry, new IntType(registry)), new IntType(registry));
    assertFalse(arrowType.isSubtype(functionType));

    // Test hasEqualParameters with another ArrowType with single parameter and unknown return type
    ArrowType arrowTypeToTest2 = new ArrowType(registry, singleParameter, null);
    assertTrue(arrowType.hasEqualParameters(arrowTypeToTest2));

    // Test isEquivalentTo with another ArrowType with single parameter and unknown return type
    ArrowType arrowTypeToTest3 = new ArrowType(registry, singleParameter, null);
    assertTrue(arrowType.isEquivalentTo(arrowTypeToTest3));
  }

  @Test
  public void testArrowTypeWithSingleParameterAndKnownReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node singleParameter = registry.createParameter("p");
    ArrowType arrowType = new ArrowType(registry, singleParameter, new IntType(registry));

    // Test isSubtype with another ArrowType with single parameter and unknown return type
    ArrowType arrowTypeToTest = new ArrowType(registry, singleParameter, null);
    assertFalse(arrowType.isSubtype(arrowTypeToTest));

    // Test isSubtype with a different ArrowType with single parameter and known return type
    ArrowType differentArrowType = new ArrowType(registry, singleParameter, new StringType(registry));
    assertFalse(arrowType.isSubtype(differentArrowType));

    // Test isSubtype with a FunctionType with a single parameter and known return type
    FunctionType functionType = new FunctionType(registry, new ArrayType(registry, new IntType(registry)), new IntType(registry));
    assertFalse(arrowType.isSubtype(functionType));

    // Test hasEqualParameters with another ArrowType with single parameter and known return type
    ArrowType arrowTypeToTest2 = new ArrowType(registry, singleParameter, new IntType(registry));
    assertFalse(arrowType.hasEqualParameters(arrowTypeToTest2));

    // Test isEquivalentTo with another ArrowType with single parameter and known return type
    ArrowType arrowTypeToTest3 = new ArrowType(registry, singleParameter, new IntType(registry));
    assertFalse(arrowType.isEquivalentTo(arrowTypeToTest3));
  }

  @Test
  public void testArrowTypeWithVarArgsParameterAndUnknownReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node varArgsParameter = registry.createParameterWithVarArgs("params");
    ArrowType arrowType = new ArrowType(registry, varArgsParameter, null);

    // Test isSubtype with another ArrowType with var args parameter and unknown return type
    ArrowType arrowTypeToTest = new ArrowType(registry, varArgsParameter, null);
    assertTrue(arrowType.isSubtype(arrowTypeToTest));

    // Test isSubtype with a different ArrowType
    ArrowType differentArrowType = new ArrowType(registry, varArgsParameter, new IntType(registry));
    assertFalse(arrowType.isSubtype(differentArrowType));

    // Test isSubtype with a FunctionType
    FunctionType functionType = new FunctionType(registry, new ArrayType(registry, new IntType(registry)), new IntType(registry));
    assertFalse(arrowType.isSubtype(functionType));

    // Test hasEqualParameters with another ArrowType with var args parameter and unknown return type
    ArrowType arrowTypeToTest2 = new ArrowType(registry, varArgsParameter, null);
    assertTrue(arrowType.hasEqualParameters(arrowTypeToTest2));

    // Test isEquivalentTo with another ArrowType with var args parameter and unknown return type
    ArrowType arrowTypeToTest3 = new ArrowType(registry, varArgsParameter, null);
    assertTrue(arrowType.isEquivalentTo(arrowTypeToTest3));
  }

  @Test
  public void testArrowTypeWithVarArgsParameterAndKnownReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node varArgsParameter = registry.createParameterWithVarArgs("params");
    ArrowType arrowType = new ArrowType(registry, varArgsParameter, new IntType(registry));

    // Test isSubtype with another ArrowType with var args parameter and known return type
    ArrowType arrowTypeToTest = new ArrowType(registry, varArgsParameter, new IntType(registry));
    assertTrue(arrowType.isSubtype(arrowTypeToTest));

    // Test isSubtype with a different ArrowType with var args parameter and known return type
    ArrowType differentArrowType = new ArrowType(registry, varArgsParameter, new StringType(registry));
    assertFalse(arrowType.isSubtype(differentArrowType));

    // Test isSubtype with a FunctionType with var args parameter and known return type
    FunctionType functionType = new FunctionType(registry, new ArrayType(registry, new IntType(registry)), new IntType(registry));
    assertFalse(arrowType.isSubtype(functionType));

    // Test hasEqualParameters with another ArrowType with var args parameter and known return type
    ArrowType arrowTypeToTest2 = new ArrowType(registry, varArgsParameter, new IntType(registry));
    assertFalse(arrowType.hasEqualParameters(arrowTypeToTest2));

    // Test isEquivalentTo with another ArrowType with var args parameter and known return type
    ArrowType arrowTypeToTest3 = new ArrowType(registry, varArgsParameter, new IntType(registry));
    assertFalse(arrowType.isEquivalentTo(arrowTypeToTest3));
  }

  @Test
  public void testArrowTypeWithMultipleParametersAndUnknownReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node firstParameter = registry.createParameter("p1");
    Node secondParameter = registry.createParameter("p2");
    ArrowType arrowType = new ArrowType(registry, new ArrayType(registry, firstParameter, secondParameter), null);

    // Test isSubtype with another ArrowType with multiple parameters and unknown return type
    ArrowType arrowTypeToTest = new ArrowType(registry, new ArrayType(registry, firstParameter, secondParameter), null);
    assertTrue(arrowType.isSubtype(arrowTypeToTest));

    // Test isSubtype with a different ArrowType
    ArrowType differentArrowType = new ArrowType(registry, new ArrayType(registry, firstParameter, secondParameter), new IntType(registry));
    assertFalse(arrowType.isSubtype(differentArrowType));

    // Test isSubtype with a FunctionType
    FunctionType functionType = new FunctionType(registry, new ArrayType(registry, new IntType(registry), new IntType(registry)), new IntType(registry));
    assertFalse(arrowType.isSubtype(functionType));

    // Test hasEqualParameters with another ArrowType with multiple parameters and unknown return type
    ArrowType arrowTypeToTest2 = new ArrowType(registry, new ArrayType(registry, firstParameter, secondParameter), null);
    assertTrue(arrowType.hasEqualParameters(arrowTypeToTest2));

    // Test isEquivalentTo with another ArrowType with multiple parameters and unknown return type
    ArrowType arrowTypeToTest3 = new ArrowType(registry, new ArrayType(registry, firstParameter, secondParameter), null);
    assertTrue(arrowType.isEquivalentTo(arrowTypeToTest3));
  }

  @Test
  public void testArrowTypeWithMultipleParametersAndKnownReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node firstParameter = registry.createParameter("p1");
    Node secondParameter = registry.createParameter("p2");
    ArrowType arrowType = new ArrowType(registry, new ArrayType(registry, firstParameter, secondParameter), new IntType(registry));

    // Test isSubtype with another ArrowType with multiple parameters and known return type
    ArrowType arrowTypeToTest = new ArrowType(registry, new ArrayType(registry, firstParameter, secondParameter), new IntType(registry));
    assertTrue(arrowType.isSubtype(arrowTypeToTest));

    // Test isSubtype with a different ArrowType
    ArrowType differentArrowType = new ArrowType(registry, new ArrayType(registry, firstParameter, secondParameter), new StringType(registry));
    assertFalse(arrowType.isSubtype(differentArrowType));

    // Test isSubtype with a FunctionType with multiple parameters and known return type
    FunctionType functionType = new FunctionType(registry, new ArrayType(registry, new IntType(registry), new IntType(registry)), new IntType(registry));
    assertFalse(arrowType.isSubtype(functionType));

    // Test hasEqualParameters with another ArrowType with multiple parameters and known return type
    ArrowType arrowTypeToTest2 = new ArrowType(registry, new ArrayType(registry, firstParameter, secondParameter), new IntType(registry));
    assertFalse(arrowType.hasEqualParameters(arrowTypeToTest2));

    // Test isEquivalentTo with another ArrowType with multiple parameters and known return type
    ArrowType arrowTypeToTest3 = new ArrowType(registry, new ArrayType(registry, firstParameter, secondParameter), new IntType(registry));
    assertFalse(arrowType.isEquivalentTo(arrowTypeToTest3));
  }
}