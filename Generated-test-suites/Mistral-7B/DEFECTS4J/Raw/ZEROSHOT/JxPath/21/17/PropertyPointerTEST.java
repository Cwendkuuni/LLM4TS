import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.AbstractPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 @Test
 public void testAbstractPropertyPointer() {
 AbstractPropertyPointer pointer = new AbstractPropertyPointer(null) {
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

 assertEquals("testProperty", pointer.getName().getLocalPart());
 assertEquals(1, pointer.getPropertyCount());
 assertArrayEquals(new String[]{"testProperty"}, pointer.getPropertyNames());
 assertTrue(pointer.isActual());
 }

 @Test
 public void testPropertyPointer() {
 PropertyPointer pointer = new PropertyPointer(null) {
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

 assertEquals("testProperty", pointer.getName().getLocalPart());
 assertEquals(1, pointer.getPropertyCount());
 assertArrayEquals(new String[]{"testProperty"}, pointer.getPropertyNames());
 assertTrue(pointer.isActual());
 }

 @Test
 public void testSetPropertyIndex() {
 PropertyPointer pointer = new PropertyPointer(null) {
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

 pointer.setPropertyIndex(0);
 assertEquals(0, pointer.getPropertyIndex());
 pointer.setPropertyIndex(Integer.MIN_VALUE);
 assertEquals(PropertyPointer.UNSPECIFIED_PROPERTY, pointer.getPropertyIndex());
 }

 @Test
 public void testGetBean() {
 PropertyPointer pointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testBean";
 }

 @Override
 public int getPropertyCount() {
 return 1;
 }

 @Override
 public String[] getPropertyNames() {
 return new String[]{"testBean"};
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }
 };
 Object bean = new Object();
 pointer.setBean(bean);
 assertEquals(bean, pointer.getBean());
 }

 @Test
 public void testGetPropertyIndex() {
 PropertyPointer pointer = new PropertyPointer(null) {
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
 assertEquals(PropertyPointer.UNSPECIFIED_PROPERTY, pointer.getPropertyIndex());
 }

 @Test
 public void testIsActual() {
 PropertyPointer pointer = new PropertyPointer(null) {
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
 assertTrue(pointer.isActual());
 }

 @Test
 public void testGetImmediateNode() {
 PropertyPointer pointer = new PropertyPointer(null) {
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
 Object bean = new Object();
 pointer.setBean(bean);
 pointer.setPropertyIndex(0);
 assertEquals(bean, pointer.getImmediateNode());
 }

 @Test
 public void testIsCollection() {
 PropertyPointer pointer = new PropertyPointer(null) {
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
 assertFalse(pointer.isCollection());

 PropertyPointer pointerCollection = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public int getPropertyCount() {return 2; }

 @Override
 public String[] getPropertyNames() {
 return new String[]{"testProperty1", "testProperty2"};
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }
 };
 assertTrue(pointerCollection.isCollection());
 }

 @Test
 public void testGetLength() {
 PropertyPointer pointer = new PropertyPointer(null) {
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
 pointer.setBean(new Object[]{1, 2, 3});
 assertEquals(3, pointer.getLength());
 }

 @Test
 public void testCreatePath() {
 PropertyPointer pointer = new PropertyPointer(null) {
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
 Object bean = new Object();
 pointer.setBean(bean);
 pointer.setPropertyIndex(0);
 JXPathContext context = new JXPathContext();
 NodePointer nodePointer = pointer.createPath(context);
 assertEquals(bean, nodePointer.getNode());
 }

 @Test
 public void testCreatePathWithValue() {
 PropertyPointer pointer = new PropertyPointer(null) {
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
 Object bean = new Object();
 pointer.setBean(bean);
 pointer.setPropertyIndex(0);
 JXPathContext context = new JXPathContext();
 pointer.createPath(context, "testValue");
 assertEquals("testValue", pointer.getNode().getValue());
 }

 @Test
 public void testGetImmediateValuePointer() {
 PropertyPointer pointer = new PropertyPointer(null) {
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
 Object bean = new Object();
 pointer.setBean(bean);
 pointer.setPropertyIndex(0);
 NodePointer nodePointer = pointer.getImmediateValuePointer();
 assertEquals(bean, nodePointer.getNode());
 }
}