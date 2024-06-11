import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.junit.Assert;
import org.junit.Test;

public class CleanerTest {
    @Test
    public void testClean_typicalCase() {
        // Typical input with multiple tags and attributes
        String dirtyHTML = "<html><head><title>Dirty</title></head><body><div id=\"main\"><p class=\"content\">Hello World</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHTML);
        
        Whitelist whitelist = new Whitelist();
        whitelist.addTags("html", "body", "div", "p");
        whitelist.addAttributes("html", "head", "title");
        whitelist.addAttributes("div", "id");
        whitelist.addAttributes("p", "class");
        
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        String expectedHTML = "<html><head><title>Dirty</title></head><body><div id=\"main\"><p class=\"content\">Hello World</p></div></body></html>";
        Document expectedDocument = Jsoup.parse(expectedHTML);
        
        Assert.assertEquals(expectedDocument, cleanDocument);
    }
    
    @Test
    public void testClean_noTags() {
        // Input with no tags
        String dirtyHTML = "";
        Document dirtyDocument = Jsoup.parse(dirtyHTML);
        
        Whitelist whitelist = new Whitelist();
        whitelist.addTags("html", "body");
        
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        String expectedHTML = "<html><head></head><body></body></html>";
        Document expectedDocument = Jsoup.parse(expectedHTML);
        
        Assert.assertEquals(expectedDocument, cleanDocument);
    }
    
    @Test
    public void testClean_noAttributes() {
        // Input with no attributes
        String dirtyHTML = "<div></div>";
        Document dirtyDocument = Jsoup.parse(dirtyHTML);
        
        Whitelist whitelist = new Whitelist();
        whitelist.addTags("div");
        
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        String expectedHTML = "<div></div>";
        Document expectedDocument = Jsoup.parse(expectedHTML);
        
        Assert.assertEquals(expectedDocument, cleanDocument);
    }
    
    @Test
    public void testIsValid_validDocument() {
        // Valid input document according to the whitelist
        String dirtyHTML = "<div id=\"main\"><p class=\"content\">Hello World</p></div>";
        Document dirtyDocument = Jsoup.parse(dirtyHTML);
        
        Whitelist whitelist = new Whitelist();
        whitelist.addTags("div", "p");
        whitelist.addAttributes("div", "id");
        whitelist.addAttributes("p", "class");
        
        Cleaner cleaner = new Cleaner(whitelist);
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        Assert.assertTrue(isValid);
    }
    
    @Test
    public void testIsValid_invalidDocument() {
        // Invalid input document according to the whitelist
        String dirtyHTML = "<div><script>alert('Hello World');</script></div>";
        Document dirtyDocument = Jsoup.parse(dirtyHTML);
        
        Whitelist whitelist = new Whitelist();
        whitelist.addTags("div");
        
        Cleaner cleaner = new Cleaner(whitelist);
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        Assert.assertFalse(isValid);
    }
    
    @Test
    public void testCopySafeNodes_typicalCase() {
        // Typical case with nested elements and text nodes
        Element div = new Element(Tag.valueOf("div"), "");
        Element p = new Element(Tag.valueOf("p"), "");
        TextNode text = new TextNode("Hello World", "");
        
        div.appendChild(p);
        p.appendChild(text);
        
        Whitelist whitelist = new Whitelist();
        whitelist.addTags("div", "p");
        
        Cleaner cleaner = new Cleaner(whitelist);
        Element destination = new Element(Tag.valueOf("div"), "");
        int numDiscarded = cleaner.copySafeNodes(div, destination);
        
        Element expectedDestination = new Element(Tag.valueOf("div"), "");
        Element expectedP = new Element(Tag.valueOf("p"), "");
        TextNode expectedText = new TextNode("Hello World", "");
        
        expectedDestination.appendChild(expectedP);
        expectedP.appendChild(expectedText);
        
        Assert.assertEquals(expectedDestination, destination);
        Assert.assertEquals(0, numDiscarded);
    }
    
    @Test
    public void testCreateSafeElement_typicalCase() {
        // Typical case with attributes to be discarded and enforced attributes
        Element sourceDiv = new Element(Tag.valueOf("div"), "");
        sourceDiv.attributes().put("id", "main");
        sourceDiv.attributes().put("class", "content");
        sourceDiv.attributes().put("style", "color:red;");
        
        Attributes enforcedAttrs = new Attributes();
        enforcedAttrs.put("class", "important");
        enforcedAttrs.put("data-custom", "custom-value");
        
        Whitelist whitelist = new Whitelist();
        whitelist.addTags("div");
        whitelist.addAttributes("div", "id");
        whitelist.addEnforcedAttribute("div", "class", "important");
        whitelist.addEnforcedAttribute("div", "data-custom", "custom-value");
        
        Cleaner cleaner = new Cleaner(whitelist);
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceDiv);
        
        Element expectedDestDiv = new Element(Tag.valueOf("div"), "");
        expectedDestDiv.attributes().put("id", "main");
        expectedDestDiv.attributes().put("class", "important");
        expectedDestDiv.attributes().put("data-custom", "custom-value");
        
        Assert.assertEquals(expectedDestDiv, elementMeta.el);
        Assert.assertEquals(1, elementMeta.numAttribsDiscarded);
    }
}