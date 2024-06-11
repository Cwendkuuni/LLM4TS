import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

   private RecordType recordType;

   @Before
   public void setUp() {
      recordType = new RecordType(null, null, null, false);
   }

   @Test
   public void testDefineDeclaredProperty() {
      recordType.defineProperty("property1", JSType.UNKNOWN, true, null);
      assertEquals(JSType.UNKNOWN, recordType.getPropertyType("property1"));
   }

   @Test
   public void testDefineSynthesizedProperty() {
      recordType.defineProperty("property2", JSType.OTHER, false, null);
      assertEquals(JSType.OTHER, recordType.getPropertyType("property2"));
   }

   @Test
   public void testIsSynthetic() {
      assertFalse(recordType.isSynthetic());
   }

   @Test
   public void testCheckRecordEquivalenceHelper() {
      RecordType otherRecord = new RecordType(null, null, null, false);
      assertFalse(recordType.checkRecordEquivalenceHelper(otherRecord, false));
   }

   @Test
   public void testGetImplicitPrototype() {
      ObjectType implicitPrototype = recordType.getImplicitPrototype();
      assertEquals(JSTypeNative.OBJECT_TYPE, implicitPrototype.getDisplayName());
   }

   @Test
   public void testIsSubtype() {
      assertFalse(recordType.isSubtype(null));
   }

   @Test
   public void testResolveInternal() {
      ErrorReporter errorReporter = new ErrorReporter();
      StaticScope<JSType> scope = new StaticScope<>();
      JSType resolvedType = recordType.resolveInternal(errorReporter, scope);
      assertEquals(recordType, resolvedType);
   }

}