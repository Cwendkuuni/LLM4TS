import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertTrue(simpleType instanceof SimpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruct_map() {
        SimpleType.construct(Map.class);
    }
  
  	@Test(expected = IllegalArgumentException.class)
    public void testConstruct_collection() {
        SimpleType.construct(Collection.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstruct_array() {
        SimpleType.construct(String[].class);
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.constructUnsafe(List.class);
        try {
            simpleType.withContentType(String.class);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        try {
            simpleType.withContentTypeHandler(null);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.constructUnsafe(Boolean.class);
        SimpleType staticType = simpleType.withStaticTyping();
        assertTrue(staticType.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.constructUnsafe(Float.class);
        JavaType result = simpleType.refine(Number.class, TypeBindings.emptyBindings(), null, null);
        assertNull(result);
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(Double.class);
        String signature = simpleType.getErasedSignature(new StringBuilder()).toString();
        assertEquals("Ljava/lang/Double;", signature);
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(Byte.class);
        String signature = simpleType.getGenericSignature(new StringBuilder()).toString();
        assertEquals("Ljava/lang/Byte;", signature);
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = SimpleType.constructUnsafe(Character.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = SimpleType.constructUnsafe(Short.class);
        String result = simpleType.toString();
        assertEquals("[simple type, class java.lang.Short]", result);
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(Long.class);
        SimpleType simpleType2 = SimpleType.constructUnsafe(Long.class);
        SimpleType simpleType3 = SimpleType.constructUnsafe(Integer.class);

        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
        assertFalse(simpleType1.equals(null));
        assertFalse(simpleType1.equals(new Object()));
    }
}