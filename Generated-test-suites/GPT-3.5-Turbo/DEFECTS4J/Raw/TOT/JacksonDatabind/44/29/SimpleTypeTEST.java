import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe_validClass() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafe_invalidClass() {
        SimpleType.constructUnsafe(Map.class);
    }

    @Test
    public void testConstruct_validClass() {
        SimpleType type = SimpleType.construct(ArrayList.class);
        assertNotNull(type);
        assertEquals(ArrayList.class, type.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruct_invalidClass() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruct_arrayClass() {
        SimpleType.construct(String[].class);
    }

    @Test
    public void testNarrow_sameSubClass() {
        SimpleType type = SimpleType.constructUnsafe(Integer.class);
        SimpleType narrowedType = (SimpleType) type._narrow(Integer.class);
        assertNotNull(narrowedType);
        assertSame(narrowedType, type);
    }

    @Test
    public void testNarrow_differentSubClass() {
        SimpleType type = SimpleType.constructUnsafe(Number.class);
        SimpleType narrowedType = (SimpleType) type._narrow(Integer.class);
        assertNotNull(narrowedType);
        assertNotSame(narrowedType, type);
        assertEquals(Integer.class, narrowedType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrow_invalidSubClass() {
        SimpleType type = SimpleType.constructUnsafe(Integer.class);
        type._narrow(String.class);
    }

    @Test
    public void testWithContentType() {
        SimpleType type = SimpleType.constructUnsafe(List.class);
        try {
            type.withContentType(SimpleType.constructUnsafe(Integer.class));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType type = SimpleType.constructUnsafe(List.class);
        try {
            type.withContentTypeHandler(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithValueHandler_sameValueHandler() {
        SimpleType type = SimpleType.constructUnsafe(List.class);
        SimpleType modifiedType = type.withValueHandler(null);
        assertSame(type, modifiedType);
    }

    @Test
    public void testWithValueHandler_differentValueHandler() {
        SimpleType type = SimpleType.constructUnsafe(List.class);
        SimpleType modifiedType = type.withValueHandler("handler");
        assertNotSame(type, modifiedType);
        assertEquals("handler", modifiedType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType type = SimpleType.constructUnsafe(List.class);
        try {
            type.withContentValueHandler(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping_alreadyStatic() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        SimpleType modifiedType = type.withStaticTyping();
        assertSame(type, modifiedType);
    }

    @Test
    public void testWithStaticTyping_notStatic() {
        SimpleType type = SimpleType.constructUnsafe(ArrayList.class);
        SimpleType modifiedType = type.withStaticTyping();
        assertNotSame(type, modifiedType);
        assertTrue(modifiedType.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType type = SimpleType.constructUnsafe(List.class);
        JavaType refinedType = type.refine(ArrayList.class, null, null, null);
        assertNull(refinedType);
    }

    @Test
    public void testIsContainerType() {
        SimpleType type = SimpleType.constructUnsafe(List.class);
        assertFalse(type.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType type = SimpleType.constructUnsafe(List.class);
        StringBuilder signature = type.getErasedSignature(new StringBuilder());
        assertNotNull(signature);
        assertEquals("Ljava/util/List;", signature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType type = SimpleType.constructUnsafe(List.class);
        StringBuilder signature = type.getGenericSignature(new StringBuilder());
        assertNotNull(signature);
        assertEquals("Ljava/util/List<*>;", signature.toString());
    }

    @Test
    public void testToString() {
        SimpleType type = SimpleType.constructUnsafe(List.class);
        assertEquals("[simple type, class java.util.List]", type.toString());
    }

    @Test
    public void testEquals_sameType() {
        SimpleType type1 = SimpleType.constructUnsafe(String.class);
        SimpleType type2 = SimpleType.constructUnsafe(String.class);
        assertTrue(type1.equals(type2));
    }

    @Test
    public void testEquals_differentType() {
        SimpleType type1 = SimpleType.constructUnsafe(String.class);
        SimpleType type2 = SimpleType.constructUnsafe(Integer.class);
        assertFalse(type1.equals(type2));
    }

    @Test
    public void testEquals_differentBindings() {
        SimpleType type1 = SimpleType.constructUnsafe(List.class);
        SimpleType type2 = SimpleType.constructUnsafe(List.class);
        type1.setBindings(TypeBindings.create(Map.class, Integer.class), null);
        assertFalse(type1.equals(type2));
    }

}