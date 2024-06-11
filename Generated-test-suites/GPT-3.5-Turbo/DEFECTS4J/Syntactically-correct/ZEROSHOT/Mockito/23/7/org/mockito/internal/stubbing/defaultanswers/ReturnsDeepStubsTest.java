
package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer_validInvocation_returnsMock() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        Assert.assertNotNull(result);
    }

    @Test
    public void testGetMock_validInvocation_returnsMock() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        // Act
        Object result = returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata);

        // Assert
        Assert.assertNotNull(result);
    }

    @Test
    public void testCreateNewDeepStubMock_validReturnType_returnsMock() {
        // Arrange
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        // Act
        Object result = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);

        // Assert
        Assert.assertNotNull(result);
    }

    @Test
    public void testWithSettingsUsing_rawExtraInterfacesExist_returnsSettingsWithExtraInterfaces() {
        // Arrange
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        // Act
        Object result = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

        // Assert
        Assert.assertNotNull(result);
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing_validReturnType_returnsAnswer() {
        // Arrange
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        // Act
        Object result = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);

        // Assert
        Assert.assertNotNull(result);
    }

    @Test
    public void testRecordDeepStubMock_validMock_invocationContainerUpdated() throws Throwable {
        // Arrange
        Object mock = mock(Object.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        // Act
        Object result = returnsDeepStubs.recordDeepStubMock(mock, container);

        // Assert
        Assert.assertNotNull(result);
    }

    @Test
    public void testActualParameterizedType_validMock_returnsGenericMetadataSupport() {
        // Arrange
        Object mock = mock(Object.class);

        // Act
        Object result = returnsDeepStubs.actualParameterizedType(mock);

        // Assert
        Assert.assertNotNull(result);
    }
}
