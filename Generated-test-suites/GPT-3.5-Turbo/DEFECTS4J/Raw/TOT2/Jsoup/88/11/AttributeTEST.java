import org.jsoup.nodes.Attribute;
import org.jsoup.SerializationException;
import org.jsoup.helper.Validate;
import org.jsoup.internal.StringUtil;
import org.jsoup.helper.Document;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;

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
        String oldVal = attribute.setValue("newValue");
        assertEquals("testValue", oldVal);
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("testKey", "testValue");
        assertEquals("testKey=\"testValue\"", attribute.html());
    }

    @Test(expected = SerializationException.class)
    public void testHtmlException() throws IOException {
        Attribute attribute = new Attribute("testKey", "testValue");
        StringUtil.borrowBuilder().append("Exception");
        attribute.html();
    }

    // Add more test cases for other methods

}