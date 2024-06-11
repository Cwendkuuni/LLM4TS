import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CollapseVariableDeclarationsTest {
    
    @Test
    public void testProcess_NoVariableDeclarations() {
        AbstractCompiler compiler = new AbstractCompiler();
        CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
        Node externs = new Node(Token.SCRIPT);
        Node root = new Node(Token.SCRIPT);
        pass.process(externs, root);
        assertEquals(0, pass.collapses.size());
    }
    
    @Test
    public void testProcess_SingleVariableDeclaration() {
        AbstractCompiler compiler = new AbstractCompiler();
        CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
        Node externs = new Node(Token.SCRIPT);
        Node root = new Node(Token.SCRIPT);
        Node varNode = new Node(Token.VAR);
        Node nameNode = new Node(Token.NAME, new Node(Token.STRING, "a"));
        varNode.addChildToBack(nameNode);
        root.addChildToBack(varNode);
        
        pass.process(externs, root);
        assertEquals(0, pass.collapses.size());
    }
    
    @Test
    public void testProcess_MultipleCollapsibleVariableDeclarations() {
        AbstractCompiler compiler = new AbstractCompiler();
        CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
        Node externs = new Node(Token.SCRIPT);
        Node root = new Node(Token.SCRIPT);
        Node varNode1 = new Node(Token.VAR);
        Node nameNode1 = new Node(Token.NAME, new Node(Token.STRING, "a"));
        varNode1.addChildToBack(nameNode1);
        Node varNode2 = new Node(Token.VAR);
        Node nameNode2 = new Node(Token.NAME, new Node(Token.STRING, "b"));
        varNode2.addChildToBack(nameNode2);
        root.addChildToBack(varNode1);
        root.addChildToBack(varNode2);
        
        pass.process(externs, root);
        assertEquals(1, pass.collapses.size());
        Collapse collapse = pass.collapses.get(0);
        assertEquals(varNode1, collapse.startNode);
        assertEquals(varNode2, collapse.endNode);
        assertEquals(root, collapse.parent);
    }
    
    @Test
    public void testProcess_NoCollapsibleVariableDeclarations() {
        AbstractCompiler compiler = new AbstractCompiler();
        CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
        Node externs = new Node(Token.SCRIPT);
        Node root = new Node(Token.SCRIPT);
        Node varNode1 = new Node(Token.VAR);
        Node nameNode1 = new Node(Token.NAME, new Node(Token.STRING, "a"));
        varNode1.addChildToBack(nameNode1);
        Node varNode2 = new Node(Token.VAR);
        Node nameNode2 = new Node(Token.NAME, new Node(Token.STRING, "b"));
        varNode2.addChildToBack(nameNode2);
        Node varNode3 = new Node(Token.VAR);
        Node nameNode3 = new Node(Token.NAME, new Node(Token.STRING, "c"));
        varNode3.addChildToBack(nameNode3);
        root.addChildToBack(varNode1);
        root.addChildToBack(varNode2);
        root.addChildToBack(varNode3);
        
        pass.process(externs, root);
        assertEquals(0, pass.collapses.size());
    }
}