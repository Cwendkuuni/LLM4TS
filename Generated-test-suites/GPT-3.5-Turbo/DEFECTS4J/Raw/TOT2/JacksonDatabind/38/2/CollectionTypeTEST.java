import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, new SimpleType(String.class));
        assertNotNull(collectionType);
        assertEquals(ArrayList.class, collectionType.getRawClass());
        assertEquals(new TypeBindings(), collectionType.getBindings());
        assertNull(collectionType.getSuperClass());
        assertNull(collectionType.getSuperInterfaces());
        assertEquals(new SimpleType(String.class), collectionType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, new SimpleType(String.class));
        Object typeHandler = new Object();
        CollectionType updatedCollectionType = collectionType.withTypeHandler(typeHandler);
        assertNotNull(updatedCollectionType);
        assertEquals(typeHandler, updatedCollectionType.getTypeHandler());
    }

    @Test
    public void testRefine() {
        CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, new SimpleType(String.class));
        TypeBindings typeBindings = new TypeBindings();
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInterfaces = {new SimpleType(Serializable.class)};
        JavaType refinedType = collectionType.refine(List.class, typeBindings, superClass, superInterfaces);
        assertNotNull(refinedType);
        assertEquals(List.class, refinedType.getRawClass());
        assertEquals(typeBindings, refinedType.getBindings());
        assertEquals(superClass, refinedType.getSuperClass());
        assertArrayEquals(superInterfaces, refinedType.getSuperInterfaces());
        assertEquals(new SimpleType(String.class), refinedType.getContentType());
    }

}