import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

   @Test
   public void testIsEquivalentTo() {
      // Typical use case
      // Create two record types with the same properties
      // and check if they are equivalent
      RecordType rt1 = createRecordTypeWithProperties();
      RecordType rt2 = createRecordTypeWithProperties();
      assertTrue(rt1.isEquivalentTo(rt2));
      
      // Edge case
      // Create two record types with different properties
      // and check if they are not equivalent
      RecordType rt3 = createRecordTypeWithProperties();
      RecordType rt4 = createRecordTypeWithDifferentProperties();
      assertFalse(rt3.isEquivalentTo(rt4));
   }

   @Test
   public void testGetLeastSupertype() {
      // Typical use case
      // Create two record types where one is a subtype of the other
      // and check if the least supertype is correct
      RecordType rt1 = createRecordTypeWithMoreProperties();
      RecordType rt2 = createRecordTypeWithProperties();
      assertEquals(rt2, rt1.getLeastSupertype(rt2));
   }

   @Test
   public void testIsSubtype() {
      // Typical use case
      // Create two record types where one is a subtype of the other
      // and check if the subtype check is correct
      RecordType rt1 = createRecordTypeWithProperties();
      RecordType rt2 = createRecordTypeWithMoreProperties();
      assertTrue(rt1.isSubtype(rt2));
      
      // Error scenario
      // Create a record type and a different type
      // and check if the subtype check is false
      RecordType rt3 = createRecordTypeWithProperties();
      assertFalse(rt3.isSubtype(JSTypeNative.NUMBER_TYPE));
   }
   
   // Helper methods to create RecordType instances for testing
   
   private RecordType createRecordTypeWithProperties() {
      Map<String, RecordProperty> properties = new HashMap<>();
      properties.put("a", new RecordProperty(JSTypeNative.STRING_TYPE, null));
      properties.put("b", new RecordProperty(JSTypeNative.NUMBER_TYPE, null));
      return new RecordType(new JSTypeRegistry(), properties);
   }
   
   private RecordType createRecordTypeWithDifferentProperties() {
      Map<String, RecordProperty> properties = new HashMap<>();
      properties.put("c", new RecordProperty(JSTypeNative.BOOLEAN_TYPE, null));
      properties.put("d", new RecordProperty(JSTypeNative.OBJECT_TYPE, null));
      return new RecordType(new JSTypeRegistry(), properties);
   }
   
   private RecordType createRecordTypeWithMoreProperties() {
      Map<String, RecordProperty> properties = new HashMap<>();
      properties.put("a", new RecordProperty(JSTypeNative.STRING_TYPE, null));
      properties.put("b", new RecordProperty(JSTypeNative.NUMBER_TYPE, null));
      properties.put("c", new RecordProperty(JSTypeNative.BOOLEAN_TYPE, null));
      return new RecordType(new JSTypeRegistry(), properties);
   }
}