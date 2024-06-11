import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

  @Test
  public void testRecordType() {
    RecordType recordType = new RecordType(registry, properties);

    // Test constructor
    assertTrue(recordType.isSynthetic() == false);
    assertTrue(recordType.isFrozen == true);

    // Test getImplicitPrototype()
    ObjectType implicitPrototype = recordType.getImplicitPrototype();
    assertTrue(implicitPrototype != null);

    // Test defineProperty()
    boolean definePropertyResult = recordType.defineProperty("propertyName", type, inferred, propertyNode);
    assertTrue(definePropertyResult == false);

    // Test getGreatestSubtypeHelper()
    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(that);
    assertTrue(greatestSubtype != null);

    // Test toMaybeRecordType()
    RecordType maybeRecordType = recordType.toMaybeRecordType();
    assertTrue(maybeRecordType != null);

    // Test isSubtype()
    boolean isSubtypeResult = recordType.isSubtype(that);
    assertTrue(isSubtypeResult == true);

  }
}