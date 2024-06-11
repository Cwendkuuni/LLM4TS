import org.junit.Test;
import static org.junit.Assert.*;

public class DevirtualizePrototypeMethodsTest {

    @Test
    public void testIsCall_validCallExpression_returnTrue() {
        DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
        // Test a valid call expression
        UseSite useSite = new UseSite(new Node(), new Definition());
        assertTrue(dpm.isCall(useSite));
    }

    @Test
    public void testIsCall_invalidCallExpression_returnFalse() {
        DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
        // Test an invalid call expression
        UseSite useSite = new UseSite(new Node(), new Definition());
        assertFalse(dpm.isCall(useSite));
    }

    @Test
    public void testIsPrototypeMethodDefinition_validMethodDefinition_returnTrue() {
        DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
        // Test a valid method definition
        Node node = new Node();
        assertTrue(dpm.isPrototypeMethodDefinition(node));
    }

    @Test
    public void testIsPrototypeMethodDefinition_invalidMethodDefinition_returnFalse() {
        DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
        // Test an invalid method definition
        Node node = new Node();
        assertFalse(dpm.isPrototypeMethodDefinition(node));
    }

    @Test
    public void testGetRewrittenMethodName_validMethodName_returnCorrectNewMethodName() {
        DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
        // Test a valid method name
        String originalMethodName = "accumulate";
        String expectedNewMethodName = "JSCompiler_StaticMethods_accumulate";
        assertEquals(expectedNewMethodName, dpm.getRewrittenMethodName(originalMethodName));
    }

    @Test
    public void testGetRewrittenMethodName_invalidMethodName_returnNull() {
        DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
        // Test an invalid method name
        String originalMethodName = null;
        assertNull(dpm.getRewrittenMethodName(originalMethodName));
    }

    @Test
    public void testIsEligibleDefinition_validDefinition_returnTrue() {
        DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
        // Test a valid definition
        SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(new AbstractCompiler());
        DefinitionSite definitionSite = new DefinitionSite(new Node(), defFinder.getDefinition());
        assertTrue(dpm.isEligibleDefinition(defFinder, definitionSite));
    }

    @Test
    public void testIsEligibleDefinition_invalidDefinition_returnFalse() {
        DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
        // Test an invalid definition
        SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(new AbstractCompiler());
        DefinitionSite definitionSite = new DefinitionSite(new Node(), defFinder.getDefinition());
        assertFalse(dpm.isEligibleDefinition(defFinder, definitionSite));
    }

    @Test
    public void testRewriteCallSites_validCallSites_callSitesRewrittenSuccessfully() {
        DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
        // Test valid call sites
        SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(new AbstractCompiler());
        Definition definition = defFinder.getDefinition();
        String newMethodName = dpm.getRewrittenMethodName(definition.getLastChild().getString());
        UseSite useSite1 = new UseSite(new Node(), definition);
        UseSite useSite2 = new UseSite(new Node(), definition);
        UseSite useSite3 = new UseSite(new Node(), definition);
        Collection<UseSite> useSites = Arrays.asList(useSite1, useSite2, useSite3);
        dpm.rewriteCallSites(defFinder, definition, newMethodName);
        
        // Assert that call sites have been rewritten successfully
        for (UseSite useSite : useSites) {
            assertEquals(newMethodName, useSite.node.getParent().getChildAtIndex(0).getString());
        }
    }

    @Test
    public void testRewriteDefinition_validDefinition_definitionRewrittenSuccessfully() {
        DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
        // Test a valid definition
        Node node = new Node();
        String newMethodName = "testMethod";
        dpm.rewriteDefinition(node, newMethodName);
        
        // Assert that definition has been rewritten successfully
        assertEquals(newMethodName, node.getParent().getFirstChild().getString());
    }

    @Test
    public void testReplaceReferencesToThis_validReferences_replacementSuccessful() {
        DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
        // Test valid references to "this"
        Node node = new Node();
        String name = "testName";
        String thisReplacement = name + "$self";
        dpm.replaceReferencesToThis(node, name);
    
        // Assert that references to "this" have been replaced successfully
        for (Node child : node.children()) {
            assertNotEquals("this", child.getString());
            assertEquals(thisReplacement, child.getString());
        }
    }

}