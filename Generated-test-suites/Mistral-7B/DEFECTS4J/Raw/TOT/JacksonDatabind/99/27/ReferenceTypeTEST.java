import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SimpleType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ReferenceTypeTest {

    @Test
    public void testConstructor() {
        Class<String> stringClass = String.class;
        JavaType stringType = SimpleType.construct(stringClass, null);
        JavaType referencedType = SimpleType.construct(Integer.class, null);
        JavaType referenceType = ReferenceType.construct(stringClass, referencedType);

        assertNotNull(referenceType);
        assertEquals(stringClass, referenceType.getRawClass());
        assertEquals(referencedType, referenceType.getContentType());
    }

    @Test
    public void testUpgradeFrom() {
        Class<String> stringClass = String.class;
        JavaType stringType = SimpleType.construct(stringClass, null);
        JavaType referencedType = SimpleType.construct(Integer.class, null);
        JavaType referenceType = ReferenceType.upgradeFrom(stringType, referencedType);

        assertNotNull(referenceType);
        assertEquals(stringType.getClass(), referenceType.getClass());
        assertEquals(stringClass, referenceType.getRawClass());
        assertEquals(referencedType, referenceType.getContentType());
    }

    @Test
    public void testWithContentType() {
        Class<String> stringClass = String.class;
        JavaType stringType = SimpleType.construct(stringClass, null);
        JavaType referencedType = SimpleType.construct(Integer.class, null);
        JavaType anchorType = SimpleType.construct(String[].class, null);
        JavaType referenceType = new ReferenceType(stringType, null, null, null, referencedType, anchorType, null, null, false);

        JavaType newReferenceType = referenceType.withContentType(referencedType);

        assertNotNull(newReferenceType);
        assertEquals(referenceType.getContentType(), newReferenceType.getContentType());
        assertEquals(referenceType.getHashCode(), newReferenceType.getHashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullRefType() {
        Class<String> stringClass = String.class;
        JavaType stringType = SimpleType.construct(stringClass, null);
        JavaType referenceType = ReferenceType.construct(stringClass, null);
    }

    @Test
    public void testGetContentType() {
        Class<String> stringClass = String.class;
        JavaType stringType = SimpleType.construct(stringClass, null);
        JavaType referencedType = SimpleType.construct(Integer.class, null);
        JavaType referenceType = new ReferenceType(stringType, null, null, null, referencedType, null, null, null, false);

        assertEquals(referencedType, referenceType.getContentType());
    }

    @Test
    public void testIsReferenceType() {
        Class<String> stringClass = String.class;
        JavaType stringType = SimpleType.construct(stringClass, null);
        JavaType referencedType = SimpleType.construct(Integer.class, null);
        JavaType referenceType = new ReferenceType(stringType, null, null, null, referencedType, null, null, null, false);

        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testGetAnchorType() {
        Class<String> stringClass = String.class;
        JavaType stringType = SimpleType.construct(stringClass, null);
        JavaType referencedType = SimpleType.construct(Integer.class, null);
        JavaType referenceType = new ReferenceType(stringType, null, null, null, referencedType, null, null, null, false);

        assertNull(referenceType.getAnchorType());

        JavaType anchorType = SimpleType.construct(String[].class, null);
        JavaType newReferenceType = new ReferenceType(stringType, null, null, null, referencedType, anchorType, null, null, false);

        assertEquals(anchorType, newReferenceType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        Class<String> stringClass = String.class;
        JavaType stringType = SimpleType.construct(stringClass, null);
        JavaType referencedType = SimpleType.construct(Integer.class, null);
        JavaType referenceType = new ReferenceType(stringType, null, null, null, referencedType, referenceType, null, null, false);

        assertFalse(referenceType.isAnchorType());

        JavaType anchorType = SimpleType.construct(String[].class, null);
        JavaType newReferenceType = new ReferenceType(stringType, null, null, null, referencedType, anchorType, null, null, false);

        assertTrue(newReferenceType.isAnchorType());
    }
}