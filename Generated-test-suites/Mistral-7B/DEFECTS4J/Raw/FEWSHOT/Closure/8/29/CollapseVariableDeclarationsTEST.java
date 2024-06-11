package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Set;

public class CollapseVariableDeclarationsTest {

    @Test
    public void testCollapseVariableDeclarations() {
        // Given
        Node root = Node.newProgram();
        Scope scope = new Scope(root, null);

        Node varA = Node.newVar("var", "a");
        Node varB = Node.newVar("var", "b");
        Node varC = Node.newVar("var", "c");

        Node assignAB = Node.newAssign(Token.ASSIGN, "=");
        Node lhsAB = Node.newName("a");
        Node rhsAB = Node.newNumber(1);
        Node assignBB = Node.newAssign(Token.ASSIGN, "=");
        Node lhsBB = Node.newName("b");
        Node rhsBB = Node.newNumber(2);
        assignAB.addChildren(lhsAB, rhsAB);
        assignBB.addChildren(lhsBB, rhsBB);

        Node ifStatement = Node.newIf(Token.IF, null, null, null);
        ifStatement.addChild(assignAB);
        ifStatement.addChild(varB);
        ifStatement.addChild(assignBB);
        ifStatement.addChild(varC);
        root.addChildToBack(ifStatement);

        CompilerPass pass = new CollapseVariableDeclarations(null);

        // When
        pass.process(null, root);

        // Then
        List<Node> variableDeclarations = Lists.newArrayList();
        for (Node child = root.getFirstChild(); child != null; child = child.getNext()) {
            if (child.isIf()) {
                Node ifStatementTest = (Node) child;
                variableDeclarations.addAll(Lists.newArrayList(ifStatementTest.getFirstChild(), ifStatementTest.getLastChild().getNext()));
            }
        }

        assertEquals(1, variableDeclarations.size());
        Node collapsedVariableDeclarations = variableDeclarations.get(0);

        assertEquals(3, collapsedVariableDeclarations.getNumChildren());
        Node varCollapsed = collapsedVariableDeclarations.getFirstChild();
        assertEquals("var", varCollapsed.getFirstChild().getString());
        assertEquals("a, b = 1, c = 2", varCollapsed.toString());

        assertEquals(2, collapsedVariableDeclarations.getLastChild().getNumChildren());
        Node assignCollapsedA = collapsedVariableDeclarations.getLastChild().getFirstChild();
        assertEquals("a, b = 1, c = 2", assignCollapsedA.toString());
        Node assignCollapsedB = collapsedVariableDeclarations.getLastChild().getLastChild();
        assertEquals("c = 2", assignCollapsedB.toString());
    }

    @Test
    public void testCollapseVariableAssignments() {
        // Given
        Node root = Node.newProgram();
        Scope scope = new Scope(root, null);

        Node varA = Node.newVar("var", "a");
        Node assignAA = Node.newAssign(Token.ASSIGN, "=");
        Node lhsAA = Node.newName("a");
        Node rhsAA = Node.newBoolean(true);
        assignAA.addChildren(lhsAA, rhsAA);

        Node varB = Node.newVar("var", "b");
        Node assignAB = Node.newAssign(Token.ASSIGN, "=");
        Node lhsAB = Node.newName("a");
        Node rhsAB = Node.newName("b");
        assignAB.addChildren(lhsAB, rhsAB);

        Node varC = Node.newVar("var", "c");
        Node assignAC = Node.newAssign(Token.ASSIGN, "=");
        Node lhsAC = Node.newName("a");
        Node rhsAC = Node.newName("c");
        assignAC.addChildren(lhsAC, rhsAC);

        Node ifStatement = Node.newIf(Token.IF, null, null, null);
        ifStatement.addChild(assignAA);
        ifStatement.addChild(varA);
        ifStatement.addChild(assignAB);
        ifStatement.addChild(varB);
        ifStatement.addChild(assignAC);
        ifStatement.addChild(varC);
        root.addChildToBack(ifStatement);

        CompilerPass pass = new CollapseVariableDeclarations(null);

        // When
        pass.process(null, root);

        // Then
        List<Node> variableDeclarations = Lists.newArrayList();
        for (Node child = root.getFirstChild(); child != null; child = child.getNext()) {
            if (child.isIf()) {
                Node ifStatementTest = (Node) child;
                variableDeclarations.addAll(Lists.newArrayList(ifStatementTest.getFirstChild(), ifStatementTest.getLastChild().getNext()));
            }
        }

        assertEquals(1, variableDeclarations.size());
        Node collapsedVariableDeclarations = variableDeclarations.get(0);

        assertEquals(3, collapsedVariableDeclarations.getNumChildren());
        Node varCollapsed = collapsedVariableDeclarations.getFirstChild();
        assertEquals("var", varCollapsed.getFirstChild().getString());
        assertEquals("a = b = c", varCollapsed.toString());

        assertEquals(2, collapsedVariableDeclarations.getLastChild().getNumChildren());
        Node assignCollapsedA = collapsedVariableDeclarations.getLastChild().getFirstChild();
        assertEquals("a = b = c", assignCollapsedA.toString());
        Node assignCollapsedB = collapsedVariableDeclarations.getLastChild().getLastChild();
        assertEquals("c", assignCollapsedB.toString());
    }

    @Test
    public void testCollapseEmptyVariableDeclarations() {
        // Given
        Node root = Node.newProgram();
        Scope scope = new Scope(root, null);

        Node varA = Node.newVar("var", "a");
        Node varB = Node.newVar("var", "b");

        Node ifStatement = Node.newIf(Token.IF, null, null, null);
        ifStatement.addChild(varA);
        ifStatement.addChild(varB);
        root.addChildToBack(ifStatement);

        CompilerPass pass = new CollapseVariableDeclarations(null);

        // When
        pass.process(null, root);

        // Then
        List<Node> variableDeclarations = Lists.newArrayList();
        for (Node child = root.getFirstChild(); child != null; child = child.getNext()) {
            if (child.isIf()) {
                Node ifStatementTest = (Node) child;
                variableDeclarations.addAll(Lists.newArrayList(ifStatementTest.getFirstChild(), ifStatementTest.getLastChild().getNext()));
            }
        }

        assertEquals(1, variableDeclarations.size());
        Node collapsedVariableDeclarations = variableDeclarations.get(0);
        assertEquals(1, collapsedVariableDeclarations.getNumChildren());
        Node varCollapsed = collapsedVariableDeclarations;
        assertEquals("var", varCollapsed.getFirstChild().getString());
        assertEquals("a, b", varCollapsed.toString());
    }

    @Test
    public void testCollapseVariableDeclarationsInsideFunction() {
        // Given
        Node root = Node.newProgram();
        Scope scope = new Scope(root, null);

        Node functionDeclaration = Node.newFunction("function", "myFunction", null, null, null, Lists.<Node>newArrayList());

        Node varA = Node.newVar("var", "a");
        Node varB = Node.newVar("var", "b");

        Node ifStatement = Node.newIf(Token.IF, null, null, null);
        ifStatement.addChild(varA);
        ifStatement.addChild(varB);

        functionDeclaration.addChildToBack(ifStatement);
        root.addChildToBack(functionDeclaration);

        CompilerPass pass = new CollapseVariableDeclarations(null);

        // When
        pass.process(null, root);

        // Then
        List<Node> variableDeclarations = Lists.newArrayList();
        for (Node child = root.getFirstChild(); child != null; child = child.getNext()) {
            if (child.isFunction()) {
                Node functionTest = (Node) child;
                variableDeclarations.addAll(Lists.newArrayList(functionTest.getFirstChild(), functionTest.getLastChild()));
            }
        }

        assertEquals(1, variableDeclarations.size());
        Node collapsedVariableDeclarations = variableDeclarations.get(0);
        assertEquals(2, collapsedVariableDeclarations.getNumChildren());
        Node function = collapsedVariableDeclarations.getFirstChild();
        assertEquals("function", function.getString());
        Node variableDeclarationsInsideFunction = function.getLastChild();
        assertEquals(1, variableDeclarationsInsideFunction.getNumChildren());
        Node collapsedVariableDeclarationsInsideFunction = variableDeclarationsInsideFunction;
        assertEquals("var", collapsedVariableDeclarationsInsideFunction.getFirstChild().getString());
        assertEquals("a, b", collapsedVariableDeclarationsInsideFunction.toString());
    }

    @Test
    public void testCollapseVariableDeclarationsWithJSDocInfo() {
        // Given
        Node root = Node.newProgram();
        Scope scope = new Scope(root, null);

        Node functionDeclaration = Node.newFunction("function", "myFunction", null, null, null, Lists.<Node>newArrayList());

        Node varA = Node.newVar("var", "a");
        Node varB = Node.newVar("var", "b");

        Node jsdocInfo = Node.newJSDocInfo();
        jsdocInfo.addSuppression("mySuppression");
        varA.setJSDocInfo(jsdocInfo);

        Node ifStatement = Node.newIf(Token.IF, null, null, null);
        ifStatement.addChild(varA);
        ifStatement.addChild(varB);

        functionDeclaration.addChildToBack(ifStatement);
        root.addChildToBack(functionDeclaration);

        CompilerPass pass = new CollapseVariableDeclarations(null);

        // When
        pass.process(null, root);

        // Then
        List<Node> variableDeclarations = Lists.newArrayList();
        for (Node child = root.getFirstChild(); child != null; child = child.getNext()) {
            if (child.isFunction()) {
                Node functionTest = (Node) child;
                variableDeclarations.addAll(Lists.newArrayList(functionTest.getFirstChild(), functionTest.getLastChild()));
            }
        }

        assertEquals(1, variableDeclarations.size());
        Node collapsedVariableDeclarations = variableDeclarations.get(0);
        assertEquals(2, collapsedVariableDeclarations.getNumChildren());
        Node function = collapsedVariableDeclarations.getFirstChild();
        assertEquals("function", function.getString());
        Node variableDeclarationsInsideFunction = function.getLastChild();
        assertEquals(1, variableDeclarationsInsideFunction.getNumChildren());
        Node collapsedVariableDeclarationsInsideFunction = variableDeclarationsInsideFunction;
        assertEquals("var", collapsedVariableDeclarationsInsideFunction.getFirstChild().getString());
        assertEquals("a, b", collapsedVariableDeclarationsInsideFunction.toString());

        Node jsdocInfoCollapsed = collapsedVariableDeclarations.getFirstChild();
        assertEquals("function", jsdocInfoCollapsed.getString());
        Node jsdocInfoCollapsedInfo = jsdocInfoCollapsed.getLastChild();
        assertEquals("myFunction", jsdocInfoCollapsedInfo.getString());
        assertEquals(1, jsdocInfoCollapsedInfo.getNumChildren());
        Node suppressionCollapsed = jsdocInfoCollapsedInfo.getLastChild();
        assertEquals("mySuppression", suppressionCollapsed.getString());
    }
}