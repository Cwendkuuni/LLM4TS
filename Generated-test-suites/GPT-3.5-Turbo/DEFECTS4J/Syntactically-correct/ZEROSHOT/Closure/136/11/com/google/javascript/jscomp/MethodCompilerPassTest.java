import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MethodCompilerPassTest {
    private MethodCompilerPass methodCompilerPass;

    @Before
    public void setUp() {
        methodCompilerPass = new MethodCompilerPass(null) {
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
    public void testProcess() {
        Node externs = null;
        Node root = null;

        methodCompilerPass.process(externs, root);
    }

    @Test
    public void testGetExternMethods() {
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;

        MethodCompilerPass.GetExternMethods getExternMethods = methodCompilerPass.new GetExternMethods();
        getExternMethods.visit(t, n, parent);
    }

    @Test
    public void testGatherSignatures() {
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;

        MethodCompilerPass.GatherSignatures gatherSignatures = methodCompilerPass.new GatherSignatures();
        gatherSignatures.visit(t, n, parent);
    }
}