import org.junit.Test;
import static org.junit.Assert.*;

public class NamedTypeTest {

    @Test
    public void testConstructor() {
        NamedType namedType = new NamedType(new JSTypeRegistry(), "reference",
                "sourceName", 10, 5);

        assertNotNull(namedType);
        assertEquals("reference", namedType.getReferenceName());
        assertEquals("sourceName", namedType.getSourceName());
        assertEquals(10, namedType.getLineNumber());
        assertEquals(5, namedType.getCharNumber());
        assertEquals(JSTypeNative.UNKNOWN_TYPE, namedType.getBaseType());
        assertNull(namedType.getPropertyContinuations());
    }

    @Test
    public void testDefinePropertyBeforeResolution() {
        NamedType namedType = new NamedType(new JSTypeRegistry(), "reference",
                "sourceName", 10, 5);

        assertTrue(namedType.defineProperty("propertyName", JSTypeNative.STRING_TYPE,
                false, null));

        assertNotNull(namedType.getPropertyContinuations());
        assertEquals(1, namedType.getPropertyContinuations().size());
        assertEquals("propertyName", namedType.getPropertyContinuations().get(0).getPropertyName());
        assertEquals(JSTypeNative.STRING_TYPE, namedType.getPropertyContinuations().get(0).getType());
        assertFalse(namedType.getPropertyContinuations().get(0).isInferred());
        assertNull(namedType.getPropertyContinuations().get(0).getPropertyNode());
    }

    @Test
    public void testDefinePropertyAfterResolution() {
        NamedType namedType = new NamedType(new JSTypeRegistry(), "reference",
                "sourceName", 10, 5);

        namedType.resolveInternal(null, null);
        assertTrue(namedType.isResolved());

        assertFalse(namedType.defineProperty("propertyName", JSTypeNative.STRING_TYPE,
                false, null));
    }

    @Test
    public void testSetReferencedAndResolvedType() {
        NamedType namedType = new NamedType(new JSTypeRegistry(), "reference",
                "sourceName", 10, 5);
        namedType.setReferencedAndResolvedType(JSTypeNative.BOOLEAN_TYPE, null, null);

        assertEquals(JSTypeNative.BOOLEAN_TYPE, namedType.getReferencedType());
        assertEquals(JSTypeNative.BOOLEAN_TYPE, namedType.getResolvedTypeInternal());
    }

    @Test
    public void testHandleTypeCycle() {
        NamedType namedType = new NamedType(new JSTypeRegistry(), "reference",
                "sourceName", 10, 5);
        namedType.handleTypeCycle(null);

        assertEquals(JSTypeNative.UNKNOWN_TYPE, namedType.getReferencedType());
        assertEquals(JSTypeNative.UNKNOWN_TYPE, namedType.getResolvedTypeInternal());
    }

    @Test
    public void testCheckEnumElementCycle() {
        NamedType namedType = new NamedType(new JSTypeRegistry(), "reference",
                "sourceName", 10, 5);

        EnumElementType enumElementType = new EnumElementType(namedType);
        namedType.setReferencedType(enumElementType);
        namedType.checkEnumElementCycle(null);

        assertEquals(JSTypeNative.UNKNOWN_TYPE, namedType.getReferencedType());
        assertEquals(JSTypeNative.UNKNOWN_TYPE, namedType.getResolvedTypeInternal());
    }

    @Test
    public void testHandleUnresolvedType() {
        NamedType namedType = new NamedType(new JSTypeRegistry(), "reference",
                "sourceName", 10, 5);
        namedType.handleUnresolvedType(null, false);

        assertEquals(JSTypeNative.NO_RESOLVED_TYPE, namedType.getReferencedType());
        assertEquals(JSTypeNative.NO_RESOLVED_TYPE, namedType.getResolvedTypeInternal());
    }
}