import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    private CollectionType collectionType;

    @Before
    public void setUp() {
        collectionType = CollectionType.construct(java.util.ArrayList.class, java.util.ArrayList.class, java.util.Stack.class, new JavaType[0], java.util.LinkedList.class, null, null, false);
    }

    @Test
    public void testConstruct() {
        assertEquals(java.util.ArrayList.class, collectionType.getRawClass());
        assertEquals(java.util.ArrayList.class, collectionType.getValueHandler());
        assertEquals(java.util.Stack.class, collectionType.getTypeHandler());
        assertEquals(java.util.LinkedList.class, collectionType.getElementType());
        assertFalse(collectionType.isContainerType());
    }

    @Test
    public void testConstructWithNullBindings() {
        collectionType = CollectionType.construct(java.util.LinkedList.class, null, null, new JavaType[0], java.util.HashMap.class);
        assertEquals(java.util.LinkedList.class, collectionType.getRawClass());
        assertNull(collectionType.getBindings());
        assertNull(collectionType.getSuperClass());
        assertArrayEquals(new JavaType[0], collectionType.getSuperInterfaces());
        assertEquals(java.util.HashMap.class, collectionType.getElementType());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isContainerType());
    }

    @Test
    public void testNarrow() {
        JavaType narrowedType = collectionType._narrow(java.util.Vector.class);
        assertEquals(java.util.Vector.class, narrowedType.getRawClass());
        assertEquals(java.util.ArrayList.class, narrowedType.getValueHandler());
        assertEquals(java.util.Stack.class, narrowedType.getTypeHandler());
        assertEquals(java.util.LinkedList.class, narrowedType.getElementType());
        assertFalse(narrowedType.isContainerType());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType();
        JavaType result = collectionType.withContentType(contentType);
        assertEquals(java.util.ArrayList.class, result.getRawClass());
        assertEquals(java.util.ArrayList.class, result.getValueHandler());
        assertEquals(java.util.Stack.class, result.getTypeHandler());
        assertEquals(contentType, result.getElementType());
        assertFalse(result.isContainerType());
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        CollectionType result = collectionType.withTypeHandler(typeHandler);
        assertEquals(java.util.ArrayList.class, result.getRawClass());
        assertEquals(java.util.ArrayList.class, result.getValueHandler());
        assertEquals(java.util.Stack.class, result.getTypeHandler());
        assertEquals(java.util.LinkedList.class, result.getElementType());
        assertEquals(typeHandler, result.getTypeHandler());
        assertFalse(result.isContainerType());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object typeHandler = new Object();
        CollectionType result = collectionType.withContentTypeHandler(typeHandler);
        assertEquals(java.util.ArrayList.class, result.getRawClass());
        assertEquals(java.util.ArrayList.class, result.getValueHandler());
        assertEquals(java.util.Stack.class, result.getTypeHandler());
        assertEquals(java.util.LinkedList.class, result.getElementType());
        assertEquals(typeHandler, result.getElementType().getTypeHandler());
        assertFalse(result.isContainerType());
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        CollectionType result = collectionType.withValueHandler(valueHandler);
        assertEquals(java.util.ArrayList.class, result.getRawClass());
        assertEquals(valueHandler, result.getValueHandler());
        assertEquals(java.util.Stack.class, result.getTypeHandler());
        assertEquals(java.util.LinkedList.class, result.getElementType());
        assertFalse(result.isContainerType());
    }

    @Test
    public void testWithContentValueHandler() {
        Object valueHandler = new Object();
        CollectionType result = collectionType.withContentValueHandler(valueHandler);
        assertEquals(java.util.ArrayList.class, result.getRawClass());
        assertEquals(java.util.ArrayList.class, result.getValueHandler());
        assertEquals(java.util.Stack.class, result.getTypeHandler());
        assertEquals(valueHandler, result.getElementType().getValueHandler());
        assertFalse(result.isContainerType());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType result = collectionType.withStaticTyping();
        assertEquals(java.util.ArrayList.class, result.getRawClass());
        assertEquals(java.util.ArrayList.class, result.getValueHandler());
        assertEquals(java.util.Stack.class, result.getTypeHandler());
        assertEquals(java.util.LinkedList.class, result.getElementType());
        assertFalse(result.isContainerType());
        assertTrue(result.isStaticTyping());
    }

    @Test
    public void testRefine() {
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
        CollectionType result = collectionType.refine(java.util.HashSet.class, null, superClass, superInterfaces);
        assertEquals(java.util.HashSet.class, result.getRawClass());
        assertNull(result.getBindings());
        assertEquals(superClass, result.getSuperClass());
        assertEquals(superInterfaces, result.getSuperInterfaces());
        assertEquals(java.util.LinkedList.class, result.getElementType());
        assertFalse(result.isContainerType());
    }

    @Test
    public void testToString() {
        String expectedResult = "[collection type; class com.fasterxml.jackson.databind.type.CollectionType, contains java.util.LinkedList]";
        assertEquals(expectedResult, collectionType.toString());
    }

}