
import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class SimpleTypeTest {

    private SimpleType simpleType;

    @Before
    public void setUp() {
        simpleType = new SimpleType();
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType newSimpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(newSimpleType);
        assertEquals(String.class, newSimpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithMapClass_ShouldThrowIllegalArgumentException() {
        SimpleType.constructUnsafe(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithCollectionClass_ShouldThrowIllegalArgumentException() {
        SimpleType.constructUnsafe(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithArrayClass_ShouldThrowIllegalArgumentException() {
        SimpleType.constructUnsafe(String[].class);
    }

    @Test
    public void testConstruct() {
        SimpleType newSimpleType = SimpleType.construct(String.class);
        assertNotNull(newSimpleType);
        assertEquals(String.class, newSimpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMapClass_ShouldThrowIllegalArgumentException() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollectionClass_ShouldThrowIllegalArgumentException() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArrayClass_ShouldThrowIllegalArgumentException() {
        SimpleType.construct(String[].class);
    }

    @Test
    public void testNarrow() {
        SimpleType newSimpleType = simpleType._narrow(String.class);
        assertNotNull(newSimpleType);
        assertEquals(String.class, newSimpleType.getRawClass());
    }

    @Test
    public void testWithContentType() {
        try {
            simpleType.withContentType(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        SimpleType newSimpleType = simpleType.withValueHandler(valueHandler);
        assertNotNull(newSimpleType);
        assertEquals(valueHandler, newSimpleType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        try {
            simpleType.withContentValueHandler(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType newSimpleType = simpleType.withStaticTyping();
        assertTrue(newSimpleType.isStatic());
    }

    @Test
    public void testRefine() {
        assertNull(simpleType.refine(String.class, null, null, null));
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        assertEquals(sb, simpleType.getErasedSignature(sb));
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        assertEquals(sb, simpleType.getGenericSignature(sb));
    }

    @Test
    public void testIsContainerType() {
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testToString() {
        assertEquals("[simple type, class java.lang.Object]", simpleType.toString());
    }

    @Test
    public void testEquals_SameInstance() {
        assertTrue(simpleType.equals(simpleType));
    }

    @Test
    public void testEquals_Null() {
        assertFalse(simpleType.equals(null));
    }

    @Test
    public void testEquals_DifferentClass() {
        assertFalse(simpleType.equals(new String()));
    }

    @Test
    public void testEquals_DifferentRawClass() {
        assertFalse(simpleType.equals(new SimpleType(Map.class)));
    }

    @Test
    public void testEquals_SameRawClass() {
        assertTrue(simpleType.equals(new SimpleType(Object.class)));
    }

    @Test
    public void testEquals_DifferentBindings() {
        assertFalse(simpleType.equals(new SimpleType(Object.class, new TypeBindings())));
    }

}
