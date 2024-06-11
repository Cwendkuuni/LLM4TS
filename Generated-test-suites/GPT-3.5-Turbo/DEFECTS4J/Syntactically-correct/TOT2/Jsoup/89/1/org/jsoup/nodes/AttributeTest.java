import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import org.jsoup.SerializationException;

public class AttributeTest {

    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("testKey", "testValue");
        assertEquals("testKey", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("testKey", "testValue");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("testKey", "testValue");
        assertEquals("testValue", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("testKey", "testValue");
        String oldValue = attribute.setValue("newValue");
        assertEquals("testValue", oldValue);
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("testKey", "testValue");
        assertEquals("testKey=\"testValue\"", attribute.html());
    }

    @Test(expected = SerializationException.class)
    public void testHtmlSerializationException() {
        Attribute attribute = new Attribute("testKey", "testValue");
        attribute.html();
    }

}