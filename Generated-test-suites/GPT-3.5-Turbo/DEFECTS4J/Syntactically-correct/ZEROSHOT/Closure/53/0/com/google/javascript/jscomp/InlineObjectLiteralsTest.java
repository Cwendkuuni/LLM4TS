import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InlineObjectLiteralsTest {
    
    private InlineObjectLiterals inline;
    
    @Before
    public void setUp() {
        inline = new InlineObjectLiterals(new AbstractCompiler(), new Supplier<String>() {
            @Override
            public String get() {
                return "id";
            }
        });
    }
    
    @Test
    public void testProcess() {
        Node externs = null;
        Node root = null;
        inline.process(externs, root);
        // Add your test assertions here
    }
    
    @Test
    public void testIsVarInlineForbidden() {
        Var var = new Var(null, null, "test", null);
        boolean result = inline.isVarInlineForbidden(var);
        assertEquals(false, result);
        // Add your test assertions here
    }
    
    @Test
    public void testIsInlinableObject() {
        // Add your test assertions here
    }
    
    @Test
    public void testBlacklistVarReferencesInTree() {
        Node root = null;
        Scope scope = null;
        inline.blacklistVarReferencesInTree(root, scope);
        // Add your test assertions here
    }
    
    @Test
    public void testAfterExitScope() {
        NodeTraversal t = null;
        ReferenceMap referenceMap = null;
        inline.afterExitScope(t, referenceMap);
        // Add your test assertions here
    }
    
    @Test
    public void testIsVarOrAssignExprLhs() {
        Node n = null;
        boolean result = inline.isVarOrAssignExprLhs(n);
        assertEquals(false, result);
        // Add your test assertions here
    }
    
    @Test
    public void testSplitObject() {
        Var v = new Var(null, null, "test", null);
        Reference declaration = null;
        Reference init = null;
        ReferenceCollection referenceInfo = null;
        inline.splitObject(v, declaration, init, referenceInfo);
        // Add your test assertions here
    }
    
    // Add more test methods here
    
}