import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class TypeParserTest {

    @Test
    public void testParse_WhenCanonicalIsEmpty_ThrowsIllegalArgumentException() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        
        try {
            parser.parse("");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", 
                         e.getMessage());
        }
    }

    @Test
    public void testParse_WhenCanonicalHasUnknownClass_ThrowsIllegalArgumentException() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        
        try {
            parser.parse("com.example.UnknownClass");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'com.example.UnknownClass' (remaining: 'com.example.UnknownClass'): " +
                         "Can not locate class 'com.example.UnknownClass', problem: UnknownClass", 
                         e.getMessage());
        }
    }

    @Test
    public void testParse_WhenCanonicalIsNonGenericClass_ReturnsJavaType() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        
        try {
            JavaType javaType = parser.parse("java.lang.String");
            assertNotNull(javaType);
            assertEquals(String.class, javaType.getRawClass());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            fail("Should not throw IllegalArgumentException");
        }
    }

    @Test
    public void testParse_WhenCanonicalIsGenericClass_ReturnsJavaTypeWithParameterTypes() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        
        try {
            JavaType javaType = parser.parse("java.util.List<java.lang.String>");
            assertNotNull(javaType);
            assertEquals(List.class, javaType.getRawClass());
            assertEquals(1, javaType.containedTypeCount());
            assertEquals(String.class, javaType.containedType(0).getRawClass());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            fail("Should not throw IllegalArgumentException");
        }
    }
    
    @Test
    public void testParse_WhenCanonicalHasUnexpectedTokensAfterCompleteType_ThrowsIllegalArgumentException() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        
        try {
            parser.parse("java.util.List<java.lang.String> unexpected");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.util.List<java.lang.String> unexpected' " + 
                         "(remaining: 'unexpected'): Unexpected tokens after complete type", 
                         e.getMessage());
        }
    }
    
    @Test
    public void testParse_WhenCanonicalTypeDoesNotEnd_ReturnsJavaTypeWithParameterTypes() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        
        try {
            JavaType javaType = parser.parse("java.util.List<java.lang.String,");
            assertNotNull(javaType);
            assertEquals(List.class, javaType.getRawClass());
            assertEquals(1, javaType.containedTypeCount());
            assertEquals(String.class, javaType.containedType(0).getRawClass());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            fail("Should not throw IllegalArgumentException");
        }
    }
    
    @Test
    public void testParse_WhenCanonicalTypeHasInvalidTokensAfterClosingGenericType_ThrowsIllegalArgumentException() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        
        try {
            parser.parse("java.util.List<java.lang.String<");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.util.List<java.lang.String<' " + 
                         "(remaining: '<'): Unexpected token '<', expected ',' or '>')", 
                         e.getMessage());
        }
    }
    
    @Test
    public void testParse_WhenCanonicalTypeHasIncompleteGenericType_ThrowsIllegalArgumentException() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        
        try {
            parser.parse("java.util.List<java.lang.String");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.util.List<java.lang.String' (remaining: 'java.lang.String'): " +
                         "Unexpected end-of-string", 
                         e.getMessage());
        }
    }
}