import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ArrowType;

public class ArrowTypeTest {

  @org.junit.Test
  public void testArrowTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = new Node(JSTypeNative.createParametersWithVarArgs(UNKNOWN_TYPE));
    Node returnNode = new Node(JSTYPE_VOID);
    ArrowType arrowType = new ArrowType(registry, parametersNode, returnNode);

    assertEquals("Unexpected return type.", JSTYPE_VOID, arrowType.getReturnType());
    assertEquals("Unexpected parameters.", parametersNode, arrowType.getParameters());
    assertFalse("Unexpected inferred return type.", arrowType.isReturnTypeInferred());
  }

  @org.junit.Test
  public void testArrowTypeWithInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = new Node(JSTypeNative.createParametersWithVarArgs(UNKNOWN_TYPE));
    ArrowType arrowType = new ArrowType(registry, parametersNode, null, true);

    assertTrue("Unexpected return type inferred status.", arrowType.isReturnTypeInferred());
    assertEquals("Unexpected return type.", UNKNOWN_TYPE, arrowType.getReturnType());
    assertEquals("Unexpected parameters.", parametersNode, arrowType.getParameters());
  }

  @org.junit.Test
  public void testArrowTypeSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Test arrow type with 'int' return type as a subtype of arrow type with 'number' return type.
    Node intTypeNode = new Node(INT_TYPE);
    Node numberTypeNode = new Node(NUMBER_TYPE);
    ArrowType arrowTypeIntReturn = new ArrowType(registry, null, intTypeNode);
    ArrowType arrowTypeNumberReturn = new ArrowType(registry, null, numberTypeNode);

    assertTrue("Unexpected subtype relationship.", arrowTypeNumberReturn.isSubtype(arrowTypeIntReturn));

    // Test arrow type with 'int' return type as a supertype of arrow type with 'number' return type.
    assertFalse("Unexpected supertype relationship.", arrowTypeIntReturn.isSubtype(arrowTypeNumberReturn));

    // Test arrow type with 'int' parameters and 'int' return type as a subtype of arrow type with 'number' parameters and 'number' return type.
    Node intParamNode = new Node(INT_TYPE);
    ArrowType arrowTypeIntParamsIntReturn = new ArrowType(registry, intParamNode, intTypeNode);
    ArrowType arrowTypeNumberParamsNumberReturn = new ArrowType(registry, null, numberTypeNode);

    assertTrue("Unexpected subtype relationship.", arrowTypeNumberParamsNumberReturn.isSubtype(arrowTypeIntParamsIntReturn));

    // Test arrow type with 'int' parameters and 'int' return type as a supertype of arrow type with 'number' parameters and 'number' return type.
    assertFalse("Unexpected supertype relationship.", arrowTypeIntParamsIntReturn.isSubtype(arrowTypeNumberParamsNumberReturn));

    // Test arrow type with 'int' parameters and 'int' return type as a subtype of arrow type with 'number' parameters and 'int' return type.
    ArrowType arrowTypeNumberParamsIntReturn = new ArrowType(registry, null, intTypeNode);

    assertTrue("Unexpected subtype relationship.", arrowTypeNumberParamsNumberReturn.isSubtype(arrowTypeNumberParamsIntReturn));

    assertTrue("Unexpected subtype relationship.", arrowTypeIntParamsIntReturn.isSubtype(arrowTypeNumberParamsIntReturn));

    // Test arrow type with 'int' parameters and 'int' return type as a supertype of arrow type with 'number' parameters and 'int' return type.
    assertFalse("Unexpected supertype relationship.", arrowTypeNumberParamsIntReturn.isSubtype(arrowTypeIntParamsIntReturn));

    // Test arrow type with 'int' parameters and 'int' return type as a subtype of arrow type with 'int' parameters and 'number' return type.
    ArrowType arrowTypeNumberReturnForIntParams = new ArrowType(registry, null, numberTypeNode);

    assertTrue("Unexpected subtype relationship.", arrowTypeNumberReturnForIntParams.isSubtype(arrowTypeIntParamsIntReturn));

    // Test arrow type with 'int' parameters and 'int' return type as a supertype of arrow type with 'int' parameters and 'number' return type.
    assertFalse("Unexpected supertype relationship.", arrowTypeIntParamsIntReturn.isSubtype(arrowTypeNumberReturnForIntParams));
  }

  @org.junit.Test
  public void testArrowTypeCheckEquivalenceHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Test arrow types with equal parameters and return types.
    Node intTypeNode = new Node(INT_TYPE);
    Node stringTypeNode = new Node(STRING_TYPE);
    ArrowType arrowTypeIntInt = new ArrowType(registry, null, intTypeNode);
    ArrowType arrowTypeIntIntEqual = new ArrowType(registry, null, intTypeNode, null);

    assertTrue("Unexpected equivalence relationship.", arrowTypeIntIntEqual.checkEquivalenceHelper(arrowTypeIntInt, true));

    // Test arrow types with different return types but equal parameters.
    ArrowType arrowTypeIntString = new ArrowType(registry, null, stringTypeNode);

    assertTrue("Unexpected equivalence relationship.", arrowTypeIntIntEqual.checkEquivalenceHelper(arrowTypeIntString, true));

    // Test arrow types with equal return types but different parameters.
    ArrowType arrowTypeStringInt = new ArrowType(registry, null, intTypeNode);

    assertTrue("Unexpected equivalence relationship.", arrowTypeIntInt.checkEquivalenceHelper(arrowTypeStringInt, true));

    // Test arrow types with different parameters and return types.
    ArrowType arrowTypeNumberNumber = new ArrowType(registry, null, numberTypeNode, null);

    assertFalse("Unexpected equivalence relationship.", arrowTypeIntIntEqual.checkEquivalenceHelper(arrowTypeNumberNumber, true));

    // Test arrow types with equal return types and same number of parameters but different types.
    Node booleanTypeNode = new Node(BOOLEAN_TYPE);
    ArrowType arrowTypeIntBoolean = new ArrowType(registry, null, booleanTypeNode, null);

    assertFalse("Unexpected equivalence relationship.", arrowTypeIntIntEqual.checkEquivalenceHelper(arrowTypeIntBoolean, true));

    // Test arrow types with equal return types and different number of parameters.
    ArrowType arrowTypeInt = new ArrowType(registry, null, null, null);

    assertFalse("Unexpected equivalence relationship.", arrowTypeIntIntEqual.checkEquivalenceHelper(arrowTypeInt, true));
  }

  @org.junit.Test
  public void testArrowTypeHashCode() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node intTypeNode = new Node(INT_TYPE);
    Node stringTypeNode = new Node(STRING_TYPE);

    // Test arrow types with equal parameters and return types.
    ArrowType arrowTypeIntInt = new ArrowType(registry, null, intTypeNode);
    ArrowType arrowTypeIntIntEqual = new ArrowType(registry, null, intTypeNode, null);

    int hashCodeIntInt = arrowTypeIntInt.hashCode();
    int hashCodeIntIntEqual = arrowTypeIntIntEqual.hashCode();

    assertEquals("Unexpected hashcode.", hashCodeIntInt, hashCodeIntIntEqual);

    // Test arrow types with different return types but equal parameters.
    ArrowType arrowTypeIntString = new ArrowType(registry, null, stringTypeNode);

    int hashCodeIntString = arrowTypeIntString.hashCode();

    assertEquals("Unexpected hashcode.", hashCodeIntInt, hashCodeIntString);

    // Test arrow types with equal return types but different parameters.
    ArrowType arrowTypeStringInt = new ArrowType(registry, null, intTypeNode);

    int hashCodeStringInt = arrowTypeStringInt.hashCode();

    assertEquals("Unexpected hashcode.", hashCodeIntInt, hashCodeStringInt);

    // Test arrow types with different parameters and return types.
    ArrowType arrowTypeNumberNumber = new ArrowType(registry, null, numberTypeNode, null);

    int hashCodeNumberNumber = arrowTypeNumberNumber.hashCode();

    assertNotEquals("Unexpected hashcode.", hashCodeIntInt, hashCodeNumberNumber);
  }

  @org.junit.Test
  public void testArrowTypeEquals() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node intTypeNode = new Node(INT_TYPE);
    Node stringTypeNode = new Node(STRING_TYPE);

    // Test arrow types with equal parameters and return types.
    ArrowType arrowTypeIntInt = new ArrowType(registry, null, intTypeNode);
    ArrowType arrowTypeIntIntEqual = new ArrowType(registry, null, intTypeNode, null);

    assertTrue("Unexpected equals relationship.", arrowTypeIntInt.equals(arrowTypeIntIntEqual));

    // Test arrow types with different return types but equal parameters.
    ArrowType arrowTypeIntString = new ArrowType(registry, null, stringTypeNode);

    assertFalse("Unexpected equals relationship.", arrowTypeIntInt.equals(arrowTypeIntString));

    // Test arrow types with equal return types but different parameters.
    ArrowType arrowTypeStringInt = new ArrowType(registry, null, intTypeNode);

    assertTrue("Unexpected equals relationship.", arrowTypeIntInt.equals(arrowTypeStringInt));

    // Test arrow types with different parameters and return types.
    ArrowType arrowTypeNumberNumber = new ArrowType(registry, null, numberTypeNode, null);

    assertFalse("Unexpected equals relationship.", arrowTypeIntInt.equals(arrowTypeNumberNumber));

    // Test arrow types with equal return types and same number of parameters but different types.
    Node booleanTypeNode = new Node(BOOLEAN_TYPE);
    ArrowType arrowTypeIntBoolean = new ArrowType(registry, null, booleanTypeNode, null);

    assertFalse("Unexpected equals relationship.", arrowTypeIntInt.equals(arrowTypeIntBoolean));

    // Test arrow types with equal return types and different number of parameters.
    ArrowType arrowTypeInt = new ArrowType(registry, null, null, null);

    assertFalse("Unexpected equals relationship.", arrowTypeIntInt.equals(arrowTypeInt));
  }
}