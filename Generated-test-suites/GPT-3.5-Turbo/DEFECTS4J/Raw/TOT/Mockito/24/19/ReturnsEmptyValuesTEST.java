import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsEmptyValuesTest {

	@Test
	public void testAnswerToStringMethodWithDefaultName() {
		ReturnsEmptyValues mockAnswer = new ReturnsEmptyValues();
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		when(invocation.getMethod()).thenReturn(Object.class.getMethods()[0]);
		Object result = mockAnswer.answer(invocation);
		assertEquals("Mock for Object, hashCode: 0", result);
	}

	@Test
	public void testAnswerToStringMethodWithCustomName() {
		ReturnsEmptyValues mockAnswer = new ReturnsEmptyValues();
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		when(invocation.getMethod()).thenReturn(Object.class.getMethods()[0]);
		Object mock = mock(Object.class);
		Mockito.when(Mockito.mockingDetails(mock).getMockName().toString()).thenReturn("CustomName");
		((Mockito) mock).returnsEmptyValues().answer(invocation);
		assertEquals("CustomName", result);
	}

	@Test
	public void testAnswerCompareToMethod() {
		ReturnsEmptyValues mockAnswer = new ReturnsEmptyValues();
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		when(invocation.getMethod()).thenReturn(Comparable.class.getMethods()[0]);
		Object result = mockAnswer.answer(invocation);
		assertEquals(1, result);
	}

	@Test
	public void testAnswerPrimitiveWrapperReturn() {
		ReturnsEmptyValues mockAnswer = new ReturnsEmptyValues();
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		when(invocation.getMethod()).thenReturn(Integer.class.getMethods()[0]);
		Object result = mockAnswer.answer(invocation);
		assertEquals(0, result);
	}

	@Test
	public void testAnswerPrimitiveReturn() {
		ReturnsEmptyValues mockAnswer = new ReturnsEmptyValues();
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		when(invocation.getMethod()).thenReturn(int.class.getMethods()[0]);
		Object result = mockAnswer.answer(invocation);
		assertEquals(0, result);
	}

	@Test
	public void testAnswerCollectionReturn() {
		ReturnsEmptyValues mockAnswer = new ReturnsEmptyValues();
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		when(invocation.getMethod()).thenReturn(Collection.class.getMethods()[0]);
		Object result = mockAnswer.answer(invocation);
		assertTrue(result instanceof LinkedList);
		assertTrue(((LinkedList<?>) result).isEmpty());
	}

	@Test
	public void testAnswerSetReturn() {
		ReturnsEmptyValues mockAnswer = new ReturnsEmptyValues();
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		when(invocation.getMethod()).thenReturn(Set.class.getMethods()[0]);
		Object result = mockAnswer.answer(invocation);
		assertTrue(result instanceof HashSet);
		assertTrue(((HashSet<?>) result).isEmpty());
	}

	@Test
	public void testAnswerHashSetReturn() {
		ReturnsEmptyValues mockAnswer = new ReturnsEmptyValues();
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		when(invocation.getMethod()).thenReturn(HashSet.class.getMethods()[0]);
		Object result = mockAnswer.answer(invocation);
		assertTrue(result instanceof HashSet);
		assertTrue(((HashSet<?>) result).isEmpty());
	}

	@Test
	public void testAnswerSortedSetReturn() {
		ReturnsEmptyValues mockAnswer = new ReturnsEmptyValues();
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		when(invocation.getMethod()).thenReturn(SortedSet.class.getMethods()[0]);
		Object result = mockAnswer.answer(invocation);
		assertTrue(result instanceof TreeSet);
		assertTrue(((TreeSet<?>) result).isEmpty());
	}

	@Test
	public void testAnswerTreeSetReturn() {
		ReturnsEmptyValues mockAnswer = new ReturnsEmptyValues();
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		when(invocation.getMethod()).thenReturn(TreeSet.class.getMethods()[0]);
		Object result = mockAnswer.answer(invocation);
		assertTrue(result instanceof TreeSet);
		assertTrue(((TreeSet<?>) result).isEmpty());
	}

	@Test
	public void testAnswerLinkedHashSetReturn() {
		ReturnsEmptyValues mockAnswer = new ReturnsEmptyValues();
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		when(invocation.getMethod()).thenReturn(LinkedHashSet.class.getMethods()[0]);
		Object result = mockAnswer.answer(invocation);
		assertTrue(result instanceof LinkedHashSet);
		assertTrue(((LinkedHashSet<?>) result).isEmpty());
	}

	@Test
	public void testAnswerListReturn() {
		ReturnsEmptyValues mockAnswer = new ReturnsEmptyValues();
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		when(invocation.getMethod()).thenReturn(List.class.getMethods()[0]);
		Object result = mockAnswer.answer(invocation);
		assertTrue(result instanceof LinkedList);
		assertTrue(((LinkedList<?>) result).isEmpty());
	}

	@Test
	public void testAnswerLinkedListReturn() {
		ReturnsEmptyValues mockAnswer = new ReturnsEmptyValues();
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		when(invocation.getMethod()).thenReturn(LinkedList.class.getMethods()[0]);
		Object result = mockAnswer.answer(invocation);
		assertTrue(result instanceof LinkedList);
		assertTrue(((LinkedList<?>) result).isEmpty());
	}

	@Test
	public void testAnswerArrayListReturn() {
		ReturnsEmptyValues mockAnswer = new ReturnsEmptyValues();
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		when(invocation.getMethod()).thenReturn(ArrayList.class.getMethods()[0]);
		Object result = mockAnswer.answer(invocation);
		assertTrue(result instanceof ArrayList);
		assertTrue(((ArrayList<?>) result).isEmpty());
	}

	@Test
	public void testAnswerMapReturn() {
		ReturnsEmptyValues mockAnswer = new ReturnsEmptyValues();
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		when(invocation.getMethod()).thenReturn(Map.class.getMethods()[0]);
		Object result = mockAnswer.answer(invocation);
		assertTrue(result instanceof HashMap);
		assertTrue(((HashMap<?, ?>) result).isEmpty());
	}

	@Test
	public void testAnswerHashMapReturn() {
		ReturnsEmptyValues mockAnswer = new ReturnsEmptyValues();
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		when(invocation.getMethod()).thenReturn(HashMap.class.getMethods()[0]);
		Object result = mockAnswer.answer(invocation);
		assertTrue(result instanceof HashMap);
		assertTrue(((HashMap<?, ?>) result).isEmpty());
	}

	@Test
	public void testAnswerSortedMapReturn() {
		ReturnsEmptyValues mockAnswer = new ReturnsEmptyValues();
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		when(invocation.getMethod()).thenReturn(SortedMap.class.getMethods()[0]);
		Object result = mockAnswer.answer(invocation);
		assertTrue(result instanceof TreeMap);
		assertTrue(((TreeMap<?, ?>) result).isEmpty());
	}

	@Test
	public void testAnswerTreeMapReturn() {
		ReturnsEmptyValues mockAnswer = new ReturnsEmptyValues();
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		when(invocation.getMethod()).thenReturn(TreeMap.class.getMethods()[0]);
		Object result = mockAnswer.answer(invocation);
		assertTrue(result instanceof TreeMap);
		assertTrue(((TreeMap<?, ?>) result).isEmpty());
	}

	@Test
	public void testAnswerLinkedHashMapReturn() {
		ReturnsEmptyValues mockAnswer = new ReturnsEmptyValues();
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		when(invocation.getMethod()).thenReturn(LinkedHashMap.class.getMethods()[0]);
		Object result = mockAnswer.answer(invocation);
		assertTrue(result instanceof LinkedHashMap);
		assertTrue(((LinkedHashMap<?, ?>) result).isEmpty());
	}

	@Test
	public void testAnswerNullValueForOtherMethods() {
		ReturnsEmptyValues mockAnswer = new ReturnsEmptyValues();
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethods()[0]);
		Object result = mockAnswer.answer(invocation);
		assertNull(result);
	}

}