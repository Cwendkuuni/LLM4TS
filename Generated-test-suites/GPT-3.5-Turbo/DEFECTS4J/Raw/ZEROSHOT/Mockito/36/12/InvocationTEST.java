import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.internal.matchers.*;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.util.*;
import java.util.*;
import org.hamcrest.Matcher;
import org.mockito.exceptions.PrintableInvocation;

@SuppressWarnings("unchecked")
public class InvocationTest {
  
  private Object mock;
  private MockitoMethod method;
  private Object[] args;
  private int sequenceNumber;
  private RealMethod realMethod;
  private Invocation invocation;
  
  @Before
  public void setUp() {
    mock = Mockito.mock(Object.class);
    method = Mockito.mock(MockitoMethod.class);
    args = new Object[]{};
    sequenceNumber = 1;
    realMethod = Mockito.mock(RealMethod.class);
    invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
  }
  
  @Test
  public void testGetMock() {
    Object result = invocation.getMock();
    assertEquals(mock, result);
  }
  
  @Test
  public void testGetMethod() {
    MockitoMethod result = invocation.getMethod();
    assertEquals(method, result);
  }
  
  @Test
  public void testGetArguments() {
    Object[] result = invocation.getArguments();
    assertArrayEquals(args, result);
  }
  
  @Test
  public void testIsVerified() {
    boolean result = invocation.isVerified();
    assertFalse(result);
  }
  
  @Test
  public void testGetSequenceNumber() {
    Integer result = invocation.getSequenceNumber();
    assertEquals(Integer.valueOf(sequenceNumber), result);
  }
  
  @Test
  public void testIsVerifiedInOrder() {
    boolean result = invocation.isVerifiedInOrder();
    assertFalse(result);
  }
  
  @Test
  public void testEquals() {
    Invocation other = new Invocation(mock, method, args, sequenceNumber, realMethod);
    boolean result = invocation.equals(other);
    assertTrue(result);
  }
  
  @Test
  public void testEquals_Null() {
    boolean result = invocation.equals(null);
    assertFalse(result);
  }
  
  @Test
  public void testEquals_DifferentClass() {
    boolean result = invocation.equals("test");
    assertFalse(result);
  }
  
  @Test
  public void testHashCode() {
    try {
      invocation.hashCode();
    } catch (RuntimeException e) {
      assertEquals("hashCode() is not implemented", e.getMessage());
    }
  }
  
  @Test
  public void testToString() {
    String result = invocation.toString();
    assertEquals("", result);
  }
  
  @Test
  public void testToString_PrintSettings() {
    PrintSettings printSettings = new PrintSettings();
    String result = invocation.toString(printSettings);
    assertEquals("", result);
  }
  
  @Test
  public void testToString_ListPrintSettings() {
    List<Matcher> matchers = new ArrayList<Matcher>();
    PrintSettings printSettings = new PrintSettings();
    String result = invocation.toString(matchers, printSettings);
    assertEquals("", result);
  }
  
  @Test
  public void testIsValidException() {
    Throwable throwable = new Throwable();
    boolean result = invocation.isValidException(throwable);
    assertFalse(result);
  }
  
  @Test
  public void testIsValidReturnType() {
    Class clazz = Object.class;
    boolean result = invocation.isValidReturnType(clazz);
    assertTrue(result);
  }
  
  @Test
  public void testIsVoid() {
    boolean result = invocation.isVoid();
    assertFalse(result);
  }
  
  @Test
  public void testPrintMethodReturnType() {
    String result = invocation.printMethodReturnType();
    assertEquals("", result);
  }
  
  @Test
  public void testGetMethodName() {
    String result = invocation.getMethodName();
    assertEquals("", result);
  }
  
  @Test
  public void testReturnsPrimitive() {
    boolean result = invocation.returnsPrimitive();
    assertFalse(result);
  }
  
  @Test
  public void testGetLocation() {
    Location result = invocation.getLocation();
    assertNotNull(result);
  }
  
  @Test
  public void testGetArgumentsCount() {
    int result = invocation.getArgumentsCount();
    assertEquals(args.length, result);
  }
  
  @Test
  public void testGetRawArguments() {
    Object[] result = invocation.getRawArguments();
    assertArrayEquals(args, result);
  }
  
  @Test
  public void testCallRealMethod() throws Throwable {
    Object result = invocation.callRealMethod();
    assertNull(result);
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