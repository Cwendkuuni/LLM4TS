import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.*;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSDocInfoBuilder;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import java.util.List;

public class CheckSideEffectsTest {

    private CheckSideEffects checkSideEffects;
    private AbstractCompiler compiler;
    private Node externs;
    private Node root;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        externs = new Node(Token.ROOT);
        root = new Node(Token.ROOT);
    }

    @Test
    public void testProcess() {
        checkSideEffects.process(externs, root);
    }

    @Test
    public void testHotSwapScript() {
        Node scriptRoot = new Node(Token.SCRIPT);
        Node originalRoot = new Node(Token.SCRIPT);
        checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
    }

    @Test
    public void testVisit() {
        NodeTraversal traversal = new NodeTraversal(compiler, checkSideEffects);
        NodeTraversal.Traverser traverser = new NodeTraversal.Traverser(traversal);
        Node n = new Node(Token.VOID);
        Node parent = new Node(Token.EXPR_RESULT);
        traverser.traverse(n, parent);
    }
}