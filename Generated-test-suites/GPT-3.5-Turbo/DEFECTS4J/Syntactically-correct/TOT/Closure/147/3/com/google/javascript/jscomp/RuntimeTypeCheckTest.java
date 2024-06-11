import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.UnionType;

public class RuntimeTypeCheckTest {

    @Test
    public void testAddMarkers() {
        AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
        RuntimeTypeCheck.AddMarkers addMarkers = new RuntimeTypeCheck.AddMarkers(compiler);
        NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
        Node externs = new Node(Token.SCRIPT);
        Node root = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.setJSType(Mockito.mock(FunctionType.class));

        addMarkers.visit(traversal, functionNode, root);

        Assert.assertTrue(root.hasChildren());
    }

    @Test
    public void testAddChecks() {
        AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
        RuntimeTypeCheck.AddChecks addChecks = new RuntimeTypeCheck.AddChecks(compiler);
        NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
        Node externs = new Node(Token.SCRIPT);
        Node root = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.setJSType(Mockito.mock(FunctionType.class));
        Node returnNode = new Node(Token.RETURN);
        returnNode.addChildToBack(Node.newString("test"));

        addChecks.visit(traversal, functionNode, root);
        addChecks.visit(traversal, returnNode, root);

        Assert.assertTrue(root.hasChildren());
    }

    @Test
    public void testProcess() {
        AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
        RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
        Node externs = new Node(Token.SCRIPT);
        Node root = new Node(Token.SCRIPT);

        runtimeTypeCheck.process(externs, root);
    }

    @Test
    public void testCreateCheckerNode() {
        AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
        RuntimeTypeCheck.AddChecks addChecks = new RuntimeTypeCheck.AddChecks(compiler);
        NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
        Node externs = new Node(Token.SCRIPT);
        Node root = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.setJSType(Mockito.mock(FunctionType.class));
        Node returnNode = new Node(Token.RETURN);
        returnNode.addChildToBack(Node.newString("test"));
        JSType jstype = Mockito.mock(JSType.class);

        addChecks.visit(traversal, functionNode, root);
        addChecks.visit(traversal, returnNode, root);

        RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
        Node node = runtimeTypeCheck.createCheckerNode(jstype);

        Assert.assertNull(node);
    }

    @Test
    public void testCreateCheckTypeCallNode() {
        AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
        RuntimeTypeCheck.AddChecks addChecks = new RuntimeTypeCheck.AddChecks(compiler);
        NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
        Node externs = new Node(Token.SCRIPT);
        Node root = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.setJSType(Mockito.mock(FunctionType.class));
        Node returnNode = new Node(Token.RETURN);
        returnNode.addChildToBack(Node.newString("test"));
        JSType jstype = Mockito.mock(JSType.class);

        addChecks.visit(traversal, functionNode, root);
        addChecks.visit(traversal, returnNode, root);

        RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
        Node node = runtimeTypeCheck.createCheckTypeCallNode(jstype, returnNode);

        Assert.assertNotNull(node);
    }

    @Test
    public void testGetBoilerplateCode() {
        AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
        String logFunction = "function(warning, expr) {}";

        Node node = RuntimeTypeCheck.getBoilerplateCode(compiler, logFunction);

        Assert.assertNotNull(node);
    }
}