
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {
    
    @Test
    public void testConstruct() {
        CollectionType collectionType = CollectionType.construct(List.class, TypeBindings.emptyBindings(),
                null, null, String.class);
        assertNotNull(collectionType);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), collectionType.getBindings());
        assertNull(collectionType.getSuperClass());
        assertNull(collectionType.getSuperInterfaces());
        assertEquals(String.class, collectionType.getContentType());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStaticTyping());
    }
    
    @Test
    public void testWithContentType() {
        CollectionType collectionType = CollectionType.construct(List.class, TypeBindings.emptyBindings(),
                null, null, String.class);
        
        JavaType newContentType = new SimpleType(Object.class);
        CollectionType modifiedType = collectionType.withContentType(newContentType);
        
        assertNotNull(modifiedType);
        assertEquals(Object.class, modifiedType.getContentType());
    }
    
    @Test
    public void testWithTypeHandler() {
        CollectionType collectionType = CollectionType.construct(List.class, TypeBindings.emptyBindings(),
                null, null, String.class);
        
        Object newTypeHandler = new SomeTypeHandler();
        CollectionType modifiedType = collectionType.withTypeHandler(newTypeHandler);
        
        assertNotNull(modifiedType);
        assertEquals(newTypeHandler, modifiedType.getTypeHandler());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        CollectionType collectionType = CollectionType.construct(List.class, TypeBindings.emptyBindings(),
                null, null, String.class);
        
        Object newContentTypeHandler = new SomeTypeHandler();
        CollectionType modifiedType = collectionType.withContentTypeHandler(newContentTypeHandler);
        
        assertNotNull(modifiedType);
        assertEquals(newContentTypeHandler, modifiedType.getContentTypeHandler());
    }
    
    @Test
    public void testWithValueHandler() {
        CollectionType collectionType = CollectionType.construct(List.class, TypeBindings.emptyBindings(),
                null, null, String.class);
        
        Object newValueHandler = new SomeValueHandler();
        CollectionType modifiedType = collectionType.withValueHandler(newValueHandler);
        
        assertNotNull(modifiedType);
        assertEquals(newValueHandler, modifiedType.getValueHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        CollectionType collectionType = CollectionType.construct(List.class, TypeBindings.emptyBindings(),
                null, null, String.class);
        
        Object newContentValueHandler = new SomeValueHandler();
        CollectionType modifiedType = collectionType.withContentValueHandler(newContentValueHandler);
        
        assertNotNull(modifiedType);
        assertEquals(newContentValueHandler, modifiedType.getContentValueHandler());
    }
    
    @Test
    public void testWithStaticTyping() {
        CollectionType collectionType = CollectionType.construct(List.class, TypeBindings.emptyBindings(),
                null, null, String.class);
        
        CollectionType modifiedType = collectionType.withStaticTyping();
        
        assertNotNull(modifiedType);
        assertTrue(modifiedType.isStaticTyping());
    }
    
    @Test
    public void testRefine() {
        CollectionType collectionType = CollectionType.construct(List.class, TypeBindings.emptyBindings(),
                null, null, String.class);
        
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(AbstractList.class);
        JavaType[] superInterfaces = new JavaType[]{new SimpleType(Collection.class)};
        
        CollectionType refinedType = collectionType.refine(rawType, bindings, superClass, superInterfaces);
        
        assertNotNull(refinedType);
        assertEquals(rawType, refinedType.getRawClass());
        assertEquals(bindings, refinedType.getBindings());
        assertEquals(superClass, refinedType.getSuperClass());
        assertArrayEquals(superInterfaces, refinedType.getSuperInterfaces());
    }
}

