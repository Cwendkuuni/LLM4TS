import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = null; TypeBindings bindings = null; JavaType superClass = null;
        JavaType[] superInts = null; JavaType elemT = null;
        CollectionLikeType result = CollectionLikeType.construct(rawType, bindings, superClass, superInts, elemT);
        assertNull(result);
    }
    
    @Test
    public void testConstructDeprecated() {
        Class<?> rawType = null; JavaType elemT = null;
        CollectionLikeType result = CollectionLikeType.construct(rawType, elemT);
        assertNull(result);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testUpgradeFromException() {
        JavaType baseType = null; JavaType elementType = null;
        CollectionLikeType.upgradeFrom(baseType, elementType);
    }
    
    @Test
    public void testNarrow() {
        Class<?> subclass = null;
        JavaType result = CollectionLikeType._narrow(subclass);
        assertNull(result);
    }
    
    @Test
    public void testWithContentType() {
        JavaType contentType = null;
        JavaType result = CollectionLikeType.withContentType(contentType);
        assertNull(result);
    }
    
    @Test
    public void testWithTypeHandler() {
        Object h = null;
        CollectionLikeType result = CollectionLikeType.withTypeHandler(h);
        assertNull(result);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object h = null;
        CollectionLikeType result = CollectionLikeType.withContentTypeHandler(h);
        assertNull(result);
    }
    
    @Test
    public void testWithValueHandler() {
        Object h = null;
        CollectionLikeType result = CollectionLikeType.withValueHandler(h);
        assertNull(result);
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object h = null;
        CollectionLikeType result = CollectionLikeType.withContentValueHandler(h);
        assertNull(result);
    }
    
    @Test
    public void testWithStaticTyping() {
        CollectionLikeType result = CollectionLikeType.withStaticTyping();
        assertNotNull(result);
    }
    
    @Test
    public void testRefine() {
        Class<?> rawType = null; TypeBindings bindings = null; JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType result = CollectionLikeType.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(result);
    }
    
    @Test
    public void testIsContainerType() {
        CollectionLikeType instance = new CollectionLikeType();
        boolean result = instance.isContainerType();
        assertTrue(result);
    }
    
    @Test
    public void testIsCollectionLikeType() {
        CollectionLikeType instance = new CollectionLikeType();
        boolean result = instance.isCollectionLikeType();
        assertTrue(result);
    }
    
    @Test
    public void testGetContentType() {
        CollectionLikeType instance = new CollectionLikeType();
        JavaType result = instance.getContentType();
        assertNotNull(result);
    }
    
    @Test
    public void testGetContentValueHandler() {
        CollectionLikeType instance = new CollectionLikeType();
        Object result = instance.getContentValueHandler();
        assertNull(result);
    }
    
    @Test
    public void testGetContentTypeHandler() {
        CollectionLikeType instance = new CollectionLikeType();
        Object result = instance.getContentTypeHandler();
        assertNull(result);
    }
    
    @Test
    public void testHasHandlers() {
        CollectionLikeType instance = new CollectionLikeType();
        boolean result = instance.hasHandlers();
        assertFalse(result);
    }
    
    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = null;
        CollectionLikeType instance = new CollectionLikeType();
        StringBuilder result = instance.getErasedSignature(sb);
        assertNotNull(result);
    }
    
    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = null;
        CollectionLikeType instance = new CollectionLikeType();
        StringBuilder result = instance.getGenericSignature(sb);
        assertNotNull(result);
    }
    
    @Test
    public void testBuildCanonicalName() {
        CollectionLikeType instance = new CollectionLikeType();
        String result = instance.buildCanonicalName();
        assertNotNull(result);
    }
    
    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType instance = new CollectionLikeType();
        boolean result = instance.isTrueCollectionType();
        assertFalse(result);
    }
    
    @Test
    public void testEquals() {
        Object o = new CollectionLikeType();
        CollectionLikeType instance = new CollectionLikeType();
        boolean result = instance.equals(o);
        assertTrue(result);
    }
    
    @Test
    public void testToString() {
        CollectionLikeType instance = new CollectionLikeType();
        String result = instance.toString();
        assertNotNull(result);
    }

}