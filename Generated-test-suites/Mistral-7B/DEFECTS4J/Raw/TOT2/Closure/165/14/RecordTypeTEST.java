import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;

import static org.junit.Assert.*;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    // Given a record type builder and a map of properties
    RecordTypeBuilder builder = new RecordTypeBuilder(null);
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Integer.class));

    // When creating a RecordType instance
    RecordType recordType = new RecordType(null, properties);

    // Then the record type should have the expected properties
    assertEquals(2, recordType.properties.size());
    assertEquals(String.class, recordType.getPropertyType("name"));
    assertEquals(Integer.class, recordType.getPropertyType("age"));
  }

  @Test
  public void testRecordTypeEquivalence() {
    // Given two RecordType instances with the same properties
    RecordType recordType1 = new RecordType(null, Maps.newHashMap());
    RecordType recordType2 = new RecordType(null, Maps.newHashMap());

    // When defining properties for both instances
    recordType1.defineProperty("name", String.class, false, null);
    recordType1.defineProperty("age", Integer.class, false, null);
    recordType2.defineProperty("name", String.class, false, null);
    recordType2.defineProperty("age", Integer.class, false, null);

    // Then both instances should be equivalent
    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testRecordTypeSubtyping() {
    // Given two RecordType instances with some shared and some different properties
    RecordType recordType1 = new RecordType(null, Maps.newHashMap());
    RecordType recordType2 = new RecordType(null, Maps.newHashMap());

    recordType1.defineProperty("name", String.class, false, null);
    recordType1.defineProperty("age", Integer.class, false, null);
    recordType1.defineProperty("city", String.class, false, null);

    recordType2.defineProperty("name", String.class, false, null);
    recordType2.defineProperty("age", Integer.class, false, null);

    // When checking the subtyping relationship between them
    assertTrue(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testRecordTypeFrozen() {
    // Given a RecordType instance
    RecordType recordType = new RecordType(null, Maps.newHashMap());

    // When defining a property
    recordType.defineProperty("name", String.class, false, null);

    // Then the property should not be able to be redefined
    assertFalse(recordType.defineProperty("name", String.class, false, null));
  }

  @Test(expected = IllegalStateException.class)
  public void testRecordTypeInvalidProperty() {
    // Given a RecordType instance
    RecordType recordType = new RecordType(null, Maps.newHashMap());

    // When trying to define a property with a null RecordProperty
    recordType.defineProperty("name", null, false, null);
  }
}