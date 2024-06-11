import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.TestBean;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

    @Test
    public void testPropertyPointerToExistingProperty() {
        TestBean bean = new TestBean();
        bean.setNumber(1);
        bean.setString("Test");

        JXPathContext context = new JXPathContext(bean);

        PropertyPointer pointer = new PropertyPointer(NodePointer.ROOT) {
            @Override
            public String getPropertyName() {
                return "number";
            }
        };

        Object result = pointer.createPath(context).getValue(context);
        assertEquals(1, result);

        pointer.setPropertyName("string");
        result = pointer.createPath(context).getValue(context);
        assertEquals("Test", result);
    }

    @Test
    public void testPropertyPointerToNonExistentProperty() {
        TestBean bean = new TestBean();

        JXPathContext context = new JXPathContext(bean);

        PropertyPointer pointer = new PropertyPointer(NodePointer.ROOT) {
            @Override
            public String getPropertyName() {
                return "nonExistentProperty";
            }
        };

        try {
            pointer.createPath(context).getValue(context);
            fail("Expected JXPathAbstractFactoryException");
        } catch (JXPathAbstractFactoryException e) {
            // Expected
        }
    }

    @Test
    public void testPropertyPointerToCollectionProperty() {
        TestBean bean = new TestBean();
        bean.setList(new int[] {1, 2, 3});

        JXPathContext context = new JXPathContext(bean);

        PropertyPointer pointer = new PropertyPointer(NodePointer.ROOT) {
            @Override
            public String getPropertyName() {
                return "list";
            }
        };

        Object result = pointer.createPath(context).getValue(context);
        assertArrayEquals(new int[] {1, 2, 3}, (int[]) result);

        pointer.setIndex(1);
        result = pointer.createPath(context).getValue(context);
        assertEquals(2, result);
    }

    @Test
    public void testPropertyPointerToCollectionPropertyOutOfBounds() {
        TestBean bean = new TestBean();
        bean.setList(new int[] {1, 2, 3});

        JXPathContext context = new JXPathContext(bean);

        PropertyPointer pointer = new PropertyPointer(NodePointer.ROOT) {
            @Override
            public String getPropertyName() {
                return "list";
            }
        };

        try {
            pointer.setIndex(3);
            pointer.createPath(context).getValue(context);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testPropertyPointerToActualProperty() {
        TestBean bean = new TestBean();
        bean.setNumber(1);
        bean.setString("Test");

        JXPathContext context = new JXPathContext(bean);

        PropertyPointer pointer = new PropertyPointer(NodePointer.ROOT) {
            @Override
            public String getPropertyName() {
                return "number";
            }

            @Override
            protected boolean isActualProperty() {
                return true;
            }
        };

        Object result = pointer.createPath(context).getValue(context);
        assertEquals(1, result);

        try {
            pointer.setPropertyName("string");
            pointer.createPath(context).getValue(context);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPropertyPointerToNonActualProperty() {
        TestBean bean = new TestBean();

        JXPathContext context = new JXPathContext(bean);

        PropertyPointer pointer = new PropertyPointer(NodePointer.ROOT) {
            @Override
            public String getPropertyName() {
                return "nonActualProperty";
            }

            @Override
            protected boolean isActualProperty() {
                return false;
            }
        };

        try {
            pointer.createPath(context).getValue(context);
            fail("Expected JXPathAbstractFactoryException");
        } catch (JXPathAbstractFactoryException e) {
            // Expected
        }
    }
}