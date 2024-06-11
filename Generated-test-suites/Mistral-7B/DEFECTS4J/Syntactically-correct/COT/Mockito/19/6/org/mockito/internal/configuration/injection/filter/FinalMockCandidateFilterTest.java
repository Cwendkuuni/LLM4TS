package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * JUnit 4 test file for the FinalMockCandidateFilter class.
 */
public class FinalMockCandidateFilterTest {

    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setup() {
        this.mocks = new ArrayList<>();
        this.field = mock(Field.class);
        this.fieldInstance = mock(Object.class);
    }

    @Test
    public void testFilterCandidate_OneMock_Success() {
        Object mock = mock(Object.class);

        this.mocks.add(mock);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter result = filter.filterCandidate(this.mocks, this.field, this.fieldInstance);

        assertNotNull(result);
        verify(mock).setField(this.fieldInstance);
    }

    @Test
    public void testFilterCandidate_OneMock_FailedBeanPropertySetter() {
        when(this.field.getName()).thenReturn("testField");
        when(this.fieldInstance.getClass()).thenReturn(Object.class);
        when(mock(BeanPropertySetter.class).set(this.fieldInstance, this.field)).thenThrow(new RuntimeException());

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter result = filter.filterCandidate(this.mocks, this.field, this.fieldInstance);

        assertNotNull(result);
        verify(new Reporter()).cannotInjectDependency(this.field, this.mocks.iterator().next(), new RuntimeException());
    }

    @Test
    public void testFilterCandidate_OneMock_FailedFieldSetter() {
        when(this.field.getName()).thenReturn("testField");
        when(this.fieldInstance.getClass()).thenReturn(Object.class);
        when(mock(BeanPropertySetter.class).set(this.fieldInstance, this.field)).thenReturn(true);
        when(mock(FieldSetter.class).set(this.fieldInstance, this.field)).thenThrow(new RuntimeException());

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter result = filter.filterCandidate(this.mocks, this.field, this.fieldInstance);

        assertNotNull(result);
        verify(new Reporter()).cannotInjectDependency(this.field, this.mocks.iterator().next(), new RuntimeException());
    }

    @Test
    public void testFilterCandidate_MultipleMocks_NullResult() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter result = filter.filterCandidate(this.mocks, this.field, this.fieldInstance);

        assertNotNull(result);
        verifyNoInteractions(this.mocks);
    }
}