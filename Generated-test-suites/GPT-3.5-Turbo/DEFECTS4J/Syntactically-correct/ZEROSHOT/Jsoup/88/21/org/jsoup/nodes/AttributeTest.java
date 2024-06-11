import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.SerializationException;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

public class AttributeTest {

	@Test
	public void testAttributeConstructor() {
		Attribute attr = new Attribute("key", "value");
		assertEquals("key", attr.getKey());
		assertEquals("value", attr.getValue());
		assertNull(attr.parent);
	}

	@Test
	public void testAttributeSetKey() {
		Attribute attr = new Attribute("key", "value");
		attr.setKey("newKey");
		assertEquals("newKey", attr.getKey());
	}

	@Test
	public void testAttributeSetValue() {
		Attribute attr = new Attribute("key", "value");
		String oldVal = attr.setValue("newValue");
		assertEquals("value", oldVal);
		assertEquals("newValue", attr.getValue());
	}

	@Test
	public void testAttributeHtml() {
		Attribute attr = new Attribute("key", "value");
		assertEquals("key=\"value\"", attr.html());
	}

	@Test
	public void testAttributeToString() {
		Attribute attr = new Attribute("key", "value");
		assertEquals("key=\"value\"", attr.toString());
	}

	@Test
	public void testAttributeCreateFromEncoded() {
		Attribute attr = Attribute.createFromEncoded("key", "&#34;value&#34;");
		assertEquals("key", attr.getKey());
		assertEquals("\"value\"", attr.getValue());
		assertNull(attr.parent);
	}

	@Test
	public void testAttributeIsDataAttribute() {
		Attribute attr = new Attribute("data-key", "value");
		assertTrue(attr.isDataAttribute());
	}

	@Test
	public void testAttributeShouldCollapseAttribute() {
		Attribute attr = new Attribute("key", "value");
		assertFalse(attr.shouldCollapseAttribute(new Document.OutputSettings()));
	}

	@Test
	public void testAttributeIsBooleanAttribute() {
		Attribute attr = new Attribute("key", "value");
		assertFalse(attr.isBooleanAttribute());
	}

	@Test
	public void testAttributeEquals() {
		Attribute attr1 = new Attribute("key", "value");
		Attribute attr2 = new Attribute("key", "value");
		Attribute attr3 = new Attribute("key", "diffValue");
		assertTrue(attr1.equals(attr2));
		assertFalse(attr1.equals(attr3));
	}

	@Test
	public void testAttributeHashCode() {
		Attribute attr1 = new Attribute("key", "value");
		Attribute attr2 = new Attribute("key", "value");
		Attribute attr3 = new Attribute("key", "diffValue");
		assertEquals(attr1.hashCode(), attr2.hashCode());
		assertNotEquals(attr1.hashCode(), attr3.hashCode());
	}

	@Test
	public void testAttributeClone() {
		Attribute attr1 = new Attribute("key", "value");
		Attribute attr2 = attr1.clone();
		assertEquals(attr1.getKey(), attr2.getKey());
		assertEquals(attr1.getValue(), attr2.getValue());
	}

}