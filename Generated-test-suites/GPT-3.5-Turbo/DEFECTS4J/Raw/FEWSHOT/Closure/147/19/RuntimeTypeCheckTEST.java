import org.junit.Test;
import static org.junit.Assert.*;

public class RuntimeTypeCheckTest {
    
    @Test
    public void testAddMarkers() {
        // AddMarkers constructor
        AbstractCompiler compiler = new AbstractCompiler();
        RuntimeTypeCheck.AddMarkers addMarkers = new RuntimeTypeCheck.AddMarkers(compiler);
        
        // visitFunction
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        addMarkers.visitFunction(t, n, parent);
        
        // addMarker
        FunctionType funType = new FunctionType();
        Node nodeToInsertAfter = null;
        ObjectType interfaceType = null;
        Node result = addMarkers.addMarker(funType, nodeToInsertAfter, interfaceType);
        
        assertEquals(nodeToInsertAfter, result);
    }
    
    @Test
    public void testAddChecks() {
        // AddChecks constructor
        AbstractCompiler compiler = new AbstractCompiler();
        RuntimeTypeCheck.AddChecks addChecks = new RuntimeTypeCheck.AddChecks(compiler);
        
        // visitFunction
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        addChecks.visitFunction(t, n);
        
        // createCheckTypeCallNode
        JSType type = new JSType();
        Node expr = new Node();
        Node result = addChecks.createCheckTypeCallNode(type, expr);
        
        assertNull(result);
        
        ObjectType objType = new ObjectType();
        objType.setConstructor(new Node());
        type = objType;
        
        result = addChecks.createCheckTypeCallNode(type, expr);
        
        assertNull(result);
        
        objType.setConstructor(new Node(Token.INTERFACE, new Node()));
        result = addChecks.createCheckTypeCallNode(type, expr);
        
        assertNull(result);
        
        objType.setConstructor(new Node(Token.FUNCTION, new Node()));
        result = addChecks.createCheckTypeCallNode(type, expr);
        
        assertNotNull(result);
        
        type = new JSType(Token.NULL);
        result = addChecks.createCheckTypeCallNode(type, expr);
        
        assertNotNull(result);
        
        type = new JSType(Token.STRING);
        result = addChecks.createCheckTypeCallNode(type, expr);
        
        assertNotNull(result);
        
        type = new JSType(Token.NUMBER);
        result = addChecks.createCheckTypeCallNode(type, expr);
        
        assertNotNull(result);
        
        type = new JSType(Token.BOOLEAN);
        result = addChecks.createCheckTypeCallNode(type, expr);
        
        assertNotNull(result);
        
        type = new JSType(Token.VOID);
        result = addChecks.createCheckTypeCallNode(type, expr);
        
        assertNotNull(result);
    }
}