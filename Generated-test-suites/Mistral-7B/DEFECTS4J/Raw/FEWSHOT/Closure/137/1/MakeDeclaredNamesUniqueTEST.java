package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class MakeDeclaredNamesUniqueTest {

    @Test
    public void testContextualRenamer() {
        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        NodeTraversal nodeTraversal = new NodeTraversal(null);
        nodeTraversal.setCompiler(new Compiler() {});
        nodeTraversal.setGlobalScope(new Scope() {});

        Node root = new Node(Token.PROGRAM);
        Node function = new Node(Token.FUNCTION);
        Node nameNode = new Node(Token.NAME, "func");
        Node body = new Node(Token.BLOCK);

        function.setFirstChild(nameNode);
        function.addChild(body);
        root.addChild(function);

        makeDeclaredNamesUnique.traverse(nodeTraversal, root, null, null);
        assertEquals("func", nameNode.getString());

        function.getFirstChild().setString("func_1");
        makeDeclaredNamesUnique.traverse(nodeTraversal, root, null, null);
        assertEquals("func_1", nameNode.getString());

        nameNode.setString("func_2");
        makeDeclaredNamesUnique.traverse(nodeTraversal, root, null, null);
        assertEquals("func_2", nameNode.getString());

        // Test with a global variable.
        Node globalVar = new Node(Token.VAR, "globalVar");
        root.addChild(globalVar);

        makeDeclaredNamesUnique.traverse(nodeTraversal, root, null, null);
        assertEquals("globalVar", globalVar.getString());

        function.getFirstChild().setString("globalVar");
        makeDeclaredNamesUnique.traverse(nodeTraversal, root, null, null);
        assertNotEquals("globalVar", globalVar.getString());
        assertEquals("globalVar_1", globalVar.getString());

        // Test with a local variable.
        Node localVar = new Node(Token.VAR, "localVar");
        body.addChild(localVar);

        makeDeclaredNamesUnique.traverse(nodeTraversal, root, null, null);
        assertEquals("localVar", localVar.getString());

        function.getFirstChild().setString("localVar");
        makeDeclaredNamesUnique.traverse(nodeTraversal, root, null, null);
        assertNotEquals("localVar", localVar.getString());
        assertEquals("localVar_1", localVar.getString());

        // Test with a function parameter.
        Node parameter = new Node(Token.VAR, "parameter");
        function.addChild(parameter);

        makeDeclaredNamesUnique.traverse(nodeTraversal, root, null, null);
        assertEquals("parameter", parameter.getString());

        function.getFirstChild().setString("parameter");
        makeDeclaredNamesUnique.traverse(nodeTraversal, root, null, null);
        assertNotEquals("parameter", parameter.getString());
        assertEquals("parameter_1", parameter.getString());

        // Test with a local function.
        Node localFunction = new Node(Token.FUNCTION);
        nameNode = new Node(Token.NAME, "func");
        localFunction.setFirstChild(nameNode);
        body = new Node(Token.BLOCK);
        localFunction.addChild(body);
        body.addChild(new Node(Token.RETURN, new Node(Token.NAME, "localVar")));
        body.addChild(new Node(Token.VAR, "localVar"));
        function.addChild(localFunction);

        makeDeclaredNamesUnique.traverse(nodeTraversal, root, null, null);
        assertEquals("func", nameNode.getString());
        assertEquals("localVar_1", localVar.getString());
    }

    @Test
    public void testInlineRenamer() {
        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique(
            new InlineRenamer(() -> "i") {});
        NodeTraversal nodeTraversal = new NodeTraversal(null);
        nodeTraversal.setCompiler(new Compiler() {});
        nodeTraversal.setGlobalScope(new Scope() {});

        Node root = new Node(Token.PROGRAM);
        Node function = new Node(Token.FUNCTION);
        Node nameNode = new Node(Token.NAME, "func");
        Node body = new Node(Token.BLOCK);

        function.setFirstChild(nameNode);
        function.addChild(body);
        root.addChild(function);

        makeDeclaredNamesUnique.traverse(nodeTraversal, root, null, null);
        assertEquals("func_i", nameNode.getString());

        function.getFirstChild().setString("func_i_1");
        makeDeclaredNamesUnique.traverse(nodeTraversal, root, null, null);
        assertEquals("func_i_1", nameNode.getString());

        nameNode.setString("func_i_2");
        makeDeclaredNamesUnique.traverse(nodeTraversal, root, null, null);
        assertEquals("func_i_2", nameNode.getString());

        // Test with a global variable.
        Node globalVar = new Node(Token.VAR, "globalVar");
        root.addChild(globalVar);

        makeDeclaredNamesUnique.traverse(nodeTraversal, root, null, null);
        assertEquals("globalVar_i", globalVar.getString());

        function.getFirstChild().setString("globalVar_i");
        makeDeclaredNamesUnique.traverse(nodeTraversal, root, null, null);
        assertNotEquals("globalVar_i", globalVar.getString());
        assertEquals("globalVar_i_1", globalVar.getString());

        // Test with a local variable.
        Node localVar = new Node(Token.VAR, "localVar");
        body.addChild(localVar);

        makeDeclaredNamesUnique.traverse(nodeTraversal, root, null, null);
        assertEquals("localVar_i", localVar.getString());

        function.getFirstChild().setString("localVar_i");
        makeDeclaredNamesUnique.traverse(nodeTraversal, root, null, null);
        assertNotEquals("localVar_i", localVar.getString());
        assertEquals("localVar_i_1", localVar.getString());

        // Test with a function parameter.
        Node parameter = new Node(Token.VAR, "parameter");
        function.addChild(parameter);

        makeDeclaredNamesUnique.traverse(nodeTraversal, root, null, null);
        assertEquals("parameter_i", parameter.getString());

        function.getFirstChild().setString("parameter_i");
        makeDeclaredNamesUnique.traverse(nodeTraversal, root, null, null);
        assertNotEquals("parameter_i", parameter.getString());
        assertEquals("parameter_i_1", parameter.getString());

        // Test with a local function.
        Node localFunction = new Node(Token.FUNCTION);
        nameNode = new Node(Token.NAME, "func");
        localFunction.setFirstChild(nameNode);
        body = new Node(Token.BLOCK);
        localFunction.addChild(body);
        body.addChild(new Node(Token.RETURN, new Node(Token.NAME, "localVar")));
        body.addChild(new Node(Token.VAR, "localVar"));
        function.addChild(localFunction);

        makeDeclaredNamesUnique.traverse(nodeTraversal, root, null, null);
        assertEquals("func_i", nameNode.getString());
        assertEquals("localVar_i", localVar.getString());
    }

    @Test
    public void testRenamingConflicts() {
        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        NodeTraversal nodeTraversal = new NodeTraversal(null);
        nodeTraversal.setCompiler(new Compiler() {});
        nodeTraversal.setGlobalScope(new Scope() {});

        Node root = new Node(Token.PROGRAM);
        Node function = new Node(Token.FUNCTION);
        Node nameNode1 = new Node(Token.NAME, "func1");
        Node nameNode2 = new Node(Token.NAME, "func2");
        Node body = new Node(Token.BLOCK);

        function.setFirstChild(nameNode1);
        function.addChild(body);
        root.addChild(function);

        Node function2 = new Node(Token.FUNCTION);
        nameNode2 = new Node(Token.NAME, "func1");
        function2.setFirstChild(nameNode2);
        body = new Node(Token.BLOCK);
        function2.addChild(body);
        root.addChild(function2);

        makeDeclaredNamesUnique.traverse(nodeTraversal, root, null, null);
        assertEquals("func1_1", nameNode1.getString());
        assertEquals("func1_2", nameNode2.getString());

        nameNode1.setString("func1_2");
        makeDeclaredNamesUnique.traverse(nodeTraversal, root, null, null);
        assertEquals("func1_3", nameNode1.getString());
        assertEquals("func1_2", nameNode2.getString());
    }

    @Test
    public void testRenamingConstVariable() {
        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        NodeTraversal nodeTraversal = new NodeTraversal(null);
        nodeTraversal.setCompiler(new Compiler() {});
        nodeTraversal.setGlobalScope(new Scope() {});

        Node root = new Node(Token.PROGRAM);
        Node function = new Node(Token.FUNCTION);
        Node nameNode = new Node(Token.NAME, "constVar");
        Node body = new Node(Token.BLOCK);

        function.setFirstChild(nameNode);
        function.addChild(body);
        root.addChild(function);
        nameNode.setProp(Node.IS_CONSTANT_NAME, true);

        makeDeclaredNamesUnique.traverse(nodeTraversal, root, null, null);
        assertTrue(nameNode.getProp(Node.IS_CONSTANT_NAME));
    }

    @Test
    public void testRenamingConstFunction() {
        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        NodeTraversal nodeTraversal = new NodeTraversal(null);
        nodeTraversal.setCompiler(new Compiler() {});
        nodeTraversal.setGlobalScope(new Scope() {});

        Node root = new Node(Token.PROGRAM);
        Node function = new Node(Token.FUNCTION, Node.IS_CONSTANT_NAME, null);
        Node nameNode = new Node(Token.NAME, "constFunc");
        Node body = new Node(Token.BLOCK);

        function.setFirstChild(nameNode);
        function.addChild(body);
        root.addChild(function);

        makeDeclaredNamesUnique.traverse(nodeTraversal, root, null, null);
        assertTrue(function.getProp(Node.IS_CONSTANT_NAME));
        assertEquals("constFunc", nameNode.getString());
    }

    @Test
    public void testRenamingGlobalConstFunction() {
        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        NodeTraversal nodeTraversal = new NodeTraversal(null);
        nodeTraversal.setCompiler(new Compiler() {});
        nodeTraversal.setGlobalScope(new Scope() {});

        Node root = new Node(Token.PROGRAM);
        Node function = new Node(Token.FUNCTION, Node.IS_CONSTANT_NAME, null);
        Node nameNode = new Node(Token.NAME, "constFunc");
        Node body = new Node(Token.BLOCK);

        function.setFirstChild(nameNode);
        function.addChild(body);
        root.addChild(function);

        makeDeclaredNamesUnique.traverse(nodeTraversal, root, null, null);
        assertTrue(function.getProp(Node.IS_CONSTANT_NAME));
        assertEquals("constFunc", nameNode.getString());

        function.setProp(Node.IS_GLOBAL, true);
        makeDeclaredNamesUnique.traverse(nodeTraversal, root, null, null);
        assertEquals("constFunc", nameNode.getString());
    }

    @Test
    public void testMergingRenamedNames() {
        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        NodeTraversal nodeTraversal = new NodeTraversal(null);
        nodeTraversal.setCompiler(new Compiler() {});
        nodeTraversal.setGlobalScope(new Scope() {});

        Node root = new Node(Token.PROGRAM);
        Node function = new Node(Token.FUNCTION);
        Node renamableNameNode = new Node(Token.NAME, "renamableName");
        Node body = new Node(Token.BLOCK);
        Node renamedNameNode = new Node(Token.NAME, "renamedName");

        function.setFirstChild(renamableNameNode);
        function.addChild(body);
        root.addChild(function);

        body.addChild(new Node(Token.ASSIGN, renamableNameNode, renamedNameNode));
        root.addChild(new Node(Token.ASSIGN, new Node(Token.NAME, "globalName"), renamableNameNode));

        makeDeclaredNamesUnique.traverse(nodeTraversal, root, null, null);
        assertEquals("renamableName_1", renamableNameNode.getString());
        assertEquals("renamedName_1", renamedNameNode.getString());
        assertEquals("globalName", nodeTraversal.getScope().getVar("globalName").getNameNode().getString());
    }
}