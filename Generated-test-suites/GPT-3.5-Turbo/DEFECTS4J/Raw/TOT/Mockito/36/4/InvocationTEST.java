import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

public class InvocationTest {

  private Invocation invocation;

  @Before
  public void setUp() {
    MockitoMethod mockMethod = mock(MockitoMethod.class);
    Object[] args = new Object[] {"arg1", 2};
    RealMethod realMethod = mock(RealMethod.class);
    invocation = new Invocation(mock(Object.class), mockMethod, args, 1, realMethod);
  }

  @Test
  public void testGetMock() {
    assertNotNull(invocation.getMock());
  }

  @Test
  public void testGetMethod() {
    assertNotNull(invocation.getMethod());
  }

  @Test
  public void testGetArguments() {
    assertNotNull(invocation.getArguments());
    assertEquals(2, invocation.getArguments().length);
  }

  @Test
  public void testIsVerified() {
    assertFalse(invocation.isVerified());
  }

  @Test
  public void testGetSequenceNumber() {
    assertNotNull(invocation.getSequenceNumber());
    assertEquals(Integer.valueOf(1), invocation.getSequenceNumber());
  }

  @Test
  public void testIsVerifiedInOrder() {
    assertFalse(invocation.isVerifiedInOrder());
  }

  @Test
  public void testEquals() {
    Invocation other = new Invocation(mock(Object.class), mock(MockitoMethod.class), new Object[] {"arg1", 2}, 1, mock(RealMethod.class));
    assertTrue(invocation.equals(other));
  }

  @Test
  public void testHashCode() {
    assertThrows(RuntimeException.class, () -> invocation.hashCode());
  }

  @Test
  public void testToString() {
    assertNotNull(invocation.toString());
  }

  @Test
  public void testArgumentsToMatchers() {
    assertNotNull(invocation.argumentsToMatchers());
    assertEquals(2, invocation.argumentsToMatchers().size());
  }

  @Test
  public void testIsToString() {
    InvocationOnMock invocationOnMock = mock(InvocationOnMock.class);
    ObjectMethodsGuru objectMethodsGuru = mock(ObjectMethodsGuru.class);
    when(objectMethodsGuru.isToString(any())).thenReturn(true);
    assertFalse(invocation.isToString(invocationOnMock));
  }

  @Test
  public void testIsValidException() {
    Throwable throwable = mock(Throwable.class);
    MockitoMethod mockMethod = mock(MockitoMethod.class);
    when(mockMethod.getExceptionTypes()).thenReturn(new Class[]{Exception.class});
    Invocation invocation = new Invocation(mock(Object.class), mockMethod, new Object[]{}, 1, mock(RealMethod.class));
    assertTrue(invocation.isValidException(throwable));
  }

  @Test
  public void testIsValidReturnType() {
    MockitoMethod mockMethod = mock(MockitoMethod.class);
    when(mockMethod.getReturnType()).thenReturn(String.class);
    Invocation invocation = new Invocation(mock(Object.class), mockMethod, new Object[]{}, 1, mock(RealMethod.class));
    assertTrue(invocation.isValidReturnType(String.class));
  }

  @Test
  public void testIsVoid() {
    MockitoMethod mockMethod = mock(MockitoMethod.class);
    when(mockMethod.getReturnType()).thenReturn(Void.TYPE);
    Invocation invocation = new Invocation(mock(Object.class), mockMethod, new Object[]{}, 1, mock(RealMethod.class));
    assertTrue(invocation.isVoid());
  }

  @Test
  public void testPrintMethodReturnType() {
    MockitoMethod mockMethod = mock(MockitoMethod.class);
    when(mockMethod.getReturnType()).thenReturn(String.class);
    Invocation invocation = new Invocation(mock(Object.class), mockMethod, new Object[]{}, 1, mock(RealMethod.class));
    assertEquals("String", invocation.printMethodReturnType());
  }

  @Test
  public void testGetMethodName() {
    MockitoMethod mockMethod = mock(MockitoMethod.class);
    when(mockMethod.getName()).thenReturn("method");
    Invocation invocation = new Invocation(mock(Object.class), mockMethod, new Object[]{}, 1, mock(RealMethod.class));
    assertEquals("method", invocation.getMethodName());
  }

  @Test
  public void testReturnsPrimitive() {
    MockitoMethod mockMethod = mock(MockitoMethod.class);
    when(mockMethod.getReturnType()).thenReturn(int.class);
    Invocation invocation = new Invocation(mock(Object.class), mockMethod, new Object[]{}, 1, mock(RealMethod.class));
    assertTrue(invocation.returnsPrimitive());
  }

  @Test
  public void testGetLocation() {
    assertNotNull(invocation.getLocation());
  }

  @Test
  public void testGetArgumentsCount() {
    assertEquals(2, invocation.getArgumentsCount());
  }

  @Test
  public void testGetRawArguments() {
    assertNotNull(invocation.getRawArguments());
    assertEquals(2, invocation.getRawArguments().length);
  }

  @Test
  public void testCallRealMethod() throws Throwable {
    assertNotNull(invocation.callRealMethod());
  }

  @Test
  public void testToStringWithPrintSettings() {
    PrintSettings printSettings = mock(PrintSettings.class);
    assertNotNull(invocation.toString(printSettings));
  }

  @Test
  public void testMarkVerified() {
    invocation.markVerified();
    assertTrue(invocation.isVerified());
  }

  @Test
  public void testMarkVerifiedInOrder() {
    invocation.markVerifiedInOrder();
    assertTrue(invocation.isVerified());
    assertTrue(invocation.isVerifiedInOrder());
  }

}