package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.DefaultCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.ObjectLiteralCast;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.jscomp.SubclassType;

import java.util.Collection;
import java.util.List;

public class ClosureCodingConventionTest {
    @Test
    public void testApplySubclassRelationship() {
        FunctionType parentCtor = new FunctionType("ParentCtor", ImmutableList.of(),
                new ObjectType("ParentClass"), null);
        FunctionType childCtor = new FunctionType("ChildCtor", ImmutableList.of(),
                new ObjectType("ChildClass"), null);

        ClosureCodingConvention codingConvention = new ClosureCodingConvention();
        codingConvention.applySubclassRelationship(parentCtor, childCtor,
                SubclassType.INHERITS);

        ObjectType parentPrototype = parentCtor.getPrototype();
        ObjectType childPrototype = childCtor.getPrototype();

        assertNotNull(childPrototype.getDeclaredProperty("superClass_"));
        assertNotNull(childPrototype.getDeclaredProperty("constructor"));
        assertEquals(parentPrototype, childPrototype.getDeclaredProperty("superClass_"));
        assertEquals(childCtor, parentPrototype.getDeclaredProperty("constructor"));
    }

    @Test
    public void testGetclasssDefinedByCall() {
        Node node = Node.createTree(
            "goog.inherits",
            Node.createTree("MySubClass",
                Node.createTree("-",
                    Node.createTree("MySuperClass"))));

        SubclassRelationship relationship = new ClosureCodingConvention()
                .getclasssDefinedByCall(node, null);
        assertNotNull(relationship);
        assertEquals(SubclassType.INHERITS, relationship.getType());
        assertEquals("MySubClass", relationship.getSubclass());
        assertEquals("MySuperClass", relationship.getSuperclass());
    }

    @Test
    public void testExtractClassNameIfProvide() {
        Node node = Node.createTree("goog.provide", Node.createTree("MyClass"));
        String className = new ClosureCodingConvention().extractClassNameIfProvide(node, null);
        assertEquals("MyClass", className);
    }

    @Test
    public void testExtractClassNameIfRequire() {
        Node node = Node.createTree("goog.require", Node.createTree("MyClass"));
        String className = new ClosureCodingConvention().extractClassNameIfRequire(node, null);
        assertEquals("MyClass", className);
    }

    @Test
    public void testIdentifyTypeDeclarationCall() {
        Node node = Node.createTree("goog.addDependency",
            Node.createTree("MyClass", Node.createTree("[",
                Node.createTree("MyType1"),
                Node.createTree(","),
                Node.createTree("MyType2"),
                Node.createTree("]"))));

        List<String> typeNames = new ClosureCodingConvention().identifyTypeDeclarationCall(node);
        assertEquals(2, typeNames.size());
        assertTrue(typeNames.contains("MyType1"));
        assertTrue(typeNames.contains("MyType2"));
    }

    @Test
    public void testGetExportPropertyFunction() {
        String functionName = new ClosureCodingConvention().getExportPropertyFunction();
        assertEquals("goog.exportProperty", functionName);
    }

    @Test
    public void testGetExportSymbolFunction() {
        String functionName = new ClosureCodingConvention().getExportSymbolFunction();
        assertEquals("goog.exportSymbol", functionName);
    }

    @Test
    public void testPropertyTestFunctions() {
        Collection<AssertionFunctionSpec> assertionFunctions =
                new ClosureCodingConvention().getAssertionFunctions();

        assertEquals(7, assertionFunctions.size());
        assertTrue(assertionFunctions.contains(
                new AssertionFunctionSpec("goog.isDef")));
        assertTrue(assertionFunctions.contains(
                new AssertionFunctionSpec("goog.isNull")));
        assertTrue(assertionFunctions.contains(
                new AssertionFunctionSpec("goog.isDefAndNotNull")));
        assertTrue(assertionFunctions.contains(
                new AssertionFunctionSpec("goog.isString")));
        assertTrue(assertionFunctions.contains(
                new AssertionFunctionSpec("goog.isNumber")));
        assertTrue(assertionFunctions.contains(
                new AssertionFunctionSpec("goog.isBoolean")));
        assertTrue(assertionFunctions.contains(
                new AssertionFunctionSpec("goog.isFunction")));
    }

    @Test
    public void testObjectLiteralCast() {
        Node callNode = Node.createTree("goog.reflect.object",
            Node.createTree("goog.object.ObjectType"));
        ObjectLiteralCast cast = new ClosureCodingConvention()
                .getObjectLiteralCast(NodeTraversal.emptyTraversal(), callNode);
        assertNotNull(cast);
        assertEquals("goog.object.ObjectType", cast.getType());
        assertEquals("[object Object]", cast.getCastedType());
    }

    @Test
    public void testIsOptionalParameter() {
        Node parameter = Node.createTree("param", new ObjectType("String"));
        assertFalse(new ClosureCodingConvention().isOptionalParameter(parameter));
    }

    @Test
    public void testIsVarArgsParameter() {
        Node parameter = Node.createTree("param", new ObjectType("Array"),
                Node.createTree("..."));
        assertFalse(new ClosureCodingConvention().isVarArgsParameter(parameter));
    }

    @Test
    public void testIsPrivate() {
        assertFalse(new ClosureCodingConvention().isPrivate("name"));
    }
}