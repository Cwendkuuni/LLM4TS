import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MethodCompilerPassTest {
    @Test
    public void testProcess() {
        MethodCompilerPass methodCompilerPass = new MethodCompilerPass() {
            @Override
            Callback getActingCallback() {
                return null;
            }

            @Override
            SignatureStore getSignatureStore() {
                return null;
            }
        };
        
        Node externs = new Node(Token.EXTERN);
        Node root = new Node(Token.ROOT);
        
        methodCompilerPass.process(externs, root);
        
        // Verify the results here
        
    }
    
    @Test
    public void testAddPossibleSignature() {
        MethodCompilerPass methodCompilerPass = new MethodCompilerPass() {
            @Override
            Callback getActingCallback() {
                return null;
            }

            @Override
            SignatureStore getSignatureStore() {
                return null;
            }
        };
        
        Node node = new Node(Token.NAME, "test");
        methodCompilerPass.addPossibleSignature("test", node, new NodeTraversal());
        
        // Verify the results here
        
    }
    
    @Test
    public void testAddSignature() {
        MethodCompilerPass methodCompilerPass = new MethodCompilerPass() {
            @Override
            Callback getActingCallback() {
                return null;
            }

            @Override
            SignatureStore getSignatureStore() {
                return null;
            }
        };
        
        Node function = new Node(Token.FUNCTION);
        methodCompilerPass.addSignature("test", function, "test.js");
        
        // Verify the results here
        
    }
    
    @Test
    public void testProcessPrototypeParent() {
        MethodCompilerPass methodCompilerPass = new MethodCompilerPass() {
            @Override
            Callback getActingCallback() {
                return null;
            }

            @Override
            SignatureStore getSignatureStore() {
                return null;
            }
        };
        
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.GETPROP);
        Node parent = new Node(Token.ASSIGN);
        
        methodCompilerPass.processPrototypeParent(t, parent);
        
        // Verify the results here
        
    }
    
    @Test
    public void testGatherSignatures() {
        MethodCompilerPass methodCompilerPass = new MethodCompilerPass() {
            @Override
            Callback getActingCallback() {
                return null;
            }

            @Override
            SignatureStore getSignatureStore() {
                return null;
            }
        };
        
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.GETPROP);
        Node parent = new Node(Token.ASSIGN);
        
        methodCompilerPass.gatherSignatures(t, n, parent);
        
        // Verify the results here
        
    }
    
    @Test
    public void testGetExternMethods() {
        MethodCompilerPass methodCompilerPass = new MethodCompilerPass() {
            @Override
            Callback getActingCallback() {
                return null;
            }

            @Override
            SignatureStore getSignatureStore() {
                return null;
            }
        };
        
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.GETPROP);
        
        methodCompilerPass.getExternMethods(t, n, null);
        
        // Verify the results here
        
    }
}