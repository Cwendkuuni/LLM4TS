
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;

public class RuntimeTypeCheckTest {

    private static class TestCompiler extends AbstractCompiler {

        @Override
        public void reportCodeChange() {
            // Do nothing
        }
    }

    private static class TestCompilerPass implements CompilerPass {
        private boolean processCalled = false;

        @Override
        public void process(Node externs, Node root) {
            processCalled = true;
        }

        public boolean isProcessCalled() {
            return processCalled;
        }
    }

    private RuntimeTypeCheck runtimeTypeCheck;
    private TestCompiler compiler;
    private TestCompilerPass compilerPass;

    @Before
    public void setUp() {
        compiler = new TestCompiler();
        runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
        compilerPass = new TestCompilerPass();
    }

    @Test
    public void testProcess() {
        Node externs = new Node(Token.EMPTY);
        Node root = new Node(Token.EMPTY);

        runtimeTypeCheck.process(externs, root);

        assertEquals(2, root.getChildCount());
        assertEquals(NodeTraversal.CallbackChange.CONTINUE,
                root.getLastChild().getChange());

        NodeTraversal.traverse(compiler, root.getLastChild(), compilerPass);

        assertEquals(true, compilerPass.isProcessCalled());
    }
}
