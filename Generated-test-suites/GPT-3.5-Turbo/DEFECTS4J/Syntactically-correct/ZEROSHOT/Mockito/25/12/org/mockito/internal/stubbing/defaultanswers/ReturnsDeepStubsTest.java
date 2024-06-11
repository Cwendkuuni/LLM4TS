
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
import org.mockito.stubbing.Answer;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {
    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setup() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);

        when(invocation.getMock()).thenReturn("mock");
        when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(new HashSet<StubbedInvocationMatcher>() {{ add(stubbedInvocationMatcher); }});
        when(container.getInvocationForStubbing().matches(any())).thenReturn(true);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn("answer");
        when(new MockUtil().getMockHandler("mock")).thenReturn(handler);

        Object result = returnsDeepStubs.answer(invocation);

        verify(invocation, times(1)).getMock();
        verify(handler, times(1)).getInvocationContainer();
        verify(container, times(1)).getStubbedInvocations();
        verify(container, times(1)).getInvocationForStubbing();
        verify(stubbedInvocationMatcher, times(1)).answer(invocation);
        verify(handler, times(0)).getMockSettings();

        assertEquals("answer", result);
    }

    @Test
    public void testGetMock() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);

        when(invocation.getMock()).thenReturn("mock");
        when(new MockUtil().getMockHandler("mock")).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(new HashSet<StubbedInvocationMatcher>() {{ add(stubbedInvocationMatcher); }});
        when(container.getInvocationForStubbing().matches(any())).thenReturn(true);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn("answer");

        Object result = returnsDeepStubs.getMock(invocation);

        verify(invocation, times(1)).getMock();
        verify(handler, times(1)).getInvocationContainer();
        verify(container, times(1)).getStubbedInvocations();
        verify(container, times(1)).getInvocationForStubbing();
        verify(stubbedInvocationMatcher, times(1)).answer(invocation);
        verify(handler, times(0)).getMockSettings();

        assertEquals("answer", result);
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        when(invocation.getMethod().getReturnType()).thenReturn(String.class);
        when(new MockUtil().getMockHandler(invocation.getMock())).thenReturn(mock(InternalMockHandler.class));

        Object result = returnsDeepStubs.recordDeepStubMock(invocation, container);

        verify(invocation, times(1)).getMethod();
        verify(invocation.getMethod(), times(1)).getReturnType();
        verify(new MockUtil(), times(1)).getMockHandler(invocation.getMock());
        verify(container, times(1)).addAnswer(any(), eq(false));
        verify(new MockUtil(), times(0)).getMockSettings();

        assertNotNull(result);
        assertEquals(String.class, result.getClass());
    }

    @Test
    public void testActualParameterizedType() {
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);

        when(new MockUtil().getMockHandler(mock)).thenReturn(mock(InternalMockHandler.class));
        when(mockSettings.getTypeToMock()).thenReturn(Object.class);

        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

        verify(new MockUtil(), times(1)).getMockHandler(mock);
        verify(mockSettings, times(1)).getTypeToMock();

        assertNotNull(result);
    }
}
