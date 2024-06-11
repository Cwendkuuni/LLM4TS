import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReferenceProperty;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.IOException;
import java.lang.annotation.Annotation;

public class ObjectIdReferencePropertyTest {

  @Test
  public void testConstructor1() {
    SettableBeanProperty forward = new SettableBeanProperty();
    ObjectIdInfo objectIdInfo = new ObjectIdInfo();
    ObjectIdReferenceProperty obj = new ObjectIdReferenceProperty(forward, objectIdInfo);
    // Assertions
    // ...
  }

  @Test
  public void testConstructor2() {
    ObjectIdReferenceProperty src = new ObjectIdReferenceProperty();
    JsonDeserializer<?> deser = new JsonDeserializer<>();
    NullValueProvider nva = new NullValueProvider();
    ObjectIdReferenceProperty obj = new ObjectIdReferenceProperty(src, deser, nva);
    // Assertions
    // ...
  }

  @Test
  public void testConstructor3() {
    ObjectIdReferenceProperty src = new ObjectIdReferenceProperty();
    PropertyName newName = new PropertyName();
    ObjectIdReferenceProperty obj = new ObjectIdReferenceProperty(src, newName);
    // Assertions
    // ...
  }

  @Test
  public void testWithName() {
    PropertyName newName = new PropertyName();
    ObjectIdReferenceProperty src = new ObjectIdReferenceProperty();
    ObjectIdReferenceProperty obj = src.withName(newName);
    // Assertions
    // ...
  }

  @Test
  public void testWithValueDeserializer() {
    JsonDeserializer<?> deser = new JsonDeserializer<>();
    ObjectIdReferenceProperty src = new ObjectIdReferenceProperty();
    ObjectIdReferenceProperty obj = src.withValueDeserializer(deser);
    // Assertions
    // ...
  }

  @Test
  public void testWithNullProvider() {
    NullValueProvider nva = new NullValueProvider();
    ObjectIdReferenceProperty src = new ObjectIdReferenceProperty();
    ObjectIdReferenceProperty obj = src.withNullProvider(nva);
    // Assertions
    // ...
  }

  @Test
  public void testFixAccess() {
    DeserializationConfig config = new DeserializationConfig();
    ObjectIdReferenceProperty obj = new ObjectIdReferenceProperty();
    obj.fixAccess(config);
    // Assertions
    // ...
  }

  @Test
  public void testGetAnnotation() {
    Class<A> acls = A.class;
    ObjectIdReferenceProperty obj = new ObjectIdReferenceProperty();
    A result = obj.getAnnotation(acls);
    // Assertions
    // ...
  }

  @Test
  public void testGetMember() {
    ObjectIdReferenceProperty obj = new ObjectIdReferenceProperty();
    AnnotatedMember result = obj.getMember();
    // Assertions
    // ...
  }

  @Test
  public void testGetCreatorIndex() {
    ObjectIdReferenceProperty obj = new ObjectIdReferenceProperty();
    int result = obj.getCreatorIndex();
    // Assertions
    // ...
  }

  @Test
  public void testDeserializeAndSet() throws IOException {
    JsonParser p = new ObjectMapper().createParser("");
    DeserializationContext ctxt = new DeserializationContext();
    Object instance = new Object();
    ObjectIdReferenceProperty obj = new ObjectIdReferenceProperty();
    obj.deserializeAndSet(p, ctxt, instance);
    // Assertions
    // ...
  }

  @Test
  public void testDeserializeSetAndReturn() throws IOException {
    JsonParser p = new ObjectMapper().createParser("");
    DeserializationContext ctxt = new DeserializationContext();
    Object instance = new Object();
    ObjectIdReferenceProperty obj = new ObjectIdReferenceProperty();
    Object result = obj.deserializeSetAndReturn(p, ctxt, instance);
    // Assertions
    // ...
  }

  @Test
  public void testSet() throws IOException {
    Object instance = new Object();
    Object value = new Object();
    ObjectIdReferenceProperty obj = new ObjectIdReferenceProperty();
    obj.set(instance, value);
    // Assertions
    // ...
  }

  @Test
  public void testSetAndReturn() throws IOException {
    Object instance = new Object();
    Object value = new Object();
    ObjectIdReferenceProperty obj = new ObjectIdReferenceProperty();
    Object result = obj.setAndReturn(instance, value);
    // Assertions
    // ...
  }

  @Test
  public void testPropertyReferring() {
    ObjectIdReferenceProperty parent = new ObjectIdReferenceProperty();
    UnresolvedForwardReference ref = new UnresolvedForwardReference();
    Class<?> type = Object.class;
    Object ob = new Object();
    ObjectIdReferenceProperty.PropertyReferring obj = new ObjectIdReferenceProperty.PropertyReferring(parent, ref, type, ob);
    // Assertions
    // ...
  }

  @Test
  public void testHandleResolvedForwardReference() throws IOException {
    Object id = new Object();
    Object value = new Object();
    ObjectIdReferenceProperty parent = new ObjectIdReferenceProperty();
    UnresolvedForwardReference ref = new UnresolvedForwardReference();
    Class<?> type = Object.class;
    Object ob = new Object();
    ObjectIdReferenceProperty.PropertyReferring obj = new ObjectIdReferenceProperty.PropertyReferring(parent, ref, type, ob);
    obj.handleResolvedForwardReference(id, value);
    // Assertions
    // ...
  }

}