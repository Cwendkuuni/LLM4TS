import org.junit.Assert;
import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    RecordType record1 = builder.build();
    RecordType record2 = builder.build();
    Assert.assertTrue(record1.isEquivalentTo(record2));
  }

  @Test
  public void testGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType record = new RecordType(registry, null);
    ObjectType prototype = record.getImplicitPrototype();
    Assert.assertNotNull(prototype);
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType record = new RecordType(registry, null);
    boolean result = record.defineProperty("myProperty", registry.getNativeType(JSTypeNative.STRING_TYPE),
        false, null);
    Assert.assertTrue(result);
  }

  @Test
  public void testGetLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    RecordType record1 = builder.build();
    RecordType record2 = builder.build();
    JSType leastSupertype = record1.getLeastSupertype(record2);
    Assert.assertNotNull(leastSupertype);
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder1 = new RecordTypeBuilder(registry);
    RecordTypeBuilder builder2 = new RecordTypeBuilder(registry);
    RecordType record1 = builder1.build();
    RecordType record2 = builder2.build();
    boolean result = record1.isSubtype(record2);
    Assert.assertFalse(result);
  }

  @Test
  public void testResolveInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    RecordType record = builder.build();
    JSType resolvedType = record.resolveInternal(errorReporter, scope);
    Assert.assertNotNull(resolvedType);
  }
}