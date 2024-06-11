package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.jxpath.AbstractFactory;
import org.apache.commons.jxpath.JXPathAbstractFactoryException;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathIntrospector;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Before;
import org.junit.Test;

public class PropertyPointerTest {
    
    private NodePointer parent;
    private PropertyPointer propertyPointer;

    @Before
    public void setUp() {
        parent = mock(NodePointer.class);
        propertyPointer = new PropertyPointerImpl(parent);
    }

    @Test
    public void testGetPropertyIndex() {
        assertEquals(PropertyPointer.UNSPECIFIED_PROPERTY, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testSetPropertyIndex() {
        propertyPointer.setPropertyIndex(1);
        assertEquals(1, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        Object bean = new Object();
        when(parent.getNode()).thenReturn(bean);
        assertEquals(bean, propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        QName name = new QName(null, "");
        assertEquals(name, propertyPointer.getName());
    }

    private class PropertyPointerImpl extends PropertyPointer {
        
        private String propertyName;

        public PropertyPointerImpl(NodePointer parent) {
            super(parent);
        }

        @Override
        public String getPropertyName() {
            return propertyName;
        }

        @Override
        public void setPropertyName(String propertyName) {
            this.propertyName = propertyName;
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
        protected boolean isActualProperty() {
            return false;
        }

        @Override
        public NodePointer getValuePointer() {
            return null;
        }

        @Override
        public NodePointer newInstance(NodePointer parentPointer) {
            return null;
        }

        @Override
        public boolean isCollection() {
            return false;
        }

        @Override
        public boolean isLeaf() {
            return false;
        }

        @Override
        public int getLength() {
            return 0;
        }

        @Override
        public void setValue(Object value) {

        }

        @Override
        public AbstractFactory getAbstractFactory(JXPathContext context) {
            return null;
        }
    }
}