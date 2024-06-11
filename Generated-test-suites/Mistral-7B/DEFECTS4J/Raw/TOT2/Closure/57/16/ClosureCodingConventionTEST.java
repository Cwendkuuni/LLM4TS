import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ClosureCodingConvention.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.Token;

import java.util.List;

public class ClosureCodingConventionTest {

  @org.junit.Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = newFunctionType(Token.FUNCTION, "ParentCtor");
    FunctionType childCtor = newFunctionType(Token.FUNCTION, "ChildCtor");
    SubclassType type = SubclassType.INHERITS;

    applySubclassRelationship(parentCtor, childCtor, type);

    assertNotNull(childCtor.getDeclaredProperty("superClass_"));
    assertNotNull(childCtor.getPrototype().getDeclaredProperty("constructor"));
  }

  @org.junit.Test
  public void testApplySubclassRelationship_mixin() {
    FunctionType parentCtor = newFunctionType(Token.FUNCTION, "ParentCtor");
    FunctionType childCtor = newFunctionType(Token.FUNCTION, "ChildCtor");
    SubclassType type = SubclassType.MIXIN;

    applySubclassRelationship(parentCtor, childCtor, type);

    assertNotNull(childCtor.getPrototype().getDeclaredProperty("constructor"));
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testApplySubclassRelationship_invalidArguments() {
    FunctionType parentCtor = newFunctionType(Token.FUNCTION, "ParentCtor");
    FunctionType childCtor = newFunctionType(Token.FUNCTION, "ChildCtor");
    SubclassType type = null;

    applySubclassRelationship(parentCtor, childCtor, type);
  }

  @org.junit.Test
  public void testExtractClassNameIfProvide() {
    Node node = newNode(Token.CALL, "goog.provide", newNode(Token.STRING, "X"));
    String className = extractClassNameIfProvide(node, null);
    assertEquals("X", className);
  }

  @org.junit.Test
  public void testExtractClassNameIfRequire() {
    Node node = newNode(Token.CALL, "goog.require", newNode(Token.STRING, "X"));
    String className = extractClassNameIfRequire(node, null);
    assertEquals("X", className);
  }

  @org.junit.Test
  public void testExtractClassNameIfGoog_invalidCall() {
    Node node = newNode(Token.CALL, "goog.foo", null);
    String className = extractClassNameIfGoog(node, null, "goog.require");
    assertNull(className);
  }

  @org.junit.Test
  public void testGetExportPropertyFunction() {
    assertEquals("goog.exportProperty", getExportPropertyFunction());
  }

  @org.junit.Test
  public void testGetExportSymbolFunction() {
    assertEquals("goog.exportSymbol", getExportSymbolFunction());
  }

  @org.junit.Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = newNode(Token.CALL, "goog.addDependency",
        newNode(Token.ARRAYLIT, newNode(Token.STRING, "X"), null),
        newNode(Token.ARRAYLIT, null));
    List<String> typeNames = identifyTypeDeclarationCall(node);
    assertEquals(1, typeNames.size());
    assertEquals("X", typeNames.get(0));
  }

  @org.junit.Test
  public void testApplySingletonGetter() {
    FunctionType functionType = newFunctionType(Token.FUNCTION, "Function");
    FunctionType getterType = newFunctionType(Token.FUNCTION, "Getter");
    ObjectType objectType = newObjectType("Object");

    applySingletonGetter(functionType, getterType, objectType);

    assertNotNull(functionType.getDeclaredProperty("getInstance"));
    assertNotNull(functionType.getDeclaredProperty("instance_"));
  }

  @org.junit.Test
  public void testGetGlobalObject() {
    assertEquals("goog.global", getGlobalObject());
  }

  @org.junit.Test
  public void testIsPropertyTestFunction() {
    Node call = newNode(Token.CALL, newNode(Token.GETPROP, newNode(Token.STRING, "goog.isDef")),
        newNode(Token.STRING, "foo"));
    assertTrue(isPropertyTestFunction(call));
  }

  @org.junit.Test
  public void testGetObjectLiteralCast() {
    NodeTraversal traversal = new NodeTraversal();
    Node callNode = newNode(Token.CALL, "goog.reflect.object",
        newNode(Token.STRING, "X"), newNode(Token.OBJECTLIT, "x", "y", "z"));
    ObjectLiteralCast cast = getObjectLiteralCast(traversal, callNode);
    assertEquals("X", cast.typeName);
    assertEquals("x", cast.object);
  }

  @org.junit.Test
  public void testIsOptionalParameter() {
    Node parameter = newNode(Token.VAR, "param", newNode(Token.QMARK, null));
    assertFalse(isOptionalParameter(parameter));
  }

  @org.junit.Test
  public void testIsVarArgsParameter() {
    Node parameter = newNode(Token.VAR, "param", newNode(Token.ELLIPSIS, null));
    assertFalse(isVarArgsParameter(parameter));
  }

  @org.junit.Test
  public void testIsPrivate() {
    assertFalse(isPrivate("privateField"));
  }

  @org.junit.Test
  public void testGetAssertionFunctions() {
    List<AssertionFunctionSpec> functions = getAssertionFunctions();
    assertEquals(8, functions.size());
  }

  @org.junit.Test
  public void testDescribeFunctionBind() {
    Node n = newNode(Token.CALL, "goog.bind",
        newNode(Token.IDENT, "fn"), newNode(Token.NULL),
        newNode(Token.ARRAYLIT, newNode(Token.STRING, "self"), newNode(Token.NULL)));
    Bind bind = describeFunctionBind(n);
    assertEquals("fn", bind.function.getQualifiedName());
    assertEquals("self", bind.thisValue.getString());
    assertEquals(1, bind.parameters.size());
    assertEquals(Token.NULL, bind.parameters.get(0).getValue());
  }
}