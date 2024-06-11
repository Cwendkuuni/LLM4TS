
import org.junit.Test;

public class MapLikeTypeTest {
    
    @Test
    public void testIsContainerType() {
        MapLikeType mapLikeType = new MapLikeType(TestClass.class, new TypeBindings(), null,
                null, null, null, null, null, false);
        boolean isContainer = mapLikeType.isContainerType();
        assert(isContainer);
    }
    
    @Test
    public void testIsMapLikeType() {
        MapLikeType mapLikeType = new MapLikeType(TestClass.class, new TypeBindings(), null,
                null, null, null, null, null, false);
        boolean isMapLike = mapLikeType.isMapLikeType();
        assert(isMapLike);
    }
    
    @Test
    public void testGetKeyType() {
        JavaType keyType = new SimpleType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(TestClass.class, new TypeBindings(), null,
                null, keyType, null, null, null, false);
        JavaType result = mapLikeType.getKeyType();
        assert(result == keyType);
    }
    
    @Test
    public void testGetContentType() {
        JavaType contentType = new SimpleType(String.class);
        MapLikeType mapLikeType = new MapLikeType(TestClass.class, new TypeBindings(), null,
                null, null, contentType, null, null, false);
        JavaType result = mapLikeType.getContentType();
        assert(result == contentType);
    }
    
    @Test
    public void testGetContentValueHandler() {
        Object valueHandler = new TestValueHandler();
        JavaType contentType = new SimpleType(String.class).withValueHandler(valueHandler);
        MapLikeType mapLikeType = new MapLikeType(TestClass.class, new TypeBindings(), null,
                null, null, contentType, null, null, false);
        Object result = mapLikeType.getContentValueHandler();
        assert(result == valueHandler);
    }
    
    @Test
    public void testGetContentTypeHandler() {
        Object typeHandler = new TestTypeHandler();
        JavaType contentType = new SimpleType(String.class).withTypeHandler(typeHandler);
        MapLikeType mapLikeType = new MapLikeType(TestClass.class, new TypeBindings(), null,
                null, null, contentType, null, null, false);
        Object result = mapLikeType.getContentTypeHandler();
        assert(result == typeHandler);
    }
    
    @Test
    public void testHasHandlersWithNoHandlers() {
        MapLikeType mapLikeType = new MapLikeType(TestClass.class, new TypeBindings(), null,
                null, null, null, null, null, false);
        boolean hasHandlers = mapLikeType.hasHandlers();
        assert(!hasHandlers);
    }
    
    @Test
    public void testHasHandlersWithValueHandler() {
        Object valueHandler = new TestValueHandler();
        JavaType contentType = new SimpleType(String.class).withValueHandler(valueHandler);
        MapLikeType mapLikeType = new MapLikeType(TestClass.class, new TypeBindings(), null,
                null, null, contentType, null, null, false);
        boolean hasHandlers = mapLikeType.hasHandlers();
        assert(hasHandlers);
    }
    
    @Test
    public void testHasHandlersWithValueHandlerInKeyType() {
        Object valueHandler = new TestValueHandler();
        JavaType keyType = new SimpleType(Integer.class).withValueHandler(valueHandler);
        MapLikeType mapLikeType = new MapLikeType(TestClass.class, new TypeBindings(), null,
                null, keyType, null, null, null, false);
        boolean hasHandlers = mapLikeType.hasHandlers();
        assert(hasHandlers);
    }
}
