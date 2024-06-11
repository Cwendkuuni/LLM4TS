import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
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
        field = getFieldFromClass();
        fieldInstance = getFieldInstance();
    }

    @Test
    public void testFilterCandidate_singleMock() {
        // Arrange
        Object mock = new Object();
        mocks.add(mock);

        // Act
        Object result = filter.filterCandidate(mocks, field, fieldInstance).thenInject();

        // Assert
        assertEquals(mock, result);
        verifyZeroInteractions(fieldInstance);
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        // Act
        Object result = filter.filterCandidate(mocks, field, fieldInstance).thenInject();

        // Assert
        assertEquals(null, result);
        verifyZeroInteractions(fieldInstance);
    }

    @Test
    public void testFilterCandidate_setField() {
        // Arrange
        Object mock = new Object();
        mocks.add(mock);
        FieldSetter fieldSetter = mock(FieldSetter.class);
        whenNew(FieldSetter.class).withArguments(fieldInstance, field).thenReturn(fieldSetter);

        // Act
        Object result = filter.filterCandidate(mocks, field, fieldInstance).thenInject();

        // Assert
        assertEquals(mock, result);
        verify(fieldSetter).set(mock);
        verifyZeroInteractions(fieldInstance);
    }

    @Test
    public void testFilterCandidate_setField_exception() {
        // Arrange
        Object mock = new Object();
        mocks.add(mock);
        FieldSetter fieldSetter = mock(FieldSetter.class);
        whenNew(FieldSetter.class).withArguments(fieldInstance, field).thenReturn(fieldSetter);
        doThrow(new RuntimeException()).when(fieldSetter).set(mock);
        Reporter reporter = mock(Reporter.class);
        whenNew(Reporter.class).thenReturn(reporter);

        // Act
        Object result = filter.filterCandidate(mocks, field, fieldInstance).thenInject();

        // Assert
        assertEquals(mock, result);
        verify(fieldSetter).set(mock);
        verify(reporter).cannotInjectDependency(field, mock, new RuntimeException());
    }

    @Test
    public void testFilterCandidate_setProperty() {
        // Arrange
        Object mock = new Object();
        mocks.add(mock);
        BeanPropertySetter beanPropertySetter = mock(BeanPropertySetter.class);
        whenNew(BeanPropertySetter.class).withArguments(fieldInstance, field).thenReturn(beanPropertySetter);
        when(beanPropertySetter.set(mock)).thenReturn(true);

        // Act
        Object result = filter.filterCandidate(mocks, field, fieldInstance).thenInject();

        // Assert
        assertEquals(mock, result);
        verify(beanPropertySetter).set(mock);
        verifyZeroInteractions(fieldInstance);
    }

    @Test
    public void testFilterCandidate_setProperty_exception() {
        // Arrange
        Object mock = new Object();
        mocks.add(mock);
        BeanPropertySetter beanPropertySetter = mock(BeanPropertySetter.class);
        whenNew(BeanPropertySetter.class).withArguments(fieldInstance, field).thenReturn(beanPropertySetter);
        when(beanPropertySetter.set(mock)).thenReturn(true);
        doThrow(new RuntimeException()).when(beanPropertySetter).set(mock);
        Reporter reporter = mock(Reporter.class);
        whenNew(Reporter.class).thenReturn(reporter);

        // Act
        Object result = filter.filterCandidate(mocks, field, fieldInstance).thenInject();

        // Assert
        assertEquals(mock, result);
        verify(beanPropertySetter).set(mock);
        verify(reporter).cannotInjectDependency(field, mock, new RuntimeException());
    }

    private Field getFieldFromClass() {
        try {
            return FinalMockCandidateFilter.class.getDeclaredField("field");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object getFieldInstance() {
        try {
            return FinalMockCandidateFilter.class.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}