import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.util.Arrays;

public class EnumSerializerTest {

    @Test
    public void testSerializeAsIndex() throws IOException {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(EnumClass.class));

        EnumClass enumInstance = EnumClass.VALUE1;
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode serializedNode = objectMapper.valueToTree(enumInstance);
        Assert.assertEquals(serializedNode.intValue(), enumInstance.ordinal());
    }

    @Test
    public void testSerializeAsName() throws IOException {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(EnumClass.class));

        EnumClass enumInstance = EnumClass.VALUE2;
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode serializedNode = objectMapper.valueToTree(enumInstance);
        Assert.assertEquals(serializedNode.textValue(), enumInstance.toString());
    }

    @Test
    public void testEnumsInSchema() throws IOException {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(EnumClass.class));

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode schemaNode = enumSerializer.getSchema(objectMapper.getSerializerProvider(), null);
        JsonNode enumNode = schemaNode.get("enum");
        Assert.assertTrue(enumNode.isArray());
        Assert.assertTrue(enumNode.size() == EnumClass.values().length);
        Assert.assertTrue(Arrays.asList(enumNode).contains(EnumClass.VALUE1.toString()));
        Assert.assertTrue(Arrays.asList(enumNode).contains(EnumClass.VALUE2.toString()));
        Assert.assertTrue(Arrays.asList(enumNode).contains(EnumClass.VALUE3.toString()));
    }

    @Test
    public void testJsonFormatVisitorWithStringFormat() throws IOException {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(EnumClass.class));

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode schemaNode = objectMapper.valueToTree(enumInstance);
        JsonNode formatNode = schemaNode.get("format");
        Assert.assertEquals(formatNode.textValue(), "string");
        JsonNode enumNode = schemaNode.get("enum");
        Assert.assertTrue(enumNode.isArray());
        Assert.assertTrue(enumNode.size() == EnumClass.values().length);
        Assert.assertTrue(Arrays.asList(enumNode).contains(EnumClass.VALUE1.toString()));
        Assert.assertTrue(Arrays.asList(enumNode).contains(EnumClass.VALUE2.toString()));
        Assert.assertTrue(Arrays.asList(enumNode).contains(EnumClass.VALUE3.toString()));
    }
}