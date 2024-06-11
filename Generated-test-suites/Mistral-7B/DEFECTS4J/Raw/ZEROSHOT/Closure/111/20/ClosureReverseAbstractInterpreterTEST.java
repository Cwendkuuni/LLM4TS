import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  private ClosureReverseAbstractInterpreter interpreter;
  private JSTypeRegistry typeRegistry;
  private Node conditionNode;
  private Node parameterNode;
  private Node functionNameNode;
  private Node stringNode;
  private FunctionType functionType;
  private ObjectType objectType;
  private JSType resultType;
  private JSType arrayType;
  private JSType numberType;
  private JSType booleanType;
  private JSType nullType;
  private JSType voidType;
  private JSType noObjectType;

  @Before
  public void setup() {
    typeRegistry = mock(JSTypeRegistry.class);
    interpreter = new ClosureReverseAbstractInterpreter(new CodingConvention(),
        typeRegistry);
    conditionNode = Node.newCall(null, "goog.isDef", Node.newQualifiedName("foo"),
        Node.newNull());
    parameterNode = Node.newName("foo");
    functionNameNode = Node.newName("isDef");
    stringNode = Node.newString("foo");
    functionType = new FunctionType(Node.newName("foo"), new Type[] {});
    objectType = new ObjectType("Object");
    arrayType = new ObjectType("Array");
    numberType = typeRegistry.createNumberType();
    booleanType = typeRegistry.createBooleanType();
    nullType = typeRegistry.createNullType();
    voidType = typeRegistry.createVoidType();
    noObjectType = typeRegistry.createNoObjectType();
    when(typeRegistry.getNativeType(ARRAY_TYPE)).thenReturn(arrayType);
    when(typeRegistry.getNativeType(OBJECT_TYPE)).thenReturn(objectType);
    when(typeRegistry.getNativeType(NO_OBJECT_TYPE)).thenReturn(noObjectType);
  }

  @Test
  public void testIsDefTrue() {
    JSType expectedType = getRestrictedWithoutUndefined(objectType);
    when(typeRegistry.getTypeIfRefinable(parameterNode, null)).thenReturn(objectType);
    resultType = interpreter.getPreciserScopeKnowingConditionOutcome(conditionNode,
        new FlowScope(parameterNode, null), true).getScopeVariable("foo").getType();
    assertEquals(expectedType, resultType);
  }

  @Test
  public void testIsDefFalse() {
    resultType = interpreter.getPreciserScopeKnowingConditionOutcome(conditionNode,
        new FlowScope(parameterNode, null), false).getScopeVariable("foo").getType();
    assertEquals(nullType, resultType);
  }

  @Test
  public void testIsDefNullParameter() {
    JSType expectedType = getRestrictedWithoutNull(nullType);
    when(typeRegistry.getTypeIfRefinable(parameterNode, null)).thenReturn(nullType);
    resultType = interpreter.getPreciserScopeKnowingConditionOutcome(conditionNode,
        new FlowScope(parameterNode, null), true).getScopeVariable("foo").getType();
    assertEquals(expectedType, resultType);
  }

  @Test
  public void testIsDefNotNullParameter() {
    JSType expectedType = getRestrictedWithoutUndefined(objectType);
    when(typeRegistry.getTypeIfRefinable(parameterNode, null)).thenReturn(objectType);
    resultType = interpreter.getPreciserScopeKnowingConditionOutcome(conditionNode,
        new FlowScope(parameterNode, null), false).getScopeVariable("foo").getType();
    assertEquals(nullType, resultType);
  }

  @Test
  public void testIsStringTrue() {
    resultType = interpreter.getPreciserScopeKnowingConditionOutcome(
        Node.newCall(null, "goog.isString", Node.newQualifiedName("foo")),
        new FlowScope(parameterNode, null), true).getScopeVariable("foo").getType();
    assertEquals(stringType, resultType);
  }

  @Test
  public void testIsStringFalse() {
    JSType expectedType = typeRegistry.createUnionType(numberType, booleanType);
    resultType = interpreter.getPreciserScopeKnowingConditionOutcome(
        Node.newCall(null, "goog.isString", Node.newQualifiedName("foo")),
        new FlowScope(parameterNode, null), false).getScopeVariable("foo").getType();
    assertEquals(expectedType, resultType);
  }

  @Test
  public void testIsBooleanTrue() {
    resultType = interpreter.getPreciserScopeKnowingConditionOutcome(
        Node.newCall(null, "goog.isBoolean", Node.newQualifiedName("foo")),
        new FlowScope(parameterNode, null), true).getScopeVariable("foo").getType();
    assertEquals(booleanType, resultType);
  }

  @Test
  public void testIsBooleanFalse() {
    JSType expectedType = typeRegistry.createUnionType(numberType, booleanType);
    resultType = interpreter.getPreciserScopeKnowingConditionOutcome(
        Node.newCall(null, "goog.isBoolean", Node.newQualifiedName("foo")),
        new FlowScope(parameterNode, null), false).getScopeVariable("foo").getType();
    assertEquals(expectedType, resultType);
  }

  @Test
  public void testIsNumberTrue() {
    resultType = interpreter.getPreciserScopeKnowingConditionOutcome(
        Node.newCall(null, "goog.isNumber", Node.newQualifiedName("foo")),
        new FlowScope(parameterNode, null), true).getScopeVariable("foo").getType();
    assertEquals(numberType, resultType);
  }

  @Test
  public void testIsNumberFalse() {
    JSType expectedType = typeRegistry.createUnionType(numberType, booleanType);
    resultType = interpreter.getPreciserScopeKnowingConditionOutcome(
        Node.newCall(null, "goog.isNumber", Node.newQualifiedName("foo")),
        new FlowScope(parameterNode, null), false).getScopeVariable("foo").getType();
    assertEquals(expectedType, resultType);
  }

  @Test
  public void testIsFunctionTrue() {
    JSType expectedType = functionType;
    resultType = interpreter.getPreciserScopeKnowingConditionOutcome(
        Node.newCall(null, "goog.isFunction", Node.newQualifiedName("foo")),
        new FlowScope(parameterNode, null), true).getScopeVariable("foo").getType();
    assertEquals(expectedType, resultType);
  }

  @Test
  public void testIsFunctionFalse() {
    JSType expectedType = typeRegistry.createUnionType(numberType, booleanType);
    resultType = interpreter.getPreciserScopeKnowingConditionOutcome(
        Node.newCall(null, "goog.isFunction", Node.newQualifiedName("foo")),
        new FlowScope(parameterNode, null), false).getScopeVariable("foo").getType();
    assertEquals(expectedType, resultType);
  }

  @Test
  public void testIsArrayTrue() {
    JSType expectedType = arrayType;
    resultType = interpreter.getPreciserScopeKnowingConditionOutcome(
        Node.newCall(null, "goog.isArray", Node.newQualifiedName("foo")),
        new FlowScope(parameterNode, null), true).getScopeVariable("foo").getType();
    assertEquals(expectedType, resultType);
  }

  @Test
  public void testIsArrayFalse() {
    resultType = interpreter.getPreciserScopeKnowingConditionOutcome(
        Node.newCall(null, "goog.isArray", Node.newQualifiedName("foo")),
        new FlowScope(parameterNode, null), false).getScopeVariable("foo").getType();
    assertEquals(nullType, resultType);
  }

  @Test
  public void testIsObjectTrue() {
    JSType expectedType = objectType;
    resultType = interpreter.getPreciserScopeKnowingConditionOutcome(
        Node.newCall(null, "goog.isObject", Node.newQualifiedName("foo")),
        new FlowScope(parameterNode, null), true).getScopeVariable("foo").getType();
    assertEquals(expectedType, resultType);
  }

  @Test
  public void testIsObjectFalse() {
    resultType = interpreter.getPreciserScopeKnowingConditionOutcome(
        Node.newCall(null, "goog.isObject", Node.newQualifiedName("foo")),
        new FlowScope(parameterNode, null), false).getScopeVariable("foo").getType();
    assertEquals(noObjectType, resultType);
  }

  private JSType getRestrictedWithoutUndefined(JSType type) {
    return interpreter.getNativeType(VOID_TYPE).getGreatestSubtype(type);
  }

  private JSType getRestrictedWithoutNull(JSType type) {
    return interpreter.getNativeType(NO_OBJECT_TYPE).getGreatestSubtype(type);
  }

  private JSType getRestrictedByTypeOfResult(JSType type, String expectedTypeString,
      boolean expectedOutcome) {
    Visitor<JSType> visitor = expectedOutcome ? interpreter.restrictToObjectVisitor :
        interpreter.restrictToNotObjectVisitor;
    return type.visit(visitor);
  }

  private JSType getRestrictedByTypeOfResult(JSType type, String expectedTypeString,
      boolean expectedOutcome, boolean isArray) {
    Visitor<JSType> visitor;
    if (isArray) {
      visitor = expectedOutcome ? interpreter.restrictToArrayVisitor :
          interpreter.restrictToNotArrayVisitor;
    } else {
      visitor = expectedOutcome ? interpreter.restrictToObjectVisitor :
          interpreter.restrictToNotObjectVisitor;
    }
    return type.visit(visitor);
  }
}