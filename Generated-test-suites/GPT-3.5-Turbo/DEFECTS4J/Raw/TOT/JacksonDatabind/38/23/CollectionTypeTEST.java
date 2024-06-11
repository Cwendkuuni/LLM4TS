import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        // typical use case
        CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
        assertNotNull(collectionType);

        // edge cases
        collectionType = CollectionType.construct(null, new TypeBindings(), null, null, Object.class);
        assertNotNull(collectionType);

        collectionType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        assertNotNull(collectionType);
    }

    @Test
    public void testNarrow() {
        // typical use case
        CollectionType collectionType = new CollectionType(ArrayList.class, new TypeBindings(), null, null, String.class, null, null, false);
        JavaType narrowedType = collectionType._narrow(LinkedList.class);
        assertNotNull(narrowedType);

        // edge cases
        narrowedType = collectionType._narrow(null);
        assertNull(narrowedType);
    }

    @Test
    public void testWithContentType() {
        // typical use case
        CollectionType collectionType = new CollectionType(ArrayList.class, new TypeBindings(), null, null, String.class, null, null, false);
        JavaType modifiedType = collectionType.withContentType(Integer.class);
        assertNotEquals(collectionType, modifiedType);

        // edge cases
        modifiedType = collectionType.withContentType(null);
        assertEquals(collectionType, modifiedType);
    }

    @Test
    public void testWithTypeHandler() {
        // typical use case
        CollectionType collectionType = new CollectionType(ArrayList.class, new TypeBindings(), null, null, String.class, null, null, false);
        CollectionType modifiedType = collectionType.withTypeHandler(new TypeHandler());
        assertNotNull(modifiedType);

        // edge cases
        modifiedType = collectionType.withTypeHandler(null);
        assertNotNull(modifiedType);
    }

    @Test
    public void testWithContentTypeHandler() {
        // typical use case
        CollectionType collectionType = new CollectionType(ArrayList.class, new TypeBindings(), null, null, String.class, null, null, false);
        CollectionType modifiedType = collectionType.withContentTypeHandler(new TypeHandler());
        assertNotNull(modifiedType);

        // edge cases
        modifiedType = collectionType.withContentTypeHandler(null);
        assertNotNull(modifiedType);
    }

    @Test
    public void testWithValueHandler() {
        // typical use case
        CollectionType collectionType = new CollectionType(ArrayList.class, new TypeBindings(), null, null, String.class, null, null, false);
        CollectionType modifiedType = collectionType.withValueHandler(new ValueHandler());
        assertNotNull(modifiedType);

        // edge cases
        modifiedType = collectionType.withValueHandler(null);
        assertNotNull(modifiedType);
    }

    @Test
    public void testWithContentValueHandler() {
        // typical use case
        CollectionType collectionType = new CollectionType(ArrayList.class, new TypeBindings(), null, null, String.class, null, null, false);
        CollectionType modifiedType = collectionType.withContentValueHandler(new ValueHandler());
        assertNotNull(modifiedType);

        // edge cases
        modifiedType = collectionType.withContentValueHandler(null);
        assertNotNull(modifiedType);
    }

    @Test
    public void testWithStaticTyping() {
        // typical use case
        CollectionType collectionType = new CollectionType(ArrayList.class, new TypeBindings(), null, null, String.class, null, null, false);
        CollectionType modifiedType = collectionType.withStaticTyping();
        assertNotEquals(collectionType, modifiedType);

        // edge cases
        collectionType = new CollectionType(ArrayList.class, new TypeBindings(), null, null, Object.class, null, null, false);
        modifiedType = collectionType.withStaticTyping();
        assertEquals(collectionType, modifiedType);
    }

    @Test
    public void testRefine() {
        // typical use case
        CollectionType collectionType = new CollectionType(ArrayList.class, new TypeBindings(), null, null, String.class, null, null, false);
        JavaType refinedType = collectionType.refine(LinkedList.class, new TypeBindings(), null, null);
        assertNotNull(refinedType);

        // edge cases
        refinedType = collectionType.refine(null, new TypeBindings(), null, null);
        assertNull(refinedType);
    }

    @Test
    public void testToString() {
        // typical use case
        CollectionType collectionType = new CollectionType(ArrayList.class, new TypeBindings(), null, null, String.class, null, null, false);
        String typeString = collectionType.toString();
        assertNotNull(typeString);

        // edge cases
        collectionType = new CollectionType(null, new TypeBindings(), null, null, String.class, null, null, false);
        typeString = collectionType.toString();
        assertNotNull(typeString);
    }
}