package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.AbstractPropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.MapPropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.ListPropertyPointer;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Arrays;

public class PropertyPointerTest {

    @Test
    public void testAbstractPropertyPointer() {
        AbstractPropertyPointer pointer = new AbstractPropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }

            @Override
            public int getPropertyIndex() {
                return 0;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"testProperty"};
            }

            @Override
            public int getPropertyCount() {
                return 1;
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        assertEquals("testProperty", pointer.getName().getLocalPart());
        assertEquals(0, pointer.getPropertyIndex());
        assertEquals(new String[]{"testProperty"}, pointer.getPropertyNames());
        assertEquals(1, pointer.getPropertyCount());
        assertTrue(pointer.isActualProperty());

        // Test equals and hashCode
        PropertyPointer samePointer = new AbstractPropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }

            @Override
            public int getPropertyIndex() {
                return 0;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"testProperty"};
            }

            @Override
            public int getPropertyCount() {
                return 1;
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        PropertyPointer differentPointer = new AbstractPropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "differentProperty";
            }

            @Override
            public int getPropertyIndex() {
                return 1;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"differentProperty"};
            }

            @Override
            public int getPropertyCount() {
                return 1;
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        assertEquals(pointer, samePointer);
        assertEquals(pointer.hashCode(), samePointer.hashCode());
        assertNotEquals(pointer, differentPointer);
        assertNotEquals(pointer.hashCode(), differentPointer.hashCode());
    }

    @Test
    public void testMapPropertyPointer() {
        Map<String, Object> map = new HashMap<>();
        map.put("testProperty1", "value1");
        map.put("testProperty2", "value2");

        MapPropertyPointer pointer = new MapPropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }

            @Override
            public int getPropertyIndex() {
                return 1;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"testProperty1", "testProperty2"};
            }

            @Override
            public int getPropertyCount() {
                return 2;
            }

            @Override
            public Object getBaseValue() {
                return map;
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        assertEquals("testProperty", pointer.getName().getLocalPart());
        assertEquals(1, pointer.getPropertyIndex());
        assertEquals(new String[]{"testProperty1", "testProperty2"}, pointer.getPropertyNames());
        assertEquals(2, pointer.getPropertyCount());
        assertTrue(pointer.isActualProperty());

        assertEquals("value1", pointer.getImmediateNode());
        assertFalse(pointer.isCollection());

        MapPropertyPointer samePointer = new MapPropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }

            @Override
            public int getPropertyIndex() {
                return 1;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"testProperty1", "testProperty2"};
            }

            @Override
            public int getPropertyCount() {
                return 2;
            }

            @Override
            public Object getBaseValue() {
                return map;
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        assertEquals(pointer, samePointer);
        assertEquals(pointer.hashCode(), samePointer.hashCode());

        MapPropertyPointer differentPointer = new MapPropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "differentProperty";
            }

            @Override
            public int getPropertyIndex() {
                return 2;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"testProperty1", "testProperty2", "differentProperty"};
            }

            @Override
            public int getPropertyCount() {
                return 3;
            }

            @Override
            public Object getBaseValue() {
                return map;
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        assertNotEquals(pointer, differentPointer);
        assertNotEquals(pointer.hashCode(), differentPointer.hashCode());
    }

    @Test
    public void testListPropertyPointer() {
        List<Object> list = Arrays.asList("value1", "value2", "value3");

        ListPropertyPointer pointer = new ListPropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }

            @Override
            public int getPropertyIndex() {
                return 1;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"testProperty"};
            }

            @Override
            public int getPropertyCount() {
                return list.size();
            }

            @Override
            public Object getBaseValue() {
                return list;
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        assertEquals("testProperty", pointer.getName().getLocalPart());
        assertEquals(1, pointer.getPropertyIndex());
        assertEquals(new String[]{"testProperty"}, pointer.getPropertyNames());
        assertEquals(3, pointer.getPropertyCount());
        assertTrue(pointer.isActualProperty());

        assertEquals("value2", pointer.getImmediateNode());
        assertTrue(pointer.isCollection());

        ListPropertyPointer samePointer = new ListPropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "testProperty";
            }

            @Override
            public int getPropertyIndex() {
                return 1;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"testProperty"};
            }

            @Override
            public int getPropertyCount() {
                return list.size();
            }

            @Override
            public Object getBaseValue() {
                return list;
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        assertEquals(pointer, samePointer);
        assertEquals(pointer.hashCode(), samePointer.hashCode());

        ListPropertyPointer differentPointer = new ListPropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "differentProperty";
            }

            @Override
            public int getPropertyIndex() {
                return 2;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"testProperty1", "testProperty2", "differentProperty"};
            }

            @Override
            public int getPropertyCount() {
                return list.size() + 1;
            }

            @Override
            public Object getBaseValue() {
                return list;
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };

        assertNotEquals(pointer, differentPointer);
        assertNotEquals(pointer.hashCode(), differentPointer.hashCode());
    }
}