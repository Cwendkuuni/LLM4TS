import static org.junit.Assert.*;
import org.junit.Test;

public class MethodCompilerPassTest {

    @Test
    public void testExternMethods() {
        // Test case for checking if external methods are correctly added
        MethodCompilerPass methodCompilerPass = new MethodCompilerPass(null);
        methodCompilerPass.process(externsNode, rootNode);
        assertTrue(methodCompilerPass.externMethods.contains("method1"));
        assertFalse(methodCompilerPass.externMethods.contains("nonExistentMethod"));
    }

    @Test
    public void testNonMethodProperties() {
        // Test case for checking non-method properties
        MethodCompilerPass methodCompilerPass = new MethodCompilerPass(null);
        methodCompilerPass.process(externsNode, rootNode);
        assertTrue(methodCompilerPass.nonMethodProperties.contains("property1"));
        assertFalse(methodCompilerPass.nonMethodProperties.contains("method1"));
    }

    @Test
    public void testMethodDefinitions() {
        // Test case for checking method definitions
        MethodCompilerPass methodCompilerPass = new MethodCompilerPass(null);
        methodCompilerPass.process(externsNode, rootNode);
        assertTrue(methodCompilerPass.methodDefinitions.containsKey("method1"));
        assertFalse(methodCompilerPass.methodDefinitions.containsKey("nonExistentMethod"));
    }

    // Add more test cases for other methods as needed

}