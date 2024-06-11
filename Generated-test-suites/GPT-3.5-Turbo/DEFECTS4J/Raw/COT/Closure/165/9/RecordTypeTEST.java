import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testDefineProperty() {
    RecordType recordType = new RecordType();
    recordType.defineProperty("property1", new JSType("Type1"), false, new Node());
    assertEquals(new JSType("Type1"), recordType.getPropertyType("property1"));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    RecordType recordType1 = new RecordType();
    recordType1.defineProperty("property1", new JSType("Type1"), false, new Node());
    recordType1.defineProperty("property2", new JSType("Type2"), false, new Node());

    RecordType recordType2 = new RecordType();
    recordType2.defineProperty("property1", new JSType("Type1"), false, new Node());
    recordType2.defineProperty("property3", new JSType("Type3"), false, new Node());

    JSType expected = new UnionType(new JSType("Type1"));
    assertEquals(expected, recordType1.getGreatestSubtypeHelper(recordType2));
  }

  @Test
  public void testIsEquivalentTo() {
    RecordType recordType1 = new RecordType();
    recordType1.defineProperty("property1", new JSType("Type1"), false, new Node());
    recordType1.defineProperty("property2", new JSType("Type2"), false, new Node());

    RecordType recordType2 = new RecordType();
    recordType2.defineProperty("property1", new JSType("Type1"), false, new Node());
    recordType2.defineProperty("property2", new JSType("Type2"), false, new Node());

    assertEquals(true, recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsSubtype() {
    RecordType recordType1 = new RecordType();
    recordType1.defineProperty("property1", new JSType("Type1"), false, new Node());
    recordType1.defineProperty("property2", new JSType("Type2"), false, new Node());

    RecordType recordType2 = new RecordType();
    recordType2.defineProperty("property1", new JSType("Type1"), false, new Node());

    assertEquals(true, recordType2.isSubtype(recordType1));
  }

  @Test
  public void testResolveInternal() {
    RecordType recordType = new RecordType();
    recordType.defineProperty("property1", new JSType("Type1"), false, new Node());

    JSTypeRegistry registry = new JSTypeRegistry();
    recordType.resolveInternal(registry.getErrorReporter(), new StaticScope<JSType>());
    assertEquals(new JSType("Type1"), recordType.getPropertyType("property1"));
  }
}