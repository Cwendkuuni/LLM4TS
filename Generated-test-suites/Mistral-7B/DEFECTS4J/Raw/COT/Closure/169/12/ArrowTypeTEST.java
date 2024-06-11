import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createArrowFunctionType;
import static com.google.javascript.rhino.NodeFactory.createJSTypeRegistry;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.NodeFactory.createParametersWithVarArgs;
import static com.google.javascript.rhino.NodeFactory.createUnknownType;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeConstruction() {
    JSTypeRegistry registry = createJSTypeRegistry();
    Node parameters = createParametersWithVarArgs(registry.createParametersWithVarArgs(createUnknownType()));
    JSType returnType = createNativeType(Integer.TYPE);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    assertEquals(ArrowType.class, arrowType.getClass());
    assertEquals(parameters, arrowType.parameters);
    assertEquals(returnType, arrowType.returnType);
    assertEquals(false, arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeWithInferredReturnType() {
    JSTypeRegistry registry = createJSTypeRegistry();
    Node parameters = createNode(createUnknownType());
    ArrowType arrowType = new ArrowType(registry, parameters, null, true);
    assertEquals(ArrowType.class, arrowType.getClass());
    assertEquals(parameters, arrowType.parameters);
    assertNull(arrowType.returnType);
    assertTrue(arrowType.returnTypeInferred);
  }

  @Test
  public void testIsSubtype_ArrowTypeToSelf() {
    JSTypeRegistry registry = createJSTypeRegistry();
    Node parameters = createParametersWithVarArgs(createUnknownType());
    JSType returnType = createNativeType(Integer.TYPE);
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_ArrowTypeToArrowTypeWithSameParameters() {
    JSTypeRegistry registry = createJSTypeRegistry();
    Node parameters = createParametersWithVarArgs(createUnknownType());
    JSType returnType1 = createNativeType(Integer.TYPE);
    JSType returnType2 = createNativeType(Boolean.TYPE);
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType2);
    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_ArrowTypeToArrowTypeWithDifferentParameters() {
    JSTypeRegistry registry = createJSTypeRegistry();
    Node parameters1 = createParametersWithVarArgs(
        createNode(createUnknownType()), createNode(createUnknownType()));
    JSType returnType = createNativeType(Integer.TYPE);
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType);

    Node parameters2 = createParametersWithVarArgs(createUnknownType());
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType);
    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_ArrowTypeWithVarArgsToArrowTypeWithoutVarArgs() {
    JSTypeRegistry registry = createJSTypeRegistry();
    Node parameters1 = createParametersWithVarArgs(createUnknownType());
    JSType returnType = createNativeType(Integer.TYPE);
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType);

    Node parameters2 = createNode(createUnknownType());
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType);
    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_ArrowTypeWithOptionalParameterToArrowTypeWithoutOptionalParameter() {
    JSTypeRegistry registry = createJSTypeRegistry();
    Node parameters1 = createNode(
        createNode(createUnknownType(), "optional"),
        createParametersWithVarArgs(createUnknownType()));
    JSType returnType = createNativeType(Integer.TYPE);
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType);

    Node parameters2 = createParametersWithVarArgs(createUnknownType());
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType);
    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_ArrowTypeWithUnknownTypeToArrowTypeWithKnownType() {
    JSTypeRegistry registry = createJSTypeRegistry();
    Node parameters = createParametersWithVarArgs(createUnknownType());
    ArrowType arrowType1 = new ArrowType(registry, parameters, createUnknownType());
    ArrowType arrowType2 = new ArrowType(registry, parameters, createNativeType(Integer.TYPE));
    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_ArrowTypeWithUnknownReturnTypeToArrowTypeWithKnownReturnType() {
    JSTypeRegistry registry = createJSTypeRegistry();
    Node parameters = createParametersWithVarArgs(createUnknownType());
    ArrowType arrowType1 = new ArrowType(registry, parameters, createUnknownType());
    ArrowType arrowType2 = new ArrowType(registry, parameters, createNativeType(Integer.TYPE));
    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_ArrowTypeWithKnownTypeToArrowTypeWithUnknownType() {
    JSTypeRegistry registry = createJSTypeRegistry();
    Node parameters = createParametersWithVarArgs(createUnknownType());
    JSType returnType = createNativeType(Integer.TYPE);
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters, createUnknownType());
    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtype_ArrowTypeWithKnownTypeToArrowTypeWithKnownType() {
    JSTypeRegistry registry = createJSTypeRegistry();
    Node parameters = createParametersWithVarArgs(createUnknownType());
    JSType returnType1 = createNativeType(Integer.TYPE);
    JSType returnType2 = createNativeType(Boolean.TYPE);
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType2);
    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testHasEqualParameters() {
    JSTypeRegistry registry = createJSTypeRegistry();

    Node parameters1 = createParametersWithVarArgs(
        createNode(createUnknownType(), "a"),
        createNode(createUnknownType(), "b"),
        createNode(createUnknownType(), "c"));
    Node parameters2 = createParametersWithVarArgs(
        createNode(createUnknownType(), "a"),
        createNode(createUnknownType(), "b"),
        createNode(createUnknownType(), "c"));

    ArrowType arrowType1 = new ArrowType(registry, parameters1, createUnknownType());
    ArrowType arrowType2 = new ArrowType(registry, parameters2, createUnknownType());
    assertTrue(arrowType1.hasEqualParameters(arrowType2, false));
    assertTrue(arrowType2.hasEqualParameters(arrowType1, false));

    Node parameters3 = createParametersWithVarArgs(
        createNode(createUnknownType(), "a"),
        createNode(createUnknownType(), "b"),
        createNode(createUnknownType(), "d"));

    ArrowType arrowType3 = new ArrowType(registry, parameters3, createUnknownType());
    assertFalse(arrowType1.hasEqualParameters(arrowType3, false));
    assertFalse(arrowType3.hasEqualParameters(arrowType1, false));

    Node parameters4 = createParametersWithVarArgs(
        createNode(createUnknownType(), "a"),
        createNode(createUnknownType(), "b"),
        createNode(createParametersWithVarArgs(createUnknownType())));

    ArrowType arrowType4 = new ArrowType(registry, parameters4, createUnknownType());
    assertFalse(arrowType1.hasEqualParameters(arrowType4, false));
    assertFalse(arrowType4.hasEqualParameters(arrowType1, false));

    ArrowType arrowType5 = new ArrowType(registry, parameters1, createUnknownType());
    assertTrue(arrowType1.hasEqualParameters(arrowType5, true));
    assertTrue(arrowType5.hasEqualParameters(arrowType1, true));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype() {
    JSTypeRegistry registry = createJSTypeRegistry();
    Node parameters = createParametersWithVarArgs(createUnknownType());
    ArrowType arrowType = new ArrowType(registry, parameters, createUnknownType());
    arrowType.getLeastSupertype(createNativeType(Integer.TYPE));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype() {
    JSTypeRegistry registry = createJSTypeRegistry();
    Node parameters = createParametersWithVarArgs(createUnknownType());
    ArrowType arrowType = new ArrowType(registry, parameters, createUnknownType());
    arrowType.getGreatestSubtype(createNativeType(Integer.TYPE));
  }

  @Test
  public void testToStringHelper() {
    JSTypeRegistry registry = createJSTypeRegistry();
    Node parameters = createParametersWithVarArgs(createUnknownType());
    ArrowType arrowType = new ArrowType(registry, parameters, createUnknownType());
    assertEquals("[ArrowType]", arrowType.toStringHelper(false));
  }

  @Test
  public void testHashCode() {
    JSTypeRegistry registry = createJSTypeRegistry();
    Node parameters = createParametersWithVarArgs(createUnknownType());
    JSType returnType1 = createNativeType(Integer.TYPE);
    JSType returnType2 = createNativeType(Boolean.TYPE);
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType2);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();

    ArrowType arrowType3 = new ArrowType(registry, parameters, returnType1);
    assertEquals(hashCode1, arrowType3.hashCode());

    JSType returnType3 = createNativeType(Double.TYPE);
    ArrowType arrowType4 = new ArrowType(registry, parameters, returnType3);
    assertNotEquals(hashCode1, arrowType4.hashCode());

    Node parameters2 = createParametersWithVarArgs(createUnknownType(), createUnknownType());
    ArrowType arrowType5 = new ArrowType(registry, parameters2, returnType1);
    assertNotEquals(hashCode1, arrowType5.hashCode());

    ArrowType arrowType6 = new ArrowType(registry, parameters, returnType1, true);
    assertNotEquals(hashCode1, arrowType6.hashCode());
  }

  @Test
  public void testHasAnyTemplateInternal() {
    JSTypeRegistry registry = createJSTypeRegistry();
    Node parameters = createParametersWithVarArgs(createUnknownType());

    ArrowType arrowType1 = new ArrowType(registry, parameters, createUnknownType());
    assertFalse(arrowType1.hasAnyTemplateInternal());

    JSType templateType = createNativeType(new Object()[0].getClass());
    ArrowType arrowType2 = new ArrowType(registry, parameters, createNativeType(templateType));
    assertTrue(arrowType2.hasAnyTemplateInternal());
  }

  private JSTypeRegistry createJSTypeRegistry() {
    return createJSTypeRegistry(new ErrorReporter() {});
  }

  private NodeFactory nodeFactory() {
    return NodeFactory.getThreadLocal();
  }

  private ErrorReporter errorReporter() {
    return new ErrorReporter() {};
  }

  private JSTypeRegistry createJSTypeRegistry(ErrorReporter errorReporter) {
    return new JSTypeRegistry(null, errorReporter, nodeFactory(), null);
  }

  private JSType createNativeType(Class<?> clazz) {
    return createJSTypeRegistry()
        .lookupNativeType(createUnknownType(), clazz.getName());
  }

  private JSType createUnknownType() {
    return createJSTypeRegistry().lookupNativeType(createUnknownType(), "Object");
  }

  private Node createArrowFunctionType(Node body, Node[] parameters) {
    JSTypeRegistry registry = createJSTypeRegistry();
    return createArrowFunctionType(registry, body, parameters);
  }

  private Node createArrowFunctionType(JSTypeRegistry registry, Node body, Node[] parameters) {
    JSType returnType = createNativeType(Void.TYPE);
    ArrowType arrowType =
        new ArrowType(registry, createParametersWithVarArgs(parameters), returnType);
    return createArrowFunctionType(registry, arrowType, body);
  }

  private Node createArrowFunctionType(JSTypeRegistry registry, ArrowType arrowType, Node body) {
    Node functionNode = registry.createFunctionNode(
        arrowType.parameters, null, body, null, null, null, null);
    nodeFactory().setJSType(functionNode, arrowType);
    return functionNode;
  }
}