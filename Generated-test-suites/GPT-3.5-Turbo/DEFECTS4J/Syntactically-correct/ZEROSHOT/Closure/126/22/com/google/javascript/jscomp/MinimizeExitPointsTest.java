import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MinimizeExitPointsTest {

    private MinimizeExitPoints minimizeExitPoints;

    @Before
    public void setUp() {
        minimizeExitPoints = new MinimizeExitPoints();
    }

    @Test
    public void testProcess() {
        // Test case using a simple AST with multiple exits
        Node externs = IR.root();
        Node root = IR.root(
                IR.block(
                        IR.ifNode(
                                IR.name("x"),
                                IR.returnNode(),
                                IR.block(
                                        IR.exprResult(
                                                IR.call(
                                                        IR.name("blah")
                                                )
                                        ),
                                        IR.exprResult(
                                                IR.call(
                                                        IR.name("foo")
                                                )
                                        )
                                )
                        )
                )
        );

        minimizeExitPoints.process(externs, root);

        // Assert that the AST has been transformed correctly
        Node expectedRoot = IR.root(
                IR.block(
                        IR.ifNode(
                                IR.name("x"),
                                IR.block(),
                                IR.block(
                                        IR.exprResult(
                                                IR.call(
                                                        IR.name("blah")
                                                )
                                        ),
                                        IR.exprResult(
                                                IR.call(
                                                        IR.name("foo")
                                                )
                                        )
                                )
                        )
                )
        );
        assertEquals(expectedRoot.toSource(), root.toSource());
    }

    @Test
    public void testVisit() {
        // Test case using a simple AST with a LABEL node
        Node externs = IR.root();
        Node root = IR.root(
                IR.block(
                        IR.labelName("myLabel"),
                        IR.block(
                                IR.breakNode("myLabel")
                        )
                )
        );

        minimizeExitPoints.process(externs, root);

        // Assert that the AST has been transformed correctly
        Node expectedRoot = IR.root(
                IR.block(
                        IR.labelName("myLabel"),
                        IR.block()
                )
        );
        assertEquals(expectedRoot.toSource(), root.toSource());
    }

    @Test
    public void testTryMinimizeExits() {
        // Test case using a simple AST with a FUNCTION node
        Node externs = IR.root();
        Node root = IR.root(
                IR.block(
                        IR.function(
                                IR.name("myFunction"),
                                IR.block(
                                        IR.returnNode()
                                )
                        )
                )
        );
        Node functionNode = ((Node) root.getFirstChild().getFirstChild());

        minimizeExitPoints.tryMinimizeExits(functionNode, Token.RETURN, null);

        // Assert that the AST has been transformed correctly
        Node expectedRoot = IR.root(
                IR.block(
                        IR.function(
                                IR.name("myFunction"),
                                IR.block()
                        )
                )
        );
        assertEquals(expectedRoot.toSource(), root.toSource());
    }

    @Test
    public void testMatchingExitNode() {
        // Test case for matching return node
        Node returnNode = IR.returnNode();

        boolean result = minimizeExitPoints.matchingExitNode(returnNode, Token.RETURN, null);

        assertFalse(result);
    }

    @Test
    public void testMoveAllFollowing() {
        // Test case for moving all following nodes
        Node srcParent = IR.block(
                IR.exprResult(
                        IR.number(1)
                ),
                IR.exprResult(
                        IR.number(2)
                )
        );
        Node destParent = IR.block(
                IR.exprResult(
                        IR.number(3)
                )
        );

        minimizeExitPoints.moveAllFollowing(srcParent.getFirstChild(), srcParent, destParent);

        // Assert that the nodes have been moved correctly
        Node expectedSrcParent = IR.block();
        Node expectedDestParent = IR.block(
                IR.exprResult(
                        IR.number(1)
                ),
                IR.exprResult(
                        IR.number(2)
                ),
                IR.exprResult(
                        IR.number(3)
                )
        );
        assertEquals(expectedSrcParent.toSource(), srcParent.toSource());
        assertEquals(expectedDestParent.toSource(), destParent.toSource());
    }

}