
package org.jsoup.safety;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

import static org.junit.Assert.*;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testClean() {
        // Create a dirty document
        Document dirtyDocument = Document.createShell("http://example.com");
        Element body = dirtyDocument.body();
        Element div1 = new Element(Tag.valueOf("div"), dirtyDocument.baseUri());
        body.appendChild(div1);
        Element img = new Element(Tag.valueOf("img"), "http://example.com");
        img.attr("src", "image.jpg");
        div1.appendChild(img);
        
        // Create a cleaner with a whitelist
        String[] allowedTags = {"div", "img"};
        Whitelist whitelist = new Whitelist();
        whitelist.addTags(allowedTags);
        Cleaner cleaner = new Cleaner(whitelist);
        
        // Clean the dirty document
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        // Compare the cleaned document with the expected result
        assertEquals("<html><head></head><body><div><img src=\"image.jpg\"></div></body></html>", cleanDocument.outerHtml());
    }
    
    @Test
    public void testIsValid_validDocument() {
        // Create a valid document
        Document validDocument = Document.createShell("http://example.com");
        Element body = validDocument.body();
        Element div = new Element(Tag.valueOf("div"), validDocument.baseUri());
        body.appendChild(div);
        
        // Create a cleaner with a whitelist
        String[] allowedTags = {"div"};
        Whitelist whitelist = new Whitelist();
        whitelist.addTags(allowedTags);
        Cleaner cleaner = new Cleaner(whitelist);
        
        // Validate the valid document
        boolean isValid = cleaner.isValid(validDocument);
        
        // The valid document should be considered valid
        assertTrue(isValid);
    }
    
    @Test
    public void testIsValid_invalidDocument() {
        // Create an invalid document
        Document invalidDocument = Document.createShell("http://example.com");
        Element head = new Element(Tag.valueOf("head"), invalidDocument.baseUri());
        invalidDocument.body().prependChild(head);
        
        // Create a cleaner with a whitelist
        String[] allowedTags = {"div"};
        Whitelist whitelist = new Whitelist();
        whitelist.addTags(allowedTags);
        Cleaner cleaner = new Cleaner(whitelist);
        
        // Validate the invalid document
        boolean isValid = cleaner.isValid(invalidDocument);
        
        // The invalid document should be considered invalid
        assertFalse(isValid);
    }
}
