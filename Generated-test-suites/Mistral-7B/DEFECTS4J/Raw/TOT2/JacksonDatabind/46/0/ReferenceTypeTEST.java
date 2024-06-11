import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;

public class ReferenceTypeTest {
    
    @Test
    public void testReferenceTypeConstructor() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        JavaType referencedType = referenceType.getReferencedType();
        assertEquals(refType, referencedType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReferenceTypeNullConstructor_cls() {
        JavaType refType = JavaType.construct(int.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        ReferenceType referenceType = ReferenceType.construct(null, refType, valueHandler, typeHandler);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReferenceTypeNullConstructor_refType() {
        Class<?> cls = String.class;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        ReferenceType referenceType = ReferenceType.construct(cls, null, valueHandler, typeHandler);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReferenceTypeNullConstructor_valueHandler() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        Object typeHandler = new Object();
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, typeHandler);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReferenceTypeNullConstructor_typeHandler() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        Object valueHandler = new Object();
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, null);
    }
    
    @Test
    public void testReferenceTypeWithTypeHandler_differentTypeHandler() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        Object valueHandler = new Object();
        Object typeHandler1 = new Object();
        Object typeHandler2 = new Integer(1);
        ReferenceType referenceType1 = ReferenceType.construct(cls, refType, valueHandler, typeHandler1);
        ReferenceType referenceType2 = referenceType1.withTypeHandler(typeHandler2);
        assertNotEquals(typeHandler1, typeHandler2);
        assertEquals(referenceType1.getReferencedType(), referenceType2.getReferencedType());
    }
    
    @Test
    public void testWithStaticTyping() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(int.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        ReferenceType staticReferenceType = referenceType.withStaticTyping();
        assertTrue(staticReferenceType.isStatic());
        ReferenceType nonStaticReferenceType = referenceType.withStaticTyping().withStaticTyping();
        assertFalse(nonStaticReferenceType.isStatic());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testIsReferenceType_notReferenceType() {
        SimpleType simpleType = new SimpleType(String.class, 1, null, null);
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.construct(int.class), null, null, false);
        assertFalse(simpleType.isReferenceType());
        assertFalse(referenceType.isReferenceType());
        assertFalse(simpleType.isInstance(referenceType));
        assertFalse(referenceType.isInstance(simpleType));
        assertFalse(simpleType.isInstance(simpleType));
        assertFalse(referenceType.isInstance(referenceType));
        assertFalse(simpleType.isInstance(new Object()));
        assertFalse(referenceType.isInstance(new Object()));
        try {
            assertTrue(simpleType.isInstance(referenceType));
        } catch (ClassCastException e) {
            // Expected
        }
        try {
            assertTrue(referenceType.isInstance(simpleType));
        } catch (ClassCastException e) {
            // Expected
        }
    }
}