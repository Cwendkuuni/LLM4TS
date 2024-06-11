
package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ReturnsEmptyValuesTest {
    
    @Test
    public void testAnswer() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        
        // Test toString() method
        assertEquals("Mock for ClassA, hashCode: 123", answer.answer(invocationOnMock("toString")).toString());
        
        // Test compareTo() method
        assertEquals(0, answer.answer(invocationOnMock("compareTo", invocationOnMock("compareTo"))));
        assertEquals(1, answer.answer(invocationOnMock("compareTo", invocationOnMock("other"))));
        
        // Test primitive-returning methods
        assertEquals(0, answer.answer(invocationOnMock("intMethod")));
        assertEquals(false, answer.answer(invocationOnMock("booleanMethod")));
        assertEquals((byte)0, answer.answer(invocationOnMock("byteMethod")));
        assertEquals(' ', answer.answer(invocationOnMock("charMethod")));
        assertEquals((double)0, answer.answer(invocationOnMock("doubleMethod")));
        assertEquals((float)0, answer.answer(invocationOnMock("floatMethod")));
        assertEquals(0, answer.answer(invocationOnMock("intMethod")));
        assertEquals(0, answer.answer(invocationOnMock("longMethod")));
        assertEquals((short)0, answer.answer(invocationOnMock("shortMethod")));
        
        // Test Collection-returning methods
        assertEquals(false, answer.answer(invocationOnMock("collectionMethod")) instanceof Collection);
        assertEquals(false, answer.answer(invocationOnMock("setMethod")) instanceof Set);
        assertEquals(false, answer.answer(invocationOnMock("hashSetMethod")) instanceof HashSet);
        assertEquals(false, answer.answer(invocationOnMock("sortedSetMethod")) instanceof SortedSet);
        assertEquals(false, answer.answer(invocationOnMock("treeSetMethod")) instanceof TreeSet);
        assertEquals(false, answer.answer(invocationOnMock("linkedHashSetMethod")) instanceof LinkedHashSet);
        assertEquals(false, answer.answer(invocationOnMock("listMethod")) instanceof List);
        assertEquals(false, answer.answer(invocationOnMock("linkedListMethod")) instanceof LinkedList);
        assertEquals(false, answer.answer(invocationOnMock("arrayListMethod")) instanceof ArrayList);
        
        // Test Map-returning methods
        assertEquals(false, answer.answer(invocationOnMock("mapMethod")) instanceof Map);
        assertEquals(false, answer.answer(invocationOnMock("hashMapMethod")) instanceof HashMap);
        assertEquals(false, answer.answer(invocationOnMock("sortedMapMethod")) instanceof SortedMap);
        assertEquals(false, answer.answer(invocationOnMock("treeMapMethod")) instanceof TreeMap);
        assertEquals(false, answer.answer(invocationOnMock("linkedHashMapMethod")) instanceof LinkedHashMap);
        
        // Test other methods
        assertNull(answer.answer(invocationOnMock("otherMethod")));
    }
    
    private InvocationOnMock invocationOnMock(String methodName, Object... arguments) {
        return new InvocationBuilder().method(methodName).arguments(arguments).build();
    }
    
    private class InvocationBuilder {
        private String methodName;
        private Object[] arguments;
        
        public InvocationBuilder method(String methodName) {
            this.methodName = methodName;
            return this;
        }
        
        public InvocationBuilder arguments(Object... arguments) {
            this.arguments = arguments;
            return this;
        }
        
        public InvocationOnMock build() {
            return new InvocationImpl(methodName, arguments);
        }
        
        private class InvocationImpl implements InvocationOnMock {
            private String methodName;
            private Object[] arguments;

            public InvocationImpl(String methodName, Object[] arguments) {
                this.methodName = methodName;
                this.arguments = arguments;
            }

            public Object[] getArguments() {
                return arguments;
            }

            public void setArguments(Object[] arguments) {
                this.arguments = arguments;
            }

            public Object getMock() {
                return null;
            }

            public void setMock(Object mock) {}

            public Object callRealMethod() throws Throwable {
                return null;
            }

            public String toString() {
                return null;
            }

            public Object getArgument(int index) {
                return arguments[index];
            }

            public Class<?> getMockitoMethodReturnType() {
                return null;
            }

            public Method getMethod() {
                return null;
            }

            public Object callRealMethodUnchecked() {
                return null;
            }

            public void markVerified() {}
        }
    }
}
