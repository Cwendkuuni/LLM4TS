import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

public class ReferenceTypeTest {

    private Class<?> cls = String.class;
    private JavaType refType = new JavaTypeStub();
    private Object valueHandler = new Object();
    private Object typeHandler = new Object();
    private boolean asStatic = true;

    @Test
    public void construct_withTypeAndHandlers_successfullyCreated() {
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        assertEquals(cls, referenceType._class);
        assertEquals(refType, referenceType._referencedType);
        assertEquals(valueHandler, referenceType._valueHandler);
        assertEquals(typeHandler, referenceType._typeHandler);
        assertEquals(asStatic, referenceType._asStatic);
    }

    @Test
    public void construct_withNullValueHandler_successfullyCreated() {
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, typeHandler);
        assertNull(referenceType._valueHandler);
    }

    @Test
    public void withTypeHandler_sameTypeHandler_returnThis() {
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, asStatic);
        ReferenceType result = referenceType.withTypeHandler(typeHandler);
        assertEquals(referenceType, result);
    }

    @Test
    public void withTypeHandler_notSameTypeHandler_returnNewInstanceWithUpdatedTypeHandler() {
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, asStatic);
        Object newTypeHandler = new Object();
        ReferenceType result = referenceType.withTypeHandler(newTypeHandler);
        assertNotEquals(referenceType, result);
        assertEquals(newTypeHandler, result._typeHandler);
    }

    @Test
    public void withContentTypeHandler_sameContentTypeHandler_returnThis() {
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, asStatic);
        ReferenceType result = referenceType.withContentTypeHandler(refType.<Object>getTypeHandler());
        assertEquals(referenceType, result);
    }

    @Test
    public void withContentTypeHandler_notSameContentTypeHandler_returnNewInstanceWithUpdatedContentTypeHandler() {
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, asStatic);
        Object newContentTypeHandler = new Object();
        ReferenceType result = referenceType.withContentTypeHandler(newContentTypeHandler);
        assertNotEquals(referenceType, result);
        assertNotEquals(newContentTypeHandler, result._referencedType.<Object>getTypeHandler());
        assertEquals(newContentTypeHandler, result._referencedType.getTypeHandler());
    }

    @Test
    public void withValueHandler_sameValueHandler_returnThis() {
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, asStatic);
        ReferenceType result = referenceType.withValueHandler(valueHandler);
        assertEquals(referenceType, result);
    }

    @Test
    public void withValueHandler_notSameValueHandler_returnNewInstanceWithUpdatedValueHandler() {
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, asStatic);
        Object newValueHandler = new Object();
        ReferenceType result = referenceType.withValueHandler(newValueHandler);
        assertNotEquals(referenceType, result);
        assertEquals(newValueHandler, result._valueHandler);
    }

    @Test
    public void withContentValueHandler_sameContentValueHandler_returnThis() {
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, asStatic);
        ReferenceType result = referenceType.withContentValueHandler(refType.<Object>getValueHandler());
        assertEquals(referenceType, result);
    }

    @Test
    public void withContentValueHandler_notSameContentValueHandler_returnNewInstanceWithUpdatedContentValueHandler() {
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, asStatic);
        Object newContentValueHandler = new Object();
        ReferenceType result = referenceType.withContentValueHandler(newContentValueHandler);
        assertNotEquals(referenceType, result);
        assertNotEquals(newContentValueHandler, result._referencedType.<Object>getValueHandler());
        assertEquals(newContentValueHandler, result._referencedType.getValueHandler());
    }

    @Test
    public void withStaticTyping_asStatic_returnThis() {
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, asStatic);
        ReferenceType result = referenceType.withStaticTyping();
        assertEquals(referenceType, result);
    }

    @Test
    public void withStaticTyping_notAsStatic_returnNewInstanceWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, asStatic);
        ReferenceType result = referenceType.withStaticTyping();
        assertNotEquals(referenceType, result);
        assertTrue(result._asStatic);
    }

    @Test
    public void getReferencedType_returnCorrectType() {
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, asStatic);
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void isReferenceType_returnTrue() {
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, asStatic);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void containedTypeCount_returnOne() {
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, asStatic);
        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void containedType_withValidIndex_returnReferencedType() {
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, asStatic);
        assertEquals(refType, referenceType.containedType(0));
    }

    @Test
    public void containedType_withInvalidIndex_returnNull() {
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, asStatic);
        assertNull(referenceType.containedType(1));
    }

    @Test
    public void containedTypeName_withValidIndex_returnTypeName() {
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, asStatic);
        assertEquals("T", referenceType.containedTypeName(0));
    }

    @Test
    public void containedTypeName_withInvalidIndex_returnNull() {
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, asStatic);
        assertNull(referenceType.containedTypeName(1));
    }

    @Test
    public void getParameterSource_returnClass() {
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, asStatic);
        assertEquals(cls, referenceType.getParameterSource());
    }

    @Test
    public void getCanonicalName_returnCanonicalName() {
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, asStatic);
        StringBuilder sb = new StringBuilder();
        sb.append(cls.getName()).append('<').append(refType.toCanonical());
        assertEquals(sb.toString(), referenceType.buildCanonicalName());
    }

    @Test
    public void narrow_withSubclass_returnReferenceTypeWithSubclass() {
        Class<?> subclass = RuntimeException.class;
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, asStatic);
        ReferenceType result = referenceType._narrow(subclass);
        assertEquals(subclass, result._class);
        assertEquals(refType, result._referencedType);
        assertEquals(valueHandler, result._valueHandler);
        assertEquals(typeHandler, result._typeHandler);
        assertEquals(asStatic, result._asStatic);
    }

    @Test
    public void getErasedSignature_returnSignatureWhenErased() {
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, asStatic);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = referenceType.getErasedSignature(sb);
        assertEquals(sb, result);
    }

    @Test
    public void getGenericSignature_returnGenericSignature() {
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, asStatic);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = referenceType.getGenericSignature(sb);
        assertEquals(sb, result);
    }

    @Test
    public void toString_returnCorrectStringRepresentation() {
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, asStatic);
        StringBuilder sb = new StringBuilder(40)
            .append("[reference type, class ")
            .append(referenceType.buildCanonicalName())
            .append('<')
            .append(refType)
            .append('>')
            .append(']');
        String expected = sb.toString();
        assertEquals(expected, referenceType.toString());
    }

    @Test
    public void equals_withSameInstance_returnTrue() {
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, asStatic);
        assertTrue(referenceType.equals(referenceType));
    }

    @Test
    public void equals_withNullObject_returnFalse() {
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, asStatic);
        assertFalse(referenceType.equals(null));
    }

    @Test
    public void equals_withDifferentClass_returnFalse() {
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, asStatic);
        assertFalse(referenceType.equals(new Object()));
    }

    @Test
    public void equals_withDifferentClass_returnFalse() {
        ReferenceType referenceType1 = new ReferenceType(cls, refType, valueHandler, typeHandler, asStatic);
        ReferenceType referenceType2 = new ReferenceType(String.class, refType, valueHandler, typeHandler, asStatic);
        assertFalse(referenceType1.equals(referenceType2));
    }

    @Test
    public void equals_withDifferentReferencedType_returnFalse() {
        JavaType otherRefType = new JavaTypeStub();
        ReferenceType referenceType1 = new ReferenceType(cls, refType, valueHandler, typeHandler, asStatic);
        ReferenceType referenceType2 = new ReferenceType(cls, otherRefType, valueHandler, typeHandler, asStatic);
        assertFalse(referenceType1.equals(referenceType2));
    }

    private static class JavaTypeStub extends JavaType {
        protected JavaTypeStub() {
            super(void.class, 0);
        }
    }
}