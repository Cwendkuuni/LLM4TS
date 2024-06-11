package org.jsoup.nodes;

import org.jsoup.helper.Validate;

import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;

public class XmlDeclarationTest {
    
    @Test
    public void testNodeName() {
        XmlDeclaration declaration = new XmlDeclaration("declaration", "baseUri", false);
        Assert.assertEquals("#declaration", declaration.nodeName());
    }
    
    @Test
    public void testName() {
        XmlDeclaration declaration = new XmlDeclaration("declaration", "baseUri", false);
        Assert.assertEquals("declaration", declaration.name());
    }
    
    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration declaration = new XmlDeclaration("declaration", "baseUri", false);
        Assert.assertEquals("declaration", declaration.getWholeDeclaration());
        
        declaration = new XmlDeclaration("xml", "baseUri", false);
        declaration.attributes.put("version", "1.0");
        declaration.attributes.put("encoding", "UTF-8");
        Assert.assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", declaration.getWholeDeclaration());
    }
    
    @Test
    public void testOuterHtmlHead() throws IOException {
        XmlDeclaration declaration = new XmlDeclaration("declaration", "baseUri", false);
        StringBuilder accum = new StringBuilder();
        declaration.outerHtmlHead(accum, 0, new Document.OutputSettings());
        Assert.assertEquals("<?declaration>", accum.toString());
        
        declaration = new XmlDeclaration("xml", "baseUri", false);
        declaration.attributes.put("version", "1.0");
        declaration.attributes.put("encoding", "UTF-8");
        accum = new StringBuilder();
        declaration.outerHtmlHead(accum, 0, new Document.OutputSettings());
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlTail() throws IOException {
        XmlDeclaration declaration = new XmlDeclaration("declaration", "baseUri", false);
        StringBuilder accum = new StringBuilder();
        declaration.outerHtmlTail(accum, 0, new Document.OutputSettings());
        Assert.assertEquals("", accum.toString());
    }
    
    @Test
    public void testToString() {
        XmlDeclaration declaration = new XmlDeclaration("declaration", "baseUri", false);
        Assert.assertEquals("<?declaration>", declaration.toString());
    }
}