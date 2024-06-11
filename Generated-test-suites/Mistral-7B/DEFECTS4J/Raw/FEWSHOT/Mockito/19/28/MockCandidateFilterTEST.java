package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.reflection.Whitebox.getInternalState;
import static org.mockito.internal.util.reflection.Whitebox.setInternalState;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.configuration.MockBean;
import org.mockito.internal.configuration.injection.InjectableField;
import org.mockito.internal.configuration.injection.MockService;
import org.mockito.internal.util.reflection.Whitebox;

public class MockCandidateFilterTest {

    @Test
    public void testNullFilter() {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = Mockito.class.getSuperclass().getDeclaredField("mocks");
        Object fieldInstance = MockitoAnnotations.getMocks(new Object[0]);

        assertNull(MockCandidateFilter.class.cast(null).filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testEmptyCollection() {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = Mockito.class.getSuperclass().getDeclaredField("mocks");
        Object fieldInstance = MockitoAnnotations.getMocks(new Object[0]);

        MockCandidateFilter filter = Mockito.mock(MockCandidateFilter.class);

        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate() {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = Mockito.class.getSuperclass().getDeclaredField("mocks");
        Object fieldInstance = MockitoAnnotations.getMocks(new Object[0]);

        MockCandidateFilter filter = Mockito.mock(MockCandidateFilter.class);
        MockService mockService = Mockito.mock(MockService.class);
        Whitebox.setInternalState(filter, "mockService", mockService);

        InjectableField injectableField = Mockito.mock(InjectableField.class);
        Whitebox.setInternalState(injectableField, "field", fieldInstance);
        Whitebox.setInternalState(injectableField, "fieldName", "testFieldName");
        Whitebox.setInternalState(injectableField, "fieldType", String.class);

        Whitebox.setInternalState(fieldToBeInjected, Mockito.MOCKITO_KEY_FIELD_ANNOTATIONS, new MockBean(String.class, "testMockName"));

        mocks.add(injectableField);

        MockService result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        Mockito.verify(mockService).createMock(String.class, "testMockName");
        Mockito.verifyNoMoreInteractions(mockService);

        Mockito.verify(filter).mockService(mockService);
    }
}