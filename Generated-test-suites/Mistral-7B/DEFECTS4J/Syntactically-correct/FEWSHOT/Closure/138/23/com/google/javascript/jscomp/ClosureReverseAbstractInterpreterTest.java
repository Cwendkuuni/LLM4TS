package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.ClosureReverseAbstractInterpreter.TypeRestriction;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;
import com.google.javascript.rhino.Node;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testRestrictIsDef() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(new CodingConvention(), typeRegistry);

    JSType type = createType("number");
    TypeRestriction restriction = new TypeRestriction(type, true);
    assertEquals(getNativeType(ObjectType.class), interpreter.getRestricted(restriction));

    type = createType("boolean");
    restriction = new TypeRestriction(type, false);
    assertNull(interpreter.getRestricted(restriction));
  }

  @Test
  public void testRestrictIsArray() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(new CodingConvention(), typeRegistry);

    JSType type = createType("number[]");
    TypeRestriction restriction = new TypeRestriction(type, true);
    assertEquals(getNativeType(JSType.ARRAY_TYPE), interpreter.getRestricted(restriction));

    type = createType("boolean");
    restriction = new TypeRestriction(type, false);
    assertNull(interpreter.getRestricted(restriction));
  }

  @Test
  public void testRestrictIsObject() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(new CodingConvention(), typeRegistry);

    JSType type = createType("Object");
    TypeRestriction restriction = new TypeRestriction(type, true);
    assertEquals(getNativeType(ObjectType.class), interpreter.getRestricted(restriction));

    type = createType("number");
    restriction = new TypeRestriction(type, false);
    assertNull(interpreter.getRestricted(restriction));
  }

  @Test
  public void testRestrictIsFunction() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(new CodingConvention(), typeRegistry);

    JSType type = createFunctionType("() => {}");
    TypeRestriction restriction = new TypeRestriction(type, true);
    assertEquals(type, interpreter.getRestricted(restriction));

    type = createType("number");
    restriction = new TypeRestriction(type, false);
    assertNull(interpreter.getRestricted(restriction));
  }

  @Test
  public void testRestrictIsString() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(new CodingConvention(), typeRegistry);

    JSType type = createType("string");
    TypeRestriction restriction = new TypeRestriction(type, true);
    assertEquals(type, interpreter.getRestricted(restriction));

    type = createType("number");
    restriction = new TypeRestriction(type, false);
    assertNull(interpreter.getRestricted(restriction));
  }

  @Test
  public void testRestrictIsBoolean() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(new CodingConvention(), typeRegistry);

    JSType type = createType("boolean");
    TypeRestriction restriction = new TypeRestriction(type, true);
    assertEquals(type, interpreter.getRestricted(restriction));

    type = createType("number");
    restriction = new TypeRestriction(type, false);
    assertNull(interpreter.getRestricted(restriction));
  }

  @Test
  public void testRestrictIsNumber() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(new CodingConvention(), typeRegistry);

    JSType type = createType("number");
    TypeRestriction restriction = new TypeRestriction(type, true);
    assertEquals(type, interpreter.getRestricted(restriction));

    type = createType("boolean");
    restriction = new TypeRestriction(type, false);
    assertNull(interpreter.getRestricted(restriction));
  }

  @Test
  public void testRestrictIsDefAndNotNull() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(new CodingConvention(), typeRegistry);

    JSType type = createType("number");
    TypeRestriction restriction = new TypeRestriction(type, true);
    assertNull(interpreter.getRestricted(restriction));

    restriction = new TypeRestriction(type, false);
    assertNull(interpreter.getRestricted(restriction));

    restriction = new TypeRestriction(type, true);
    type = interpreter.getRestricted(restriction);
    assertEquals(type, interpreter.getRestricted(new TypeRestriction(type, false)));
    assertNull(interpreter.getRestricted(new TypeRestriction(type, false)));
  }

  @Test
  public void testRestrictIsNull() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(new CodingConvention(), typeRegistry);

    JSType type = createType("number");
    TypeRestriction restriction = new TypeRestriction(type, false);
    assertNull(interpreter.getRestricted(restriction));

    restriction = new TypeRestriction(null, true);
    assertEquals(getNativeType(JSType.NULL_TYPE), interpreter.getRestricted(restriction));

    restriction = new TypeRestriction(null, false);
    assertNull(interpreter.getRestricted(restriction));
  }

  private Node createNode(String typeString) {
    Node node = new Node();
    node.setType(getType(typeString));
    return node;
  }

  private Node createFunctionType(String functionExpression) {
    Node node = new Node();
    node.setType(FunctionType.class);
    Node params = new Node();
    params.setType(JSType.ARRAY_TYPE);
    params.setChild(new Node().setType(JSType.NULL_TYPE));
    node.setChild(params);
    node.setChild(new Node().setType(Node.CALL));
    Node callee = new Node();
    callee.setType(getNameType("Function"));
    callee.setChild(new Node().setType(NAME).setString("Function"));
    callee.setChild(new Node().setType(STRING).setString(functionExpression));
    node.setChild(callee);
    return node;
  }

  private Node createType(String typeString) {
    Node node = new Node();
    node.setType(getType(typeString));
    return node;
  }

  private JSType getType(String typeString) {
    if ("number".equals(typeString)) {
      return JSType.NUMBER;
    } else if ("boolean".equals(typeString)) {
      return JSType.BOOLEAN;
    } else if ("string".equals(typeString)) {
      return JSType.STRING;
    } else if ("Object".equals(typeString)) {
      return ObjectType.class;
    } else if ("function".equals(typeString)) {
      return FunctionType.class;
    } else if (typeString.endsWith("[]")) {
      String elementType = typeString.substring(0, typeString.length() - 2);
      if ("number".equals(elementType)) {
        return JSType.ARRAY_TYPE;
      } else {
        return getType(elementType);
      }
    } else {
      return null;
    }
  }

  private Node getNameType(String name) {
    Node nameNode = new Node();
    nameNode.setType(NAME);
    nameNode.setString(name);
    return nameNode;
  }

  private JSType getNativeType(Class<?> clazz) {
    return getNativeTypeRegistry().lookup(clazz);
  }

  private JSType getNativeType(int token) {
    return getNativeTypeRegistry().lookup(token);
  }

  private JSTypeRegistry getNativeTypeRegistry() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    typeRegistry.put(ObjectType.class, getNativeType(ObjectType.class));
    typeRegistry.put(FunctionType.class, getNativeType(FunctionType.class));
    typeRegistry.put(JSType.ARRAY_TYPE, getNativeType(JSType.ARRAY_TYPE));
    typeRegistry.put(JSType.NULL_TYPE, getNativeType(JSType.NULL_TYPE));
    return typeRegistry;
  }
}