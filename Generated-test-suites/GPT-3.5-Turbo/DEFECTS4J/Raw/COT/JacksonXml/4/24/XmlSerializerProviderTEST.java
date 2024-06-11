
import static org.junit.Assert.*;
import org.junit.Test;

public class XmlSerializerProviderTest {

    @Test
    public void testSerializeValue_NullValue_ShouldSerializeXmlNull() {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        ToXmlGenerator generator = new ToXmlGenerator(null, 0, null, null);
        try {
            provider.serializeValue(generator, null);
            
            String actualResult = generator.getOutputTarget().toString();
            String expectedResult = "<null>";
            
            assertEquals(expectedResult, actualResult);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testSerializeValue_NonNullValue_ShouldSerializeValue() {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        ToXmlGenerator generator = new ToXmlGenerator(null, 0, null, null);
        try {
            provider.serializeValue(generator, "TestString");
            
            String actualResult = generator.getOutputTarget().toString();
            String expectedResult = "<string>TestString</string>";
            
            assertEquals(expectedResult, actualResult);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testSerializeValue_NullValueAndJavaType_ShouldSerializeXmlNull() {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        ToXmlGenerator generator = new ToXmlGenerator(null, 0, null, null);
        try {
            provider.serializeValue(generator, null, JavaType.STRING);
            
            String actualResult = generator.getOutputTarget().toString();
            String expectedResult = "<null>";
            
            assertEquals(expectedResult, actualResult);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testSerializeValue_NonNullValueAndJavaType_ShouldSerializeValue() {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        ToXmlGenerator generator = new ToXmlGenerator(null, 0, null, null);
        try {
            provider.serializeValue(generator, "TestString", JavaType.STRING);
            
            String actualResult = generator.getOutputTarget().toString();
            String expectedResult = "<string>TestString</string>";
            
            assertEquals(expectedResult, actualResult);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testSerializeValue_NonNullValueAndJavaTypeAndSerializer_ShouldSerializeValue() {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        ToXmlGenerator generator = new ToXmlGenerator(null, 0, null, null);
        JsonSerializer<Object> serializer = new JsonSerializer<Object>() {
            
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
                gen.writeString("CustomSerialization");
            }
            
            @Override
            public Class<Object> handledType() {
                return Object.class;
            }
        };
        try {
            provider.serializeValue(generator, "TestString", JavaType.STRING, serializer);
            
            String actualResult = generator.getOutputTarget().toString();
            String expectedResult = "<string>CustomSerialization</string>";
            
            assertEquals(expectedResult, actualResult);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void test_startRootArray_ShouldWriteStartObjectAndFieldName() {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        ToXmlGenerator generator = new ToXmlGenerator(null, 0, null, null);
        QName rootName = new QName("test");
        try {
            provider._startRootArray(generator, rootName);
            
            String actualResult = generator.getOutputTarget().toString();
            String expectedResult = "{\"item\"";
            
            assertTrue(actualResult.startsWith(expectedResult));
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void test_initWithRootName_ShouldSetNextNameIfMissing() {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        ToXmlGenerator generator = new ToXmlGenerator(null, 0, null, null);
        QName rootName = new QName("test");
        try {
            provider._initWithRootName(generator, rootName);
            
            String actualResult = generator.getOutputTarget().toString();
            String expectedResult = "<test>";
            
            assertEquals(expectedResult, actualResult);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void test_rootNameFromConfig_ShouldReturnRootNameIfConfigured() {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        provider.configure(SerializationConfig.Feature.ROOT_WRAPPING, true);
        QName expectedRootName = new QName("test");
        
        QName actualRootName = provider._rootNameFromConfig();
        
        assertEquals(expectedRootName, actualRootName);
    }

    @Test
    public void test_asXmlGenerator_GivenToXmlGenerator_ShouldReturnToXmlGeneratorInstance() {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        ToXmlGenerator generator = new ToXmlGenerator(null, 0, null, null);
        
        ToXmlGenerator actualResult = provider._asXmlGenerator(generator);
        
        assertEquals(generator, actualResult);
    }

    @Test(expected = JsonMappingException.class)
    public void test_asXmlGenerator_GivenNonToXmlGenerator_ShouldThrowJsonMappingException() {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        TokenBuffer generator = new TokenBuffer(null, false);
        
        provider._asXmlGenerator(generator);
    }

}
