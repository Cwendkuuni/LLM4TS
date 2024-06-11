import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.stubbing.Answer;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
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
import static org.junit.Assert.*;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

    @Test
    public void answer_WhenToStringMethod_ReturnsMockDescription() {
        InvocationOnMock mockInvocation = createMockInvocation("toString");
        Object result = returnsEmptyValues.answer(mockInvocation);
        assertEquals("Mock for Object, hashCode: 0", result);
    }

    @Test
    public void answer_WhenCompareToMethod_AndReferencesAreSame_ReturnsZero() {
        InvocationOnMock mockInvocation = createMockInvocation("compareTo", Object.class);
        mockInvocation.getArguments()[0] = mockInvocation.getMock();
        Object result = returnsEmptyValues.answer(mockInvocation);
        assertEquals(0, result);
    }

    @Test
    public void answer_WhenCompareToMethod_AndReferencesAreDifferent_ReturnsOne() {
        InvocationOnMock mockInvocation = createMockInvocation("compareTo", Object.class);
        mockInvocation.getArguments()[0] = new Object();
        Object result = returnsEmptyValues.answer(mockInvocation);
        assertEquals(1, result);
    }

    @Test
    public void answer_WhenReturnTypeIsPrimitive_ReturnsDefaultValue() {
        InvocationOnMock mockInvocation = createMockInvocation("primitiveMethod");
        Object result = returnsEmptyValues.answer(mockInvocation);
        assertEquals(0, result);
    }

    @Test
    public void answer_WhenReturnTypeIsPrimitiveWrapper_ReturnsDefaultValue() {
        InvocationOnMock mockInvocation = createMockInvocation("integerMethod");
        Object result = returnsEmptyValues.answer(mockInvocation);
        assertEquals(0, result);
    }

    @Test
    public void answer_WhenReturnTypeIsCollection_ReturnsEmptyLinkedList() {
        InvocationOnMock mockInvocation = createMockInvocation("collectionMethod");
        Object result = returnsEmptyValues.answer(mockInvocation);
        assertTrue(result instanceof LinkedList);
        assertTrue(((LinkedList) result).isEmpty());
    }

    @Test
    public void answer_WhenReturnTypeIsSet_ReturnsEmptyHashSet() {
        InvocationOnMock mockInvocation = createMockInvocation("setMethod");
        Object result = returnsEmptyValues.answer(mockInvocation);
        assertTrue(result instanceof HashSet);
        assertTrue(((HashSet) result).isEmpty());
    }

    @Test
    public void answer_WhenReturnTypeIsSortedSet_ReturnsEmptyTreeSet() {
        InvocationOnMock mockInvocation = createMockInvocation("sortedSetMethod");
        Object result = returnsEmptyValues.answer(mockInvocation);
        assertTrue(result instanceof TreeSet);
        assertTrue(((TreeSet) result).isEmpty());
    }

    @Test
    public void answer_WhenReturnTypeIsLinkedHashSet_ReturnsEmptyLinkedHashSet() {
        InvocationOnMock mockInvocation = createMockInvocation("linkedHashSetMethod");
        Object result = returnsEmptyValues.answer(mockInvocation);
        assertTrue(result instanceof LinkedHashSet);
        assertTrue(((LinkedHashSet) result).isEmpty());
    }

    @Test
    public void answer_WhenReturnTypeIsList_ReturnsEmptyLinkedList() {
        InvocationOnMock mockInvocation = createMockInvocation("listMethod");
        Object result = returnsEmptyValues.answer(mockInvocation);
        assertTrue(result instanceof LinkedList);
        assertTrue(((LinkedList) result).isEmpty());
    }

    @Test
    public void answer_WhenReturnTypeIsArrayList_ReturnsEmptyArrayList() {
        InvocationOnMock mockInvocation = createMockInvocation("arrayListMethod");
        Object result = returnsEmptyValues.answer(mockInvocation);
        assertTrue(result instanceof ArrayList);
        assertTrue(((ArrayList) result).isEmpty());
    }

    @Test
    public void answer_WhenReturnTypeIsMap_ReturnsEmptyHashMap() {
        InvocationOnMock mockInvocation = createMockInvocation("mapMethod");
        Object result = returnsEmptyValues.answer(mockInvocation);
        assertTrue(result instanceof HashMap);
        assertTrue(((HashMap) result).isEmpty());
    }

    @Test
    public void answer_WhenReturnTypeIsSortedMap_ReturnsEmptyTreeMap() {
        InvocationOnMock mockInvocation = createMockInvocation("sortedMapMethod");
        Object result = returnsEmptyValues.answer(mockInvocation);
        assertTrue(result instanceof TreeMap);
        assertTrue(((TreeMap) result).isEmpty());
    }

    @Test
    public void answer_WhenReturnTypeIsLinkedHashMap_ReturnsEmptyLinkedHashMap() {
        InvocationOnMock mockInvocation = createMockInvocation("linkedHashMapMethod");
        Object result = returnsEmptyValues.answer(mockInvocation);
        assertTrue(result instanceof LinkedHashMap);
        assertTrue(((LinkedHashMap) result).isEmpty());
    }

    private InvocationOnMock createMockInvocation(String methodName, Class<?>... parameterTypes) {
        ObjectMethodsGuru methodsGuru = new ObjectMethodsGuru();
        MockUtil mockUtil = new MockUtil();
        Object mock = new Object();
        MockName name = mockUtil.getMockName(mock);
        return new InvocationOnMock() {
            @Override
            public Object getMock() {
                return mock;
            }

            @Override
            public Object[] getArguments() {
                return new Object[parameterTypes.length];
            }

            @Override
            public java.lang.reflect.Method getMethod() {
                try {
                    return ObjectMethodsGuru.class.getMethod(methodName, parameterTypes);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}