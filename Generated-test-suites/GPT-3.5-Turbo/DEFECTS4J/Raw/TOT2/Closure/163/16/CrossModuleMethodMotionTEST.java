import static org.junit.Assert.*;
import org.junit.Test;

public class CrossModuleMethodMotionTest {

    @Test
    public void testMoveMethods() {
        // Initialize the required objects
        AbstractCompiler compiler = new Compiler();
        IdGenerator idGenerator = new IdGenerator();
        CrossModuleMethodMotion methodMotion = new CrossModuleMethodMotion(compiler, idGenerator, true);

        // Create a sample Node for testing
        Node externRoot = new Node(Token.ROOT);
        Node root = new Node(Token.ROOT);

        // Call the process method and test if it runs without errors
        try {
            methodMotion.process(externRoot, root);
        } catch (Exception e) {
            fail("Exception thrown while moving methods: " + e.getMessage());
        }
    }

    @Test
    public void testNullCommonModuleError() {
        // Initialize the required objects
        AbstractCompiler compiler = new Compiler();
        IdGenerator idGenerator = new IdGenerator();
        CrossModuleMethodMotion methodMotion = new CrossModuleMethodMotion(compiler, idGenerator, true);

        // Create a sample NameInfo with null deepest common module
        NameInfo nameInfo = new NameInfo();
        nameInfo.setDeepestCommonModuleRef(null);

        // Add the NameInfo to a collection
        Collection<NameInfo> allNameInfo = new ArrayList<>();
        allNameInfo.add(nameInfo);

        // Test if the NULL_COMMON_MODULE_ERROR is reported
        try {
            methodMotion.moveMethods(allNameInfo);
            fail("Exception not thrown for null deepest common module");
        } catch (JSError expected) {
            assertEquals("JSC_INTERNAL_ERROR_MODULE_DEPEND", expected.getType().key);
        }
    }
}