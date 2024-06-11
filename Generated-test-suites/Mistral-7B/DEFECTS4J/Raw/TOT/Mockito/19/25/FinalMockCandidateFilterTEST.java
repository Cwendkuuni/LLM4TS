import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.stub.Stub;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.util.reflection.FieldAccessOrder.descending;

public class FinalMockCandidateFilterTest {
    private FinalMockCandidateFilter filter;
    private Collection<Object> mocks;
    private Object fieldInstance;
    private Field field;
    private Reporter reporter;

    @Before
    public void setup() {
        this.filter = new FinalMockCandidateFilter();
        this.mocks = new ArrayList<>();
        this.fieldInstance = new Object();
        this.field = new Field() {{
            setAccessible(true);
            setName("testField");
        }};
        this.reporter = mock(Reporter.class);
    }

    @Test
    public void testSingleMockWithPropertySetterSuccess() {
        Object mock = createMock(String.class);
        mocks.add(mock);

        when(mock.getClass()).thenReturn(String.class);
        when(mock.getName()).thenReturn("mock1");
        when(mock.hasField(any(Field.class))).thenReturn(true);
        doReturn("testValue").when(mock).getFieldValue(any(Field.class));
        doReturn(null).when(mock).getPropertyValue(any(String.class));
        doAnswer(invocation -> {
            Field field = invocation.getArgument(0);
            BeanPropertySetter setter = new BeanPropertySetter(fieldInstance, field);
            setter.set(mock, "testValue");
            return null;
        }).when(mock).setPropertyValue(any(String.class), any(String.class));

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        assertEquals("mock1", result);
        verify(reporter, never()).cannotInjectDependency(any(Field.class), any(Object.class), any(RuntimeException.class));
    }

    @Test
    public void testSingleMockWithPropertySetterFailure() {
        Object mock = createMock(String.class);
        mocks.add(mock);

        when(mock.getClass()).thenReturn(String.class);
        when(mock.getName()).thenReturn("mock1");
        when(mock.hasField(any(Field.class))).thenReturn(true);
        doReturn("testValue").when(mock).getFieldValue(any(Field.class));
        doReturn("testPropertyValue").when(mock).getPropertyValue(any(String.class));
        doAnswer(invocation -> {
            Field field = invocation.getArgument(0);
            BeanPropertySetter setter = new BeanPropertySetter(fieldInstance, field);
            setter.set(mock, "testValue");
            return null;
        }).when(mock).setPropertyValue(any(String.class), any(String.class));
        doThrow(new RuntimeException("Test Exception")).when(mock).setPropertyValue(any(String.class), anyString());

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        assertNull(result);
        verify(reporter).cannotInjectDependency(field, mock, argThat(e -> e instanceof RuntimeException));
    }

    @Test
    public void testSingleMockWithFieldAccessSuccess() {
        Object mock = createMock(String.class);
        mocks.add(mock);

        when(mock.getClass()).thenReturn(String.class);
        when(mock.getName()).thenReturn("mock1");
        when(mock.hasField(any(Field.class))).thenReturn(false);
        doReturn("testValue").when(mock).getFieldValue(any(Field.class));
        doReturn(null).when(mock).getPropertyValue(any(String.class));
        doAnswer(invocation -> {
            Field field = invocation.getArgument(0);
            FieldSetter setter = new FieldSetter(fieldInstance, field);
            setter.set(mock, "testValue");
            return null;
        }).when(mock).setPropertyValue(any(String.class), any(String.class));

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        assertEquals("mock1", result);
        verify(reporter, never()).cannotInjectDependency(any(Field.class), any(Object.class), any(RuntimeException.class));
    }

    @Test
    public void testSingleMockWithFieldAccessFailure() {
        Object mock = createMock(String.class);
        mocks.add(mock);

        when(mock.getClass()).thenReturn(String.class);
        when(mock.getName()).thenReturn("mock1");
        when(mock.hasField(any(Field.class))).thenReturn(false);
        doReturn("testValue").when(mock).getFieldValue(any(Field.class));
        doReturn("testFieldValue").when(mock).getPropertyValue(any(String.class));
        doAnswer(invocation -> {
            Field field = invocation.getArgument(0);
            FieldSetter setter = new FieldSetter(fieldInstance, field);
            setter.set(mock, "testValue");
            return null;
        }).when(mock).setPropertyValue(any(String.class), any(String.class));
        doThrow(new RuntimeException("Test Exception")).when(mock).setFieldValue(any(Field.class), any(Object.class));

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        assertNull(result);
        verify(reporter).cannotInjectDependency(field, mock, argThat(e -> e instanceof RuntimeException));
    }

    @Test
    public void testMultipleMocksWithPropertySetterSuccess() {
        Object mock1 = createMock(String.class);
        Object mock2 = createMock(String.class);
        mocks.addAll(Arrays.asList(mock1, mock2));

        when(mock1.getClass()).thenReturn(String.class);
        when(mock1.getName()).thenReturn("mock1");
        when(mock1.hasField(any(Field.class))).thenReturn(true);
        when(mock1.getFieldValue(any(Field.class))).thenReturn("testValue1");
        when(mock1.getPropertyValue(any(String.class))).thenReturn("testPropertyValue1");
        when(mock1.setPropertyValue(any(String.class), any(String.class))).thenReturn(null);

        when(mock2.getClass()).thenReturn(String.class);
        when(mock2.getName()).thenReturn("mock2");
        when(mock2.hasField(any(Field.class))).thenReturn(true);
        when(mock2.getFieldValue(any(Field.class))).thenReturn("testValue2");
        when(mock2.getPropertyValue(any(String.class))).thenReturn("testPropertyValue2");
        when(mock2.setPropertyValue(any(String.class), any(String.class))).thenReturn(null);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        assertNull(result);
        verifyNoInteractions(mock1, mock2, reporter);
    }

    @Test
    public void testMultipleMocksWithPropertySetterFailure() {
        Object mock1 = createMock(String.class);
        Object mock2 = createMock(String.class);
        mocks.addAll(Arrays.asList(mock1, mock2));

        when(mock1.getClass()).thenReturn(String.class);
        when(mock1.getName()).thenReturn("mock1");
        when(mock1.hasField(any(Field.class))).thenReturn(true);
        when(mock1.getFieldValue(any(Field.class))).thenReturn("testValue1");
        when(mock1.getPropertyValue(any(String.class))).thenReturn("testPropertyValue1");
        when(mock1.setPropertyValue(any(String.class), any(String.class))).thenThrow(new RuntimeException("Test Exception 1"));

        when(mock2.getClass()).thenReturn(String.class);
        when(mock2.getName()).thenReturn("mock2");
        when(mock2.hasField(any(Field.class))).thenReturn(true);
        when(mock2.getFieldValue(any(Field.class))).thenReturn("testValue2");
        when(mock2.getPropertyValue(any(String.class))).thenReturn("testPropertyValue2");
        when(mock2.setPropertyValue(any(String.class), any(String.class))).thenReturn(null);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        assertNull(result);
        verify(reporter).cannotInjectDependency(field, mock1, argThat(e -> e instanceof RuntimeException));
    }

    @Test
    public void testMultipleMocksWithFieldAccessSuccess() {
        Object mock1 = createMock(String.class);
        Object mock2 = createMock(String.class);
        mocks.addAll(Arrays.asList(mock1, mock2));

        when(mock1.getClass()).thenReturn(String.class);
        when(mock1.getName()).thenReturn("mock1");
        when(mock1.hasField(any(Field.class))).thenReturn(false);
        when(mock1.getFieldValue(any(Field.class))).thenReturn("testValue1");
        when(mock1.getPropertyValue(any(String.class))).thenReturn("testPropertyValue1");
        when(mock1.setPropertyValue(any(String.class), any(String.class))).thenReturn(null);

        when(mock2.getClass()).thenReturn(String.class);
        when(mock2.getName()).thenReturn("mock2");
        when(mock2.hasField(any(Field.class))).thenReturn(false);
        when(mock2.getFieldValue(any(Field.class))).thenReturn("testValue2");
        when(mock2.getPropertyValue(any(String.class))).thenReturn("testPropertyValue2");
        when(mock2.setFieldValue(any(Field.class), any(Object.class))).thenReturn(null);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        assertNull(result);
        verifyNoInteractions(mock1, mock2, reporter);
    }

    @Test
    public void testMultipleMocksWithFieldAccessFailure() {
        Object mock1 = createMock(String.class);
        Object mock2 = createMock(String.class);
        mocks.addAll(Arrays.asList(mock1, mock2));

        when(mock1.getClass()).thenReturn(String.class);
        when(mock1.getName()).thenReturn("mock1");
        when(mock1.hasField(any(Field.class))).thenReturn(false);
        when(mock1.getFieldValue(any(Field.class))).thenReturn("testValue1");
        when(mock1.getPropertyValue(any(String.class))).thenReturn("testPropertyValue1");
        when(mock1.setPropertyValue(any(String.class), any(String.class))).thenReturn(null);
        when(mock1.setFieldValue(any(Field.class), any(Object.class))).thenThrow(new RuntimeException("Test Exception 1"));

        when(mock2.getClass()).thenReturn(String.class);
        when(mock2.getName()).thenReturn("mock2");
        when(mock2.hasField(any(Field.class))).thenReturn(false);
        when(mock2.getFieldValue(any(Field.class))).thenReturn("testValue2");
        when(mock2.getPropertyValue(any(String.class))).thenReturn("testPropertyValue2");
        when(mock2.setFieldValue(any(Field.class), any(Object.class))).thenReturn(null);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        assertNull(result);
        verify(reporter).cannotInjectDependency(field, mock1, argThat(e -> e instanceof RuntimeException));
    }

    @Test
    public void testEmptyMockCollection() {
        when(mocks.isEmpty()).thenReturn(true);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        assertNull(result);
        verifyNoInteractions(reporter);
    }
}