import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.ReturnsDeepStubs;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mockList;
 private Object mockMap;

 @Before
 public void setup() {
 mockList = Mockito.mock(List.class, Mockito.withSettings().defaultAnswer(ReturnsDeepStubs.RETURNS_DEEP_STUBS));
 mockMap = Mockito.mock(Map.class, Mockito.withSettings().defaultAnswer(ReturnsDeepStubs.RETURNS_DEEP_STUBS));
 returnsDeepStubs = (ReturnsDeepStubs) Mockito.mock(ReturnsDeepStubs.class);
 }

 @Test
 public void testAnswerDeepStubForList() {
 // Given
 List<Integer> list = (List<Integer>) mockList.entrySet().iterator().next().getValue();

 // When
 Mockito.doReturn(list).when(mockList).iterator();

 // Then
 List<Integer> actualList = (List<Integer>) returnsDeepStubs.answer(Mockito.argThat(i -> i.getClass().isAssignableFrom(List.class)));

 // Verify
 verify(mockList).iterator();
 verify(actualList).getClass().isAssignableFrom(List.class);
 }

 @Test
 public void testAnswerDeepStubForMap() {
 // Given
 Map<String, Number> map = (Map<String, Number>) mockMap.entrySet().iterator().next().getValue();
 Number number = (Number) mockMap.entrySet().iterator().next().getValue().iterator().next();

 // When
 Mockito.doReturn(map).when(mockMap).entrySet().iterator();
 Mockito.doReturn(number).when(mockMap).entrySet().iterator().next().getValue().iterator().next();

 // Then
 Map<String, Number> actualMap = (Map<String, Number>) returnsDeepStubs.answer(Mockito.argThat(i -> i.getClass().isAssignableFrom(Map.class)));
 Number actualNumber = (Number) actualMap.entrySet().iterator().next().getValue();

 // Verify
 verify(mockMap).entrySet().iterator();
 verify(mockMap).entrySet().iterator().next().getValue().iterator();
 verify(actualMap).getClass().isAssignableFrom(Map.class);
 verify(actualNumber).getClass().isAssignableFrom(Number.class);
 }

 @Test(expected = Throwable.class)
 public void testAnswerDeepStubForNonMockableType() {
 // Given
 Object nonMockableType = new Object() {};

 // When & Then
 Mockito.doThrow(new RuntimeException("NonMockableType is not mockable")).when(returnsDeepStubs).answer(Mockito.argThat(i -> i.getClass().equals(nonMockableType.getClass())));

 returnsDeepStubs.answer(Mockito.argThat(i -> i.getClass().equals(nonMockableType.getClass())));
 }
}