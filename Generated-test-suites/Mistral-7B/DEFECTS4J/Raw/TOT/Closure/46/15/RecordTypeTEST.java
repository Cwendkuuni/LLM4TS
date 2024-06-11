import static com.google.javascript.rhino.JSType.*;
import static org.junit.Assert.*;

import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {

  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("x", new RecordProperty("x", INT));
    properties.put("y", new RecordProperty("y", STRING));

    // Test creating a record type from a map of properties
    RecordType recordType = new RecordType(registry, properties);
    assertEquals("{ x : INT, y : STRING }", recordType.toString());

    // Test getting properties
    assertEquals(INT, recordType.getPropertyType("x"));
    assertEquals(STRING, recordType.getPropertyType("y"));

    // Test defining a property
    recordType = recordType.defineProperty("z", DOUBLE);
    assertEquals("{ x : INT, y : STRING, z : DOUBLE }", recordType.toString());

    // Test defining a property with a name that already exists
    try {
      recordType.defineProperty("x", INT);
      fail("Expected an IllegalStateException.");
    } catch (IllegalStateException e) {
      // Expected
    }

    // Test getting a non-existent property
    try {
      recordType.getPropertyType("z");
      fail("Expected a NoSuchPropertyError.");
    } catch (NoSuchPropertyError e) {
      // Expected
    }

    // Test isEquivalentTo method
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);
    assertTrue(recordType1.isEquivalentTo(recordType2));

    // Test isSubtype method
    RecordType recordType3 = new RecordType(registry, properties);
    RecordType recordType4 = new RecordType(registry, Maps.newHashMap());
    RecordType recordType5 = new RecordType(registry, properties);

    assertTrue(recordType3.isSubtype(recordType3));
    assertTrue(recordType3.isSubtype(recordType4)); // empty record is subtype of all records
    assertTrue(recordType4.isSubtype(recordType3));
    assertTrue(recordType3.isSubtype(recordType5));
    assertFalse(recordType4.isSubtype(recordType3)); // empty record is not subtype of non-empty record

    // Test getLeastSupertype method
    RecordType recordType6 = new RecordType(registry, Maps.newHashMap());
    RecordType recordType7 = new RecordType(registry, Maps.newHashMap("x", INT));
    RecordType recordType8 = new RecordType(registry, Maps.newHashMap("x", INT, "y", STRING));

    assertEquals(recordType6, recordType6.getLeastSupertype(recordType6));
    assertEquals(recordType6, recordType6.getLeastSupertype(recordType7));
    assertEquals(recordType7, recordType7.getLeastSupertype(recordType6));
    assertEquals(recordType8, recordType8.getLeastSupertype(recordType7));
    assertEquals(recordType7, recordType7.getLeastSupertype(recordType8));

    // Test getGreatestSubtype helper method
    RecordType recordType9 = new RecordType(registry, Maps.newHashMap());
    RecordType recordType10 = new RecordType(registry, Maps.newHashMap("x", INT));
    RecordType recordType11 = new RecordType(registry, Maps.newHashMap("x", INT, "y", STRING));

    assertEquals(recordType11, recordType11.getGreatestSubtypeHelper(recordType10));
    assertEquals(recordType10, recordType10.getGreatestSubtypeHelper(recordType9));
    assertEquals(recordType9, recordType9.getGreatestSubtypeHelper(recordType11));

    // Test toMaybeRecordType method
    assertEquals(recordType3, recordType3.toMaybeRecordType());
    assertEquals(recordType3, recordType1.toMaybeRecordType());
    assertEquals(recordType4, recordType4.toMaybeRecordType());

    // Test toString method
    assertEquals("{ x : INT, y : STRING, z : DOUBLE }", recordType.toString());

    // Test defineProperty method with frozen record
    RecordType frozenRecord = new RecordType(registry, properties);
    frozenRecord.freeze();
    try {
      frozenRecord.defineProperty("z", DOUBLE);
      fail("Expected an IllegalStateException.");
    } catch (IllegalStateException e) {
      // Expected
    }

    // Test isFrozen property
    assertTrue(frozenRecord.isFrozen());

    // Test isEquivalentTo method with frozen record
    RecordType frozenRecord2 = new RecordType(registry, properties);
    frozenRecord2.freeze();
    assertFalse(frozenRecord.isEquivalentTo(frozenRecord2));
  }

  @Test
  public void testRecordTypeWithUnionTypes() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("x", new RecordProperty("x", INT));
    properties.put("y", new RecordProperty("y", UNIONType.of(INT, STRING)));

    RecordType recordType = new RecordType(registry, properties);
    assertEquals("{ x : INT, y : INT | STRING }", recordType.toString());

    UnionTypeUnionCase intUnionCase = new UnionTypeUnionCase("INT_CASE", INT);
    UnionTypeUnionCase stringUnionCase = new UnionTypeUnionCase("STRING_CASE", STRING);
    UnionTypeBuilder unionBuilder = new UnionTypeBuilder(registry);
    unionBuilder.addAlternate(intUnionCase);
    unionBuilder.addAlternate(stringUnionCase);
    JSType unionType = unionBuilder.build();

    RecordProperty yProperty = recordType.getProperty("y");
    assertEquals(unionType, yProperty.getType());

    // Test assigning a value of the correct type to a property
    RecordType record = new RecordType(registry, properties);
    Node recordNode = registry.getFactory().createRecord(new Node(), record);
    recordNode.setProperty("x", registry.getFactory().createInt(1));
    recordNode.setProperty("y", registry.getFactory().createInt(2));
    assertEquals(INT, recordNode.getType().getPropertyType("x").getType());
    assertEquals(UNIONType.of(INT, STRING), recordNode.getType().getPropertyType("y").getType());
    assertEquals(2, recordNode.getProperty("y").getValue().toInt32());

    // Test assigning a value of a super type to a property
    recordNode.setProperty("y", registry.getFactory().createString("hello"));
    assertEquals(STRING, recordNode.getType().getPropertyType("y").getType());
    assertEquals("hello", recordNode.getProperty("y").getValue().getStringValue());
  }
}