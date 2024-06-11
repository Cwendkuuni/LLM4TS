import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new JavaType() {
        };
        resolvedType.setReference(ref);
        assertEquals(ref, resolvedType.getSelfReferencedType());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSetReference_WhenAlreadySet_ThrowsIllegalStateException() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, null);
        JavaType ref1 = new JavaType() {
        };
        resolvedType.setReference(ref1);
        JavaType ref2 = new JavaType() {
        };
        resolvedType.setReference(ref2);
    }
    
    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new JavaType() {
            @Override
            public StringBuilder getGenericSignature(StringBuilder sb) {
                sb.append("Generic Signature");
                return sb;
            }
        };
        resolvedType.setReference(ref);
        StringBuilder sb = new StringBuilder();
        assertEquals("Generic Signature", resolvedType.getGenericSignature(sb).toString());
    }
    
    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new JavaType() {
            @Override
            public StringBuilder getErasedSignature(StringBuilder sb) {
                sb.append("Erased Signature");
                return sb;
            }
        };
        resolvedType.setReference(ref);
        StringBuilder sb = new StringBuilder();
        assertEquals("Erased Signature", resolvedType.getErasedSignature(sb).toString());
    }
    
    @Test
    public void testWithContentType() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new JavaType() {
        };
        resolvedType.setReference(ref);
        assertSame(resolvedType, resolvedType.withContentType(ref));
    }
    
    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, null);
        assertSame(resolvedType, resolvedType.withTypeHandler(null));
    }
    
    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, null);
        assertSame(resolvedType, resolvedType.withContentTypeHandler(null));
    }
    
    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, null);
        assertSame(resolvedType, resolvedType.withValueHandler(null));
    }
    
    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, null);
        assertSame(resolvedType, resolvedType.withContentValueHandler(null));
    }
    
    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, null);
        assertSame(resolvedType, resolvedType.withStaticTyping());
    }
    
    @Test
    public void test_narrow() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new JavaType() {
        };
        resolvedType.setReference(ref);
        assertSame(resolvedType, resolvedType._narrow(Object.class));
    }
    
    @Test
    public void testRefine() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, null);
        assertNull(resolvedType.refine(null, null, null, null));
    }
    
    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, null);
        assertFalse(resolvedType.isContainerType());
    }
    
    @Test
    public void testToString() {
        ResolvedRecursiveType resolvedType1 = new ResolvedRecursiveType(String.class, null);
        assertEquals("[recursive type; UNRESOLVED]", resolvedType1.toString());
        
        JavaType ref = new JavaType() {
            @Override
            public Class getRawClass() {
                return String.class;
            }
        };
        ResolvedRecursiveType resolvedType2 = new ResolvedRecursiveType(String.class, null);
        resolvedType2.setReference(ref);
        assertEquals("[recursive type; java.lang.String]", resolvedType2.toString());
    }
    
    @Test
    public void testEquals() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(String.class, null);
        
        assertFalse(resolvedType.equals(null));
        assertFalse(resolvedType.equals(this));
        
        JavaType ref1 = new JavaType() {
        };
        
        assertFalse(resolvedType.equals(new ResolvedRecursiveType(String.class, null)));
        assertFalse(resolvedType.equals(new ResolvedRecursiveType(String.class, null)));
        assertFalse(resolvedType.equals(new ResolvedRecursiveType(Integer.class, null)));
        
        resolvedType.setReference(ref1);
        assertFalse(resolvedType.equals(new ResolvedRecursiveType(String.class, null)));
        
        JavaType ref2 = new JavaType() {
            @Override
            public JavaType getSelfReferencedType() {
                return ref1;
            }
        };
        
        assertTrue(resolvedType.equals(new ResolvedRecursiveType(String.class, null)));
    }
}