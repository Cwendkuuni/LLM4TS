import org.junit.*;
import java.util.concurrent.atomic.AtomicReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class AtomicReferenceDeserializerTest {
  
  @Test
  public void testWithResolved() {
    JavaType fullType = new JavaType();
    ValueInstantiator inst = new ValueInstantiator();
    TypeDeserializer typeDeser = new TypeDeserializer();
    JsonDeserializer<?> valueDeser = new JsonDeserializer<?>();
    
    AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, valueDeser);
    
    AtomicReferenceDeserializer result = atomicReferenceDeserializer.withResolved(typeDeser, valueDeser);
    
    Assert.assertEquals(fullType, result.fullType);
    Assert.assertEquals(inst, result._valueInstantiator);
    Assert.assertEquals(typeDeser, result._valueTypeDeserializer);
    Assert.assertEquals(valueDeser, result._valueDeserializer);
  }
  
  @Test
  public void testGetNullValue() {
    DeserializationContext ctxt = new DeserializationContext();
    
    AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer();
    
    AtomicReference<Object> result = atomicReferenceDeserializer.getNullValue(ctxt);
    
    Assert.assertNotNull(result);
  }
  
  @Test
  public void testGetEmptyValue() {
    DeserializationContext ctxt = new DeserializationContext();
    
    AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer();
    
    Object result = atomicReferenceDeserializer.getEmptyValue(ctxt);
    
    Assert.assertNotNull(result);
    Assert.assertTrue(result instanceof AtomicReference);
  }
  
  @Test
  public void testReferenceValue() {
    Object contents = new Object();
    
    AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer();
    
    AtomicReference<Object> result = atomicReferenceDeserializer.referenceValue(contents);
    
    Assert.assertNotNull(result);
    Assert.assertEquals(contents, result.get());
  }
  
  @Test
  public void testGetReferenced() {
    AtomicReference<Object> reference = new AtomicReference<Object>();
    
    AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer();
    
    Object result = atomicReferenceDeserializer.getReferenced(reference);
    
    Assert.assertNull(result);
    
    Object contents = new Object();
    reference.set(contents);
    
    result = atomicReferenceDeserializer.getReferenced(reference);
    
    Assert.assertEquals(contents, result);
  }
  
  @Test
  public void testUpdateReference() {
    AtomicReference<Object> reference = new AtomicReference<Object>();
    Object contents = new Object();
    
    AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer();
    
    AtomicReference<Object> result = atomicReferenceDeserializer.updateReference(reference, contents);
    
    Assert.assertEquals(reference, result);
    Assert.assertEquals(contents, reference.get());
  }
  
  @Test
  public void testSupportsUpdate() {
    DeserializationConfig config = new DeserializationConfig();
    
    AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer();
    
    Boolean result = atomicReferenceDeserializer.supportsUpdate(config);
    
    Assert.assertEquals(Boolean.TRUE, result);
  }
}