
package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, false);
        
        // Test withStaticTyping method when asStatic is false
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertEquals(true, newReferenceType._asStatic);
        
        // Test withStaticTyping method when asStatic is already true
        ReferenceType sameReferenceType = referenceType.withStaticTyping();
        assertEquals(referenceType, sameReferenceType);
        
        // Test if the new instance is not the same as the original instance
        assertNotEquals(referenceType, newReferenceType);
    }

    // Add more test cases for other methods as needed

}

