import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = CollectionType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = new JavaType();
        
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        
        assertNotNull(collectionType);
        assertEquals(rawType, collectionType.getRawClass());
        assertEquals(bindings, collectionType.getBindings());
        assertEquals(superClass, collectionType.getSuperClass());
        assertArrayEquals(superInts, collectionType.getInterfaces());
        assertEquals(elemT, collectionType.getElementType());
    }
    
    @Test
    public void testConstructWithElemT() {
        Class<?> rawType = CollectionType.class;
        JavaType elemT = new JavaType();
        
        CollectionType collectionType = CollectionType.construct(rawType, elemT);
        
        assertNotNull(collectionType);
        assertEquals(rawType, collectionType.getRawClass());
        assertNull(collectionType.getBindings());
        assertNull(collectionType.getSuperClass());
        assertNull(collectionType.getInterfaces());
        assertEquals(elemT, collectionType.getElementType());
    }
    
    @Test
    public void testNarrow() {
        Class<?> subclass = CollectionType.class;
        
        CollectionType collectionType = new CollectionType();
        JavaType narrowedType = collectionType._narrow(subclass);
        
        assertNotNull(narrowedType);
        assertEquals(subclass, narrowedType.getRawClass());
        assertEquals(collectionType.getBindings(), narrowedType.getBindings());
        assertEquals(collectionType.getSuperClass(), narrowedType.getSuperClass());
        assertArrayEquals(collectionType.getInterfaces(), narrowedType.getInterfaces());
        assertEquals(collectionType.getElementType(), narrowedType.getElementType());
    }
    
    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType();
        
        CollectionType collectionType = new CollectionType();
        CollectionType newType = collectionType.withContentType(contentType);
        
        assertNotNull(newType);
        assertEquals(collectionType.getRawClass(), newType.getRawClass());
        assertEquals(collectionType.getBindings(), newType.getBindings());
        assertEquals(collectionType.getSuperClass(), newType.getSuperClass());
        assertArrayEquals(collectionType.getInterfaces(), newType.getInterfaces());
        assertEquals(contentType, newType.getElementType());
        assertEquals(collectionType.getValueHandler(), newType.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), newType.getTypeHandler());
        assertEquals(collectionType.isStatic(), newType.isStatic());
    }
    
    @Test
    public void testWithTypeHandler() {
        Object h = new Object();
        
        CollectionType collectionType = new CollectionType();
        CollectionType newType = collectionType.withTypeHandler(h);
        
        assertNotNull(newType);
        assertEquals(collectionType.getRawClass(), newType.getRawClass());
        assertEquals(collectionType.getBindings(), newType.getBindings());
        assertEquals(collectionType.getSuperClass(), newType.getSuperClass());
        assertArrayEquals(collectionType.getInterfaces(), newType.getInterfaces());
        assertEquals(collectionType.getElementType(), newType.getElementType());
        assertEquals(collectionType.getValueHandler(), newType.getValueHandler());
        assertEquals(h, newType.getTypeHandler());
        assertEquals(collectionType.isStatic(), newType.isStatic());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object h = new Object();
        
        CollectionType collectionType = new CollectionType();
        CollectionType newType = collectionType.withContentTypeHandler(h);
        
        assertNotNull(newType);
        assertEquals(collectionType.getRawClass(), newType.getRawClass());
        assertEquals(collectionType.getBindings(), newType.getBindings());
        assertEquals(collectionType.getSuperClass(), newType.getSuperClass());
        assertArrayEquals(collectionType.getInterfaces(), newType.getInterfaces());
        assertEquals(collectionType.getElementType().withTypeHandler(h), newType.getElementType());
        assertEquals(collectionType.getValueHandler(), newType.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), newType.getTypeHandler());
        assertEquals(collectionType.isStatic(), newType.isStatic());
    }
    
    @Test
    public void testWithValueHandler() {
        Object h = new Object();
        
        CollectionType collectionType = new CollectionType();
        CollectionType newType = collectionType.withValueHandler(h);
        
        assertNotNull(newType);
        assertEquals(collectionType.getRawClass(), newType.getRawClass());
        assertEquals(collectionType.getBindings(), newType.getBindings());
        assertEquals(collectionType.getSuperClass(), newType.getSuperClass());
        assertArrayEquals(collectionType.getInterfaces(), newType.getInterfaces());
        assertEquals(collectionType.getElementType(), newType.getElementType());
        assertEquals(h, newType.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), newType.getTypeHandler());
        assertEquals(collectionType.isStatic(), newType.isStatic());
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();
        
        CollectionType collectionType = new CollectionType();
        CollectionType newType = collectionType.withContentValueHandler(h);
        
        assertNotNull(newType);
        assertEquals(collectionType.getRawClass(), newType.getRawClass());
        assertEquals(collectionType.getBindings(), newType.getBindings());
        assertEquals(collectionType.getSuperClass(), newType.getSuperClass());
        assertArrayEquals(collectionType.getInterfaces(), newType.getInterfaces());
        assertEquals(collectionType.getElementType().withValueHandler(h), newType.getElementType());
        assertEquals(collectionType.getValueHandler(), newType.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), newType.getTypeHandler());
        assertEquals(collectionType.isStatic(), newType.isStatic());
    }
    
    @Test
    public void testWithStaticTyping() {
        CollectionType collectionType = new CollectionType();
        CollectionType newType = collectionType.withStaticTyping();
        
        assertNotNull(newType);
        assertEquals(collectionType.getRawClass(), newType.getRawClass());
        assertEquals(collectionType.getBindings(), newType.getBindings());
        assertEquals(collectionType.getSuperClass(), newType.getSuperClass());
        assertArrayEquals(collectionType.getInterfaces(), newType.getInterfaces());
        assertEquals(collectionType.getElementType().withStaticTyping(), newType.getElementType());
        assertEquals(collectionType.getValueHandler(), newType.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), newType.getTypeHandler());
        assertTrue(newType.isStatic());
    }
    
    @Test
    public void testRefine() {
        Class<?> rawType = CollectionType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elemT = new JavaType();
        
        CollectionType collectionType = new CollectionType();
        JavaType refinedType = collectionType.refine(rawType, bindings, superClass, superInterfaces);
        
        assertNotNull(refinedType);
        assertEquals(rawType, refinedType.getRawClass());
        assertEquals(bindings, refinedType.getBindings());
        assertEquals(superClass, refinedType.getSuperClass());
        assertArrayEquals(superInterfaces, refinedType.getInterfaces());
        assertEquals(collectionType.getElementType(), refinedType.getElementType());
        assertEquals(collectionType.getValueHandler(), refinedType.getValueHandler());
        assertEquals(collectionType.getTypeHandler(), refinedType.getTypeHandler());
        assertEquals(collectionType.isStatic(), refinedType.isStatic());
    }

}