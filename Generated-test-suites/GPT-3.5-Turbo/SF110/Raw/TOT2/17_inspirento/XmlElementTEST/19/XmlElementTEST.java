import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import org.junit.Test;


public class XmlElementTest {

    @Test
    public void testDefaultConstructor() {
        XmlElement element = new XmlElement();
        Assert.assertNotNull(element);
    }

    @Test
    public void testConstructorWithName() {
        XmlElement element = new XmlElement("testName");
        Assert.assertEquals("testName", element.getName());
    }

    @Test
    public void testAddAttribute() {
        XmlElement element = new XmlElement();
        element.addAttribute("key", "value");
        Assert.assertEquals("value", element.getAttribute("key"));
    }

    @Test
    public void testRemoveElementByObject() {
        XmlElement parent = new XmlElement();
        XmlElement child = new XmlElement("child");
        parent.addElement(child);
        Assert.assertEquals(child, parent.removeElement(child));
    }

    // Add more test cases for the other methods...

}