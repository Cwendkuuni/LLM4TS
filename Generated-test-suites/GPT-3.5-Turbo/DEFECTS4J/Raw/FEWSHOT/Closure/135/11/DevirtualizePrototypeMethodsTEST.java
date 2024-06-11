package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DevirtualizePrototypeMethodsTest {
    @Test
    public void testIsCall() {
        Node node = new Node(Token.CALL, new Node(Token.NAME));
        UseSite site = new UseSite(node);
        assertEquals(true, DevirtualizePrototypeMethods.isCall(site));
        
        Node parent = new Node(Token.VAR);
        parent.addChildToBack(node);
        assertEquals(false, DevirtualizePrototypeMethods.isCall(site));
    }

    @Test
    public void testIsPrototypeMethodDefinition() {
        Node node = new Node(Token.GETPROP, new Node(Token.NAME), new Node(Token.STRING));
        assertEquals(true, DevirtualizePrototypeMethods.isPrototypeMethodDefinition(node));

        Node parent = new Node(Token.GETPROP, new Node(Token.THIS), new Node(Token.STRING));
        assertEquals(false, DevirtualizePrototypeMethods.isPrototypeMethodDefinition(parent));
    }

    @Test
    public void testGetRewrittenMethodName() {
        String orginalMethodName = "testMethod";
        assertEquals("JSCompiler_StaticMethods_testMethod", DevirtualizePrototypeMethods.getRewrittenMethodName(orginalMethodName));
    }

    @Test
    public void testIsEligibleDefinition() {
        SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
        DefinitionSite definitionSite = new DefinitionSite(node, true, true, def);

        assertEquals(true, DevirtualizePrototypeMethods.isEligibleDefinition(defFinder, definitionSite));
    }

    @Test
    public void testRewriteDefinition() {
        String newMethodName = "JSCompiler_StaticMethods_testMethod";
        Node functionNode = new Node(Token.FUNCTION, new Node(Token.NAME), new Node(Token.PARAM_LIST), new Node(Token.BLOCK));

        DevirtualizePrototypeMethods.rewriteDefinition(node, newMethodName);

        assertEquals(functionNode, node);
    }

    @Test
    public void testFixFunctionType() {
        FunctionType type = new FunctionType(null, null, null);
        functionNode.setJSType(type);

        DevirtualizePrototypeMethods.fixFunctionType(functionNode);

        assertEquals(newType, functionNode.getJSType());
    }

    @Test
    public void testReplaceReferencesToThis() {
        String name = "testName";
        Node thisNode = new Node(Token.THIS);
        Node newName = new Node(Token.NAME, new Node(Token.STRING, name));
        Node child = new Node(Token.EXPR_RESULT, thisNode);

        DevirtualizePrototypeMethods.replaceReferencesToThis(child, name);

        assertEquals(newName, child.getChildAtIndex(0));
    }
}