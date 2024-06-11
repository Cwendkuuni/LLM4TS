import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.testing.PolyMergedTypes;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {

  private static final JSTypeRegistry registry;
  private JSType returnType;
  private JSType returnTypeInferred;
  private ArrowType arrowType;
  private ArrowType arrowTypeWithNullReturn;
  private ArrowType arrowTypeWithVariableParams;
  private ArrowType arrowTypeWithVariableParamsAndInferredReturn;
  private ArrowType arrowTypeWithVarArgsAndDifferentParamTypes;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();

    JSTypeRegistry polyRegistry = new JSTypeRegistry();
    PolyMergedTypes.registerTypes(polyRegistry);

    JSType NativeInt = registry.createNamedType("Int", JSTypeNative.INT);
    returnType = registry.createNamedType("String", JSTypeNative.STRING);
    returnTypeInferred = registry.createUnknownType();

    Node node1 = registry.createNode("(function(x, y) { return x; })");
    arrowType = new ArrowType(registry, node1.getChild(0), returnType);

    Node node2 = registry.createNode("(function() { return; })");
    arrowTypeWithNullReturn = new ArrowType(registry, null, returnTypeInferred);

    Node node3 = registry.createNode("(function(x, y, z) { return x; })");
    arrowTypeWithVariableParams = new ArrowType(registry, node3.getFirstChild(), returnType);

    Node node4 = registry.createNode("(function(x, y) { return x; })");
    arrowTypeWithVariableParamsAndInferredReturn = new ArrowType(registry, node4.getFirstChild(), returnTypeInferred);

    Node node5 = registry.createNode("(function(a, b) { })");
    arrowTypeWithVarArgsAndDifferentParamTypes = new ArrowType(
        registry, node5.getFirstChild(), returnTypeInferred);
  }

  @Test
  public void testIsSubtype() {
    // ArrowType with known parameters and known return type is a subtype
    // of ArrowType with the same known parameters and the same known return type
    ArrowType arrowType1 = new ArrowType(registry, null, returnType, false);
    ArrowType arrowType2 = new ArrowType(registry, null, returnType, false);
    assertTrue(arrowType1.isSubtype(arrowType2));

    // ArrowType with known parameters and known return type is a subtype
    // of ArrowType with the same known parameters and a covariant return type
    ArrowType arrowTypeWithCovariantReturn = new ArrowType(registry, null, JSType.getVoidType(), false);
    assertTrue(arrowType.isSubtype(arrowTypeWithCovariantReturn));

    // ArrowType with known parameters and known return type is not a subtype
    // of ArrowType with different known parameters
    ArrowType arrowTypeWithDifferentParams = new ArrowType(registry, null, returnType, false);
    assertFalse(arrowType.isSubtype(arrowTypeWithDifferentParams));

    // ArrowType with known parameters and known return type is not a subtype
    // of ArrowType with a contravariant return type
    ArrowType arrowTypeWithContravariantReturn = new ArrowType(registry, null, JSTypeNative.OBJECT, false);
    assertFalse(arrowType.isSubtype(arrowTypeWithContravariantReturn));

    // ArrowType with known parameters and inferred return type is a subtype
    // of ArrowType with the same known parameters and inferred return type
    ArrowType arrowTypeWithInferredReturn = new ArrowType(registry, null, returnTypeInferred, false);
    assertTrue(arrowType.isSubtype(arrowTypeWithInferredReturn));

    // ArrowType with known parameters and inferred return type is a subtype
    // of ArrowType with the same known parameters and a covariant return type
    ArrowType arrowTypeWithCovariantAndInferredReturn = new ArrowType(
        registry, null, JSType.getVoidType(), returnTypeInferred, false);
    assertTrue(arrowType.isSubtype(arrowTypeWithCovariantAndInferredReturn));

    // ArrowType with known parameters and inferred return type is not a subtype
    // of ArrowType with different known parameters
    ArrowType arrowTypeWithDifferentParamsAndInferredReturn = new ArrowType(
        registry, null, JSTypeNative.OBJECT, returnTypeInferred, false);
    assertFalse(arrowType.isSubtype(arrowTypeWithDifferentParamsAndInferredReturn));

    // ArrowType with known parameters and inferred return type is not a subtype
    // of ArrowType with a contravariant return type
    ArrowType arrowTypeWithContravariantAndInferredReturn = new ArrowType(
        registry, null, JSTypeNative.OBJECT, returnTypeInferred, false);
    assertFalse(arrowType.isSubtype(arrowTypeWithContravariantAndInferredReturn));

    // ArrowType with null return type is not a subtype of ArrowType with a known return type
    assertFalse(arrowTypeWithNullReturn.isSubtype(arrowType));

    // ArrowType with variable params is a subtype of ArrowType with the same params and a covariant return type
    assertTrue(arrowTypeWithVariableParams.isSubtype(arrowTypeWithVariableParamsAndInferredReturn));

    // ArrowType with variable params and inferred return type is a subtype of ArrowType with the same params and inferred return type
    assertTrue(arrowTypeWithVariableParamsAndInferredReturn.isSubtype(arrowTypeWithVariableParams));

    // ArrowType with variable params and different param types is not a subtype of ArrowType with the same params
    assertFalse(arrowTypeWithVarArgsAndDifferentParamTypes.isSubtype(arrowTypeWithVariableParams));

    // ArrowType with variable params and different param types is not a subtype of ArrowType with a covariant return type
    assertFalse(arrowTypeWithVarArgsAndDifferentParamTypes.isSubtype(arrowTypeWithCovariantReturn));

    // ArrowType with variable params and different param types is not a subtype of ArrowType with a contravariant return type
    assertFalse(arrowTypeWithVarArgsAndDifferentParamTypes.isSubtype(arrowTypeWithContravariantReturn));

    // ArrowType with variable params and inferred return type is a subtype of ArrowType with the same params and inferred return type
    assertTrue(arrowTypeWithVarArgsAndDifferentParamTypes.isSubtype(arrowTypeWithVariableParamsAndInferredReturn));

    // ArrowType with variable params and inferred return type is a subtype of ArrowType with a covariant return type
    assertTrue(arrowTypeWithVarArgsAndDifferentParamTypes.isSubtype(arrowTypeWithCovariantReturn));

    // ArrowType with variable params and inferred return type is a subtype of ArrowType with a contravariant return type
    assertTrue(arrowTypeWithVarArgsAndDifferentParamTypes.isSubtype(arrowTypeWithContravariantReturn));
  }

  @Test
  public void testHasEqualParameters() {
    // Two ArrowTypes with equal parameter lists have equal parameters
    ArrowType arrowType1 = new ArrowType(registry, null, returnType, false);
    ArrowType arrowType2 = new ArrowType(registry, null, returnType, false);
    assertTrue(arrowType1.hasEqualParameters(arrowType2));

    // Two ArrowTypes with equal parameter lists and inferred return types have equal parameters
    ArrowType arrowTypeWithInferredReturn1 = new ArrowType(registry, null, returnTypeInferred, false);
    ArrowType arrowTypeWithInferredReturn2 = new ArrowType(registry, null, returnTypeInferred, false);
    assertTrue(arrowType1.hasEqualParameters(arrowTypeWithInferredReturn2));

    // Two ArrowTypes with different parameter lists have no equal parameters
    ArrowType arrowTypeWithDifferentParams1 = new ArrowType(registry, null, returnType, false);
    ArrowType arrowTypeWithDifferentParams2 = new ArrowType(registry, null, returnType, false);
    assertFalse(arrowType1.hasEqualParameters(arrowTypeWithDifferentParams2));

    // Two ArrowTypes with different parameter lists and inferred return types have no equal parameters
    ArrowType arrowTypeWithDifferentParamsAndInferredReturn1 = new ArrowType(
        registry, null, returnTypeInferred, false);
    ArrowType arrowTypeWithDifferentParamsAndInferredReturn2 = new ArrowType(
        registry, null, returnTypeInferred, false);
    assertFalse(arrowType1.hasEqualParameters(arrowTypeWithDifferentParamsAndInferredReturn2));

    // ArrowType with variable params and equal parameter lists has equal parameters
    ArrowType arrowTypeWithVariableParams1 = new ArrowType(registry, null, returnType, false);
    ArrowType arrowTypeWithVariableParams2 = new ArrowType(registry, null, returnType, false);
    assertTrue(arrowTypeWithVariableParams1.hasEqualParameters(arrowTypeWithVariableParams2));

    // ArrowType with variable params and inferred return types and equal parameter lists has equal parameters
    ArrowType arrowTypeWithVariableParamsAndInferredReturn1 = new ArrowType(
        registry, null, returnTypeInferred, false);
    ArrowType arrowTypeWithVariableParamsAndInferredReturn2 = new ArrowType(
        registry, null, returnTypeInferred, false);
    assertTrue(arrowTypeWithVariableParams1.hasEqualParameters(arrowTypeWithVariableParamsAndInferredReturn2));

    // ArrowType with variable params and different parameter lists has no equal parameters
    ArrowType arrowTypeWithVariableParamsAndDifferentParams1 = new ArrowType(
        registry, null, returnType, false);
    ArrowType arrowTypeWithVariableParamsAndDifferentParams2 = new ArrowType(
        registry, null, returnType, false);
    assertFalse(arrowTypeWithVariableParams1.hasEqualParameters(arrowTypeWithVariableParamsAndDifferentParams2));

    // ArrowType with variable params and different parameter lists and inferred return types has no equal parameters
    ArrowType arrowTypeWithVariableParamsAndDifferentParamsAndInferredReturn1 = new ArrowType(
        registry, null, returnTypeInferred, false);
    ArrowType arrowTypeWithVariableParamsAndDifferentParamsAndInferredReturn2 = new ArrowType(
        registry, null, returnTypeInferred, false);
    assertFalse(arrowTypeWithVariableParams1.hasEqualParameters(arrowTypeWithVariableParamsAndDifferentParamsAndInferredReturn2));
  }

  @Test
  public void testIsEquivalentTo() {
    // Two ArrowTypes with equal parameter lists and known return types are equivalent
    ArrowType arrowType1 = new ArrowType(registry, null, returnType, false);
    ArrowType arrowType2 = new ArrowType(registry, null, returnType, false);
    assertEquals(arrowType1, arrowType2);

    // Two ArrowTypes with equal parameter lists and inferred return types are equivalent
    ArrowType arrowTypeWithInferredReturn1 = new ArrowType(registry, null, returnTypeInferred, false);
    ArrowType arrowTypeWithInferredReturn2 = new ArrowType(registry, null, returnTypeInferred, false);
    assertEquals(arrowType1, arrowTypeWithInferredReturn2);

    // Two ArrowTypes with different parameter lists and known return types are not equivalent
    ArrowType arrowTypeWithDifferentParams1 = new ArrowType(registry, null, returnType, false);
    ArrowType arrowTypeWithDifferentParams2 = new ArrowType(registry, null, returnType, false);
    assertNotSame(arrowType1, arrowTypeWithDifferentParams2);

    // Two ArrowTypes with different parameter lists and inferred return types are not equivalent
    ArrowType arrowTypeWithDifferentParamsAndInferredReturn1 = new ArrowType(
        registry, null, returnTypeInferred, false);
    ArrowType arrowTypeWithDifferentParamsAndInferredReturn2 = new ArrowType(
        registry, null, returnTypeInferred, false);
    assertNotSame(arrowType1, arrowTypeWithDifferentParamsAndInferredReturn2);

    // ArrowType with null return type is not equivalent to ArrowType with a known return type
    assertNotSame(arrowType, arrowTypeWithNullReturn);

    // ArrowType with variable params is equivalent to ArrowType with the same params and a covariant return type
    assertEquals(arrowTypeWithVariableParams, arrowTypeWithVariableParamsAndInferredReturn);

    // ArrowType with variable params and inferred return type is equivalent to ArrowType with the same params and inferred return type
    assertEquals(arrowTypeWithVariableParamsAndInferredReturn, arrowTypeWithVariableParams);

    // ArrowType with variable params and different param types is not equivalent to ArrowType with the same params
    assertNotSame(arrowTypeWithVarArgsAndDifferentParamTypes, arrowTypeWithVariableParams);

    // ArrowType with variable params and different param types and inferred return types is not equivalent to ArrowType with the same params
    assertNotSame(arrowTypeWithVarArgsAndDifferentParamTypes, arrowTypeWithVariableParamsAndInferredReturn);
  }

  @Test
  public void testHashCode() {
    // Two ArrowTypes with equal parameter lists and known return types have the same hashcode
    ArrowType arrowType1 = new ArrowType(registry, null, returnType, false);
    ArrowType arrowType2 = new ArrowType(registry, null, returnType, false);
    assertEquals(arrowType1.hashCode(), arrowType2.hashCode());

    // Two ArrowTypes with equal parameter lists and inferred return types have the same hashcode
    ArrowType arrowTypeWithInferredReturn1 = new ArrowType(registry, null, returnTypeInferred, false);
    ArrowType arrowTypeWithInferredReturn2 = new ArrowType(registry, null, returnTypeInferred, false);
    assertEquals(arrowType1.hashCode(), arrowTypeWithInferredReturn2.hashCode());

    // Two ArrowTypes with different parameter lists and known return types have different hashcodes
    ArrowType arrowTypeWithDifferentParams1 = new ArrowType(registry, null, returnType, false);
    ArrowType arrowTypeWithDifferentParams2 = new ArrowType(registry, null, returnType, false);
    assertNotSame(arrowType1.hashCode(), arrowTypeWithDifferentParams2.hashCode());

    // Two ArrowTypes with different parameter lists and inferred return types have different hashcodes
    ArrowType arrowTypeWithDifferentParamsAndInferredReturn1 = new ArrowType(
        registry, null, returnTypeInferred, false);
    ArrowType arrowTypeWithDifferentParamsAndInferredReturn2 = new ArrowType(
        registry, null, returnTypeInferred, false);
    assertNotSame(arrowType1.hashCode(), arrowTypeWithDifferentParamsAndInferredReturn2.hashCode());

    // ArrowType with null return type has a different hashcode than ArrowType with a known return type
    assertNotSame(arrowType.hashCode(), arrowTypeWithNullReturn.hashCode());

    // ArrowType with variable params has the same hashcode as ArrowType with the same params and a covariant return type
    assertEquals(arrowTypeWithVariableParams.hashCode(), arrowTypeWithVariableParamsAndInferredReturn.hashCode());

    // ArrowType with variable params and inferred return type has the same hashcode as ArrowType with the same params and inferred return type
    assertEquals(arrowTypeWithVariableParamsAndInferredReturn.hashCode(), arrowTypeWithVariableParams.hashCode());

    // ArrowType with variable params and different param types has a different hashcode than ArrowType with the same params
    assertNotSame(arrowTypeWithVarArgsAndDifferentParamTypes.hashCode(), arrowTypeWithVariableParams.hashCode());

    // ArrowType with variable params and different param types and inferred return types has a different hashcode than ArrowType with the same params
    assertNotSame(arrowTypeWithVarArgsAndDifferentParamTypes.hashCode(), arrowTypeWithVariableParamsAndInferredReturn.hashCode());
  }
}