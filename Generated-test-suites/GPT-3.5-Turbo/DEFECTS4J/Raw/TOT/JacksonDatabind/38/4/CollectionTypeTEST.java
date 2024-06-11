
import org.junit.Test;

import static org.junit.Assert.*;

public class CollectionTypeTest {

    @Test
    public void construct_Test() {
        // Arrange
        Class rawType = List.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = new JavaType();

        // Act
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);

        // Assert
        assertEquals(rawType, collectionType.getRawType());
        assertEquals(bindings, collectionType.getBindings());
        assertEquals(superClass, collectionType.getSuperClass());
        assertEquals(superInts, collectionType.getSuperInterfaces());
        assertEquals(elemT, collectionType.getContentType());
    }

    @Test
    public void withContentType_Test() {
        // Arrange
        JavaType contentType = new JavaType();

        // Act
        CollectionType collectionType = new CollectionType();
        CollectionType result = collectionType.withContentType(contentType);

        // Assert
        assertSame(collectionType, result);
        assertSame(contentType, collectionType.getContentType());
    }

    @Test
    public void withTypeHandler_Test() {
        // Arrange
        Object handler = new Object();

        // Act
        CollectionType collectionType = new CollectionType();
        CollectionType result = collectionType.withTypeHandler(handler);

        // Assert
        assertSame(collectionType, result);
        assertSame(handler, collectionType.getTypeHandler());
    }

    @Test
    public void withContentTypeHandler_Test() {
        // Arrange
        Object handler = new Object();

        // Act
        CollectionType collectionType = new CollectionType();
        CollectionType result = collectionType.withContentTypeHandler(handler);

        // Assert
        assertSame(collectionType, result);
        assertSame(handler, collectionType.getContentTypeHandler());
    }

    @Test
    public void withValueHandler_Test() {
        // Arrange
        Object handler = new Object();

        // Act
        CollectionType collectionType = new CollectionType();
        CollectionType result = collectionType.withValueHandler(handler);

        // Assert
        assertSame(collectionType, result);
        assertSame(handler, collectionType.getValueHandler());
    }

    @Test
    public void withContentValueHandler_Test() {
        // Arrange
        Object handler = new Object();

        // Act
        CollectionType collectionType = new CollectionType();
        CollectionType result = collectionType.withContentValueHandler(handler);

        // Assert
        assertSame(collectionType, result);
        assertSame(handler, collectionType.getContentValueHandler());
    }

    @Test
    public void withStaticTyping_Test() {
        // Act
        CollectionType collectionType = new CollectionType();
        CollectionType result = collectionType.withStaticTyping();

        // Assert
        assertSame(collectionType, result);
        assertTrue(collectionType.isStaticTyping());
    }

    @Test
    public void refine_Test() {
        // Arrange
        Class rawType = List.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = new JavaType();

        // Act
        CollectionType collectionType = new CollectionType();
        JavaType result = collectionType.refine(rawType, bindings, superClass, superInts);

        // Assert
        assertEquals(rawType, result.getRawType());
        assertEquals(bindings, result.getBindings());
        assertEquals(superClass, result.getSuperClass());
        assertEquals(superInts, result.getSuperInterfaces());
        assertEquals(elemT, result.getContentType());
    }

    @Test
    public void toString_Test() {
        // Arrange
        CollectionType collectionType = new CollectionType();

        // Act
        String result = collectionType.toString();

        // Assert
        assertEquals("[collection type; class com.fasterxml.jackson.databind.type.CollectionType, contains "+collectionType.getContentType()+"]", result);
    }
}
