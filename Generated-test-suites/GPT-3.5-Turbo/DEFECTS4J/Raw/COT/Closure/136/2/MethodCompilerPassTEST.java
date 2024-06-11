
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MethodCompilerPassTest {
    
    private MethodCompilerPass compilerPass;
    
    @Before
    public void setUp() {
        compilerPass = new MethodCompilerPass() {
            @Override
            Callback getActingCallback() {
                return null;
            }

            @Override
            SignatureStore getSignatureStore() {
                return null;
            }

            @Override
            public void visit(NodeTraversal t, Node n, Node parent) {}
        };
    }

    @Test
    public void testProcess() {
        Node externs = null;
        Node root = null;
        compilerPass.process(externs, root);
        
        // Test cases for each public method
        
        // test externMethods
        assertNotNull(compilerPass.externMethods);
        
        // test externMethodsWithoutSignatures
        assertNotNull(compilerPass.externMethodsWithoutSignatures);
        
        // test nonMethodProperties
        assertNotNull(compilerPass.nonMethodProperties);
        
        // test methodDefinitions
        assertNotNull(compilerPass.methodDefinitions);
    }

    @Test
    public void testAddPossibleSignature() {
        String name = "testFunction";
        Node node = new Node(Token.FUNCTION);
        NodeTraversal t = null;
        
        // Test case for adding a function signature
        compilerPass.addPossibleSignature(name, node, t);
        // Assert that the signature is added to the SignatureStore and methodDefinitions
        assertTrue(compilerPass.getSignatureStore().contains(name));
        assertTrue(compilerPass.methodDefinitions.containsEntry(name, node));
        
        // Test case for adding a name signature
        name = "testName";
        node.setType(Token.NAME);
        Node function = new Node(Token.FUNCTION);
        node.addChildToFront(function);
        
        compilerPass.addPossibleSignature(name, node, t);
        // Assert that the signature is added to the SignatureStore and methodDefinitions
        assertTrue(compilerPass.getSignatureStore().contains(name));
        assertTrue(compilerPass.methodDefinitions.containsEntry(name, function));
        
        // Test case for adding a name signature with undefined function
        name = "undefinedName";
        node.setType(Token.NAME);
        node.removeChild(function);
        
        try {
          compilerPass.addPossibleSignature(name, node, t);
          fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
          // Exception caught, test passed
        }
    }

    @Test
    public void testGetExternMethods() {
        // Create sample externs code
        String externsCode = "var externsObj = {\n" +
                    "  externMethod1: function() {},\n" +
                    "  externMethod2: method2\n" +
                    "};\n" +
                    "function method2() {}";
        
        Node externs = parseCode(externsCode);
        NodeTraversal.traverse(compilerPass.compiler, externs, compilerPass.new GetExternMethods());
        
        // Test case for method with signature
        assertTrue(compilerPass.externMethods.contains("externMethod1"));
        assertTrue(compilerPass.getSignatureStore().contains("externMethod1"));
        assertFalse(compilerPass.externMethodsWithoutSignatures.contains("externMethod1"));
        
        // Test case for method without signature
        assertTrue(compilerPass.externMethods.contains("externMethod2"));
        assertFalse(compilerPass.getSignatureStore().contains("externMethod2"));
        assertTrue(compilerPass.externMethodsWithoutSignatures.contains("externMethod2"));
    }

    @Test
    public void testGatherSignatures() {
        // Create sample code with method signatures
        String code = "var obj1 = {\n" +
                    "  method1: function() {},\n" +
                    "  method2() {}\n" +
                    "};\n" +
                    "var obj2 = {\n" +
                    "  method3: function() {},\n" +
                    "  method4() {}\n" +
                    "};";
        
        Node root = parseCode(code);
        NodeTraversal.traverseRoots(
            compilerPass.compiler, Lists.newArrayList(null, root), compilerPass.new GatherSignatures());

        // Test case for method with signature in object literal
        assertTrue(compilerPass.getSignatureStore().contains("method1"));
        assertTrue(compilerPass.methodDefinitions.containsKey("method1"));
        
        // Test case for method with implicit signature in object literal
        assertTrue(compilerPass.getSignatureStore().contains("method2"));
        assertTrue(compilerPass.methodDefinitions.containsKey("method2"));
        
        // Test case for method with signature in object literal using bracket notation
        assertTrue(compilerPass.getSignatureStore().contains("method3"));
        assertTrue(compilerPass.methodDefinitions.containsKey("method3"));
        
        // Test case for method with implicit signature in object literal using bracket notation
        assertTrue(compilerPass.getSignatureStore().contains("method4"));
        assertTrue(compilerPass.methodDefinitions.containsKey("method4"));
    }
    
    // Helper method to parse JavaScript code into an abstract syntax tree (AST)
    private Node parseCode(String code) {
        Compiler compiler = new Compiler();
        compiler.initOptions(new CompilerOptions());
        Result result = compiler.parse(SourceFile.fromCode("Test.js", code));
        return result.ast;
    }
}
