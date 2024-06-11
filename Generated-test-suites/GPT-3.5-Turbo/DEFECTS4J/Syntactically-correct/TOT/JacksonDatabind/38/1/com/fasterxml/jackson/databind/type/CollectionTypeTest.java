import com.fasterxml.jackson.databind.JavaType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    private CollectionType collectionType;

    @Before
    public void setUp() {
        collectionType = new CollectionType(ArrayList.class, null, null, null, String.class, null, null, false);
    }

    @Test
    public void toStringTest() {
        String expected = "[collection type; class java.util.ArrayList, contains java.lang.String]";
        assertEquals(expected, collectionType.toString());
    }

    @Test
    public void constructTest() {
        CollectionType type = CollectionType.construct(ArrayList.class, null, null, null, Integer.class);
        assertEquals(ArrayList.class, type.getRawClass());
        assertEquals(null, type.getBindings());
        assertEquals(null, type.getSuperClass());
        assertEquals(null, type.getSuperInterfaces());
        assertEquals(Integer.class, type.getContentType());
        assertEquals(null, type.getValueHandler());
        assertEquals(null, type.getTypeHandler());
        assertEquals(false, type.isStaticTyping());
    }

    @Test
    public void withContentTypeTest() {
        JavaType contentType = new SimpleType(String.class);
        CollectionType newType = collectionType.withContentType(contentType);
        assertEquals(ArrayList.class, newType.getRawClass());
        assertEquals(null, newType.getBindings());
        assertEquals(null, newType.getSuperClass());
        assertEquals(null, newType.getSuperInterfaces());
        assertEquals(String.class, newType.getContentType());
        assertEquals(null, newType.getValueHandler());
        assertEquals(null, newType.getTypeHandler());
        assertEquals(false, newType.isStaticTyping());
    }

    @Test
    public void withTypeHandlerTest() {
        Object handler = new TypeHandler();
        CollectionType newType = collectionType.withTypeHandler(handler);
        assertEquals(ArrayList.class, newType.getRawClass());
        assertEquals(null, newType.getBindings());
        assertEquals(null, newType.getSuperClass());
        assertEquals(null, newType.getSuperInterfaces());
        assertEquals(String.class, newType.getContentType());
        assertEquals(null, newType.getValueHandler());
        assertEquals(handler, newType.getTypeHandler());
        assertEquals(false, newType.isStaticTyping());
    }

    @Test
    public void withContentTypeHandlerTest() {
        Object handler = new TypeHandler();
        CollectionType newType = collectionType.withContentTypeHandler(handler);
        assertEquals(ArrayList.class, newType.getRawClass());
        assertEquals(null, newType.getBindings());
        assertEquals(null, newType.getSuperClass());
        assertEquals(null, newType.getSuperInterfaces());
        assertEquals(String.class, newType.getContentType());
        assertEquals(null, newType.getValueHandler());
        assertEquals(null, newType.getTypeHandler());
        assertEquals(false, newType.isStaticTyping());
        assertEquals(handler, newType.getContentType().getTypeHandler());
    }

    @Test
    public void withValueHandlerTest() {
        Object handler = new ValueHandler();
        CollectionType newType = collectionType.withValueHandler(handler);
        assertEquals(ArrayList.class, newType.getRawClass());
        assertEquals(null, newType.getBindings());
        assertEquals(null, newType.getSuperClass());
        assertEquals(null, newType.getSuperInterfaces());
        assertEquals(String.class, newType.getContentType());
        assertEquals(handler, newType.getValueHandler());
        assertEquals(null, newType.getTypeHandler());
        assertEquals(false, newType.isStaticTyping());
    }

    @Test
    public void withContentValueHandlerTest() {
        Object handler = new ValueHandler();
        CollectionType newType = collectionType.withContentValueHandler(handler);
        assertEquals(ArrayList.class, newType.getRawClass());
        assertEquals(null, newType.getBindings());
        assertEquals(null, newType.getSuperClass());
        assertEquals(null, newType.getSuperInterfaces());
        assertEquals(String.class, newType.getContentType());
        assertEquals(null, newType.getValueHandler());
        assertEquals(null, newType.getTypeHandler());
        assertEquals(false, newType.isStaticTyping());
        assertEquals(handler, newType.getContentType().getValueHandler());
    }

    @Test
    public void withStaticTypingTest() {
        CollectionType newType = collectionType.withStaticTyping();
        assertEquals(ArrayList.class, newType.getRawClass());
        assertEquals(null, newType.getBindings());
        assertEquals(null, newType.getSuperClass());
        assertEquals(null, newType.getSuperInterfaces());
        assertEquals(String.class, newType.getContentType());
        assertEquals(null, newType.getValueHandler());
        assertEquals(null, newType.getTypeHandler());
        assertEquals(true, newType.isStaticTyping());
    }

    @Test
    public void refineTest() {
        CollectionType newType = collectionType.refine(LinkedList.class, null, null, new JavaType[]{new SimpleType(CharSequence.class)});
        assertEquals(LinkedList.class, newType.getRawClass());
        assertEquals(null, newType.getBindings());
        assertEquals(null, newType.getSuperClass());
        assertEquals(1, newType.getSuperInterfaces().length);
        assertEquals(CharSequence.class, newType.getSuperInterfaces()[0]);
        assertEquals(String.class, newType.getContentType());
        assertEquals(null, newType.getValueHandler());
        assertEquals(null, newType.getTypeHandler());
        assertEquals(false, newType.isStaticTyping());
    }
}