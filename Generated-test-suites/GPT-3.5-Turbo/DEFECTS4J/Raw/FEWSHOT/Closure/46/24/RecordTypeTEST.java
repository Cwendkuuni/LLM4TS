import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class RecordTypeTest {
    @Test
    public void testIsEquivalentTo() {
      JSTypeRegistry registry = new JSTypeRegistry();
      RecordTypeBuilder builder = new RecordTypeBuilder(registry);
      RecordType recordType = builder.build();

      // TODO: write test cases for isEquivalentTo()

    }

    @Test
    public void testGetImplicitPrototype() {
      JSTypeRegistry registry = new JSTypeRegistry();
      RecordTypeBuilder builder = new RecordTypeBuilder(registry);
      RecordType recordType = builder.build();

      // TODO: write test cases for getImplicitPrototype()

    }

    @Test
    public void testDefineProperty() {
      JSTypeRegistry registry = new JSTypeRegistry();
      RecordProperty property = new RecordProperty();
      property.setType(registry.getNativeType(JSTypeNative.NUMBER_TYPE));

      RecordTypeBuilder builder = new RecordTypeBuilder(registry);
      RecordType recordType = builder.addProperty("age", registry.getNativeType(JSTypeNative.NUMBER_TYPE), null).build();

      // TODO: write test cases for defineProperty()

    }

    @Test
    public void testGetLeastSupertype() {
      JSTypeRegistry registry = new JSTypeRegistry();
      RecordTypeBuilder builder = new RecordTypeBuilder(registry);
      RecordType recordType = builder.build();

      // TODO: write test cases for getLeastSupertype()

    }

    @Test
    public void testGetGreatestSubtypeHelper() {
      JSTypeRegistry registry = new JSTypeRegistry();
      RecordTypeBuilder builder = new RecordTypeBuilder(registry);
      RecordType recordType = builder.build();

      // TODO: write test cases for getGreatestSubtypeHelper()

    }

    @Test
    public void testIsSubtype() {
      JSTypeRegistry registry = new JSTypeRegistry();
      RecordTypeBuilder builder = new RecordTypeBuilder(registry);
      RecordType recordType = builder.build();

      // TODO: write test cases for isSubtype()

    }

    @Test
    public void testResolveInternal() {
      JSTypeRegistry registry = new JSTypeRegistry();
      RecordTypeBuilder builder = new RecordTypeBuilder(registry);
      RecordType recordType = builder.build();

      // TODO: write test cases for resolveInternal()

    }
}