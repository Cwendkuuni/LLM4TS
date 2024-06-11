import static org.junit.Assert.*;
import org.junit.Test;

public class MethodCompilerPassTest {

    @Test
    public void testExternMethods() {
        MethodCompilerPass compilerPass = new MethodCompilerPassTest();
        compilerPass.process(externs, root);

        // Test for extern methods
        assertTrue(compilerPass.externMethods.contains("method1"));
        assertTrue(compilerPass.externMethods.contains("method2"));
    }

    @Test
    public void testNonMethodProperties() {
        MethodCompilerPass compilerPass = new MethodCompilerPassTest();
        compilerPass.process(externs, root);

        // Test for non-method properties
        assertTrue(compilerPass.nonMethodProperties.contains("property1"));
    }

    @Test
    public void testAddPossibleSignature() {
        MethodCompilerPass compilerPass = new MethodCompilerPassTest();
        compilerPass.process(externs, root);

        // Test adding possible signatures
        Node node = new Node(Token.FUNCTION);
        compilerPass.addPossibleSignature("testFunction", node, t);
        assertTrue(compilerPass.methodDefinitions.containsEntry("testFunction", node));
        assertTrue(compilerPass.getSignatureStore().containsSignature("testFunction"));
    }

    @Test
    public void testGetExternMethods() {
        MethodCompilerPass compilerPass = new MethodCompilerPassTest();
        compilerPass.process(externs, root);

        // Test the GetExternMethods callback
        assertTrue(compilerPass.externMethods.contains("externMethod1"));
    }

    @Test
    public void testGatherSignatures() {
        MethodCompilerPass compilerPass = new MethodCompilerPassTest();
        compilerPass.process(externs, root);

        // Test the GatherSignatures callback
        assertTrue(compilerPass.methodDefinitions.containsValue(node));
        assertTrue(compilerPass.getSignatureStore().containsSignature("testMethod"));
    }
}