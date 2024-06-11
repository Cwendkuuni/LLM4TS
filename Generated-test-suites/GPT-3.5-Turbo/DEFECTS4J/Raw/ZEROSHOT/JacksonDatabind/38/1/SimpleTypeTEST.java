import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, type.getRawClass());
    }
    
    @Test
    public void testConstruct() {
        SimpleType type = SimpleType.construct(Integer.class);
        assertEquals(Integer.class, type.getRawClass());
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
        SimpleType.construct(String[].class);
    }
    
    @Test
    public void testNarrow() {
        SimpleType type = SimpleType.construct(Number.class);
        SimpleType narrowedType = (SimpleType) type._narrow(Integer.class);
        assertEquals(Integer.class, narrowedType.getRawClass());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNarrowInvalidSubclass() {
        SimpleType type = SimpleType.construct(Number.class);
        type._narrow(String.class);
    }
    
    @Test
    public void testWithContentType() {
        SimpleType type = SimpleType.construct(Integer.class);
        try {
            type.withContentType(SimpleType.construct(String.class));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }
    
    @Test
    public void testWithTypeHandler() {
        SimpleType type = SimpleType.construct(String.class);
        Object typeHandler = new Object();
        SimpleType newType = type.withTypeHandler(typeHandler);
        assertEquals(typeHandler, newType.getTypeHandler());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        SimpleType type = SimpleType.construct(Number.class);
        try {
            type.withContentTypeHandler(new Object());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }
    
    @Test
    public void testWithValueHandler() {
        SimpleType type = SimpleType.construct(String.class);
        Object valueHandler = new Object();
        SimpleType newType = type.withValueHandler(valueHandler);
        assertEquals(valueHandler, newType.getValueHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        SimpleType type = SimpleType.construct(Number.class);
        try {
            type.withContentValueHandler(new Object());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType type = SimpleType.construct(Integer.class);
        SimpleType newType = type.withStaticTyping();
        assertTrue(newType.isStatic());
    }
    
    @Test
    public void testRefine() {
        SimpleType type = SimpleType.construct(Number.class);
        JavaType refinedType = type.refine(Integer.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }
    
    @Test
    public void testIsContainerType() {
        SimpleType type = SimpleType.construct(String.class);
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testGetErasedSignature() {
        SimpleType type = SimpleType.construct(String.class);
        StringBuilder sb = type.getErasedSignature(new StringBuilder());
        assertEquals("Ljava/lang/String;", sb.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType type = SimpleType.construct(List.class);
        StringBuilder sb = type.getGenericSignature(new StringBuilder());
        assertEquals("Ljava/util/List<*>;", sb.toString());
    }
    
    @Test
    public void testToString() {
        SimpleType type = SimpleType.construct(Number.class);
        assertEquals("[simple type, class java.lang.Number]", type.toString());
    }
    
    @Test
    public void testEquals() {
        SimpleType type1 = SimpleType.construct(String.class);
        SimpleType type2 = SimpleType.construct(String.class);
        assertTrue(type1.equals(type2));
    }
}