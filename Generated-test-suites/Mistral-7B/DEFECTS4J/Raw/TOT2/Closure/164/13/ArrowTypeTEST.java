import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticallyTypedScope;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeNative;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {
  private ErrorReporter errorReporter;
  private JSTypeRegistry registry;

  @Before
  public void setup() {
    errorReporter = new ErrorReporter();
    registry = new JSTypeRegistry(errorReporter);
  }

  @Test
  public void testArrowTypeConstructor_nullParameters_createsEmptyFunctionType() {
    ArrowType arrowType = new ArrowType(registry, null, null);
    assertNotNull(arrowType);
    assertTrue(arrowType.isFunction());
    assertNull(arrowType.parameters);
    assertEquals(JSTypeNative.UNKNOWN_TYPE, arrowType.returnType);
    assertFalse(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeConstructor_nullReturnType_createsArrowTypeWithUnknownTypeReturn() {
    ArrowType arrowType = new ArrowType(registry, Node.newList(), null);
    assertNotNull(arrowType);
    assertTrue(arrowType.isFunction());
    assertNull(arrowType.parameters);
    assertNull(arrowType.returnType);
    assertTrue(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeConstructor_validParametersAndReturnType_createsArrowType() {
    Node parameters = Node.newList(
        Node.newName("a"),
        Node.newName("b")
    );
    JSType returnType = registry.createStringType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    assertNotNull(arrowType);
    assertTrue(arrowType.isFunction());
    assertEquals(parameters, arrowType.parameters);
    assertEquals(returnType, arrowType.returnType);
    assertFalse(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeIsSubtype_arrowTypesWithEqualSignatures_areSubtypes() {
    ArrowType arrowType1 = new ArrowType(registry, Node.newList(), Node.INT_TYPE);
    ArrowType arrowType2 = new ArrowType(registry, Node.newList(), Node.INT_TYPE);
    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testArrowTypeIsSubtype_arrowTypeWithMoreRestrictiveParametersAndSameReturnType_isSubtype() {
    ArrowType arrowType1 = new ArrowType(registry, Node.newList(Node.newName("a")), Node.INT_TYPE);
    ArrowType arrowType2 = new ArrowType(registry, Node.newList(), Node.INT_TYPE);
    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testArrowTypeIsSubtype_arrowTypeWithLessRestrictiveParametersAndSameReturnType_isSubtype() {
    ArrowType arrowType1 = new ArrowType(registry, Node.newList(Node.newName("a"), Node.newName("b")), Node.INT_TYPE);
    ArrowType arrowType2 = new ArrowType(registry, Node.newList(), Node.INT_TYPE);
    assertTrue(arrowType2.isSubtype(arrowType1));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testArrowTypeGetLeastSupertype() {
    ArrowType arrowType = new ArrowType(registry, Node.newList(), Node.INT_TYPE);
    arrowType.getLeastSupertype(Node.INT_TYPE);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testArrowTypeGetGreatestSubtype() {
    ArrowType arrowType = new ArrowType(registry, Node.newList(), Node.INT_TYPE);
    arrowType.getGreatestSubtype(Node.INT_TYPE);
  }

  @Test
  public void testArrowTypeToString() {
    Node parameters = Node.newList(Node.newName("a"), Node.newName("b"));
    ArrowType arrowType = new ArrowType(registry, parameters, Node.INT_TYPE);
    String expected = "ArrowType([" + parameters.toSource() + "] -> Int)";
    String actual = arrowType.toString();
    assertEquals(expected, actual);
  }

  @Test
  public void testArrowTypeToStringForAnnotations() {
    Node parameters = Node.newList(Node.newName("a"), Node.newName("b"));
    ArrowType arrowType = new ArrowType(registry, parameters, Node.INT_TYPE);
    String expected = "ArrowType([" + parameters.toSource() + "] -> Int)";
    String actual = arrowType.toStringHelper(true);
    assertEquals(expected, actual);
  }

  @Test
  public void testArrowTypeHasEqualParameters_arrowTypeWithEqualParameters_areEqual() {
    Node parameters1 = Node.newList(Node.newName("a"), Node.newName("b"));
    ArrowType arrowType1 = new ArrowType(registry, parameters1, Node.INT_TYPE);
    Node parameters2 = Node.newList(Node.newName("a"), Node.newName("b"));
    ArrowType arrowType2 = new ArrowType(registry, parameters2, Node.INT_TYPE);
    assertTrue(arrowType1.hasEqualParameters(arrowType2));
    assertTrue(arrowType2.hasEqualParameters(arrowType1));
  }

  @Test
  public void testArrowTypeHasEqualParameters_arrowTypesWithDifferentParameters_areNotEqual() {
    Node parameters1 = Node.newList(Node.newName("a"), Node.newName("b"));
    Node parameters2 = Node.newList(Node.newName("a"), Node.newName("c"));
    ArrowType arrowType1 = new ArrowType(registry, parameters1, Node.INT_TYPE);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, Node.INT_TYPE);
    assertFalse(arrowType1.hasEqualParameters(arrowType2));
    assertFalse(arrowType2.hasEqualParameters(arrowType1));
  }

  @Test
  public void testArrowTypeIsEquivalentTo_arrowTypesWithEqualSignaturesAndTypes_areEquivalent() {
    ArrowType arrowType1 = new ArrowType(registry, Node.newList(), Node.INT_TYPE);
    ArrowType arrowType2 = new ArrowType(registry, Node.newList(), Node.INT_TYPE);
    assertTrue(arrowType1.isEquivalentTo(arrowType2));
    assertTrue(arrowType2.isEquivalentTo(arrowType1));
  }

  @Test
  public void testArrowTypeIsEquivalentTo_arrowTypesWithDifferentSignatures_areNotEquivalent() {
    ArrowType arrowType1 = new ArrowType(registry, Node.newList(Node.newName("a")), Node.INT_TYPE);
    ArrowType arrowType2 = new ArrowType(registry, Node.newList(), Node.INT_TYPE);
    assertFalse(arrowType1.isEquivalentTo(arrowType2));
    assertFalse(arrowType2.isEquivalentTo(arrowType1));
  }

  @Test
  public void testArrowTypeHashCode_arrowTypesWithEqualSignaturesAndTypes_haveEqualHashCodes() {
    ArrowType arrowType1 = new ArrowType(registry, Node.newList(), Node.INT_TYPE);
    ArrowType arrowType2 = new ArrowType(registry, Node.newList(), Node.INT_TYPE);
    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();
    assertEquals(hashCode1, hashCode2);
  }

  @Test
  public void testArrowTypeHashCode_arrowTypesWithDifferentSignatures_haveDifferentHashCodes() {
    ArrowType arrowType1 = new ArrowType(registry, Node.newList(Node.newName("a")), Node.INT_TYPE);
    ArrowType arrowType2 = new ArrowType(registry, Node.newList(), Node.INT_TYPE);
    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();
    assertNotEquals(hashCode1, hashCode2);
  }

  @Test
  public void testArrowTypeVisit_visitorReturnsNull() {
    StaticallyTypedScope scope = new StaticallyTypedScope();
    ArrowType arrowType = new ArrowType(registry, Node.newList(), Node.INT_TYPE);
    assertNull(arrowType.visit(new Visitor<Object>() {
      @Override
      public Object visitArrowType(ArrowType arrowType, StaticallyTypedScope scope) {
        return null;
      }
    }));
  }

  @Test
  public void testArrowTypeVisit_visitorReturnsValue() {
    StaticallyTypedScope scope = new StaticallyTypedScope();
    ArrowType arrowType = new ArrowType(registry, Node.newList(), Node.INT_TYPE);
    assertNull(arrowType.visit(new Visitor<Object>() {
      @Override
      public Object visitArrowType(ArrowType arrowType, StaticallyTypedScope scope) {
        return null;
      }

      @Override
      public Object visitIntType(IntType intType, StaticallyTypedScope scope) {
        return "VISITED";
      }
    }));
    assertEquals("VISITED", arrowType.visit(new Visitor<Object>() {
      @Override
      public Object visitArrowType(ArrowType arrowType, StaticallyTypedScope scope) {
        return null;
      }

      @Override
      public Object visitIntType(IntType intType, StaticallyTypedScope scope) {
        return "VISITED";
      }
    }));
  }

  @Test
  public void testArrowTypeTestForEquality_identicalObjects_areEqual() {
    ArrowType arrowType1 = new ArrowType(registry, Node.newList(), Node.INT_TYPE);
    ArrowType arrowType2 = new ArrowType(registry, Node.newList(), Node.INT_TYPE);
    TernaryValue result = arrowType1.testForEquality(arrowType2);
    assertTrue(result.getValue());
  }

  @Test
  public void testArrowTypeTestForEquality_differentObjects_areNotEqual() {
    ArrowType arrowType1 = new ArrowType(registry, Node.newList(), Node.INT_TYPE);
    ArrowType arrowType2 = new ArrowType(registry, Node.newList(), Node.STRING_TYPE);
    TernaryValue result = arrowType1.testForEquality(arrowType2);
    assertFalse(result.getValue());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testArrowTypeTestForEquality_unsupportedOperation() {
    ArrowType arrowType = new ArrowType(registry, Node.newList(), Node.INT_TYPE);
    arrowType.testForEquality(Node.INT_TYPE);
  }

  @Test
  public void testResolveInternal_resolvedTypeParametersAndReturnType() {
    Node parameters = Node.newList(Node.newName("a"), Node.newName("b"));
    ArrowType arrowType = new ArrowType(registry, parameters, Node.INT_TYPE);
    ArrowType resolvedArrowType = arrowType.resolveInternal(errorReporter, new StaticallyTypedScope());
    assertEquals(parameters, resolvedArrowType.parameters);
    assertEquals(Node.INT_TYPE, resolvedArrowType.returnType);
    assertFalse(resolvedArrowType.returnTypeInferred);
  }

  @Test
  public void testResolveInternal_unresolvedParameters_throwsError() {
    ArrowType arrowType = new ArrowType(registry, null, Node.INT_TYPE);
    assertThrows(IllegalArgumentException.class, () -> arrowType.resolveInternal(errorReporter, new StaticallyTypedScope()));
  }

  @Test
  public void testResolveInternal_unresolvedReturnType_throwsError() {
    Node parameters = Node.newList(Node.newName("a"));
    ArrowType arrowType = new ArrowType(registry, parameters, null);
    assertThrows(IllegalArgumentException.class, () -> arrowType.resolveInternal(errorReporter, new StaticallyTypedScope()));
  }
}