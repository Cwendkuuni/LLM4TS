import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import com.google.javascript.rhino.jstype.*;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;
import java.util.SortedMap;

import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Number.class));

    RecordType recordType = new RecordType(registry, properties);

    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
        recordType.getImplicitPrototype());
    assertEquals("RecordType", recordType.getName());
    assertEquals(2, recordType.getPropertyCount());

    String nameProperty = "name";
    assertNotNull(recordType.getPropertyType(nameProperty));
    assertEquals(String.class, recordType.getPropertyType(nameProperty));

    String ageProperty = "age";
    assertNotNull(recordType.getPropertyType(ageProperty));
    assertEquals(Number.class, recordType.getPropertyType(ageProperty));

    assertEquals(RecordType.class, recordType.getClass());
  }

  @Test
  public void testRecordTypeEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesA.put("name", new RecordProperty("name", String.class));
    propertiesA.put("age", new RecordProperty("age", Number.class));
    propertiesB.put("name", new RecordProperty("name", String.class));
    propertiesB.put("age", new RecordProperty("age", Number.class));
    propertiesB.put("gender", new RecordProperty("gender", String.class));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertFalse(recordTypeA.isEquivalentTo(recordTypeB));

    RecordType recordTypeC = new RecordType(registry, propertiesA);
    assertTrue(recordTypeA.isEquivalentTo(recordTypeC));
  }

  @Test
  public void testRecordTypeSubtyping() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesA.put("name", new RecordProperty("name", String.class));
    propertiesA.put("age", new RecordProperty("age", Number.class));
    propertiesB.put("name", new RecordProperty("name", String.class));
    propertiesB.put("age", new RecordProperty("age", Number.class));
    propertiesB.put("gender", new RecordProperty("gender", String.class));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertFalse(recordTypeB.isSubtype(recordTypeA));

    RecordType recordTypeC = new RecordType(registry, propertiesA);
    assertTrue(recordTypeA.isSubtype(recordTypeC));

    RecordType recordTypeD = new RecordType(registry, propertiesB);
    assertTrue(recordTypeC.isSubtype(recordTypeD));
  }

  @Test
  public void testRecordTypeLeastCommonSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    Map<String, RecordProperty> propertiesC = Maps.newHashMap();
    propertiesA.put("name", new RecordProperty("name", String.class));
    propertiesA.put("age", new RecordProperty("age", Number.class));
    propertiesB.put("name", new RecordProperty("name", String.class));
    propertiesB.put("age", new RecordProperty("age", Number.class));
    propertiesB.put("gender", new RecordProperty("gender", String.class));
    propertiesC.put("name", new RecordProperty("name", String.class));
    propertiesC.put("age", new RecordProperty("age", Number.class));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);
    RecordType recordTypeC = new RecordType(registry, propertiesC);

    RecordType leastCommonSupertype = recordTypeA.getLeastSupertype(recordTypeB);
    assertEquals(recordTypeC, leastCommonSupertype);
  }

  @Test
  public void testRecordTypeGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesA.put("name", new RecordProperty("name", String.class));
    propertiesA.put("age", new RecordProperty("age", Number.class));
    propertiesB.put("name", new RecordProperty("name", String.class));
    propertiesB.put("age", new RecordProperty("age", Number.class));
    propertiesB.put("gender", new RecordProperty("gender", String.class));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    JSType greatestSubtype = recordTypeB.getGreatestSubtypeHelper(recordTypeA);
    assertNotNull(grearestSubtype);
    assertTrue(grearestSubtype.isEmptyType());

    propertiesA.put("gender", new RecordProperty("gender", String.class));
    RecordType recordTypeC = new RecordType(registry, propertiesA);

    greatestSubtype = recordTypeB.getGreatestSubtypeHelper(recordTypeC);
    assertEquals(recordTypeB, greatestSubtype);
  }
}