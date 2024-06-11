
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Sets;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MethodCompilerPassTest {
    
    private MethodCompilerPass methodCompilerPass;
    
    @Mock
    private AbstractCompiler compiler;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        methodCompilerPass = new MethodCompilerPass(compiler) {
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
    public void testProcess_withExternsContainingMethod_verifyMethodDefinitions() {
        // Setup
        Node externs = new Node(Token.SCRIPT, new Node(Token.EMPTY));
        Node root = new Node(Token.SCRIPT, new Node(Token.EMPTY));
        
        // Stub
        Set<String> externMethods = Sets.newHashSet("method1", "method2");
        methodCompilerPass.externMethods = externMethods;
        Set<String> externMethodsWithoutSignatures = Sets.newHashSet();
        methodCompilerPass.externMethodsWithoutSignatures = externMethodsWithoutSignatures;
        HashMultimap<String, Node> methodDefinitions = HashMultimap.create();
        methodDefinitions.put("method1", new Node(Token.FUNCTION));
        methodDefinitions.put("method2", new Node(Token.FUNCTION));
        methodCompilerPass.methodDefinitions = methodDefinitions;
        
        // Test
        methodCompilerPass.process(externs, root);
        
        // Verify
        assertEquals(2, methodCompilerPass.methodDefinitions.get("method1").size());
        assertEquals(2, methodCompilerPass.methodDefinitions.get("method2").size());
    }
    
    @Test
    public void testAddPossibleSignature_withFunctionNode_verifySignatureAdded() {
        // Setup
        String name = "method1";
        Node functionNode = new Node(Token.FUNCTION);
        
        // Test
        methodCompilerPass.addPossibleSignature(name, functionNode, null);
        
        // Verify
        assertEquals(1, methodCompilerPass.methodDefinitions.get("method1").size());
        assertEquals(functionNode, methodCompilerPass.methodDefinitions.get("method1").iterator().next());
    }
    
    @Test
    public void testAddPossibleSignature_withNameNode_verifySignatureAdded() {
        // Setup
        String name = "method2";
        Node nameNode = new Node(Token.NAME, "method2");
        nameNode.putProp(Node.SCOPE, new Scope(Node.newString(Token.NAME, "method2")));
        Node functionNode = new Node(Token.FUNCTION);
        when(compiler.isIdeMode()).thenReturn(false);
        
        // Test
        methodCompilerPass.addPossibleSignature(name, nameNode, new NodeTraversal(compiler, null, new CompilerOptions()));
        
        // Verify
        assertEquals(1, methodCompilerPass.methodDefinitions.get("method2").size());
        assertEquals(functionNode, methodCompilerPass.methodDefinitions.get("method2").iterator().next());
    }
    
    @Test
    public void testAddPossibleSignature_withNameNodeAndUndefinedFunction_verifyExceptionThrown() {
        // Setup
        String name = "undefinedMethod";
        Node nameNode = new Node(Token.NAME, "undefinedMethod");
        nameNode.putProp(Node.SCOPE, new Scope(Node.newString(Token.NAME, "undefinedMethod")));
        
        // Assert
        assertThrows(IllegalStateException.class, () -> {
            // Test
            methodCompilerPass.addPossibleSignature(name, nameNode, new NodeTraversal(compiler, null, new CompilerOptions()));
        });
    }
    
    @Test
    public void testAddPossibleSignature_withInvalidNode_verifyNonMethodPropertyAdded() {
        // Setup
        String name = "property";
        Node invalidNode = new Node(Token.STRING, "property");
        
        // Test
        methodCompilerPass.addPossibleSignature(name, invalidNode, null);
        
        // Verify
        assertTrue(methodCompilerPass.nonMethodProperties.contains(name));
    }
    
    @Test
    public void testAddSignature_withExternMethodWithoutSignatures_verifyNoSignatureAdded() {
        // Setup
        String name = "method3";
        Node functionNode = new Node(Token.FUNCTION);
        methodCompilerPass.externMethodsWithoutSignatures.add(name);
        
        // Test
        methodCompilerPass.addSignature(name, functionNode, null);
        
        // Verify
        assertFalse(methodCompilerPass.methodDefinitions.containsKey(name));
    }
    
    @Test
    public void testAddSignature_withExistingExternMethodWithoutSignatures_verifyNoSignatureAdded() {
        // Setup
        String name = "method4";
        Node functionNode = new Node(Token.FUNCTION);
        methodCompilerPass.externMethodsWithoutSignatures.add(name);
        methodCompilerPass.methodDefinitions.put(name, new Node(Token.NAME));
        
        // Test
        methodCompilerPass.addSignature(name, functionNode, null);
        
        // Verify
        assertEquals(1, methodCompilerPass.methodDefinitions.get(name).size());
        assertEquals(functionNode, methodCompilerPass.methodDefinitions.get(name).iterator().next());
    }
    
    @Test
    public void testProcess_withExternsAndRoot_verifyMethodDefinitionsCleared() {
        // Setup
        Node externs = new Node(Token.SCRIPT, new Node(Token.EMPTY));
        Node root = new Node(Token.SCRIPT, new Node(Token.EMPTY));
        methodCompilerPass.externMethods.add("method5");
        methodCompilerPass.methodDefinitions.put("method5", new Node(Token.NAME));
        
        // Test
        methodCompilerPass.process(externs, root);
        
        // Verify
        assertTrue(methodCompilerPass.externMethods.isEmpty());
        assertTrue(methodCompilerPass.methodDefinitions.isEmpty());
    }
}

