import static org.junit.Assert.*;
import org.junit.Test;

public class JavaTypeTest {

    private JavaType javaType;

    @Before
    public void setUp() {
        javaType = new DummyJavaType();
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        JavaType newType = javaType.withTypeHandler(handler);
        assertNotNull(newType);
        assertEquals(handler, newType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        JavaType newType = javaType.withContentTypeHandler(handler);
        assertNotNull(newType);
        assertEquals(handler, newType.getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        JavaType newType = javaType.withValueHandler(handler);
        assertNotNull(newType);
        assertEquals(handler, newType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        JavaType newType = javaType.withContentValueHandler(handler);
        assertNotNull(newType);
        assertEquals(handler, newType.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType newType = javaType.withStaticTyping();
        assertNotNull(newType);
        assertTrue(newType.useStaticType());
    }

    @Test
    public void testNarrowBy() {
        JavaType newType = javaType.narrowBy(String.class);
        assertNotNull(newType);
        assertEquals(String.class, newType.getRawClass());
    }

    @Test
    public void testForcedNarrowBy() {
        JavaType newType = javaType.forcedNarrowBy(Integer.class);
        assertNotNull(newType);
        assertEquals(Integer.class, newType.getRawClass());
    }

    @Test
    public void testWidenBy() {
        JavaType newType = javaType.widenBy(Object.class);
        assertNotNull(newType);
        assertEquals(Object.class, newType.getRawClass());
    }

    @Test
    public void testContainedTypeOrUnknown() {
        JavaType newType = javaType.containedTypeOrUnknown(0);
        assertNotNull(newType);
        assertEquals(Object.class, newType.getRawClass());
    }

    @After
    public void tearDown() {
        javaType = null;
    }
}