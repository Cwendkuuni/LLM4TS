package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.reflection.ReflectionUtils.getField;
import static org.mockito.internal.util.reflection.ReflectionUtils.setField;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.MockitoConfiguration;
import org.mockito.internal.configuration.injection.Injectable;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.ReflectionUtils;

public class FinalMockCandidateFilterTest {
    private Reporter reporter;
    private Collection<Object> mocks;
    private Object fieldInstance;
    private Field field;

    @Before
    public void setup() {
        reporter = new Reporter();
        mocks = new ArrayList<>();
        fieldInstance = new Object();
        field = ReflectionUtils.getField(fieldInstance, "fieldToInject");
    }

    @Test
    public void testFilterSingleMock() {
        Object mock = new MockitoConfiguration().mock(Object.class);
        mocks.add(mock);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Injectable injected = new Injectable();
        injecter.thenInject(injected);

        assertNull(field.get(fieldInstance));
        assertEquals(mock, field.get(fieldInstance));
        assertEquals(mock, injected.getMock());
    }

    @Test
    public void testFilterMultipleMocksPropertySetter() {
        Object mock1 = new MockitoConfiguration().mock(Object.class);
        Object mock2 = new MockitoConfiguration().mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Injectable injected1 = new Injectable();
        injecter.thenInject(injected1);
        Injectable injected2 = new Injectable();
        injecter.thenInject(injected2);

        assertNull(field.get(fieldInstance));
        assertEquals(mock1, field.get(fieldInstance));
        assertEquals(mock1, injected1.getMock());
        assertEquals(mock2, injected2.getMock());
    }

    @Test
    public void testFilterMultipleMocksFieldAccess() {
        Object mock1 = new MockitoConfiguration().mock(Object.class);
        Object mock2 = new MockitoConfiguration().mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Injectable injected1 = new Injectable();
        injecter.thenInject(injected1);
        Injectable injected2 = new Injectable();
        injecter.thenInject(injected2);

        setField(fieldInstance, "fieldToInject", mock1);
        assertEquals(mock1, field.get(fieldInstance));
        assertEquals(mock1, injected1.getMock());
        assertEquals(mock2, injected2.getMock());
    }

    @Test(expected = RuntimeException.class)
    public void testFilterCannotInjectDependency() {
        Object mock = new MockitoConfiguration().mock(Object.class);
        mocks.add(mock);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        new Reporter().cannotInjectDependency(field, mock, new RuntimeException("Test error"));
    }

    @Test
    public void testFilterNoMockFound() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(injecter.thenInject());
    }
}