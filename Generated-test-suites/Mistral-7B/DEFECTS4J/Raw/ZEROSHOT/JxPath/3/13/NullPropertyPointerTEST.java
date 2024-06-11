import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.QName;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyOwnerPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

 @Test
 public void testCreateNullPropertyPointer() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertNull(nullPropertyPointer.getImmediateNode());
 assertFalse(nullPropertyPointer.isActual());
 assertFalse(nullPropertyPointer.isActualProperty());
 assertTrue(nullPropertyPointer.isContainer());
 assertFalse(nullPropertyPointer.isLeaf());
 assertEquals(0, nullPropertyPointer.getLength());
 assertTrue(nullPropertyPointer.isCollection());
 assertEquals(0, nullPropertyPointer.getPropertyCount());
 assertArrayEquals(new String[0], nullPropertyPointer.getPropertyNames());
 assertEquals("", nullPropertyPointer.getPropertyName());
 assertNull(nullPropertyPointer.getValue());
 assertNull(nullPropertyPointer.getValuePointer());
 }

 @Test
 public void testCreateNullPropertyPointerWithParent() {
 NodePointer parent = new PropertyOwnerPointer(null);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertSame(parent, nullPropertyPointer.getParentPointer());
 assertNull(nullPropertyPointer.getImmediateNode());
 assertFalse(nullPropertyPointer.isActual());
 assertFalse(nullPropertyPointer.isActualProperty());
 assertTrue(nullPropertyPointer.isContainer());
 assertFalse(nullPropertyPointer.isLeaf());
 assertEquals(0, nullPropertyPointer.getLength());
 assertTrue(nullPropertyPointer.isCollection());
 assertEquals(0, nullPropertyPointer.getPropertyCount());
 assertArrayEquals(new String[0], nullPropertyPointer.getPropertyNames());
 assertEquals("", nullPropertyPointer.getPropertyName());
 assertNull(nullPropertyPointer.getValue());
 assertNull(nullPropertyPointer.getValuePointer());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetValueNullPropertyPointer() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setValue("test");
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetValueWithParentNullPropertyPointer() {
 NodePointer parent = new PropertyOwnerPointer(null);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setValue("test");
 }

 @Test
 public void testCreatePathNullPropertyPointer() {
 JXPathContext context = new JXPathContext();
 NodePointer expected = new PropertyOwnerPointer(null);
 NodePointer actual = new NullPropertyPointer(null).createPath(context);
 assertSame(expected, actual);
 }

 @Test
 public void testCreatePathNullPropertyPointerWithValue() {
 JXPathContext context = new JXPathContext();
 NodePointer expected = new PropertyOwnerPointer(null);
 NodePointer actual = new NullPropertyPointer(null).createPath(context, "test");
 assertSame(expected, actual);
 }

 @Test
 public void testCreatePathWithParentNullPropertyPointer() {
 JXPathContext context = new JXPathContext();
 NodePointer parent = new PropertyOwnerPointer(null);
 PropertyOwnerPointer expected = new PropertyOwnerPointer(parent);
 NodePointer actual = new NullPropertyPointer(parent).createPath(context);
 assertSame(expected, actual);
 }

 @Test
 public void testCreateChildNullPropertyPointer() {
 JXPathContext context = new JXPathContext();
 NodePointer parent = new PropertyOwnerPointer(null);
 NodePointer expected = new PropertyOwnerPointer(parent);
 NodePointer actual = new NullPropertyPointer(parent).createChild(context, new QName("test"));
 assertSame(expected, actual);
 }

 @Test
 public void testCreateChildNullPropertyPointerWithValue() {
 JXPathContext context = new JXPathContext();
 NodePointer parent = new PropertyOwnerPointer(null);
 NodePointer expected = new PropertyOwnerPointer(parent);
 NodePointer actual = new NullPropertyPointer(parent).createChild(context, new QName("test"), 0, "test");
 assertSame(expected, actual);
 }

 @Test
 public void testSetPropertyNameNullPropertyPointer() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setPropertyName("test");
 assertEquals("test", nullPropertyPointer.getPropertyName());
 }

 @Test
 public void testSetNameAttributeValueNullPropertyPointer() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setNameAttributeValue("test");
 assertEquals("test", nullPropertyPointer.getPropertyName());
 assertTrue(nullPropertyPointer.isByNameAttribute());
 }

 @Test
 public void testIsCollectionNullPropertyPointer() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertTrue(nullPropertyPointer.isCollection());
 }

 @Test
 public void testGetPropertyCountNullPropertyPointer() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertEquals(0, nullPropertyPointer.getPropertyCount());
 }

 @Test
 public void testGetPropertyNamesNullPropertyPointer() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertArrayEquals(new String[0], nullPropertyPointer.getPropertyNames());
 }

 @Test
 public void testAsPathNullPropertyPointer() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertEquals("", nullPropertyPointer.asPath());
 }

 @Test
 public void testAsPathNullPropertyPointerByNameAttribute() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setNameAttributeValue("test");
 assertEquals("[@name='test']", nullPropertyPointer.asPath());
 }

 @Test
 public void testEscapeString() {
 String test = "Test <special> 'characters' with \"quotes\"";
 String escaped = new NullPropertyPointer().escape(test);
 String expected = "Test &lt;special&gt; &apos;&#39;characters&quot;";
 assertEquals(expected, escaped);
 }
}