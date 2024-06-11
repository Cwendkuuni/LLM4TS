package com.google.javascript.jscomp.test;

import static com.google.javascript.rhino.Token.CALL;
import static com.google.javascript.rhino.Token.GETPROP;
import static com.google.javascript.rhino.Token.NAME;
import static com.google.javascript.rhino.Token.STRING;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.TypeRestriction;

import java.util.Map;

/**
 * Test cases for {@link ClosureReverseAbstractInterpreter}.
 */
public class ClosureReverseAbstractInterpreterTest {
  private static final JSTypeRegistry mockTypeRegistry = mock(JSTypeRegistry.class);
  private static final CodingConvention mockCodingConvention = mock(CodingConvention.class);

  @Test
  public void testRestrictToArray() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(mockCodingConvention, mockTypeRegistry);
    JSType expectedArrayType = getNativeType(interpreter.typeRegistry, ARRAY_TYPE);

    JSType result = interpreter.restrictToArrayVisitor.visit(expectedArrayType);
    assertSame(expectedArrayType, result);

    JSType otherType = getNativeType(interpreter.typeRegistry, OBJECT_TYPE);
    result = interpreter.restrictToArrayVisitor.visit(otherType);
    assertNull(result);
  }

  @Test
  public void testRestrictToNotArray() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(mockCodingConvention, mockTypeRegistry);
    JSType expectedObjectType = getNativeType(interpreter.typeRegistry, OBJECT_TYPE);

    JSType result = interpreter.restrictToNotArrayVisitor.visit(expectedObjectType);
    assertSame(expectedObjectType, result);

    JSType arrayType = getNativeType(interpreter.typeRegistry, ARRAY_TYPE);
    result = interpreter.restrictToNotArrayVisitor.visit(arrayType);
    assertNull(result);
  }

  @Test
  public void testRestrictToObject() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(mockCodingConvention, mockTypeRegistry);
    JSType expectedObjectType = getNativeType(interpreter.typeRegistry, NO_OBJECT_TYPE);

    JSType result = interpreter.restrictToObjectVisitor.visit(expectedObjectType);
    assertSame(expectedObjectType, result);

    JSType otherType = getNativeType(interpreter.typeRegistry, ARRAY_TYPE);
    result = interpreter.restrictToObjectVisitor.visit(otherType);
    assertSame(expectedObjectType, result);

    FunctionType functionType = getFunctionType(interpreter.typeRegistry, "() -> void");
    result = interpreter.restrictToObjectVisitor.visit(functionType);
    assertSame(expectedObjectType, result);
  }

  @Test
  public void testRestrictToNotObject() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(mockCodingConvention, mockTypeRegistry);
    JSType expectedObjectType = getNativeType(interpreter.typeRegistry, NO_OBJECT_TYPE);

    JSType result = interpreter.restrictToNotObjectVisitor.visit(expectedObjectType);
    assertNull(result);

    FunctionType functionType = getFunctionType(interpreter.typeRegistry, "() -> void");
    result = interpreter.restrictToNotObjectVisitor.visit(functionType);
    assertNull(result);
  }

  @Test
  public void testRestrictByTypeOfResult() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(mockCodingConvention, mockTypeRegistry);
    JSType expectedStringType = getNativeType(interpreter.typeRegistry, "string");

    JSType result = interpreter.restrictToStringVisitor.visit(expectedStringType);
    assertSame(expectedStringType, result);
  }

  @Test
  public void testRestrictParameter() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(mockCodingConvention, mockTypeRegistry);

    JSType inputType = getNativeType(interpreter.typeRegistry, "number");
    JSType expectedOutputType = getNativeType(interpreter.typeRegistry, "boolean");

    Node conditionNode = createCallNodeWithGetPropChildAndString("goog.isNumber", "input");

    FlowScope blindScope = createBlindFlowScope();

    TypeRestriction typeRestriction = new TypeRestriction(inputType, true);
    Function<TypeRestriction, JSType> restricter = interpreter.restricters.get("isNumber");

    FlowScope restrictedScope = interpreter.restrictParameter(conditionNode.getFirstChild().getFirstChild(), inputType,
        blindScope, restricter, true);

    JSType restrictedType = restrictedScope.getVariable("input").getType();
    assertSame(expectedOutputType, restrictedType);
  }

  private Node createCallNodeWithGetPropChildAndString(String calleeName, String propertyName) {
    Node calleeNode = createNameNode(calleeName);
    Node getPropNode = createNameNode("get");
    Node propertyNode = createNameNode(propertyName);
    Node callNode = createCallNode(calleeNode, propertyNode, getPropNode);

    Node stringNode = createStringNode("input");
    Node paramNode = createNameNode("param");

    Node paramNodeWithStringNode = createCallNode(createNameNode("goog.getName"), propertyNode, stringNode);
    Node propertyChainNode = createCallNode(getPropNode, paramNodeWithStringNode);

    Node conditionNode = createCallNode(calleeNode, propertyChainNode);
    return conditionNode;
  }

  private Node createCallNode(Node... nodes) {
    Node callNode = new Node();
    callNode.setType(CALL);
    callNode.setChildren(nodes);
    return callNode;
  }

  private Node createNameNode(String name) {
    Node node = new Node();
    node.setType(NAME);
    node.setString(name);
    return node;
  }

  private Node createStringNode(String string) {
    Node node = new Node();
    node.setType(STRING);
    node.setString(string);
    return node;
  }

  private FlowScope createBlindFlowScope() {
    FlowScope flowScope = new FlowScope();
    flowScope.setName("BlindScope");
    return flowScope;
  }

  private JSType getNativeType(JSTypeRegistry typeRegistry, String name) {
    return typeRegistry.getNativeType(name);
  }

  private FunctionType getFunctionType(JSTypeRegistry typeRegistry, String signature) {
    return (FunctionType) typeRegistry.getFunctionType(signature);
  }
}