import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.Set;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class MethodCompilerPassTest {

    private MethodCompilerPass methodCompilerPass;

    @Before
    public void setUp() {
        methodCompilerPass = new MethodCompilerPass() {
            public Callback getActingCallback() {
                return null;
            }
            public SignatureStore getSignatureStore() {
                return null;
            }
        };
    }

    @Test
    public void testProcess() {
        Node externs = new Node(Token.ROOT);
        Node root = new Node(Token.ROOT);
        methodCompilerPass.process(externs, root);
        // Add assertions here
    }

    @Test
    public void testAddPossibleSignature() {
        String name = "testName";
        Node node = new Node(Token.FUNCTION);
        NodeTraversal t = new NodeTraversal();
        methodCompilerPass.addPossibleSignature(name, node, t);
        // Add assertions here
    }

    @Test
    public void testAddSignature() {
        String name = "testName";
        Node function = new Node(Token.FUNCTION);
        String fnSourceName = "testFnSourceName";
        methodCompilerPass.addSignature(name, function, fnSourceName);
        // Add assertions here
    }

    @Test
    public void testGetActingCallback() {
        Callback callback = methodCompilerPass.getActingCallback();
        // Add assertions here
    }

    @Test
    public void testGetSignatureStore() {
        SignatureStore store = methodCompilerPass.getSignatureStore();
        // Add assertions here
    }

    @Test
    public void testGetExternMethods() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.ROOT);
        Node parent = new Node(Token.ROOT);
        MethodCompilerPass.GetExternMethods externMethods = methodCompilerPass.new GetExternMethods();
        externMethods.visit(t, n, parent);
        // Add assertions here
    }

    @Test
    public void testGatherSignatures() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.ROOT);
        Node parent = new Node(Token.ROOT);
        MethodCompilerPass.GatherSignatures gatherSignatures = methodCompilerPass.new GatherSignatures();
        gatherSignatures.visit(t, n, parent);
        // Add assertions here
    }
}