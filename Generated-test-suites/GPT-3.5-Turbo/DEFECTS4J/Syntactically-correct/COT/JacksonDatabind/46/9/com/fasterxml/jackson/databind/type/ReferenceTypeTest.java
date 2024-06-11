
import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.databind.type.ReferenceType;
import com.fasterxml.jackson.databind.JavaType;

public class ReferenceTypeTest {
    
    @Test
    public void testConstruct() {
        ReferenceType referenceType = ReferenceType.construct(String.class, JavaType.unknownType(),
                null, null);
        Assert.assertNotNull(referenceType);
        Assert.assertEquals(String.class, referenceType.getRawClass());
        Assert.assertEquals(JavaType.unknownType(), referenceType.getReferencedType());
    }
    
    @Test
    public void testWithTypeHandler() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.unknownType(),
                null, null, false);
        Object newTypeHandler = new Object();
        ReferenceType newReferenceType = referenceType.withTypeHandler(newTypeHandler);
        Assert.assertNotEquals(referenceType, newReferenceType);
        Assert.assertEquals(String.class, newReferenceType.getRawClass());
        Assert.assertEquals(JavaType.unknownType(), newReferenceType.getReferencedType());
        Assert.assertEquals(newTypeHandler, newReferenceType.getTypeHandler());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.unknownType(),
                null, null, false);
        Object newContentTypeHandler = new Object();
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(newContentTypeHandler);
        Assert.assertNotEquals(referenceType, newReferenceType);
        Assert.assertEquals(String.class, newReferenceType.getRawClass());
        Assert.assertEquals(JavaType.unknownType(), newReferenceType.getReferencedType());
        Assert.assertEquals(newContentTypeHandler, newReferenceType.getReferencedType().getTypeHandler());
    }
    
    @Test
    public void testWithValueHandler() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.unknownType(),
                null, null, false);
        Object newValueHandler = new Object();
        ReferenceType newReferenceType = referenceType.withValueHandler(newValueHandler);
        Assert.assertNotEquals(referenceType, newReferenceType);
        Assert.assertEquals(String.class, newReferenceType.getRawClass());
        Assert.assertEquals(JavaType.unknownType(), newReferenceType.getReferencedType());
        Assert.assertEquals(newValueHandler, newReferenceType.getValueHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.unknownType(),
                null, null, false);
        Object newContentValueHandler = new Object();
        ReferenceType newReferenceType = referenceType.withContentValueHandler(newContentValueHandler);
        Assert.assertNotEquals(referenceType, newReferenceType);
        Assert.assertEquals(String.class, newReferenceType.getRawClass());
        Assert.assertEquals(JavaType.unknownType(), newReferenceType.getReferencedType());
        Assert.assertEquals(newContentValueHandler, newReferenceType.getReferencedType().getValueHandler());
    }
    
    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.unknownType(),
                null, null, false);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        Assert.assertNotEquals(referenceType, newReferenceType);
        Assert.assertEquals(String.class, newReferenceType.getRawClass());
        Assert.assertEquals(JavaType.unknownType(), newReferenceType.getReferencedType());
        Assert.assertTrue(newReferenceType.isStaticTyping());
    }
    
    @Test
    public void testGetReferencedType() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.unknownType(),
                null, null, false);
        JavaType referencedType = referenceType.getReferencedType();
        Assert.assertEquals(JavaType.unknownType(), referencedType);
    }
    
    @Test
    public void testIsReferenceType() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.unknownType(),
                null, null, false);
        Assert.assertTrue(referenceType.isReferenceType());
    }
    
    @Test
    public void testContainedTypeCount() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.unknownType(),
                null, null, false);
        Assert.assertEquals(1, referenceType.containedTypeCount());
    }
    
    @Test
    public void testContainedType() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.unknownType(),
                null, null, false);
        JavaType containedType = referenceType.containedType(0);
        Assert.assertEquals(JavaType.unknownType(), containedType);
    }
    
    @Test
    public void testContainedTypeName() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.unknownType(),
                null, null, false);
        String containedTypeName = referenceType.containedTypeName(0);
        Assert.assertEquals("T", containedTypeName);
    }
    
    @Test
    public void testGetParameterSource() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.unknownType(),
                null, null, false);
        Class<?> parameterSource = referenceType.getParameterSource();
        Assert.assertEquals(String.class, parameterSource);
    }
    
    @Test
    public void testGetErasedSignature() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.unknownType(),
                null, null, false);
        StringBuilder sb = new StringBuilder();
        StringBuilder erasedSignature = referenceType.getErasedSignature(sb);
        Assert.assertEquals("Ljava/lang/String;", erasedSignature.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.unknownType(),
                null, null, false);
        StringBuilder sb = new StringBuilder();
        StringBuilder genericSignature = referenceType.getGenericSignature(sb);
        Assert.assertEquals("Ljava/lang/String<T>;", genericSignature.toString());
    }
    
}
