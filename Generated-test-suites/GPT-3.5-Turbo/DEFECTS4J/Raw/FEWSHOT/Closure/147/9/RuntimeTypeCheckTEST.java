
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;

public class RuntimeTypeCheckTest {

    private RuntimeTypeCheck runtimeTypeCheck;
    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        compiler = new Compiler();
        runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    }

    @Test
    public void testProcess() {
        Node externs = new Node(Token.SCRIPT);
        Node root = new Node(Token.SCRIPT);
        
        runtimeTypeCheck.process(externs, root);
        
        // Assertions go here
    }

    @Test
    public void testAddMarkers() {
        NodeTraversal t = new NodeTraversal(compiler, null);
        Node functionNode = new Node(Token.FUNCTION);
        NodeTraversal.Callback addMarkers = runtimeTypeCheck.new AddMarkers(compiler);

        addMarkers.visit(t, functionNode, null);

        // Assertions go here
    }

    @Test
    public void testAddChecks() {
        NodeTraversal t = new NodeTraversal(compiler, null);
        Node returnNode = new Node(Token.RETURN);
        NodeTraversal.Callback addChecks = runtimeTypeCheck.new AddChecks(compiler);

        addChecks.visit(t, returnNode, null);

        // Assertions go here
    }

    @Test
    public void testCreateCheckTypeCallNode_withUnionType() {
        JSType type = new UnionType();
        Node expr = new Node(Token.NAME);

        Node checkNode = runtimeTypeCheck.new AddChecks(compiler).createCheckTypeCallNode(type, expr);
        
        assertTrue(checkNode == null);
    }

    @Test
    public void testCreateCheckTypeCallNode_withInstanceType() {
        JSType type = new ObjectType();
        Node expr = new Node(Token.NAME);

        Node checkNode = runtimeTypeCheck.new AddChecks(compiler).createCheckTypeCallNode(type, expr);
        
        // Assertions go here
    }

    @Test
    public void testCreateCheckerNode_withInstanceType() {
        JSType type = new ObjectType();

        Node checkerNode = runtimeTypeCheck.new AddChecks(compiler).createCheckerNode(type);

        // Assertions go here
    }

    @Test
    public void testAddBoilerplateCode() {
        runtimeTypeCheck.addBoilerplateCode();

        // Assertions go here
    }

    @Test
    public void testJsCode() {
        String prop = "test";

        Node jsCodeNode = RuntimeTypeCheck.jsCode(prop);

        // Assertions go here
    }

    @Test
    public void testGetBoilerplateCode() {
        Node boilerplateCode = RuntimeTypeCheck.getBoilerplateCode(compiler, null);

        // Assertions go here
    }
}
