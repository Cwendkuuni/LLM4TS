import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
public class FinalMockCandidateFilterTest {

 private FinalMockCandidateFilter filter;
 private Collection<Object> mocks;
 private Object fieldInstance;
 private Field field;

 @Before
 public void setup() {
 mocks = Arrays.asList(Mockito.mock(Object.class), Mockito.mock(Object.class));
 filter = new FinalMockCandidateFilter();
 fieldInstance = Mockito.mock(Object.class);
 field = Mockito.mock(Field.class);
 }

 @Test
 public void testFilterCandidate_oneMock_propertySetterSuccess() {
 Object mock = mocks.iterator().next();
 when(field.getName()).thenReturn("testField");
 when(field.getType()).thenReturn(String.class);
 doReturn(true).when(new BeanPropertySetter(fieldInstance, field)).set(mock);

 Object result = filter.filterCandidate(mocks, field, fieldInstance);

 assertSame(mock, result);
 }

 @Test
 public void testFilterCandidate_oneMock_propertySetterFailure_fieldSetterSuccess() {
 Object mock = mocks.iterator().next();
 when(field.getName()).thenReturn("testField");
 when(field.getType()).thenReturn(String.class);
 doThrow(new RuntimeException("Test Exception")).when(new BeanPropertySetter(fieldInstance, field)).set(mock);
 doReturn(true).when(new FieldSetter(fieldInstance, field)).set(mock);

 Object result = filter.filterCandidate(mocks, field, fieldInstance);

 assertSame(mock, result);
 }

 @Test(expected = RuntimeException.class)
 public void testFilterCandidate_oneMock_propertySetterFailure_fieldSetterFailure() {
 Object mock = mocks.iterator().next();
 when(field.getName()).thenReturn("testField");
 when(field.getType()).thenReturn(String.class);
 doThrow(new RuntimeException("Test Exception")).when(new BeanPropertySetter(fieldInstance, field)).set(mock);
 doThrow(new RuntimeException("Test Exception")).when(new FieldSetter(fieldInstance, field)).set(mock);

 filter.filterCandidate(mocks, field, fieldInstance);
 }

 @Test
 public void testFilterCandidate_multipleMocks_noMatchingMock() {
 Object mock1 = mocks.iterator().next();
 Object mock2 = mocks.iterator().next();
 when(field.getName()).thenReturn("testField");
 when(field.getType()).thenReturn(String.class);
 doReturn(mock1).when(new BeanPropertySetter(fieldInstance, field)).set(mock1);
 doReturn(mock2).when(new BeanPropertySetter(fieldInstance, field)).set(mock2);

 Object result = filter.filterCandidate(mocks, field, fieldInstance);

 assertNull(result);
 }

 @Test
 public void testFilterCandidate_multipleMocks_matchingMock_propertySetterSuccess() {
 Object mock = mocks.iterator().next();
 Object anotherMock = mocks.iterator().next();
 when(field.getName()).thenReturn("testField");
 when(field.getType()).thenReturn(String.class);
 doReturn(mock).when(new BeanPropertySetter(fieldInstance, field)).set(mock);
 doReturn(anotherMock).when(new BeanPropertySetter(fieldInstance, field)).set(anotherMock);

 Object result = filter.filterCandidate(mocks, field, fieldInstance);

 assertSame(mock, result);
 }

 @Test
 public void testFilterCandidate_multipleMocks_matchingMock_fieldSetterSuccess() {
 Object mock = mocks.iterator().next();
 Object anotherMock = mocks.iterator().next();
 when(field.getName()).thenReturn("testField");
 when(field.getType()).thenReturn(String.class);
 doThrow(new RuntimeException("Test Exception")).when(new BeanPropertySetter(fieldInstance, field)).set(mock);
 doThrow(new RuntimeException("Test Exception")).when(new BeanPropertySetter(fieldInstance, field)).set(anotherMock);
 doReturn(true).when(new FieldSetter(fieldInstance, field)).set(mock);

 Object result = filter.filterCandidate(mocks, field, fieldInstance);

 assertSame(mock, result);
 }

 @Test
 public void testFilterCandidate_multipleMocks_matchingMock_fieldSetterFailure() {
 Object mock = mocks.iterator().next();
 Object anotherMock = mocks.iterator().next();
 when(field.getName()).thenReturn("testField");
 when(field.getType()).thenReturn(String.class);
 doThrow(new RuntimeException("Test Exception")).when(new BeanPropertySetter(fieldInstance, field)).set(mock);
 doThrow(new RuntimeException("Test Exception")).when(new BeanPropertySetter(fieldInstance, field)).set(anotherMock);
 doThrow(new RuntimeException("Test Exception")).when(new FieldSetter(fieldInstance, field)).set(mock);

 filter.filterCandidate(mocks, field, fieldInstance);
 }

 @Test
 public void testFilterCandidate_emptyMockList() {
 Object fieldInstance = Mockito.mock(Object.class);

 Object result = filter.filterCandidate(Collections.emptyList(), field, fieldInstance);

 assertNull(result);
 }
}