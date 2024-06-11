package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

    private SyntacticScopeCreator creator;
    private AbstractCompiler compiler;
    private Node node;
    private Scope parent;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        creator = new SyntacticScopeCreator(compiler);
        node = new Node(Token.ROOT);
        parent = new Scope(NodeUtil.createName(""), compiler);
    }

    @Test
    public void createScope_withFunctionNode_hasCorrectScope() {
        Node functionNode = new Node(Token.FUNCTION);

        Scope scope = creator.createScope(functionNode, parent);

        assertEquals(functionNode, scope.getDeclaration());
        assertFalse(scope.isGlobal());
        assertFalse(scope.isCatchScope());
        assertFalse(scope.isFunctionBlockScope());
        assertFalse(scope.isModuleScope());
        assertFalse(scope.isDeclared("arguments", false));
        assertTrue(scope.getParent() == parent);
        assertTrue(scope.getVarCount() == 0);
    }

    @Test
    public void createScope_withGlobalNode_hasCorrectScope() {
        Node globalNode = new Node(Token.SCRIPT);

        Scope scope = creator.createScope(globalNode, null);

        assertEquals(globalNode, scope.getDeclaration());
        assertTrue(scope.isGlobal());
        assertFalse(scope.isCatchScope());
        assertFalse(scope.isFunctionBlockScope());
        assertFalse(scope.isModuleScope());
        assertFalse(scope.isDeclared("arguments", false));
        assertNull(scope.getParent());
        assertTrue(scope.getVarCount() == 0);
    }

    @Test
    public void scanVars_withVarNode_declaresVariables() {
        Node varNode = new Node(Token.VAR);
        Node child1 = new Node(Token.NAME);
        child1.setString("x");
        Node child2 = new Node(Token.NAME);
        child2.setString("y");
        varNode.addChildToBack(child1);
        varNode.addChildToBack(child2);

        creator.scanVars(varNode, null);
        Scope.Var var1 = parent.getVar("x");
        Scope.Var var2 = parent.getVar("y");

        assertTrue(var1.isDeclared());
        assertEquals(child1, var1.getNode());
        assertEquals(varNode, var1.getParentNode());
        assertEquals(parent, var1.getParentScope());
        assertNull(var1.getType());
        assertEquals(compiler.getInput(null), var1.input);
        assertTrue(var2.isDeclared());
        assertEquals(child2, var2.getNode());
        assertEquals(varNode, var2.getParentNode());
        assertEquals(parent, var2.getParentScope());
        assertNull(var2.getType());
        assertEquals(compiler.getInput(null), var2.input);
    }

    @Test
    public void scanVars_withFunctionNode_declaresFunctionName() {
        Node functionNode = new Node(Token.FUNCTION);
        Node nameNode = new Node(Token.NAME);
        nameNode.setString("fn");
        functionNode.addChildToFront(nameNode);

        creator.scanVars(functionNode, null);
        Scope.Var var = parent.getVar("fn");

        assertTrue(var.isDeclared());
        assertEquals(nameNode, var.getNode());
        assertEquals(functionNode, var.getParentNode());
        assertEquals(parent, var.getParentScope());
        assertNull(var.getType());
        assertEquals(compiler.getInput(null), var.input);
    }

    @Test
    public void scanRoot_withFunctionNode_declaresFunctionName() {
        Node functionNode = new Node(Token.FUNCTION);
        Node nameNode = new Node(Token.NAME);
        nameNode.setString("fn");
        functionNode.addChildToFront(nameNode);

        creator.scanRoot(functionNode, parent);
        Scope.Var var = parent.getVar("fn");

        assertTrue(var.isDeclared());
        assertEquals(nameNode, var.getNode());
        assertEquals(functionNode, var.getParentNode());
        assertEquals(parent, var.getParentScope());
        assertNull(var.getType());
        assertEquals(compiler.getInput(null), var.input);
    }

    @Test
    public void scanVars_withCatchNode_declaresCatchVar() {
        Node catchNode = new Node(Token.CATCH);
        Node varNode = new Node(Token.NAME);
        varNode.setString("e");
        catchNode.addChildToFront(varNode);
        Node blockNode = new Node(Token.BLOCK);
        catchNode.addChildToBack(blockNode);

        creator.scanVars(catchNode, null);
        Scope.Var var = parent.getVar("e");

        assertTrue(var.isDeclared());
        assertEquals(varNode, var.getNode());
        assertEquals(catchNode, var.getParentNode());
        assertEquals(parent, var.getParentScope());
        assertNull(var.getType());
        assertEquals(compiler.getInput(null), var.input);
    }

    @Test
    public void scanVars_withControlStructureNode_scansChildren() {
        Node ifNode = new Node(Token.IF);

        creator.scanVars(ifNode, null);

        assertEquals(ifNode, node.getFirstChild());
    }

    @Test
    public void scanVars_withStatementBlockNode_scansChildren() {
        Node blockNode = new Node(Token.BLOCK);

        creator.scanVars(blockNode, null);

        assertEquals(blockNode, node.getFirstChild());
    }
}