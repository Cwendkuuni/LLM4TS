package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testConstructor() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        
        Assert.assertEquals(erasedType, type.getErasedType());
        Assert.assertEquals(bindings, type.getTypeBindings());
        Assert.assertNull(type.getContentType());
        Assert.assertNull(type.getKeyType());
        Assert.assertNull(type.getReferencedType());
        Assert.assertEquals(0, type.getBindingsCount());
        Assert.assertNull(type.getSubTypeHandler());
        Assert.assertNull(type.getValueHandler());
        Assert.assertFalse(type.isContainerType());
        Assert.assertFalse(type.isReferenceType());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSetReference_ThrowsExceptionWhenRepeatedlyCalled() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        type.setReference(new JavaType() {});
        type.setReference(new JavaType() {}); // Throws exception
    }
    
    @Test
    public void testGetSelfReferencedType_ReturnsNullWhenNotSet() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        Assert.assertNull(type.getSelfReferencedType());
    }
    
    @Test
    public void testGetGenericSignature_ReturnsGenericSignatureOfReferencedType() {
        JavaType referencedType = new JavaType() {
            public StringBuilder getGenericSignature(StringBuilder sb) {
                return sb.append("Lcom/fasterxml/jackson/databind/java/TypeFactory;");
            }
        };
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        type.setReference(referencedType);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getGenericSignature(sb);
        
        Assert.assertEquals("Lcom/fasterxml/jackson/databind/java/TypeFactory;", result.toString());
    }
    
    @Test
    public void testGetErasedSignature_ReturnsErasedSignatureOfReferencedType() {
        JavaType referencedType = new JavaType() {
            public StringBuilder getErasedSignature(StringBuilder sb) {
                return sb.append("Ljava/lang/String;");
            }
        };
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        type.setReference(referencedType);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getErasedSignature(sb);
        
        Assert.assertEquals("Ljava/lang/String;", result.toString());
    }
    
    @Test
    public void testWithTypeHandler_ReturnsOriginalType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        JavaType result = type.withTypeHandler(new Object());
        
        Assert.assertEquals(type, result);
    }
    
    @Test
    public void testWithContentTypeHandler_ReturnsOriginalType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        JavaType result = type.withContentTypeHandler(new Object());
        
        Assert.assertEquals(type, result);
    }
    
    @Test
    public void testWithValueHandler_ReturnsOriginalType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        JavaType result = type.withValueHandler(new Object());
        
        Assert.assertEquals(type, result);
    }
    
    @Test
    public void testWithContentValueHandler_ReturnsOriginalType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        JavaType result = type.withContentValueHandler(new Object());
        
        Assert.assertEquals(type, result);
    }
    
    @Test
    public void testWithStaticTyping_ReturnsOriginalType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        JavaType result = type.withStaticTyping();
        
        Assert.assertEquals(type, result);
    }
    
    @Test
    public void testNarrow_ReturnsOriginalType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        JavaType result = type._narrow(String.class);
        
        Assert.assertEquals(type, result);
    }
    
    @Test
    public void testRefine_ReturnsNull() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        JavaType result = type.refine(Object.class, new TypeBindings(), new JavaType() {}, new JavaType[] {});
        
        Assert.assertNull(result);
    }
    
    @Test
    public void testIsContainerType_ReturnsFalse() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        boolean result = type.isContainerType();
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testToString_ReturnsStringRepresentation() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        String result = type.toString();
        
        Assert.assertEquals("[recursive type; UNRESOLVED]", result);
    }
    
    @Test
    public void testEquals_ReturnsTrueWhenSameInstance() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        boolean result = type.equals(type);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testEquals_ReturnsFalseWhenNull() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        boolean result = type.equals(null);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testEquals_ReturnsFalseWhenDifferentClass() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object obj = new Object();
        
        boolean result = type.equals(obj);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testEquals_ReturnsFalseWhenReferencedTypeIsNull() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, new TypeBindings());
        
        boolean result = type1.equals(type2);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testEquals_ReturnsFalseWhenReferencedTypesAreDifferent() {
        JavaType referencedType1 = new JavaType() {
            @Override
            public boolean equals(Object obj) {
                return false;
            }
        };
        JavaType referencedType2 = new JavaType() {
            @Override
            public boolean equals(Object obj) {
                return false;
            }
        };
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        type1.setReference(referencedType1);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, new TypeBindings());
        type2.setReference(referencedType2);
        
        boolean result = type1.equals(type2);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testEquals_ReturnsTrueWhenReferencedTypesAreEqual() {
        JavaType referencedType = new JavaType() {
            @Override
            public boolean equals(Object obj) {
                return true;
            }
        };
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        type1.setReference(referencedType);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, new TypeBindings());
        type2.setReference(referencedType);
        
        boolean result = type1.equals(type2);
        
        Assert.assertTrue(result);
    }

}