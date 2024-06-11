import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathNotFoundException;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.xml.DocumentContainer;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

    private JXPathContext context;
    private Document document;

    @Before
    public void setUp() {
        Element root = new Element("root");

        Element element1 = new Element("element1");
        element1.setAttribute(new Attribute("attribute1", "value1"));
        element1.setAttribute(new Attribute("attribute2", "value2"));

        Element element2 = new Element("element2");
        element2.setAttribute(new Attribute("attribute1", "value1"));

        root.addContent(element1);
        root.addContent(element2);

        document = new Document(root);

        context = JXPathContext.newContext(new DocumentContainer(document));
    }

    @Test
    public void testGetFirstAttributeByName() {
        String xpath = "/root/element1[@attribute1='value1']/@attribute1";
        Pointer pointer = context.getPointer(xpath);

        assertTrue(pointer.getValue().equals("value1"));
    }

    @Test(expected = JXPathNotFoundException.class)
    public void testGetMissingAttribute() {
        String xpath = "/root/element2[@attribute3='value3']/@attribute3";
        Pointer pointer = context.getPointer(xpath);

        pointer.getValue();
    }

    @Test
    public void testGetAttributeByNamespace() {
        Namespace ns = Namespace.getNamespace("prefix", "http://www.example.com");
        Attribute attribute = new Attribute("attribute4", "value4", ns);

        Element element = new Element("element3");
        element.setAttribute(attribute);

        document.getRootElement().addContent(element);

        String xpath = "/root/element3[@prefix:attribute4='value4']/@prefix:attribute4";
        Pointer pointer = context.getPointer(xpath);

        assertTrue(pointer.getValue().equals("value4"));
    }

    @Test
    public void testGetAllAttributes() {
        String xpath = "/root/element1[@attribute2='value2']/*";
        Pointer pointer = context.getPointer(xpath);

        assertTrue(pointer.getNode().equals(document.getRootElement().getChild("element1")));
    }

}