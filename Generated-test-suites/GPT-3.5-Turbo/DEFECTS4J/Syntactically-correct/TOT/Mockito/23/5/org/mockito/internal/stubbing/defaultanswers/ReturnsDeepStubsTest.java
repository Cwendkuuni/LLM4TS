import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

  private ReturnsDeepStubs returnsDeepStubs;

  @Before
  public void setUp() {
    returnsDeepStubs = new ReturnsDeepStubs();
  }

  @After
  public void tearDown() {
    returnsDeepStubs = null;
  }

  @Test
  public void testAnswer_invocationMatches_returnsAnswer() throws Throwable {
    // Arrange
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMock()).thenReturn("mock");
    when(invocation.getMethod()).thenReturn(getClass().getMethod("testAnswer_invocationMatches_returnsAnswer"));
    GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
    when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);
    MockitoCore mockitoCore = mock(MockitoCore.class);
    when(mockitoCore.isTypeMockable(String.class)).thenReturn(true);
    when(mockitoCore.mock(String.class, withSettingsUsing(returnTypeGenericMetadata))).thenReturn("mock");
    returnsDeepStubs.mockitoCore = mockitoCore;

    // Act
    Object result = returnsDeepStubs.answer(invocation);

    // Assert
    Assert.assertEquals("mock", result);
    verify(mockitoCore).mock(String.class, withSettingsUsing(returnTypeGenericMetadata));
  }

  @Test
  public void testAnswer_invocationDoesNotMatch_returnsDelegateReturnValue() throws Throwable {
    // Arrange
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMock()).thenReturn("mock");
    when(invocation.getMethod()).thenReturn(getClass().getMethod("testAnswer_invocationDoesNotMatch_returnsDelegateReturnValue"));
    GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
    when(returnTypeGenericMetadata.rawType()).thenReturn(Integer.class);
    MockitoCore mockitoCore = mock(MockitoCore.class);
    when(mockitoCore.isTypeMockable(Integer.class)).thenReturn(false);
    DelegateReturnValue delegate = mock(DelegateReturnValue.class);
    when(delegate.returnValueFor(Integer.class)).thenReturn(10);
    returnsDeepStubs.delegate = delegate;
    returnsDeepStubs.mockitoCore = mockitoCore;

    // Act
    Object result = returnsDeepStubs.answer(invocation);

    // Assert
    Assert.assertEquals(10, result);
    verify(delegate).returnValueFor(Integer.class);
  }

  @Test
  public void testGetMock_invocationMatches_returnsAnswer() throws Throwable {
    // Arrange
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMock()).thenReturn("mock");
    MockUtil mockUtil = mock(MockUtil.class);
    InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
    InvocationContainerImpl container = mock(InvocationContainerImpl.class);
    when(handler.getInvocationContainer()).thenReturn(container);
    when(mockUtil.getMockHandler("mock")).thenReturn(handler);
    StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
    when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
    when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);
    returnsDeepStubs.mockUtil = mockUtil;

    // Act
    Object result = returnsDeepStubs.getMock(invocation, null);

    // Assert
    Assert.assertEquals(stubbedInvocationMatcher.answer(invocation), result);
  }

  @Test
  public void testGetMock_invocationDoesNotMatch_returnsRecordDeepStubMock() throws Throwable {
    // Arrange
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMock()).thenReturn("mock");
    MockUtil mockUtil = mock(MockUtil.class);
    InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
    InvocationContainerImpl container = mock(InvocationContainerImpl.class);
    when(handler.getInvocationContainer()).thenReturn(container);
    when(mockUtil.getMockHandler("mock")).thenReturn(handler);
    when(container.getStubbedInvocations()).thenReturn(Collections.emptyList());
    returnsDeepStubs.mockUtil = mockUtil;

    // Act
    Object result = returnsDeepStubs.getMock(invocation, null);

    // Assert
    Assert.assertNotNull(result);
  }

  @Test
  public void testCreateNewDeepStubMock_rawExtraInterfacesPresent_returnsMockWithExtraInterfaces() {
    // Arrange
    GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
    when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);
    when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[] { Serializable.class });
    MockitoCore mockitoCore = mock(MockitoCore.class);
    when(mockitoCore.mock(String.class, withSettingsUsing(returnTypeGenericMetadata))).thenReturn("mock");
    returnsDeepStubs.mockitoCore = mockitoCore;

    // Act
    Object result = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);

    // Assert
    Assert.assertNotNull(result);
    verify(mockitoCore).mock(String.class, withSettingsUsing(returnTypeGenericMetadata));
  }

  @Test
  public void testCreateNewDeepStubMock_rawExtraInterfacesNotPresent_returnsMockWithoutExtraInterfaces() {
    // Arrange
    GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
    when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);
    when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[0]);
    MockitoCore mockitoCore = mock(MockitoCore.class);
    when(mockitoCore.mock(String.class, withSettingsUsing(returnTypeGenericMetadata))).thenReturn("mock");
    returnsDeepStubs.mockitoCore = mockitoCore;

    // Act
    Object result = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);

    // Assert
    Assert.assertNotNull(result);
    verify(mockitoCore).mock(String.class, withSettingsUsing(returnTypeGenericMetadata));
  }

  @Test
  public void testWithSettingsUsing_rawExtraInterfacesPresent_returnsMockSettingsWithExtraInterfaces() {
    // Arrange
    GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
    when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[] { Serializable.class });
    MockSettings mockSettings = mock(MockSettings.class);
    MockSettings mockSettingsExtraInterfaces = mock(MockSettings.class);
    when(mockSettings.extraInterfaces(returnTypeGenericMetadata.rawExtraInterfaces())).thenReturn(mockSettingsExtraInterfaces);
    when(mockSettings.defaultAnswer(returnsDeepStubsAnswerUsing(returnTypeGenericMetadata))).thenReturn(mockSettings);
    when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);

    // Act
    MockSettings result = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata, mockSettings);

    // Assert
    Assert.assertEquals(mockSettingsExtraInterfaces, result);
    verify(mockSettingsExtraInterfaces).extraInterfaces(returnTypeGenericMetadata.rawExtraInterfaces());
    verify(mockSettingsExtraInterfaces).defaultAnswer(returnsDeepStubsAnswerUsing(returnTypeGenericMetadata));
  }

  @Test
  public void testWithSettingsUsing_rawExtraInterfacesNotPresent_returnsMockSettingsWithoutExtraInterfaces() {
    // Arrange
    GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
    when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[0]);
    MockSettings mockSettings = mock(MockSettings.class);
    when(mockSettings.defaultAnswer(returnsDeepStubsAnswerUsing(returnTypeGenericMetadata))).thenReturn(mockSettings);
    when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);

    // Act
    MockSettings result = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata, mockSettings);

    // Assert
    Assert.assertEquals(mockSettings, result);
    verify(mockSettings).defaultAnswer(returnsDeepStubsAnswerUsing(returnTypeGenericMetadata));
  }

  @Test
  public void testReturnsDeepStubsAnswerUsing() {
    // Arrange
    GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
    ReturnsDeepStubs expectedReturnValue = new ReturnsDeepStubs() {
      @Override
      protected GenericMetadataSupport actualParameterizedType(Object mock) {
        return returnTypeGenericMetadata;
      }
    };

    // Act
    ReturnsDeepStubs result = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);

    // Assert
    Assert.assertEquals(expectedReturnValue.getClass(), result.getClass());
    Assert.assertEquals(expectedReturnValue.actualParameterizedType(null), result.actualParameterizedType(null));
  }

  @Test
  public void testRecordDeepStubMock() throws Throwable {
    // Arrange
    Object mock = mock(Object.class);
    InvocationContainerImpl container = mock(InvocationContainerImpl.class);
    returnsDeepStubs.recordDeepStubMock(mock, container);
    verify(container).addAnswer(any(Answer.class), eq(false));

    // Act
    Object result = returnsDeepStubs.recordDeepStubMock(mock, container);

    // Assert
    Assert.assertEquals(mock, result);
  }

  @Test
  public void testActualParameterizedType() {
    // Arrange
    Object mock = mock(Object.class);
    CreationSettings mockSettings = mock(CreationSettings.class);
    MockUtil mockUtil = mock(MockUtil.class);
    when(mockUtil.getMockHandler(mock)).thenReturn(mockSettings);
    when(mockSettings.getTypeToMock()).thenReturn(String.class);
    ReturnsDeepStubs expectedReturnValue = new ReturnsDeepStubs() {
      @Override
      protected CreationSettings getMockHandler(Object mock) {
        return mockUtil.getMockHandler(mock);
      }
    };

    // Act
    GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

    // Assert
    Assert.assertEquals(expectedReturnValue.actualParameterizedType(mock), result);
  }
}