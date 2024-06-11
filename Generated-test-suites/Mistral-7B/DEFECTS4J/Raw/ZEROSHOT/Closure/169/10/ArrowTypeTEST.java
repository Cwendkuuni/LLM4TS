import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeNative;

import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newParametersNode(
        Node.newNameNode("a"),
        Node.newNameNode("b")
    );
    ArrowType arrowType = new ArrowType(registry, parameters, JSTypeNative.INT);
    assertEquals(JSTypeNative.INT, arrowType.getReturnType());
    assertFalse(arrowType.isReturnTypeInferred());
    assertEquals(parameters, arrowType.getParameters());
  }

  @Test
  public void testArrowTypeCreationWithInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newParametersNode(
        Node.newNameNode("a"),
        Node.newNameNode("b")
    );
    ArrowType arrowType = new ArrowType(registry, parameters, null, true);
    assertNull(arrowType.getReturnType());
    assertTrue(arrowType.isReturnTypeInferred());
    assertEquals(parameters, arrowType.getParameters());
  }

  @Test
  public void testArrowTypeIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersA = Node.newParametersNode(
        Node.newNameNode("a"),
        Node.newNameNode("b")
    );
    Node parametersB = Node.newParametersNode(
        Node.newNameNode("b"),
        Node.newNameNode("a")
    );

    ArrowType arrowTypeA = new ArrowType(registry, parametersA, JSTypeNative.INT);
    ArrowType arrowTypeB = new ArrowType(registry, parametersB, JSTypeNative.INT);

    assertTrue(arrowTypeA.isSubtype(arrowTypeB));
    assertFalse(arrowTypeB.isSubtype(arrowTypeA));

    arrowTypeA = new ArrowType(registry, parametersA, JSTypeNative.INT);
    arrowTypeB = new ArrowType(registry, parametersB, JSTypeNative.BOOLEAN);

    assertFalse(arrowTypeA.isSubtype(arrowTypeB));

    arrowTypeA = new ArrowType(registry, parametersA, JSTypeNative.INT);
    arrowTypeB = new ArrowType(registry, Node.newNull(), JSTypeNative.INT);

    assertTrue(arrowTypeA.isSubtype(arrowTypeB));

    arrowTypeA = new ArrowType(registry, parametersA, JSTypeNative.INT);
    arrowTypeB = new ArrowType(registry, Node.newNull(), JSTypeNative.VOID);

    assertFalse(arrowTypeA.isSubtype(arrowTypeB));

    arrowTypeA = new ArrowType(registry, Node.newNull(), JSTypeNative.INT);
    arrowTypeB = new ArrowType(registry, Node.newNull(), JSTypeNative.INT);

    assertTrue(arrowTypeA.isSubtype(arrowTypeB));
    assertTrue(arrowTypeB.isSubtype(arrowTypeA));
  }

  @Test
  public void testArrowTypeHasEqualParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersA = Node.newParametersNode(
        Node.newNameNode("a"),
        Node.newNameNode("b")
    );
    Node parametersB = Node.newParametersNode(
        Node.newNameNode("b"),
        Node.newNameNode("a")
    );
    ArrowType arrowTypeA = new ArrowType(registry, parametersA, JSTypeNative.INT);
    ArrowType arrowTypeB = new ArrowType(registry, parametersB, JSTypeNative.INT);

    assertFalse(arrowTypeA.hasEqualParameters(arrowTypeB, false));
    assertFalse(arrowTypeA.hasEqualParameters(arrowTypeB, true));

    arrowTypeB = new ArrowType(registry, parametersA, JSTypeNative.INT);

    assertTrue(arrowTypeA.hasEqualParameters(arrowTypeB, false));
    assertTrue(arrowTypeA.hasEqualParameters(arrowTypeB, true));
  }

  @Test
  public void testArrowTypeHashCode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newParametersNode(
        Node.newNameNode("a"),
        Node.newNameNode("b")
    );
    ArrowType arrowTypeA = new ArrowType(registry, parameters, JSTypeNative.INT);
    ArrowType arrowTypeB = new ArrowType(registry, parameters, JSTypeNative.INT);

    int hashCodeA = arrowTypeA.hashCode();
    int hashCodeB = arrowTypeB.hashCode();

    assertEquals(hashCodeA, hashCodeB);

    arrowTypeA = new ArrowType(registry, Node.newNull(), JSTypeNative.INT);
    arrowTypeB = new ArrowType(registry, Node.newNull(), JSTypeNative.INT);

    assertEquals(hashCodeA, hashCodeB);

    arrowTypeA = new ArrowType(registry, parameters, JSTypeNative.INT);
    arrowTypeB = new ArrowType(registry, parameters, JSTypeNative.BOOLEAN);

    assertNotEquals(hashCodeA, hashCodeB);
  }

  @Test
  public void testArrowTypeResolve() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newParametersNode(
        Node.newNameNode("a"),
        Node.newNameNode("b")
    );
    ArrowType arrowType = new ArrowType(registry, parameters, JSTypeNative.INT);

    ErrorReporter errorReporter = new ErrorReporter() {};
    StaticScope<JSType> scope = new StaticScope<>(registry);

    arrowType.resolveInternal(errorReporter, scope);

    JSType resolvedReturnType = arrowType.getReturnType();
    assertTrue(resolvedReturnType instanceof JSTypeNative);
    assertEquals(JSTypeNative.INT, resolvedReturnType);

    Node resolvedParameterA = arrowType.getParameters().getFirstChild();
    JSType resolvedParameterAType = resolvedParameterA.getJSType();
    assertTrue(resolvedParameterAType instanceof JSTypeNative);
    assertEquals(JSTypeNative.ANY, resolvedParameterAType);

    Node resolvedParameterB = arrowType.getParameters().getNext();
    JSType resolvedParameterBType = resolvedParameterB.getJSType();
    assertTrue(resolvedParameterBType instanceof JSTypeNative);
    assertEquals(JSTypeNative.ANY, resolvedParameterBType);
  }

  @Test
  public void testArrowTypeWithUnknownParamsOrReturn() {
    JSTypeRegistry registry = new JSTypeRegistry();

    ArrowType arrowTypeA = new ArrowType(registry, Node.newNull(), null);
    assertTrue(arrowTypeA.hasUnknownParamsOrReturn());

    ArrowType arrowTypeB = new ArrowType(registry, Node.newNameNode("a"), null);
    assertFalse(arrowTypeB.hasUnknownParamsOrReturn());

    ArrowType arrowTypeC = new ArrowType(registry, Node.newParametersNode(
        Node.newNameNode("a"),
        Node.newNameNode("b")
    ), null);
    assertFalse(arrowTypeC.hasUnknownParamsOrReturn());

    ArrowType arrowTypeD = new ArrowType(registry, Node.newParametersNode(
        Node.newNameNode("a"),
        Node.newNameNode("b"),
        Node.newNameNode("c")
    ), null);
    assertFalse(arrowTypeD.hasUnknownParamsOrReturn());

    ArrowType arrowTypeE = new ArrowType(registry, Node.newParametersNode(
        Node.newNameNode("a"),
        Node.newNameNode("b"),
        Node.newNameNode("c"),
        Node.newNameNode("d")
    ), null);
    assertFalse(arrowTypeE.hasUnknownParamsOrReturn());

    ArrowType arrowTypeF = new ArrowType(registry, Node.newParametersNode(
        Node.newNameNode("a"),
        Node.newNameNode("b"),
        Node.newNameNode("c"),
        Node.newNameNode("d"),
        Node.newNameNode("e")
    ), null);
    assertFalse(arrowTypeF.hasUnknownParamsOrReturn());

    ArrowType arrowTypeG = new ArrowType(registry, Node.newParametersNode(
        Node.newNameNode("a"),
        Node.newNameNode("b"),
        Node.newNameNode("c"),
        Node.newNameNode("d"),
        Node.newNameNode("e"),
        Node.newNameNode("f")
    ), null);
    assertFalse(arrowTypeG.hasUnknownParamsOrReturn());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testArrowTypeGetLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, Node.newNull(), JSTypeNative.INT);
    arrowType.getLeastSupertype(JSTypeNative.VOID);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testArrowTypeGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, Node.newNull(), JSTypeNative.INT);
    arrowType.getGreatestSubtype(JSTypeNative.VOID);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testArrowTypeTestForEquality() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, Node.newNull(), JSTypeNative.INT);
    arrowType.testForEquality(JSTypeNative.VOID);
  }

  @Test
  public void testArrowTypeVisit() {
    JSTypeRegistry registry = new JSTypeRegistry();

    ArrowType arrowType = new ArrowType(registry, Node.newNull(), JSTypeNative.INT);

    ArrowTypeVisitor arrowTypeVisitor = new ArrowTypeVisitor();
    arrowType.visit(arrowTypeVisitor);

    assertNotNull(arrowTypeVisitor.getReturnType());
    assertEquals(JSTypeNative.INT, arrowTypeVisitor.getReturnType());

    assertNull(arrowTypeVisitor.getParameters());
  }

  @Test
  public void testArrowTypeToString() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newParametersNode(
        Node.newNameNode("a"),
        Node.newNameNode("b")
    );
    ArrowType arrowType = new ArrowType(registry, parameters, JSTypeNative.INT);

    assertEquals("[ArrowType(parameters=[a, b], returnType=int)]", arrowType.toString());
  }
}

class ArrowTypeVisitor extends AbstractTypeVisitor<JSType> {
  JSType returnType;

  @Override
  public JSType visitArrowType(ArrowType arrowType, JSTypeRegistry registry) {
    this.returnType = arrowType.getReturnType();
    return super.visitArrowType(arrowType, registry);
  }

  public JSType getReturnType() {
    return returnType;
  }
}