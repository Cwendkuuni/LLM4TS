package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;

import java.io.IOException;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationCreation() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        Assert.assertEquals("#declaration", xmlDeclaration.nodeName());
        Assert.assertEquals("xml", xmlDeclaration.name());
        Assert.assertFalse(xmlDeclaration.isProcessingInstruction());

        XmlDeclaration xmlPiDeclaration = new XmlDeclaration("<?xml", null, true);
        Assert.assertEquals("#declaration", xmlPiDeclaration.nodeName());
        Assert.assertNull(xmlPiDeclaration.name());
        Assert.assertTrue(xmlPiDeclaration.isProcessingInstruction());
    }

    @Test
    public void testGetWholeDeclaration_Default() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        Assert.assertEquals("xml", xmlDeclaration.getWholeDeclaration());

        XmlDeclaration xmlPiDeclaration = new XmlDeclaration("<?xml", null, true);
        Assert.assertEquals("<?xml>", xmlPiDeclaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclaration_VersionSpecified() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        XmlDeclaration xmlDeclarationWithVersion = new XmlDeclaration("xml", null, false, new Attributes(){{put("version", "1.0");}});
        Assert.assertEquals("xml version=\"1.0\"", xmlDeclarationWithVersion.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclaration_EncodingSpecified() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        XmlDeclaration xmlDeclarationWithEncoding = new XmlDeclaration("xml", null, false, new Attributes(){{put("encoding", "UTF-8");}});
        Assert.assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclarationWithEncoding.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclaration_BothVersionAndEncodingSpecified() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        XmlDeclaration xmlDeclarationWithVersionAndEncoding = new XmlDeclaration("xml", null, false, new Attributes(){{put("version", "1.0"); put("encoding", "UTF-8");}});
        Assert.assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclarationWithVersionAndEncoding.getWholeDeclaration());
    }

    @Test(expected = NullPointerException.class)
    public void testGetWholeDeclaration_NullName() {
        new XmlDeclaration(null, null, false);
    }

    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        Assert.assertEquals("<xml>", xmlDeclaration.toString());

        XmlDeclaration xmlPiDeclaration = new XmlDeclaration("<?xml", null, true);
        Assert.assertEquals("<?xml>", xmlPiDeclaration.toString());
    }
}