import static org.junit.Assert.*;
import org.junit.Test;

public class CollapseVariableDeclarationsTest {
    
    @Test
    public void testProcess_NoCollapses() {
        Compiler compiler = new Compiler();
        CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
        
        Node externs = null;
        Node root = new Node(Token.ROOT);
        
        pass.process(externs, root);
        
        // Assert that there are no collapses
        assertEquals(0, pass.collapses.size());
        
        // Assert that the parse tree is not modified
        assertSame(root, compiler.getRoot());
    }
    
    @Test
    public void testProcess_CollapseVarNodes() {
        Compiler compiler = new Compiler();
        CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
        
        Node externs = null;
        Node root = new Node(Token.ROOT);
        
        // Create var nodes to be collapsed
        Node var1 = new Node(Token.VAR);
        Node var2 = new Node(Token.VAR);
        Node var3 = new Node(Token.VAR);
        
        // Add child nodes to the var nodes
        var1.addChildToBack(new Node(Token.NAME, new Node(Token.STRING, "a")));
        var2.addChildToBack(new Node(Token.NAME, new Node(Token.STRING, "b")));
        var2.addChildToBack(new Node(Token.NAME, new Node(Token.STRING, "c")));
        var3.addChildToBack(new Node(Token.NAME, new Node(Token.STRING, "d")));
        var3.addChildToBack(new Node(Token.NAME, new Node(Token.STRING, "e")));
        var3.addChildToBack(new Node(Token.NAME, new Node(Token.STRING, "f")));
        
        // Add var nodes to the parse tree
        root.addChildToBack(var1);
        root.addChildToBack(var2);
        root.addChildToBack(var3);
        
        pass.process(externs, root);
        
        // Assert that there is one collapse
        assertEquals(1, pass.collapses.size());
        
        // Assert that the collapse information is correct
        CollapseVariableDeclarations.Collapse collapse = pass.collapses.get(0);
        assertSame(var1, collapse.startNode);
        assertSame(var3.getNext(), collapse.endNode);
        assertSame(root, collapse.parent);
        
        // Assert that the var nodes are collapsed into one node
        Node collapsedVar = root.getFirstChild();
        assertSame(Token.VAR, collapsedVar.getType());
        
        Node name1 = collapsedVar.getFirstChild();
        assertSame(Token.NAME, name1.getType());
        assertEquals("a", name1.getString());
        
        Node name2 = name1.getNext();
        assertSame(Token.NAME, name2.getType());
        assertEquals("b", name2.getString());
        
        Node name3 = name2.getNext();
        assertSame(Token.NAME, name3.getType());
        assertEquals("c", name3.getString());
        
        Node name4 = name3.getNext();
        assertSame(Token.NAME, name4.getType());
        assertEquals("d", name4.getString());
        
        Node name5 = name4.getNext();
        assertSame(Token.NAME, name5.getType());
        assertEquals("e", name5.getString());
        
        Node name6 = name5.getNext();
        assertSame(Token.NAME, name6.getType());
        assertEquals("f", name6.getString());
        
        assertNull(name6.getNext());
    }
    
    @Test
    public void testProcess_CollapseAssignNodes() {
        Compiler compiler = new Compiler();
        CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
        
        Node externs = null;
        Node root = new Node(Token.ROOT);
        
        // Create assign nodes to be collapsed
        Node assign1 = new Node(Token.ASSIGN);
        Node assign2 = new Node(Token.ASSIGN);
        Node assign3 = new Node(Token.ASSIGN);
        
        // Add child nodes to the assign nodes
        assign1.addChildToBack(new Node(Token.NAME, new Node(Token.STRING, "a")));
        assign1.addChildToBack(new Node(Token.TRUE));
        assign2.addChildToBack(new Node(Token.NAME, new Node(Token.STRING, "b")));
        assign2.addChildToBack(new Node(Token.TRUE));
        assign3.addChildToBack(new Node(Token.NAME, new Node(Token.STRING, "c")));
        assign3.addChildToBack(new Node(Token.TRUE));
        
        // Create var nodes to contain the assign nodes
        Node var1 = new Node(Token.VAR, assign1);
        Node var2 = new Node(Token.VAR, assign2);
        Node var3 = new Node(Token.VAR, assign3);
        
        // Add var nodes to the parse tree
        root.addChildToBack(var1);
        root.addChildToBack(var2);
        root.addChildToBack(var3);
        
        pass.process(externs, root);
        
        // Assert that there is one collapse
        assertEquals(1, pass.collapses.size());
        
        // Assert that the collapse information is correct
        CollapseVariableDeclarations.Collapse collapse = pass.collapses.get(0);
        assertSame(var1, collapse.startNode);
        assertSame(var3.getNext(), collapse.endNode);
        assertSame(root, collapse.parent);
        
        // Assert that the assign nodes are collapsed into one node
        Node collapsedVar = root.getFirstChild();
        assertSame(Token.VAR, collapsedVar.getType());
        
        Node assign = collapsedVar.getFirstChild();
        assertSame(Token.ASSIGN, assign.getType());
        
        Node name1 = assign.getFirstChild();
        assertSame(Token.NAME, name1.getType());
        assertEquals("a", name1.getString());
        
        Node true1 = name1.getNext();
        assertSame(Token.TRUE, true1.getType());
        
        Node name2 = true1.getNext();
        assertSame(Token.NAME, name2.getType());
        assertEquals("b", name2.getString());
        
        Node true2 = name2.getNext();
        assertSame(Token.TRUE, true2.getType());
        
        Node name3 = true2.getNext();
        assertSame(Token.NAME, name3.getType());
        assertEquals("c", name3.getString());
        
        Node true3 = name3.getNext();
        assertSame(Token.TRUE, true3.getType());
        
        assertNull(true3.getNext());
    }
    
    @Test
    public void testProcess_CollapseVarAndAssignNodes() {
        Compiler compiler = new Compiler();
        CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
        
        Node externs = null;
        Node root = new Node(Token.ROOT);
        
        // Create var nodes to be collapsed
        Node var1 = new Node(Token.VAR);
        Node var2 = new Node(Token.VAR);
        
        // Add child nodes to the var nodes
        var1.addChildToBack(new Node(Token.NAME, new Node(Token.STRING, "a")));
        var1.addChildToBack(new Node(Token.NAME, new Node(Token.STRING, "b")));
        var2.addChildToBack(new Node(Token.NAME, new Node(Token.STRING, "c")));
        
        // Add var nodes to the parse tree
        root.addChildToBack(var1);
        root.addChildToBack(var2);
        
        // Create assign nodes to be collapsed
        Node assign1 = new Node(Token.ASSIGN);
        Node assign2 = new Node(Token.ASSIGN);
        
        // Add child nodes to the assign nodes
        assign1.addChildToBack(new Node(Token.NAME, new Node(Token.STRING, "d")));
        assign1.addChildToBack(new Node(Token.TRUE));
        assign2.addChildToBack(new Node(Token.NAME, new Node(Token.STRING, "e")));
        assign2.addChildToBack(new Node(Token.TRUE));
        
        // Create var nodes to contain the assign nodes
        Node var3 = new Node(Token.VAR, assign1);
        Node var4 = new Node(Token.VAR, assign2);
        
        // Add var nodes to the parse tree
        var1.addChildToBack(var3);
        var1.addChildToBack(var4);
        
        pass.process(externs, root);
        
        // Assert that there is one collapse
        assertEquals(1, pass.collapses.size());
        
        // Assert that the collapse information is correct
        CollapseVariableDeclarations.Collapse collapse = pass.collapses.get(0);
        assertSame(var1, collapse.startNode);
        assertSame(var4.getNext(), collapse.endNode);
        assertSame(root, collapse.parent);
        
        // Assert that the var and assign nodes are collapsed into one node
        Node collapsedVar = root.getFirstChild();
        assertSame(Token.VAR, collapsedVar.getType());
        
        Node name1 = collapsedVar.getFirstFirstChild();
        assertSame(Token.NAME, name1.getType());
        assertEquals("a", name1.getString());
        
        Node name2 = name1.getNext();
        assertSame(Token.NAME, name2.getType());
        assertEquals("b", name2.getString());
        
        Node name3 = name2.getNext();
        assertSame(Token.NAME, name3.getType());
        assertEquals("c", name3.getString());
        
        Node assign = name3.getNext();
        assertSame(Token.ASSIGN, assign.getType());
        
        Node name4 = assign.getFirstChild();
        assertSame(Token.NAME, name4.getType());
        assertEquals("d", name4.getString());
        
        Node true1 = name4.getNext();
        assertSame(Token.TRUE, true1.getType());
        
        Node name5 = true1.getNext();
        assertSame(Token.NAME, name5.getType());
        assertEquals("e", name5.getString());
        
        Node true2 = name5.getNext();
        assertSame(Token.TRUE, true2.getType());
        
        assertNull(true2.getNext());
    }
    
}