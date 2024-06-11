import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeNative;
import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {
  JSTypeRegistry registry;
  ErrorReporter errorReporter;

  @Before
  public void setup() {
    registry = new JSTypeRegistry();
    errorReporter = new ErrorReporter() {
      @Override
      public void error(String message, int line, int lineNumber) {
        fail("Unexpected error: " + message);
      }

      @Override
      public void warning(String message, int line, int lineNumber) {
        // Ignore warnings
      }
    };
  }

  @Test
  public void testArrowTypeConstructor_NullParametersAndReturnType() {
    ArrowType arrowType = new ArrowType(registry, null, null);
    assertEquals(ArrowType.class, arrowType.getClass());
    assertNull(arrowType.parameters);
    assertEquals(JSTypeNative.UNKNOWN_TYPE, arrowType.returnType);
    assertTrue(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeConstructor_ParametersAndReturnType() {
    Node parametersNode = new Node(registry.createIdentifier("p"));
    JSType returnType = registry.createStringType();

    ArrowType arrowType = new ArrowType(registry, parametersNode, returnType);
    assertEquals(ArrowType.class, arrowType.getClass());
    assertEquals(parametersNode, arrowType.parameters);
    assertEquals(returnType, arrowType.returnType);
    assertFalse(arrowType.returnTypeInferred);
  }

  @Test
  public void testIsSubtype_ArrowWithDifferentReturnTypes() {
    Node p1 = registry.createIdentifier("p1");
    Node p2 = registry.createIdentifier("p2");
    JSType returnType1 = registry.createNumberType();
    JSType returnType2 = registry.createBooleanType();

    ArrowType arrowType1 = new ArrowType(registry, p1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, p2, returnType2);

    assertFalse(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testIsSubtype_ArrowWithSameReturnTypes() {
    Node p1 = registry.createIdentifier("p1");
    Node p2 = registry.createIdentifier("p2");
    JSType returnType = registry.createNumberType();

    ArrowType arrowType1 = new ArrowType(registry, p1, returnType);
    ArrowType arrowType2 = new ArrowType(registry, p2, returnType);

    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testIsSubtype_ArrowWithDifferentParameterTypes() {
    Node p1 = registry.createIdentifier("p1");
    Node p2 = registry.createIdentifier("p2");
    JSType returnType = registry.createNumberType();

    ArrowType arrowType1 = new ArrowType(registry, p1, returnType);
    ArrowType arrowType2 = new ArrowType(registry, p2, returnType);

    assertFalse(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testIsSubtype_ArrowWithSameParameterTypes() {
    Node p1 = registry.createIdentifier("p1");
    Node p1Copy = registry.createIdentifier("p1");
    JSType returnType = registry.createNumberType();

    ArrowType arrowType1 = new ArrowType(registry, p1, returnType);
    ArrowType arrowType2 = new ArrowType(registry, p1Copy, returnType);

    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @Test
  public void testHasEqualParameters_SameArrowTypes() {
    Node p1 = registry.createIdentifier("p1");
    Node p2 = registry.createIdentifier("p2");
    JSType returnType = registry.createNumberType();

    ArrowType arrowType1 = new ArrowType(registry, p1, returnType);
    ArrowType arrowType2 = new ArrowType(registry, p2, returnType);

    // Both arrow types have the same parameter lists, so they should be equal.
    assertTrue(arrowType1.hasEqualParameters(arrowType2, false));
  }

  @Test
  public void testHasEqualParameters_DifferentArrowTypesWithSameParameterTypes() {
    Node p1 = registry.createIdentifier("p1");
    Node p2 = registry.createIdentifier("p2");
    JSType returnType1 = registry.createNumberType();
    JSType returnType2 = registry.createBooleanType();

    ArrowType arrowType1 = new ArrowType(registry, p1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, p2, returnType2);

    assertFalse(arrowType1.hasEqualParameters(arrowType2, false));
  }

  @Test
  public void testHasEqualParameters_ArrowWithOptionalParameters() {
    Node p1 = registry.createIdentifier("p1");
    Node p2 = registry.createIdentifier("p2");
    JSType returnType = registry.createNumberType();

    ArrowType arrowType1 = new ArrowType(registry, p1, returnType);
    ArrowType arrowType2 = new ArrowType(registry, p2, returnType, true);

    // The second arrow type has an optional parameter, so it should not be
    // considered equal to the first one.
    assertFalse(arrowType1.hasEqualParameters(arrowType2, false));
  }

  @Test
  public void testHasEqualParameters_ArrowWithVarArgs() {
    Node p1 = registry.createIdentifier("p1");
    Node p2 = registry.createIdentifier("p2");
    Node p3 = registry.createIdentifier("p3");
    JSType returnType = registry.createNumberType();

    ArrowType arrowType1 = new ArrowType(registry, p1, p3, returnType);
    ArrowType arrowType2 = new ArrowType(registry, p1, p2, p3, returnType);

    // The second arrow type has an additional parameter, but it is a var_arg,
    // so it should be considered equal to the first one.
    assertTrue(arrowType1.hasEqualParameters(arrowType2, false));
  }

  @Test
  public void testHashCode_SameArrowTypes() {
    Node p1 = registry.createIdentifier("p1");
    Node p2 = registry.createIdentifier("p2");
    JSType returnType1 = registry.createNumberType();
    JSType returnType2 = registry.createBooleanType();

    ArrowType arrowType1 = new ArrowType(registry, p1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, p2, returnType1);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();

    assertEquals(hashCode1, hashCode2);
  }

  @Test
  public void testHashCode_DifferentArrowTypes() {
    Node p1 = registry.createIdentifier("p1");
    Node p2 = registry.createIdentifier("p2");
    JSType returnType1 = registry.createNumberType();
    JSType returnType2 = registry.createBooleanType();

    ArrowType arrowType1 = new ArrowType(registry, p1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, p2, returnType2);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();

    assertNotEquals(hashCode1, hashCode2);
  }
}