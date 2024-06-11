import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeRegistryImpl;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {

  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;
  private StaticScope<JSType> scope;

  @Before
  public void setUp() {
    registry = new JSTypeRegistryImpl();
    errorReporter = mock(ErrorReporter.class);
    scope = mock(StaticScope.class);
  }

  @Test
  public void testArrowTypeConstructorWithNullParametersAndReturnType() {
    ArrowType arrowType = new ArrowType(registry, null, null);

    assertEquals("ArrowType", arrowType.getClass().getSimpleName());
    assertNull(arrowType.parameters);
    assertNull(arrowType.returnType);
    assertFalse(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeConstructorWithParametersAndReturnType() {
    Node nodeParameters = Node.newNode("(int, float)");
    JSType typeParameters = new JSTypeRegistryImpl().createParameters(nodeParameters);
    JSType typeReturn = new JSTypeRegistryImpl().createStringType();

    ArrowType arrowType = new ArrowType(registry, nodeParameters, typeReturn);

    assertEquals("ArrowType", arrowType.getClass().getSimpleName());
    assertEquals(nodeParameters, arrowType.parameters);
    assertEquals(typeReturn, arrowType.returnType);
    assertFalse(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeConstructorWithParametersAndInferredReturnType() {
    Node nodeParameters = Node.newNode("(int, float)");
    ArrowType arrowType = new ArrowType(registry, nodeParameters, null, true);

    assertEquals("ArrowType", arrowType.getClass().getSimpleName());
    assertEquals(nodeParameters, arrowType.parameters);
    assertNull(arrowType.returnType);
    assertTrue(arrowType.returnTypeInferred);
  }

  @Test
  public void testIsSubtype_ArrowTypeToUnknownType() {
    ArrowType arrowType = new ArrowType(registry, null, null);
    JSType typeUnknown = registry.createUnknownType();

    assertFalse(arrowType.isSubtype(typeUnknown));
  }

  @Test
  public void testIsSubtype_ArrowTypeToArrowTypeWithEqualParametersAndReturnType() {
    Node nodeParameters = Node.newNode("(int, float)");
    JSType typeParameters = new JSTypeRegistryImpl().createParameters(nodeParameters);
    JSType typeReturn = new JSTypeRegistryImpl().createNumberType();

    ArrowType arrowTypeA = new ArrowType(registry, nodeParameters, typeReturn);
    ArrowType arrowTypeB = new ArrowType(registry, nodeParameters, typeReturn);

    assertTrue(arrowTypeA.isSubtype(arrowTypeB));
    assertTrue(arrowTypeB.isSubtype(arrowTypeA));
  }

  @Test
  public void testIsSubtype_ArrowTypeToArrowTypeWithDifferentReturnType() {
    Node nodeParameters = Node.newNode("(int, float)");
    JSType typeParameters = new JSTypeRegistryImpl().createParameters(nodeParameters);
    JSType typeReturnA = new JSTypeRegistryImpl().createNumberType();
    JSType typeReturnB = new JSTypeRegistryImpl().createStringType();

    ArrowType arrowTypeA = new ArrowType(registry, nodeParameters, typeReturnA);
    ArrowType arrowTypeB = new ArrowType(registry, nodeParameters, typeReturnB);

    assertFalse(arrowTypeA.isSubtype(arrowTypeB));
    assertFalse(arrowTypeB.isSubtype(arrowTypeA));
  }

  @Test
  public void testIsSubtype_ArrowTypeToArrowTypeWithDifferentParameters() {
    Node nodeParametersA = Node.newNode("(int, float)");
    JSType typeParametersA = new JSTypeRegistryImpl().createParameters(nodeParametersA);
    Node nodeParametersB = Node.newNode("(float, int)");
    JSType typeParametersB = new JSTypeRegistryImpl().createParameters(nodeParametersB);
    JSType typeReturn = new JSTypeRegistryImpl().createNumberType();

    ArrowType arrowTypeA = new ArrowType(registry, nodeParametersA, typeReturn);
    ArrowType arrowTypeB = new ArrowType(registry, nodeParametersB, typeReturn);

    assertFalse(arrowTypeA.isSubtype(arrowTypeB));
    assertFalse(arrowTypeB.isSubtype(arrowTypeA));
  }

  @Test
  public void testIsSubtype_ArrowTypeToFunctionTypeWithDifferentReturnType() {
    Node nodeParameters = Node.newNode("(int, float)");
    JSType typeParameters = new JSTypeRegistryImpl().createParameters(nodeParameters);
    JSType typeFunctionReturn = new JSTypeRegistryImpl().createNumberType();
    JSType typeArrowReturn = new JSTypeRegistryImpl().createStringType();

    ArrowType arrowType = new ArrowType(registry, nodeParameters, typeArrowReturn);
    JSType functionType = new FunctionType(registry, typeParameters, typeFunctionReturn);

    assertFalse(arrowType.isSubtype(functionType));
    assertFalse(functionType.isSubtype(arrowType));
  }

  @Test
  public void testIsSubtype_ArrowTypeToFunctionTypeWithEqualReturnType() {
    Node nodeParameters = Node.newNode("(int, float)");
    JSType typeParameters = new JSTypeRegistryImpl().createParameters(nodeParameters);
    JSType typeFunctionReturn = new JSTypeRegistryImpl().createNumberType();
    JSType typeArrowReturn = typeFunctionReturn;

    ArrowType arrowType = new ArrowType(registry, nodeParameters, typeArrowReturn);
    JSType functionType = new FunctionType(registry, typeParameters, typeFunctionReturn);

    assertTrue(arrowType.isSubtype(functionType));
    assertTrue(functionType.isSubtype(arrowType));
  }

  @Test
  public void testIsSubtype_ArrowTypeToFunctionTypeWithReturnTypeInferred() {
    Node nodeParameters = Node.newNode("(int, float)");
    JSType typeParameters = new JSTypeRegistryImpl().createParameters(nodeParameters);
    JSType typeArrowReturn = new JSTypeNative(JSTypeNative.UNKNOWN_TYPE);
    JSType typeFunctionReturn = new FunctionType(registry, typeParameters, null);

    ArrowType arrowType = new ArrowType(registry, nodeParameters, typeArrowReturn, true);
    JSType functionType = new FunctionType(registry, typeParameters, null);

    assertTrue(arrowType.isSubtype(functionType));
    assertTrue(functionType.isSubtype(arrowType));
  }

  @Test
  public void testIsSubtype_ArrowTypeToArrowTypeWithVarArgs() {
    Node nodeParametersA = Node.newNode("(int, float, ...varArgsInt)");
    JSType typeParametersA = new JSTypeRegistryImpl().createParameters(nodeParametersA);
    Node nodeParametersB = Node.newNode("(int, float)");
    JSType typeParametersB = new JSTypeRegistryImpl().createParameters(nodeParametersB);
    JSType typeReturn = new JSTypeRegistryImpl().createNumberType();

    ArrowType arrowTypeA = new ArrowType(registry, nodeParametersA, typeReturn);
    ArrowType arrowTypeB = new ArrowType(registry, nodeParametersB, typeReturn);

    assertFalse(arrowTypeA.isSubtype(arrowTypeB));
    assertFalse(arrowTypeB.isSubtype(arrowTypeA));
  }

  @Test
  public void testIsSubtype_ArrowTypeToArrowTypeWithOptionalArg() {
    Node nodeParametersA = Node.newNode("(int, float, number?)");
    JSType typeParametersA = new JSTypeRegistryImpl().createParameters(nodeParametersA);
    Node nodeParametersB = Node.newNode("(int, float)");
    JSType typeParametersB = new JSTypeRegistryImpl().createParameters(nodeParametersB);
    JSType typeReturn = new JSTypeRegistryImpl().createNumberType();

    ArrowType arrowTypeA = new ArrowType(registry, nodeParametersA, typeReturn);
    ArrowType arrowTypeB = new ArrowType(registry, nodeParametersB, typeReturn);

    assertFalse(arrowTypeA.isSubtype(arrowTypeB));
    assertFalse(arrowTypeB.isSubtype(arrowTypeA));
  }

  @Test
  public void testHasEqualParameters_ArrowTypeToArrowTypeWithEqualParameters() {
    Node nodeParameters = Node.newNode("(int, float)");
    JSType typeParametersA = new JSTypeRegistryImpl().createParameters(nodeParameters);
    JSType typeParametersB = new JSTypeRegistryImpl().createParameters(nodeParameters);
    ArrowType arrowTypeA = new ArrowType(registry, nodeParameters, null);
    ArrowType arrowTypeB = new ArrowType(registry, nodeParameters, null);

    assertTrue(arrowTypeA.hasEqualParameters(arrowTypeB, false));
    assertTrue(arrowTypeB.hasEqualParameters(arrowTypeA, false));
  }

  @Test
  public void testHasEqualParameters_ArrowTypeToArrowTypeWithUnequalParameters() {
    Node nodeParametersA = Node.newNode("(int, float)");
    Node nodeParametersB = Node.newNode("(float, int)");
    JSType typeParametersA = new JSTypeRegistryImpl().createParameters(nodeParametersA);
    JSType typeParametersB = new JSTypeRegistryImpl().createParameters(nodeParametersB);
    ArrowType arrowTypeA = new ArrowType(registry, nodeParametersA, null);
    ArrowType arrowTypeB = new ArrowType(registry, nodeParametersB, null);

    assertFalse(arrowTypeA.hasEqualParameters(arrowTypeB, false));
    assertFalse(arrowTypeB.hasEqualParameters(arrowTypeA, false));
  }

  @Test
  public void testHasEqualParameters_ArrowTypeToArrowTypeWithOptionalArg() {
    Node nodeParametersA = Node.newNode("(int, float, number?)");
    Node nodeParametersB = Node.newNode("(int, float)");
    JSType typeParametersA = new JSTypeRegistryImpl().createParameters(nodeParametersA);
    JSType typeParametersB = new JSTypeRegistryImpl().createParameters(nodeParametersB);
    ArrowType arrowTypeA = new ArrowType(registry, nodeParametersA, null);
    ArrowType arrowTypeB = new ArrowType(registry, nodeParametersB, null);

    assertFalse(arrowTypeA.hasEqualParameters(arrowTypeB, false));
    assertFalse(arrowTypeB.hasEqualParameters(arrowTypeA, false));
  }

  @Test
  public void testHasEqualParameters_ArrowTypeToArrowTypeWithOptionalArgAndTolerateUnknowns() {
    Node nodeParametersA = Node.newNode("(int, float, number?)");
    Node nodeParametersB = Node.newNode("(int, float, number?)");
    JSType typeParametersA = new JSTypeRegistryImpl().createParameters(nodeParametersA);
    JSType typeParametersB = new JSTypeRegistryImpl().createParameters(nodeParametersB);
    ArrowType arrowTypeA = new ArrowType(registry, nodeParametersA, null);
    ArrowType arrowTypeB = new ArrowType(registry, nodeParametersB, null);

    assertTrue(arrowTypeA.hasEqualParameters(arrowTypeB, true));
    assertTrue(arrowTypeB.hasEqualParameters(arrowTypeA, true));
  }

  @Test
  public void testHashCode() {
    JSType typeReturnA = new JSTypeRegistryImpl().createNumberType();
    JSType typeReturnB = new JSTypeRegistryImpl().createStringType();
    Node nodeParametersA = Node.newNode("(int, float)");
    JSType typeParametersA = new JSTypeRegistryImpl().createParameters(nodeParametersA);
    Node nodeParametersB = Node.newNode("(float, int)");
    JSType typeParametersB = new JSTypeRegistryImpl().createParameters(nodeParametersB);

    ArrowType arrowTypeA = new ArrowType(registry, nodeParametersA, typeReturnA);
    ArrowType arrowTypeB = new ArrowType(registry, nodeParametersB, typeReturnB);

    assertEquals(arrowTypeA.hashCode(), arrowTypeA.hashCode());
    assertEquals(arrowTypeA.hashCode(), arrowTypeA.hashCode());
    assertEquals(arrowTypeA.hashCode(), arrowTypeA.hashCode());
    assertEquals(arrowTypeA.hashCode(), arrowTypeA.hashCode());

    assertNotEquals(arrowTypeA.hashCode(), arrowTypeB.hashCode());
    assertNotEquals(arrowTypeB.hashCode(), arrowTypeA.hashCode());
  }

  @Test
  public void testGetLeastSupertype() {
    throw new UnsupportedOperationException("ArrowType does not support getLeastSupertype()");
  }

  @Test
  public void testGetGreatestSubtype() {
    throw new UnsupportedOperationException("ArrowType does not support getGreatestSubtype()");
  }

  @Test
  public void testToString() {
    Node nodeParameters = Node.newNode("(int, float)");
    JSType typeParameters = new JSTypeRegistryImpl().createParameters(nodeParameters);
    JSType typeReturn = new JSTypeRegistryImpl().createNumberType();
    ArrowType arrowType = new ArrowType(registry, nodeParameters, typeReturn);

    assertEquals("ArrowType[parameters=(int, float), returnType=Number]", arrowType.toString());
  }

  @Test
  public void testHasAnyTemplateInternal() {
    Node nodeParameters = Node.newNode("(int, float)");
    JSType typeParameters = new JSTypeRegistryImpl().createParameters(nodeParameters);
    JSType typeReturn = new JSTypeRegistryImpl().createStringType();
    ArrowType arrowType = new ArrowType(registry, nodeParameters, typeReturn);

    assertFalse(arrowType.hasAnyTemplateInternal());

    typeParameters = new JSTypeRegistryImpl().createTemplateParameters(Node.newNode("T"));
    arrowType = new ArrowType(registry, nodeParameters, typeReturn);

    assertTrue(arrowType.hasAnyTemplateInternal());
  }

  @Test
  public void testResolveInternal() {
    JSType typeReturn = new JSTypeRegistryImpl().createNumberType();
    Node nodeParameters = Node.newNode("(int, float)");
    JSType typeParameters = new JSTypeRegistryImpl().createParameters(nodeParameters);
    ArrowType arrowType = new ArrowType(registry, nodeParameters, typeReturn);

    when(scope.getType(nodeParameters)).thenReturn(typeParameters);

    arrowType.resolveInternal(errorReporter, scope);

    assertEquals(typeParameters, arrowType.parameters);
    assertEquals(typeReturn, arrowType.returnType);
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    Node nodeParameters = Node.newNode("(int, float)");
    JSType typeParameters = new JSTypeRegistryImpl().createParameters(nodeParameters);
    JSType typeReturn = null;
    ArrowType arrowType = new ArrowType(registry, nodeParameters, typeReturn);

    assertFalse(arrowType.hasUnknownParamsOrReturn());

    typeReturn = new JSTypeRegistryImpl().createUnknownType();
    arrowType = new ArrowType(registry, nodeParameters, typeReturn);

    assertTrue(arrowType.hasUnknownParamsOrReturn());

    nodeParameters = Node.newNode("(int, float, ...varArgsInt)");
    arrowType = new ArrowType(registry, nodeParameters, typeReturn);

    assertTrue(arrowType.hasUnknownParamsOrReturn());
  }
}