import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeTest {

 @Test
 public void testIsSynthetic() {
   RecordType record = new RecordType(null, null, false);
   assertTrue(record.isSynthetic());
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
   RecordType record1 = new RecordType(null, null, false);
   RecordType record2 = new RecordType(null, null, false);
   assertTrue(record1.checkRecordEquivalenceHelper(record2, true));
 }

 @Test
 public void testGetImplicitPrototype() {
   RecordType record = new RecordType(null, null, false);
   assertNotNull(record.getImplicitPrototype());
 }

 @Test
 public void testDefineProperty() {
   RecordType record = new RecordType(null, null, true);
   assertTrue(record.defineProperty("property1", null, false, null));
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
   RecordType record1 = new RecordType(null, null, false);
   JSType type = record1.getGreatestSubtypeHelper(null);
   assertNull(type);
 }

 @Test
 public void testIsSubtype() {
   RecordType record1 = new RecordType(null, null, false);
   assertFalse(record1.isSubtype(null));
 }

 @Test
 public void testResolveInternal() {
   RecordType record = new RecordType(null, null, false);
   JSType type = record.resolveInternal(null, null);
   assertNotNull(type);
 }

}