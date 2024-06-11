import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {
  @Test
  public void testRecordTypeCreation() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("property1", new RecordProperty("property1", JSType.getUnknownType()));
    RecordType recordType = new RecordType(
        new JSTypeRegistry(null), properties, true);
    assertNotNull(recordType);
  }

  @Test
  public void testRecordTypeEquivalence() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("property1", new RecordProperty("property1", JSType.getBooleanType()));
    RecordType recordType1 = new RecordType(
        new JSTypeRegistry(null), properties1, true);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("property1", new RecordProperty("property1", JSType.getBooleanType()));
    properties2.put("property2", new RecordProperty("property2", JSType.getBooleanType()));
    RecordType recordType2 = new RecordType(
        new JSTypeRegistry(null), properties2, true);

    // Test equivalence with the same properties
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType1, true));
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, true));
    assertTrue(recordType2.checkRecordEquivalenceHelper(recordType1, true));

    // Test equivalence with different properties
    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, false));
    assertFalse(recordType2.checkRecordEquivalenceHelper(recordType1, false));
  }

  @Test(expected = IllegalStateException.class)
  public void testRecordTypeCreationWithNullRecordProperty() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("property1", null);
    new RecordType(new JSTypeRegistry(null), properties, true);
  }
}