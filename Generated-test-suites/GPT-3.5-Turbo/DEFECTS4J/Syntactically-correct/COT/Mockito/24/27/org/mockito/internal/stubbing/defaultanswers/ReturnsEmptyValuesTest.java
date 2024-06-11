import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;

import java.util.*;

import static org.junit.Assert.*;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnsEmptyValues;

    @Test
    public void testAnswer_toStringMethod_returnsMockDescription() {
        returnsEmptyValues = new ReturnsEmptyValues();
        String actual = (String) returnsEmptyValues.answer(new InvocationOnMock() {
            @Override
            public Object getMock() {
                return new Object();
            }

            @Override
            public Object[] getArguments() {
                return null;
            }

            @Override
            public Method getMethod() {
                try {
                    return this.getClass().getMethod("toString");
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public Object callRealMethod() throws Throwable {
                return null;
            }

            @Override
            public boolean isIgnoredForVerification() {
                return false;
            }
        });
        
        assertNotNull(actual);
        assertTrue(actual.startsWith("Mock for "));
        assertTrue(actual.contains(", hashCode:"));
    }

    @Test
    public void testAnswer_compareToMethod_returnsNonZero() {
        returnsEmptyValues = new ReturnsEmptyValues();
        int actual = (int) returnsEmptyValues.answer(new InvocationOnMock() {
            @Override
            public Object getMock() {
                return new Object();
            }

            @Override
            public Object[] getArguments() {
                return null;
            }

            @Override
            public Method getMethod() {
                try {
                    return this.getClass().getMethod("compareTo", Object.class);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public Object callRealMethod() throws Throwable {
                return null;
            }

            @Override
            public boolean isIgnoredForVerification() {
                return false;
            }
        });
        
        assertNotEquals(0, actual);
    }

    @Test
    public void testAnswer_primitiveReturnType_returnsDefaultValue() {
        returnsEmptyValues = new ReturnsEmptyValues();
        int actualInteger = (int) returnsEmptyValues.answer(new InvocationOnMock() {
            @Override
            public Object getMock() {
                return new Object();
            }

            @Override
            public Object[] getArguments() {
                return null;
            }

            @Override
            public Method getMethod() {
                try {
                    return this.getClass().getMethod("intMethod");
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public Object callRealMethod() throws Throwable {
                return null;
            }

            @Override
            public boolean isIgnoredForVerification() {
                return false;
            }
        });
        
        assertEquals(0, actualInteger);

        String actualString = (String) returnsEmptyValues.answer(new InvocationOnMock() {
            @Override
            public Object getMock() {
                return new Object();
            }

            @Override
            public Object[] getArguments() {
                return null;
            }

            @Override
            public Method getMethod() {
                try {
                    return this.getClass().getMethod("stringMethod");
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public Object callRealMethod() throws Throwable {
                return null;
            }

            @Override
            public boolean isIgnoredForVerification() {
                return false;
            }
        });

        assertNull(actualString);
    }

    @Test
    public void testAnswer_collectionReturnType_returnsEmptyCollection() {
        returnsEmptyValues = new ReturnsEmptyValues();
        Collection<Object> actualCollection = (Collection<Object>) returnsEmptyValues.answer(new InvocationOnMock() {
            @Override
            public Object getMock() {
                return new Object();
            }

            @Override
            public Object[] getArguments() {
                return null;
            }

            @Override
            public Method getMethod() {
                try {
                    return this.getClass().getMethod("collectionMethod");
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public Object callRealMethod() throws Throwable {
                return null;
            }

            @Override
            public boolean isIgnoredForVerification() {
                return false;
            }
        });

        assertNotNull(actualCollection);
        assertTrue(actualCollection.isEmpty());
    }

    public int intMethod() {
        return 0;
    }

    public String stringMethod() {
        return null;
    }

    public Collection<Object> collectionMethod() {
        return null;
    }
}