import static org.junit.Assert.*;
import static com.google.javascript.rhino.node.JsConst.*;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeNative;

public class ArrowTypeTest {

  @org.junit.Test
  public void testArrowTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = JSTypeNative.createParametersWithVarArgs(
        registry.createNamedType("Number"));
    JSType returnType = registry.createNamedType("String");
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertEquals("Expected ArrowType object", ArrowType.class, arrowType.getClass());
    assertEquals("Expected correct number of parameters", 0, arrowType.parameters.getChildCount());
    assertEquals("Expected correct return type", returnType, arrowType.returnType);
    assertTrue("Expected return type not inferred", !arrowType.returnTypeInferred);

    // Test with null parameters and null return type
    Node nullParameters = null;
    ArrowType arrowTypeNull = new ArrowType(registry, nullParameters, null);
    assertEquals("Expected ArrowType object", ArrowType.class, arrowTypeNull.getClass());
    assertNull("Expected null parameters", arrowTypeNull.parameters);
    assertNull("Expected null return type", arrowTypeNull.returnType);
    assertTrue("Expected return type inferred", arrowTypeNull.returnTypeInferred);

    // Test with empty parameters and null return type
    Node emptyParameters = JSTypeNative.createEmptyList();
    ArrowType arrowTypeEmpty = new ArrowType(registry, emptyParameters, null);
    assertEquals("Expected ArrowType object", ArrowType.class, arrowTypeEmpty.getClass());
    assertEquals("Expected empty list of parameters", 0, arrowTypeEmpty.parameters.getChildCount());
    assertNull("Expected null return type", arrowTypeEmpty.returnType);
    assertTrue("Expected return type inferred", arrowTypeEmpty.returnTypeInferred);
  }

  @org.junit.Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node param1 = registry.createNamedType("Number");
    Node param2 = registry.createNamedType("String");
    Node param3 = registry.createNamedType("Number");
    Node param4 = registry.createNamedType("Boolean");
    Node param5 = registry.createNamedType("String");
    Node varargs = registry.createVarArgsType();
    JSType returnType1 = registry.createNamedType("Number");
    JSType returnType2 = registry.createNamedType("String");
    JSType returnType3 = registry.createNamedType("Boolean");

    ArrowType arrowType11 = new ArrowType(registry, null, returnType1);
    ArrowType arrowType12 = new ArrowType(registry, param1, returnType1);
    ArrowType arrowType13 = new ArrowType(registry, param1, param2, returnType1);
    ArrowType arrowType14 = new ArrowType(registry, param1, param2, param3, returnType1);
    ArrowType arrowType15 = new ArrowType(registry, param1, param2, param3, param4, varargs, returnType1);
    ArrowType arrowType16 = new ArrowType(registry, param1, varargs, returnType1);

    ArrowType arrowType21 = new ArrowType(registry, null, returnType2);
    ArrowType arrowType22 = new ArrowType(registry, param1, returnType2);
    ArrowType arrowType23 = new ArrowType(registry, param1, param2, returnType2);
    ArrowType arrowType24 = new ArrowType(registry, param1, param2, param3, returnType2);
    ArrowType arrowType25 = new ArrowType(registry, param1, param2, param3, param4, varargs, returnType2);
    ArrowType arrowType26 = new ArrowType(registry, param1, varargs, returnType2);

    ArrowType arrowType31 = new ArrowType(registry, null, returnType3);
    ArrowType arrowType32 = new ArrowType(registry, param1, returnType3);
    ArrowType arrowType33 = new ArrowType(registry, param1, param2, returnType3);
    ArrowType arrowType34 = new ArrowType(registry, param1, param2, param3, returnType3);
    ArrowType arrowType35 = new ArrowType(registry, param1, param2, param3, param4, varargs, returnType3);
    ArrowType arrowType36 = new ArrowType(registry, param1, varargs, returnType3);

    // Test with same arrow type
    assertTrue("Expected true for subtypes with same arrow type",
        arrowType11.isSubtype(arrowType11));
    assertTrue("Expected true for subtypes with same arrow type",
        arrowType12.isSubtype(arrowType12));
    assertTrue("Expected true for subtypes with same arrow type",
        arrowType13.isSubtype(arrowType13));
    assertTrue("Expected true for subtypes with same arrow type",
        arrowType14.isSubtype(arrowType14));
    assertTrue("Expected true for subtypes with same arrow type",
        arrowType15.isSubtype(arrowType15));
    assertTrue("Expected true for subtypes with same arrow type",
        arrowType16.isSubtype(arrowType16));

    // Test with same arrow type but different return types
    assertFalse("Expected false for arrow types with different return types",
        arrowType11.isSubtype(arrowType21));
    assertFalse("Expected false for arrow types with different return types",
        arrowType12.isSubtype(arrowType22));
    assertFalse("Expected false for arrow types with different return types",
        arrowType13.isSubtype(arrowType23));
    assertFalse("Expected false for arrow types with different return types",
        arrowType14.isSubtype(arrowType24));
    assertFalse("Expected false for arrow types with different return types",
        arrowType15.isSubtype(arrowType25));
    assertFalse("Expected false for arrow types with different return types",
        arrowType16.isSubtype(arrowType26));

    // Test with arrow types with different parameters
    assertFalse("Expected false for arrow types with different parameters",
        arrowType11.isSubtype(arrowType31));
    assertFalse("Expected false for arrow types with different parameters",
        arrowType12.isSubtype(arrowType32));
    assertFalse("Expected false for arrow types with different parameters",
        arrowType13.isSubtype(arrowType33));
    assertFalse("Expected false for arrow types with different parameters",
        arrowType14.isSubtype(arrowType34));
    assertFalse("Expected false for arrow types with different parameters",
        arrowType15.isSubtype(arrowType35));
    assertFalse("Expected false for arrow types with different parameters",
        arrowType16.isSubtype(arrowType36));

    // Test with arrow types with varargs in different positions
    assertFalse("Expected false for arrow types with varargs in different positions",
        arrowType16.isSubtype(arrowType35));
    assertFalse("Expected false for arrow types with varargs in different positions",
        arrowType36.isSubtype(arrowType15));

    // Test with arrow types where one has optional parameters and the other doesn't
    ArrowType arrowTypeOptional1 = new ArrowType(registry,
        JSTypeNative.createNamedOptionalParam("param1", registry.createNamedType("Number")),
        returnType1);
    ArrowType arrowTypeOptional2 = new ArrowType(registry, param1, returnType1);
    assertFalse("Expected false for arrow types with optional parameters and without",
        arrowTypeOptional1.isSubtype(arrowTypeOptional2));
    assertFalse("Expected false for arrow types without optional parameters and with",
        arrowTypeOptional2.isSubtype(arrowTypeOptional1));

    // Test with arrow types where one has varargs and the other doesn't
    ArrowType arrowTypeVarargs1 = new ArrowType(registry, param1, varargs, returnType1);
    ArrowType arrowTypeVarargs2 = new ArrowType(registry, param1, returnType1);
    assertFalse("Expected false for arrow types with varargs and without",
        arrowTypeVarargs1.isSubtype(arrowTypeVarargs2));
    assertFalse("Expected false for arrow types without varargs and with",
        arrowTypeVarargs2.isSubtype(arrowTypeVarargs1));

    // Test with arrow types where both have varargs but in different positions
    ArrowType arrowTypeVarargs1Pos1 = new ArrowType(registry, param1, varargs, returnType1);
    ArrowType arrowTypeVarargs1Pos2 = new ArrowType(registry, varargs, param1, returnType1);
    assertFalse("Expected false for arrow types with varargs in different positions",
        arrowTypeVarargs1Pos1.isSubtype(arrowTypeVarargs1Pos2));
    assertFalse("Expected false for arrow types with varargs in different positions",
        arrowTypeVarargs1Pos2.isSubtype(arrowTypeVarargs1Pos1));
  }

  @org.junit.Test
  public void testHasEqualParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node param1 = registry.createNamedType("Number");
    Node param2 = registry.createNamedType("String");
    Node param3 = registry.createNamedType("Number");
    Node param4 = registry.createNamedType("Boolean");
    Node param5 = registry.createNamedType("String");
    Node varargs = registry.createVarArgsType();

    ArrowType arrowType1 = new ArrowType(registry, param1, param2, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, param2, param1, returnType1);
    ArrowType arrowType3 = new ArrowType(registry, param1, param2, param3, returnType1);
    ArrowType arrowType4 = new ArrowType(registry, param1, param2, param3, param4, varargs, returnType1);
    ArrowType arrowType5 = new ArrowType(registry, param1, varargs, returnType1);
    ArrowType arrowType6 = new ArrowType(registry, param1, param2, varargs, returnType1);

    // Test with same arrow types
    assertTrue("Expected true for equal arrow types",
        arrowType1.hasEqualParameters(arrowType1, true));
    assertTrue("Expected true for equal arrow types",
        arrowType2.hasEqualParameters(arrowType2, true));
    assertTrue("Expected true for equal arrow types",
        arrowType3.hasEqualParameters(arrowType3, true));
    assertTrue("Expected true for equal arrow types",
        arrowType4.hasEqualParameters(arrowType4, true));
    assertTrue("Expected true for equal arrow types",
        arrowType5.hasEqualParameters(arrowType5, true));
    assertTrue("Expected true for equal arrow types",
        arrowType6.hasEqualParameters(arrowType6, true));

    // Test with different arrow types
    assertFalse("Expected false for different arrow types",
        arrowType1.hasEqualParameters(arrowType2, true));
    assertFalse("Expected false for different arrow types",
        arrowType1.hasEqualParameters(arrowType3, true));
    assertFalse("Expected false for different arrow types",
        arrowType1.hasEqualParameters(arrowType4, true));
    assertFalse("Expected false for different arrow types",
        arrowType1.hasEqualParameters(arrowType5, true));
    assertFalse("Expected false for different arrow types",
        arrowType1.hasEqualParameters(arrowType6, true));

    // Test with arrow types having different numbers of parameters
    assertFalse("Expected false for arrow types with different numbers of parameters",
        arrowType1.hasEqualParameters(arrowType3, true));
    assertFalse("Expected false for arrow types with different numbers of parameters",
        arrowType1.hasEqualParameters(arrowType4, true));
    assertFalse("Expected false for arrow types with different numbers of parameters",
        arrowType1.hasEqualParameters(arrowType5, true));
    assertFalse("Expected false for arrow types with different numbers of parameters",
        arrowType1.hasEqualParameters(arrowType6, true));

    // Test with arrow types having different types of parameters
    assertFalse("Expected false for arrow types with different parameter types",
        arrowType1.hasEqualParameters(arrowType3, true));
    assertFalse("Expected false for arrow types with different parameter types",
        arrowType1.hasEqualParameters(arrowType4, true));
    assertFalse("Expected false for arrow types with different parameter types",
        arrowType1.hasEqualParameters(arrowType5, true));
    assertFalse("Expected false for arrow types with different parameter types",
        arrowType1.hasEqualParameters(arrowType6, true));

    // Test with arrow types having optional parameters
    ArrowType arrowTypeOptional1 = new ArrowType(registry,
        JSTypeNative.createNamedOptionalParam("param1", registry.createNamedType("Number")),
        returnType1);
    ArrowType arrowTypeOptional2 = new ArrowType(registry, param1, returnType1);
    assertFalse("Expected false for arrow types with optional parameters and without",
        arrowTypeOptional1.hasEqualParameters(arrowTypeOptional2, true));
    assertFalse("Expected false for arrow types without optional parameters and with",
        arrowTypeOptional2.hasEqualParameters(arrowTypeOptional1, true));

    // Test with arrow types having varargs
    ArrowType arrowTypeVarargs1 = new ArrowType(registry, param1, varargs, returnType1);
    ArrowType arrowTypeVarargs2 = new ArrowType(registry, param1, returnType1);
    assertFalse("Expected false for arrow types with varargs and without",
        arrowTypeVarargs1.hasEqualParameters(arrowTypeVarargs2, true));
    assertFalse("Expected false for arrow types without varargs and with",
        arrowTypeVarargs2.hasEqualParameters(arrowTypeVarargs1, true));

    // Test with tolerating unknown types
    assertTrue("Expected true for equal arrow types with tolerated unknown types",
        arrowType1.hasEqualParameters(arrowType2, false));
    assertTrue("Expected true for equal arrow types with tolerated unknown types",
        arrowType1.hasEqualParameters(arrowType3, false));
    assertTrue("Expected true for equal arrow types with tolerated unknown types",
        arrowType1.hasEqualParameters(arrowType4, false));
    assertTrue("Expected true for equal arrow types with tolerated unknown types",
        arrowType1.hasEqualParameters(arrowType5, false));
    assertTrue("Expected true for equal arrow types with tolerated unknown types",
        arrowType1.hasEqualParameters(arrowType6, false));
  }
}