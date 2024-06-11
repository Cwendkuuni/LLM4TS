import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class FinalMockCandidateFilterTest {

 private FinalMockCandidateFilter filter;
 private Collection<Object> mocks;
 private Field field;
 private Object fieldInstance;

 @Before
 public void setup() {
 mocks = new ArrayList<>();
 field = new Field("testField", Object.class);
 fieldInstance = new Object();
 filter = new FinalMockCandidateFilter();
 }

 @Test
 public void testFilterCandidate_singleMock_fieldSetterSuccess() {
 Stubber stubber = mock(InvocationOnMock.class);
 Object mock = stubber.createMock(Object.class);
 mocks.add(mock);

 when(mock.answer(Answer.CALLS_REAL_METHOD)).thenAnswer(invocation -> {
 new FieldSetter(fieldInstance, field).set(mock);
 return null;
 });

 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
 assertTrue(result.thenInject());
 }

 @Test
 public void testFilterCandidate_singleMock_fieldSetterFailure() {
 whenThrows(MockitoException.class, () -> {
 new FieldSetter(fieldInstance, field).set(mock);
 });

 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
 assertFalse(result.thenInject());
 }

 @Test
 public void testFilterCandidate_singleMock_fieldAccessSuccess() {
 Object mock = mock(Object.class);
 mocks.add(mock);

 when(mock.getClass()).thenReturn(Object.class);
 when(mock.equals(fieldInstance)).thenReturn(true);

 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
 assertTrue(result.thenInject());
 }

 @Test(expected = MockitoException.class)
 public void testFilterCandidate_singleMock_fieldAccessFailure() {
 Object mock = mock(Object.class);
 mocks.add(mock);

 when(mock.getClass()).thenReturn(Object.class);
 when(mock.equals(fieldInstance)).thenReturn(true);
 when(mock.getClass()).thenReturn(String.class);

 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
 assertThrows(MockitoException.class, () -> result.thenInject());
 }

 @Test
 public void testFilterCandidate_multipleMocks() {
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
 assertFalse(result.thenInject());
 }

}