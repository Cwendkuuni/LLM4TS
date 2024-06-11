import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import java.util.List;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.relaxed());
    }

    @Test
    public void testClean() {
        Document dirtyDocument = new Document("");
        dirtyDocument.body().append("<p>Hello World!</p>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element body = cleanDocument.body();
        List<Node> children = body.childNodes();
        Assert.assertEquals(1, children.size());
        Assert.assertEquals("p", ((Element) children.get(0)).tagName());
        Assert.assertEquals("Hello World!", body.text());
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = new Document("");
        dirtyDocument.body().append("<p>Hello World!</p>");
        Assert.assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Element root = new Element("div");
        Element destination = new Element("div");
        Element child1 = new Element("p");
        child1.append("Hello");
        Element child2 = new Element("span");
        child2.attr("class", "test");
        child2.append("World");
        root.appendChild(child1);
        root.appendChild(child2);
        int numDiscarded = cleaner.copySafeNodes(root, destination);
        Assert.assertEquals(0, numDiscarded);
        List<Node> children = destination.childNodes();
        Assert.assertEquals(2, children.size());
        Assert.assertTrue(children.get(0) instanceof Element);
        Assert.assertTrue(children.get(1) instanceof Element);
        Assert.assertEquals("p", ((Element) children.get(0)).tagName());
        Assert.assertEquals("span", ((Element) children.get(1)).tagName());
        Assert.assertEquals("Hello", ((Element) children.get(0)).text());
        Assert.assertEquals("World", ((Element) children.get(1)).text());
        Assert.assertEquals("test", ((Element) children.get(1)).attr("class"));
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element("div");
        sourceEl.attr("id", "test");
        sourceEl.attr("class", "test");
        sourceEl.attr("data", "test");
        ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        Element dest = elementMeta.el;
        Assert.assertEquals("div", dest.tagName());
        Assert.assertEquals(1, dest.attributes().size());
        Assert.assertEquals("test", dest.attr("class"));
        Assert.assertEquals(2, elementMeta.numAttribsDiscarded);
    }
}