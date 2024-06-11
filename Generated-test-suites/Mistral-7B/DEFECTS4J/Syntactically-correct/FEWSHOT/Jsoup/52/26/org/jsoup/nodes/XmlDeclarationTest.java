package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationWithNameAndBaseUri() {
        String name = "xml";
        String baseUri = "http://example.com/";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("<?xml?>", xmlDeclaration.getWholeDeclaration());
        assertEquals("< Declaration xml=\"http://example.com/\" xml:version=\"\">\n", xmlDeclaration.outerHtml());
    }

    @Test
    public void testXmlDeclarationWithNameAndBaseUriAndVersion() {
        String name = "xml";
        String baseUri = "http://example.com/";
        boolean isProcessingInstruction = false;
        String version = "1.1";

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction, version);

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("<?xml version=\"1.1\"?>", xmlDeclaration.getWholeDeclaration());
        assertEquals("< Declaration xml=\"http://example.com/\" xml:version=\"1.1\">\n", xmlDeclaration.outerHtml());
    }

    @Test
    public void testXmlDeclarationWithNameAndBaseUriAndEncoding() {
        String name = "xml";
        String baseUri = "http://example.com/";
        boolean isProcessingInstruction = false;
        String encoding = "UTF-8";

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction, null, encoding);

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("<?xml encoding=\"UTF-8\"?>", xmlDeclaration.getWholeDeclaration());
        assertEquals("< Declaration xml=\"http://example.com/\" encoding=\"UTF-8\">\n", xmlDeclaration.outerHtml());
    }

    @Test
    public void testXmlDeclarationWithNameAndBaseUriAndVersionAndEncoding() {
        String name = "xml";
        String baseUri = "http://example.com/";
        boolean isProcessingInstruction = false;
        String version = "1.1";
        String encoding = "UTF-8";

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction, version, encoding);

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("<?xml version=\"1.1\" encoding=\"UTF-8\"?>", xmlDeclaration.getWholeDeclaration());
        assertEquals("< Declaration xml=\"http://example.com/\" xml:version=\"1.1\" encoding=\"UTF-8\">\n", xmlDeclaration.outerHtml());
    }

    @Test
    public void testXmlDeclarationWithProcessingInstruction() {
        String name = null;
        String baseUri = null;
        boolean isProcessingInstruction = true;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertNull(xmlDeclaration.name());
        assertNull(xmlDeclaration.getWholeDeclaration());
        assertEquals("<??>", xmlDeclaration.outerHtml());
    }

    @Test(expected = NullPointerException.class)
    public void testNullName() {
        String baseUri = "http://example.com/";
        boolean isProcessingInstruction = false;
        String name = null;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        // This test should throw a NullPointerException because name is null.
    }

	@Test
	public void testToString() {
		String name = "xml";
		String baseUri = "http://example.com/";
		boolean isProcessingInstruction = false;

		XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
		assertEquals("<declaration xml=\"http://example.com/\">\n" +
				"<![CDATA[\n" +
				"<?xml>\n" +
				"]]>\n</declaration>", xmlDeclaration.toString());
	}
}