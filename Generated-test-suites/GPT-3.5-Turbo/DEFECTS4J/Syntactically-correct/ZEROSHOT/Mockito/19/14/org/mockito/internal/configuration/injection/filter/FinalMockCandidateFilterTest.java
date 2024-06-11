import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setup() {
        filter = new FinalMockCandidateFilter();
        mocks = new ArrayList<>();
        field = null;
        fieldInstance = null;
    }

    @Test
    public void testFilterCandidate_SingleMock_MatchingMockSetAsProperty() {
        // Arrange
        Object mock = mock(Object.class);
        mocks.add(mock);

        BeanPropertySetter setter = mock(BeanPropertySetter.class);
        when(setter.set(fieldInstance, field)).thenReturn(true);
        whenNew(BeanPropertySetter.class).withArguments(fieldInstance, field).thenReturn(setter);

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(setter).set(fieldInstance, field);
        verifyZeroInteractions(new Reporter());
        verifyNoMoreInteractions(mock, setter);
    }

    @Test
    public void testFilterCandidate_SingleMock_MatchingMockSetAsField() {
        // Arrange
        Object mock = mock(Object.class);
        mocks.add(mock);

        BeanPropertySetter setter = mock(BeanPropertySetter.class);
        when(setter.set(fieldInstance, field)).thenReturn(false);
        when(setter.set(fieldInstance, field)).thenReturn(true);
        whenNew(BeanPropertySetter.class).withArguments(fieldInstance, field)
                .thenReturn(setter);

        FieldSetter fieldSetter = mock(FieldSetter.class);
        when(fieldSetter.set(fieldInstance, field)).thenReturn(true);
        whenNew(FieldSetter.class).withArguments(fieldInstance, field)
                .thenReturn(fieldSetter);

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(setter).set(fieldInstance, field);
        verify(fieldSetter).set(fieldInstance, field);
        verifyZeroInteractions(new Reporter());
        verifyNoMoreInteractions(mock, setter, fieldSetter);
    }

    @Test
    public void testFilterCandidate_SingleMock_MatchingMockSetThrowsException() {
        // Arrange
        Object mock = mock(Object.class);
        mocks.add(mock);

        BeanPropertySetter setter = mock(BeanPropertySetter.class);
        when(setter.set(fieldInstance, field)).thenThrow(new RuntimeException());
        whenNew(BeanPropertySetter.class).withArguments(fieldInstance, field).thenReturn(setter);

        Reporter reporter = mock(Reporter.class);
        whenNew(Reporter.class).thenReturn(reporter);

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(setter).set(fieldInstance, field);
        verify(reporter).cannotInjectDependency(field, mock, new RuntimeException());
        verifyNoMoreInteractions(setter, reporter);
    }

    @Test
    public void testFilterCandidate_MultipleMocks_NoMatchingMock() {
        // Arrange
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));

        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertNull(injecter);
    }

    @Test
    public void testFilterCandidate_MultipleMocks() {
        // Arrange
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));

        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertNull(injecter);
    }
}