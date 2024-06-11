package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerForToStringMethod() {
        Object mock = mock(String.class);
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        InvocationOnMock invocation = mock.mockingDetails().getMockingDetails().getInvocations().get(0);

        String expected = "Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode();
        String actual = (String) answer.answer(invocation);

        assertEquals(expected, actual);
    }

    @Test
    public void testAnswerForCompareToMethod() {
        Object mock1 = mock(Comparable.class);
        Object mock2 = mock(Comparable.class);

        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        int actual = ((Comparable) answer).compareTo(mock1);
        int expected = 1;

        assertEquals(expected, actual);

        actual = ((Comparable) answer).compareTo(mock2);
        assertNotSame(expected, actual);
    }

    @Test
    public void testAnswerForPrimitiveTypes() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        byte actualByte = (byte) answer.answer(mock(ByteType.class).mockingDetails().getMockingDetails().getInvocations().get(0));
        byte expectedByte = Primitives.byteValue;
        assertEquals(expectedByte, actualByte);

        short actualShort = (short) answer.answer(mock(ShortType.class).mockingDetails().getMockingDetails().getInvocations().get(0));
        short expectedShort = Primitives.shortValue;
        assertEquals(expectedShort, actualShort);

        int actualInt = (int) answer.answer(mock(IntegerType.class).mockingDetails().getMockingDetails().getInvocations().get(0));
        int expectedInt = Primitives.intValue;
        assertEquals(expectedInt, actualInt);

        long actualLong = (long) answer.answer(mock(LongType.class).mockingDetails().getMockingDetails().getInvocations().get(0));
        long expectedLong = Primitives.longValue;
        assertEquals(expectedLong, actualLong);

        float actualFloat = (float) answer.answer(mock(FloatType.class).mockingDetails().getMockingDetails().getInvocations().get(0));
        float expectedFloat = Primitives.floatValue;
        assertEquals(expectedFloat, actualFloat, 0.0f);

        double actualDouble = (double) answer.answer(mock(DoubleType.class).mockingDetails().getMockingDetails().getInvocations().get(0));
        double expectedDouble = Primitives.doubleValue;
        assertEquals(expectedDouble, actualDouble, 0.0d);

        char actualChar = (char) answer.answer(mock(CharacterType.class).mockingDetails().getMockingDetails().getInvocations().get(0));
        char expectedChar = Primitives.charValue;
        assertEquals(expectedChar, actualChar);

        boolean actualBoolean = (boolean) answer.answer(mock(BooleanType.class).mockingDetails().getMockingDetails().getInvocations().get(0));
        boolean expectedBoolean = Primitives.booleanValue;
        assertEquals(expectedBoolean, actualBoolean);
    }

    @Test
    public void testAnswerForCollectionTypes() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        Collection<String> actualCollection = (Collection<String>) answer.answer(mock(CollectionType.class).mockingDetails().getMockingDetails().getInvocations().get(0));
        Collection<String> expectedCollection = Collections.emptyList();
        assertEquals(expectedCollection, actualCollection);

        Set<String> actualSet = (Set<String>) answer.answer(mock(SetType.class).mockingDetails().getMockingDetails().getInvocations().get(0));
        Set<String> expectedSet = Collections.emptySet();
        assertEquals(expectedSet, actualSet);

        HashSet<String> actualHashSet = (HashSet<String>) answer.answer(mock(HashSetType.class).mockingDetails().getMockingDetails().getInvocations().get(0));
        HashSet<String> expectedHashSet = new HashSet<>();
        assertEquals(expectedHashSet, actualHashSet);

        SortedSet<String> actualSortedSet = (SortedSet<String>) answer.answer(mock(SortedSetType.class).mockingDetails().getMockingDetails().getInvocations().get(0));
        SortedSet<String> expectedSortedSet = new TreeSet<>();
        assertEquals(expectedSortedSet, actualSortedSet);

        TreeSet<String> actualTreeSet = (TreeSet<String>) answer.answer(mock(TreeSetType.class).mockingDetails().getMockingDetails().getInvocations().get(0));
        TreeSet<String> expectedTreeSet = new TreeSet<>();
        assertEquals(expectedTreeSet, actualTreeSet);

        LinkedHashSet<String> actualLinkedHashSet = (LinkedHashSet<String>) answer.answer(mock(LinkedHashSetType.class).mockingDetails().getMockingDetails().getInvocations().get(0));
        LinkedHashSet<String> expectedLinkedHashSet = new LinkedHashSet<>();
        assertEquals(expectedLinkedHashSet, actualLinkedHashSet);
    }

    @Test
    public void testAnswerForListTypes() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        List<String> actualList = (List<String>) answer.answer(mock(ListType.class).mockingDetails().getMockingDetails().getInvocations().get(0));
        List<String> expectedList = new LinkedList<>();
        assertEquals(expectedList, actualList);

        LinkedList<String> actualLinkedList = (LinkedList<String>) answer.answer(mock(LinkedListType.class).mockingDetails().getMockingDetails().getInvocations().get(0));
        LinkedList<String> expectedLinkedList = new LinkedList<>();
        assertEquals(expectedLinkedList, actualLinkedList);

        ArrayList<String> actualArrayList = (ArrayList<String>) answer.answer(mock(ArrayListType.class).mockingDetails().getMockingDetails().getInvocations().get(0));
        ArrayList<String> expectedArrayList = new ArrayList<>();
        assertEquals(expectedArrayList, actualArrayList);
    }

    @Test
    public void testAnswerForMapTypes() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        Map<String, String> actualMap = (Map<String, String>) answer.answer(mock(MapType.class).mockingDetails().getMockingDetails().getInvocations().get(0));
        Map<String, String> expectedMap = new HashMap<>();
        assertEquals(expectedMap, actualMap);

        HashMap<String, String> actualHashMap = (HashMap<String, String>) answer.answer(mock(HashMapType.class).mockingDetails().getMockingDetails().getInvocations().get(0));
        HashMap<String, String> expectedHashMap = new HashMap<>();
        assertEquals(expectedHashMap, actualHashMap);

        LinkedHashMap<String, String> actualLinkedHashMap = (LinkedHashMap<String, String>) answer.answer(mock(LinkedHashMapType.class).mockingDetails().getMockingDetails().getInvocations().get(0));
        LinkedHashMap<String, String> expectedLinkedHashMap = new LinkedHashMap<>();
        assertEquals(expectedLinkedHashMap, actualLinkedHashMap);
    }
}