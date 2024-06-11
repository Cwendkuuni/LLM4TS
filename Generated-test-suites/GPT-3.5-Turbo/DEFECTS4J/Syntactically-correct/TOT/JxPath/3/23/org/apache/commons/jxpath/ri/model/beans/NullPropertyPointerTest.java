import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.QName;

public class NullPropertyPointerTest {

   private NullPropertyPointer nullPropertyPointer;
   private NodePointer parentPointer;
   private JXPathContext context;

   @Before
   public void setUp() throws Exception {
      parentPointer = createMock(NodePointer.class); // create a mock NodePointer object
      nullPropertyPointer = new NullPropertyPointer(parentPointer);
      context = createMock(JXPathContext.class); // create a mock JXPathContext object
   }

   @Test
   public void testGetName() {
      QName expected = new QName("*");
      QName actual = nullPropertyPointer.getName();
      assertEquals(expected, actual);
   }

   @Test
   public void testSetPropertyIndex() {
      int index = 5;
      nullPropertyPointer.setPropertyIndex(index);
      int expected = 0;
      int actual = nullPropertyPointer.getLength();
      assertEquals(expected, actual);
   }

   @Test
   public void testGetBaseValue() {
      Object expected = null;
      Object actual = nullPropertyPointer.getBaseValue();
      assertEquals(expected, actual);
   }

   @Test
   public void testGetImmediateNode() {
      Object expected = null;
      Object actual = nullPropertyPointer.getImmediateNode();
      assertEquals(expected, actual);
   }

   @Test
   public void testIsLeaf() {
      boolean expected = true;
      boolean actual = nullPropertyPointer.isLeaf();
      assertEquals(expected, actual);
   }

   @Test
   public void testGetValuePointer() {
      NodePointer expected = new NullPointer(nullPropertyPointer, new QName(nullPropertyPointer.getPropertyName()));
      NodePointer actual = nullPropertyPointer.getValuePointer();
      assertEquals(expected, actual);
   }

   @Test
   public void testIsActualProperty() {
      boolean expected = false;
      boolean actual = nullPropertyPointer.isActualProperty();
      assertEquals(expected, actual);
   }

   @Test
   public void testIsActual() {
      boolean expected = false;
      boolean actual = nullPropertyPointer.isActual();
      assertEquals(expected, actual);
   }

   @Test
   public void testIsContainer() {
      boolean expected = true;
      boolean actual = nullPropertyPointer.isContainer();
      assertEquals(expected, actual);
   }

   @Test(expected = JXPathInvalidAccessException.class)
   public void testSetValue_NullParent() {
      nullPropertyPointer.setValue("value");
   }

   @Test(expected = JXPathInvalidAccessException.class)
   public void testSetValue_InvalidPath() {
      NullPointer nullPointer = new NullPointer(nullPropertyPointer, new QName(nullPropertyPointer.getPropertyName()));
      nullPropertyPointer.setParent(nullPointer);
      nullPropertyPointer.setValue("value");
   }

   @Test
   public void testCreatePath_Attribute() {
      NodePointer newParent = createMock(NodePointer.class); // create a mock NodePointer object
      expect(parentPointer.createPath(context)).andReturn(newParent);
      QName attributeName = new QName("attribute");
      NodePointer attributePointer = createMock(NodePointer.class); // create a mock NodePointer object
      expect(newParent.createAttribute(context, attributeName)).andReturn(attributePointer);
      replay(parentPointer, newParent);

      NodePointer actual = nullPropertyPointer.createPath(context);
      assertEquals(attributePointer, actual);

      verify(parentPointer, newParent);
   }

   @Test
   public void testCreatePath_Element() {
      PropertyOwnerPointer propertyOwnerPointer = createMock(PropertyOwnerPointer.class); // create a mock PropertyOwnerPointer object
      propertyOwnerPointer.setPropertyPointer(nullPropertyPointer);
      NodePointer newParent = createMock(NodePointer.class); // create a mock NodePointer object
      expect(parentPointer.createPath(context)).andReturn(propertyOwnerPointer);
      expect(propertyOwnerPointer.getPropertyPointer()).andReturn(newParent);
      QName elementName = new QName("element");
      int index = 1;
      NodePointer childPointer = createMock(NodePointer.class); // create a mock NodePointer object
      expect(newParent.createChild(context, elementName, index)).andReturn(childPointer);
      replay(parentPointer, propertyOwnerPointer, newParent);

      nullPropertyPointer.setPropertyName("element");
      nullPropertyPointer.setPropertyIndex(index);
      NodePointer actual = nullPropertyPointer.createPath(context);
      assertEquals(childPointer, actual);

      verify(parentPointer, propertyOwnerPointer, newParent);
   }

   @Test
   public void testCreatePath_Element_WithAttributeValue() {
      NullPointer nullPointer = createMock(NullPointer.class); // create a mock NullPointer object
      nullPropertyPointer.setParent(nullPointer);
      PropertyOwnerPointer propertyOwnerPointer = createMock(PropertyOwnerPointer.class);
      propertyOwnerPointer.setPropertyPointer(nullPointer);
      NodePointer newParent = createMock(NodePointer.class);
      expect(nullPointer.createPath(context)).andReturn(propertyOwnerPointer);
      expect(propertyOwnerPointer.getPropertyPointer()).andReturn(newParent);
      QName elementName = new QName("element");
      int index = 1;
      NodePointer childPointer = createMock(NodePointer.class);
      expect(newParent.createChild(context, elementName, index)).andReturn(childPointer);
      replay(nullPointer, propertyOwnerPointer, newParent);

      nullPropertyPointer.setPropertyName("element");
      nullPropertyPointer.setPropertyIndex(index);
      NodePointer actual = nullPropertyPointer.createPath(context);
      assertEquals(childPointer, actual);

      verify(nullPointer, propertyOwnerPointer, newParent);
   }

   @Test
   public void testCreatePath_ElementWithValue() {
      PropertyOwnerPointer propertyOwnerPointer = createMock(PropertyOwnerPointer.class);
      propertyOwnerPointer.setPropertyPointer(nullPropertyPointer);
      NodePointer newParent = createMock(NodePointer.class);
      expect(parentPointer.createPath(context)).andReturn(propertyOwnerPointer);
      expect(propertyOwnerPointer.getPropertyPointer()).andReturn(newParent);
      QName elementName = new QName("element");
      int index = 1;
      Object value = new Object();
      NodePointer childPointer = createMock(NodePointer.class);
      expect(newParent.createChild(context, elementName, index, value)).andReturn(childPointer);
      replay(parentPointer, propertyOwnerPointer, newParent);

      nullPropertyPointer.setPropertyName("element");
      nullPropertyPointer.setPropertyIndex(index);
      NodePointer actual = nullPropertyPointer.createPath(context, value);
      assertEquals(childPointer, actual);

      verify(parentPointer, propertyOwnerPointer, newParent);
   }

   @Test
   public void testGetPropertyName() {
      String expected = "*";
      String actual = nullPropertyPointer.getPropertyName();
      assertEquals(expected, actual);
   }

   @Test
   public void testSetPropertyName() {
      String propertyName = "property";
      nullPropertyPointer.setPropertyName(propertyName);
      String expected = propertyName;
      String actual = nullPropertyPointer.getPropertyName();
      assertEquals(expected, actual);
   }

   @Test
   public void testIsCollection() {
      boolean expected = false;
      boolean actual = nullPropertyPointer.isCollection();
      assertEquals(expected, actual);
   }

   @Test
   public void testGetPropertyCount() {
      int expected = 0;
      int actual = nullPropertyPointer.getPropertyCount();
      assertEquals(expected, actual);
   }

   @Test
   public void testGetPropertyNames() {
      String[] expected = new String[0];
      String[] actual = nullPropertyPointer.getPropertyNames();
      assertArrayEquals(expected, actual);
   }

   @Test
   public void testAsPath_NotByNameAttribute() {
      NodePointer immediateParentPointer = createMock(NodePointer.class);
      expect(nullPropertyPointer.getImmediateParentPointer()).andReturn(immediateParentPointer);
      expect(immediateParentPointer.asPath()).andReturn("/path/to");
      replay(nullPropertyPointer, immediateParentPointer);

      String expected = "/path/to[@name='*']";
      String actual = nullPropertyPointer.asPath();
      assertEquals(expected, actual);

      verify(nullPropertyPointer, immediateParentPointer);
   }

   @Test
   public void testAsPath_ByNameAttribute() {
      NodePointer immediateParentPointer = createMock(NodePointer.class);
      expect(nullPropertyPointer.getImmediateParentPointer()).andReturn(immediateParentPointer);
      expect(immediateParentPointer.asPath()).andReturn("/path/to");
      expect(nullPropertyPointer.getPropertyName()).andReturn("property");
      replay(nullPropertyPointer, immediateParentPointer);

      nullPropertyPointer.setNameAttributeValue("property");
      String expected = "/path/to[@name='property']";
      String actual = nullPropertyPointer.asPath();
      assertEquals(expected, actual);

      verify(nullPropertyPointer, immediateParentPointer);
   }

   @Test
   public void testAsPath_WithIndex() {
      NodePointer immediateParentPointer = createMock(NodePointer.class);
      expect(nullPropertyPointer.getImmediateParentPointer()).andReturn(immediateParentPointer);
      expect(immediateParentPointer.asPath()).andReturn("/path/to");
      expect(nullPropertyPointer.getPropertyName()).andReturn("property");
      nullPropertyPointer.setPropertyIndex(1);
      replay(nullPropertyPointer, immediateParentPointer);

      String expected = "/path/to[@name='property'][2]";
      String actual = nullPropertyPointer.asPath();
      assertEquals(expected, actual);

      verify(nullPropertyPointer, immediateParentPointer);
   }

   private <T> T createMock(Class<T> clazz) {
      return org.easymock.EasyMock.createMock(clazz);
   }

   private void replay(Object... mocks) {
      org.easymock.EasyMock.replay(mocks);
   }

   private void verify(Object... mocks) {
      org.easymock.EasyMock.verify(mocks);
   }

   private void assertArrayEquals(Object[] expected, Object[] actual) {
      org.junit.Assert.assertArrayEquals(expected, actual);
   }
}