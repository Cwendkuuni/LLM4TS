import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyOwnerPointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.JXPathInvalidAccessException;

public class NullPropertyPointerTest {

    @Mock
    private NodePointer parentPointer;
    @Mock
    private PropertyOwnerPointer propertyOwnerPointer;

    private NullPropertyPointer nullPropertyPointer;

    public NullPropertyPointerTest() {
        MockitoAnnotations.initMocks(this);
        nullPropertyPointer = new NullPropertyPointer(parentPointer);
    }

    @Test
    public void testGetName_typicalCase() {
        QName expectedName = new QName("*");
        QName actualName = nullPropertyPointer.getName();
        Assert.assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetPropertyIndex_typicalCase() {
        // No action needed since setPropertyIndex method does nothing in NullPropertyPointer
        nullPropertyPointer.setPropertyIndex(0);
        // Confirm that no exception is thrown
        Assert.assertTrue(true);
    }

    @Test
    public void testGetLength_typicalCase() {
        int expectedLength = 0;
        int actualLength = nullPropertyPointer.getLength();
        Assert.assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testGetBaseValue_typicalCase() {
        Object expectedValue = null;
        Object actualValue = nullPropertyPointer.getBaseValue();
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetImmediateNode_typicalCase() {
        Object expectedNode = null;
        Object actualNode = nullPropertyPointer.getImmediateNode();
        Assert.assertEquals(expectedNode, actualNode);
    }

    @Test
    public void testIsLeaf_typicalCase() {
        boolean expectedIsLeaf = true;
        boolean actualIsLeaf = nullPropertyPointer.isLeaf();
        Assert.assertEquals(expectedIsLeaf, actualIsLeaf);
    }

    @Test
    public void testGetValuePointer_typicalCase() {
        NullPointer expectedPointer = new NullPointer(nullPropertyPointer, new QName("*"));
        NodePointer actualPointer = nullPropertyPointer.getValuePointer();
        // Confirm that the expected NullPointer instance is created
        Assert.assertEquals(expectedPointer.getClass(), actualPointer.getClass());
        // Confirm that the NullPropertyPointer configuration is used correctly
        Assert.assertEquals(expectedPointer.getPropertyPointer().getClass(), actualPointer.getPropertyPointer().getClass());
    }

    @Test
    public void testIsActualProperty_typicalCase() {
        boolean expectedIsActualProperty = false;
        boolean actualIsActualProperty = nullPropertyPointer.isActualProperty();
        Assert.assertEquals(expectedIsActualProperty, actualIsActualProperty);
    }

    @Test
    public void testIsActual_typicalCase() {
        boolean expectedIsActual = false;
        boolean actualIsActual = nullPropertyPointer.isActual();
        Assert.assertEquals(expectedIsActual, actualIsActual);
    }

    @Test
    public void testIsContainer_typicalCase() {
        boolean expectedIsContainer = true;
        boolean actualIsContainer = nullPropertyPointer.isContainer();
        Assert.assertEquals(expectedIsContainer, actualIsContainer);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_parentIsNull() {
        // Null out the parent to simulate a null parent scenario
        nullPropertyPointer.setParent(null);
        nullPropertyPointer.setValue("someValue");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_parentIsNotContainer() {
        // Mock parent pointer to be of type PropertyOwnerPointer
        nullPropertyPointer.setParent(propertyOwnerPointer);
        // Mock property pointer to be returned by PropertyOwnerPointer
        NullPropertyPointer mockPropertyPointer = new NullPropertyPointer(parentPointer);
        propertyOwnerPointer.setPropertyPointer(mockPropertyPointer);
        nullPropertyPointer.setValue("someValue");
    }

    @Test
    public void testCreatePath_typicalCase() {
        JXPathContext context = new JXPathContext(null);
        NodePointer newPath = nullPropertyPointer.createPath(context);
        // Confirm that a new path is created
        Assert.assertNotNull(newPath);
    }

    @Test
    public void testCreatePath_parentIsPropertyOwnerPointer() {
        // Mock parent pointer to be of type PropertyOwnerPointer
        nullPropertyPointer.setParent(propertyOwnerPointer);
        // Mock property pointer to be returned by PropertyOwnerPointer
        NullPropertyPointer mockPropertyPointer = new NullPropertyPointer(parentPointer);
        propertyOwnerPointer.setPropertyPointer(mockPropertyPointer);

        JXPathContext context = new JXPathContext(null);
        NodePointer newPath = nullPropertyPointer.createPath(context);

        // Confirm that a new path is created
        Assert.assertNotNull(newPath);
        // Confirm that the createPath method of parent returns the expected path
        Assert.assertEquals(newPath, propertyOwnerPointer.createPath(context));
    }

    @Test
    public void testCreatePath_parentIsNotPropertyOwnerPointer() {
        // Mock parent pointer to be of type NodePointer
        nullPropertyPointer.setParent(parentPointer);

        JXPathContext context = new JXPathContext(null);
        NodePointer newPath = nullPropertyPointer.createPath(context);

        // Confirm that a new path is created
        Assert.assertNotNull(newPath);
    }

    @Test
    public void testCreatePath_withValue_typicalCase() {
        JXPathContext context = new JXPathContext(null);
        NodePointer newPath = nullPropertyPointer.createPath(context, "someValue");

        // Confirm that a new path is created
        Assert.assertNotNull(newPath);
        // Confirm that the value is correctly set
        Assert.assertEquals("someValue", newPath.getValue());
    }

    @Test
    public void testCreateChild_typicalCase() {
        JXPathContext context = new JXPathContext(null);
        QName childName = new QName("child");
        int childIndex = 0;
        NodePointer newChild = nullPropertyPointer.createChild(context, childName, childIndex);

        // Confirm that a new child is created
        Assert.assertNotNull(newChild);
        // Confirm that the child has the correct name and index
        Assert.assertEquals(childName, newChild.getName());
        Assert.assertEquals(childIndex, newChild.getIndex());
    }

    @Test
    public void testCreateChild_withValue_typicalCase() {
        JXPathContext context = new JXPathContext(null);
        QName childName = new QName("child");
        int childIndex = 0;
        NodePointer newChild = nullPropertyPointer.createChild(context, childName, childIndex, "childValue");

        // Confirm that a new child is created
        Assert.assertNotNull(newChild);
        // Confirm that the child has the correct name, index, and value
        Assert.assertEquals(childName, newChild.getName());
        Assert.assertEquals(childIndex, newChild.getIndex());
        Assert.assertEquals("childValue", newChild.getValue());
    }

    @Test
    public void testGetPropertyName_typicalCase() {
        String expectedPropertyName = "*";
        String actualPropertyName = nullPropertyPointer.getPropertyName();
        Assert.assertEquals(expectedPropertyName, actualPropertyName);
    }

    @Test
    public void testSetPropertyName_typicalCase() {
        String newPropertyName = "newPropertyName";
        nullPropertyPointer.setPropertyName(newPropertyName);
        // Confirm that the property name is updated
        Assert.assertEquals(newPropertyName, nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue_typicalCase() {
        String attributeValue = "attributeValue";
        nullPropertyPointer.setNameAttributeValue(attributeValue);
        // Confirm that the property name is updated
        Assert.assertEquals(attributeValue, nullPropertyPointer.getPropertyName());
        // Confirm that the byNameAttribute flag is set to true
        Assert.assertTrue(nullPropertyPointer.byNameAttribute);
    }

    @Test
    public void testIsCollection_typicalCase() {
        // Set an index to make it eligible for collection
        nullPropertyPointer.setIndex(0);
        Assert.assertTrue(nullPropertyPointer.isCollection());
    }

    @Test
    public void testGetPropertyCount_typicalCase() {
        int expectedPropertyCount = 0;
        int actualPropertyCount = nullPropertyPointer.getPropertyCount();
        Assert.assertEquals(expectedPropertyCount, actualPropertyCount);
    }

    @Test
    public void testGetPropertyNames_typicalCase() {
        String[] expectedPropertyNames = new String[0];
        String[] actualPropertyNames = nullPropertyPointer.getPropertyNames();
        Assert.assertArrayEquals(expectedPropertyNames, actualPropertyNames);
    }

    @Test
    public void testAsPath_typicalCase() {
        String expectedPath = "/[name()='*']";
        String actualPath = nullPropertyPointer.asPath();
        Assert.assertEquals(expectedPath, actualPath);
    }

    @Test
    public void testEscape_typicalCase() {
        String stringToEscape = "string'test\"escape";
        String expectedEscapedString = "string&apos;test&quot;escape";
        String actualEscapedString = nullPropertyPointer.escape(stringToEscape);
        Assert.assertEquals(expectedEscapedString, actualEscapedString);
    }
}