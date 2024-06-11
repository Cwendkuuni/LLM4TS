import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class MethodCompilerPassTest {
    
    private AbstractCompiler compiler;
    private MethodCompilerPass.SignatureStore signatureStore;
    private MethodCompilerPass methodCompilerPass;
    
    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        signatureStore = mock(MethodCompilerPass.SignatureStore.class);
        methodCompilerPass = new MethodCompilerPass(compiler) {

            @Override
            MethodCompilerPass.Callback getActingCallback() {
                return null;
            }

            @Override
            MethodCompilerPass.SignatureStore getSignatureStore() {
                return signatureStore;
            }
            
        };
    }
    
    @Test
    public void testProcess_withExternsNull() {
        Node root = new Node(Token.ROOT);
        methodCompilerPass.process(null, root);
        
        verify(signatureStore).reset();
        assertEquals(0, methodCompilerPass.externMethods.size());
        assertEquals(0, methodCompilerPass.externMethodsWithoutSignatures.size());
        assertEquals(0, methodCompilerPass.nonMethodProperties.size());
        assertEquals(0, methodCompilerPass.methodDefinitions.size());
    }
    
    @Test
    public void testProcess_withExternsAndRoot() {
        Node externs = new Node(Token.ROOT);
        Node root = new Node(Token.ROOT);
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.setFunctionId("function1");
        Node nameNode = new Node(Token.NAME);
        nameNode.setString("name1");
        Node assignNode = new Node(Token.ASSIGN, nameNode, functionNode);
        Node getPropNode = new Node(Token.GETPROP, new Node(Token.NAME, "prop1"), new Node(Token.STRING, "string1"));
        
        externs.addChildToFront(assignNode);
        root.addChildToFront(getPropNode);
        
        methodCompilerPass.process(externs, root);
        
        verify(signatureStore).reset();
        assertEquals(1, methodCompilerPass.externMethods.size());
        assertEquals(0, methodCompilerPass.externMethodsWithoutSignatures.size());
        assertEquals(0, methodCompilerPass.nonMethodProperties.size());
        assertEquals(1, methodCompilerPass.methodDefinitions.size());
        assertEquals(functionNode, methodCompilerPass.methodDefinitions.get("string1").iterator().next());
    }
    
    @Test
    public void testProcess_withExternsAndRoot_withoutSignature() {
        Node externs = new Node(Token.ROOT);
        Node root = new Node(Token.ROOT);
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.setFunctionId("function1");
        Node nameNode = new Node(Token.NAME);
        nameNode.setString("name1");
        Node assignNode = new Node(Token.ASSIGN, nameNode, functionNode);
        Node getPropNode = new Node(Token.GETPROP, new Node(Token.NAME, "prop1"), new Node(Token.STRING, "string1"));
        
        externs.addChildToFront(assignNode);
        root.addChildToFront(getPropNode);
        
        methodCompilerPass.process(externs, root);
        
        verify(signatureStore).reset();
        assertEquals(1, methodCompilerPass.externMethods.size());
        assertEquals(1, methodCompilerPass.externMethodsWithoutSignatures.size());
        assertEquals(0, methodCompilerPass.nonMethodProperties.size());
        assertEquals(0, methodCompilerPass.methodDefinitions.size());
    }
    
    @Test
    public void testProcess_withExternsAndRoot_withParentPrototype() {
        Node externs = new Node(Token.ROOT);
        Node root = new Node(Token.ROOT);
        Node getPropNode1 = new Node(Token.GETPROP, new Node(Token.GETPROP, new Node(Token.NAME, "parent1"), new Node(Token.STRING, "prototype")), new Node(Token.STRING, "string1"));
        Node getPropNode2 = new Node(Token.GETPROP, new Node(Token.GETPROP, new Node(Token.NAME, "parent2"), new Node(Token.STRING, "prototype")), new Node(Token.STRING, "string2"));
        Node nameNode1 = new Node(Token.NAME);
        nameNode1.setString("name1");
        Node assignNode1 = new Node(Token.ASSIGN, nameNode1, new Node(Token.FUNCTION));
        Node nameNode2 = new Node(Token.NAME);
        nameNode2.setString("name2");
        Node assignNode2 = new Node(Token.ASSIGN, nameNode2, new Node(Token.FUNCTION));
        
        externs.addChildToFront(assignNode1);
        externs.addChildToFront(assignNode2);
        root.addChildToFront(getPropNode1);
        root.addChildToFront(getPropNode2);
        
        methodCompilerPass.process(externs, root);
        
        verify(signatureStore, times(2)).addSignature(anyString(), any(Node.class), anyString());
    }
    
    @Test
    public void testGatherSignatures_withGetPropAndFunction() {
        NodeTraversal traversal = mock(NodeTraversal.class);
        Node parent = new Node(Token.ASSIGN);
        Node getPropNode = new Node(Token.GETPROP, new Node(Token.NAME, "parent"), new Node(Token.STRING, "string1"));
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.setFunctionId("function1");
        Node nameNode = new Node(Token.NAME);
        nameNode.setString("name1");
        Node assignNode = new Node(Token.ASSIGN, nameNode, functionNode);
        
        parent.addChildToFront(getPropNode);
        parent.addChildToFront(assignNode);
        
        methodCompilerPass.new GatherSignatures().visit(traversal, getPropNode, parent);
        
        verify(signatureStore).addSignature(eq("string1"), eq(functionNode), anyString());
    }
    
    @Test
    public void testGatherSignatures_withGetPropAndName() {
        NodeTraversal traversal = mock(NodeTraversal.class);
        Node parent = new Node(Token.ASSIGN);
        Node getPropNode = new Node(Token.GETPROP, new Node(Token.NAME, "parent"), new Node(Token.STRING, "string1"));
        Node nameNode = new Node(Token.NAME);
        nameNode.setString("name1");
        Node assignNode = new Node(Token.ASSIGN, nameNode, new Node(Token.FUNCTION));
        
        parent.addChildToFront(getPropNode);
        parent.addChildToFront(assignNode);
        
        methodCompilerPass.new GatherSignatures().visit(traversal, getPropNode, parent);
        
        verify(signatureStore, never()).addSignature(eq("string1"), any(Node.class), anyString());
    }
    
    @Test
    public void testGatherSignatures_withObjectLit() {
        NodeTraversal traversal = mock(NodeTraversal.class);
        Node parent = new Node(Token.OBJECTLIT);
        Node key1 = new Node(Token.STRING);
        key1.setString("string1");
        Node value1 = new Node(Token.FUNCTION);
        Node key2 = new Node(Token.STRING);
        key2.setString("string2");
        Node value2 = new Node(Token.FUNCTION);
        
        parent.addChildToFront(key1);
        key1.addChildToFront(value1);
        key1.addChildToFront(key2);
        key2.addChildToFront(value2);
        
        methodCompilerPass.new GatherSignatures().visit(traversal, parent, null);
        
        verify(signatureStore).addSignature(eq("string1"), eq(value1), anyString());
        verify(signatureStore).addSignature(eq("string2"), eq(value2), anyString());
    }
}