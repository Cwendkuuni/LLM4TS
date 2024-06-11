
package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {
    @Test
    public void testWithContentType() {
        ReferenceType rt = new ReferenceType();
        JavaType contentType = new JavaType();

        JavaType result = rt.withContentType(contentType);

        assertEquals(contentType, result.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        ReferenceType rt = new ReferenceType();
        Object handler = new Object();

        ReferenceType result = rt.withTypeHandler(handler);

        assertEquals(handler, result.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        ReferenceType rt = new ReferenceType();
        Object handler = new Object();

        ReferenceType result = rt.withContentTypeHandler(handler);

        assertEquals(handler, result.getContentType().getTypeHandler());
    }
    
    // Test edge case: contentType and referencedType are the same object
    @Test
    public void testWithContentTypeSame() {
        ReferenceType rt = new ReferenceType();
        JavaType contentType = new JavaType();
        rt.withContentType(contentType);

        JavaType result = rt.withContentType(contentType);

        assertSame(contentType, result.getContentType());
    }
    
    // Test edge case: typeHandler and referencedType.typeHandler are the same object
    @Test
    public void testWithTypeHandlerSame() {
        ReferenceType rt = new ReferenceType();
        Object handler = new Object();
        rt.withTypeHandler(handler);

        ReferenceType result = rt.withTypeHandler(handler);

        assertSame(handler, result.getTypeHandler());
    }
    
    // Test exception: Missing referencedType
    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromMissingReferencedType() {
        JavaType baseType = new JavaType();

        ReferenceType.upgradeFrom(baseType, null);
    }

    // Test exception: Can not upgrade from an instance of the baseType class
    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType baseType = new JavaTypeInvalid();

        ReferenceType.upgradeFrom(baseType, new JavaType());
    }

    // Test exception: Missing referencedType in deprecated construct method
    @Test(expected = IllegalArgumentException.class)
    public void testConstructMissingReferencedType() {
        ReferenceType.construct(ReferenceType.class, null);
    }

    // Test referencedType in construct method
    @Test
    public void testConstruct() {
        JavaType referencedType = new JavaType();

        ReferenceType result = ReferenceType.construct(ReferenceType.class, referencedType);

        assertEquals(referencedType, result.getReferencedType());
    }

    // Test isReferenceType
    @Test
    public void testIsReferenceType() {
        ReferenceType rt = new ReferenceType();

        boolean result = rt.isReferenceType();

        assertTrue(result);
    }
}
