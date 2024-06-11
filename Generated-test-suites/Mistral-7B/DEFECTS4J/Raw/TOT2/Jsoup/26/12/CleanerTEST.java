import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;

public class CleanerTest {
    @Test
    public void testCleanerConstructor() {
        Whitelist whitelist = Whitelist.simpleDefault();
        Cleaner cleaner = new Cleaner(whitelist);
        Assert.assertNotNull(cleaner);
    }

    @Test
    public void testCleanMethod() {
        Whitelist whitelist = Whitelist.simpleDefault();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Assert.assertEquals("<html><body><h1></h1></body></html>", cleanDocument.html());
    }

    @Test
    public void testIsValidMethod() {
        Whitelist whitelist = Whitelist.simpleDefault();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        Assert.assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodesMethod() {
        Whitelist whitelist = Whitelist.simpleDefault();
        Cleaner cleaner = new Cleaner(whitelist);
        Element source = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>").body();
        Element dest = new Element("div");
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        Assert.assertEquals(0, numDiscarded);
        Assert.assertEquals("<div><h1></h1></div>", dest.html());
    }

    @Test(expected = NullPointerException.class)
    public void testCleanerConstructorWithNullWhitelist() {
        Whitelist whitelist = null;
        Cleaner cleaner = new Cleaner(whitelist);
    }

    @Test(expected = NullPointerException.class)
    public void testCleanMethodWithNullDocument() {
        Cleaner cleaner = new Cleaner(Whitelist.simpleDefault());
        Document dirtyDocument = null;
        Document cleanDocument = cleaner.clean(dirtyDocument);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsValidMethodWithNullDocument() {
        Cleaner cleaner = new Cleaner(Whitelist.simpleDefault());
        Document dirtyDocument = null;
        cleaner.isValid(dirtyDocument);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCopySafeNodesMethodWithNullSource() {
        Cleaner cleaner = new Cleaner(Whitelist.simpleDefault());
        Element source = null;
        Element dest = new Element("div");
        cleaner.copySafeNodes(source, dest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsValidMethodWithMalformedHTML() {
        Whitelist whitelist = Whitelist.simpleDefault();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        Assert.assertFalse(isValid);
    }

    @Test
    public void testInvalidTag() {
        Whitelist whitelist = new Whitelist() {
            @Override
            public boolean isSafeTag(String tagName) {
                return tagName.equals("script") ? false : super.isSafeTag(tagName);
            }
        };
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Assert.assertEquals("<html><body></body></html>", cleanDocument.html());
    }

    @Test
    public void testInvalidAttribute() {
        Whitelist whitelist = new Whitelist() {
            @Override
            public boolean isSafeAttribute(String tagName, Element contextElement, Attribute attribute) {
                return attribute.attributeName().equals("onclick") ? false : super.isSafeAttribute(tagName, contextElement, attribute);
            }
        };
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><button onclick=\"alert('XSS')\">Click me</button></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Assert.assertEquals("<html><body><button></button></body></html>", cleanDocument.html());
    }

    @Test
    public void testEmptyWhitelist() {
        Cleaner cleaner = new Cleaner(new Whitelist());
        Document dirtyDocument = Jsoup.parse("<html><body></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Assert.assertEquals("<html><body></body></html>", cleanDocument.html());
    }

    @Test
    public void testCleanMethodWithEmptyInput() {
        Cleaner cleaner = new Cleaner(Whitelist.simpleDefault());
        Document dirtyDocument = Jsoup.parse("");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Assert.assertEquals("", cleanDocument.html());
    }

    @Test
    public void testCleanMethodWithNullInput() {
        Cleaner cleaner = new Cleaner(Whitelist.simpleDefault());
        Document dirtyDocument = null;
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Assert.assertNull(cleanDocument);
    }

    @Test
    public void testInvalidBaseURI() {
        Cleaner cleaner = new Cleaner(Whitelist.simpleDefault());
        Document dirtyDocument = Jsoup.parse("<html><body><script src=\"javascript:alert('XSS')\"></script></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Assert.assertEquals("<html><body></body></html>", cleanDocument.html());
    }
}