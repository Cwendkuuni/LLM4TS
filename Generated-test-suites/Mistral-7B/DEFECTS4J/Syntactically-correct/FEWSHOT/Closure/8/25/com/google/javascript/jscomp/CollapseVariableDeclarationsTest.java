package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.test.TestUtils.createNode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Folder;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Set;

public class CollapseVariableDeclarationsTest {
    private static Node createVariableDeclaration(String name) {
        Node varNode = new Node(Token.VAR);
        varNode.setName(name);
        Node assignNode = new Node(Token.ASSIGN);
        Node valueNode = new Node(Token.NULL);
        assignNode.addChild(varNode);
        assignNode.addChild(valueNode);
        return assignNode;
    }

    @org.junit.Test
    public void testCollapseVariableDeclarations() {
        Folder folder = new Folder();
        AbstractCompiler compiler = new Compiler(null, null, null, null, folder);
        CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

        Node root = createNode(Token.PROGRAM);

        Node varA = createVariableDeclaration("a");
        Node varB = createVariableDeclaration("b");
        Node varC = createVariableDeclaration("c");

        root.addChild(varA);
        root.addChild(varB);
        root.addChild(varC);

        pass.process(null, root);

        Node expected = new Node(Token.VAR);
        expected.setName("a");
        expected.addChild(new Node(Token.ASSIGN));
        expected.addChild(new Node(Token.VAR));
        expected.getLastChild().setName("b");
        expected.addChild(new Node(Token.ASSIGN));
        expected.getLastChild().addChild(new Node(Token.VAR));
        expected.getLastChild().getLastChild().setName("c");

        assertEquals(expected.toStringTree(true), root.toStringTree(true));
    }

    @org.junit.Test
    public void testCollapseVariableAssignments() {
        Folder folder = new Folder();
        AbstractCompiler compiler = new Compiler(null, null, null, null, folder);
        CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

        Node root = createNode(Token.PROGRAM);

        Node varA = new Node(Token.ASSIGN);
        Node lhsA = new Node(Token.NAME, "a");
        Node rhsA = new Node(Token.BOOL, true);
        varA.addChild(lhsA);
        varA.addChild(rhsA);

        Node varB = new Node(Token.ASSIGN);
        Node lhsB = new Node(Token.NAME, "a");
        Node rhsB = new Node(Token.BOOL, true);
        varB.addChild(lhsB);
        varB.addChild(rhsB);

        Node varC = new Node(Token.VAR);
        varC.setName("c");

        root.addChild(varA);
        root.addChild(varB);
        root.addChild(varC);

        pass.process(null, root);

        Node expected = new Node(Token.VAR);
        expected.setName("a");
        expected.addChild(new Node(Token.ASSIGN));
        expected.getLastChild().addChild(new Node(Token.ASSIGN));
        expected.getLastChild().getLastChild().setName("c");

        assertEquals(expected.toStringTree(true), root.toStringTree(true));
    }

    @org.junit.Test
    public void testEmptyCollapse() {
        Folder folder = new Folder();
        AbstractCompiler compiler = new Compiler(null, null, null, null, folder);
        CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

        Node root = createNode(Token.PROGRAM);

        pass.process(null, root);

        assertEquals("", root.toStringTree(true));
    }

    @org.junit.Test
    public void testNullParent() {
        Folder folder = new Folder();
        AbstractCompiler compiler = new Compiler(null, null, null, null, folder);
        CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

        Node root = createNode(Token.PROGRAM);

        Node varA = createVariableDeclaration("a");
        Node varB = createVariableDeclaration("b");

        root.addChild(varA);
        root.addChild(null); // null parent
        root.addChild(varB);

        try {
            pass.process(null, root);
            fail("Expected CompilerException");
        } catch (CompilerException e) {
            // Expected
        }

        assertNull(compiler.getDiagnostics().getErrorCount());
    }

    @org.junit.Test
    public void testCollapseFunctionDeclarations() {
        Folder folder = new Folder();
        AbstractCompiler compiler = new Compiler(null, null, null, null, folder);
        CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

        Node root = createNode(Token.FUNCTION);
        root.setName("test");
        root.addChild(new Node(Token.FUNCTION, "arguments", Lists.newArrayList(root)));

        Node varA = new Node(Token.VAR);
        varA.setName("a");
        root.addChild(varA);

        Node varB = new Node(Token.VAR);
        varB.setName("b");
        root.addChild(varB);

        pass.process(null, root);

        Node expected = new Node(Token.VAR);
        expected.setName("a");
        expected.addChild(new Node(Token.VAR));
        expected.getLastChild().setName("b");

        assertEquals(expected.toStringTree(true), root.toStringTree(true));
    }

    @org.junit.Test
    public void testCollapseVariableDeclarationWithJSDocComment() {
        Folder folder = new Folder();
        AbstractCompiler compiler = new Compiler(null, null, null, null, folder);
        CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);

        Node root = createNode(Token.PROGRAM);

        Node varA = new Node(Token.VAR);
        varA.setName("a");
        varA.addJSDocInfo(new JSDocInfo());
        varA.getJSDocInfo().put("name", "foo");
        root.addChild(varA);

        Node varB = new Node(Token.VAR);
        varB.setName("b");
        varB.addJSDocInfo(new JSDocInfo());
        varB.getJSDocInfo().put("name", "bar");
        root.addChild(varB);

        pass.process(null, root);

        Node expected = new Node(Token.VAR);
        expected.setName("a");
        expected.addJSDocInfo(new JSDocInfo());
        expected.getJSDocInfo().put("name", "foo");
        expected.addChild(new Node(Token.VAR));
        expected.getLastChild().setName("b");
        expected.getLastChild().addJSDocInfo(new JSDocInfo());
        expected.getLastChild().getJSDocInfo().put("name", "bar");

        assertEquals(expected.toStringTree(true), root.toStringTree(true));
    }
}