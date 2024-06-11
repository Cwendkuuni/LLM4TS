package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.SubclassType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = new FunctionType("ParentCtor", ImmutableList.of(),
        ObjectType.getType());
    FunctionType childCtor = new FunctionType("ChildCtor", ImmutableList.of(),
        ObjectType.getType());
    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(parentCtor, childCtor, SubclassType.INHERITS);

    ObjectType parentPrototype = parentCtor.getPrototype();
    ObjectType childPrototype = childCtor.getPrototype();

    assertSame(parentPrototype, childPrototype.getProperty("superClass_"));
    assertSame(childCtor, childPrototype.getProperty("constructor"));
  }

  @Test
  public void testGetclasssDefinedByCall_SubClassInherits() {
    Node callNode = Node.newCall(Token.NAME, "MySubClass",
        ImmutableList.of(Node.newName("SuperClass")));
    ClosureCodingConvention convention = new ClosureCodingConvention();
    SubclassRelationship relationship = convention.getclasssDefinedByCall(callNode, null);
    assertTrue(relationship != null);
    assertTrue(relationship.getType() == SubclassType.INHERITS);
  }

  @Test
  public void testGetclasssDefinedByCall_SubClassMixin() {
    Node callNode = Node.newCall(Token.NAME, "MySubClass",
        ImmutableList.of(Node.newName("SuperClass.prototype")));
    ClosureCodingConvention convention = new ClosureCodingConvention();
    SubclassRelationship relationship = convention.getclasssDefinedByCall(callNode, null);
    assertTrue(relationship != null);
    assertTrue(relationship.getType() == SubclassType.MIXIN);
  }

  @Test
  public void testApplySubclassRelationship_Null() {
    FunctionType parentCtor = new FunctionType("ParentCtor", ImmutableList.of(),
        ObjectType.getType());
    FunctionType childCtor = new FunctionType("ChildCtor", ImmutableList.of(),
        ObjectType.getType());
    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(parentCtor, childCtor, null);
    assertNull(childPrototype.getProperty("superClass_"));
    assertNull(childPrototype.getProperty("constructor"));
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = Node.newCall(Node.newGetProp(Node.newName("goog"),
        Token.NAME, "provide"), Node.newArray(Token.STRING, "MyClass"));
    String className = new ClosureCodingConvention().extractClassNameIfProvide(node, null);
    assertTrue(className != null);
    assertTrue(className.equals("MyClass"));
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = Node.newCall(Node.newGetProp(Node.newName("goog"),
        Token.NAME, "require"), Node.newArray(Token.STRING, "MyClass"));
    String className = new ClosureCodingConvention().extractClassNameIfRequire(node, null);
    assertTrue(className != null);
    assertTrue(className.equals("MyClass"));
  }

  @Test
  public void testGetExportPropertyFunction() {
    String exportPropertyFunction = new ClosureCodingConvention().getExportPropertyFunction();
    assertTrue(exportPropertyFunction != null);
    assertTrue(exportPropertyFunction.equals("goog.exportProperty"));
  }

  @Test
  public void testGetExportSymbolFunction() {
    String exportSymbolFunction = new ClosureCodingConvention().getExportSymbolFunction();
    assertTrue(exportSymbolFunction != null);
    assertTrue(exportSymbolFunction.equals("goog.exportSymbol"));
  }

  @Test
  public void testIsSuperClassReference() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertTrue(convention.isSuperClassReference("superClass_"));
    assertFalse(convention.isSuperClassReference("otherProperty"));
  }

  @Test
  public void testEndsWithPrototype() {
    Node qualifiedName = Node.newCall(Node.newName("a"), Token.DOT,
        Node.newName("b"), Token.DOT, Token.STRING, "prototype");
    assertTrue(new ClosureCodingConvention().endsWithPrototype(qualifiedName));
    Node nonPrototypeQualifiedName = Node.newCall(Node.newName("a"), Token.DOT,
        Node.newName("b"), Token.DOT, Token.STRING, "otherProperty");
    assertFalse(new ClosureCodingConvention().endsWithPrototype(nonPrototypeQualifiedName));
  }

  @Test
  public void testExtractClassNameIfGoog_Provide() {
    Node node = Node.newCall(Node.newGetProp(Node.newName("goog"),
        Token.NAME, "provide"), Node.newArray(Token.STRING, "MyClass"));
    String className = new ClosureCodingConvention().extractClassNameIfGoog(node, null, "goog.provide");
    assertTrue(className != null);
    assertTrue(className.equals("MyClass"));
  }

  @Test
  public void testExtractClassNameIfGoog_Require() {
    Node node = Node.newCall(Node.newGetProp(Node.newName("goog"),
        Token.NAME, "require"), Node.newArray(Token.STRING, "MyClass"));
    String className = new ClosureCodingConvention().extractClassNameIfGoog(node, null, "goog.require");
    assertTrue(className != null);
    assertTrue(className.equals("MyClass"));
  }

  @Test
  public void testExtractClassNameIfGoog_NotGoog() {
    Node node = Node.newCall(Node.newGetProp(Node.newName("someOtherNamespace"),
        Token.NAME, "someOtherFunction"), null);
    String className = new ClosureCodingConvention().extractClassNameIfGoog(node, null, "goog.provide");
    assertNull(className);
  }

  @Test
  public void testIsPropertyTestFunction() {
    Node node = Node.newCall(Node.newGetProp(Node.newName("goog"),
        Token.NAME, "isDef"), Node.newArray(Token.STRING, "somePropertyName"));
    assertTrue(new ClosureCodingConvention().isPropertyTestFunction(node));
  }

  @Test
  public void testIsPropertyTestFunction_NotPropertyTestFunction() {
    Node node = Node.newCall(Node.newName("someFunction"), null);
    assertFalse(new ClosureCodingConvention().isPropertyTestFunction(node));
  }

  @Test
  public void testGetObjectLiteralCast() {
    Node callNode = Node.newCall(Node.newGetProp(Node.newName("goog.reflect"),
        Token.NAME, "object"), Node.newArray(Token.OBJECT, Node.newObjectLit()));
    ObjectLiteralCast objectLiteralCast = new ClosureCodingConvention().getObjectLiteralCast(null, callNode);
    assertSame(ObjectType.getType(), objectLiteralCast.getType());
    assertSame(Node.newObjectLit(), objectLiteralCast.getNode());
  }

  @Test
  public void testGetObjectLiteralCast_Null() {
    assertNull(new ClosureCodingConvention().getObjectLiteralCast(null, null));
  }

  @Test
  public void testIsOptionalParameter() {
    Node node = Node.newParam(Token.IDENT, "someParameter");
    assertFalse(new ClosureCodingConvention().isOptionalParameter(node));
  }

  @Test
  public void testIsVarArgsParameter() {
    Node node = Node.newParam(Token.IDENT, "someParameter",
        Token.ELLIPSIS, Token.IDENT, "args");
    assertFalse(new ClosureCodingConvention().isVarArgsParameter(node));
  }

  @Test
  public void testIsPrivate() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertFalse(convention.isPrivate("someProperty"));
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = Node.newCall(Node.newGetProp(Node.newName("goog"),
        Token.NAME, "addDependency"), Node.newArray(Token.STRING, "MyClass"),
        Node.newArray(new ObjectType(Token.ARRAY_TYPE, JSTypeNative.TYPES)));
    List<String> typeNames = new ClosureCodingConvention().identifyTypeDeclarationCall(node);
    assertSame(ImmutableList.of("MyClass"), typeNames);
  }

  @Test
  public void testIdentifyTypeDeclarationCall_Null() {
    Node node = Node.newCall(Node.newName("someFunction"), null);
    List<String> typeNames = new ClosureCodingConvention().identifyTypeDeclarationCall(node);
    assertNull(typeNames);
  }

  @Test
  public void testGetAbstractMethodName() {
    String abstractMethodName = new ClosureCodingConvention().getAbstractMethodName();
    assertTrue(abstractMethodName != null);
    assertTrue(abstractMethodName.equals("goog.abstractMethod"));
  }

  @Test
  public void testGetSingletonGetterClassName() {
    Node callNode = Node.newCall(Node.newGetProp(Node.newName("goog.addSingletonGetter"), null),
        Node.newString("MySingletonGetterName"));
    String className = new ClosureCodingConvention().getSingletonGetterClassName(callNode, null);
    assertTrue(className != null);
    assertTrue(className.equals("MySingletonGetterName"));
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType functionType = new FunctionType("MyFunctionType",
        ImmutableList.of(), ObjectType.getType());
    ObjectType getterType = new ObjectType("MyGetterType",
        ImmutableList.of(Node.newParam(Token.IDENT, "this")));
    ObjectType objectType = new ObjectType("MyObjectType",
        ImmutableList.of());
    new ClosureCodingConvention().applySingletonGetter(functionType, getterType, objectType);

    ObjectType prototype = functionType.getPrototype();
    assertSame(getterType, prototype.getProperty("getInstance"));
    assertSame(objectType, prototype.getProperty("instance_"));
  }

  @Test
  public void testGetGlobalObject() {
    String globalObject = new ClosureCodingConvention().getGlobalObject();
    assertTrue(globalObject != null);
    assertTrue(globalObject.equals("goog.global"));
  }
}