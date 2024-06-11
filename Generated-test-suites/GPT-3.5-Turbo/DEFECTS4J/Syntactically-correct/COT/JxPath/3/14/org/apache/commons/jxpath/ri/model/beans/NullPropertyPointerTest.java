import org.junit.Test;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;

public class NullPropertyPointerTest {
    private NullPropertyPointer nullPropertyPointer;

    @Before
    public void setup() {
        NullPropertyPointer parent = new NullPropertyPointer();
        nullPropertyPointer = new NullPropertyPointer(parent);
    }

    @Test
    public void testGetName() {
        QName expected = new QName("*");
        QName actual = nullPropertyPointer.getName();

        assertEquals(expected, actual);
    }

    @Test
    public void testSetPropertyIndex() {
        int index = 1;
        nullPropertyPointer.setPropertyIndex(index);

        // No assertions, just testing if the method runs without errors
    }

    @Test
    public void testGetLength() {
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
        QName expectedName = new QName("*");
        NullPointer expectedPointer = new NullPointer(nullPropertyPointer, expectedName);
        NodePointer actual = nullPropertyPointer.getValuePointer();

        assertEquals(expectedPointer, actual);
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
    public void testSetValue_NullParent_ThrowsException() {
        Object value = new Object();
        nullPropertyPointer.setValue(value);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_NonContainerParent_ThrowsException() {
        NullPropertyPointer parent = new NullPropertyPointer();
        parent.setIsContainer(false);
        nullPropertyPointer.setParent(parent);

        Object value = new Object();
        nullPropertyPointer.setValue(value);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_PropertyOwnerWithDynamicPropertyCreation_DisabledThrowsException() {
        PropertyOwnerPointer parent = new PropertyOwnerPointer();
        parent.setDynamicPropertyDeclarationSupported(false);
        nullPropertyPointer.setParent(parent);

        Object value = new Object();
        nullPropertyPointer.setValue(value);
    }

    @Test
    public void testSetValue_PropertyOwnerWithDynamicPropertyCreation_EnablesSetValue() {
        PropertyOwnerPointer parent = new PropertyOwnerPointer();
        parent.setDynamicPropertyDeclarationSupported(true);
        nullPropertyPointer.setParent(parent);

        Object value = new Object();
        nullPropertyPointer.setValue(value);

        // No assertions, just checking if the method runs without errors
    }

    @Test
    public void testCreatePath_NoParent_ReturnsNullPointer() {
        JXPathContext context = new JXPathContext();

        NodePointer actual = nullPropertyPointer.createPath(context);

        assertTrue(actual instanceof NullPointer);
    }

    @Test
    public void testCreatePath_ParentIsPropertyOwner_ReturnsPropertyPointer() {
        PropertyOwnerPointer parent = new PropertyOwnerPointer();
        nullPropertyPointer.setParent(parent);

        JXPathContext context = new JXPathContext();
        NodePointer actual = nullPropertyPointer.createPath(context);

        assertTrue(actual instanceof PropertyPointer);
    }

    @Test
    public void testCreatePath_ParentIsNotPropertyOwner_ReturnsParentCreateChild() {
        NullPointer parent = new NullPointer();
        nullPropertyPointer.setParent(parent);

        JXPathContext context = new JXPathContext();
        NodePointer actual = nullPropertyPointer.createPath(context);

        assertEquals(parent.createChild(context, nullPropertyPointer.getName(), nullPropertyPointer.getIndex()), actual);
    }

    @Test
    public void testCreatePathWithValue_Attribute_ReturnsAttributePointer() {
        JXPathContext context = new JXPathContext();
        Object value = new Object();

        NodePointer actual = nullPropertyPointer.createPath(context, value);

        assertTrue(actual instanceof AttributePointer);
    }

    @Test
    public void testCreatePathWithValue_PropertyOwner_ReturnsPropertyPointer() {
        PropertyOwnerPointer parent = new PropertyOwnerPointer();
        nullPropertyPointer.setParent(parent);

        JXPathContext context = new JXPathContext();
        Object value = new Object();

        NodePointer actual = nullPropertyPointer.createPath(context, value);

        assertTrue(actual instanceof PropertyPointer);
    }

    @Test
    public void testCreatePathWithValue_NotPropertyOwner_ReturnsParentCreateChildWithValue() {
        NullPointer parent = new NullPointer();
        nullPropertyPointer.setParent(parent);

        JXPathContext context = new JXPathContext();
        Object value = new Object();

        NodePointer actual = nullPropertyPointer.createPath(context, value);

        assertEquals(parent.createChild(context, nullPropertyPointer.getName(), nullPropertyPointer.getIndex(), value), actual);
    }

    @Test
    public void testCreateChild_PathCreation_ReturnsParentCreateChild() {
        JXPathContext context = new JXPathContext();
        QName name = new QName();
        int index = 1;

        NodePointer actual = nullPropertyPointer.createChild(context, name, index);

        assertEquals(nullPropertyPointer.createPath(context).createChild(context, name, index), actual);
    }

    @Test
    public void testCreateChildWithValue_PathCreation_ReturnsParentCreateChildWithValue() {
        JXPathContext context = new JXPathContext();
        QName name = new QName();
        int index = 1;
        Object value = new Object();

        NodePointer actual = nullPropertyPointer.createChild(context, name, index, value);

        assertEquals(nullPropertyPointer.createPath(context).createChild(context, name, index, value), actual);
    }

    @Test
    public void testGetPropertyName_Initialized_ReturnsPropertyName() {
        String expected = "*";
        String actual = nullPropertyPointer.getPropertyName();

        assertEquals(expected, actual);
    }

    @Test
    public void testSetPropertyName_NewPropertyName_ChangesPropertyName() {
        String expected = "newProperty";
        nullPropertyPointer.setPropertyName(expected);

        assertEquals(expected, nullPropertyPointer.getPropertyName());
    }
}

