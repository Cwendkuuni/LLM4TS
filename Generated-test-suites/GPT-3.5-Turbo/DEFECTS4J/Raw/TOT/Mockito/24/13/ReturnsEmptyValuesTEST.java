import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues defaultAnswer;

    @Before
    public void setUp() {
        defaultAnswer = new ReturnsEmptyValues();
    }

    @Test
    public void testAnswerToStringWithDefaultMockName() {
        // Arrange
        InvocationOnMock invocation = createInvocation("toString");

        // Act
        Object result = defaultAnswer.answer(invocation);

        // Assert
        assertEquals("Mock for Object, hashCode: 0", result);
    }

    @Test
    public void testAnswerToStringWithCustomMockName() {
        // Arrange
        InvocationOnMock invocation = createInvocation("toString");
        MockName mockName = new MockName("customName");
        mockName.applyTo(invocation.getMock());

        // Act
        Object result = defaultAnswer.answer(invocation);

        // Assert
        assertEquals("customName", result);
    }

    @Test
    public void testAnswerCompareTo() {
        // Arrange
        InvocationOnMock invocation = createInvocation("compareTo");
        Object argument = new Object();
        invocation.getArgument(0);
        invocation.getArguments()[0] = argument;

        // Act
        Object result = defaultAnswer.answer(invocation);

        // Assert
        assertEquals(1, result);
    }

    @Test
    public void testAnswerPrimitiveReturnType() {
        // Arrange
        InvocationOnMock invocation = createInvocation("primitiveReturnType");

        // Act
        Object result = defaultAnswer.answer(invocation);

        // Assert
        assertEquals(0, result);
    }

    @Test
    public void testAnswerWrapperReturnType() {
        // Arrange
        InvocationOnMock invocation = createInvocation("wrapperReturnType");

        // Act
        Object result = defaultAnswer.answer(invocation);

        // Assert
        assertEquals(0, result);
    }

    @Test
    public void testAnswerCollectionReturnType() {
        // Arrange
        InvocationOnMock invocation = createInvocation("collectionReturnType");

        // Act
        Object result = defaultAnswer.answer(invocation);

        // Assert
        assertTrue(result instanceof Collection);
        assertEquals(0, ((Collection<?>) result).size());
    }

    @Test
    public void testAnswerSetReturnType() {
        // Arrange
        InvocationOnMock invocation = createInvocation("setReturnType");

        // Act
        Object result = defaultAnswer.answer(invocation);

        // Assert
        assertTrue(result instanceof Set);
        assertEquals(0, ((Set<?>) result).size());
    }

    @Test
    public void testAnswerHashSetReturnType() {
        // Arrange
        InvocationOnMock invocation = createInvocation("hashSetReturnType");

        // Act
        Object result = defaultAnswer.answer(invocation);

        // Assert
        assertTrue(result instanceof HashSet);
        assertEquals(0, ((HashSet<?>) result).size());
    }

    @Test
    public void testAnswerSortedSetReturnType() {
        // Arrange
        InvocationOnMock invocation = createInvocation("sortedSetReturnType");

        // Act
        Object result = defaultAnswer.answer(invocation);

        // Assert
        assertTrue(result instanceof Set);
        assertEquals(0, ((Set<?>) result).size());
    }

    @Test
    public void testAnswerTreeSetReturnType() {
        // Arrange
        InvocationOnMock invocation = createInvocation("treeSetReturnType");

        // Act
        Object result = defaultAnswer.answer(invocation);

        // Assert
        assertTrue(result instanceof Set);
        assertEquals(0, ((Set<?>) result).size());
    }

    @Test
    public void testAnswerLinkedHashSetReturnType() {
        // Arrange
        InvocationOnMock invocation = createInvocation("linkedHashSetReturnType");

        // Act
        Object result = defaultAnswer.answer(invocation);

        // Assert
        assertTrue(result instanceof Set);
        assertEquals(0, ((Set<?>) result).size());
    }

    @Test
    public void testAnswerListReturnType() {
        // Arrange
        InvocationOnMock invocation = createInvocation("listReturnType");

        // Act
        Object result = defaultAnswer.answer(invocation);

        // Assert
        assertTrue(result instanceof List);
        assertEquals(0, ((List<?>) result).size());
    }

    @Test
    public void testAnswerLinkedListReturnType() {
        // Arrange
        InvocationOnMock invocation = createInvocation("linkedListReturnType");

        // Act
        Object result = defaultAnswer.answer(invocation);

        // Assert
        assertTrue(result instanceof List);
        assertEquals(0, ((List<?>) result).size());
    }

    @Test
    public void testAnswerArrayListReturnType() {
        // Arrange
        InvocationOnMock invocation = createInvocation("arrayListReturnType");

        // Act
        Object result = defaultAnswer.answer(invocation);

        // Assert
        assertTrue(result instanceof List);
        assertEquals(0, ((List<?>) result).size());
    }

    @Test
    public void testAnswerMapReturnType() {
        // Arrange
        InvocationOnMock invocation = createInvocation("mapReturnType");

        // Act
        Object result = defaultAnswer.answer(invocation);

        // Assert
        assertTrue(result instanceof Map);
        assertEquals(0, ((Map<?, ?>) result).size());
    }

    @Test
    public void testAnswerHashMapReturnType() {
        // Arrange
        InvocationOnMock invocation = createInvocation("hashMapReturnType");

        // Act
        Object result = defaultAnswer.answer(invocation);

        // Assert
        assertTrue(result instanceof Map);
        assertEquals(0, ((Map<?, ?>) result).size());
    }

    @Test
    public void testAnswerSortedMapReturnType() {
        // Arrange
        InvocationOnMock invocation = createInvocation("sortedMapReturnType");

        // Act
        Object result = defaultAnswer.answer(invocation);

        // Assert
        assertTrue(result instanceof Map);
        assertEquals(0, ((Map<?, ?>) result).size());
    }

    @Test
    public void testAnswerTreeMapReturnType() {
        // Arrange
        InvocationOnMock invocation = createInvocation("treeMapReturnType");

        // Act
        Object result = defaultAnswer.answer(invocation);

        // Assert
        assertTrue(result instanceof Map);
        assertEquals(0, ((Map<?, ?>) result).size());
    }

    @Test
    public void testAnswerLinkedHashMapReturnType() {
        // Arrange
        InvocationOnMock invocation = createInvocation("linkedHashMapReturnType");

        // Act
        Object result = defaultAnswer.answer(invocation);

        // Assert
        assertTrue(result instanceof Map);
        assertEquals(0, ((Map<?, ?>) result).size());
    }

    private InvocationOnMock createInvocation(String methodName) {
        Answer<Object> answer = defaultAnswer;
        InvocationOnMock invocation = new InvocationOnMock() {
            @Override
            public Object getMock() {
                return new Object();
            }

            @Override
            public Object[] getArguments() {
                return new Object[] {};
            }

            @Override
            public <T> T getArgument(int index) {
                return null;
            }

            @Override
            public void setArgument(int index, Object value) {

            }

            @Override
            public Method getMethod() {
                try {
                    return Answer.class.getDeclaredMethod(methodName);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public Object callRealMethod() throws Throwable {
                return null;
            }

            @Override
            public Object callRealMethod(Object... arguments) throws Throwable {
                return null;
            }

            @Override
            public MockitoInvocationHandler getMockitoInterceptor() {
                return null;
            }

            @Override
            public void markVerified() {

            }

            @Override
            public void markStubbed(Void value) {

            }

            @Override
            public void markStubbed(Object value) {

            }

            @Override
            public Object[] getArgumentsWithMatchers() {
                return new Object[]{};
            }

            @Override
            public int getSequenceNumber() {
                return 0;
            }
        };
    return invocation;
    }
}