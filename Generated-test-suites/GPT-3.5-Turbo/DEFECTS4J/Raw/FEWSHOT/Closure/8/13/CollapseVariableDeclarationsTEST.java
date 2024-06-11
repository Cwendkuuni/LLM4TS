package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CollapseVariableDeclarationsTest {

    private CollapseVariableDeclarations cVD;

    @Before
    public void setUp() throws Exception {
        cVD = new CollapseVariableDeclarations();
    }

    @Test
    public void testProcess_noCollapsesApplied() {
        Node externs = null;
        Node root = new Node(Token.ROOT);
        cVD.process(externs, root);
        Node expectedRoot = new Node(Token.ROOT);
        assertEquals(expectedRoot, root);
    }

    @Test
    public void testProcess_varAndAssignCollapseApplied() {
        Node externs = null;

        Node firstVarNode = new Node(Token.VAR);
        Node firstChild1 = new Node(Token.NAME);
        firstChild1.setString("a");
        Node firstChild2 = new Node(Token.NAME);
        firstChild2.setString("b");
        firstVarNode.addChildToBack(firstChild1);
        firstVarNode.addChildToBack(firstChild2);

        Node assignNode1 = new Node(Token.EXPR_RESULT);
        Node assign1 = new Node(Token.ASSIGN);
        Node lhs1 = new Node(Token.NAME);
        lhs1.setString("c");
        Node rhs1 = new Node(Token.TRUE);
        assign1.addChildToBack(lhs1);
        assign1.addChildToBack(rhs1);
        assignNode1.addChildToBack(assign1);

        Node assignNode2 = new Node(Token.EXPR_RESULT);
        Node assign2 = new Node(Token.ASSIGN);
        Node lhs2 = new Node(Token.NAME);
        lhs2.setString("d");
        Node rhs2 = new Node(Token.GETPROP);
        Node propAccess1 = new Node(Token.NAME);
        propAccess1.setString("e");
        Node propAccess2 = new Node(Token.STRING);
        propAccess2.setString("f");
        rhs2.addChildToBack(propAccess1);
        rhs2.addChildToBack(propAccess2);
        assign2.addChildToBack(lhs2);
        assign2.addChildToBack(rhs2);
        assignNode2.addChildToBack(assign2);

        Node root = new Node(Token.ROOT);
        root.addChildToBack(firstVarNode);
        root.addChildToBack(assignNode1);
        root.addChildToBack(assignNode2);

        Node resultVarNode = new Node(Token.VAR);
        Node resultChild1 = new Node(Token.NAME);
        resultChild1.setString("a");
        Node resultChild2 = new Node(Token.NAME);
        resultChild2.setString("b");
        resultChild2.addChildToBack(new Node(Token.NAME).setString("c"));
        resultVarNode.addChildToBack(resultChild1);
        resultVarNode.addChildToBack(resultChild2);
        
        Node expectedResult = new Node(Token.ROOT);
        expectedResult.addChildToBack(resultVarNode);
        expectedResult.addChildToBack(assignNode1);
        expectedResult.addChildToBack(assignNode2);

        cVD.process(externs, root);

        assertEquals(expectedResult, root);
    }
}