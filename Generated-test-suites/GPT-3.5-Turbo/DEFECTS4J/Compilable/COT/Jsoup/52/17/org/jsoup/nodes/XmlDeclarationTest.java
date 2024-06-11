package org.jsoup.nodes;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.helper.Validate;

import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {
    
    private XmlDeclaration xmlDeclaration;
    private String baseUri = "http://example.com";
    
    @BeforeClass
    public static void setUpClass() {
        // Add necessary setup code here
    }
    
    @AfterClass
    public static void tearDownClass() {
        // Add necessary cleanup code here
    }
    
    @Before
    public void setUp() {
        xmlDeclaration = new XmlDeclaration("xml", baseUri, false);
    }
    
    @After
    public void tearDown() {
        // Add necessary cleanup code here
    }

    @Test
    public void testXmlDeclaration_name() {
        assertEquals("xml", xmlDeclaration.name());
    }

    @Test
    public void testXmlDeclaration_getWholeDeclaration() {
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.getWholeDeclaration());
    }
    
    @Test
    public void testXmlDeclaration_nodeName() {
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }
    
    @Test
    public void testXmlDeclaration_toString() {
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.toString());
    }
}