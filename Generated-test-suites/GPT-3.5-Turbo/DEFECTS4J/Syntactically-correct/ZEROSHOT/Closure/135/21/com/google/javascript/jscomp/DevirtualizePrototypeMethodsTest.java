import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DevirtualizePrototypeMethodsTest {
    
    private DevirtualizePrototypeMethods devirtualizePrototypeMethods;
    
    @Before
    public void setUp() throws Exception {
        devirtualizePrototypeMethods = new DevirtualizePrototypeMethods();
    }
    
    @Test
    public void testIsCall() {
        UseSite site = new UseSite(null, null);
        site.node = new Node(Token.CALL, new Node(Token.NAME));
        boolean result = devirtualizePrototypeMethods.isCall(site);
        assertTrue(result);
    }
    
    @Test
    public void testIsCall_notACall() {
        UseSite site = new UseSite(null, null);
        site.node = new Node(Token.NAME);
        boolean result = devirtualizePrototypeMethods.isCall(site);
        assertFalse(result);
    }
    
    @Test
    public void testIsPrototypeMethodDefinition() {
        Node node = new Node(Token.ASSIGN);
        node.setParent(new Node(Token.EXPR_RESULT));
        Node functionNode = new Node(Token.FUNCTION);
        node.addChildToFront(functionNode);
        Node getPropNode = new Node(Token.GETPROP);
        node.addChildToBack(getPropNode);
        Node prototypeNode = new Node(Token.STRING, "prototype");
        getPropNode.addChildToFront(prototypeNode);
        boolean result = devirtualizePrototypeMethods.isPrototypeMethodDefinition(node);
        assertTrue(result);
    }
    
    @Test
    public void testIsPrototypeMethodDefinition_notAPrototypeMethodDefinition() {
        Node node = new Node(Token.ASSIGN);
        node.setParent(new Node(Token.EXPR_RESULT));
        Node functionNode = new Node(Token.FUNCTION);
        node.addChildToFront(functionNode);
        Node getPropNode = new Node(Token.GETPROP);
        node.addChildToBack(getPropNode);
        boolean result = devirtualizePrototypeMethods.isPrototypeMethodDefinition(node);
        assertFalse(result);
    }
    
    @Test
    public void testGetRewrittenMethodName() {
        String originalMethodName = "accumulate";
        String result = devirtualizePrototypeMethods.getRewrittenMethodName(originalMethodName);
        assertEquals("JSCompiler_StaticMethods_accumulate", result);
    }
    
    @Test
    public void testIsEligibleDefinition() {
        SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder();
        DefinitionSite definitionSite = new DefinitionSite();
        definitionSite.inExterns = false;
        definitionSite.inGlobalScope = true;
        Definition definition = new Definition();
        definition.setRValue(new Node(Token.FUNCTION));
        definition.setLValue(new Node(Token.GETPROP));
        defFinder.addDefinition(definition, definitionSite);
        UseSite useSite = new UseSite();
        useSite.node = new Node(Token.CALL);
        defFinder.addUseSite(definition, useSite);
        boolean result = devirtualizePrototypeMethods.isEligibleDefinition(defFinder, definitionSite);
        assertTrue(result);
    }
    
    @Test
    public void testIsEligibleDefinition_notEligible() {
        SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder();
        DefinitionSite definitionSite = new DefinitionSite();
        definitionSite.inExterns = false;
        definitionSite.inGlobalScope = true;
        Definition definition = new Definition();
        definition.setRValue(new Node(Token.FUNCTION));
        definition.setLValue(new Node(Token.GETPROP));
        defFinder.addDefinition(definition, definitionSite);
        boolean result = devirtualizePrototypeMethods.isEligibleDefinition(defFinder, definitionSite);
        assertFalse(result);
    }
    
    @Test
    public void testRewriteCallSites() {
        SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder();
        Definition definition = new Definition();
        UseSite useSite = new UseSite();
        useSite.node = new Node(Token.CALL, new Node(Token.NAME));
        defFinder.addUseSite(definition, useSite);
        String newMethodName = "JSCompiler_StaticMethods_accumulate";
        devirtualizePrototypeMethods.rewriteCallSites(defFinder, definition, newMethodName);
        Node expectedNode = new Node(Token.CALL, new Node(Token.NAME, newMethodName));
        assertEquals(expectedNode, useSite.node);
    }
    
    @Test
    public void testRewriteDefinition() {
        Node node = new Node(Token.ASSIGN);
        node.setParent(new Node(Token.EXPR_RESULT));
        Node functionNode = new Node(Token.FUNCTION);
        node.addChildToFront(functionNode);
        Node expr = new Node(Token.EXPR_RESULT, node);
        Node block = new Node(Token.BLOCK, expr);
        
        String newMethodName = "JSCompiler_StaticMethods_accumulate";
        devirtualizePrototypeMethods.rewriteDefinition(node, newMethodName);
        
        Node expectedNode = new Node(Token.VAR, new Node(Token.NAME, newMethodName));
        expectedNode.getFirstChild().addChildToFront(functionNode);
        Node argList = new Node(Token.PARAM_LIST, new Node(Token.NAME, newMethodName + "$self"));
        functionNode.addChildToFront(argList);
        Node body = new Node(Token.BLOCK);
        functionNode.addChildToBack(body);
        Node newNameNode = new Node(Token.NAME, newMethodName);
        body.addChildToFront(newNameNode);
        assertEquals(expectedNode, block.getFirstChild());
    }
    
    @Test
    public void testFixFunctionType() {
        Node functionNode = new Node(Token.FUNCTION);
        FunctionType type = new FunctionType();
        functionNode.setJSType(type);
        devirtualizePrototypeMethods.fixFunctionType(functionNode);
        JSType expectedType = new FunctionType();
        expectedType.addParameter(new ObjectType(JSTypeNative.OBJECT_TYPE));
        expectedType.setReturnType(type.getReturnType());
        assertEquals(expectedType, functionNode.getJSType());
    }
    
    @Test
    public void testReplaceReferencesToThis() {
        Node node = new Node(Token.FUNCTION);
        Node body = new Node(Token.BLOCK,
                             new Node(Token.THIS),
                             new Node(Token.NAME, "x"),
                             new Node(Token.THIS));
        node.addChildToBack(body);
        String name = "self";
        devirtualizePrototypeMethods.replaceReferencesToThis(node, name);
        Node expectedNode = new Node(Token.BLOCK,
                                     new Node(Token.NAME, name),
                                     new Node(Token.NAME, "x"),
                                     new Node(Token.NAME, name));
        assertEquals(expectedNode, body);
    }
}