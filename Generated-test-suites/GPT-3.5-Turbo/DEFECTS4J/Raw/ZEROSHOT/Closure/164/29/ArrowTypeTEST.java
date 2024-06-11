import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.Node;

public class ArrowTypeTest {
    
    private JSTypeRegistry registry;
    
    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
    }
    
    @Test
    public void testConstructorWithParametersAndReturnType() {
        Node parameters = new Node();
        JSType returnType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        ArrowType arrowType = new ArrowType(registry, parameters, returnType, false);
        
        assertNotNull(arrowType);
        assertEquals(parameters, arrowType.parameters);
        assertEquals(returnType, arrowType.returnType);
        assertFalse(arrowType.returnTypeInferred);
    }
    
    @Test
    public void testConstructorWithParameters() {
        Node parameters = new Node();
        ArrowType arrowType = new ArrowType(registry, parameters, null);
        
        assertNotNull(arrowType);
        assertEquals(parameters, arrowType.parameters);
        assertEquals(registry.getNativeType(JSTypeNative.UNKNOWN_TYPE), arrowType.returnType);
        assertFalse(arrowType.returnTypeInferred);
    }
    
    @Test
    public void testConstructorWithoutParametersAndReturnType() {
        ArrowType arrowType = new ArrowType(registry, null, null);
        
        assertNotNull(arrowType);
        assertEquals(registry.createParametersWithVarArgs(registry.getNativeType(JSTypeNative.UNKNOWN_TYPE)), arrowType.parameters);
        assertEquals(registry.getNativeType(JSTypeNative.UNKNOWN_TYPE), arrowType.returnType);
        assertFalse(arrowType.returnTypeInferred);
    }
    
    @Test
    public void testEqualParameters() {
        Node parameters = new Node();
        ArrowType arrowType1 = new ArrowType(registry, parameters, null);
        ArrowType arrowType2 = new ArrowType(registry, parameters, null);
        
        assertTrue(arrowType1.hasEqualParameters(arrowType2));
    }
    
    @Test
    public void testNotEqualParameters() {
        Node parameters1 = new Node();
        Node parameters2 = new Node();
        ArrowType arrowType1 = new ArrowType(registry, parameters1, null);
        ArrowType arrowType2 = new ArrowType(registry, parameters2, null);
        
        assertFalse(arrowType1.hasEqualParameters(arrowType2));
    }
    
    @Test
    public void testIsSubtypeWithDifferentReturnType() {
        Node parameters = new Node();
        JSType returnType1 = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        JSType returnType2 = registry.getNativeType(JSTypeNative.BOOLEAN_TYPE);
        ArrowType arrowType1 = new ArrowType(registry, parameters, returnType1);
        ArrowType arrowType2 = new ArrowType(registry, parameters, returnType2);
        
        assertFalse(arrowType1.isSubtype(arrowType2));
    }
    
    @Test
    public void testIsSubtypeWithDifferentParameters() {
        Node parameters1 = new Node();
        Node parameters2 = new Node();
        ArrowType arrowType1 = new ArrowType(registry, parameters1, null);
        ArrowType arrowType2 = new ArrowType(registry, parameters2, null);
        
        assertFalse(arrowType1.isSubtype(arrowType2));
    }
    
    @Test
    public void testIsSubtypeWithEqualParameters() {
        Node parameters = new Node();
        ArrowType arrowType1 = new ArrowType(registry, parameters, null);
        ArrowType arrowType2 = new ArrowType(registry, parameters, null);
        
        assertTrue(arrowType1.isSubtype(arrowType2));
    }

    @Test
    public void testIsEquivalentTo() {
        Node parameters = new Node();
        JSType returnType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
        ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
        
        assertTrue(arrowType1.isEquivalentTo(arrowType2));
    }
    
    @Test
    public void testGetHashCode() {
        Node parameters = new Node();
        JSType returnType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
        ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
        
        assertEquals(arrowType1.hashCode(), arrowType2.hashCode());
    }
}