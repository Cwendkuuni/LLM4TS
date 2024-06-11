import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.jsoup.SerializationException;
import org.jsoup.helper.Validate;
import org.jsoup.internal.StringUtil;
import org.junit.Before;
import org.junit.Test;

public class AttributeTest {
	
	private Attribute attribute;
	private Attributes attributes;
	
	@Before
	public void setUp() {
		attribute = new Attribute("testKey", "testValue");
		attributes = new Attributes();
	}
	
	@Test
	public void testAttribute() {
		assertEquals("testKey", attribute.getKey());
		assertEquals("testValue", attribute.getValue());
		assertNull(attribute.parent);
	}

	@Test
	public void testSetKey() {
		attribute.setKey("testKey2");
		assertEquals("testKey2", attribute.getKey());
		
		attribute.parent = attributes;
		attribute.setKey("testKey3");
		assertEquals("testKey3", attribute.getKey());
		assertEquals(0, attributes.indexOfKey("testKey"));
		assertEquals(1, attributes.indexOfKey("testKey3"));
	}

	@Test
	public void testSetValue() {
		assertEquals("testValue", attribute.setValue("testValue2"));
		assertEquals("testValue2", attribute.getValue());
		
		attribute.parent = attributes;
		assertEquals("testValue2", attribute.setValue("testValue3"));
		assertEquals("testValue3", attribute.getValue());
		assertEquals("testValue", attributes.get("testKey"));
		assertEquals("testValue3", attributes.get("testKey3"));
	}

	@Test
	public void testHtml() {
		assertEquals("testKey=\"testValue\"", attribute.html());
		
		attribute.setValue("testValue&");
		assertEquals("testKey=\"testValue&amp;\"", attribute.html());
	}

	@Test
	public void testHtmlWithIOException() {
		attribute = new Attribute("testKey", "testValue") {
			@Override
			protected void html(Appendable accum, Document.OutputSettings out) throws IOException {
				throw new IOException("Test Exception");
			}
		};
		
		try {
			attribute.html();
		} catch (SerializationException e) {
			assertEquals("java.io.IOException: Test Exception", e.getMessage());
		}
	}

	@Test
	public void testToString() {
		assertEquals("testKey=\"testValue\"", attribute.toString());
	}

	@Test
	public void testCreateFromEncoded() {
		Attribute attr = Attribute.createFromEncoded("testKey", "testValue&amp;");
		assertEquals("testValue&", attr.getValue());
		assertNull(attr.parent);
	}

	@Test
	public void testIsDataAttribute() {
		assertFalse(attribute.isDataAttribute());
		
		attribute = new Attribute("data-testKey", "testValue");
		assertTrue(attribute.isDataAttribute());
	}

	@Test
	public void testShouldCollapseAttribute() {
		Attribute attr = new Attribute("testCollapse", "testCollapse");
		assertTrue(attr.shouldCollapseAttribute(new Document.OutputSettings()));
		
		attr.setValue("");
		assertTrue(attr.shouldCollapseAttribute(new Document.OutputSettings()));
		
		attr = new Attribute("autoFocus", "autoFocus");
		assertFalse(attr.shouldCollapseAttribute(new Document.OutputSettings()));
		
		attr.setValue("");
		assertTrue(attr.shouldCollapseAttribute(new Document.OutputSettings()));
	}

	@Test
	public void testIsBooleanAttribute() {
		assertFalse(attribute.isBooleanAttribute());
		
		attribute = new Attribute("isMap", null);
		assertTrue(attribute.isBooleanAttribute());
	}

	@Test
	public void testEquals() {
		assertTrue(attribute.equals(attribute));
		assertFalse(attribute.equals(null));
		assertFalse(attribute.equals(new Object()));
		
		Attribute attr = new Attribute("testKey", "testValue");
		assertTrue(attribute.equals(attr));
		
		attr = new Attribute("testKey2", "testValue");
		assertFalse(attribute.equals(attr));
		
		attr = new Attribute("testKey", "testValue2");
		assertFalse(attribute.equals(attr));
		
		attr = new Attribute("testKey", null);
		assertFalse(attribute.equals(attr));
	}

	@Test
	public void testHashCode() {
		assertEquals(attribute.hashCode(), attribute.hashCode());
		
		Attribute attr = new Attribute("testKey", "testValue");
		assertEquals(attribute.hashCode(), attr.hashCode());
		
		attr = new Attribute("testKey2", "testValue");
		assertNotEquals(attribute.hashCode(), attr.hashCode());
		
		attr = new Attribute("testKey", "testValue2");
		assertNotEquals(attribute.hashCode(), attr.hashCode());
		
		attr = new Attribute("testKey", null);
		assertNotEquals(attribute.hashCode(), attr.hashCode());
	}

	@Test
	public void testClone() {
		Attribute attr = attribute.clone();
		assertEquals(attribute.getKey(), attr.getKey());
		assertEquals(attribute.getValue(), attr.getValue());
		assertNotNull(attr.parent );
	}
}