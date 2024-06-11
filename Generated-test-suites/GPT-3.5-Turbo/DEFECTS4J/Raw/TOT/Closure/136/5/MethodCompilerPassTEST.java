import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MethodCompilerPassTest {

    private MethodCompilerPass.SignatureStoreMock signatureStoreMock;
    private MethodCompilerPass methodCompilerPass;

    @Before
    public void setUp() {
        signatureStoreMock = new MethodCompilerPass.SignatureStoreMock();
        methodCompilerPass = new MethodCompilerPassImpl(new CompilerMock(), signatureStoreMock);
    }

    @Test
    public void testProcess_WithExternsAndRoot_CallsExpectedMethods() {
        Node externs = createExternsNode();
        Node root = createRootNode();

        methodCompilerPass.process(externs, root);

        Assert.assertEquals(2, signatureStoreMock.addSignatureCount);
        Assert.assertEquals(1, signatureStoreMock.removeSignatureCount);
        Assert.assertEquals(1, signatureStoreMock.resetCount);
    }

    @Test
    public void testGetActingCallback_ReturnsExpectedCallback() {
        MethodCompilerPass.Callback callback = methodCompilerPass.getActingCallback();

        Assert.assertNotNull(callback);
        Assert.assertTrue(callback instanceof ActingCallbackMock);
    }

    @Test
    public void testGetSignatureStore_ReturnsExpectedSignatureStore() {
        MethodCompilerPass.SignatureStore signatureStore = methodCompilerPass.getSignatureStore();

        Assert.assertNotNull(signatureStore);
        Assert.assertTrue(signatureStore instanceof SignatureStoreMock);
    }

    @Test
    public void testAddPossibleSignature_WithFunctionNode_AddsSignature() {
        Node functionNode = createFunctionNode();
        NodeTraversalMock nodeTraversalMock = new NodeTraversalMock();
        String functionName = "testFunction";

        methodCompilerPass.addPossibleSignature(functionName, functionNode, nodeTraversalMock);

        Assert.assertEquals(1, signatureStoreMock.addSignatureCount);
        Assert.assertEquals(functionName, signatureStoreMock.addedSignatureName);
        Assert.assertEquals(functionNode, signatureStoreMock.addedSignatureNode);
        Assert.assertEquals(nodeTraversalMock.getSourceName(), signatureStoreMock.addedSignatureSourceFile);
    }

    @Test
    public void testAddPossibleSignature_WithNameNode_AddsSignature() {
        Node nameNode = createNameNode();
        NodeTraversalMock nodeTraversalMock = new NodeTraversalMock();
        String functionName = "testFunction";

        methodCompilerPass.addPossibleSignature(functionName, nameNode, nodeTraversalMock);

        Assert.assertEquals(1, signatureStoreMock.addSignatureCount);
        Assert.assertEquals(functionName, signatureStoreMock.addedSignatureName);
        Assert.assertEquals(nameNode.getNext(), signatureStoreMock.addedSignatureNode);
        Assert.assertEquals(nodeTraversalMock.getSourceName(), signatureStoreMock.addedSignatureSourceFile);
    }

    @Test
    public void testAddPossibleSignature_WithNonFunctionNode_AddsToNonMethodProperties() {
        Node nonFunctionNode = createNonFunctionNode();
        NodeTraversalMock nodeTraversalMock = new NodeTraversalMock();
        String functionName = "testProperty";

        methodCompilerPass.addPossibleSignature(functionName, nonFunctionNode, nodeTraversalMock);

        Assert.assertEquals(0, signatureStoreMock.addSignatureCount);
        Assert.assertEquals(1, methodCompilerPass.nonMethodProperties.size());
        Assert.assertTrue(methodCompilerPass.nonMethodProperties.contains(functionName));
    }

    @Test
    public void testProcess_WithoutExterns_CallsExpectedMethods() {
        Node root = createRootNode();

        methodCompilerPass.process(null, root);

        Assert.assertEquals(0, signatureStoreMock.addSignatureCount);
        Assert.assertEquals(0, signatureStoreMock.removeSignatureCount);
        Assert.assertEquals(1, signatureStoreMock.resetCount);
    }

    @Test
    public void testProcess_WithoutExternsAndRoot_DoesNotThrowException() {
        Assert.doesNotThrow(() -> methodCompilerPass.process(null, null));
    }

    private Node createExternsNode() {
        // Create mock externs Node for test
        return new Node(Token.ROOT);
    }

    private Node createRootNode() {
        // Create mock root Node for test
        return new Node(Token.ROOT);
    }

    private Node createFunctionNode() {
        // Create mock function Node for test
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.setInputName("mock_input.js");

        return functionNode;
    }

    private Node createNameNode() {
        // Create mock name Node for test
        Node nameNode = new Node(Token.NAME, "test", null);
        nameNode.setInputName("mock_input.js");

        return nameNode;
    }

    private Node createNonFunctionNode() {
        // Create mock non-function Node for test
        Node nonFunctionNode = new Node(Token.STRING, "property", null);
        nonFunctionNode.setInputName("mock_input.js");

        return nonFunctionNode;
    }

    private static class MethodCompilerPassImpl extends MethodCompilerPass {

        private SignatureStore signatureStore;

        public MethodCompilerPassImpl(AbstractCompiler compiler, SignatureStore signatureStore) {
            super(compiler);
            this.signatureStore = signatureStore;
        }

        @Override
        Callback getActingCallback() {
            return new ActingCallbackMock();
        }

        @Override
        SignatureStore getSignatureStore() {
            return signatureStore;
        }
    }

    private static class CompilerMock extends AbstractCompiler {

        // override necessary methods of AbstractCompiler for the test
    }

    private static class SignatureStoreMock implements MethodCompilerPass.SignatureStore {

        private int addSignatureCount = 0;
        private int removeSignatureCount = 0;
        private int resetCount = 0;
        private String addedSignatureName;
        private Node addedSignatureNode;
        private String addedSignatureSourceFile;

        @Override
        public void reset() {
            resetCount++;
        }

        @Override
        public void addSignature(String functionName, Node functionNode, String sourceFile) {
            addSignatureCount++;
            addedSignatureName = functionName;
            addedSignatureNode = functionNode;
            addedSignatureSourceFile = sourceFile;
        }

        @Override
        public void removeSignature(String functionName) {
            removeSignatureCount++;
        }
    }

    private static class NodeTraversalMock extends NodeTraversal {

        @Override
        public String getSourceName() {
            return "mock_input.js";
        }

        // override necessary methods of NodeTraversal for the test
    }

    private static class ActingCallbackMock extends MethodCompilerPass.Callback {

        @Override
        public void visit(NodeTraversal t, Node n, Node parent) {
            // perform necessary actions for the test
        }

        // override necessary methods of Callback for the test
    }
}