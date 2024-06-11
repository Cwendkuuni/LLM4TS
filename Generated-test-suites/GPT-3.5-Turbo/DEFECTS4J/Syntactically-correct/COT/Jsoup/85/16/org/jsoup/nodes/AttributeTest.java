import org.junit.Test;
import org.junit.Assert.*;
import org.junit.Before;

public class AttributeTest {
    
    Attribute attribute;
    
    @Before
    public void setUp() {
        attribute = new Attribute("key", "value");
    }
    
    @Test
    public void testGetKey() {
        String expected = "key";
        String actual = attribute.getKey();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSetKey() {
        String expected = "newKey";
        attribute.setKey(expected);
        String actual = attribute.getKey();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetValue() {
        String expected = "value";
        String actual = attribute.getValue();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSetValue() {
        String expected = "newValue";
        attribute.setValue(expected);
        String actual = attribute.getValue();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHtml() {
        String expected = "key=\"value\"";
        String actual = attribute.html();
        assertEquals(expected, actual);
    }
    
    @Test(expected = SerializationException.class)
    public void testHtmlWithIOException() throws IOException {
        Appendable accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        attribute.html(accum, outputSettings);
    }
    
    @Test
    public void testToString() {
        String expected = "key=\"value\"";
        String actual = attribute.toString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCreateFromEncoded() {
        String unencodedKey = "key";
        String encodedValue = "encodedValue";
        Attribute expected = new Attribute(unencodedKey, encodedValue, null);
        Attribute actual = Attribute.createFromEncoded(unencodedKey, encodedValue);
        assertEquals(expected.getKey(), actual.getKey());
        assertEquals(expected.getValue(), actual.getValue());
    }
    
    @Test
    public void testIsDataAttribute() {
        String key = "data-key";
        boolean expected = true;
        boolean actual = attribute.isDataAttribute(key);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        boolean expected = true;
        boolean actual = attribute.shouldCollapseAttribute(outputSettings);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsBooleanAttribute() {
        String key = "checked";
        boolean expected = true;
        boolean actual = attribute.isBooleanAttribute(key);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals() {
        Attribute attribute2 = new Attribute("key", "value");
        boolean expected = true;
        boolean actual = attribute.equals(attribute2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHashCode() {
        int expected = attribute.getKey().hashCode() + attribute.getValue().hashCode();
        int actual = attribute.hashCode();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testClone() {
        Attribute expected = attribute;
        Attribute actual = attribute.clone();
        assertEquals(expected, actual);
    }
}