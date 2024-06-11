import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldType;
import org.mockito.internal.util.reflection.SimpleField;
import org.mockito.internal.util.reflection.SimpleValue;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private List<Object> mocks;
    private Object fieldInstance;
    private Field field;

    @Before
    public void setup() {
        filter = new FinalMockCandidateFilter();
        mocks = new ArrayList<>();
        fieldInstance = mock(Object.class);
        field = new SimpleField("testField", FieldType.FIELD, new SimpleValue(Integer.class), new Object[]{fieldInstance}, new Object[0]);
    }

    @Test
    public void testFilterCandidateWithSingleMockAndPropertySetterSuccess() {
        Object mock = mock(Object.class);
        mocks.add(mock);

        when(mock.getClass().getDeclaredField("testField")).thenReturn(field);
        when(mock.getClass().getDeclaredField("testField")).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return 42;
            }
        });

        when(new BeanPropertySetter(fieldInstance, field).canSet(mock)).thenReturn(true);
        when(new BeanPropertySetter(fieldInstance, field).set(mock)).thenReturn(true);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        assertEquals(mock, result);
    }

    @Test
    public void testFilterCandidateWithSingleMockAndPropertySetterFailure() {
        Object mock = mock(Object.class);
        mocks.add(mock);

        when(mock.getClass().getDeclaredField("testField")).thenReturn(field);
        when(mock.getClass().getDeclaredField("testField")).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                throw new RuntimeException("Property setter failed.");
            }
        });

        when(new BeanPropertySetter(fieldInstance, field).canSet(mock)).thenReturn(true);
        when(new BeanPropertySetter(fieldInstance, field).set(mock)).thenThrow(new RuntimeException("Property setter failed."));

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        try {
            injecter.thenInject();
            fail("Expected MockitoException to be thrown.");
        } catch (MockitoException e) {
            assertEquals(e.getMessage(), "cannotInjectDependency: [field] [org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilterTest$MockitoTest$1@2f6b3447] [java.lang.reflect.Field: name = testField, type = class java.lang.Integer]");
        }
    }

    @Test
    public void testFilterCandidateWithSingleMockAndFieldAccessSuccess() {
        Object mock = mock(Object.class);
        mocks.add(mock);

        when(mock.getClass().getDeclaredField("testField")).thenReturn(field);
        when(mock.getClass().getDeclaredField("testField")).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return null;
            }
        });

        when(new FieldSetter(fieldInstance, field).canSet(mock)).thenReturn(true);
        when(new FieldSetter(fieldInstance, field).set(mock)).thenReturn(true);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        assertNull(result);
        assertEquals(mock, fieldInstance);
    }

    @Test
    public void testFilterCandidateWithSingleMockAndFieldAccessFailure() {
        Object mock = mock(Object.class);
        mocks.add(mock);

        when(mock.getClass().getDeclaredField("testField")).thenReturn(field);
        when(mock.getClass().getDeclaredField("testField")).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                throw new RuntimeException("Field access failed.");
            }
        });

        when(new FieldSetter(fieldInstance, field).canSet(mock)).thenReturn(true);
        when(new FieldSetter(fieldInstance, field).set(mock)).thenThrow(new RuntimeException("Field access failed."));

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        try {
            injecter.thenInject();
            fail("Expected MockitoException to be thrown.");
        } catch (MockitoException e) {
            assertEquals(e.getMessage(), "cannotInjectDependency: [field] [org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilterTest$MockitoTest$1@2f6b3447] [java.lang.reflect.Field: name = testField, type = class java.lang.Integer]");
        }
    }

    @Test
    public void testFilterCandidateWithMultipleMocksAndNoMatch() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.addAll(Arrays.asList(mock1, mock2));

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        assertNull(result);
    }
}