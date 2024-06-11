import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.defaultInstance();
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType rt = ReferenceType.construct(cls, refType, valueHandler, typeHandler);

        assertEquals(cls, rt._class);
        assertEquals(refType, rt._referencedType);
        assertEquals(valueHandler, rt._valueHandler);
        assertEquals(typeHandler, rt._typeHandler);
        assertEquals(false, rt._asStatic);
    }

    @Test
    public void testWithContentTypeHandler_SameHandler() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.defaultInstance();
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType rt = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        ReferenceType updatedRt = rt.withContentTypeHandler(typeHandler);

        assertEquals(rt, updatedRt);
    }

    @Test
    public void testWithContentTypeHandler_DifferentHandler() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.defaultInstance();
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType rt = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        Object newTypeHandler = new Object();

        ReferenceType updatedRt = rt.withContentTypeHandler(newTypeHandler);

        assertNotEquals(rt, updatedRt);
        assertEquals(cls, updatedRt._class);
        assertEquals(refType.withTypeHandler(newTypeHandler), updatedRt._referencedType);
        assertEquals(valueHandler, updatedRt._valueHandler);
        assertEquals(typeHandler, updatedRt._typeHandler);
        assertEquals(false, updatedRt._asStatic);
    }

    @Test
    public void testWithContentValueHandler_SameHandler() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.defaultInstance();
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType rt = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        ReferenceType updatedRt = rt.withContentValueHandler(valueHandler);

        assertEquals(rt, updatedRt);
    }

    @Test
    public void testWithContentValueHandler_DifferentHandler() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.defaultInstance();
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType rt = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        Object newValueHandler = new Object();

        ReferenceType updatedRt = rt.withContentValueHandler(newValueHandler);

        assertNotEquals(rt, updatedRt);
        assertEquals(cls, updatedRt._class);
        assertEquals(refType.withValueHandler(newValueHandler), updatedRt._referencedType);
        assertEquals(valueHandler, updatedRt._valueHandler);
        assertEquals(typeHandler, updatedRt._typeHandler);
        assertEquals(false, updatedRt._asStatic);
    }

    @Test
    public void testWithStaticTyping_Static() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.defaultInstance();
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType rt = new ReferenceType(cls, refType, valueHandler, typeHandler, true);

        ReferenceType updatedRt = rt.withStaticTyping();

        assertEquals(rt, updatedRt);
    }

    @Test
    public void testWithStaticTyping_NonStatic() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.defaultInstance();
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType rt = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        ReferenceType updatedRt = rt.withStaticTyping();

        assertNotEquals(rt, updatedRt);
        assertEquals(cls, updatedRt._class);
        assertEquals(refType.withStaticTyping(), updatedRt._referencedType);
        assertEquals(valueHandler, updatedRt._valueHandler);
        assertEquals(typeHandler, updatedRt._typeHandler);
        assertEquals(true, updatedRt._asStatic);
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.defaultInstance();
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType rt = new ReferenceType(cls, refType, valueHandler, typeHandler, true);

        StringBuilder expected = new StringBuilder();
        expected.append(cls.getName());
        expected.append('<');
        expected.append(refType.toCanonical());

        assertEquals(expected.toString(), rt.buildCanonicalName());
    }

    // Additional test cases for other methods can be added here

}