import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeBuilderTest {

   @Test
   public void testAddProperty() {
      RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
      JSType type = new JSType();
      Node propertyNode = new Node();

      builder.addProperty("name", type, propertyNode);
      assertNotNull(builder.build());
   }

   @Test
   public void testAddDuplicateProperty() {
      RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
      JSType type1 = new JSType();
      Node propertyNode1 = new Node();
      JSType type2 = new JSType();
      Node propertyNode2 = new Node();

      builder.addProperty("name", type1, propertyNode1);
      assertNull(builder.addProperty("name", type2, propertyNode2));
   }

   @Test
   public void testBuildEmptyRecord() {
      RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
      assertNotNull(builder.build());
   }

   @Test
   public void testBuildNonEmptyRecord() {
      RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
      JSType type = new JSType();
      Node propertyNode = new Node();

      builder.addProperty("name", type, propertyNode);
      assertNotNull(builder.build());
   }

   @Test
   public void testRecordPropertyGetType() {
      JSType type = new JSType();
      Node propertyNode = new Node();
      RecordTypeBuilder.RecordProperty recordProperty = new RecordTypeBuilder.RecordProperty(type, propertyNode);
      
      assertEquals(type, recordProperty.getType());
   }

   @Test
   public void testRecordPropertyGetPropertyNode() {
      JSType type = new JSType();
      Node propertyNode = new Node();
      RecordTypeBuilder.RecordProperty recordProperty = new RecordTypeBuilder.RecordProperty(type, propertyNode);
      
      assertEquals(propertyNode, recordProperty.getPropertyNode());
   }
}