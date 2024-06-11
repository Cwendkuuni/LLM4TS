
package org.mockito.internal.creation;

import org.junit.Test;
import org.junit.Assert;
import java.lang.reflect.Method;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = new MethodStub() {
            @Override
            public Class<?>[] getExceptionTypes() {
                return new Class[] {Exception1.class, Exception2.class};
            }
        }.getMock();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        Assert.assertArrayEquals(new Class<?>[] {Exception1.class, Exception2.class}, exceptionTypes);
    }

    @Test
    public void testGetName() {
        Method method = new MethodStub() {
            @Override
            public String getName() {
                return "testMethod";
            }
        }.getMock();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        String name = delegatingMethod.getName();
        Assert.assertEquals("testMethod", name);
    }

    @Test
    public void testIsAbstract() {
        Method method = new MethodStub() {
            @Override
            public boolean isAbstract() {
                return false;
            }
        }.getMock();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean isAbstract = delegatingMethod.isAbstract();
        Assert.assertFalse(isAbstract);

        method = new MethodStub() {
            @Override
            public boolean isAbstract() {
                return true;
            }
        }.getMock();
        delegatingMethod = new DelegatingMethod(method);

        isAbstract = delegatingMethod.isAbstract();
        Assert.assertTrue(isAbstract);
    }

    static class MethodStub implements Method {
        private Class<?>[] exceptionTypes;
        private String name;
        private boolean isAbstract;

        public Class<?>[] getExceptionTypes() {
            return exceptionTypes;
        }

        public void setExceptionTypes(Class<?>[] exceptionTypes) {
            this.exceptionTypes = exceptionTypes;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isAbstract() {
            return isAbstract;
        }

        public void setAbstract(boolean isAbstract) {
            this.isAbstract = isAbstract;
        }

        public Method getMock() {
            // Mockito method to create a mocked method object
            // ...
            return this;
        }
    }

    static class Exception1 {}
    static class Exception2 {}
}