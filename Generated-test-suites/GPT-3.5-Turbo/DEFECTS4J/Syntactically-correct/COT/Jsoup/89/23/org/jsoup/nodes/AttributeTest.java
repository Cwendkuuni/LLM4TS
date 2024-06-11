
import org.junit.Test;
import org.junit.Assert;

public class AttributeTest {

    @Test
    public void testGetKey() {
        Attribute attr = new Attribute("name", "John");
        Assert.assertEquals("name", attr.getKey());
    }

    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("name", "John");
        attr.setKey("firstName");
        Assert.assertEquals("firstName", attr.getKey());
    }

    @Test
    public void testGetValue() {
        Attribute attr = new Attribute("name", "John");
        Assert.assertEquals("John", attr.getValue());
    }

    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("name", "John");
        attr.setValue("Doe");
        Assert.assertEquals("Doe", attr.getValue());
    }

    @Test
    public void testHtml() {
        Attribute attr = new Attribute("name", "John");
        Assert.assertEquals("name=\"John\"", attr.html());
    }

    @Test
    public void testToString() {
        Attribute attr = new Attribute("name", "John");
        Assert.assertEquals("name=\"John\"", attr.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("name", "John&amp;Doe");
        Assert.assertEquals("name", attr.getKey());
        Assert.assertEquals("John&Doe", attr.getValue());
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("name", "John");
        Attribute attr2 = new Attribute("name", "John");
        Attribute attr3 = new Attribute("firstName", "John");
        Assert.assertTrue(attr1.equals(attr2));
        Assert.assertFalse(attr1.equals(attr3));
    }

    @Test
    public void testHashCode() {
        Attribute attr1 = new Attribute("name", "John");
        Attribute attr2 = new Attribute("name", "John");
        Assert.assertEquals(attr1.hashCode(), attr2.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attr = new Attribute("name", "John");
        Attribute clone = attr.clone();
        Assert.assertEquals(attr.getKey(), clone.getKey());
        Assert.assertEquals(attr.getValue(), clone.getValue());
        Assert.assertNotSame(attr, clone);
    }
}

