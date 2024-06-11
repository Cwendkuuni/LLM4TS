import static org.junit.Assert.*;
import org.junit.*;
import com.fasterxml.jackson.databind.JavaType;

public class ReferenceTypeTest {

    private ReferenceType referenceType;

    @Before
    public void setUp() {
        Class<?> cls = Object.class;
        JavaType refType = null;
        Object valueHandler = null;
        Object typeHandler = null;

        referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
    }

    @Test
    public void testConstruct() {
        Class<?> cls = Object.class;
        JavaType refType = null;
        Object valueHandler = null;
        Object typeHandler = null;

        ReferenceType result = ReferenceType.construct(cls, refType, valueHandler, typeHandler);

        assertNotNull(result);
        assertEquals(cls, result.getParameterSource());
        assertFalse(result.isReferenceType());
    }

    @Test
    public void testWithTypeHandler() {
        Object h = null;

        ReferenceType result = referenceType.withTypeHandler(h);

        assertNotNull(result);
        assertSame(referenceType.getClass(), result.getClass());
        assertEquals(referenceType.getParameterSource(), result.getParameterSource());
        assertEquals(referenceType.getReferencedType(), result.getReferencedType());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = null;

        ReferenceType result = referenceType.withContentTypeHandler(h);

        assertNotNull(result);
        assertSame(referenceType.getClass(), result.getClass());
        assertEquals(referenceType.getParameterSource(), result.getParameterSource());
        assertEquals(referenceType.getReferencedType(), result.getReferencedType());
    }

    @Test
    public void testWithValueHandler() {
        Object h = null;

        ReferenceType result = referenceType.withValueHandler(h);

        assertNotNull(result);
        assertSame(referenceType.getClass(), result.getClass());
        assertEquals(referenceType.getParameterSource(), result.getParameterSource());
        assertEquals(referenceType.getReferencedType(), result.getReferencedType());
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = null;

        ReferenceType result = referenceType.withContentValueHandler(h);

        assertNotNull(result);
        assertSame(referenceType.getClass(), result.getClass());
        assertEquals(referenceType.getParameterSource(), result.getParameterSource());
        assertEquals(referenceType.getReferencedType(), result.getReferencedType());
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType result = referenceType.withStaticTyping();

        assertNotNull(result);
        assertSame(referenceType.getClass(), result.getClass());
        assertEquals(referenceType.getParameterSource(), result.getParameterSource());
        assertEquals(referenceType.getReferencedType(), result.getReferencedType());
    }

    @Test
    public void testGetReferencedType() {
        JavaType result = referenceType.getReferencedType();

        assertNull(result);
    }

    @Test
    public void testIsReferenceType() {
        boolean result = referenceType.isReferenceType();

        assertTrue(result);
    }

    @Test
    public void testContainedTypeCount() {
        int result = referenceType.containedTypeCount();

        assertEquals(1, result);
    }

    @Test
    public void testContainedType() {
        int index = 0;

        JavaType result = referenceType.containedType(index);

        assertNull(result);
    }

    @Test
    public void testContainedTypeName() {
        int index = 0;

        String result = referenceType.containedTypeName(index);

        assertNull(result);
    }

    @Test
    public void testGetParameterSource() {
        Class<?> result = referenceType.getParameterSource();

        assertEquals(Object.class, result);
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = referenceType.getErasedSignature(sb);

        assertNotNull(result);
        assertEquals(Object.class.getName(), result.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = referenceType.getGenericSignature(sb);

        assertNotNull(result);
        assertEquals(Object.class.getName() + "<" + referenceType.getReferencedType() + ";", result.toString());
    }

    @Test
    public void testEquals_same() {
        Object obj = referenceType;

        boolean result = referenceType.equals(obj);

        assertTrue(result);
    }

    @Test
    public void testEquals_differentClass() {
        Object obj = new Object();

        boolean result = referenceType.equals(obj);

        assertFalse(result);
    }

    @Test
    public void testEquals_differentParameterSource() {
        Class<?> cls = String.class;
        JavaType refType = null;
        Object valueHandler = null;
        Object typeHandler = null;
        ReferenceType obj = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        boolean result = referenceType.equals(obj);

        assertFalse(result);
    }

    @Test
    public void testEquals_differentReferencedType() {
        Class<?> cls = Object.class;
        JavaType refType = JavaType.untyped(cls);
        Object valueHandler = null;
        Object typeHandler = null;
        ReferenceType obj = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        boolean result = referenceType.equals(obj);

        assertFalse(result);
    }

    @Test
    public void testToString() {
        String result = referenceType.toString();

        assertNotNull(result);
        assertTrue(result.contains(referenceType.buildCanonicalName()));
        assertTrue(result.contains(referenceType.getReferencedType().toString()));
    }

}