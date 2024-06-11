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
    public void setUp() {
        filter = new FinalMockCandidateFilter();
        mocks = new ArrayList<>();
        field = mock(Field.class);
        fieldInstance = mock(Object.class);
    }

    @Test
    public void filterCandidate_singleMock_successfulInjection() {
        // Arrange
        Object matchingMock = mock(Object.class);
        mocks.add(matchingMock);
        BeanPropertySetter propertySetter = mock(BeanPropertySetter.class);
        FieldSetter fieldSetter = mock(FieldSetter.class);
        Reporter reporter = mock(Reporter.class);
        whenNew(BeanPropertySetter.class).withArguments(fieldInstance, field).thenReturn(propertySetter);
        whenNew(FieldSetter.class).withArguments(fieldInstance, field).thenReturn(fieldSetter);
        whenNew(Reporter.class).thenReturn(reporter);
        
        // Act
        filter.filterCandidate(mocks, field, fieldInstance).thenInject();

        // Assert
        verify(propertySetter).set(matchingMock);
        verify(fieldSetter, never()).set(matchingMock);
        verify(reporter, never()).cannotInjectDependency(field, matchingMock, any());
    }

    @Test
    public void filterCandidate_singleMock_failedInjection() {
        // Arrange
        Object matchingMock = mock(Object.class);
        mocks.add(matchingMock);
        BeanPropertySetter propertySetter = mock(BeanPropertySetter.class);
        FieldSetter fieldSetter = mock(FieldSetter.class);
        Reporter reporter = mock(Reporter.class);
        whenNew(BeanPropertySetter.class).withArguments(fieldInstance, field).thenReturn(propertySetter);
        when(propertySetter.set(matchingMock)).thenReturn(false);
        whenNew(FieldSetter.class).withArguments(fieldInstance, field).thenReturn(fieldSetter);
        whenNew(Reporter.class).thenReturn(reporter);

        // Act
        filter.filterCandidate(mocks, field, fieldInstance).thenInject();

        // Assert
        verify(propertySetter).set(matchingMock);
        verify(fieldSetter).set(matchingMock);
        verify(reporter, never()).cannotInjectDependency(field, matchingMock, any());
    }

    @Test
    public void filterCandidate_singleMock_exceptionWhileInjection() {
        // Arrange
        Object matchingMock = mock(Object.class);
        mocks.add(matchingMock);
        BeanPropertySetter propertySetter = mock(BeanPropertySetter.class);
        FieldSetter fieldSetter = mock(FieldSetter.class);
        Reporter reporter = mock(Reporter.class);
        whenNew(BeanPropertySetter.class).withArguments(fieldInstance, field).thenReturn(propertySetter);
        whenNew(FieldSetter.class).withArguments(fieldInstance, field).thenReturn(fieldSetter);
        when(fieldSetter.set(matchingMock)).thenThrow(new RuntimeException());
        whenNew(Reporter.class).thenReturn(reporter);

        // Act
        filter.filterCandidate(mocks, field, fieldInstance).thenInject();

        // Assert
        verify(propertySetter).set(matchingMock);
        verify(fieldSetter).set(matchingMock);
        verify(reporter).cannotInjectDependency(field, matchingMock, any());
    }

    @Test
    public void filterCandidate_multipleMocks_nullInjection() {
        // Arrange
        Object matchingMock1 = mock(Object.class);
        Object matchingMock2 = mock(Object.class);
        mocks.add(matchingMock1);
        mocks.add(matchingMock2);
        
        // Act
        Object result = filter.filterCandidate(mocks, field, fieldInstance).thenInject();

        // Assert
        assertNull(result);
    }
}