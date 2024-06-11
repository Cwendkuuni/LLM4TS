import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ScopedAliasesTest {
    private ScopedAliases scopedAliases;

    @Before
    public void setUp() {
        scopedAliases = new ScopedAliases(new AbstractCompiler(), null, new AliasTransformationHandler() {
            @Override
            public AliasTransformation logAliasTransformation(String sourceFileName, SourcePosition<AliasTransformation> sourceRegion) {
                return null;
            }
        });
    }

    @Test
    public void testEnterScope() {
        // TODO: Write test case
    }

    @Test
    public void testExitScope() {
        // TODO: Write test case
    }

    @Test
    public void testShouldTraverse() {
        // TODO: Write test case
    }

    @Test
    public void testHotSwapScript() {
        // TODO: Write test case
    }

    @Test
    public void testProcess() {
        // TODO: Write test case
    }

    @Test
    public void testValidateScopeCall() {
        // TODO: Write test case
    }

    @Test
    public void testVisit() {
        // TODO: Write test case
    }
}