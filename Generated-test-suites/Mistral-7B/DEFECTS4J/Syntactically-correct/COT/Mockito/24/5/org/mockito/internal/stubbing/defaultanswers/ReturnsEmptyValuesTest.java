import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 @Test
 public void testToStringMethod() {
 MockitoAnswer mockAnswer = mock(Answer.class);
 String mockToString = "Mockito Test Mock";
 when(mockAnswer.answer(Mockito.any(InvocationOnMock.class))).thenReturn(mockToString);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Object mock = mock(String.class);
 when(invocation.getMethod()).thenReturn(mock.getClass().getMethod("toString"));
 Object result = returnsEmptyValues.answer(invocation);
 assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), result);
 }

 @Test
 public void testCompareToMethod() {
 MockitoAnswer mockAnswer = mock(Answer.class);
 when(mockAnswer.answer(Mockito.any(InvocationOnMock.class))).thenReturn(1);
 Object mock1 = mock(Comparable.class);
 Object mock2 = mock(Comparable.class);
 when(mock1.compareTo(mock2)).thenReturn(answer(mockAnswer));
 int result = returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class)).compareTo(mock2);
 assertEquals(1, result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testEmptyCollectionModification() {
 List<Object> mockList = Mockito.mock(List.class);
 when(mockList.remove(0)).thenReturn(true);
 when(returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class))).thenReturn(mockList);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.getMethod().invoke(returnsEmptyValues, new Object[0]);
 //this should throw UnsupportedOperationException
 mockList.remove(0);
 }

 @Test
 public void testEmptySetModification() {
 Set<Object> mockSet = Mockito.mock(Set.class);
 when(mockSet.add(Mockito.any())).thenReturn(true);
 when(returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class))).thenReturn(mockSet);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.getMethod().invoke(returnsEmptyValues, new Object[0]);
 //this should not have any side effects
 mockSet.add(new Object());
 }

 @Test
 public void testNullReturnType() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Object result = returnsEmptyValues.answer(invocation);
 assertNull(result);
 }

}