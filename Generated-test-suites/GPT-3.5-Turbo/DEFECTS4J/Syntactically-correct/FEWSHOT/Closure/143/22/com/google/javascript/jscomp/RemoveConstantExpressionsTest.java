import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RemoveConstantExpressionsTest {

    private RemoveConstantExpressions removeConstantExpressions;

    @Before
    public void setup() {
        removeConstantExpressions = new RemoveConstantExpressions();
    }

    @Test
    public void testProcess() {
        Node externs = createExterns();
        Node root = createRoot();

        removeConstantExpressions.process(externs, root);

        // assert something
    }

    @Test
    public void testTrySimplify_WhenNodeIsNotExprResult_DoNothing() {
        Node parent = createParent();
        Node node = createNonExprResultNode();

        removeConstantExpressions.trySimplify(parent, node);

        // assert nothing is changed
    }

    @Test
    public void testTrySimplify_WhenNodeHasSideEffects_DoNothing() {
        Node parent = createParent();
        Node node = createExprResultNodeWithSideEffects();

        removeConstantExpressions.trySimplify(parent, node);

        // assert nothing is changed
    }

    @Test
    public void testTrySimplify_WhenNodeHasNoSideEffects_ReplaceWithSideEffectNodes() {
        Node parent = createParent();
        Node node = createExprResultNodeWithNoSideEffects();

        removeConstantExpressions.trySimplify(parent, node);

        // assert that parent has the side effect nodes as children
    }

    @Test
    public void testGetSideEffectNodes() {
        Node node = createNode();

        List<Node> sideEffectNodes = removeConstantExpressions.getSideEffectNodes(node);

        // assert something
    }

    // helper methods to create test data
    private Node createExterns() {
        // TODO: create externs node
    }

    private Node createRoot() {
        // TODO: create root node
    }

    private Node createParent() {
        // TODO: create parent node
    }

    private Node createNonExprResultNode() {
        // TODO: create a non EXPR_RESULT node
    }

    private Node createExprResultNodeWithSideEffects() {
        // TODO: create an EXPR_RESULT node with side effects
    }

    private Node createExprResultNodeWithNoSideEffects() {
        // TODO: create an EXPR_RESULT node without side effects
    }

    private Node createNode() {
        // TODO: create a node
    }
}