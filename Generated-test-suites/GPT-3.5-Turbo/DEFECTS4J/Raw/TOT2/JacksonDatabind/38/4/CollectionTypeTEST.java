import static org.junit.Assert.*;
import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        CollectionType collectionType = CollectionType.construct(List.class, TypeBindings.empty(), null, null, Value.class);
        assertNotNull(collectionType);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(Value.class, collectionType.getContentType());
    }

    @Test
    public void testToString() {
        CollectionType collectionType = CollectionType.construct(Set.class, TypeBindings.empty(), null, null, SubValue.class);
        String expected = "[collection type; class java.util.Set, contains " + SubValue.class.getName() + "]";
        assertEquals(expected, collectionType.toString());
    }

    @Test
    public void testWithContentTypeHandler() {
        CollectionType collectionType = CollectionType.construct(Map.class, TypeBindings.empty(), null, null, Element.class);
        Object handler = new TypeHandler();
        CollectionType updatedType = collectionType.withContentTypeHandler(handler);
        assertNotNull(updatedType);
        assertEquals(handler, updatedType.getContentTypeHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        CollectionType collectionType = CollectionType.construct(Queue.class, TypeBindings.empty(), null, null, Node.class);
        Object handler = new ValueHandler();
        CollectionType updatedType = collectionType.withContentValueHandler(handler);
        assertNotNull(updatedType);
        assertEquals(handler, updatedType.getContentValueHandler());
    }

    @Test
    public void testRefine() {
        CollectionType collectionType = CollectionType.construct(Deque.class, TypeBindings.empty(), null, null, Element.class);
        JavaType superClass = new SuperType();
        JavaType[] superInts = {new SuperInterface()};
        CollectionType refinedType = collectionType.refine(List.class, TypeBindings.empty(), superClass, superInts);
        assertNotNull(refinedType);
        assertEquals(List.class, refinedType.getRawClass());
        assertEquals(Element.class, refinedType.getContentType());
        assertEquals(superClass, refinedType.getSuperClass());
        assertArrayEquals(superInts, refinedType.getInterfaces());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType collectionType = CollectionType.construct(Stack.class, TypeBindings.empty(), null, null, Value.class);
        CollectionType staticType = collectionType.withStaticTyping();
        assertNotNull(staticType);
        assertTrue(staticType.isStatic());
    }

    @Test
    public void testNarrow() {
        CollectionType collectionType = CollectionType.construct(Stack.class, TypeBindings.empty(), null, null, Element.class);
        CollectionType narrowedType = collectionType.narrow(LinkedList.class);
        assertNotNull(narrowedType);
        assertEquals(LinkedList.class, narrowedType.getRawClass());
        assertEquals(Element.class, narrowedType.getContentType());
    }

}