package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.SerializationException;
import org.jsoup.internal.StringUtil;
import org.jsoup.helper.Validate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import org.junit.Test;

public class AttributeTest {
    @Test
    public void testConstructorAndGetters() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
    }

    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
    }

    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("key", "value");
        String oldVal = attr.setValue("newValue");
        assertEquals("value", oldVal);
        assertEquals("newValue", attr.getValue());
    }
    
    @Test
    public void testHtml() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key=\"value\"", attr.html());
    }
    
    @Test(expected = SerializationException.class)
    public void testHtmlIOException() {
        Attribute attr = new Attribute("key", "value") {
            protected void html(Appendable accum, Document.OutputSettings out) throws IOException {
                throw new IOException();
            }
        };
        attr.html();
    }
    
    @Test
    public void testToString() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key=\"value\"", attr.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("key", "&quot;value&quot;");
        assertEquals("key", attr.getKey());
        assertEquals("\"value\"", attr.getValue());
    }
    
    @Test
    public void testIsDataAttribute() {
        assertTrue(Attribute.isDataAttribute("data-key"));
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.html);
        
        assertTrue(Attribute.shouldCollapseAttribute("key", "key", out));
    }
    
    @Test
    public void testIsBooleanAttribute() {
        assertTrue(Attribute.isBooleanAttribute("class"));
    }
    
    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key", "value");
        Attribute attr2 = new Attribute("key", "value");
        Attribute attr3 = new Attribute("newKey", "value");
        Attribute attr4 = new Attribute("key", "newValue");
        
        assertEquals(attr1, attr2);
        assertEquals(attr2, attr1);
        assertEquals(attr1, attr1);
        
        assertTrue(!attr1.equals(attr3));
        assertTrue(!attr1.equals(attr4));
        assertTrue(!attr1.equals(null));
        assertTrue(!attr1.equals("key=value"));
    }
    
    @Test
    public void testHashCode() {
        Attribute attr1 = new Attribute("key", "value");
        Attribute attr2 = new Attribute("key", "value");
        
        assertEquals(attr1.hashCode(), attr2.hashCode());
    }
    
    @Test
    public void testClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute clone = attr.clone();
        
        assertEquals(attr, clone);
        assertTrue(attr != clone);
    }
}