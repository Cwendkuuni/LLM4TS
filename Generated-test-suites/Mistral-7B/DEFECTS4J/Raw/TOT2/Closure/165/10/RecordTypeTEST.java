import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Test;

import static org.junit.Assert.*;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    RecordType recordType = new RecordType(registry, properties);

    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
        recordType.getImplicitPrototype());
    assertTrue(recordType.isFrozen());
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    RecordType recordType = new RecordType(registry, properties);

    JSType newPropertyType = registry.getIntType();
    String newPropertyName = "age";
    recordType.defineProperty(newPropertyName, newPropertyType, false, null);

    assertTrue(recordType.hasProperty(newPropertyName));
    assertEquals(newPropertyType, recordType.getPropertyType(newPropertyName));
  }

  @Test(expected = IllegalStateException.class)
  public void testDefinePropertyAfterFreeze() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    RecordType recordType = new RecordType(registry, properties);

    recordType.defineProperty("age", registry.getIntType(), false, null);
    recordType.freeze();

    recordType.defineProperty("newProperty", registry.getStringType(), false, null);
  }

  @Test
  public void testGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("name", new RecordProperty("name", registry.getStringType()));
    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("name", new RecordProperty("name", registry.getStringType()));
    propertiesB.put("age", new RecordProperty("age", registry.getIntType()));
    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    UnionTypeBuilder builder = new UnionTypeBuilder(registry);
    builder.addAlternate(recordTypeA);
    builder.addAlternate(recordTypeB);
    JSType greatestSubtype = builder.build();

    RecordType greatestRecordType = greatestSubtype.toMaybeRecordType();
    assertEquals(recordTypeA, greatestRecordType);
  }
}