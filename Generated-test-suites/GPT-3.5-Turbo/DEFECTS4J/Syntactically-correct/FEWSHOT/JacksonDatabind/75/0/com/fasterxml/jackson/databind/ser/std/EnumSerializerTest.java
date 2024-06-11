import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;
import org.junit.Test;

public class EnumSerializerTest {
    
    @Test
    public void testSerialize() {
        EnumValues enumValues = EnumValues.constructFromName(null, TestEnum.class);
        
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
        assertEquals("0", enumSerializer.serialize(TestEnum.FIRST, null, null));
        assertEquals("1", enumSerializer.serialize(TestEnum.SECOND, null, null));
        
        enumSerializer = new EnumSerializer(enumValues, false);
        assertEquals("FIRST", enumSerializer.serialize(TestEnum.FIRST, null, null));
        assertEquals("SECOND", enumSerializer.serialize(TestEnum.SECOND, null, null));
    }
    
    @Test
    public void testCreateContextual() throws Exception {
        EnumValues enumValues = EnumValues.constructFromName(null, TestEnum.class);
        
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
        EnumSerializer contextualSerializer = (EnumSerializer) enumSerializer.createContextual(null, null);
        assertEquals(enumSerializer.serialize(TestEnum.FIRST, null, null), contextualSerializer.serialize(TestEnum.FIRST, null, null));
        assertEquals(enumSerializer.serialize(TestEnum.SECOND, null, null), contextualSerializer.serialize(TestEnum.SECOND, null, null));
        
        enumSerializer = new EnumSerializer(enumValues, false);
        contextualSerializer = (EnumSerializer) enumSerializer.createContextual(null, null);
        assertEquals(enumSerializer.serialize(TestEnum.FIRST, null, null), contextualSerializer.serialize(TestEnum.FIRST, null, null));
        assertEquals(enumSerializer.serialize(TestEnum.SECOND, null, null), contextualSerializer.serialize(TestEnum.SECOND, null, null));
    }
    
    @Test
    public void testGetEnumValues() {
        EnumValues enumValues = EnumValues.constructFromName(null, TestEnum.class);
        
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
        assertEquals(enumValues, enumSerializer.getEnumValues());
    }
    
    @Test
    public void testGetSchema() throws Exception {
        EnumValues enumValues = EnumValues.constructFromName(null, TestEnum.class);
        
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
        assertEquals("integer", enumSerializer.getSchema(null, null).get("type").asText());
        
        enumSerializer = new EnumSerializer(enumValues, false);
        assertEquals("string", enumSerializer.getSchema(null, null).get("type").asText());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        EnumValues enumValues = EnumValues.constructFromName(null, TestEnum.class);
        
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
        enumSerializer.acceptJsonFormatVisitor(null, null);

        enumSerializer = new EnumSerializer(enumValues, false);
        enumSerializer.acceptJsonFormatVisitor(null, null);
    }
    
    private enum TestEnum {
        FIRST,
        SECOND
    }
}