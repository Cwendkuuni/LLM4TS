import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.Document;

import static org.junit.Assert.*;
import org.junit.Test;

public class XmlDeclarationTest {

	@Test
	public void testXmlDeclarationName() {
		XmlDeclaration declaration = new XmlDeclaration("xml", "example.com", true);
		assertEquals("xml", declaration.name());
	}

	@Test
	public void testGetWholeDeclaration() {
		XmlDeclaration declaration = new XmlDeclaration("xml", "example.com", true);
		assertEquals("xml", declaration.getWholeDeclaration());

		declaration.attributes().put("version", "1.0");
		assertEquals("xml version=\"1.0\"", declaration.getWholeDeclaration());

		declaration.attributes().put("encoding", "UTF-8");
		assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", declaration.getWholeDeclaration());
	}

	@Test
	public void testOuterHtmlHead() {
		XmlDeclaration declaration = new XmlDeclaration("xml", "example.com", true);
		StringBuilder sb = new StringBuilder();
		try {
			declaration.outerHtmlHead(sb, 0, new Document.OutputSettings());
		} catch (IOException e) {
			fail("IOException should not be thrown");
		}

		assertEquals("<!xml>", sb.toString());
	}

	@Test
	public void testToString() {
		XmlDeclaration declaration = new XmlDeclaration("xml", "example.com", true);
		assertEquals("<!xml>", declaration.toString());
	}
}