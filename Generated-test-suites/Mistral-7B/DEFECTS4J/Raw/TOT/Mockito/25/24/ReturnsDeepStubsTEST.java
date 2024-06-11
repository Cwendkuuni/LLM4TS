import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.ReturnsDeepStubs;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object nestedMap;

 @Before
 public void setUp() {
 returnsDeepStubs = new ReturnsDeepStubs();

 Class<?> genericsMapClass = GenericMetadataSupport.inferFrom(Map.class);
 Class<?> genericNestedMapClass = GenericMetadataSupport.inferFrom(Map.class, Map.class);

 nestedMap = mock(genericNestedMapClass, returnsDeepStubs);
 }

 @Test
 public void testDeepStubsWithSimpleTypes() {
 Object simpleMock = mock(Object.class, returnsDeepStubs);

 Object simpleReturnValue = simpleMock;
 Object deepSimpleReturnValue = returnsDeepStubs.answer(Mockito.argThat(i -> i.getMethod().getName().equals("answer")));

 assert (simpleMock == deepSimpleReturnValue);
 }

 @Test
 public void testDeepStubsWithNestedTypes() {
 Object mapMock = mock(Map.class, returnsDeepStubs);
 Object nestedMapMock = mapMock;

 List<Map.Entry<String, Number>> mapEntryList = new ArrayList<>();
 Map.Entry<String, Number> mapEntry = Mockito.argThat(i -> i.getValue().getClass().isInstance(new Integer(1)));
 Map<String, Number> map = new HashMap<>();
 map.put(ArgumentMatchers.anyString(), mapEntry);
 mapEntryList.add(map.entrySet().iterator().next());

 verify(mapMock).entrySet().iterator().next();
 verify(mapMock).entrySet().iterator().next().getValue().iterator().next();

 Object deepNestedMapReturnValue = returnsDeepStubs.answer(Mockito.argThat(i -> i.getMethod().getName().equals("answer")));

 assert (mapMock == deepNestedMapReturnValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeepStubsWithUnmockableType() {
 Class<List<String>> listClass = (Class<List<String>>) List.class;
 Object listMock = mock(listClass, returnsDeepStubs);

 throwsException(IllegalArgumentException.class, () -> returnsDeepStubs.answer(Mockito.argThat(i -> i.getMethod().getName().equals("answer"))));
 }
}