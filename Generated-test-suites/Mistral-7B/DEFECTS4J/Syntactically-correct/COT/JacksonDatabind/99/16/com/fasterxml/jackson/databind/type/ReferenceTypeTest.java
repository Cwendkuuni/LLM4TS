import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

    @Test
    public void testConstructor() {
        JavaType refType = new JavaType(int.class);
        JavaType anchorType = new JavaType(String.class);
        ReferenceType referenceType = ReferenceType.construct(String.class, null, null, null, refType);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(refType, referenceType.getReferencedType());
        assertNull(referenceType.getAnchorType());
        assertFalse(referenceType.isAnchorType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new JavaType(int[].class);
        JavaType refType = new JavaType(int.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertEquals(int.class, referenceType.getRawClass());
        assertEquals(refType, referenceType.getReferencedType());
        assertSame(referenceType, referenceType.getAnchorType());
        assertTrue(referenceType.isAnchorType());
    }

    @Test
    public void testGetContentType() {
        JavaType refType = new JavaType(int.class);
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, refType);
        assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType refType = new JavaType(int.class);
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, refType);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        JavaType refType = new JavaType(int.class);
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, refType);
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType refType = new JavaType(int.class);
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, refType);
        StringBuilder expected = new StringBuilder("[Ljava/lang/String;");
        StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType refType = new JavaType(int.class);
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, refType);
        StringBuilder expected = new StringBuilder("[Ljava/lang/String;<T extends java.lang.Number>T;");
        StringBuilder actual = referenceType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromNullReferencedType() {
        JavaType baseType = new JavaType(int[].class);
        ReferenceType.upgradeFrom(baseType, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType baseType = new JavaType(Double.class);
        JavaType refType = new JavaType(int.class);
        ReferenceType.upgradeFrom(baseType, refType);
    }
}