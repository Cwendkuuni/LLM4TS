import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CollectionTypeTest {
    
    private CollectionType collectionType;

    @Before
    public void setUp() {
        collectionType = new CollectionType(String.class, null, null, null, null, null, null, false);
    }
    
    @Test
    public void testConstruct() {
        CollectionType constructedType = CollectionType.construct(String.class, null, null, null, null);
        assertEquals(String.class, constructedType.getRawClass());
    }
    
    @Test
    public void testWithContentType() {
        JavaType contentType = new SimpleType(String.class);
        CollectionType newType = collectionType.withContentType(contentType);
        assertEquals(contentType, newType.getContentType());
    }
    
    @Test
    public void testWithTypeHandler() {
        Object handler = new TypeHandler();
        CollectionType newType = collectionType.withTypeHandler(handler);
        assertEquals(handler, newType.getTypeHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object handler = new ValueHandler();
        CollectionType newType = collectionType.withContentValueHandler(handler);
        assertEquals(handler, newType.getContentValueHandler());
    }
    
    @Test
    public void testWithStaticTyping() {
        CollectionType newType = collectionType.withStaticTyping();
        assertTrue(newType.hasStaticTyping());
    }
    
    @Test
    public void testRefine() {
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] interfaces = {new SimpleType(Serializable.class)};
        CollectionType newType = collectionType.refine(List.class, bindings, superClass, interfaces);
        assertEquals(List.class, newType.getRawClass());
    }
}