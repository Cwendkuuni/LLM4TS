package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer() {
        ReturnsEmptyValues rev = new ReturnsEmptyValues();

        // Test toString() method
        assertNotNull(rev.answer(new InvocationOnMock() {
            public Object getMock() {
                return null;
            }

            public java.lang.reflect.Method getMethod() {
                try {
                    return getClass().getMethod("toString");
                } catch (NoSuchMethodException | SecurityException e) {
                    e.printStackTrace();
                }
                return null;
            }

            public Object[] getArguments() {
                return null;
            }
        }));

        // Test compareTo() method
        assertEquals(0, rev.answer(new InvocationOnMock() {
            public Object getMock() {
                return null;
            }

            public java.lang.reflect.Method getMethod() {
                try {
                    return getClass().getMethod("compareTo", Object.class);
                } catch (NoSuchMethodException | SecurityException e) {
                    e.printStackTrace();
                }
                return null;
            }

            public Object[] getArguments() {
                return new Object[] { this };
            }
        }));

        // Test primitive and wrapper return types
        assertEquals(false, rev.answer(new InvocationOnMock() {
            public Object getMock() {
                return null;
            }

            public java.lang.reflect.Method getMethod() {
                try {
                    return getClass().getMethod("returnValueFor", Class.class);
                } catch (NoSuchMethodException | SecurityException e) {
                    e.printStackTrace();
                }
                return null;
            }

            public Object[] getArguments() {
                return new Object[] { boolean.class };
            }
        }));

        // Test collection return types
        assertEquals(new LinkedList<Object>(), rev.answer(new InvocationOnMock() {
            public Object getMock() {
                return null;
            }

            public java.lang.reflect.Method getMethod() {
                try {
                    return getClass().getMethod("returnValueFor", Class.class);
                } catch (NoSuchMethodException | SecurityException e) {
                    e.printStackTrace();
                }
                return null;
            }

            public Object[] getArguments() {
                return new Object[] { Collection.class };
            }
        }));

        // Test set return types
        assertEquals(new HashSet<Object>(), rev.answer(new InvocationOnMock() {
            public Object getMock() {
                return null;
            }

            public java.lang.reflect.Method getMethod() {
                try {
                    return getClass().getMethod("returnValueFor", Class.class);
                } catch (NoSuchMethodException | SecurityException e) {
                    e.printStackTrace();
                }
                return null;
            }

            public Object[] getArguments() {
                return new Object[] { Set.class };
            }
        }));

        // Test list return types
        assertEquals(new LinkedList<Object>(), rev.answer(new InvocationOnMock() {
            public Object getMock() {
                return null;
            }

            public java.lang.reflect.Method getMethod() {
                try {
                    return getClass().getMethod("returnValueFor", Class.class);
                } catch (NoSuchMethodException | SecurityException e) {
                    e.printStackTrace();
                }
                return null;
            }

            public Object[] getArguments() {
                return new Object[] { List.class };
            }
        }));

        // Test map return types
        assertEquals(new HashMap<Object, Object>(), rev.answer(new InvocationOnMock() {
            public Object getMock() {
                return null;
            }

            public java.lang.reflect.Method getMethod() {
                try {
                    return getClass().getMethod("returnValueFor", Class.class);
                } catch (NoSuchMethodException | SecurityException e) {
                    e.printStackTrace();
                }
                return null;
            }

            public Object[] getArguments() {
                return new Object[] { Map.class };
            }
        }));
    }
}