import org.junit.Test;
import static org.junit.Assert.*;

public class MethodCompilerPassTest {

    @Test
    public void testProcess() {
        MethodCompilerPass pass = new MethodCompilerPassMock();
        pass.process(null, null);
    }

    @Test
    public void testGetActingCallback() {
        MethodCompilerPass pass = new MethodCompilerPassMock();
        MethodCompilerPass.SignatureStore store = pass.getSignatureStore();
        pass.getActingCallback();
    }

    @Test
    public void testGetSignatureStore() {
        MethodCompilerPass pass = new MethodCompilerPassMock();
        MethodCompilerPass.SignatureStore store = pass.getSignatureStore();
    }

    @Test
    public void testAddPossibleSignature() {
        MethodCompilerPass pass = new MethodCompilerPassMock();
        MethodCompilerPass.SignatureStore store = pass.getSignatureStore();
        pass.addPossibleSignature("test", null, null);
    }

    @Test
    public void testAddSignature() {
        MethodCompilerPass pass = new MethodCompilerPassMock();
        MethodCompilerPass.SignatureStore store = pass.getSignatureStore();
        pass.addSignature("test", null, null);
    }

    @Test
    public void testGetExternMethods() {
        MethodCompilerPass pass = new MethodCompilerPassMock();
        MethodCompilerPass.GetExternMethods callback = pass.new GetExternMethods();
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;
        callback.visit(t, n, parent);
    }

    @Test
    public void testGatherSignatures() {
        MethodCompilerPass pass = new MethodCompilerPassMock();
        MethodCompilerPass.GatherSignatures callback = pass.new GatherSignatures();
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;
        callback.visit(t, n, parent);
    }

    private class MethodCompilerPassMock extends MethodCompilerPass {
        public MethodCompilerPassMock() {
            super(null);
        }

        @Override
        Callback getActingCallback() {
            return null;
        }

        @Override
        SignatureStore getSignatureStore() {
            return null;
        }
    }
}