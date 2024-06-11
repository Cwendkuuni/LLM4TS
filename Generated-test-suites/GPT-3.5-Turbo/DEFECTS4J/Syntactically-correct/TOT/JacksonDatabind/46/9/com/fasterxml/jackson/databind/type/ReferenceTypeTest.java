import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        JavaType refType = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);

        assertEquals(cls, referenceType.getParameterSource());
        assertEquals(refType, referenceType.getReferencedType());
        assertFalse(referenceType.isReferenceType());
        assertEquals(1, referenceType.containedTypeCount());
        assertNull(referenceType.containedType(1));
    }

    @Test
    public void testWithGenericTypeHandler_sameTypeHandler() {
        Class<?> cls = ReferenceTypeTest.class;
        JavaType refType = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        ReferenceType newReferenceType = referenceType.withTypeHandler(typeHandler);

        assertEquals(referenceType, newReferenceType);
    }

    @Test
    public void testWithGenericTypeHandler_differentTypeHandler() {
        Class<?> cls = ReferenceTypeTest.class;
        JavaType refType = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        Object newTypeHandler = new Object();
        ReferenceType newReferenceType = referenceType.withTypeHandler(newTypeHandler);

        assertNotEquals(referenceType, newReferenceType);
        assertEquals(newTypeHandler, newReferenceType.getTypeHandler());
    }

    @Test
    public void testWithGenericValueHandler_sameValueHandler() {
        Class<?> cls = ReferenceTypeTest.class;
        JavaType refType = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        ReferenceType newReferenceType = referenceType.withValueHandler(valueHandler);

        assertEquals(referenceType, newReferenceType);
    }

    @Test
    public void testWithGenericValueHandler_differentValueHandler() {
        Class<?> cls = ReferenceTypeTest.class;
        JavaType refType = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        Object newValueHandler = new Object();
        ReferenceType newReferenceType = referenceType.withValueHandler(newValueHandler);

        assertNotEquals(referenceType, newReferenceType);
        assertEquals(newValueHandler, newReferenceType.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> cls = ReferenceTypeTest.class;
        JavaType refType = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        ReferenceType newReferenceType = referenceType.withStaticTyping();

        assertTrue(newReferenceType.isStaticTyping());
        assertEquals(referenceType, newReferenceType);
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = ReferenceTypeTest.class;
        JavaType refType = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        String expectedCanonicalName = cls.getName() + "<" + refType.toCanonical() + ">";
        assertEquals(expectedCanonicalName, referenceType.buildCanonicalName());
    }

    @Test
    public void testNarrow() {
        Class<?> cls = ReferenceTypeTest.class;
        JavaType refType = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        Class<?> subClass = Object.class;
        ReferenceType narrowedType = (ReferenceType) referenceType.narrow(subClass);

        assertNotEquals(referenceType, narrowedType);
        assertEquals(subClass, narrowedType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = ReferenceTypeTest.class;
        JavaType refType = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        StringBuilder sb = new StringBuilder();
        StringBuilder expectedSignature = referenceType._classSignature(cls, sb, true);

        StringBuilder erasedSignature = referenceType.getErasedSignature(new StringBuilder());

        assertEquals(expectedSignature, erasedSignature);
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = ReferenceTypeTest.class;
        JavaType refType = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        StringBuilder sb = new StringBuilder();
        StringBuilder expectedSignature = referenceType._classSignature(cls, sb, false);
        expectedSignature.append('<');
        expectedSignature = refType.getGenericSignature(expectedSignature);
        expectedSignature.append(';');

        StringBuilder genericSignature = referenceType.getGenericSignature(new StringBuilder());

        assertEquals(expectedSignature, genericSignature);
    }

    @Test
    public void testToString() {
        Class<?> cls = ReferenceTypeTest.class;
        JavaType refType = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        String expectedToString = "[reference type, class " + referenceType.buildCanonicalName() + "<" + refType + ">]";
        assertEquals(expectedToString, referenceType.toString());
    }

    @Test
    public void testEquals_sameReferenceType() {
        Class<?> cls = ReferenceTypeTest.class;
        JavaType refType = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        assertEquals(referenceType, referenceType);
    }

    @Test
    public void testEquals_differentClass() {
        Class<?> cls = ReferenceTypeTest.class;
        JavaType refType = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        String otherClass = "OtherClass";
        assertNotEquals(referenceType, otherClass);
    }

    @Test
    public void testEquals_differentReferencedType() {
        Class<?> cls = ReferenceTypeTest.class;
        JavaType refType = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        JavaType otherRefType = new JavaType();
        ReferenceType otherReferenceType = new ReferenceType(cls, otherRefType, valueHandler, typeHandler, false);

        assertNotEquals(referenceType, otherReferenceType);
    }
}