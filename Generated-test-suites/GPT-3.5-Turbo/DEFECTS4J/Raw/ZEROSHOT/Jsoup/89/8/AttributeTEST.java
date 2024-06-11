import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeTest {

private Attribute attribute;
private Attributes parent;

@Before
public void setUp() {
parent = new Attributes();
parent.put("test", "value");
attribute = new Attribute("key", "value", parent);
}

@Test
public void testGetKey() {
assertEquals("key", attribute.getKey());
}

@Test
public void testSetKey() {
attribute.setKey("newKey");
assertEquals("newKey", attribute.getKey());
}

@Test
public void testGetValue() {
assertEquals("value", attribute.getValue());
}

@Test
public void testSetValue() {
assertEquals("value", attribute.setValue("newValue"));
assertEquals("newValue", attribute.getValue());
}

@Test
public void testHtml() {
assertEquals("key=\"value\"", attribute.html());
}

@Test
public void testToString() {
assertEquals("key=\"value\"", attribute.toString());
}

@Test
public void testCreateFromEncoded() {
Attribute attr = Attribute.createFromEncoded("key", "value");
assertEquals("key", attr.getKey());
assertEquals("value", attr.getValue());
}

@Test
public void testIsDataAttribute() {
assertTrue(attribute.isDataAttribute());
}

@Test
public void testShouldCollapseAttribute() {
Document.OutputSettings out = new Document.OutputSettings();
out.syntax(Document.OutputSettings.Syntax.html);
assertTrue(attribute.shouldCollapseAttribute(out));
}

@Test
public void testIsBooleanAttribute() {
assertFalse(attribute.isBooleanAttribute());
}

@Test
public void testEquals() {
Attribute equalAttribute = new Attribute("key", "value", parent);
Attribute differentAttribute = new Attribute("differentKey", "differentValue", parent);
assertEquals(attribute, equalAttribute);
assertNotEquals(attribute, differentAttribute);
}

@Test
public void testHashCode() {
Attribute equalAttribute = new Attribute("key", "value", parent);
Attribute differentAttribute = new Attribute("differentKey", "differentValue", parent);
assertEquals(attribute.hashCode(), equalAttribute.hashCode());
assertNotEquals(attribute.hashCode(), differentAttribute.hashCode());
}

@Test
public void testClone() {
Attribute clonedAttribute = attribute.clone();
assertEquals(attribute, clonedAttribute);
assertNotSame(attribute, clonedAttribute);
}

}