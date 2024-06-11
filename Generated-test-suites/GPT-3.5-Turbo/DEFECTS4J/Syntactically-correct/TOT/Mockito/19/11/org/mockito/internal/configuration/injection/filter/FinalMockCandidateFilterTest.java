import org.junit.Test;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_withOneMatchingMock_shouldInjectMock() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        List<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        Field field = getField("fieldInstance");
        Object fieldInstance = new Object();

        // Act
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object injectedMock = ongoingInjecter.thenInject();

        // Assert
        assertEquals(mock, injectedMock);
    }

    @Test
    public void testFilterCandidate_withMultipleMatchingMocks_shouldNotInjectMock() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        List<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        Field field = getField("fieldInstance");
        Object fieldInstance = new Object();

        // Act
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object injectedMock = ongoingInjecter.thenInject();

        // Assert
        assertNull(injectedMock);
    }

    @Test
    public void testFilterCandidate_withNoMatchingMocks_shouldNotInjectMock() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        List<Object> mocks = new ArrayList<>();
        Field field = getField("fieldInstance");
        Object fieldInstance = new Object();

        // Act
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object injectedMock = ongoingInjecter.thenInject();

        // Assert
        assertNull(injectedMock);
    }

    @Test
    public void testFilterCandidate_withMatchingMock_andSuccessfulBeanPropertySetterInjection_shouldInjectMock() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        List<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        Field field = getField("fieldInstance");
        Object fieldInstance = new Object();
        BeanPropertySetter beanPropertySetter = mock(BeanPropertySetter.class);
        when(beanPropertySetter.set(mock)).thenReturn(true);
        whenNew(BeanPropertySetter.class).withArguments(fieldInstance, field).thenReturn(beanPropertySetter);

        // Act
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object injectedMock = ongoingInjecter.thenInject();

        // Assert
        assertEquals(mock, injectedMock);
        verify(beanPropertySetter).set(mock);
        verifyNoMoreInteractions(beanPropertySetter);
    }

    @Test
    public void testFilterCandidate_withMatchingMock_andFailedBeanPropertySetterInjection_shouldAttemptFieldSetterInjection() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        List<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        Field field = getField("fieldInstance");
        Object fieldInstance = new Object();
        BeanPropertySetter beanPropertySetter = mock(BeanPropertySetter.class);
        when(beanPropertySetter.set(mock)).thenReturn(false);
        whenNew(BeanPropertySetter.class).withArguments(fieldInstance, field).thenReturn(beanPropertySetter);
        FieldSetter fieldSetter = mock(FieldSetter.class);
        whenNew(FieldSetter.class).withArguments(fieldInstance, field).thenReturn(fieldSetter);

        // Act
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object injectedMock = ongoingInjecter.thenInject();

        // Assert
        assertEquals(mock, injectedMock);
        verify(beanPropertySetter).set(mock);
        verify(fieldSetter).set(mock);
        verifyNoMoreInteractions(beanPropertySetter, fieldSetter);
    }

    @Test
    public void testFilterCandidate_withMatchingMock_andFailedBeanPropertySetterInjection_andFailedFieldSetterInjection_shouldNotInjectMock() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        List<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        Field field = getField("fieldInstance");
        Object fieldInstance = new Object();
        BeanPropertySetter beanPropertySetter = mock(BeanPropertySetter.class);
        when(beanPropertySetter.set(mock)).thenReturn(false);
        whenNew(BeanPropertySetter.class).withArguments(fieldInstance, field).thenReturn(beanPropertySetter);
        FieldSetter fieldSetter = mock(FieldSetter.class);
        whenNew(FieldSetter.class).withArguments(fieldInstance, field).thenReturn(fieldSetter);
        Reporter reporter = mock(Reporter.class);
        whenNew(Reporter.class).thenReturn(reporter);

        // Act
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object injectedMock = ongoingInjecter.thenInject();

        // Assert
        assertNull(injectedMock);
        verify(beanPropertySetter).set(mock);
        verify(fieldSetter).set(mock);
        verify(reporter).cannotInjectDependency(field, mock, null);
        verifyNoMoreInteractions(beanPropertySetter, fieldSetter, reporter);
    }

    private Field getField(String name) {
        try {
            return FinalMockCandidateFilter.class.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
}