import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.type.CollectionType;

public class CollectionTypeTest {
    
    private CollectionType collectionType;

    @Before
    public void setUp() {
        collectionType = new CollectionType(ArrayList.class, null, null, null, null, null, null, false);
    }

    @Test
    public void testConstructor() {
        assertNotNull(collectionType);
    }

    @Test
    public void testConstruct() {
        CollectionType constructedType = CollectionType.construct(ArrayList.class, null, null, null, null);
        assertNotNull(constructedType);
        assertEquals(constructedType.getClass(), CollectionType.class);
    }

    @Test
    public void testConstructDeprecated() {
        CollectionType constructedType = CollectionType.construct(ArrayList.class, null);
        assertNotNull(constructedType);
        assertEquals(constructedType.getClass(), CollectionType.class);
    }

    @Test
    public void testNarrow() {
        JavaType narrowedType = collectionType._narrow(LinkedList.class);
        assertNotNull(narrowedType);
        assertEquals(narrowedType.getClass(), CollectionType.class);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new SimpleType(String.class);
        JavaType updatedType = collectionType.withContentType(contentType);
        assertNotNull(updatedType);
        assertEquals(updatedType.getClass(), CollectionType.class);
        assertEquals(((CollectionType) updatedType).getContentType(), contentType);
    }
    
    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new CustomTypeHandler();
        CollectionType updatedType = collectionType.withTypeHandler(typeHandler);
        assertNotNull(updatedType);
        assertEquals(updatedType.getClass(), CollectionType.class);
        assertEquals(updatedType.getTypeHandler(), typeHandler);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object contentTypeHandler = new CustomTypeHandler();
        CollectionType updatedType = collectionType.withContentTypeHandler(contentTypeHandler);
        assertNotNull(updatedType);
        assertEquals(updatedType.getClass(), CollectionType.class);
        assertEquals(updatedType.getContentType().getTypeHandler(), contentTypeHandler);
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new CustomValueHandler();
        CollectionType updatedType = collectionType.withValueHandler(valueHandler);
        assertNotNull(updatedType);
        assertEquals(updatedType.getClass(), CollectionType.class);
        assertEquals(updatedType.getValueHandler(), valueHandler);
    }

    @Test
    public void testWithContentValueHandler() {
        Object contentValueHandler = new CustomValueHandler();
        CollectionType updatedType = collectionType.withContentValueHandler(contentValueHandler);
        assertNotNull(updatedType);
        assertEquals(updatedType.getClass(), CollectionType.class);
        assertEquals(updatedType.getContentType().getValueHandler(), contentValueHandler);
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType updatedType = collectionType.withStaticTyping();
        assertNotNull(updatedType);
        assertEquals(updatedType.getClass(), CollectionType.class);
        assertTrue(updatedType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        CollectionType refinedType = collectionType.refine(ArrayList.class, null, null, null);
        assertNotNull(refinedType);
        assertEquals(refinedType.getClass(), CollectionType.class);
    }

    @Test
    public void testToString() {
        String stringRepresentation = collectionType.toString();
        assertNotNull(stringRepresentation);
        assertTrue(stringRepresentation.contains(collectionType.getClass().getName()));
        assertTrue(stringRepresentation.contains(collectionType.getElementType().toString()));
    }

}