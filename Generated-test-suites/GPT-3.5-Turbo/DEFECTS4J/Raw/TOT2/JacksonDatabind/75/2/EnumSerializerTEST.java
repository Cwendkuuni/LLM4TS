import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;

public class EnumSerializerTest {
  
  @Test
  public void testSerialize() {
    EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(TestEnum.class), false);
    
    // Test typical case
    assertEquals("VALUE1", enumSerializer.serialize(TestEnum.VALUE1));
    assertEquals("VALUE2", enumSerializer.serialize(TestEnum.VALUE2));
  }
  
  @Test
  public void testSerializeAsIndex() {
    EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(TestEnum.class), true);
    
    // Test typical case
    assertEquals(0, enumSerializer.serialize(TestEnum.VALUE1));
    assertEquals(1, enumSerializer.serialize(TestEnum.VALUE2));
  }
  
  @Test
  public void testGetEnumValues() {
    EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(TestEnum.class), false);
    
    // Test typical case
    assertEquals(EnumValues.constructFromName(TestEnum.class), enumSerializer.getEnumValues());
  }
  
  @Test
  public void testCreateContextual() {
    EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(TestEnum.class), true);
    
    // Test typical case
    assertEquals(enumSerializer, enumSerializer.createContextual(null, null));
  }
  
  @Test
  public void testGetSchema() {
    EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(TestEnum.class), false);
    
    // Test typical case
    assertNotNull(enumSerializer.getSchema(null, null));
  }
  
  @Test
  public void testAcceptJsonFormatVisitor() {
    EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(TestEnum.class), false);
    
    // Test typical case
    assertNotNull(enumSerializer.acceptJsonFormatVisitor(null, null));
  }
  
}