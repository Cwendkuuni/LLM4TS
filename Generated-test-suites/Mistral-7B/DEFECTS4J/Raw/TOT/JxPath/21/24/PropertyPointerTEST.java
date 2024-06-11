package org.apache.commons.jxpath.ri.model.beans;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PropertyPointerTest {

    private PropertyPointer propertyPointer;
    private Object bean;

    @Before
    public void setUp() {
        bean = new DummyBean("Test");
        propertyPointer = new DummyPropertyPointer(new DummyParentPointer(null));
        propertyPointer.setBean(bean);
        propertyPointer.setPropertyName("name");
        propertyPointer.setPropertyIndex(0);
    }

    @Test
    public void testGetPropertyIndex() {
        int expectedPropertyIndex = 0;
        Assert.assertEquals("Unexpected property index", expectedPropertyIndex, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        Assert.assertEquals("Unexpected bean", bean, propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        String expectedPropertyName = "name";
        QName actualPropertyName = propertyPointer.getName();
        String actualPropertyNameString = actualPropertyName.getLocalPart();
        Assert.assertEquals("Unexpected property name", expectedPropertyName, actualPropertyNameString);
    }

    @Test
    public void testIsActual() {
        Assert.assertTrue("Property pointer should be actual", propertyPointer.isActual());
    }

    @Test
    public void testGetImmediateNode() {
        String expectedPropertyValue = "Test";
        Object actualPropertyValue = propertyPointer.getImmediateNode();
        Assert.assertEquals("Unexpected property value", expectedPropertyValue, actualPropertyValue);
    }

    @Test
    public void testIsCollection() {
        Assert.assertFalse("Property pointer should not be a collection", propertyPointer.isCollection());
    }

    @Test
    public void testGetLength() {
        int expectedLength = 1;
        Assert.assertEquals("Unexpected length", expectedLength, propertyPointer.getLength());
    }

    @Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext(bean);
        NodePointer actualPath = propertyPointer.createPath(context);
        String expectedPathString = "/" + propertyPointer.getPropertyName();
        String actualPathString = actualPath.asPath();
        Assert.assertEquals("Unexpected path", expectedPathString, actualPathString);
    }

    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = new JXPathContext(bean);
        String newPropertyValue = "New Value";
        NodePointer actualPath = propertyPointer.createPath(context, newPropertyValue);
        String expectedPathString = "/" + propertyPointer.getPropertyName();
        String actualPathString = actualPath.asPath();
        Assert.assertEquals("Unexpected path", expectedPathString, actualPathString);
        Assert.assertEquals("Unexpected new property value", newPropertyValue, actualPath.getNode().toString());
    }

    @Test
    public void testGetImmediateValuePointer() {
        NodePointer expectedValuePointer = new DummyValuePropertyPointer(propertyPointer);
        NodePointer actualValuePointer = propertyPointer.getImmediateValuePointer();
        Assert.assertEquals("Unexpected value pointer", expectedValuePointer, actualValuePointer);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateChild() {
        QName childName = new QName("", "child");
        int childIndex = 0;
        Object childValue = new DummyChildValue();
        propertyPointer.createChild(new JXPathContext(bean), childName, childIndex, childValue);
    }

    @Test
    public void testCreateChildWithName() {
        QName childName = new QName("", "child");
        NodePointer expectedChildPointer = new DummyChildPropertyPointer(propertyPointer, childName);
        NodePointer actualChildPointer = propertyPointer.createChild(new JXPathContext(bean), childName, 0);
        Assert.assertEquals("Unexpected child pointer", expectedChildPointer, actualChildPointer);
    }

    @Test
    public void testCreateChildWithIndex() {
        int childIndex = 1;
        Object childValue = new DummyChildValue();
        NodePointer expectedChildPointer = new DummyChildPropertyPointer(propertyPointer, new QName("", "child"));
        NodePointer actualChildPointer = propertyPointer.createChild(new JXPathContext(bean), null, childIndex, childValue);
        Assert.assertEquals("Unexpected child pointer", expectedChildPointer, actualChildPointer);
        Assert.assertEquals("Unexpected child value", childValue, actualChildPointer.getNode().toString());
    }

    @Test
    public void testHashCode() {
        PropertyPointer anotherPropertyPointer = new DummyPropertyPointer(new DummyParentPointer(null));
        anotherPropertyPointer.setBean(bean);
        anotherPropertyPointer.setPropertyName("name");
        anotherPropertyPointer.setPropertyIndex(0);
        int expectedHashCode = 90643 * 123 + 0 + 0;
        int actualHashCode = propertyPointer.hashCode();
        int anotherActualHashCode = anotherPropertyPointer.hashCode();
        Assert.assertEquals("Unexpected hash code", expectedHashCode, actualHashCode);
        Assert.assertEquals("Unexpected another hash code", expectedHashCode, anotherActualHashCode);
    }

    @Test
    public void testEquals() {
        PropertyPointer anotherPropertyPointer = new DummyPropertyPointer(new DummyParentPointer(null));
        anotherPropertyPointer.setBean(bean);
        anotherPropertyPointer.setPropertyName("name");
        anotherPropertyPointer.setPropertyIndex(0);
        Assert.assertTrue("Property pointers should be equal", propertyPointer.equals(anotherPropertyPointer));
    }

    @Test
    public void testUnequalPropertyPointers() {
        PropertyPointer anotherPropertyPointer = new DummyPropertyPointer(new DummyParentPointer(null));
        anotherPropertyPointer.setBean(new DummyBean("AnotherTest"));
        anotherPropertyPointer.setPropertyName("anotherName");
        anotherPropertyPointer.setPropertyIndex(1);
        Assert.assertFalse("Property pointers should not be equal", propertyPointer.equals(anotherPropertyPointer));
    }

    @Test
    public void testUnequalPropertyPointersWithDifferentParents() {
        PropertyPointer anotherPropertyPointer = new DummyPropertyPointer(new DummyParentPointer2(null));
        anotherPropertyPointer.setBean(bean);
        anotherPropertyPointer.setPropertyName("name");
        anotherPropertyPointer.setPropertyIndex(0);
        Assert.assertFalse("Property pointers should not be equal", propertyPointer.equals(anotherPropertyPointer));
    }

    @Test
    public void testUnequalPropertyPointersWithDifferentNames() {
        PropertyPointer anotherPropertyPointer = new DummyPropertyPointer(new DummyParentPointer(null));
        anotherPropertyPointer.setBean(bean);
        anotherPropertyPointer.setPropertyName("anotherName");
        anotherPropertyPointer.setPropertyIndex(0);
        Assert.assertFalse("Property pointers should not be equal", propertyPointer.equals(anotherPropertyPointer));
    }

    @Test
    public void testUnequalPropertyPointersWithDifferentIndices() {
        PropertyPointer anotherPropertyPointer = new DummyPropertyPointer(new DummyParentPointer(null));
        anotherPropertyPointer.setBean(bean);
        anotherPropertyPointer.setPropertyName("name");
        anotherPropertyPointer.setPropertyIndex(1);
        Assert.assertFalse("Property pointers should not be equal", propertyPointer.equals(anotherPropertyPointer));
    }

    @Test
    public void testUnequalPropertyPointersWithNull() {
        Assert.assertFalse("Property pointer should not be equal to null", propertyPointer.equals(null));
    }

    private static class DummyBean {
        private String name;

        public DummyBean(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public Object getValue(int index) {
            return name;
        }
    }

    private static class DummyParentPointer extends NodePointer {
        public DummyParentPointer(NodePointer parent) {
            super(parent);
        }
    }

    private static class DummyPropertyPointer extends PropertyPointer {
        public DummyPropertyPointer(NodePointer parent) {
            super(parent);
        }

        @Override
        public String getPropertyName() {
            return "name";
        }

        @Override
        public void setPropertyName(String propertyName) {
            // Empty implementation
        }

        @Override
        public int getPropertyCount() {
            return 1;
        }

        @Override
        public String[] getPropertyNames() {
            return new String[]{"name"};
        }

        @Override
        public boolean isActualProperty() {
            return true;
        }
    }

    private static class DummyValuePropertyPointer extends NodePointer {
        private DummyPropertyPointer propertyPointer;

        public DummyValuePropertyPointer(DummyPropertyPointer propertyPointer) {
            super(propertyPointer);
            this.propertyPointer = propertyPointer;
        }

        @Override
        public boolean isActual() {
            return propertyPointer.isActual();
        }
    }

    private static class DummyChildPropertyPointer extends NodePointer {
        private DummyPropertyPointer propertyPointer;
        private QName childName;

        public DummyChildPropertyPointer(DummyPropertyPointer propertyPointer, QName childName) {
            super(propertyPointer);
            this.propertyPointer = propertyPointer;
            this.childName = childName;
        }

        @Override
        public boolean isActual() {
            return propertyPointer.isActual();
        }

        @Override
        public String getName() {
            return childName.toString();
        }
    }

    private static class DummyChildValue {
        // Empty implementation
    }

    private static class DummyParentPointer2 extends NodePointer {
        public DummyParentPointer2(NodePointer parent) {
            super(parent);
        }
    }
}