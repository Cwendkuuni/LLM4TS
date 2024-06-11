import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ReferenceTypeTest {

    @Test
    public void testConstructorWithTypeBindingsAndSuperTypes() {
        Class<Integer> cls = Integer.class;
        JavaType superClass = JavaType.construct(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = JavaType.construct(String.class);
        JavaType anchorType = JavaType.construct(Object.class);

        ReferenceType referenceType = ReferenceType.construct(cls, TypeBindings.emptyBindings(), superClass, superInts, refType, anchorType, null, null, false);

        assertEquals(cls, referenceType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), referenceType.getTypeBindings());
        assertEquals(superClass, referenceType.getSuperClass());
        assertEquals(superInts, referenceType.getSuperInterfaces());
        assertEquals(refType.hashCode(), referenceType.getTypeId());
        assertEquals(anchorType, referenceType.getAnchorType());
    }

    @Test
    public void testConstructorWithBaseAndReferencedType() {
        Class<List<Integer>> cls = List.class;
        JavaType baseType = JavaType.construct(Integer.class);
        JavaType refType = JavaType.construct(String.class);

        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);

        assertEquals(cls, referenceType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), referenceType.getTypeBindings());
        assertEquals(baseType, referenceType.getSuperClass());
        assertEquals(new JavaType[0], referenceType.getSuperInterfaces());
        assertEquals(refType.hashCode(), referenceType.getTypeId());
        assertNotNull(referenceType.getAnchorType());
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testGetContentType() {
        Class<List<Integer>> cls = List.class;
        JavaType superClass = JavaType.construct(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = JavaType.construct(String.class);
        JavaType anchorType = JavaType.construct(Object.class);

        ReferenceType referenceType = new ReferenceType(cls, TypeBindings.emptyBindings(), superClass, superInts, refType, anchorType, null, null, false);

        assertEquals(refType, referenceType.getContentType());
        assertEquals(refType.hashCode(), referenceType.getTypeId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithNullReferencedType() {
        JavaType baseType = JavaType.construct(Integer.class);

        ReferenceType.upgradeFrom(baseType, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithInvalidBaseType() {
        JavaType baseType = JavaType.construct(String.class);

        ReferenceType.upgradeFrom(baseType, JavaType.construct(Integer.class));
    }
}