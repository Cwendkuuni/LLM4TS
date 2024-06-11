import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RecordTypeTest {
   @Test
   public void testRecordTypeCreation() {
      Map<String, RecordProperty> properties = Maps.newHashMap();
      properties.put("name", new RecordProperty("name", String.class));
      properties.put("age", new RecordProperty("age", Integer.class));

      JSTypeRegistry registry = new JSTypeRegistry();
      RecordType recordType = new RecordType(registry, properties);

      assertEquals("RecordType", recordType.getName());
      assertNotNull(recordType.getGreatestSubtypeHelper(null));
   }

   @Test(expected = IllegalStateException.class)
   public void testRecordTypePropertyNull() {
      Map<String, RecordProperty> properties = Maps.newHashMap();
      properties.put("name", null);
      properties.put("age", new RecordProperty("age", Integer.class));

      JSTypeRegistry registry = new JSTypeRegistry();
      try {
         new RecordType(registry, properties);
      } catch (IllegalStateException e) {
         assertEquals("RecordProperty associated with a property should not be null!", e.getMessage());
         throw e;
      }
   }
}