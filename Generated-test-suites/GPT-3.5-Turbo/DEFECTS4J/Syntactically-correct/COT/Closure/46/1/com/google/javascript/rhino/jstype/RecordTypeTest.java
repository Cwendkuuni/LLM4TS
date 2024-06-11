import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;
import java.util.SortedMap;
import com.google.javascript.rhino.jstype.*;

public class RecordTypeTest {

  private RecordType recordType;

  @Before
  public void setUp() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties = new TreeMap<String, RecordTypeBuilder.RecordProperty>();
    properties.put("property1", new RecordTypeBuilder.RecordProperty(new JSType(), new Node()));
    properties.put("property2", new RecordTypeBuilder.RecordProperty(new JSType(), new Node()));
    recordType = new RecordType(registry, properties);
  }

  @Test
  public void testIsEquivalentTo() {
    assertTrue(recordType.isEquivalentTo(recordType));
    assertFalse(recordType.isEquivalentTo(new JSType()));
  }

  @Test
  public void testGetImplicitPrototype() {
    assertNotNull(recordType.getImplicitPrototype());
  }

  @Test
  public void testDefineProperty() {
    assertFalse(recordType.defineProperty("property3", new JSType(), false, new Node()));
  }

  @Test
  public void testGetLeastSupertype() {
    JSType otherType = new JSType();
    RecordTypeBuilder.RecordProperty prop1 =
        new RecordTypeBuilder.RecordProperty(new JSType(), new Node());
    RecordTypeBuilder.RecordProperty prop2 =
        new RecordTypeBuilder.RecordProperty(new JSType(), new Node());
    Map<String, RecordTypeBuilder.RecordProperty> otherProperties =
        new TreeMap<String, RecordTypeBuilder.RecordProperty>();
    otherProperties.put("property1", prop1);
    otherProperties.put("property2", prop2);
    RecordType otherRecordType = new RecordType(new JSTypeRegistry(), otherProperties);
    assertTrue(recordType.getLeastSupertype(otherType).isEmptyType());
    assertTrue(recordType.getLeastSupertype(otherRecordType).isEquivalentTo(recordType));
  }

  @Test
  public void testToMaybeRecordType() {
    assertEquals(recordType, recordType.toMaybeRecordType());
  }

  @Test
  public void testIsSubtype() {
    assertFalse(recordType.isSubtype(new JSType()));
    assertTrue(recordType.isSubtype(
        recordType.getImplicitPrototype()));
  }

  @Test
  public void testResolveInternal() {
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<JSType>();
    assertEquals(recordType, recordType.resolveInternal(errorReporter, scope));
  }

}