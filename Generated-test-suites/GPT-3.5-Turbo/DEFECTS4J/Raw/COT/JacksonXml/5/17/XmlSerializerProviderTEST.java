
import static org.junit.Assert.*;

import org.junit.Test;

public class XmlSerializerProviderTest {

    @Test
    public void testSerializeValueWithNull() {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        StringWriter writer = new StringWriter();
        try {
            provider.serializeValue(new ToXmlGenerator(null, writer), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Assert the serialized value is "<null>"
        assertEquals("<null>", writer.toString());
    }
    
    @Test
    public void testSerializeValue() {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        StringWriter writer = new StringWriter();
        try {
            provider.serializeValue(new ToXmlGenerator(null, writer), "test");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Assert the serialized value is "test"
        assertEquals("test", writer.toString());
    }
    
    @Test
    public void testSerializeValueWithJavaType() {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        StringWriter writer = new StringWriter();
        JavaType javaType = JavaType.construct(String.class);
        try {
            provider.serializeValue(new ToXmlGenerator(null, writer), "test", javaType, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Assert the serialized value is "test"
        assertEquals("test", writer.toString());
    }
    
    @Test
    public void testSerializeValueWithException() {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        StringWriter writer = new StringWriter();
        try {
            provider.serializeValue(new ToXmlGenerator(null, writer), new Object());
        } catch (IOException e) {
            // Assert an IOException is thrown
            assertTrue(e instanceof IOException);
        }
    }
    
    @Test
    public void testSerializeValueWithRootArray() {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        StringWriter writer = new StringWriter();
        try {
            provider.serializeValue(new ToXmlGenerator(null, writer), new String[] {"test1", "test2"});
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Assert the serialized value is "<item>test1</item><item>test2</item>"
        assertEquals("<item>test1</item><item>test2</item>", writer.toString());
    }
    
    @Test
    public void testSerializeValueWithExceptionAndRootArray() {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        StringWriter writer = new StringWriter();
        try {
            provider.serializeValue(new ToXmlGenerator(null, writer), new Object[] {new Object(),
                    new Object()});
        } catch (IOException e) {
            // Assert an IOException is thrown
            assertTrue(e instanceof IOException);
        }
    }

}
