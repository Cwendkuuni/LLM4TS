import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MethodCompilerPassTest {
    @Test
    public void testProcess() {
        MethodCompilerPass compilerPass = new MethodCompilerPassStub();

        // Test method with function signature
        compilerPass.process(null, createMethodDeclaration("add", true));
        assertTrue(compilerPass.methodDefinitions.containsKey("add"));

        // Test method with name signature
        compilerPass.process(null, createMethodDeclaration("subtract", false));
        assertTrue(compilerPass.methodDefinitions.containsKey("subtract"));

        // Test method without signature
        compilerPass.process(null, createMethodDeclaration("multiply", false));
        assertTrue(compilerPass.methodDefinitions.containsKey("multiply"));
    }

    @Test
    public void testProcessWithExterns() {
        MethodCompilerPass compilerPass = new MethodCompilerPassStub();

        // Test method with function signature in externs
        compilerPass.process(createExterns("add"), createMethodDeclaration("add", true));
        assertTrue(compilerPass.methodDefinitions.containsKey("add"));
        assertTrue(compilerPass.externMethods.contains("add"));

        // Test method with name signature in externs
        compilerPass.process(createExterns("subtract"), createMethodDeclaration("subtract", false));
        assertTrue(compilerPass.methodDefinitions.containsKey("subtract"));
        assertTrue(compilerPass.externMethods.contains("subtract"));

        // Test method without signature in externs
        compilerPass.process(createExterns("multiply"), createMethodDeclaration("multiply", false));
        assertTrue(compilerPass.methodDefinitions.containsKey("multiply"));
        assertTrue(compilerPass.externMethods.contains("multiply"));
    }

    @Test
    public void testGetActingCallback() {
        MethodCompilerPassStub compilerPass = new MethodCompilerPassStub();

        compilerPass.getActingCallback();

        assertTrue(compilerPass.getActingCallbackCalled);
    }

    @Test
    public void testGetSignatureStore() {
        MethodCompilerPassStub compilerPass = new MethodCompilerPassStub();

        compilerPass.getSignatureStore();

        assertTrue(compilerPass.getSignatureStoreCalled);
    }

    private Node createMethodDeclaration(String methodName, boolean isFunction) {
        Node methodNode = new Node(Token.FUNCTION);
        if (!isFunction) {
            Node methodNameNode = new Node(Token.NAME, methodName);
            methodNode.addChildToBack(methodNameNode);
        }
        return methodNode;
    }

    private Node createExterns(String methodName) {
        Node getPropNode = new Node(Token.GETPROP);
        Node nameNode = new Node(Token.NAME, "extern");
        getPropNode.addChildToBack(nameNode);
        Node stringNode = new Node(Token.STRING, methodName);
        getPropNode.addChildToBack(stringNode);
        return getPropNode;
    }

    private class MethodCompilerPassStub extends MethodCompilerPass {
        public boolean getActingCallbackCalled = false;
        public boolean getSignatureStoreCalled = false;

        MethodCompilerPassStub() {
            super(null);
        }

        @Override
        public Callback getActingCallback() {
            getActingCallbackCalled = true;
            return null;
        }

        @Override
        public SignatureStore getSignatureStore() {
            getSignatureStoreCalled = true;
            return null;
        }
    }
}