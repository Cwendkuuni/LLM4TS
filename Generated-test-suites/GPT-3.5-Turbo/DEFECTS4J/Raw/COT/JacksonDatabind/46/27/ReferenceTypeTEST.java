import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {
  
  private ReferenceType referenceType;
  
  @Before
  public void setUp() {
    referenceType = ReferenceType.construct(String.class, JavaType.STRING, null, null);
  }
  
  @Test
  public void testWithTypeHandler() {
    Object typeHandler = new Object();
    ReferenceType updatedReferenceType = referenceType.withTypeHandler(typeHandler);
    
    assertEquals(typeHandler, updatedReferenceType.getTypeHandler());
  }
  
  @Test
  public void testWithContentTypeHandler() {
    Object contentTypeHandler = new Object();
    ReferenceType updatedReferenceType = referenceType.withContentTypeHandler(contentTypeHandler);
    
    assertEquals(contentTypeHandler, updatedReferenceType.getContentTypeHandler());
  }
  
  @Test
  public void testWithValueHandler() {
    Object valueHandler = new Object();
    ReferenceType updatedReferenceType = referenceType.withValueHandler(valueHandler);
    
    assertEquals(valueHandler, updatedReferenceType.getValueHandler());
  }
  
  @Test
  public void testWithContentValueHandler() {
    Object contentValueHandler = new Object();
    ReferenceType updatedReferenceType = referenceType.withContentValueHandler(contentValueHandler);
    
    assertEquals(contentValueHandler, updatedReferenceType.getContentValueHandler());
  }
  
  @Test
  public void testWithStaticTyping() {
    ReferenceType updatedReferenceType = referenceType.withStaticTyping();
    
    assertTrue(updatedReferenceType.isStaticTyping());
  }
  
  @Test
  public void testBuildCanonicalName() {
    String canonicalName = referenceType.buildCanonicalName();
    
    assertEquals("java.lang.String", canonicalName);
  }
  
  @Test
  public void testNarrow() {
    Class<?> subclass = Integer.class;
    ReferenceType narrowedReferenceType = referenceType._narrow(subclass);
    
    assertEquals(subclass, narrowedReferenceType.getClass());
  }
  
  @Test
  public void testGetReferencedType() {
    JavaType referencedType = referenceType.getReferencedType();
    
    assertEquals(JavaType.STRING, referencedType);
  }
  
  @Test
  public void testIsReferenceType() {
    assertTrue(referenceType.isReferenceType());
  }
  
  @Test
  public void testContainedTypeCount() {
    int containedTypeCount = referenceType.containedTypeCount();
    
    assertEquals(1, containedTypeCount);
  }
  
  @Test
  public void testContainedType() {
    JavaType containedType = referenceType.containedType(0);
    
    assertEquals(JavaType.STRING, containedType);
  }
  
  @Test
  public void testContainedTypeName() {
    String containedTypeName = referenceType.containedTypeName(0);
    
    assertEquals("T", containedTypeName);
  }
  
  @Test
  public void testGetParameterSource() {
    Class<?> parameterSource = referenceType.getParameterSource();
    
    assertEquals(String.class, parameterSource);
  }
  
  @Test
  public void testGetErasedSignature() {
    StringBuilder sb = new StringBuilder();
    StringBuilder erasedSignature = referenceType.getErasedSignature(sb);
    
    assertNotNull(erasedSignature);
    assertEquals("Ljava/lang/String;", erasedSignature.toString());
  }
  
  @Test
  public void testGetGenericSignature() {
    StringBuilder sb = new StringBuilder();
    StringBuilder genericSignature = referenceType.getGenericSignature(sb);
    
    assertNotNull(genericSignature);
    assertEquals("Ljava/lang/String;", genericSignature.toString());
  }
  
  @Test
  public void testToString() {
    String typeString = referenceType.toString();
    String expectedString = "[reference type, class java.lang.String<java.lang.String>]";
    
    assertEquals(expectedString, typeString);
  }
  
  @Test
  public void testEquals() {
    ReferenceType sameReferenceType = ReferenceType.construct(String.class, JavaType.STRING, null, null);
    ReferenceType differentReferenceType = ReferenceType.construct(Integer.class, JavaType.INT, null, null);
    
    assertTrue(referenceType.equals(sameReferenceType));
    assertFalse(referenceType.equals(differentReferenceType));
  }
}