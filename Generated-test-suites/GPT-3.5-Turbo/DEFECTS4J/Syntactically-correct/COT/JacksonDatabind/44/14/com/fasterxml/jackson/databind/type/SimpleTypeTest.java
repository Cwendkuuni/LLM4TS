
import org.junit.Assert;
import org.junit.Test;

public class SimpleTypeTest {
    
    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        Assert.assertEquals(String.class, type.getRawClass());
        Assert.assertEquals("java.lang.String", type.toCanonical());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructForMap() {
        SimpleType.construct(Map.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructForCollection() {
        SimpleType.construct(Collection.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructForArray() {
        SimpleType.construct(Object[].class);
    }
    
    @Test
    public void testWithContentType() {
        SimpleType type = SimpleType.constructUnsafe(List.class);
        try {
            type.withContentType(Integer.class);
            Assert.fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testWithContentTypeHandler() {
        SimpleType type = SimpleType.constructUnsafe(List.class);
        try {
            type.withContentTypeHandler(new Object());
            Assert.fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testWithValueHandler() {
        SimpleType type = SimpleType.constructUnsafe(List.class);
        try {
            type.withValueHandler(new Object());
            Assert.fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testWithContentValueHandler() {
        SimpleType type = SimpleType.constructUnsafe(List.class);
        try {
            type.withContentValueHandler(new Object());
            Assert.fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        SimpleType staticType = type.withStaticTyping();
        Assert.assertNotSame(type, staticType);
        Assert.assertTrue(staticType.isStatic());
    }
    
    @Test
    public void testRefine() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        JavaType refinedType = type.refine(Object.class, TypeBindings.emptyBindings(), null, null);
        Assert.assertNull(refinedType);
    }
    
    @Test
    public void testIsContainerType() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        Assert.assertFalse(type.isContainerType());
    }
    
    @Test
    public void testGetErasedSignature() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = type.getErasedSignature(new StringBuilder());
        Assert.assertEquals("Ljava/lang/String;", sb.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType type = SimpleType.constructUnsafe(List.class);
        StringBuilder sb = type.getGenericSignature(new StringBuilder());
        Assert.assertEquals("Ljava/util/List<*>;", sb.toString());
    }
}
