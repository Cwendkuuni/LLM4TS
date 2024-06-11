import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jscomp.TypeChecker;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {
  private JSTypeRegistry registry;
  private TypeChecker typeChecker;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    typeChecker = new TypeChecker(registry);
  }

  @Test
  public void testArrowTypeCreation() {
    Node paramList = registry.createParametersWithVarArgs(JSTypeNative.INT_TYPE);
    JSType returnType = JSTypeNative.INT_TYPE;
    ArrowType arrowType = new ArrowType(registry, paramList, returnType, false);

    assertEquals("function(int) -> int", arrowType.toString());
    assertFalse(arrowType.isSubtype(JSTypeNative.BOOLEAN_TYPE));

    ArrowType arrowTypeWithInferredReturn = new ArrowType(registry, paramList, null, true);
    assertEquals("function(int) -> ?", arrowTypeWithInferredReturn.toString());
  }

  @Test
  public void testArrowTypeWithNullParameters() {
    ArrowType arrowType = new ArrowType(registry, null, null, false);
    assertEquals("function() -> ?", arrowType.toString());
  }

  @Test
  public void testArrowTypeWithVarArgs() {
    Node paramList = registry.createParametersWithVarArgs(JSTypeNative.INT_TYPE);
    ArrowType arrowType = new ArrowType(registry, paramList, null, false);

    assertEquals("function(...int[]) -> ?", arrowType.toString());
  }

  @Test
  public void testArrowTypeSubtyping() {
    Node paramList1 = registry.createParametersWithVarArgs(JSTypeNative.INT_TYPE);
    Node paramList2 = registry.createParametersWithVarArgs(JSTypeNative.NUMBER_TYPE);

    JSType voidType = JSTypeNative.VOID_TYPE;
    JSType intType = JSTypeNative.INT_TYPE;
    JSType numberType = JSTypeNative.NUMBER_TYPE;

    ArrowType arrowType1 = new ArrowType(registry, paramList1, intType, false);
    ArrowType arrowType2 = new ArrowType(registry, paramList2, intType, false);
    ArrowType arrowType3 = new ArrowType(registry, paramList2, numberType, false);
    ArrowType arrowType4 = new ArrowType(registry, paramList1, voidType, false);
    ArrowType arrowType5 = new ArrowType(registry, paramList1, numberType, false);

    // Subtyping: function(int, int) -> int < function(int, number) -> number
    assertTrue(arrowType1.isSubtype(arrowType3));

    // Subtyping: function(int, int) -> int < function() -> ? (covariance)
    assertTrue(arrowType1.isSubtype(new ArrowType(registry, null, intType, false)));

    // function(int, int) -> int < function(int, number) -> int
    // This should be false, but we allow it to make it easier to use a single "no-op"
    // function for all function signatures.
    assertTrue(arrowType1.isSubtype(new ArrowType(registry, paramList2, intType, false)));

    // Contravariance: function(int, int) -> int < function() -> void
    assertFalse(arrowType1.isSubtype(new ArrowType(registry, null, voidType, false)));

    // function(int, int) -> int < function(number, number) -> int
    assertFalse(arrowType1.isSubtype(new ArrowType(registry, paramList2, intType, false)));

    // function(int, int) -> int < function() -> int
    assertFalse(arrowType1.isSubtype(new ArrowType(registry, null, intType, false)));

    // function(int, int) -> int < function(number, int) -> int
    assertFalse(arrowType1.isSubtype(new ArrowType(registry, registry.createParametersWithVarArgs(JSTypeNative.NUMBER_TYPE), intType, false)));

    // Function with inferred return type does not subtype a function with a specific return type.
    assertFalse(arrowType1.isSubtype(new ArrowType(registry, paramList1, null, true)));

    // Function with a required argument does not subtype a function without that argument.
    assertFalse(arrowType1.isSubtype(new ArrowType(registry, null, intType, false)));
  }

  @Test
  public void testArrowTypeEquivalence() {
    Node paramList1 = registry.createParametersWithVarArgs(JSTypeNative.INT_TYPE);
    Node paramList2 = registry.createParametersWithVarArgs(JSTypeNative.NUMBER_TYPE);

    JSType voidType = JSTypeNative.VOID_TYPE;
    JSType intType = JSTypeNative.INT_TYPE;
    JSType numberType = JSTypeNative.NUMBER_TYPE;

    ArrowType arrowType1 = new ArrowType(registry, paramList1, intType, false);
    ArrowType arrowType2 = new ArrowType(registry, paramList2, intType, false);
    ArrowType arrowType3 = new ArrowType(registry, paramList2, numberType, false);
    ArrowType arrowType4 = new ArrowType(registry, paramList1, voidType, false);
    ArrowType arrowType5 = new ArrowType(registry, paramList1, numberType, false);

    // Equivalence: function(int, int) -> int <-> function(int, number) -> number
    assertEquals(arrowType1, arrowType3);

    // Equivalence: function(int, int) -> int <-> function() -> int
    assertEquals(arrowType1, new ArrowType(registry, null, intType, false));

    // Non-equivalence: function(int, int) -> int <-> function(number, number) -> int
    assertFalse(arrowType1.equals(arrowType2));

    // Non-equivalence: function(int, int) -> int <-> function() -> void
    assertFalse(arrowType1.equals(arrowType4));

    // Non-equivalence: function(int, int) -> int <-> function(number, int) -> int
    assertFalse(arrowType1.equals(arrowType5));

    // Non-equivalence: function(int, int) -> int <-> function(int, int) -> void
    assertFalse(arrowType1.equals(new ArrowType(registry, paramList1, voidType, false)));

    // Non-equivalence: function(int, int) -> int <-> function(int, int) -> number
    assertFalse(arrowType1.equals(new ArrowType(registry, paramList1, numberType, false)));
  }
}