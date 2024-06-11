package org.mockito.internal.creation;

import org.junit.Test;
import org.junit.Assert.*;

public class DelegatingMethodTest {

    @Test
    public void testGetName() {
        String expectedName = "someMethodName";
        Method method = new MockitoMethod() {
            @Override
            public String getName() {
                return expectedName;
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String actualName = delegatingMethod.getName();
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetExceptionTypesEmpty() {
        Class[] expectedExceptionTypes = new Class[0];
        Method method = new MockitoMethod() {
            @Override
            public Class<?>[] getExceptionTypes() {
                return expectedExceptionTypes;
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] actualExceptionTypes = delegatingMethod.getExceptionTypes();
        assertArrayEquals(expectedExceptionTypes, actualExceptionTypes);
    }

    @Test
    public void testGetExceptionTypesSingleException() {
        Class expectedExceptionType = RuntimeException.class;
        Method method = new MockitoMethod() {
            @Override
            public Class<?>[] getExceptionTypes() {
                return new Class[] {expectedExceptionType};
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] actualExceptionTypes = delegatingMethod.getExceptionTypes();
        assertArrayEquals(new Class[] {expectedExceptionType}, actualExceptionTypes);
    }

    @Test
    public void testIsAbstractTrue() {
        Method method = new MockitoMethod() {
            @Override
            public boolean isAbstract() {
                return true;
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertTrue(delegatingMethod.isAbstract());
    }

    @Test
    public void testIsAbstractFalse() {
        Method method = new MockitoMethod() {
            @Override
            public boolean isAbstract() {
                return false;
            }
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertFalse(delegatingMethod.isAbstract());
    }
}