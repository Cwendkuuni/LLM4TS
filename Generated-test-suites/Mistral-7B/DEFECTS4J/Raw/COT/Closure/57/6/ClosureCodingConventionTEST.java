package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.DefaultCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.JSTypeNative;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.jscomp.SubclassType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;
import java.util.stream.Collectors;

@RunWith(JUnit4.class)
public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = new FunctionType(null, ImmutableList.of(), null);
    FunctionType childCtor = new FunctionType(null, ImmutableList.of(), null);
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    codingConvention.applySubclassRelationship(parentCtor, childCtor, SubclassType.INHERITS);

    FunctionType prototype = childCtor.getPrototype();
    ObjectType constructor = prototype.getPrototype();

    assertTrue(prototype.hasProperty("superClass_"));
    assertSame(parentCtor, prototype.getProperty("superClass_"));
    assertTrue(prototype.hasProperty("constructor"));
    assertSame(childCtor, prototype.getProperty("constructor"));
    assertTrue(constructor.hasProperty("constructor"));
    assertSame(childCtor, constructor.getProperty("constructor"));
  }

  @Test
  public void testApplySubclassRelationship_MIXIN() {
    FunctionType parentCtor = new FunctionType(null, ImmutableList.of(), null);
    FunctionType childCtor = new FunctionType(null, ImmutableList.of(), null);
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    childCtor.setPrototype(parentCtor);
    codingConvention.applySubclassRelationship(parentCtor, childCtor, SubclassType.MIXIN);

    ObjectType prototype = childCtor.getPrototype();

    // Ensure "constructor" property was added to the child prototype.
    assertTrue(prototype.hasProperty("constructor"));
    assertSame(childCtor, prototype.getProperty("constructor"));
  }

  @Test
  public void testGetclasssDefinedByCall() {
    Node node = Node.newNode(Token.CALL, null,
        Node.newNode(Token.GETPROP, null, Node.newNode(Token.IDENT, "goog")),
        Node.newNode(Token.GETPROP, null, Node.newNode(Token.IDENT, "inherits")),
        Node.newNode(Token.IDENT, "SubClass"),
        Node.newNode(Token.IDENT, "SuperClass")
    );

    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    SubclassRelationship relationship = codingConvention.getclasssDefinedByCall(node);

    assertSame(SubclassType.INHERITS, relationship.getType());
    assertSame("SubClass", relationship.getSubclass());
    assertSame("SuperClass", relationship.getSuperclass());
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = Node.newNode(Token.CALL, null,
        Node.newNode(Token.GETPROP, null, Node.newNode(Token.IDENT, "goog")),
        Node.newNode(Token.IDENT, "provide")
    );
    Node parent = Node.newNode(Token.IDENT, "MyParent");

    String className = new ClosureCodingConvention().extractClassNameIfProvide(node, parent);

    assertSame("MyParent", className);
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = Node.newNode(Token.CALL, null,
        Node.newNode(Token.GETPROP, null, Node.newNode(Token.IDENT, "goog")),
        Node.newNode(Token.IDENT, "require")
    );
    Node parent = Node.newNode(Token.IDENT, "MyParent");

    String className = new ClosureCodingConvention().extractClassNameIfRequire(node, parent);

    assertSame("MyParent", className);
  }

  @Test
  public void testGetExportPropertyFunction() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    String exportPropertyFunction = codingConvention.getExportPropertyFunction();

    assertTrue(exportPropertyFunction.equals("goog.exportProperty"));
  }

  @Test
  public void testGetSingletonGetterClassName() {
    Node node = Node.newNode(Token.CALL, null,
        Node.newNode(Token.GETPROP, Node.newNode(Token.IDENT, "goog"),
            Node.newNode(Token.IDENT, "addSingletonGetter")),
        Node.newNode(Token.IDENT, "MyClass")
    );

    String className = new ClosureCodingConvention().getSingletonGetterClassName(node);

    assertSame("MyClass", className);
  }

  @Test
  public void testIsSuperClassReference() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    assertTrue(codingConvention.isSuperClassReference("superClass_"));
  }

  @Test
  public void testEndsWithPrototype() {
    Node node1 = Node.newNode(Token.GETPROP, null, Node.newNode(Token.IDENT, "a"));
    Node node2 = Node.newNode(Token.GETPROP, null, Node.newNode(Token.IDENT, "a"),
        Node.newNode(Token.IDENT, "prototype")
    );

    assertTrue(new ClosureCodingConvention().endsWithPrototype(node2));
    assertFalse(new ClosureCodingConvention().endsWithPrototype(node1));
  }

  @Test
  public void testExtractClassNameIfGoog() {
    Node node = Node.newNode(Token.CALL, null,
        Node.newNode(Token.GETPROP, Node.newNode(Token.IDENT, "goog"),
            Node.newNode(Token.IDENT, "require")),
        Node.newNode(Token.IDENT, "MyClass")
    );

    String className = new ClosureCodingConvention().extractClassNameIfRequire(node, null);

    assertSame("MyClass", className);
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node node = Node.newNode(Token.CALL, null,
        Node.newNode(Token.GETPROP, Node.newNode(Token.IDENT, "goog"),
            Node.newNode(Token.IDENT, "isDef")), Node.newNode(Token.NULL)
    );

    assertTrue(new ClosureCodingConvention().isPropertyTestFunction(node));
  }

  @Test
  public void testGetObjectLiteralCast() {
    Node node = Node.newNode(Token.CALL, null,
        Node.newNode(Token.GETPROP, Node.newNode(Token.IDENT, "goog"),
            Node.newNode(Token.IDENT, "reflect"), Node.newNode(Token.IDENT, "object")),
        Node.newNode(Token.IDENT, "MyObjectType")
    );

    ObjectLiteralCast expectedObjectLiteralCast = new ObjectLiteralCast(
        "MyObjectType", Node.newNode(Token.OBJECTLIT, null)
    );

    ObjectLiteralCast objectLiteralCast = new ClosureCodingConvention().getObjectLiteralCast(
        NodeUtil.getTraverser(Node.TOP_DOWN), node
    );

    assertSame(expectedObjectLiteralCast, objectLiteralCast);
  }

  @Test
  public void testIsOptionalParameter() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    assertFalse(codingConvention.isOptionalParameter(null));
  }

  @Test
  public void testIsVarArgsParameter() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    assertFalse(codingConvention.isVarArgsParameter(null));
  }

  @Test
  public void testIsPrivate() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();

    assertFalse(codingConvention.isPrivate("name"));
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = Node.newNode(Token.CALL, null,
        Node.newNode(Token.GETPROP, Node.newNode(Token.IDENT, "goog"),
            Node.newNode(Token.IDENT, "addDependency")),
        Node.newNode(Token.ARRAYLIT, null),
        Node.newNode(Token.IDENT, "MyTypeName"),
        Node.newNode(Token.NULL),
        Node.newNode(Token.NULL),
        Node.newNode(Token.NULL)
    );

    List<String> typeNames = new ClosureCodingConvention().identifyTypeDeclarationCall(node);

    assertEquals(ImmutableList.of("MyTypeName"), typeNames);
  }

  @Test
  public void testDescribeFunctionBind() {
    Node node = Node.newNode(Token.CALL, null,
        Node.newNode(Token.GETPROP, Node.newNode(Token.IDENT, "goog"),
            Node.newNode(Token.IDENT, "bind")),
        Node.newNode(Token.GETPROP, Node.newNode(Token.IDENT, "myFunction"), Node.newNode(Token.IDENT, "thisVar")),
        Node.newNode(Token.IDENT, "arg1"),
        Node.newNode(Token.IDENT, "arg2")
    );

    Bind bind = new ClosureCodingConvention().describeFunctionBind(node);

    FunctionType fn = bind.getFunctionType();
    ObjectType thisObject = bind.getThisObject();
    List<Node> arguments = bind.getArguments();

    Node function = Node.newNode(Token.FUNCTION, null, null, null, null, null);
    FunctionType expectedFunctionType = new FunctionType(null, arguments, null);

    assertEquals(expectedFunctionType, fn);
    assertSame(thisObject, Node.newNode(Token.IDENT, "thisVar"));
    assertEquals(ImmutableList.of(Node.newNode(Token.IDENT, "arg1"), Node.newNode(Token.IDENT, "arg2")), arguments);
  }

  @Test(expected = AssertionError.class)
  public void testDescribeFunctionBind_MissingFunction() {
    Node node = Node.newNode(Token.CALL, null,
        Node.newNode(Token.GETPROP, Node.newNode(Token.IDENT, "goog"), Node.newNode(Token.IDENT, "bind")),
        Node.newNode(Token.IDENT, "missingFunction")
    );

    new ClosureCodingConvention().describeFunctionBind(node);
  }

  @Test(expected = AssertionError.class)
  public void testDescribeFunctionBind_InvalidCall() {
    Node node = Node.newNode(Token.CALL, null, Node.newNode(Token.IDENT, "goog"), Node.newNode(Token.IDENT, "bind"));

    new ClosureCodingConvention().describeFunctionBind(node);
  }

  @Test
  public void testPropertyTestFunctions() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    List<String> propertyTestFunctions = codingConvention.getAssertionFunctions().stream()
        .map(AssertionFunctionSpec::getName)
        .collect(Collectors.toList());

    ImmutableSet<String> expectedPropertyTestFunctions = ImmutableSet.of(
        "goog.isDef", "goog.isNull", "goog.isDefAndNotNull",
        "goog.isString", "goog.isNumber", "goog.isBoolean",
        "goog.isFunction", "goog.isArray", "goog.isObject"
    );

    assertEquals(expectedPropertyTestFunctions, codingConvention.getPropertyTestFunctions());
  }
}