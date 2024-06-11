import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

public class NullPropertyPointerTest {

    @Test
    public void testNullPropertyPointerCreation() {
        NodePointer parent = mock(NodePointer.class);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertEquals(parent, nullPropertyPointer.getParent());
        assertEquals("*", nullPropertyPointer.getPropertyName());
        assertNull(nullPropertyPointer.getValuePointer());
        assertEquals(true, nullPropertyPointer.isLeaf());
        assertEquals(false, nullPropertyPointer.isContainer());
    }

    @Test
    public void testNullPropertyPointerGetName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals("*", nullPropertyPointer.getName().getName());
    }

    @Test
    public void testSetValueToNullParent() {
        NodePointer parent = mock(NodePointer.class);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertThrows(JXPathInvalidAccessException.class, () -> {
            nullPropertyPointer.setValue(new Object());
        });
    }

    @Test
    public void testCreatePathWithNullParent() {
        NodePointer parent = null;
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertThrows(JXPathInvalidAccessException.class, () -> {
            nullPropertyPointer.createPath(mock(JXPathContext.class));
        });
    }

    @Test
    public void testCreatePathWithNonPropertyOwnerParent() {
        NodePointer nonPropertyOwnerParent = mock(NodePointer.class);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nonPropertyOwnerParent);
        assertThrows(JXPathInvalidAccessException.class, () -> {
            nullPropertyPointer.createPath(mock(JXPathContext.class));
        });
    }

    @Test
    public void testCreatePathWithPropertyOwnerParent() {
        PropertyOwnerPointer propertyOwnerParent = mock(PropertyOwnerPointer.class);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerParent);
        NodePointer expectedPath = mock(NodePointer.class);
        when(propertyOwnerParent.getPropertyPointer()).thenReturn(expectedPath);
        NodePointer actualPath = nullPropertyPointer.createPath(mock(JXPathContext.class));
        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void testCreateChildWithValidInput() {
        JXPathContext context = mock(JXPathContext.class);
        NodePointer parent = mock(NodePointer.class);
        QName name = new QName("name");
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer expectedChild = mock(NodePointer.class);
        when(parent.createChild(context, name, 1)).thenReturn(expectedChild);
        NodePointer actualChild = nullPropertyPointer.createChild(context, name, 1);
        assertEquals(expectedChild, actualChild);
    }

    @Test
    public void testEscapeFunctionality() {
        String input = "This string contains 'single quote' and \"double quote\". ";
        String expectedOutput = "This string contains &apos;single quote&apos; and &quot;double quote&quot;.";
        String output = new NullPropertyPointer().escape(input);
        assertEquals(expectedOutput, output);
    }
}