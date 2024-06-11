package com.example; // replace with your test package if needed

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for JavaType.
 */
public class JavaTypeTest {

    

    @Test
    public void testConstructor() {
        Class<String> rawType = String.class;
        JavaType javaType = new JavaType(rawType, 0, null, null, false);
        assertNotNull(javaType);
        assertEquals(rawType, javaType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSubclass() {
        Class<String> rawType = String.class;
        JavaType javaType = new JavaType(rawType, 0, null, null, false);
        JavaType invalidSubclass = javaType.narrowBy(Integer.class);
    }

    @Test
    public void testNarrowBy() {
        Class<String> rawType = String.class;
        JavaType javaType = new JavaType(rawType, 0, null, null, false);
        JavaType stringType = javaType.narrowBy(String.class);
        JavaType stringBufferType = javaType.narrowBy(StringBuffer.class);

        assertNotNull(stringType);
        assertNotNull(stringBufferType);
        assertTrue(stringType.isInstance(new String("hello")));
        assertTrue(stringBufferType.isInstance(new StringBuffer("hello")));
    }

    @Test
    public void testWidenBy() {
        Class<String> rawType = String.class;
        JavaType javaType = new JavaType(rawType, 0, null, null, false);
        JavaType objectType = javaType.widenBy(Object.class);

        assertNotNull(objectType);
        assertTrue(objectType.isInstance(new Object()));
        assertFalse(objectType.isInstance(new String("hello")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSuperclass() {
        Class<String> rawType = String.class;
        JavaType javaType = new JavaType(rawType, 0, null, null, false);
        javaType.widenBy(String.class);
    }

    @Test
    public void testGetHashCode() {
        Class<String> rawType = String.class;
        JavaType javaType1 = new JavaType(rawType, 0, null, null, false);
        JavaType javaType2 = new JavaType(rawType, 0, null, null, false);

        int hashCode1 = javaType1.hashCode();
        int hashCode2 = javaType2.hashCode();

        assertNotEquals(hashCode1, hashCode2);
    }

    
}