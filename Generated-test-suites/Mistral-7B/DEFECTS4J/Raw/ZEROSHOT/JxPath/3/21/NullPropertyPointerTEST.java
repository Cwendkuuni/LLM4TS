import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyOwnerPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

 @Test
 public void testGetName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 QName name = nullPropertyPointer.getName();
 assertEquals("*", name.getLocalPart());
 }

 @Test
 public void testSetPropertyIndex() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setPropertyIndex(1);
 assertEquals(1, nullPropertyPointer.getIndex());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetLength() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.getLength();
 }

 @Test
 public void testGetBaseValue() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertNull(nullPropertyPointer.getBaseValue());
 }

 @Test
 public void testGetImmediateNode() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertNull(nullPropertyPointer.getImmediateNode());
 }

 @Test
 public void testIsLeaf() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertTrue(nullPropertyPointer.isLeaf());
 }

 @Test
 public void testGetValuePointer() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 NodePointer nodePointer = nullPropertyPointer.getValuePointer();
 assertNotNull(nodePointer);
 assertEquals(NullPointer.class, nodePointer.getClass());
 }

 @Test
 public void testIsActualProperty() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertFalse(nullPropertyPointer.isActualProperty());
 }

 @Test
 public void testIsActual() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertFalse(nullPropertyPointer.isActual());
 }

 @Test
 public void testIsContainer() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertTrue(nullPropertyPointer.isContainer());
 }

 @Test
 public void testSetValue() {
 PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(new Object()[0]);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
 String value = "testValue";
 nullPropertyPointer.setValue(value);
 assertEquals(value, propertyOwnerPointer.getPropertyValue("*"));
 }

 @Test
 public void testCreatePath() {
 JXPathContext context = new JXPathContext(new Object[]{null});
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 NodePointer nodePointer = nullPropertyPointer.createPath(context);
 assertNull(nodePointer);
 }

 @Test
 public void testCreatePathWithValue() {
 JXPathContext context = new JXPathContext(new Object[]{null});
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 Object value = new Object();
 NodePointer nodePointer = nullPropertyPointer.createPath(context, value);
 assertNull(nodePointer);
 }

 @Test
 public void testCreateChild() {
 JXPathContext context = new JXPathContext(new Object[]{null});
 PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(new Object[0]);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
 NodePointer nodePointer = nullPropertyPointer.createChild(context, new QName("child"), 0);
 assertNull(nodePointer);
 }

 @Test
 public void testCreateChildWithValue() {
 JXPathContext context = new JXPathContext(new Object[]{null});
 PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(new Object[0]);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
 NodePointer nodePointer = nullPropertyPointer.createChild(context, new QName("child"), 0, new Object());
 assertNull(nodePointer);
 }

 @Test
 public void testGetPropertyName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setPropertyName("testPropertyName");
 assertEquals("testPropertyName", nullPropertyPointer.getPropertyName());
 }

 @Test
 public void testSetNameAttributeValue() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setNameAttributeValue("testPropertyName");
 assertEquals("testPropertyName", nullPropertyPointer.getPropertyName());
 assertTrue(nullPropertyPointer.isByNameAttribute());
 }

 @Test
 public void testIsCollection() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertFalse(nullPropertyPointer.isCollection());
 }

 @Test
 public void testGetPropertyCount() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertEquals(0, nullPropertyPointer.getPropertyCount());
 }

 @Test
 public void testGetPropertyNames() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 String[] propertyNames = nullPropertyPointer.getPropertyNames();
 assertArrayEquals(new String[0], propertyNames);
 }

 @Test
 public void testAsPath() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setPropertyName("testPropertyName");
 nullPropertyPointer.setNameAttributeValue("testAttributeName");
 nullPropertyPointer.setIndex(1);
 String expectedPath = "[@name='testAttributeName']";
 assertEquals(expectedPath, nullPropertyPointer.asPath());
 }

 @Test
 public void testEscape() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 String string = "testStringWithQuotes: 'single\'quote and \"double\"quote";
 String escapedString = nullPropertyPointer.escape(string);
 String expectedEscapedString =
 "testStringWithQuotes: &apos;single&apos;quote and &quot;double&quot;quote";
 assertEquals(expectedEscapedString, escapedString);
 }
}