package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setup() {
        filter = new FinalMockCandidateFilter();
        mocks = new ArrayList<>();
        field = Mockito.spy(Mockito.field(FinalMockCandidateFilter.class, "field"));
        fieldInstance = Mockito.mock(Object.class);
    }

    @Test
    public void testFilterCandidate_OneMock_PropertySetSuccessfully() {
        Object mock = mock(Object.class);
        mocks.add(mock);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();

        assertNotNull(fieldInstance);
        Mockito.verify(mock).set(fieldInstance, field);
    }

    @Test
    public void testFilterCandidate_OneMock_FieldSetSuccessfully() {
        Object mock = mock(Object.class);
        mocks.add(mock);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();

        assertNotNull(fieldInstance);
        Mockito.verify(fieldInstance, Mockito.times(1)).set(mock, field);
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_OneMock_PropertySetFailed() {
        Object mock = mock(Object.class);
        mocks.add(mock);

        Mockito.doThrow(new RuntimeException("Test Exception")).when(field).set(Mockito.any(), Mockito.any());

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();

        // This test case should throw a MockitoException as the property setter fails
    }

    @Test
    public void testFilterCandidate_MultipleMocks_NoInject() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();

        assertNull(fieldInstance);
    }
}