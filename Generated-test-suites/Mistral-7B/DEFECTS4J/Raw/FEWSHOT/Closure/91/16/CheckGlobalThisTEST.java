package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {
    private AbstractCompiler compiler;
    private CheckGlobalThis checker;
    private NodeTraversal traversal;

    @org.junit.Before
    public void setup() {
        compiler = mock(AbstractCompiler.class);
        checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        traversal = NodeTraversal.create(checker, null, null);
    }

    @org.junit.Test
    public void testShouldTraverseGlobalFunctions() {
        Node globalFunction = Node.newFunction("myFunction");
        Node functionParent = Node.newBlock();
        functionParent.appendChild(globalFunction);
        assertTrue(checker.shouldTraverse(traversal, globalFunction, functionParent));
    }

    @org.junit.Test
    public void testShouldNotTraverseConstructorFunctions() {
        Node constructorFunction = Node.newFunction("MyConstructor");
        JSDocInfo jsDoc = Node.newJsDoc("constructor");
        constructorFunction.setJSDocInfo(jsDoc);
        Node constructorParent = Node.newBlock();
        constructorParent.appendChild(constructorFunction);
        assertFalse(checker.shouldTraverse(traversal, constructorFunction, constructorParent));
    }

    @org.junit.Test
    public void testShouldNotTraverseFunctionsAnnotatedWithAtthis() {
        Node atthisFunction = Node.newFunction("myFunction");
        JSDocInfo jsDoc = Node.newJsDoc("@this");
        atthisFunction.setJSDocInfo(jsDoc);
        Node functionParent = Node.newBlock();
        functionParent.appendChild(atthisFunction);
        assertFalse(checker.shouldTraverse(traversal, atthisFunction, functionParent));
    }

    @org.junit.Test
    public void testShouldNotTraverseFunctionsAnnotatedWithAtconstructor() {
        Node constructorFunction = Node.newFunction("MyConstructor");
        JSDocInfo jsDoc = Node.newJsDoc("constructor");
        constructorFunction.setJSDocInfo(jsDoc);
        Node functionParent = Node.newBlock();
        functionParent.appendChild(constructorFunction);
        assertFalse(checker.shouldTraverse(traversal, constructorFunction, functionParent));
    }

    @org.junit.Test
    public void testShouldNotTraverseFunctionsAnnotatedWithIsinterface() {
        Node interfaceFunction = Node.newFunction("MyInterface");
        JSDocInfo jsDoc = Node.newJsDoc("interface");
        interfaceFunction.setJSDocInfo(jsDoc);
        Node functionParent = Node.newBlock();
        functionParent.appendChild(interfaceFunction);
        assertFalse(checker.shouldTraverse(traversal, interfaceFunction, functionParent));
    }

    @org.junit.Test
    public void testShouldNotTraverseFunctionsAnnotatedWithIsoverride() {
        Node overrideFunction = Node.newFunction("myFunction");
        JSDocInfo jsDoc = Node.newJsDoc("override");
        overrideFunction.setJSDocInfo(jsDoc);
        Node functionParent = Node.newBlock();
        functionParent.appendChild(overrideFunction);
        assertFalse(checker.shouldTraverse(traversal, overrideFunction, functionParent));
    }

    @org.junit.Test
    public void testShouldTraverseFunctionWithNoAnnotation() {
        Node function = Node.newFunction("myFunction");
        Node functionParent = Node.newBlock();
        functionParent.appendChild(function);
        assertTrue(checker.shouldTraverse(traversal, function, functionParent));
    }

    @org.junit.Test
    public void testShouldTraverseFunctionWithProtoPropertyAssignment() {
        Node assignParent = Node.newBlock();
        Node assign = Node.newAssign();
        Node lhs = Node.newGetProp("myProto", "myProperty");
        Node rhs = Node.newThis();
        assign.setLeftHandSide(lhs);
        assign.setRightHandSide(rhs);
        assignParent.appendChild(assign);
        Node function = Node.newFunction("myFunction");
        function.setParent(assignParent);
        assertTrue(checker.shouldTraverse(traversal, function, assignParent));
    }

    @org.junit.Test
    public void testShouldNotTraverseFunctionWithProtoPropertyAccess() {
        Node getPropParent = Node.newBlock();
        Node getProp = Node.newGetProp("myProto", "myProperty");
        Node function = Node.newFunction("myFunction");
        getPropParent.appendChild(getProp);
        function.setParent(getPropParent);
        assertFalse(checker.shouldTraverse(traversal, function, getPropParent));
    }

    @org.junit.Test
    public void testShouldNotTraverseFunctionWithProtoPropertyAccessDeep() {
        Node getPropParent = Node.newBlock();
        Node getProp1 = Node.newGetProp("myProto1", "getMyProperty");
        Node getProp2 = Node.newGetProp("myProto2", "myProperty");
        Node function = Node.newFunction("myFunction");
        getProp2.setParent(getProp1);
        getProp1.setParent(getPropParent);
        function.setParent(getPropParent);
        assertFalse(checker.shouldTraverse(traversal, function, getPropParent));
    }

    @org.junit.Test
    public void testShouldReportThisOnLeftSideOfAssignment() {
        Node assignParent = Node.newBlock();
        Node assign = Node.newAssign();
        Node lhs = Node.newThis();
        Node rhs = Node.newString("foo");
        assign.setLeftHandSide(lhs);
        assign.setRightHandSide(rhs);
        assignParent.appendChild(assign);
        Node function = Node.newFunction("myFunction");
        function.setParent(assignParent);
        assertTrue(checker.shouldReportThis(assign, assignParent));
    }

    @org.junit.Test
    public void testShouldNotReportThisOnRightSideOfAssignment() {
        Node assignParent = Node.newBlock();
        Node assign = Node.newAssign();
        Node lhs = Node.newGetProp("myObj", "myProperty");
        Node rhs = Node.newThis();
        assign.setLeftHandSide(lhs);
        assign.setRightHandSide(rhs);
        assignParent.appendChild(assign);
        Node function = Node.newFunction("myFunction");
        function.setParent(assignParent);
        assertFalse(checker.shouldReportThis(rhs, assignParent));
    }

    @org.junit.Test
    public void testShouldReportThisOnPropertyAccess() {
        Node getPropParent = Node.newBlock();
        Node getProp = Node.newGetProp("myObj", "myProperty");
        Node function = Node.newFunction("myFunction");
        getPropParent.appendChild(getProp);
        function.setParent(getPropParent);
        assertTrue(checker.shouldReportThis(getProp, getPropParent));
    }

    @org.junit.Test
    public void testGetFunctionJsDocInfo() {
        Node function = Node.newFunction("myFunction");
        JSDocInfo jsDoc = checker.getFunctionJsDocInfo(function);
        assertTrue(jsDoc == null);

        Node jsDocNode = Node.newJsDoc("constructor");
        function.setJSDocInfo(jsDocNode);
        jsDoc = checker.getFunctionJsDocInfo(function);
        assertTrue(jsDoc != null);
        assertTrue(jsDoc.isConstructor());

        jsDocNode = Node.newJsDoc("interface");
        function.setJSDocInfo(jsDocNode);
        jsDoc = checker.getFunctionJsDocInfo(function);
        assertTrue(jsDoc != null);
        assertTrue(jsDoc.isInterface());

        jsDocNode = Node.newJsDoc("@this");
        function.setJSDocInfo(jsDocNode);
        jsDoc = checker.getFunctionJsDocInfo(function);
        assertTrue(jsDoc != null);
        assertTrue(jsDoc.hasThisType());

        jsDocNode = Node.newJsDoc("@override");
        function.setJSDocInfo(jsDocNode);
        jsDoc = checker.getFunctionJsDocInfo(function);
        assertTrue(jsDoc != null);
        assertTrue(jsDoc.isOverride());
    }
}