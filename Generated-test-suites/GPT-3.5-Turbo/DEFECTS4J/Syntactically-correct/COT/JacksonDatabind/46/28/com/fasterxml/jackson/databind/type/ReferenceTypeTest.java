
import static org.junit.Assert.*;
import org.junit.Test;

public class ReferenceTypeTest {

  @Test
  public void testConstruct() {
    Class<?> cls = null; // TODO: Set class
    JavaType refType = null; // TODO: Set reference type
    Object valueHandler = null; // TODO: Set value handler
    Object typeHandler = null; // TODO: Set type handler
    
    ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
    
    // TODO: Add assertions
  }

  @Test
  public void testWithTypeHandler() {
    Object h = null; // TODO: Set type handler
    
    ReferenceType referenceType = new ReferenceType();
    ReferenceType result = referenceType.withTypeHandler(h);
    
    // TODO: Add assertions
  }

  @Test
  public void testWithContentTypeHandler() {
    Object h = null; // TODO: Set content type handler
    
    ReferenceType referenceType = new ReferenceType();
    ReferenceType result = referenceType.withContentTypeHandler(h);
    
    // TODO: Add assertions
  }

  @Test
  public void testWithValueHandler() {
    Object h = null; // TODO: Set value handler
    
    ReferenceType referenceType = new ReferenceType();
    ReferenceType result = referenceType.withValueHandler(h);
    
    // TODO: Add assertions
  }

  @Test
  public void testWithContentValueHandler() {
    Object h = null; // TODO: Set content value handler
    
    ReferenceType referenceType = new ReferenceType();
    ReferenceType result = referenceType.withContentValueHandler(h);
    
    // TODO: Add assertions
  }

  @Test
  public void testWithStaticTyping() {
    ReferenceType referenceType = new ReferenceType();
    ReferenceType result = referenceType.withStaticTyping();
    
    // TODO: Add assertions
  }

  @Test
  public void testNarrow() {
    Class<?> subclass = null; // TODO: Set subclass

    ReferenceType referenceType = new ReferenceType();
    JavaType result = referenceType._narrow(subclass);
    
    // TODO: Add assertions
  }

  @Test
  public void testGetReferencedType() {
    ReferenceType referenceType = new ReferenceType();
    
    JavaType result = referenceType.getReferencedType();
    
    // TODO: Add assertions
  }

  @Test
  public void testIsReferenceType() {
    ReferenceType referenceType = new ReferenceType();
    
    boolean result = referenceType.isReferenceType();
    
    // TODO: Add assertions
  }

  @Test
  public void testContainedTypeCount() {
    ReferenceType referenceType = new ReferenceType();
    
    int result = referenceType.containedTypeCount();
    
    // TODO: Add assertions
  }

  @Test
  public void testContainedType() {
    int index = 0; // TODO: Set index

    ReferenceType referenceType = new ReferenceType();
    JavaType result = referenceType.containedType(index);
    
    // TODO: Add assertions
  }

  @Test
  public void testContainedTypeName() {
    int index = 0; // TODO: Set index

    ReferenceType referenceType = new ReferenceType();
    String result = referenceType.containedTypeName(index);
    
    // TODO: Add assertions
  }

  @Test
  public void testGetParameterSource() {
    ReferenceType referenceType = new ReferenceType();
    
    Class<?> result = referenceType.getParameterSource();
    
    // TODO: Add assertions
  }
  
  @Test
  public void testGetErasedSignature() {
    ReferenceType referenceType = new ReferenceType();
    StringBuilder sb = new StringBuilder();
    StringBuilder result = referenceType.getErasedSignature(sb);
    
    // TODO: Add assertions
  }
  
  @Test
  public void testGetGenericSignature() {
    ReferenceType referenceType = new ReferenceType();
    StringBuilder sb = new StringBuilder();
    StringBuilder result = referenceType.getGenericSignature(sb);
    
    // TODO: Add assertions
  }

  @Test
  public void testToString() {
    ReferenceType referenceType = new ReferenceType();
    
    String result = referenceType.toString();
    
    // TODO: Add assertions
  }

  @Test
  public void testEquals() {
    Object o = null; // TODO: Set object

    ReferenceType referenceType = new ReferenceType();
    boolean result = referenceType.equals(o);
    
    // TODO: Add assertions
  }
}

