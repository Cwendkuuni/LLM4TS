import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;

public class RecordTypeTest {
  
  private RecordType recordType;

  @Before
  public void setUp() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("a", new RecordProperty(new JSType(), new Node()));
    properties.put("b", new RecordProperty(new JSType(), new Node()));
    recordType = new RecordType(new JSTypeRegistry(), properties);
  }

  @Test
  public void testIsSynthetic() {
    assertFalse(recordType.isSynthetic());
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    RecordType otherRecord = new RecordType(new JSTypeRegistry(), new HashMap<>());
    assertTrue(recordType.checkRecordEquivalenceHelper(otherRecord, true));
  }

  @Test
  public void testGetPropertyType() {
    assertEquals(new JSType(), recordType.getPropertyType("a"));
    assertEquals(new JSType(), recordType.getPropertyType("b"));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSType that = new JSType();
    assertEquals(recordType.registry.getNativeObjectType(JSTypeNative.NO_TYPE), recordType.getGreatestSubtypeHelper(that));
  }

  @Test
  public void testIsSubtype() {
    JSType that = new JSType();
    assertFalse(recordType.isSubtype(that));
  }

  @Test
  public void testResolveInternal() {
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>();
    assertEquals(new JSType(), recordType.resolveInternal(t, scope));
  }

}