
import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.DevirtualizePrototypeMethods;

public class DevirtualizePrototypeMethodsTest {

    @Test
    public void testIsEligibleDefinition() {
        // Test with eligible definition
        DevirtualizePrototypeMethods devirtualize = new DevirtualizePrototypeMethods(null);
        SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(null);
        Definition definition = new Definition(null, null, null);
        DefinitionSite definitionSite = new DefinitionSite(definition, null, false, false);
        boolean eligible = devirtualize.isEligibleDefinition(defFinder, definitionSite);
        assertEquals(true, eligible);

        // Test with ineligible definition
        Node rValue = new Node(Token.FUNCTION);
        definition.setRValue(rValue);
        eligible = devirtualize.isEligibleDefinition(defFinder, definitionSite);
        assertEquals(false, eligible);
    }

    @Test
    public void testRewriteDefinition() {
        // Test with valid method definition
        DevirtualizePrototypeMethods devirtualize = new DevirtualizePrototypeMethods(null);
        Node node = new Node(Token.STRING);
        node.addChildToBack(new Node(Token.NAME, "testFunc"));
        Node parent = new Node(Token.EXPR_RESULT);
        parent.addChildToBack(node);
        Node expr = new Node(Token.NAME);
        expr.addChildToFront(new Node(Token.VAR, node));
        parent.addChildToBack(expr);

        String newMethodName = devirtualize.getRewrittenMethodName("testFunc");
        devirtualize.rewriteDefinition(node, newMethodName);

        // Check that the definition is rewritten correctly
        Node expectedNewNameNode = Node.newString(Token.NAME, newMethodName);
        Node expectedVar = new Node(Token.VAR, expectedNewNameNode);
        Node expectedResult = Node.newString(Token.STRING, "testFunc");
        expectedResult.replaceChild(expectedResult.getFirstChild(), expectedNewNameNode);
        assertEquals(true, parent.isEquivalentTo(expectedVar));
        assertEquals(true, node.isEquivalentTo(expectedResult));
    }

    @Test
    public void testRewriteCallSites() {
        // Test with valid call site
        DevirtualizePrototypeMethods devirtualize = new DevirtualizePrototypeMethods(null);
        Node callNode = new Node(Token.STRING);
        Node node = new Node(Token.NAME, "testFunc");
        callNode.addChildToBack(node);
        Node parent = new Node(Token.CALL);
        parent.addChildToBack(callNode);

        Node objectNode = new Node(Token.NAME, "testObj");
        node.addChildToFront(objectNode);

        Definition definition = new Definition(null, null, null);
        String newMethodName = devirtualize.getRewrittenMethodName("testFunc");
        devirtualize.rewriteCallSites(null, definition, newMethodName);

        // Check that the call site is rewritten correctly
        Node expectedNewNameNode = Node.newString(Token.NAME, newMethodName);
        expectedNewNameNode.addChildToFront(objectNode);
        Node expectedCall = new Node(Token.CALL, expectedNewNameNode);
        assertEquals(true, parent.isEquivalentTo(expectedCall));
        assertEquals(true, callNode.isEquivalentTo(expectedNewNameNode));
        assertEquals(true, objectNode.isEquivalentTo(expectedNewNameNode.getFirstChild()));
    }
}
