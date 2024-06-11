import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.InjectionPoint;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.internal.util.reflection.ReflectionUtil.setFieldValue;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private List<Object> mocks;
    private Object fieldInstance;
    private Field field;

    @Before
    public void setUp() {
        filter = new FinalMockCandidateFilter();
        mocks = new ArrayList<>();
        fieldInstance = new Object();
        field = new Field("testField", String.class, fieldInstance.getClass());
    }

    @Test
    public void testFilterCandidateWithSingleMockAndSuccessfulPropertyInjection() {
        Object mock = mock(String.class);
        mocks.add(mock);

        when(mock.getClass().getDeclaredField("testField")).then(new Answer<Field>() {
            @Override
            public Field answer(InvocationOnMock invocation) throws Throwable {
                return field;
            }
        });

        when(mock.getClass().getDeclaredMethod("setTestField", String.class)).then(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                String value = "test";
                setFieldValue(mock, "testField", value);
                return null;
            }
        });

        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        String result = (String) actualInjecter.thenInject();

        assertEquals("test", result);
        verify(mock).setTestField("test");
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidateWithSingleMockAndFailurePropertyInjection() {
        Object mock = mock(String.class);
        mocks.add(mock);

        when(mock.getClass().getDeclaredField("testField")).then(new Answer<Field>() {
            @Override
            public Field answer(InvocationOnMock invocation) throws Throwable {
                return field;
            }
        });

        when(mock.getClass().getDeclaredMethod("setTestField", String.class)).thenThrow(new RuntimeException("Failed to set field"));

        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        actualInjecter.thenInject();

        fail("Should throw MockitoException");
    }

    @Test
    public void testFilterCandidateWithMultipleMocksAndSuccessfulFieldInjection() {
        Object mock1 = mock(String.class);
        Object mock2 = mock(String.class);
        mocks.add(mock1);
        mocks.add(mock2);

        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        // Do nothing, as the injecter returns null in this case

        // Verify that no mocks were modified
        assertEquals("", mock1.getClass().getDeclaredField("testField").get(mock1));
        assertEquals("", mock2.getClass().getDeclaredField("testField").get(mock2));
    }

    @Test
    public void testFilterCandidateWithMultipleMocksAndSuccessfulPropertyInjection() {
        Object mock1 = mock(String.class);
        Object mock2 = mock(String.class);
        mocks.add(mock1);
        mocks.add(mock2);

        when(mock1.getClass().getDeclaredField("testField")).thenReturn(field);
        when(mock1.getClass().getDeclaredMethod("setTestField", String.class)).then(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                String value = "test";
                setFieldValue(mock1, "testField", value);
                return null;
            }
        });

        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        String result = (String) actualInjecter.thenInject();

        assertEquals("test", result);
        verify(mock1).setTestField("test");
        verifyNoInteractions(mock2);
    }

    @Test
    public void testFilterCandidateWithMultipleMocksAndFailurePropertyInjection() {
        Object mock1 = mock(String.class);
        Object mock2 = mock(String.class);
        mocks.add(mock1);
        mocks.add(mock2);

        when(mock1.getClass().getDeclaredField("testField")).thenReturn(field);
        when(mock1.getClass().getDeclaredMethod("setTestField", String.class)).thenThrow(new RuntimeException("Failed to set field"));

        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        actualInjecter.thenInject();

        // The test should not fail here, as the injecter returns null in this case
        // Verify that no mocks were modified
        assertEquals("", mock1.getClass().getDeclaredField("testField").get(mock1));
        assertEquals("", mock2.getClass().getDeclaredField("testField").get(mock2));
    }

    @Test
    public void testFilterCandidateWithMultipleMocksAndSuccessfulFieldInjectionAfterPropertyFailure() {
        Object mock1 = mock(String.class);
        Object mock2 = mock(String.class);
        mocks.add(mock1);
        mocks.add(mock2);

        when(mock1.getClass().getDeclaredField("testField")).thenReturn(field);
        when(mock1.getClass().getDeclaredMethod("setTestField", String.class)).thenThrow(new RuntimeException("Failed to set field"));

        when(mock2.getClass().getDeclaredField("testField")).thenReturn(field);
        when(mock2.getClass().getDeclaredMethod("setTestField", String.class)).then(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                String value = "test";
                setFieldValue(mock2, "testField", value);
                return null;
            }
        });

        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        String result = (String) actualInjecter.thenInject();

        assertEquals("test", result);
        verify(mock2).setTestField("test");
        verifyNoInteractions(mock1);
    }
}