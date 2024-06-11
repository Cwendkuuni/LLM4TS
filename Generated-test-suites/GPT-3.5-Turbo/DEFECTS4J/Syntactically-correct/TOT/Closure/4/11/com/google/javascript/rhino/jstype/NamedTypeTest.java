import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class NamedTypeTest {

    private NamedType namedType;
    private JSTypeRegistry registry;
    private ErrorReporter errorReporter;
    private StaticScope<JSType> enclosing;
    
    @Before
    public void setUp() {
        registry = mock(JSTypeRegistry.class);
        namedType = new NamedType(registry, "Test", "source", 1, 1);
        errorReporter = mock(ErrorReporter.class);
        enclosing = mock(StaticScope.class);
    }
    
    @Test
    public void testResolveInternal_ValidType_ReturnsResolvedType() {
        JSType type = new JSType();
        namedType.setReferencedAndResolvedType(type, errorReporter, enclosing);
        JSType resolvedType = namedType.resolveInternal(errorReporter, enclosing);
        Assert.assertEquals(type, resolvedType);
    }
    
    @Test
    public void testResolveInternal_NullRegistryType_ReturnsReferencedType() {
        namedType.setReferencedType(mock(JSType.class));
        JSType resolvedType = namedType.resolveInternal(errorReporter, enclosing);
        Assert.assertEquals(namedType.getReferencedType(), resolvedType);
    }
    
    @Test
    public void testResolveInternal_UnknownRegistryType_ReturnsUnknownType() {
        namedType.setReferencedType(null);
        JSType resolvedType = namedType.resolveInternal(errorReporter, enclosing);
        Assert.assertEquals(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), resolvedType);
    }

    @Test
    public void testResolveInternal_ErrorResolvingType_ReturnsUnknownType() {
        namedType.setReferencedType(null);
        namedType.setResolvedTypeInternal(null);
        JSType resolvedType = namedType.resolveInternal(errorReporter, enclosing);
        Assert.assertEquals(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), resolvedType);
    }
    
    @Test
    public void testDefineProperty_UnresolvedType_SavesPropertyContinuation() {
        namedType.defineProperty("propertyName", mock(JSType.class), true, mock(Node.class));
        Assert.assertNotNull(namedType.propertyContinuations);
        Assert.assertTrue(namedType.propertyContinuations.size() > 0);
    }
    
    @Test
    public void testFinishPropertyContinuations_ResolvedType_DefinesProperties() {
        JSType type = new JSType();
        namedType.setReferencedAndResolvedType(type, errorReporter, enclosing);
        namedType.defineProperty("propertyName", mock(JSType.class), true, mock(Node.class));
        namedType.finishPropertyContinuations();
        Assert.assertFalse(namedType.propertyContinuations.size() > 0);
    }
}