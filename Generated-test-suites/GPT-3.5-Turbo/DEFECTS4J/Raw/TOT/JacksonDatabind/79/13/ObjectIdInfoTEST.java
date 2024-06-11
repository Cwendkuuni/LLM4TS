import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdInfoTest {
  
  // Test cases for the constructor ObjectIdInfo(Propertyname, Class, Class, Class) 
  
  // Test typical use case
  @Test
  public void testObjectIdInfoTypical() {
    PropertyName propertyName = new PropertyName("name");
    Class<?> scope = Object.class;
    Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
    Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;
    
    ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
    
    assertEquals(propertyName, objectIdInfo.getPropertyName());
    assertEquals(scope, objectIdInfo.getScope());
    assertEquals(generator, objectIdInfo.getGeneratorType());
    assertEquals(resolver, objectIdInfo.getResolverType());
    assertFalse(objectIdInfo.getAlwaysAsId());
  }
  
  // Test edge case where propertyName is null
  @Test
  public void testObjectIdInfoNullPropertyName() {
    PropertyName propertyName = null;
    Class<?> scope = Object.class;
    Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
    Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;
    
    try {
      ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
      fail("Expected NullPointerException to be thrown");
    } catch (NullPointerException e) {
      // Pass
    }
  }
  
  // Test edge case where scope is null
  @Test
  public void testObjectIdInfoNullScope() {
    PropertyName propertyName = new PropertyName("name");
    Class<?> scope = null;
    Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
    Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;
    
    ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
    
    assertEquals(propertyName, objectIdInfo.getPropertyName());
    assertNull(objectIdInfo.getScope());
    assertEquals(generator, objectIdInfo.getGeneratorType());
    assertEquals(resolver, objectIdInfo.getResolverType());
    assertFalse(objectIdInfo.getAlwaysAsId());
  }
  
  // Test edge case where generator is null
  @Test
  public void testObjectIdInfoNullGenerator() {
    PropertyName propertyName = new PropertyName("name");
    Class<?> scope = Object.class;
    Class<? extends ObjectIdGenerator<?>> generator = null;
    Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;
    
    try {
      ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
      fail("Expected NullPointerException to be thrown");
    } catch (NullPointerException e) {
      // Pass
    }
  }
  
  // Test edge case where resolver is null
  @Test
  public void testObjectIdInfoNullResolver() {
    PropertyName propertyName = new PropertyName("name");
    Class<?> scope = Object.class;
    Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
    Class<? extends ObjectIdResolver> resolver = null;
    
    ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
    
    assertEquals(propertyName, objectIdInfo.getPropertyName());
    assertEquals(scope, objectIdInfo.getScope());
    assertEquals(generator, objectIdInfo.getGeneratorType());
    assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
    assertFalse(objectIdInfo.getAlwaysAsId());
  }
  
  // Test case for the constructor ObjectIdInfo(Propertyname, Class, Class, Class, boolean)
  
  // Test typical use case
  @Test
  public void testObjectIdInfoWithAlwaysAsIdTypical() {
    PropertyName propertyName = new PropertyName("name");
    Class<?> scope = Object.class;
    Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
    Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;
    boolean alwaysAsId = true;
    
    ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, alwaysAsId, resolver);
    
    assertEquals(propertyName, objectIdInfo.getPropertyName());
    assertEquals(scope, objectIdInfo.getScope());
    assertEquals(generator, objectIdInfo.getGeneratorType());
    assertEquals(resolver, objectIdInfo.getResolverType());
    assertTrue(objectIdInfo.getAlwaysAsId());
  }
  
  // Test case for the method withAlwaysAsId(boolean)
  
  // Test typical use case where state is changed to true
  @Test
  public void testWithAlwaysAsIdTypicalTrue() {
    PropertyName propertyName = new PropertyName("name");
    Class<?> scope = Object.class;
    Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
    Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;
    boolean alwaysAsId = false;
    ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, alwaysAsId, resolver);
    
    ObjectIdInfo updatedObjectIdInfo = objectIdInfo.withAlwaysAsId(true);
    
    assertEquals(propertyName, updatedObjectIdInfo.getPropertyName());
    assertEquals(scope, updatedObjectIdInfo.getScope());
    assertEquals(generator, updatedObjectIdInfo.getGeneratorType());
    assertEquals(resolver, updatedObjectIdInfo.getResolverType());
    assertTrue(updatedObjectIdInfo.getAlwaysAsId());
  }
  
  // Test typical use case where state is changed to false
  @Test
  public void testWithAlwaysAsIdTypicalFalse() {
    PropertyName propertyName = new PropertyName("name");
    Class<?> scope = Object.class;
    Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
    Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;
    boolean alwaysAsId = true;
    ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, alwaysAsId, resolver);
    
    ObjectIdInfo updatedObjectIdInfo = objectIdInfo.withAlwaysAsId(false);
    
    assertEquals(propertyName, updatedObjectIdInfo.getPropertyName());
    assertEquals(scope, updatedObjectIdInfo.getScope());
    assertEquals(generator, updatedObjectIdInfo.getGeneratorType());
    assertEquals(resolver, updatedObjectIdInfo.getResolverType());
    assertFalse(updatedObjectIdInfo.getAlwaysAsId());
  }
  
  // Test case for toString()
  
  @Test
  public void testToString() {
    PropertyName propertyName = new PropertyName("name");
    Class<?> scope = Object.class;
    Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
    Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;
    boolean alwaysAsId = true;
    ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, alwaysAsId, resolver);
    
    String expectedString = "ObjectIdInfo: propName=name, scope=java.lang.Object, generatorType=ObjectIdGenerator, alwaysAsId=true";
    assertEquals(expectedString, objectIdInfo.toString());
  }
}