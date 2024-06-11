import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", INT));
    properties.put("b", new RecordProperty("b", STRING));

    RecordType expectedRecordType = new RecordType(registry, properties, true);
    assertEquals(expectedRecordType.getClass(), RecordType.class);
    assertEquals(expectedRecordType.getName(), "RecordType{a: number, b: string}");

    RecordType actualRecordType = new RecordType(registry, properties, true);
    assertEquals(expectedRecordType, actualRecordType);
  }

  @Test
  public void testRecordTypeEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", INT));
    propertiesA.put("b", new RecordProperty("b", STRING));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("b", new RecordProperty("b", STRING));
    propertiesB.put("a", new RecordProperty("a", INT));

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, true));
    assertTrue(recordTypeB.checkRecordEquivalenceHelper(recordTypeA, true));

    propertiesB.remove("a");
    assertFalse(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, true));
    assertFalse(recordTypeB.checkRecordEquivalenceHelper(recordTypeA, true));
  }

  @Test
  public void testRecordTypeSubtyping() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", INT));
    propertiesA.put("b", new RecordProperty("b", STRING));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("b", new RecordProperty("b", STRING));
    propertiesB.put("a", new RecordProperty("a", INT));

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    JSType subType = recordTypeB.getGreatestSubtypeHelper(recordTypeA);
    assertEquals(recordTypeA.getClass(), subType.getClass());
    assertEquals(recordTypeA, subType);

    Map<String, RecordProperty> propertiesC = Maps.newHashMap();
    propertiesC.put("a", new RecordProperty("a", INT));
    propertiesC.put("c", new RecordProperty("c", INT));

    RecordType recordTypeC = new RecordType(registry, propertiesC, true);
    assertEquals(INT.getClass(), recordTypeA.getGreatestSubtypeHelper(recordTypeC).getClass());
  }

  @Test(expected = IllegalStateException.class)
  public void testRecordTypeInvalidProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", INT));
    properties.put("b", null);

    new RecordType(registry, properties, true);
  }

  @Test
  public void testRecordTypeToString() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", INT));
    properties.put("b", new RecordProperty("b", STRING));

    RecordType recordType = new RecordType(registry, properties, true);
    assertEquals("RecordType{a: number, b: string}", recordType.toString());
  }
}