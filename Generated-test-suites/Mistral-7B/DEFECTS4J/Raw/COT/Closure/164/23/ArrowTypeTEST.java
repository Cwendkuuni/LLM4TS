import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;

import org.junit.Test;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ArrowType;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = Node.newList();
    ArrowType arrowType = new ArrowType(registry, parametersNode, getNativeType(INT), false);

    assertEquals("ArrowType{parameters=[], returnType=INT, returnTypeInferred=false}", arrowType.toString());
  }

  @Test
  public void testArrowTypeCreationWithParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = Node.newList(
        Node.newNamed("param1", INT),
        Node.newNamed("param2", STRING)
    );
    ArrowType arrowType = new ArrowType(registry, parametersNode, getNativeType(BOOL), false);

    assertEquals("ArrowType{parameters=[param1: INT, param2: STRING], returnType=BOOL, returnTypeInferred=false}", arrowType.toString());
  }

  @Test
  public void testArrowTypeCreationWithInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parametersNode = Node.newList(Node.newNamed("param", INT));
    ArrowType arrowType = new ArrowType(registry, parametersNode, null, true);

    assertEquals("ArrowType{parameters=[param: INT], returnType=undefined, returnTypeInferred=true}", arrowType.toString());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testIsSubtypeThrowsException() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType1 = new ArrowType(registry, null, INT, false);
    ArrowType arrowType2 = new ArrowType(registry, null, BOOL, false);

    arrowType1.isSubtype(arrowType2);
  }

  @Test
  public void testIsSubtypeSimpleCase() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType1 = new ArrowType(registry, null, INT, false);
    ArrowType arrowType2 = new ArrowType(registry, null, INT, false);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtypeWithDifferentReturnTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType1 = new ArrowType(registry, null, INT, false);
    ArrowType arrowType2 = new ArrowType(registry, null, BOOL, false);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtypeWithDifferentParameterTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType1 = new ArrowType(registry, Node.newList(Node.newNamed("param1", INT)), getNativeType(INT), false);
    ArrowType arrowType2 = new ArrowType(registry, Node.newList(Node.newNamed("param1", STRING)), getNativeType(INT), false);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtypeWithMismatchedVarArgs() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType1 = new ArrowType(registry, Node.newList(Node.newNamed("param1", INT), Node.newVarArgs()), getNativeType(INT), false);
    ArrowType arrowType2 = new ArrowType(registry, Node.newList(Node.newNamed("param1", INT)), getNativeType(INT), false);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testIsSubtypeWithMatchingVarArgs() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType1 = new ArrowType(registry, Node.newList(Node.newNamed("param1", INT), Node.newVarArgs()), getNativeType(INT), false);
    ArrowType arrowType2 = new ArrowType(registry, Node.newList(Node.newNamed("param1", INT), Node.newVarArgs()), getNativeType(INT), false);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testHasEqualParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType1 = new ArrowType(registry, Node.newList(Node.newNamed("param1", INT), Node.newNamed("param2", INT)), getNativeType(INT), false);
    ArrowType arrowType2 = new ArrowType(registry, Node.newList(Node.newNamed("param2", INT), Node.newNamed("param1", INT)), getNativeType(INT), false);
    ArrowType arrowType3 = new ArrowType(registry, Node.newList(Node.newNamed("param1", INT), Node.newNamed("param2", INT)), getNativeType(INT), false);

    assertTrue(arrowType1.hasEqualParameters(arrowType3));
    assertTrue(arrowType3.hasEqualParameters(arrowType1));
    assertFalse(arrowType1.hasEqualParameters(arrowType2));
    assertFalse(arrowType2.hasEqualParameters(arrowType1));
  }

  @Test
  public void testIsEquivalentToSimpleCase() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType1 = new ArrowType(registry, null, INT, false);
    ArrowType arrowType2 = new ArrowType(registry, null, INT, false);

    assertTrue(arrowType1.isEquivalentTo(arrowType2));
    assertTrue(arrowType2.isEquivalentTo(arrowType1));
  }

  @Test
  public void testIsEquivalentToWithDifferentReturnTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType1 = new ArrowType(registry, null, INT, false);
    ArrowType arrowType2 = new ArrowType(registry, null, BOOL, false);

    assertFalse(arrowType1.isEquivalentTo(arrowType2));
    assertFalse(arrowType2.isEquivalentTo(arrowType1));
  }

  @Test
  public void testIsEquivalentToWithDifferentParameterTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType1 = new ArrowType(registry, Node.newList(Node.newNamed("param1", INT)), getNativeType(INT), false);
    ArrowType arrowType2 = new ArrowType(registry, Node.newList(Node.newNamed("param1", STRING)), getNativeType(INT), false);

    assertFalse(arrowType1.isEquivalentTo(arrowType2));
    assertFalse(arrowType2.isEquivalentTo(arrowType1));
  }

  @Test
  public void testHashCode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType1 = new ArrowType(registry, null, INT, false);
    ArrowType arrowType2 = new ArrowType(registry, null, INT, false);

    assertEquals(arrowType1.hashCode(), arrowType2.hashCode());

    ArrowType arrowType3 = new ArrowType(registry, Node.newList(Node.newNamed("param1", INT)), getNativeType(INT), false);
    assertNotEquals(arrowType1.hashCode(), arrowType3.hashCode());
  }

  @Test
  public void testGetLeastSupertype() {
    throw new UnsupportedOperationException();
  }

  @Test
  public void testGetGreatestSubtype() {
    throw new UnsupportedOperationException();
  }

  @Test
  public void testToString() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, INT, false);

    assertEquals("ArrowType{parameters=[], returnType=INT, returnTypeInferred=false}", arrowType.toString());
  }

  @Test
  public void testToStringWithParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, Node.newList(Node.newNamed("param1", INT), Node.newNamed("param2", STRING)), getNativeType(INT), false);

    assertEquals("ArrowType{parameters=[param1: INT, param2: STRING], returnType=INT, returnTypeInferred=false}", arrowType.toString());
  }

  @Test
  public void testToStringWithInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, Node.newList(), null, true);

    assertEquals("ArrowType{parameters=[], returnType=undefined, returnTypeInferred=true}", arrowType.toString());
  }

  @Test
  public void testResolveInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter errorReporter = new ErrorReporter() {};
    StaticScope<JSType> scope = new StaticScope<JSType>(JSTypeNative.UNDEFINED) {};
    ArrowType arrowType = new ArrowType(registry, null, INT, false);

    arrowType.resolveInternal(errorReporter, scope);

    assertTrue(arrowType.returnType instanceof IntType);
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, Node.newList(), null, true);

    assertTrue(arrowType.hasUnknownParamsOrReturn());

    arrowType = new ArrowType(registry, null, getNativeType(UNKNOWN_TYPE), false);

    assertTrue(arrowType.hasUnknownParamsOrReturn());
  }

  @Test
  public void testHasUnknownParamsOrReturnWithParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, Node.newList(Node.newNamed("param", INT), Node.newVarArgs()), null, true);

    assertTrue(arrowType.hasUnknownParamsOrReturn());
  }
}