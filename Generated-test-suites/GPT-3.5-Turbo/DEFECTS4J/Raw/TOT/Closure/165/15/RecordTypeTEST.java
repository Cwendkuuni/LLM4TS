import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

  @Test
  public void defineProperty_validInput_success() {
    RecordType recordType = new RecordType();
    recordType.defineProperty("property1", JSType.STRING_TYPE, false, null);

    assertTrue(recordType.hasProperty("property1"));
    assertEquals(JSType.STRING_TYPE, recordType.getPropertyType("property1"));
  }

  @Test
  public void defineProperty_nullPropertyNode_throwException() {
    RecordType recordType = new RecordType();

    try {
      recordType.defineProperty("property1", JSType.STRING_TYPE, false, null);
      fail("Expected IllegalStateException to be thrown");
    } catch (IllegalStateException e) {
      assertEquals("RecordProperty associated with a property should not be null!", e.getMessage());
    }
  }

  @Test
  public void defineProperty_frozenRecordType_returnFalse() {
    RecordType recordType = new RecordType();
    recordType.setFrozen(true);

    assertFalse(recordType.defineProperty("property1", JSType.STRING_TYPE, false, null));
  }

  @Test
  public void isEquivalentTo_sameProperties_returnTrue() {
    RecordType recordType1 = new RecordType();
    recordType1.defineProperty("property1", JSType.STRING_TYPE, false, null);
    recordType1.defineProperty("property2", JSType.NUMBER_TYPE, false, null);

    RecordType recordType2 = new RecordType();
    recordType2.defineProperty("property1", JSType.STRING_TYPE, false, null);
    recordType2.defineProperty("property2", JSType.NUMBER_TYPE, false, null);

    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void isEquivalentTo_differentProperties_returnFalse() {
    RecordType recordType1 = new RecordType();
    recordType1.defineProperty("property1", JSType.STRING_TYPE, false, null);

    RecordType recordType2 = new RecordType();
    recordType2.defineProperty("property2", JSType.STRING_TYPE, false, null);

    assertFalse(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void getImplicitPrototype_returnNativeObjectType() {
    RecordType recordType = new RecordType();
    assertSame(JSTypeNative.OBJECT_TYPE, recordType.getImplicitPrototype());
  }

  @Test
  public void getGreatestSubtypeHelper_bothRecordTypes_returnNewRecordType() {
    RecordType recordType1 = new RecordType();
    recordType1.defineProperty("property1", JSType.STRING_TYPE, false, null);

    RecordType recordType2 = new RecordType();
    recordType2.defineProperty("property1", JSType.STRING_TYPE, false, null);
    recordType2.defineProperty("property2", JSType.NUMBER_TYPE, false, null);

    RecordType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertTrue(greatestSubtype.isRecordType());
    assertTrue(greatestSubtype.hasProperty("property1"));
    assertTrue(greatestSubtype.hasProperty("property2"));
    assertEquals(JSType.STRING_TYPE, greatestSubtype.getPropertyType("property1"));
    assertEquals(JSType.NUMBER_TYPE, greatestSubtype.getPropertyType("property2"));
  }

  @Test
  public void isSubtype_subTypeRecordType_returnTrue() {
    RecordType recordType1 = new RecordType();
    recordType1.defineProperty("property1", JSType.STRING_TYPE, false, null);

    RecordType recordType2 = new RecordType();
    recordType2.defineProperty("property1", JSType.STRING_TYPE, false, null);
    recordType2.defineProperty("property2", JSType.NUMBER_TYPE, false, null);

    assertTrue(recordType1.isSubtype(recordType2));
  }

  @Test
  public void resolveInternal_resolveProperties_returnResolvedType() {
    RecordType recordType = new RecordType();
    recordType.defineProperty("property1", JSType.STRING_TYPE, false, null);
    recordType.defineProperty("property2", JSType.NUMBER_TYPE, false, null);

    JSType resolvedType = recordType.resolveInternal(t, scope);

    assertTrue(resolvedType.isRecordType());
    assertTrue(resolvedType.hasProperty("property1"));
    assertTrue(resolvedType.hasProperty("property2"));
    assertEquals(JSType.STRING_TYPE, resolvedType.getPropertyType("property1"));
    assertEquals(JSType.NUMBER_TYPE, resolvedType.getPropertyType("property2"));
  }
}