import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.MethodCompilerPass.SignatureStore;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.Node;

public class MethodCompilerPassTest {

    private MethodCompilerPass compilerPass;
    private AbstractCompiler compiler;
    
    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        compilerPass = new MethodCompilerPass(compiler) {
            
            @Override
            Callback getActingCallback() {
                return null;
            }
            
            @Override
            SignatureStore getSignatureStore() {
                return null;
            }
        };
    }
    
    @Test
    public void testProcess() {
        Node externs = getExternsNode();
        Node root = getRootNode();
        
        compilerPass.process(externs, root);
        
        // Verify that externMethods is properly populated
        assertTrue(compilerPass.externMethods.contains("setTimeout"));
        
        // Verify that externMethodsWithoutSignatures is properly populated
        assertTrue(compilerPass.externMethodsWithoutSignatures.contains("alert"));
        
        // Verify that nonMethodProperties is properly populated
        assertTrue(compilerPass.nonMethodProperties.contains("nonMethod"));
        
        // Verify that methodDefinitions is properly populated
        assertTrue(compilerPass.methodDefinitions.containsKey("method1"));
        assertTrue(compilerPass.methodDefinitions.get("method1").contains(getMethodNode("method1")));
        
        assertTrue(compilerPass.methodDefinitions.containsKey("method2"));
        assertTrue(compilerPass.methodDefinitions.get("method2").contains(getMethodNode("method2")));
        
        assertTrue(compilerPass.methodDefinitions.containsKey("method3"));
        assertTrue(compilerPass.methodDefinitions.get("method3").contains(getMethodNode("method3")));
    }
    
    private Node getExternsNode() {
        // Mock code for externs node
        return new Node(Token.SCRIPT).addChildToBack(new Node(Token.VAR).addChildToBack(
                new Node(Token.GETPROP, new Node(Token.NAME, new Node(Token.THIS)),
                        new Node(Token.STRING, "setTimeout"))
        )).addChildToBack(new Node(Token.STRING, "alert"));
    }
    
    private Node getRootNode() {
        // Mock code for root node
        return new Node(Token.SCRIPT).addChildToBack(new Node(Token.NAME, new Node(Token.FUNCTION, getMethodNode("method1"))))
                .addChildToBack(new Node(Token.VAR).addChildToBack(new Node(Token.GETPROP, new Node(Token.STRING, "nonMethod"))))
                .addChildToBack(new Node(Token.NAME, new Node(Token.FUNCTION, getMethodNode("method2"))))
                .addChildToBack(new Node(Token.NAME, new Node(Token.FUNCTION, getMethodNode("method3"))));
    }
    
    private Node getMethodNode(String name) {
        return new Node(Token.STRING, name);
    }
}