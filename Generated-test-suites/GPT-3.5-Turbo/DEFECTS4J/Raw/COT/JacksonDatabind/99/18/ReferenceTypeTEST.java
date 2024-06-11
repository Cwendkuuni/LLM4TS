
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testWithContentType() {
        // Given
        JavaType baseType = new SimpleType(Object.class);
        JavaType contentType = new SimpleType(String.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, contentType);

        // When
        JavaType result = referenceType.withContentType(contentType);

        // Then
        assertEquals(contentType, result);
    }

    @Test
    public void testWithTypeHandler() {
        // Given
        JavaType baseType = new SimpleType(Object.class);
        JavaType refType = new SimpleType(String.class);
        Object typeHandler = new StringHandler();
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);

        // When
        ReferenceType result = referenceType.withTypeHandler(typeHandler);

        // Then
        assertEquals(typeHandler, result.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        // Given
        JavaType baseType = new SimpleType(Object.class);
        JavaType refType = new SimpleType(String.class);
        Object typeHandler = new StringHandler();
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);

        // When
        ReferenceType result = referenceType.withContentTypeHandler(typeHandler);

        // Then
        assertEquals(typeHandler, result.getContentType().getTypeHandler());
    }

    @Test
    public void testWithStaticTyping() {
        // Given
        JavaType baseType = new SimpleType(Object.class);
        JavaType refType = new SimpleType(String.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);

        // When
        ReferenceType result = referenceType.withStaticTyping();

        // Then
        assertTrue(result.isStaticTyping());
    }

    @Test
    public void testRefine() {
        // Given
        JavaType baseType = new SimpleType(Object.class);
        JavaType refType = new SimpleType(String.class);
        JavaType superClass = new SimpleType(Number.class);
        JavaType[] superInterfaces = new JavaType[]{new SimpleType(Comparable.class)};
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);

        // When
        JavaType result = referenceType.refine(Number.class, null, superClass, superInterfaces);

        // Then
        assertEquals(Number.class, result.getRawClass());
        assertArrayEquals(superInterfaces, result.getInterfaces());
    }

    @Test
    public void testGetContentType() {
        // Given
        JavaType baseType = new SimpleType(Object.class);
        JavaType refType = new SimpleType(String.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);

        // When
        JavaType result = referenceType.getContentType();

        // Then
        assertEquals(refType, result);
    }

    @Test
    public void testGetReferencedType() {
        // Given
        JavaType baseType = new SimpleType(Object.class);
        JavaType refType = new SimpleType(String.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);

        // When
        JavaType result = referenceType.getReferencedType();

        // Then
        assertEquals(refType, result);
    }

    @Test
    public void testHasContentType() {
        // Given
        JavaType baseType = new SimpleType(Object.class);
        JavaType refType = new SimpleType(String.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);

        // When
        boolean result = referenceType.hasContentType();

        // Then
        assertTrue(result);
    }

    @Test
    public void testIsReferenceType() {
        // Given
        JavaType baseType = new SimpleType(Object.class);
        JavaType refType = new SimpleType(String.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);

        // When
        boolean result = referenceType.isReferenceType();

        // Then
        assertTrue(result);
    }

}

class StringHandler {}
