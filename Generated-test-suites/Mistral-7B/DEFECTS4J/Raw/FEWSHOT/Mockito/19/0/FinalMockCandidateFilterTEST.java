package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.MockitoConfiguration;
import org.mockito.internal.configuration.injection.MockInjectionResult;
import org.mockito.internal.configuration.injection.MockingContext;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class FinalMockCandidateFilterTest {

    private Reporter reporter;
    private BeanPropertySetter propertySetter;
    private FieldSetter fieldSetter;
    private MockingContext mockingContext;
    private FinalMockCandidateFilter filter;

    @Before
    public void setup() {
        reporter = mock(Reporter.class);
        propertySetter = spy(new BeanPropertySetter(null, null));
        fieldSetter = spy(new FieldSetter(null, null));
        mockingContext = mock(MockingContext.class);
        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidate_oneMock_propertySetterSuccess() {
        Object mock = mock(Object.class);
        Object fieldInstance = mock(Object.class);
        Field field = getTestField();

        List<Object> mocks = Collections.singletonList(mock);

        MockInjectionResult result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mock, result.getInjectedInstance());
        assertEquals(mock, propertySetter.getTarget());
        assertEquals(field, propertySetter.getProperty());
        assertEquals(fieldInstance, propertySetter.getFieldInstance());
    }

    @Test
    public void testFilterCandidate_oneMock_propertySetterFailure() {
        Object mock = mock(Object.class);
        Object fieldInstance = mock(Object.class);
        Field field = getTestField();

        List<Object> mocks = Collections.singletonList(mock);

        doThrow(new RuntimeException("Test Exception")).when(propertySetter).set(mock, fieldInstance, field);

        MockInjectionResult result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(result.getInjectedInstance());
        assertEquals("Could not inject", reporter.getLastMessage());
    }

    @Test
    public void testFilterCandidate_oneMock_fieldSetterSuccess() {
        Object mock = mock(Object.class);
        Object fieldInstance = mock(Object.class);
        Field field = getTestField();

        List<Object> mocks = Collections.singletonList(mock);

        doCallRealMethod().when(propertySetter).set(mock, fieldInstance, field);
        doNothing().when(fieldSetter).set(mock, fieldInstance, field);

        MockInjectionResult result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mock, result.getInjectedInstance());
        assertEquals(mock, fieldSetter.getTarget());
        assertEquals(field, fieldSetter.getField());
        assertEquals(fieldInstance, fieldSetter.getFieldInstance());
    }

    @Test
    public void testFilterCandidate_oneMock_fieldSetterFailure() {
        Object mock = mock(Object.class);
        Object fieldInstance = mock(Object.class);
        Field field = getTestField();

        List<Object> mocks = Collections.singletonList(mock);

        doCallRealMethod().when(propertySetter).set(mock, fieldInstance, field);
        doThrow(new RuntimeException("Test Exception")).when(fieldSetter).set(mock, fieldInstance, field);

        MockInjectionResult result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(result.getInjectedInstance());
        assertEquals("Could not inject", reporter.getLastMessage());
    }

    @Test
    public void testFilterCandidate_multipleMocks_noInject() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object fieldInstance = mock(Object.class);
        Field field = getTestField();

        List<Object> mocks = Arrays.asList(mock1, mock2);

        MockInjectionResult result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(result.getInjectedInstance());
    }

    private Field getTestField() {
        try {
            return Whitebox.getInternalState(MockitoConfiguration.class, "fieldToInject");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}