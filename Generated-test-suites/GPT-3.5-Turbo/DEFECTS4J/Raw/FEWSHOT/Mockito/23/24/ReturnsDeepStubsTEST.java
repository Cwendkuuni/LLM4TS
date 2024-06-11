import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.MockSettings;
import org.mockito.Mockito;

public class ReturnsDeepStubsTest {
    private ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    private MockitoCore mockitoCore = new MockitoCore();
    private ReturnsEmptyValues delegate = new ReturnsEmptyValues();
    private MockUtil mockUtil = new MockUtil();

    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        // mock is not type mockable
        when(mockitoCore.isTypeMockable(Object.class)).thenReturn(false);
        assertEquals(delegate.returnValueFor(Object.class), returnsDeepStubs.answer(invocation));

        // mock is type mockable
        when(mockitoCore.isTypeMockable(String.class)).thenReturn(true);

        // raw type has no generic parameters
        when(invocation.getMock()).thenReturn(mock(String.class));
        GenericMetadataSupport genericMetadataSupport1 = mock(GenericMetadataSupport.class);
        when(mockUtil.getMockHandler(mock(String.class))).thenReturn(mock(InternalMockHandler.class));
        when(invocation.getMock()).thenReturn(mock(String.class));
        when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);
        when(returnsDeepStubs.createNewDeepStubMock(genericMetadataSupport1)).thenReturn(mock(String.class));
        when(returnsDeepStubs.recordDeepStubMock(mock(String.class), mock(InvocationContainerImpl.class))).thenReturn(mock(String.class));
        assertEquals(mock(String.class), returnsDeepStubs.answer(invocation));

        // raw type has generic parameters
        when(invocation.getMock()).thenReturn(mock(Integer.class));
        GenericMetadataSupport genericMetadataSupport2 = mock(GenericMetadataSupport.class);
        when(mockUtil.getMockHandler(mock(Integer.class))).thenReturn(mock(InternalMockHandler.class));
        when(invocation.getMock()).thenReturn(mock(Integer.class));
        when(returnTypeGenericMetadata.rawType()).thenReturn(Integer.class);
        when(returnsDeepStubs.createNewDeepStubMock(genericMetadataSupport2)).thenReturn(mock(Integer.class));
        when(returnsDeepStubs.recordDeepStubMock(mock(Integer.class), mock(InvocationContainerImpl.class))).thenReturn(mock(Integer.class));
        assertEquals(mock(Integer.class), returnsDeepStubs.answer(invocation));
    }

    @Test
    public void testGetMock() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);

        // invocation matches stubbedInvocationMatcher
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);
        assertEquals(stubbedInvocationMatcher.answer(invocation), returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata));

        // invocation does not match stubbedInvocationMatcher
        when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);
        when(returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata)).thenReturn(mock(String.class));
        when(returnsDeepStubs.recordDeepStubMock(mock(String.class), container)).thenReturn(mock(String.class));
        assertEquals(mock(String.class), returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata));
    }

    @Test
    public void testCreateNewDeepStubMock() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);
        when(mockitoCore.mock(String.class, withSettingsUsing(returnTypeGenericMetadata))).thenReturn(mock(String.class));
        assertEquals(mock(String.class), returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata));
    }

    @Test
    public void testWithSettingsUsing() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        MockSettings mockSettings = mock(MockSettings.class);

        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[]{Comparable.class});
        when(returnsDeepStubs.withSettings()).thenReturn(mockSettings);

        assertEquals(mockSettings, returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata));

        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[]{});
        assertEquals(mockSettings, returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata));
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        ReturnsDeepStubs actualReturnsDeepStubs = mock(ReturnsDeepStubs.class);

        when(actualReturnsDeepStubs.actualParameterizedType(any(Object.class))).thenReturn(returnTypeGenericMetadata);

        assertEquals(actualReturnsDeepStubs, returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata));
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        Object mock = mock(Object.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        Answer<Object> answer = mock(Answer.class);

        when(answer.answer(any(InvocationOnMock.class))).thenAnswer(invocation -> mock);

        when(container.addAnswer(answer, false)).thenReturn(mock);

        assertEquals(mock, returnsDeepStubs.recordDeepStubMock(mock, container));
    }

    @Test
    public void testActualParameterizedType() {
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);

        when(mockUtil.getMockHandler(mock)).thenReturn(mock(InternalMockHandler.class));
        when(mockUtil.getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(String.class);

        assertEquals(mockSettings, returnsDeepStubs.actualParameterizedType(mock));
    }
}