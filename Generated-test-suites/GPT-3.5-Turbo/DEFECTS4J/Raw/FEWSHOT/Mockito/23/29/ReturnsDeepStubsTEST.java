package org.mockito.internal.stubbing.defaultanswers;

import static org.mockito.Mockito.*;
import org.junit.Test;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsDeepStubsTest {
    @Test
    public void testAnswer() throws Throwable {
        ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);
        when(delegate.returnValueFor(null)).thenReturn(null);

        MockitoCore mockitoCore = mock(MockitoCore.class);
        when(mockitoCore.isTypeMockable(null)).thenReturn(false);
        when(mockitoCore.isTypeMockable(String.class)).thenReturn(true);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        returnsDeepStubs.delegate = delegate;
        returnsDeepStubs.mockitoCore = mockitoCore;

        InvocationOnMock invocation1 = mock(InvocationOnMock.class);
        when(invocation1.getMock()).thenReturn(null);

        GenericMetadataSupport returnTypeGenericMetadata1 = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata1.rawType()).thenReturn(null);
        when(returnTypeGenericMetadata1.rawExtraInterfaces()).thenReturn(new Class[0]);

        assertEquals(null, returnsDeepStubs.answer(invocation1));


        InvocationOnMock invocation2 = mock(InvocationOnMock.class);
        when(invocation2.getMock()).thenReturn(null);

        GenericMetadataSupport returnTypeGenericMetadata2 = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata2.rawType()).thenReturn(String.class);
        when(returnTypeGenericMetadata2.rawExtraInterfaces()).thenReturn(new Class[0]);

        assertEquals(null, returnsDeepStubs.answer(invocation2));


        InvocationOnMock invocation3 = mock(InvocationOnMock.class);
        when(invocation3.getMock()).thenReturn(null);

        GenericMetadataSupport returnTypeGenericMetadata3 = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata3.rawType()).thenReturn(null);
        when(returnTypeGenericMetadata3.rawExtraInterfaces()).thenReturn(new Class[]{});

        assertEquals(null, returnsDeepStubs.answer(invocation3));

    }


    @Test
    public void testGetMock() throws Throwable {
        MockUtil mockUtil = mock(MockUtil.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        when(handler.getInvocationContainer()).thenReturn(new InvocationContainerImpl());

        Answer<Object> answer = mock(Answer.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(null);
        when(invocation.getMethod()).thenReturn(null);

        MockitoCore mockitoCore = mock(MockitoCore.class);
        when(mockitoCore.isTypeMockable(null)).thenReturn(false);
        when(mockitoCore.isTypeMockable(String.class)).thenReturn(true);

        ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);
        when(delegate.returnValueFor(null)).thenReturn(null);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        returnsDeepStubs.mockitoCore = mockitoCore;
        returnsDeepStubs.delegate = delegate;

        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(returnTypeGenericMetadata.resolveGenericReturnType(null)).thenReturn(null);

        handler.getInvocationContainer().setInvocationForStubbing(null);

        assertNotNull(returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata));

        assertTrue(handler.getInvocationContainer().getStubbedInvocations().contains(null));

    }


    @Test
    public void testCreateNewDeepStubMock() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(null);

        MockitoCore mockitoCore = mock(MockitoCore.class);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        returnsDeepStubs.mockitoCore = mockitoCore;

        assertNotNull(returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata));

    }


    @Test
    public void testWithSettingsUsing() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[]{});
        when(returnTypeGenericMetadata.rawType()).thenReturn(null);

        assertNotNull(new ReturnsDeepStubs().withSettingsUsing(returnTypeGenericMetadata));

    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(null);

        ReturnsDeepStubs rd = new ReturnsDeepStubs();
        assertNotNull(rd.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata));


    }


    @Test
    public void testRecordDeepStubMock() throws Throwable {
        InvocationContainerImpl iv = new InvocationContainerImpl();

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        iv.addAnswer(null, false);


        assertNotNull(returnsDeepStubs.recordDeepStubMock(null, iv));

    }

   

    @Test
    public void testActualParameterizedType() {
        MockUtil mockUtil = mock(MockUtil.class);
        CreationSettings mockSettings = mock(CreationSettings.class);

        when(mockUtil.getMockHandler(null).getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(null);

        assertNotNull(new ReturnsDeepStubs().actualParameterizedType(null));

    }


}