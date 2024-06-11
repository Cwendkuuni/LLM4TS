import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

public class NullPropertyPointerTest {

 @Test
 public void testNullPropertyPointerCreation() {
 PropertyPointer parent = new PropertyOwnerPointer(new DOMNodePointer(null, 0));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertNull(nullPropertyPointer.getBaseValue());
 assertNull(nullPropertyPointer.getImmediateNode());
 assertSame(parent, nullPropertyPointer.getParentPointer());
 assertEquals("", nullPropertyPointer.asPath());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSettingValueOnNullPropertyPointer() {
 PropertyPointer parent = new PropertyOwnerPointer(new DOMNodePointer(null, 0));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setValue("Test Value");
 }

 @Test
 public void testCreatePathWithNullPropertyPointer() {
 JXPathContext context = new JXPathContext(new DOMNode());
 PropertyPointer parent = new PropertyOwnerPointer(new DOMNodePointer(context, 0));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 NodePointer child = nullPropertyPointer.createPath(context);
 assertNull(child.getBaseValue());
 assertNull(child.getImmediateNode());
 assertSame(context.getDocumentNode(), child.getParentPointer().getBaseValue());
 }

 @Test
 public void testCreatePathWithValueSetOnNullPropertyPointer() {
 JXPathContext context = new JXPathContext(new DOMNode());
 PropertyPointer parent = new PropertyOwnerPointer(new DOMNodePointer(context, 0));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setValue("Test Value");
 NodePointer child = nullPropertyPointer.createPath(context);
 assertNull(child.getBaseValue());
 assertNull(child.getImmediateNode());
 assertSame(context.getDocumentNode(), child.getParentPointer().getBaseValue());
 assertSame("Test Value", child.getValue());
 }

 @Test
 public void testCreatePathWithAttributeValueSetOnNullPropertyPointer() {
 JXPathContext context = new JXPathContext(new DOMNode());
 PropertyPointer parent = new PropertyOwnerPointer(new DOMNodePointer(context, 0));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setPropertyName("testAttribute");
 nullPropertyPointer.setValue("Test Value");
 NodePointer attributeNode = nullPropertyPointer.createPath(context);
 assertSame("testAttribute", attributeNode.getName().getLocalPart());
 assertSame("Test Value", attributeNode.getValue());
 }

 @Test
 public void testCreatePathWithIndexSetOnNullPropertyPointer() {
 JXPathContext context = new JXPathContext(new DOMNode());
 PropertyPointer parent = new PropertyOwnerPointer(new DOMNodePointer(context, 0));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 NodePointer collection = new ArrayNodePointer(context, new QName("array"), new DOMNode());
 nullPropertyPointer.setValue(collection);
 NodePointer child = nullPropertyPointer.createPath(context, 1);
 assertNull(child.getBaseValue());
 assertNull(child.getImmediateNode());
 assertSame(context.getDocumentNode(), child.getParentPointer().getBaseValue());
 assertSame(collection, child.getParentPointer());
 }

 @Test
 public void testSetPropertyNameOnNullPropertyPointer() {
 PropertyPointer parent = new PropertyOwnerPointer(new DOMNodePointer(null, 0));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setPropertyName("testProperty");
 assertSame("testProperty", nullPropertyPointer.getPropertyName().getLocalPart());
 }

 @Test
 public void testSetNameAttributeValueOnNullPropertyPointer() {
 PropertyPointer parent = new PropertyOwnerPointer(new DOMNodePointer(null, 0));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setNameAttributeValue("testAttribute");
 nullPropertyPointer.setPropertyName("testProperty");
 assertSame("testProperty", nullPropertyPointer.getPropertyName().getLocalPart());
 assertSame("testAttribute", nullPropertyPointer.asPath().substring(1, nullPropertyPointer.asPath().length() - 1));
}
}