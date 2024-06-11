import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

  @Test
  public void testIsEquivalentTo() {
    RecordType record1 = new RecordType();
    record1.defineDeclaredProperty("a", new JSType());
    record1.defineDeclaredProperty("b", new JSType());

    RecordType record2 = new RecordType();
    record2.defineDeclaredProperty("a", new JSType());
    record2.defineDeclaredProperty("b", new JSType());

    assertTrue(record1.isEquivalentTo(record2));
  }

  @Test
  public void testGetLeastSupertype() {
    RecordType record1 = new RecordType();
    record1.defineDeclaredProperty("a", new JSType());
    record1.defineDeclaredProperty("b", new JSType());

    RecordType record2 = new RecordType();
    record2.defineDeclaredProperty("a", new JSType());
    record2.defineDeclaredProperty("b", new JSType());
    record2.defineDeclaredProperty("c", new JSType());

    RecordType leastSupertype = record1.getLeastSupertype(record2);

    assertEquals(3, leastSupertype.getPropertyCount());
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    RecordType record1 = new RecordType();
    record1.defineDeclaredProperty("a", new JSType());

    RecordType record2 = new RecordType();
    record2.defineDeclaredProperty("a", new JSType());
    record2.defineDeclaredProperty("b", new JSType());

    JSType greatestSubtype = record1.getGreatestSubtypeHelper(record2);

    assertFalse(greatestSubtype.isEmptyType());
    assertEquals(2, greatestSubtype.getPropertyCount());
  }

  @Test
  public void testIsSubtype() {
    RecordType record1 = new RecordType();
    record1.defineDeclaredProperty("a", new JSType());

    RecordType record2 = new RecordType();
    record2.defineDeclaredProperty("a", new JSType());
    record2.defineDeclaredProperty("b", new JSType());

    assertTrue(record2.isSubtype(record1));
  }

  @Test
  public void testResolveInternal() {
    RecordType record = new RecordType();
    record.defineDeclaredProperty("a", new JSType());
    record.defineDeclaredProperty("b", new JSType());

    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>();

    JSType resolvedType = record.resolveInternal(errorReporter, scope);

    assertEquals(2, resolvedType.getPropertyCount());
  }
  
}