package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Set;

public class CollapseVariableDeclarationsTest {
    @Test
    public void testCollapseVariableDeclarations() {
        Node root = Node.createTree(
            "function foo() {" +
            "  var a;" +
            "  var b = 1;" +
            "  var c = 2;" +
            "  var d = 3;" +
            "}"
        );

        CollapseVariableDeclarations pass = new CollapseVariableDeclarations(new Compiler());
        pass.process(null, root);

        List<Node> variableDeclarations = Lists.newArrayList();
        for (Node node = root.getFirstChild(); node != null; node = node.getNext()) {
            if (node.isFunction()) {
                break;
            }
            if (node.isVar()) {
                variableDeclarations.add(node);
            }
        }

        assertEquals(3, variableDeclarations.size());
        Node varA = variableDeclarations.get(0);
        Node varB = variableDeclarations.get(1);
        Node varC = variableDeclarations.get(2);

        assertEquals(Token.VAR, varA.getTokenType());
        assertNull(varA.getFirstChild());

        assertEquals(Token.VAR, varB.getTokenType());
        assertEquals("b", varB.getString());
        assertEquals(Token.ASSIGN, varB.getLastChild().getTokenType());
        assertEquals(Token.NUM, varB.getLastChild().getLastChild().getTokenType());
        assertEquals(1, varB.getLastChild().getLastChild().getInt());

        assertEquals(Token.VAR, varC.getTokenType());
        assertEquals("c", varC.getString());
        assertEquals(Token.ASSIGN, varC.getFirstChild().getTokenType());
        assertEquals(Token.NUM, varC.getFirstChild().getLastChild().getTokenType());
        assertEquals(2, varC.getFirstChild().getLastChild().getInt());

        assertEquals(Token.VAR, varA.getParent().getLastChild().getTokenType());
        Node collapsedVariables = varA.getParent().getLastChild().getPrevious();
        assertEquals(Token.VAR, collapsedVariables.getTokenType());
        assertEquals(Token.ASSIGN, collapsedVariables.getLastChild().getTokenType());

        Node varList = collapsedVariables.getLastChild();
        List<Node> varListChildren = Lists.newArrayList();
        for (Node node = varList; node != null; node = node.getNext()) {
            if (node.isVar()) {
                varListChildren.add(node);
            }
        }

        assertEquals(3, varListChildren.size());
        Node varAB = varListChildren.get(0);
        Node varBC = varListChildren.get(1);
        Node varCD = varListChildren.get(2);

        assertEquals(Token.VAR, varAB.getTokenType());
        assertEquals("a", varAB.getString());

        assertEquals(Token.COMMA, varAB.getLastChild().getTokenType());

        assertEquals(Token.VAR, varBC.getTokenType());
        assertEquals("b", varBC.getString());
        assertEquals(Token.EQUAL, varBC.getLastChild().getTokenType());
        assertEquals(Token.NUM, varBC.getLastChild().getLastChild().getTokenType());
        assertEquals(1, varBC.getLastChild().getLastChild().getInt());

        assertEquals(Token.COMMA, varBC.getLastChild().getNext().getTokenType());

        assertEquals(Token.VAR, varCD.getTokenType());
        assertEquals("c", varCD.getString());
        assertEquals(Token.EQUAL, varCD.getFirstChild().getTokenType());
        assertEquals(Token.NUM, varCD.getFirstChild().getLastChild().getTokenType());
        assertEquals(2, varCD.getFirstChild().getLastChild().getInt());
    }

    @Test
    public void testCollapseVariableAssignments() {
        Node root = Node.createTree(
            "function foo() {" +
            "  var a = true;" +
            "  var b = true;" +
            "  var c = true;" +
            "}"
        );

        CollapseVariableDeclarations pass = new CollapseVariableDeclarations(new Compiler());
        pass.process(null, root);

        List<Node> variableDeclarations = Lists.newArrayList();
        for (Node node = root.getFirstChild(); node != null; node = node.getNext()) {
            if (node.isFunction()) {
                break;
            }
            if (node.isVar()) {
                variableDeclarations.add(node);
            }
        }

        assertEquals(3, variableDeclarations.size());
        Node varA = variableDeclarations.get(0);
        Node varB = variableDeclarations.get(1);
        Node varC = variableDeclarations.get(2);

        assertEquals(Token.VAR, varA.getTokenType());
        assertNull(varA.getFirstChild());

        assertEquals(Token.VAR, varB.getTokenType());
        assertEquals("b", varB.getString());
        assertEquals(Token.ASSIGN, varB.getLastChild().getTokenType());
        assertEquals(Token.BOOL, varB.getLastChild().getLastChild().getTokenType());
        assertEquals(true, varB.getLastChild().getLastChild().getBoolValue());

        assertEquals(Token.VAR, varC.getTokenType());
        assertEquals("c", varC.getString());
        assertEquals(Token.ASSIGN, varC.getFirstChild().getTokenType());
        assertEquals(Token.BOOL, varC.getFirstChild().getLastChild().getTokenType());
        assertEquals(true, varC.getFirstChild().getLastChild().getBoolValue());

        assertEquals(Token.VAR, varA.getParent().getLastChild().getTokenType());
        Node collapsedVariables = varA.getParent().getLastChild().getPrevious();
        assertEquals(Token.VAR, collapsedVariables.getTokenType());
        assertEquals(Token.ASSIGN, collapsedVariables.getLastChild().getTokenType());
        assertEquals(Token.ASSIGN, collapsedVariables.getLastChild().getLastChild().getTokenType());

        Node varList = collapsedVariables.getLastChild();
        List<Node> varListChildren = Lists.newArrayList();
        for (Node node = varList; node != null; node = node.getNext()) {
            if (node.isVar()) {
                varListChildren.add(node);
            }
        }

        assertEquals(3, varListChildren.size());
        Node varAB = varListChildren.get(0);
        Node varBC = varListChildren.get(1);
        Node varCD = varListChildren.get(2);

        assertEquals(Token.VAR, varAB.getTokenType());
        assertEquals("a", varAB.getString());

        assertEquals(Token.ASSIGN, varAB.getLastChild().getTokenType());

        assertEquals(Token.VAR, varBC.getTokenType());
        assertEquals("b", varBC.getString());
        assertEquals(Token.ASSIGN, varBC.getFirstChild().getTokenType());
        assertEquals(Token.VAR, varBC.getFirstChild().getLastChild().getTokenType());
        assertEquals("c", varBC.getFirstChild().getLastChild().getString());

        assertEquals(Token.ASSIGN, varBC.getFirstChild().getPrevious().getTokenType());

        assertEquals(Token.VAR, varCD.getTokenType());
        assertEquals("c", varCD.getString());
        assertEquals(Token.ASSIGN, varCD.getFirstChild().getTokenType());
        assertEquals(Token.BOOL, varCD.getFirstChild().getLastChild().getTokenType());
        assertEquals(true, varCD.getFirstChild().getLastChild().getBoolValue());

        assertEquals(Token.COMMA, varBC.getLastChild().getNext().getTokenType());

        assertEquals(Token.VAR, varAB.getParent().getLastChild().getTokenType());
        assertEquals(Token.ASSIGN, varAB.getParent().getLastChild().getLastChild().getTokenType());
    }

    @Test
    public void testCollapseVariableDeclarationsInFunction() {
        Node root = Node.createTree(
            "function foo() {" +
            "  function bar() {" +
            "    var a;" +
            "    var b = 1;" +
            "    var c = 2;" +
            "  }" +
            "}"
        );

        CollapseVariableDeclarations pass = new CollapseVariableDeclarations(new Compiler());
        pass.process(null, root);

        List<Node> variableDeclarations = Lists.newArrayList();
        for (Node node = root.getFirstChild(); node != null; node = node.getNext()) {
            if (node.isFunction()) {
                List<Node> functionVariableDeclarations = Lists.newArrayList();
                for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
                    if (child.isVar()) {
                        functionVariableDeclarations.add(child);
                    }
                }
                variableDeclarations.add(functionVariableDeclarations.get(0));
                variableDeclarations.addAll(functionVariableDeclarations.subList(1, functionVariableDeclarations.size()));
            }
        }

        assertEquals(2, variableDeclarations.size());
        Node varFunction = variableDeclarations.get(0);
        List<Node> collapsedVariables = Lists.newArrayList();
        for (Node node = varFunction.getNext(); node != null; node = node.getNext()) {
            if (node.isVar()) {
                collapsedVariables.add(node);
            }
        }

        Node collapsedVariableList = collapsedVariables.get(0);
        List<Node> collapsedVariableListChildren = Lists.newArrayList();
        for (Node node = collapsedVariableList; node != null; node = node.getNext()) {
            if (node.isVar()) {
                collapsedVariableListChildren.add(node);
            }
        }

        assertEquals(3, collapsedVariableListChildren.size());
        Node collapsedVariableA = collapsedVariableListChildren.get(0);
        Node collapsedVariableB = collapsedVariableListChildren.get(1);
        Node collapsedVariableC = collapsedVariableListChildren.get(2);

        assertEquals(Token.VAR, collapsedVariableA.getTokenType());
        assertEquals("a", collapsedVariableA.getString());

        assertEquals(Token.ASSIGN, collapsedVariableA.getLastChild().getTokenType());

        assertEquals(Token.VAR, collapsedVariableB.getTokenType());
        assertEquals("b", collapsedVariableB.getString());
        assertEquals(Token.EQUAL, collapsedVariableB.getLastChild().getTokenType());
        assertEquals(Token.NUM, collapsedVariableB.getLastChild().getLastChild().getTokenType());
        assertEquals(1, collapsedVariableB.getLastChild().getLastChild().getInt());

        assertEquals(Token.COMMA, collapsedVariableB.getLastChild().getNext().getTokenType());

        assertEquals(Token.VAR, collapsedVariableC.getTokenType());
        assertEquals("c", collapsedVariableC.getString());
        assertEquals(Token.EQUAL, collapsedVariableC.getFirstChild().getTokenType());
        assertEquals(Token.NUM, collapsedVariableC.getFirstChild().getLastChild().getTokenType());
        assertEquals(2, collapsedVariableC.getFirstChild().getLastChild().getInt());

        assertEquals(Token.VAR, collapsedVariableA.getParent().getLastChild().getTokenType());
        assertEquals(Token.ASSIGN, collapsedVariableA.getParent().getLastChild().getLastChild().getTokenType());
    }

    @Test
    public void testCollapseVariableDeclarationsInLoop() {
        Node root = Node.createTree(
            "function foo() {" +
            "  for (var i = 0; i < 10; i++) {" +
            "    var a = i * 2;" +
            "  }" +
            "}"
        );

        CollapseVariableDeclarations pass = new CollapseVariableDeclarations(new Compiler());
        pass.process(null, root);

        List<Node> variableDeclarations = Lists.newArrayList();
        for (Node node = root.getFirstChild(); node != null; node = node.getNext()) {
            if (node.isFunction()) {
                List<Node> functionVariableDeclarations = Lists.newArrayList();
                for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
                    if (child.isVar()) {
                        functionVariableDeclarations.add(child);
                    }
                }
                variableDeclarations.add(functionVariableDeclarations.get(0));
                variableDeclarations.addAll(functionVariableDeclarations.subList(1, functionVariableDeclarations.size()));
            }
        }

        assertEquals(2, variableDeclarations.size());
        Node varFunction = variableDeclarations.get(0);
        List<Node> collapsedVariables = Lists.newArrayList();
        for (Node node = varFunction.getNext(); node != null; node = node.getNext()) {
            if (node.isVar()) {
                collapsedVariables.add(node);
            }
        }

        Node collapsedVariableList = collapsedVariables.get(0);
        List<Node> collapsedVariableListChildren = Lists.newArrayList();
        for (Node node = collapsedVariableList; node != null; node = node.getNext()) {
            if (node.isVar()) {
                collapsedVariableListChildren.add(node);
            }
        }

        assertEquals(2, collapsedVariableListChildren.size());
        Node collapsedVariableI = collapsedVariableListChildren.get(0);
        Node collapsedVariableA = collapsedVariableListChildren.get(1);

        assertEquals(Token.VAR, collapsedVariableI.getTokenType());
        assertEquals("i", collapsedVariableI.getString());

        assertEquals(Token.ASSIGN, collapsedVariableI.getLastChild().getTokenType());

        assertEquals(Token.FOR, collapsedVariableI.getParent().getTokenType());
        assertEquals("i", collapsedVariableI.getParent().getFirstChild().getFirstChild().getString());
        assertEquals(Token.EQUAL, collapsedVariableI.getParent().getFirstChild().getLastChild().getTokenType());
        assertEquals(Token.NUM, collapsedVariableI.getParent().getFirstChild().getLastChild().getLastChild().getTokenType());
        assertEquals(0, collapsedVariableI.getParent().getFirstChild().getLastChild().getLastChild().getInt());

        assertEquals(Token.SEMICOLON, collapsedVariableI.getParent().getLastChild().getTokenType());

        assertEquals(Token.VAR, collapsedVariableA.getTokenType());
        assertEquals("a", collapsedVariableA.getString());
        assertEquals(Token.ASSIGN, collapsedVariableA.getLastChild().getTokenType());

        assertEquals(Token.FOR, collapsedVariableA.getParent().getTokenType());
        assertEquals(Token.VAR, collapsedVariableA.getParent().getLastChild().getTokenType());
        assertEquals("i", collapsedVariableA.getParent().getLastChild().getString());

        assertEquals(Token.ASSIGN, collapsedVariableA.getParent().getLastChild().getLastChild().getTokenType());

        assertEquals(Token.MUL, collapsedVariableA.getParent().getLastChild().getLastChild().getLastChild().getTokenType());
        assertEquals(Token.NUM, collapsedVariableA.getParent().getLastChild().getLastChild().getLastChild().getLastChild().getTokenType());
        assertEquals(2, collapsedVariableA.getParent().getLastChild().getLastChild().getLastChild().getLastChild().getInt());

        assertEquals(Token.SEMICOLON, collapsedVariableA.getParent().getLastChild().getLastChild().getLastChild().getTokenType());
    }
}