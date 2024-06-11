import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli.ArrowType;
import org.junit.Test;

public class ArrowTypeTest {
    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType = new ArrowType(registry, null, null, false);
        assertFalse(arrowType.isSubtype(null));
        
        ArrowType that = new ArrowType(registry, null, null, false);
        assertTrue(arrowType.isSubtype(that));
        
        that = new ArrowType(registry, null, new JSType(registry), false);
        assertFalse(arrowType.isSubtype(that));
        
        that = new ArrowType(registry, null, null, true);
        assertFalse(arrowType.isSubtype(that));
    }

    @Test
    public void testHasEqualParameters() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType = new ArrowType(registry, null, null, false);
        assertTrue(arrowType.hasEqualParameters(null));
        
        ArrowType that = new ArrowType(registry, null, null, false);
        assertTrue(arrowType.hasEqualParameters(that));
        
        Node parameters = new Node();
        that = new ArrowType(registry, parameters, null, false);
        assertFalse(arrowType.hasEqualParameters(that));
        
        arrowType = new ArrowType(registry, parameters, null, false);
        assertTrue(arrowType.hasEqualParameters(that));
        
        Node paramNode = new Node();
        parameters.addChildToFront(paramNode);
        that = new ArrowType(registry, parameters, null, false);
        assertFalse(arrowType.hasEqualParameters(that));
        
        paramNode.setJSType(new JSType(registry));
        that = new ArrowType(registry, parameters, null, false);
        assertTrue(arrowType.hasEqualParameters(that));
    }

    @Test
    public void testIsEquivalentTo() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType = new ArrowType(registry, null, null, false);
        assertFalse(arrowType.isEquivalentTo(null));
        
        ArrowType that = new ArrowType(registry, null, null, false);
        assertTrue(arrowType.isEquivalentTo(that));
        
        that = new ArrowType(registry, null, new JSType(registry), false);
        assertFalse(arrowType.isEquivalentTo(that));
        
        that = new ArrowType(registry, null, null, true);
        assertFalse(arrowType.isEquivalentTo(that));
    }

    @Test
    public void testHashCode() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType = new ArrowType(registry, null, null, false);
        assertEquals(0, arrowType.hashCode());
        
        arrowType = new ArrowType(registry, null, new JSType(registry), false);
        assertEquals(registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE)), arrowType.hashCode());
        
        arrowType = new ArrowType(registry, null, null, true);
        assertEquals(1, arrowType.hashCode());
        
        Node parameters = new Node();
        Node paramNode = new Node();
        parameters.addChildToFront(paramNode);
        arrowType = new ArrowType(registry, parameters, null, false);
        assertEquals(registry.createParametersWithVarArgs(getNativeType(UNKNOWN_TYPE)), arrowType.hashCode());
        
        paramNode.setJSType(new JSType(registry));
        assertEquals(arrowType.parameters.hashCode() + paramNode.getJSType().hashCode(), arrowType.hashCode());
    }
    
    // More test methods for the remaining methods in ArrowType class
    
}