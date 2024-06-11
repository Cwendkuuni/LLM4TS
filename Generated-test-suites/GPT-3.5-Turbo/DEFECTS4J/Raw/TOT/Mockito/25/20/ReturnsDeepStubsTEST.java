import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;

import java.util.*;

import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setUp() {
        this.returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void answer_shouldReturnEmptyValueWhenNonMockableReturnType() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));

        Object result = this.returnsDeepStubs.answer(invocation);

        assert result == null;
    }

    @Test
    public void answer_shouldReturnEmptyValueCollectionWhenReturnTypeIsSet() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Set.class));

        Object result = this.returnsDeepStubs.answer(invocation);

        assert result != null;
        assert result instanceof Set;
        assert ((Set) result).isEmpty();
    }

    @Test
    public void answer_shouldReturnEmptyValueMapWhenReturnTypeIsMap() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Map.class));

        Object result = this.returnsDeepStubs.answer(invocation);

        assert result != null;
        assert result instanceof Map;
        assert ((Map) result).isEmpty();
    }

    @Test
    public void getMock_shouldReturnPreviouslyCreatedMockIfInvocationMatches() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));

        // Prepare mock invocation container
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(container.getStubbedInvocations()).thenReturn(new ArrayList<>());
        when(container.getInvocationForStubbing()).thenReturn(invocation);

        // Prepare mock internal mock handler
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(new MockUtil().getMockHandler(invocation.getMock())).thenReturn(handler);

        Object result = this.returnsDeepStubs.getMock(invocation);

        assert result != null;
        assert result instanceof Object;
        assert result.equals(invocation.getMock());
    }

    @Test
    public void getMock_shouldReturnRecordDeepStubMockIfInvocationDoesNotMatch() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));

        // Prepare mock invocation container
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(container.getStubbedInvocations()).thenReturn(new ArrayList<>());
        when(container.getInvocationForStubbing()).thenReturn(mock(InvocationOnMock.class));

        // Prepare mock internal mock handler
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(new MockUtil().getMockHandler(invocation.getMock())).thenReturn(handler);

        Object result = this.returnsDeepStubs.getMock(invocation);

        assert result != null;
        assert result instanceof Object;
        assert !result.equals(invocation.getMock());
    }

    @Test
    public void recordDeepStubMock_shouldCreateMockUsingGenericsMetadata() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("hashCode"));

        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(container.addAnswer(any(), anyBoolean())).thenReturn(0);

        // Prepare mock internal mock handler
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(new MockUtil().getMockHandler(invocation.getMock())).thenReturn(handler);

        Object result = this.returnsDeepStubs.recordDeepStubMock(invocation, container);

        assert result != null;
        assert result instanceof Object;
    }

    @Test
    public void actualParameterizedType_shouldReturnGenericMetadataFromMockSettingsType() throws Throwable {
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(mockSettings.getTypeToMock()).thenReturn(Object.class);

        // Prepare mock internal mock handler
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        when(handler.getMockSettings()).thenReturn(mockSettings);
        when(new MockUtil().getMockHandler(mock)).thenReturn(handler);

        GenericMetadataSupport result = this.returnsDeepStubs.actualParameterizedType(mock);

        assert result != null;
        assert result instanceof GenericMetadataSupport;
        assert result.rawType().equals(Object.class);
    }

}