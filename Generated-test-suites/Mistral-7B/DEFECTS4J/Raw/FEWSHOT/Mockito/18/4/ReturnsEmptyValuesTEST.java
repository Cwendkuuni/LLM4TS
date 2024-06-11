package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.internal.util.Primitives.wrap;
import static org.mockito.internal.util.Primitives.unwrap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import org.junit.Test;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;

public class ReturnsEmptyValuesTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testAnswerThrowsUoE_WhenListIsModifed() {
        // given
        List<String> mockList = MockitoAnnotations.mockingDetails(new ReturnsEmptyValues()).getMock();

        // when
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        answer.answer(Mockito.argThat(i -> i.getMethod().getReturnType() == List.class));
        mockList.add("foo");

        // then
        // The test should fail with UnsupportedOperationException
    }

    @Test
    public void testAnswerToString() {
        // given
        Object mock = MockitoAnnotations.mockingDetails(new ReturnsEmptyValues()).getMock();

        // when
        String answer = new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getName().equals("toString"))).toString();

        // then
        MockName name = MockUtil.getMockName(mock);
        String expected = "Mock for " + MockUtil.getMockSettings(mock).getTypeToMock().getSimpleName() + ", hashCode: " + mock.hashCode();
        assertEquals(expected, answer);
    }

    @Test
    public void testAnswerCompareTo() {
        // given
        Object obj1 = MockitoAnnotations.mockingDetails(new ReturnsEmptyValues()).getMock();
        Object obj2 = MockitoAnnotations.mockingDetails(new ReturnsEmptyValues()).getMock();

        // when
        int answer1 = new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getName().equals("compareTo"))).answer(Mockito.eq(obj1)).getArgument(0);
        int answer2 = new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getName().equals("compareTo"))).answer(Mockito.eq(obj2)).getArgument(0);

        // then
        assertNull(answer1);
        assertNull(answer2);
        assertEquals(1, new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getName().equals("compareTo"))).answer(Mockito.eq(obj1)).answer(Mockito.eq(obj2)).getArgument(0));
    }

    @Test
    public void testAnswerPrimitiveBoolean() {
        // given
        boolean expected = true;
        Class<Boolean> booleanType = Boolean.TYPE;

        // when
        Boolean answer = (Boolean) new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getParameterTypes()[0].equals(booleanType) && i.getMethod().getName().equals("booleanValue"))).getArgument(0);

        // then
        assertEquals(expected, answer);
    }

    @Test
    public void testAnswerPrimitiveByte() {
        // given
        byte expected = 1;
        Class<Byte> byteType = Byte.TYPE;

        // when
        Byte answer = (Byte) new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getParameterTypes()[0].equals(byteType) && i.getMethod().getName().equals("byteValue"))).getArgument(0);

        // then
        assertEquals(expected, unwrap(answer));
    }

    @Test
    public void testAnswerPrimitiveShort() {
        // given
        short expected = 1;
        Class<Short> shortType = Short.TYPE;

        // when
        Short answer = (Short) new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getParameterTypes()[0].equals(shortType) && i.getMethod().getName().equals("shortValue"))).getArgument(0);

        // then
        assertEquals(expected, unwrap(answer));
    }

    @Test
    public void testAnswerPrimitiveInt() {
        // given
        int expected = 1;
        Class<Integer> intType = Integer.TYPE;

        // when
        Integer answer = (Integer) new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getParameterTypes()[0].equals(intType) && i.getMethod().getName().equals("intValue"))).getArgument(0);

        // then
        assertEquals(expected, unwrap(answer));
    }

    @Test
    public void testAnswerPrimitiveLong() {
        // given
        long expected = 1;
        Class<Long> longType = Long.TYPE;

        // when
        Long answer = (Long) new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getParameterTypes()[0].equals(longType) && i.getMethod().getName().equals("longValue"))).getArgument(0);

        // then
        assertEquals(expected, unwrap(answer));
    }

    @Test
    public void testAnswerPrimitiveFloat() {
        // given
        float expected = 1;
        Class<Float> floatType = Float.TYPE;

        // when
        Float answer = (Float) new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getParameterTypes()[0].equals(floatType) && i.getMethod().getName().equals("floatValue"))).getArgument(0);

        // then
        assertEquals(expected, unwrap(answer));
    }

    @Test
    public void testAnswerPrimitiveDouble() {
        // given
        double expected = 1;
        Class<Double> doubleType = Double.TYPE;

        // when
        Double answer = (Double) new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getParameterTypes()[0].equals(doubleType) && i.getMethod().getName().equals("doubleValue"))).getArgument(0);

        // then
        assertEquals(expected, answer);
    }

    @Test
    public void testAnswerPrimitiveChar() {
        // given
        char expected = '1';
        Class<Character> charType = Character.TYPE;

        // when
        Character answer = (Character) new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getParameterTypes()[0].equals(charType) && i.getMethod().getName().equals("charValue"))).getArgument(0);

        // then
        assertEquals(expected, unwrap(answer));
    }

    @Test
    public void testAnswerPrimitiveByteArray() {
        // given
        byte[] expected = new byte[]{1};
        Class<byte[]> byteArrayType = byte[].class;

        // when
        byte[] answer = (byte[]) new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getParameterTypes()[0].equals(byteArrayType) && i.getMethod().getName().equals("getValue"))).getArgument(0);

        // then
        assertArrayEquals(expected, answer);
    }

    @Test
    public void testAnswerEmptyList() {
        // given
        Class<List> listType = List.class;

        // when
        List answer = (List) new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getReturnType().equals(listType))).getArgument(0);

        // then
        assertTrue(answer instanceof LinkedList);
        assertTrue(answer.isEmpty());
    }

    @Test
    public void testAnswerEmptySet() {
        // given
        Class<Set> setType = Set.class;

        // when
        Set answer = (Set) new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getReturnType().equals(setType))).getArgument(0);

        // then
        assertTrue(answer instanceof HashSet);
        assertTrue(answer.isEmpty());
    }

    @Test
    public void testAnswerEmptyHashSet() {
        // given
        Class<HashSet> hashSetType = HashSet.class;

        // when
        Set answer = new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getReturnType().equals(hashSetType))).getArgument(0);

        // then
        assertTrue(answer instanceof HashSet);
        assertTrue(answer.isEmpty());
    }

    @Test
    public void testAnswerEmptyTreeSet() {
        // given
        Class<TreeSet> treeSetType = TreeSet.class;

        // when
        Set answer = new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getReturnType().equals(treeSetType))).getArgument(0);

        // then
        assertTrue(answer instanceof TreeSet);
        assertTrue(answer.isEmpty());
    }

    @Test
    public void testAnswerEmptyLinkedHashSet() {
        // given
        Class<LinkedHashSet> linkedHashSetType = LinkedHashSet.class;

        // when
        Set answer = new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getReturnType().equals(linkedHashSetType))).getArgument(0);

        // then
        assertTrue(answer instanceof LinkedHashSet);
        assertTrue(answer.isEmpty());
    }

    @Test
    public void testAnswerEmptyArrayList() {
        // given
        Class<List> listType = ArrayList.class;

        // when
        List answer = new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getReturnType().equals(listType))).getArgument(0);

        // then
        assertTrue(answer instanceof ArrayList);
        assertTrue(answer.isEmpty());
    }

    @Test
    public void testAnswerEmptyLinkedList() {
        // given
        Class<List> listType = LinkedList.class;

        // when
        List answer = new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getReturnType().equals(listType))).getArgument(0);

        // then
        assertTrue(answer instanceof LinkedList);
        assertTrue(answer.isEmpty());
    }

    @Test
    public void testAnswerEmptyArrayListWrapper() {
        // given
        Class<List> listType = ArrayList.class;
        Class<List> wrapperListType = List.class;

        // when
        List answer = new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getReturnType().equals(wrapperListType))).getArgument(0);

        // then
        assertTrue(answer instanceof ArrayList);
        assertTrue(answer.isEmpty());
    }

    @Test
    public void testAnswerEmptyMap() {
        // given
        Class<Map> mapType = Map.class;

        // when
        Map answer = (Map) new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getReturnType().equals(mapType))).getArgument(0);

        // then
        assertTrue(answer instanceof HashMap);
        assertTrue(answer.isEmpty());
    }

    @Test
    public void testAnswerEmptyHashMap() {
        // given
        Class<Map> mapType = HashMap.class;

        // when
        Map answer = new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getReturnType().equals(mapType))).getArgument(0);

        // then
        assertTrue(answer instanceof HashMap);
        assertTrue(answer.isEmpty());
    }

    @Test
    public void testAnswerEmptyTreeMap() {
        // given
        Class<Map> mapType = TreeMap.class;

        // when
        Map answer = new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getReturnType().equals(mapType))).getArgument(0);

        // then
        assertTrue(answer instanceof TreeMap);
        assertTrue(answer.isEmpty());
    }

    @Test
    public void testAnswerEmptyLinkedHashMap() {
        // given
        Class<Map> mapType = LinkedHashMap.class;

        // when
        Map answer = new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getReturnType().equals(mapType))).getArgument(0);

        // then
        assertTrue(answer instanceof LinkedHashMap);
        assertTrue(answer.isEmpty());
    }
}