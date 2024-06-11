import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.AbstractCompiler;

public class MethodCompilerPassTest {

    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        MethodCompilerPass methodCompilerPass = new MethodCompilerPass(compiler);

        // Test case for process method
        methodCompilerPass.process(null, null);

        // Check if the externMethods set is empty after process method is called
        assertEquals(true, methodCompilerPass.externMethods.isEmpty());

        // Check if the externMethodsWithoutSignatures set is empty after process method is called
        assertEquals(true, methodCompilerPass.externMethodsWithoutSignatures.isEmpty());

        // Check if the methodDefinitions multimap is empty after the process method is called
        assertEquals(true, methodCompilerPass.methodDefinitions.isEmpty());
    }

    @Test
    public void testGetActingCallback() {
        AbstractCompiler compiler = new AbstractCompiler();
        MethodCompilerPass methodCompilerPass = new MethodCompilerPass(compiler);

        // Test case for getActingCallback method
        assertNotNull(methodCompilerPass.getActingCallback());
    }

    @Test
    public void testGetSignatureStore() {
        AbstractCompiler compiler = new AbstractCompiler();
        MethodCompilerPass methodCompilerPass = new MethodCompilerPass(compiler);

        // Test case for getSignatureStore method
        assertNotNull(methodCompilerPass.getSignatureStore());
    }

    @Test
    public void testAddPossibleSignature() {
        AbstractCompiler compiler = new AbstractCompiler();
        MethodCompilerPass methodCompilerPass = new MethodCompilerPass(compiler);

        // Test case for addPossibleSignature method with function type node
        methodCompilerPass.addPossibleSignature("testFunction", new Node(Token.FUNCTION), new NodeTraversal());

        // Test case for addPossibleSignature method with name type node
        Node nameNode = new Node(Token.NAME);
        nameNode.setString("testName");
        methodCompilerPass.addPossibleSignature("testName", nameNode, new NodeTraversal());

        // Check if the nonMethodProperties set contains the names of both nodes
        assertEquals(true, methodCompilerPass.nonMethodProperties.contains("testFunction"));
        assertEquals(true, methodCompilerPass.nonMethodProperties.contains("testName"));
    }

    @Test
    public void testAddSignature() {
        AbstractCompiler compiler = new AbstractCompiler();
        MethodCompilerPass methodCompilerPass = new MethodCompilerPass(compiler);

        // Test case for addSignature method with valid inputs
        Node functionNode = new Node(Token.FUNCTION);
        String functionName = "testFunction";
        String fnSourceName = "testSource";
        methodCompilerPass.addSignature(functionName, functionNode, fnSourceName);

        // Check if the methodDefinitions multi map contains the function node with the corresponding name
        assertEquals(true, methodCompilerPass.methodDefinitions.containsEntry(functionName, functionNode));
    }

    @Test
    public void testGetExternMethods() {
        AbstractCompiler compiler = new AbstractCompiler();
        MethodCompilerPass.GetExternMethods getExternMethods = new MethodCompilerPass.GetExternMethods();

        // Test case for visit method with GETPROP and function type node
        NodeTraversal t = new NodeTraversal();
        Node parent = new Node(Token.ASSIGN);
        Node n = new Node(Token.GETPROP);
        Node dest = new Node(Token.STRING);
        dest.setString("testFunction");
        n.addChildToFront(dest);
        Node functionNode = new Node(Token.FUNCTION);
        t.traverse(null, n, parent);
        getExternMethods.visit(t, n, parent);

        // Check if the externMethods set contains the name of the function
        assertEquals(true, getExternMethods.externMethods.contains("testFunction"));

        // Check if the methodDefinitions multi map contains the function node with the corresponding name
        MethodCompilerPass methodCompilerPass = new MethodCompilerPass(compiler);
        assertEquals(true, methodCompilerPass.methodDefinitions.containsEntry("testFunction", functionNode));

        // Test case for visit method with GETPROP and non-function type node
        parent = new Node(Token.ASSIGN);
        n = new Node(Token.GETPROP);
        dest = new Node(Token.STRING);
        dest.setString("testProperty");
        n.addChildToFront(dest);
        Node nonFunctionNode = new Node(Token.STRING);
        t.traverse(null, n, parent);
        getExternMethods.visit(t, n, parent);

        // Check if the externMethodsWithoutSignatures set contains the name of the property
        assertEquals(true, getExternMethods.externMethodsWithoutSignatures.contains("testProperty"));

        // Check if the methodDefinitions multi map does not contain the non-function node with the corresponding name
        assertEquals(false, methodCompilerPass.methodDefinitions.containsEntry("testProperty", nonFunctionNode));

        // Test case for visit method with OBJECTLIT
        parent = new Node(Token.ASSIGN);
        n = new Node(Token.OBJECTLIT);
        Node key = new Node(Token.STRING);
        Node value = new Node(Token.STRING);
        key.setString("testMethod");
        value.setString("testValue");
        n.addChildToBack(key);
        n.addChildToBack(value);
        t.traverse(null, n, parent);
        getExternMethods.visit(t, n, parent);

        // Check if the externMethods set contains the name of the method
        assertEquals(true, getExternMethods.externMethods.contains("testMethod"));

        // Check if the methodDefinitions multi map contains the method node with the corresponding name
        assertEquals(true, methodCompilerPass.methodDefinitions.containsEntry("testMethod", value));
    }

    @Test
    public void testGatherSignatures() {
        AbstractCompiler compiler = new AbstractCompiler();
        MethodCompilerPass.GatherSignatures gatherSignatures = new MethodCompilerPass.GatherSignatures();

        // Test case for visit method with GETPROP and GETPROP parent
        NodeTraversal t = new NodeTraversal();
        Node parent = new Node(Token.GETPROP);
        parent.addChildToBack(new Node(Token.STRING, new Node(Token.NAME, "Foo")));
        parent.addChildToBack(new Node(Token.STRING, "prototype"));
        Node grandParent = new Node(Token.ASSIGN);
        grandParent.addChildToBack(parent);
        Node n = parent;
        t.traverse(null, n, grandParent);
        gatherSignatures.visit(t, n, grandParent);

        // Check if the nonMethodProperties set contains the name of the property
        MethodCompilerPass methodCompilerPass = new MethodCompilerPass(compiler);
        assertEquals(true, methodCompilerPass.nonMethodProperties.contains("prototype"));

        // Test case for visit method with GETPROP and ASSIGN parent
        parent = new Node(Token.GETPROP);
        parent.addChildToBack(new Node(Token.STRING, new Node(Token.NAME, "Foo")));
        parent.addChildToBack(new Node(Token.STRING, "prototype"));
        grandParent = new Node(Token.ASSIGN);
        grandParent.addChildToBack(parent);
        n = parent;
        t.traverse(null, n, grandParent);
        gatherSignatures.visit(t, n, grandParent);

        // Check if the nonMethodProperties set does not contain the name of the property
        assertEquals(false, methodCompilerPass.nonMethodProperties.contains("prototype"));
    }

    @Test
    public void testRemoveSignature() {
        AbstractCompiler compiler = new AbstractCompiler();
        MethodCompilerPass methodCompilerPass = new MethodCompilerPass(compiler);

        // Test case for removeSignature method
        methodCompilerPass.removeSignature("testFunction");

        // Check that the methodDefinitions multimap does not contain the signature with the corresponding name
        assertEquals(false, methodCompilerPass.methodDefinitions.containsKey("testFunction"));
    }
}