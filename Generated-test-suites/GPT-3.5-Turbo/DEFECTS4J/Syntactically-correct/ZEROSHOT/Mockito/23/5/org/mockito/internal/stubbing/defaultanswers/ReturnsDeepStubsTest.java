import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsDeepStubsTest {

  private ReturnsDeepStubs returnsDeepStubs;

  @Before
  public void setup() {
    returnsDeepStubs = new ReturnsDeepStubs();
  }

  @Test
  public void testAnswer_invocationNotMocked_returnsDefaultValue() throws Throwable {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    Object result = returnsDeepStubs.answer(invocation);

    assertNull(result);
  }

  @Test
  public void testAnswer_isTypeMockable_returnsDefaultValue() throws Throwable {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMock()).thenReturn(1);
    Object result = returnsDeepStubs.answer(invocation);

    assertNotNull(result);
    assertEquals(Integer.class, result.getClass());
  }

  @Test
  public void testAnswer_invocationMatches_returnsStubbedValue() throws Throwable {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMock()).thenReturn(this);
    Object result = returnsDeepStubs.answer(invocation);

    assertNotNull(result);
    assertEquals(this, result);
  }

  @Test
  public void testGetMock_invocationNotMocked_returnsDefaultValue() throws Throwable {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    Object result = returnsDeepStubs.getMock(invocation, new GenericMetadataSupport());

    assertNull(result);
  }

  @Test
  public void testGetMock_invocationMatches_returnsStubbedValue() throws Throwable {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMock()).thenReturn(this);
    Object result = returnsDeepStubs.getMock(invocation, new GenericMetadataSupport());

    assertNotNull(result);
    assertEquals(this, result);
  }

  @Test
  public void testCreateNewDeepStubMock_returnsMock() throws Throwable {
    GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport();
    Object result = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);

    assertNotNull(result);
    assertEquals(Object.class, result.getClass());
  }

  @Test
  public void testWithSettingsUsing_noExtraInterfaces_returnsMockSettings() {
    GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport();
    MockSettings mockSettings = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

    assertNotNull(mockSettings);
  }

  @Test
  public void testWithSettingsUsing_withExtraInterfaces_returnsMockSettings() {
    GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport();
    returnTypeGenericMetadata.setRawExtraInterfaces(new Class[]{Serializable.class});
    MockSettings mockSettings = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

    assertNotNull(mockSettings);
    assertArrayEquals(new Class[]{Serializable.class}, mockSettings.getExtraInterfaces());
  }

  @Test
  public void testReturnsDeepStubsAnswerUsing_actualParameterizedType_returnsGenericMetadataSupport() {
    GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport();
    ReturnsDeepStubs result = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);

    assertNotNull(result);
    assertEquals(returnTypeGenericMetadata, result.actualParameterizedType(null));
  }

  @Test
  public void testRecordDeepStubMock_addsAnswerToContainer() throws Throwable {
    Object mock = new Object();
    InvocationContainerImpl container = mock(InvocationContainerImpl.class);
    Object result = returnsDeepStubs.recordDeepStubMock(mock, container);

    assertNotNull(result);
    assertEquals(mock, result);
    verify(container, times(1)).addAnswer(any(), eq(false));
  }

  @Test
  public void testActualParameterizedType_mockSettingsIsCreationSettings_returnsGenericMetadataSupport() {
    Object mock = mock(Object.class);
    CreationSettings mockSettings = mock(CreationSettings.class);
    when(mockSettings.getTypeToMock()).thenReturn(Object.class);
    when(((MockUtil)any())).thenReturn(new MockUtil());
    when(((MockUtil)any()).getMockHandler(mock)).thenReturn(mockSettings);

    GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

    assertNotNull(result);
  }
}