package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Object fieldInstance;
    private Object mock1;
    private Object mock2;
    private Field field;
    private Reporter reporter;

    @Before
    public void setUp() {
        reporter = Mockito.spy(new Reporter());
        fieldInstance = mock(Object.class);
        mock1 = mock(Object.class);
        mock2 = mock(Object.class);
        field = Mockito.spy(new Field("testField", String.class, FieldType.PRIVATE, null) {
            @Override
            public Object get(Object obj) throws IllegalAccessException {
                return null;
            }

            @Override
            public void set(Object obj, Object value) throws IllegalAccessException {
                super.set(obj, value);
            }
        });

        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void testSingleMock() {
        List<Object> mocks = new ArrayList<>(1);
        mocks.add(mock1);

        filter.filterCandidate(mocks, field, fieldInstance);

        verify(mock1).setField(fieldInstance, "expectedValue"); // Should be set by BeanPropertySetter
        verify(reporter, Mockito.never()).cannotInjectDependency(Mockito.anyField(), Mockito.any(), Mockito.any(RuntimeException.class));
    }

    @Test
    public void testMultipleMocks() {
        List<Object> mocks = new ArrayList<>(2);
        mocks.add(mock1);
        mocks.add(mock2);

        filter.filterCandidate(mocks, field, fieldInstance);

        verify(reporter, Mockito.never()).cannotInjectDependency(Mockito.anyField(), Mockito.any(), Mockito.any(RuntimeException.class));
    }

    @Test(expected = RuntimeException.class)
    public void testFailedInjection() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, InstantiationException {
        field.setAccessible(true);

        List<Object> mocks = Collections.singletonList(mock1);

        filter.filterCandidate(mocks, field, fieldInstance);

        field.set(mock1, "expectedValue"); // Should be set by FieldSetter

        // This test case will fail, deliberately, to simulate a failed injection
        field.set(fieldInstance, mock2);

        verify(reporter, Mockito.once()).cannotInjectDependency(Mockito.anyField(), mock1, Mockito.any(RuntimeException.class));
    }
}