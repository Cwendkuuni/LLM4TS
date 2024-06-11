import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        // Typical use case
        CollectionType collectionType = CollectionType.construct(
                java.util.ArrayList.class, TypeBindings.emptyBindings(),
                null, null, JavaType.construct(String.class));

        assertNotNull(collectionType);
        assertEquals(java.util.ArrayList.class, collectionType.getRawClass());
        assertEquals(JavaType.construct(String.class), collectionType.getContentType());
    }

    @Test
    public void testWithContentType() {
        // Typical use case
        CollectionType originalType = CollectionType.construct(
                java.util.HashSet.class, TypeBindings.emptyBindings(),
                null, null, JavaType.construct(Integer.class));

        CollectionType modifiedType = originalType.withContentType(JavaType.construct(Long.class));

        assertNotSame(originalType, modifiedType);
        assertEquals(java.util.HashSet.class, modifiedType.getRawClass());
        assertEquals(JavaType.construct(Long.class), modifiedType.getContentType());
    }

    @Test
    public void testWithContentTypeHandler() {
        // Edge case with type handler set
        CollectionType collectionType = CollectionType.construct(
                java.util.LinkedList.class, TypeBindings.emptyBindings(),
                null, null, JavaType.construct(Double.class)).withTypeHandler(new Object());

        assertNotNull(collectionType.getTypeHandler());
    }

    @Test
    public void testRefine() {
        // Error scenario with null raw type
        CollectionType originalType = CollectionType.construct(
                java.util.HashSet.class, TypeBindings.emptyBindings(),
                null, null, JavaType.construct(Integer.class));

        CollectionType refinedType = (CollectionType) originalType.refine(null, null, null, null);

        assertNull(refinedType);
    }

    @Test
    public void testToString() {
        // Typical use case
        CollectionType collectionType = CollectionType.construct(
                java.util.TreeSet.class, TypeBindings.emptyBindings(),
                null, null, JavaType.construct(Float.class));
        
        assertEquals("[collection type; class java.util.TreeSet, contains FLOAT]", collectionType.toString());
    }
}