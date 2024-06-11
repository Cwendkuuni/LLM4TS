import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.Invocation;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doCallRealMethod;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullIterable;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.arrayContaining;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Assert;
import org.junit.runner.JUnitCore;

@RunWith(JUnitCore.class)
public class InvocationTest {

 private Object mock;
 private Invocation invocation;

 @Before
 public void setUp() {
 mock = mock(String.class);
 invocation = new Invocation(mock, new MockitoMethod("testMethod", String[].class, new Class[0]), new Object[0], 0, null);
 }

 @Test
 public void testGetMock() {
 assertThat(invocation.getMock(), is(mock));
 }

 @Test
 public void testGetMethod() {
 assertThat(invocation.getMethod(), is(new MockitoMethod("testMethod", String[].class, new Class[0])));
 }

 @Test
 public void testGetArguments() {
 Object[] expectedArguments = new Object[]{"arg1", "arg2"};
 doAnswer(i -> {
 invocation.setArguments(expectedArguments);
 return null;
 }).when(mock).testMethod(invocation.getArguments());

 Object[] actualArguments = invocation.getArguments();
 assertThat(actualArguments, is(equalTo(expectedArguments)));
 }

 @Test
 public void testEquals() {
 Invocation invocation1 = new Invocation(mock, new MockitoMethod("testMethod", String[].class, new Class[0]), new Object[]{"arg1", "arg2"}, 0, null);
 Invocation invocation2 = new Invocation(mock, new MockitoMethod("testMethod", String[].class, new Class[0]), new Object[]{"arg1", "arg2"}, 0, null);

 assertThat(invocation.equals(invocation1), is(true));
 }

 @Test(expected = RuntimeException.class)
 public void testHashCode() {
 invocation.hashCode();
 }

 @Test
 public void testToString() {
 String expectedToString = "testMethod(arg1, arg2)";
 String actualToString = invocation.toString();

 assertThat(actualToString, is(equalTo(expectedToString)));
 }

 @Test
 public void testIsVerified() {
 invocation.markVerified();

 assertThat(invocation.isVerified(), is(true));
 }

 @Test
 public void testIsVerifiedInOrder() {
 invocation.markVerifiedInOrder();

 assertThat(invocation.isVerifiedInOrder(), is(true));
 }

 @Test
 public void testGetSequenceNumber() {
 assertThat(invocation.getSequenceNumber(), is(0));
 }

 @Test
 public void testIsVerifiedInOrderIsFalse() {
 invocation.markVerified();

 assertThat(invocation.isVerifiedInOrder(), is(false));
 }

 @Test
 public void testIsToString() {
 assertThat(Invocation.isToString(mock(InvocationOnMock.class)), is(true));
 }

 @Test
 public void testIsValidException() {
 try {
 throw new NullPointerException();
 } catch (NullPointerException e) {
 assertThat(invocation.isValidException(e), is(true));
 }
 }

 @Test
 public void testIsValidReturnType() {
 Class<String> clazz = String.class;

 assertThat(invocation.isValidReturnType(clazz), is(true));
 }

 @Test
 public void testIsVoid() {
 MockitoMethod mockitoMethod = new MockitoMethod("testVoidMethod", new Class[0]);
 Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 0, null);

 assertThat(invocation.isVoid(), is(true));
 }

 @Test
 public void testPrintMethodReturnType() {
 assertThat(invocation.printMethodReturnType(), is("String"));
 }

 @Test
 public void testGetMethodName() {
 assertThat(invocation.getMethodName(), is("testMethod"));
 }

 @Test
 public void testReturnsPrimitive() {
 MockitoMethod mockitoMethod = new MockitoMethod("testBooleanMethod", boolean.class, new Class[0]);
 Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 0, null);

 assertThat(invocation.returnsPrimitive(), is(false));
 }

 @Test
 public void testGetLocation() {
 Location expectedLocation = new Location();
 Invocation invocation = spy(new Invocation(mock, new MockitoMethod("testMethod", String[].class, new Class[0]), new Object[0], 0, null));
 doReturn(expectedLocation).when(invocation).getLocation();

 Location actualLocation = invocation.getLocation();

 assertThat(actualLocation, is(equalTo(expectedLocation)));
 }

 @Test
 public void testGetArgumentsCount() {
 assertThat(invocation.getArgumentsCount(), is(0));
 }

 @Test
 public void testCallRealMethod() throws Throwable {
 String expectedReturnValue = "expectedReturnValue";
 doReturn(expectedReturnValue).when(mock).toString();

 Object actualReturnValue = invocation.callRealMethod();

 assertThat(actualReturnValue, is(equalTo(expectedReturnValue)));
 }

 @Test(expected = MockitoException.class)
 public void testCallRealMethodWithInvalidException() throws Throwable {
 doThrow(new RuntimeException()).when(mock).testMethod(invocation.getArguments());

 try {
 invocation.callRealMethod();
 } catch (Throwable e) {
 // do nothing
 }

 verify(mock).testMethod(invocation.getArguments());
 }

 @Test(expected = MockitoException.class)
 public void testCallRealMethodWithInvalidReturnType() throws Throwable {
 doAnswer(i -> {
 throw new AssertionError("Invalid return type");
 }).when(mock).testMethod(invocation.getArguments());

 try {
 invocation.callRealMethod();
 } catch (Throwable e) {
 // do nothing
 }

 verify(mock).testMethod(invocation.getArguments());
 }

 @Test(expected = MockitoException.class)
 public void testCallRealMethodWithVoidMethod() throws Throwable {
 MockitoMethod mockitoMethod = new MockitoMethod("testVoidMethod", new Class[0]);
 Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 0, null);

 try {
 invocation.callRealMethod();
 } catch (Throwable e) {
 // do nothing
 }

 verifyNoMoreInteractions(mock);
 }

 @Test(expected = MockitoException.class)
 public void testCallRealMethodWithUnsupportedReturnType() throws Throwable {
 doAnswer(i -> {
 throw new UnsupportedOperationException("Unsupported return type");
 }).when(mock).testMethod(invocation.getArguments());

 try {
 invocation.callRealMethod();
 } catch (Throwable e) {
 // do nothing
 }

 verifyNoMoreInteractions(mock);
 }

 @Test(expected = MockitoException.class)
 public void testCallRealMethodWithNullReturnType() throws Throwable {
 doAnswer(i -> {
 throw new NullPointerException("Null return type");
 }).when(mock).testMethod(invocation.getArguments());

 try {
 invocation.callRealMethod();
 } catch (Throwable e) {
 // do nothing
 }

 verifyNoMoreInteractions(mock);
 }

 @Test
 public void testToStringWithArgumentsMatchers() {
 List<Matcher> matchers = new ArrayList<>();
 matchers.add(new ArgThat<Object>(new TypeSafeMatcher<Object>() {
 @Override
 public boolean matchesSafely(Object item) {
 return "arg1".equals(item);
 }

 @Override
 public void describeTo(Description description) {
 description.appendText("is arg1");
 }
 }));
 matchers.add(new ArgThat<Object>(new TypeSafeMatcher<Object>() {
 @Override
 public boolean matchesSafely(Object item) {
 return "arg2".equals(item);
 }

 @Override
 public void describeTo(Description description) {
 description.appendText("is arg2");
 }
 }));

 String expectedToString = "testMethod(arg1, arg2)";
 String actualToString = invocation.toString(matchers, new PrintSettings());

 assertThat(actualToString, is(equalTo(expectedToString)));
 }

 @Test
 public void testToStringWithPrintingFriendlyInvocation() {
 Invocation invocation = new Invocation(mock, new MockitoMethod("testToStringMethod", String[].class, new Class[0]), new Object[0], 0, null);
 PrintingFriendlyInvocation printingFriendlyInvocation = new PrintingFriendlyInvocation(invocation);
 String expectedToString = "testToStringMethod(\"[mock]\", []);";
 String actualToString = printingFriendlyInvocation.toString();

 assertThat(actualToString, is(equalTo(expectedToString)));
 }

 @Test
 public void testToStringWithPrintSettings() {
 List<Matcher> matchers = new ArrayList<>();
 matchers.add(new ArgThat<Object>(new TypeSafeMatcher<Object>() {
 @Override
 public boolean matchesSafely(Object item) {
 return "arg1".equals(item);
 }

 @Override
 public void describeTo(Description description) {
 description.appendText("is arg1");
 }
 }));
 matchers.add(new ArgThat<Object>(new TypeSafeMatcher<Object>() {
 @Override
 public boolean matchesSafely(Object item) {
 return "arg2".equals(item);
 }

 @Override
 public void describeTo(Description description) {
 description.appendText("is arg2");
 }
 }));
 PrintSettings printSettings = new PrintSettings();
 printSettings.setMultiline(true);

 String expectedToString = "testMethod(\n" +
 "    arg1,\n" +
 "    arg2\n" +
 ")";
 String actualToString = invocation.toString(matchers, printSettings);

 assertThat(actualToString, is(equalTo(expectedToString)));
 }
}