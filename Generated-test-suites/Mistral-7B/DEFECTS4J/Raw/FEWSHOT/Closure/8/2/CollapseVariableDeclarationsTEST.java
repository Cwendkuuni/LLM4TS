package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Set;

public class CollapseVariableDeclarationsTest {

    @Test
    public void testCollapseVariableDeclarations() {
        // Test simple variable declarations collapsing
        Node root = createNode("var a; var b = 1; var c = 2;");
        CollapseVariableDeclarations compilerPass = new CollapseVariableDeclarations(new Compiler() {
            @Override
            public NodeFactory getFactory() {
                return new NodeFactory();
            }

            @Override
            public NodeTraversal getTraversal() {
                return new NodeTraversal();
            }
        });
        compilerPass.process(null, root);
        Node result = compilerPass.getResult();

        assertVarDeclaredAsOne(result, "a");
        assertVarDeclaredAsOne(result, "b", 1);
        assertVarDeclaredAsOne(result, "c", 2);

        // Test variable declarations with names and initial values
        root = createNode("var a = 1; var b = 2; var c = 3;");
        compilerPass = new CollapseVariableDeclarations(new Compiler() {
            @Override
            public NodeFactory getFactory() {
                return new NodeFactory();
            }

            @Override
            public NodeTraversal getTraversal() {
                return new NodeTraversal();
            }
        });
        compilerPass.process(null, root);
        result = compilerPass.getResult();

        assertVarDeclaredAsOne(result, "a", 1);
        assertVarDeclaredAsOne(result, "b", 2);
        assertVarDeclaredAsOne(result, "c", 3);

        // Test variable declarations with same name but different scopes
        root = createNode("function f() { var a = 1; } var a = 2;");
        compilerPass = new CollapseVariableDeclarations(new Compiler() {
            @Override
            public NodeFactory getFactory() {
                return new NodeFactory();
            }

            @Override
            public NodeTraversal getTraversal() {
                return new NodeTraversal();
            }
        });
        compilerPass.process(null, root);
        result = compilerPass.getResult();

        assertVarDeclaredInFunction(result, "f", "a", 1);
        assertVarDeclaredOutsideFunction(result, "a", 2);

        // Test collapsing multiple variable declarations into one with initial values
        root = createNode("var a, b = 1, c = 2;");
        compilerPass = new CollapseVariableDeclarations(new Compiler() {
            @Override
            public NodeFactory getFactory() {
                return new NodeFactory();
            }

            @Override
            public NodeTraversal getTraversal() {
                return new NodeTraversal();
            }
        });
        compilerPass.process(null, root);
        result = compilerPass.getResult();

        assertVarDeclaredAsOne(result, "a", null);
        assertVarDeclaredAsOne(result, "b", 1);
        assertVarDeclaredAsOne(result, "c", 2);

        // Test collapsing multiple variable declarations into one with no initial values
        root = createNode("var a, b, c;");
        compilerPass = new CollapseVariableDeclarations(new Compiler() {
            @Override
            public NodeFactory getFactory() {
                return new NodeFactory();
            }

            @Override
            public NodeTraversal getTraversal() {
                return new NodeTraversal();
            }
        });
        compilerPass.process(null, root);
        result = compilerPass.getResult();

        assertVarDeclaredAsOne(result, "a, b, c");

        // Test collapsing multiple assignments into one
        root = createNode("var a; a = 1; a = 2;");
        compilerPass = new CollapseVariableDeclarations(new Compiler() {
            @Override
            public NodeFactory getFactory() {
                return new NodeFactory();
            }

            @Override
            public NodeTraversal getTraversal() {
                return new NodeTraversal();
            }
        });
        compilerPass.process(null, root);
        result = compilerPass.getResult();

        assertVarDeclaredAsOne(result, "a");
        assertAssign(result, "a", 2);

        // Test collapsing multiple assignments into one with redeclared variables
        root = createNode("var a; a = 1; var a = 2;");
        compilerPass = new CollapseVariableDeclarations(new Compiler() {
            @Override
            public NodeFactory getFactory() {
                return new NodeFactory();
            }

            @Override
            public NodeTraversal getTraversal() {
                return new NodeTraversal();
            }
        });
        compilerPass.process(null, root);
        result = compilerPass.getResult();

        assertVarDeclaredAsOne(result, "a");
        assertAssignWithSuppression(result, "a", 2, "duplicate");

        // Test collapsing assignments into a variable declaration
        root = createNode("var a; a = 1; a = 2;");
        compilerPass = new CollapseVariableDeclarations(new Compiler() {
            @Override
            public NodeFactory getFactory() {
                return new NodeFactory();
            }

            @Override
            public NodeTraversal getTraversal() {
                return new NodeTraversal();
            }
        });
        compilerPass.process(null, root);
        result = compilerPass.getResult();

        assertVarDeclaredAsOne(result, "a", 2);

        // Test collapsing multiple assignments into one with no redeclared variables
        root = createNode("var a; a = 1; var b = a; a = 2;");
        compilerPass = new CollapseVariableDeclarations(new Compiler() {
            @Override
            public NodeFactory getFactory() {
                return new NodeFactory();
            }

            @Override
            public NodeTraversal getTraversal() {
                return new NodeTraversal();
            }
        });
        compilerPass.process(null, root);
        result = compilerPass.getResult();

        assertVarDeclaredAsOne(result, "a");
        assertAssign(result, "b", 1);
        assertAssign(result, "a", 2);

        // Test collapsing multiple assignments with different variables into one
        root = createNode("var a, b; a = 1; b = 2;");
        compilerPass = new CollapseVariableDeclarations(new Compiler() {
            @Override
            public NodeFactory getFactory() {
                return new NodeFactory();
            }

            @Override
            public NodeTraversal getTraversal() {
                return new NodeTraversal();
            }
        });
        compilerPass.process(null, root);
        result = compilerPass.getResult();

        assertVarDeclaredAsOne(result, "a, b");
        assertAssign(result, "a", 1);
        assertAssign(result, "b", 2);

        // Test collapsing multiple declarations inside an IF statement
        root = createNode("if (true) { var a; var b = 1; } var c = 2;");
        compilerPass = new CollapseVariableDeclarations(new Compiler() {
            @Override
            public NodeFactory getFactory() {
                return new NodeFactory();
            }

            @Override
            public NodeTraversal getTraversal() {
                return new NodeTraversal();
            }
        });
        compilerPass.process(null, root);
        result = compilerPass.getResult();

        assertVarDeclaredInIfStatement(result, "a");
        assertVarDeclaredOutsideIfStatement(result, "c");
        assertVarDeclaredInIfStatement(result, "b", 1);

        // Test collapsing multiple declarations inside a LOOP statement
        root = createNode("for (var i = 0; i < 10; i++) { var a; var b = i; } var c = 2;");
        compilerPass = new CollapseVariableDeclarations(new Compiler() {
            @Override
            public NodeFactory getFactory() {
                return new NodeFactory();
            }

            @Override
            public NodeTraversal getTraversal() {
                return new NodeTraversal();
            }
        });
        compilerPass.process(null, root);
        result = compilerPass.getResult();

        assertVarDeclaredInLoopStatement(result, "i");
        assertVarDeclaredOutsideLoopStatement(result, "c");
        assertVarDeclaredInLoopStatement(result, "a");
        assertVarDeclaredInLoopStatement(result, "b", 0);

        // Test collapsing multiple declarations inside a CASE statement
        root = createNode("switch (2) { case 1: var a; case 2: var b; } var c = 2;");
        compilerPass = new CollapseVariableDeclarations(new Compiler() {
            @Override
            public NodeFactory getFactory() {
                return new NodeFactory();
            }

            @Override
            public NodeTraversal getTraversal() {
                return new NodeTraversal();
            }
        });
        compilerPass.process(null, root);
        result = compilerPass.getResult();

        assertVarDeclaredInCaseStatement(result, 1, "a");
        assertVarDeclaredInCaseStatement(result, 2, "b");
        assertVarDeclaredOutsideCaseStatement(result, "c");
    }

    private Node createNode(String code) {
        NodeFactory factory = new NodeFactory();
        Node root = factory.parse(code);
        return root;
    }

    private void assertVarDeclaredAsOne(Node node, String... variableNames) {
        int count = 0;
        for (String variableName : variableNames) {
            assertNotNull(node.getVariable(variableName));
            count++;
        }
        assertEquals("Expected " + variableNames.length + " variable declarations, but found " + count,
            variableNames.length, count);
    }

    private void assertVarDeclaredAsOne(Node node, String variableName, Object initialValue) {
        Var var = node.getVariable(variableName);
        assertNotNull(var);
        assertEquals(variableName, var.getName());
        assertNull(var.getValue());
        if (initialValue != null) {
            Node valueNode = var.getInitialValue();
            assertNotNull(valueNode);
            assertEquals(initialValue.getClass().getName(), valueNode.getClass().getName());
            assertEquals(initialValue.toString(), valueNode.toString());
        }
    }

    private void assertVarDeclaredInFunction(Node node, String functionName, String variableName, Object initialValue) {
        Scope functionScope = node.getFunctionScope();
        Var var = functionScope.getVar(variableName);
        assertNotNull(var);
        assertEquals(functionName, var.getScope().getName());
        assertNotNull(var.getValue());
        if (initialValue != null) {
            Node valueNode = var.getValue();
            assertNotNull(valueNode);
            assertEquals(initialValue.getClass().getName(), valueNode.getClass().getName());
            assertEquals(initialValue.toString(), valueNode.toString());
        }
    }

    private void assertVarDeclaredOutsideFunction(Node node, String variableName, Object initialValue) {
        Scope globalScope = node.getTopLevelScope();
        Var var = globalScope.getVar(variableName);
        assertNotNull(var);
        assertNull(var.getScope());
        if (initialValue != null) {
            Node valueNode = var.getValue();
            assertNotNull(valueNode);
            assertEquals(initialValue.getClass().getName(), valueNode.getClass().getName());
            assertEquals(initialValue.toString(), valueNode.toString());
        }
    }

    private void assertAssign(Node node, String variableName, Object value) {
        Node assignment = getAssignNode(node, variableName);
        assertNotNull(assignment);
        Node valueNode = assignment.getLastChild();
        assertNotNull(valueNode);
        assertEquals(value.getClass().getName(), valueNode.getClass().getName());
        assertEquals(value.toString(), valueNode.toString());
    }

    private void assertAssignWithSuppression(Node node, String variableName, Object value, String suppression) {
        Node assignment = getAssignNode(node, variableName);
        assertNotNull(assignment);
        Node valueNode = assignment.getLastChild();
        assertNotNull(valueNode);
        JSDocInfo info = assignment.getJSDocInfo();
        assertNotNull(info);
        assertEquals(suppression, info.getSuppressions().get(0).getName());
        assertEquals(value.getClass().getName(), valueNode.getClass().getName());
        assertEquals(value.toString(), valueNode.toString());
    }

    private Node getAssignNode(Node node, String variableName) {
        for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
            if (child.isExprAssign()) {
                Node lhs = child.getFirstChild();
                if (lhs.isName() && lhs.getString().equals(variableName)) {
                    return child;
                }
            }
        }
        return null;
    }
}