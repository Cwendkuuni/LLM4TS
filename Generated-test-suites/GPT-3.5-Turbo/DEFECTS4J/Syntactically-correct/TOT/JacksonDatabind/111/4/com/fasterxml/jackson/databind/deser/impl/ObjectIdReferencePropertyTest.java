import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdReferencePropertyTest {
  
  @Test
  public void testConstructor() {
    ObjectIdInfo objectIdInfo = new ObjectIdInfo(PropertyName.construct("idProperty"), null, null);
    SettableBeanProperty forward = new SettableBeanProperty(PropertyName.construct("forwardProperty"), null, null);
    
    ObjectIdReferenceProperty objIdRefProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
    
    assertEquals(forward, objIdRefProperty._forward);
    assertEquals(objectIdInfo, objIdRefProperty._objectIdInfo);
  }
  
  @Test
  public void testDeserConstructor() {
    ObjectIdInfo objectIdInfo = new ObjectIdInfo(PropertyName.construct("idProperty"), null, null);
    SettableBeanProperty forward = new SettableBeanProperty(PropertyName.construct("forwardProperty"), null, null);
    JsonDeserializer<?> deser = null;
    NullValueProvider nva = null;

    ObjectIdReferenceProperty objIdRefProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
    ObjectIdReferenceProperty objIdRefProperty2 = new ObjectIdReferenceProperty(objIdRefProperty, deser, nva);
    
    assertEquals(forward, objIdRefProperty2._forward);
    assertEquals(objectIdInfo, objIdRefProperty2._objectIdInfo);
  }
  
  @Test
  public void testNamedConstructor() {
    ObjectIdInfo objectIdInfo = new ObjectIdInfo(PropertyName.construct("idProperty"), null, null);
    SettableBeanProperty forward = new SettableBeanProperty(PropertyName.construct("forwardProperty"), null, null);
    PropertyName newName = new PropertyName("newName");

    ObjectIdReferenceProperty objIdRefProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
    ObjectIdReferenceProperty objIdRefProperty2 = new ObjectIdReferenceProperty(objIdRefProperty, newName);
    
    assertEquals(forward, objIdRefProperty2._forward);
    assertEquals(objectIdInfo, objIdRefProperty2._objectIdInfo);
    assertEquals(newName, objIdRefProperty2.getFullName());
  }
  
  @Test
  public void testWithName() {
    ObjectIdInfo objectIdInfo = new ObjectIdInfo(PropertyName.construct("idProperty"), null, null);
    SettableBeanProperty forward = new SettableBeanProperty(PropertyName.construct("forwardProperty"), null, null);
    PropertyName newName = new PropertyName("newName");

    ObjectIdReferenceProperty objIdRefProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
    ObjectIdReferenceProperty objIdRefProperty2 = objIdRefProperty.withName(newName);
    
    assertEquals(forward, objIdRefProperty2._forward);
    assertEquals(objectIdInfo, objIdRefProperty2._objectIdInfo);
    assertEquals(newName, objIdRefProperty2.getFullName());
  }
  
  @Test
  public void testWithValueDeserializer() {
    ObjectIdInfo objectIdInfo = new ObjectIdInfo(PropertyName.construct("idProperty"), null, null);
    SettableBeanProperty forward = new SettableBeanProperty(PropertyName.construct("forwardProperty"), null, null);
    JsonDeserializer<?> deser = null;

    ObjectIdReferenceProperty objIdRefProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
    ObjectIdReferenceProperty objIdRefProperty2 = objIdRefProperty.withValueDeserializer(deser);
    
    assertEquals(forward, objIdRefProperty2._forward);
    assertEquals(objectIdInfo, objIdRefProperty2._objectIdInfo);
    assertNull(objIdRefProperty2.getValueDeserializer());
  }
  
  @Test
  public void testWithNullProvider() {
    ObjectIdInfo objectIdInfo = new ObjectIdInfo(PropertyName.construct("idProperty"), null, null);
    SettableBeanProperty forward = new SettableBeanProperty(PropertyName.construct("forwardProperty"), null, null);
    NullValueProvider nva = null;

    ObjectIdReferenceProperty objIdRefProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
    ObjectIdReferenceProperty objIdRefProperty2 = objIdRefProperty.withNullProvider(nva);
    
    assertEquals(forward, objIdRefProperty2._forward);
    assertEquals(objectIdInfo, objIdRefProperty._objectIdInfo);
    assertEquals(nva, objIdRefProperty2.getNullValueProvider());
  }
  
  @Test
  public void testFixAccess() {
    ObjectIdInfo objectIdInfo = new ObjectIdInfo(PropertyName.construct("idProperty"), null, null);
    SettableBeanProperty forward = new SettableBeanProperty(PropertyName.construct("forwardProperty"), null, null);
    DeserializationConfig config = null;

    ObjectIdReferenceProperty objIdRefProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
    objIdRefProperty.fixAccess(config);
    
    // No assertions as method is void
  }
  
  @Test
  public void testGetAnnotation() {
    ObjectIdInfo objectIdInfo = new ObjectIdInfo(PropertyName.construct("idProperty"), null, null);
    SettableBeanProperty forward = new SettableBeanProperty(PropertyName.construct("forwardProperty"), null, null);
    Class<A> acls = null;

    ObjectIdReferenceProperty objIdRefProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
    A annotation = objIdRefProperty.getAnnotation(acls);
    
    assertNull(annotation);
  }
  
  @Test
  public void testGetMember() {
    ObjectIdInfo objectIdInfo = new ObjectIdInfo(PropertyName.construct("idProperty"), null, null);
    SettableBeanProperty forward = new SettableBeanProperty(PropertyName.construct("forwardProperty"), null, null);

    ObjectIdReferenceProperty objIdRefProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
    AnnotatedMember member = objIdRefProperty.getMember();
    
    assertEquals(forward.getMember(), member);
  }
  
  @Test
  public void testGetCreatorIndex() {
    ObjectIdInfo objectIdInfo = new ObjectIdInfo(PropertyName.construct("idProperty"), null, null);
    SettableBeanProperty forward = new SettableBeanProperty(PropertyName.construct("forwardProperty"), null, null);

    ObjectIdReferenceProperty objIdRefProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
    int creatorIndex = objIdRefProperty.getCreatorIndex();
    
    assertEquals(forward.getCreatorIndex(), creatorIndex);
  }
  
  @Test
  public void testDeserializeAndSet() {
    ObjectIdInfo objectIdInfo = new ObjectIdInfo(PropertyName.construct("idProperty"), null, null);
    SettableBeanProperty forward = new SettableBeanProperty(PropertyName.construct("forwardProperty"), null, null);
    JsonParser p = null;
    DeserializationContext ctxt = null;
    Object instance = null;

    ObjectIdReferenceProperty objIdRefProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
    
    try {
      objIdRefProperty.deserializeAndSet(p, ctxt, instance);
    } catch (IOException e) {
      // No assertions as method throws IOException
    }
  }
  
  @Test
  public void testDeserializeSetAndReturn() {
    ObjectIdInfo objectIdInfo = new ObjectIdInfo(PropertyName.construct("idProperty"), null, null);
    SettableBeanProperty forward = new SettableBeanProperty(PropertyName.construct("forwardProperty"), null, null);
    JsonParser p = null;
    DeserializationContext ctxt = null;
    Object instance = null;

    ObjectIdReferenceProperty objIdRefProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
    
    try {
      objIdRefProperty.deserializeSetAndReturn(p, ctxt, instance);
    } catch (IOException e) {
      // No assertions as method throws IOException
    }
  }
  
  @Test
  public void testSet() {
    ObjectIdInfo objectIdInfo = new ObjectIdInfo(PropertyName.construct("idProperty"), null, null);
    SettableBeanProperty forward = new SettableBeanProperty(PropertyName.construct("forwardProperty"), null, null);
    Object instance = null;
    Object value = null;

    ObjectIdReferenceProperty objIdRefProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
    
    try {
      objIdRefProperty.set(instance, value);
    } catch (IOException e) {
      // No assertions as method throws IOException
    }
  }
  
  @Test
  public void testSetAndReturn() {
    ObjectIdInfo objectIdInfo = new ObjectIdInfo(PropertyName.construct("idProperty"), null, null);
    SettableBeanProperty forward = new SettableBeanProperty(PropertyName.construct("forwardProperty"), null, null);
    Object instance = null;
    Object value = null;

    ObjectIdReferenceProperty objIdRefProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
    
    try {
      objIdRefProperty.setAndReturn(instance, value);
    } catch (IOException e) {
      // No assertions as method throws IOException
    }
  }
  
  @Test
  public void testPropertyReferring() {
    ObjectIdInfo objectIdInfo = new ObjectIdInfo(PropertyName.construct("idProperty"), null, null);
    SettableBeanProperty forward = new SettableBeanProperty(PropertyName.construct("forwardProperty"), null, null);
    UnresolvedForwardReference ref = null;
    Class<?> type = null;
    Object ob = null;

    ObjectIdReferenceProperty objIdRefProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
    ObjectIdReferenceProperty.PropertyReferring propRef = objIdRefProperty.new PropertyReferring(objIdRefProperty, ref, type, ob);
    
    assertEquals(objIdRefProperty, propRef._parent);
    assertEquals(ob, propRef._pojo);
  }
  
  @Test
  public void testHandleResolvedForwardReference() {
    ObjectIdInfo objectIdInfo = new ObjectIdInfo(PropertyName.construct("idProperty"), null, null);
    SettableBeanProperty forward = new SettableBeanProperty(PropertyName.construct("forwardProperty"), null, null);
    UnresolvedForwardReference ref = null;
    Class<?> type = null;
    Object ob = null;
    Object id = null;
    Object value = null;

    ObjectIdReferenceProperty objIdRefProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
    ObjectIdReferenceProperty.PropertyReferring propRef = objIdRefProperty.new PropertyReferring(objIdRefProperty, ref, type, ob);
    
    try {
      propRef.handleResolvedForwardReference(id, value);
    } catch (IOException e) {
      // No assertions as method throws IOException
    }
  }
}