import static org.junit.Assert.*;
import org.junit.Test;

public class CollapseVariableDeclarationsTest {
    
    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        CollapseVariableDeclarations cvd = new CollapseVariableDeclarations(compiler);
        
        Node externs = new Node(Token.EXTERN);
        Node root = new Node(Token.ROOT);
        
        cvd.process(externs, root);
        
        // Test that collapses list is empty after process
        assertEquals(0, cvd.collapses.size());
        
        // Test that nodesToCollapse set is empty after process
        assertEquals(0, cvd.nodesToCollapse.size());
        
        // Test that reportCodeChange is called after process
        assertTrue(compiler.reportCodeChangeCalled);
    }
    
    @Test
    public void testCanNotBeRedeclared() {
        AbstractCompiler compiler = new AbstractCompiler();
        CollapseVariableDeclarations cvd = new CollapseVariableDeclarations(compiler);
        
        Node node = new Node(Token.NOT);
        Scope scope = new Scope();
        
        boolean result = cvd.canBeRedeclared(node, scope);
        
        // Test that result is false if node is not an assignment expression
        assertFalse(result);
    }
    
    @Test
    public void testCanBeRedeclared_VarNull() {
        AbstractCompiler compiler = new AbstractCompiler();
        CollapseVariableDeclarations cvd = new CollapseVariableDeclarations(compiler);
        
        Node node = new Node(Token.VAR);
        Scope scope = new Scope();
        
        boolean result = cvd.canBeRedeclared(node, scope);
        
        // Test that result is false if node is a var declaration node and scope has no variable null
        assertFalse(result);
    }
    
    @Test
    public void testCanBeRedeclared_VarWithBlacklistedVars() {
        AbstractCompiler compiler = new AbstractCompiler();
        CollapseVariableDeclarations cvd = new CollapseVariableDeclarations(compiler);
        
        Node node = new Node(Token.VAR);
        Node child = new Node(Token.NAME, "x");
        node.addChildToBack(child);
        
        Scope scope = new Scope();
        scope.addVar(child.getString(), child);
        
        cvd.blacklistedVars.add(child.getVar());
        
        boolean result = cvd.canBeRedeclared(node, scope);
        
        // Test that result is false if node is a var declaration node and scope has variable null that is blacklisted
        assertFalse(result);
    }
    
    @Test
    public void testCanBeRedeclared_VarWithValidVar() {
        AbstractCompiler compiler = new AbstractCompiler();
        CollapseVariableDeclarations cvd = new CollapseVariableDeclarations(compiler);
        
        Node node = new Node(Token.VAR);
        Node child = new Node(Token.NAME, "x");
        node.addChildToBack(child);
        
        Scope scope = new Scope();
        scope.addVar(child.getString(), child);
        
        boolean result = cvd.canBeRedeclared(node, scope);
        
        // Test that result is true if node is a var declaration node and scope has valid variable null
        assertTrue(result);
    }
    
    @Test
    public void testProcess_GatherCollapses_BlacklistStubVars() {
        AbstractCompiler compiler = new AbstractCompiler();
        CollapseVariableDeclarations cvd = new CollapseVariableDeclarations(compiler);
        
        NodeTraversal t = new NodeTraversal(compiler);
        Node varNode = new Node(Token.VAR);
        Node child = new Node(Token.NAME, "x");
        varNode.addChildToBack(child);
        
        cvd.collapses.add(new Collapse(varNode, null, null));
        cvd.nodesToCollapse.add(varNode);
        cvd.blacklistStubVars(t, varNode);
        
        // Test that blacklistedVars set contains the scope variable null
        assertTrue(cvd.blacklistedVars.contains(child.getVar()));
    }
    
    @Test
    public void testProcess_GatherCollapses_CanNotBeCollapsed() {
        AbstractCompiler compiler = new AbstractCompiler();
        CollapseVariableDeclarations cvd = new CollapseVariableDeclarations(compiler);
        
        NodeTraversal t = new NodeTraversal(compiler);
        Node n = new Node(Token.NAME, "x");
        Node parent = new Node(Token.ROOT);
        
        cvd.nodesToCollapse.add(n);
        cvd.collapses.add(new Collapse(n, null, parent));
        
        NodeTraversal.Callback callback = cvd.new GatherCollapses();
        callback.visit(t, n, parent);
        
        // Test that collapses list remains empty if node is not a var node and cannot be redeclared
        assertEquals(0, cvd.collapses.size());
        
        // Test that nodesToCollapse set remains empty if node is not a var node and cannot be redeclared
        assertEquals(0, cvd.nodesToCollapse.size());
    }
    
    @Test
    public void testProcess_GatherCollapses_Collapsed() {
        AbstractCompiler compiler = new AbstractCompiler();
        CollapseVariableDeclarations cvd = new CollapseVariableDeclarations(compiler);
        
        NodeTraversal t = new NodeTraversal(compiler);
        Node varNode1 = new Node(Token.VAR);
        Node child1 = new Node(Token.NAME, "x");
        varNode1.addChildToBack(child1);
        
        Node varNode2 = new Node(Token.VAR);
        Node child2 = new Node(Token.NAME, "y");
        varNode2.addChildToBack(child2);
        
        Node parent = new Node(Token.ROOT);
        parent.addChildToBack(varNode1);
        parent.addChildToBack(varNode2);
        
        cvd.nodesToCollapse.add(varNode1);
        cvd.nodesToCollapse.add(varNode2);
        cvd.collapses.add(new Collapse(varNode1, varNode2, parent));
        
        NodeTraversal.Callback callback = cvd.new GatherCollapses();
        callback.visit(t, varNode1, parent);
        
        // Test that collapses list contains the collapse object if there are nodes to collapse
        assertEquals(1, cvd.collapses.size());
        
        // Test that nodesToCollapse set contains all the nodes to collapse
        assertEquals(4, cvd.nodesToCollapse.size());
    }
    
    @Test
    public void testApplyCollapses() {
        AbstractCompiler compiler = new AbstractCompiler();
        CollapseVariableDeclarations cvd = new CollapseVariableDeclarations(compiler);
        
        Node varNode1 = new Node(Token.VAR);
        Node child1 = new Node(Token.NAME, "x");
        varNode1.addChildToBack(child1);
        
        Node varNode2 = new Node(Token.VAR);
        Node child2 = new Node(Token.NAME, "y");
        varNode2.addChildToBack(child2);
        
        Node parent = new Node(Token.ROOT);
        parent.addChildToBack(varNode1);
        parent.addChildToBack(varNode2);
        
        cvd.collapses.add(new Collapse(varNode1, varNode2, parent));
        
        cvd.applyCollapses();
        
        // Test that the VAR node is added to the parent with correct children
        assertEquals(1, parent.getChildCount());
        assertEquals(Token.VAR, parent.getFirstChild().getType());
        
        // Test that the first child of the VAR node is the child1 node
        assertEquals(child1, parent.getFirstChild().getFirstChild());
        
        // Test that the second child of the VAR node is the child2 node
        assertEquals(child2, parent.getFirstChild().getLastChild());
    }

}