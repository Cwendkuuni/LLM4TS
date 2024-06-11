
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        assertNotNull(simpleType);
        assertEquals(Object.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
        assertEquals("[simple type, class java.lang.Object]", simpleType.toString());
    }
    
    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
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
    public void testNarrow_sameClass()
    {
        SimpleType simpleType = SimpleType.construct(String.class);
        SimpleType narrowedType = (SimpleType) simpleType._narrow(String.class);
        assertSame(simpleType, narrowedType);
    }
    
    @Test
    public void testWithContentType_exceptionThrown()
    {
        SimpleType simpleType = SimpleType.construct(String.class);
        try {
            simpleType.withContentType(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }
    
    @Test
    public void testWithContentTypeHandler_exceptionThrown()
    {
        SimpleType simpleType = SimpleType.construct(String.class);
        try {
            simpleType.withContentTypeHandler(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }
    
    @Test
    public void testWithContentValueHandler_exceptionThrown()
    {
        SimpleType simpleType = SimpleType.construct(String.class);
        try {
            simpleType.withContentValueHandler(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }
    
    @Test
    public void testWithStaticTyping()
    {
        SimpleType simpleType = SimpleType.construct(String.class);
        SimpleType staticType = simpleType.withStaticTyping();
        assertTrue(staticType.isStatic());
    }
    
    @Test
    public void testRefine()
    {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertNull(simpleType.refine(Object.class, TypeBindings.emptyBindings(), null, null));
    }
    
    @Test
    public void testGetErasedSignature()
    {
        SimpleType simpleType = SimpleType.construct(String.class);
        StringBuilder builder = new StringBuilder();
        StringBuilder signature = simpleType.getErasedSignature(builder);
        assertNotNull(signature);
        assertEquals("Ljava/lang/String;", signature.toString());
    }
    
    @Test
    public void testGetGenericSignature()
    {
        SimpleType simpleType = SimpleType.construct(String.class);
        StringBuilder builder = new StringBuilder();
        StringBuilder signature = simpleType.getGenericSignature(builder);
        assertNotNull(signature);
        assertEquals("Ljava/lang/String;", signature.toString());
    }
    
    @Test
    public void testBuildCanonicalName()
    {
        SimpleType simpleType = SimpleType.construct(String.class);
        String canonicalName = simpleType.buildCanonicalName();
        assertNotNull(canonicalName);
        assertEquals("java.lang.String", canonicalName);
    }
    
    @Test
    public void testEquals_sameObject()
    {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertTrue(simpleType.equals(simpleType));
    }
    
    @Test
    public void testEquals_sameProperties()
    {
        SimpleType simpleType1 = SimpleType.construct(String.class);
        SimpleType simpleType2 = SimpleType.construct(String.class);
        assertTrue(simpleType1.equals(simpleType2));
    }
    
    @Test
    public void testEquals_differentClass()
    {
        SimpleType simpleType1 = SimpleType.construct(Object.class);
        SimpleType simpleType2 = SimpleType.construct(String.class);
        assertFalse(simpleType1.equals(simpleType2));
    }
    
    @Test
    public void testEquals_differentBindings()
    {
        SimpleType simpleType1 = SimpleType.construct(String.class);
        SimpleType simpleType2 = SimpleType.construct(String.class);
        assertFalse(simpleType1.equals(simpleType2));
    }
    
    @Test
    public void testEquals_null()
    {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertFalse(simpleType.equals(null));
    }
}

