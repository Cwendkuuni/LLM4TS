import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Testing for {@link PropertyPointer} class.
 */
public class PropertyPointerTest {

    private PropertyPointer propertyPointer;
    private JXPathContext context;

    @Before
    public void setup() {
        context = Mockito.mock(JXPathContext.class);
        propertyPointer = new PropertyPointer(Mockito.mock(NodePointer.class)) {
            @Override
            public String getPropertyName() {
                return "testProperty";
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
    }

    @Test
    public void testGetPropertyIndex() {
        // Test getting property index when set
        propertyPointer.setPropertyIndex(0);
        Assert.assertEquals(0, propertyPointer.getPropertyIndex());

        // Test getting property index when not set
        propertyPointer.setPropertyIndex(PropertyPointer.UNSPECIFIED_PROPERTY);
        Assert.assertEquals(PropertyPointer.UNSPECIFIED_PROPERTY, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        Object bean = new Object();
        Mockito.when(propertyPointer.getImmediateParentPointer().getNode())
                .thenReturn(bean);
        Assert.assertEquals(bean, propertyPointer.getBean());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetBeanWhenNotInitialized() {
        propertyPointer.getBean();
    }

    @Test
    public void testGetName() {
        Assert.assertEquals("testProperty", propertyPointer.getName().getLocalPart());
    }

    @Test
    public void testIsActualProperty() {
        Assert.assertTrue(propertyPointer.isActual());
    }

    @Test
    public void testIsLeaf() {
        Assert.assertFalse(propertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        Assert.assertEquals(2, propertyPointer.getLength());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetImmediateNodeWhenIndexOutOfBounds() {
        propertyPointer.setIndex(3);
        propertyPointer.getImmediateNode();
    }

    @Test
    public void testCreatePath() {
        Mockito.when(context.createObject(
                Mockito.eq(propertyPointer),
                Mockito.eq(propertyPointer.getBean()),
                Mockito.eq("testProperty"),
                Mockito.eq(0)))
                .thenReturn(new Object());

        Assert.assertNotNull(propertyPointer.createPath(context));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatePathWithInvalidIndex() {
        propertyPointer.createPath(context, -1);
    }

    @Test
    public void testCreateChild() {
        PropertyPointer childPropertyPointer = new PropertyPointer(propertyPointer) {
            @Override
            public String getPropertyName() {
                return "testChildProperty";
            }
        };
        Object childValue = new Object();

        Mockito.when(context.createObject(
                Mockito.eq(childPropertyPointer),
                Mockito.eq(propertyPointer.getBean()),
                Mockito.eq("testChildProperty"),
                Mockito.eq(0)))
                .thenReturn(childValue);

        NodePointer childNodePointer = propertyPointer.createChild(context, null, 0, childValue);
        Assert.assertNotNull(childNodePointer);
        Assert.assertEquals(childPropertyPointer.getClass(), childNodePointer.getClass());
        Assert.assertEquals(childValue, childNodePointer.getNode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateChildWithNegativeIndex() {
        propertyPointer.createChild(context, null, -1);
    }

}