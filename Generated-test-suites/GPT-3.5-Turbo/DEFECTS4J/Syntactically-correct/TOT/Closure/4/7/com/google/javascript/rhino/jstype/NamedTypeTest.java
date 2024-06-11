import org.junit.Test;
import static org.junit.Assert.*;

public class NamedTypeTest {

    @Test
    public void testResolveInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();
        NamedType namedType = new NamedType(registry, "com.google.javascript.rhino.jstype.TestType", "TestSource", 1, 2);
        
        // Test that the type is resolved correctly when it exists in the type registry
        JSType resolvedType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        registry.addType("com.google.javascript.rhino.jstype.TestType", resolvedType);
        namedType.resolveInternal(null, null);
        assertEquals(resolvedType, namedType.getReferencedType());
        assertTrue(namedType.isResolved());
        
        // Test that the type remains unresolved when it does not exist in the type registry
        namedType = new NamedType(registry, "com.google.javascript.rhino.jstype.NonExistentType", "TestSource", 1, 2);
        namedType.resolveInternal(null, null);
        assertEquals(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), namedType.getReferencedType());
        assertFalse(namedType.isResolved());
    }

    @Test
    public void testSetValidator() {
        JSTypeRegistry registry = new JSTypeRegistry();
        NamedType namedType = new NamedType(registry, "com.google.javascript.rhino.jstype.TestType", "TestSource", 1, 2);
        
        // Test that the validator is set correctly when the type is already resolved
        JSType resolvedType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        registry.addType("com.google.javascript.rhino.jstype.TestType", resolvedType);
        namedType.resolveInternal(null, null);
        assertTrue(namedType.setValidator(type -> true));

        // Test that the validator is set correctly when the type is not yet resolved
        namedType = new NamedType(registry, "com.google.javascript.rhino.jstype.NonExistentType", "TestSource", 1, 2);
        assertTrue(namedType.setValidator(type -> true));
    }

    @Test
    public void testDefineProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        NamedType namedType = new NamedType(registry, "com.google.javascript.rhino.jstype.TestType", "TestSource", 1, 2);
        
        // Test that the property is defined correctly when the type is resolved
        JSType resolvedType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        registry.addType("com.google.javascript.rhino.jstype.TestType", resolvedType);
        namedType.resolveInternal(null, null);
        assertTrue(namedType.defineProperty("property", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null));
        
        // Test that the property is not defined when the type is unresolved
        NamedType unresolvedType = new NamedType(registry, "com.google.javascript.rhino.jstype.NonExistentType", "TestSource", 1, 2);
        assertFalse(unresolvedType.isResolved());
        assertFalse(unresolvedType.defineProperty("property", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null));
    }
}