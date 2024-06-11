import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

    @Test
    public void testPropertyPointerWithActualProperty() {
        Object parent = new Object() {
            String property1 = "test";
            String property2 = "test2";
        };

        PropertyPointer propertyPointer1 = new PropertyPointer(NodePointer.newChildNodePointer(null, null, parent)) {
            @Override
            public String getPropertyName() {
                return "property1";
            }

            @Override
            public int getPropertyIndex() {
                return 0;
            }

            @Override
            public int getPropertyCount() {
                return 2;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"property1", "property2"};
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        JXPathContext context = new JXPathContext(parent);
        Object result = propertyPointer1.createPath(context).getValue(context);
        assertEquals("test", result);
    }

    @Test
    public void testPropertyPointerWithNonExistentProperty() {
        Object parent = new Object() {
            String property1 = "test";
        };

        PropertyPointer propertyPointer1 = new PropertyPointer(NodePointer.newChildNodePointer(null, null, parent)) {
            @Override
            public String getPropertyName() {
                return "property3";
            }

            @Override
            public int getPropertyIndex() {
                return 0;
            }

            @Override
            public int getPropertyCount() {
                return 1;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"property1"};
            }

            @Override
            public boolean isActualProperty() {
                return false;
            }
        };

        JXPathContext context = new JXPathContext(parent);
        try {
            propertyPointer1.createPath(context).getValue(context);
            fail("Expected JXPathAbstractFactoryException not thrown");
        } catch (JXPathAbstractFactoryException e) {
            // Expected
        }
    }

    @Test
    public void testPropertyPointerWithCollectionProperty() {
        Object parent = new Object() {
            String[] properties = {"test1", "test2", "test3"};
        };

        PropertyPointer propertyPointer1 = new PropertyPointer(NodePointer.newChildNodePointer(null, null, parent)) {
            @Override
            public String getPropertyName() {
                return "properties";
            }

            @Override
            public int getPropertyIndex() {
                return 1;
            }

            @Override
            public int getPropertyCount() {
                return 3;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"properties"};
            }

            @Override
            public boolean isActualProperty() {
                return false;
            }
        };

        JXPathContext context = new JXPathContext(parent);
        Object result = propertyPointer1.createPath(context).getValue(context);
        assertEquals("test2", result);
    }

    @Test
    public void testPropertyPointerWithNegativeIndex() {
        Object parent = new Object() {
            String[] properties = {"test1", "test2", "test3"};
        };

        PropertyPointer propertyPointer1 = new PropertyPointer(NodePointer.newChildNodePointer(null, null, parent)) {
            @Override
            public String getPropertyName() {
                return "properties";
            }

            @Override
            public int getPropertyIndex() {
                return -1;
            }

            @Override
            public int getPropertyCount() {
                return 3;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"properties"};
            }

            @Override
            public boolean isActualProperty() {
                return false;
            }
        };

        JXPathContext context = new JXPathContext(parent);
        try {
            propertyPointer1.createPath(context).getValue(context);
            fail("Expected IndexOutOfBoundsException not thrown");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }
}