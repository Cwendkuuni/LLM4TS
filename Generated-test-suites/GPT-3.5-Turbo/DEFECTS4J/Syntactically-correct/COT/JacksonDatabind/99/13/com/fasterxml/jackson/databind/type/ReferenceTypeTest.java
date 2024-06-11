import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testWithContentType_SameContentType_ReturnsSameReferenceType() {
        ReferenceType referenceType = new ReferenceType();
        JavaType contentType = new JavaType();
        referenceType.setContentType(contentType);
        ReferenceType resultType = referenceType.withContentType(contentType);
        assertSame(referenceType, resultType);
    }

    @Test
    public void testWithContentType_DifferentContentType_ReturnsNewReferenceTypeWithUpdatedContentType() {
        ReferenceType referenceType = new ReferenceType();
        JavaType contentType = new JavaType();
        referenceType.setContentType(contentType);
        JavaType newContentType = new JavaType();
        ReferenceType resultType = referenceType.withContentType(newContentType);
        assertNotSame(referenceType, resultType);
        assertSame(newContentType, resultType.getContentType());
    }

    @Test
    public void testWithTypeHandler_SameTypeHandler_ReturnsSameReferenceType() {
        ReferenceType referenceType = new ReferenceType();
        Object typeHandler = new Object();
        referenceType.setTypeHandler(typeHandler);
        ReferenceType resultType = referenceType.withTypeHandler(typeHandler);
        assertSame(referenceType, resultType);
    }

    @Test
    public void testWithTypeHandler_DifferentTypeHandler_ReturnsNewReferenceTypeWithUpdatedTypeHandler() {
        ReferenceType referenceType = new ReferenceType();
        Object typeHandler = new Object();
        referenceType.setTypeHandler(typeHandler);
        Object newTypeHandler = new Object();
        ReferenceType resultType = referenceType.withTypeHandler(newTypeHandler);
        assertNotSame(referenceType, resultType);
        assertSame(newTypeHandler, resultType.getTypeHandler());
    }

    @Test
    public void testWithContentValueHandler_SameContentValueHandler_ReturnsSameReferenceType() {
        ReferenceType referenceType = new ReferenceType();
        Object valueHandler = new Object();
        referenceType.setContentValueHandler(valueHandler);
        ReferenceType resultType = referenceType.withContentValueHandler(valueHandler);
        assertSame(referenceType, resultType);
    }

    @Test
    public void testWithContentValueHandler_DifferentContentValueHandler_ReturnsNewReferenceTypeWithUpdatedContentValueHandler() {
        ReferenceType referenceType = new ReferenceType();
        Object valueHandler = new Object();
        referenceType.setContentValueHandler(valueHandler);
        Object newValueHandler = new Object();
        ReferenceType resultType = referenceType.withContentValueHandler(newValueHandler);
        assertNotSame(referenceType, resultType);
        assertSame(newValueHandler, resultType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping_AlreadyStatic_ReturnsSameReferenceType() {
        ReferenceType referenceType = new ReferenceType();
        referenceType.setStaticTyping(true);
        ReferenceType resultType = referenceType.withStaticTyping();
        assertSame(referenceType, resultType);
    }

    @Test
    public void testWithStaticTyping_NonStatic_ReturnsNewReferenceTypeWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType();
        referenceType.setStaticTyping(false);
        ReferenceType resultType = referenceType.withStaticTyping();
        assertNotSame(referenceType, resultType);
        assertTrue(resultType.isStaticTyping());
    }

    @Test
    public void testRefine_ValidArguments_ReturnsNewReferenceTypeWithUpdatedSuperClassAndSuperInterfaces() {
        ReferenceType referenceType = new ReferenceType();
        Class<?> rawType = new Class<?>();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
        ReferenceType resultType = referenceType.refine(rawType, bindings, superClass, superInterfaces);
        assertNotSame(referenceType, resultType);
        assertSame(rawType, resultType.getRawType());
        assertSame(bindings, resultType.getTypeBindings());
        assertSame(superClass, resultType.getSuperClass());
        assertSame(superInterfaces, resultType.getSuperInterfaces());
    }

    @Test
    public void testGetAnchorType_AlreadyAnchorType_ReturnsSameReferenceType() {
        ReferenceType referenceType = new ReferenceType();
        ReferenceType anchorType = new ReferenceType();
        referenceType.setAnchorType(anchorType);
        ReferenceType resultType = referenceType.getAnchorType();
        assertSame(referenceType, resultType);
    }

    @Test
    public void testGetAnchorType_NotAnchorType_ReturnsAnchorType() {
        ReferenceType referenceType = new ReferenceType();
        ReferenceType anchorType = new ReferenceType();
        referenceType.setAnchorType(anchorType);
        ReferenceType resultType = referenceType.getAnchorType();
        assertNotSame(referenceType, resultType);
        assertSame(anchorType, resultType);
    }

    @Test
    public void testIsAnchorType_AlreadyAnchorType_ReturnsTrue() {
        ReferenceType referenceType = new ReferenceType();
        ReferenceType anchorType = new ReferenceType();
        referenceType.setAnchorType(anchorType);
        boolean result = referenceType.isAnchorType();
        assertTrue(result);
    }

    @Test
    public void testIsAnchorType_NotAnchorType_ReturnsFalse() {
        ReferenceType referenceType = new ReferenceType();
        ReferenceType anchorType = new ReferenceType();
        referenceType.setAnchorType(anchorType);
        boolean result = referenceType.isAnchorType();
        assertFalse(result);
    }
}