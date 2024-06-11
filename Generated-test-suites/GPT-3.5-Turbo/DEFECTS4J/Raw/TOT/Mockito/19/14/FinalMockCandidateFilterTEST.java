import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;

    @Before
    public void setUp() {
        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidate_singleMockInjectsByPropertySetter() {
        // Arrange
        Collection<Object> mocks = Arrays.asList(mock(Object.class));
        Field field = getField();
        Object fieldInstance = new Object();

        // Act
        Object result = filter.filterCandidate(mocks, field, fieldInstance).thenInject();

        // Assert
        assertNotNull(result);
        verifyFieldInjection(fieldInstance, field, (Object)mocks.iterator().next());
        verifyZeroInteractions(fieldInstance, field);
    }

    @Test
    public void testFilterCandidate_multipleMocksReturnsNull() {
        // Arrange
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        Field field = getField();
        Object fieldInstance = new Object();

        // Act
        Object result = filter.filterCandidate(mocks, field, fieldInstance).thenInject();

        // Assert
        assertNull(result);
        verifyZeroInteractions(fieldInstance, field);
    }

    @Test
    public void testFilterCandidate_singleMockInjectsByFieldSetter() {
        // Arrange
        Collection<Object> mocks = Arrays.asList(mock(Object.class));
        Field field = getField();
        Object fieldInstance = new Object();
        setPropertySetterReturn(fieldInstance, field, false);

        // Act
        Object result = filter.filterCandidate(mocks, field, fieldInstance).thenInject();

        // Assert
        assertNotNull(result);
        verifyFieldInjection(fieldInstance, field, (Object)mocks.iterator().next());
        verifyZeroInteractions(fieldInstance, field);
    }

    @Test
    public void testFilterCandidate_singleMockThrowsException() {
        // Arrange
        Collection<Object> mocks = Arrays.asList(mock(Object.class));
        Field field = getField();
        Object fieldInstance = new Object();
        RuntimeException exception = new RuntimeException("Injection failed");
        setPropertySetterException(fieldInstance, field, exception);

        // Act
        Object result = filter.filterCandidate(mocks, field, fieldInstance).thenInject();

        // Assert
        assertNotNull(result);
        verifyZeroInteractions(fieldInstance, field);
        verifyExceptionThrown(field, (Object)mocks.iterator().next(), exception);
    }

    private Field getField() {
        try {
            return FinalMockCandidateFilter.class.getDeclaredField("mocks");
        } catch (NoSuchFieldException e) {
            fail("Field not found: " + e.getMessage());
            return null;
        }
    }

    private void setPropertySetterReturn(Object fieldInstance, Field field, boolean returnValue) {
        BeanPropertySetter propertySetter = spy(new BeanPropertySetter(fieldInstance, field));
        when(propertySetter.set(any())).thenReturn(returnValue);
    }

    private void setPropertySetterException(Object fieldInstance, Field field, RuntimeException exception) {
        BeanPropertySetter propertySetter = spy(new BeanPropertySetter(fieldInstance, field));
        doThrow(exception).when(propertySetter).set(any());
    }

    private void verifyFieldInjection(Object fieldInstance, Field field, Object expectedMock) {
        FieldSetter fieldSetter = mock(FieldSetter.class);
        verify(fieldSetter).set(fieldInstance, field, expectedMock);
    }

    private void verifyExceptionThrown(Field field, Object mock, RuntimeException exception) {
        Reporter mockitoReporter = mock(Reporter.class);
        verify(mockitoReporter).cannotInjectDependency(field, mock, exception);
    }

}