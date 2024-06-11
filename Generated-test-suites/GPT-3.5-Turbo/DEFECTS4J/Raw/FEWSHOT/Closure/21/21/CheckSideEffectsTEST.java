import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CheckSideEffectsTest {
    
    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckSideEffects check = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        
        Node externs = new Node(Token.EXTERN);
        Node root = new Node(Token.ROOT);
        
        check.process(externs, root);
        
        // assert something about the results here
        
        // The compiler should not have reported any code changes
        assertEquals(0, compiler.getChangeScope().getChangeCount());
    }
    
    @Test
    public void testHotSwapScript() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckSideEffects check = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        
        Node scriptRoot = new Node(Token.SCRIPT);
        Node originalRoot = new Node(Token.ROOT);
        
        check.hotSwapScript(scriptRoot, originalRoot);
        
        // assert something about the results here
        
        // The compiler should not have reported any code changes
        assertEquals(0, compiler.getChangeScope().getChangeCount());
    }
    
    @Test
    public void testVisit() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckSideEffects check = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        
        NodeTraversal traversal = new NodeTraversal(compiler, null);
        
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.EXPR_RESULT);
        
        check.visit(traversal, n, parent);
        
        // assert something about the results here
        
        // The compiler should not have reported any code changes
        assertEquals(0, compiler.getChangeScope().getChangeCount());
    }
    
    @Test
    public void testProtectSideEffects() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckSideEffects check = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        
        // Create a problem node to protect
        Node problemNode = new Node(Token.NAME);
        
        check.problemNodes.add(problemNode);
        check.protectSideEffects();
        
        // assert something about the results here
        
        // The compiler should have reported a code change
        assertEquals(1, compiler.getChangeScope().getChangeCount());
    }
    
    @Test
    public void testStripProtection() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
        
        Node externs = new Node(Token.EXTERN);
        Node root = new Node(Token.ROOT);
        
        stripProtection.process(externs, root);
        
        // assert something about the results here
        
        // The compiler should not have reported any code changes
        assertEquals(0, compiler.getChangeScope().getChangeCount());
    }
}