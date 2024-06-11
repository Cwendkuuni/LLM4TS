import static org.junit.Assert.*;
import org.junit.Test;

public class RuntimeTypeCheckTest {

    @Test
    public void testAddMarkers() {
        AbstractCompiler compiler = new AbstractCompiler();
        RuntimeTypeCheck.AddMarkers addMarkers = new RuntimeTypeCheck.AddMarkers(compiler);
        NodeTraversal t = new NodeTraversal(compiler, addMarkers);
        Node externs = new Node(Token.SCRIPT);
        Node root = new Node(Token.SCRIPT);

        // Create a function node
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.setJSType(new FunctionType());
        root.addChildToFront(functionNode);

        addMarkers.visit(t, functionNode, null);
    }

    @Test
    public void testAddChecks() {
        AbstractCompiler compiler = new AbstractCompiler();
        RuntimeTypeCheck.AddChecks addChecks = new RuntimeTypeCheck.AddChecks(compiler);
        NodeTraversal t = new NodeTraversal(compiler, addChecks);
        Node externs = new Node(Token.SCRIPT);
        Node root = new Node(Token.SCRIPT);

        // Create a function node
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.setJSType(new FunctionType());
        root.addChildToFront(functionNode);

        addChecks.visit(t, functionNode, null);
    }

    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
        runtimeTypeCheck.process(null, null);
    }
}