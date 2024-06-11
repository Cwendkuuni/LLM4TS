
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CollapseVariableDeclarations;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CollapseVariableDeclarationsTest {

    private CollapseVariableDeclarations collapseVariableDeclarations;

    @Before
    public void setUp() {
        AbstractCompiler compiler = new AbstractCompiler() {
            @Override
            public void reportCodeChange() {
                // Do nothing
            }
        };
        collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
    }

    @Test
    public void testProcess_noCollapses() {
        // Create AST nodes for test
        Node externs = new Node(Token.ROOT);
        Node root = new Node(Token.ROOT);

        // Process the nodes
        collapseVariableDeclarations.process(externs, root);

        // Assert no collapses
        assertEquals(0, collapseVariableDeclarations.collapses.size());
    }

    @Test
    public void testProcess_collapseVariableDeclarations() {
        // Create AST nodes for test
        Node externs = new Node(Token.ROOT);
        Node root = createTestAST();

        // Process the nodes
        collapseVariableDeclarations.process(externs, root);

        // Assert collapses
        assertEquals(2, collapseVariableDeclarations.collapses.size());

        // Verify the first collapse
        CollapseVariableDeclarations.Collapse collapse1 = collapseVariableDeclarations.collapses.get(0);
        assertEquals(Token.VAR, collapse1.startNode.getToken());
        assertEquals(Token.EXPR_RESULT, collapse1.startNode.getNext().getToken());
        assertEquals(3, collapse1.startNode.getNext().getChildCount());
        assertEquals(Token.VAR, collapse1.endNode.getToken());
        assertEquals(Token.EXPR_RESULT, collapse1.endNode.getNext().getToken());
        assertEquals(1, collapse1.endNode.getNext().getChildCount());

        // Verify the second collapse
        CollapseVariableDeclarations.Collapse collapse2 = collapseVariableDeclarations.collapses.get(1);
        assertEquals(Token.VAR, collapse2.startNode.getToken());
        assertEquals(Token.EXPR_RESULT, collapse2.startNode.getNext().getToken());
        assertEquals(1, collapse2.startNode.getNext().getChildCount());
        assertEquals(Token.VAR, collapse2.endNode.getToken());
        assertEquals(Token.EXPR_RESULT, collapse2.endNode.getNext().getToken());
        assertEquals(1, collapse2.endNode.getNext().getChildCount());
    }

    private Node createTestAST() {
        Node var1 = IR.var(IR.name("a"));
        Node var2 = IR.var(IR.name("b"), IR.number(1));
        Node var3 = IR.var(IR.name("c"), IR.number(2));

        Node expr1 = IR.exprResult(IR.assign(IR.name("a"), IR.trueNode()));
        Node expr2 = IR.exprResult(IR.assign(IR.name("b"), IR.trueNode()));
        Node expr3 = IR.exprResult(IR.var(IR.name("c"), IR.trueNode()));

        Node root = new Node(Token.BLOCK, var1, var2, var3, expr1, expr2, expr3);
        return root;
    }
}
