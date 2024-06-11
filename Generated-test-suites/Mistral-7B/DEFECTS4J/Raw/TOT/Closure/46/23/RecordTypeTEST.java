import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.testing.TypeEqualityTester;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RecordTypeTest {

  @Test
  public void testRecordTypeSubtyping() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordType recordTypeA = new RecordType(registry, Maps.newHashMap()
        .put("a", new IntType(registry))
        .put("b", new StringType(registry)));

    RecordType recordTypeB = new RecordType(registry, Maps.newHashMap()
        .put("a", new IntType(registry))
        .put("b", new StringType(registry))
        .put("c", new IntType(registry)));

    RecordType recordTypeC = new RecordType(registry, Maps.newHashMap()
        .put("a", new IntType(registry))
        .put("b", new StringType(registry))
        .put("c", new ObjectType(registry)));

    TypeEqualityTester.testReflexiveAndSymmetric(registry, recordTypeA, recordTypeA);
    TypeEqualityTester.testTransitive(registry, recordTypeA, recordTypeB, recordTypeC);
    TypeEqualityTester.testTransitive(registry, recordTypeB, recordTypeA, recordTypeC);
    TypeEqualityTester.testTransitive(registry, recordTypeC, recordTypeA, recordTypeB);

    // Test assignment
    Object objA = recordTypeA.getInstance(registry, new Object());
    Object objB = recordTypeB.getInstance(registry, new Object());
    Object objC = recordTypeC.getInstance(registry, new Object());

    assertEquals("Assigning a record of type A to a variable of type B should succeed.", objB, objA);
    assertEquals("Assigning a record of type B to a variable of type C should succeed.", objC, objB);
    try {
      objC = (Object) objA; // assignment from record type to Object should not fail
      fail("Assigning a record of type A to a variable of type Object should fail.");
    } catch (ClassCastException e) {
      // expected
    }

    try {
      objC = (Object) objB; // assignment from record type to Object should not fail
      fail("Assigning a record of type B to a variable of type Object should fail.");
    } catch (ClassCastException e) {
      // expected
    }

    try {
      objC = (RecordType) objA; // assignment from record type to record type should not fail
      fail("Assigning a record of type A to a variable of type RecordType should fail.");
    } catch (ClassCastException e) {
      // expected
    }

    try {
      objC = (RecordType) objB; // assignment from record type to record type should not fail
      fail("Assigning a record of type B to a variable of type RecordType should fail.");
    } catch (ClassCastException e) {
      // expected
    }

    // Test assignment from a record to a subtype
    RecordType subTypeRecord = new RecordType(registry, Maps.newHashMap()
        .put("a", new IntType(registry))
        .put("b", new IntType(registry))
        .put("c", new IntType(registry))
        .put("d", new IntType(registry)));

    Object subTypeObj = subTypeRecord.getInstance(registry, new Object());

    assertEquals("Assigning a record of type A to a variable of type B (supertype) should succeed.", objB, subTypeObj);
  }

  @Test
  public void testRecordTypeIsSubtypeOfObject() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordType recordType = new RecordType(registry, Maps.newHashMap());
    ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    TypeEqualityTester.testTransitive(registry, recordType, objectType, objectType);
    TypeEqualityTester.testTransitive(registry, objectType, recordType, recordType);
  }

  @Test
  public void testRecordTypeHasProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordType recordType = new RecordType(registry, Maps.newHashMap()
        .put("a", new IntType(registry))
        .put("b", new StringType(registry)));

    assertEquals("Record type has property 'a'.", true, recordType.hasProperty("a"));
    assertEquals("Record type has property 'b'.", true, recordType.hasProperty("b"));
    assertEquals("Record type does not have property 'c'.", false, recordType.hasProperty("c"));
  }

  @Test
  public void testRecordTypeGetPropertyType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordType recordType = new RecordType(registry, Maps.newHashMap()
        .put("a", new IntType(registry))
        .put("b", new StringType(registry)));

    assertEquals("Record type 'a' property type is IntType.", new IntType(registry), recordType.getPropertyType("a"));
    assertEquals("Record type 'b' property type is StringType.", new StringType(registry), recordType.getPropertyType("b"));
    try {
      recordType.getPropertyType("c");
      fail("Record type does not have property 'c'.");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test
  public void testRecordTypeGetPropertyTypeUnknownType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordType recordType = new RecordType(registry, Maps.newHashMap()
        .put("a", new UnknownType(registry)));

    assertEquals("Record type 'a' property type is UnknownType.", new UnknownType(registry), recordType.getPropertyType("a"));
  }

  @Test
  public void testRecordTypeGetPropertyNode() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordType recordType = new RecordType(registry, Maps.newHashMap()
        .put("a", new IntType(registry))
        .put("b", new StringType(registry)));

    Node propertyNodeA = recordType.getPropertyNode("a");
    Node propertyNodeB = recordType.getPropertyNode("b");

    assertEquals("Record type 'a' property node should exist.", propertyNodeA, null);
    assertEquals("Record type 'b' property node should exist.", propertyNodeB, null);
    try {
      recordType.getPropertyNode("c");
      fail("Record type does not have property 'c'.");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test
  public void testRecordTypeIsFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordType recordType = new RecordType(registry, Maps.newHashMap()
        .put("a", new IntType(registry))
        .put("b", new StringType(registry)));

    assertEquals("Record type is not frozen.", false, recordType.isFrozen());

    recordType = new RecordType(registry, Maps.newHashMap()
        .put("a", new IntType(registry))
        .put("b", new StringType(registry)));
    recordType.freeze();

    assertEquals("Record type is frozen.", true, recordType.isFrozen());
  }
}