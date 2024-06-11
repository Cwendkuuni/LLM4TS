import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;

public class AtomicReferenceDeserializerTest {

  private AtomicReferenceDeserializer atomicReferenceDeserializer;
  private DeserializationContext deserializationContext;

  @Before
  public void setUp() {
    atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
    deserializationContext = new DeserializationContext(null, null, null);
  }

  @Test
  public void testGetNullValue() throws JsonMappingException {
    AtomicReference<Object> atomicReference = atomicReferenceDeserializer.getNullValue(deserializationContext);
    assertNull(atomicReference.get());
  }

  @Test
  public void testGetEmptyValue() {
    AtomicReference<Object> atomicReference = atomicReferenceDeserializer.getEmptyValue(deserializationContext);
    assertNull(atomicReference.get());
  }

  @Test
  public void testReferenceValue() {
    String contents = "test";
    AtomicReference<Object> atomicReference = atomicReferenceDeserializer.referenceValue(contents);
    assertEquals(contents, atomicReference.get());
  }

  @Test
  public void testGetReferenced() {
    String contents = "test";
    AtomicReference<Object> atomicReference = new AtomicReference<>(contents);
    assertEquals(contents, atomicReferenceDeserializer.getReferenced(atomicReference));
  }

  @Test
  public void testUpdateReference() {
    String contents = "test";
    AtomicReference<Object> atomicReference = new AtomicReference<>();
    atomicReferenceDeserializer.updateReference(atomicReference, contents);
    assertEquals(contents, atomicReference.get());
  }

  @Test
  public void testSupportsUpdate() {
    DeserializationConfig config = new DeserializationConfig();
    assertTrue(atomicReferenceDeserializer.supportsUpdate(config));
  }
}