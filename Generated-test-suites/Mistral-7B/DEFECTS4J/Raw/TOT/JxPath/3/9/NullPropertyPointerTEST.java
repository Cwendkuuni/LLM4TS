import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyOwnerPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

 @Test
 public void testNullPropertyPointerCreation() {
 PropertyOwnerPointer parent = new PropertyOwnerPointer(new NodePointerMock());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertEquals("Expected null property pointer to have null parent", null, nullPropertyPointer.getParent());
 assertEquals("Expected null property pointer to have empty name", "*", nullPropertyPointer.getPropertyName());
 assertEquals("Expected null property pointer to be a container", true, nullPropertyPointer.isContainer());
 assertEquals("Expected null property pointer to have length 0", 0, nullPropertyPointer.getLength());
 assertNull("Expected null property pointer to have null base value", nullPropertyPointer.getBaseValue());
 assertNull("Expected null property pointer to have null immediate node", nullPropertyPointer.getImmediateNode());
 }

 @Test
 public void testNullPropertyPointerSetValue() {
 PropertyOwnerPointer parent = new PropertyOwnerPointer(new NodePointerMock());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 try {
 nullPropertyPointer.setValue("Test value");
 fail("Expected JXPathInvalidAccessException to be thrown");
 } catch (JXPathInvalidAccessException e) {
 // Expected
 }
 }

 @Test
 public void testNullPropertyPointerCreatePath() {
 JXPathContext context = new JXPathContext(new NodePointerMock());
 PropertyOwnerPointer parent = new PropertyOwnerPointer(new NodePointerMock());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 NodePointer result = nullPropertyPointer.createPath(context);
 assertNull("Expected null result", result);
 }

 @Test
 public void testNullPropertyPointerCreatePathWithValue() {
 JXPathContext context = new JXPathContext(new NodePointerMock());
 PropertyOwnerPointer parent = new PropertyOwnerPointer(new NodePointerMock());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 NodePointer result = nullPropertyPointer.createPath(context, "Test value");
 assertNull("Expected null result", result);
 }

 @Test
 public void testNullPropertyPointerCreateChild() {
 JXPathContext context = new JXPathContext(new NodePointerMock());
 PropertyOwnerPointer parent = new PropertyOwnerPointer(new NodePointerMock());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 NodePointer child = nullPropertyPointer.createChild(context, new QName("test:child"), 0);
 assertNull("Expected null child", child);
 }

 @Test
 public void testNullPropertyPointerCreateChildWithValue() {
 JXPathContext context = new JXPathContext(new NodePointerMock());
 PropertyOwnerPointer parent = new PropertyOwnerPointer(new NodePointerMock());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 NodePointer child = nullPropertyPointer.createChild(context, new QName("test:child"), 0, "Test value");
 assertNull("Expected null child", child);
 }

 @Test
 public void testNullPropertyPointerIsActualProperty() {
 PropertyOwnerPointer parent = new PropertyOwnerPointer(new NodePointerMock());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertFalse("Expected null property pointer to not be an actual property", nullPropertyPointer.isActualProperty());
 }

 @Test
 public void testNullPropertyPointerIsActual() {
 PropertyOwnerPointer parent = new PropertyOwnerPointer(new NodePointerMock());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertFalse("Expected null property pointer to not be actual", nullPropertyPointer.isActual());
 }

 @Test
 public void testNullPropertyPointerIsLeaf() {
 PropertyOwnerPointer parent = new PropertyOwnerPointer(new NodePointerMock());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertTrue("Expected null property pointer to be a leaf", nullPropertyPointer.isLeaf());
 }

 @Test
 public void testNullPropertyPointerHasValuePointer() {
 PropertyOwnerPointer parent = new PropertyOwnerPointer(new NodePointerMock());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 NullPointer valuePointer = nullPropertyPointer.getValuePointer();
 assertNotNull("Expected null property pointer to have a value pointer", valuePointer);
 assertEquals("Expected value pointer to have null parent", nullPropertyPointer, valuePointer.getParent());
 assertEquals("Expected value pointer to have name null:property", new QName("null:property"), valuePointer.getName());
 }

 @Test
 public void testNullPropertyPointerWithAttributeName() {
 PropertyOwnerPointer parent = new PropertyOwnerPointer(new NodePointerMock());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setPropertyName("test:name");
 nullPropertyPointer.setNameAttributeValue("test:attribute");
 assertEquals("Expected name attribute value to be test:attribute", "test:attribute", nullPropertyPointer.getPropertyName());
 assertEquals("Expected name to be [@name='test:attribute']", "[@name='test:attribute']", nullPropertyPointer.asPath());
 }
}

class NodePointerMock implements NodePointer {
 @Override
 public NodePointer createPath(JXPathContext context) {
 return null;
 }

 @Override
 public NodePointer createPath(JXPathContext context, Object value) {
 return null;
 }

 @Override
 public NodePointer createChild(JXPathContext context, QName name, int index) {
 return null;
 }

 @Override
 public NodePointer createChild(JXPathContext context, QName name, int index, Object value) {
 return null;
 }

 @Override
 public String asPath() {
 return "";
 }

 @Override
 public NodePointer getParent() {
 return null;
 }

 @Override
 public Object getValue() {
 return null;
 }

 @Override
 public int getIndex() {
 return 0;
 }

 @Override
 public int getLength() {
 return 0;
 }

 @Override
 public boolean isContainer() {
 return false;
 }

 @Override
 public void setValue(Object value) {
}

 @Override
 public void setIndex(int index) {
}

 @Override
 public void setName(QName name) {
}

 @Override
 public void setParent(NodePointer parent) {
}
}