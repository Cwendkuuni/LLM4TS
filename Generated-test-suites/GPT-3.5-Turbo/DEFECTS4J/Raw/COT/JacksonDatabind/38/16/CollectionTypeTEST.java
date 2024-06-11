
import org.junit.Test;

import static org.junit.Assert.*;

public class CollectionTypeTest {

    @Test
    public void constructorTest() {
        Class<?> collT = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        CollectionType collectionType = new CollectionType(collT, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);

        assertNotNull(collectionType);
        assertSame(collT, collectionType.getRawClass());
        assertSame(bindings, collectionType.getBindings());
        assertSame(superClass, collectionType.getSuperClass());
        assertSame(superInts, collectionType.getInterfaces());
        assertSame(elemT, collectionType.getContentType());
        assertSame(valueHandler, collectionType.getValueHandler());
        assertSame(typeHandler, collectionType.getTypeHandler());
        assertEquals(asStatic, collectionType.isStatic());
    }

    @Test
    public void constructTest() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType elemT = null;
        
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInterfaces, elemT);
        
        assertNotNull(collectionType);
        assertSame(rawType, collectionType.getRawClass());
        assertSame(bindings, collectionType.getBindings());
        assertSame(superClass, collectionType.getSuperClass());
        assertSame(superInterfaces, collectionType.getInterfaces());
        assertSame(elemT, collectionType.getContentType());
    }

    @Test
    public void withContentTypeTest() {
        JavaType contentType = null;
        
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = collectionType.withContentType(contentType);
        
        assertSame(collectionType, result);
        assertSame(contentType, result.getContentType());
    }

    @Test
    public void withTypeHandlerTest() {
        Object h = null;
        
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = collectionType.withTypeHandler(h);

        assertSame(collectionType, result);
        assertSame(h, result.getTypeHandler());
    }

    @Test
    public void withContentTypeHandlerTest() {
        Object h = null;
        
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = collectionType.withContentTypeHandler(h);

        assertSame(collectionType, result);
        assertSame(h, result.getContentType().getTypeHandler());
    }

    @Test
    public void withValueHandlerTest() {
        Object h = null;

        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = collectionType.withValueHandler(h);

        assertSame(collectionType, result);
        assertSame(h, result.getValueHandler());
    }

    @Test
    public void withContentValueHandlerTest() {
        Object h = null;

        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = collectionType.withContentValueHandler(h);

        assertSame(collectionType, result);
        assertSame(h, result.getContentType().getValueHandler());
    }

    @Test
    public void withStaticTypingTest() {
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = collectionType.withStaticTyping();

        assertSame(collectionType, result);
        assertTrue(result.isStatic());
    }

    @Test
    public void refineTest() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType elemT = null;

        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = collectionType.refine(rawType, bindings, superClass, superInterfaces);

        assertSame(collectionType, result);
        assertSame(rawType, result.getRawClass());
        assertSame(bindings, result.getBindings());
        assertSame(superClass, result.getSuperClass());
        assertSame(superInterfaces, result.getInterfaces());
        assertSame(elemT, result.getContentType());
    }
}
