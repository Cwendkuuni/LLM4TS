import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReferenceTypeTest {

    private ReferenceType referenceType;

    @Before
    public void setup() {
        referenceType = ReferenceType.construct(ReferenceType.class, TypeBindings.emptyBindings(), null, null, null);
    }

    @Test
    public void testGetContentType() {
        // Test when referenced type is null
        assertNull(referenceType.getContentType());

        // Test when referenced type is not null
        JavaType referencedType = new JavaType();
        referenceType = ReferenceType.upgradeFrom(referenceType, referencedType);
        assertEquals(referencedType, referenceType.getContentType());
    }

    @Test
    public void testGetReferencedType() {
        // Test when referenced type is null
        assertNull(referenceType.getReferencedType());

        // Test when referenced type is not null
        JavaType referencedType = new JavaType();
        referenceType = ReferenceType.upgradeFrom(referenceType, referencedType);
        assertEquals(referencedType, referenceType.getReferencedType());
    }

    @Test
    public void testHasContentType() {
        // Test when referenced type is null
        assertFalse(referenceType.hasContentType());

        // Test when referenced type is not null
        JavaType referencedType = new JavaType();
        referenceType = ReferenceType.upgradeFrom(referenceType, referencedType);
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testIsReferenceType() {
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testGetContentType_withNullReferencedType_returnThis() {
        // Test when referenced type is null
        assertSame(referenceType, referenceType.getContentType());
    }

    @Test
    public void testGetContentType_withNonNullReferencedType_returnNewReferenceType() {
        // Test when referenced type is not null
        JavaType referencedType = new JavaType();
        referenceType = ReferenceType.upgradeFrom(referenceType, referencedType);
        ReferenceType contentType = (ReferenceType) referenceType.getContentType();
        assertNotSame(referenceType, contentType);
        assertEquals(referencedType, contentType.getReferencedType());
    }

    @Test
    public void testWithContentType_withSameContentType_returnThis() {
        // Test when content type is the same as current referenced type
        JavaType referencedType = new JavaType();
        referenceType = ReferenceType.upgradeFrom(referenceType, referencedType);
        assertSame(referenceType, referenceType.withContentType(referencedType));
    }

    @Test
    public void testWithContentType_withDifferentContentType_returnNewReferenceType() {
        // Test when content type is different from current referenced type
        JavaType referencedType = new JavaType();
        referenceType = ReferenceType.upgradeFrom(referenceType, referencedType);
        JavaType newReferencedType = new JavaType();
        ReferenceType newReferenceType = (ReferenceType) referenceType.withContentType(newReferencedType);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(newReferencedType, newReferenceType.getReferencedType());
    }

    @Test
    public void testWithTypeHandler_withSameTypeHandler_returnThis() {
        // Test when type handler is the same as current type handler
        Object typeHandler = new Object();
        referenceType = referenceType.withTypeHandler(typeHandler);
        assertSame(referenceType, referenceType.withTypeHandler(typeHandler));
    }

    @Test
    public void testWithTypeHandler_withDifferentTypeHandler_returnNewReferenceType() {
        // Test when type handler is different from current type handler
        Object typeHandler = new Object();
        referenceType = referenceType.withTypeHandler(typeHandler);
        Object newTypeHandler = new Object();
        ReferenceType newReferenceType = referenceType.withTypeHandler(newTypeHandler);
        assertNotSame(referenceType, newReferenceType);
        assertSame(newTypeHandler, newReferenceType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler_withSameContentTypeHandler_returnThis() {
        // Test when content type handler is the same as current content type handler
        Object typeHandler = new Object();
        referenceType = referenceType.withContentTypeHandler(typeHandler);
        assertSame(referenceType, referenceType.withContentTypeHandler(typeHandler));
    }

    @Test
    public void testWithContentTypeHandler_withDifferentContentTypeHandler_returnNewReferenceType() {
        // Test when content type handler is different from current content type handler
        Object typeHandler = new Object();
        referenceType = referenceType.withContentTypeHandler(typeHandler);
        Object newTypeHandler = new Object();
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(newTypeHandler);
        assertNotSame(referenceType, newReferenceType);
        assertSame(newTypeHandler, newReferenceType.getContentType().getTypeHandler());
    }

    @Test
    public void testWithValueHandler_withSameValueHandler_returnThis() {
        // Test when value handler is the same as current value handler
        Object valueHandler = new Object();
        referenceType = referenceType.withValueHandler(valueHandler);
        assertSame(referenceType, referenceType.withValueHandler(valueHandler));
    }

    @Test
    public void testWithValueHandler_withDifferentValueHandler_returnNewReferenceType() {
        // Test when value handler is different from current value handler
        Object valueHandler = new Object();
        referenceType = referenceType.withValueHandler(valueHandler);
        Object newValueHandler = new Object();
        ReferenceType newReferenceType = referenceType.withValueHandler(newValueHandler);
        assertNotSame(referenceType, newReferenceType);
        assertSame(newValueHandler, newReferenceType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler_withSameContentValueHandler_returnThis() {
        // Test when content value handler is the same as current content value handler
        Object valueHandler = new Object();
        referenceType = referenceType.withContentValueHandler(valueHandler);
        assertSame(referenceType, referenceType.withContentValueHandler(valueHandler));
    }

    @Test
    public void testWithContentValueHandler_withDifferentContentValueHandler_returnNewReferenceType() {
        // Test when content value handler is different from current content value handler
        Object valueHandler = new Object();
        referenceType = referenceType.withContentValueHandler(valueHandler);
        Object newValueHandler = new Object();
        ReferenceType newReferenceType = referenceType.withContentValueHandler(newValueHandler);
        assertNotSame(referenceType, newReferenceType);
        assertSame(newValueHandler, newReferenceType.getContentType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping_withIsStatic_returnThis() {
        // Test when isStatic is already true
        referenceType = referenceType.withStaticTyping();
        assertSame(referenceType, referenceType.withStaticTyping());
    }

    @Test
    public void testWithStaticTyping_withIsNotStatic_returnNewReferenceType() {
        // Test when isStatic is not true
        referenceType = referenceType.withStaticTyping();
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertNotSame(referenceType, newReferenceType);
        assertTrue(newReferenceType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        // Test refine method
        Class<?> newClass = String.class;
        TypeBindings newBindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[2];
        JavaType newReferencedType = new JavaType();
        ReferenceType newReferenceType = referenceType.refine(newClass, newBindings, superClass, superInterfaces, newReferencedType);
        assertNotSame(referenceType, newReferenceType);
        assertSame(newClass, newReferenceType.getRawClass());
        assertSame(newBindings, newReferenceType.getBindings());
        assertSame(superClass, newReferenceType.getSuperClass());
        assertSame(superInterfaces, newReferenceType.getInterfaces());
        assertSame(newReferencedType, newReferenceType.getReferencedType());
    }

    @Test
    public void testToString() {
        assertNotNull(referenceType.toString());
        assertEquals("[reference type, class com.fasterxml.jackson.databind.type.ReferenceType<null>]", referenceType.toString());
    }

    @Test
    public void testEquals() {
        // Test when object is the same as this
        assertTrue(referenceType.equals(referenceType));

        // Test when object is null
        assertFalse(referenceType.equals(null));

        // Test when object is not an instance of ReferenceType
        assertFalse(referenceType.equals(""));

        // Test when object's class is different from this class
        assertFalse(referenceType.equals(new Object()));

        // Test when object's class is the same as this class but the class is different
        ReferenceType other = ReferenceType.construct(String.class, null, null, null, null);
        assertFalse(referenceType.equals(other));

        // Test when object's class is the same as this class and all attributes are the same
        other = ReferenceType.construct(ReferenceType.class, TypeBindings.emptyBindings(), null, null, null);
        assertTrue(referenceType.equals(other));
    }
}