import static com.google.javascript.jscomp.NodeTraversal.traverse;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AstChangeProxy;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Result;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class RemoveConstantExpressionsTest {
    @Mock
    private AbstractCompiler compiler;

    @Mock
    private NodeFactory nodeFactory;

    @InjectMocks
    private RemoveConstantRValuesCallback removeConstantRValuesCallback;

    @Before
    public void setUp() {
        when(compiler.getFactory()).thenReturn(nodeFactory);
    }

    @Test
    public void testTrySimplify() {
        Node root = new Node(Token.PROGRAM, Lists.newArrayList());
        Node exprBody = new Node(Token.NUMBER, 1);

        Node parent = new Node(Token.EXPR_STATEMENT, Lists.newArrayList(
            exprBody
        ));

        Node node = new Node(Token.EXPR_RESULT, Lists.newArrayList(
            parent
        ));

        Arguments arguments = Arguments.of(null, root, node);
        ArgumentCaptor<AstChangeProxy> changeProxyArgumentCaptor = ArgumentCaptor.forClass(AstChangeProxy.class);

        removeConstantRValuesCallback.trySimplify(null, node);

        // Verify that AstChangeProxy is called with the expected arguments
        removeConstantRValuesCallback.getChangeProxy().assertChanged(arguments);

        // Verify that the replaceWith method is called with the expected arguments
        removeConstantRValuesCallback.getChangeProxy().assertChangedNodes(changeProxyArgumentCaptor.capture(), Lists.newArrayList(parent));
    }
}