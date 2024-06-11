import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DevirtualizePrototypeMethodsTest {

    private DevirtualizePrototypeMethods devirtualizePrototypeMethods;

    @Before
    public void setUp() {
        devirtualizePrototypeMethods = new DevirtualizePrototypeMethods();
    }

    @Test
    public void testIsCall() {
        UseSite site = new UseSite();
        site.node = new Node(Token.CALL);
        boolean result = devirtualizePrototypeMethods.isCall(site);
        assertTrue(result);
    }

    @Test
    public void testIsNotCall() {
        UseSite site = new UseSite();
        site.node = new Node(Token.NAME);
        boolean result = devirtualizePrototypeMethods.isCall(site);
        assertFalse(result);
    }

    @Test
    public void testIsPrototypeMethodDefinition() {
        Node node = new Node(Token.GETPROP);
        Node parent = new Node(Token.EXPR_RESULT);
        node.addChildToFront(new Node(Token.STRING, "prototype"));
        parent.addChildToBack(node);
        boolean result = devirtualizePrototypeMethods.isPrototypeMethodDefinition(node);
        assertTrue(result);
    }

    @Test
    public void testIsNotPrototypeMethodDefinition() {
        Node node = new Node(Token.GETPROP);
        Node parent = new Node(Token.EXPR_RESULT);
        node.addChildToFront(new Node(Token.STRING, "notPrototype"));
        parent.addChildToBack(node);
        boolean result = devirtualizePrototypeMethods.isPrototypeMethodDefinition(node);
        assertFalse(result);
    }

    @Test
    public void testGetRewrittenMethodName() {
        String methodName = "originalMethodName";
        String result = devirtualizePrototypeMethods.getRewrittenMethodName(methodName);
        assertEquals("JSCompiler_StaticMethods_originalMethodName", result);
    }

    @Test
    public void testIsEligibleDefinition() {
        SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder();
        DefinitionSite definitionSite = new DefinitionSite();
        definitionSite.inExterns = false;
        definitionSite.inGlobalScope = true;
        Definition definition = new Definition();
        definition.setRValue(new Node(Token.FUNCTION));
        definition.setLValue(new Node(Token.GETPROP));
        defFinder.addDefinition(definitionSite, definition);
        Collection<UseSite> useSites = new ArrayList<>();
        UseSite useSite = new UseSite();
        useSite.node = new Node(Token.CALL);
        useSites.add(useSite);
        defFinder.addUseSites(definition, useSites);
        boolean result = devirtualizePrototypeMethods.isEligibleDefinition(defFinder, definitionSite);
        assertTrue(result);
    }

    @Test
    public void testIsNotEligibleDefinition() {
        SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder();
        DefinitionSite definitionSite = new DefinitionSite();
        definitionSite.inExterns = false;
        definitionSite.inGlobalScope = true;
        Definition definition = new Definition();
        definition.setRValue(new Node(Token.VAR));
        definition.setLValue(new Node(Token.GETPROP));
        defFinder.addDefinition(definitionSite, definition);
        boolean result = devirtualizePrototypeMethods.isEligibleDefinition(defFinder, definitionSite);
        assertFalse(result);
    }

    @Test
    public void testRewriteCallSites() {
        SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder();
        Definition definition = new Definition();
        String newMethodName = "newMethod";
        UseSite useSite = new UseSite();
        useSite.node = new Node(Token.CALL);
        Collection<UseSite> useSites = new ArrayList<>();
        useSites.add(useSite);
        defFinder.addUseSites(definition, useSites);
        devirtualizePrototypeMethods.rewriteCallSites(defFinder, definition, newMethodName);
        assertEquals(Token.NAME, useSite.node.getToken());
        assertEquals(newMethodName, useSite.node.getString());
    }

    @Test
    public void testRewriteDefinition() {
        Node node = new Node(Token.GETPROP);
        Node grandparent = new Node(Token.EXPR_RESULT);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToBack(new Node(Token.NAME));
        parent.addChildToBack(new Node(Token.FUNCTION));
        parent.getLastChild().addChildToBack(new Node(Token.NAME));
        Node expr = new Node(Token.VAR);
        grandparent.addChildToFront(expr);
        expr.addChildToFront(parent);
        grandparent.getParent().replaceChild(grandparent, node);
        String newMethodName = "newMethod";
        devirtualizePrototypeMethods.rewriteDefinition(node, newMethodName);
        assertEquals(Token.VAR, expr.getToken());
        assertEquals(Token.NAME, node.getToken());
        assertEquals(newMethodName, node.getString());
        assertEquals(newMethodName + "$self", parent.getFirstChild().getNext().getFirstChild().getString());
    }

    @Test
    public void testFixFunctionType() {
        Node functionNode = new Node(Token.FUNCTION);
        JSTypeRegistry typeRegistry = compiler.getTypeRegistry();
        JSType type = typeRegistry.getNativeType(JSTypeNative.UNKNOWN_TYPE);
        JSType returnType = typeRegistry.getNativeType(JSTypeNative.NULL_TYPE);
        JSType newType = typeRegistry.createFunctionType(type, returnType, null);
        functionNode.setJSType(newType);
        devirtualizePrototypeMethods.fixFunctionType(functionNode);
        assertEquals(newType, functionNode.getJSType());
    }

    @Test
    public void testReplaceReferencesToThis() {
        Node node = new Node(Token.ADD);
        Node child = new Node(Token.THIS);
        node.addChildToBack(child);
        String name = "self";
        devirtualizePrototypeMethods.replaceReferencesToThis(node, name);
        assertEquals(Token.NAME, child.getToken());
        assertEquals(name, child.getString());
    }
}