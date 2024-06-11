import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationTest;
import com.fasterxml.jackson.databind.json.JsonSerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class XmlSerializerProviderTest {

    private ObjectMapper objectMapper;
    private XmlMapper xmlMapper;
    private SimpleModule module;
    private JsonSerializer<String> stringSerializer;

    @Before
    public void setup() {
        module = new SimpleModule();
        module.addSerializer(String.class, new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeString(value);
            }
        });
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(module);
        xmlMapper = new XmlMapper();
        stringSerializer = objectMapper.findValueSerializer(String.class, null, null);
    }

    @Test
    public void testSerializeNull() throws IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(xmlMapper.getRootNameLookup());
        String xml = provider.getSerializerProvider().serializeValueAsString(null);
        assertEquals("<null xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"></null>", xml);
    }

    @Test
    public void testSerializeString() throws IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(xmlMapper.getRootNameLookup());
        String xml = provider.getSerializerProvider().serializeValueAsString("test", stringSerializer);
        assertEquals("<test xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"></test>", xml);
    }

    @Test
    public void testSerializeArrayOfStrings() throws IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(xmlMapper.getRootNameLookup());
        String xml = provider.getSerializerProvider().serializeValueAsString(new String[]{"test1", "test2"}, stringSerializer);
        assertEquals("<item xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
                "<test1 xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"></test1>" +
                "<test2 xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"></test2>" +
                "</item>", xml);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeArrayOfIncompatibleTypes() throws IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(xmlMapper.getRootNameLookup());
        provider.getSerializerProvider().serializeValue(new ObjectMapper().getFactory().createGenerator(), new Object[]{"test", 1});
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeNonArrayWithRootName() throws IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(xmlMapper.getRootNameLookup());
        String rootName = "testRoot";
        provider.getSerializerProvider().setSerializationConfig(new SerializationConfig(new ObjectMapper().getDefaultConfig()));
        provider.getSerializerProvider().setSerializationConfig(provider.getSerializerProvider().getSerializationConfig().withRootName(new QName(rootName)));
        String xml = provider.getSerializerProvider().serializeValueAsString(new String[]{"test1", "test2"}, stringSerializer);
        assertEquals("<testRoot xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><item><test1 xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"></test1><test2 xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"></test2></item></testRoot>", xml);
    }

    @Test
    public void testSerializeEmptyArray() throws IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(xmlMapper.getRootNameLookup());
        String xml = provider.getSerializerProvider().serializeValueAsString(new String[0], stringSerializer);
        assertEquals("<item xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"></item>", xml);
    }

    @Test
    public void testSerializeArrayWithEmptyElement() throws IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(xmlMapper.getRootNameLookup());
        String[] array = {"test1", ""};
        String xml = provider.getSerializerProvider().serializeValueAsString(array, stringSerializer);
        assertEquals("<item xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><test1 xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"></test1><test2 xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"></test2></item>", xml);
    }
}