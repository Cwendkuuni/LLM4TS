import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Set;

public class MethodCompilerPassTest {
    private MethodCompilerPass compilerPass;

    @Before
    public void setUp() {
        compilerPass = new MethodCompilerPass(null) {

            @Override
            Callback getActingCallback() {
                return new Callback() {

                    @Override
                    public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
                        return true;
                    }

                    @Override
                    public void visit(NodeTraversal t, Node n, Node parent) {
                        // Implementation is not needed for testing
                    }
                };
            }

            @Override
            SignatureStore getSignatureStore() {
                return new SignatureStore() {

                    @Override
                    public void reset() {
                        // Implementation is not needed for testing
                    }

                    @Override
                    public void addSignature(String functionName, Node functionNode, String sourceFile) {
                        // Implementation is not needed for testing
                    }

                    @Override
                    public void removeSignature(String functionName) {
                        // Implementation is not needed for testing
                    }
                };
            }
        };
    }

    @Test
    public void testProcess_WithExterns() {
        Node externs = createExterns();
        Node root = createRoot();

        compilerPass.process(externs, root);

        // Assertions for the externMethods set
        assertTrue(compilerPass.externMethods.contains("setTimeout"));
        assertTrue(compilerPass.externMethods.contains("setInterval"));
        assertFalse(compilerPass.externMethods.contains("alert"));

        // Assertions for the externMethodsWithoutSignatures set
        assertTrue(compilerPass.externMethodsWithoutSignatures.contains("setInterval"));

        // Assertions for the nonMethodProperties set
        assertTrue(compilerPass.nonMethodProperties.contains("alert"));

        // Assertions for the methodDefinitions map
        Multimap<String, Node> expectedMethodDefinitions = HashMultimap.create();
        expectedMethodDefinitions.put("setTimeout", createFunctionNode());
        expectedMethodDefinitions.put("setInterval", createFunctionNode());
        assertEquals(expectedMethodDefinitions, compilerPass.methodDefinitions);
    }

    private Node createExterns() {
        /*
         * setTimeout = function() {};
         * setInterval = function() {};
         * alert = function() {};
         */
        Node setTimeout = new Node(Token.GETPROP, createNameNode("setTimeout"));
        setTimeout.addChildToBack(createStringNode("setTimeout"));
        setTimeout.addChildToBack(createFunctionNode());

        Node setInterval = new Node(Token.GETPROP, createNameNode("setInterval"));
        setInterval.addChildToBack(createStringNode("setInterval"));
        setInterval.addChildToBack(createFunctionNode());

        Node alert = new Node(Token.GETPROP, createNameNode("alert"));
        alert.addChildToBack(createStringNode("alert"));
        alert.addChildToBack(createFunctionNode());

        return new Node(Token.SCRIPT, setTimeout, setInterval, alert);
    }

    private Node createRoot() {
        /*
         * Foo.prototype.bar = function() {};
         * Foo.prototype.getBar = function() {};
         * Foo.prototype = {};
         * Object = {};
         */
        Node fooPrototypeBar = new Node(Token.GETPROP, createNameNode("Foo.prototype"));
        fooPrototypeBar.addChildToBack(createStringNode("bar"));
        fooPrototypeBar.addChildToBack(createFunctionNode());

        Node fooPrototypeGetBar = new Node(Token.GETPROP, createNameNode("Foo.prototype"));
        fooPrototypeGetBar.addChildToBack(createStringNode("getBar"));
        fooPrototypeGetBar.addChildToBack(createFunctionNode());

        Node fooPrototypeAssignment = new Node(Token.ASSIGN, fooPrototypeGetBar, createNameNode("Foo.prototype"));
        fooPrototypeAssignment.addChildToBack(createObjectLiteralNode());

        Node objectAssignment = new Node(Token.ASSIGN, createNameNode("Object"), createObjectLiteralNode());

        return new Node(Token.SCRIPT, fooPrototypeBar, fooPrototypeAssignment, objectAssignment);
    }

    private Node createNameNode(String name) {
        return new Node(Token.NAME, name);
    }

    private Node createStringNode(String value) {
        return new Node(Token.STRING, value);
    }

    private Node createFunctionNode() {
        return new Node(Token.FUNCTION);
    }

    private Node createObjectLiteralNode() {
        return new Node(Token.OBJECTLIT);
    }
}