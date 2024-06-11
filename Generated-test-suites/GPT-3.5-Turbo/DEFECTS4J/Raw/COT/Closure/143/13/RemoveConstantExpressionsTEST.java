
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class RemoveConstantExpressionsTest {

    private AbstractCompiler compiler;
    private RemoveConstantExpressions.RemoveConstantRValuesCallback callback;
    private Node externs;
    private Node root;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        callback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        externs = new Node(Token.EXTERNs);
        root = new Node(Token.ROOT);
    }

    @After
    public void tearDown() {
        compiler = null;
        callback = null;
        externs = null;
        root = null;
    }

    @Test
    public void testProcess() {
        RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
        pass.process(externs, root);
        // assert something
    }

    @Test
    public void testRemoveConstantRValuesCallback() {
        NodeTraversal traversal = mock(NodeTraversal.class);
        Node node = new Node(Token.NAME, "test");
        Node parent = new Node(Token.EXPR_RESULT);
        callback.visit(traversal, node, parent);
        // assert something
    }

    @Test
    public void testTrySimplify() {
        Node parent = new Node(Token.EXPR_RESULT);
        Node node = new Node(Token.ADD);
        parent.addChildToFront(node);
        callback.trySimplify(parent, node);
        // assert something
    }

    @Test
    public void testGetSideEffectNodes() {
        Node node = new Node(Token.ADD);
        List<Node> sideEffectNodes = callback.getSideEffectNodes(node);
        // assert something
    }

    @Test
    public void testGetResult() {
        RemoveConstantExpressions.RemoveConstantRValuesCallback callback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        RemoveConstantExpressions.Result result = callback.getResult();
        // assert something
    }
}
