import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ReferenceTypeTest {

  /**
   * Test case for the construct method.
   * This method should create a new instance of ReferenceType with the given parameters.
   * Typical use case.
   */
  @Test
  public void testConstruct() {
    Class<?> cls = String.class;
    JavaType refType = new SimpleType();
    Object valueHandler = new Object();
    Object typeHandler = new Object();
    
    ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
    
    assertEquals(cls, referenceType._class);
    assertEquals(refType, referenceType._referencedType);
    assertEquals(valueHandler, referenceType._valueHandler);
    assertEquals(typeHandler, referenceType._typeHandler);
    assertEquals(false, referenceType._asStatic);
  }
  
  /**
   * Test case for the withTypeHandler method.
   * This method should return a new instance of ReferenceType with the given type handler.
   * Typical use case.
   */
  @Test
  public void testWithTypeHandler() {
    Object h = new Object();
    
    ReferenceType referenceType = new ReferenceType();
    ReferenceType newReferenceType = referenceType.withTypeHandler(h);
    
    assertEquals(h, newReferenceType._typeHandler);
    assertEquals(referenceType._class, newReferenceType._class);
    assertEquals(referenceType._referencedType, newReferenceType._referencedType);
    assertEquals(referenceType._valueHandler, newReferenceType._valueHandler);
    assertEquals(referenceType._asStatic, newReferenceType._asStatic);
  }
  
  /**
   * Test case for the withContentTypeHandler method.
   * This method should return a new instance of ReferenceType with the given content type handler.
   * Typical use case.
   */
  @Test
  public void testWithContentTypeHandler() {
    Object h = new Object();
    
    ReferenceType referenceType = new ReferenceType();
    ReferenceType newReferenceType = referenceType.withContentTypeHandler(h);
    
    assertEquals(h, newReferenceType._referencedType.<Object>getTypeHandler());
    assertEquals(referenceType._class, newReferenceType._class);
    assertEquals(referenceType._referencedType.withTypeHandler(h), newReferenceType._referencedType);
    assertEquals(referenceType._valueHandler, newReferenceType._valueHandler);
    assertEquals(referenceType._typeHandler, newReferenceType._typeHandler);
    assertEquals(referenceType._asStatic, newReferenceType._asStatic);
  }
  
  /**
   * Test case for the withValueHandler method.
   * This method should return a new instance of ReferenceType with the given value handler.
   * Typical use case.
   */
  @Test
  public void testWithValueHandler() {
    Object h = new Object();
    
    ReferenceType referenceType = new ReferenceType();
    ReferenceType newReferenceType = referenceType.withValueHandler(h);
    
    assertEquals(h, newReferenceType._valueHandler);
    assertEquals(referenceType._class, newReferenceType._class);
    assertEquals(referenceType._referencedType, newReferenceType._referencedType);
    assertEquals(referenceType._typeHandler, newReferenceType._typeHandler);
    assertEquals(referenceType._asStatic, newReferenceType._asStatic);
  }
  
  /**
   * Test case for the withContentValueHandler method.
   * This method should return a new instance of ReferenceType with the given content value handler.
   * Typical use case.
   */
  @Test
  public void testWithContentValueHandler() {
    Object h = new Object();
    
    ReferenceType referenceType = new ReferenceType();
    ReferenceType newReferenceType = referenceType.withContentValueHandler(h);
    
    assertEquals(h, newReferenceType._referencedType.<Object>getValueHandler());
    assertEquals(referenceType._class, newReferenceType._class);
    assertEquals(referenceType._referencedType.withValueHandler(h), newReferenceType._referencedType);
    assertEquals(referenceType._valueHandler, newReferenceType._valueHandler);
    assertEquals(referenceType._typeHandler, newReferenceType._typeHandler);
    assertEquals(referenceType._asStatic, newReferenceType._asStatic);
  }
  
  /**
   * Test case for the withStaticTyping method.
   * This method should return a new instance of ReferenceType with static typing enabled.
   * Typical use case.
   */
  @Test
  public void testWithStaticTyping() {
    ReferenceType referenceType = new ReferenceType();
    ReferenceType newReferenceType = referenceType.withStaticTyping();
    
    assertEquals(referenceType._class, newReferenceType._class);
    assertEquals(referenceType._referencedType.withStaticTyping(), newReferenceType._referencedType);
    assertEquals(referenceType._valueHandler, newReferenceType._valueHandler);
    assertEquals(referenceType._typeHandler, newReferenceType._typeHandler);
    assertEquals(true, newReferenceType._asStatic);
  }
  
  /**
   * Test case for the buildCanonicalName method.
   * This method should return the canonical name of the reference type.
   * Typical use case.
   */
  @Test
  public void testBuildCanonicalName() {
    ReferenceType referenceType = new ReferenceType();
    
    String canonicalName = referenceType.buildCanonicalName();
    
    assertEquals(referenceType._class.getName() + '<' + referenceType._referencedType.toCanonical() + '>', canonicalName);
  }
  
  /**
   * Test case for the _narrow method.
   * This method should return a new instance of ReferenceType with the specified subclass.
   * Typical use case.
   */
  @Test
  public void testNarrow() {
    Class<?> subclass = String.class;
    
    ReferenceType referenceType = new ReferenceType();
    ReferenceType newReferenceType = referenceType._narrow(subclass);
    
    assertEquals(subclass, newReferenceType._class);
    assertEquals(referenceType._referencedType, newReferenceType._referencedType);
    assertEquals(referenceType._valueHandler, newReferenceType._valueHandler);
    assertEquals(referenceType._typeHandler, newReferenceType._typeHandler);
    assertEquals(referenceType._asStatic, newReferenceType._asStatic);
  }
  
  /**
   * Test case for the getReferencedType method.
   * This method should return the referenced type of the ReferenceType instance.
   * Typical use case.
   */
  @Test
  public void testGetReferencedType() {
    ReferenceType referenceType = new ReferenceType();
    
    JavaType referencedType = referenceType.getReferencedType();
    
    assertEquals(referenceType._referencedType, referencedType);
  }
  
  /**
   * Test case for the isReferenceType method.
   * This method should return true for a ReferenceType instance.
   * Typical use case.
   */
  @Test
  public void testIsReferenceType() {
    ReferenceType referenceType = new ReferenceType();
    
    boolean isReferenceType = referenceType.isReferenceType();
    
    assertEquals(true, isReferenceType);
  }
  
  /**
   * Test case for the containedTypeCount method.
   * This method should return 1 for a ReferenceType instance.
   * Typical use case.
   */
  @Test
  public void testContainedTypeCount() {
    ReferenceType referenceType = new ReferenceType();
    
    int containedTypeCount = referenceType.containedTypeCount();
    
    assertEquals(1, containedTypeCount);
  }
  
  /**
   * Test case for the containedType method with index equals 0.
   * This method should return the referenced type of the ReferenceType instance.
   * Typical use case.
   */
  @Test
  public void testContainedTypeWithIndex0() {
    ReferenceType referenceType = new ReferenceType();
    
    JavaType containedType = referenceType.containedType(0);
    
    assertEquals(referenceType._referencedType, containedType);
  }
  
  /**
   * Test case for the containedType method with index not equals 0.
   * This method should return null for a ReferenceType instance.
   * Error scenario.
   */
  @Test
  public void testContainedTypeWithIndexNot0() {
    ReferenceType referenceType = new ReferenceType();
    
    JavaType containedType = referenceType.containedType(1);
    
    assertEquals(null, containedType);
  }
  
  /**
   * Test case for the containedTypeName method with index equals 0.
   * This method should return "T" for a ReferenceType instance.
   * Typical use case.
   */
  @Test
  public void testContainedTypeNameWithIndex0() {
    ReferenceType referenceType = new ReferenceType();
    
    String containedTypeName = referenceType.containedTypeName(0);
    
    assertEquals("T", containedTypeName);
  }
  
  /**
   * Test case for the containedTypeName method with index not equals 0.
   * This method should return null for a ReferenceType instance.
   * Error scenario.
   */
  @Test
  public void testContainedTypeNameWithIndexNot0() {
    ReferenceType referenceType = new ReferenceType();
    
    String containedTypeName = referenceType.containedTypeName(1);
    
    assertEquals(null, containedTypeName);
  }
  
  /**
   * Test case for the getParameterSource method.
   * This method should return the raw class of the ReferenceType instance.
   * Typical use case.
   */
  @Test
  public void testGetParameterSource() {
    ReferenceType referenceType = new ReferenceType();
    
    Class<?> parameterSource = referenceType.getParameterSource();
    
    assertEquals(referenceType._class, parameterSource);
  }
  
  /**
   * Test case for the getErasedSignature method.
   * This method should return the erased signature of the ReferenceType instance.
   * Typical use case.
   */
  @Test
  public void testGetErasedSignature() {
    ReferenceType referenceType = new ReferenceType();
    
    StringBuilder sb = new StringBuilder();
    StringBuilder erasedSignature = referenceType.getErasedSignature(sb);
    
    StringBuilder expectedErasedSignature = new StringBuilder();
    expectedErasedSignature.append(referenceType._classSignature(referenceType._class, sb, true));
    
    assertEquals(expectedErasedSignature, erasedSignature);
  }
  
  /**
   * Test case for the getGenericSignature method.
   * This method should return the generic signature of the ReferenceType instance.
   * Typical use case.
   */
  @Test
  public void testGetGenericSignature() {
    ReferenceType referenceType = new ReferenceType();
    
    StringBuilder sb = new StringBuilder();
    StringBuilder genericSignature = referenceType.getGenericSignature(sb);
    
    StringBuilder expectedGenericSignature = new StringBuilder();
    expectedGenericSignature.append(referenceType._classSignature(referenceType._class, sb, false));
    expectedGenericSignature.append('<');
    expectedGenericSignature.append(referenceType._referencedType.getGenericSignature(sb));
    expectedGenericSignature.append(';');
    
    assertEquals(expectedGenericSignature, genericSignature);
  }
  
  /**
   * Test case for the toString method.
   * This method should return a string representation of the ReferenceType instance.
   * Typical use case.
   */
  @Test
  public void testToString() {
    ReferenceType referenceType = new ReferenceType();
    
    String referenceTypeString = referenceType.toString();
    
    StringBuilder expectedReferenceTypeString = new StringBuilder(40)
        .append("[reference type, class ")
        .append(referenceType.buildCanonicalName())
        .append('<')
        .append(referenceType._referencedType)
        .append('>')
        .append(']');
        
    assertEquals(expectedReferenceTypeString, referenceTypeString);
  }
  
  /**
   * Test case for the equals method.
   * This method should return true if the given object is a ReferenceType instance and has the same class and referenced type as the current instance.
   * Typical use case.
   */
  @Test
  public void testEquals() {
    ReferenceType referenceType1 = new ReferenceType();
    ReferenceType referenceType2 = new ReferenceType();
    
    boolean equals = referenceType1.equals(referenceType2);
    
    assertEquals(true, equals);
  }
  
  /**
   * Test case for the equals method.
   * This method should return false if the given object is not a ReferenceType instance.
   * Error scenario.
   */
  @Test
  public void testEqualsWithNonReferenceType() {
    ReferenceType referenceType = new ReferenceType();
    Object obj = new Object();
    
    boolean equals = referenceType.equals(obj);
    
    assertEquals(false, equals);
  }
  
  /**
   * Test case for the equals method.
   * This method should return false if the given object is a ReferenceType instance but has a different class than the current instance.
   * Error scenario.
   */
  @Test
  public void testEqualsWithDifferentClass() {
    ReferenceType referenceType1 = new ReferenceType();
    ReferenceType referenceType2 = new ReferenceType();
    referenceType2._class = String.class;
    
    boolean equals = referenceType1.equals(referenceType2);
    
    assertEquals(false, equals);
  }
  
  /**
   * Test case for the equals method.
   * This method should return false if the given object is a ReferenceType instance with the same class as the current instance but has a different referenced type.
   * Error scenario.
   */
  @Test
  public void testEqualsWithDifferentReferencedType() {
    ReferenceType referenceType1 = new ReferenceType();
    ReferenceType referenceType2 = new ReferenceType();
    referenceType2._referencedType = new SimpleType();
    
    boolean equals = referenceType1.equals(referenceType2);
    
    assertEquals(false, equals);
  }

}