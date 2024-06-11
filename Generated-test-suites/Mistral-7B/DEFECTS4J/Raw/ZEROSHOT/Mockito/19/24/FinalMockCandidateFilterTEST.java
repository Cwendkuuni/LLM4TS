import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.exceptions.Reporter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class FinalMockCandidateFilterTest {

 private FinalMockCandidateFilter filter;
 private Collection<Object> mocks;
 private Object fieldInstance;
 private Field field;
 private Object matchingMock;
 private Object nonMatchingMock;
 private Reporter reporter;

 @Before
 public void setUp() {
 mocks = new ArrayList<>();
 fieldInstance = mock(Object.class);
 field = mock(Field.class);
 when(field.getName()).thenReturn("fieldName");
 matchingMock = mock(Object.class);
 nonMatchingMock = mock(Object.class);
 mocks.add(matchingMock);
 mocks.add(nonMatchingMock);
 reporter = mock(Reporter.class);
 filter = new FinalMockCandidateFilter();
 }

 @Test
 public void testFilterCandidate_oneMock_propertySetterSuccess() {
 when(new BeanPropertySetter(fieldInstance, field).canSet(matchingMock)).thenReturn(true);

 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 verify(matchingMock).equals(fieldInstance);
 verifyNoInteractions(nonMatchingMock);
 verify(reporter).cannotInjectDependency(isNull(), isNull(), isNull());

 assertNotNull(result);
 assertSame(matchingMock, result.thenInject());
 }

 @Test
 public void testFilterCandidate_oneMock_propertySetterFailure() {
 when(new BeanPropertySetter(fieldInstance, field).canSet(matchingMock)).thenReturn(false);
 when(new FieldSetter(fieldInstance, field).canSet(matchingMock)).thenReturn(true);

 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 verify(matchingMock).equals(fieldInstance);
 verifyNoInteractions(nonMatchingMock);
 verify(reporter).cannotInjectDependency(isNull(), isNull(), isA(RuntimeException.class));

 assertNotNull(result);
 assertNull(result.thenInject());
 }

 @Test
 public void testFilterCandidate_oneMock_propertySetterAndFieldAccessFailure() {
 when(new BeanPropertySetter(fieldInstance, field).canSet(matchingMock)).thenReturn(false);
 when(new FieldSetter(fieldInstance, field).canSet(matchingMock)).thenReturn(false);

 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 verify(matchingMock).equals(fieldInstance);
 verify(reporter).cannotInjectDependency(isNull(), isNull(), isA(RuntimeException.class));

 assertNotNull(result);
 assertNull(result.thenInject());
 }

 @Test
 public void testFilterCandidate_multipleMocks_noMatchingMock() {
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 verifyNoInteractions(matchingMock, nonMatchingMock);
 verify(reporter).cannotInjectDependency(isNull(), isNull(), isNull());

 assertNotNull(result);
 assertNull(result.thenInject());
 }
}