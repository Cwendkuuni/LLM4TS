package com.google.javascript.jscomp.test.unit;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.DefaultCodingConvention;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ObjectType;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    CodingConvention convention = mock(CodingConvention.class);
    FunctionType parentCtor = mock(FunctionType.class);
    FunctionType childCtor = mock(FunctionType.class);
    SubclassType type = SubclassType.INHERITS;

    when(parentCtor.getPrototype()).thenReturn(new ObjectType("parent"));
    when(parentCtor.getSource()).thenReturn("parentSource");
    when(childCtor.getPrototype()).thenReturn(new ObjectType("child"));
    when(childCtor.getSource()).thenReturn("childSource");

    ClosureCodingConvention closureCodingConvention = new ClosureCodingConvention();
    closureCodingConvention.applySubclassRelationship(parentCtor, childCtor, type);

    FunctionType expectedParentPrototype = new ObjectType("parent");
    FunctionType expectedChildPrototype = new ObjectType("child");
    FunctionType expectedChild = new FunctionType("child", ImmutableList.of(),
        new ObjectType("child"));

    assertThat(childCtor.getPrototype().getProperties())
        .containsProperty("superClass_", Mockito.eq(parentCtor));
    assertThat(childCtor.getPrototype().getProperties())
        .containsProperty("constructor", Mockito.eq(childCtor));
    assertThat(childCtor.getProperties())
        .containsProperty("superClass_", Mockito.eq(parentCtor));
    assertThat(childCtor.getProperties())
        .containsProperty("constructor", Mockito.eq(childCtor));
    assertThat(parentCtor.getProperties())
        .doesNotContainKey("subClasses");
    assertThat(childCtor.getSource())
        .isEqualTo("childSource");
    assertThat(parentCtor.getSource())
        .isEqualTo("parentSource");
  }

  @Test
  public void testGetclasssDefinedByCall_Inherits() {
    Node node = Node.newNode(Token.CALL, new ObjectType("parent"),
        Node.newNode(Token.GETPROP, new ObjectType("child"),
            Node.newNode(Token.IDENT, "inherits")));

    ClosureCodingConvention closureCodingConvention = new ClosureCodingConvention();
    SubclassRelationship result = closureCodingConvention.getclasssDefinedByCall(node);
    assertThat(result).isNotNull();
    assertThat(result.getType()).isEqualTo(SubclassType.INHERITS);
    assertThat(result.getSubclass()).isNotNull();
    assertThat(result.getSuperclass()).isNotNull();
  }

  @Test
  public void testGetclasssDefinedByCall_Mixin() {
    Node node = Node.newNode(Token.CALL, new ObjectType("parent"),
        Node.newNode(Token.GETPROP, new ObjectType("child"),
            Node.newNode(Token.IDENT, "mixin")));

    ClosureCodingConvention closureCodingConvention = new ClosureCodingConvention();
    SubclassRelationship result = closureCodingConvention.getclasssDefinedByCall(node);
    assertThat(result).isNull();
  }

  @Test
  public void testIsSuperClassReference() {
    ClosureCodingConvention closureCodingConvention = new ClosureCodingConvention();
    boolean result = closureCodingConvention.isSuperClassReference("superClass_");
    assertThat(result).isTrue();
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = Node.newNode(Token.CALL, new ObjectType("goog"),
        Node.newNode(Token.IDENT, "provide"),
        Node.newNode(Token.ARRAYLIT, Node.newNode(Token.STRING, "X")));

    ClosureCodingConvention closureCodingConvention = new ClosureCodingConvention();
    String result = closureCodingConvention.extractClassNameIfProvide(node, null);
    assertThat(result).isEqualTo("X");
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = Node.newNode(Token.CALL, new ObjectType("goog"),
        Node.newNode(Token.IDENT, "require"),
        Node.newNode(Token.ARRAYLIT, Node.newNode(Token.STRING, "X")));

    ClosureCodingConvention closureCodingConvention = new ClosureCodingConvention();
    String result = closureCodingConvention.extractClassNameIfRequire(node, null);
    assertThat(result).isEqualTo("X");
  }

  @Test
  public void testGetExportPropertyFunction() {
    ClosureCodingConvention closureCodingConvention = new ClosureCodingConvention();
    String result = closureCodingConvention.getExportPropertyFunction();
    assertThat(result).isEqualTo("goog.exportProperty");
  }

  @Test
  public void testGetExportSymbolFunction() {
    ClosureCodingConvention closureCodingConvention = new ClosureCodingConvention();
    String result = closureCodingConvention.getExportSymbolFunction();
    assertThat(result).isEqualTo("goog.exportSymbol");
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = Node.newNode(Token.CALL, new ObjectType("goog"),
        Node.newNode(Token.IDENT, "addDependency"),
        new ObjectType("Array"),
        new Node.newArrayLit(Node.newNode(Token.STRING, "X")));

    ClosureCodingConvention closureCodingConvention = new ClosureCodingConvention();
    List<String> result = closureCodingConvention.identifyTypeDeclarationCall(node);
    assertThat(result).containsExactly("X");
  }

  @Test
  public void testGetAbstractMethodName() {
    ClosureCodingConvention closureCodingConvention = new ClosureCodingConvention();
    String result = closureCodingConvention.getAbstractMethodName();
    assertThat(result).isEqualTo("goog.abstractMethod");
  }

  @Test
  public void testGetSingletonGetterClassName() {
    Node node = Node.newNode(Token.CALL, new ObjectType("goog"),
        Node.newNode(Token.IDENT, "addSingletonGetter"),
        Node.newNode(Token.IDENT, "MyClass"));

    ClosureCodingConvention closureCodingConvention = new ClosureCodingConvention();
    String result = closureCodingConvention.getSingletonGetterClassName(node);
    assertThat(result).isEqualTo("MyClass");
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType functionType = mock(FunctionType.class);
    FunctionType getterType = mock(FunctionType.class);
    ObjectType objectType = mock(ObjectType.class);

    when(objectType.getProperties())
        .thenReturn(ImmutableMap.of("getInstance", getterType));

    ClosureCodingConvention closureCodingConvention = new ClosureCodingConvention();
    closureCodingConvention.applySingletonGetter(functionType, getterType, objectType);

    FunctionType expectedFunctionType = new FunctionType("MyClass",
        ImmutableList.of(), new ObjectType("MyClass"));

    assertThat(functionType.getProperties())
        .containsProperty("getInstance", Mockito.eq(getterType));
    assertThat(functionType.getProperties())
        .containsProperty("instance_", Mockito.eq(objectType));
  }

  @Test
  public void testGetGlobalObject() {
    ClosureCodingConvention closureCodingConvention = new ClosureCodingConvention();
    String result = closureCodingConvention.getGlobalObject();
    assertThat(result).isEqualTo("goog.global");
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node node = Node.newNode(Token.CALL, new ObjectType("goog"),
        Node.newNode(Token.IDENT, "isDef"), Node.newNode(Token.NULL));

    ClosureCodingConvention closureCodingConvention = new ClosureCodingConvention();
    boolean result = closureCodingConvention.isPropertyTestFunction(node);
    assertThat(result).isTrue();
  }

  @Test
  public void testGetObjectLiteralCast() {
    Node node = Node.newNode(Token.CALL, new ObjectType("goog.reflect"),
        Node.newNode(Token.IDENT, "object"),
        Node.newNode(Token.IDENT, "MyObjectType"));

    ClosureCodingConvention closureCodingConvention = new ClosureCodingConvention();
    ObjectLiteralCast result = closureCodingConvention.getObjectLiteralCast(
        Mockito.mock(NodeTraversal.class), node);

    assertThat(result).isNotNull();
    assertThat(result.getTypeName()).isEqualTo("MyObjectType");
    assertThat(result.getNode()).isNotNull();
  }

  @Test
  public void testIsOptionalParameter() {
    Node node = Node.newNode(Token.VAR, "myParam", Node.newNode(Token.NULL));

    ClosureCodingConvention closureCodingConvention = new ClosureCodingConvention();
    boolean result = closureCodingConvention.isOptionalParameter(node);
    assertThat(result).isFalse();
  }

  @Test
  public void testIsVarArgsParameter() {
    Node node = Node.newNode(Token.VAR, "myParam", Node.newNode(Token.IDENT, "args"));

    ClosureCodingConvention closureCodingConvention = new ClosureCodingConvention();
    boolean result = closureCodingConvention.isVarArgsParameter(node);
    assertThat(result).isFalse();
  }

  @Test
  public void testIsPrivate() {
    ClosureCodingConvention closureCodingConvention = new ClosureCodingConvention();
    boolean result = closureCodingConvention.isPrivate("myVariable");
    assertThat(result).isFalse();
  }

  @Test
  public void testGetAssertionFunctions() {
    ClosureCodingConvention closureCodingConvention = new ClosureCodingConvention();
    Collection<AssertionFunctionSpec> result = closureCodingConvention.getAssertionFunctions();

    List<AssertionFunctionSpec> expected = ImmutableList.of(
        new AssertionFunctionSpec("goog.asserts.assert"),
        new AssertionFunctionSpec("goog.asserts.assertNumber", JSTypeNative.NUMBER_TYPE),
        new AssertionFunctionSpec("goog.asserts.assertString", JSTypeNative.STRING_TYPE),
        new AssertionFunctionSpec("goog.asserts.assertFunction", JSTypeNative.FUNCTION_INSTANCE_TYPE),
        new AssertionFunctionSpec("goog.asserts.assertObject", JSTypeNative.OBJECT_TYPE),
        new AssertionFunctionSpec("goog.asserts.assertArray", JSTypeNative.ARRAY_TYPE),
        new AssertionFunctionSpec("goog.asserts.assertInstanceof", JSTypeNative.OBJECT_TYPE)
    );

    assertThat(result).containsExactlyElementsIn(expected);
  }

  @Test
  public void testDescribeFunctionBind() {
    Node node = Node.newNode(Token.CALL, new ObjectType("fn"),
        Node.newNode(Token.IDENT, "bind"),
        Node.newNode(Token.THIS, new ObjectType("self")),
        Node.newNode(Token.ARRAYLIT, Node.newNode(Token.IDENT, "args")));

    ClosureCodingConvention closureCodingConvention = new ClosureCodingConvention();
    Bind result = closureCodingConvention.describeFunctionBind(node);

    assertThat(result).isNotNull();
    assertThat(result.getFunction()).isNotNull();
    assertThat(result.getThisValue()).isNotNull();
    assertThat(result.getArguments()).containsExactly("args");
  }
}

class AssertionFunctionSpec {
  private final String name;
  private final JSType type;

  AssertionFunctionSpec(String name) {
    this(name, null);
  }

  AssertionFunctionSpec(String name, JSType type) {
    this.name = name;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public JSType getType() {
    return type;
  }
}

interface SubclassType {
  SubclassType INHERITS = new SubclassType() {};
  SubclassType MIXIN = new SubclassType() {};
}