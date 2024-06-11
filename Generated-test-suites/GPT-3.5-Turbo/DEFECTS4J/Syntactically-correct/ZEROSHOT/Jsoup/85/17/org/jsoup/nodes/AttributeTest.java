import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;

public class AttributeTest {
	
	private Attribute attribute;
	
	@Before
	public void setUp() {
		attribute = new Attribute("testKey", "testValue");
	}
	
	@Test
	public void testGetKey() {
		assertEquals("testKey", attribute.getKey());
	}
	
	@Test
	public void testSetKey() {
		attribute.setKey("newKey");
		assertEquals("newKey", attribute.getKey());
	}
	
	@Test
	public void testGetValue() {
		assertEquals("testValue", attribute.getValue());
	}
	
	@Test
	public void testSetValue() {
		attribute.setValue("newValue");
		assertEquals("newValue", attribute.getValue());
	}
	
	@Test
	public void testHtml() {
		assertEquals("testKey=\"testValue\"", attribute.html());
	}
	
	@Test
	public void testToString() {
		assertEquals("testKey=\"testValue\"", attribute.toString());
	}
	
	@Test
	public void testEquals() {
		Attribute other = new Attribute("testKey", "testValue");
		assertTrue(attribute.equals(other));
	}
	
	@Test
	public void testHashCode() {
		Attribute other = new Attribute("testKey", "testValue");
		assertEquals(attribute.hashCode(), other.hashCode());
	}
	
	@Test
	public void testClone() {
		Attribute clone = attribute.clone();
		assertEquals(attribute, clone);
	}
	
}