import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 @Test
 public void testPropertyPointerWithActualProperty() {
 PropertyPointer propertyPointer = new MyPropertyPointer(new MyParentPointer());
 propertyPointer.setPropertyIndex(0);
 propertyPointer.setPropertyName("property1");

 JXPathContext context = new JXPathContext(new MyParentObject());
 Object value = propertyPointer.createPath(context).getValue();
 assertEquals("expectedValue1", value);
 }

 @Test
 public void testPropertyPointerWithNonexistentProperty() {
 PropertyPointer propertyPointer = new MyPropertyPointer(new MyParentPointer());
 propertyPointer.setPropertyIndex(1);
 propertyPointer.setPropertyName("nonexistentProperty");

 JXPathContext context = new JXPathContext(new MyParentObject());
 Object value = propertyPointer.createPath(context).getValue();
 assertNull(value);
 }

 @Test(expected = JXPathAbstractFactoryException.class)
 public void testPropertyPointerWithNegativeIndex() {
 PropertyPointer propertyPointer = new MyPropertyPointer(new MyParentPointer());
 propertyPointer.setPropertyIndex(-1);
 propertyPointer.setPropertyName("property1");

 JXPathContext context = new JXPathContext(new MyParentObject());
 propertyPointer.createPath(context);
 }

 @Test
 public void testPropertyPointerWithCollection() {
 PropertyPointer propertyPointer = new MyCollectionPropertyPointer(new MyParentPointer());
 propertyPointer.setPropertyIndex(1);
 propertyPointer.setPropertyName("property1");

 JXPathContext context = new JXPathContext(new MyParentObjectWithCollection());
 Object value = propertyPointer.createPath(context).getValue();
 assertEquals("expectedValue2", value);
 }

 @Test
 public void testPropertyPointerWithLeafCollection() {
 PropertyPointer propertyPointer = new MyLeafCollectionPropertyPointer(new MyParentPointer());
 propertyPointer.setPropertyIndex(0);
 propertyPointer.setPropertyName("property1");

 JXPathContext context = new JXPathContext(new MyParentObjectWithLeafCollection());
 Object value = propertyPointer.createPath(context).getValue();
 assertEquals("expectedValue3", value);
 }

 @Test(expected = JXPathAbstractFactoryException.class)
 public void testPropertyPointerWithNullParent() {
 new PropertyPointer(null);
 }
}

class MyParentPointer extends NodePointer {
 MyParentPointer(NodePointer parent) {
 super(parent);
 }
}

class MyParentObject {
 String property1 = "expectedValue1";
 String property2;
}

class MyParentObjectWithCollection {
 Object[] property1 = new Object[]{"expectedValue2", "expectedValue3"};
}

class MyParentObjectWithLeafCollection {
 List<String> property1 = Arrays.asList("expectedValue3", "expectedValue4");
}

class MyPropertyPointer extends PropertyPointer {
 MyPropertyPointer(NodePointer parent) {
 super(parent);
 }

 @Override
 public String getPropertyName() {
 return "property1";
 }
}

class MyCollectionPropertyPointer extends PropertyPointer {
 MyCollectionPropertyPointer(NodePointer parent) {
 super(parent);
 }

 @Override
 public String getPropertyName() {
 return "property1";
 }

 @Override
 public int getPropertyCount() {
 return 2;
 }
}

class MyLeafCollectionPropertyPointer extends PropertyPointer {
 MyLeafCollectionPropertyPointer(NodePointer parent) {
 super(parent);
 }

 @Override
 public String getPropertyName() {
 return "property1";
 }

 @Override
 public boolean isCollection() {
 return true;
 }
}