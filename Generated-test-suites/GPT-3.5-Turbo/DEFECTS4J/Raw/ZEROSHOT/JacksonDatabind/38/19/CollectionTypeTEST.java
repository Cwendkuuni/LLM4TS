import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    private CollectionType collectionType;

    @Before
    public void setUp() {
        collectionType = new CollectionType(ArrayList.class, null, null, null, null, null, null, false);
    }

    @Test
    public void testConstruct() {
        CollectionType constructedType = CollectionType.construct(ArrayList.class, null, null, null, null);
        assertEquals(collectionType, constructedType);
    }

    @Test
    public void testWithContentType() {
        CollectionType withContentTypeType = collectionType.withContentType(String.class);
        assertEquals(collectionType.getContentType(), String.class);
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        CollectionType withTypeHandlerType = collectionType.withTypeHandler(typeHandler);
        assertEquals(collectionType.getTypeHandler(), typeHandler);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object typeHandler = new Object();
        CollectionType withContentTypeHandlerType = collectionType.withContentTypeHandler(typeHandler);
        assertEquals(collectionType.getElementType().getTypeHandler(), typeHandler);
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        CollectionType withValueHandlerType = collectionType.withValueHandler(valueHandler);
        assertEquals(collectionType.getValueHandler(), valueHandler);
    }

    @Test
    public void testWithContentValueHandler() {
        Object valueHandler = new Object();
        CollectionType withContentValueHandlerType = collectionType.withContentValueHandler(valueHandler);
        assertEquals(collectionType.getElementType().getValueHandler(), valueHandler);
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType withStaticTypingType = collectionType.withStaticTyping();
        assertTrue(withStaticTypingType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        CollectionType refinedType = collectionType.refine(rawType, bindings, superClass, superInterfaces);
        assertEquals(collectionType.getRawClass(), rawType);
        assertEquals(collectionType.getBindings(), bindings);
        assertEquals(collectionType.getSuperClass(), superClass);
        assertEquals(collectionType.getInterfaces(), superInterfaces);
    }

    @Test
    public void testToString() {
        String expected = "[collection type; class " + collectionType.getRawClass().getName() + ", contains " + collectionType.getContentType() + "]";
        assertEquals(expected, collectionType.toString());
    }
}