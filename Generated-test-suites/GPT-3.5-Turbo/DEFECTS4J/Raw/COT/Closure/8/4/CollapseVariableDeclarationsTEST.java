
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.Token;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

public class CollapseVariableDeclarationsTest {

    private CollapseVariableDeclarations collapseVariableDeclarations;

    @Before
    public void setUp() {
        this.collapseVariableDeclarations = new CollapseVariableDeclarations();
    }

    @Test
    public void testCollapseVariableDeclarations() {
        Node startNode = new Node(Token.VAR);
        Node endNode = new Node(Token.VAR);
        Node parent = new Node(Token.VAR);
        CollapseVariableDeclarations.Collapse collapse = new CollapseVariableDeclarations.Collapse(startNode, endNode, parent);

        collapseVariableDeclarations.applyCollapses();

        Assert.assertNotNull(collapseVariableDeclarations);
        Assert.assertEquals(startNode, collapse.startNode);
        Assert.assertEquals(endNode, collapse.endNode);
        Assert.assertEquals(parent, collapse.parent);
    }

    @Test
    public void testGatherCollapses() {
        GatherCollapses gatherCollapses = new GatherCollapses();
        NodeTraversal t = new NodeTraversal();
        Node node = new Node(Token.VAR);
        Node parent = new Node(Token.VAR);

        gatherCollapses.visit(t, node, parent);

        Assert.assertNotNull(gatherCollapses);
        Assert.assertEquals(node, gatherCollapses.n);
        Assert.assertEquals(parent, gatherCollapses.parent);
    }

    @Test
    public void testCanBeRedeclared() {
        GatherCollapses gatherCollapses = new GatherCollapses();
        Node node = new Node(Token.VAR);
        Scope s = new Scope();
        Node assign = new Node(Token.VAR);
        Node lhs = new Node("lhs");
        Node rhs = new Node("rhs");

        assign.addChildToBack(lhs);
        assign.addChildToBack(rhs);
        node.addChildToBack(assign);

        boolean result = gatherCollapses.canBeRedeclared(node, s);

        Assert.assertTrue(result);
        Assert.assertEquals(lhs, gatherCollapses.lhs);
        Assert.assertEquals(rhs, gatherCollapses.rhs);
    }
}
