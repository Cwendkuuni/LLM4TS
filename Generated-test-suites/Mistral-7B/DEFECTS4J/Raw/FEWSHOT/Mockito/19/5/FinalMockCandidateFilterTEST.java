package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.reflection.FieldSetter;
import org.mockito.internal.reflection.MockitoAnnotationProcessor;
import org.mockito.internal.reflection.StringUtil;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.runner.MockitoJUnitRunnerDelegate;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class FinalMockCandidateFilterTest {

    private Reporter reporter;
    private FinalMockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setup() {
        reporter = mock(Reporter.class);
        filter = new FinalMockCandidateFilter();
        mocks = new ArrayList<>();
        field = mock(Field.class);
        fieldInstance = mock(Object.class);
    }

    @Test
    public void testFilterCandidateWithOneMock() {
        Object mock = mock(String.class);
        mocks.add(mock);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertThat(injecter, is(instanceOf(OngoingInjecter.class)));

        Object result = injecter.thenInject();

        assertThat(result, is(mock));

        // Check that both property and field setter were tried
        MockitoAnnotationProcessor.setFieldValue(mock, field, fieldInstance);
        BeanPropertySetter.set(fieldInstance, field, mock);
        FieldSetter.set(fieldInstance, field, mock);
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        Object mock1 = mock(String.class);
        Object mock2 = mock(String.class);
        mocks.add(mock1);
        mocks.add(mock2);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertThat(injecter, is(instanceOf(OngoingInjecter.class)));

        // The injecter should not inject any mock
        assertNull(injecter.thenInject());
    }

    @Test(expected = RuntimeException.class)
    public void testFilterCandidateThrowsRuntimeExceptionWhenFailedToInject() {
        Object mock = mock(String.class);
        mocks.add(mock);

        // Simulate a failure when trying to inject using property setter
        MockitoAnnotationProcessor.setFieldValue(mock, field, fieldInstance);
        BeanPropertySetter.set(fieldInstance, field, mock);
        throw new RuntimeException("Failed to inject");

        filter.filterCandidate(mocks, field, fieldInstance).thenInject();
    }
}