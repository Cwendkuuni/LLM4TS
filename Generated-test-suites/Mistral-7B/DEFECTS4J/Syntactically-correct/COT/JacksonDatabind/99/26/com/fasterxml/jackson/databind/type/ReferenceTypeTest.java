import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

    @Test
    public void testConstructor() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = SimpleType.construct(int[].class, bindings);
        JavaType refType = JavaType.construct(cls, bindings);
        JavaType anchorType = null;
        ReferenceType referenceType = ReferenceType.construct(cls, bindings, superClass, refType);
        assertEquals(cls, referenceType.getClass());
        assertEquals(bindings, referenceType.getBindings());
        assertEquals(superClass, referenceType.getSuperclass());
        assertEquals(refType, referenceType.getContentType());
        assertEquals(anchorType, referenceType.getAnchorType());
        assertEquals(false, referenceType.isAnchorType());
    }

    @Test
    public void testUpgradeFrom() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType baseType = SimpleType.construct(int[].class, bindings);
        JavaType refType = JavaType.construct(cls, bindings);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertEquals(cls, referenceType.getClass());
        assertEquals(bindings, referenceType.getBindings());
        assertEquals(baseType, referenceType.getSuperclass());
        assertEquals(refType, referenceType.getContentType());
        assertEquals(referenceType, referenceType.getAnchorType());
        assertTrue(referenceType.isAnchorType());
    }

    @Test
    public void testGetContentType() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = SimpleType.construct(int[].class, bindings);
        JavaType refType = JavaType.construct(cls, bindings);
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, refType);
        JavaType expectedContentType = refType;
        JavaType actualContentType = referenceType.getContentType();
        assertEquals(expectedContentType, actualContentType);
    }

    @Test
    public void testIsReferenceType() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = SimpleType.construct(int[].class, bindings);
        JavaType refType = JavaType.construct(cls, bindings);
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, refType);
        assertTrue(referenceType.isReferenceType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromMissingRefType() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType baseType = SimpleType.construct(int[].class, bindings);
        ReferenceType.upgradeFrom(baseType, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWrongBaseType() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType baseType = new Object() {};
        JavaType refType = JavaType.construct(cls, bindings);
        ReferenceType.upgradeFrom(baseType, refType);
    }

    @Test
    public void testWithContentType() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = SimpleType.construct(int[].class, bindings);
        JavaType refType = JavaType.construct(cls, bindings);
        JavaType newContentType = JavaType.construct(Object.class, bindings);
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, refType);
        ReferenceType newReferenceType = referenceType.withContentType(newContentType);
        assertEquals(newContentType, newReferenceType.getContentType());
        assertNotSame(referenceType, newReferenceType);
    }

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = SimpleType.construct(int[].class, bindings);
        JavaType refType = JavaType.construct(cls, bindings);
        ReferenceType referenceType = ReferenceType.construct(cls, bindings, superClass, refType);
        assertEquals(cls, referenceType.getClass());
        assertEquals(bindings, referenceType.getBindings());
        assertEquals(superClass, referenceType.getSuperclass());
        assertEquals(refType, referenceType.getContentType());
        assertEquals(null, referenceType.getAnchorType());
        assertFalse(referenceType.isAnchorType());
    }

    @Test
    public void testGetAnchorType() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = SimpleType.construct(int[].class, bindings);
        JavaType refType = JavaType.construct(cls, bindings);
        JavaType anchorType = JavaType.construct(Object.class, bindings);
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, refType, anchorType);
        JavaType expectedAnchorType = anchorType;
        JavaType actualAnchorType = referenceType.getAnchorType();
        assertEquals(expectedAnchorType, actualAnchorType);
    }

    @Test
    public void testIsAnchorType() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = SimpleType.construct(int[].class, bindings);
        JavaType refType = JavaType.construct(cls, bindings);
        JavaType anchorType = JavaType.construct(Object.class, bindings);
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, refType, anchorType);
        assertTrue(referenceType.isAnchorType());
    }

    @Test
    public void testToString() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = SimpleType.construct(int[].class, bindings);
        JavaType refType = JavaType.construct(cls, bindings);
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, refType);
        String expectedString = "[reference type, class " + cls.getName() + "<" + refType + ">]";
        String actualString = referenceType.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testEquals() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = SimpleType.construct(int[].class, bindings);
        JavaType refType1 = JavaType.construct(cls, bindings);
        JavaType refType2 = JavaType.construct(cls, bindings);
        ReferenceType referenceType1 = new ReferenceType(cls, bindings, superClass, refType1);
        ReferenceType referenceType2 = new ReferenceType(cls, bindings, superClass, refType2);
        assertTrue(referenceType1.equals(referenceType2));

        JavaType superClass2 = SimpleType.construct(float[].class, bindings);
        ReferenceType referenceType3 = new ReferenceType(cls, bindings, superClass, refType1);
        assertFalse(referenceType1.equals(referenceType3));

        JavaType refType3 = JavaType.construct(int[].class, bindings);
        ReferenceType referenceType4 = new ReferenceType(String.class, bindings, superClass, refType3);
        assertFalse(referenceType1.equals(referenceType4));
    }

}