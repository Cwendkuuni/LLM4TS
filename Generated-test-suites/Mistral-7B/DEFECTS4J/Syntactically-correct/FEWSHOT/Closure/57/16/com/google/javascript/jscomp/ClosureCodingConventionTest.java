package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

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
        FunctionType parentCtor = new FunctionType("ParentCtor", ObjectType.NULL, ImmutableList.of());
        FunctionType childCtor = new FunctionType("ChildCtor", ObjectType.NULL, ImmutableList.of());
        SubclassType type = SubclassType.INHERITS;
        ClosureCodingConvention convention = new ClosureCodingConvention();
        convention.applySubclassRelationship(parentCtor, childCtor, type);
        FunctionType childPrototype = childCtor.getPrototype();
        ObjectType childConstructor = childPrototype.getPrototype();
        assertThat(childPrototype.hasProperty("superClass_")).isTrue();
        assertThat(childPrototype.getPrototype().hasProperty("constructor")).isTrue();
        assertThat(childPrototype.getProperty("superClass_").getValueType()).isEqualTo(parentCtor.getType());
        assertThat(childConstructor.getProperty("constructor").getValueType()).isEqualTo(childCtor);
    }

    @Test
    public void testGetclasssDefinedByCall() {
        Node node = Node.createCall("goog.inherits",
            Node.createUnscopedName("SubClass"),
            Node.createUnscopedName("SuperClass"));
        ClosureCodingConvention convention = new ClosureCodingConvention();
        SubclassRelationship relationship = convention.getclasssDefinedByCall(node);
        assertThat(relationship).isNotNull();
        assertThat(relationship.getSubclassName()).isEqualTo("SubClass");
        assertThat(relationship.getSuperclassName()).isEqualTo("SuperClass");
    }

    @Test
    public void testGetclasssDefinedByCall_Null() {
        Node node = Node.createCall(Token.ABSTRACT, null, null);
        ClosureCodingConvention convention = new ClosureCodingConvention();
        SubclassRelationship relationship = convention.getclasssDefinedByCall(node);
        assertNull(relationship);
    }

    @Test
    public void testGetclasssDefinedByCall_Mixin() {
        Node node = Node.createCall("goog.mixin",
            Node.createUnscopedName("SubClassPrototype"),
            Node.createUnscopedName("SuperClassPrototype"));
        ClosureCodingConvention convention = new ClosureCodingConvention();
        SubclassRelationship relationship = convention.getclasssDefinedByCall(node);
        assertThat(relationship).isNull();
    }

    @Test
    public void testExtractClassNameIfProvide() {
        Node node = Node.createCall("goog.provide", Node.createString("X"));
        ClosureCodingConvention convention = new ClosureCodingConvention();
        String className = convention.extractClassNameIfProvide(node, null);
        assertThat(className).isEqualTo("X");
    }

    @Test
    public void testExtractClassNameIfRequire() {
        Node node = Node.createCall("goog.require", Node.createString("X"));
        ClosureCodingConvention convention = new ClosureCodingConvention();
        String className = convention.extractClassNameIfRequire(node, null);
        assertThat(className).isEqualTo("X");
    }

    @Test
    public void testExtractClassNameIfGoog_Null() {
        Node node = Node.createCall(Token.ABSTRACT, null, null);
        ClosureCodingConvention convention = new ClosureCodingConvention();
        String className = convention.extractClassNameIfGoog(node, null, "goog.require");
        assertNull(className);
    }

    @Test
    public void testExtractClassNameIfGoog() {
        Node node = Node.createCall("goog.require",
            Node.createUnscopedName("goog.foo"));
        ClosureCodingConvention convention = new ClosureCodingConvention();
        String className = convention.extractClassNameIfGoog(node, null, "goog.require");
        assertThat(className).isEqualTo("goog.foo");
    }

    @Test
    public void testIsSuperClassReference() {
        ClosureCodingConvention convention = new ClosureCodingConvention();
        boolean isSuperClassReference = convention.isSuperClassReference("superClass_");
        assertThat(isSuperClassReference).isTrue();
    }

    @Test
    public void testEndsWithPrototype() {
        Node node = Node.createQualifiedName("a.b.c.prototype");
        ClosureCodingConvention convention = new ClosureCodingConvention();
        boolean endsWithPrototype = convention.endsWithPrototype(node);
        assertThat(endsWithPrototype).isTrue();
    }

    @Test
    public void testEndsWithPrototype_NoPrototype() {
        Node node = Node.createQualifiedName("a.b.c");
        ClosureCodingConvention convention = new ClosureCodingConvention();
        boolean endsWithPrototype = convention.endsWithPrototype(node);
        assertThat(endsWithPrototype).isFalse();
    }

    @Test
    public void testGetExportPropertyFunction() {
        ClosureCodingConvention convention = new ClosureCodingConvention();
        String exportPropertyFunction = convention.getExportPropertyFunction();
        assertThat(exportPropertyFunction).isEqualTo("goog.exportProperty");
    }

    @Test
    public void testGetSingletonGetterClassName() {
        Node node = Node.createCall("goog.addSingletonGetter",
            Node.createUnscopedName("getX"));
        ClosureCodingConvention convention = new ClosureCodingConvention();
        String singletonGetterClassName = convention.getSingletonGetterClassName(node);
        assertThat(singletonGetterClassName).isNotNull();
        assertThat(singletonGetterClassName).isEqualTo("X");
    }

    @Test
    public void testApplySingletonGetter() {
        FunctionType getterType = new FunctionType("getter", ObjectType.NULL, ImmutableList.of());
        ObjectType objectType = new ObjectType("X", ImmutableList.of());
        FunctionType functionType = new FunctionType("X", ImmutableList.of(), ImmutableList.of());
        ClosureCodingConvention convention = new ClosureCodingConvention();
        convention.applySingletonGetter(functionType, getterType, objectType);
        FunctionType getInstance = functionType.getProperty("getInstance");
        ObjectType instance = getInstance.getReturnType();
        ObjectType instance_ = functionType.getProperty("instance_");
        assertThat(getInstance.isStatic()).isFalse();
        assertThat(getInstance.getParameters().size()).isEqualTo(0);
        assertThat(getInstance.getPrototype()).isEqualTo(instance_);
        assertThat(instance_.getDeclaredProperties().size()).isEqualTo(1);
        assertThat(instance_.getDeclaredProperties().get(0).getName()).isEqualTo("instance_");
        assertThat(instance_.getDeclaredProperties().get(0).getValueType()).isEqualTo(objectType);
    }

    @Test
    public void testIsPropertyTestFunction() {
        Node node = Node.createCall("goog.isDef", Node.createNull());
        ClosureCodingConvention convention = new ClosureCodingConvention();
        boolean isPropertyTestFunction = convention.isPropertyTestFunction(node);
        assertThat(isPropertyTestFunction).isTrue();
    }

    @Test
    public void testIsPropertyTestFunction_NotFunctionCall() {
        Node node = Node.createString("goog.isDef");
        ClosureCodingConvention convention = new ClosureCodingConvention();
        boolean isPropertyTestFunction = convention.isPropertyTestFunction(node);
        assertThat(isPropertyTestFunction).isFalse();
    }

    @Test
    public void testGetObjectLiteralCast() {
        Node node = Node.createCall("goog.reflect.object.getObjectLiteralCast",
            Node.createUnscopedName("goog.foo"));
        ClosureCodingConvention convention = new ClosureCodingConvention();
        ObjectLiteralCast objectLiteralCast = convention.getObjectLiteralCast(null, node);
        assertThat(objectLiteralCast).isNotNull();
        assertThat(objectLiteralCast.getTypeName()).isEqualTo("goog.foo");
        assertThat(objectLiteralCast.getObjectLiteral()).isNotNull();
    }

    @Test
    public void testGetObjectLiteralCast_Null() {
        Node node = null;
        ClosureCodingConvention convention = new ClosureCodingConvention();
        ObjectLiteralCast objectLiteralCast = convention.getObjectLiteralCast(null, node);
        assertNull(objectLiteralCast);
    }

    @Test
    public void testIsOptionalParameter() {
        ClosureCodingConvention convention = new ClosureCodingConvention();
        boolean isOptionalParameter = convention.isOptionalParameter(Node.createName("param"));
        assertThat(isOptionalParameter).isFalse();
    }

    @Test
    public void testIsVarArgsParameter() {
        ClosureCodingConvention convention = new ClosureCodingConvention();
        boolean isVarArgsParameter = convention.isVarArgsParameter(Node.createName("params"));
        assertThat(isVarArgsParameter).isFalse();
    }

    @Test
    public void testIsPrivate() {
        ClosureCodingConvention convention = new ClosureCodingConvention();
        boolean isPrivate = convention.isPrivate("privateVariable");
        assertThat(isPrivate).isFalse();
    }

    @Test
    public void testGetAssertionFunctions() {
        ClosureCodingConvention convention = new ClosureCodingConvention();
        List<AssertionFunctionSpec> assertionFunctions = convention.getAssertionFunctions();
        assertThat(assertionFunctions).hasSize(8);
    }

    @Test
    public void testDescribeFunctionBind() {
        Node node = Node.createCall("goog.bind",
            Node.createUnscopedName("fn"),
            Node.createNull(),
            Node.createArrayLiteral(Node.createNull()));
        ClosureCodingConvention convention = new ClosureCodingConvention();
        Bind bind = convention.describeFunctionBind(node);
        assertThat(bind).isNotNull();
        assertThat(bind.getFunction()).isNotNull();
        assertThat(bind.getThisValue()).isNull();
        assertThat(bind.getArguments()).hasSize(0);
    }
}