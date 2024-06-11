
import org.junit.Test;
import static org.junit.Assert.*;

public class MethodCompilerPassTest {

    MethodCompilerPass methodCompilerPass = new MethodCompilerPass() {
        @Override
        public Callback getActingCallback() {
            return null;
        }

        @Override
        public SignatureStore getSignatureStore() {
            return null;
        }
    };

    @Test
    public void testProcess() {
        // test process method with externs and root
        methodCompilerPass.process(new Node(Token.EXTERN, "externs"), new Node(Token.ROOT, "root"));
        // assertion statements
        assertTrue(methodCompilerPass.methodDefinitions.isEmpty());
        assertTrue(methodCompilerPass.externMethodsWithoutSignatures.isEmpty());
        assertTrue(methodCompilerPass.externMethods.isEmpty());
        assertTrue(methodCompilerPass.nonMethodProperties.isEmpty());
    }

    @Test
    public void testAddPossibleSignature() {
        // test addPossibleSignature method with a function node
        Node functionNode = new Node(Token.FUNCTION, "function");
        methodCompilerPass.addPossibleSignature("testFunction", functionNode, null);
        // assertion statements
        assertFalse(methodCompilerPass.nonMethodProperties.contains("testFunction"));
        assertTrue(methodCompilerPass.methodDefinitions.containsKey("testFunction"));
        assertTrue(methodCompilerPass.methodDefinitions.get("testFunction").contains(functionNode));
    }

    @Test
    public void testAddSignature() {
        // test addSignature method with a function node
        Node functionNode = new Node(Token.FUNCTION, "function");
        methodCompilerPass.addSignature("testFunction", functionNode, null);
        // assertion statements
        assertFalse(methodCompilerPass.externMethodsWithoutSignatures.contains("testFunction"));
        assertTrue(methodCompilerPass.methodDefinitions.containsKey("testFunction"));
        assertTrue(methodCompilerPass.methodDefinitions.get("testFunction").contains(functionNode));
    }

    @Test
    public void testGetExternMethods() {
        // test visit method of GetExternMethods with getprop and function nodes
        Node getPropNode = new Node(Token.GETPROP, "getprop");
        Node functionNode = new Node(Token.FUNCTION, "function");
        Node assignNode = new Node(Token.ASSIGN, "assign");
        getPropNode.addChildToBack(assignNode);
        assignNode.addChildToBack(getPropNode);
        assignNode.addChildToBack(functionNode);
        NodeTraversal t = new NodeTraversal(null, null, null);
        methodCompilerPass.getExternMethods().visit(t, getPropNode, null);
        // assertion statements
        assertTrue(methodCompilerPass.methodDefinitions.containsKey("bar"));
        assertTrue(methodCompilerPass.methodDefinitions.get("bar").contains(functionNode));
        assertTrue(methodCompilerPass.externMethods.contains("bar"));
    }

    @Test
    public void testGatherSignatures() {
        // test visit method of GatherSignatures with getprop and object literal nodes
        Node getPropNode = new Node(Token.GETPROP, "getprop");
        Node objectLitNode = new Node(Token.OBJECTLIT, "objectlit");
        Node stringNode = new Node(Token.STRING, "string");
        Node functionNode = new Node(Token.FUNCTION, "function");
        getPropNode.addChildToBack(stringNode);
        objectLitNode.addChildToBack(getPropNode);
        objectLitNode.addChildToBack(functionNode);
        NodeTraversal t = new NodeTraversal(null, null, null);
        methodCompilerPass.getGatherSignatures().visit(t, getPropNode, null);
        // assertion statements
        assertTrue(methodCompilerPass.methodDefinitions.containsKey("bar"));
        assertTrue(methodCompilerPass.methodDefinitions.get("bar").contains(functionNode));
        assertFalse(methodCompilerPass.nonMethodProperties.contains("bar"));
        assertTrue(methodCompilerPass.nonMethodProperties.contains("string"));
    }
}
