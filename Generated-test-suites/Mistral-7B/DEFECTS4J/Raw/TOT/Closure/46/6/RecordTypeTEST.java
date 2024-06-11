import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

  @Test
  public void testSimpleRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = new TreeMap<>();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties);

    // Test the record type properties.
    assertEquals("name", recordType.getPropertyNames().get(0));
    assertEquals("string", recordType.getPropertyType("name").getTypeName());
    assertEquals("number", recordType.getPropertyType("age").getTypeName());
    assertEquals(2, recordType.getPropertyNames().size());

    // Test type equivalence.
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);
    assertTrue(recordType1.isEquivalentTo(recordType2));

    // Test subtyping.
    RecordType subTypeRecord = new RecordType(registry, properties);
    RecordType superTypeRecord = new RecordType(registry, new TreeMap<String, RecordProperty>() {{
      putAll(properties);
      put("color", new RecordProperty("color", registry.getStringType()));
    }});
    assertTrue(subTypeRecord.isSubtype(superTypeRecord));

    // Test isEmptyType.
    RecordType emptyRecordType = new RecordType(registry, new TreeMap<String, RecordProperty>());
    assertTrue(emptyRecordType.isEmptyType());

    // Test assignment.
    Object record1 = new RecordTypeBuilder(registry)
        .addProperty("name", registry.getStringType())
        .addProperty("age", registry.getNumberType())
        .build();
    Object record2 = new RecordTypeBuilder(registry)
        .addProperty("name", registry.getStringType())
        .addProperty("age", registry.getNumberType())
        .addProperty("color", registry.getStringType())
        .build();

    assertFalse(record1 instanceof RecordType);
    assertFalse(record2 instanceof RecordType);

    assertNull(record1.getClass());
    assertNotNull(record2.getClass());

    record1 = recordType1;
    record2 = superTypeRecord;

    // Test assignment to subtype.
    assertFalse(record1 instanceof RecordType);
    assertFalse(record2 instanceof RecordType);

    record1 = recordType1;
    record2 = subTypeRecord;

    // Test assignment to supertype.
    assertTrue(record1 instanceof RecordType);
    assertTrue(record2 instanceof RecordType);

    record1 = record1.resolve(null, null);
    record2 = record2.resolve(null, null);

    // Test assignment to self.
    record1 = record1;
    record2 = record2;

    // Test assignment to native Object.
    record1 = new Object();
    record2 = recordType1;

    assertFalse(record1 instanceof RecordType);
    assertFalse(record2 instanceof RecordType);

    // Test assignment from native Object.
    record1 = recordType1;
    record2 = record1;

    assertTrue(record1 instanceof RecordType);
    assertTrue(record2 instanceof RecordType);

    // Test assignment to empty record.
    record1 = recordType1;
    record2 = emptyRecordType;

    assertTrue(record1 instanceof RecordType);
    assertFalse(record2 instanceof RecordType);

    // Test assignment from empty record.
    record1 = emptyRecordType;
    record2 = recordType1;

    assertFalse(record1 instanceof RecordType);
    assertTrue(record2 instanceof RecordType);

    // Test least supertype.
    RecordType leastSup1 = recordType1.getLeastSupertype(superTypeRecord);
    RecordType leastSup2 = superTypeRecord.getLeastSupertype(recordType1);

    assertEquals(superTypeRecord, leastSup1);
    assertEquals(superTypeRecord, leastSup2);

    // Test greatest subtype.
    RecordType greatestSub1 = recordType1.getGreatestSubtype(recordType2);
    RecordType greatestSub2 = recordType2.getGreatestSubtype(recordType1);

    assertEquals(recordType1, greatestSub1);
    assertEquals(recordType1, greatestSub2);

    // Test getImplicitPrototype.
    assertEquals(JSTypeNative.OBJECT_TYPE, recordType1.getImplicitPrototype());
  }
}