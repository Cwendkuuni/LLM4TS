package org.jsoup.safety;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
    private Cleaner cleaner;
    
    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.basic());
    }
    
    @Test
    public void testClean() {
        Document dirtyDocument = createDirtyDocument();
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        Element dirtyBody = dirtyDocument.body();
        Element cleanBody = cleanDocument.body();
        
        // Test that all unsafe elements have been removed
        assertTrue(cleanBody.getElementsByTag("script").isEmpty());
        assertTrue(cleanBody.getElementsByTag("iframe").isEmpty());
        assertTrue(cleanBody.getElementsByTag("object").isEmpty());
        assertTrue(cleanBody.getElementsByTag("embed").isEmpty());
        
        // Test that all safe elements have been kept
        assertFalse(cleanBody.getElementsByTag("h1").isEmpty());
        assertFalse(cleanBody.getElementsByTag("h2").isEmpty());
        assertFalse(cleanBody.getElementsByTag("p").isEmpty());
        assertFalse(cleanBody.getElementsByTag("a").isEmpty());
    }
    
    @Test
    public void testIsValid() {
        Document dirtyDocument = createDirtyDocument();
        boolean valid = cleaner.isValid(dirtyDocument);
        
        // Test that the document is considered invalid
        assertFalse(valid);
        
        Document cleanDocument = cleaner.clean(dirtyDocument);
        valid = cleaner.isValid(cleanDocument);
        
        // Test that the document is now considered valid after cleaning
        assertTrue(valid);
    }
    
    private Document createDirtyDocument() {
        Document dirtyDocument = new Document("");
        Element dirtyBody = new Element("body");
        
        Element script = new Element("script");
        dirtyBody.appendChild(script);
        
        Element iframe = new Element("iframe");
        dirtyBody.appendChild(iframe);
        
        Element object = new Element("object");
        dirtyBody.appendChild(object);
        
        Element embed = new Element("embed");
        dirtyBody.appendChild(embed);
        
        Element h1 = new Element("h1");
        dirtyBody.appendChild(h1);
        
        Element h2 = new Element("h2");
        dirtyBody.appendChild(h2);
        
        Element p = new Element("p");
        dirtyBody.appendChild(p);
        
        Element a = new Element("a");
        dirtyBody.appendChild(a);
        
        dirtyDocument.appendChild(dirtyBody);
        
        return dirtyDocument;
    }
}