import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import static org.junit.Assert.*;

public class W3CDomTest {
	
	@Test
	public void testFromJsoup() {
		W3CDom w3CDom = new W3CDom();
		org.jsoup.nodes.Document in = new org.jsoup.nodes.Document("");
		Document out = w3CDom.fromJsoup(in);
		
		assertNotNull(out);
	}
	
	@Test
	public void testConvert() {
		W3CDom w3CDom = new W3CDom();
		org.jsoup.nodes.Document in = new org.jsoup.nodes.Document("");
		Document out = w3CDom.fromJsoup(in);
		
		w3CDom.convert(in, out);
		
		// Check if the document has been converted correctly
		assertNotNull(out);
		assertEquals(in.location(), out.getDocumentURI());
		org.jsoup.nodes.Element rootEl = in.child(0);
		Element convertedRootEl = (Element) out.getFirstChild();
		assertEquals(rootEl.tagName(), convertedRootEl.getTagName());
	}
	
	@Test
	public void testAsString() {
		W3CDom w3CDom = new W3CDom();
		org.jsoup.nodes.Document in = new org.jsoup.nodes.Document("");
		Document document = w3CDom.fromJsoup(in);
		
		String result = w3CDom.asString(out);
		
		assertNotNull(result);
	}
	
}