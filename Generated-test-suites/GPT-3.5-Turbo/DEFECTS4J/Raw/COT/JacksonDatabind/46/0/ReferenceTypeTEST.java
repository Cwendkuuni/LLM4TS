
import org.junit.Test;

public class ReferenceTypeTest {
    
    @Test
    public void testConstruct() {
        Class<?> cls = ReferenceType.class;
        JavaType refType = new SimpleType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        ReferenceType rt = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        
        // Assert statements
    }
    
    @Test
    public void testWithTypeHandler() {
        ReferenceType rt = new ReferenceType();
        Object typeHandler = new Object();
        
        ReferenceType result = rt.withTypeHandler(typeHandler);
        
        // Assert statements
    }
    
    @Test
    public void testWithContentTypeHandler() {
        ReferenceType rt = new ReferenceType();
        Object contentTypeHandler = new Object();
        
        ReferenceType result = rt.withContentTypeHandler(contentTypeHandler);
        
        // Assert statements
    }
    
    @Test
    public void testWithValueHandler() {
        ReferenceType rt = new ReferenceType();
        Object valueHandler = new Object();
        
        ReferenceType result = rt.withValueHandler(valueHandler);
        
        // Assert statements
    }
    
    @Test
    public void testWithContentValueHandler() {
        ReferenceType rt = new ReferenceType();
        Object contentValueHandler = new Object();
        
        ReferenceType result = rt.withContentValueHandler(contentValueHandler);
        
        // Assert statements
    }
    
    @Test
    public void testWithStaticTyping() {
        ReferenceType rt = new ReferenceType();
        
        ReferenceType result = rt.withStaticTyping();
        
        // Assert statements
    }
    
    @Test
    public void testBuildCanonicalName() {
        ReferenceType rt = new ReferenceType();
        
        String canonicalName = rt.buildCanonicalName();
        
        // Assert statements
    }
    
    @Test
    public void testNarrow() {
        ReferenceType rt = new ReferenceType();
        Class<?> subclass = ReferenceSubType.class;
        
        JavaType result = rt._narrow(subclass);
        
        // Assert statements
    }
    
    @Test
    public void testGetReferencedType() {
        ReferenceType rt = new ReferenceType();
        
        JavaType referencedType = rt.getReferencedType();
        
        // Assert statements
    }
    
    @Test
    public void testIsReferenceType() {
        ReferenceType rt = new ReferenceType();
        
        boolean isReference = rt.isReferenceType();
        
        // Assert statements
    }
    
    @Test
    public void testContainedTypeCount() {
        ReferenceType rt = new ReferenceType();
        
        int count = rt.containedTypeCount();
        
        // Assert statements
    }
    
    @Test
    public void testContainedType() {
        ReferenceType rt = new ReferenceType();
        int index = 0;
        
        JavaType containedType = rt.containedType(index);
        
        // Assert statements
    }
    
    @Test
    public void testContainedTypeName() {
        ReferenceType rt = new ReferenceType();
        int index = 0;
        
        String containedTypeName = rt.containedTypeName(index);
        
        // Assert statements
    }
    
    @Test
    public void testGetParameterSource() {
        ReferenceType rt = new ReferenceType();
        
        Class<?> parameterSource = rt.getParameterSource();
        
        // Assert statements
    }
    
    @Test
    public void testGetErasedSignature() {
        ReferenceType rt = new ReferenceType();
        StringBuilder sb = new StringBuilder();
        
        StringBuilder erasedSignature = rt.getErasedSignature(sb);
        
        // Assert statements
    }
    
    @Test
    public void testGetGenericSignature() {
        ReferenceType rt = new ReferenceType();
        StringBuilder sb = new StringBuilder();
        
        StringBuilder genericSignature = rt.getGenericSignature(sb);
        
        // Assert statements
    }
    
    @Test
    public void testToString() {
        ReferenceType rt = new ReferenceType();
        
        String string = rt.toString();
        
        // Assert statements
    }
    
    @Test
    public void testEquals() {
        ReferenceType rt1 = new ReferenceType();
        ReferenceType rt2 = new ReferenceType();
        
        boolean equals = rt1.equals(rt2);
        
        // Assert statements
    }
}
