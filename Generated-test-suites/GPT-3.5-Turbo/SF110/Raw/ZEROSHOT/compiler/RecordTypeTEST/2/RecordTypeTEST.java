import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;
import java.util.HashMap;
import java.util.Map;

public class RecordTypeTest {

  private RecordType recordType;

  @Before
  public void setUp() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("property1", new RecordProperty("property1", JSTypeRegistry.UNKNOWN_TYPE, Node.NULL));
    properties.put("property2", new RecordProperty("property2", JSTypeRegistry.UNKNOWN_TYPE, Node.NULL));
    recordType = new RecordType(new JSTypeRegistry(), properties);
  }

  @Test
  public void testIsSynthetic() {
    assertFalse(recordType.isSynthetic());
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    RecordType otherRecord = new RecordType(new JSTypeRegistry(), new HashMap<>());
    assertTrue(recordType.checkRecordEquivalenceHelper(otherRecord, null));
  }

  @Test
  public void testGetImplicitPrototype() {
    assertNotNull(recordType.getImplicitPrototype());
  }

  @Test
  public void testDefineProperty() {
    assertFalse(recordType.defineProperty("property3", JSTypeRegistry.UNKNOWN_TYPE, true, null));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSType that = new JSType(new JSTypeRegistry());
    assertNull(recordType.getGreatestSubtypeHelper(that));
  }

  @Test
  public void testToMaybeRecordType() {
    assertSame(recordType, recordType.toMaybeRecordType());
  }

  @Test
  public void testIsSubtype() {
    JSType that = new JSType(new JSTypeRegistry());
    assertFalse(recordType.isSubtype(that));
  }

  @Test
  public void testIsSubtypeWithRecordType() {
    RecordType typeA = new RecordType(new JSTypeRegistry(), new HashMap<>());
    RecordType typeB = new RecordType(new JSTypeRegistry(), new HashMap<>());
    assertTrue(RecordType.isSubtype(typeA, typeB));
  }

}