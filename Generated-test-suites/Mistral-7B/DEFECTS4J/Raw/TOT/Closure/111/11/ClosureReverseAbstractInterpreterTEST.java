import static org.junit.Assert.*;
import static com.google.javascript.jscomp.type.JSTypeNative.*;

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
  private CodingConvention convention;
  private JSTypeRegistry typeRegistry;
  private ClosureReverseAbstractInterpreter interpreter;

  @Before
  public void setup() {
    this.convention = new CodingConvention();
    this.typeRegistry = new JSTypeRegistry();
    this.interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
  }

  @Test
  public void testIsDef() {
    JSType arrayType = getNativeType(ARRAY_TYPE);
    JSType objectType = getNativeType(OBJECT_TYPE);
    JSType functionType = getFunctionType(Types.VOID, Types.BOOLEAN);
    JSType undefinedType = getNativeType(VOID_TYPE).getGreatestSubtype(null);

    // true positive: defined array
    Node definedArray = Node.newCall(Types.ID, "goog", "isArray",
        Node.objectLiteral(Types.ID, "array"));
    JSType expectedArray = arrayType;
    JSType actualArray = interpreter.getPreciserScopeKnowingConditionOutcome(
        definedArray, null, true).getDeclaredVariable("array").type;
    assertEquals(expectedArray, actualArray);

    // true positive: defined object
    Node definedObject = Node.newCall(Types.ID, "goog", "isObject",
        Node.objectLiteral(Types.ID, "object"));
    JSType expectedObject = objectType;
    JSType actualObject = interpreter.getPreciserScopeKnowingConditionOutcome(
        definedObject, null, true).getDeclaredVariable("object").type;
    assertEquals(expectedObject, actualObject);

    // true positive: defined function
    Node definedFunction = Node.newCall(Types.ID, "goog", "isFunction",
        Node.objectLiteral(Types.ID, "function"));
    JSType expectedFunction = functionType;
    JSType actualFunction = interpreter.getPreciserScopeKnowingConditionOutcome(
        definedFunction, null, true).getDeclaredVariable("function").type;
    assertEquals(expectedFunction, actualFunction);

    // false positive: undefined array
    Node undefinedArray = Node.newCall(Types.ID, "goog", "isArray", null);
    JSType expectedUndefinedArray = undefinedType;
    JSType actualUndefinedArray = interpreter.getPreciserScopeKnowingConditionOutcome(
        undefinedArray, null, false).getDeclaredVariable("array").type;
    assertEquals(expectedUndefinedArray, actualUndefinedArray);

    // false positive: undefined object
    Node undefinedObject = Node.newCall(Types.ID, "goog", "isObject", null);
    JSType expectedUndefinedObject = undefinedType;
    JSType actualUndefinedObject = interpreter.getPreciserScopeKnowingConditionOutcome(
        undefinedObject, null, false).getDeclaredVariable("object").type;
    assertEquals(expectedUndefinedObject, actualUndefinedObject);

    // false positive: undefined function
    Node undefinedFunction = Node.newCall(Types.ID, "goog", "isFunction", null);
    JSType expectedUndefinedFunction = undefinedType;
    JSType actualUndefinedFunction = interpreter.getPreciserScopeKnowingConditionOutcome(
        undefinedFunction, null, false).getDeclaredVariable("function").type;
    assertEquals(expectedUndefinedFunction, actualUndefinedFunction);
  }

  @Test
  public void testIsDefAndNotNull() {
    JSType numberType = getNativeType(NUMBER_TYPE);
    JSType booleanType = getNativeType(BOOLEAN_TYPE);
    JSType nullType = getNativeType(NULL_TYPE);
    JSType voidType = getNativeType(VOID_TYPE);
    JSType undefinedType = getNativeType(VOID_TYPE).getGreatestSubtype(null);

    // true positive: defined and not null number
    Node definedAndNotNullNumber = Node.newCall(Types.ID, "goog", "isDefAndNotNull",
        Node.objectLiteral(Types.NUMBER, 123));
    JSType expectedDefAndNotNullNumber = numberType;
    JSType actualDefAndNotNullNumber = interpreter.getPreciserScopeKnowingConditionOutcome(
        definedAndNotNullNumber, null, true).getDeclaredVariable("number").type;
    assertEquals(expectedDefAndNotNullNumber, actualDefAndNotNullNumber);

    // false positive: undefined number
    Node undefinedNumber = Node.newCall(Types.ID, "goog", "isDefAndNotNull", null);
    JSType expectedUndefinedNumber = undefinedType;
    JSType actualUndefinedNumber = interpreter.getPreciserScopeKnowingConditionOutcome(
        undefinedNumber, null, false).getDeclaredVariable("number").type;
    assertEquals(expectedUndefinedNumber, actualUndefinedNumber);

    // false positive: null number
    Node nullNumber = Node.newCall(Types.ID, "goog", "isDefAndNotNull",
        Node.objectLiteral(Types.NULL, null));
    JSType expectedNullNumber = nullType;
    JSType actualNullNumber = interpreter.getPreciserScopeKnowingConditionOutcome(
        nullNumber, null, false).getDeclaredVariable("number").type;
    assertEquals(expectedNullNumber, actualNullNumber);
  }

  @Test
  public void testIsString() {
    JSType stringType = getNativeType(STRING_TYPE);
    JSType booleanType = getNativeType(BOOLEAN_TYPE);
    JSType numberType = getNativeType(NUMBER_TYPE);

    // true positive: string
    Node stringNode = Node.newStringLiteral("hello");
    JSType expectedString = stringType;
    JSType actualString = interpreter.getPreciserScopeKnowingConditionOutcome(
        stringNode, null, true).getDeclaredVariable("string").type;
    assertEquals(expectedString, actualString);

    // false positive: boolean
    Node booleanNode = Node.newBoolean(true);
    JSType expectedBoolean = booleanType;
    JSType actualBoolean = interpreter.getPreciserScopeKnowingConditionOutcome(
        booleanNode, null, false).getDeclaredVariable("boolean").type;
    assertEquals(expectedBoolean, actualBoolean);

    // false positive: number
    Node numberNode = Node.newNumber(123);
    JSType expectedNumber = numberType;
    JSType actualNumber = interpreter.getPreciserScopeKnowingConditionOutcome(
        numberNode, null, false).getDeclaredVariable("number").type;
    assertEquals(expectedNumber, actualNumber);
  }

  // Add similar tests for isBoolean, isNumber, isFunction, isArray, and isObject.

  private Node getFunctionType(JSType... types) {
    ObjectType functionObjectType = typeRegistry.createFunctionType(types);
    return Node.newCall(Types.ID, "function", null, Types.VOID,
        Types.createList(functionObjectType.getParameterTypes()));
  }

  private JSType getNativeType(int typeCode) {
    return typeRegistry.getNativeType(typeCode);
  }

  private JSType getNativeType(String typeName) {
    return typeRegistry.getNativeType(typeName);
  }

  private JSType getTypeIfRefinable(Node node, FlowScope scope) {
    if (node.isGetProp() && node.getLastChild().isName() &&
        node.getFirstChild().isThis()) {
      String propertyName = node.getLastChild().getString();
      JSType type = scope.getVariable(propertyName).type;
      if (type != null) {
        return type;
      }
    }

    return null;
  }
}