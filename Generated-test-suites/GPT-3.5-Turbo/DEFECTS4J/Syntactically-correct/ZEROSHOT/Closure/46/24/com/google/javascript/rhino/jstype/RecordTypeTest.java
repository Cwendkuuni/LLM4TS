import static org.junit.Assert.assertTrue;
import java.util.Map;
import java.util.SortedMap;
import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties = new Map<String, RecordTypeBuilder.RecordProperty>();
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);
    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties = new Map<String, RecordTypeBuilder.RecordProperty>();
    RecordType recordType = new RecordType(registry, properties);
    assertTrue(recordType.getImplicitPrototype() instanceof ObjectType);
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties = new Map<String, RecordTypeBuilder.RecordProperty>();
    RecordType recordType = new RecordType(registry, properties);
    assertTrue(recordType.defineProperty("propertyName", new JSType(), false, new Node()));
  }

  @Test
  public void testGetLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties = new Map<String, RecordTypeBuilder.RecordProperty>();
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);
    assertTrue(recordType1.getLeastSupertype(recordType2) instanceof JSType);
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties = new Map<String, RecordTypeBuilder.RecordProperty>();
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);
    assertTrue(recordType1.getGreatestSubtypeHelper(recordType2) instanceof JSType);
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties = new Map<String, RecordTypeBuilder.RecordProperty>();
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);
    assertTrue(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testResolveInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties = new Map<String, RecordTypeBuilder.RecordProperty>();
    RecordType recordType = new RecordType(registry, properties);
    assertTrue(recordType.resolveInternal(new ErrorReporter(), new StaticScope<JSType>()) instanceof JSType);
  }
}