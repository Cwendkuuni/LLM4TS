
import org.junit.Assert;
import org.junit.Test;

public class PropertyPointerTest {
    
    @Test
    public void testGetPropertyIndex() {
        NodePointer parentPointer = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer) {
            @Override
            public String getPropertyName() {
                return null;
            }

            @Override
            public void setPropertyName(String propertyName) {

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
        };

        propertyPointer.setPropertyIndex(1);
        int expected = 1;
        int actual = propertyPointer.getPropertyIndex();
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testGetBean() {
        NodePointer parentPointer = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer) {
            @Override
            public String getPropertyName() {
                return null;
            }

            @Override
            public void setPropertyName(String propertyName) {

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
        };

        propertyPointer.setBean("test");
        Object expected = "test";
        Object actual = propertyPointer.getBean();
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testGetName() {
        NodePointer parentPointer = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer) {
            @Override
            public String getPropertyName() {
                return "test";
            }

            @Override
            public void setPropertyName(String propertyName) {

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
        };

        QName expected = new QName(null, "test");
        QName actual = propertyPointer.getName();
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testGetLength() {
        NodePointer parentPointer = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer) {
            @Override
            public String getPropertyName() {
                return null;
            }

            @Override
            public void setPropertyName(String propertyName) {

            }

            @Override
            public int getPropertyCount() {
                return 5;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[0];
            }

            @Override
            protected boolean isActualProperty() {
                return false;
            }
        };

        int expected = 5;
        int actual = propertyPointer.getLength();
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testGetImmediateValuePointer() {
        NodePointer parentPointer = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer) {
            @Override
            public String getPropertyName() {
                return null;
            }

            @Override
            public void setPropertyName(String propertyName) {

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
        };

        NodePointer expected = new NodePointer();
        NodePointer actual = propertyPointer.getImmediateValuePointer();
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext();
        NodePointer parentPointer = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer) {
            @Override
            public String getPropertyName() {
                return null;
            }

            @Override
            public void setPropertyName(String propertyName) {

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
            public String asPath() {
                return "test";
            }
        };

        NodePointer expected = new NodePointer();
        NodePointer actual = propertyPointer.createPath(context);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = new JXPathContext();
        NodePointer parentPointer = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer) {
            @Override
            public String getPropertyName() {
                return null;
            }

            @Override
            public void setPropertyName(String propertyName) {

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
            public String asPath() {
                return "test";
            }
            
            @Override
            public Object getBaseValue() {
                return null;
            }
        };

        NodePointer expected = new NodePointer();
        NodePointer actual = propertyPointer.createPath(context, "test");
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testCreateChildWithIndex() {
        JXPathContext context = new JXPathContext();
        QName name = null;
        int index = 0;
        NodePointer parentPointer = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer) {
            @Override
            public String getPropertyName() {
                return null;
            }

            @Override
            public void setPropertyName(String propertyName) {

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
            public NodePointer createPath(JXPathContext context, Object value) {
                return new NodePointer();
            }
        };

        NodePointer expected = new NodePointer();
        NodePointer actual = propertyPointer.createChild(context, name, index);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals() {
        NodePointer parentPointer1 = new NodePointer();
        NodePointer parentPointer2 = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer1) {
            @Override
            public String getPropertyName() {
                return null;
            }

            @Override
            public void setPropertyName(String propertyName) {

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
        };
        
        PropertyPointer propertyPointer2 = new PropertyPointer(parentPointer2) {
            @Override
            public String getPropertyName() {
                return null;
            }

            @Override
            public void setPropertyName(String propertyName) {

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
        };

        boolean expected = true;
        boolean actual = propertyPointer.equals(propertyPointer);
        Assert.assertEquals(expected, actual);
        
        expected = false;
        actual = propertyPointer.equals(propertyPointer2);
        Assert.assertEquals(expected, actual);
        
        expected = false;
        actual = propertyPointer.equals(null);
        Assert.assertEquals(expected, actual);
        
        expected = false;
        actual = propertyPointer.equals(new String());
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testCompareChildNodePointers() {
        NodePointer pointer1 = new NodePointer();
        NodePointer pointer2 = new NodePointer();
        NodePointer parentPointer = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer) {
            @Override
            public String getPropertyName() {
                return null;
            }

            @Override
            public void setPropertyName(String propertyName) {

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
                return new NodePointer();
            }
        };

        int expected = 0;
        int actual = propertyPointer.compareChildNodePointers(pointer1, pointer2);
        Assert.assertEquals(expected, actual);
    }
}
