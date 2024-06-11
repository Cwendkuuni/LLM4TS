package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.Visitor;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {

  private static final ErrorReporter errorReporter = new ErrorReporter() {
    @Override
    public void warning(String message, Object... args) {
      // Ignore warnings
    }

    @Override
    public void error(String message, Object... args) {
      throw new RuntimeException(message);
    }
  };

  private JSTypeRegistry registry;
  private ArrowType arrowType;
  private ArrowType arrowTypeWithUnknownReturn;
  private ArrowType arrowTypeWithVarArgs;
  private ArrowType arrowTypeWithOptionalArg;
  private ArrowType arrowTypeWithInferredReturn;
  private ArrowType arrowTypeWithMismatchedParams;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    Node functionNode = Node.newFunctionNode(new NodeList(), new NodeList());

    arrowType = new ArrowType(registry, null, null);
    arrowTypeWithUnknownReturn = new ArrowType(registry, null,
        getNativeType(UNKNOWN_TYPE), true);
    arrowTypeWithVarArgs = new ArrowType(registry, null, null, true);
    arrowTypeWithOptionalArg = new ArrowType(registry, Node.newNameNode("x"), null, true, true);
    arrowTypeWithInferredReturn = new ArrowType(registry, Node.newNameNode("x"), null, false, true);
    arrowTypeWithMismatchedParams = new ArrowType(registry, Node.newNameNode("x"), Node.newNameNode("y"), false);

    arrowTypeWithOptionalArg.parameters.setFirstChild(arrowTypeWithOptionalArg.parameters.getFirstChild().getNext());
  }

  @Test
  public void testIsSubtype() {
    // Test subtypes with no parameters and no return type
    assertTrue(arrowType.isSubtype(arrowType));
    assertTrue(arrowType.isSubtype(arrowTypeWithInferredReturn));

    // Test subtypes with no parameters and a return type
    assertTrue(arrowType.isSubtype(arrowTypeWithReturnType(registry, UNKNOWN_TYPE)));
    assertTrue(arrowTypeWithInferredReturn.isSubtype(arrowTypeWithReturnType(registry, UNKNOWN_TYPE)));

    // Test subtypes with parameters and a return type
    Node param1 = Node.newNameNode("x");
    Node param2 = Node.newNameNode("y");
    ArrowType arrowTypeWithParams = new ArrowType(registry, param1, param2, false);
    ArrowType arrowTypeWithParamsAndInferredReturn = new ArrowType(registry, param1, param2, false, true);

    // The subtype has the same or fewer parameters
    assertTrue(arrowType.isSubtype(arrowTypeWithParams));
    assertTrue(arrowTypeWithParams.isSubtype(arrowType));

    // Both have the same number of parameters, and the types of the parameters
    // are subtypes of each other
    assertTrue(arrowTypeWithParams.isSubtype(arrowTypeWithParamsAndInferredReturn));
    assertTrue(arrowTypeWithParamsAndInferredReturn.isSubtype(arrowTypeWithParams));

    // The subtype has more parameters, but they are all optional or var_args
    ArrowType arrowTypeWithOptionalParams = new ArrowType(registry, param1, param2, false, true, true);
    assertTrue(arrowTypeWithParamsAndInferredReturn.isSubtype(arrowTypeWithOptionalParams));
    assertTrue(arrowTypeWithOptionalParams.isSubtype(arrowTypeWithParamsAndInferredReturn));

    // The subtype has more parameters, but it requires some of them
    ArrowType arrowTypeWithRequiredParams = new ArrowType(registry, param1, param2, false, false, true);
    assertFalse(arrowTypeWithParamsAndInferredReturn.isSubtype(arrowTypeWithRequiredParams));
    assertFalse(arrowTypeWithRequiredParams.isSubtype(arrowTypeWithParamsAndInferredReturn));

    // The subtype has fewer parameters, but some of them are required in the supertype
    ArrowType arrowTypeWithFewerParams = new ArrowType(registry, Node.newNameNode("x"), null, false);
    assertFalse(arrowTypeWithParams.isSubtype(arrowTypeWithFewerParams));
    assertFalse(arrowTypeWithFewerParams.isSubtype(arrowTypeWithParams));

    // Test subtypes with var_args
    ArrowType arrowTypeWithVarArgsIn = new ArrowType(registry, Node.newNameNode("x"), null, true);
    ArrowType arrowTypeWithVarArgsOut = new ArrowType(registry, null, null, true);
    assertTrue(arrowTypeWithVarArgsIn.isSubtype(arrowTypeWithVarArgsOut));
    assertTrue(arrowTypeWithVarArgsOut.isSubtype(arrowTypeWithVarArgsIn));

    // Test subtypes with optional arguments
    ArrowType arrowTypeWithOptionalArgsIn = new ArrowType(registry, Node.newNameNode("x"), null, true, true);
    ArrowType arrowTypeWithOptionalArgsOut = new ArrowType(registry, Node.newNameNode("x"), null, true);
    assertTrue(arrowTypeWithOptionalArgsIn.isSubtype(arrowTypeWithOptionalArgsOut));
    assertTrue(arrowTypeWithOptionalArgsOut.isSubtype(arrowTypeWithOptionalArgsIn));

    // Test subtypes with mismatched return types
    ArrowType arrowTypeWithIntegerReturn = new ArrowType(registry, Node.newNameNode("x"), Node.newNumberNode(1), false);
    assertFalse(arrowType.isSubtype(arrowTypeWithIntegerReturn));
    assertFalse(arrowTypeWithIntegerReturn.isSubtype(arrowType));

    // Test subtypes with mismatched parameter types
    ArrowType arrowTypeWithMismatchedParamsIn = new ArrowType(registry, Node.newNameNode("x"), Node.newNameNode("y"), false);
    assertFalse(arrowType.isSubtype(arrowTypeWithMismatchedParamsIn));
    assertFalse(arrowTypeWithMismatchedParamsIn.isSubtype(arrowType));
  }

  @Test
  public void testHasEqualParameters() {
    // Test equal parameter lists
    assertTrue(arrowType.hasEqualParameters(arrowType, true));
    assertTrue(arrowType.hasEqualParameters(arrowTypeWithInferredReturn, true));
    assertTrue(arrowType.hasEqualParameters(arrowTypeWithParams, true));
    assertTrue(arrowType.hasEqualParameters(arrowTypeWithParamsAndInferredReturn, true));
    assertTrue(arrowTypeWithOptionalArgsIn.hasEqualParameters(arrowTypeWithOptionalArgsOut, true));

    // Test unequal parameter lists with tolerateUnknowns = true
    assertTrue(arrowType.hasEqualParameters(arrowTypeWithUnknownReturn, true));
    assertTrue(arrowTypeWithOptionalArgsIn.hasEqualParameters(arrowTypeWithOptionalArgsIn, true));

    // Test unequal parameter lists with tolerateUnknowns = false
    assertFalse(arrowType.hasEqualParameters(arrowTypeWithParamsAndInferredReturn, false));
    assertFalse(arrowTypeWithOptionalArgsIn.hasEqualParameters(arrowTypeWithMismatchedParams, false));
  }

  @Test
  public void testCheckArrowEquivalenceHelper() {
    // Test equal arrow types (reflexivity)
    assertTrue(arrowType.checkArrowEquivalenceHelper(arrowType, true));

    // Test equal arrow types with inferred return types
    assertTrue(arrowType.checkArrowEquivalenceHelper(arrowTypeWithInferredReturn, true));

    // Test equal arrow types with mismatched parameter types but tolerateUnknowns = true
    assertTrue(arrowType.checkArrowEquivalenceHelper(arrowTypeWithMismatchedParams, true));

    // Test unequal arrow types
    assertFalse(arrowType.checkArrowEquivalenceHelper(arrowTypeWithReturnType(registry, Node.newNumberNode(1)), true));
    assertFalse(arrowType.checkArrowEquivalenceHelper(arrowTypeWithMismatchedParams, false));
  }

  @Test
  public void testHashCode() {
    JSType returnType1 = getNativeType(Node.INT_TYPE);
    JSType returnType2 = getNativeType(Node.NUMBER_TYPE);

    int hashCode1 = arrowType.hashCode();
    int hashCode2 = arrowTypeWithReturnType(registry, returnType1).hashCode();
    int hashCode3 = arrowTypeWithReturnType(registry, returnType2).hashCode();

    // The hashCode of an arrow type is the hashCode of its return type and the
    // hashCodes of its parameter types (if any)
    assertEquals(hashCode1, hashCode1);
    assertEquals(hashCode1, hashCode2);
    assertEquals(hashCode2, hashCode2);
    assertEquals(hashCode2, hashCode3);
    assertEquals(hashCode3, hashCode3);

    // Changing the return type should change the hashCode
    arrowTypeWithReturnType(registry, returnType2).hashCode();
    assertNotEquals(hashCode2, hashCode3);

    // Adding a parameter should change the hashCode
    Node param = Node.newNameNode("x");
    ArrowType arrowTypeWithParam = new ArrowType(registry, param, null);
    assertNotEquals(hashCode2, arrowTypeWithParam.hashCode());
  }

  @Test
  public void testVisit() {
    Visitor visitor = new Visitor<Object>() {
      @Override
      public Object visitArrowType(ArrowType arrowType) {
        return arrowType.toString();
      }
    };

    Object result = arrowType.visit(visitor);
    assertEquals("[ArrowType]", result);
  }

  @Test
  public void testResolveInternal() {
    // Test resolving an arrow type with no parameters and no return type
    ArrowType resolvedArrowType = arrowType.resolveInternal(errorReporter, new StaticScope<>(registry));
    assertEquals(arrowType, resolvedArrowType);

    // Test resolving an arrow type with a null return type and a null parameters list
    JSType returnType = getNativeType(UNKNOWN_TYPE);
    ArrowType arrowTypeWithNullReturn = new ArrowType(registry, null, returnType, false);
    ArrowType resolvedArrowTypeWithNullReturn = arrowTypeWithNullReturn.resolveInternal(errorReporter, new StaticScope<>(registry));
    assertEquals(arrowTypeWithNullReturn, resolvedArrowTypeWithNullReturn);

    // Test resolving an arrow type with no parameters and a non-null return type
    ArrowType arrowTypeWithNonNullReturn = new ArrowType(registry, null, Node.INT_TYPE, false);
    ArrowType resolvedArrowTypeWithNonNullReturn = arrowTypeWithNonNullReturn.resolveInternal(errorReporter, new StaticScope<>(registry));
    assertEquals(arrowTypeWithNonNullReturn, resolvedArrowTypeWithNonNullReturn);

    // Test resolving an arrow type with parameters and a non-null return type
    Node param1 = Node.newNameNode("x");
    Node param2 = Node.newNameNode("y");
    ArrowType arrowTypeWithParamsAndNonNullReturn = new ArrowType(registry, param1, param2, false, false, false,
        Node.INT_TYPE);
    ArrowType resolvedArrowTypeWithParamsAndNonNullReturn = arrowTypeWithParamsAndNonNullReturn.resolveInternal(errorReporter, new StaticScope<>(registry));
    assertEquals(arrowTypeWithParamsAndNonNullReturn, resolvedArrowTypeWithParamsAndNonNullReturn);

    // Test error reporting when resolving an arrow type with no parameters and no return type and an error is thrown
    ArrowType arrowTypeWithError = new ArrowType(registry, null, null);
    ArrowType resolvedArrowTypeWithError = arrowTypeWithError.resolveInternal(errorReporter, new StaticScope<>(registry));
    assertNotNull(resolvedArrowTypeWithError);
    assertTrue(resolvedArrowTypeWithError instanceof ArrowType);
    assertEquals(arrowTypeWithError, resolvedArrowTypeWithError);
    assertTrue(errorReporter.hasErrors());
  }

  @Test
  public void testHasAnyTemplateInternal() {
    // Test arrow types without templates
    assertFalse(arrowType.hasAnyTemplateInternal());
    assertFalse(arrowTypeWithUnknownReturn.hasAnyTemplateInternal());
    assertFalse(arrowTypeWithVarArgs.hasAnyTemplateInternal());
    assertFalse(arrowTypeWithOptionalArg.hasAnyTemplateInternal());
    assertFalse(arrowTypeWithInferredReturn.hasAnyTemplateInternal());
    assertFalse(arrowTypeWithMismatchedParams.hasAnyTemplateInternal());

    // Test arrow types with templates
    Node templateParam1 = Node.newNameNode("T");
    Node templateParam2 = Node.newNameNode("U");
    ArrowType arrowTypeWithTemplateParams = new ArrowType(registry, Node.newNameNode("x"), Node.newNameNode("y"), false,
        false, false, new ArrowType(registry, templateParam1, templateParam2, false));
    assertTrue(arrowTypeWithTemplateParams.hasAnyTemplateInternal());
  }

  private static JSType arrowTypeWithReturnType(JSTypeRegistry registry, JSType returnType) {
    Node parameters = Node.newList();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    return arrowType;
  }
}