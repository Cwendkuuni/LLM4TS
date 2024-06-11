import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.*;

public class XmlSerializerProviderTest {

    @Test
    public void testSerializeValue() throws JsonProcessingException, XMLStreamException {
        ObjectMapper mapper = new ObjectMapper(new XmlMapper.Builder()
                .withSerializationFeature("FAIL_ON_EMPTY_BEANS", false)
                .withXMLFactory(XMLFactory.instance)
                .build());

        ObjectNode jsonNode = mapper.createObjectNode();
        JsonGenerator gen = xmlFactory.createGenerator(jsonNode);
        XmlSerializerProvider provider = new XmlSerializerProvider(mapper.getSerializationConfig(), mapper.getSerializerProvider().getFactory());

        Object value = "Test String";
        provider.serializeValue(gen, value);
        gen.flush();

        assertEquals("<TestString>Test String</TestString>", jsonNode.get("").asText());
        gen.close();
    }
}