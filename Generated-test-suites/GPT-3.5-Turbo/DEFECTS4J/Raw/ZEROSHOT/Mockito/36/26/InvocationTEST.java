import org.junit.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class InvocationTest {
  
  private Object mock;
  private MockitoMethod method;
  private Object[] args;
  private int sequenceNumber;
  private RealMethod realMethod;
  private Invocation invocation;

  @Before
  public void setUp() {
    mock = mock(Object.class);
    method = mock(MockitoMethod.class);
    args = new Object[]{};
    sequenceNumber = 1;
    realMethod = mock(RealMethod.class);
    invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
  }

  @Test
  public void testGetMock() {
    assertEquals(mock, invocation.getMock());
  }

  @Test
  public void testGetMethod() {
    assertEquals(method, invocation.getMethod());
  }

  @Test
  public void testGetArguments() {
    assertEquals(args, invocation.getArguments());
  }

  @Test
  public void testIsVerified() {
    assertFalse(invocation.isVerified());
  }

  @Test
  public void testGetSequenceNumber() {
    assertEquals(Integer.valueOf(sequenceNumber), invocation.getSequenceNumber());
  }

  @Test
  public void testIsVerifiedInOrder() {
    assertFalse(invocation.isVerifiedInOrder());
  }

  @Test
  public void testEquals() {
    Invocation other = new Invocation(mock, method, args, sequenceNumber, realMethod);
    assertTrue(invocation.equals(other));
  }

  @Test
  public void testHashCode() {
    try {
      invocation.hashCode();
      fail("Should throw RuntimeException");
    } catch (RuntimeException e) {
      assertEquals("hashCode() is not implemented", e.getMessage());
    }
  }

  @Test
  public void testToString() {
    String expected = mock + "." + method.getName();
    assertEquals(expected, invocation.toString());
  }

  @Test
  public void testToStringWithPrintSettings() {
    PrintSettings printSettings = new PrintSettings();
    String expected = mock + "." + method.getName();
    assertEquals(expected, invocation.toString(printSettings));
  }

  @Test
  public void testIsToString() {
    InvocationOnMock invocationOnMock = mock(InvocationOnMock.class);
    MockitoMethod method = mock(MockitoMethod.class);
    when(invocationOnMock.getMethod()).thenReturn(method);
    when(method.getName()).thenReturn("toString");
    assertTrue(Invocation.isToString(invocationOnMock));
  }

  @Test
  public void testIsValidException() {
    Throwable throwable = mock(Throwable.class);
    MockitoMethod method = mock(MockitoMethod.class);
    Class<?>[] exceptions = new Class<?>[] { Exception.class };
    when(method.getExceptionTypes()).thenReturn(exceptions);
    invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
    assertTrue(invocation.isValidException(throwable));
  }

  @Test
  public void testIsValidReturnType() {
    Class<?> clazz = String.class;
    MockitoMethod method = mock(MockitoMethod.class);
    when(method.getReturnType()).thenReturn(Object.class);
    invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
    assertFalse(invocation.isValidReturnType(clazz));
  }

  @Test
  public void testIsVoid() {
    MockitoMethod method = mock(MockitoMethod.class);
    when(method.getReturnType()).thenReturn(Void.TYPE);
    invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
    assertTrue(invocation.isVoid());
  }

  @Test
  public void testPrintMethodReturnType() {
    MockitoMethod method = mock(MockitoMethod.class);
    when(method.getReturnType()).thenReturn(Integer.TYPE);
    invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
    assertEquals("int", invocation.printMethodReturnType());
  }

  @Test
  public void testGetMethodName() {
    MockitoMethod method = mock(MockitoMethod.class);
    when(method.getName()).thenReturn("testMethod");
    invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
    assertEquals("testMethod", invocation.getMethodName());
  }

  @Test
  public void testReturnsPrimitive() {
    MockitoMethod method = mock(MockitoMethod.class);
    when(method.getReturnType()).thenReturn(Integer.TYPE);
    invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
    assertTrue(invocation.returnsPrimitive());
  }

  @Test
  public void testGetLocation() {
    assertNotNull(invocation.getLocation());
  }

  @Test
  public void testGetArgumentsCount() {
    assertEquals(0, invocation.getArgumentsCount());
  }

  @Test
  public void testGetRawArguments() {
    assertEquals(args, invocation.getRawArguments());
  }

  @Test
  public void testCallRealMethod() {
    try {
      invocation.callRealMethod();
    } catch (Throwable e) {
      fail("Should not throw exception");
    }
  }

  @Test
  public void testToStringWithArguments() {
    PrintSettings printSettings = new PrintSettings();
    List<Matcher> matchers = new ArrayList<>();
    MatchersPrinter matchersPrinter = new MatchersPrinter();
    String method = mock + "." + invocation.getMethod().getName();
    String expected = method + matchersPrinter.getArgumentsBlock(matchers, printSettings);
    assertEquals(expected, invocation.toString(matchers, printSettings));
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