import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    @Test
    public void testConstruct_withRawTypeAndElement() {
        Class<?> rawType = List.class;
        JavaType elemT = TypeFactory.defaultInstance().constructType(Integer.class);
        
        CollectionType collectionType = CollectionType.construct(rawType, elemT);
        
        assertNotNull(collectionType);
        assertEquals(rawType, collectionType.getRawClass());
        assertEquals(elemT, collectionType.getContentType());
        assertNull(collectionType.getTypeHandler());
    }
    
    @Test
    public void testConstruct_withRawTypeAndBindingsAndSuperClassAndSuperInterfacesAndElement() {
        Class<?> rawType = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = TypeFactory.defaultInstance().constructType(Integer.class);
        
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        
        assertNotNull(collectionType);
        assertEquals(rawType, collectionType.getRawClass());
        assertEquals(bindings, collectionType.getBindings());
        assertEquals(superClass, collectionType.getSuperClass());
        assertEquals(superInts, collectionType.getInterfaces());
        assertEquals(elemT, collectionType.getContentType());
        assertSame(collectionType, collectionType.withTypeHandler(null));
    }
    
    @Test
    public void testWithContentType_withDifferentContentType_ReturnsNewCollectionTypeWithUpdatedContentType() {
        Class<?> rawType = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = TypeFactory.defaultInstance().constructType(Integer.class);
        
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        
        JavaType newContentType = TypeFactory.defaultInstance().constructType(String.class);
        CollectionType newCollectionType = collectionType.withContentType(newContentType);
        
        assertNotSame(collectionType, newCollectionType);
        assertEquals(rawType, newCollectionType.getRawClass());
        assertEquals(bindings, newCollectionType.getBindings());
        assertEquals(superClass, newCollectionType.getSuperClass());
        assertEquals(superInts, newCollectionType.getInterfaces());
        assertEquals(newContentType, newCollectionType.getContentType());
        assertSame(newCollectionType, newCollectionType.withTypeHandler(null));
    }
    
    @Test
    public void testWithContentTypeHandler_withTypeHandler_ReturnsNewCollectionTypeWithUpdatedTypeHandler() {
        Class<?> rawType = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = TypeFactory.defaultInstance().constructType(Integer.class);
        
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        
        Object typeHandler = new TypeHandler();
        CollectionType newCollectionType = collectionType.withTypeHandler(typeHandler);
        
        assertNotSame(collectionType, newCollectionType);
        assertEquals(rawType, newCollectionType.getRawClass());
        assertEquals(bindings, newCollectionType.getBindings());
        assertEquals(superClass, newCollectionType.getSuperClass());
        assertEquals(superInts, newCollectionType.getInterfaces());
        assertEquals(typeHandler, newCollectionType.getTypeHandler());
        assertSame(newCollectionType, newCollectionType.withContentTypeHandler(null));
    }
    
    @Test
    public void testWithContentTypeHandler_withContentValueHandler_ReturnsNewCollectionTypeWithUpdatedContentValueHandler() {
        Class<?> rawType = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = TypeFactory.defaultInstance().constructType(Integer.class);
        
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        
        Object contentValueHandler = new ContentValueHandler();
        CollectionType newCollectionType = collectionType.withContentValueHandler(contentValueHandler);
        
        assertNotSame(collectionType, newCollectionType);
        assertEquals(rawType, newCollectionType.getRawClass());
        assertEquals(bindings, newCollectionType.getBindings());
        assertEquals(superClass, newCollectionType.getSuperClass());
        assertEquals(superInts, newCollectionType.getInterfaces());
        assertEquals(contentValueHandler, newCollectionType.getContentValueHandler());
        assertSame(newCollectionType, newCollectionType.withValueHandler(null));
    }
    
    @Test
    public void testWithStaticTyping_whenStaticTypingIsNotEnabled_ReturnsNewCollectionTypeWithStaticTypingEnabled() {
        Class<?> rawType = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = TypeFactory.defaultInstance().constructType(Integer.class);
        
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        
        CollectionType newCollectionType = collectionType.withStaticTyping();
        
        assertNotSame(collectionType, newCollectionType);
        assertEquals(rawType, newCollectionType.getRawClass());
        assertEquals(bindings, newCollectionType.getBindings());
        assertEquals(superClass, newCollectionType.getSuperClass());
        assertEquals(superInts, newCollectionType.getInterfaces());
        assertEquals(elemT.withStaticTyping(), newCollectionType.getContentType());
        assertSame(newCollectionType, newCollectionType.withTypeHandler(null));
    }
    
    @Test
    public void testRefine_withRawTypeAndBindingsAndSuperClassAndSuperInterfaces_ReturnsNewCollectionTypeWithRefinedProperties() {
        Class<?> rawType = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = TypeFactory.defaultInstance().constructType(Integer.class);
        
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        
        JavaType[] newSuperInts = new JavaType[1];
        newSuperInts[0] = TypeFactory.defaultInstance().constructType(Serializable.class);
        
        JavaType refinedType = collectionType.refine(List.class, bindings, superClass, newSuperInts);
        
        assertNotSame(collectionType, refinedType);
        assertEquals(List.class, refinedType.getRawClass());
        assertEquals(bindings, refinedType.getBindings());
        assertEquals(superClass, refinedType.getSuperClass());
        assertEquals(newSuperInts, refinedType.getInterfaces());
        assertEquals(elemT, refinedType.getContentType());
    }
    
    @Test
    public void testToString_ReturnsStringRepresentationOfCollectionType() {
        Class<?> rawType = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = TypeFactory.defaultInstance().constructType(Integer.class);
        
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        
        String expected = "[collection type; class " + rawType.getName() + ", contains " + elemT + "]";
        assertEquals(expected, collectionType.toString());
    }
}