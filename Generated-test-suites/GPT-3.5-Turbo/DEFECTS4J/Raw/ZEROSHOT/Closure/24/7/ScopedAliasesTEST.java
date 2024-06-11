import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ScopedAliasesTest {

    private ScopedAliases scopedAliases;

    @Before
    public void setUp() {
        scopedAliases = new ScopedAliases();
    }

    @Test
    public void testEnterScope() {
        // Test when entering a scope
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node();
        scopedAliases.enterScope(nodeTraversal);
        assertTrue(scopedAliases.transformation != null);
        assertTrue(scopedAliases.aliases.isEmpty());
    }

    @Test
    public void testExitScope() {
        // Test when exiting a scope
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node();
        scopedAliases.exitScope(nodeTraversal);
        assertTrue(scopedAliases.aliases.isEmpty());
        assertTrue(scopedAliases.transformation == null);
    }

    @Test
    public void testShouldTraverse() {
        // Test shouldTraverse() method
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node();
        Node parent = new Node();
        assertTrue(scopedAliases.shouldTraverse(nodeTraversal, node, parent));

        nodeTraversal.enterScope(node);
        assertFalse(scopedAliases.shouldTraverse(nodeTraversal, node, parent));

        nodeTraversal.exitScope(node);
        assertTrue(scopedAliases.shouldTraverse(nodeTraversal, node, parent));
    }

    @Test
    public void testFindAliases() {
        // Test findAliases() method
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node();
        scopedAliases.findAliases(nodeTraversal);
        assertTrue(scopedAliases.aliases.isEmpty());

        Scope scope = new Scope();
        Var var = new Var();
        Node aliasReference = new Node();
        Node aliasDefinition = new Node();
        var.setNode(aliasReference);
        scope.addVar(var);
        aliasDefinition.addChildToBack(aliasReference);
        node.setParent(node);
        nodeTraversal.setCurrentNode(node);
        nodeTraversal.setScope(scope);
        scopedAliases.findAliases(nodeTraversal);
        assertFalse(scopedAliases.aliases.isEmpty());
    }

    @Test
    public void testValidateScopeCall() {
        // Test validateScopeCall() method
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node();
        Node parent = new Node();
        scopedAliases.validateScopeCall(nodeTraversal, node, parent);
        assertTrue(scopedAliases.hasErrors);

        Node anonymousFnNode = new Node(Token.FUNCTION);
        anonymousFnNode.putProp("name", "anonymous");
        Node childAtIndex = new Node();
        anonymousFnNode.addChildToBack(childAtIndex);
        parent.addChildToBack(anonymousFnNode);
        scopedAliases.validateScopeCall(nodeTraversal, node, parent);
        assertTrue(scopedAliases.hasErrors);

        parent.addChildToBack(new Node());
        scopedAliases.validateScopeCall(nodeTraversal, node, parent);
        assertFalse(scopedAliases.hasErrors);
    }

    @Test
    public void testVisit() {
        // Test visit() method
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node();
        Node parent = new Node();
        Node aliasVar = new Node();
        Node aliasNode = new Node();
        aliasVar.addChildToBack(aliasNode);
        parent.addChildToBack(aliasVar);
        scopedAliases.aliases.put("alias", new Var());
        scopedAliases.visit(nodeTraversal, node, parent);
        assertFalse(scopedAliases.aliasDefinitionsInOrder.isEmpty());
        scopedAliases.aliases.clear();

        Node returnNode = new Node(Token.RETURN);
        parent.addChildToBack(returnNode);
        scopedAliases.visit(nodeTraversal, node, parent);
        assertTrue(scopedAliases.hasErrors);

        Node thisNode = new Node(Token.THIS);
        parent.addChildToBack(thisNode);
        scopedAliases.visit(nodeTraversal, node, parent);
        assertTrue(scopedAliases.hasErrors);

        Node throwNode = new Node(Token.THROW);
        parent.addChildToBack(throwNode);
        scopedAliases.visit(nodeTraversal, node, parent);
        assertTrue(scopedAliases.hasErrors);

        Node typeNode = new Node(Token.TYPE);
        typeNode.putProp("type", "string");
        Node stringNode = new Node(Token.STRING, "alias.type");
        typeNode.addChildToBack(stringNode);
        scopedAliases.aliases.put("alias", new Var());
        scopedAliases.visit(nodeTraversal, typeNode, parent);
        assertFalse(scopedAliases.aliasUsages.isEmpty());
    }

    @Test
    public void testFixTypeNode() {
        // Test fixTypeNode() method
        Node typeNode = new Node(Token.TYPE);
        Node stringNode = new Node(Token.STRING, "alias.type");
        typeNode.addChildToBack(stringNode);
        scopedAliases.aliases.put("alias", new Var());
        scopedAliases.fixTypeNode(typeNode);
        assertFalse(scopedAliases.aliasUsages.isEmpty());
    }
}