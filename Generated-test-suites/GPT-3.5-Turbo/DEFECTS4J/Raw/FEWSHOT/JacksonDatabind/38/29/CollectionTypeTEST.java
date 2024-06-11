import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollectionTypeTest {

    private CollectionType collectionType;
    private JavaType elementType;

    @Before
    public void setup() {
        elementType = JavaType.builder().build();
        collectionType = CollectionType.construct(Collection.class, elementType);
    }

    @Test
    public void testConstruct() {
        assertNotNull(collectionType);
        assertEquals(Collection.class, collectionType.getRawClass());
        assertEquals(elementType, collectionType.getContentType());
    }

    @Test
    public void testWithContentType() {
        JavaType newContentType = JavaType.builder().build();
        CollectionType newCollectionType = collectionType.withContentType(newContentType);
        assertNotNull(newCollectionType);
        assertEquals(Collection.class, newCollectionType.getRawClass());
        assertEquals(newContentType, newCollectionType.getContentType());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType staticTypingCollectionType = collectionType.withStaticTyping();
        assertNotNull(staticTypingCollectionType);
        assertEquals(Collection.class, staticTypingCollectionType.getRawClass());
        assertTrue(staticTypingCollectionType.isStaticTyping());
    }

    @Test
    public void testToString() {
        String expected = "[collection type; class java.util.Collection, contains " + elementType + "]";
        assertEquals(expected, collectionType.toString());
    }
}