package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.JSType;
import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

    private static final String ARGUMENTS = "arguments";
    private SyntacticScopeCreator scopeCreator;

    @Before
    public void setUp() {
        Compiler compiler = new Compiler();
        scopeCreator = new SyntacticScopeCreator(compiler);
    }

    @Test
    public void testCreateScope() {
        Node root = new Node(Token.SCRIPT);
        Scope scope = scopeCreator.createScope(root, null);
        assertNull(scope.getParent());

        Node functionNode = new Node(Token.FUNCTION);
        root.addChildToFront(functionNode);
        Scope functionScope = scopeCreator.createScope(functionNode, scope);
        assertEquals(scope, functionScope.getParent());
    }

    @Test
    public void testScanRoot_withFunction() {
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.putProp(Node.SOURCENAME_PROP, "test.js");
        Node functionNameNode = new Node(Token.NAME, "testFunction");
        Node argsNode = new Node(Token.LP);
        Node bodyNode = new Node(Token.BLOCK);
        functionNode.addChildToBack(functionNameNode);
        functionNode.addChildToBack(argsNode);
        functionNode.addChildToBack(bodyNode);

        Scope scope = new Scope(null, functionNode);
        scopeCreator.scanRoot(functionNode, scope);

        assertTrue(scope.isDeclared("testFunction", false));
        assertTrue(scope.isDeclared(ARGUMENTS, false));
    }

    @Test
    public void testScanRoot_withGlobalBlock() {
        Node globalBlockNode = new Node(Token.BLOCK);
        Scope scope = new Scope(null, globalBlockNode);
        scopeCreator.scanRoot(globalBlockNode, scope);

        assertNull(scope.getParent());
        assertNull(globalBlockNode.getFirstChild());
    }

    @Test
    public void testScanVars_withVarDeclaration() {
        Node varDeclarationNode = new Node(Token.VAR);
        Node varNode = new Node(Token.NAME, "x");
        varDeclarationNode.addChildToBack(varNode);
        Node rootNode = new Node(Token.SCRIPT);
        rootNode.addChildToBack(varDeclarationNode);

        Scope scope = new Scope(null, rootNode);
        scopeCreator.scanVars(varDeclarationNode, rootNode);

        assertTrue(scope.isDeclared("x", false));
    }

    @Test
    public void testScanVars_withFunctionDeclaration() {
        Node functionDeclarationNode = new Node(Token.FUNCTION);
        Node functionNameNode = new Node(Token.NAME, "testFunction");
        functionDeclarationNode.addChildToBack(functionNameNode);
        Node rootNode = new Node(Token.SCRIPT);
        rootNode.addChildToBack(functionDeclarationNode);

        Scope scope = new Scope(null, rootNode);
        scopeCreator.scanVars(functionDeclarationNode, rootNode);

        assertTrue(scope.isDeclared("testFunction", false));
    }

    @Test
    public void testScanVars_withCatchDeclaration() {
        Node catchDeclarationNode = new Node(Token.CATCH);
        Node catchVarNode = new Node(Token.NAME, "e");
        catchDeclarationNode.addChildToBack(catchVarNode);
        Node blockNode = new Node(Token.BLOCK);
        catchDeclarationNode.addChildToBack(blockNode);
        Node rootNode = new Node(Token.SCRIPT);
        rootNode.addChildToBack(catchDeclarationNode);

        Scope scope = new Scope(null, rootNode);
        scopeCreator.scanVars(catchDeclarationNode, rootNode);

        assertTrue(scope.isDeclared("e", false));
    }

    @Test
    public void testScanVars_withScriptNode() {
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.putProp(Node.SOURCENAME_PROP, "test.js");

        scopeCreator.scanVars(scriptNode, null);

        assertNull(scopeCreator.sourceName);
    }

    @Test
    public void testScanVars_withControlStructure() {
        Node ifNode = new Node(Token.IF);
        Node blockNode = new Node(Token.BLOCK);
        ifNode.addChildToBack(blockNode);

        scopeCreator.scanVars(ifNode, null);

        assertNull(scopeCreator.sourceName);
    }

    @Test
    public void testDeclareVar() {
        Node varDeclarationNode = new Node(Token.VAR);
        Node varNode = new Node(Token.NAME, "x");
        varDeclarationNode.addChildToBack(varNode);
        Node rootNode = new Node(Token.SCRIPT);
        rootNode.addChildToBack(varDeclarationNode);

        Scope scope = new Scope(null, rootNode);
        scopeCreator.declareVar("x", varNode, varDeclarationNode, null, null, varDeclarationNode);

        assertTrue(scope.isDeclared("x", false));
    }
}