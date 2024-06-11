import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RecordTypeTest {
  private RecordType recordType;

  @Before
  public void setUp() {
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("a", new RecordTypeBuilder.RecordProperty("TYPE_1", null));
    properties.put("b", new RecordTypeBuilder.RecordProperty("TYPE_2", null));
    properties.put("c", new RecordTypeBuilder.RecordProperty("TYPE_3", null));
    recordType = new RecordType(properties);
  }

  @Test
  public void testDefineProperty() {
    boolean result = recordType.defineProperty("d", "TYPE_4", false, null);
    assertTrue(result);
  }

  @Test
  public void testDefineProperty_withInferred() {
    boolean result = recordType.defineProperty("e", "TYPE_5", true, null);
    assertFalse(result);
  }

  @Test
  public void testDefineProperty_whenFrozen() {
    recordType.setFrozen(true);
    boolean result = recordType.defineProperty("f", "TYPE_6", false, null);
    assertFalse(result);
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    RecordType otherType = new RecordType(new HashMap<>());
    JSType result = recordType.getGreatestSubtypeHelper(otherType);
    assertNotNull(result);
  }

  @Test
  public void testGetImplicitPrototype() {
    ObjectType result = recordType.getImplicitPrototype();
    assertNotNull(result);
  }

  @Test
  public void testIsEquivalentTo() {
    RecordType otherType = new RecordType(new HashMap<>());
    boolean result = recordType.isEquivalentTo(otherType);
    assertFalse(result);
  }

  @Test
  public void testIsSubtype_whenSubtype() {
    RecordType otherType = new RecordType(new HashMap<>());
    boolean result = recordType.isSubtype(otherType);
    assertTrue(result);
  }

  @Test
  public void testIsSubtype_whenNotSubtype() {
    ObjectType objectType = new ObjectType();
    boolean result = recordType.isSubtype(objectType);
    assertFalse(result);
  }

  @Test
  public void testResolveInternal() {
    StaticScope<JSType> scope = new StaticScope<>();
    JSType result = recordType.resolveInternal(null, scope);
    assertNotNull(result);
  }
}