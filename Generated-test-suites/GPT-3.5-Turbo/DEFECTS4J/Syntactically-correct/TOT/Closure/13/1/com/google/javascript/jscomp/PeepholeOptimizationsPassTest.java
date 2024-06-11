import com.google.javascript.jscomp.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PeepholeOptimizationsPassTest {
    private PeepholeOptimizationsPass pass;

    @Before
    public void setUp() {
        AbstractCompiler compiler = new Compiler();
        AbstractPeepholeOptimization[] optimizations = {new ConstantFolding()};
        pass = new PeepholeOptimizationsPass(compiler, optimizations);
    }

    @Test
    public void process_WithEmptyAST_NoChangeReported() {
        Node root = new Node(Token.EMPTY);
        Node externs = new Node(Token.EMPTY);

        pass.process(externs, root);

        assertFalse(pass.getCompiler().hasHaltingErrors());
    }

    @Test
    public void shouldVisit_FunctionNode_ReturnsTrue() {
        Node functionNode = IR.function(IR.functionSignature(), IR.block());

        boolean result = pass.shouldVisit(functionNode);

        assertTrue(result);
    }

    @Test
    public void shouldVisit_FunctionNodeInScopeWithNoTraverseChildScopes_ReturnsFalse() {
        Node functionNode = IR.function(IR.functionSignature(), IR.block());

        pass.shouldVisit(functionNode);
        pass.shouldRetraverse(functionNode);

        boolean result = pass.shouldVisit(functionNode);

        assertFalse(result);
    }

    @Test
    public void shouldRetraverse_ScriptNode_ChangedTriggersRetraversal() {
        Node scriptNode = IR.script();

        pass.shouldVisit(scriptNode);
        pass.visit(scriptNode);

        boolean result = pass.shouldRetraverse(scriptNode);

        assertTrue(result);
    }

    @Test
    public void shouldRetraverse_ScopeNodeWithNoChange_NoRetraversal() {
        Node scopeNode = IR.script();

        boolean result = pass.shouldRetraverse(scopeNode);

        assertFalse(result);
    }

    @Test
    public void visit_OptimizationModifiesNode_NodeReplacedWithNewVersion() {
        Node expressionNode = IR.exprResult(IR.name("x"), IR.assign(IR.name("y")));
        Node optimizedExpressionNode = IR.exprResult(IR.name("x"), IR.assign(IR.string("abc")));

        AbstractPeepholeOptimization optimization = new TestOptimization(optimizedExpressionNode);
        pass = new PeepholeOptimizationsPass(pass.getCompiler(), optimization);

        pass.visit(expressionNode);

        assertEquals(optimizedExpressionNode, expressionNode);
    }

    @Test
    public void visit_OptimizationDoesNotModifyNode_NodeRemainsTheSame() {
        Node expressionNode = IR.exprResult(IR.name("x"));
        Node optimizedExpressionNode = expressionNode.cloneTree();

        AbstractPeepholeOptimization optimization = new TestOptimization(optimizedExpressionNode);
        pass = new PeepholeOptimizationsPass(pass.getCompiler(), optimization);

        pass.visit(expressionNode);

        assertEquals(optimizedExpressionNode, expressionNode);
    }

    @Test
    public void visit_OptimizationOptimizesSubtree_NodeChangedInSubtreeOnly() {
        Node assignmentNode = IR.assign(IR.name("x"), IR.number(5));
        Node expressionNode = IR.exprResult(IR.add(assignmentNode, IR.number(3)));
        Node optimizedAssignmentNode = IR.assign(IR.name("x"), IR.number(7));
        Node optimizedExpressionNode = IR.exprResult(IR.add(optimizedAssignmentNode, IR.number(3)));

        AbstractPeepholeOptimization optimization = new TestOptimization(optimizedAssignmentNode);
        pass = new PeepholeOptimizationsPass(pass.getCompiler(), optimization);

        pass.visit(expressionNode);

        assertEquals(optimizedExpressionNode, expressionNode);
    }

    private class TestOptimization extends AbstractPeepholeOptimization {
        private Node optimizedNode;

        TestOptimization(Node optimizedNode) {
            this.optimizedNode = optimizedNode;
        }

        @Override
        Node optimizeSubtree(Node n) {
            return optimizedNode;
        }
    }
}