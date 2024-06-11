package org.mockito.internal.configuration.injection;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.MockitoConfiguration;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.MockitoInternalConfiguration;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.MockitoStubInvocationHandler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_CollectionSizeOne() {
        MockitoConfiguration configuration = mock(MockitoConfiguration.class);
        MockitoInternalConfiguration internalConfiguration = mock(MockitoInternalConfiguration.class);
        OngoingInjecter filter = new FinalMockCandidateFilter().filterCandidate(mock(Collection.class), mock(Field.class), mock(Object.class));

        Object mock = Mockito.spy(new Object() {});
        when(configuration.getInternalConfiguration()).thenReturn(internalConfiguration);
        when(internalConfiguration.isMockCreationEnabled()).thenReturn(true);
        when(internalConfiguration.isFieldAccessible()).thenReturn(true);

        Assert.assertTrue(filter.thenInject());
        verify(configuration).injectFields(any(Object.class), any(Field.class), any(Object.class));
        verifyNoMoreInteractions(configuration);
    }

    @Test
    public void testFilterCandidate_CollectionSizeOne_NoSetter() {
        MockitoConfiguration configuration = mock(MockitoConfiguration.class);
        MockitoInternalConfiguration internalConfiguration = mock(MockitoInternalConfiguration.class);
        OngoingInjecter filter = new FinalMockCandidateFilter().filterCandidate(mock(Collection.class), mock(Field.class), mock(Object.class));

        Object mock = Mockito.spy(new Object() {});
        when(configuration.getInternalConfiguration()).thenReturn(internalConfiguration);
        when(internalConfiguration.isMockCreationEnabled()).thenReturn(true);
        when(internalConfiguration.isFieldAccessible()).thenReturn(false);

        Assert.assertFalse(filter.thenInject());
        verifyNoMoreInteractions(configuration);
    }

    @Test
    public void testFilterCandidate_CollectionSizeOne_ExceptionThrown() {
        MockitoConfiguration configuration = mock(MockitoConfiguration.class);
        MockitoInternalConfiguration internalConfiguration = mock(MockitoInternalConfiguration.class);
        OngoingInjecter filter = new FinalMockCandidateFilter().filterCandidate(mock(Collection.class), mock(Field.class), mock(Object.class));

        Object mock = Mockito.spy(new Object() {});
        when(configuration.getInternalConfiguration()).thenReturn(internalConfiguration);
        when(internalConfiguration.isMockCreationEnabled()).thenReturn(true);
        when(internalConfiguration.isFieldAccessible()).thenReturn(true);
        when(new FieldSetter(mock, mock(Field.class))::set).thenThrow(new MockitoException("test exception"));

        Assert.assertFalse(filter.thenInject());
        verify(configuration).injectFields(any(Object.class), any(Field.class), any(Object.class));
        verify(configuration).throwException(any(MockitoException.class));
    }

    @Test
    public void testFilterCandidate_CollectionSizeMoreThanOne() {
        MockitoConfiguration configuration = mock(MockitoConfiguration.class);
        MockitoInternalConfiguration internalConfiguration = mock(MockitoInternalConfiguration.class);
        OngoingInjecter filter = new FinalMockCandidateFilter().filterCandidate(mock(Collection.class), mock(Field.class), mock(Object.class));

        Object mock1 = Mockito.spy(new Object() {});
        Object mock2 = Mockito.spy(new Object() {});
        when(configuration.getInternalConfiguration()).thenReturn(internalConfiguration);
        when(internalConfiguration.isMockCreationEnabled()).thenReturn(true);
        when(internalConfiguration.isFieldAccessible()).thenReturn(true);

        Assert.assertFalse(filter.thenInject());
        verifyNoMoreInteractions(configuration);
    }
}