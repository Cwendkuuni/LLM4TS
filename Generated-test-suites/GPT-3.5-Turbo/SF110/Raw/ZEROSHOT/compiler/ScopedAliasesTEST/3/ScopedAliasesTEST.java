import org.junit.Test;
import static org.junit.Assert.*;

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
        scopedAliases.enterScope(null);
    }

    @Test
    public void testExitScope() {
        ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
        scopedAliases.exitScope(null);
    }

    @Test
    public void testShouldTraverse() {
        ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
        scopedAliases.shouldTraverse(null, null, null);
    }

    @Test
    public void testVisit() {
        ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
        scopedAliases.visit(null, null, null);
    }

    @Test
    public void testValidateScopeCall() {
        ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
        scopedAliases.validateScopeCall(null, null, null);
    }

    @Test
    public void testFixTypeNode() {
        ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
        scopedAliases.fixTypeNode(null);
    }
}