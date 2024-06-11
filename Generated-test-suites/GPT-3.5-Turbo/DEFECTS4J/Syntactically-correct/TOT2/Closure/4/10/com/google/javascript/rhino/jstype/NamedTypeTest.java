import org.junit.Test;
import static org.junit.Assert.*;

public class NamedTypeTest {

    @Test
    public void testNamedTypeCreation() {
        JSTypeRegistry registry = new JSTypeRegistry();
        NamedType namedType = new NamedType(registry, "SomeType", "test", 1, 1);
        
        assertEquals("SomeType", namedType.getReferenceName());
        assertEquals("test", namedType.getSourceName());
        assertEquals(1, namedType.getLineno());
        assertEquals(1, namedType.getCharno());
        assertNotNull(namedType);
    }

    @Test
    public void testDefineProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        NamedType namedType = new NamedType(registry, "SomeType", "test", 1, 1);
        
        assertFalse(namedType.defineProperty("prop1", registry.getNativeObjectType(JSTypeNative.STRING_TYPE), false, null));
    }

    @Test
    public void testResolveViaRegistry() {
        JSTypeRegistry registry = new JSTypeRegistry();
        registry.registerType("SomeType", registry.getNativeObjectType(JSTypeNative.NUMBER_TYPE));
        
        NamedType namedType = new NamedType(registry, "SomeType", "test", 1, 1);
        namedType.resolveInternal(new ErrorReporter(), new StaticScope<JSType>());
        
        assertEquals(registry.getNativeObjectType(JSTypeNative.NUMBER_TYPE), namedType.getReferencedType());
    }

    @Test
    public void testLookupViaProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();
        StaticScope<JSType> enclosingScope = new StaticScope<>();
        StaticSlot<JSType> testSlot = new StaticSlot<>(registry.getNativeObjectType(JSTypeNative.STRING_TYPE));
        enclosingScope.addSlot("TestType", testSlot);
        
        NamedType namedType = new NamedType(registry, "TestType.prop1", "test", 1, 1);
        JSType value = namedType.lookupViaProperties(new ErrorReporter(), enclosingScope);
        
        assertNotNull(value);
        assertEquals(registry.getNativeObjectType(JSTypeNative.STRING_TYPE), value);
    }

    @Test
    public void testSetValidator() {
        JSTypeRegistry registry = new JSTypeRegistry();
        NamedType namedType = new NamedType(registry, "SomeType", "test", 1, 1);
        
        Predicate<JSType> validator = type -> type.isUnknownType();
        
        assertTrue(namedType.setValidator(validator));
    }

    @Test
    public void testHandleUnresolvedType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        NamedType namedType = new NamedType(registry, "UnknownType", "test", 1, 1);
        
        namedType.resolveInternal(new ErrorReporter(), new StaticScope<JSType>());
        
        assertEquals(registry.getNativeObjectType(JSTypeNative.NO_RESOLVED_TYPE), namedType.getReferencedType());
    }
}