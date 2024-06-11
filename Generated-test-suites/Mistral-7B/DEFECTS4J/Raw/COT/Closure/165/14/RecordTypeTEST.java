import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType = new RecordType(new JSTypeRegistry(), properties);
    assertEquals("RecordType{name=class com.google.javascript.rhino.jstype.String, age=class java.lang.Integer}",
        recordType.toString());
  }

  @Test
  public void testIsEquivalentTo() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("name", new RecordProperty("name", String.class));
    properties1.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("name", new RecordProperty("name", String.class));
    properties2.put("age", new RecordProperty("age", Integer.class));
    properties2.put("gender", new RecordProperty("gender", String.class));

    RecordType recordType1 = new RecordType(new JSTypeRegistry(), properties1);
    RecordType recordType2 = new RecordType(new JSTypeRegistry(), properties2);

    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsEquivalentToDifferentTypes() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("name", new RecordProperty("name", String.class));
    properties1.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("name", new RecordProperty("name", String.class));
    properties2.put("age", new RecordProperty("age", Float.class));

    RecordType recordType1 = new RecordType(new JSTypeRegistry(), properties1);
    RecordType recordType2 = new RecordType(new JSTypeRegistry(), properties2);

    assertFalse(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testGetImplicitPrototype() {
    RecordType recordType = new RecordType(new JSTypeRegistry(), Maps.newHashMap());
    assertEquals(JSType.OBJECT_TYPE, recordType.getImplicitPrototype());
  }

  @Test
  public void testDefinePropertyFrozen() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType = new RecordType(new JSTypeRegistry(), properties);

    assertFalse(recordType.defineProperty("name", String.class, false, null));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSType recordType1 = new RecordType(new JSTypeRegistry(), Maps.newHashMap()) {
      @Override
      RecordType toMaybeRecordType() {
        return this;
      }
    };

    JSType recordType2 = new RecordType(new JSTypeRegistry(), Maps.newHashMap()) {
      @Override
      RecordType toMaybeRecordType() {
        return this;
      }
    };

    JSType recordType3 = new RecordType(new JSTypeRegistry(), Maps.newHashMap()) {
      @Override
      RecordType toMaybeRecordType() {
        return this;
      }
    }.setGreatestSubtype(recordType1);

    JSType recordType4 = new RecordType(new JSTypeRegistry(), Maps.newHashMap()) {
      @Override
      RecordType toMaybeRecordType() {
        return this;
      }
    }.setGreatestSubtype(recordType2);

    JSType recordType5 = new RecordType(new JSTypeRegistry(), Maps.newHashMap()) {
      @Override
      RecordType toMaybeRecordType() {
        return this;
      }
    }.setGreatestSubtype(recordType3);

    JSType result = recordType5.getGreatestSubtypeHelper(recordType4);
    assertEquals(recordType5, result);
  }

  @Test
  public void testGetGreatestSubtypeHelperDifferentTypes() {
    JSType recordType1 = new RecordType(new JSTypeRegistry(), Maps.newHashMap()) {
      @Override
      RecordType toMaybeRecordType() {
        return this;
      }
    };

    JSType recordType2 = new RecordType(new JSTypeRegistry(), Maps.newHashMap()) {
      @Override
      RecordType toMaybeRecordType() {
        return this;
      }
    };

    JSType recordType3 = new RecordType(new JSTypeRegistry(), Maps.newHashMap()) {
      @Override
      RecordType toMaybeRecordType() {
        return this;
      }
    };

    JSType recordType4 = new UnionTypeBuilder(new JSTypeRegistry())
        .addAlternate(recordType1)
        .addAlternate(recordType2)
        .build();

    JSType result = recordType3.getGreatestSubtypeHelper(recordType4);
    assertNull(result);
  }

  @Test
  public void testIsSubtype() {
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("name", new RecordProperty("name", String.class));
    propertiesA.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("name", new RecordProperty("name", String.class));
    propertiesB.put("age", new RecordProperty("age", Integer.class));
    propertiesB.put("gender", new RecordProperty("gender", String.class));

    RecordType recordTypeA = new RecordType(new JSTypeRegistry(), propertiesA);
    RecordType recordTypeB = new RecordType(new JSTypeRegistry(), propertiesB);

    assertTrue(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testIsSubtypeDifferentTypes() {
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("name", new RecordProperty("name", String.class));
    propertiesA.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("name", new RecordProperty("name", String.class));
    propertiesB.put("age", new RecordProperty("age", Float.class));

    RecordType recordTypeA = new RecordType(new JSTypeRegistry(), propertiesA);
    RecordType recordTypeB = new RecordType(new JSTypeRegistry(), propertiesB);

    assertFalse(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testIsSubtypeRecordTypeToObject() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", String.class));

    RecordType recordType = new RecordType(new JSTypeRegistry(), properties);
    assertTrue(recordType.isSubtype(JSTypeRegistry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
  }

  @Test
  public void testIsSubtypeObjectToRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", String.class));

    RecordType recordType = new RecordType(new JSTypeRegistry(), properties);
    assertFalse(JSTypeRegistry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).isSubtype(recordType));
  }

  @Test
  public void testToMaybeRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", String.class));

    RecordType recordType = new RecordType(new JSTypeRegistry(), properties);
    assertEquals(recordType, recordType.toMaybeRecordType());
  }

  @Test
  public void testResolveInternal() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType = new RecordType(new JSTypeRegistry(), properties) {
      @Override
      RecordType toMaybeRecordType() {
        return this;
      }
    };

    JSType type = new JSTypeRegistry();
    type.put("String", String.class);
    type.put("Integer", Integer.class);

    RecordType spyRecordType = spy(recordType);
    when(spyRecordType).thenReturn(recordType);
    when(spyRecordType.getProperties()).thenReturn(properties);

    RecordType result = (RecordType) recordType.resolveInternal(null, new StaticScope<>(type));
    assertSame(recordType, result);
  }
}