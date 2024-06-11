import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = null;
        JavaType refType = null;
        Object valueHandler = null;
        Object typeHandler = null;
        ReferenceType result = ReferenceType.construct(cls, refType, valueHandler, typeHandler);

        assertNotNull(result);
        assertEquals(cls, result._class);
        assertEquals(refType, result._referencedType);
        assertNull(result._valueHandler);
        assertNull(result._typeHandler);
        assertFalse(result._asStatic);
    }

    @Test
    public void testWithTypeHandler() {
        Object h = null;

        // Test for same type handler
        ReferenceType result1 = result.withTypeHandler(h);
        assertEquals(result1, result);

        // Test for different type handler
        ReferenceType result2 = result.withTypeHandler(new Object());
        assertNotEquals(result2, result);
        assertEquals(h, result2._typeHandler);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = null;

        // Test for same type handler
        ReferenceType result1 = result.withContentTypeHandler(h);
        assertEquals(result1, result);

        // Test for different type handler
        ReferenceType result2 = result.withContentTypeHandler(new Object());
        assertNotEquals(result2, result);
        assertEquals(h, result2._referencedType.getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object h = null;

        // Test for same value handler
        ReferenceType result1 = result.withValueHandler(h);
        assertEquals(result1, result);

        // Test for different value handler
        ReferenceType result2 = result.withValueHandler(new Object());
        assertNotEquals(result2, result);
        assertEquals(h, result2._valueHandler);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = null;

        // Test for same value handler
        ReferenceType result1 = result.withContentValueHandler(h);
        assertEquals(result1, result);

        // Test for different value handler
        ReferenceType result2 = result.withContentValueHandler(new Object());
        assertNotEquals(result2, result);
        assertEquals(h, result2._referencedType.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {

        // Test for static typing already enabled
        ReferenceType result1 = result.withStaticTyping();
        assertEquals(result1, result);

        // Test for enabling static typing
        ReferenceType result2 = result.withStaticTyping();
        assertNotEquals(result2, result);
        assertTrue(result2._asStatic);
    }

    @Test
    public void testBuildCanonicalName() {
        String expected = result._class.getName() + '<' + result._referencedType.toCanonical() + '>';
        String result = result.buildCanonicalName();

        assertEquals(expected, result);
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = null;
        ReferenceType result = result._narrow(subclass);

        assertNotNull(result);
        assertEquals(subclass, result._class);
        assertEquals(result._referencedType, result._referencedType);
        assertNull(result._valueHandler);
        assertNull(result._typeHandler);
        assertFalse(result._asStatic);
    }

    @Test
    public void testGetReferencedType() {
        JavaType expected = result._referencedType;
        JavaType result = result.getReferencedType();

        assertEquals(expected, result);
    }

    @Test
    public void testIsReferenceType() {
        boolean result = result.isReferenceType();

        assertTrue(result);
    }

    @Test
    public void testContainedTypeCount() {
        int expected = 1;
        int result = result.containedTypeCount();

        assertEquals(expected, result);
    }

    @Test
    public void testContainedType() {
        int index = 0;
        JavaType expected = result._referencedType;
        JavaType result = result.containedType(index);

        assertEquals(expected, result);

        index = 1;
        result = result.containedType(index);

        assertNull(result);
    }

    @Test
    public void testContainedTypeName() {
        int index = 0;
        String expected = "T";
        String result = result.containedTypeName(index);

        assertEquals(expected, result);

        index = 1;
        result = result.containedTypeName(index);

        assertNull(result);
    }

    @Test
    public void testGetParameterSource() {
        Class<?> result = result.getParameterSource();

        assertEquals(result._class, result);
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = result._classSignature(result._class, sb, true);
        expected.append(result._class.getName());
        expected.append('<');
        expected.append(result._referencedType.getGenericSignature(sb));
        String result = result.getErasedSignature(sb).toString();

        assertEquals(expected.toString(), result);
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = result._classSignature(result._class, sb, false);
        expected.append(result._class.getName());
        expected.append('<');
        expected.append(result._referencedType.getGenericSignature(sb));
        expected.append(';');
        String result = result.getGenericSignature(sb).toString();

        assertEquals(expected.toString(), result);
    }

    @Test
    public void testToString() {
        String expected = new StringBuilder(40)
            .append("[reference type, class ")
            .append(result.buildCanonicalName())
            .append('<')
            .append(result._referencedType)
            .append('>')
            .append(']')
            .toString();
        String result = result.toString();

        assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        Object o = null;
        boolean result = result.equals(o);

        assertFalse(result);

        o = new ReferenceType();
        result = result.equals(o);

        assertFalse(result);

        o = result;
        result = result.equals(o);

        assertTrue(result);

        Class<?> cls = result._class;
        JavaType refType = result._referencedType;
        result = result.equals(new ReferenceType(cls, refType, null, null, result._asStatic));

        assertTrue(result);
    }
}