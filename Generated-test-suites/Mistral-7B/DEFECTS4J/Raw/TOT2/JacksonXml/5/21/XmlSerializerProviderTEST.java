import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

public class XmlSerializerProviderTest {

    @Test
    public void testSerializeNull() throws JsonGenerationException, JsonMappingException {
        // Given
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(null);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator jsonGenerator = objectMapper.getXmlGenerator();

        // When
        xmlSerializerProvider.serializeValue(jsonGenerator, null);

        // Then
        String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<null/>";
        String actualXml = jsonGenerator.getOutputFactory().createXmlText().toString();
        Assert.assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testSerializePrimitiveTypes() throws JsonGenerationException, JsonMappingException {
        // Given
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(null);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator jsonGenerator = objectMapper.getXmlGenerator();

        // When
        xmlSerializerProvider.serializeValue(jsonGenerator, "Hello");
        xmlSerializerProvider.serializeValue(jsonGenerator, 123);
        xmlSerializerProvider.serializeValue(jsonGenerator, 1.23);
        xmlSerializerProvider.serializeValue(jsonGenerator, true);

        // Then
        String expectedXml = "<string>Hello</string>\n<int>123</int>\n<double>1.23</double>\n<boolean>true</boolean>";
        String actualXml = jsonGenerator.getOutputFactory().createXmlText().toString();
        Assert.assertEquals(expectedXml, actualXml);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeUnknownType() throws JsonGenerationException, JsonMappingException {
        // Given
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(null);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator jsonGenerator = objectMapper.getXmlGenerator();

        // When & Then
        xmlSerializerProvider.serializeValue(jsonGenerator, new MyClass());
    }

    // ... Add test cases for other methods
}

class MyClass {
    // A simple class with no default serializer
}


public class XmlSerializerProviderTest {
    // ... Test methods
}

