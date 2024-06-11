import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void withContentTypeTest() {
        JavaType contentType = new SimpleType();
        ReferenceType referenceType = new ReferenceType();
        referenceType.withContentType(contentType);
        assertEquals(contentType, referenceType.getContentType());
    }

    @Test
    public void withTypeHandlerTest() {
        Object typeHandler = new Object();
        ReferenceType referenceType = new ReferenceType();
        referenceType.withTypeHandler(typeHandler);
        assertEquals(typeHandler, referenceType.getTypeHandler());
    }

    @Test
    public void withContentTypeHandlerTest() {
        Object contentTypeHandler = new Object();
        ReferenceType referenceType = new ReferenceType();
        referenceType.withContentTypeHandler(contentTypeHandler);
        assertEquals(contentTypeHandler, referenceType.getContentType().getTypeHandler());
    }

    @Test
    public void withValueHandlerTest() {
        Object valueHandler = new Object();
        ReferenceType referenceType = new ReferenceType();
        referenceType.withValueHandler(valueHandler);
        assertEquals(valueHandler, referenceType.getValueHandler());
    }

    @Test
    public void withContentValueHandlerTest() {
        Object contentValueHandler = new Object();
        ReferenceType referenceType = new ReferenceType();
        referenceType.withContentValueHandler(contentValueHandler);
        assertEquals(contentValueHandler, referenceType.getContentType().getValueHandler());
    }

    @Test
    public void withStaticTypingTest() {
        ReferenceType referenceType = new ReferenceType();
        referenceType.withStaticTyping();
        assertTrue(referenceType.isStatic());
    }

    @Test
    public void buildCanonicalNameTest() {
        ReferenceType referenceType = new ReferenceType();
        String canonicalName = referenceType.buildCanonicalName();
        assertNotNull(canonicalName);
        assertFalse(canonicalName.isEmpty());
    }

    @Test
    public void narrowTest() {
        Class<?> subclass = ReferenceType.class;
        ReferenceType referenceType = new ReferenceType();
        ReferenceType narrowedType = referenceType.narrow(subclass);
        assertFalse(narrowedType == referenceType);
        assertEquals(subclass, narrowedType.getRawClass());
    }

    @Test
    public void getContentTypeTest() {
        JavaType contentType = new SimpleType();
        ReferenceType referenceType = new ReferenceType();
        referenceType.setContentType(contentType);
        assertEquals(contentType, referenceType.getContentType());
    }

    @Test
    public void getReferencedTypeTest() {
        JavaType referencedType = new SimpleType();
        ReferenceType referenceType = new ReferenceType();
        referenceType.setReferencedType(referencedType);
        assertEquals(referencedType, referenceType.getReferencedType());
    }

    @Test
    public void hasContentTypeTest() {
        ReferenceType referenceType = new ReferenceType();
        referenceType.setContentType(new SimpleType());
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void isReferenceTypeTest() {
        ReferenceType referenceType = new ReferenceType();
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void getErasedSignatureTest() {
        StringBuilder sb = new StringBuilder();
        ReferenceType referenceType = new ReferenceType();
        String erasedSignature = referenceType.getErasedSignature(sb).toString();
        assertNotNull(erasedSignature);
        assertFalse(erasedSignature.isEmpty());
    }

    @Test
    public void getGenericSignatureTest() {
        StringBuilder sb = new StringBuilder();
        ReferenceType referenceType = new ReferenceType();
        String genericSignature = referenceType.getGenericSignature(sb).toString();
        assertNotNull(genericSignature);
        assertFalse(genericSignature.isEmpty());
    }

    @Test
    public void getAnchorTypeTest() {
        JavaType anchorType = new SimpleType();
        ReferenceType referenceType = new ReferenceType();
        referenceType.setAnchorType(anchorType);
        assertEquals(anchorType, referenceType.getAnchorType());
    }

    @Test
    public void isAnchorTypeTest() {
        ReferenceType referenceType = new ReferenceType();
        assertTrue(referenceType.isAnchorType());
    }

    @Test
    public void equalsTest() {
        ReferenceType referenceType1 = new ReferenceType();
        ReferenceType referenceType2 = new ReferenceType();
        assertTrue(referenceType1.equals(referenceType2));
    }

}