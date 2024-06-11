import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class PropertyPointerTest {

 @Test
 public void testPropertyPointerCreation() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public int getPropertyCount() {
 return 1;
 }

 @Override
 public String[] getPropertyNames() {
 return new String[]{"testProperty"};
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }
 };

 assertEquals("testProperty", propertyPointer.getName().getLocalPart());
 assertFalse(propertyPointer.isCollection());
 assertNull(propertyPointer.getValue());
 assertSame(propertyPointer.getParentPointer(), PropertyPointer.WHOLE_COLLECTION);
 }

 @Test
 public void testSetPropertyIndex() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public int getPropertyCount() {
 return 1;
 }

 @Override
 public String[] getPropertyNames() {
 return new String[]{"testProperty"};
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }
 };

 propertyPointer.setPropertyIndex(0);
 assertEquals(0, propertyPointer.getPropertyIndex());

 PropertyPointer propertyPointer2 = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public int getPropertyCount() {
 return 1;
 }

 @Override
 public String[] getPropertyNames() {
 return new String[]{"testProperty"};
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }
 };
 propertyPointer2.setPropertyIndex(1);
 assertEquals(1, propertyPointer2.getPropertyIndex());

 propertyPointer.setPropertyIndex(1);
 assertEquals(1, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testGetBean() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public int getPropertyCount() {
 return 1;
 }

 @Override
 public String[] getPropertyNames() {
 return new String[]{"testProperty"};
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }

 @Override
 public Object getBean() {
 return new Object() {
 };
 }
 };

 assertNotNull(propertyPointer.getBean());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetBeanTwice() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public int getPropertyCount() {
 return 1;
 }

 @Override
 public String[] getPropertyNames() {
 return new String[]{"testProperty"};
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }

 @Override
 public Object getBean() {
 return new Object() {
 };
 }
 };

 propertyPointer.getBean();
 propertyPointer.getBean();
 }

 @Test
 public void testGetName() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public int getPropertyCount() {
 return 1;
 }

 @Override
 public String[] getPropertyNames() {
 return new String[]{"testProperty"};
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }
 };

 assertEquals("testProperty", propertyPointer.getName().getLocalPart());
 }

 @Test
 public void testSetPropertyName() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public int getPropertyCount() {
 return 1;
 }

 @Override
 public String[] getPropertyNames() {
 return new String[]{"testProperty"};
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }
 };

 propertyPointer.setPropertyName("testProperty2");
 assertEquals("testProperty2", propertyPointer.getName().getLocalPart());
 }

 @Test
 public void testGetPropertyCount() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public int getPropertyCount() {
 return 1;
 }

 @Override
 public String[] getPropertyNames() {
 return new String[]{"testProperty"};
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }
 };

 assertEquals(1, propertyPointer.getPropertyCount());
 }

 @Test
 public void testGetPropertyNames() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public int getPropertyCount() {
 return 1;
 }

 @Override
 public String[] getPropertyNames() {
 return new String[]{"testProperty"};
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }
 };

 String[] propertyNames = propertyPointer.getPropertyNames();
 assertEquals(1, propertyNames.length);
 assertEquals("testProperty", propertyNames[0]);
 }

 @Test
 public void testIsActualProperty() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public int getPropertyCount() {
 return 1;
 }

 @Override
 public String[] getPropertyNames() {
 return new String[]{"testProperty"};
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }
 };

 assertTrue(propertyPointer.isActualProperty());
 }

 @Test
 public void testGetImmediateNode() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public int getPropertyCount() {
 return 1;
 }

 @Override
 public String[] getPropertyNames() {
 return new String[]{"testProperty"};
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }

 @Override
 public Object getBaseValue() {
 return new String[]{"testValue"};
 }

 @Override
 public int getPropertyIndex() {
 return 0;
 }
 };

 assertEquals("testValue", propertyPointer.getImmediateNode());
 }

 @Test
 public void testIsCollection() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public int getPropertyCount() {
 return 1;
 }

 @Override
 public String[] getPropertyNames() {
 return new String[]{"testProperty"};
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }

 @Override
 public Object getBaseValue() {
 return new String[]{"testValue1", "testValue2"};
 }

 @Override
 public int getPropertyIndex() {
 return 0;
 }
 };

 assertTrue(propertyPointer.isCollection());
 }

 @Test
 public void testGetLength() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public int getPropertyCount() {
 return 1;
 }

 @Override
 public String[] getPropertyNames() {
 return new String[]{"testProperty"};
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }

 @Override
 public Object getBaseValue() {
 return new String[]{"testValue1", "testValue2"};
 }

 @Override
 public int getPropertyIndex() {
 return 0;
 }
 };

 assertEquals(2, propertyPointer.getLength());
 }

 @Test
 public void testGetImmediateValuePointer() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public int getPropertyCount() {
 return 1;
 }

 @Override
 public String[] getPropertyNames() {
 return new String[]{"testProperty"};
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }

 @Override
 public Object getBaseValue() {
 return new String[]{"testValue1", "testValue2"};
 }

 @Override
 public int getPropertyIndex() {
 return 0;
 }
 };

 NodePointer immediateValuePointer = propertyPointer.getImmediateValuePointer();
 assertEquals(propertyPointer, immediateValuePointer.getParentPointer());
 assertEquals("testProperty", immediateValuePointer.getNodeName().getLocalPart());
 assertEquals("testValue1", immediateValuePointer.getNode().toString());
 }

 @Test
 public void testCreatePath() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public int getPropertyCount() {
 return 1;
 }

 @Override
 public String[] getPropertyNames() {
 return new String[]{"testProperty"};
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }

 @Override
 public Object getBaseValue() {
 return new String[]{"testValue1", "testValue2"};
 }

 @Override
 public int getPropertyIndex() {
 return 0;
 }
 };

 JXPathContext context = JXPathContext.newContext(new Object());
 NodePointer nodePointer = propertyPointer.createPath(context);
 assertEquals(propertyPointer, nodePointer.getParentPointer());
 assertEquals("testProperty", nodePointer.getNodeName().getLocalPart());
 assertEquals("testValue1", nodePointer.getNode().toString());
 }

 @Test
 public void testCreatePathWithValue() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public int getPropertyCount() {
 return 1;
 }

 @Override
 public String[] getPropertyNames() {
 return new String[]{"testProperty"};
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }

 @Override
 public Object getBaseValue() {
 return new String[]{"testValue1", "testValue2"};
 }

 @Override
 public int getPropertyIndex() {
 return 0;
 }
 };

 JXPathContext context = JXPathContext.newContext(new Object());
 NodePointer nodePointer = propertyPointer.createPath(context, "testValue2");
 assertEquals(propertyPointer, nodePointer.getParentPointer());
 assertEquals("testProperty", nodePointer.getNodeName().getLocalPart());
 assertEquals("testValue2", nodePointer.getNode().toString());
 }

 @Test
 public void testCreateChild() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public int getPropertyCount() {
 return 1;
 }

 @Override
 public String[] getPropertyNames() {
 return new String[]{"testProperty"};
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }

 @Override
 public Object getBaseValue() {
 return new String[]{"testValue1", "testValue2"};
 }

 @Override
 public int getPropertyIndex() {
 return 0;
 }
 };

 JXPathContext context = JXPathContext.newContext(new Object());
 NodePointer childNodePointer = propertyPointer.createChild(context, null, 1, "testChild");
 assertEquals(propertyPointer, childNodePointer.getParentPointer());
 assertEquals("testProperty", childNodePointer.getNodeName().getLocalPart());
 assertEquals(1, childNodePointer.getIndex());
 assertEquals(null, childNodePointer.getNode());
 }

 @Test
 public void testCreateChildWithName() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public int getPropertyCount() {
 return 1;
 }

 @Override
 public String[] getPropertyNames() {
 return new String[]{"testProperty"};
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }

 @Override
 public Object getBaseValue() {
 return new String[]{"testValue1", "testValue2"};
 }

 @Override
 public int getPropertyIndex() {
 return 0;
 }
 };

 JXPathContext context = JXPathContext.newContext(new Object());
 NodePointer childNodePointer = propertyPointer.createChild(context, null, 1, "testChild");
 assertEquals(propertyPointer, childNodePointer.getParentPointer());
 assertEquals("testProperty", childNodePointer.getNodeName().getLocalPart());
 assertEquals(1, childNodePointer.getIndex());
 assertEquals(null, childNodePointer.getNode());

 childNodePointer = propertyPointer.createChild(context, null, 1, "testChild2");
 assertEquals(propertyPointer, childNodePointer.getParentPointer());
 assertEquals("testProperty", childNodePointer.getNodeName().getLocalPart());
 assertEquals(1, childNodePointer.getIndex());
 assertEquals("testChild2", childNodePointer.getNode().toString());
 }

 @Test
 public void testCreateChildWithValue() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public int getPropertyCount() {
 return 1;
 }

 @Override
 public String[] getPropertyNames() {
 return new String[]{"testProperty"};
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }

 @Override
 public Object getBaseValue() {
 return new String[]{"testValue1", "testValue2"};
 }

 @Override
 public int getPropertyIndex() {
 return 0;
 }
 };

 JXPathContext context = JXPathContext.newContext(new Object());
 NodePointer childNodePointer = propertyPointer.createChild(context, null, 1, "testChild");
 assertEquals(propertyPointer, childNodePointer.getParentPointer());
 assertEquals("testProperty", childNodePointer.getNodeName().getLocalPart());
 assertEquals(1, childNodePointer.getIndex());
 assertEquals(null, childNodePointer.getNode());

 childNodePointer = propertyPointer.createChild(context, null, 1, "testChild2", "testValue2");
 assertEquals(propertyPointer, childNodePointer.getParentPointer());
 assertEquals("testProperty", childNodePointer.getNodeName().getLocalPart());
 assertEquals(1, childNodePointer.getIndex());
 assertEquals("testValue2", childNodePointer.getNode().toString());
 }
}