import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class InvocationTest {
  
  private Invocation invocation;
  
  @Before
  public void setUp() {
    // Create a mock object
    Object mock = Mockito.mock(Object.class);
    // Create a MockitoMethod object
    MockitoMethod method = new MockitoMethod("testMethod", null, null);
    // Create an array of arguments
    Object[] args = {"arg1", "arg2"};
    // Create a sequence number
    int sequenceNumber = 1;
    // Create a RealMethod object
    RealMethod realMethod = new RealMethod();
    // Create an instance of Invocation
    invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
  }
  
  @Test
  public void testGetMock() {
    Object expected = Mockito.mock(Object.class);
    Object actual = invocation.getMock();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetMethod() {
    MockitoMethod expected = new MockitoMethod("testMethod", null, null);
    MockitoMethod actual = invocation.getMethod();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetArguments() {
    Object[] expected = {"arg1", "arg2"};
    Object[] actual = invocation.getArguments();
    assertArrayEquals(expected, actual);
  }
  
  @Test
  public void testIsVerified() {
    boolean expected = false;
    boolean actual = invocation.isVerified();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetSequenceNumber() {
    Integer expected = 1;
    Integer actual = invocation.getSequenceNumber();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testIsVerifiedInOrder() {
    boolean expected = false;
    boolean actual = invocation.isVerifiedInOrder();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testEquals() {
    Object obj = new Invocation(Mockito.mock(Object.class), new MockitoMethod("testMethod", null, null), new Object[]{"arg1", "arg2"}, 1, new RealMethod());
    boolean expected = true;
    boolean actual = invocation.equals(obj);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testHashCode() {
    try {
      invocation.hashCode();
      fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      // Exception expected
    }
  }
  
  @Test
  public void testToString() {
    String expected = "Mock for class java.lang.Object, hashCode: " + invocation.getMock().hashCode() + "." 
    					+ invocation.getMethod().getName() + "(\"arg1\", \"arg2\")";
    String actual = invocation.toString();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testToStringWithPrintSettings() {
    String expected = "Mock for class java.lang.Object, hashCode: " + invocation.getMock().hashCode() + "." 
                    	+ invocation.getMethod().getName() + "(\"arg1\", \"arg2\")";
    String actual = invocation.toString(new PrintSettings());
    assertEquals(expected, actual);
  }
  
  @Test
  public void testIsToString() {
    InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
    MockitoMethod method = new MockitoMethod("toString", null, null);
    Mockito.when(invocation.getMethod()).thenReturn(method);
    boolean expected = true;
    boolean actual = Invocation.isToString(invocation);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testIsValidException() {
    Throwable throwable = new Throwable();
    boolean expected = false;
    boolean actual = invocation.isValidException(throwable);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testIsValidReturnType() {
    Class clazz = Object.class;
    boolean expected = true;
    boolean actual = invocation.isValidReturnType(clazz);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testIsVoid() {
    boolean expected = false;
    boolean actual = invocation.isVoid();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testPrintMethodReturnType() {
    String expected = "void";
    String actual = invocation.printMethodReturnType();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetMethodName() {
    String expected = "testMethod";
    String actual = invocation.getMethodName();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testReturnsPrimitive() {
    boolean expected = false;
    boolean actual = invocation.returnsPrimitive();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetLocation() {
    Location expected = new Location();
    Location actual = invocation.getLocation();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetArgumentsCount() {
    int expected = 2;
    int actual = invocation.getArgumentsCount();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetRawArguments() {
    Object[] expected = {"arg1", "arg2"};
    Object[] actual = invocation.getRawArguments();
    assertArrayEquals(expected, actual);
  }
  
  @Test
  public void testCallRealMethod() {
    try {
      invocation.callRealMethod();
      fail("Expected Throwable");
    } catch (Throwable e) {
      // Exception expected
    }
  }
  
  @Test
  public void testToStringWithPrintSettingsMultiline() {
    PrintSettings printSettings = Mockito.mock(PrintSettings.class);
    Mockito.when(printSettings.isMultiline()).thenReturn(true);
    MatchersPrinter matchersPrinter = new MatchersPrinter();
    String expected = matchersPrinter.getArgumentsBlock(invocation.argumentsToMatchers(), printSettings);
    String actual = invocation.toString(printSettings);
    assertEquals(expected, actual);
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