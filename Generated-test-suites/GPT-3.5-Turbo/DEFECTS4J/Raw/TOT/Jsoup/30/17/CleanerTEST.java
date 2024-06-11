import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
    
    private Cleaner cleaner;
    private Document dirtyDocument;
    
    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.relaxed());
        dirtyDocument = new Document("");
        Element body = new Element(Tag.valueOf("body"), "");
        dirtyDocument.appendChild(body);
    }
    
    @Test
    public void testClean_withValidDirtyDocument_returnCleanedDocument() {
        Document expected = new Document("");
        
        Element expectedBody = new Element(Tag.valueOf("body"), "");
        expected.appendChild(expectedBody);
        
        Document actual = cleaner.clean(dirtyDocument);
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testClean_withInvalidDirtyDocument_returnCleanedDocumentWithEmptyBody() {
        Document expected = new Document("");
        Document actual = cleaner.clean(dirtyDocument);
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testIsValid_withValidDirtyDocument_returnTrue() {
        boolean actual = cleaner.isValid(dirtyDocument);
        
        Assert.assertTrue(actual);
    }
    
    @Test
    public void testIsValid_withInvalidDirtyDocument_returnFalse() {
        Document dirtyDocumentWithUnsafeTag = new Document("");
        Element body = new Element(Tag.valueOf("body"), "");
        Element unsafeTag = new Element(Tag.valueOf("script"), "");
        body.appendChild(unsafeTag);
        dirtyDocumentWithUnsafeTag.appendChild(body);
        
        boolean actual = cleaner.isValid(dirtyDocumentWithUnsafeTag);
        
        Assert.assertFalse(actual);
    }

    @Test
    public void testIsValid_withNullDirtyDocument_throwException() {
        try {
            cleaner.isValid(null);
            Assert.fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Test passed
        }
    }
    
    
    @Test
    public void testCopySafeNodes_withElementContainingSafeTag_copySafeNodesToDestination() {
        Document destination = new Document("");
        Element root = new Element(Tag.valueOf("div"), "");
        Element safeTag = new Element(Tag.valueOf("p"), "");
        Element safeChild = new Element(Tag.valueOf("span"), "");
        safeTag.appendChild(safeChild);
        root.appendChild(safeTag);
        
        cleaner.copySafeNodes(root, destination);
        
        Assert.assertEquals(safeTag, destination.body().child(0));
        Assert.assertEquals(safeChild, destination.body().child(0).child(0));
    }
    
    @Test
    public void testCopySafeNodes_withElementContainingUnsafeTag_copySafeNodesToDestinationAndDiscardUnsafeTag() {
        Document destination = new Document("");
        Element root = new Element(Tag.valueOf("div"), "");
        Element safeTag = new Element(Tag.valueOf("p"), "");
        Element unsafeTag = new Element(Tag.valueOf("script"), "");
        Element safeChild = new Element(Tag.valueOf("span"), "");
        safeTag.appendChild(safeChild);
        unsafeTag.text("Unsafe script");
        root.appendChild(safeTag);
        root.appendChild(unsafeTag);
        
        cleaner.copySafeNodes(root, destination);
        
        Assert.assertEquals(safeTag, destination.body().child(0));
        Assert.assertEquals(safeChild, destination.body().child(0).child(0));
        Assert.assertEquals(0, destination.body().childNodeSize());
    }
    
    @Test
    public void testCopySafeNodes_withTextNode_copyTextNodeToDestination() {
        Document destination = new Document("");
        Element root = new Element(Tag.valueOf("div"), "");
        Element safeTag = new Element(Tag.valueOf("p"), "");
        Element unsafeTag = new Element(Tag.valueOf("script"), "");
        Element safeChild = new Element(Tag.valueOf("span"), "");
        TextNode textNode = new TextNode("Sample Text", "");
        safeTag.appendChild(safeChild);
        unsafeTag.text("Unsafe script");
        root.appendChild(textNode);
        root.appendChild(safeTag);
        root.appendChild(unsafeTag);
        
        cleaner.copySafeNodes(root, destination);
        
        Assert.assertEquals(textNode, destination.body().child(0));
    }
    
    
    @Test
    public void testCreateSafeElement_withSafeElement_createSafeElementWithAttributes() {
        Element sourceEl = new Element(Tag.valueOf("p"), "");
        sourceEl.attr("class", "safe");
        sourceEl.attr("id", "safe-id");
        
        ElementMeta expected = new ElementMeta(sourceEl, 0);
        ElementMeta actual = cleaner.createSafeElement(sourceEl);
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testCreateSafeElement_withUnsafeElement_createSafeElementWithDiscardedAttributes() {
        Element sourceEl = new Element(Tag.valueOf("p"), "");
        sourceEl.attr("class", "safe");
        sourceEl.attr("id", "safe-id");
        sourceEl.attr("onclick", "javascript:alert('Unsafe');");
        
        ElementMeta expected = new ElementMeta(sourceEl, 1);
        ElementMeta actual = cleaner.createSafeElement(sourceEl);
        
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCreateSafeElement_withNullElement_throwException() {
        try {
            cleaner.createSafeElement(null);
            Assert.fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Test passed
        }
    }
}