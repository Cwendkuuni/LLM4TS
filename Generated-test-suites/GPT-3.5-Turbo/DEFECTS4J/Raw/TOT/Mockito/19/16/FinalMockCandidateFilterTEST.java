import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    @Test
    public void filterCandidate_shouldReturnOngoingInjecterWithMatchingMock_whenMocksSizeIs1() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        List<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        Object mock = mock(Object.class);
        mocks.add(mock);

        // Act
        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertSame(mock, injecter.thenInject());
        verify(fieldInstance, times(1)).setAccessible(true);
        verify(field, times(1)).set(fieldInstance, mock);
    }

    @Test
    public void filterCandidate_shouldReturnOngoingInjecterWithNullValue_whenMocksSizeIsNot1() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        List<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        // Act
        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertNull(injecter.thenInject());
        verify(fieldInstance, never()).setAccessible(true);
        verify(field, never()).set(fieldInstance, any());
    }

    @Test
    public void filterCandidate_shouldThrowExceptionAndInvokeReporter_whenSettingBeanPropertyReturnsFalse() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        List<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        Object mock = mock(Object.class);
        mocks.add(mock);

        BeanPropertySetter beanPropertySetter = mock(BeanPropertySetter.class);
        when(beanPropertySetter.set(mock)).thenReturn(false);
        doThrow(RuntimeException.class).when(beanPropertySetter).set(fieldInstance, field);

        doReturn(beanPropertySetter).whenNew(BeanPropertySetter.class)
            .withArguments(fieldInstance, field);

        Reporter reporter = mock(Reporter.class);
        doNothing().when(reporter).cannotInjectDependency(field, mock, any(RuntimeException.class));

        doReturn(reporter).whenNew(Reporter.class).withNoArguments();

        // Act
        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertSame(mock, injecter.thenInject());
        verify(fieldInstance, never()).setAccessible(true);
        verify(field, never()).set(fieldInstance, any());
        verify(beanPropertySetter, times(1)).set(mock);
        verify(beanPropertySetter, times(1)).set(fieldInstance, field);
        verify(reporter, times(1)).cannotInjectDependency(field, mock, any(RuntimeException.class));
    }

    @Test
    public void filterCandidate_shouldThrowExceptionAndInvokeReporter_whenSettingFieldReturnsFalse() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        List<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        Object mock = mock(Object.class);
        mocks.add(mock);

        FieldSetter fieldSetter = mock(FieldSetter.class);
        when(fieldSetter.set(mock)).thenReturn(false);
        doThrow(RuntimeException.class).when(fieldSetter).set(fieldInstance, field);

        doReturn(fieldSetter).whenNew(FieldSetter.class).withArguments(fieldInstance, field);

        Reporter reporter = mock(Reporter.class);
        doNothing().when(reporter).cannotInjectDependency(field, mock, any(RuntimeException.class));

        doReturn(reporter).whenNew(Reporter.class).withNoArguments();

        // Act
        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertSame(mock, injecter.thenInject());
        verify(fieldInstance, times(1)).setAccessible(true);
        verify(field, never()).set(fieldInstance, any());
        verify(fieldSetter, times(1)).set(mock);
        verify(fieldSetter, times(1)).set(fieldInstance, field);
        verify(reporter, times(1)).cannotInjectDependency(field, mock, any(RuntimeException.class));
    }

}