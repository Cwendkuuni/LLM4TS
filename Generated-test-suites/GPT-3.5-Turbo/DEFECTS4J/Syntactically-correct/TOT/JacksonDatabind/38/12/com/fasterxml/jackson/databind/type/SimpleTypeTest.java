import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

import java.util.Collection;
import java.util.Map;

import static org.junit.Assert.*;

public class SimpleTypeTest {

    private SimpleType simpleType;

    @Before
    public void setUp() {
        simpleType = SimpleType.constructUnsafe(String.class);
    }

    @Test
    public void testConstructor_WithClass_SetsClassCorrectly() {
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test
    public void testConstructor_WithClass_SetsBindingsCorrectly() {
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
    }

    @Test
    public void testConstructor_WithClassAndBindings_SetsClassCorrectly() {
        TypeBindings bindings = new TypeBindings();
        simpleType = new SimpleType(String.class, bindings, null, null);

        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test
    public void testConstructor_WithClassAndBindings_SetsBindingsCorrectly() {
        TypeBindings bindings = new TypeBindings();
        simpleType = new SimpleType(String.class, bindings, null, null);

        assertEquals(bindings, simpleType.getBindings());
    }

    @Test
    public void testConstructor_WithClassAndBindingsAndSuperClassAndSuperInterfaces_SetsSuperClassCorrectly() {
        JavaType superClass = SimpleType.constructUnsafe(Object.class);
        simpleType = new SimpleType(String.class, null, superClass, null);

        assertEquals(superClass, simpleType.getSuperClass());
    }

    @Test
    public void testConstructor_WithClassAndBindingsAndSuperClassAndSuperInterfaces_SetsSuperInterfacesCorrectly() {
        JavaType[] superInterfaces = { SimpleType.constructUnsafe(Collection.class) };
        simpleType = new SimpleType(String.class, null, null, superInterfaces);

        assertArrayEquals(superInterfaces, simpleType.getInterfaces());
    }

    @Test
    public void testConstructUnsafe_ReturnsSimpleTypeWithCorrectClass() {
        SimpleType result = SimpleType.constructUnsafe(Map.class);

        assertEquals(Map.class, result.getRawClass());
    }

    @Test
    public void testConstructUnsafe_ReturnsSimpleTypeWithNullBindings() {
        SimpleType result = SimpleType.constructUnsafe(Map.class);

        assertNull(result.getBindings());
    }

    @Test
    public void testConstructUnsafe_ReturnsSimpleTypeWithNullSuperClass() {
        SimpleType result = SimpleType.constructUnsafe(Map.class);

        assertNull(result.getSuperClass());
    }

    @Test
    public void testConstructUnsafe_ReturnsSimpleTypeWithNullSuperInterfaces() {
        SimpleType result = SimpleType.constructUnsafe(Map.class);

        assertNull(result.getInterfaces());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruct_WithMapClass_ThrowsIllegalArgumentException() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruct_WithCollectionClass_ThrowsIllegalArgumentException() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruct_WithArrayClass_ThrowsIllegalArgumentException() {
        SimpleType.construct(String[].class);
    }

    @Test
    public void testNarrow_WithSameSubClass_ReturnsThis() {
        JavaType result = simpleType.narrow(String.class);

        assertSame(simpleType, result);
    }

    @Test
    public void testNarrow_WithDifferentSubClass_ReturnsNewSimpleTypeWithSubClass() {
        JavaType result = simpleType.narrow(Object.class);

        assertEquals(Object.class, result.getRawClass());
    }

    @Test
    public void testWithContentType_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> simpleType.withContentType(SimpleType.constructUnsafe(String.class)));
    }

    @Test
    public void testWithContentTypeHandler_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> simpleType.withContentTypeHandler("handler"));
    }

    @Test
    public void testWithContenValueHandler_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> simpleType.withContentValueHandler("handler"));
    }

    @Test
    public void testWithStaticTyping_WhenAsStaticIsTrue_ReturnsThis() {
        JavaType result = simpleType.withStaticTyping();

        assertSame(simpleType, result);
    }

    @Test
    public void testWithStaticTyping_WhenAsStaticIsFalse_ReturnsNewSimpleTypeWithStaticTyping() {
        simpleType = SimpleType.constructUnsafe(String.class);
        simpleType = simpleType.withStaticTyping();

        assertTrue(simpleType.isStatic());
    }

    @Test
    public void testRefine_ReturnsNull() {
        JavaType result = simpleType.refine(String.class, null, null, null);

        assertNull(result);
    }

    @Test
    public void testIsContainerType_ReturnsFalse() {
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature_ReturnsClassSignatureStringBuilder() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = simpleType.getErasedSignature(sb);

        assertNotNull(result);
        assertEquals(sb, result);
    }

    @Test
    public void testGetGenericSignature_ReturnsGenericSignatureStringBuilder() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = simpleType.getGenericSignature(sb);

        assertNotNull(result);
        assertEquals(sb, result);
    }

    @Test
    public void testToString_ReturnsSimpleTypeString() {
        String result = simpleType.toString();

        assertEquals("[simple type, class java.lang.String]", result);
    }

    @Test
    public void testEquals_WithSameSimpleType_ReturnsTrue() {
        assertTrue(simpleType.equals(simpleType));
    }

    @Test
    public void testEquals_WithNull_ReturnsFalse() {
        assertFalse(simpleType.equals(null));
    }

    @Test
    public void testEquals_WithDifferentClass_ReturnsFalse() {
        assertFalse(simpleType.equals(SimpleType.constructUnsafe(Object.class)));
    }

    @Test
    public void testEquals_WithDifferentBindings_ReturnsFalse() {
        SimpleType other = SimpleType.constructUnsafe(String.class);
        other = new SimpleType(String.class, new TypeBindings(), null, null);

        assertFalse(simpleType.equals(other));
    }

    @Test
    public void testSerializable() {
        assertSerializable(simpleType);
    }

    private void assertSerializable(SimpleType type) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(type);
            out.close();

            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bis);
            SimpleType deserialized = (SimpleType) in.readObject();

            assertEquals(type, deserialized);
        } catch (Exception e) {
            fail("Exception during serialization: " + e);
        }
    }
}