package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;

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

import org.junit.Test;

import java.util.List;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    FunctionType parentCtor = new FunctionType("ParentCtor", ImmutableList.of(),
        new ObjectType("ParentType"), ImmutableList.of());
    FunctionType childCtor = new FunctionType("ChildCtor", ImmutableList.of(),
        new ObjectType("ChildType"), ImmutableList.of());
    SubclassType type = SubclassType.INHERITS;
    codingConvention.applySubclassRelationship(parentCtor, childCtor, type);
    FunctionType childPrototype = childCtor.getPrototype();
    ObjectType constructorType = childPrototype.getPrototype();
    assertThat(childPrototype.hasProperty("superClass_")).isTrue();
    assertThat(childPrototype.hasProperty("constructor")).isTrue();
    assertThat(constructorType.hasProperty("constructor")).isTrue();
    assertThat(constructorType.getProperty("constructor").getValueType())
        .isEqualTo(childCtor);
  }

  @Test
  public void testGetclasssDefinedByCall_SubClass_Inherits() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    Node node = new Node("goog.inherits(MySubClass, MySuperClass)");
    String extractedClassName = codingConvention.extractClassNameIfInherits(node, null);
    assertThat(extractedClassName).isEqualTo("MySubClass");
  }

  @Test
  public void testGetclasssDefinedByCall_SubClass_Mixin() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    Node node = new Node("MySubClass.mixin(MySuperClass.prototype)");
    String extractedClassName = codingConvention.extractClassNameIfMixin(node, null);
    assertThat(extractedClassName).isEqualTo("MySubClass");
  }

  @Test
  public void testIsSuperClassReference() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    assertThat(codingConvention.isSuperClassReference("superClass_")).isTrue();
  }

  @Test
  public void testEndsWithPrototype() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    Node qualifiedNameNode = new Node("a.b.c.prototype");
    assertThat(codingConvention.endsWithPrototype(qualifiedNameNode)).isTrue();
  }

  @Test
  public void testExtractClassNameIfProvide() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    Node node = new Node("goog.provide('MyModule')");
    String extractedClassName = codingConvention.extractClassNameIfProvide(node, null);
    assertThat(extractedClassName).isEqualTo("MyModule");
  }

  @Test
  public void testExtractClassNameIfRequire() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    Node node = new Node("goog.require('MyModule')");
    String extractedClassName = codingConvention.extractClassNameIfRequire(node, null);
    assertThat(extractedClassName).isEqualTo("MyModule");
  }

  @Test
  public void testGetExportPropertyFunction() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    String exportPropertyFunction = codingConvention.getExportPropertyFunction();
    assertThat(exportPropertyFunction).isEqualTo("goog.exportProperty");
  }

  @Test
  public void testGetExportSymbolFunction() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    String exportSymbolFunction = codingConvention.getExportSymbolFunction();
    assertThat(exportSymbolFunction).isEqualTo("goog.exportSymbol");
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    Node node = new Node("goog.addDependency('MyModule', [" +
        "  'MyDependency1', 'MyDependency2' " +
        "])");
    List<String> typeNames = codingConvention.identifyTypeDeclarationCall(node);
    assertThat(typeNames).containsExactly("MyDependency1", "MyDependency2");
  }

  @Test
  public void testGetAbstractMethodName() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    String abstractMethodName = codingConvention.getAbstractMethodName();
    assertThat(abstractMethodName).isEqualTo("goog.abstractMethod");
  }

  @Test
  public void testSingletonGetterClassName() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    Node node =
        new Node("goog.addSingletonGetter(MyClass, 'myProperty')");
    String singletonGetterClassName = codingConvention.getSingletonGetterClassName(node);
    assertThat(singletonGetterClassName).isEqualTo("MyClass");
  }

  @Test
  public void testApplySingletonGetter() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    FunctionType functionType = new FunctionType("MyClass", ImmutableList.of(),
        new ObjectType("MyClassType"), ImmutableList.of());
    FunctionType getterType = new FunctionType("MyClass_getter",
        ImmutableList.of(), new ObjectType("MyClassType"), ImmutableList.of());
    ObjectType objectType = new ObjectType("MyClassType");
    codingConvention.applySingletonGetter(functionType, getterType, objectType);
    assertThat(functionType.hasProperty("getInstance")).isTrue();
    assertThat(functionType.hasProperty("instance_")).isTrue();
  }

  @Test
  public void testGetGlobalObject() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    String globalObject = codingConvention.getGlobalObject();
    assertThat(globalObject).isEqualTo("goog.global");
  }

  @Test
  public void testIsPropertyTestFunction() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    Node callNode = new Node("goog.isDef(obj)");
    boolean isPropertyTestFunction = codingConvention.isPropertyTestFunction(callNode);
    assertThat(isPropertyTestFunction).isTrue();
  }

  @Test
  public void testObjectLiteralCast() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    NodeTraversal traversal = new NodeTraversal();
    Node callNode = new Node("goog.reflect.object(obj)");
    ObjectLiteralCast objectLiteralCast = codingConvention.getObjectLiteralCast(traversal, callNode);
    assertThat(objectLiteralCast).isNotNull();
  }

  @Test
  public void testIsOptionalParameter() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    Node parameter = new Node("param");
    boolean isOptionalParameter = codingConvention.isOptionalParameter(parameter);
    assertThat(isOptionalParameter).isFalse();
  }

  @Test
  public void testIsVarArgsParameter() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    Node parameter = new Node("params...");
    boolean isVarArgsParameter = codingConvention.isVarArgsParameter(parameter);
    assertThat(isVarArgsParameter).isFalse();
  }

  @Test
  public void testIsPrivate() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    boolean isPrivate = codingConvention.isPrivate("privateProperty");
    assertThat(isPrivate).isFalse();
  }

  @Test
  public void testGetAssertionFunctions() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    List<AssertionFunctionSpec> assertionFunctions = codingConvention.getAssertionFunctions();
    assertThat(assertionFunctions).hasSize(8);
    for (AssertionFunctionSpec assertionFunctionSpec : assertionFunctions) {
      assertThat(assertionFunctionSpec.functionName).isNotNull();
      JSTypeNative functionType = assertionFunctionSpec.functionType;
      assertThat(functionType).isNotNull();
    }
  }

  @Test(expected = AssertionError.class)
  public void testObjectLitExpected() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    Node node = new Node("goog.reflect.object(obj)");
    ObjectLiteralCast objectLiteralCast = codingConvention.getObjectLiteralCast(null, node);
  }

  @Test
  public void testDescribeFunctionBind() {
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    Node node = new Node("fn.bind(this, args...)");
    Bind bind = codingConvention.describeFunctionBind(node);
    FunctionType fn = (FunctionType) bind.function;
    Node thisValueNode = bind.thisValue;
    Node parametersNode = bind.parameters;
    assertThat(fn).isNotNull();
    assertThat(thisValueNode).isNotNull();
    assertThat(parametersNode).isNotNull();
  }
}

class AssertionFunctionSpec {
  String functionName;
  JSTypeNative functionType;

  AssertionFunctionSpec(String functionName, JSTypeNative functionType) {
    this.functionName = functionName;
    this.functionType = functionType;
  }
}