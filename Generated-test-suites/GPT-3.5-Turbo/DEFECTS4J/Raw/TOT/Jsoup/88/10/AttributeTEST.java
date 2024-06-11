import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class AttributeTest {

    private String key;
    private String val;
    private String expectedKey;
    private String expectedVal;
    private Attribute attribute;

    public AttributeTest(String key, String val, String expectedKey, String expectedVal) {
        this.key = key;
        this.val = val;
        this.expectedKey = expectedKey;
        this.expectedVal = expectedVal;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"href", "link.html", "href", "link.html"},
                {"src", "image.png", "src", "image.png"},
                {"checked", "true", "checked", "true"}
        });
    }

    @Test
    public void testSetKey() {
        attribute = new Attribute(key, val);
        attribute.setKey(expectedKey);
        assertEquals(expectedKey, attribute.getKey());
    }

    @Test
    public void testSetValue() {
        attribute = new Attribute(key, val);
        attribute.setValue(expectedVal);
        assertEquals(expectedVal, attribute.getValue());
    }

    @Test
    public void testHtml() {
        attribute = new Attribute(key, val);
        String expectedHtml = key + "=\"" + val + "\"";
        assertEquals(expectedHtml, attribute.html());
    }

    @Test
    public void testToString() {
        attribute = new Attribute(key, val);
        String expectedString = key + "=\"" + val + "\"";
        assertEquals(expectedString, attribute.toString());
    }
}