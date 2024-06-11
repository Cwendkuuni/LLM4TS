import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.ControlFlowGraph;
import com.google.javascript.jscomp.graph.DiGraph;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MustBeReachingVariableDefTest {

    private ControlFlowGraph<Node> cfg;
    private Scope jsScope;
    private MustBeReachingVariableDef analysis;

    @Before
    public void setup() {
        DiGraph<Node, ControlFlowGraph.Branch> graph = new DiGraph<>();
        cfg = new ControlFlowGraph<>(graph, null);
        jsScope = new Scope(new Node(Token.SCRIPT), null);
        analysis = new MustBeReachingVariableDef(cfg, jsScope, null);
    }

    @Test
    public void getDef_ExistingVariable_ReturnsDefinition() {
        // Arrange
        Node defNode = new Node(Token.EXPR_RESULT);
        defNode.addChildToBack(new Node(Token.ASSIGN));

        jsScope.declare("varName", defNode.getFirstChild());

        // Act
        Node result = analysis.getDef("varName", defNode);

        // Assert
        Assert.assertEquals(defNode.getFirstChild(), result);
    }

    @Test
    public void getDef_NonexistentVariable_ReturnsNull() {
        // Arrange
        Node defNode = new Node(Token.EXPR_RESULT);
        defNode.addChildToBack(new Node(Token.ASSIGN));

        // Act
        Node result = analysis.getDef("varName", defNode);

        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void dependsOnOuterScopeVars_VariableHasNoDependencies_ReturnsFalse() {
        // Arrange
        Node defNode = new Node(Token.EXPR_RESULT);
        defNode.addChildToBack(new Node(Token.ASSIGN));

        jsScope.declare("varName", defNode.getFirstChild());

        // Act
        boolean result = analysis.dependsOnOuterScopeVars("varName", defNode);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void dependsOnOuterScopeVars_VariableHasDependencies_ReturnsTrue() {
        // Arrange
        Node defNode = new Node(Token.EXPR_RESULT);
        defNode.addChildToBack(new Node(Token.ASSIGN));
        Node dependencyNode = new Node(Token.NAME, "dependency");

        jsScope.declare("varName", defNode.getFirstChild());
        jsScope.getParent().declare("dependency", dependencyNode);

        // Act
        boolean result = analysis.dependsOnOuterScopeVars("varName", defNode);

        // Assert
        Assert.assertTrue(result);
    }

}