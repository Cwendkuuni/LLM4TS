package com.example;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.JSTypeNative;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.jscomp.SubclassType;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class ClosureCodingConventionTest {

  private ClosureCodingConvention codingConvention;

  @Before
  public void setUp() {
    codingConvention = mock(ClosureCodingConvention.class);
  }

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = mock(FunctionType.class);
    FunctionType childCtor = mock(FunctionType.class);
    SubclassType type = SubclassType.INHERITS;

    when(parentCtor.getPrototype()).thenReturn(new ObjectType("Prototype"));
    when(parentCtor.getSource()).thenReturn(new Node("ParentSource"));

    when(childCtor.getPrototype()).thenReturn(new ObjectType("ChildPrototype"));
    when(childCtor.getSource()).thenReturn(new Node("ChildSource"));

    codingConvention.applySubclassRelationship(parentCtor, childCtor, type);

    verify(childCtor).defineDeclaredProperty("superClass_", eq(parentCtor),
        eq(new Node("ParentSource")));
    verify(childCtor.getPrototype()).defineDeclaredProperty("constructor",
        eq(childCtor), eq(new Node("ChildSource")));
  }

  @Test
  public void testgetTypeClassDefiningName() {
    Node callNode = new Node("goog.inherits");
    SubclassType expectedType = SubclassType.INHERITS;

    assertEquals(expectedType, codingConvention.typeofClassDefiningName(callNode));

    callNode.setFirstChild(new Node("goog$"));
    assertEquals(expectedType, codingConvention.typeofClassDefiningName(callNode));

    callNode.setFirstChild(new Node("goog.mixin"));
    assertEquals(SubclassType.MIXIN, codingConvention.typeofClassDefiningName(callNode));
  }

  @Test
  public void testIsSuperClassReference() {
    assertEquals(true, codingConvention.isSuperClassReference("superClass_"));
  }

  @Test
  public void testEndsWithPrototype() {
    Node qualifiedName = new Node("a.b.c.prototype");
    assertTrue(codingConvention.endsWithPrototype(qualifiedName));

    qualifiedName.setFirstChild(qualifiedName.getFirstChild().getNext());
    assertFalse(codingConvention.endsWithPrototype(qualifiedName));
  }

  @Test
  public void extractClassNameIfProvideAndRequire() {
    Node node = new Node("goog.provide('Foo')");
    String expectedClassName = "Foo";
    assertEquals(expectedClassName, codingConvention.extractClassNameIfProvide(node, null));

    node.setQualifiedName("goog.require('Foo')");
    assertEquals(expectedClassName, codingConvention.extractClassNameIfRequire(node, null));
  }

  @Test
  public void testGetExportPropertyFunctionAndGetExportSymbolFunction() {
    assertEquals("goog.exportProperty",
        codingConvention.getExportPropertyFunction());
    assertEquals("goog.exportSymbol", codingConvention.getExportSymbolFunction());
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = new Node("goog.addDependency('Foo', ['Bar', 'Baz'])");
    List<String> expectedTypeNames = ImmutableList.of("Bar", "Baz");
    assertEquals(expectedTypeNames, codingConvention.identifyTypeDeclarationCall(node));
  }

  @Test
  public void testGetAbstractMethodName() {
    assertEquals("goog.abstractMethod", codingConvention.getAbstractMethodName());
  }

  @Test
  public void testGetSingletonGetterClassName() {
    Node callNode = new Node("goog.addSingletonGetter(Foo, 'getBar')");
    String expectedClassName = "Foo";
    assertEquals(expectedClassName, codingConvention.getSingletonGetterClassName(callNode));
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType functionType = mock(FunctionType.class);
    FunctionType getterType = mock(FunctionType.class);
    ObjectType objectType = mock(ObjectType.class);

    when(getterType.getReturnType()).thenReturn(JSTypeNative.FUNCTION_INSTANCE_TYPE);

    codingConvention.applySingletonGetter(functionType, getterType, objectType);

    verify(functionType).defineDeclaredProperty("getInstance", getterType,
        anyNode());
    verify(functionType).defineDeclaredProperty("instance_", eq(objectType),
        anyNode());
  }

  @Test
  public void testGetGlobalObject() {
    assertEquals("goog.global", codingConvention.getGlobalObject());
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node callNode = new Node("goog.isDef(foo)");
    assertTrue(codingConvention.isPropertyTestFunction(callNode));
  }

  @Test
  public void testGetObjectLiteralCast() {
    Node callNode = new Node("goog.reflect.object(foo)");
    ObjectType expectedType = new ObjectType("ObjectType");
    Node expectedObjectLiteral = new Node("{prop: 'val'}");

    codingConvention.getObjectLiteralCast(mock(NodeTraversal.class), callNode);

    Node castTypeNode = callNode.getNext();
    assertEquals("ObjectType", castTypeNode.getString());

    Node castObjectNode = callNode.getNext().getNext();
    assertEquals("ObjectType", castObjectNode.getType().getName());
    assertEquals("{prop: 'val'}", castObjectNode.toString());
  }

  @Test
  public void testIsOptionalParameter() {
    Node node = new Node("function foo(x = 0) {}");
    assertFalse(codingConvention.isOptionalParameter(node.getFirstChild()));
  }

  @Test
  public void testIsVarArgsParameter() {
    Node node = new Node("function foo(x, ...args) {}");
    assertTrue(codingConvention.isVarArgsParameter(node.getLastChild()));
  }

  @Test
  public void testIsPrivate() {
    assertFalse(codingConvention.isPrivate("foo"));
  }

  @Test
  public void testGetAssertionFunctions() {
    List<AssertionFunctionSpec> expectedAssertionFunctions = ImmutableList.of(
        new AssertionFunctionSpec("goog.asserts.assert"),
        new AssertionFunctionSpec("goog.asserts.assertNumber",
            JSTypeNative.NUMBER_TYPE),
        new AssertionFunctionSpec("goog.asserts.assertString",
            JSTypeNative.STRING_TYPE),
        new AssertionFunctionSpec("goog.asserts.assertFunction",
            JSTypeNative.FUNCTION_INSTANCE_TYPE),
        new AssertionFunctionSpec("goog.asserts.assertObject",
            JSTypeNative.OBJECT_TYPE),
        new AssertionFunctionSpec("goog.asserts.assertArray",
            JSTypeNative.ARRAY_TYPE),
        new AssertionFunctionSpec("goog.asserts.assertInstanceof",
            JSTypeNative.OBJECT_TYPE)
    );

    assertEquals(expectedAssertionFunctions, codingConvention.getAssertionFunctions());
  }

  @Test
  public void testDescribeFunctionBind() {
    Node fnNode = new Node("function foo() {}");
    Node bindNode = new Node("goog.bind(foo, this)");

    assertEquals(fnNode, codingConvention.describeFunctionBind(bindNode).getFunction());
    assertEquals(this, codingConvention.describeFunctionBind(bindNode).getThis());
    assertEquals(ImmutableList.of(), codingConvention.describeFunctionBind(bindNode).getParameters());
  }

  @Test
  public void testDescribeFunctionBindPartial() {
    Node fnNode = new Node("function foo(x, y) {}");
    Node partialNode = new Node("goog.partial(foo, 'arg1')");

    assertEquals(fnNode, codingConvention.describeFunctionBind(partialNode).getFunction());
    assertEquals(ImmutableList.of("arg1"), codingConvention.describeFunctionBind(partialNode).getArguments());
  }
}

class AssertionFunctionSpec {
  private final String name;
  private final JSTypeNative type;

  public AssertionFunctionSpec(String name) {
    this.name = name;
    this.type = null;
  }

  public AssertionFunctionSpec(String name, JSTypeNative type) {
    this.name = name;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public JSTypeNative getType() {
    return type;
  }
}