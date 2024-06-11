import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.AbstractPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 private PropertyPointer propertyPointer;
 private AbstractPropertyPointer abstractPropertyPointer;

 @Test
 public void testConstructor() {
 propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public int getPropertyCount() {
 return 0;
 }

 @Override
 public String[] getPropertyNames() {
 return new String[0];
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }
 };

 assertNotNull(propertyPointer);
 }

 @Test
 public void testGetPropertyIndex() {
 propertyPointer = new PropertyPointer(null) {
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
 }

 @Test
 public void testSetPropertyIndex() {
 propertyPointer = new PropertyPointer(null) {
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

 propertyPointer.setPropertyIndex(1);
 assertEquals(-1, propertyPointer.getPropertyIndex());
 propertyPointer.setPropertyIndex(0);
 assertEquals(0, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testGetBean() {
 propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public int getPropertyCount() {
 return 0;
 }

 @Override
 public String[] getPropertyNames() {
 return new String[0];
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
 propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public int getPropertyCount() {
 return 0;
 }

 @Override
 public String[] getPropertyNames() {
 return new String[0];
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
 propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public int getPropertyCount() {
 return 0;
 }

 @Override
 public String[] getPropertyNames() {
 return new String[0];
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }
 };

 assertEquals("testProperty", propertyPointer.getName().getLocalPart());
 }

 @Test
 public void testGetLength() {
 abstractPropertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public int getPropertyCount() {
 return 1;
 }

 @Override
 public String[] getPropertyNames() {
 return new String[]{"testProperty"};
 }
 };

 propertyPointer = new PropertyPointer(abstractPropertyPointer) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };

 assertEquals(-1, propertyPointer.getLength());
 }

 @Test
 public void testIsCollection() {
 abstractPropertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public int getPropertyCount() {return 1;}

 @Override
 public String[] getPropertyNames() {return new String[]{"testProperty"};}
 };

 propertyPointer = new PropertyPointer(abstractPropertyPointer) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };

 propertyPointer.setValue(new Object[]{1, 2, 3});
 assertTrue(propertyPointer.isCollection());
 }

 @Test
 public void testIsLeaf() {
 abstractPropertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public int getPropertyCount() {return 1;}

 @Override
 public String[] getPropertyNames() {return new String[]{"testProperty"};}
 };

 propertyPointer = new PropertyPointer(abstractPropertyPointer) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };

 propertyPointer.setValue(new Object(){});
 assertTrue(propertyPointer.isLeaf());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetImmediateNodeIndexOutOfBounds() {
 abstractPropertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public int getPropertyCount() {return 1;}

 @Override
 public String[] getPropertyNames() {return new String[]{"testProperty"};}
 };

 propertyPointer = new PropertyPointer(abstractPropertyPointer) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };

 propertyPointer.setValue(new Object[]{1, 2, 3});
 propertyPointer.setIndex(3);
 propertyPointer.getImmediateNode();
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetBaseValueIndexOutOfBounds() {
 abstractPropertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public int getPropertyCount() {return 1;}

 @Override
 public String[] getPropertyNames() {return new String[]{"testProperty"};}
 };

 propertyPointer = new PropertyPointer(abstractPropertyPointer) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };

 propertyPointer.setValue(new Object[]{1, 2, 3});
 propertyPointer.setIndex(1);
 propertyPointer.setBaseValue(new Object[]{1, 2});
 propertyPointer.getImmediateNode();
 }

 @Test
 public void testGetImmediateValuePointer() {
 abstractPropertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public int getPropertyCount() {return 1;}

 @Override
 public String[] getPropertyNames() {return new String[]{"testProperty"};}
 };

 propertyPointer = new PropertyPointer(abstractPropertyPointer) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };

 propertyPointer.setValue(new Object[]{1, 2, 3});
 NodePointer nodePointer = propertyPointer.getImmediateValuePointer();
 assertEquals(NodePointer.class, nodePointer.getClass());
 }

 @Test
 public void testCreatePath() {
 abstractPropertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public int getPropertyCount() {return 1;}

 @Override
 public String[] getPropertyNames() {return new String[]{"testProperty"};}
 };

 propertyPointer = new PropertyPointer(abstractPropertyPointer) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };

 propertyPointer.setValue(new Object[]{1, 2, 3});
 JXPathContext context = new JXPathContext();
 assertEquals(propertyPointer, propertyPointer.createPath(context));
 }

 @Test
 public void testCreatePathWithValue() {
 abstractPropertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public int getPropertyCount() {return 1;}

 @Override
 public String[] getPropertyNames() {return new String[]{"testProperty"};}
 };

 propertyPointer = new PropertyPointer(abstractPropertyPointer) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };

 propertyPointer.setValue(new Object[]{1, 2, 3});
 JXPathContext context = new JXPathContext();
 propertyPointer.createPath(context, 4);
 assertEquals(4, propertyPointer.getIndex());
 }

 @Test
 public void testCreateChild() {
 abstractPropertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public int getPropertyCount() {return 1;}

 @Override
 public String[] getPropertyNames() {return new String[]{"testProperty"};}
 };

 propertyPointer = new PropertyPointer(abstractPropertyPointer) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };

 propertyPointer.setValue(new Object[]{1, 2, 3});
 JXPathContext context = new JXPathContext();
 PropertyPointer childPropertyPointer1 = (PropertyPointer) propertyPointer.createChild(context, null, 1, null);
 PropertyPointer childPropertyPointer2 = (PropertyPointer) propertyPointer.createChild(context, null, 1, "testChild");
 assertEquals(1, childPropertyPointer1.getPropertyIndex());
 assertEquals("testChild", childPropertyPointer2.getPropertyName());
 }

 @Test
 public void testCreateChildWithValue() {
 abstractPropertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public int getPropertyCount() {return 1;}

 @Override
 public String[] getPropertyNames() {return new String[]{"testProperty"};}
 };

 propertyPointer = new PropertyPointer(abstractPropertyPointer) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };

 propertyPointer.setValue(new Object[]{1, 2, 3});
 JXPathContext context = new JXPathContext();
 PropertyPointer childPropertyPointer1 = (PropertyPointer) propertyPointer.createChild(context, null, 1, new Object[]{4, 5});
 PropertyPointer childPropertyPointer2 = (PropertyPointer) propertyPointer.createChild(context, null, 1, "testChild");
 assertEquals(4, childPropertyPointer1.getPropertyIndex());
 assertEquals("testChild", childPropertyPointer2.getPropertyName());
 }

 @Test
 public void testCreateChildWithName() {
 abstractPropertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public int getPropertyCount() {return 1;}

 @Override
 public String[] getPropertyNames() {return new String[]{"testProperty"};}
 };

 propertyPointer = new PropertyPointer(abstractPropertyPointer) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };

 propertyPointer.setValue(new Object[]{1, 2, 3});
 JXPathContext context = new JXPathContext();
 propertyPointer.createChild(context, new QName("test:testChild"), 1);
 assertEquals("test:testChild", propertyPointer.getName().getLocalPart());
 }

 @Test
 public void testHashCode() {
 PropertyPointer propertyPointer1 = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty1";
 }
 };
 PropertyPointer propertyPointer2 = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty2";
 }
 };
 PropertyPointer propertyPointer3 = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty1";
 }
 };
 assertEquals(propertyPointer1.hashCode(), propertyPointer1.hashCode());
 assertEquals(propertyPointer1.hashCode(), propertyPointer3.hashCode());
 assertNotEquals(propertyPointer1.hashCode(), propertyPointer2.hashCode());
 }

 @Test
 public void testEquals() {
 PropertyPointer propertyPointer1 = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty1";
 }
 };
 PropertyPointer propertyPointer2 = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty1";
 }
 };
 PropertyPointer propertyPointer3 = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty2";
 }
 };
 PropertyPointer propertyPointer4 = new PropertyPointer(propertyPointer1);
 PropertyPointer propertyPointer5 = new PropertyPointer(propertyPointer1);
 PropertyPointer propertyPointer6 = new PropertyPointer(propertyPointer2);
 assertFalse(propertyPointer1.equals(propertyPointer2));
 assertTrue(propertyPointer1.equals(propertyPointer4));
 assertTrue(propertyPointer1.equals(propertyPointer5));
 assertFalse(propertyPointer1.equals(propertyPointer3));
 assertFalse(propertyPointer1.equals(propertyPointer6));
 }

 @Test
 public void testCompareChildNodePointers() {
 PropertyPointer propertyPointer1 = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty1";
 }
 };
 PropertyPointer propertyPointer2 = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty2";
 }
 };
 NodePointer nodePointer1 = new NodePointer(null) {
 @Override
 public String getName() {
 return "testNode1";
 }
 };
 NodePointer nodePointer2 = new NodePointer(null) {
 @Override
 public String getName() {
 return "testNode2";
 }
 };
 NodePointer valuePointer1 = new NodePointer(null) {
 @Override
 public String getName() {
 return "testValue1";
 }
 };
 NodePointer valuePointer2 = new NodePointer(null) {
 @Override
 public String getName() {
 return "testValue2";
 }
 };
 assertEquals(0, propertyPointer1.compareChildNodePointers(propertyPointer1, propertyPointer1));
 assertEquals(-1, propertyPointer1.compareChildNodePointers(propertyPointer1, propertyPointer2));
 assertEquals(1, propertyPointer1.compareChildNodePointers(propertyPointer2, propertyPointer1));
 assertEquals(0, propertyPointer1.compareChildNodePointers(propertyPointer1, nodePointer1));
 assertEquals(-1, propertyPointer1.compareChildNodePointers(propertyPointer1, valuePointer1));
 assertEquals(1, propertyPointer1.compareChildNodePointers(nodePointer1, propertyPointer1));
 assertEquals(0, propertyPointer1.compareChildNodePointers(nodePointer1, nodePointer1));
 assertEquals(-1, propertyPointer1.compareChildNodePointers(valuePointer1, propertyPointer1));
 assertEquals(0, propertyPointer1.compareChildNodePointers(valuePointer1, valuePointer1));
 }
}