import static org.junit.Assert.*;
import org.junit.Test;

public class DevirtualizePrototypeMethodsTest {

    @Test
    public void testRewriteDefinition() {
        DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods();
        
        // Create a mock node and retrieve the rewritten method name
        Node mockNode = createMockNode();
        String newMethodName = devirtualizer.getRewrittenMethodName(mockNode.getLastChild().getString());

        // Check that the method name starts with "JSCompiler_StaticMethods_"
        assertTrue(newMethodName.startsWith("JSCompiler_StaticMethods_"));
    }

    @Test
    public void testIsEligibleDefinition() {
        DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods();
        SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);

        // Create a mock definition site and module graph
        DefinitionSite mockDefinitionSite = createMockDefinitionSite();
        JSModuleGraph mockModuleGraph = createMockModuleGraph();

        // Check that the definition is eligible for rewrite
        assertTrue(devirtualizer.isEligibleDefinition(defFinder, mockDefinitionSite, mockModuleGraph));
    }

    @Test
    public void testRewriteCallSites() {
        DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods();
        SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);

        // Create a mock definition and use site
        Definition mockDefinition = createMockDefinition();
        UseSite mockUseSite = createMockUseSite();

        // Rewrite the call site and check that it has been modified
        devirtualizer.rewriteCallSites(defFinder, mockDefinition, "newMethodName");
        assertTrue(mockUseSite.node.getParent().getToken() == Token.NAME);
    }

    @Test
    public void testFixFunctionType() {
        DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods();
        Node functionNode = createMockFunctionNode();

        // Check that the function type has been modified
        devirtualizer.fixFunctionType(functionNode);
        JSType newType = functionNode.getJSType();
        assertNotNull(newType);
        assertTrue(newType.getParameterTypes().get(0) instanceof ObjectType);
        assertTrue(newType.getReturnType() instanceof JSTypeNative);
    }

    @Test
    public void testReplaceReferencesToThis() {
        DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods();
        Node node = createMockNode();

        // Replace references to "this" with a new name and check that the node has been modified
        devirtualizer.replaceReferencesToThis(node, "newName");
        assertTrue(node.toString().contains("newName"));
    }

    // Helper methods for creating mock objects
    private Node createMockNode() {
        Node node = new Node(Token.GETPROP);
        Node nameNode = new Node(Token.NAME, "prototype");
        Node childNode = new Node(Token.NAME, "foo");
        childNode.setParent(nameNode);
        nameNode.addChildToFront(childNode);
        childNode.setParent(node);
        node.addChildToFront(nameNode);
        return node;
    }

    private DefinitionSite createMockDefinitionSite() {
        DefinitionSite definitionSite = new DefinitionSite();
        definitionSite.inExterns = false;
        definitionSite.inGlobalScope = true;
        definitionSite.module = createMockModule();
        definitionSite.node = createMockNode();
        definitionSite.definition = createMockDefinition();
        return definitionSite;
    }

    private Definition createMockDefinition() {
        Definition definition = new Definition();
        definition.setLValue(createMockNode());
        definition.setRValue(createMockNode());
        return definition;
    }

    private UseSite createMockUseSite() {
        UseSite useSite = new UseSite();
        useSite.node = createMockNode();
        useSite.module = createMockModule();
        return useSite;
    }

    private JSModuleGraph createMockModuleGraph() {
        JSModuleGraph moduleGraph = new JSModuleGraph();
        JSModule module1 = createMockModule();
        JSModule module2 = createMockModule();
        moduleGraph.addModule(module1);
        moduleGraph.addModule(module2);
        moduleGraph.addDependency(module2, module1);
        return moduleGraph;
    }

    private JSModule createMockModule() {
        return new JSModule("mockModule");
    }

    private Node createMockFunctionNode() {
        Node functionNode = NodeUtil.newFunctionNode("mockFunction");
        JSTypeRegistry typeRegistry = new JSTypeRegistry();
        functionNode.setJSType(typeRegistry.getNativeType(JSTypeNative.FUNCTION_TYPE));
        return functionNode;
    }

}