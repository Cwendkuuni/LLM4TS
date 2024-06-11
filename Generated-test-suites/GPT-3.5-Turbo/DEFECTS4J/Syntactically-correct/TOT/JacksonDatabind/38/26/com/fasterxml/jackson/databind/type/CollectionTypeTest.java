import static org.junit.Assert.*;
import org.junit.*;

public class CollectionTypeTest {

    // Test cases for the construct() method
    @Test
    public void testConstruct_typical() {
        CollectionType type = CollectionType.construct(List.class, new TypeBindings(), null, null, String.class);
        assertNotNull(type);
        assertEquals(List.class, type.getRawClass());
        assertEquals(String.class, type.getContentType().getRawClass());
    }

    @Test
    public void testConstruct_nullRawType() {
        try {
            CollectionType.construct(null, new TypeBindings(), null, null, String.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testConstruct_nullBindings() {
        try {
            CollectionType.construct(List.class, null, null, null, String.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    // Test cases for the _narrow() method
    @Test
    public void testNarrow_typical() {
        CollectionType type = CollectionType.construct(List.class, new TypeBindings(), null, null, String.class);
        CollectionType narrowedType = (CollectionType) type._narrow(ArrayList.class);
        assertNotNull(narrowedType);
        assertEquals(ArrayList.class, narrowedType.getRawClass());
        assertEquals(String.class, narrowedType.getContentType().getRawClass());
    }

    // Test cases for the withContentType() method
    @Test
    public void testWithContentType_typical() {
        CollectionType type = CollectionType.construct(List.class, new TypeBindings(), null, null, String.class);
        CollectionType modifiedType = (CollectionType) type.withContentType(Integer.class);
        assertNotNull(modifiedType);
        assertEquals(Integer.class, modifiedType.getContentType().getRawClass());
    }

    // Test cases for the withTypeHandler() method
    @Test
    public void testWithTypeHandler_typical() {
        Object handler = new Object();
        CollectionType type = CollectionType.construct(List.class, new TypeBindings(), null, null, String.class);
        CollectionType modifiedType = (CollectionType) type.withTypeHandler(handler);
        assertNotNull(modifiedType);
        assertEquals(handler, modifiedType.getTypeHandler());
    }

    // Test cases for the withContentTypeHandler() method
    @Test
    public void testWithContentTypeHandler_typical() {
        Object handler = new Object();
        CollectionType type = CollectionType.construct(List.class, new TypeBindings(), null, null, String.class);
        CollectionType modifiedType = (CollectionType) type.withContentTypeHandler(handler);
        assertNotNull(modifiedType);
        assertEquals(handler, modifiedType.getContentTypeHandler());
    }

    // Test cases for the withValueHandler() method
    @Test
    public void testWithValueHandler_typical() {
        Object handler = new Object();
        CollectionType type = CollectionType.construct(List.class, new TypeBindings(), null, null, String.class);
        CollectionType modifiedType = (CollectionType) type.withValueHandler(handler);
        assertNotNull(modifiedType);
        assertEquals(handler, modifiedType.getValueHandler());
    }

    // Test cases for the withContentValueHandler() method
    @Test
    public void testWithContentValueHandler_typical() {
        Object handler = new Object();
        CollectionType type = CollectionType.construct(List.class, new TypeBindings(), null, null, String.class);
        CollectionType modifiedType = (CollectionType) type.withContentValueHandler(handler);
        assertNotNull(modifiedType);
        assertEquals(handler, modifiedType.getContentType().getValueHandler());
    }

    // Test cases for the withStaticTyping() method
    @Test
    public void testWithStaticTyping_typical() {
        CollectionType type = CollectionType.construct(List.class, new TypeBindings(), null, null, String.class);
        CollectionType modifiedType = (CollectionType) type.withStaticTyping();
        assertNotNull(modifiedType);
        assertTrue(modifiedType.isStatic());
    }

    // Test cases for the refine() method
    @Test
    public void testRefine_typical() {
        CollectionType type = CollectionType.construct(List.class, new TypeBindings(), null, null, String.class);
        CollectionType refinedType = (CollectionType) type.refine(ArrayList.class, new TypeBindings(), null, null);
        assertNotNull(refinedType);
        assertEquals(ArrayList.class, refinedType.getRawClass());
    }

}