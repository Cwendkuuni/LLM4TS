import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        
        when(invocation.getMock()).thenReturn(returnTypeGenericMetadata);
        when(returnTypeGenericMetadata.resolveGenericReturnType(any())).thenReturn(returnTypeGenericMetadata);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class<?>[] {});
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[] {});
        
        Object result = returnsDeepStubs.answer(invocation);
        
        assertNull(result);
    }

    @Test
    public void testGetMock() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        MockUtil mockUtil = mock(MockUtil.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        
        when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(handler);
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[] {stubbedInvocationMatcher});
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn("mockResult");
        
        Object result = returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata);
        
        assertEquals("mockResult", result);
    }

    @Test
    public void testCreateNewDeepStubMock() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        MockitoCore mockitoCore = mock(MockitoCore.class);
        
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(mockitoCore.mock(Object.class, withSettingsUsing(returnTypeGenericMetadata))).thenReturn("mockObject");
        
        Object result = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);
        
        assertEquals("mockObject", result);
    }

    @Test
    public void testWithSettingsUsing() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        MockSettings mockSettingsMock = mock(MockSettings.class);
        
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class<?>[]{});
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(mockSettingsMock.extraInterfaces(returnTypeGenericMetadata.rawExtraInterfaces())).thenReturn(mockSettingsMock);
        when(mockSettingsMock.defaultAnswer(returnsDeepStubsAnswerUsing(returnTypeGenericMetadata))).thenReturn(mockSettingsMock);
        
        MockSettings result = (MockSettings) returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);
        
        assertSame(mockSettingsMock, result);
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        
        ReturnsDeepStubs result = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
        
        assertNotNull(result);
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        Object mock = mock(Object.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        Answer<Object> answer = mock(Answer.class);
        Answer<Object> additionalAnswer = mock(Answer.class);
        
        when(container.addAnswer(answer, false)).thenReturn(additionalAnswer);
        
        Object result = returnsDeepStubs.recordDeepStubMock(mock, container);
        
        assertSame(mock, result);
        assertSame(additionalAnswer, result);
    }

    @Test
    public void testActualParameterizedType() {
        Object mock = mock(Object.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        MockUtil mockUtil = mock(MockUtil.class);
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        
        when(mockUtil.getMockHandler(mock)).thenReturn(mockUtil);
        when(mockUtil.getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn("typeToMock");
        when(genericMetadataSupport.inferFrom("typeToMock")).thenReturn(genericMetadataSupport);
        
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);
        
        assertSame(genericMetadataSupport, result);
    }

    private MockSettings withSettingsUsing(GenericMetadataSupport returnTypeGenericMetadata) {
        MockSettings mockSettings =
                returnTypeGenericMetadata.rawExtraInterfaces().length > 0 ?
                withSettings().extraInterfaces(returnTypeGenericMetadata.rawExtraInterfaces())
                : withSettings();

        return mockSettings
                .defaultAnswer(returnsDeepStubsAnswerUsing(returnTypeGenericMetadata));
    }

    private ReturnsDeepStubs returnsDeepStubsAnswerUsing(final GenericMetadataSupport returnTypeGenericMetadata) {
        return new ReturnsDeepStubs() {
            @Override
            protected GenericMetadataSupport actualParameterizedType(Object mock) {
                return returnTypeGenericMetadata;
            }
        };
    }
}