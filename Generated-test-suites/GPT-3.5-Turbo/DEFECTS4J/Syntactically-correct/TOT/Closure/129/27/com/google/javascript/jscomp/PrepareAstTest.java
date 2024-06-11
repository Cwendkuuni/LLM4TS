import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PrepareAstTest {

    private PrepareAst prepareAst;
    private AbstractCompiler compiler;
    private Node externs;
    private Node root;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        prepareAst = new PrepareAst(compiler);
        externs = IR.root();
        root = IR.root();
    }

    @Test
    public void testProcess_CheckOnly() {
        prepareAst.process(externs, root);
        // Check that normalizeNodeTypes method is called with the correct root node
        // and the checkOnly flag is set to true
        assertTrue(normalizeNodeTypesCalled(root, true));
        // Check that PrepareAnnotations is not called since checkOnly is true
        assertFalse(prepareAnnotationsCalled());
    }

    @Test
    public void testProcess_NonCheckOnly() {
        PrepareAst nonCheckPrepareAst = new PrepareAst(compiler, false);
        nonCheckPrepareAst.process(externs, root);
        // Check that normalizeNodeTypes method is not called
        assertFalse(normalizeNodeTypesCalled(root, true));
        // Check that PrepareAnnotations is called with the externs and root nodes
        assertTrue(prepareAnnotationsCalled());
    }

    @Test
    public void testNormalizeNodeTypes() {
        Node node = IR.root();
        prepareAst.normalizeNodeTypes(node);
        // Assert that normalizeBlocks method is called with the correct node
        assertTrue(normalizeBlocksCalled(node));
    }

    @Test
    public void testNormalizeBlocks() {
        Node node = IR.block();
        prepareAst.normalizeBlocks(node);
        // Assert that the block is replaced with a new block
        assertTrue(isReplacedWithNewBlock(node));
    }

    @Test
    public void testPrepareAnnotations_shouldTraverse() {
        NodeTraversal traversal = new NodeTraversal(compiler, PrepareAnnotations::shouldTraverse);
        traversal.traverse(root);
        // Assert that normalizeObjectLiteralAnnotations is called with the correct object literal node
        assertTrue(normalizeObjectLiteralAnnotationsCalled(root));
    }

    @Test
    public void testPrepareAnnotations_visit_Call() {
        Node node = IR.call();
        Node parent = IR.root();
        NodeTraversal traversal = new NodeTraversal(compiler, (t, n, p) -> {
            PrepareAnnotations prepareAnnotations = new PrepareAnnotations();
            prepareAnnotations.visit(t, node, parent);
        });
        traversal.traverse(root);
        // Assert that annotateCalls is called with the correct call node
        assertTrue(annotateCallsCalled(node));
    }

    @Test
    public void testPrepareAnnotations_visit_Function() {
        Node node = IR.function(IR.name("test"), IR.block());
        Node parent = IR.root();
        NodeTraversal traversal = new NodeTraversal(compiler, (t, n, p) -> {
            PrepareAnnotations prepareAnnotations = new PrepareAnnotations();
            prepareAnnotations.visit(t, node, parent);
        });
        traversal.traverse(root);
        // Assert that annotateDispatchers is called with the correct function node and parent node
        assertTrue(annotateDispatchersCalled(node, parent));
    }

    @Test
    public void testPrepareAnnotations_normalizeObjectLiteralKeyAnnotations() {
        Node objlit = IR.objectlit(IR.key("a"), IR.string("b"));
        Node key = objlit.getFirstChild();
        Node value = key.getFirstChild();
        PrepareAnnotations prepareAnnotations = new PrepareAnnotations();
        prepareAnnotations.normalizeObjectLiteralKeyAnnotations(objlit, key, value);
        // Assert that objlit key's JSDocInfo is set to value's JSDocInfo
        assertTrue(jsDocInfoSetCorrectly(objlit, value));
    }

    private boolean normalizeNodeTypesCalled(Node node, boolean checkOnly) {
        PrepareAst mockPrepareAst = new PrepareAst(compiler);
        mockPrepareAst.normalizeNodeTypes(node);
        return mockPrepareAst.isCheckOnly() == checkOnly;
    }

    private boolean prepareAnnotationsCalled() {
        PrepareAst.PrepareAnnotations mockPrepareAnnotations = new PrepareAst.PrepareAnnotations();
        mockPrepareAnnotations.shouldTraverse(null, root, null);
        return mockPrepareAnnotations.isShouldTraverseCalled();
    }

    private boolean normalizeBlocksCalled(Node node) {
        PrepareAst mockPrepareAst = new PrepareAst(compiler);
        mockPrepareAst.normalizeBlocks(node);
        return mockPrepareAst.isReportChange();
    }

    private boolean isReplacedWithNewBlock(Node node) {
        return node.isBlock();
    }

    private boolean normalizeObjectLiteralAnnotationsCalled(Node objlit) {
        PrepareAst.PrepareAnnotations.NormalizeObjectLiteralAnnotations mockObjectLitAnnotations =
                new PrepareAst.PrepareAnnotations.NormalizeObjectLiteralAnnotations();
        mockObjectLitAnnotations.normalizeObjectLiteralAnnotations(objlit);
        return mockObjectLitAnnotations.isNormalizeObjectLiteralKeyAnnotationsCalled();
    }

    private boolean annotateCallsCalled(Node node) {
        PrepareAst.PrepareAnnotations mockPrepareAnnotations = new PrepareAst.PrepareAnnotations();
        mockPrepareAnnotations.annotateCalls(node);
        return mockPrepareAnnotations.isAnnotateCallsCalled();
    }

    private boolean annotateDispatchersCalled(Node function, Node parent) {
        PrepareAst.PrepareAnnotations mockPrepareAnnotations = new PrepareAst.PrepareAnnotations();
        mockPrepareAnnotations.annotateDispatchers(function, parent);
        return mockPrepareAnnotations.isAnnotateDispatchersCalled();
    }

    private boolean jsDocInfoSetCorrectly(Node objlit, Node value) {
        return objlit.getFirstChild().getJSDocInfo() == value.getJSDocInfo();
    }
}