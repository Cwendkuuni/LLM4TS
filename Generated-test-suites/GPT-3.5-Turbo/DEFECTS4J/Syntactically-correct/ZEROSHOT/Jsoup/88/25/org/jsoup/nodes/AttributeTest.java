import org.junit.Before;
import org.junit.Test;
import org.jsoup.SerializationException;
import org.jsoup.helper.Validate;
import org.jsoup.internal.StringUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.*;

public class AttributeTest {
    private Attribute attribute;

    @Before
    public void setUp() {
        attribute = new Attribute("testKey", "testValue");
    }

    @Test
    public void getKey() {
        assertEquals("testKey", attribute.getKey());
    }

    @Test
    public void setKey() {
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void getValue() {
        assertEquals("testValue", attribute.getValue());
    }

    @Test
    public void setValue() {
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void html() {
        assertEquals("testKey=\"testValue\"", attribute.html());
    }

    @Test
    public void toStringTest() {
        assertEquals("testKey=\"testValue\"", attribute.toString());
    }

    @Test
    public void createFromEncoded() {
        Attribute newAttribute = Attribute.createFromEncoded("key", "test%20value");
        assertEquals("key", newAttribute.getKey());
        assertEquals("test value", newAttribute.getValue());
    }

    @Test
    public void isDataAttribute() {
        assertFalse(attribute.isDataAttribute());
    }

    @Test
    public void shouldCollapseAttribute() {
        Attribute booleanAttribute = new Attribute("checked", null);
        assertTrue(booleanAttribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void isBooleanAttribute() {
        assertFalse(attribute.isBooleanAttribute());
    }

    @Test
    public void equalsTest() {
        Attribute equalAttribute = new Attribute("testKey", "testValue");
        assertEquals(attribute, equalAttribute);
    }

    @Test
    public void hashCodeTest() {
        Attribute equalAttribute = new Attribute("testKey", "testValue");
        assertEquals(attribute.hashCode(), equalAttribute.hashCode());
    }

    @Test
    public void cloneTest() {
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
    }
}