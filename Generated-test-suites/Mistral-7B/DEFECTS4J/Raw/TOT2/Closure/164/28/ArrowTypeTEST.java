import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createArrowType;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.NodeFactory.createString;
import static com.google.javascript.rhino.NodeFactory.createTypeLiteral;
import static com.google.javascript.rhino.testing.TypeCheckingTestCase.checkTypes;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JsContext;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.testing.TypeCheckingTestCase;

public class ArrowTypeTest extends TypeCheckingTestCase {

  public void testArrowTypeCreation() {
    JsContext cx = enteringContext();
    ErrorReporter errorReporter = cx.getCurrentErrorReporter();
    StaticScope<JSType> scope = cx.getCurrentScope();

    // Test ArrowType creation with no parameters and no return type
    Node arrowTypeNode = createArrowType(null, null);
    ArrowType arrowType = (ArrowType) arrowTypeNode.getValue(cx);
    assertEquals("ArrowType", arrowType.getTypeName(cx));
    assertTrue(arrowType.isSubtype(arrowType));

    // Test ArrowType creation with parameters and no return type
    Node parametersNode = createNode("(number, number)", createTypeLiteral(JSType.NUMBER));
    Node arrowTypeNodeWithParams = createArrowType(parametersNode, null);
    ArrowType arrowTypeWithParams = (ArrowType) arrowTypeNodeWithParams.getValue(cx);
    assertEquals("ArrowType", arrowTypeWithParams.getTypeName(cx));
    assertTrue(arrowTypeWithParams.isSubtype(arrowType));
    assertFalse(arrowType.isSubtype(arrowTypeWithParams));

    // Test ArrowType creation with parameters and return type
    Node returnTypeNode = createTypeLiteral(JSType.STRING);
    Node arrowTypeNodeWithParamsAndReturn = createArrowType(parametersNode, returnTypeNode);
    ArrowType arrowTypeWithParamsAndReturn = (ArrowType) arrowTypeNodeWithParamsAndReturn.getValue(cx);
    assertEquals("ArrowType", arrowTypeWithParamsAndReturn.getTypeName(cx));
    assertTrue(arrowTypeWithParams.isSubtype(arrowTypeWithParamsAndReturn));
    assertFalse(arrowTypeWithParamsAndReturn.isSubtype(arrowType));

    // Test ArrowType creation with varargs
    Node varargsNode = createNode("(number, ...number)", createTypeLiteral(JSType.NUMBER));
    Node arrowTypeNodeWithVarargs = createArrowType(varargsNode, null);
    ArrowType arrowTypeWithVarargs = (ArrowType) arrowTypeNodeWithVarargs.getValue(cx);
    assertEquals("ArrowType", arrowTypeWithVarargs.getTypeName(cx));
    assertTrue(arrowTypeWithParams.isSubtype(arrowTypeWithVarargs));
    assertFalse(arrowTypeWithVarargs.isSubtype(arrowType));

    checkTypes(errorReporter, arrowTypeNode, arrowTypeNodeWithParams, arrowTypeNodeWithParamsAndReturn, arrowTypeNodeWithVarargs);

    leavingContext();
  }

  public void testIsSubtype() {
    JsContext cx = enteringContext();
    ErrorReporter errorReporter = cx.getCurrentErrorReporter();
    StaticScope<JSType> scope = cx.getCurrentScope();

    // Test ArrowType isSubtype with same parameters and same return type
    Node arrowTypeNode1 = createArrowType(null, createTypeLiteral(JSType.NUMBER));
    Node arrowTypeNode2 = createArrowType(null, createTypeLiteral(JSType.NUMBER));
    ArrowType arrowType1 = (ArrowType) arrowTypeNode1.getValue(cx);
    ArrowType arrowType2 = (ArrowType) arrowTypeNode2.getValue(cx);
    assertTrue(arrowType1.isSubtype(arrowType1));
    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
    assertTrue(arrowType1.isSubtype(arrowType2));

    // Test ArrowType isSubtype with same parameters but different return types
    Node arrowTypeNodeWithReturn1 = createArrowType(null, createTypeLiteral(JSType.NUMBER));
    Node arrowTypeNodeWithReturn2 = createArrowType(null, createTypeLiteral(JSType.STRING));
    ArrowType arrowTypeWithReturn1 = (ArrowType) arrowTypeNodeWithReturn1.getValue(cx);
    ArrowType arrowTypeWithReturn2 = (ArrowType) arrowTypeNodeWithReturn2.getValue(cx);
    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));

    // Test ArrowType isSubtype with different parameters but same return type
    Node arrowTypeNodeDifferentParams1 = createArrowType(createNode("(number)", createTypeLiteral(JSType.NUMBER)), createTypeLiteral(JSType.NUMBER));
    Node arrowTypeNodeDifferentParams2 = createArrowType(createNode("(string)", createTypeLiteral(JSType.NUMBER)), createTypeLiteral(JSType.NUMBER));
    ArrowType arrowTypeDifferentParams1 = (ArrowType) arrowTypeNodeDifferentParams1.getValue(cx);
    ArrowType arrowTypeDifferentParams2 = (ArrowType) arrowTypeNodeDifferentParams2.getValue(cx);
    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));

    leavingContext();
  }

  public void testHasEqualParameters() {
    JsContext cx = enteringContext();
    ErrorReporter errorReporter = cx.getCurrentErrorReporter();
    StaticScope<JSType> scope = cx.getCurrentScope();

    // Test ArrowType hasEqualParameters with same parameters
    Node arrowTypeNode1 = createArrowType(null, createTypeLiteral(JSType.NUMBER));
    Node arrowTypeNode2 = createArrowType(null, createTypeLiteral(JSType.NUMBER));
    ArrowType arrowType1 = (ArrowType) arrowTypeNode1.getValue(cx);
    ArrowType arrowType2 = (ArrowType) arrowTypeNode2.getValue(cx);
    ArrowType arrowType3 = createArrowType(createNode("(number)", createTypeLiteral(JSType.NUMBER)), createTypeLiteral(JSType.NUMBER));
    ArrowType arrowType4 = createArrowType(createNode("(number)", createTypeLiteral(JSType.NUMBER)), createTypeLiteral(JSType.STRING));
    assertTrue(arrowType1.hasEqualParameters(arrowType1));
    assertTrue(arrowType1.hasEqualParameters(arrowType2));
    assertFalse(arrowType1.hasEqualParameters(arrowType3));
    assertFalse(arrowType1.hasEqualParameters(arrowType4));

    leavingContext();
  }

  public void testIsEquivalentTo() {
    JsContext cx = enteringContext();
    ErrorReporter errorReporter = cx.getCurrentErrorReporter();
    StaticScope<JSType> scope = cx.getCurrentScope();

    // Test ArrowType isEquivalentTo with same parameters and same return type
    Node arrowTypeNode1 = createArrowType(null, createTypeLiteral(JSType.NUMBER));
    Node arrowTypeNode2 = createArrowType(null, createTypeLiteral(JSType.NUMBER));
    ArrowType arrowType1 = (ArrowType) arrowTypeNode1.getValue(cx);
    ArrowType arrowType2 = (ArrowType) arrowTypeNode2.getValue(cx);
    assertTrue(arrowType1.isEquivalentTo(arrowType1));
    assertTrue(arrowType1.isEquivalentTo(arrowType2));
    assertTrue(arrowType2.isEquivalentTo(arrowType1));
    assertTrue(arrowType1.isEquivalentTo(arrowType2));

    // Test ArrowType isEquivalentTo with same parameters but different return types
    Node arrowTypeNodeWithReturn1 = createArrowType(null, createTypeLiteral(JSType.NUMBER));
    Node arrowTypeNodeWithReturn2 = createArrowType(null, createTypeLiteral(JSType.STRING));
    ArrowType arrowTypeWithReturn1 = (ArrowType) arrowTypeNodeWithReturn1.getValue(cx);
    ArrowType arrowTypeWithReturn2 = (ArrowType) arrowTypeNodeWithReturn2.getValue(cx);
    assertFalse(arrowType1.isEquivalentTo(arrowType2));
    assertFalse(arrowType2.isEquivalentTo(arrowType1));

    leavingContext();
  }

  public void testHashCode() {
    JsContext cx = enteringContext();
    ErrorReporter errorReporter = cx.getCurrentErrorReporter();
    StaticScope<JSType> scope = cx.getCurrentScope();

    // Test ArrowType hashCode with same parameters and same return type
    Node arrowTypeNode1 = createArrowType(null, createTypeLiteral(JSType.NUMBER));
    Node arrowTypeNode2 = createArrowType(null, createTypeLiteral(JSType.NUMBER));
    ArrowType arrowType1 = (ArrowType) arrowTypeNode1.getValue(cx);
    ArrowType arrowType2 = (ArrowType) arrowTypeNode2.getValue(cx);
    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();
    assertEquals(hashCode1, hashCode2);

    // Test ArrowType hashCode with same parameters but different return types
    Node arrowTypeNodeWithReturn1 = createArrowType(null, createTypeLiteral(JSType.NUMBER));
    Node arrowTypeNodeWithReturn2 = createArrowType(null, createTypeLiteral(JSType.STRING));
    ArrowType arrowTypeWithReturn1 = (ArrowType) arrowTypeNodeWithReturn1.getValue(cx);
    ArrowType arrowTypeWithReturn2 = (ArrowType) arrowTypeNodeWithReturn2.getValue(cx);
    int hashCodeWithReturn1 = arrowTypeWithReturn1.hashCode();
    int hashCodeWithReturn2 = arrowTypeWithReturn2.hashCode();
    assertNotEquals(hashCodeWithReturn1, hashCodeWithReturn2);

    leavingContext();
  }

  public void testGetLeastSupertype() {
    JsContext cx = enteringContext();
    ErrorReporter errorReporter = cx.getCurrentErrorReporter();
    StaticScope<JSType> scope = cx.getCurrentScope();

    // Test ArrowType getLeastSupertype with same parameters and same return type
    Node arrowTypeNode1 = createArrowType(null, createTypeLiteral(JSType.NUMBER));
    Node arrowTypeNode2 = createArrowType(null, createTypeLiteral(JSType.NUMBER));
    ArrowType arrowType1 = (ArrowType) arrowTypeNode1.getValue(cx);
    ArrowType arrowType2 = (ArrowType) arrowTypeNode2.getValue(cx);
    ArrowType leastSupertype = arrowType1.getLeastSupertype(arrowType2);
    ArrowType leastSupertype2 = arrowType2.getLeastSupertype(arrowType1);
    assertEquals(arrowType1, leastSupertype);
    assertEquals(arrowType1, leastSupertype2);

    leavingContext();
  }

  public void testGetGreatestSubtype() {
    JsContext cx = enteringContext();
    ErrorReporter errorReporter = cx.getCurrentErrorReporter();
    StaticScope<JSType> scope = cx.getCurrentScope();

    // Test ArrowType getGreatestSubtype with same parameters and same return type
    Node arrowTypeNode1 = createArrowType(null, createTypeLiteral(JSType.NUMBER));
    Node arrowTypeNode2 = createArrowType(null, createTypeLiteral(JSType.NUMBER));
    ArrowType arrowType1 = (ArrowType) arrowTypeNode1.getValue(cx);
    ArrowType arrowType2 = (ArrowType) arrowTypeNode2.getValue(cx);
    ArrowType greatestSubtype = arrowType1.getGreatestSubtype(arrowType2);
    ArrowType greatestSubtype2 = arrowType2.getGreatestSubtype(arrowType1);
    assertEquals(arrowType2, greatestSubtype);
    assertEquals(arrowType2, greatestSubtype2);

    leavingContext();
  }

  public void testToStringHelper() {
    JsContext cx = enteringContext();
    ErrorReporter errorReporter = cx.getCurrentErrorReporter();
    StaticScope<JSType> scope = cx.getCurrentScope();

    // Test ArrowType toStringHelper
    Node arrowTypeNode = createArrowType(null, createTypeLiteral(JSType.NUMBER));
    ArrowType arrowType = (ArrowType) arrowTypeNode.getValue(cx);
    assertEquals("ArrowType", arrowType.toStringHelper(false).toString());

    leavingContext();
  }

}