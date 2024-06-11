
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        // Create a CollectionLikeType object
        // using construct() method and check if
        // it is not null
        CollectionLikeType collectionLikeType =
                CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(Integer.class));
        assertNotNull(collectionLikeType);
    }

    @Test
    public void testConstructWithElementType() {
        // Create a CollectionLikeType object
        // using construct() method with only the raw type and
        // the element type and check if it is not null
        CollectionLikeType collectionLikeType =
                CollectionLikeType.construct(Collection.class, SimpleType.construct(Integer.class));
        assertNotNull(collectionLikeType);
    }

    @Test
    public void testUpgradeFrom() {
        // Create a CollectionLikeType object
        // using upgradeFrom() method and check if
        // it is not null
        CollectionLikeType collectionLikeType =
                CollectionLikeType.upgradeFrom(SimpleType.construct(Integer.class), SimpleType.construct(Long.class));
        assertNotNull(collectionLikeType);
    }

    @Test
    public void testNarrow() {
        // Create a CollectionLikeType object
        // using _narrow() method and check if
        // it is not null
        CollectionLikeType collectionLikeType = (CollectionLikeType) SimpleType.construct(Integer.class).narrow(List.class);
        assertNotNull(collectionLikeType);
        assertEquals(List.class, collectionLikeType.getRawClass());
    }

    @Test
    public void testWithContentType() {
        // Create a CollectionLikeType object
        // using withContentType() method and check if
        // it is not null
        CollectionLikeType collectionLikeType =
                (CollectionLikeType) CollectionLikeType.construct(Collection.class, SimpleType.construct(Integer.class)).withContentType(SimpleType.construct(Long.class));
        assertNotNull(collectionLikeType);
        assertEquals(Long.class, collectionLikeType.getContentType().getRawClass());
    }

    @Test
    public void testWithTypeHandler() {
        // Create a CollectionLikeType object
        // using withTypeHandler() method and check if
        // it is not null
        CollectionLikeType collectionLikeType =
                (CollectionLikeType) CollectionLikeType.construct(Collection.class, SimpleType.construct(Integer.class)).withTypeHandler("handler");
        assertNotNull(collectionLikeType);
        assertEquals("handler", collectionLikeType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        // Create a CollectionLikeType object
        // using withContentTypeHandler() method and check if
        // it is not null
        CollectionLikeType collectionLikeType =
                (CollectionLikeType) CollectionLikeType.construct(Collection.class, SimpleType.construct(Integer.class)).withContentTypeHandler("handler");
        assertNotNull(collectionLikeType);
        assertEquals("handler", collectionLikeType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        // Create a CollectionLikeType object
        // using withValueHandler() method and check if
        // it is not null
        CollectionLikeType collectionLikeType =
                (CollectionLikeType) CollectionLikeType.construct(Collection.class, SimpleType.construct(Integer.class)).withValueHandler("handler");
        assertNotNull(collectionLikeType);
        assertEquals("handler", collectionLikeType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        // Create a CollectionLikeType object
        // using withContentValueHandler() method and check if
        // it is not null
        CollectionLikeType collectionLikeType =
                (CollectionLikeType) CollectionLikeType.construct(Collection.class, SimpleType.construct(Integer.class)).withContentValueHandler("handler");
        assertNotNull(collectionLikeType);
        assertEquals("handler", collectionLikeType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        // Create a CollectionLikeType object
        // using withStaticTyping() method and check if
        // it is not null
        CollectionLikeType collectionLikeType =
                (CollectionLikeType) CollectionLikeType.construct(Collection.class, SimpleType.construct(Integer.class)).withStaticTyping();
        assertNotNull(collectionLikeType);
        assertTrue(collectionLikeType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        // Create a CollectionLikeType object
        // using refine() method and check if
        // it is not null
        CollectionLikeType collectionLikeType =
                (CollectionLikeType) CollectionLikeType.construct(Collection.class, SimpleType.construct(Integer.class)).refine(List.class, TypeBindings.emptyBindings(), null, null);
        assertNotNull(collectionLikeType);
        assertEquals(List.class, collectionLikeType.getRawClass());
    }

    @Test
    public void testIsContainerType() {
        // Create a CollectionLikeType object
        // and check if isContainerType() returns true
        CollectionLikeType collectionLikeType =
                CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(Integer.class));
        assertTrue(collectionLikeType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        // Create a CollectionLikeType object
        // and check if isCollectionLikeType() returns true
        CollectionLikeType collectionLikeType =
                CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(Integer.class));
        assertTrue(collectionLikeType.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        // Create a CollectionLikeType object
        // and check if getContentType() returns the expected JavaType
        CollectionLikeType collectionLikeType =
                CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(Integer.class));
        assertEquals(Integer.class, collectionLikeType.getContentType().getRawClass());
    }

    @Test
    public void testGetContentValueHandler() {
        // Create a CollectionLikeType object
        // and check if getContentValueHandler() returns the expected value
        CollectionLikeType collectionLikeType =
                CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(Integer.class));
        assertNull(collectionLikeType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        // Create a CollectionLikeType object
        // and check if getContentTypeHandler() returns the expected value
        CollectionLikeType collectionLikeType =
                CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(Integer.class));
        assertNull(collectionLikeType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        // Create a CollectionLikeType object
        // and check if hasHandlers() returns false
        CollectionLikeType collectionLikeType =
                CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(Integer.class));
        assertFalse(collectionLikeType.hasHandlers());
    }

    @Test
    public void testGetErasedSignature() {
        // Create a CollectionLikeType object
        // and check if getErasedSignature() returns the expected signature string
        CollectionLikeType collectionLikeType =
                CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(Integer.class));
        assertEquals("java.util.Collection<;", collectionLikeType.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetGenericSignature() {
        // Create a CollectionLikeType object
        // and check if getGenericSignature() returns the expected signature string
        CollectionLikeType collectionLikeType =
                CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(Integer.class));
        assertEquals("java.util.Collection<java.lang.Integer>;", collectionLikeType.getGenericSignature(new StringBuilder()).toString());
    }

    @Test
    public void testEquals() {
        // Create two identical CollectionLikeType objects
        CollectionLikeType collectionLikeType1 =
                CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(Integer.class));
        CollectionLikeType collectionLikeType2 =
                CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(Integer.class));
        // Check if they are equal
        assertEquals(collectionLikeType1, collectionLikeType2);
    }

    @Test
    public void testNotEquals() {
        // Create two different CollectionLikeType objects
        CollectionLikeType collectionLikeType1 =
                CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(Integer.class));
        CollectionLikeType collectionLikeType2 =
                CollectionLikeType.construct(List.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(Integer.class));
        // Check if they are not equal
        assertNotEquals(collectionLikeType1, collectionLikeType2);
    }

    @Test
    public void testToString() {
        // Create a CollectionLikeType object and check if its
        // toString() method returns the expected string
        CollectionLikeType collectionLikeType =
                CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, SimpleType.construct(Integer.class));
        assertEquals("[collection-like type; class java.util.Collection, contains java.lang.Integer]", collectionLikeType.toString());
    }
}
