package com.google.javascript.jscomp;

import static com.google.javascript.rhino.Token.CALL;
import static com.google.javascript.rhino.Token.GETPROP;
import static com.google.javascript.rhino.Token.NAME;
import static com.google.javascript.rhino.Token.STRING;
import static com.google.javascript.rhino.jstype.JSTypeNative.ARRAY_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.NULL_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.OBJECT_TYPE;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;
import com.google.javascript.rhino.jstype.Visitor.VisitorException;
import org.junit.Test;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {

  private JSTypeRegistry typeRegistry;
  private ClosureReverseAbstractInterpreter rainterpreter;

  @org.junit.Before
  public void setup() {
    typeRegistry = new JSTypeRegistry();
    rainterpreter = new ClosureReverseAbstractInterpreter(
        new CodingConvention(), typeRegistry);
  }

  @Test
  public void testRestrictToArrayVisitor() throws VisitorException {
    JSType type = typeRegistry.getNativeType(ObjectType.class);
    JSType expectedType = typeRegistry.getNativeType(ARRAY_TYPE);

    JSType result = rainterpreter.restrictToArrayVisitor.visit(type);
    assertNotNull(result);
    assertNotNull(result.isSubtype(expectedType));
  }

  @Test
  public void testRestrictToNotArrayVisitor() throws VisitorException {
    JSType type = typeRegistry.getNativeType(ObjectType.class);
    JSType expectedType = typeRegistry.getNativeType(OBJECT_TYPE);

    JSType result = rainterpreter.restrictToNotArrayVisitor.visit(type);
    assertNull(result);
  }

  @Test
  public void testRestrictToObjectVisitor() throws VisitorException {
    JSType type = typeRegistry.getNativeType(ObjectType.class);
    JSType functionType = typeRegistry.getNativeType(FunctionType.class);

    JSType result = rainterpreter.restrictToObjectVisitor.visit(type);
    assertNotNull(result);
    assertNotNull(result.isSubtype(typeRegistry.getNativeType(NO_OBJECT_TYPE)));

    result = rainterpreter.restrictToObjectVisitor.visit(functionType);
    assertNotNull(result);
    assertNotNull(result.isSubtype(typeRegistry.getNativeType(NO_OBJECT_TYPE)));
  }

  @Test
  public void testRestrictToNotObjectVisitor() throws VisitorException {
    JSType type = typeRegistry.getNativeType(ObjectType.class);

    JSType result = rainterpreter.restrictToNotObjectVisitor.visit(type);
    assertNull(result);
  }

  @Test
  public void testRestrictParameter() throws VisitorException {
    Node parameterNode = new Node(NAME, "param");
    JSType type = typeRegistry.getNativeType(ObjectType.class);

    JSType expectedType = typeRegistry.getNativeType(ARRAY_TYPE);
    rainterpreter.restricters.get("isArray").apply(new TypeRestriction(type, true));

    FlowScope blindScope = new FlowScope(typeRegistry, null, null);
    FlowScope informedScope = rainterpreter.restrictParameter(
        parameterNode, type, blindScope,
        rainterpreter.restricters.get("isArray"), true);

    JSType resultType = informedScope.getVariable("param").getType();
    assertNotNull(resultType);
    assertNotNull(resultType.isSubtype(expectedType));

    expectedType = typeRegistry.getNativeType(OBJECT_TYPE);
    rainterpreter.restricters.get("isObject").apply(new TypeRestriction(type, false));

    informedScope = rainterpreter.restrictParameter(
        parameterNode, type, blindScope,
        rainterpreter.restricters.get("isObject"), false);

    resultType = informedScope.getVariable("param").getType();
    assertNull(resultType);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() throws VisitorException {
    Node conditionNode = new Node(CALL, 2);
    conditionNode.setFirstChild(new Node(GETPROP, null, "goog"));
    conditionNode.setLastChild(new Node(STRING, "isDef"));

    Node parameterNode = new Node(NAME, "param");
    JSType type = typeRegistry.getNativeType(ObjectType.class);

    FlowScope blindScope = new FlowScope(typeRegistry, null, null);
    FlowScope informedScope = rainterpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, blindScope, true);

    JSType resultType = informedScope.getVariable("param").getType();
    assertNotNull(resultType);

    conditionNode.getLastChild().setString("isNull");
    informedScope = rainterpreter.getPreciserScopeKnowingConditionOutcome(
        conditionNode, blindScope, false);

    resultType = informedScope.getVariable("param").getType();
    assertNull(resultType);
  }
}