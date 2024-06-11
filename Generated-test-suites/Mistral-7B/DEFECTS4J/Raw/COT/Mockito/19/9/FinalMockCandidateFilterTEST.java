import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldType;
import org.mockito.internal.util.reflection.Whitebox;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

 private FinalMockCandidateFilter filter;
 private List<Object> mocks;
 private Object fieldInstance;
 private Field field;

 @Before
 public void setUp() {
 mocks = Arrays.asList(mock(Object.class), mock(Object.class));
 fieldInstance = mock(Object.class);
 field = mock(Field.class);
 Whitebox.setInternalState(field, "name", "testField");
 Whitebox.setInternalState(field, "type", new FieldType(String.class));
 filter = new FinalMockCandidateFilter();
 }

 @Test
 public void testFilterCandidate_singleMock_beanPropertySetterSuccess() {
 Object mock = mocks.get(0);
 when(mock.getClass().getDeclaredField("testField")).thenReturn(field);
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
 assertTrue(injecter instanceof OngoingInjecter);
 assertNotNull(injecter.thenInject());
 }

 @Test
 public void testFilterCandidate_singleMock_beanPropertySetterFailure_fieldSetterSuccess() {
 Object mock = mocks.get(0);
 when(mock.getClass().getDeclaredField("testField")).thenReturn(field);
 doThrow(RuntimeException.class).when(field).set(anyObject(), any());
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
 assertTrue(injecter instanceof OngoingInjecter);
 assertNotNull(injecter.thenInject());
 }

 @Test(expected = MockitoException.class)
 public void testFilterCandidate_singleMock_beanPropertySetterFailure_reporterError() {
 Object mock = mocks.get(0);
 when(mock.getClass().getDeclaredField("testField")).thenReturn(field);
 doThrow(RuntimeException.class).when(field).set(anyObject(), any());
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
 assertTrue(injecter instanceof OngoingInjecter);
 injecter.thenInject(); // this call should trigger the Reporter error
 }

 @Test
 public void testFilterCandidate_multipleMocks_nullReturned() {
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
 assertNotNull(injecter);
 assertNull(injecter.thenInject());
 }

}