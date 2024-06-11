package org.apache.commons.jxpath.ri.model.beans;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class PropertyPointerTest {

    @Mock
    private NodePointer mockParent;
    private PropertyPointer propertyPointer;

    @Before
    public void setup() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        propertyPointer = new PropertyPointer(mockParent) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }
        };
    }

    @Test
    public void testGetPropertyIndex() {
        Assert.assertEquals(-1, propertyPointer.getPropertyIndex());
        propertyPointer.setPropertyIndex(1);
        Assert.assertEquals(1, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        Object mockBean = Mockito.mock(Object.class);
        Mockito.when(mockParent.getNode()).thenReturn(mockBean);
        Assert.assertEquals(mockBean, propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        Assert.assertEquals(new QName("test", "testProperty"), propertyPointer.getName());
    }

    @Test
    public void testGetPropertyCount() throws Exception {
        Mockito.when(mockParent.getNode()).thenReturn(new Object() {
            String[] names = {"testProperty1", "testProperty2"};

            public String[] getPropertyNames() {
                return names;
            }
        });
        Assert.assertEquals(2, propertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() throws Exception {
        Mockito.when(mockParent.getNode()).thenReturn(new Object() {
            String[] names = {"testProperty1", "testProperty2"};

            public String[] getPropertyNames() {
                return names;
            }
        });
        Assert.assertArrayEquals(new String[]{"testProperty1", "testProperty2"}, propertyPointer.getPropertyNames());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsActualPropertyUnsupported() {
        Mockito.when(mockParent.getNode()).thenReturn(new Object() {
            // Empty class
        });
        propertyPointer.isActualProperty();
    }

    @Test
    public void testIsActualPropertyTrue() {
        Mockito.when(mockParent.getNode()).thenReturn(new Object() {
            public String testProperty;

            public String getPropertyName() {
                return "testProperty";
            }
        });
        Assert.assertTrue(propertyPointer.isActualProperty());
    }

    @Test
    public void testIsActualTrue() {
        Mockito.when(mockParent.getNode()).thenReturn(new Object());
        Assert.assertTrue(propertyPointer.isActual());
    }

    @Test
    public void testGetImmediateNodeUNinitialized() {
        Mockito.when(mockParent.getNode()).thenReturn(new Object());
        Assert.assertNull(propertyPointer.getImmediateNode());
    }

    @Test
    public void testGetImmediateNodeInitialized() {
        Mockito.when(mockParent.getNode()).thenReturn(new Object() {
            public String testProperty = "testValue";
        });
        Assert.assertEquals("testValue", propertyPointer.getImmediateNode());
    }

    @Test
    public void testIsCollectionFalse() {
        Mockito.when(mockParent.getNode()).thenReturn(new Object() {
            public String testProperty;
        });
        Assert.assertFalse(propertyPointer.isCollection());
    }

    @Test
    public void testIsCollectionTrue() {
        Mockito.when(mockParent.getNode()).thenReturn(new Object() {
            public String[] testProperty;
        });
        Assert.assertTrue(propertyPointer.isCollection());
    }

    @Test
    public void testGetLengthZero() {
        Mockito.when(mockParent.getNode()).thenReturn(new Object() {
            public String testProperty;
        });
        Assert.assertEquals(0, propertyPointer.getLength());
    }

    @Test
    public void testGetLengthNonZero() {
        Mockito.when(mockParent.getNode()).thenReturn(new Object() {
            public String[] testProperty = {"testValue1", "testValue2"};
        });
        Assert.assertEquals(2, propertyPointer.getLength());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetImmediateValuePointerInvalidIndex() {
        Mockito.when(mockParent.getNode()).thenReturn(new Object() {
            public String[] testProperty = {"testValue1", "testValue2"};
        });
        propertyPointer.setIndex(3);
        propertyPointer.getImmediateValuePointer();
    }

    @Test
    public void testGetImmediateValuePointerValidIndex() {
        Mockito.when(mockParent.getNode()).thenReturn(new Object() {
            public String[] testProperty = {"testValue1", "testValue2"};
        });
        propertyPointer.setIndex(0);
        Assert.assertNotNull(propertyPointer.getImmediateValuePointer());
    }

    @Test
    public void testCreatePath() throws Exception {
        JXPathContext context = Mockito.mock(JXPathContext.class);
        Mockito.when(mockParent.getNode()).thenReturn(new Object() {
            public String testProperty;
        });
        propertyPointer.createPath(context);
        Mockito.verify(context).createNode(Mockito.eq(propertyPointer.getName()), Mockito.eq(propertyPointer.getBean()), Mockito.eq(propertyPointer.getPropertyIndex()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatePathNullValue() throws Exception {
        JXPathContext context = Mockito.mock(JXPathContext.class);
        Mockito.when(mockParent.getNode()).thenReturn(new Object() {
            public String testProperty;
        });
        propertyPointer.createPath(context, null);
    }

    @Test
    public void testCreatePathValidValue() throws Exception {
        JXPathContext context = Mockito.mock(JXPathContext.class);
        Mockito.when(mockParent.getNode()).thenReturn(new Object() {
            public String testProperty;
        });
        propertyPointer.createPath(context, "testValue");
        Mockito.verify(context).createNode(Mockito.eq(propertyPointer.getName()), Mockito.eq(propertyPointer.getBean()), Mockito.eq(propertyPointer.getPropertyIndex()), Mockito.eq("testValue"));
    }

    @Test
    public void testCreateChildPath() throws Exception {
        JXPathContext context = Mockito.mock(JXPathContext.class);
        Mockito.when(mockParent.getNode()).thenReturn(new Object() {
            public String testProperty;
        });
        PropertyPointer childPropertyPointer = new PropertyPointer(propertyPointer) {
            @Override
            public String getPropertyName() {
                return "testChildProperty";
            }
        };
        childPropertyPointer.createPath(context, "testValue");
        Mockito.verify(context).createNode(Mockito.eq(propertyPointer.getName()), Mockito.eq(propertyPointer.getBean()), Mockito.eq(childPropertyPointer.getPropertyIndex()), Mockito.eq("testValue"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateChildPathNullName() throws Exception {
        JXPathContext context = Mockito.mock(JXPathContext.class);
        Mockito.when(mockParent.getNode()).thenReturn(new Object() {
            public String testProperty;
        });
        PropertyPointer childPropertyPointer = new PropertyPointer(propertyPointer) {
            @Override
            public String getPropertyName() {
                return null;
            }
        };
        childPropertyPointer.createPath(context, "testValue");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCreateChildPathInvalidIndex() throws Exception {
        JXPathContext context = Mockito.mock(JXPathContext.class);
        Mockito.when(mockParent.getNode()).thenReturn(new Object() {
            public String testProperty;
            public String[] getPropertyNames() {
                return new String[]{"testProperty1", "testProperty2"};
            }
        });
        PropertyPointer childPropertyPointer = new PropertyPointer(propertyPointer) {
            @Override
            public String getPropertyName() {
                return "testProperty2";
            }
        };
        childPropertyPointer.createPath(context, "testValue");
    }

    @Test
    public void testCreateChildPathValidIndex() throws Exception {
        JXPathContext context = Mockito.mock(JXPathContext.class);
        Mockito.when(mockParent.getNode()).thenReturn(new Object() {
            public String testProperty;
            public String[] getPropertyNames() {
                return new String[]{"testProperty1", "testProperty2"};
            }
        });
        PropertyPointer childPropertyPointer = new PropertyPointer(propertyPointer) {
            @Override
            public String getPropertyName() {
                return "testProperty1";
            }
        };
        childPropertyPointer.createPath(context, "testValue");
        Mockito.verify(context).createNode(Mockito.eq(propertyPointer.getName()), Mockito.eq(propertyPointer.getBean()), Mockito.eq(childPropertyPointer.getPropertyIndex()), Mockito.eq("testValue"));
    }
}