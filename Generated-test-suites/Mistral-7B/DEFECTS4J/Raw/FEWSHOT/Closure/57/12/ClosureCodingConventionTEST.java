package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.jscomp.SubclassType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeTraversal;
import org.junit.Test;

/**
 * Tests for the ClosureCodingConvention class.
 */
public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = new FunctionType("ParentCtor", ImmutableList.of());
    FunctionType childCtor = new FunctionType("ChildCtor", ImmutableList.of());
    SubclassType type = SubclassType.INHERITS;
    new ClosureCodingConvention().applySubclassRelationship(parentCtor, childCtor, type);

    Preconditions.checkState(parentCtor.getPrototype().hasProperty("constructor"));
    Preconditions.checkState(childCtor.getPrototype().hasProperty("constructor"));
    Preconditions.checkState(childCtor.getPrototype().hasProperty("superClass_"));

    FunctionType parentPrototype = parentCtor.getPrototype();
    FunctionType childPrototype = childCtor.getPrototype();

    Preconditions.checkState(parentPrototype.getProperty("constructor")
        .getReferenceType().isSameType(childPrototype));
    Preconditions.checkState(childPrototype.getProperty("superClass_")
        .getValueType().isSameType(parentPrototype));
  }

  @Test
  public void testGetclasssDefinedByCall() {
    Node node = Node.newNode(
        Token.CALL,
        Node.newNode(Token.GETPROP, Node.newNode(Token.NAME, "goog.inherits"),
            Node.newQualifiedName("SubClass")),
        Node.newQualifiedName("SuperClass"));

    SubclassRelationship relationship = new ClosureCodingConvention()
        .getclasssDefinedByCall(node);

    assertNull(relationship);

    node = Node.newNode(
        Token.CALL,
        Node.newNode(Token.GETPROP, Node.newName("goog.inherits"),
            Node.newQualifiedName("SubClass")),
        Node.newQualifiedName("SuperClass"));

    relationship = new ClosureCodingConvention().getclasssDefinedByCall(node);

    Preconditions.checkState(relationship != null);
    Preconditions.checkState(relationship.getType() == SubclassType.INHERITS);
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = Node.newNode(
        Token.CALL,
        Node.newName("goog.provide"),
        Node.newQualifiedName("X"));

    String className = new ClosureCodingConvention()
        .extractClassNameIfProvide(node, null);

    assertThat(className).isEqualTo("X");
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = Node.newNode(
        Token.CALL,
        Node.newName("goog.require"),
        Node.newQualifiedName("X"));

    String className = new ClosureCodingConvention()
        .extractClassNameIfRequire(node, null);

    assertThat(className).isEqualTo("X");
  }

  @Test
  public void testIsSuperClassReference() {
    assertThat(new ClosureCodingConvention().isSuperClassReference("superClass_"))
        .isTrue();
  }

  @Test
  public void testEndsWithPrototype() {
    Node node = Node.newNode(Token.QUALIFIED,
        Node.newName("a"), Node.newName("b"));

    Node prototypeNode = Node.newNode(Token.GETPROP, node,
        new Node("prototype"));

    assertThat(new ClosureCodingConvention().endsWithPrototype(prototypeNode))
        .isTrue();

    assertThat(new ClosureCodingConvention().endsWithPrototype(node))
        .isFalse();
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = Node.newNode(
        Token.CALL,
        Node.newName("goog.addDependency"),
        Node.newQualifiedName("X"),
        Node.newArrayLiteral(
            Node.newString("goog.net.XhrIo"),
            Node.newString("goog.net.EventType"),
            Node.newString("goog.net.EventType.CREATE")
        ));

    List<String> typeNames = new ClosureCodingConvention()
        .identifyTypeDeclarationCall(node);

    assertThat(typeNames).hasSize(3);
    assertThat(typeNames).containsExactly("goog.net.XhrIo",
        "goog.net.EventType", "goog.net.EventType.CREATE");
  }

  @Test
  public void testGetAbstractMethodName() {
    String name = new ClosureCodingConvention().getAbstractMethodName();

    assertThat(name).isEqualTo("goog.abstractMethod");
  }

  @Test
  public void testGetSingletonGetterClassName() {
    Node node = Node.newNode(
        Token.CALL,
        Node.newName("goog.addSingletonGetter"),
        Node.newQualifiedName("getX"),
        Node.newQualifiedName("X"));

    String className = new ClosureCodingConvention()
        .getSingletonGetterClassName(node);

    assertThat(className).isEqualTo("X");
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType functionType = new FunctionType("X", ImmutableList.of());
    FunctionType getterType = new FunctionType("getX", ImmutableList.of());
    ObjectType objectType = new ObjectType("X");

    new ClosureCodingConvention().applySingletonGetter(functionType,
        getterType, objectType);

    Preconditions.checkState(functionType.hasProperty("getInstance"));
    Preconditions.checkState(functionType.hasProperty("instance_"));
  }

  @Test
  public void testGetGlobalObject() {
    String name = new ClosureCodingConvention().getGlobalObject();

    assertThat(name).isEqualTo("goog.global");
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node node = Node.newNode(
        Token.CALL,
        Node.newName("goog.isDef"),
        Node.newString("foo"));

    boolean isPropertyTestFunction = new ClosureCodingConvention()
        .isPropertyTestFunction(node);

    assertThat(isPropertyTestFunction).isTrue();
  }

  @Test
  public void testGetObjectLiteralCast() {
    Node node = Node.newNode(
        Token.CALL,
        Node.newName("goog.reflect.object"),
        Node.newQualifiedName("X"),
        Node.newQualifiedName("Y"));

    ObjectLiteralCast cast = new ClosureCodingConvention()
        .getObjectLiteralCast(new NodeTraversal(), node);

    Preconditions.checkState(cast != null);
    Preconditions.checkState(cast.getTypeName().equals("X"));
    Preconditions.checkState(cast.getCastExpression() != null);
  }

  @Test
  public void testIsOptionalParameter() {
    Node node = Node.newNode(Token.VARDECL, Node.newName("x"),
        Node.newToken(Token.QUESTION), Node.newToken(Token.IDENT),
        Node.newList(Node.newList(Node.newToken(Token.SEMICOLON))));

    boolean isOptionalParameter = new ClosureCodingConvention()
        .isOptionalParameter(node);

    assertThat(isOptionalParameter).isTrue();
  }

  @Test
  public void testIsVarArgsParameter() {
    Node node = Node.newNode(Token.VARDECL, Node.newName("x"),
        Node.newList(Node.newToken(Token.ELLIPSIS)),
        Node.newList(Node.newToken(Token.SEMICOLON)));

    boolean isVarArgsParameter = new ClosureCodingConvention()
        .isVarArgsParameter(node);

    assertThat(isVarArgsParameter).isTrue();
  }

  @Test
  public void testIsPrivate() {
    String name = "x";

    boolean isPrivate = new ClosureCodingConvention().isPrivate(name);

    assertThat(isPrivate).isFalse();
  }

  @Test
  public void testGetAssertionFunctions() {
    Collection<AssertionFunctionSpec> functions = new ClosureCodingConvention()
        .getAssertionFunctions();

    Preconditions.checkState(functions.size() == 8);
  }

  @Test
  public void testDescribeFunctionBind() {
    Node node = Node.newNode(
        Token.CALL,
        Node.newName("goog.bind"),
        Node.newName("fn"),
        Node.newThis(),
        Node.newList(Node.newList(Node.newToken(Token.SEMICOLON))));

    Bind bind = new ClosureCodingConvention().describeFunctionBind(node);

    Preconditions.checkState(bind != null);
    Preconditions.checkState(bind.getFunction().getName().equals("fn"));
    Preconditions.checkState(bind.getThisValue() != null);
    Preconditions.checkState(bind.getParameters().get(0) != null);
  }

  @Test
  public void testDescribeFunctionBindWithPartial() {
    Node node = Node.newNode(
        Token.CALL,
        Node.newName("goog.partial"),
        Node.newName("fn"),
        Node.newList(Node.newList(Node.newString("arg1"))),
        Node.newList(Node.newList(Node.newString("arg2"))));

    Bind bind = new ClosureCodingConvention().describeFunctionBind(node);

    Preconditions.checkState(bind != null);
    Preconditions.checkState(bind.getFunction().getName().equals("fn"));
    Preconditions.checkState(bind.getThisValue() == null);
    Preconditions.checkState(bind.getParameters().get(0) != null);
    Preconditions.checkState(bind.getParameters().get(0).getValue().isString()
        && bind.getParameters().get(0).getValue().getStringValue().equals("arg1"));
    Preconditions.checkState(bind.getParameters().get(1) != null);
    Preconditions.checkState(bind.getParameters().get(1).getValue().isString()
        && bind.getParameters().get(1).getValue().getStringValue().equals("arg2"));
  }
}