
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;

public class CollapseVariableDeclarationsTest {

    private CollapseVariableDeclarations collapseVariableDeclarations;

    @Before
    public void setUp() {
        collapseVariableDeclarations = new CollapseVariableDeclarations();
    }

    @Test
    public void testCollapseVariableDeclarations() {
        Node startNode = Mockito.mock(Node.class);
        Node endNode = Mockito.mock(Node.class);
        Node parent = Mockito.mock(Node.class);
        
        CollapseVariableDeclarations.Collapse collapse = new CollapseVariableDeclarations.Collapse(startNode, endNode, parent);
        
        collapseVariableDeclarations.applyCollapses();
        
        assertTrue(collapseVariableDeclarations.collapses.isEmpty());
        
        Mockito.verify(parent).addChildBefore(Mockito.any(), Mockito.eq(startNode));
        Mockito.verify(parent).removeChildAfter(Mockito.eq(collapseVariableDeclarations.var));
        Mockito.verify(startNode, Mockito.times(2)).isVar();
        Mockito.verify(startNode, Mockito.times(2)).getNext();
        Mockito.verify(startNode, Mockito.times(0)).getFirstChild();
        Mockito.verify(startNode, Mockito.times(2)).getFirstChild();
        Mockito.verify(endNode, Mockito.times(2)).getNext();
        Mockito.verify(collapseVariableDeclarations.var).setJSDocInfo(Mockito.any());
    }

    @Test
    public void testGatherCollapses() {
        NodeTraversal t = Mockito.mock(NodeTraversal.class);
        Node n = Mockito.mock(Node.class);
        Node parent = Mockito.mock(Node.class);
        
        collapseVariableDeclarations.blacklistStubVars(t, n);
        
        assertFalse(collapseVariableDeclarations.canBeRedeclared(n));
        
        collapseVariableDeclarations.visit(t, n, parent);
        
        Mockito.verify(n).isVar();
        Mockito.verify(n).getNext();
        Mockito.verify(parent, Mockito.never()).isIf();
        Mockito.verify(n, Mockito.times(2)).isVar();
        Mockito.verify(parent, Mockito.times(2)).addChildBefore(Mockito.any(), Mockito.eq(n));
        Mockito.verify(parent, Mockito.times(1)).removeChildAfter(Mockito.eq(collapseVariableDeclarations.var));
        Mockito.verify(collapseVariableDeclarations.startNode, Mockito.times(1)).isVar();
        Mockito.verify(collapseVariableDeclarations.startNode, Mockito.times(1)).getNext();
        Mockito.verify(collapseVariableDeclarations.startNode, Mockito.times(1)).getFirstChild();
        Mockito.verify(collapseVariableDeclarations.startNode, Mockito.times(0)).getFirstChild();
        Mockito.verify(collapseVariableDeclarations.endNode, Mockito.times(0)).getNext();
        Mockito.verify(collapseVariableDeclarations.var, Mockito.times(1)).setJSDocInfo(Mockito.any());
    }

    @Test
    public void testApplyCollapses() {
        CollapseVariableDeclarations.Collapse collapse = new CollapseVariableDeclarations.Collapse(new Node(Token.VAR), new Node(Token.VAR), new Node(Token.VAR));

        assertTrue(collapseVariableDeclarations.collapses.isEmpty());
        
        collapseVariableDeclarations.collapses.add(collapse);
        
        collapseVariableDeclarations.applyCollapses();
        
        assertFalse(collapseVariableDeclarations.collapses.isEmpty());
    }
}

