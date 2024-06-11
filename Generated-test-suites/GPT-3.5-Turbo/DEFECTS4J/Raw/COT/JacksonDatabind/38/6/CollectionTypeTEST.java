import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionType;

public class CollectionTypeTest {
  
  @Test
  public void testConstruct() {
    Class<?> rawType = Class.class;
    TypeBindings bindings = null;
    JavaType superClass = null;
    JavaType[] superInts = null;
    JavaType elemT = null;
    
    CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
    
    assertEquals(rawType, collectionType.getRawClass());
    assertNull(collectionType.getTypeHandler());
    assertNull(collectionType.getValueHandler());
    assertFalse(collectionType.isContainerType());
  }
  
  @Test
  public void testWithContentType() {
    JavaType contentType = new JavaType(Object.class);
    
    CollectionType collectionType = new CollectionType();
    CollectionType modifiedType = collectionType.withContentType(contentType);
    
    assertEquals(contentType, modifiedType.getContentType());
  }
  
  @Test
  public void testWithTypeHandler() {
    Object handler = new Object();
    
    CollectionType collectionType = new CollectionType();  
    CollectionType modifiedType = collectionType.withTypeHandler(handler);
    
    assertEquals(handler, modifiedType.getTypeHandler());
  }
  
  @Test
  public void testWithContentTypeHandler() {
    Object handler = new Object();
    
    CollectionType collectionType = new CollectionType();
    CollectionType modifiedType = collectionType.withContentTypeHandler(handler);
    
    assertEquals(handler, modifiedType.getContentType().getTypeHandler());
  }
  
  @Test
  public void testWithValueHandler() {
    Object handler = new Object();
    
    CollectionType collectionType = new CollectionType();
    CollectionType modifiedType = collectionType.withValueHandler(handler);
    
    assertEquals(handler, modifiedType.getValueHandler());
  }
  
  @Test
  public void testWithContentValueHandler() {
    Object handler = new Object();
    
    CollectionType collectionType = new CollectionType();
    CollectionType modifiedType = collectionType.withContentValueHandler(handler);
    
    assertEquals(handler, modifiedType.getContentType().getValueHandler());
  }
  
  @Test
  public void testWithStaticTyping() {
    CollectionType collectionType = new CollectionType();
    CollectionType modifiedType = collectionType.withStaticTyping();
    
    assertTrue(modifiedType.isStaticTyping());
  }
  
  @Test
  public void testRefine() {
    Class<?> rawType = Class.class;
    TypeBindings bindings = null;
    JavaType superClass = null;
    JavaType[] superInterfaces = null;
    JavaType elemT = null;
    
    CollectionType collectionType = new CollectionType();
    CollectionType refinedType = collectionType.refine(rawType, bindings, superClass, superInterfaces);
    
    assertEquals(rawType, refinedType.getRawClass());
    assertEquals(bindings, refinedType.getBindings());
    assertEquals(superClass, refinedType.getSuperClass());
    assertEquals(superInterfaces, refinedType.getInterfaces());
    assertEquals(elemT, refinedType.getElementType());
  }
  
  @Test
  public void testToString() {
    Class<?> rawClass = Class.class;
    JavaType elementType = new JavaType(Object.class);
    
    CollectionType collectionType = new CollectionType(rawClass, null, null, null, elementType, null, null, false);
    String expectedString = "[collection type; class "+rawClass.getName()+", contains "+elementType+"]";
    
    assertEquals(expectedString, collectionType.toString());
  }
}