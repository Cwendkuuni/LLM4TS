import static org.junit.Assert.*;
import org.junit.Test;

public class ScopedAliasesTest {

    @Test
    public void testProcess() {
        ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
        scopedAliases.process(null, null);
    }

    @Test
    public void testHotSwapScript() {
        ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
        scopedAliases.hotSwapScript(null, null);
    }

    @Test
    public void testEnterScope() {
        ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
        NodeTraversal t = new NodeTraversal(null, null, null, null);
        scopedAliases.enterScope(t);
    }

    @Test
    public void testExitScope() {
        ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
        NodeTraversal t = new NodeTraversal(null, null, null, null);
        scopedAliases.exitScope(t);
    }

    @Test
    public void testShouldTraverse() {
        ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
        NodeTraversal t = new NodeTraversal(null, null, null, null);
        assertFalse(scopedAliases.shouldTraverse(t, null, null));
    }

    @Test
    public void testVisit() {
        ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
        NodeTraversal t = new NodeTraversal(null, null, null, null);
        scopedAliases.visit(t, null, null);
    }

    @Test
    public void testValidateScopeCall() {
        ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
        NodeTraversal t = new NodeTraversal(null, null, null, null);
        scopedAliases.validateScopeCall(t, null, null);
    }

    @Test
    public void testFixTypeNode() {
        ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
        Node typeNode = new Node();
        scopedAliases.fixTypeNode(typeNode);
    }

}