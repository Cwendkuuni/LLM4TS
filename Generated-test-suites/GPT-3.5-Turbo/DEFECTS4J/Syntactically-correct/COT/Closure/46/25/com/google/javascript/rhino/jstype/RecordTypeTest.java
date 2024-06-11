import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

   @Test
   public void testIsEquivalentTo() {
      RecordType record1 = new RecordType(registry, properties1);
      RecordType record2 = new RecordType(registry, properties2);
      
      assertTrue(record1.isEquivalentTo(record1));
      assertFalse(record1.isEquivalentTo(record2));
   }
   
   @Test
   public void testDefineProperty() {
      RecordType record = new RecordType(registry, properties);
      boolean result = record.defineProperty("property", new JSType(), false, null);
      
      assertFalse(result);
   }
   
   @Test
   public void testGetLeastSupertype() {
      RecordType record1 = new RecordType(registry, properties1);
      RecordType record2 = new RecordType(registry, properties2);
      
      JSType leastSupertype = record1.getLeastSupertype(record2);
      
      assertEquals(leastSupertype, record1);
   }
   
   @Test
   public void testGetGreatestSubtypeHelper() {
      RecordType record1 = new RecordType(registry, properties1);
      RecordType record2 = new RecordType(registry, properties2);
      
      JSType greatestSubtype = record1.getGreatestSubtypeHelper(record2);
      
      assertEquals(greatestSubtype, record1);
   }
   
   @Test
   public void testIsSubtype() {
      RecordType record1 = new RecordType(registry, properties1);
      RecordType record2 = new RecordType(registry, properties2);
      
      assertTrue(record1.isSubtype(record1));
      assertFalse(record1.isSubtype(record2));
   }
   
   @Test
   public void testResolveInternal() {
      RecordType record = new RecordType(registry, properties);
      ErrorReporter t = new ErrorReporter();
      StaticScope<JSType> scope = new StaticScope<JSType>();
      
      JSType resolvedType = record.resolveInternal(t, scope);
      
      assertEquals(resolvedType, record);
   }

}