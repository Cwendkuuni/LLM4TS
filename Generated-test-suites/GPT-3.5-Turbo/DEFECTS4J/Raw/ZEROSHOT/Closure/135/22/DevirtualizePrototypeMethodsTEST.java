import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class DevirtualizePrototypeMethodsTest {

    @Test
    public void testIsEligibleDefinition() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        DevirtualizePrototypeMethods devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);

        // Test case 1: Definition is not a function
        SimpleDefinitionFinder defFinder = mock(SimpleDefinitionFinder.class);
        DefinitionSite definitionSite = new DefinitionSite(null, null, defFinder);
        Definition definition = new Definition(null, null);
        Node rvalue = mock(Node.class);
        when(rvalue.getBooleanProp(Node.SYNTHETIC_BLOCK)).thenReturn(false);
        when(rvalue.isFunction()).thenReturn(false);
        definition.setRValue(rvalue);
        definitionSite.definition = definition;
        boolean result = devirtualizePrototypeMethods.isEligibleDefinition(defFinder, definitionSite);
        assertFalse(result);

        // Test case 2: Definition is a function but has var args
        definition = new Definition(null, null);
        rvalue = mock(Node.class);
        when(rvalue.getBooleanProp(Node.SYNTHETIC_BLOCK)).thenReturn(false);
        when(rvalue.isFunction()).thenReturn(true);
        definition.setRValue(rvalue);
        Node paramNode = mock(Node.class);
        when(paramNode.getBooleanProp(Node.VARARGS)).thenReturn(true);
        Node paramList = mock(Node.class);
        when(paramList.getFirstChild()).thenReturn(paramNode);
        when(rvalue.getSecondChild()).thenReturn(paramList);
        definitionSite.definition = definition;
        result = devirtualizePrototypeMethods.isEligibleDefinition(defFinder, definitionSite);
        assertFalse(result);

        // Test case 3: Definition is a function and does not have var args, but is exported
        definition = new Definition(null, null);
        rvalue = mock(Node.class);
        when(rvalue.getBooleanProp(Node.SYNTHETIC_BLOCK)).thenReturn(false);
        when(rvalue.isFunction()).thenReturn(true);
        definition.setRValue(rvalue);
        lvalue = mock(Node.class);
        when(lvalue .isGetProp()).thenReturn(true);
        when(lvalue .getLastChild()).thenReturn("exportedMethod");
        when(codingConvention.isExported("exportedMethod")).thenReturn(true);
        definition.setLValue(lvalue);
        definitionSite.definition = definition;
        result = devirtualizePrototypeMethods.isEligibleDefinition(defFinder, definitionSite);
        assertFalse(result);

        // Test case 4: Definition is a function and does not have var args, is not exported, and has no use sites
        definition = new Definition(null, null);
        rvalue = mock(Node.class);
        when(rvalue.getBooleanProp(Node.SYNTHETIC_BLOCK)).thenReturn(false);
        when(rvalue.isFunction()).thenReturn(true);
        definition.setRValue(rvalue);
        definitionSite.definition = definition;
        result = devirtualizePrototypeMethods.isEligibleDefinition(defFinder, definitionSite);
        assertFalse(result);

        // Test case 5: Definition is a function and does not have var args, is not exported, and has use sites
        definition = new Definition(null, null);
        rvalue = mock(Node.class);
        when(rvalue.getBooleanProp(Node.SYNTHETIC_BLOCK)).thenReturn(false);
        when(rvalue.isFunction()).thenReturn(true);
        definition.setRValue(rvalue);
        definitionSite.definition = definition;
        UseSite useSite = new UseSite(null, null, null);
        when(defFinder.getUseSites(definition)).thenReturn(Collections.singleton(useSite));
        result = devirtualizePrototypeMethods.isEligibleDefinition(defFinder, definitionSite);
        assertTrue(result);
    }
}