
import static org.junit.Assert.*;
import org.junit.Test;

public class DevirtualizePrototypeMethodsTest {

    @Test
    public void testIsCall_True() {
        DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
        assertTrue(dpm.isCall(new UseSite()));
    }

    @Test
    public void testIsCall_False() {
        DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
        assertFalse(dpm.isCall(new UseSite()));
    }

    @Test
    public void testIsPrototypeMethodDefinition_True() {
        DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
        assertTrue(dpm.isPrototypeMethodDefinition(new Node()));
    }

    @Test
    public void testIsPrototypeMethodDefinition_False() {
        DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
        assertFalse(dpm.isPrototypeMethodDefinition(new Node()));
    }

    @Test
    public void testGetRewrittenMethodName() {
        DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
        String expected = "JSCompiler_StaticMethods_orginalMethodName";
        assertEquals(expected, dpm.getRewrittenMethodName("orginalMethodName"));
    }

    @Test
    public void testIsEligibleDefinition_True() {
        DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
        SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder();
        DefinitionSite definitionSite = new DefinitionSite();
        assertTrue(dpm.isEligibleDefinition(defFinder, definitionSite));
    }

    @Test
    public void testIsEligibleDefinition_False() {
        DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
        SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder();
        DefinitionSite definitionSite = new DefinitionSite();
        assertFalse(dpm.isEligibleDefinition(defFinder, definitionSite));
    }

    @Test
    public void testRewriteCallSites() {
        DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
        SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder();
        Definition definition = new Definition();
        String newMethodName = "JSCompiler_StaticMethods_newMethodName";
        dpm.rewriteCallSites(defFinder, definition, newMethodName);
    }

    @Test
    public void testRewriteDefinition() {
        DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
        Node node = new Node();
        String newMethodName = "JSCompiler_StaticMethods_newMethodName";
        dpm.rewriteDefinition(node, newMethodName);
    }

    @Test
    public void testFixFunctionType() {
        DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
        Node functionNode = new Node();
        dpm.fixFunctionType(functionNode);
    }

    @Test
    public void testReplaceReferencesToThis() {
        DevirtualizePrototypeMethods dpm = new DevirtualizePrototypeMethods();
        Node node = new Node();
        String name = "name";
        dpm.replaceReferencesToThis(node, name);
    }
}
