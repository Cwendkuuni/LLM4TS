import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PrepareAstTest {

    private PrepareAst prepareAst;

    @Before
    public void setUp() {
        prepareAst = new PrepareAst(new AbstractCompiler());
    }

    @Test
    public void testNormalizeNodeTypes() {
        Node root = IR.root();
        Node externs = IR.root();
        prepareAst.process(externs, root);
        // Add assertions here to test the functionality of normalizeNodeTypes()
    }

    @Test
    public void testNormalizeBlocks() {
        Node node = IR.block();
        prepareAst.normalizeBlocks(node);
        // Add assertions here to test the functionality of normalizeBlocks()
    }

    @Test
    public void testPrepareAnnotations() {
        PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
        NodeTraversal traversal = new NodeTraversal(new AbstractCompiler(), prepareAnnotations);
        Node root = IR.root();
        traversal.traverse(root);
        // Add assertions here to test the functionality of prepareAnnotations()
    }
}