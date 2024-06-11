package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.mocktype.MockType;
import org.mockito.internal.configuration.injection.injector.OngoingInjecter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.reflection.BeanPropertySetter;
import org.mockito.internal.reflection.FieldSetter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.MockIterator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class FinalMockCandidateFilterTest {

    private Reporter reporter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setup() {
        reporter = mock(Reporter.class);
        mocks = new ArrayList<>();
        field = mock(Field.class);
        fieldInstance = mock(Object.class);
    }

    @Test
    public void testFilterCandidate_OneMock_PropertySetterSuccess() {
        Object mock = mock(MockType.class);
        BeanPropertySetter propertySetter = spy(new BeanPropertySetter(fieldInstance, field));
        doAnswer(invocation -> propertySetter.set(mock)).when(propertySetter).set(any());

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter filterResult = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(filterResult);
        assertEquals(mock, filterResult.thenInject());
    }

    @Test
    public void testFilterCandidate_OneMock_PropertySetterFailure() {
        Object mock = mock(MockType.class);
        BeanPropertySetter propertySetter = mock(BeanPropertySetter.class);
        doThrow(new RuntimeException()).when(propertySetter).set(any());

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter filterResult = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(filterResult);
        assertNull(filterResult.thenInject());

        verify(reporter, times(1)).cannotInjectDependency(field, mock, any(RuntimeException.class));
    }

    @Test
    public void testFilterCandidate_OneMock_FieldSetterSuccess() {
        Object mock = mock(MockType.class);
        FieldSetter fieldSetter = spy(new FieldSetter(fieldInstance, field));
        doAnswer(invocation -> fieldSetter.set(mock)).when(fieldSetter).set(any());

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter filterResult = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(filterResult);
        assertEquals(mock, filterResult.thenInject());
    }

    @Test
    public void testFilterCandidate_MultipleMocks_NoMatchingMock() {
        Object mock1 = mock(MockType.class);
        Object mock2 = mock(MockType.class);
        mocks.add(mock1);
        mocks.add(mock2);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter filterResult = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(filterResult);
        assertNull(filterResult.thenInject());
    }

    @Test
    public void testFilterCandidate_MultipleMocks_MatchingMock() {
        Object mock = mock(MockType.class);
        mocks.add(mock);
        mocks.add(mock);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter filterResult = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(filterResult);
        assertEquals(mock, filterResult.thenInject());
    }
}