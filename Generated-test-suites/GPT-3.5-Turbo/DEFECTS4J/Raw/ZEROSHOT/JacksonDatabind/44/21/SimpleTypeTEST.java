import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        assertNotNull(simpleType);
        assertEquals(Integer.class, simpleType.getRawClass());
    }

    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        SimpleType.construct(Integer[].class);
    }

    @Test
    public void testNarrowWithSameClass() {
        SimpleType simpleType = SimpleType.construct(Integer.class);
        assertSame(simpleType, simpleType.narrow(Integer.class));
    }

    @Test
    public void testNarrowWithDifferentClass() {
        SimpleType simpleType = SimpleType.construct(Number.class);
        SimpleType narrowedType = simpleType.narrow(Integer.class);
        assertNotNull(narrowedType);
        assertEquals(Integer.class, narrowedType.getRawClass());
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.construct(String.class);
        try {
            simpleType.withContentType(SimpleType.construct(Integer.class));
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = SimpleType.construct(Integer.class);
        Object dummyHandler = new Object();
        SimpleType newType = simpleType.withTypeHandler(dummyHandler);
        assertNotNull(newType);
        assertEquals(dummyHandler, newType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = SimpleType.construct(String.class);
        try {
            simpleType.withContentTypeHandler(new Object());
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = SimpleType.construct(Integer.class);
        Object dummyHandler = new Object();
        SimpleType newType = simpleType.withValueHandler(dummyHandler);
        assertNotNull(newType);
        assertEquals(dummyHandler, newType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = SimpleType.construct(String.class);
        try {
            simpleType.withContentValueHandler(new Object());
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.construct(Integer.class);
        SimpleType newType = simpleType.withStaticTyping();
        assertNotNull(newType);
        assertTrue(newType.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.construct(Number.class);
        assertNull(simpleType.refine(Integer.class, TypeBindings.emptyBindings(), SimpleType.construct(Number.class), null));
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.construct(String.class);
        StringBuilder sb = simpleType.getErasedSignature(new StringBuilder());
        assertNotNull(sb);
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = SimpleType.construct(String.class);
        StringBuilder sb = simpleType.getGenericSignature(new StringBuilder());
        assertNotNull(sb);
    }

    @Test
    public void testToString() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertNotNull(simpleType.toString());
    }

    @Test
    public void testEqualsWithSameObject() {
        SimpleType simpleType = SimpleType.construct(Integer.class);
        assertTrue(simpleType.equals(simpleType));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        SimpleType simpleType1 = SimpleType.construct(Number.class);
        SimpleType simpleType2 = SimpleType.construct(Integer.class);
        assertFalse(simpleType1.equals(simpleType2));
    }

    @Test
    public void testEqualsWithNull() {
        SimpleType simpleType = SimpleType.construct(Number.class);
        assertFalse(simpleType.equals(null));
    }

    @Test
    public void testEqualsWithDifferentType() {
        SimpleType simpleType = SimpleType.construct(Number.class);
        assertFalse(simpleType.equals(new Object()));
    }

    @Test
    public void testEqualsWithSameClassAndBindings() {
        SimpleType simpleType1 = SimpleType.construct(Number.class);
        SimpleType simpleType2 = SimpleType.construct(Number.class);
        assertTrue(simpleType1.equals(simpleType2));
    }

    @Test
    public void testEqualsWithDifferentBindings() {
        SimpleType simpleType1 = SimpleType.construct(Integer.class);
        SimpleType simpleType2 = SimpleType.construct(Float.class);
        assertFalse(simpleType1.equals(simpleType2));
    }

    @Test
    public void testEqualsWithDifferentSuperClass() {
        SimpleType simpleType1 = SimpleType.construct(Number.class);
        SimpleType simpleType2 = SimpleType.construct(Integer.class);
        assertFalse(simpleType1.equals(simpleType2));
    }
}