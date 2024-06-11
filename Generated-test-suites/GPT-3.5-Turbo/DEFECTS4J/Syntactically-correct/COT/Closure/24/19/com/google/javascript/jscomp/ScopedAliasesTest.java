import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ScopedAliasesTest {

    private ScopedAliases scopedAliases;

    @Before
    public void setUp() {
        scopedAliases = new ScopedAliases(new AbstractCompiler(), null, new AliasTransformationHandler());
    }

    @Test
    public void testProcess() {
        // To be implemented
    }

    @Test
    public void testHotSwapScript() {
        // To be implemented
    }

    @Test
    public void testEnterScope() {
        // To be implemented
    }

    @Test
    public void testExitScope() {
        // To be implemented
    }

    @Test
    public void testShouldTraverse() {
        // To be implemented
    }

    @Test
    public void testFindAliases() {
        // To be implemented
    }

    @Test
    public void testValidateScopeCall() {
        // To be implemented
    }

    @Test
    public void testVisit() {
        // To be implemented
    }

    @Test
    public void testFixTypeNode() {
        // To be implemented
    }
}