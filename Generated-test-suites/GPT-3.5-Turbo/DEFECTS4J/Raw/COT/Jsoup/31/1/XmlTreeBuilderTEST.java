import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Assert;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInsertNode() {
        Element parentElement = new Element("parent");
        Element childElement = new Element("child");
        
        xmlTreeBuilder.insertNode(parentElement);
        xmlTreeBuilder.insertNode(childElement);
        
        Assert.assertEquals(childElement, parentElement.childNode(0));
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder.Token.StartTag startTag = new XmlTreeBuilder.Token.StartTag("tag");
        startTag.setSelfClosing(false);
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        Assert.assertNotNull(element);
        Assert.assertEquals("tag", element.tagName());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder.Token.Comment commentToken = new XmlTreeBuilder.Token.Comment("Test comment");
        
        xmlTreeBuilder.insert(commentToken);
        
        Assert.assertEquals("<!--Test comment-->", xmlTreeBuilder.currentElement().html());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder.Token.Character characterToken = new XmlTreeBuilder.Token.Character("Test character");
        
        xmlTreeBuilder.insert(characterToken);
        
        Assert.assertEquals("Test character", xmlTreeBuilder.currentElement().html());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder.Token.Doctype doctypeToken = new XmlTreeBuilder.Token.Doctype("html", "", "", "");
        
        xmlTreeBuilder.insert(doctypeToken);
        
        Assert.assertEquals("<!DOCTYPE html>", xmlTreeBuilder.currentElement().html());
    }

    @Test
    public void testPopStackToClose() {
        Element parentElement = new Element("parent");
        Element childElement = new Element("child");
        Element grandchildElement = new Element("grandchild");
        
        parentElement.appendChild(childElement);
        childElement.appendChild(grandchildElement);
        
        xmlTreeBuilder.popStackToClose(new XmlTreeBuilder.Token.EndTag("child"));
        
        Assert.assertNull(childElement.parent());
        Assert.assertNotNull(grandchildElement.parent());
    }

}