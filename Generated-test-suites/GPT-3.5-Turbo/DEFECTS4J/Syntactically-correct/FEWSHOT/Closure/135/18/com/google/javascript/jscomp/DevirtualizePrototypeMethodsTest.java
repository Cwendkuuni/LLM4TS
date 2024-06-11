import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class DevirtualizePrototypeMethodsTest {

    @Test
    public void testIsCall() {
        assertFalse(DevirtualizePrototypeMethods.isCall(new UseSite(null, null)));
        assertFalse(DevirtualizePrototypeMethods.isCall(new UseSite(new Node(null, null), null)));
        assertTrue(DevirtualizePrototypeMethods.isCall(new UseSite(new Node(null, null), new Node(null, null))));
    }

    @Test
    public void testIsPrototypeMethodDefinition() {
        assertFalse(DevirtualizePrototypeMethods.isPrototypeMethodDefinition(null));
        assertFalse(DevirtualizePrototypeMethods.isPrototypeMethodDefinition(new Node(null, null)));
        assertFalse(DevirtualizePrototypeMethods.isPrototypeMethodDefinition(new Node(null, null, new Node(null, null))));
        assertFalse(DevirtualizePrototypeMethods.isPrototypeMethodDefinition(new Node(null, null, new Node(null, null, new Node(null, null)))));
        assertFalse(DevirtualizePrototypeMethods.isPrototypeMethodDefinition(new Node(null, null, new Node(null, null, new Node(null, null, new Node(null, null))))));
        assertTrue(DevirtualizePrototypeMethods.isPrototypeMethodDefinition(new Node(null, null, new Node(null, null, new Node(null, null, new Node(null, null, new Node(null, null)))))));
    }

    @Test
    public void testGetRewrittenMethodName() {
        assertEquals("JSCompiler_StaticMethods_foo", DevirtualizePrototypeMethods.getRewrittenMethodName("foo"));
    }

    @Test
    public void testIsEligibleDefinition() {
        SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(null);
        assertFalse(DevirtualizePrototypeMethods.isEligibleDefinition(defFinder, null));
        Definition definition = new Definition(new Node(null, null), new Node(null, null));
        assertTrue(DevirtualizePrototypeMethods.isEligibleDefinition(defFinder, new DefinitionSite(definition, true, true, null)));
    }

}