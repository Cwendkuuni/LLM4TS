import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class InlineObjectLiteralsTest {

    private static AbstractCompiler compiler;
    private static InlineObjectLiterals inliner;

    @BeforeClass
    public static void setUp() {
        compiler = new AbstractCompiler();
        inliner = new InlineObjectLiterals(compiler, () -> "1");
    }

    @Test
    public void testProcess() {
        // test code coverage of process method
    }

    @Test
    public void testAfterExitScope() {
        // test code coverage of afterExitScope method
    }

    @Test
    public void testIsVarInlineForbidden() {
        // test code coverage of isVarInlineForbidden method
    }

    @Test
    public void testIsInlinableObject() {
        // test code coverage of isInlinableObject method
    }

    @Test
    public void testBlacklistVarReferencesInTree() {
        // test code coverage of blacklistVarReferencesInTree method
    }

    @Test
    public void testComputeVarList() {
        // test code coverage of computeVarList method
    }

    @Test
    public void testFillInitialValues() {
        // test code coverage of fillInitialValues method
    }

    @Test
    public void testReplaceAssignmentExpression() {
        // test code coverage of replaceAssignmentExpression method
    }

    @Test
    public void testSplitObject() {
        // test code coverage of splitObject method
    }
}