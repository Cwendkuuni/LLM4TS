import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
        assertNotNull(collectionType);
        assertEquals(ArrayList.class, collectionType.getRawClass());
        assertEquals(TypeBindings.class, collectionType.getBindings().getClass());
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
        CollectionType updatedType = collectionType.withTypeHandler(typeHandler);
        assertNotNull(updatedType);
        assertEquals(typeHandler, updatedType.getTypeHandler());
        assertNotEquals(collectionType, updatedType);
    }

    @Test
    public void testRefine() {
        CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
        JavaType refinedType = collectionType.refine(LinkedList.class, new TypeBindings(), null, null);
        assertNotNull(refinedType);
        assertEquals(LinkedList.class, refinedType.getRawClass());
    }

    @Test
    public void testToString() {
        CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
        assertEquals("[collection type; class java.util.ArrayList, contains class java.lang.String]", collectionType.toString());
    }

}