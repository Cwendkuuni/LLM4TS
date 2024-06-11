import org.junit.Test;
import static org.junit.Assert.*;

public class NamedTypeTest {

    @Test
    public void testResolveInternal_UnknownType() {
        NamedType nt = new NamedType(null, "UnknownType", null, 0, 0);
        nt.resolveInternal(null, null);
        assertFalse(nt.isResolved());
        assertEquals(JSTypeNative.UNKNOWN_TYPE, nt.getResolvedType());
    }
    
    @Test
    public void testResolveInternal_KnownType() {
        NamedType nt = new NamedType(null, "KnownType", null, 0, 0);
        nt.resolveInternal(null, null);
        assertTrue(nt.isResolved());
        assertEquals("KnownType", nt.getResolvedType());
    }
    
    @Test
    public void testResolveInternal_UnknownPropertyType() {
        NamedType nt = new NamedType(null, "Object.unknownProperty", null, 0, 0);
        nt.resolveInternal(null, null);
        assertFalse(nt.isResolved());
        assertEquals(JSTypeNative.UNKNOWN_TYPE, nt.getResolvedType());
    }
    
    @Test
    public void testResolveInternal_KnownPropertyType() {
        NamedType nt = new NamedType(null, "Object.knownProperty", null, 0, 0);
        nt.resolveInternal(null, null);
        assertTrue(nt.isResolved());
        assertEquals("KnownPropertyType", nt.getResolvedType());
    }
    
    @Test
    public void testResolveInternal_FunctionType() {
        NamedType nt = new NamedType(null, "FunctionType", null, 0, 0);
        nt.resolveInternal(null, null);
        assertTrue(nt.isResolved());
        assertEquals("FunctionType", nt.getResolvedType());
    }
    
    @Test
    public void testResolveInternal_EnumType() {
        NamedType nt = new NamedType(null, "EnumType", null, 0, 0);
        nt.resolveInternal(null, null);
        assertTrue(nt.isResolved());
        assertEquals("EnumType", nt.getResolvedType());
    }

    @Test
    public void testResolveInternal_UnresolvedType() {
        NamedType nt = new NamedType(null, "UnresolvedType", null, 0, 0);
        nt.resolveInternal(null, null);
        assertFalse(nt.isResolved());
        assertEquals(JSTypeNative.UNKNOWN_TYPE, nt.getResolvedType());
    }
}