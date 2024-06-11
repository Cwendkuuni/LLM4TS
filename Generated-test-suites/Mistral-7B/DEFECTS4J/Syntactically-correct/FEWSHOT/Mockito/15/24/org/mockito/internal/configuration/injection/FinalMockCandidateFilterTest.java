package org.mockito.internal.configuration.injection.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldUtils;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.mock.MockComponent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Object fieldInstance;
    private Field field;
    private Collection<Object> mocks;
    private MockComponent mockComponent1;
    private MockComponent mockComponent2;

    @Before
    public void setup() throws Exception {
        filter = new FinalMockCandidateFilter();
        fieldInstance = mock(Object.class);
        field = FieldUtils.getField(fieldInstance, "testField");
        mocks = new ArrayList<>();
        mockComponent1 = mockComponent(MockComponent.class, "mockComponent1");
        mockComponent2 = mockComponent(MockComponent.class, "mockComponent2");
        mocks.add(mockComponent1);
        mocks.add(mockComponent2);
    }

    @Test
    public void testFilterCandidateWithSingleMock() {
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertInstanceOf(OngoingInjecter.class, injecter);
        assertNotNull(injecter.getThenInjectMethod());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidateWithSingleMockFailure() throws Exception {
        Whitebox.setInternalState(filter, "throwExceptionOnFailure", true);
        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidateWithMultipleMocksAndSuccess() throws Exception {
        Object mockComponent = mockComponent1;
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(injecter.thenInject());
        assertSame(mockComponent, fieldInstance);
    }

    @Test
    public void testFilterCandidateWithMultipleMocksAndFailure() throws Exception {
        Object otherMockComponent = mockComponent2;
        Whitebox.setInternalState(filter, "throwExceptionOnFailure", true);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertInstanceOf(OngoingInjecter.class, injecter);
        assertNotNull(injecter.getThenInjectMethod());
        assertSame(otherMockComponent, fieldInstance);
    }

    @Test
    public void testSetFieldViaSetter() throws Exception {
        Object mockComponent = mockComponent1;

        FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
        fieldSetter.set(mockComponent);

        assertSame(mockComponent, fieldInstance);
    }

    @Test(expected = MockitoException.class)
    public void testSetFieldViaSetterFailure() throws Exception {
        Object otherMockComponent = mockComponent2;

        Field fieldSpy = spy(field);

        Whitebox.setInternalState(filter, "throwExceptionOnFailure", true);

        new FieldSetter(fieldInstance, fieldSpy).set(otherMockComponent);
    }
}

class MockComponent {
    // Mock Component class
}