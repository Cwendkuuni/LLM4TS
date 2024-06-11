package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.reflection.FieldSetter;
import org.mockito.internal.reflection.MethodUtil;
import org.mockito.internal.reflection.Whitebox;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class FinalMockCandidateFilterTest {

    private static final String FIELD_NAME = "fieldName";
    private static final String FIELD_TYPE = "String.class";
    private static final Object FIELD_VALUE = "fieldValue";
    private static final Object MOCK_INSTANCE = mock(Object.class);
    private static final Object MOCK_FIELD_INSTANCE = mock(Object.class, new Answer<Object>() {
        @Override
        public Object answer(InvocationOnMock invocation) {
            return FIELD_VALUE;
        }
    });
    private static final Object MOCK_MATCHING_MOCK = mock(Object.class);
    private static final Object MOCK_REPORTER = mock(Reporter.class);
    private static final Object MOCK_BEAN_PROPERTY_SETTER = mock(BeanPropertySetter.class);
    private static final Object MOCK_FIELD_SETTER = mock(FieldSetter.class);

    private FinalMockCandidateFilter filter;
    private OngoingInjecter injecter;

    @Before
    public void setUp() {
        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidateWithOneMock() {
        List<Object> mocks = new ArrayList<Object>() {{
            add(MOCK_MATCHING_MOCK);
        }};

        Field field = MethodUtil.getDeclaredField(MOCK_INSTANCE.getClass(), FIELD_NAME, FIELD_TYPE);
        Object fieldInstance = Whitebox.getInternalState(MOCK_INSTANCE, field);

        injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(MOCK_MATCHING_MOCK, injecter.thenInject());
        verify(MOCK_BEAN_PROPERTY_SETTER).set(fieldInstance, MOCK_MATCHING_MOCK, FIELD_VALUE);
        verify(MOCK_FIELD_SETTER).set(fieldInstance, field, MOCK_MATCHING_MOCK);
        verify(MOCK_REPORTER).cannotInjectDependency(field, MOCK_MATCHING_MOCK, Mockito.isInstanceOf(RuntimeException.class));
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        List<Object> mocks = new ArrayList<Object>() {{
            add(MOCK_INSTANCE);
            add(MOCK_MATCHING_MOCK);
        }};

        Field field = MethodUtil.getDeclaredField(MOCK_INSTANCE.getClass(), FIELD_NAME, FIELD_TYPE);
        Object fieldInstance = Whitebox.getInternalState(MOCK_INSTANCE, field);

        injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(injecter.thenInject());
    }

    @Test
    public void testFilterCandidateWithNullMocks() {
        Field field = MethodUtil.getDeclaredField(MOCK_INSTANCE.getClass(), FIELD_NAME, FIELD_TYPE);
        Object fieldInstance = Whitebox.getInternalState(MOCK_INSTANCE, field);

        injecter = filter.filterCandidate(null, field, fieldInstance);

        assertNull(injecter.thenInject());
    }

    @Test
    public void testFilterCandidateWithNullField() {
        List<Object> mocks = new ArrayList<Object>() {{
            add(MOCK_MATCHING_MOCK);
        }};

        Object fieldInstance = Whitebox.getInternalState(MOCK_INSTANCE, "nonExistentField");

        injecter = filter.filterCandidate(mocks, null, fieldInstance);

        assertNull(injecter.thenInject());
    }

    @Test
    public void testFilterCandidateWithNullFieldInstance() {
        List<Object> mocks = new ArrayList<Object>() {{
            add(MOCK_MATCHING_MOCK);
        }};

        Field field = MethodUtil.getDeclaredField(MOCK_INSTANCE.getClass(), FIELD_NAME, FIELD_TYPE);

        injecter = filter.filterCandidate(mocks, null, null);

        assertNull(injecter.thenInject());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidateWithEmptyMocks() {
        Field field = MethodUtil.getDeclaredField(MOCK_INSTANCE.getClass(), FIELD_NAME, FIELD_TYPE);
        Object fieldInstance = Whitebox.getInternalState(MOCK_INSTANCE, field);

        filter.filterCandidate(Collections.emptyList(), field, fieldInstance);
    }
}