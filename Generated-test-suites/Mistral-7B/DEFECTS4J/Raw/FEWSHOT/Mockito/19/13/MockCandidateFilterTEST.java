package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.reflection.Whitebox.getInternalState;
import static org.mockito.internal.util.reflection.Whitebox.setInternalState;

import java.lang.reflect.Collection;
import java.lang.reflect.Field;
import java.util.Arrays;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.InjectionPoint;
import org.mockito.internal.configuration.MockBean;
import org.mockito.internal.configuration.MockitoSession;
import org.mockito.internal.configuration.MockitoSpy;
import org.mockito.internal.util.reflection.Whitebox;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate_NullMocks() {
        MockCandidateFilter filter = Mockito.spy(MockitoSpy.class);
        assertNull(filter.filterCandidate(null, null, null));
    }

    @Test
    public void testFilterCandidate_NullField() {
        MockCandidateFilter filter = Mockito.spy(MockitoSpy.class);
        Collection<Object> mocks = Arrays.asList(Mockito.mock(Object.class));
        Object fieldInstance = new Object();
        assertNull(filter.filterCandidate(mocks, null, fieldInstance));
    }

    @Test
    public void testFilterCandidate_NonMockFieldInstance() {
        MockCandidateFilter filter = Mockito.spy(MockitoSpy.class);
        Collection<Object> mocks = Arrays.asList(Mockito.mock(Object.class));
        Object fieldInstance = new Object();

        assertNull(filter.filterCandidate(mocks, new Field("field", Object.class, null, null), fieldInstance));
    }

    @Test
    public void testFilterCandidate_MockFieldInstance() {
        MockCandidateFilter filter = Mockito.spy(MockitoSpy.class);
        Collection<Object> mocks = Arrays.asList(Mockito.mock(Object.class), Mockito.mock(Object.class));
        Object fieldInstance = Mockito.spy(new Object());
        InjectionPoint injectionPoint = new InjectionPoint(new MockBean(Object.class), new Field("field", Object.class, null, null), fieldInstance);

        MockitoSession session = Mockito.session();
        Whitebox.setInternalState(filter, "session", session);

        OngoingInjecter result = filter.filterCandidate(mocks, new Field("field", Object.class, null, null), fieldInstance);
        assertNull(result);
        session.finishMocking();
    }

    @Test
    public void testFilterCandidate_MockFieldInstance_FieldIsAnnotatedWithMockBean() {
        MockCandidateFilter filter = Mockito.spy(MockitoSpy.class);
        Collection<Object> mocks = Arrays.asList(Mockito.mock(Object.class), Mockito.mock(Object.class));
        Object fieldInstance = Mockito.spy(new Object());
        Field field = new Field("field", Object.class, null, null);
        field.setAnnotation(new MockBean());

        MockitoSession session = Mockito.session();
        Whitebox.setInternalState(filter, "session", session);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
        session.finishMocking();
    }

    @Test
    public void testFilterCandidate_MockFieldInstance_FieldIsAnnotatedWithMock() {
        MockCandidateFilter filter = Mockito.spy(MockitoSpy.class);
        Collection<Object> mocks = Arrays.asList(Mockito.mock(Object.class), Mockito.mock(Object.class));
        Object fieldInstance = Mockito.spy(new Object());
        Field field = new Field("field", Object.class, null, null);
        field.setAnnotation(Mockito.class);

        MockitoSession session = Mockito.session();
        Whitebox.setInternalState(filter, "session", session);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
        session.finishMocking();
    }

    @Test
    public void testFilterCandidate_MockFieldInstance_FieldIsAnnotatedWithMockAndMockBean() {
        MockCandidateFilter filter = Mockito.spy(MockitoSpy.class);
        Collection<Object> mocks = Arrays.asList(Mockito.mock(Object.class), Mockito.mock(Object.class));
        Object fieldInstance = Mockito.spy(new Object());
        Field field = new Field("field", Object.class, null, null);
        field.setAnnotation(Mockito.class);
        field.setAnnotation(new MockBean());

        MockitoSession session = Mockito.session();
        Whitebox.setInternalState(filter, "session", session);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
        session.finishMocking();
    }

}