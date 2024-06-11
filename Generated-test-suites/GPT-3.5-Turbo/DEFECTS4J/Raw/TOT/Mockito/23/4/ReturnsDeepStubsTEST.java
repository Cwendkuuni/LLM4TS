import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {

	@Test
	public void testAnswer() {
		ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
		
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		Object result = null;
		try {
			result = returnsDeepStubs.answer(invocation);
		} catch (Throwable e) {
			fail("Should not throw exception");
		}
		
		assertNull(result);
		
	}
	
	@Test
	public void testGetMock() {
		ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
		
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
		
		Object result = null;
		try {
			result = returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata);
		} catch (Throwable e) {
			fail("Should not throw exception");
		}
		
		assertNull(result);
	}
	
	@Test
	public void testCreateNewDeepStubMock() {
		ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
		
		GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
		
		Object result = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);
		
		assertNotNull(result);
	}
	
	@Test
	public void testWithSettingsUsing() {
		ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
		
		GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
		
		MockSettings mockSettings = mock(MockSettings.class);
		when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[]{Serializable.class});
		when(mockSettings.extraInterfaces(returnTypeGenericMetadata.rawExtraInterfaces())).thenReturn(mockSettings);
		when(mockSettings.defaultAnswer(returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata))).thenReturn(mockSettings);
		
		when(mockSettings.extraInterfaces(returnTypeGenericMetadata.rawExtraInterfaces())).thenReturn(mockSettings);
		
		Object result = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);
		
		assertNotNull(result);
	}
	
	@Test
	public void testReturnsDeepStubsAnswerUsing() {
		ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
		GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
		
		ReturnsDeepStubs result = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
		
		assertNotNull(result);
	}

	@Test
	public void testRecordDeepStubMock() {
		ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
		InvocationContainerImpl container = mock(InvocationContainerImpl.class);
		Object mock = mock(Object.class);
		
		Object result = null;
		try {
			result = returnsDeepStubs.recordDeepStubMock(mock, container);
		} catch (Throwable e) {
			fail("Should not throw exception");
		}
		
		assertNotNull(result);
	}
	
	@Test
	public void testActualParameterizedType() {
		ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
		Object mock = mock(Object.class);
		CreationSettings mockSettings = mock(CreationSettings.class);
		MockUtil mockUtil = mock(MockUtil.class);
		
		when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);
		when(mockHandler.getMockSettings()).thenReturn(mockSettings);
		when(mockSettings.getTypeToMock()).thenReturn(Object.class);
		
		GenericMetadataSupport result = null;
		try {
			result = returnsDeepStubs.actualParameterizedType(mock);
		} catch (Throwable e) {
			fail("Should not throw exception");
		}
		
		assertNotNull(result);
		
	}
}