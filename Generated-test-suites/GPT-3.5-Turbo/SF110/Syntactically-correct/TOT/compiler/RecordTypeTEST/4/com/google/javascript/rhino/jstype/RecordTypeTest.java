import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class RecordTypeTest {

  private RecordType recordType;

  @Before
  public void setUp() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("property1", new RecordProperty("property1", new JSType(), new Node()));
    properties.put("property2", new RecordProperty("property2", new JSType(), new Node()));

    recordType = new RecordType(null, properties);
  }

  @Test
  public void testIsSynthetic() {
    assertFalse(recordType.isSynthetic());
  }

  @Test
  public void testCheckRecordEquivalenceHelper_SameRecord_ReturnsTrue() {
    RecordType otherRecord = new RecordType(null, new HashMap(recordType.getProperties()));
    assertTrue(recordType.checkRecordEquivalenceHelper(otherRecord, null));
  }

  @Test
  public void testCheckRecordEquivalenceHelper_DifferentRecord_ReturnsFalse() {
    RecordType otherRecord = new RecordType(null, new HashMap<>());
    assertFalse(recordType.checkRecordEquivalenceHelper(otherRecord, null));
  }

  @Test
  public void testDefineProperty_IsFrozen_ReturnsFalse() {
    recordType.setFrozen(true);
    assertFalse(recordType.defineProperty("property3", new JSType(), true, new Node()));
  }

  @Test
  public void testGetGreatestSubtypeHelper_BothRecordTypes_ReturnsExpectedResult() {
    RecordType that = new RecordType(null, new HashMap<>(recordType.getProperties()));
    that.defineProperty("property3", new JSType(), true, new Node());

    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(that);
    // Assert expected properties in greatestSubtype
    assertTrue(greatestSubtype.hasProperty("property1"));
    assertTrue(greatestSubtype.hasProperty("property2"));
    assertTrue(greatestSubtype.hasProperty("property3"));
    // Assert all properties in recordType are also in greatestSubtype
    assertEquals(recordType.getPropertyType("property1"), greatestSubtype.getPropertyType("property1"));
    assertEquals(recordType.getPropertyType("property2"), greatestSubtype.getPropertyType("property2"));
  }

  @Test
  public void testGetGreatestSubtypeHelper_NotRecordTypes_ReturnsEmptyRecordType() {
    JSType that = new JSType();
    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(that);
    // Assert that greatestSubtype is an empty record type
    assertFalse(greatestSubtype.hasOwnProperty("property1"));
    assertFalse(greatestSubtype.hasOwnProperty("property2"));
  }

  @Test
  public void testIsSubtype_Subtype_ReturnsTrue() {
    RecordType subtype = new RecordType(null, new HashMap<>(recordType.getProperties()));
    assertTrue(recordType.isSubtype(subtype));
  }

  @Test
  public void testIsSubtype_SameType_ReturnsTrue() {
    assertTrue(recordType.isSubtype(recordType));
  }

  @Test
  public void testIsSubtype_NotSubtype_ReturnsFalse() {
    RecordType notSubtype = new RecordType(null, new HashMap<>());
    assertFalse(recordType.isSubtype(notSubtype));
  }

}