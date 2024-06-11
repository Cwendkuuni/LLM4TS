import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

    @Test
    public void testGetPropertyIndex() {
        // Test the initial value of propertyIndex
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            public String getPropertyName() {
                return null;
            }

            public void setPropertyName(String propertyName) {

            }

            public int getPropertyCount() {
                return 0;
            }

            public String[] getPropertyNames() {
                return new String[0];
            }

            protected boolean isActualProperty() {
                return false;
            }
        };

        assertEquals(PropertyPointer.UNSPECIFIED_PROPERTY, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testSetPropertyIndex() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            public String getPropertyName() {
                return null;
            }

            public void setPropertyName(String propertyName) {

            }

            public int getPropertyCount() {
                return 0;
            }

            public String[] getPropertyNames() {
                return new String[0];
            }

            protected boolean isActualProperty() {
                return false;
            }
        };

        propertyPointer.setPropertyIndex(5);
        assertEquals(5, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetPropertyIndexChangeIndex() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            public String getPropertyName() {
                return null;
            }

            public void setPropertyName(String propertyName) {

            }

            public int getPropertyCount() {
                return 0;
            }

            public String[] getPropertyNames() {
                return new String[0];
            }

            protected boolean isActualProperty() {
                return false;
            }
        };

        propertyPointer.setPropertyIndex(5);
        propertyPointer.setPropertyIndex(7);
        assertEquals(7, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            public String getPropertyName() {
                return null;
            }

            public void setPropertyName(String propertyName) {

            }

            public int getPropertyCount() {
                return 0;
            }

            public String[] getPropertyNames() {
                return new String[0];
            }

            protected boolean isActualProperty() {
                return false;
            }
        };

        assertEquals(null, propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            public String getPropertyName() {
                return "propertyName";
            }

            public void setPropertyName(String propertyName) {

            }

            public int getPropertyCount() {
                return 0;
            }

            public String[] getPropertyNames() {
                return new String[0];
            }

            protected boolean isActualProperty() {
                return false;
            }
        };

        assertEquals(new QName(null, "propertyName"), propertyPointer.getName());
    }

    @Test
    public void testGetPropertyName() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            public String getPropertyName() {
                return "propertyName";
            }

            public void setPropertyName(String propertyName) {

            }

            public int getPropertyCount() {
                return 0;
            }

            public String[] getPropertyNames() {
                return new String[0];
            }

            protected boolean isActualProperty() {
                return false;
            }
        };

        assertEquals("propertyName", propertyPointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            public String getPropertyName() {
                return null;
            }

            public void setPropertyName(String propertyName) {
                assertEquals("propertyName", propertyName);
            }

            public int getPropertyCount() {
                return 0;
            }

            public String[] getPropertyNames() {
                return new String[0];
            }

            protected boolean isActualProperty() {
                return false;
            }
        };

        propertyPointer.setPropertyName("propertyName");
    }

    @Test
    public void testGetPropertyCount() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            public String getPropertyName() {
                return null;
            }

            public void setPropertyName(String propertyName) {

            }

            public int getPropertyCount() {
                return 5;
            }

            public String[] getPropertyNames() {
                return new String[0];
            }

            protected boolean isActualProperty() {
                return false;
            }
        };

        assertEquals(5, propertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            public String getPropertyName() {
                return null;
            }

            public void setPropertyName(String propertyName) {

            }

            public int getPropertyCount() {
                return 0;
            }

            public String[] getPropertyNames() {
                return new String[]{"prop1", "prop2", "prop3"};
            }

            protected boolean isActualProperty() {
                return false;
            }
        };

        assertArrayEquals(new String[]{"prop1", "prop2", "prop3"}, propertyPointer.getPropertyNames());
    }

    @Test
    public void testIsActualProperty() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            public String getPropertyName() {
                return null;
            }

            public void setPropertyName(String propertyName) {

            }

            public int getPropertyCount() {
                return 0;
            }

            public String[] getPropertyNames() {
                return new String[0];
            }

            protected boolean isActualProperty() {
                return true;
            }
        };

        assertTrue(propertyPointer.isActual());
    }

}