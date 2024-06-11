import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);

        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(Integer.class, referenceType.getContentType().getRawClass());
    }

    @Test
    public void testReferenceTypeMethods() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);

        JavaType contentType = referenceType.withContentType(refType);
        assertEquals(Integer.class, contentType.getRawClass());
        assertEquals(Integer.class, referenceType.getContentType().getRawClass());

        JavaType anchorType = referenceType.getAnchorType();
        assertEquals(referenceType, anchorType);

        JavaType withTypeHandler = referenceType.withTypeHandler(null);
        assertNotSame(referenceType, withTypeHandler);

        JavaType withContentTypeHandler = referenceType.withContentTypeHandler(null);
        assertNotSame(referenceType, withContentTypeHandler);

        JavaType withValueHandler = referenceType.withValueHandler(null);
        assertNotSame(referenceType, withValueHandler);

        JavaType withContentValueHandler = referenceType.withContentValueHandler(null);
        assertNotSame(referenceType, withContentValueHandler);

        JavaType withStaticTyping = referenceType.withStaticTyping();
        assertEquals(referenceType.getRawClass(), withStaticTyping.getRawClass());
        assertTrue(withStaticTyping.isStatic());

        JavaType refine = referenceType.refine(String[].class, TypeBindings.emptyBindings(), null, new JavaType[] {});
        assertNotSame(referenceType, refine);

        String canonicalName = referenceType.buildCanonicalName();
        assertEquals("class [Ljava.lang.String;<Ljava.lang.Integer;>", canonicalName);

        assertEquals(referenceType, referenceType.withContentType(refType));
        assertEquals(Integer.class, referenceType.getReferencedType().getRawClass());
        assertEquals(true, referenceType.isReferenceType());
        assertEquals(true, referenceType.hasContentType());
        assertEquals(String.class, referenceType.getRawClass());
    }
}