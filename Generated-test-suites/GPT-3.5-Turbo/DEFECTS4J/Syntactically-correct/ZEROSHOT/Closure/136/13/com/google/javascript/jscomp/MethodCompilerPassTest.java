import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import java.util.List;
import java.util.Set;

public class MethodCompilerPassTest {

    private MethodCompilerPass instance;
    private AbstractCompiler compiler;
    private AbstractPostOrderCallback postOrderCallback;
    private Callback callback;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        instance = new MethodCompilerPass(compiler) {

            @Override
            Callback getActingCallback() {
                return callback;
            }

            @Override
            SignatureStore getSignatureStore() {
                return null;
            }
        };
        postOrderCallback = instance.new GatherSignatures();
        callback = new Callback() {
            @Override
            public boolean shouldTraverse(NodeTraversal arg0, Node arg1, Node arg2) {
                return true;
            }
            @Override
            public void visit(NodeTraversal arg0, Node arg1, Node arg2) {
                // implement visit method
            }
        };
    }

    @After
    public void tearDown() {
        instance = null;
        compiler = null;
        postOrderCallback = null;
        callback = null;
    }

    @Test
    public void testProcess() {
        Node externs = null;
        Node root = null;
        MethodCompilerPass instance = new MethodCompilerPass(compiler);
        instance.process(externs, root);
        // add assertion here
    }

    @Test
    public void testGetActingCallback() {
        Callback result = instance.getActingCallback();
        // add assertion here
    }

    @Test
    public void testGetSignatureStore() {
        SignatureStore result = instance.getSignatureStore();
        // add assertion here
    }

    @Test
    public void testAddPossibleSignature() {
        String name = "";
        Node node = null;
        NodeTraversal t = null;
        instance.addPossibleSignature(name, node, t);
        // add assertion here
    }

    @Test
    public void testAddSignature() {
        String name = "";
        Node function = null;
        String fnSourceName = "";
        instance.addSignature(name, function, fnSourceName);
        // add assertion here
    }

    @Test
    public void testVisit() {
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;
        instance.visit(t, n, parent);
        // add assertion here
    }

    @Test
    public void testGetExternMethodsVisit() {
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;
        instance.new GetExternMethods().visit(t, n, parent);
        // add assertion here
    }

    @Test
    public void testGatherSignaturesVisit() {
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;
        instance.new GatherSignatures().visit(t, n, parent);
        // add assertion here
    }

    @Test
    public void testProcessPrototypeParent() {
        NodeTraversal t = null;
        Node n = null;
        instance.new GatherSignatures().processPrototypeParent(t, n);
        // add assertion here
    }

}