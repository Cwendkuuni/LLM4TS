import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.ser.XmlRootNameLookup;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

public class XmlSerializerProviderTest {

    private ObjectMapper objectMapper;
    private XmlRootNameLookup xmlRootNameLookup;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        xmlRootNameLookup = new XmlRootNameLookup();
    }

    @Test
    public void testSerializationWithRootNameFromConfig() throws JsonProcessingException, IOException {
        SerializationConfig config = objectMapper.getSerializationConfig();
        config.setRootValueSeparator("_");
        config.setRootName("rootElement");

        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup, config);

        String input = "{\"name\": \"John\", \"age\": 30}";
        MyClass myClass = objectMapper.readValue(input, MyClass.class);

        JsonGenerator jsonGenerator = new TokenBuffer(new StaxWriter());
        xmlSerializerProvider.serializeValue(jsonGenerator, myClass);
        String output = jsonGenerator.getAsString();

        Assert.assertEquals("<rootElement><name>John</name><age>30</age></rootElement>", output);
    }

    @Test
    public void testSerializationWithRootNameFromLookup() throws JsonProcessingException, IOException {
        SerializationConfig config = objectMapper.getSerializationConfig();
        config.setRootValueSeparator("_");
        config.setRootName(null);

        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup, config);

        String input = "{\"name\": \"John\", \"age\": 30}";
        MyClass myClass = objectMapper.readValue(input, MyClass.class);

        JsonGenerator jsonGenerator = new TokenBuffer(new StaxWriter());
        xmlSerializerProvider.serializeValue(jsonGenerator, myClass);
        String output = jsonGenerator.getAsString();

        Assert.assertEquals("<name>John</name><age>30</age>", output);
    }

    @Test
    public void testSerializationWithNullValue() throws JsonProcessingException, IOException {
        SerializationConfig config = objectMapper.getSerializationConfig();
        config.setRootValueSeparator("_");
        config.setRootName(null);

        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup, config);

        JsonGenerator jsonGenerator = new TokenBuffer(new StaxWriter());
        xmlSerializerProvider.serializeValue(jsonGenerator, null);
        String output = jsonGenerator.getAsString();

        Assert.assertEquals("<null/>", output);
    }

    @Test
    public void testSerializationAsArray() throws JsonProcessingException, IOException {
        SerializationConfig config = objectMapper.getSerializationConfig();
        config.setRootValueSeparator("_");
        config.setRootName(null);

        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup, config);

        MyArray myArray = new MyArray();
        myArray.add(new MyClass("John", 30));
        myArray.add(new MyClass("Mike", 25));

        JsonGenerator jsonGenerator = new TokenBuffer(new StaxWriter());
        xmlSerializerProvider.serializeValue(jsonGenerator, myArray);
        String output = jsonGenerator.getAsString();

        Assert.assertEquals("<item><name>John</name><age>30</age></item>" +
                "<item><name>Mike</name><age>25</age></item>", output);
    }

    static class MyClass {
        private final String name;
        private final int age;

        public MyClass(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }

    static class MyArray extends ArrayList<MyClass> {
    }

}

class StaxWriter extends com.fasterxml.jackson.dataformat.xml.ser.StaxWriter {
}