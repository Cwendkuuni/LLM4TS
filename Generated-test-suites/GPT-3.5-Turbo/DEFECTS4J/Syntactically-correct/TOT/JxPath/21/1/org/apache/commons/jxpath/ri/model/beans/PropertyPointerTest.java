import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.JXPathAbstractFactoryException;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointerImpl;
import org.apache.commons.jxpath.ri.model.beans.SimplePropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.XPathMixin;
import org.apache.commons.jxpath.util.Methods;
import org.apache.commons.jxpath.util.ValueUtils;
import org.apache.commons.jxpath.ri.JXPathContextReferenceImpl;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.junit.Assert.*;

public class PropertyPointerTest {

    private PropertyPointer propertyPointer;

    @Before
    public void setup() {
        propertyPointer = new SimplePropertyPointer();
    }

    @Test
    public void testGetPropertyName() {
        propertyPointer.setPropertyName("testProperty");
        assertEquals("testProperty", propertyPointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        propertyPointer.setPropertyName("testProperty");
        assertEquals("testProperty", propertyPointer.getPropertyName());
    }

    @Test
    public void testGetPropertyIndex() {
        propertyPointer.setPropertyIndex(5);
        assertEquals(5, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testSetPropertyIndex() {
        propertyPointer.setPropertyIndex(5);
        assertEquals(5, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        Object bean = new Object();
        PropertyPointer parent = new SimplePropertyPointer();
        parent.setNode(bean);
        propertyPointer.setParent(parent);
        assertEquals(bean, propertyPointer.getBean());
    }

    @Test
    public void testGetBean_NullParent() {
        propertyPointer.setParent(null);
        assertNull(propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        propertyPointer.setPropertyName("testProperty");
        assertEquals("testProperty", propertyPointer.getName().getName());
        assertNull(propertyPointer.getName().getNamespaceURI());
    }

    @Test
    public void testIsActual() {
        PropertyPointer parent = new SimplePropertyPointer();
        parent.setNode(new Object());
        propertyPointer.setParent(parent);
        propertyPointer.setPropertyName("testProperty");
        assertTrue(propertyPointer.isActual());
    }

    @Test
    public void testIsActualProperty() {
        assertTrue(propertyPointer.isActualProperty());
    }

    @Test
    public void testGetImmediateNode_NullValue() {
        propertyPointer.setBaseValue(null);
        assertNull(propertyPointer.getImmediateNode());
    }

    @Test
    public void testGetImmediateNode_InitializedValue() {
        propertyPointer.setBaseValue("test");
        propertyPointer.setValue("newValue");
        assertEquals("newValue", propertyPointer.getImmediateNode());
    }

    @Test
    public void testGetImmediateNode_UninitializedValue() {
        propertyPointer.setBaseValue("test");
        assertEquals("test", propertyPointer.getImmediateNode());
    }

    @Test
    public void testIsCollection_True() {
        propertyPointer.setBaseValue(Arrays.asList(1, 2, 3));
        assertTrue(propertyPointer.isCollection());
    }

    @Test
    public void testIsCollection_False() {
        propertyPointer.setBaseValue("test");
        assertFalse(propertyPointer.isCollection());
    }

    @Test
    public void testIsLeaf_True() {
        propertyPointer.setNode("test");
        assertTrue(propertyPointer.isLeaf());
    }

    @Test
    public void testIsLeaf_False() {
        propertyPointer.setNode(new Object());
        assertFalse(propertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        propertyPointer.setBaseValue(Arrays.asList(1, 2, 3));
        assertEquals(3, propertyPointer.getLength());
    }

    @Test
    public void testGetLength_NullValue() {
        propertyPointer.setBaseValue(null);
        assertEquals(-1, propertyPointer.getLength());
    }

    @Test
    public void testGetLength_NotCollection() {
        propertyPointer.setBaseValue("test");
        assertEquals(-1, propertyPointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        PropertyPointer parent = new SimplePropertyPointer();
        parent.setNode("test");
        propertyPointer.setParent(parent);
        propertyPointer.setPropertyName("testProperty");
        propertyPointer.setIndex(1);
        Object value = propertyPointer.getImmediateValuePointer().getNode();
        assertEquals("test", value);
    }

    @Test
    public void testCreatePath_NullImmediateNode() {
        JXPathContext context = JXPathContext.newContext(null);
        propertyPointer.setParent(new SimplePropertyPointer());
        propertyPointer.setBaseValue(context.getRootNode());
        propertyPointer.setPropertyName("testProperty");
        propertyPointer.setIndex(0);

        propertyPointer.createPath(context);
        assertTrue(propertyPointer.getImmediateNode() instanceof XPathMixin);
    }

    @Test(expected=JXPathAbstractFactoryException.class)
    public void testCreatePath_Failure() {
        JXPathContext context = JXPathContext.newContext(null);
        propertyPointer.setParent(new SimplePropertyPointer());
        propertyPointer.setBaseValue(context.getRootNode());
        propertyPointer.setPropertyName("testProperty");
        propertyPointer.setIndex(0);
        propertyPointer.createPath(context);
    }

    @Test
    public void testCreatePath_WithExistingNode() {
        Object testObject = new Object();
        JXPathContext context = JXPathContext.newContext(testObject);
        propertyPointer.setParent(new SimplePropertyPointer());
        propertyPointer.setBaseValue(context.getRootNode());
        propertyPointer.setPropertyName("testProperty");

        propertyPointer.createPath(context);
        assertSame(testObject, propertyPointer.getImmediateNode());
    }

    @Test
    public void testCreatePath_WithExistingNodeAndIndex() {
        Object testObject = new Object();
        JXPathContext context = JXPathContext.newContext(testObject);
        propertyPointer.setParent(new SimplePropertyPointer());
        propertyPointer.setBaseValue(context.getRootNode());
        propertyPointer.setPropertyName("testProperty");
        propertyPointer.setIndex(1);

        propertyPointer.createPath(context);
        assertSame(testObject, propertyPointer.getImmediateNode());
    }

    @Test
    public void testCreatePath_WithExistingNodeAndValue() {
        Object testObject = new Object();
        JXPathContext context = JXPathContext.newContext(testObject);
        propertyPointer.setParent(new SimplePropertyPointer());
        propertyPointer.setBaseValue(context.getRootNode());
        propertyPointer.setPropertyName("testProperty");

        Object value = "testValue";
        propertyPointer.createPath(context, value);
        assertEquals(value, propertyPointer.getImmediateNode());
    }

    @Test
    public void testCreateChild() {
        PropertyPointer parent = new SimplePropertyPointer();
        parent.setNode("test");
        propertyPointer.setParent(parent);
        propertyPointer.setBaseValue("testObject");
        propertyPointer.setPropertyName("child");

        QName name = new QName("testNS", "testChild");
        int index = 1;
        Object value = new Object();
        PropertyPointer child = (PropertyPointer) propertyPointer.createChild(JXPathContextReferenceImpl.getNode(context), name, index, value);
        assertNotNull(child);
        assertEquals(name.toString(), child.getPropertyName());
        assertEquals("testObject", propertyPointer.getBaseValue());
        assertEquals(value, child.getImmediateNode());
    }

    @Test
    public void testHashCode() {
        PropertyPointer parent = new SimplePropertyPointer();
        parent.setNode("test");
        propertyPointer.setParent(parent);
        propertyPointer.setPropertyName("testProperty");
        propertyPointer.setIndex(1);
        int hashCode = propertyPointer.hashCode();

        assertEquals(parent.hashCode() + propertyPointer.getPropertyIndex() + propertyPointer.getIndex(), hashCode);
    }

    @Test
    public void testEquals_True() {
        PropertyPointer parent1 = new SimplePropertyPointer();
        PropertyPointer parent2 = new SimplePropertyPointer();
        parent1.setNode("test");
        parent2.setNode("test");
        propertyPointer.setParent(parent1);
        PropertyPointer other = new SimplePropertyPointer();
        other.setParent(parent2);
        assertEquals(propertyPointer, other);
    }

    @Test
    public void testEquals_False() {
        PropertyPointer parent1 = new SimplePropertyPointer();
        PropertyPointer parent2 = new SimplePropertyPointer();
        parent1.setNode("test1");
        parent2.setNode("test2");
        propertyPointer.setParent(parent1);
        PropertyPointer other = new SimplePropertyPointer();
        other.setParent(parent2);
        assertNotEquals(propertyPointer, other);
    }

    @Test
    public void testCompareChildNodePointers() {
        NodePointer pointer1 = new SimplePropertyPointer();
        pointer1.setBaseValue("test1");
        NodePointer pointer2 = new SimplePropertyPointer();
        pointer2.setBaseValue("test2");
        assertEquals(0, propertyPointer.compareChildNodePointers(pointer1, pointer2));
    }

}