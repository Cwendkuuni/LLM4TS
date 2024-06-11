import static org.junit.Assert.*;
import org.junit.Test;

public class DevirtualizePrototypeMethodsTest {
    
    @Test
    public void testRewriteDefinitionIfEligible() {
        AbstractCompiler compiler = new AbstractCompiler();
        DevirtualizePrototypeMethods devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);
        SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
        
        // Create a mock DefinitionSite
        DefinitionSite defSite = new DefinitionSite();
        defSite.inExterns = false;
        defSite.inGlobalScope = true;
        defSite.node = new Node(Token.ASSIGN);

        // Create a mock Definition
        Definition definition = new Definition();
        Node rValue = new Node(Token.FUNCTION);
        definition.setRValue(rValue);
        Node paramName = new Node(Token.NAME, "param");
        Node paramList = new Node(Token.PARAM_LIST, paramName);
        rValue.addChildToBack(paramList);

        defSite.definition = definition;
        
        // Create a mock SimpleDefinitionFinder object
        defFinder.addDefinitionSite(defSite);
        
        // Call the method being tested
        devirtualizePrototypeMethods.rewriteDefinitionIfEligible(defSite, defFinder);
        
        // Verify the result
        assertEquals("JSCompiler_StaticMethods_param", defSite.definition.getLValue().getLastChild().getString());
    }
    
    @Test
    public void testRewriteCallSites() {
        AbstractCompiler compiler = new AbstractCompiler();
        DevirtualizePrototypeMethods devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);
        SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
        
        // Create a mock UseSite
        UseSite useSite = new UseSite();
        useSite.node = new Node(Token.CALL);

        // Create a mock Definition
        Definition definition = new Definition();

        // Create a mock SimpleDefinitionFinder object
        defFinder.addUseSite(useSite, definition);
        
        // Call the method being tested
        devirtualizePrototypeMethods.rewriteCallSites(defFinder, definition, "newMethodName");
        
        // Verify the result
        assertEquals("newMethodName", useSite.node.getLastChild().getString());
    }
    
    @Test
    public void testRewriteDefinition() {
        AbstractCompiler compiler = new AbstractCompiler();
        DevirtualizePrototypeMethods devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);
        
        // Create a mock Node
        Node node = new Node(Token.GETPROP);
        Node functionNode = new Node(Token.FUNCTION);
        String orginalMethodName = "originalMethodName";
        node.addChildToFront(Node.newString(Token.NAME, orginalMethodName));
        functionNode.addChildToFront(new Node(Token.NAME, orginalMethodName));
        functionNode.addChildToBack(new Node(Token.NAME, "param"));
        node.addChildToBack(functionNode);
        
        // Create a mock String
        String newMethodName = "newMethodName";
        
        // Call the method being tested
        devirtualizePrototypeMethods.rewriteDefinition(node, newMethodName);
        
        // Verify the result
        assertEquals(newMethodName, node.getFirstChild().getString());
        assertEquals(newMethodName + "$self", functionNode.getSecondChild().getString());
    }
    
    @Test
    public void testFixFunctionType() {
        AbstractCompiler compiler = new AbstractCompiler();
        DevirtualizePrototypeMethods devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);
        
        // Create a mock Node
        Node functionNode = new Node(Token.FUNCTION);
        ObjectType thisType = compiler.getTypeRegistry().getNativeObjectType(JSTypeNative.UNKNOWN_TYPE);
        JSType returnType = compiler.getTypeRegistry().getNativeType(JSTypeNative.VOID_TYPE);
        functionNode.setJSType(compiler.getTypeRegistry().createFunctionType(thisType, returnType));
        
        // Call the method being tested
        devirtualizePrototypeMethods.fixFunctionType(functionNode);
        
        // Verify the result
        assertNotNull(functionNode.getJSType());
    }
    
    @Test
    public void testReplaceReferencesToThis() {
        AbstractCompiler compiler = new AbstractCompiler();
        DevirtualizePrototypeMethods devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);
        
        // Create a mock Node
        Node node = new Node(Token.BLOCK);
        Node functionNode = new Node(Token.FUNCTION);
        Node body = new Node(Token.BLOCK);
        Node expr = new Node(Token.EXPR_RESULT);
        Node thisRef = new Node(Token.THIS);
        expr.addChildToBack(thisRef);
        body.addChildToBack(expr);
        functionNode.addChildToBack(body);
        node.addChildToBack(functionNode);
        
        // Call the method being tested
        devirtualizePrototypeMethods.replaceReferencesToThis(node, "name");
        
        // Verify the result
        assertEquals("name", thisRef.getString());
    }
    
}