package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.InjectingFieldTestUtils;
import org.mockito.internal.util.reflection.TestFields;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Reporter reporter;
    private Field field;
    private Object fieldInstance;
    private Collection<Object> mocks;

    @Rule
    public TestRule testRule = new TestFields(this);

    @Before
    public void setUp() {
        filter = new FinalMockCandidateFilter();
        reporter = mock(Reporter.class);
        field = TestFields.field(FinalMockCandidateFilterTest.class, "field");
        fieldInstance = new Object();
        mocks = Arrays.asList(mock(Object.class), mock(Object.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFilterCandidateWithOneMock() {
        // Given
        Object mock = mock(Object.class);
        when(mock.getClass()).thenReturn(Object.class);

        // When
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertThat(injecter, is(notNullValue()));
        assertThat(injecter, is(instanceOf(OngoingInjecter.class)));
        assertThat(injecter.thenInject(), is(sameInstance(mock)));
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        // Given

        // When
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertThat(injecter, is(notNullValue()));
        assertThat(injecter, is(instanceOf(OngoingInjecter.class)));
        verifyNoMoreInteractions(mocks);
    }

    @Test(expected = RuntimeException.class)
    public void testFilterCandidateWithException() {
        // Given
        when(field.getType()).thenReturn(String.class);
        when(fieldInstance.getClass()).thenReturn(Object.class);
        when(mocks.iterator().next().getClass()).thenReturn(Integer.class);

        // When and Then
        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testInjectingFieldWithPropertySetter() {
        // Given
        Object mock = mock(Object.class);
        when(mock.getClass()).thenReturn(String.class);
        when(field.getType()).thenReturn(String.class);
        when(fieldInstance.getClass()).thenReturn(Object.class);
        when(mocks.iterator().next().getClass()).thenReturn(String.class);
        Object value = "testValue";

        BeanPropertySetter propertySetter = new BeanPropertySetter(fieldInstance, field);
        propertySetter.set(mock, value);

        when(mocks.iterator().next()).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return mock;
            }
        });

        // When
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        // Then
        assertThat(injecter, is(notNullValue()));
        assertThat(injecter, is(instanceOf(OngoingInjecter.class)));
        assertThat(result, is(sameInstance(mock)));
        verify(mock, never()).getClass();
    }

    @Test
    public void testInjectingFieldWithFieldSetter() {
        // Given
        Object mock = mock(Object.class);
        when(mock.getClass()).thenReturn(String.class);
        when(field.getType()).thenReturn(String.class);
        when(fieldInstance.getClass()).thenReturn(Object.class);
        when(mocks.iterator().next().getClass()).thenReturn(String.class);
        Object value = "testValue";

        FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
        fieldSetter.set(mock, value);

        when(mocks.iterator().next()).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return mock;
            }
        });

        // When
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        // Then
        assertThat(injecter, is(notNullValue()));
        assertThat(injecter, is(instanceOf(OngoingInjecter.class)));
        assertThat(result, is(sameInstance(mock)));
        verify(mock, never()).getClass();
    }

    @Test
    public void testReturnNullInjecterWithMultipleMocks() {
        // Given

        // When
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertThat(injecter, is(notNullValue()));
        assertThat(injecter, is(instanceOf(OngoingInjecter.class)));
        verifyNoMoreInteractions(mocks);
    }
}