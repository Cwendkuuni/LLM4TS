import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    // List all the public methods in CollectionType class

    @Test
    public void testCollectionTypeConstructor() {
        Class<CollectionType> collectionTypeClass = CollectionType.class;
        JavaType elemT = JavaType.construct(int[].class);
        JavaScriptType superClass = JavaType.construct(Collection.class);
        TypeBindings bindings = TypeBindings.Empty;
        JavaType collectionType = CollectionType.construct(Collection.class, bindings, superClass, null, elemT);
        CollectionType actualCollectionType = new CollectionType(collectionTypeClass, bindings, superClass, null, elemT);
        assertEquals(collectionType, actualCollectionType.getType());
    }

    @Test
    public void testDeprecatedConstructWithRawTypeAndElemT() {
        Class<CollectionType> collectionTypeClass = CollectionType.class;
        JavaType elemT = JavaType.construct(int[].class);
        JavaType collectionType = CollectionType.construct(Collection.class, null, null, null, elemT);
        CollectionType actualCollectionType = new CollectionType(null, null, null, null, elemT);
        assertEquals(collectionType, actualCollectionType.getType());
    }

    @Test(expected = DeprecatedException.class)
    public void testDeprecatedConstructWithRawTypeAndNoArgs() {
        Class<CollectionType> collectionTypeClass = CollectionType.class;
        JavaType elemT = JavaType.construct(int[].class);
        CollectionType collectionType = new CollectionType(collectionTypeClass, elemT);
    }

    @Test
    public void testNarrowMethod() {
        Class<CollectionType> collectionTypeClass = CollectionType.class;
        JavaType subclass = JavaType.construct(ArrayList.class);
        JavaType collectionType = CollectionType.construct(Collection.class, null, null, null, null);
        CollectionType actualCollectionType = new CollectionType(collectionTypeClass, null, null, null, null);
        CollectionType narrowedCollectionType = actualCollectionType.narrow(subclass);
        assertEquals(subclass, narrowedCollectionType.getType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeprecatedNarrowMethod() {
        Class<CollectionType> collectionTypeClass = CollectionType.class;
        JavaType subclass = JavaType.construct(ArrayList.class);
        JavaType collectionType = CollectionType.construct(Collection.class, null, null, null, null);
        CollectionType actualCollectionType = new CollectionType(collectionTypeClass, null, null, null, null);
        CollectionType narrowedCollectionType = actualCollectionType._narrow(subclass);
    }

    @Test
    public void testWithContentTypeMethod() {
        Class<CollectionType> collectionTypeClass = CollectionType.class;
        JavaType contentType = JavaType.construct(String.class);
        JavaType collectionType = CollectionType.construct(Collection.class, null, null, null, null);
        CollectionType actualCollectionType = new CollectionType(collectionTypeClass, null, null, null, null);
        CollectionType newCollectionType = actualCollectionType.withContentType(contentType);
        assertEquals(contentType, newCollectionType.getContentType());
    }

    @Test
    public void testWithTypeHandlerMethod() {
        Class<CollectionType> collectionTypeClass = CollectionType.class;
        Object handler = new Object();
        JavaType collectionType = CollectionType.construct(Collection.class, null, null, null, null);
        CollectionType actualCollectionType = new CollectionType(collectionTypeClass, null, null, null, null);
        CollectionType newCollectionType = actualCollectionType.withTypeHandler(handler);
        assertNotNull(newCollectionType.getTypeHandler());
        assertEquals(handler, newCollectionType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandlerMethod() {
        Class<CollectionType> collectionTypeClass = CollectionType.class;
        Object handler = new Object();
        JavaType elemType = JavaType.construct(String.class);
        JavaType collectionType = CollectionType.construct(Collection.class, null, null, null, elemType);
        CollectionType actualCollectionType = new CollectionType(collectionTypeClass, null, null, null, elemType);
        CollectionType newCollectionType = actualCollectionType.withContentTypeHandler(handler);
        assertNotNull(newCollectionType.getContentTypeHandler());
        assertEquals(handler, newCollectionType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandlerMethod() {
        Class<CollectionType> collectionTypeClass = CollectionType.class;
        Object handler = new Object();
        JavaType collectionType = CollectionType.construct(Collection.class, null, null, null, null);
        CollectionType actualCollectionType = new CollectionType(collectionTypeClass, null, null, null, null);
        CollectionType newCollectionType = actualCollectionType.withValueHandler(handler);
        assertNotNull(newCollectionType.getValueHandler());
        assertEquals(handler, newCollectionType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandlerMethod() {
        Class<CollectionType> collectionTypeClass = CollectionType.class;
        Object handler = new Object();
        JavaType elemType = JavaType.construct(String.class);
        JavaType collectionType = CollectionType.construct(Collection.class, null, null, null, elemType);
        CollectionType actualCollectionType = new CollectionType(collectionTypeClass, null, null, null, elemType);
        CollectionType newCollectionType = actualCollectionType.withContentValueHandler(handler);
        assertNotNull(newCollectionType.getContentValueHandler());
        assertEquals(handler, newCollectionType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTypingMethod() {
        Class<CollectionType> collectionTypeClass = CollectionType.class;
        JavaType collectionType = CollectionType.construct(Collection.class, null, null, null, null);
        CollectionType actualCollectionType = new CollectionType(collectionTypeClass, null, null, null, null);
        CollectionType newCollectionType = actualCollectionType.withStaticTyping();
        assertTrue(newCollectionType.isStatic());
    }

    @Test
    public void testRefineMethod() {
        Class<CollectionType> collectionTypeClass = CollectionType.class;
        Class rawType = ArrayList.class;
        TypeBindings bindings = TypeBindings.Empty;
        JavaType superClass = JavaType.construct(Collection.class);
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elemT = JavaType.construct(int[].class);
        JavaType newCollectionType = CollectionType.construct(Collection.class, bindings, superClass, superInterfaces, elemT);
        CollectionType actualCollectionType = new CollectionType(collectionTypeClass, null, null, null, null);
        CollectionType refinedCollectionType = actualCollectionType.refine(rawType, bindings, superClass, superInterfaces);
        assertEquals(newCollectionType, refinedCollectionType);
    }

    @Test
    public void testToStringMethod() {
        Class<CollectionType> collectionTypeClass = CollectionType.class;
        JavaType collectionType = CollectionType.construct(Collection.class, null, null, null, null);
        CollectionType actualCollectionType = new CollectionType(collectionTypeClass, null, null, null, null);
        String expectedToString = "[collection type; class Collection, contains class [I]]";
        String actualToString = actualCollectionType.toString();
        assertEquals(expectedToString, actualToString);
    }
}