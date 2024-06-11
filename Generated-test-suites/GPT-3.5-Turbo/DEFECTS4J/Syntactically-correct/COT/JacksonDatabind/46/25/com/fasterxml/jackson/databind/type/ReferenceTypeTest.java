import com.fasterxml.jackson.databind.type.ReferenceType;
import com.fasterxml.jackson.databind.JavaType;

import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testWithReferencedType() {
        Class<?> cls = String.class;
        JavaType refType = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        assertEquals(cls, referenceType.getReferencedType());
        assertTrue(referenceType.isReferenceType());
        assertEquals(1, referenceType.containedTypeCount());
        assertEquals(refType, referenceType.containedType(0));
        assertNull(referenceType.containedType(1));
        assertEquals("T", referenceType.containedTypeName(0));
        assertNull(referenceType.containedTypeName(1));
        assertEquals(cls, referenceType.getParameterSource());
        assertEquals("[reference type, class java.lang.String<T>]", referenceType.toString());
        assertTrue(referenceType.equals(referenceType));
    }
}