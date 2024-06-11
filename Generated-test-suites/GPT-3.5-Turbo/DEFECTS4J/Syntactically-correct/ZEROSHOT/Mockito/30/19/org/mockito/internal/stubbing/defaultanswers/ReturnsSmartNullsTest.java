import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

	private ReturnsSmartNulls returnsSmartNulls;

	@Before
	public void setup() {
		returnsSmartNulls = new ReturnsSmartNulls();
	}

	@Test
	public void testAnswer_withNotNullReturnValue_shouldReturnSameValue() throws Throwable {
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		Object expectedResult = new Object();
		
		Object actualResult = returnsSmartNulls.answer(invocation);
		
		assertEquals(expectedResult, actualResult);
	}

	@Test
	public void testAnswer_withNullReturnValue_shouldReturnNull() throws Throwable {
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		Object expectedResult = null;
		
		Object actualResult = returnsSmartNulls.answer(invocation);
		
		assertNull(actualResult);
	}
	
	@Test
	public void testAnswer_withMockableReturnType_shouldReturnMock() throws Throwable {
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		Class<?> returnType = Object.class;
		ClassImposterizer imposterizer = mock(ClassImposterizer.class);
		MethodInterceptor interceptor = mock(MethodInterceptor.class);
		
		ClassImposterizer.INSTANCE = imposterizer;
		
		Object actualResult = returnsSmartNulls.answer(invocation);
		
		assertNotNull(actualResult);
		assertEquals(invocation.getMethod().getReturnType(), actualResult.getClass());
	}
	
	@Test
	public void testAnswer_withNonMockableReturnType_shouldReturnNull() throws Throwable {
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		Class<?> returnType = String.class;
		ClassImposterizer imposterizer = mock(ClassImposterizer.class);
		MethodInterceptor interceptor = mock(MethodInterceptor.class);
		
		ClassImposterizer.INSTANCE = imposterizer;
		
		Object actualResult = returnsSmartNulls.answer(invocation);
		
		assertNull(actualResult);
	}

	@Test
	public void testThrowingInterceptor_intercept_withToStringMethod_shouldReturnFormattedString() throws Throwable {
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		Method method = String.class.getMethod("toString");
		Object[] args = null;
		MethodProxy proxy = mock(MethodProxy.class);
		Object expectedResult = "SmartNull returned by unstubbed toString method on mock";
		Location location = new Location();
		ObjectMethodsGuru objectMethodsGuru = mock(ObjectMethodsGuru.class);
		
		Reporter reporter = mock(Reporter.class);
		Location location = mock(Location.class);
		
		new Reporter().smartNullPointerException(location);
		
		Object actualResult = new ReturnsSmartNulls().new ThrowingInterceptor(invocation).intercept(null, method, args, proxy);
		
		assertEquals(expectedResult, actualResult);
	}
	
	@Test(expected = Throwable.class)
	public void testThrowingInterceptor_intercept_withNonToStringMethod_shouldThrowThrowable() throws Throwable {
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		Method method = Object.class.getMethod("equals", Object.class);
		Object[] args = null;
		MethodProxy proxy = mock(MethodProxy.class);
		Location location = new Location();
		ObjectMethodsGuru objectMethodsGuru = mock(ObjectMethodsGuru.class);
		
		Reporter reporter = mock(Reporter.class);
		Location location = mock(Location.class);
		
		new Reporter().smartNullPointerException(location);
		
		new ReturnsSmartNulls().new ThrowingInterceptor(invocation).intercept(null, method, args, proxy);
	}

}