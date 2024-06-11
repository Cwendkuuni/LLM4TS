import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordTypeBuilder.RecordProperty;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.JSTypeNative;

public class RecordTypeTest {

  private RecordTypeBuilder.RecordProperty property;
  private Map<String, RecordTypeBuilder.RecordProperty> properties;
  private JSTypeRegistry registry;
  private RecordType recordType;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    properties = new HashMap<>();
    property = new RecordTypeBuilder.RecordProperty(null, null, null);
    properties.put("property1", property);
    recordType = new RecordType(registry, properties);
  }

  @Test
  public void testIsEquivalentTo() {
    assertFalse(recordType.isEquivalentTo(null));
    assertFalse(recordType.isEquivalentTo(registry.getNativeType(JSTypeNative.NUMBER_TYPE)));
  }

  @Test
  public void testGetImplicitPrototype() {
    assertEquals(recordType.getImplicitPrototype(), registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
  }

  @Test
  public void testDefineProperty() {
    assertFalse(recordType.defineProperty(null, null, false, null));
  }

  @Test
  public void testGetLeastSupertype() {
    assertEquals(recordType.getLeastSupertype(null), registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE));
  }

  @Test
  public void testToMaybeRecordType() {
    assertEquals(recordType.toMaybeRecordType(), recordType);
  }

  @Test
  public void testIsSubtype() {
    assertFalse(recordType.isSubtype(null));
    assertFalse(recordType.isSubtype(registry.getNativeType(JSTypeNative.OBJECT_TYPE)));
  }

  @Test
  public void testResolveInternal() {
    ErrorReporter t = null;
    StaticScope<JSType> scope = null;
    assertEquals(recordType.resolveInternal(t, scope), recordType);
  }

}