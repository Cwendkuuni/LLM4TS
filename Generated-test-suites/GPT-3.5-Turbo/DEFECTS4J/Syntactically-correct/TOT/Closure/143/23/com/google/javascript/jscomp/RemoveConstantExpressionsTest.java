import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.Token;

@RunWith(MockitoJUnitRunner.class)
public class RemoveConstantExpressionsTest {

    @Mock
    private AbstractCompiler compiler;

    private RemoveConstantExpressions.RemoveConstantRValuesCallback removeConstantRValuesCallback;
    private RemoveConstantExpressions removeConstantExpressions;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        removeConstantExpressions = new RemoveConstantExpressions(compiler);
        removeConstantRValuesCallback = removeConstantExpressions.new RemoveConstantRValuesCallback();
    }

    @Test
    public void testTrySimplify_WhenNodeTypeIsNotEXPR_RESULT_ShouldNotReplaceNode() {
        Node node = new Node(Token.NAME);
        Node parent = new Node(Token.ADD);

        removeConstantRValuesCallback.trySimplify(parent, node);

        Mockito.verifyZeroInteractions(compiler);
    }

    @Test
    public void testTrySimplify_WhenExprBodyHasNoSideEffects_ShouldReplaceNodeWithSideEffectNodes() {
        Node node = new Node(Token.EXPR_RESULT);
        Node parent = new Node(Token.ADD);

        Node exprBody = new Node(Token.ADD, new Node(Token.NUMBER, "1"), new Node(Token.NUMBER, "2"));
        node.addChildToFront(exprBody);

        removeConstantRValuesCallback.trySimplify(parent, node);

        Mockito.verify(compiler).reportCodeChange();
    }

    @Test
    public void testTrySimplify_WhenExprBodyHasSideEffects_ShouldNotReplaceNode() {
        Node node = new Node(Token.EXPR_RESULT);
        Node parent = new Node(Token.ADD);

        Node exprBody = new Node(Token.CALL, new Node(Token.NAME, "foo"));
        node.addChildToFront(exprBody);

        removeConstantRValuesCallback.trySimplify(parent, node);

        Mockito.verifyZeroInteractions(compiler);
    }

    @Test
    public void testGetSideEffectNodes_WhenNodeContainsMultipleSubexpressions_ShouldReturnListOfReplacementNodes() {
        Node node = new Node(Token.ADD, new Node(Token.NUMBER, "1"), new Node(Token.NUMBER, "2"));

        List<Node> result = removeConstantRValuesCallback.getSideEffectNodes(node);

        Assert.assertEquals(2, result.size());
        Assert.assertEquals(Token.EXPR_RESULT, result.get(0).getType());
        Assert.assertEquals(Token.NUMBER, result.get(0).getFirstChild().getType());
        Assert.assertEquals(Token.EXPR_RESULT, result.get(1).getType());
        Assert.assertEquals(Token.NUMBER, result.get(1).getFirstChild().getType());
    }

}