import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;

public class CollectionTypeTest {

    // Basic test cases for construct methods

    @Test
    public void testConstructWithAllParams() {
        JavaType collectionType = CollectionType.construct(List.class, null, null, null, String.class);
        assertInstanceOf(CollectionType.class, collectionType);
    }

    @Test
    public void testConstructWithDeprecatedParam() {
        try {
            CollectionType collectionType = CollectionType.construct(List.class, String.class);
            fail("Expected DeprecationException but got CollectionType object");
        } catch (DeprecationException e) {
            // Expected
        }
    }

    // Basic test cases for methods withContentType, withTypeHandler, withContentTypeHandler, withValueHandler, withContentValueHandler and withStaticTyping

    @Test
    public void testWithContentType() {
        JavaType collectionType = new CollectionType(List.class, null, null, null, String.class).withContentType(Integer.class);
        assertEquals(JavaType.constructParametricType(List.class, Integer.class), collectionType);
    }

    @Test
    public void testWithTypeHandler() {
        JavaType collectionType = new CollectionType(List.class, null, null, null, String.class).withTypeHandler(null);
        assertNotSame(collectionType, new CollectionType(List.class, null, null, null, String.class));
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType collectionType = new CollectionType(List.class, null, null, null, String.class)
                .withContentTypeHandler(null);
        assertNotSame(collectionType, new CollectionType(List.class, null, null, null, String.class));
    }

    @Test
    public void testWithValueHandler() {
        JavaType collectionType = new CollectionType(List.class, null, null, null, String.class).withValueHandler(null);
        assertNotSame(collectionType, new CollectionType(List.class, null, null, null, String.class));
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType collectionType = new CollectionType(List.class, null, null, null, String.class)
                .withContentValueHandler(null);
        assertNotSame(collectionType, new CollectionType(List.class, null, null, null, String.class));
    }

    @Test
    public void testWithStaticTyping() {
        JavaType collectionType = new CollectionType(List.class, null, null, null, String.class).withStaticTyping();
        assertTrue(collectionType._asStatic);
    }

    // Edge cases and exception handling

    @Test(expected = NullPointerException.class)
    public void testRefineWithNullClass() {
        CollectionType collectionType = new CollectionType(List.class, null, null, null, String.class);
        collectionType.refine(null, null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testRefineWithNullTypeBindings() {
        CollectionType collectionType = new CollectionType(List.class, null, null, null, String.class);
        collectionType.refine(List.class, null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testRefineWithNullSuperClass() {
        CollectionType collectionType = new CollectionType(List.class, null, null, null, String.class);
        collectionType.refine(List.class, null, null, null);
    }

    @Test(expected = ArrayStoreException.class)
    public void testRefineWithIncompatibleSuperInterfaces() {
        CollectionType collectionType = new CollectionType(List.class, null, null, null, String.class);
        collectionType.refine(List.class, null, new JavaType[] {Integer[].class}, null);
    }
}