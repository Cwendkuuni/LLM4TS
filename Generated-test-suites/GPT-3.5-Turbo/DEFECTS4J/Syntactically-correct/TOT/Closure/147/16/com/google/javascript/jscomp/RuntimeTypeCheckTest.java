
import static org.junit.Assert.*;
import org.junit.Test;

public class RuntimeTypeCheckTest {
    
    @Test
    public void testAddMarkers() {
        AbstractCompiler compiler = new AbstractCompiler();
        RuntimeTypeCheck.AddMarkers addMarkers = new RuntimeTypeCheck.AddMarkers(compiler);
        
        // Create a dummy node for testing
        Node node = new Node(Token.FUNCTION);
        NodeTraversal t = new NodeTraversal();
        
        // Test when NodeUtil.isFunction returns false
        assertFalse(addMarkers.visit(t, node, null));
        
        // Test when NodeUtil.isFunction returns true but FunctionType is not constructor
        node.putProp(Node.IS_FUNCTION, true);
        assertFalse(addMarkers.visit(t, node, null));
        
        // Test when FunctionType has no source
        FunctionType funType = new FunctionType();
        funType.setConstructor(true);
        node.setJSType(funType);
        assertFalse(addMarkers.visit(t, node, null));
        
        // Test when FunctionType has source and class name is null
        funType.setSource(new Node(Token.NAME, "test", -1, -1));
        assertNull(NodeUtil.getFunctionName(funType.getSource()));
        assertFalse(addMarkers.visit(t, node, null));
        
        // Test when FunctionType has source and class name is not null
        funType.getSource().setString("TestClassName");
        assertTrue(addMarkers.visit(t, node, null));
    }
    
    @Test
    public void testAddChecks() {
        AbstractCompiler compiler = new AbstractCompiler();
        RuntimeTypeCheck.AddChecks addChecks = new RuntimeTypeCheck.AddChecks(compiler);
        
        // Create dummy nodes for testing
        Node node = new Node(Token.FUNCTION);
        NodeTraversal t = new NodeTraversal();
        
        // Test when NodeUtil.isFunction returns false
        assertFalse(addChecks.visit(t, node, null));
        
        // Test when NodeUtil.isFunction returns true but FunctionType is not constructor
        node.putProp(Node.IS_FUNCTION, true);
        assertFalse(addChecks.visit(t, node, null));
        
        // Test visitFunction for function with parameters
        node.setJSType(createFunctionTypeWithParameters());
        node.addChildToFront(NodeUtil.newName("param1"));
        node.addChildToFront(NodeUtil.newName("param0"));
        assertTrue(addChecks.visit(t, node, null));
        
        // Test visitFunction for function with no parameters
        node.detachChildren();
        assertFalse(addChecks.visit(t, node, null));
        
        // Test visitReturn for function with return type
        Node returnNode = new Node(Token.RETURN);
        returnNode.addChildToFront(NodeUtil.newName("returnValue"));
        assertTrue(addChecks.visit(t, returnNode, node));
        
        // Test visitReturn for function without return type
        returnNode.detachChildren();
        assertFalse(addChecks.visit(t, returnNode, node));
    }
    
    private FunctionType createFunctionTypeWithParameters() {
        FunctionType funType = new FunctionType();
        ObjectType objType = new ObjectType();
        objType.setConstructor(true);
        funType.setReturnType(objType);
        ObjectType paramType = new ObjectType();
        funType.getParameters().add(paramType);
        return funType;
    }
    
    @Test
    public void testAddBoilerplateCode() {
        AbstractCompiler compiler = new AbstractCompiler();
        RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
        runtimeTypeCheck.addBoilerplateCode();
        // Check that the boilerplate code was added to the compiler's node for code insertion
        assertEquals(1, compiler.getNodeForCodeInsertion(null).getChildCount());
    }
    
    @Test
    public void testGetBoilerplateCode() {
        AbstractCompiler compiler = new AbstractCompiler();
        String logFunction = "console.log";
        Node boilerplateCode = RuntimeTypeCheck.getBoilerplateCode(compiler, logFunction);
        // Check that the boilerplate code contains the log function
        assertTrue(NodeUtil.getSourceCode(boilerplateCode).contains(logFunction));
    }
}
