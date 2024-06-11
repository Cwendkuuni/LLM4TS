import org.jsoup.nodes.Attribute;
import org.jsoup.SerializationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class AttributeTest {
    private Attribute attribute;

    @Before
    public void setUp() {
        attribute = new Attribute("testKey", "testValue");
    }

    @Test
    public void testGetKey() {
        Assert.assertEquals("testKey", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        attribute.setKey("newKey");
        Assert.assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testGetValue() {
        Assert.assertEquals("testValue", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        String oldValue = attribute.setValue("newValue");
        Assert.assertEquals("testValue", oldValue);
        Assert.assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        Assert.assertEquals("testKey=\"testValue\"", attribute.html());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute newAttribute = Attribute.createFromEncoded("key", "encodedValue");
        Assert.assertEquals("key", newAttribute.getKey());
        Assert.assertEquals("encodedValue", newAttribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Assert.assertFalse(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Assert.assertFalse(attribute.shouldCollapseAttribute());
    }

    @Test
    public void testIsBooleanAttribute() {
        Assert.assertFalse(attribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute other = new Attribute("testKey", "testValue");
        Assert.assertTrue(attribute.equals(other));
    }

    @Test
    public void testHashCode() {
        Attribute other = new Attribute("testKey", "testValue");
        Assert.assertEquals(attribute.hashCode(), other.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        Assert.assertEquals(attribute.getKey(), clonedAttribute.getKey());
        Assert.assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}