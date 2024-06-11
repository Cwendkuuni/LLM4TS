
package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues defaultAnswer;

    @Before
    public void setup() {
        defaultAnswer = new ReturnsEmptyValues();
    }

    @Test
    public void testAnswerForToStringMethod() {
        // Arrange
        MockUtil mockUtil = new MockUtil();
        String expectedMockDescription = "Mock for String, hashCode: 12345";
        Object mock = "mock";
        ObjectMethodsGuru methodsGuru = new ObjectMethodsGuru();
        ObjectMethodsGuru spyMethodsGuru = Mockito.spy(methodsGuru);
        Mockito.when(spyMethodsGuru.isToString(Mockito.any())).thenReturn(true);
        Mockito.when(spyMethodsGuru.isToString(Mockito.mock(Method.class))).thenReturn(false);

        Mockito.when(mockUtil.getMockName(Mockito.any())).thenReturn(MockUtil.DEFAULT_NAME);
        Mockito.when(mockUtil.getMockSettings(Mockito.any()).getTypeToMock()).thenReturn(String.class);
        Mockito.when(mockUtil.getMockSettings(Mockito.any()).getMockedType()).thenReturn(String.class);

        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMock()).thenReturn(mock);
        Mockito.mock(invocation.getMethod());

        // Act
        Object result = defaultAnswer.answer(invocation);

        // Assert
        assertEquals(expectedMockDescription, result);
    }

    @Test
    public void testAnswerForCompareToMethod() {
        // Arrange
        ObjectMethodsGuru methodsGuru = new ObjectMethodsGuru();
        ObjectMethodsGuru spyMethodsGuru = Mockito.spy(methodsGuru);
        Mockito.when(spyMethodsGuru.isCompareToMethod(Mockito.any())).thenReturn(true);
        Mockito.when(spyMethodsGuru.isCompareToMethod(Mockito.mock(Method.class))).thenReturn(false);

        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.mock(invocation.getMethod());

        // Act
        Object result = defaultAnswer.answer(invocation);

        // Assert
        assertEquals(1, result);
    }

    @Test
    public void testAnswerForPrimitiveOrWrapperTypes() {
        // Arrange
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.mock(invocation.getMethod());

        // Act
        Object result1 = defaultAnswer.answer(invocation);
        Object result2 = defaultAnswer.answer(invocation);
        Object result3 = defaultAnswer.answer(invocation);
        Object result4 = defaultAnswer.answer(invocation);
        Object result5 = defaultAnswer.answer(invocation);
        Object result6 = defaultAnswer.answer(invocation);
        Object result7 = defaultAnswer.answer(invocation);
        Object result8 = defaultAnswer.answer(invocation);
        Object result9 = defaultAnswer.answer(invocation);
        Object result10 = defaultAnswer.answer(invocation);
        Object result11 = defaultAnswer.answer(invocation);
        Object result12 = defaultAnswer.answer(invocation);
        Object result13 = defaultAnswer.answer(invocation);
        Object result14 = defaultAnswer.answer(invocation);
        Object result15 = defaultAnswer.answer(invocation);
        Object result16 = defaultAnswer.answer(invocation);
        Object result17 = defaultAnswer.answer(invocation);
        Object result18 = defaultAnswer.answer(invocation);
        Object result19 = defaultAnswer.answer(invocation);
        Object result20 = defaultAnswer.answer(invocation);
        Object result21 = defaultAnswer.answer(invocation);
        Object result22 = defaultAnswer.answer(invocation);
        Object result23 = defaultAnswer.answer(invocation);
        Object result24 = defaultAnswer.answer(invocation);
        Object result25 = defaultAnswer.answer(invocation);

        // Assert
        assertEquals(0, result1);
        assertEquals(0, result2);
        assertEquals(0, result3);
        assertEquals(0, result4);
        assertEquals(0, result5);
        assertEquals(0, result6);
        assertEquals(0, result7);
        assertEquals(0, result8);
        assertEquals(0, result9);
        assertEquals(0, result10);
        assertEquals(0, result11);
        assertEquals(0, result12);
        assertEquals(0, result13);
        assertEquals(0, result14);
        assertEquals(0, result15);
        assertEquals(0, result16);
        assertEquals(0, result17);
        assertEquals(0, result18);
        assertEquals(0, result19);
        assertEquals(0, result20);
        assertEquals(0, result21);
        assertEquals(0, result22);
        assertEquals(0, result23);
        assertEquals(0, result24);
        assertEquals(0, result25);
    }

    @Test
    public void testAnswerForCollectionTypes() {
        // Arrange
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.mock(invocation.getMethod());

        // Act
        Object result1 = defaultAnswer.answer(invocation);
        Object result2 = defaultAnswer.answer(invocation);
        Object result3 = defaultAnswer.answer(invocation);
        Object result4 = defaultAnswer.answer(invocation);

        // Assert
        assertEquals(new LinkedList<Object>(), result1);
        assertEquals(new LinkedList<Object>(), result2);
        assertEquals(new LinkedList<Object>(), result3);
        assertEquals(new LinkedList<Object>(), result4);
    }

    @Test
    public void testAnswerForSetTypes() {
        // Arrange
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.mock(invocation.getMethod());

        // Act
        Object result1 = defaultAnswer.answer(invocation);
        Object result2 = defaultAnswer.answer(invocation);

        // Assert
        assertEquals(new HashSet<Object>(), result1);
        assertEquals(new HashSet<Object>(), result2);
    }

    @Test
    public void testAnswerForHashSetTypes() {
        // Arrange
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.mock(invocation.getMethod());

        // Act
        Object result = defaultAnswer.answer(invocation);

        // Assert
        assertEquals(new HashSet<Object>(), result);
    }

    @Test
    public void testAnswerForSortedSetTypes() {
        // Arrange
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.mock(invocation.getMethod());

        // Act
        Object result = defaultAnswer.answer(invocation);

        // Assert
        assertEquals(new TreeSet<Object>(), result);
    }

    @Test
    public void testAnswerForTreeSetTypes() {
        // Arrange
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.mock(invocation.getMethod());

        // Act
        Object result = defaultAnswer.answer(invocation);

        // Assert
        assertEquals(new TreeSet<Object>(), result);
    }

    @Test
    public void testAnswerForLinkedHashSetTypes() {
        // Arrange
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.mock(invocation.getMethod());

        // Act
        Object result = defaultAnswer.answer(invocation);

        // Assert
        assertEquals(new LinkedHashSet<Object>(), result);
    }

    @Test
    public void testAnswerForListTypes() {
        // Arrange
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.mock(invocation.getMethod());

        // Act
        Object result1 = defaultAnswer.answer(invocation);
        Object result2 = defaultAnswer.answer(invocation);

        // Assert
        assertEquals(new LinkedList<Object>(), result1);
        assertEquals(new LinkedList<Object>(), result2);
    }

    @Test
    public void testAnswerForLinkedListTypes() {
        // Arrange
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.mock(invocation.getMethod());

        // Act
        Object result = defaultAnswer.answer(invocation);

        // Assert
        assertEquals(new LinkedList<Object>(), result);
    }

    @Test
    public void testAnswerForArrayListTypes() {
        // Arrange
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.mock(invocation.getMethod());

        // Act
        Object result = defaultAnswer.answer(invocation);

        // Assert
        assertEquals(new ArrayList<Object>(), result);
    }

    @Test
    public void testAnswerForMapTypes() {
        // Arrange
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.mock(invocation.getMethod());

        // Act
        Object result1 = defaultAnswer.answer(invocation);
        Object result2 = defaultAnswer.answer(invocation);

        // Assert
        assertEquals(new HashMap<Object, Object>(), result1);
        assertEquals(new HashMap<Object, Object>(), result2);
    }

    @Test
    public void testAnswerForHashMapTypes() {
        // Arrange
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.mock(invocation.getMethod());

        // Act
        Object result = defaultAnswer.answer(invocation);

        // Assert
        assertEquals(new HashMap<Object, Object>(), result);
    }

    @Test
    public void testAnswerForSortedMapTypes() {
        // Arrange
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.mock(invocation.getMethod());

        // Act
        Object result = defaultAnswer.answer(invocation);

        // Assert
        assertEquals(new TreeMap<Object, Object>(), result);
    }

    @Test
    public void testAnswerForTreeMapTypes() {
        // Arrange
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.mock(invocation.getMethod());

        // Act
        Object result = defaultAnswer.answer(invocation);

        // Assert
        assertEquals(new TreeMap<Object, Object>(), result);
    }

    @Test
    public void testAnswerForLinkedHashMapTypes() {
        // Arrange
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.mock(invocation.getMethod());

        // Act
        Object result = defaultAnswer.answer(invocation);

        // Assert
        assertEquals(new LinkedHashMap<Object, Object>(), result);
    }

    @Test
    public void testAnswerForOtherTypes() {
        // Arrange
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.mock(invocation.getMethod());

        // Act
        Object result = defaultAnswer.answer(invocation);

        // Assert
        assertNull(result);
    }
}
