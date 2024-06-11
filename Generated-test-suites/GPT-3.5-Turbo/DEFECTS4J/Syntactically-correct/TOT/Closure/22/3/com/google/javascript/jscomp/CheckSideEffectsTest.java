import static org.junit.Assert.*;
import org.junit.Test;

public class CheckSideEffectsTest {

    // Test case for process method
    @Test
    public void testProcess() {
        // typical use case: no side effect statements
        AbstractCompiler compiler = new AbstractCompiler();
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects check = new CheckSideEffects(compiler, CheckLevel.WARNING, protectSideEffectFreeCode);
        
        // no side effect statements
        Node externs = new Node(Token.BLOCK);
        Node root = new Node(Token.BLOCK);
        check.process(externs, root);
        // no warnings should be reported
        assertFalse(compiler.getErrorCount() > 0);
        
        // edge case: empty externs and root
        externs = new Node(Token.BLOCK);
        root = new Node(Token.BLOCK);
        check.process(externs, root);
        // no warnings should be reported
        assertFalse(compiler.getErrorCount() > 0);
        
        // error scenario: null externs and root
        externs = null;
        root = null;
        check.process(externs, root);
        // no warnings should be reported
        assertFalse(compiler.getErrorCount() > 0);
    }

    // Test case for hotSwapScript method
    @Test
    public void testHotSwapScript() {
        // typical use case: no side effect statements
        AbstractCompiler compiler = new AbstractCompiler();
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects check = new CheckSideEffects(compiler, CheckLevel.WARNING, protectSideEffectFreeCode);
        
        // no side effect statements
        Node scriptRoot = new Node(Token.BLOCK);
        Node originalRoot = new Node(Token.BLOCK);
        check.hotSwapScript(scriptRoot, originalRoot);
        // no warnings should be reported
        assertFalse(compiler.getErrorCount() > 0);
        
        // edge case: empty scriptRoot and originalRoot
        scriptRoot = new Node(Token.BLOCK);
        originalRoot = new Node(Token.BLOCK);
        check.hotSwapScript(scriptRoot, originalRoot);
        // no warnings should be reported
        assertFalse(compiler.getErrorCount() > 0);
        
        // error scenario: null scriptRoot and originalRoot
        scriptRoot = null;
        originalRoot = null;
        check.hotSwapScript(scriptRoot, originalRoot);
        // no warnings should be reported
        assertFalse(compiler.getErrorCount() > 0);
    }

    // Test case for visit method
    @Test
    public void testVisit() {
        // typical use case: side effect statement
        AbstractCompiler compiler = new AbstractCompiler();
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects check = new CheckSideEffects(compiler, CheckLevel.WARNING, protectSideEffectFreeCode);
        
        // side effect statement
        NodeTraversal t = new NodeTraversal(compiler, null, check);
        Node n = new Node(Token.CALL);
        Node parent = new Node(Token.COMMA);
        n.setFirstFirstChild(new Node(Token.NAME, "alert"));
        parent.setFirstChild(n);
        t.visit(n, parent);
        // warning should be reported
        assertTrue(compiler.getErrorCount() > 0);
        
        // edge case: empty node
        n = new Node(Token.EMPTY);
        parent = new Node(Token.COMMA);
        parent.setFirstChild(n);
        t.visit(n, parent);
        // warning should not be reported
        assertFalse(compiler.getErrorCount() > 0);
        
        // error scenario: null parent
        n = new Node(Token.EXPR_RESULT);
        parent = null;
        t.visit(n, parent);
        // warning should not be reported
        assertFalse(compiler.getErrorCount() > 0);
    }

    // Test case for protectSideEffects method
    @Test
    public void testProtectSideEffects() {
        // typical use case: side effect free code
        AbstractCompiler compiler = new AbstractCompiler();
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects check = new CheckSideEffects(compiler, CheckLevel.WARNING, protectSideEffectFreeCode);
        
        // side effect free code
        Node n = new Node(Token.BLOCK);
        check.visit(new NodeTraversal(compiler, null, check), n, null);
        check.protectSideEffects();
        // no warnings should be reported
        assertFalse(compiler.getErrorCount() > 0);
        
        // edge case: problemNodes empty
        check.problemNodes.clear();
        check.protectSideEffects();
        // no warnings should be reported
        assertFalse(compiler.getErrorCount() > 0);
        
        // error scenario: null problemNodes
        check.problemNodes = null;
        check.protectSideEffects();
        // no warnings should be reported
        assertFalse(compiler.getErrorCount() > 0);
    }

    // Test case for addExtern method
    @Test
    public void testAddExtern() {
        // typical use case: add extern
        AbstractCompiler compiler = new AbstractCompiler();
        CheckSideEffects check = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        check.addExtern();
        // extern added successfully
        assertNotNull(compiler.getSynthesizedExternsInput());
        
        // edge case: nothing to add
        check.problemNodes.clear();
        compiler = new AbstractCompiler();
        check = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        check.addExtern();
        // extern added successfully
        assertNotNull(compiler.getSynthesizedExternsInput());
        
        // error scenario: null compiler
        compiler = null;
        check = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        check.addExtern();
        // extern added successfully
        assertNotNull(compiler.getSynthesizedExternsInput());
    }

    // Test case for StripProtection class
    @Test
    public void testStripProtection_Class() {
        // typical use case: side effect statement
        AbstractCompiler compiler = new AbstractCompiler();
        CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
        
        // side effect statement
        NodeTraversal t = new NodeTraversal(compiler, null, stripProtection);
        Node n = new Node(Token.CALL);
        Node parent = new Node(Token.COMMA);
        n.setFirstFirstChild(new Node(Token.NAME, "alert"));
        parent.setFirstChild(n);
        t.visit(n, parent);
        // side effect protection should be removed
        assertEquals(n.getType(), Token.NAME);
        assertEquals("alert", n.getString());
        
        // edge case: empty node
        n = new Node(Token.EMPTY);
        parent = new Node(Token.COMMA);
        parent.setFirstChild(n);
        t.visit(n, parent);
        // side effect protection should not be removed
        assertFalse(n.getType() == Token.NAME && "alert".equals(n.getString()));
        
        // error scenario: null parent
        n = new Node(Token.CALL);
        parent = null;
        t.visit(n, parent);
        // side effect protection should not be removed
        assertFalse(n.getType() == Token.NAME && "alert".equals(n.getString()));
    }

    // Test case for process method of StripProtection class
    @Test
    public void testStripProtection_ProcessMethod() {
        // typical use case: side effect statement
        AbstractCompiler compiler = new AbstractCompiler();
        CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
        
        // side effect statement
        Node externs = new Node(Token.BLOCK);
        Node root = new Node(Token.BLOCK);
        stripProtection.process(externs, root);
        // side effect protection should be removed
        assertFalse(root.hasChildren());
        
        // edge case: empty externs and root
        externs = new Node(Token.BLOCK);
        root = new Node(Token.BLOCK);
        stripProtection.process(externs, root);
        // side effect protection should not be removed
        assertFalse(root.hasChildren());
        
        // error scenario: null externs and root
        externs = null;
        root = null;
        stripProtection.process(externs, root);
        // side effect protection should not be removed
        assertFalse(root.hasChildren());
    }
}