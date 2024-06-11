package com.jstevh.viewer;

import org.junit.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import org.xml.sax.helpers.*;
import java.util.*;

public class SAXDirParserTest {

    @Test
    public void testGetWebData() throws Exception {
        SAXDirParser saxDirParser = new SAXDirParser();
        Assert.assertEquals("", saxDirParser.getWebData());
    }

    @Test
    public void testStartElement() throws Exception {
        SAXDirParser saxDirParser = new SAXDirParser();
        Attributes attributes = new AttributesImpl();
        saxDirParser.startElement("", "", "BrowserLoc", attributes);
        Assert.assertTrue(saxDirParser.check);
        Assert.assertEquals(0, saxDirParser.level);
    }

    @Test
    public void testCharacters() throws Exception {
        SAXDirParser saxDirParser = new SAXDirParser();
        char[] charArray = new char[5];
        charArray[0] = 't';
        charArray[1] = 'e';
        charArray[2] = 's';
        charArray[3] = 't';
        charArray[4] = '1';
        saxDirParser.check = true;
        saxDirParser.level = 0;
        saxDirParser.characters(charArray, 0, 5);
        Assert.assertEquals("test1", SAXDirParser.localBrowser);
        Assert.assertFalse(saxDirParser.check);
        Assert.assertEquals(-1, saxDirParser.level);
    }

    @Test
    public void testEndElement() throws Exception {
        SAXDirParser saxDirParser = new SAXDirParser();
        saxDirParser.check = true;
        saxDirParser.endElement("", "", "Group");
        Assert.assertFalse(saxDirParser.check);
    }

    @Test
    public void testGetDirectory() throws Exception {
        SAXDirParser saxDirParser = new SAXDirParser();
        Assert.assertEquals(new HashMap<String, String>(), saxDirParser.getDirectory());
    }

    @Test
    public void testGetLocalPackages() throws Exception {
        SAXDirParser saxDirParser = new SAXDirParser();
        Assert.assertNull(saxDirParser.getLocalPackages());
    }

    @Test
    public void testGetLocalBrowser() throws Exception {
        SAXDirParser saxDirParser = new SAXDirParser();
        Assert.assertEquals("", saxDirParser.getLocalBrowser());
    }

    @Test
    public void testGetEditor() throws Exception {
        SAXDirParser saxDirParser = new SAXDirParser();
        Assert.assertEquals("", saxDirParser.getEditor());
    }

    @Test
    public void testAcceptsLineNumber() throws Exception {
        SAXDirParser saxDirParser = new SAXDirParser();
        Assert.assertFalse(saxDirParser.acceptsLineNumber());
    }

    @Test
    public void testLineNumberParameter() throws Exception {
        SAXDirParser saxDirParser = new SAXDirParser();
        Assert.assertEquals("", saxDirParser.lineNumberParameter());
    }

    public void testMain() throws Exception {
        SAXDirParser.main(new String[]{});
    }
}