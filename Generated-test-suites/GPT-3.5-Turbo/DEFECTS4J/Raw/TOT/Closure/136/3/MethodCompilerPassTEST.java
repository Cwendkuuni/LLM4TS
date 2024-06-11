import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class MethodCompilerPassTest {

    private MethodCompilerPass methodCompilerPass;

    @Before
    public void setUp() {
        methodCompilerPass = new MethodCompilerPass() {
            @Override
            Callback getActingCallback() {
                return new Callback() {
                    @Override
                    public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node, Node node1) {
                        return false;
                    }

                    @Override
                    public void visit(NodeTraversal nodeTraversal, Node node, Node node1) {

                    }
                };
            }

            @Override
            SignatureStore getSignatureStore() {
                return new SignatureStore() {
                    @Override
                    public void reset() {

                    }

                    @Override
                    public void addSignature(String functionName, Node functionNode, String sourceFile) {

                    }

                    @Override
                    public void removeSignature(String functionName) {

                    }
                };
            }
        };
    }

    @Test
    public void testProcess_ExistingExternMethods_AddsToExternMethods() {
        Node externs = new Node(Token.SCRIPT);
        Node root = new Node(Token.SCRIPT);
        Node externMethod = new Node(Token.GETPROP,
                new Node(Token.NAME, "methods"),
                new Node(Token.STRING, "setTimeout"));
        externs.addChildToBack(new Node(Token.EXPR_RESULT, externMethod));

        methodCompilerPass.process(externs, root);

        assertTrue(methodCompilerPass.externMethods.contains("setTimeout"));
    }

    @Test
    public void testProcess_MethodWithoutSignature_AddsToExternMethodsWithoutSignatures() {
        Node externs = new Node(Token.SCRIPT);
        Node root = new Node(Token.SCRIPT);
        Node externMethod = new Node(Token.GETPROP,
                new Node(Token.NAME, "methods"),
                new Node(Token.STRING, "setTimeout"));
        Node function = new Node(Token.FUNCTION);
        externMethod.addChildToBack(function);
        externs.addChildToBack(new Node(Token.EXPR_RESULT, externMethod));

        methodCompilerPass.process(externs, root);

        assertTrue(methodCompilerPass.externMethodsWithoutSignatures.contains("setTimeout"));
    }

    @Test
    public void testProcess_StaticMethod_AddsToMethodDefinitions() {
        Node externs = new Node(Token.SCRIPT);
        Node root = new Node(Token.SCRIPT);
        Node methodName = new Node(Token.STRING, "getBar");
        Node function = new Node(Token.FUNCTION);
        Node staticMethod = new Node(Token.GETPROP,
                new Node(Token.NAME, "Foo"),
                methodName);
        staticMethod.addChildToBack(function);
        Node assign = new Node(Token.ASSIGN, staticMethod, function);
        root.addChildToBack(assign);

        methodCompilerPass.process(externs, root);

        assertTrue(methodCompilerPass.methodDefinitions.containsKey("getBar"));
        assertTrue(methodCompilerPass.methodDefinitions.containsValue(function));
    }

    @Test
    public void testProcess_StaticMethodWithoutSignature_DoesNotAddToMethodDefinitions() {
        Node externs = new Node(Token.SCRIPT);
        Node root = new Node(Token.SCRIPT);
        Node methodName = new Node(Token.STRING, "getBar");
        Node staticMethod = new Node(Token.GETPROP,
                new Node(Token.NAME, "Foo"),
                methodName);
        Node assign = new Node(Token.ASSIGN, staticMethod, methodName);
        root.addChildToBack(assign);

        methodCompilerPass.process(externs, root);

        assertFalse(methodCompilerPass.methodDefinitions.containsKey("getBar"));
    }

    @Test
    public void testProcess_FunctionSignature_AddsToSignatureStore() {
        Node externs = new Node(Token.SCRIPT);
        Node root = new Node(Token.SCRIPT);
        Node functionName = new Node(Token.STRING, "addNumbers");
        Node function = new Node(Token.FUNCTION);
        Node assign = new Node(Token.ASSIGN,
                new Node(Token.GETPROP,
                        Node.newString(Token.NAME, "Foo"),
                        functionName),
                function);
        root.addChildToBack(assign);

        methodCompilerPass.process(externs, root);

        // Assert signature store addSignature method was called with the correct arguments
    }

    @Test
    public void testProcess_VariableSignature_AddsToSignatureStore() {
        Node externs = new Node(Token.SCRIPT);
        Node root = new Node(Token.SCRIPT);
        Node functionName = new Node(Token.STRING, "addNumbers");
        Node function = new Node(Token.NAME, functionName);
        Node assign = new Node(Token.ASSIGN,
                new Node(Token.GETPROP,
                        Node.newString(Token.NAME, "Foo"),
                        functionName),
                function);
        root.addChildToBack(assign);

        methodCompilerPass.process(externs, root);

        // Assert signature store addSignature method was called with the correct arguments
    }

    @Test
    public void testProcess_VariableSignatureWithoutFunction_DoesNotAddToSignatureStore() {
        Node externs = new Node(Token.SCRIPT);
        Node root = new Node(Token.SCRIPT);
        Node functionName = new Node(Token.STRING, "addNumbers");
        Node function = new Node(Token.NULL);
        Node assign = new Node(Token.ASSIGN,
                new Node(Token.GETPROP,
                        Node.newString(Token.NAME, "Foo"),
                        functionName),
                function);
        root.addChildToBack(assign);

        methodCompilerPass.process(externs, root);

        // Assert signature store addSignature method was not called
    }

    @Test
    public void testAddPossibleSignature_FunctionSignature_AddsToSignatureStore() {
        NodeTraversal t = new NodeTraversal(null, null, null);
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN,
                new Node(Token.GETPROP,
                        Node.newString(Token.NAME, "Foo"),
                        new Node(Token.STRING, "getBar")),
                node);
        String functionName = "getBar";

        methodCompilerPass.addPossibleSignature(functionName, node, t);

        // Assert signature store addSignature method was called with the correct arguments
    }

    @Test
    public void testAddPossibleSignature_VariableSignature_AddsToSignatureStore() {
        NodeTraversal t = new NodeTraversal(null, null, null);
        Node node = new Node(Token.NAME, "getBar");
        Node parent = new Node(Token.ASSIGN,
                new Node(Token.GETPROP,
                        Node.newString(Token.NAME, "Foo"),
                        new Node(Token.STRING, "getBar")),
                node);
        String functionName = "getBar";

        methodCompilerPass.addPossibleSignature(functionName, node, t);

        // Assert signature store addSignature method was called with the correct arguments
    }

    @Test
    public void testAddPossibleSignature_VariableSignatureWithoutFunction_AddsToNonMethodProperties() {
        NodeTraversal t = new NodeTraversal(null, null, null);
        Node node = new Node(Token.NULL);
        Node parent = new Node(Token.ASSIGN,
                new Node(Token.GETPROP,
                        Node.newString(Token.NAME, "Foo"),
                        new Node(Token.STRING, "getBar")),
                node);
        String functionName = "getBar";

        methodCompilerPass.addPossibleSignature(functionName, node, t);

        assertTrue(methodCompilerPass.nonMethodProperties.contains(functionName));
    }
}